package com.example.data.models

import androidx.compose.runtime.Immutable

@Immutable
data class LearningTrack(
    val id: String,
    val name: String,
    val description: String,
    val iconEmoji: String,
    val totalCourses: Int
)

@Immutable
data class Course(
    val id: String,
    val trackId: String,
    val title: String,
    val level: String, // "Pemula", "Menengah", "Lanjut"
    val durationMinutes: Int,
    val rating: Float,
    val studentsCount: Int,
    val description: String,
    val prerequisites: List<String>,
    val instructor: String,
    val modules: List<CourseModule>,
    val quizQuestions: List<QuizQuestion>,
    val sectionQuizzes: List<SectionQuiz> = emptyList(),
    val cheatSheetMarkdown: String? = null,
    val sectionTitles: Map<String, String> = emptyMap(),
    val finalProject: FinalProjectSpec? = null
)

enum class ProjectSubmissionStatus {
    LOCKED,
    DRAFT,
    PENDING_REVIEW,
    REVISION_REQUESTED,
    APPROVED;

    companion object {
        fun fromString(value: String?): ProjectSubmissionStatus {
            return entries.firstOrNull { it.name.equals(value?.trim(), ignoreCase = true) } ?: DRAFT
        }
    }
}

@Immutable
data class ProjectRubricItem(
    val id: String,
    val title: String,
    val description: String,
    val weightPercent: Int = 25
)

@Immutable
data class FinalProjectSpec(
    val id: String,
    val courseId: String,
    val title: String,
    val description: String,
    val scenario: String,
    val language: String,
    val starterCode: String = "",
    val minPassingScore: Int = 75,
    val xpReward: Int = 100,
    val rubrics: List<ProjectRubricItem> = emptyList()
)

@Immutable
data class SectionQuiz(
    val id: String,
    val courseId: String,
    val section: ModuleSection,
    val title: String,
    val questionPool: List<QuizQuestion>,
    val questionsPerAttempt: Int = 5,
    val passingPercent: Int = 80,
    val durationMinutes: Int = 15
)

enum class ModuleSection { PERSIAPAN, PENDAHULUAN, INTI, LANJUTAN, EVALUASI, CSS_DASAR, PENDALAMAN_CSS }

@Immutable
data class CourseModule(
    val id: String,
    val courseId: String,
    val order: Int,
    val title: String,
    val readingMinutes: Int,
    val content: String,
    val codeSnippet: String? = null,
    val codeLanguage: String? = null,
    val codeSnippets: Map<String, String>? = null,
    val starterHtml: String = "",
    val starterCss: String = "",
    val starterJs: String = "",
    val playgroundTask: String? = null,
    val section: ModuleSection = ModuleSection.INTI,
    val verificationRules: List<String> = emptyList(),
    val isProjectContinuation: Boolean = false,
    val learningObjectives: List<String> = emptyList()
)

@Immutable
data class QuizQuestion(
    val id: Int,
    val question: String,
    val options: List<String>,
    val correctIndex: Int,
    val explanation: String,
    val sourceModuleId: String? = null
)

@Immutable
data class BadgeItem(
    val id: String,
    val name: String,
    val emoji: String,
    val description: String,
    val isUnlocked: Boolean = false
)

@Immutable
data class DailyQuest(
    val id: String,
    val title: String,
    val description: String,
    val xpReward: Int = 50,
    val target: Int = 1,
    val current: Int = 0,
    val isCompleted: Boolean = false
)

@Immutable
data class Certificate(
    val certId: String,
    val courseId: String,
    val courseTitle: String,
    val userName: String,
    val issueDate: String,
    val scorePercent: Int
)

@Immutable
data class NotificationItem(
    val id: String,
    val title: String,
    val message: String,
    val type: String, // "STREAK", "QUEST", "LEVEL_UP", "BADGE", "SYSTEM"
    val timestamp: String,
    val isRead: Boolean = false
)

@Immutable
data class ChatMessage(
    val id: String,
    val text: String,
    val isUser: Boolean,
    val timestamp: Long = System.currentTimeMillis(),
    val codeSnippet: String? = null,
    val imageUri: String? = null
)

object GamificationLevels {
    @Immutable
    data class LevelThreshold(val level: Int, val name: String, val minXp: Int)

    val levels = listOf(
        LevelThreshold(1, "Code Newbie", 0),
        LevelThreshold(2, "Code Explorer", 100),
        LevelThreshold(3, "Code Learner", 300),
        LevelThreshold(4, "Code Builder", 600),
        LevelThreshold(5, "Code Apprentice", 1000),
        LevelThreshold(6, "Code Crafter", 1500),
        LevelThreshold(7, "Code Developer", 2200),
        LevelThreshold(8, "Code Engineer", 3000),
        LevelThreshold(9, "Code Architect", 4000),
        LevelThreshold(10, "Code Master", 5500)
    )

    fun getLevelForXp(xp: Int): LevelThreshold {
        return levels.lastOrNull { xp >= it.minXp } ?: levels.first()
    }

    fun getNextLevel(currentLevel: Int): LevelThreshold? {
        return levels.firstOrNull { it.level == currentLevel + 1 }
    }

    fun getLevelProgress(xp: Int): Float {
        val current = getLevelForXp(xp)
        val next = getNextLevel(current.level) ?: return 1.0f
        val range = next.minXp - current.minXp
        val currentProgress = xp - current.minXp
        return (currentProgress.toFloat() / range.toFloat()).coerceIn(0f, 1f)
    }
}

@Immutable
data class PlacementTestQuestion(
    val question: QuizQuestion,
    val section: ModuleSection,
    val sectionTitle: String,
    val quizId: String
)

@Immutable
data class PlacementSectionResult(
    val section: ModuleSection,
    val sectionTitle: String,
    val quizId: String,
    val correctCount: Int,
    val totalQuestions: Int,
    val scorePercent: Int,
    val isPassed: Boolean,
    val fastTrackedModuleCount: Int,
    val bonusXpEarned: Int
)

@Immutable
data class PlacementTestResult(
    val courseId: String,
    val courseTitle: String,
    val totalQuestions: Int,
    val totalCorrect: Int,
    val overallScorePercent: Int,
    val totalBonusXpEarned: Int,
    val totalModulesFastTracked: Int,
    val sectionResults: List<PlacementSectionResult>
)

data class SearchResultItem(
    val courseId: String,
    val courseTitle: String,
    val moduleId: String,
    val moduleTitle: String,
    val moduleOrder: Int,
    val section: ModuleSection? = null,
    val matchedInTitle: Boolean = false,
    val matchedInContent: Boolean = false,
    val snippet: String? = null,
    val query: String = ""
)
