package com.example.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.data.model.*
import com.example.data.model.CertificateEntity
import com.example.data.model.BookmarkEntity
import com.example.data.model.NotificationEntity
import com.example.data.model.PlaygroundDraftEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface CodeAcademyDao {
    // Profile
    @Query("SELECT * FROM profiles LIMIT 1")
    fun getProfileFlow(): Flow<ProfileEntity?>

    @Query("SELECT * FROM profiles LIMIT 1")
    suspend fun getProfile(): ProfileEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertProfile(profile: ProfileEntity)

    @Update
    suspend fun updateProfile(profile: ProfileEntity)

    // Tracks
    @Query("SELECT * FROM tracks")
    fun getAllTracksFlow(): Flow<List<TrackEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTracks(tracks: List<TrackEntity>)

    // Courses
    @Query("SELECT * FROM courses")
    fun getAllCoursesFlow(): Flow<List<CourseEntity>>

    @Query("SELECT * FROM courses WHERE trackId = :trackId")
    fun getCoursesByTrackFlow(trackId: String): Flow<List<CourseEntity>>

    @Query("SELECT * FROM courses WHERE id = :courseId")
    fun getCourseByIdFlow(courseId: String): Flow<CourseEntity?>

    @Query("SELECT * FROM courses WHERE id = :courseId")
    suspend fun getCourseById(courseId: String): CourseEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCourses(courses: List<CourseEntity>)

    // Modules
    @Query("SELECT * FROM modules WHERE courseId = :courseId ORDER BY `order` ASC")
    fun getModulesByCourseFlow(courseId: String): Flow<List<ModuleEntity>>

    @Query("SELECT * FROM modules WHERE courseId = :courseId ORDER BY `order` ASC")
    suspend fun getModulesByCourse(courseId: String): List<ModuleEntity>

    @Query("SELECT * FROM modules WHERE id = :moduleId")
    fun getModuleByIdFlow(moduleId: String): Flow<ModuleEntity?>

    @Query("SELECT * FROM modules WHERE id = :moduleId")
    suspend fun getModuleById(moduleId: String): ModuleEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertModules(modules: List<ModuleEntity>)

    // Quiz Questions
    @Query("SELECT * FROM quiz_questions WHERE courseId = :courseId")
    suspend fun getQuizQuestionsByCourse(courseId: String): List<QuizQuestionEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertQuizQuestions(questions: List<QuizQuestionEntity>)

    // User Progress
    @Query("SELECT * FROM user_progress WHERE userId = :userId")
    fun getUserProgressFlow(userId: String): Flow<List<UserProgressEntity>>

    @Query("SELECT * FROM user_progress WHERE userId = :userId AND courseId = :courseId")
    fun getCourseProgressFlow(userId: String, courseId: String): Flow<List<UserProgressEntity>>

    @Query("SELECT * FROM user_progress WHERE userId = :userId AND courseId = :courseId")
    suspend fun getCourseProgressList(userId: String, courseId: String): List<UserProgressEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertProgress(progress: UserProgressEntity)

    // Certificates
    @Query("SELECT * FROM certificates WHERE userId = :userId ORDER BY issueDate DESC")
    fun getCertificatesFlow(userId: String): Flow<List<CertificateEntity>>

    @Query("SELECT * FROM certificates WHERE userId = :userId AND courseId = :courseId LIMIT 1")
    suspend fun getCertificateForCourse(userId: String, courseId: String): CertificateEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCertificate(cert: CertificateEntity)

    // Badges & User Badges
    @Query("SELECT * FROM badges")
    fun getAllBadgesFlow(): Flow<List<BadgeEntity>>

    @Query("SELECT * FROM user_badges WHERE userId = :userId")
    fun getUserBadgesFlow(userId: String): Flow<List<UserBadgeEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertBadges(badges: List<BadgeEntity>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun unlockBadge(userBadge: UserBadgeEntity)

    // Bookmarks
    @Query("SELECT * FROM bookmarks WHERE userId = :userId ORDER BY savedAt DESC")
    fun getBookmarksFlow(userId: String): Flow<List<BookmarkEntity>>

    @Query("SELECT * FROM bookmarks WHERE userId = :userId AND itemId = :itemId LIMIT 1")
    suspend fun getBookmark(userId: String, itemId: String): BookmarkEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertBookmark(bookmark: BookmarkEntity)

    @Query("DELETE FROM bookmarks WHERE userId = :userId AND itemId = :itemId")
    suspend fun deleteBookmark(userId: String, itemId: String)

    // Notifications
    @Query("SELECT * FROM notifications WHERE userId = :userId ORDER BY createdAt DESC")
    fun getNotificationsFlow(userId: String): Flow<List<NotificationEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertNotification(notification: NotificationEntity)

    @Query("UPDATE notifications SET isRead = 1 WHERE userId = :userId")
    suspend fun markAllNotificationsAsRead(userId: String)

    @Query("UPDATE notifications SET isRead = 1 WHERE id = :id")
    suspend fun markNotificationAsRead(id: String)

    // Playground Drafts
    @Query("SELECT * FROM playground_drafts WHERE userId = :userId AND courseId = :courseId LIMIT 1")
    suspend fun getPlaygroundDraft(userId: String, courseId: String): PlaygroundDraftEntity?

    @Query("SELECT * FROM playground_drafts WHERE userId = :userId ORDER BY updatedAt DESC")
    fun getAllPlaygroundDraftsFlow(userId: String): Flow<List<PlaygroundDraftEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun savePlaygroundDraft(draft: PlaygroundDraftEntity)

    // AI Chat
    @Query("SELECT * FROM ai_chat_sessions WHERE userId = :userId ORDER BY createdAt DESC")
    fun getAiChatSessionsFlow(userId: String): Flow<List<AiChatSessionEntity>>

    @Query("SELECT * FROM ai_chat_messages WHERE sessionId = :sessionId ORDER BY sentAt ASC")
    fun getAiChatMessagesFlow(sessionId: String): Flow<List<AiChatMessageEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAiChatSession(session: AiChatSessionEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAiChatMessage(message: AiChatMessageEntity)

    // Forum
    @Query("SELECT * FROM forum_threads ORDER BY createdAt DESC")
    fun getAllForumThreadsFlow(): Flow<List<ForumThreadEntity>>

    @Query("SELECT * FROM forum_threads WHERE courseId = :courseId ORDER BY createdAt DESC")
    fun getForumThreadsByCourseFlow(courseId: String): Flow<List<ForumThreadEntity>>

    @Query("SELECT * FROM forum_threads WHERE id = :threadId LIMIT 1")
    fun getForumThreadByIdFlow(threadId: String): Flow<ForumThreadEntity?>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertForumThread(thread: ForumThreadEntity)

    @Update
    suspend fun updateForumThread(thread: ForumThreadEntity)

    @Query("SELECT * FROM forum_replies WHERE threadId = :threadId ORDER BY createdAt ASC")
    fun getForumRepliesFlow(threadId: String): Flow<List<ForumReplyEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertForumReply(reply: ForumReplyEntity)

    @Query("UPDATE forum_threads SET repliesCount = repliesCount + 1 WHERE id = :threadId")
    suspend fun incrementReplyCount(threadId: String)

    // Social / Friends
    @Query("SELECT * FROM friendships WHERE requesterId = :userId OR addresseeId = :userId")
    fun getFriendshipsFlow(userId: String): Flow<List<FriendshipEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertFriendship(friendship: FriendshipEntity)

    @Query("UPDATE friendships SET status = :status WHERE (requesterId = :rId AND addresseeId = :aId) OR (requesterId = :aId AND addresseeId = :rId)")
    suspend fun updateFriendshipStatus(rId: String, aId: String, status: String)

    // 1-on-1 Chat
    @Query("SELECT * FROM chat_conversations ORDER BY lastMessageTime DESC")
    fun getChatConversationsFlow(): Flow<List<ChatConversationEntity>>

    @Query("SELECT * FROM chat_conversations WHERE id = :id LIMIT 1")
    fun getChatConversationById(id: String): Flow<ChatConversationEntity?>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertChatConversation(conv: ChatConversationEntity)

    @Query("SELECT * FROM chat_messages WHERE conversationId = :convId ORDER BY sentAt ASC")
    fun getChatMessagesFlow(convId: String): Flow<List<ChatMessageEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertChatMessage(msg: ChatMessageEntity)

    @Query("UPDATE chat_conversations SET lastMessage = :body, lastMessageTime = :time WHERE id = :convId")
    suspend fun updateLastChatMessage(convId: String, body: String, time: Long)

    // AR Assets
    @Query("SELECT * FROM ar_assets WHERE moduleId = :moduleId LIMIT 1")
    suspend fun getArAssetForModule(moduleId: String): ArAssetEntity?

    @Query("SELECT * FROM ar_assets")
    fun getAllArAssetsFlow(): Flow<List<ArAssetEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertArAssets(assets: List<ArAssetEntity>)
}
