package com.example.data.local

import com.example.data.model.*
import com.example.data.model.CertificateEntity
import com.example.data.model.BookmarkEntity
import com.example.data.model.NotificationEntity
import com.example.data.model.PlaygroundDraftEntity
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.map

class InMemoryCodeAcademyDao : CodeAcademyDao {
    private val profileFlow = MutableStateFlow<ProfileEntity?>(null)
    private val tracksFlow = MutableStateFlow<List<TrackEntity>>(emptyList())
    private val coursesFlow = MutableStateFlow<List<CourseEntity>>(emptyList())
    private val modulesFlow = MutableStateFlow<List<ModuleEntity>>(emptyList())
    private val quizQuestions = mutableListOf<QuizQuestionEntity>()
    private val progressFlow = MutableStateFlow<List<UserProgressEntity>>(emptyList())
    private val certificatesFlow = MutableStateFlow<List<CertificateEntity>>(emptyList())
    private val badgesFlow = MutableStateFlow<List<BadgeEntity>>(emptyList())
    private val userBadgesFlow = MutableStateFlow<List<UserBadgeEntity>>(emptyList())
    private val bookmarksFlow = MutableStateFlow<List<BookmarkEntity>>(emptyList())
    private val notificationsFlow = MutableStateFlow<List<NotificationEntity>>(emptyList())
    private val playgroundDraftsFlow = MutableStateFlow<List<PlaygroundDraftEntity>>(emptyList())
    private val aiSessionsFlow = MutableStateFlow<List<AiChatSessionEntity>>(emptyList())
    private val aiMessagesFlow = MutableStateFlow<List<AiChatMessageEntity>>(emptyList())
    private val forumThreadsFlow = MutableStateFlow<List<ForumThreadEntity>>(emptyList())
    private val forumRepliesFlow = MutableStateFlow<List<ForumReplyEntity>>(emptyList())
    private val friendshipsFlow = MutableStateFlow<List<FriendshipEntity>>(emptyList())
    private val chatConversationsFlow = MutableStateFlow<List<ChatConversationEntity>>(emptyList())
    private val chatMessagesFlow = MutableStateFlow<List<ChatMessageEntity>>(emptyList())
    private val arAssetsFlow = MutableStateFlow<List<ArAssetEntity>>(emptyList())

    companion object {
        val instance = InMemoryCodeAcademyDao()
    }

    override fun getProfileFlow(): Flow<ProfileEntity?> = profileFlow
    override suspend fun getProfile(): ProfileEntity? = profileFlow.value
    override suspend fun insertProfile(profile: ProfileEntity) { profileFlow.value = profile }
    override suspend fun updateProfile(profile: ProfileEntity) { profileFlow.value = profile }

    override fun getAllTracksFlow(): Flow<List<TrackEntity>> = tracksFlow
    override suspend fun insertTracks(tracks: List<TrackEntity>) {
        val current = tracksFlow.value.toMutableList()
        tracks.forEach { t -> current.removeAll { it.id == t.id }; current.add(t) }
        tracksFlow.value = current
    }

    override fun getAllCoursesFlow(): Flow<List<CourseEntity>> = coursesFlow
    override fun getCoursesByTrackFlow(trackId: String): Flow<List<CourseEntity>> =
        coursesFlow.map { list -> list.filter { it.trackId == trackId } }
    override fun getCourseByIdFlow(courseId: String): Flow<CourseEntity?> =
        coursesFlow.map { list -> list.find { it.id == courseId } }
    override suspend fun getCourseById(courseId: String): CourseEntity? =
        coursesFlow.value.find { it.id == courseId }
    override suspend fun insertCourses(courses: List<CourseEntity>) {
        val current = coursesFlow.value.toMutableList()
        courses.forEach { c -> current.removeAll { it.id == c.id }; current.add(c) }
        coursesFlow.value = current
    }

    override fun getModulesByCourseFlow(courseId: String): Flow<List<ModuleEntity>> =
        modulesFlow.map { list -> list.filter { it.courseId == courseId }.sortedBy { it.order } }
    override suspend fun getModulesByCourse(courseId: String): List<ModuleEntity> =
        modulesFlow.value.filter { it.courseId == courseId }.sortedBy { it.order }
    override fun getModuleByIdFlow(moduleId: String): Flow<ModuleEntity?> =
        modulesFlow.map { list -> list.find { it.id == moduleId } }
    override suspend fun getModuleById(moduleId: String): ModuleEntity? =
        modulesFlow.value.find { it.id == moduleId }
    override suspend fun insertModules(modules: List<ModuleEntity>) {
        val current = modulesFlow.value.toMutableList()
        modules.forEach { m -> current.removeAll { it.id == m.id }; current.add(m) }
        modulesFlow.value = current
    }

    override suspend fun getQuizQuestionsByCourse(courseId: String): List<QuizQuestionEntity> =
        synchronized(quizQuestions) { quizQuestions.filter { it.courseId == courseId } }
    override suspend fun insertQuizQuestions(questions: List<QuizQuestionEntity>) {
        synchronized(quizQuestions) {
            questions.forEach { q -> quizQuestions.removeAll { it.id == q.id }; quizQuestions.add(q) }
        }
    }

    override fun getUserProgressFlow(userId: String): Flow<List<UserProgressEntity>> =
        progressFlow.map { list -> list.filter { it.userId == userId } }
    override fun getCourseProgressFlow(userId: String, courseId: String): Flow<List<UserProgressEntity>> =
        progressFlow.map { list -> list.filter { it.userId == userId && it.courseId == courseId } }
    override suspend fun getCourseProgressList(userId: String, courseId: String): List<UserProgressEntity> =
        progressFlow.value.filter { it.userId == userId && it.courseId == courseId }
    override suspend fun insertProgress(progress: UserProgressEntity) {
        val current = progressFlow.value.toMutableList()
        current.removeAll { it.userId == progress.userId && it.moduleId == progress.moduleId }
        current.add(progress)
        progressFlow.value = current
    }

    override fun getCertificatesFlow(userId: String): Flow<List<CertificateEntity>> =
        certificatesFlow.map { list -> list.filter { it.userId == userId } }
    override suspend fun getCertificateForCourse(userId: String, courseId: String): CertificateEntity? =
        certificatesFlow.value.find { it.userId == userId && it.courseId == courseId }
    override suspend fun insertCertificate(cert: CertificateEntity) {
        val current = certificatesFlow.value.toMutableList()
        current.removeAll { it.certId == cert.certId }
        current.add(cert)
        certificatesFlow.value = current
    }

    override fun getAllBadgesFlow(): Flow<List<BadgeEntity>> = badgesFlow
    override fun getUserBadgesFlow(userId: String): Flow<List<UserBadgeEntity>> =
        userBadgesFlow.map { list -> list.filter { it.userId == userId } }
    override suspend fun insertBadges(badges: List<BadgeEntity>) {
        val current = badgesFlow.value.toMutableList()
        badges.forEach { b -> current.removeAll { it.id == b.id }; current.add(b) }
        badgesFlow.value = current
    }
    override suspend fun unlockBadge(userBadge: UserBadgeEntity) {
        val current = userBadgesFlow.value.toMutableList()
        current.removeAll { it.userId == userBadge.userId && it.badgeId == userBadge.badgeId }
        current.add(userBadge)
        userBadgesFlow.value = current
    }

    override fun getBookmarksFlow(userId: String): Flow<List<BookmarkEntity>> =
        bookmarksFlow.map { list -> list.filter { it.userId == userId } }
    override suspend fun getBookmark(userId: String, itemId: String): BookmarkEntity? =
        bookmarksFlow.value.find { it.userId == userId && it.itemId == itemId }
    override suspend fun insertBookmark(bookmark: BookmarkEntity) {
        val current = bookmarksFlow.value.toMutableList()
        current.removeAll { it.userId == bookmark.userId && it.itemId == bookmark.itemId }
        current.add(bookmark)
        bookmarksFlow.value = current
    }
    override suspend fun deleteBookmark(userId: String, itemId: String) {
        val current = bookmarksFlow.value.toMutableList()
        current.removeAll { it.userId == userId && it.itemId == itemId }
        bookmarksFlow.value = current
    }

    override fun getNotificationsFlow(userId: String): Flow<List<NotificationEntity>> =
        notificationsFlow.map { list -> list.filter { it.userId == userId } }
    override suspend fun insertNotification(notification: NotificationEntity) {
        val current = notificationsFlow.value.toMutableList()
        current.add(0, notification)
        notificationsFlow.value = current
    }
    override suspend fun markAllNotificationsAsRead(userId: String) {
        notificationsFlow.value = notificationsFlow.value.map {
            if (it.userId == userId) it.copy(isRead = true) else it
        }
    }
    override suspend fun markNotificationAsRead(id: String) {
        notificationsFlow.value = notificationsFlow.value.map {
            if (it.id == id) it.copy(isRead = true) else it
        }
    }

    override suspend fun getPlaygroundDraft(userId: String, courseId: String): PlaygroundDraftEntity? =
        playgroundDraftsFlow.value.find { it.userId == userId && it.courseId == courseId }
    override fun getAllPlaygroundDraftsFlow(userId: String): Flow<List<PlaygroundDraftEntity>> =
        playgroundDraftsFlow.map { list -> list.filter { it.userId == userId } }
    override suspend fun savePlaygroundDraft(draft: PlaygroundDraftEntity) {
        val current = playgroundDraftsFlow.value.toMutableList()
        current.removeAll { it.userId == draft.userId && it.courseId == draft.courseId }
        current.add(draft)
        playgroundDraftsFlow.value = current
    }

    override fun getAiChatSessionsFlow(userId: String): Flow<List<AiChatSessionEntity>> =
        aiSessionsFlow.map { list -> list.filter { it.userId == userId } }
    override fun getAiChatMessagesFlow(sessionId: String): Flow<List<AiChatMessageEntity>> =
        aiMessagesFlow.map { list -> list.filter { it.sessionId == sessionId } }
    override suspend fun insertAiChatSession(session: AiChatSessionEntity) {
        val current = aiSessionsFlow.value.toMutableList()
        current.removeAll { it.id == session.id }
        current.add(session)
        aiSessionsFlow.value = current
    }
    override suspend fun insertAiChatMessage(message: AiChatMessageEntity) {
        val current = aiMessagesFlow.value.toMutableList()
        current.add(message)
        aiMessagesFlow.value = current
    }

    override fun getAllForumThreadsFlow(): Flow<List<ForumThreadEntity>> = forumThreadsFlow
    override fun getForumThreadsByCourseFlow(courseId: String): Flow<List<ForumThreadEntity>> =
        forumThreadsFlow.map { list -> list.filter { it.courseId == courseId } }
    override fun getForumThreadByIdFlow(threadId: String): Flow<ForumThreadEntity?> =
        forumThreadsFlow.map { list -> list.find { it.id == threadId } }
    override suspend fun insertForumThread(thread: ForumThreadEntity) {
        val current = forumThreadsFlow.value.toMutableList()
        current.removeAll { it.id == thread.id }
        current.add(0, thread)
        forumThreadsFlow.value = current
    }
    override suspend fun updateForumThread(thread: ForumThreadEntity) {
        val current = forumThreadsFlow.value.toMutableList()
        val idx = current.indexOfFirst { it.id == thread.id }
        if (idx >= 0) { current[idx] = thread; forumThreadsFlow.value = current }
    }
    override fun getForumRepliesFlow(threadId: String): Flow<List<ForumReplyEntity>> =
        forumRepliesFlow.map { list -> list.filter { it.threadId == threadId } }
    override suspend fun insertForumReply(reply: ForumReplyEntity) {
        val current = forumRepliesFlow.value.toMutableList()
        current.add(reply)
        forumRepliesFlow.value = current
    }
    override suspend fun incrementReplyCount(threadId: String) {
        val current = forumThreadsFlow.value.toMutableList()
        val idx = current.indexOfFirst { it.id == threadId }
        if (idx >= 0) {
            val t = current[idx]
            current[idx] = t.copy(repliesCount = t.repliesCount + 1)
            forumThreadsFlow.value = current
        }
    }

    override fun getFriendshipsFlow(userId: String): Flow<List<FriendshipEntity>> =
        friendshipsFlow.map { list -> list.filter { it.requesterId == userId || it.addresseeId == userId } }
    override suspend fun insertFriendship(friendship: FriendshipEntity) {
        val current = friendshipsFlow.value.toMutableList()
        current.add(friendship)
        friendshipsFlow.value = current
    }
    override suspend fun updateFriendshipStatus(rId: String, aId: String, status: String) {
        val current = friendshipsFlow.value.toMutableList()
        val idx = current.indexOfFirst {
            (it.requesterId == rId && it.addresseeId == aId) || (it.requesterId == aId && it.addresseeId == rId)
        }
        if (idx >= 0) {
            current[idx] = current[idx].copy(status = status)
            friendshipsFlow.value = current
        }
    }

    override fun getChatConversationsFlow(): Flow<List<ChatConversationEntity>> = chatConversationsFlow
    override fun getChatConversationById(id: String): Flow<ChatConversationEntity?> =
        chatConversationsFlow.map { list -> list.find { it.id == id } }
    override suspend fun insertChatConversation(conv: ChatConversationEntity) {
        val current = chatConversationsFlow.value.toMutableList()
        current.removeAll { it.id == conv.id }
        current.add(0, conv)
        chatConversationsFlow.value = current
    }
    override fun getChatMessagesFlow(convId: String): Flow<List<ChatMessageEntity>> =
        chatMessagesFlow.map { list -> list.filter { it.conversationId == convId } }
    override suspend fun insertChatMessage(msg: ChatMessageEntity) {
        val current = chatMessagesFlow.value.toMutableList()
        current.add(msg)
        chatMessagesFlow.value = current
    }
    override suspend fun updateLastChatMessage(convId: String, body: String, time: Long) {
        val current = chatConversationsFlow.value.toMutableList()
        val idx = current.indexOfFirst { it.id == convId }
        if (idx >= 0) {
            current[idx] = current[idx].copy(lastMessage = body, lastMessageTime = time)
            chatConversationsFlow.value = current
        }
    }

    override suspend fun getArAssetForModule(moduleId: String): ArAssetEntity? =
        arAssetsFlow.value.find { it.moduleId == moduleId }
    override fun getAllArAssetsFlow(): Flow<List<ArAssetEntity>> = arAssetsFlow
    override suspend fun insertArAssets(assets: List<ArAssetEntity>) {
        val current = arAssetsFlow.value.toMutableList()
        assets.forEach { a -> current.removeAll { it.id == a.id }; current.add(a) }
        arAssetsFlow.value = current
    }
}
