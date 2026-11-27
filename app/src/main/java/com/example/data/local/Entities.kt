package com.example.data.local

import androidx.compose.runtime.Immutable
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

@Immutable
@Entity(
    tableName = "user_profile",
    foreignKeys = [
        ForeignKey(
            entity = LocalAccountEntity::class,
            parentColumns = ["id"],
            childColumns = ["accountId"],
            onDelete = ForeignKey.CASCADE
        )
    ]
)
data class UserProfileEntity(
    @PrimaryKey val accountId: Long,
    val name: String,
    val email: String,
    val bio: String = "Siswa Baru di CodeAcademy 🚀",
    val totalXp: Int = 0,
    val level: Int = 1,
    val currentStreak: Int = 0,
    val longestStreak: Int = 0,
    val lastActiveDate: String = "",
    val dailyQuestId: String = "quest_mod",
    val dailyQuestProgress: Int = 0,
    val dailyQuestCompleted: Boolean = false,
    val selectedTracks: String = "",
    val goal: String = "Persiapan Karier",
    val isDarkMode: Boolean = true,
    val isOnboarded: Boolean = false,
    val seenCoachMarks: String = "",
    val bannerTheme: String = "teal_ocean",
    val avatarStyle: String = "initial",
    val avatarFrame: String = "classic",
    val headlineTitle: String = "WebDev Learner",
    val skillTags: String = "",
    val featuredBadgeId: String = ""
) {
    val id: Long get() = accountId
}

@Immutable
@Entity(
    tableName = "module_progress",
    primaryKeys = ["accountId", "courseId", "moduleId"],
    foreignKeys = [
        ForeignKey(
            entity = LocalAccountEntity::class,
            parentColumns = ["id"],
            childColumns = ["accountId"],
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [Index(value = ["accountId"])]
)
data class ModuleProgressEntity(
    val accountId: Long,
    val courseId: String,
    val moduleId: String,
    val isCompleted: Boolean = true,
    val completedAt: Long = System.currentTimeMillis()
)

@Immutable
@Entity(
    tableName = "bookmarks",
    foreignKeys = [
        ForeignKey(
            entity = LocalAccountEntity::class,
            parentColumns = ["id"],
            childColumns = ["accountId"],
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [
        Index(value = ["accountId"]),
        Index(value = ["accountId", "itemId"], unique = true)
    ]
)
data class BookmarkEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val accountId: Long,
    val type: String, // "COURSE" or "MODULE"
    val itemId: String,
    val title: String,
    val subtitle: String,
    val trackName: String,
    val savedAt: Long = System.currentTimeMillis()
)

@Immutable
@Entity(
    tableName = "certificates",
    primaryKeys = ["accountId", "certId"],
    foreignKeys = [
        ForeignKey(
            entity = LocalAccountEntity::class,
            parentColumns = ["id"],
            childColumns = ["accountId"],
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [Index(value = ["accountId"])]
)
data class CertificateEntity(
    val accountId: Long,
    val certId: String,
    val courseId: String,
    val courseTitle: String,
    val userName: String,
    val issueDate: String,
    val scorePercent: Int
) {
    val id: String get() = certId
}

@Immutable
@Entity(
    tableName = "playground_drafts",
    primaryKeys = ["accountId", "courseId"],
    foreignKeys = [
        ForeignKey(
            entity = LocalAccountEntity::class,
            parentColumns = ["id"],
            childColumns = ["accountId"],
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [Index(value = ["accountId"])]
)
data class PlaygroundDraftEntity(
    val accountId: Long,
    val courseId: String,
    val htmlCode: String,
    val cssCode: String,
    val jsCode: String,
    val updatedAt: Long = System.currentTimeMillis()
)

@Immutable
@Entity(
    tableName = "notifications",
    foreignKeys = [
        ForeignKey(
            entity = LocalAccountEntity::class,
            parentColumns = ["id"],
            childColumns = ["accountId"],
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [Index(value = ["accountId"])]
)
data class NotificationEntity(
    @PrimaryKey val id: String,
    val accountId: Long,
    val title: String,
    val message: String,
    val type: String,
    val timestamp: String,
    val isRead: Boolean = false
)

@Immutable
@Entity(
    tableName = "local_accounts",
    indices = [Index(value = ["email"], unique = true)]
)
data class LocalAccountEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val email: String,
    val passwordHash: String,
    val passwordSalt: String = "",
    val displayName: String,
    val createdAt: Long = System.currentTimeMillis()
)

@Immutable
@Entity(
    tableName = "forum_posts",
    indices = [
        Index(value = ["courseId"]),
        Index(value = ["timestamp"])
    ]
)
data class ForumPostEntity(
    @PrimaryKey val postId: String,
    val courseId: String,
    val authorId: String,
    val authorName: String,
    val content: String,
    val timestamp: Long = System.currentTimeMillis(),
    val likeCount: Int = 0,
    val isLikedByMe: Boolean = false,
    val replyToPostId: String? = null,
    val replyToAuthorName: String? = null,
    val replyToContentSnippet: String? = null,
    val authorLevel: Int = 1,
    val authorIsMentor: Boolean = false,
    val authorAccessRole: String = "",
    val isVerifiedAnswer: Boolean = false,
    val isHelpful: Boolean = false
)

@Immutable
@Entity(
    tableName = "leaderboard_cache",
    primaryKeys = ["scopeKey", "userId"],
    indices = [
        Index(value = ["scopeKey", "rank"]),
        Index(value = ["rank"])
    ]
)
data class LeaderboardEntryCacheEntity(
    val scopeKey: String = "weekly_global",
    val userId: String,
    val userName: String,
    val totalXp: Int,
    val level: Int,
    val rank: Int,
    val cachedAt: Long = System.currentTimeMillis()
)

@Immutable
@Entity(
    tableName = "weekly_challenges",
    indices = [Index(value = ["startDate"])]
)
data class WeeklyChallengeEntity(
    @PrimaryKey val id: String,
    val title: String,
    val description: String,
    val targetCount: Int,
    val rewardXp: Int,
    val type: String = "MODULES_COMPLETED",
    val startDate: Long = System.currentTimeMillis(),
    val endDate: Long = System.currentTimeMillis() + 7 * 86400000L,
    val isClaimed: Boolean = false
)

@Immutable
@Entity(
    tableName = "section_quiz_attempts",
    indices = [
        Index(value = ["userId", "quizId"]),
        Index(value = ["timestamp"])
    ]
)
data class SectionQuizAttemptEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val userId: String,
    val quizId: String,
    val timestamp: Long,
    val scorePercent: Int,
    val passed: Boolean,
    val failStreak: Int,
    val questionIdsJson: String,
    val answersJson: String,
    val isPlacementAttempt: Boolean = false,
    val failReason: String? = null
)

@Immutable
@Entity(
    tableName = "active_section_quiz_sessions",
    indices = [
        Index(value = ["userId", "quizId"], unique = true)
    ]
)
data class ActiveSectionQuizSessionEntity(
    @PrimaryKey val sessionKey: String, // "$userId-$quizId"
    val userId: String,
    val quizId: String,
    val courseId: String,
    val startTime: Long,
    val durationMinutes: Int = 15,
    val violationCount: Int = 0,
    val lastViolationTimestamp: Long = 0L,
    val questionIdsJson: String,
    val answersJson: String = "{}",
    val isTerminated: Boolean = false,
    val terminationReason: String? = null
)

@Immutable
@Entity(
    tableName = "project_submissions",
    primaryKeys = ["accountId", "courseId"],
    foreignKeys = [
        ForeignKey(
            entity = LocalAccountEntity::class,
            parentColumns = ["id"],
            childColumns = ["accountId"],
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [
        Index(value = ["accountId"]),
        Index(value = ["submissionId"])
    ]
)
data class ProjectSubmissionEntity(
    val accountId: Long,
    val courseId: String,
    val submissionId: String,
    val studentName: String,
    val studentEmail: String,
    val courseTitle: String,
    val finalQuizScore: Int = 0,
    val status: String = "DRAFT",
    val workspaceCode: String = "",
    val projectUrl: String = "",
    val studentNotes: String = "",
    val checkedRubricIdsJson: String = "[]",
    val attemptNumber: Int = 1,
    val submittedAt: Long = 0L,
    val reviewedAt: Long? = null,
    val facilitatorName: String? = null,
    val facilitatorFeedback: String? = null,
    val facilitatorScore: Int? = null,
    val rubricScoresJson: String = "{}",
    val studentRating: Int? = null,
    val studentRatingComment: String = "",
    val studentRatingTagsJson: String = "[]",
    val ratedAt: Long? = null
)



