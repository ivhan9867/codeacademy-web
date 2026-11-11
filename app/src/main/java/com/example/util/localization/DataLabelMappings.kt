package com.example.util.localization

import android.content.Context
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import com.example.R
import com.example.data.models.BadgeItem

/**
 * Mappings from raw persistent data values (JSON, database, models) to localized display labels.
 * TAHAP 3B: Separates persistent DATA VALUES from DISPLAY LABELS.
 *
 * Rules:
 * - Data values ("Pemula", "Menengah", "Lanjut", "Orientasi", "Pemula-Menengah", badge IDs, XP levels)
 *   remain unchanged in storage and models.
 * - Unknown values fall back to displaying the raw value as-is.
 */
object DataLabelMappings {

    /**
     * Resolves resource ID for course level.
     * Returns null if the value is unrecognized, ensuring fallback to raw text.
     */
    fun getCourseLevelResId(level: String): Int? = when (level.trim().lowercase()) {
        "semua", "all" -> R.string.course_level_all
        "orientasi", "orientation" -> R.string.course_level_orientation
        "pemula", "beginner" -> R.string.course_level_beginner
        "menengah", "intermediate" -> R.string.course_level_intermediate
        "lanjut", "advanced" -> R.string.course_level_advanced
        "pemula-menengah", "pemulamenengah", "beginner-intermediate" -> R.string.course_level_beginner_intermediate
        else -> null
    }

    /**
     * Localized course level label composable.
     */
    @Composable
    fun courseLevelLabel(level: String): String {
        val resId = getCourseLevelResId(level)
        return if (resId != null) stringResource(resId) else level
    }

    /**
     * Localized course level label for Context / non-composable usage.
     */
    fun courseLevelLabel(level: String, context: Context): String {
        val resId = getCourseLevelResId(level)
        return if (resId != null) context.getString(resId) else level
    }

    /**
     * Resolves resource ID for badge name by badge ID.
     */
    fun getBadgeNameResId(badgeId: String): Int? = when (badgeId) {
        "b1" -> R.string.badge_b1_name
        "b2" -> R.string.badge_b2_name
        "b3" -> R.string.badge_b3_name
        "b4" -> R.string.badge_b4_name
        "b5" -> R.string.badge_b5_name
        "b6" -> R.string.badge_b6_name
        "b7" -> R.string.badge_b7_name
        "b8" -> R.string.badge_b8_name
        "b9" -> R.string.badge_b9_name
        "b10" -> R.string.badge_b10_name
        "b11" -> R.string.badge_b11_name
        "b12" -> R.string.badge_b12_name
        else -> null
    }

    /**
     * Resolves resource ID for badge description by badge ID.
     */
    fun getBadgeDescriptionResId(badgeId: String): Int? = when (badgeId) {
        "b1" -> R.string.badge_b1_desc
        "b2" -> R.string.badge_b2_desc
        "b3" -> R.string.badge_b3_desc
        "b4" -> R.string.badge_b4_desc
        "b5" -> R.string.badge_b5_desc
        "b6" -> R.string.badge_b6_desc
        "b7" -> R.string.badge_b7_desc
        "b8" -> R.string.badge_b8_desc
        "b9" -> R.string.badge_b9_desc
        "b10" -> R.string.badge_b10_desc
        "b11" -> R.string.badge_b11_desc
        "b12" -> R.string.badge_b12_desc
        else -> null
    }

    @Composable
    fun badgeName(badgeId: String, fallback: String): String {
        val resId = getBadgeNameResId(badgeId)
        return if (resId != null) stringResource(resId) else fallback
    }

    @Composable
    fun badgeDescription(badgeId: String, fallback: String): String {
        val resId = getBadgeDescriptionResId(badgeId)
        return if (resId != null) stringResource(resId) else fallback
    }

    fun badgeName(badgeId: String, fallback: String, context: Context): String {
        val resId = getBadgeNameResId(badgeId)
        return if (resId != null) context.getString(resId) else fallback
    }

    fun badgeDescription(badgeId: String, fallback: String, context: Context): String {
        val resId = getBadgeDescriptionResId(badgeId)
        return if (resId != null) context.getString(resId) else fallback
    }

    @Composable
    fun badgeName(badge: BadgeItem): String = badgeName(badge.id, badge.name)

    @Composable
    fun badgeDescription(badge: BadgeItem): String = badgeDescription(badge.id, badge.description)

    fun badgeName(badge: BadgeItem, context: Context): String = badgeName(badge.id, badge.name, context)

    fun badgeDescription(badge: BadgeItem, context: Context): String = badgeDescription(badge.id, badge.description, context)

    @Composable
    fun badgeName(badge: com.example.data.model.BadgeEntity): String = badgeName(badge.id, badge.name)

    @Composable
    fun badgeDescription(badge: com.example.data.model.BadgeEntity): String = badgeDescription(badge.id, badge.description)

    fun badgeName(badge: com.example.data.model.BadgeEntity, context: Context): String = badgeName(badge.id, badge.name, context)

    fun badgeDescription(badge: com.example.data.model.BadgeEntity, context: Context): String = badgeDescription(badge.id, badge.description, context)

    /**
     * Resolves resource ID for Gamification XP Level Title by level integer.
     */
    fun getXpLevelNameResId(level: Int): Int? = when (level) {
        1 -> R.string.xp_level_1_name
        2 -> R.string.xp_level_2_name
        3 -> R.string.xp_level_3_name
        4 -> R.string.xp_level_4_name
        5 -> R.string.xp_level_5_name
        6 -> R.string.xp_level_6_name
        7 -> R.string.xp_level_7_name
        8 -> R.string.xp_level_8_name
        9 -> R.string.xp_level_9_name
        10 -> R.string.xp_level_10_name
        else -> null
    }

    @Composable
    fun xpLevelName(level: Int, fallbackName: String): String {
        val resId = getXpLevelNameResId(level)
        return if (resId != null) stringResource(resId) else fallbackName
    }

    fun xpLevelName(level: Int, fallbackName: String, context: Context): String {
        val resId = getXpLevelNameResId(level)
        return if (resId != null) context.getString(resId) else fallbackName
    }

    /**
     * Resolves resource ID for forum/community learner level tier.
     */
    fun getLevelTierResId(level: Int): Int = when {
        level >= 20 -> R.string.level_tier_master
        level >= 10 -> R.string.level_tier_advanced
        level >= 5 -> R.string.level_tier_learner
        else -> R.string.level_tier_beginner
    }

    @Composable
    fun levelTierLabel(level: Int): String {
        return stringResource(getLevelTierResId(level))
    }

    fun levelTierLabel(level: Int, context: Context): String {
        return context.getString(getLevelTierResId(level))
    }

    /**
     * Resolves resource ID for Daily Quest title and description.
     */
    fun getDailyQuestTitleResId(questId: String): Int? = when (questId) {
        "quest_mod" -> R.string.quest_mod_title
        "quest_quiz" -> R.string.quest_quiz_title
        "quest_ai" -> R.string.quest_ai_title
        "quest_play" -> R.string.quest_play_title
        else -> null
    }

    fun getDailyQuestDescResId(questId: String): Int? = when (questId) {
        "quest_mod" -> R.string.quest_mod_desc
        "quest_quiz" -> R.string.quest_quiz_desc
        "quest_ai" -> R.string.quest_ai_desc
        "quest_play" -> R.string.quest_play_desc
        else -> null
    }

    @Composable
    fun dailyQuestTitle(questId: String, fallback: String): String {
        val resId = getDailyQuestTitleResId(questId)
        return if (resId != null) stringResource(resId) else fallback
    }

    @Composable
    fun dailyQuestDesc(questId: String, fallback: String): String {
        val resId = getDailyQuestDescResId(questId)
        return if (resId != null) stringResource(resId) else fallback
    }

    /**
     * Resolves resource ID for LearningTrack name by trackId.
     */
    fun getTrackNameResId(trackId: String): Int? = when (trackId.lowercase()) {
        "front-end" -> R.string.track_front_end_name
        "back-end" -> R.string.track_back_end_name
        "python" -> R.string.track_python_name
        "c-lang" -> R.string.track_c_lang_name
        "cpp-lang" -> R.string.track_cpp_lang_name
        else -> null
    }

    /**
     * Resolves resource ID for LearningTrack description by trackId.
     */
    fun getTrackDescResId(trackId: String): Int? = when (trackId.lowercase()) {
        "front-end" -> R.string.track_front_end_desc
        "back-end" -> R.string.track_back_end_desc
        "python" -> R.string.track_python_desc
        "c-lang" -> R.string.track_c_lang_desc
        "cpp-lang" -> R.string.track_cpp_lang_desc
        else -> null
    }

    @Composable
    fun trackName(track: com.example.data.models.LearningTrack): String {
        val resId = getTrackNameResId(track.id)
        return if (resId != null) stringResource(resId) else track.name
    }

    fun trackName(track: com.example.data.models.LearningTrack, context: Context): String {
        val resId = getTrackNameResId(track.id)
        return if (resId != null) context.getString(resId) else track.name
    }

    @Composable
    fun trackName(trackId: String, fallback: String): String {
        val resId = getTrackNameResId(trackId)
        return if (resId != null) stringResource(resId) else fallback
    }

    @Composable
    fun trackDescription(track: com.example.data.models.LearningTrack): String {
        val resId = getTrackDescResId(track.id)
        return if (resId != null) stringResource(resId) else track.description
    }

    @Composable
    fun trackDescription(trackId: String, fallback: String): String {
        val resId = getTrackDescResId(trackId)
        return if (resId != null) stringResource(resId) else fallback
    }

    fun trackDescription(track: com.example.data.models.LearningTrack, context: Context): String {
        val resId = getTrackDescResId(track.id)
        return if (resId != null) context.getString(resId) else track.description
    }

    /**
     * Resolves resource ID for Course title by courseId.
     */
    fun getCourseTitleResId(courseId: String): Int? = when (courseId.lowercase()) {
        "fe-web-foundation-00" -> R.string.course_fe_web_foundation_00_title
        "fe-css-02" -> R.string.course_fe_css_02_title
        "fe-js-03" -> R.string.course_fe_js_03_title
        "be-node-01" -> R.string.course_be_node_01_title
        "py-fund-01" -> R.string.course_py_fund_01_title
        "c-fund-01" -> R.string.course_c_fund_01_title
        "cpp-fund-01" -> R.string.course_cpp_fund_01_title
        else -> null
    }

    /**
     * Resolves resource ID for Course description by courseId.
     */
    fun getCourseDescResId(courseId: String): Int? = when (courseId.lowercase()) {
        "fe-web-foundation-00" -> R.string.course_fe_web_foundation_00_desc
        "fe-css-02" -> R.string.course_fe_css_02_desc
        "fe-js-03" -> R.string.course_fe_js_03_desc
        "be-node-01" -> R.string.course_be_node_01_desc
        "py-fund-01" -> R.string.course_py_fund_01_desc
        "c-fund-01" -> R.string.course_c_fund_01_desc
        "cpp-fund-01" -> R.string.course_cpp_fund_01_desc
        else -> null
    }

    @Composable
    fun courseTitle(course: com.example.data.models.Course): String {
        val resId = getCourseTitleResId(course.id)
        return if (resId != null) stringResource(resId) else course.title
    }

    fun courseTitle(course: com.example.data.models.Course, context: Context): String {
        val resId = getCourseTitleResId(course.id)
        return if (resId != null) context.getString(resId) else course.title
    }

    @Composable
    fun courseTitle(courseId: String, fallback: String): String {
        val resId = getCourseTitleResId(courseId)
        return if (resId != null) stringResource(resId) else fallback
    }

    @Composable
    fun courseDescription(course: com.example.data.models.Course): String {
        val resId = getCourseDescResId(course.id)
        return if (resId != null) stringResource(resId) else course.description
    }

    fun courseDescription(course: com.example.data.models.Course, context: Context): String {
        val resId = getCourseDescResId(course.id)
        return if (resId != null) context.getString(resId) else course.description
    }

    @Composable
    fun courseDescription(courseId: String, fallback: String): String {
        val resId = getCourseDescResId(courseId)
        return if (resId != null) stringResource(resId) else fallback
    }

    fun courseDescription(courseId: String, fallback: String, context: Context): String {
        val resId = getCourseDescResId(courseId)
        return if (resId != null) context.getString(resId) else fallback
    }

    fun courseTitle(courseId: String, fallback: String, context: Context): String {
        val resId = getCourseTitleResId(courseId)
        return if (resId != null) context.getString(resId) else fallback
    }

    fun trackName(trackId: String, fallback: String, context: Context): String {
        val resId = getTrackNameResId(trackId)
        return if (resId != null) context.getString(resId) else fallback
    }

    fun trackDescription(trackId: String, fallback: String, context: Context): String {
        val resId = getTrackDescResId(trackId)
        return if (resId != null) context.getString(resId) else fallback
    }

    @Composable
    fun courseTitle(course: com.example.data.model.CourseEntity): String = courseTitle(course.id, course.title)

    fun courseTitle(course: com.example.data.model.CourseEntity, context: Context): String = courseTitle(course.id, course.title, context)

    @Composable
    fun courseDescription(course: com.example.data.model.CourseEntity): String = courseDescription(course.id, course.description)

    fun courseDescription(course: com.example.data.model.CourseEntity, context: Context): String = courseDescription(course.id, course.description, context)

    @Composable
    fun trackName(track: com.example.data.model.TrackEntity): String = trackName(track.id, track.name)

    fun trackName(track: com.example.data.model.TrackEntity, context: Context): String = trackName(track.id, track.name, context)

    @Composable
    fun trackDescription(track: com.example.data.model.TrackEntity): String = trackDescription(track.id, track.description)

    fun trackDescription(track: com.example.data.model.TrackEntity, context: Context): String = trackDescription(track.id, track.description, context)

    @Composable
    fun courseTitle(course: com.example.data.local.model.CourseEntity): String = courseTitle(course.id, course.title)

    fun courseTitle(course: com.example.data.local.model.CourseEntity, context: Context): String = courseTitle(course.id, course.title, context)

    @Composable
    fun courseDescription(course: com.example.data.local.model.CourseEntity): String = courseDescription(course.id, course.description)

    fun courseDescription(course: com.example.data.local.model.CourseEntity, context: Context): String = courseDescription(course.id, course.description, context)

    @Composable
    fun finalProjectTitle(courseId: String, fallback: String): String {
        val lang = LocalAppLanguage.current
        if (lang == AppLanguage.INDONESIAN) return fallback
        return when (courseId.lowercase()) {
            "fe-web-foundation-00" -> "Final Project: Developer Portfolio & Profile Page"
            "fe-css-02" -> "Final Project: Responsive Catalog Dashboard with Flexbox & Grid"
            "fe-js-03" -> "Final Project: Interactive Learning Goal Tracker (DOM & Events)"
            "be-node-01" -> "Final Project: Node.js Mini REST API & Data Service"
            "py-fund-01" -> "Final Project: Python Academic Grade Analytics & Report System"
            "c-fund-01" -> "Final Project: Inventory & Memory Management with Structs and Pointers in C"
            "cpp-fund-01" -> "Final Project: Digital Library System with C++ OOP & STL"
            else -> fallback
        }
    }

    @Composable
    fun finalProjectScenario(courseId: String, fallback: String): String {
        val lang = LocalAppLanguage.current
        if (lang == AppLanguage.INDONESIAN) return fallback
        return when (courseId.lowercase()) {
            "fe-web-foundation-00" -> "Build a single developer profile page displaying: (1) Header with your name and role, (2) 'About Me' and skills list using HTML lists, (3) Project/learning goal cards styled with CSS Box Model (margin, padding, border, background), and (4) Contact footer."
            "fe-css-02" -> "Build a product/course showcase page featuring: (1) A responsive Flexbox navbar (logo on left, menu on right), (2) Concise hero section, and (3) A card grid of at least 3 items using CSS Grid (grid-template-columns) with @media queries for mobile screens."
            "fe-js-03" -> "Create an interactive To-Do / Learning Goal tracker that allows users to: (1) Add new learning targets via text input and button, (2) Validate against empty inputs, and (3) Render the list dynamically from a JavaScript array into the DOM with a completed counter."
            "be-node-01" -> "Write a Node.js server architecture script that includes: (1) A module/function managing a list of books or courses, (2) Asynchronous processing (Promise / async-await) with try/catch error handling, and (3) HTTP server handlers for GET /api/items and POST /api/items plus a 404 JSON fallback."
            "py-fund-01" -> "Create a Python program that processes student scores stored as a list of dictionaries: (1) A function calculating averages and grade predicates (A/B/C/Needs Improvement), (2) A function finding the top-scoring student, and (3) Formatted summary output."
            "c-fund-01" -> "Write a C program defining an Item struct (id, name, stock, price), a function taking a pointer parameter (Item *item) to update stock/values, and a function computing total inventory value from a struct array and printing with printf."
            "cpp-fund-01" -> "Build a C++ program featuring: (1) A base/main class with encapsulated attributes and constructor, (2) A derived class or virtual method displaying specific info, and (3) Object collection management in std::vector with const-reference search/summary functions."
            else -> fallback
        }
    }

    @Composable
    fun rubricTitle(courseId: String, rubricId: String, fallback: String): String {
        val lang = LocalAppLanguage.current
        if (lang == AppLanguage.INDONESIAN) return fallback
        return when ("${courseId.lowercase()}:${rubricId.lowercase()}") {
            "fe-web-foundation-00:r1" -> "Semantic HTML Structure"
            "fe-web-foundation-00:r2" -> "Profile Content & Navigation"
            "fe-web-foundation-00:r3" -> "Basic Styling & CSS Box Model"
            "fe-web-foundation-00:r4" -> "Code Cleanliness & Validity"
            "fe-css-02:r1" -> "Flexbox Navigation & Header"
            "fe-css-02:r2" -> "CSS Grid Card Gallery"
            "fe-css-02:r3" -> "Responsive Design (Media Queries)"
            "fe-css-02:r4" -> "Consistent Spacing & Aesthetics"
            "fe-js-03:r1" -> "Variable Declarations & Data Structures"
            "fe-js-03:r2" -> "Dynamic DOM Manipulation"
            "fe-js-03:r3" -> "Interactive Event Handling"
            "fe-js-03:r4" -> "Input Validation & Function Modularity"
            "be-node-01:r1" -> "CommonJS / ESM Modularization"
            "be-node-01:r2" -> "Async Operations & Data Handling"
            "be-node-01:r3" -> "HTTP Server Routing & JSON Endpoints"
            "be-node-01:r4" -> "Error Handling & Edge Cases"
            "py-fund-01:r1" -> "Variables, Data Types & Conditionals"
            "py-fund-01:r2" -> "Loops & Data Collections (List/Dict)"
            "py-fund-01:r3" -> "Function Modularity (def & return)"
            "py-fund-01:r4" -> "Output Formatting & Input Handling"
            "c-fund-01:r1" -> "Data Types, Arrays & Functions"
            "c-fund-01:r2" -> "Data Modeling with Struct"
            "c-fund-01:r3" -> "Pointers & Memory Addresses"
            "c-fund-01:r4" -> "Memory Safety & printf Formatting"
            "cpp-fund-01:r1" -> "Class Encapsulation & Constructors"
            "cpp-fund-01:r2" -> "STL Usage (std::vector & std::string)"
            "cpp-fund-01:r3" -> "Inheritance & Polymorphism"
            "cpp-fund-01:r4" -> "Modern Resource Management (RAII / References)"
            else -> fallback
        }
    }

    @Composable
    fun rubricDescription(courseId: String, rubricId: String, fallback: String): String {
        val lang = LocalAppLanguage.current
        if (lang == AppLanguage.INDONESIAN) return fallback
        return when ("${courseId.lowercase()}:${rubricId.lowercase()}") {
            "fe-web-foundation-00:r1" -> "Properly uses <!DOCTYPE html>, header, main, section, and footer elements."
            "fe-web-foundation-00:r2" -> "Displays a complete developer profile/portfolio with a skills list and links."
            "fe-web-foundation-00:r3" -> "Applies clean colors, typography, margin, padding, and borders."
            "fe-web-foundation-00:r4" -> "Clean tag nesting and CSS selectors without syntax errors."
            "fe-css-02:r1" -> "Uses display: flex, justify-content, and align-items on the navigation bar."
            "fe-css-02:r2" -> "Uses display: grid and grid-template-columns for the project card layout."
            "fe-css-02:r3" -> "Adapts layout across mobile and desktop viewports using @media."
            "fe-css-02:r4" -> "Uses gap, balanced whitespace, and accessible color contrast."
            "fe-js-03:r1" -> "Uses const/let, arrays, and objects to store application items."
            "fe-js-03:r2" -> "Dynamically renders, adds, or updates HTML elements via JavaScript."
            "fe-js-03:r3" -> "Uses addEventListener to respond to button clicks or user input."
            "fe-js-03:r4" -> "Prevents empty inputs and separates logic into clean functions."
            "be-node-01:r1" -> "Separates utility/data functions and imports them cleanly."
            "be-node-01:r2" -> "Uses Promises or async/await with proper try/catch error handling."
            "be-node-01:r3" -> "Handles GET/POST requests and returns accurate status codes and JSON payloads."
            "be-node-01:r4" -> "Returns structured 404/400 responses when routes or data are invalid."
            "py-fund-01:r1" -> "Uses appropriate data types and if/elif/else logic for business rules."
            "py-fund-01:r2" -> "Manages data collections using lists and dictionaries efficiently."
            "py-fund-01:r3" -> "Divides program into functions with clear parameters and return values."
            "py-fund-01:r4" -> "Prints a well-formatted summary report and handles empty inputs safely."
            "c-fund-01:r1" -> "Declares data types, arrays, and modular functions following C standards."
            "c-fund-01:r2" -> "Uses structs to represent data entities in a structured manner."
            "c-fund-01:r3" -> "Utilizes pointers to access or modify data across functions."
            "c-fund-01:r4" -> "Guards array bounds, frees dynamic memory (if any), and prints clean output."
            "cpp-fund-01:r1" -> "Designs classes with private/protected attributes, constructors, and public methods."
            "cpp-fund-01:r2" -> "Manages dynamic object collections using standard C++ STL containers."
            "cpp-fund-01:r3" -> "Applies derived classes or virtual methods following OOP principles."
            "cpp-fund-01:r4" -> "Uses const references in function parameters and maintains memory safety."
            else -> fallback
        }
    }
}
