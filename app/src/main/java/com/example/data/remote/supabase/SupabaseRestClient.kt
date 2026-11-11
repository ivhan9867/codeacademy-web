package com.example.data.remote.supabase

import android.util.Log
import com.example.data.model.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody
import org.json.JSONArray
import org.json.JSONObject
import java.util.concurrent.TimeUnit

class SupabaseRestClient(private val authService: SupabaseAuthService) {
    private val TAG = "SupabaseRestClient"

    private val httpClient = OkHttpClient.Builder()
        .connectTimeout(30, TimeUnit.SECONDS)
        .readTimeout(30, TimeUnit.SECONDS)
        .writeTimeout(30, TimeUnit.SECONDS)
        .build()

    private val baseUrl: String
        get() = "${SupabaseConfig.SUPABASE_URL}/rest/v1"

    private fun buildRequest(
        endpoint: String,
        method: String = "GET",
        bodyJson: String? = null,
        preferHeader: String? = null
    ): Request {
        val token = authService.currentAccessToken ?: SupabaseConfig.SUPABASE_ANON_KEY
        val builder = Request.Builder()
            .url("$baseUrl/$endpoint")
            .addHeader("apikey", SupabaseConfig.SUPABASE_ANON_KEY)
            .addHeader("Authorization", "Bearer $token")
            .addHeader("Content-Type", "application/json")

        if (!preferHeader.isNullOrBlank()) {
            builder.addHeader("Prefer", preferHeader)
        }

        val requestBody = bodyJson?.toRequestBody("application/json".toMediaType())

        when (method.uppercase()) {
            "GET" -> builder.get()
            "POST" -> builder.post(requestBody ?: "{}".toRequestBody("application/json".toMediaType()))
            "PATCH" -> builder.patch(requestBody ?: "{}".toRequestBody("application/json".toMediaType()))
            "DELETE" -> builder.delete(requestBody)
        }

        return builder.build()
    }

    private suspend fun executeGetArray(endpoint: String): Result<JSONArray> = withContext(Dispatchers.IO) {
        try {
            val request = buildRequest(endpoint, method = "GET")
            val response = httpClient.newCall(request).execute()
            val body = response.body?.string() ?: "[]"
            if (!response.isSuccessful) {
                Log.w(TAG, "GET $endpoint returned HTTP ${response.code}: $body")
                return@withContext Result.failure(Exception("HTTP ${response.code}: $body"))
            }
            Result.success(JSONArray(body))
        } catch (e: Exception) {
            Log.w(TAG, "executeGetArray failed for $endpoint: ${e.message}")
            Result.failure(e)
        }
    }

    private suspend fun executePost(endpoint: String, bodyJson: String, upsert: Boolean = false): Result<String> = withContext(Dispatchers.IO) {
        try {
            val prefer = if (upsert) "return=representation,resolution=merge-duplicates" else "return=representation"
            val request = buildRequest(endpoint, method = "POST", bodyJson = bodyJson, preferHeader = prefer)
            val response = httpClient.newCall(request).execute()
            val body = response.body?.string() ?: ""
            if (!response.isSuccessful) {
                Log.w(TAG, "POST $endpoint returned HTTP ${response.code}: $body")
                return@withContext Result.failure(Exception("HTTP ${response.code}: $body"))
            }
            Result.success(body)
        } catch (e: Exception) {
            Log.w(TAG, "executePost failed for $endpoint: ${e.message}")
            Result.failure(e)
        }
    }

    private suspend fun executePatch(endpoint: String, bodyJson: String): Result<String> = withContext(Dispatchers.IO) {
        try {
            val request = buildRequest(endpoint, method = "PATCH", bodyJson = bodyJson, preferHeader = "return=representation")
            val response = httpClient.newCall(request).execute()
            val body = response.body?.string() ?: ""
            if (!response.isSuccessful) {
                Log.w(TAG, "PATCH $endpoint returned HTTP ${response.code}: $body")
                return@withContext Result.failure(Exception("HTTP ${response.code}: $body"))
            }
            Result.success(body)
        } catch (e: Exception) {
            Log.w(TAG, "executePatch failed for $endpoint: ${e.message}")
            Result.failure(e)
        }
    }

    private suspend fun executeDelete(endpoint: String): Result<Boolean> = withContext(Dispatchers.IO) {
        try {
            val request = buildRequest(endpoint, method = "DELETE")
            val response = httpClient.newCall(request).execute()
            if (!response.isSuccessful) {
                return@withContext Result.failure(Exception("HTTP ${response.code}"))
            }
            Result.success(true)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    // Reference Data
    suspend fun fetchTracks(): List<TrackEntity> {
        return executeGetArray("tracks?select=*&order=order_index.asc").getOrNull()?.let { arr ->
            val list = mutableListOf<TrackEntity>()
            for (i in 0 until arr.length()) {
                list.add(SupabaseJsonMapper.parseTrack(arr.getJSONObject(i)))
            }
            list
        } ?: emptyList()
    }

    suspend fun fetchCourses(): List<CourseEntity> {
        return executeGetArray("courses?select=*&order=order_index.asc").getOrNull()?.let { arr ->
            val list = mutableListOf<CourseEntity>()
            for (i in 0 until arr.length()) {
                list.add(SupabaseJsonMapper.parseCourse(arr.getJSONObject(i)))
            }
            list
        } ?: emptyList()
    }

    suspend fun fetchModules(): List<ModuleEntity> {
        return executeGetArray("modules?select=*&order=order.asc").getOrNull()?.let { arr ->
            val list = mutableListOf<ModuleEntity>()
            for (i in 0 until arr.length()) {
                list.add(SupabaseJsonMapper.parseModule(arr.getJSONObject(i)))
            }
            list
        } ?: emptyList()
    }

    suspend fun fetchQuizQuestions(): List<QuizQuestionEntity> {
        return executeGetArray("quiz_questions?select=*").getOrNull()?.let { arr ->
            val list = mutableListOf<QuizQuestionEntity>()
            for (i in 0 until arr.length()) {
                list.add(SupabaseJsonMapper.parseQuizQuestion(arr.getJSONObject(i)))
            }
            list
        } ?: emptyList()
    }

    suspend fun fetchBadges(): List<BadgeEntity> {
        return executeGetArray("badges?select=*").getOrNull()?.let { arr ->
            val list = mutableListOf<BadgeEntity>()
            for (i in 0 until arr.length()) {
                list.add(SupabaseJsonMapper.parseBadge(arr.getJSONObject(i)))
            }
            list
        } ?: emptyList()
    }

    suspend fun fetchArAssets(): List<ArAssetEntity> {
        return executeGetArray("ar_assets?select=*").getOrNull()?.let { arr ->
            val list = mutableListOf<ArAssetEntity>()
            for (i in 0 until arr.length()) {
                list.add(SupabaseJsonMapper.parseArAsset(arr.getJSONObject(i)))
            }
            list
        } ?: emptyList()
    }

    // Seed reference data to Supabase if remote tables are empty
    suspend fun seedReferenceDataToSupabase(
        tracks: List<TrackEntity>,
        courses: List<CourseEntity>,
        modules: List<ModuleEntity>,
        quizzes: List<QuizQuestionEntity>,
        badges: List<BadgeEntity>,
        arAssets: List<ArAssetEntity>
    ) {
        val tracksArr = JSONArray()
        tracks.forEachIndexed { idx, t -> tracksArr.put(SupabaseJsonMapper.trackToJson(t, idx)) }
        executePost("tracks", tracksArr.toString(), upsert = true)

        val coursesArr = JSONArray()
        courses.forEachIndexed { idx, c -> coursesArr.put(SupabaseJsonMapper.courseToJson(c, idx)) }
        executePost("courses", coursesArr.toString(), upsert = true)

        val modulesArr = JSONArray()
        modules.forEach { m -> modulesArr.put(SupabaseJsonMapper.moduleToJson(m)) }
        executePost("modules", modulesArr.toString(), upsert = true)

        val quizzesArr = JSONArray()
        quizzes.forEach { q -> quizzesArr.put(SupabaseJsonMapper.quizQuestionToJson(q)) }
        executePost("quiz_questions", quizzesArr.toString(), upsert = true)

        val badgesArr = JSONArray()
        badges.forEach { b -> badgesArr.put(SupabaseJsonMapper.badgeToJson(b)) }
        executePost("badges", badgesArr.toString(), upsert = true)

        val arArr = JSONArray()
        arAssets.forEach { a -> arArr.put(SupabaseJsonMapper.arAssetToJson(a)) }
        executePost("ar_assets", arArr.toString(), upsert = true)
    }

    // Profile
    suspend fun fetchProfile(userId: String): ProfileEntity? {
        val res = executeGetArray("profiles?id=eq.$userId&select=*").getOrNull()
        if (res != null && res.length() > 0) {
            return SupabaseJsonMapper.parseProfile(res.getJSONObject(0))
        }
        return null
    }

    suspend fun upsertProfile(profile: ProfileEntity): Result<String> {
        val json = SupabaseJsonMapper.profileToJson(profile)
        return executePost("profiles", json.toString(), upsert = true)
    }

    // User Progress
    suspend fun fetchUserProgress(userId: String): List<UserProgressEntity> {
        return executeGetArray("user_progress?user_id=eq.$userId&select=*").getOrNull()?.let { arr ->
            val list = mutableListOf<UserProgressEntity>()
            for (i in 0 until arr.length()) {
                list.add(SupabaseJsonMapper.parseUserProgress(arr.getJSONObject(i)))
            }
            list
        } ?: emptyList()
    }

    suspend fun upsertUserProgress(progress: UserProgressEntity): Result<String> {
        val json = SupabaseJsonMapper.userProgressToJson(progress)
        return executePost("user_progress", json.toString(), upsert = true)
    }

    // User Badges
    suspend fun fetchUserBadges(userId: String): List<UserBadgeEntity> {
        return executeGetArray("user_badges?user_id=eq.$userId&select=*").getOrNull()?.let { arr ->
            val list = mutableListOf<UserBadgeEntity>()
            for (i in 0 until arr.length()) {
                list.add(SupabaseJsonMapper.parseUserBadge(arr.getJSONObject(i)))
            }
            list
        } ?: emptyList()
    }

    suspend fun insertUserBadge(userBadge: UserBadgeEntity): Result<String> {
        val json = SupabaseJsonMapper.userBadgeToJson(userBadge)
        return executePost("user_badges", json.toString(), upsert = true)
    }

    // Certificates
    suspend fun fetchCertificates(userId: String): List<CertificateEntity> {
        return executeGetArray("certificates?user_id=eq.$userId&select=*").getOrNull()?.let { arr ->
            val list = mutableListOf<CertificateEntity>()
            for (i in 0 until arr.length()) {
                list.add(SupabaseJsonMapper.parseCertificate(arr.getJSONObject(i)))
            }
            list
        } ?: emptyList()
    }

    suspend fun insertCertificate(cert: CertificateEntity): Result<String> {
        val json = SupabaseJsonMapper.certificateToJson(cert)
        return executePost("certificates", json.toString(), upsert = true)
    }

    // Bookmarks
    suspend fun fetchBookmarks(userId: String): List<BookmarkEntity> {
        return executeGetArray("bookmarks?user_id=eq.$userId&select=*").getOrNull()?.let { arr ->
            val list = mutableListOf<BookmarkEntity>()
            for (i in 0 until arr.length()) {
                list.add(SupabaseJsonMapper.parseBookmark(arr.getJSONObject(i)))
            }
            list
        } ?: emptyList()
    }

    suspend fun insertBookmark(bookmark: BookmarkEntity): Result<String> {
        val json = SupabaseJsonMapper.bookmarkToJson(bookmark)
        return executePost("bookmarks", json.toString(), upsert = false)
    }

    suspend fun deleteBookmark(userId: String, itemId: String): Result<Boolean> {
        return executeDelete("bookmarks?user_id=eq.$userId&item_id=eq.$itemId")
    }

    // Notifications
    suspend fun fetchNotifications(userId: String): List<NotificationEntity> {
        return executeGetArray("notifications?user_id=eq.$userId&order=created_at.desc&select=*").getOrNull()?.let { arr ->
            val list = mutableListOf<NotificationEntity>()
            for (i in 0 until arr.length()) {
                list.add(SupabaseJsonMapper.parseNotification(arr.getJSONObject(i)))
            }
            list
        } ?: emptyList()
    }

    suspend fun insertNotification(notif: NotificationEntity): Result<String> {
        val json = SupabaseJsonMapper.notificationToJson(notif)
        return executePost("notifications", json.toString(), upsert = true)
    }

    suspend fun markAllNotificationsAsRead(userId: String): Result<String> {
        val body = JSONObject().put("is_read", true).toString()
        return executePatch("notifications?user_id=eq.$userId", body)
    }

    suspend fun markNotificationAsRead(id: String): Result<String> {
        val body = JSONObject().put("is_read", true).toString()
        return executePatch("notifications?id=eq.$id", body)
    }

    // Playground Drafts
    suspend fun fetchPlaygroundDraft(userId: String, courseId: String): PlaygroundDraftEntity? {
        val res = executeGetArray("playground_drafts?user_id=eq.$userId&course_id=eq.$courseId&select=*").getOrNull()
        if (res != null && res.length() > 0) {
            return SupabaseJsonMapper.parsePlaygroundDraft(res.getJSONObject(0))
        }
        return null
    }

    suspend fun upsertPlaygroundDraft(draft: PlaygroundDraftEntity): Result<String> {
        val json = SupabaseJsonMapper.playgroundDraftToJson(draft)
        return executePost("playground_drafts", json.toString(), upsert = true)
    }

    // Social: Forum Threads
    suspend fun fetchForumThreads(): List<ForumThreadEntity> {
        val currentUid = authService.currentUserId
        return executeGetArray("forum_threads?select=*&order=created_at.desc").getOrNull()?.let { arr ->
            val list = mutableListOf<ForumThreadEntity>()
            for (i in 0 until arr.length()) {
                list.add(SupabaseJsonMapper.parseForumThread(arr.getJSONObject(i), currentUid))
            }
            list
        } ?: emptyList()
    }

    suspend fun fetchForumThreadById(threadId: String): ForumThreadEntity? {
        val res = executeGetArray("forum_threads?id=eq.$threadId&select=*").getOrNull()
        if (res != null && res.length() > 0) {
            return SupabaseJsonMapper.parseForumThread(res.getJSONObject(0), authService.currentUserId)
        }
        return null
    }

    suspend fun insertForumThread(thread: ForumThreadEntity): Result<String> {
        val json = SupabaseJsonMapper.forumThreadToJson(thread)
        return executePost("forum_threads", json.toString(), upsert = false)
    }

    suspend fun updateForumThread(thread: ForumThreadEntity): Result<String> {
        val body = JSONObject().apply {
            put("upvotes", thread.upvotes)
            put("replies_count", thread.repliesCount)
        }.toString()
        return executePatch("forum_threads?id=eq.${thread.id}", body)
    }

    // Social: Forum Replies
    suspend fun fetchForumReplies(threadId: String): List<ForumReplyEntity> {
        return executeGetArray("forum_replies?thread_id=eq.$threadId&order=created_at.asc&select=*").getOrNull()?.let { arr ->
            val list = mutableListOf<ForumReplyEntity>()
            for (i in 0 until arr.length()) {
                list.add(SupabaseJsonMapper.parseForumReply(arr.getJSONObject(i)))
            }
            list
        } ?: emptyList()
    }

    suspend fun insertForumReply(reply: ForumReplyEntity): Result<String> {
        val json = SupabaseJsonMapper.forumReplyToJson(reply)
        return executePost("forum_replies", json.toString(), upsert = false)
    }

    // Social: Friendships
    suspend fun fetchFriendships(userId: String): List<FriendshipEntity> {
        return executeGetArray("friendships?or=(requester_id.eq.$userId,addressee_id.eq.$userId)&order=created_at.desc&select=*").getOrNull()?.let { arr ->
            val list = mutableListOf<FriendshipEntity>()
            for (i in 0 until arr.length()) {
                list.add(SupabaseJsonMapper.parseFriendship(arr.getJSONObject(i)))
            }
            list
        } ?: emptyList()
    }

    suspend fun insertFriendship(friendship: FriendshipEntity): Result<String> {
        val json = SupabaseJsonMapper.friendshipToJson(friendship)
        return executePost("friendships", json.toString(), upsert = true)
    }

    suspend fun updateFriendshipStatus(requesterId: String, addresseeId: String, status: String): Result<String> {
        val body = JSONObject().put("status", status).toString()
        return executePatch("friendships?requester_id=eq.$requesterId&addressee_id=eq.$addresseeId", body)
    }

    // Social: Chat Conversations & Messages
    suspend fun fetchChatConversations(userId: String): List<ChatConversationEntity> {
        return executeGetArray("chat_conversations?order=last_message_time.desc&select=*").getOrNull()?.let { arr ->
            val list = mutableListOf<ChatConversationEntity>()
            for (i in 0 until arr.length()) {
                list.add(SupabaseJsonMapper.parseChatConversation(arr.getJSONObject(i)))
            }
            list
        } ?: emptyList()
    }

    suspend fun insertChatConversation(conv: ChatConversationEntity): Result<String> {
        val json = SupabaseJsonMapper.chatConversationToJson(conv)
        return executePost("chat_conversations", json.toString(), upsert = true)
    }

    suspend fun updateChatConversationLastMessage(convId: String, message: String, timestamp: Long): Result<String> {
        val body = JSONObject().apply {
            put("last_message", message)
            put("last_message_time", timestamp)
        }.toString()
        return executePatch("chat_conversations?id=eq.$convId", body)
    }

    suspend fun fetchChatMessages(convId: String): List<ChatMessageEntity> {
        return executeGetArray("chat_messages?conversation_id=eq.$convId&order=sent_at.asc&select=*").getOrNull()?.let { arr ->
            val list = mutableListOf<ChatMessageEntity>()
            for (i in 0 until arr.length()) {
                list.add(SupabaseJsonMapper.parseChatMessage(arr.getJSONObject(i)))
            }
            list
        } ?: emptyList()
    }

    suspend fun insertChatMessage(message: ChatMessageEntity): Result<String> {
        val json = SupabaseJsonMapper.chatMessageToJson(message)
        return executePost("chat_messages", json.toString(), upsert = false)
    }
}
