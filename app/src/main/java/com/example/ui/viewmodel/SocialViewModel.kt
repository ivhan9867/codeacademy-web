package com.example.ui.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.SocialRepository
import com.example.data.local.ForumPostEntity
import com.example.data.local.LeaderboardEntryCacheEntity
import com.example.data.local.WeeklyChallengeEntity
import com.example.data.model.FriendshipEntity
import com.example.data.repository.CodeAcademyRepository
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.ListenerRegistration
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

/**
 * Canonical ViewModel for social features across CodeAcademy:
 * - Community, leaderboard, weekly challenges, and course discussion forum (Firestore & local cache).
 * - Real-time friendship management and friend requests.
 */
@OptIn(ExperimentalCoroutinesApi::class)
class SocialViewModel(application: Application) : AndroidViewModel(application) {
    private val socialRepo: SocialRepository = SocialRepository.getInstance(application)
    private val repository: CodeAcademyRepository = CodeAcademyRepository.getInstance(application)

    init {
        repository.acquireRealtime("social")
    }

    override fun onCleared() {
        super.onCleared()
        repository.releaseRealtime("social")
    }

    // ==========================================
    // FRIENDSHIPS & SOCIAL (REALTIME)
    // ==========================================
    val currentUserId: StateFlow<String?> = repository.authService.currentUserFlow
        .map { it?.id }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), repository.authService.currentUserId)

    val friendships: StateFlow<List<FriendshipEntity>> = currentUserId.flatMapLatest { uid ->
        if (uid != null) repository.getFriendshipsFlow(uid) else flowOf(emptyList())
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    fun sendFriendRequest(targetName: String) {
        val uid = currentUserId.value ?: return
        viewModelScope.launch {
            repository.sendFriendRequest(uid, targetName)
        }
    }

    fun acceptFriendRequest(requesterId: String, addresseeId: String) {
        viewModelScope.launch {
            repository.acceptFriendRequest(requesterId, addresseeId)
        }
    }

    // ==========================================
    // LEADERBOARD (CLOUD DATA + ROOM CACHE MULTI-SCOPE)
    // ==========================================
    private val _selectedLeaderboardPeriod = MutableStateFlow("weekly")
    val selectedLeaderboardPeriod: StateFlow<String> = _selectedLeaderboardPeriod.asStateFlow()

    private val _selectedLeaderboardTrack = MutableStateFlow("global")
    val selectedLeaderboardTrack: StateFlow<String> = _selectedLeaderboardTrack.asStateFlow()

    val leaderboardEntries: StateFlow<List<LeaderboardEntryCacheEntity>> =
        combine(_selectedLeaderboardPeriod, _selectedLeaderboardTrack) { period, track ->
            "${period}_${track}"
        }.flatMapLatest { scopeKey ->
            socialRepo.getCachedLeaderboard(scopeKey)
        }.distinctUntilChanged()
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )

    private val _isLeaderboardRefreshing = MutableStateFlow(false)
    val isLeaderboardRefreshing: StateFlow<Boolean> = _isLeaderboardRefreshing.asStateFlow()

    private val _leaderboardError = MutableStateFlow<String?>(null)
    val leaderboardError: StateFlow<String?> = _leaderboardError.asStateFlow()

    private val _myLeaderboardRank = MutableStateFlow<Int?>(null)
    val myLeaderboardRank: StateFlow<Int?> = _myLeaderboardRank.asStateFlow()

    private var lastLeaderboardRefreshTime: Long = 0L
    private var lastLeaderboardPeriod: String? = null
    private var lastLeaderboardTrackId: String? = null
    private var lastForumHighlightsRefreshTime: Long = 0L
    private val MIN_REFRESH_INTERVAL_MS = 60_000L

    fun setLeaderboardFilters(period: String, track: String) {
        _selectedLeaderboardPeriod.value = period
        _selectedLeaderboardTrack.value = track
        refreshLeaderboard(period, track, force = true)
    }

    fun refreshLeaderboard(
        period: String = _selectedLeaderboardPeriod.value,
        trackId: String = _selectedLeaderboardTrack.value,
        force: Boolean = false
    ) {
        val now = System.currentTimeMillis()
        val filterChanged = (period != lastLeaderboardPeriod || trackId != lastLeaderboardTrackId)
        if (!force && !filterChanged && (now - lastLeaderboardRefreshTime < MIN_REFRESH_INTERVAL_MS)) {
            return
        }
        viewModelScope.launch {
            _isLeaderboardRefreshing.value = true
            try {
                socialRepo.refreshLeaderboard(period = period, trackId = if (trackId == "global") null else trackId)
                lastLeaderboardRefreshTime = System.currentTimeMillis()
                lastLeaderboardPeriod = period
                lastLeaderboardTrackId = trackId
                val profile = socialRepo.getUserProfileOnce()
                val scoreToCompare = if (period == "streak") (profile?.currentStreak ?: 0) else (profile?.totalXp ?: 0)
                val rank = socialRepo.calculateUserRank(
                    userXp = scoreToCompare,
                    period = period,
                    trackId = if (trackId == "global") null else trackId
                )
                _myLeaderboardRank.value = rank
                _leaderboardError.value = null
            } catch (e: Exception) {
                _leaderboardError.value = "Gagal memuat papan peringkat. Periksa koneksi internetmu."
            } finally {
                _isLeaderboardRefreshing.value = false
            }
        }
    }

    // ==========================================
    // WEEKLY CHALLENGES (KOMUNITAS)
    // ==========================================
    val weeklyChallenge: StateFlow<WeeklyChallengeEntity?> = socialRepo.activeWeeklyChallenge.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = null
    )

    val weeklyChallengeProgress: StateFlow<Int> = socialRepo.observeModuleProgressList().map { list ->
        list.count { it.isCompleted }
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = 0
    )

    fun claimWeeklyChallengeReward(challengeId: String, onResult: (Boolean) -> Unit = {}) {
        viewModelScope.launch {
            val success = socialRepo.claimWeeklyChallengeReward(challengeId)
            onResult(success)
        }
    }

    // ==========================================
    // SOROTAN FORUM (KOMUNITAS)
    // ==========================================
    private val _forumHighlights = MutableStateFlow<List<ForumPostEntity>>(emptyList())
    val forumHighlights: StateFlow<List<ForumPostEntity>> = _forumHighlights.asStateFlow()

    private val _isForumHighlightsLoading = MutableStateFlow(false)
    val isForumHighlightsLoading: StateFlow<Boolean> = _isForumHighlightsLoading.asStateFlow()

    fun refreshForumHighlights(force: Boolean = false) {
        val now = System.currentTimeMillis()
        if (!force && (now - lastForumHighlightsRefreshTime < MIN_REFRESH_INTERVAL_MS)) {
            return
        }
        viewModelScope.launch {
            _isForumHighlightsLoading.value = true
            try {
                val highlights = socialRepo.getTopForumHighlights(limit = 3L)
                _forumHighlights.value = highlights
                lastForumHighlightsRefreshTime = System.currentTimeMillis()
            } finally {
                _isForumHighlightsLoading.value = false
            }
        }
    }

    // ==========================================
    // FORUM DISKUSI PER KELAS
    // ==========================================
    fun getCourseForumPosts(courseId: String): Flow<List<ForumPostEntity>> {
        return socialRepo.getForumPosts(courseId)
    }

    fun refreshCourseForum(
        courseId: String,
        onFinished: (DocumentSnapshot?) -> Unit = {}
    ) {
        viewModelScope.launch {
            val result = socialRepo.refreshForumPosts(courseId, limit = 20L)
            val doc = if (result.isSuccess) result.getOrNull()?.second else null
            onFinished(doc)
        }
    }

    fun loadMoreForumPosts(
        courseId: String,
        lastDoc: DocumentSnapshot?,
        onFinished: (DocumentSnapshot?, Int) -> Unit
    ) {
        viewModelScope.launch {
            val result = socialRepo.loadMoreForumPosts(courseId, lastDoc, limit = 20L)
            if (result.isSuccess) {
                val (newPosts, nextDoc) = result.getOrThrow()
                onFinished(nextDoc, newPosts.size)
            } else {
                onFinished(lastDoc, 0)
            }
        }
    }

    fun createForumPost(
        courseId: String,
        content: String,
        replyToPostId: String? = null,
        replyToAuthorName: String? = null,
        replyToContentSnippet: String? = null,
        onComplete: (Boolean) -> Unit
    ) {
        viewModelScope.launch {
            val result = socialRepo.createForumPost(
                courseId = courseId,
                content = content,
                replyToPostId = replyToPostId,
                replyToAuthorName = replyToAuthorName,
                replyToContentSnippet = replyToContentSnippet
            )
            onComplete(result.isSuccess)
        }
    }

    fun toggleLikeForumPost(postId: String, currentLikes: Int, isLiked: Boolean) {
        viewModelScope.launch {
            socialRepo.toggleLikePost(postId, currentLikes, isLiked)
        }
    }

    fun attachForumListener(
        courseId: String,
        onError: (Exception) -> Unit = {},
        onUpdate: (List<ForumPostEntity>) -> Unit
    ): ListenerRegistration? {
        return socialRepo.firestoreService.attachRecentPostsListener(
            courseId = courseId,
            onUpdate = onUpdate,
            onError = onError
        )
    }
}
