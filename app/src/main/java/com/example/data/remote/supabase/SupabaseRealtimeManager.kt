package com.example.data.remote.supabase

import android.util.Log
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import okhttp3.*
import org.json.JSONObject
import java.util.concurrent.TimeUnit
import java.util.concurrent.atomic.AtomicInteger

sealed class RealtimeEvent(val table: String) {
    data class ForumThreadChanged(val eventType: String, val threadId: String? = null) : RealtimeEvent("forum_threads")
    data class ForumReplyInserted(val threadId: String, val replyId: String) : RealtimeEvent("forum_replies")
    data class ChatMessageInserted(val conversationId: String, val messageId: String) : RealtimeEvent("chat_messages")
    data class FriendshipChanged(val requesterId: String, val addresseeId: String) : RealtimeEvent("friendships")
}

class SupabaseRealtimeManager(
    private val authService: SupabaseAuthService,
    private val scope: CoroutineScope
) {
    private val TAG = "SupabaseRealtime"
    private val lock = Any()
    private val refCount = AtomicInteger(0)
    private val activeConsumerTags = mutableListOf<String>()
    private var isAppInForeground = true

    private var webSocket: WebSocket? = null
    private val messageRefCounter = AtomicInteger(1)
    private var isConnected = false
    private var isConnecting = false
    private var reconnectJob: Job? = null
    private var heartbeatJob: Job? = null
    private var pollingJob: Job? = null

    private val _events = MutableSharedFlow<RealtimeEvent>(extraBufferCapacity = 64)
    val events: SharedFlow<RealtimeEvent> = _events.asSharedFlow()

    private val okHttpClient = OkHttpClient.Builder()
        .pingInterval(20, TimeUnit.SECONDS)
        .readTimeout(0, TimeUnit.MILLISECONDS)
        .connectTimeout(15, TimeUnit.SECONDS)
        .build()

    /**
     * Acquire realtime connection for a specific feature (e.g. "forum", "chat", "social").
     * Uses reference counting so multiple screens can share the same socket without
     * disconnect/reconnect thrashing.
     */
    fun acquire(consumerTag: String) {
        synchronized(lock) {
            val count = refCount.incrementAndGet()
            activeConsumerTags.add(consumerTag)
            Log.d(TAG, "Realtime acquired by '$consumerTag'. Ref count: $count. Active: $activeConsumerTags")
            if (count == 1 && isAppInForeground) {
                startConnection()
            }
        }
    }

    /**
     * Release realtime connection for a specific feature when its ViewModel is cleared
     * or the screen is exited. When the reference count reaches 0, the WebSocket and
     * polling fallback are completely terminated.
     */
    fun release(consumerTag: String) {
        synchronized(lock) {
            activeConsumerTags.remove(consumerTag)
            val count = refCount.decrementAndGet()
            if (count < 0) {
                refCount.set(0)
            }
            val currentCount = refCount.get()
            Log.d(TAG, "Realtime released by '$consumerTag'. Ref count: $currentCount. Active: $activeConsumerTags")
            if (currentCount == 0) {
                stopConnection()
            }
        }
    }

    /**
     * Called when the app enters the background (e.g. onStop / ProcessLifecycleOwner).
     * Stops the WebSocket and polling fallback to preserve battery and CPU.
     */
    fun onAppBackgrounded() {
        synchronized(lock) {
            Log.d(TAG, "App in background. Pausing realtime connection (Active refs: ${refCount.get()})")
            isAppInForeground = false
            stopConnection()
        }
    }

    /**
     * Called when the app returns to the foreground. If any active screens/ViewModels
     * still hold a reference (refCount > 0), the connection is resumed.
     */
    fun onAppForegrounded() {
        synchronized(lock) {
            Log.d(TAG, "App in foreground. (Active refs: ${refCount.get()})")
            isAppInForeground = true
            if (refCount.get() > 0) {
                Log.d(TAG, "Resuming realtime connection for active consumers: $activeConsumerTags")
                startConnection()
            }
        }
    }

    // Legacy helper methods
    fun start() {
        acquire("legacy_start")
    }

    fun stop() {
        release("legacy_start")
    }

    private fun startConnection() {
        if (isConnected || isConnecting) {
            Log.d(TAG, "Realtime already connected or connecting, skipping")
            return
        }
        isConnecting = true
        connectWebSocket()
        startPeriodicPollingFallback()
    }

    private fun stopConnection() {
        Log.d(TAG, "Stopping Realtime WebSocket & polling fallback...")
        isConnecting = false
        heartbeatJob?.cancel()
        reconnectJob?.cancel()
        pollingJob?.cancel()
        heartbeatJob = null
        reconnectJob = null
        pollingJob = null
        try {
            webSocket?.close(1000, "Client idle or backgrounded")
        } catch (e: Exception) {
            Log.w(TAG, "Error closing websocket: ${e.message}")
        }
        webSocket = null
        isConnected = false
    }

    private fun connectWebSocket() {
        val host = SupabaseConfig.SUPABASE_URL
            .replace("https://", "wss://")
            .replace("http://", "ws://")
        val wsUrl = "$host/realtime/v1/websocket?apikey=${SupabaseConfig.SUPABASE_ANON_KEY}&vsn=1.0.0"

        val request = Request.Builder()
            .url(wsUrl)
            .build()

        webSocket = okHttpClient.newWebSocket(request, object : WebSocketListener() {
            override fun onOpen(webSocket: WebSocket, response: Response) {
                Log.d(TAG, "Realtime WebSocket connected!")
                isConnected = true
                isConnecting = false
                startHeartbeat()
                joinChannel("realtime:public:forum_threads")
                joinChannel("realtime:public:forum_replies")
                joinChannel("realtime:public:chat_messages")
                joinChannel("realtime:public:friendships")
            }

            override fun onMessage(webSocket: WebSocket, text: String) {
                handleIncomingMessage(text)
            }

            override fun onFailure(webSocket: WebSocket, t: Throwable, response: Response?) {
                Log.w(TAG, "Realtime WebSocket failure: ${t.message}")
                isConnected = false
                isConnecting = false
                scheduleReconnect()
            }

            override fun onClosed(webSocket: WebSocket, code: Int, reason: String) {
                Log.d(TAG, "Realtime WebSocket closed: $reason")
                isConnected = false
                isConnecting = false
                scheduleReconnect()
            }
        })
    }

    private fun joinChannel(topic: String) {
        val ref = messageRefCounter.incrementAndGet().toString()
        val payload = JSONObject().apply {
            put("topic", topic)
            put("event", "phx_join")
            put("payload", JSONObject().apply {
                put("config", JSONObject().apply {
                    put("broadcast", JSONObject().put("self", false))
                    put("presence", JSONObject().put("key", ""))
                    put("postgres_changes", JSONObject().apply {
                        val table = topic.substringAfterLast(":")
                        put("table", table)
                        put("schema", "public")
                        put("event", "*")
                    })
                })
            })
            put("ref", ref)
        }
        webSocket?.send(payload.toString())
    }

    private fun startHeartbeat() {
        heartbeatJob?.cancel()
        heartbeatJob = scope.launch(Dispatchers.IO) {
            while (isActive && isConnected) {
                delay(25000)
                val ref = messageRefCounter.incrementAndGet().toString()
                val hb = JSONObject().apply {
                    put("topic", "phoenix")
                    put("event", "heartbeat")
                    put("payload", JSONObject())
                    put("ref", ref)
                }
                webSocket?.send(hb.toString())
            }
        }
    }

    private fun handleIncomingMessage(text: String) {
        try {
            val json = JSONObject(text)
            val topic = json.optString("topic")
            val event = json.optString("event")
            val payload = json.optJSONObject("payload") ?: JSONObject()

            if (event == "postgres_changes" || event == "INSERT" || event == "UPDATE" || event == "DELETE") {
                val record = payload.optJSONObject("data")?.optJSONObject("record") ?: payload.optJSONObject("record")
                when {
                    topic.contains("forum_threads") -> {
                        val threadId = record?.optString("id")
                        _events.tryEmit(RealtimeEvent.ForumThreadChanged(event, threadId))
                    }
                    topic.contains("forum_replies") -> {
                        val threadId = record?.optString("thread_id") ?: ""
                        val replyId = record?.optString("id") ?: ""
                        _events.tryEmit(RealtimeEvent.ForumReplyInserted(threadId, replyId))
                    }
                    topic.contains("chat_messages") -> {
                        val convId = record?.optString("conversation_id") ?: ""
                        val msgId = record?.optString("id") ?: ""
                        _events.tryEmit(RealtimeEvent.ChatMessageInserted(convId, msgId))
                    }
                    topic.contains("friendships") -> {
                        val rId = record?.optString("requester_id") ?: ""
                        val aId = record?.optString("addressee_id") ?: ""
                        _events.tryEmit(RealtimeEvent.FriendshipChanged(rId, aId))
                    }
                }
            }
        } catch (e: Exception) {
            Log.w(TAG, "Error handling realtime message: ${e.message}")
        }
    }

    private fun scheduleReconnect() {
        synchronized(lock) {
            if (refCount.get() <= 0 || !isAppInForeground) {
                Log.d(TAG, "Skipping reconnect: no active consumers or app in background")
                return
            }
        }
        if (reconnectJob?.isActive == true) return
        reconnectJob = scope.launch(Dispatchers.IO) {
            delay(5000)
            synchronized(lock) {
                if (refCount.get() <= 0 || !isAppInForeground) return@launch
            }
            Log.d(TAG, "Reconnecting Realtime WebSocket...")
            connectWebSocket()
        }
    }

    /**
     * Periodic polling fallback (every 5 seconds) when active consumers are present,
     * ensuring real-time social data stays synced even on firewalled networks.
     */
    private fun startPeriodicPollingFallback() {
        pollingJob?.cancel()
        pollingJob = scope.launch(Dispatchers.IO) {
            while (isActive) {
                delay(5000)
                synchronized(lock) {
                    if (refCount.get() <= 0 || !isAppInForeground) return@launch
                }
                _events.tryEmit(RealtimeEvent.ForumThreadChanged("POLL_SYNC", null))
                _events.tryEmit(RealtimeEvent.FriendshipChanged("", ""))
                _events.tryEmit(RealtimeEvent.ChatMessageInserted("", ""))
            }
        }
    }

    fun notifyLocalForumCreated(threadId: String) {
        _events.tryEmit(RealtimeEvent.ForumThreadChanged("INSERT", threadId))
    }

    fun notifyLocalReplyAdded(threadId: String, replyId: String) {
        _events.tryEmit(RealtimeEvent.ForumReplyInserted(threadId, replyId))
    }

    fun notifyLocalChatMessageSent(convId: String, msgId: String) {
        _events.tryEmit(RealtimeEvent.ChatMessageInserted(convId, msgId))
    }

    fun notifyLocalFriendshipChanged(rId: String, aId: String) {
        _events.tryEmit(RealtimeEvent.FriendshipChanged(rId, aId))
    }
}
