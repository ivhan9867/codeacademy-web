package com.example.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.UUID

@Entity(tableName = "profiles")
data class ProfileEntity(
    @PrimaryKey val id: String = UUID.randomUUID().toString(),
    val email: String,
    val displayName: String,
    val bio: String = "Siswa Baru di CodeAcademy 🚀",
    val totalXp: Int = 0,
    val level: Int = 1,
    val currentStreak: Int = 0,
    val longestStreak: Int = 0,
    val lastActiveDate: String? = null,
    val selectedTracks: String = "front-end,python",
    val goal: String = "Persiapan Karier",
    val isOnboarded: Boolean = false,
    val createdAt: Long = System.currentTimeMillis()
)

@Entity(tableName = "tracks")
data class TrackEntity(
    @PrimaryKey val id: String,
    val name: String,
    val description: String,
    val icon: String
)

@Entity(tableName = "courses")
data class CourseEntity(
    @PrimaryKey val id: String,
    val trackId: String,
    val title: String,
    val level: String, // 'Pemula', 'Menengah', 'Lanjut'
    val durationMinutes: Int = 0,
    val rating: Float = 0f,
    val studentsCount: Int = 0,
    val instructor: String,
    val description: String,
    val isFeatured: Boolean = false
)

@Entity(tableName = "modules")
data class ModuleEntity(
    @PrimaryKey val id: String,
    val courseId: String,
    val order: Int,
    val title: String,
    val readingMinutes: Int = 5,
    val content: String,
    val codeLanguage: String? = null,
    val codeSnippet: String? = null,
    val starterHtml: String? = null,
    val starterCss: String? = null,
    val starterJs: String? = null,
    val playgroundTask: String? = null,
    val hasArAsset: Boolean = false
)

@Entity(tableName = "quiz_questions")
data class QuizQuestionEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val courseId: String,
    val question: String,
    val optionsJson: String, // Pipe-delimited or comma-separated options: "OptA|OptB|OptC|OptD"
    val correctIndex: Int,
    val explanation: String
) {
    fun getOptionsList(): List<String> = optionsJson.split("|||")
}

@Entity(tableName = "user_progress", primaryKeys = ["userId", "moduleId"])
data class UserProgressEntity(
    val userId: String,
    val courseId: String,
    val moduleId: String,
    val isCompleted: Boolean = true,
    val completedAt: Long = System.currentTimeMillis()
)

@Entity(tableName = "certificates")
data class CertificateEntity(
    @PrimaryKey val certId: String,
    val userId: String,
    val courseId: String,
    val courseTitle: String,
    val studentName: String,
    val issueDate: String,
    val scorePercent: Int
)

@Entity(tableName = "badges")
data class BadgeEntity(
    @PrimaryKey val id: String,
    val name: String,
    val emoji: String,
    val description: String,
    val requiredXp: Int = 0
)

@Entity(tableName = "user_badges", primaryKeys = ["userId", "badgeId"])
data class UserBadgeEntity(
    val userId: String,
    val badgeId: String,
    val unlockedAt: Long = System.currentTimeMillis()
)

@Entity(tableName = "bookmarks")
data class BookmarkEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val userId: String,
    val itemType: String, // 'COURSE' or 'MODULE'
    val itemId: String,
    val savedAt: Long = System.currentTimeMillis()
)

@Entity(tableName = "notifications")
data class NotificationEntity(
    @PrimaryKey val id: String = UUID.randomUUID().toString(),
    val userId: String,
    val title: String,
    val message: String,
    val type: String, // 'STREAK', 'QUEST', 'LEVEL_UP', 'BADGE', 'SYSTEM', 'FORUM', 'CHAT', 'FRIEND'
    val isRead: Boolean = false,
    val targetRoute: String? = null,
    val createdAt: Long = System.currentTimeMillis()
)

@Entity(tableName = "playground_drafts", primaryKeys = ["userId", "courseId"])
data class PlaygroundDraftEntity(
    val userId: String,
    val courseId: String,
    val htmlCode: String = "",
    val cssCode: String = "",
    val jsCode: String = "",
    val updatedAt: Long = System.currentTimeMillis()
)

@Entity(tableName = "ai_chat_sessions")
data class AiChatSessionEntity(
    @PrimaryKey val id: String = UUID.randomUUID().toString(),
    val userId: String,
    val courseId: String? = null,
    val title: String = "Diskusi Kode",
    val createdAt: Long = System.currentTimeMillis()
)

@Entity(tableName = "ai_chat_messages")
data class AiChatMessageEntity(
    @PrimaryKey val id: String = UUID.randomUUID().toString(),
    val sessionId: String,
    val isUser: Boolean,
    val message: String,
    val codeSnippet: String? = null,
    val sentAt: Long = System.currentTimeMillis()
)

@Entity(tableName = "friendships", primaryKeys = ["requesterId", "addresseeId"])
data class FriendshipEntity(
    val requesterId: String,
    val addresseeId: String,
    val friendName: String,
    val friendBio: String,
    val friendXp: Int,
    val friendLevel: Int,
    val status: String, // 'PENDING', 'ACCEPTED', 'REJECTED'
    val createdAt: Long = System.currentTimeMillis()
)

@Entity(tableName = "forum_threads")
data class ForumThreadEntity(
    @PrimaryKey val id: String = UUID.randomUUID().toString(),
    val userId: String,
    val authorName: String,
    val courseId: String? = null,
    val courseTitle: String? = null,
    val title: String,
    val body: String,
    val upvotes: Int = 0,
    val hasUpvoted: Boolean = false,
    val repliesCount: Int = 0,
    val createdAt: Long = System.currentTimeMillis()
)

@Entity(tableName = "forum_replies")
data class ForumReplyEntity(
    @PrimaryKey val id: String = UUID.randomUUID().toString(),
    val threadId: String,
    val userId: String,
    val authorName: String,
    val body: String,
    val createdAt: Long = System.currentTimeMillis()
)

@Entity(tableName = "chat_conversations")
data class ChatConversationEntity(
    @PrimaryKey val id: String = UUID.randomUUID().toString(),
    val participantId: String,
    val participantName: String,
    val participantBio: String,
    val lastMessage: String = "",
    val lastMessageTime: Long = System.currentTimeMillis(),
    val unreadCount: Int = 0
)

@Entity(tableName = "chat_messages")
data class ChatMessageEntity(
    @PrimaryKey val id: String = UUID.randomUUID().toString(),
    val conversationId: String,
    val senderId: String,
    val body: String,
    val sentAt: Long = System.currentTimeMillis(),
    val isRead: Boolean = false
)

@Entity(tableName = "ar_assets")
data class ArAssetEntity(
    @PrimaryKey val id: String,
    val moduleId: String,
    val title: String,
    val assetUrl: String,
    val codeLanguage: String,
    val visualizationType: String, // 'pointer', 'memory', 'flow'
    val description: String
)
