package com.example.data.repository

import android.content.Context
import android.util.Log
import com.example.data.local.AppDatabase
import com.example.data.local.CodeAcademyDao
import com.example.data.local.InitialDataSeed
import com.example.data.model.*
import com.example.data.model.CertificateEntity
import com.example.data.model.BookmarkEntity
import com.example.data.model.NotificationEntity
import com.example.data.model.PlaygroundDraftEntity
import com.example.data.remote.GeminiService
import com.example.data.remote.supabase.*
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.*
import java.text.SimpleDateFormat
import java.util.*

class CodeAcademyRepository(
    private val database: AppDatabase,
    context: Context
) {
    private val TAG = "CodeAcademyRepo"
    private val dao: CodeAcademyDao = database.codeAcademyDao()
    private val scope = CoroutineScope(Dispatchers.IO + SupervisorJob())

    val sessionManager = com.example.data.local.SessionManager(context)
    val authService = SupabaseAuthService(context)
    val supabaseRest = SupabaseRestClient(authService)
    val realtimeManager = SupabaseRealtimeManager(authService, scope)

    // Dynamic state for Social data (Forum, Friendships, Chat)
    private val _liveForumThreads = MutableStateFlow<List<ForumThreadEntity>>(emptyList())
    private val _liveFriendships = MutableStateFlow<List<FriendshipEntity>>(emptyList())
    private val _liveChatConversations = MutableStateFlow<List<ChatConversationEntity>>(emptyList())

    init {
        // NOTE: Realtime is no longer auto-started here to avoid draining battery & network.
        // It is strictly acquired and released on-demand by ViewModels that need it (Forum, Chat, Social).

        // Register process lifecycle observer to pause realtime in background and resume in foreground
        android.os.Handler(android.os.Looper.getMainLooper()).post {
            try {
                val ownerClass = Class.forName("androidx.lifecycle.ProcessLifecycleOwner")
                val getMethod = ownerClass.getMethod("get")
                val owner = getMethod.invoke(null) as? androidx.lifecycle.LifecycleOwner
                owner?.lifecycle?.addObserver(
                    androidx.lifecycle.LifecycleEventObserver { _, event ->
                        when (event) {
                            androidx.lifecycle.Lifecycle.Event.ON_START -> {
                                realtimeManager.onAppForegrounded()
                            }
                            androidx.lifecycle.Lifecycle.Event.ON_STOP -> {
                                realtimeManager.onAppBackgrounded()
                            }
                            else -> {}
                        }
                    }
                )
                Log.d(TAG, "ProcessLifecycleOwner observer registered for Realtime Manager")
            } catch (e: Throwable) {
                Log.w(TAG, "ProcessLifecycleOwner observer registration notice: ${e.message}")
            }
        }

        scope.launch {
            // Initial reference data fetch & sync
            syncReferenceData()

            // Observe auth session changes to sync user data
            authService.currentUserFlow.collect { user ->
                if (user != null) {
                    syncUserData(user.id)
                    refreshSocialData(user.id)
                }
            }
        }

        // Listen to Supabase Realtime events for instant updates
        scope.launch {
            realtimeManager.events.collect { event ->
                val currentUid = authService.currentUserId
                when (event) {
                    is RealtimeEvent.ForumThreadChanged -> {
                        refreshForumThreads()
                    }
                    is RealtimeEvent.FriendshipChanged -> {
                        if (currentUid != null) refreshFriendships(currentUid)
                    }
                    is RealtimeEvent.ChatMessageInserted -> {
                        if (currentUid != null) refreshChatConversations(currentUid)
                    }
                    is RealtimeEvent.ForumReplyInserted -> {
                        refreshForumThreads()
                    }
                }
            }
        }
    }

    // Realtime lifecycle management (on-demand ref counting)
    fun acquireRealtime(featureTag: String) {
        realtimeManager.acquire(featureTag)
    }

    fun releaseRealtime(featureTag: String) {
        realtimeManager.release(featureTag)
    }

    fun onAppForegrounded() {
        realtimeManager.onAppForegrounded()
    }

    fun onAppBackgrounded() {
        realtimeManager.onAppBackgrounded()
    }

    // ==========================================
    // 1. REFERENCE DATA (Offline-First Remote->Room Cache)
    // ==========================================

    suspend fun syncReferenceData() = withContext(Dispatchers.IO) {
        try {
            // Check remote Supabase tracks
            val remoteTracks = supabaseRest.fetchTracks()
            if (remoteTracks.isNotEmpty()) {
                dao.insertTracks(remoteTracks)
                val remoteCourses = supabaseRest.fetchCourses()
                if (remoteCourses.isNotEmpty()) dao.insertCourses(remoteCourses)
                val remoteModules = supabaseRest.fetchModules()
                if (remoteModules.isNotEmpty()) dao.insertModules(remoteModules)
                val remoteQuizzes = supabaseRest.fetchQuizQuestions()
                if (remoteQuizzes.isNotEmpty()) dao.insertQuizQuestions(remoteQuizzes)
                val remoteBadges = supabaseRest.fetchBadges()
                if (remoteBadges.isNotEmpty()) dao.insertBadges(remoteBadges)
                val remoteAr = supabaseRest.fetchArAssets()
                if (remoteAr.isNotEmpty()) dao.insertArAssets(remoteAr)
            } else {
                // If Supabase tables are fresh/empty, seed default reference data locally and push to Supabase
                val existingTracks = dao.getAllTracksFlow().firstOrNull()
                if (existingTracks.isNullOrEmpty()) {
                    dao.insertTracks(InitialDataSeed.defaultTracks)
                    dao.insertCourses(InitialDataSeed.defaultCourses)
                    dao.insertModules(InitialDataSeed.defaultModules)
                    dao.insertQuizQuestions(InitialDataSeed.defaultQuizQuestions)
                    dao.insertBadges(InitialDataSeed.defaultBadges)
                    dao.insertArAssets(InitialDataSeed.defaultArAssets)

                    // Push reference seed to Supabase
                    supabaseRest.seedReferenceDataToSupabase(
                        tracks = InitialDataSeed.defaultTracks,
                        courses = InitialDataSeed.defaultCourses,
                        modules = InitialDataSeed.defaultModules,
                        quizzes = InitialDataSeed.defaultQuizQuestions,
                        badges = InitialDataSeed.defaultBadges,
                        arAssets = InitialDataSeed.defaultArAssets
                    )
                }
            }
        } catch (e: Exception) {
            Log.w(TAG, "Reference data sync notice: ${e.message}")
            // Fallback to local cache if offline
            val existingTracks = dao.getAllTracksFlow().firstOrNull()
            if (existingTracks.isNullOrEmpty()) {
                dao.insertTracks(InitialDataSeed.defaultTracks)
                dao.insertCourses(InitialDataSeed.defaultCourses)
                dao.insertModules(InitialDataSeed.defaultModules)
                dao.insertQuizQuestions(InitialDataSeed.defaultQuizQuestions)
                dao.insertBadges(InitialDataSeed.defaultBadges)
                dao.insertArAssets(InitialDataSeed.defaultArAssets)
            }
        }
    }

    // Courses & Tracks (Read from Room - Offline-First)
    fun getAllTracksFlow(): Flow<List<TrackEntity>> = dao.getAllTracksFlow()
    fun getAllCoursesFlow(): Flow<List<CourseEntity>> = dao.getAllCoursesFlow()
    fun getCoursesByTrackFlow(trackId: String): Flow<List<CourseEntity>> = dao.getCoursesByTrackFlow(trackId)
    fun getCourseByIdFlow(courseId: String): Flow<CourseEntity?> = dao.getCourseByIdFlow(courseId)
    suspend fun getCourseById(courseId: String): CourseEntity? = dao.getCourseById(courseId)

    // Modules
    fun getModulesByCourseFlow(courseId: String): Flow<List<ModuleEntity>> = dao.getModulesByCourseFlow(courseId)
    suspend fun getModulesByCourse(courseId: String): List<ModuleEntity> = dao.getModulesByCourse(courseId)
    fun getModuleByIdFlow(moduleId: String): Flow<ModuleEntity?> = dao.getModuleByIdFlow(moduleId)
    suspend fun getModuleById(moduleId: String): ModuleEntity? = dao.getModuleById(moduleId)

    // Badges & AR
    fun getAllBadgesFlow(): Flow<List<BadgeEntity>> = dao.getAllBadgesFlow()
    suspend fun getArAssetForModule(moduleId: String): ArAssetEntity? = dao.getArAssetForModule(moduleId)
    fun getAllArAssetsFlow(): Flow<List<ArAssetEntity>> = dao.getAllArAssetsFlow()

    // ==========================================
    // 2. USER DATA (Write to Supabase + Room, Read from Room)
    // ==========================================

    suspend fun syncUserData(userId: String) = withContext(Dispatchers.IO) {
        try {
            // Sync Profile
            val remoteProfile = supabaseRest.fetchProfile(userId)
            if (remoteProfile != null) {
                dao.insertProfile(remoteProfile)
            } else {
                val localProfile = dao.getProfile()
                if (localProfile != null && localProfile.id == userId) {
                    supabaseRest.upsertProfile(localProfile)
                } else {
                    val user = authService.currentUserFlow.value
                    val newProfile = ProfileEntity(
                        id = userId,
                        email = user?.email ?: "",
                        displayName = user?.displayName?.ifBlank { "Siswa CodeAcademy" } ?: "Siswa CodeAcademy",
                        isOnboarded = false
                    )
                    dao.insertProfile(newProfile)
                    supabaseRest.upsertProfile(newProfile)
                }
            }

            // Sync User Progress
            val remoteProgress = supabaseRest.fetchUserProgress(userId)
            remoteProgress.forEach { dao.insertProgress(it) }

            // Sync Badges
            val remoteBadges = supabaseRest.fetchUserBadges(userId)
            remoteBadges.forEach { dao.unlockBadge(it) }

            // Sync Certificates
            val remoteCerts = supabaseRest.fetchCertificates(userId)
            remoteCerts.forEach { dao.insertCertificate(it) }

            // Sync Bookmarks
            val remoteBookmarks = supabaseRest.fetchBookmarks(userId)
            remoteBookmarks.forEach { dao.insertBookmark(it) }

            // Sync Notifications
            val remoteNotifs = supabaseRest.fetchNotifications(userId)
            remoteNotifs.forEach { dao.insertNotification(it) }
        } catch (e: Exception) {
            Log.w(TAG, "User data sync notice: ${e.message}")
        }
    }

    // Profile Flow
    fun getProfileFlow(): Flow<ProfileEntity?> = dao.getProfileFlow()
    suspend fun getProfile(): ProfileEntity? = dao.getProfile()

    suspend fun updateProfile(profile: ProfileEntity) {
        dao.updateProfile(profile)
        scope.launch {
            try {
                supabaseRest.upsertProfile(profile)
            } catch (e: Exception) {
                Log.w(TAG, "Update profile remote sync error: ${e.message}")
            }
        }
    }

    suspend fun completeOnboarding(selectedTracks: List<String>, goal: String) {
        val current = dao.getProfile() ?: return
        val updated = current.copy(
            selectedTracks = selectedTracks.joinToString(","),
            goal = goal,
            isOnboarded = true
        )
        updateProfile(updated)
    }

    // Gamification & Level Up
    suspend fun addXpAndCheckLevelUp(userId: String, xpGained: Int, reason: String): Pair<Boolean, Int> {
        val profile = dao.getProfile() ?: return Pair(false, 1)
        val newXp = profile.totalXp + xpGained
        val newLevel = (newXp / 250) + 1
        val isLevelUp = newLevel > profile.level

        val today = getTodayDateString()
        var newStreak = profile.currentStreak
        val lastDate = profile.lastActiveDate

        if (lastDate == null) {
            newStreak = 1
        } else if (lastDate != today) {
            val daysDiff = getDaysDifference(lastDate, today)
            newStreak = if (daysDiff == 1) profile.currentStreak + 1 else 1
        }

        val longestStreak = maxOf(profile.longestStreak, newStreak)

        val updatedProfile = profile.copy(
            totalXp = newXp,
            level = newLevel,
            currentStreak = newStreak,
            longestStreak = longestStreak,
            lastActiveDate = today
        )
        updateProfile(updatedProfile)

        if (isLevelUp) {
            val notif = NotificationEntity(
                userId = userId,
                title = "Level Up! Kamu Naik ke Level $newLevel 🎉",
                message = "Hebat! Kamu telah mencapai level baru dengan total $newXp XP ($reason).",
                type = "LEVEL_UP"
            )
            dao.insertNotification(notif)
            scope.launch { supabaseRest.insertNotification(notif) }
        }

        checkBadgesEligibility(userId, newXp, newStreak)
        return Pair(isLevelUp, newLevel)
    }

    private suspend fun checkBadgesEligibility(userId: String, totalXp: Int, streak: Int) {
        if (totalXp >= 50) unlockBadge(userId, "badge_first_code")
        if (streak >= 3) unlockBadge(userId, "badge_streak_3")
        if (totalXp >= 500) unlockBadge(userId, "badge_web_dev")
        if (totalXp >= 1000) unlockBadge(userId, "badge_polyglot")
    }

    private suspend fun unlockBadge(userId: String, badgeId: String) {
        val userBadge = UserBadgeEntity(userId = userId, badgeId = badgeId)
        dao.unlockBadge(userBadge)
        scope.launch { supabaseRest.insertUserBadge(userBadge) }
    }

    // User Progress (Read from Room, Write to Supabase + Room)
    fun getUserProgressFlow(userId: String): Flow<List<UserProgressEntity>> = dao.getUserProgressFlow(userId)
    fun getCourseProgressFlow(userId: String, courseId: String): Flow<List<UserProgressEntity>> = dao.getCourseProgressFlow(userId, courseId)

    suspend fun markModuleCompleted(userId: String, courseId: String, moduleId: String): Pair<Boolean, Int> {
        val progress = UserProgressEntity(
            userId = userId,
            courseId = courseId,
            moduleId = moduleId,
            isCompleted = true,
            completedAt = System.currentTimeMillis()
        )
        dao.insertProgress(progress)
        scope.launch { supabaseRest.upsertUserProgress(progress) }

        return addXpAndCheckLevelUp(userId, 50, "Menyelesaikan Modul")
    }

    // Quizzes, Evaluation & Certificates
    suspend fun getQuizQuestions(courseId: String): List<QuizQuestionEntity> = dao.getQuizQuestionsByCourse(courseId)

    suspend fun submitQuiz(
        userId: String,
        courseId: String,
        answers: Map<Int, Int>
    ): Triple<Int, Boolean, CertificateEntity?> {
        val questions = dao.getQuizQuestionsByCourse(courseId)
        if (questions.isEmpty()) return Triple(0, false, null)

        var correctCount = 0
        questions.forEachIndexed { index, question ->
            val chosen = answers[index]
            if (chosen != null && chosen == question.correctIndex) {
                correctCount++
            }
        }

        val scorePercent = (correctCount * 100) / questions.size
        val isPassed = scorePercent >= 70
        var generatedCert: CertificateEntity? = null

        if (isPassed) {
            addXpAndCheckLevelUp(userId, 150, "Lulus Quiz Course")

            if (scorePercent == 100) {
                unlockBadge(userId, "badge_quiz_master")
            }

            val existingCert = dao.getCertificateForCourse(userId, courseId)
            if (existingCert == null) {
                val profile = dao.getProfile()
                val course = dao.getCourseById(courseId)
                val certId = "CA-CERT-" + UUID.randomUUID().toString().take(8).uppercase()
                val cert = CertificateEntity(
                    certId = certId,
                    userId = userId,
                    courseId = courseId,
                    courseTitle = course?.title ?: "Kursus Pemrograman",
                    studentName = profile?.displayName ?: "Siswa CodeAcademy",
                    issueDate = getTodayDateString(),
                    scorePercent = scorePercent
                )
                dao.insertCertificate(cert)
                scope.launch {
                    supabaseRest.insertCertificate(cert)
                    val notif = NotificationEntity(
                        userId = userId,
                        title = "Sertifikat Kelulusan Terbit! 🎓",
                        message = "Selamat! Kamu telah lulus ${course?.title} dengan skor $scorePercent%.",
                        type = "BADGE",
                        targetRoute = "certificate_detail/$certId"
                    )
                    dao.insertNotification(notif)
                    supabaseRest.insertNotification(notif)
                }
                generatedCert = cert
            } else {
                generatedCert = existingCert
            }
        }

        return Triple(scorePercent, isPassed, generatedCert)
    }

    // Certificates & Badges
    fun getCertificatesFlow(userId: String): Flow<List<CertificateEntity>> = dao.getCertificatesFlow(userId)
    fun getUserBadgesFlow(userId: String): Flow<List<UserBadgeEntity>> = dao.getUserBadgesFlow(userId)

    // Bookmarks
    fun getBookmarksFlow(userId: String): Flow<List<BookmarkEntity>> = dao.getBookmarksFlow(userId)
    suspend fun toggleBookmark(userId: String, itemType: String, itemId: String): Boolean {
        val existing = dao.getBookmark(userId, itemId)
        return if (existing != null) {
            dao.deleteBookmark(userId, itemId)
            scope.launch { supabaseRest.deleteBookmark(userId, itemId) }
            false
        } else {
            val bookmark = BookmarkEntity(userId = userId, itemType = itemType, itemId = itemId)
            dao.insertBookmark(bookmark)
            scope.launch { supabaseRest.insertBookmark(bookmark) }
            true
        }
    }

    // Notifications
    fun getNotificationsFlow(userId: String): Flow<List<NotificationEntity>> = dao.getNotificationsFlow(userId)
    suspend fun markAllNotificationsAsRead(userId: String) {
        dao.markAllNotificationsAsRead(userId)
        scope.launch { supabaseRest.markAllNotificationsAsRead(userId) }
    }
    suspend fun markNotificationAsRead(id: String) {
        dao.markNotificationAsRead(id)
        scope.launch { supabaseRest.markNotificationAsRead(id) }
    }

    // Playground Drafts
    fun getAllPlaygroundDraftsFlow(userId: String): Flow<List<PlaygroundDraftEntity>> = dao.getAllPlaygroundDraftsFlow(userId)

    suspend fun getPlaygroundDraft(userId: String, courseId: String): PlaygroundDraftEntity? {
        val local = dao.getPlaygroundDraft(userId, courseId)
        if (local != null) return local
        return try {
            val remote = supabaseRest.fetchPlaygroundDraft(userId, courseId)
            if (remote != null) dao.savePlaygroundDraft(remote)
            remote
        } catch (e: Exception) {
            null
        }
    }

    suspend fun savePlaygroundDraft(draft: PlaygroundDraftEntity) {
        dao.savePlaygroundDraft(draft)
        scope.launch { supabaseRest.upsertPlaygroundDraft(draft) }
    }

    // AI Chat
    fun getAiChatSessionsFlow(userId: String): Flow<List<AiChatSessionEntity>> = dao.getAiChatSessionsFlow(userId)
    fun getAiChatMessagesFlow(sessionId: String): Flow<List<AiChatMessageEntity>> = dao.getAiChatMessagesFlow(sessionId)

    suspend fun sendAiMessage(
        sessionId: String,
        userMessage: String,
        codeSnippet: String? = null,
        history: List<Pair<String, Boolean>>,
        language: String = "id"
    ): String {
        dao.insertAiChatMessage(
            AiChatMessageEntity(
                sessionId = sessionId,
                isUser = true,
                message = userMessage,
                codeSnippet = codeSnippet
            )
        )

        val token = authService.currentAccessToken
        val result = GeminiService.askAiTutor(
            userMessage = userMessage,
            codeContext = codeSnippet,
            conversationHistory = history,
            authToken = token,
            language = language
        )
        val aiReply = result.getOrDefault("Maaf, terjadi kendala saat memproses jawaban. Silakan coba lagi.")

        dao.insertAiChatMessage(
            AiChatMessageEntity(
                sessionId = sessionId,
                isUser = false,
                message = aiReply
            )
        )
        return aiReply
    }

    suspend fun createAiChatSession(userId: String, courseId: String?, title: String): String {
        val sessionId = UUID.randomUUID().toString()
        dao.insertAiChatSession(
            AiChatSessionEntity(
                id = sessionId,
                userId = userId,
                courseId = courseId,
                title = title
            )
        )
        return sessionId
    }

    // ==========================================
    // 3. SOCIAL DATA (Direct to Supabase + Realtime Subscription)
    // ==========================================

    private suspend fun refreshSocialData(userId: String) {
        refreshForumThreads()
        refreshFriendships(userId)
        refreshChatConversations(userId)
    }

    private suspend fun refreshForumThreads() = withContext(Dispatchers.IO) {
        try {
            val threads = supabaseRest.fetchForumThreads()
            _liveForumThreads.value = threads
            // Cache locally
            threads.forEach { dao.insertForumThread(it) }
        } catch (e: Exception) {
            Log.w(TAG, "Refresh forum threads notice: ${e.message}")
        }
    }

    private suspend fun refreshFriendships(userId: String) = withContext(Dispatchers.IO) {
        try {
            val friendships = supabaseRest.fetchFriendships(userId)
            _liveFriendships.value = friendships
            friendships.forEach { dao.insertFriendship(it) }
        } catch (e: Exception) {
            Log.w(TAG, "Refresh friendships notice: ${e.message}")
        }
    }

    private suspend fun refreshChatConversations(userId: String) = withContext(Dispatchers.IO) {
        try {
            val conversations = supabaseRest.fetchChatConversations(userId)
            _liveChatConversations.value = conversations
            conversations.forEach { dao.insertChatConversation(it) }
        } catch (e: Exception) {
            Log.w(TAG, "Refresh chat convs notice: ${e.message}")
        }
    }

    fun getAllForumThreadsFlow(): Flow<List<ForumThreadEntity>> {
        // Refresh immediately and return live flow combined with room fallback
        scope.launch { refreshForumThreads() }
        return _liveForumThreads.map { liveList ->
            liveList.ifEmpty { dao.getAllForumThreadsFlow().firstOrNull() ?: emptyList() }
        }
    }

    fun getForumThreadsByCourseFlow(courseId: String): Flow<List<ForumThreadEntity>> {
        return getAllForumThreadsFlow().map { list ->
            list.filter { it.courseId == courseId }
        }
    }

    fun getForumThreadByIdFlow(threadId: String): Flow<ForumThreadEntity?> = flow {
        val initial = supabaseRest.fetchForumThreadById(threadId) ?: dao.getForumThreadByIdFlow(threadId).firstOrNull()
        emit(initial)
        realtimeManager.events.filterIsInstance<RealtimeEvent.ForumThreadChanged>().collect {
            emit(supabaseRest.fetchForumThreadById(threadId))
        }
    }

    fun getForumRepliesFlow(threadId: String): Flow<List<ForumReplyEntity>> = flow {
        val initial = supabaseRest.fetchForumReplies(threadId)
        emit(initial)
        realtimeManager.events.filterIsInstance<RealtimeEvent.ForumReplyInserted>().collect {
            if (it.threadId == threadId || it.threadId.isEmpty()) {
                val updated = supabaseRest.fetchForumReplies(threadId)
                emit(updated)
            }
        }
    }

    suspend fun createForumThread(
        userId: String,
        authorName: String,
        courseId: String?,
        courseTitle: String?,
        title: String,
        body: String
    ) {
        val thread = ForumThreadEntity(
            userId = userId,
            authorName = authorName,
            courseId = courseId,
            courseTitle = courseTitle,
            title = title,
            body = body
        )
        dao.insertForumThread(thread)
        _liveForumThreads.update { listOf(thread) + it }
        scope.launch {
            supabaseRest.insertForumThread(thread)
            realtimeManager.notifyLocalForumCreated(thread.id)
            refreshForumThreads()
        }
        addXpAndCheckLevelUp(userId, 25, "Membuat Diskusi Forum")
    }

    suspend fun toggleUpvoteThread(thread: ForumThreadEntity) {
        val updated = if (thread.hasUpvoted) {
            thread.copy(upvotes = maxOf(0, thread.upvotes - 1), hasUpvoted = false)
        } else {
            thread.copy(upvotes = thread.upvotes + 1, hasUpvoted = true)
        }
        dao.updateForumThread(updated)
        _liveForumThreads.update { list ->
            list.map { if (it.id == updated.id) updated else it }
        }
        scope.launch {
            supabaseRest.updateForumThread(updated)
            realtimeManager.notifyLocalForumCreated(thread.id)
        }
    }

    suspend fun addForumReply(threadId: String, userId: String, authorName: String, body: String) {
        val reply = ForumReplyEntity(
            threadId = threadId,
            userId = userId,
            authorName = authorName,
            body = body
        )
        dao.insertForumReply(reply)
        dao.incrementReplyCount(threadId)
        scope.launch {
            supabaseRest.insertForumReply(reply)
            realtimeManager.notifyLocalReplyAdded(threadId, reply.id)
            refreshForumThreads()
        }
        addXpAndCheckLevelUp(userId, 15, "Menjawab Diskusi Forum")
    }

    // Friendships (Supabase Direct + Realtime)
    fun getFriendshipsFlow(userId: String): Flow<List<FriendshipEntity>> {
        scope.launch { refreshFriendships(userId) }
        return _liveFriendships.map { liveList ->
            liveList.ifEmpty { dao.getFriendshipsFlow(userId).firstOrNull() ?: emptyList() }
        }
    }

    suspend fun sendFriendRequest(userId: String, targetName: String) {
        val friendId = "friend_" + UUID.randomUUID().toString().take(6)
        val friendship = FriendshipEntity(
            requesterId = userId,
            addresseeId = friendId,
            friendName = targetName,
            friendBio = "Siswa Pembelajar di CodeAcademy 🚀",
            friendXp = 650,
            friendLevel = 3,
            status = "PENDING"
        )
        dao.insertFriendship(friendship)
        _liveFriendships.update { listOf(friendship) + it }
        scope.launch {
            supabaseRest.insertFriendship(friendship)
            realtimeManager.notifyLocalFriendshipChanged(userId, friendId)
            refreshFriendships(userId)
        }
    }

    suspend fun acceptFriendRequest(rId: String, aId: String) {
        dao.updateFriendshipStatus(rId, aId, "ACCEPTED")
        _liveFriendships.update { list ->
            list.map { if (it.requesterId == rId && it.addresseeId == aId) it.copy(status = "ACCEPTED") else it }
        }
        scope.launch {
            supabaseRest.updateFriendshipStatus(rId, aId, "ACCEPTED")
            realtimeManager.notifyLocalFriendshipChanged(rId, aId)
            val currentUid = authService.currentUserId
            if (currentUid != null) refreshFriendships(currentUid)
        }
    }

    // Direct 1-on-1 Chat (Supabase Direct + Realtime)
    fun getChatConversationsFlow(userId: String): Flow<List<ChatConversationEntity>> {
        scope.launch { refreshChatConversations(userId) }
        return _liveChatConversations.map { liveList ->
            liveList.ifEmpty { dao.getChatConversationsFlow().firstOrNull() ?: emptyList() }
        }
    }

    fun getChatMessagesFlow(convId: String): Flow<List<ChatMessageEntity>> = flow {
        val initial = supabaseRest.fetchChatMessages(convId).ifEmpty {
            dao.getChatMessagesFlow(convId).firstOrNull() ?: emptyList()
        }
        emit(initial)
        realtimeManager.events.filterIsInstance<RealtimeEvent.ChatMessageInserted>().collect {
            val updated = supabaseRest.fetchChatMessages(convId)
            emit(updated)
        }
    }

    suspend fun sendChatMessage(convId: String, senderId: String, body: String) {
        val msg = ChatMessageEntity(
            conversationId = convId,
            senderId = senderId,
            body = body,
            sentAt = System.currentTimeMillis()
        )
        dao.insertChatMessage(msg)
        dao.updateLastChatMessage(convId, body, System.currentTimeMillis())
        _liveChatConversations.update { list ->
            list.map { if (it.id == convId) it.copy(lastMessage = body, lastMessageTime = System.currentTimeMillis()) else it }
        }
        scope.launch {
            supabaseRest.insertChatMessage(msg)
            supabaseRest.updateChatConversationLastMessage(convId, body, System.currentTimeMillis())
            realtimeManager.notifyLocalChatMessageSent(convId, msg.id)
        }
    }

    // Helpers
    private fun getTodayDateString(): String {
        val sdf = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
        return sdf.format(Date())
    }

    private fun getDaysDifference(dateStr1: String, dateStr2: String): Int {
        val sdf = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
        return try {
            val d1 = sdf.parse(dateStr1) ?: return 0
            val d2 = sdf.parse(dateStr2) ?: return 0
            val diff = d2.time - d1.time
            (diff / (1000 * 60 * 60 * 24)).toInt()
        } catch (e: Exception) {
            0
        }
    }

    companion object {
        @Volatile
        private var INSTANCE: CodeAcademyRepository? = null

        fun getInstance(context: Context): CodeAcademyRepository {
            return INSTANCE ?: synchronized(this) {
                INSTANCE ?: CodeAcademyRepository(
                    AppDatabase.getDatabase(context.applicationContext),
                    context.applicationContext
                ).also { INSTANCE = it }
            }
        }
    }
}
