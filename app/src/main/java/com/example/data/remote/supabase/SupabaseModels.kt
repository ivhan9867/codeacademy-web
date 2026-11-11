package com.example.data.remote.supabase

import com.example.data.model.*
import org.json.JSONArray
import org.json.JSONObject

// Auth Models
data class SupabaseUser(
    val id: String,
    val email: String,
    val displayName: String = "",
    val createdAt: String = ""
)

data class SupabaseSession(
    val accessToken: String,
    val refreshToken: String? = null,
    val user: SupabaseUser,
    val tokenType: String = "bearer",
    val expiresIn: Long = 3600
)

// Helpers to map JSON <-> Entities

object SupabaseJsonMapper {

    fun parseProfile(json: JSONObject): ProfileEntity {
        return ProfileEntity(
            id = json.optString("id"),
            email = json.optString("email"),
            displayName = json.optString("display_name", json.optString("displayName", "Siswa CodeAcademy")),
            bio = json.optString("bio", "Siswa Baru di CodeAcademy 🚀"),
            totalXp = json.optInt("total_xp", json.optInt("totalXp", 0)),
            level = json.optInt("level", 1),
            currentStreak = json.optInt("current_streak", json.optInt("currentStreak", 0)),
            longestStreak = json.optInt("longest_streak", json.optInt("longestStreak", 0)),
            lastActiveDate = if (json.isNull("last_active_date")) null else json.optString("last_active_date", null),
            selectedTracks = json.optString("selected_tracks", json.optString("selectedTracks", "front-end,python")),
            goal = json.optString("goal", "Persiapan Karier"),
            isOnboarded = json.optBoolean("is_onboarded", json.optBoolean("isOnboarded", false)),
            createdAt = json.optLong("created_at", json.optLong("createdAt", System.currentTimeMillis()))
        )
    }

    fun profileToJson(profile: ProfileEntity): JSONObject {
        return JSONObject().apply {
            put("id", profile.id)
            put("email", profile.email)
            put("display_name", profile.displayName)
            put("bio", profile.bio)
            put("total_xp", profile.totalXp)
            put("level", profile.level)
            put("current_streak", profile.currentStreak)
            put("longest_streak", profile.longestStreak)
            put("last_active_date", profile.lastActiveDate)
            put("selected_tracks", profile.selectedTracks)
            put("goal", profile.goal)
            put("is_onboarded", profile.isOnboarded)
            put("created_at", profile.createdAt)
        }
    }

    fun parseTrack(json: JSONObject): TrackEntity {
        return TrackEntity(
            id = json.optString("id"),
            name = json.optString("name"),
            description = json.optString("description"),
            icon = json.optString("icon", "💻")
        )
    }

    fun trackToJson(track: TrackEntity, orderIndex: Int = 0): JSONObject {
        return JSONObject().apply {
            put("id", track.id)
            put("name", track.name)
            put("description", track.description)
            put("icon", track.icon)
            put("order_index", orderIndex)
        }
    }

    fun parseCourse(json: JSONObject): CourseEntity {
        return CourseEntity(
            id = json.optString("id"),
            trackId = json.optString("track_id", json.optString("trackId")),
            title = json.optString("title"),
            level = json.optString("level", "Pemula"),
            durationMinutes = json.optInt("duration_minutes", json.optInt("durationMinutes", 0)),
            rating = json.optDouble("rating", 0.0).toFloat(),
            studentsCount = json.optInt("students_count", json.optInt("studentsCount", 0)),
            instructor = json.optString("instructor", "CodeAcademy Lead"),
            description = json.optString("description"),
            isFeatured = json.optBoolean("is_featured", json.optBoolean("isFeatured", false))
        )
    }

    fun courseToJson(course: CourseEntity, orderIndex: Int = 0): JSONObject {
        return JSONObject().apply {
            put("id", course.id)
            put("track_id", course.trackId)
            put("title", course.title)
            put("level", course.level)
            put("duration_minutes", course.durationMinutes)
            put("rating", course.rating)
            put("students_count", course.studentsCount)
            put("instructor", course.instructor)
            put("description", course.description)
            put("is_featured", course.isFeatured)
            put("order_index", orderIndex)
        }
    }

    fun parseModule(json: JSONObject): ModuleEntity {
        return ModuleEntity(
            id = json.optString("id"),
            courseId = json.optString("course_id", json.optString("courseId")),
            order = json.optInt("order", 1),
            title = json.optString("title"),
            readingMinutes = json.optInt("reading_minutes", json.optInt("readingMinutes", 5)),
            content = json.optString("content"),
            codeLanguage = if (json.isNull("code_language")) null else json.optString("code_language", null),
            codeSnippet = if (json.isNull("code_snippet")) null else json.optString("code_snippet", null),
            starterHtml = if (json.isNull("starter_html")) null else json.optString("starter_html", null),
            starterCss = if (json.isNull("starter_css")) null else json.optString("starter_css", null),
            starterJs = if (json.isNull("starter_js")) null else json.optString("starter_js", null),
            playgroundTask = if (json.isNull("playground_task")) null else json.optString("playground_task", null),
            hasArAsset = json.optBoolean("has_ar_asset", json.optBoolean("hasArAsset", false))
        )
    }

    fun moduleToJson(module: ModuleEntity): JSONObject {
        return JSONObject().apply {
            put("id", module.id)
            put("course_id", module.courseId)
            put("order", module.order)
            put("title", module.title)
            put("reading_minutes", module.readingMinutes)
            put("content", module.content)
            put("code_language", module.codeLanguage)
            put("code_snippet", module.codeSnippet)
            put("starter_html", module.starterHtml)
            put("starter_css", module.starterCss)
            put("starter_js", module.starterJs)
            put("playground_task", module.playgroundTask)
            put("has_ar_asset", module.hasArAsset)
        }
    }

    fun parseQuizQuestion(json: JSONObject): QuizQuestionEntity {
        return QuizQuestionEntity(
            id = json.optInt("id", 0),
            courseId = json.optString("course_id", json.optString("courseId")),
            question = json.optString("question"),
            optionsJson = json.optString("options_json", json.optString("optionsJson")),
            correctIndex = json.optInt("correct_index", json.optInt("correctIndex", 0)),
            explanation = json.optString("explanation")
        )
    }

    fun quizQuestionToJson(quiz: QuizQuestionEntity): JSONObject {
        return JSONObject().apply {
            if (quiz.id > 0) put("id", quiz.id)
            put("course_id", quiz.courseId)
            put("question", quiz.question)
            put("options_json", quiz.optionsJson)
            put("correct_index", quiz.correctIndex)
            put("explanation", quiz.explanation)
        }
    }

    fun parseBadge(json: JSONObject): BadgeEntity {
        return BadgeEntity(
            id = json.optString("id"),
            name = json.optString("name"),
            emoji = json.optString("emoji", "🏆"),
            description = json.optString("description"),
            requiredXp = json.optInt("required_xp", json.optInt("requiredXp", 0))
        )
    }

    fun badgeToJson(badge: BadgeEntity): JSONObject {
        return JSONObject().apply {
            put("id", badge.id)
            put("name", badge.name)
            put("emoji", badge.emoji)
            put("description", badge.description)
            put("required_xp", badge.requiredXp)
        }
    }

    fun parseUserProgress(json: JSONObject): UserProgressEntity {
        return UserProgressEntity(
            userId = json.optString("user_id", json.optString("userId")),
            courseId = json.optString("course_id", json.optString("courseId")),
            moduleId = json.optString("module_id", json.optString("moduleId")),
            isCompleted = json.optBoolean("is_completed", json.optBoolean("isCompleted", true)),
            completedAt = json.optLong("completed_at", json.optLong("completedAt", System.currentTimeMillis()))
        )
    }

    fun userProgressToJson(progress: UserProgressEntity): JSONObject {
        return JSONObject().apply {
            put("user_id", progress.userId)
            put("course_id", progress.courseId)
            put("module_id", progress.moduleId)
            put("is_completed", progress.isCompleted)
            put("completed_at", progress.completedAt)
        }
    }

    fun parseUserBadge(json: JSONObject): UserBadgeEntity {
        return UserBadgeEntity(
            userId = json.optString("user_id", json.optString("userId")),
            badgeId = json.optString("badge_id", json.optString("badgeId")),
            unlockedAt = json.optLong("unlocked_at", json.optLong("unlockedAt", System.currentTimeMillis()))
        )
    }

    fun userBadgeToJson(userBadge: UserBadgeEntity): JSONObject {
        return JSONObject().apply {
            put("user_id", userBadge.userId)
            put("badge_id", userBadge.badgeId)
            put("unlocked_at", userBadge.unlockedAt)
        }
    }

    fun parseCertificate(json: JSONObject): CertificateEntity {
        return CertificateEntity(
            certId = json.optString("cert_id", json.optString("certId")),
            userId = json.optString("user_id", json.optString("userId")),
            courseId = json.optString("course_id", json.optString("courseId")),
            courseTitle = json.optString("course_title", json.optString("courseTitle")),
            studentName = json.optString("student_name", json.optString("studentName")),
            issueDate = json.optString("issue_date", json.optString("issueDate")),
            scorePercent = json.optInt("score_percent", json.optInt("scorePercent", 100))
        )
    }

    fun certificateToJson(cert: CertificateEntity): JSONObject {
        return JSONObject().apply {
            put("cert_id", cert.certId)
            put("user_id", cert.userId)
            put("course_id", cert.courseId)
            put("course_title", cert.courseTitle)
            put("student_name", cert.studentName)
            put("issue_date", cert.issueDate)
            put("score_percent", cert.scorePercent)
        }
    }

    fun parseBookmark(json: JSONObject): BookmarkEntity {
        return BookmarkEntity(
            id = json.optLong("id", 0),
            userId = json.optString("user_id", json.optString("userId")),
            itemType = json.optString("item_type", json.optString("itemType", "COURSE")),
            itemId = json.optString("item_id", json.optString("itemId")),
            savedAt = json.optLong("saved_at", json.optLong("savedAt", System.currentTimeMillis()))
        )
    }

    fun bookmarkToJson(bookmark: BookmarkEntity): JSONObject {
        return JSONObject().apply {
            if (bookmark.id > 0) put("id", bookmark.id)
            put("user_id", bookmark.userId)
            put("item_type", bookmark.itemType)
            put("item_id", bookmark.itemId)
            put("saved_at", bookmark.savedAt)
        }
    }

    fun parseNotification(json: JSONObject): NotificationEntity {
        return NotificationEntity(
            id = json.optString("id"),
            userId = json.optString("user_id", json.optString("userId")),
            title = json.optString("title"),
            message = json.optString("message"),
            type = json.optString("type", "SYSTEM"),
            isRead = json.optBoolean("is_read", json.optBoolean("isRead", false)),
            targetRoute = if (json.isNull("target_route")) null else json.optString("target_route", null),
            createdAt = json.optLong("created_at", json.optLong("createdAt", System.currentTimeMillis()))
        )
    }

    fun notificationToJson(notif: NotificationEntity): JSONObject {
        return JSONObject().apply {
            put("id", notif.id)
            put("user_id", notif.userId)
            put("title", notif.title)
            put("message", notif.message)
            put("type", notif.type)
            put("is_read", notif.isRead)
            put("target_route", notif.targetRoute)
            put("created_at", notif.createdAt)
        }
    }

    fun parsePlaygroundDraft(json: JSONObject): PlaygroundDraftEntity {
        return PlaygroundDraftEntity(
            userId = json.optString("user_id", json.optString("userId")),
            courseId = json.optString("course_id", json.optString("courseId")),
            htmlCode = json.optString("html_code", json.optString("htmlCode", "")),
            cssCode = json.optString("css_code", json.optString("cssCode", "")),
            jsCode = json.optString("js_code", json.optString("jsCode", "")),
            updatedAt = json.optLong("updated_at", json.optLong("updatedAt", System.currentTimeMillis()))
        )
    }

    fun playgroundDraftToJson(draft: PlaygroundDraftEntity): JSONObject {
        return JSONObject().apply {
            put("user_id", draft.userId)
            put("course_id", draft.courseId)
            put("html_code", draft.htmlCode)
            put("css_code", draft.cssCode)
            put("js_code", draft.jsCode)
            put("updated_at", draft.updatedAt)
        }
    }

    fun parseForumThread(json: JSONObject, currentUserId: String? = null): ForumThreadEntity {
        return ForumThreadEntity(
            id = json.optString("id"),
            userId = json.optString("user_id", json.optString("userId")),
            authorName = json.optString("author_name", json.optString("authorName", "Siswa")),
            courseId = if (json.isNull("course_id")) null else json.optString("course_id", null),
            courseTitle = if (json.isNull("course_title")) null else json.optString("course_title", null),
            title = json.optString("title"),
            body = json.optString("body"),
            upvotes = json.optInt("upvotes", 0),
            hasUpvoted = json.optBoolean("has_upvoted", false),
            repliesCount = json.optInt("replies_count", json.optInt("repliesCount", 0)),
            createdAt = json.optLong("created_at", json.optLong("createdAt", System.currentTimeMillis()))
        )
    }

    fun forumThreadToJson(thread: ForumThreadEntity): JSONObject {
        return JSONObject().apply {
            put("id", thread.id)
            put("user_id", thread.userId)
            put("author_name", thread.authorName)
            put("course_id", thread.courseId)
            put("course_title", thread.courseTitle)
            put("title", thread.title)
            put("body", thread.body)
            put("upvotes", thread.upvotes)
            put("replies_count", thread.repliesCount)
            put("created_at", thread.createdAt)
        }
    }

    fun parseForumReply(json: JSONObject): ForumReplyEntity {
        return ForumReplyEntity(
            id = json.optString("id"),
            threadId = json.optString("thread_id", json.optString("threadId")),
            userId = json.optString("user_id", json.optString("userId")),
            authorName = json.optString("author_name", json.optString("authorName", "Siswa")),
            body = json.optString("body"),
            createdAt = json.optLong("created_at", json.optLong("createdAt", System.currentTimeMillis()))
        )
    }

    fun forumReplyToJson(reply: ForumReplyEntity): JSONObject {
        return JSONObject().apply {
            put("id", reply.id)
            put("thread_id", reply.threadId)
            put("user_id", reply.userId)
            put("author_name", reply.authorName)
            put("body", reply.body)
            put("created_at", reply.createdAt)
        }
    }

    fun parseFriendship(json: JSONObject): FriendshipEntity {
        return FriendshipEntity(
            requesterId = json.optString("requester_id", json.optString("requesterId")),
            addresseeId = json.optString("addressee_id", json.optString("addresseeId")),
            friendName = json.optString("friend_name", json.optString("friendName", "Teman Siswa")),
            friendBio = json.optString("friend_bio", json.optString("friendBio", "Siswa Pembelajar di CodeAcademy 🚀")),
            friendXp = json.optInt("friend_xp", json.optInt("friendXp", 0)),
            friendLevel = json.optInt("friend_level", json.optInt("friendLevel", 1)),
            status = json.optString("status", "PENDING"),
            createdAt = json.optLong("created_at", json.optLong("createdAt", System.currentTimeMillis()))
        )
    }

    fun friendshipToJson(friendship: FriendshipEntity): JSONObject {
        return JSONObject().apply {
            put("requester_id", friendship.requesterId)
            put("addressee_id", friendship.addresseeId)
            put("friend_name", friendship.friendName)
            put("friend_bio", friendship.friendBio)
            put("friend_xp", friendship.friendXp)
            put("friend_level", friendship.friendLevel)
            put("status", friendship.status)
            put("created_at", friendship.createdAt)
        }
    }

    fun parseChatConversation(json: JSONObject): ChatConversationEntity {
        return ChatConversationEntity(
            id = json.optString("id"),
            participantId = json.optString("participant_id", json.optString("participantId")),
            participantName = json.optString("participant_name", json.optString("participantName", "Teman")),
            participantBio = json.optString("participant_bio", json.optString("participantBio", "")),
            lastMessage = json.optString("last_message", json.optString("lastMessage", "")),
            lastMessageTime = json.optLong("last_message_time", json.optLong("lastMessageTime", System.currentTimeMillis())),
            unreadCount = json.optInt("unread_count", json.optInt("unreadCount", 0))
        )
    }

    fun chatConversationToJson(conv: ChatConversationEntity): JSONObject {
        return JSONObject().apply {
            put("id", conv.id)
            put("participant_id", conv.participantId)
            put("participant_name", conv.participantName)
            put("participant_bio", conv.participantBio)
            put("last_message", conv.lastMessage)
            put("last_message_time", conv.lastMessageTime)
            put("unread_count", conv.unreadCount)
        }
    }

    fun parseChatMessage(json: JSONObject): ChatMessageEntity {
        return ChatMessageEntity(
            id = json.optString("id"),
            conversationId = json.optString("conversation_id", json.optString("conversationId")),
            senderId = json.optString("sender_id", json.optString("senderId")),
            body = json.optString("body"),
            sentAt = json.optLong("sent_at", json.optLong("sentAt", System.currentTimeMillis())),
            isRead = json.optBoolean("is_read", json.optBoolean("isRead", false))
        )
    }

    fun chatMessageToJson(msg: ChatMessageEntity): JSONObject {
        return JSONObject().apply {
            put("id", msg.id)
            put("conversation_id", msg.conversationId)
            put("sender_id", msg.senderId)
            put("body", msg.body)
            put("sent_at", msg.sentAt)
            put("is_read", msg.isRead)
        }
    }

    fun parseArAsset(json: JSONObject): ArAssetEntity {
        return ArAssetEntity(
            id = json.optString("id"),
            moduleId = json.optString("module_id", json.optString("moduleId")),
            title = json.optString("title"),
            assetUrl = json.optString("asset_url", json.optString("assetUrl")),
            codeLanguage = json.optString("code_language", json.optString("codeLanguage", "c")),
            visualizationType = json.optString("visualization_type", json.optString("visualizationType", "pointer")),
            description = json.optString("description")
        )
    }

    fun arAssetToJson(ar: ArAssetEntity): JSONObject {
        return JSONObject().apply {
            put("id", ar.id)
            put("module_id", ar.moduleId)
            put("title", ar.title)
            put("asset_url", ar.assetUrl)
            put("code_language", ar.codeLanguage)
            put("visualization_type", ar.visualizationType)
            put("description", ar.description)
        }
    }
}
