package com.example.ui.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.local.AppDatabase
import com.example.data.model.*
import com.example.data.remote.supabase.SupabaseSession
import com.example.data.remote.supabase.SupabaseUser
import com.example.data.repository.CodeAcademyRepository
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

@OptIn(ExperimentalCoroutinesApi::class)
class CodeAcademyViewModel(application: Application) : AndroidViewModel(application) {
    val repository = CodeAcademyRepository.getInstance(application)

    // Auth & Identity
    val authSession: StateFlow<SupabaseSession?> = repository.authService.sessionFlow
    val currentUser: StateFlow<SupabaseUser?> = repository.authService.currentUserFlow

    val currentUserId: StateFlow<String?> = currentUser.map { it?.id }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), repository.authService.currentUserId)

    // Profile
    val profile: StateFlow<ProfileEntity?> = repository.getProfileFlow()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), null)

    // Reference Data (Offline-First via Room)
    val tracks: StateFlow<List<TrackEntity>> = repository.getAllTracksFlow()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val courses: StateFlow<List<CourseEntity>> = repository.getAllCoursesFlow()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val allBadges: StateFlow<List<BadgeEntity>> = repository.getAllBadgesFlow()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    // User-Owned Data (Dynamic by Authenticated User ID)
    val userProgress: StateFlow<List<UserProgressEntity>> = currentUserId.flatMapLatest { uid ->
        if (uid != null) repository.getUserProgressFlow(uid) else flowOf(emptyList())
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val userBadges: StateFlow<List<UserBadgeEntity>> = currentUserId.flatMapLatest { uid ->
        if (uid != null) repository.getUserBadgesFlow(uid) else flowOf(emptyList())
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val certificates: StateFlow<List<CertificateEntity>> = currentUserId.flatMapLatest { uid ->
        if (uid != null) repository.getCertificatesFlow(uid) else flowOf(emptyList())
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val bookmarks: StateFlow<List<BookmarkEntity>> = currentUserId.flatMapLatest { uid ->
        if (uid != null) repository.getBookmarksFlow(uid) else flowOf(emptyList())
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val notifications: StateFlow<List<NotificationEntity>> = currentUserId.flatMapLatest { uid ->
        if (uid != null) repository.getNotificationsFlow(uid) else flowOf(emptyList())
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val unreadNotifCount = notifications.map { list -> list.count { !it.isRead } }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), 0)

    // Social Data (Supabase Live + Realtime)
    val forumThreads: StateFlow<List<ForumThreadEntity>> = repository.getAllForumThreadsFlow()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val friendships: StateFlow<List<FriendshipEntity>> = currentUserId.flatMapLatest { uid ->
        if (uid != null) repository.getFriendshipsFlow(uid) else flowOf(emptyList())
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val chatConversations: StateFlow<List<ChatConversationEntity>> = currentUserId.flatMapLatest { uid ->
        if (uid != null) repository.getChatConversationsFlow(uid) else flowOf(emptyList())
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    // Level up dialog trigger
    private val _levelUpEvent = MutableStateFlow<Int?>(null)
    val levelUpEvent: StateFlow<Int?> = _levelUpEvent.asStateFlow()

    // AI Tutor state
    private val _activeSessionId = MutableStateFlow("session_general")
    val activeSessionId: StateFlow<String> = _activeSessionId.asStateFlow()

    val aiMessages: StateFlow<List<AiChatMessageEntity>> = _activeSessionId.flatMapLatest { sessionId ->
        repository.getAiChatMessagesFlow(sessionId)
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    private val _isAiGenerating = MutableStateFlow(false)
    val isAiGenerating: StateFlow<Boolean> = _isAiGenerating.asStateFlow()

    fun dismissLevelUpDialog() {
        _levelUpEvent.value = null
    }

    // ==========================================
    // Auth Actions (Supabase Auth)
    // ==========================================

    fun signInWithPassword(email: String, password: String, onResult: (Boolean, String?) -> Unit) {
        viewModelScope.launch {
            val result = repository.authService.signInWithPassword(email, password)
            if (result.isSuccess) {
                val user = result.getOrNull()?.user
                if (user != null) {
                    repository.syncUserData(user.id)
                }
                onResult(true, null)
            } else {
                val errorMsg = result.exceptionOrNull()?.message ?: "Login gagal. Periksa kembali email dan sandi."
                onResult(false, errorMsg)
            }
        }
    }

    fun signUp(email: String, password: String, displayName: String, onResult: (Boolean, String?) -> Unit) {
        viewModelScope.launch {
            val result = repository.authService.signUp(email, password, displayName)
            if (result.isSuccess) {
                val user = result.getOrNull()
                if (user != null) {
                    repository.syncUserData(user.id)
                }
                onResult(true, null)
            } else {
                val errorMsg = result.exceptionOrNull()?.message ?: "Pendaftaran gagal. Silakan coba lagi."
                onResult(false, errorMsg)
            }
        }
    }

    fun signOut(onComplete: () -> Unit = {}) {
        viewModelScope.launch {
            repository.authService.signOut()
            onComplete()
        }
    }

    fun completeOnboarding(selectedTracks: List<String>, goal: String) {
        viewModelScope.launch {
            repository.completeOnboarding(selectedTracks, goal)
        }
    }

    fun updateProfile(name: String, bio: String) {
        viewModelScope.launch {
            val current = repository.getProfile() ?: return@launch
            repository.updateProfile(current.copy(displayName = name, bio = bio))
        }
    }

    fun markModuleCompleted(courseId: String, moduleId: String) {
        val uid = currentUserId.value ?: return
        viewModelScope.launch {
            val (isLevelUp, newLevel) = repository.markModuleCompleted(uid, courseId, moduleId)
            if (isLevelUp) {
                _levelUpEvent.value = newLevel
            }
        }
    }

    fun toggleBookmark(courseId: String) {
        val uid = currentUserId.value ?: return
        viewModelScope.launch {
            repository.toggleBookmark(uid, "COURSE", courseId)
        }
    }

    fun markAllNotificationsRead() {
        val uid = currentUserId.value ?: return
        viewModelScope.launch {
            repository.markAllNotificationsAsRead(uid)
        }
    }

    // AI Chat
    fun sendAiMessage(userMessage: String, codeSnippet: String?) {
        viewModelScope.launch {
            _isAiGenerating.value = true
            val history = aiMessages.value.map { Pair(it.message, it.isUser) }
            val langCode = repository.sessionManager.appLanguageCode.firstOrNull() ?: "id"
            repository.sendAiMessage(_activeSessionId.value, userMessage, codeSnippet, history, language = langCode)
            _isAiGenerating.value = false
        }
    }

    // Forum
    fun toggleUpvoteThread(thread: ForumThreadEntity) {
        viewModelScope.launch {
            repository.toggleUpvoteThread(thread)
        }
    }

    fun createForumThread(courseId: String?, courseTitle: String?, title: String, body: String) {
        val uid = currentUserId.value ?: return
        viewModelScope.launch {
            val author = profile.value?.displayName ?: currentUser.value?.displayName ?: "Siswa Dev"
            repository.createForumThread(uid, author, courseId, courseTitle, title, body)
        }
    }

    fun addForumReply(threadId: String, body: String) {
        val uid = currentUserId.value ?: return
        viewModelScope.launch {
            val author = profile.value?.displayName ?: currentUser.value?.displayName ?: "Siswa Dev"
            repository.addForumReply(threadId, uid, author, body)
        }
    }

    // Friends & Direct Messaging
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

    fun sendChatMessage(convId: String, body: String) {
        val uid = currentUserId.value ?: return
        viewModelScope.launch {
            repository.sendChatMessage(convId, uid, body)
        }
    }

    // Specific Queries Flows
    fun getModulesForCourse(courseId: String): Flow<List<ModuleEntity>> = repository.getModulesByCourseFlow(courseId)
    fun getModuleById(moduleId: String): Flow<ModuleEntity?> = repository.getModuleByIdFlow(moduleId)
    fun getForumThreadById(threadId: String): Flow<ForumThreadEntity?> = repository.getForumThreadByIdFlow(threadId)
    fun getForumReplies(threadId: String): Flow<List<ForumReplyEntity>> = repository.getForumRepliesFlow(threadId)
    fun getChatMessages(convId: String): Flow<List<ChatMessageEntity>> = repository.getChatMessagesFlow(convId)

    suspend fun getQuizQuestions(courseId: String): List<QuizQuestionEntity> = repository.getQuizQuestions(courseId)
    suspend fun submitQuiz(courseId: String, answers: Map<Int, Int>): Triple<Int, Boolean, CertificateEntity?> {
        val uid = currentUserId.value ?: "guest_user"
        return repository.submitQuiz(uid, courseId, answers)
    }
    suspend fun getArAssetForModule(moduleId: String): ArAssetEntity? = repository.getArAssetForModule(moduleId)
}
