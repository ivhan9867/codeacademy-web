package com.example.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import kotlinx.coroutines.flow.Flow

@Dao
interface UserDao {
    @Query("SELECT * FROM user_profile WHERE accountId = :accountId")
    fun getUserProfile(accountId: Long): Flow<UserProfileEntity?>

    @Query("SELECT * FROM user_profile WHERE accountId = :accountId")
    suspend fun getUserProfileOnce(accountId: Long): UserProfileEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertOrUpdateUser(user: UserProfileEntity)

    @Query("UPDATE user_profile SET totalXp = :newXp, level = :newLevel WHERE accountId = :accountId")
    suspend fun updateXpAndLevel(accountId: Long, newXp: Int, newLevel: Int)

    @Query("UPDATE user_profile SET currentStreak = :streak, longestStreak = :longest, lastActiveDate = :date WHERE accountId = :accountId")
    suspend fun updateStreak(accountId: Long, streak: Int, longest: Int, date: String)

    @Query("UPDATE user_profile SET isDarkMode = :isDark WHERE accountId = :accountId")
    suspend fun setDarkMode(accountId: Long, isDark: Boolean)

    @Query("UPDATE user_profile SET name = :name, bio = :bio WHERE accountId = :accountId")
    suspend fun updateProfileInfo(accountId: Long, name: String, bio: String)

    @Query("UPDATE user_profile SET dailyQuestProgress = :progress, dailyQuestCompleted = :completed WHERE accountId = :accountId")
    suspend fun updateDailyQuest(accountId: Long, progress: Int, completed: Boolean)

    @Query("UPDATE user_profile SET isOnboarded = :isOnboarded, selectedTracks = :selectedTracks WHERE accountId = :accountId")
    suspend fun updateOnboardingStatus(accountId: Long, isOnboarded: Boolean, selectedTracks: String)

    @Query("UPDATE user_profile SET seenCoachMarks = :seenCoachMarks WHERE accountId = :accountId")
    suspend fun updateSeenCoachMarks(accountId: Long, seenCoachMarks: String)

    @Query("UPDATE user_profile SET name = :name, email = :email WHERE accountId = :accountId")
    suspend fun updateAccountProfile(accountId: Long, name: String, email: String)

    @Query("DELETE FROM user_profile WHERE accountId = :accountId")
    suspend fun deleteUserProfile(accountId: Long)
}

@Dao
interface ModuleProgressDao {
    @Query("SELECT * FROM module_progress WHERE accountId = :accountId")
    fun getAllProgress(accountId: Long): Flow<List<ModuleProgressEntity>>

    @Query("SELECT * FROM module_progress WHERE accountId = :accountId AND courseId = :courseId")
    fun getCourseProgress(accountId: Long, courseId: String): Flow<List<ModuleProgressEntity>>

    @Query("SELECT COUNT(*) FROM module_progress WHERE accountId = :accountId AND courseId = :courseId AND isCompleted = 1")
    fun getCompletedModulesCount(accountId: Long, courseId: String): Flow<Int>

    @Query("SELECT COUNT(*) FROM module_progress WHERE accountId = :accountId AND isCompleted = 1")
    fun getTotalCompletedModulesCount(accountId: Long): Flow<Int>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun markModuleCompleted(progress: ModuleProgressEntity)

    @Query("SELECT isCompleted FROM module_progress WHERE accountId = :accountId AND courseId = :courseId AND moduleId = :moduleId")
    suspend fun isModuleCompleted(accountId: Long, courseId: String, moduleId: String): Boolean?

    @Query("DELETE FROM module_progress WHERE accountId = :accountId")
    suspend fun deleteAllForAccount(accountId: Long)
}

@Dao
interface BookmarkDao {
    @Query("SELECT * FROM bookmarks WHERE accountId = :accountId ORDER BY savedAt DESC")
    fun getAllBookmarks(accountId: Long): Flow<List<BookmarkEntity>>

    @Query("SELECT * FROM bookmarks WHERE accountId = :accountId AND type = :type ORDER BY savedAt DESC")
    fun getBookmarksByType(accountId: Long, type: String): Flow<List<BookmarkEntity>>

    @Query("SELECT EXISTS(SELECT 1 FROM bookmarks WHERE accountId = :accountId AND itemId = :itemId)")
    fun isBookmarked(accountId: Long, itemId: String): Flow<Boolean>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertBookmark(bookmark: BookmarkEntity)

    @Query("DELETE FROM bookmarks WHERE accountId = :accountId AND itemId = :itemId")
    suspend fun deleteBookmarkByItemId(accountId: Long, itemId: String)

    @Query("DELETE FROM bookmarks WHERE accountId = :accountId")
    suspend fun deleteAllForAccount(accountId: Long)
}

@Dao
interface CertificateDao {
    @Query("SELECT * FROM certificates WHERE accountId = :accountId ORDER BY issueDate DESC")
    fun getAllCertificates(accountId: Long): Flow<List<CertificateEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCertificate(cert: CertificateEntity)

    @Query("DELETE FROM certificates WHERE accountId = :accountId")
    suspend fun deleteAllForAccount(accountId: Long)
}

@Dao
interface PlaygroundDao {
    @Query("SELECT * FROM playground_drafts WHERE accountId = :accountId AND courseId = :courseId")
    suspend fun getDraft(accountId: Long, courseId: String): PlaygroundDraftEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun saveDraft(draft: PlaygroundDraftEntity)

    @Query("DELETE FROM playground_drafts WHERE accountId = :accountId AND courseId = :courseId")
    suspend fun clearDraft(accountId: Long, courseId: String)

    @Query("DELETE FROM playground_drafts WHERE accountId = :accountId")
    suspend fun deleteAllForAccount(accountId: Long)
}

@Dao
interface NotificationDao {
    @Query("SELECT * FROM notifications WHERE accountId = :accountId ORDER BY timestamp DESC")
    fun getAllNotifications(accountId: Long): Flow<List<NotificationEntity>>

    @Query("SELECT COUNT(*) FROM notifications WHERE accountId = :accountId AND isRead = 0")
    fun getUnreadCount(accountId: Long): Flow<Int>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertNotification(notification: NotificationEntity)

    @Query("UPDATE notifications SET isRead = 1 WHERE accountId = :accountId AND id = :id")
    suspend fun markAsRead(accountId: Long, id: String)

    @Query("UPDATE notifications SET isRead = 1 WHERE accountId = :accountId")
    suspend fun markAllAsRead(accountId: Long)

    @Query("DELETE FROM notifications WHERE accountId = :accountId")
    suspend fun deleteAllForAccount(accountId: Long)
}

@Dao
interface LocalAccountDao {
    @Query("SELECT * FROM local_accounts WHERE email = :email LIMIT 1")
    suspend fun getAccountByEmail(email: String): LocalAccountEntity?

    @Query("SELECT * FROM local_accounts WHERE id = :id LIMIT 1")
    suspend fun getAccountById(id: Long): LocalAccountEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAccount(account: LocalAccountEntity): Long

    @Query("SELECT COUNT(*) FROM local_accounts")
    suspend fun getAccountCount(): Int

    @Query("DELETE FROM local_accounts WHERE id = :id")
    suspend fun deleteAccountById(id: Long)
}

@Dao
interface ForumPostDao {
    @Query("SELECT * FROM forum_posts WHERE courseId = :courseId ORDER BY timestamp DESC")
    fun getPostsByCourse(courseId: String): Flow<List<ForumPostEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertPosts(posts: List<ForumPostEntity>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertPost(post: ForumPostEntity)

    @Query("UPDATE forum_posts SET likeCount = :likeCount, isLikedByMe = :isLiked WHERE postId = :postId")
    suspend fun updateLikeStatus(postId: String, likeCount: Int, isLiked: Boolean)

    @Query("DELETE FROM forum_posts WHERE courseId = :courseId")
    suspend fun clearPostsForCourse(courseId: String)
}

@Dao
interface LeaderboardDao {
    @Query("SELECT * FROM leaderboard_cache WHERE scopeKey = :scopeKey ORDER BY rank ASC")
    fun getCachedLeaderboard(scopeKey: String = "weekly_global"): Flow<List<LeaderboardEntryCacheEntity>>

    @Query("SELECT * FROM leaderboard_cache ORDER BY rank ASC")
    fun getAllCachedLeaderboard(): Flow<List<LeaderboardEntryCacheEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertLeaderboard(entries: List<LeaderboardEntryCacheEntity>)

    @Query("DELETE FROM leaderboard_cache WHERE scopeKey = :scopeKey")
    suspend fun clearLeaderboard(scopeKey: String)

    @Transaction
    suspend fun replaceLeaderboard(scopeKey: String, entries: List<LeaderboardEntryCacheEntity>) {
        clearLeaderboard(scopeKey)
        insertLeaderboard(entries)
    }

    @Query("DELETE FROM leaderboard_cache")
    suspend fun clearAllLeaderboard()
}

@Dao
interface WeeklyChallengeDao {
    @Query("SELECT * FROM weekly_challenges ORDER BY startDate DESC LIMIT 1")
    fun getActiveWeeklyChallenge(): Flow<WeeklyChallengeEntity?>

    @Query("SELECT * FROM weekly_challenges ORDER BY startDate DESC LIMIT 1")
    suspend fun getActiveWeeklyChallengeOnce(): WeeklyChallengeEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertChallenge(challenge: WeeklyChallengeEntity)

    @Query("UPDATE weekly_challenges SET isClaimed = 1 WHERE id = :challengeId")
    suspend fun markClaimed(challengeId: String)
}

@Dao
interface SectionQuizAttemptDao {
    @Query("SELECT * FROM section_quiz_attempts WHERE userId = :userId AND quizId = :quizId AND isPlacementAttempt = 0 ORDER BY timestamp DESC")
    fun getAttempts(userId: String, quizId: String): Flow<List<SectionQuizAttemptEntity>>

    @Query("SELECT * FROM section_quiz_attempts WHERE userId = :userId AND quizId = :quizId AND isPlacementAttempt = 0 ORDER BY timestamp DESC")
    suspend fun getAttemptsOnce(userId: String, quizId: String): List<SectionQuizAttemptEntity>

    @Query("SELECT * FROM section_quiz_attempts WHERE id = :id LIMIT 1")
    suspend fun getAttemptById(id: Long): SectionQuizAttemptEntity?

    @Query("SELECT * FROM section_quiz_attempts WHERE userId = :userId AND quizId = :quizId AND isPlacementAttempt = 0 ORDER BY timestamp DESC LIMIT 1")
    suspend fun getLastAttempt(userId: String, quizId: String): SectionQuizAttemptEntity?

    @Query("SELECT DISTINCT quizId FROM section_quiz_attempts WHERE userId = :userId AND passed = 1")
    fun getPassedQuizIds(userId: String): Flow<List<String>>

    @Query("SELECT DISTINCT quizId FROM section_quiz_attempts WHERE userId = :userId AND passed = 1")
    suspend fun getPassedQuizIdsOnce(userId: String): List<String>

    @Query("SELECT * FROM section_quiz_attempts WHERE userId = :userId AND isPlacementAttempt = 1 ORDER BY timestamp DESC")
    suspend fun getPlacementAttempts(userId: String): List<SectionQuizAttemptEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAttempt(attempt: SectionQuizAttemptEntity): Long
}

@Dao
interface ActiveSectionQuizSessionDao {
    @Query("SELECT * FROM active_section_quiz_sessions WHERE userId = :userId AND quizId = :quizId LIMIT 1")
    suspend fun getActiveSession(userId: String, quizId: String): ActiveSectionQuizSessionEntity?

    @Query("SELECT * FROM active_section_quiz_sessions WHERE userId = :userId AND isTerminated = 1 LIMIT 1")
    suspend fun getTerminatedSession(userId: String): ActiveSectionQuizSessionEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun saveActiveSession(session: ActiveSectionQuizSessionEntity)

    @Query("UPDATE active_section_quiz_sessions SET violationCount = :violationCount, lastViolationTimestamp = :timestamp, isTerminated = :isTerminated, terminationReason = :terminationReason WHERE userId = :userId AND quizId = :quizId")
    suspend fun updateViolation(
        userId: String,
        quizId: String,
        violationCount: Int,
        timestamp: Long,
        isTerminated: Boolean,
        terminationReason: String?
    )

    @Query("UPDATE active_section_quiz_sessions SET answersJson = :answersJson WHERE userId = :userId AND quizId = :quizId")
    suspend fun updateAnswers(userId: String, quizId: String, answersJson: String)

    @Query("DELETE FROM active_section_quiz_sessions WHERE userId = :userId AND quizId = :quizId")
    suspend fun clearActiveSession(userId: String, quizId: String)
}

@Dao
interface ProjectSubmissionDao {
    @Query("SELECT * FROM project_submissions WHERE accountId = :accountId")
    fun getAllSubmissions(accountId: Long): Flow<List<ProjectSubmissionEntity>>

    @Query("SELECT * FROM project_submissions WHERE accountId = :accountId AND courseId = :courseId LIMIT 1")
    fun getSubmission(accountId: Long, courseId: String): Flow<ProjectSubmissionEntity?>

    @Query("SELECT * FROM project_submissions WHERE accountId = :accountId AND courseId = :courseId LIMIT 1")
    suspend fun getSubmissionOnce(accountId: Long, courseId: String): ProjectSubmissionEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsertSubmission(submission: ProjectSubmissionEntity)

    @Query("DELETE FROM project_submissions WHERE accountId = :accountId")
    suspend fun deleteAllForAccount(accountId: Long)
}





