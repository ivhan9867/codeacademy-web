package com.example

import com.example.data.models.BadgeItem
import com.example.util.localization.DataLabelMappings
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertNull
import org.junit.Test

/**
 * Unit tests for DataLabelMappings (TAHAP 3B).
 * Verifies that raw persistent data values correctly map to Android string resource IDs,
 * while unrecognized/future values safely fall back to raw data without crashing.
 */
class DataLabelMappingsTest {

    @Test
    fun testCourseLevelMapping() {
        // Known course level data values
        assertEquals(R.string.course_level_orientation, DataLabelMappings.getCourseLevelResId("Orientasi"))
        assertEquals(R.string.course_level_beginner, DataLabelMappings.getCourseLevelResId("Pemula"))
        assertEquals(R.string.course_level_intermediate, DataLabelMappings.getCourseLevelResId("Menengah"))
        assertEquals(R.string.course_level_advanced, DataLabelMappings.getCourseLevelResId("Lanjut"))
        assertEquals(R.string.course_level_beginner_intermediate, DataLabelMappings.getCourseLevelResId("Pemula-Menengah"))
        assertEquals(R.string.course_level_all, DataLabelMappings.getCourseLevelResId("Semua"))

        // Case insensitivity
        assertEquals(R.string.course_level_beginner, DataLabelMappings.getCourseLevelResId("pemula"))
        assertEquals(R.string.course_level_advanced, DataLabelMappings.getCourseLevelResId("LANJUT"))
        assertEquals(R.string.course_level_intermediate, DataLabelMappings.getCourseLevelResId(" Menengah "))

        // English data value aliases
        assertEquals(R.string.course_level_beginner, DataLabelMappings.getCourseLevelResId("beginner"))
        assertEquals(R.string.course_level_intermediate, DataLabelMappings.getCourseLevelResId("intermediate"))
        assertEquals(R.string.course_level_advanced, DataLabelMappings.getCourseLevelResId("advanced"))
        assertEquals(R.string.course_level_orientation, DataLabelMappings.getCourseLevelResId("orientation"))

        // Unrecognized values must return null to trigger fallback
        assertNull(DataLabelMappings.getCourseLevelResId("Expert"))
        assertNull(DataLabelMappings.getCourseLevelResId("UnknownLevel"))
        assertNull(DataLabelMappings.getCourseLevelResId(""))
    }

    @Test
    fun testBadgeNameAndDescriptionMapping() {
        val expectedBadgeNames = mapOf(
            "b1" to R.string.badge_b1_name,
            "b2" to R.string.badge_b2_name,
            "b3" to R.string.badge_b3_name,
            "b4" to R.string.badge_b4_name,
            "b5" to R.string.badge_b5_name,
            "b6" to R.string.badge_b6_name,
            "b7" to R.string.badge_b7_name,
            "b8" to R.string.badge_b8_name,
            "b9" to R.string.badge_b9_name,
            "b10" to R.string.badge_b10_name,
            "b11" to R.string.badge_b11_name,
            "b12" to R.string.badge_b12_name
        )

        val expectedBadgeDescriptions = mapOf(
            "b1" to R.string.badge_b1_desc,
            "b2" to R.string.badge_b2_desc,
            "b3" to R.string.badge_b3_desc,
            "b4" to R.string.badge_b4_desc,
            "b5" to R.string.badge_b5_desc,
            "b6" to R.string.badge_b6_desc,
            "b7" to R.string.badge_b7_desc,
            "b8" to R.string.badge_b8_desc,
            "b9" to R.string.badge_b9_desc,
            "b10" to R.string.badge_b10_desc,
            "b11" to R.string.badge_b11_desc,
            "b12" to R.string.badge_b12_desc
        )

        for ((id, expectedRes) in expectedBadgeNames) {
            assertEquals("Badge $id must map to expected name resId", expectedRes, DataLabelMappings.getBadgeNameResId(id))
        }

        for ((id, expectedRes) in expectedBadgeDescriptions) {
            assertEquals("Badge $id must map to expected description resId", expectedRes, DataLabelMappings.getBadgeDescriptionResId(id))
        }

        // Unknown badge IDs must return null
        assertNull(DataLabelMappings.getBadgeNameResId("b99"))
        assertNull(DataLabelMappings.getBadgeDescriptionResId("b99"))
    }

    @Test
    fun testXpLevelNameMapping() {
        val expectedLevels = mapOf(
            1 to R.string.xp_level_1_name,
            2 to R.string.xp_level_2_name,
            3 to R.string.xp_level_3_name,
            4 to R.string.xp_level_4_name,
            5 to R.string.xp_level_5_name,
            6 to R.string.xp_level_6_name,
            7 to R.string.xp_level_7_name,
            8 to R.string.xp_level_8_name,
            9 to R.string.xp_level_9_name,
            10 to R.string.xp_level_10_name
        )

        for ((level, expectedRes) in expectedLevels) {
            assertEquals("XP Level $level must map to expected name resId", expectedRes, DataLabelMappings.getXpLevelNameResId(level))
        }

        // Unknown levels
        assertNull(DataLabelMappings.getXpLevelNameResId(0))
        assertNull(DataLabelMappings.getXpLevelNameResId(11))
    }

    @Test
    fun testLevelTierMapping() {
        assertEquals(R.string.level_tier_master, DataLabelMappings.getLevelTierResId(25))
        assertEquals(R.string.level_tier_master, DataLabelMappings.getLevelTierResId(20))
        assertEquals(R.string.level_tier_advanced, DataLabelMappings.getLevelTierResId(19))
        assertEquals(R.string.level_tier_advanced, DataLabelMappings.getLevelTierResId(10))
        assertEquals(R.string.level_tier_learner, DataLabelMappings.getLevelTierResId(9))
        assertEquals(R.string.level_tier_learner, DataLabelMappings.getLevelTierResId(5))
        assertEquals(R.string.level_tier_beginner, DataLabelMappings.getLevelTierResId(4))
        assertEquals(R.string.level_tier_beginner, DataLabelMappings.getLevelTierResId(1))
    }

    @Test
    fun testDailyQuestMapping() {
        assertEquals(R.string.quest_mod_title, DataLabelMappings.getDailyQuestTitleResId("quest_mod"))
        assertEquals(R.string.quest_mod_desc, DataLabelMappings.getDailyQuestDescResId("quest_mod"))

        assertEquals(R.string.quest_quiz_title, DataLabelMappings.getDailyQuestTitleResId("quest_quiz"))
        assertEquals(R.string.quest_quiz_desc, DataLabelMappings.getDailyQuestDescResId("quest_quiz"))

        assertEquals(R.string.quest_ai_title, DataLabelMappings.getDailyQuestTitleResId("quest_ai"))
        assertEquals(R.string.quest_ai_desc, DataLabelMappings.getDailyQuestDescResId("quest_ai"))

        assertEquals(R.string.quest_play_title, DataLabelMappings.getDailyQuestTitleResId("quest_play"))
        assertEquals(R.string.quest_play_desc, DataLabelMappings.getDailyQuestDescResId("quest_play"))

        assertNull(DataLabelMappings.getDailyQuestTitleResId("quest_unknown"))
        assertNull(DataLabelMappings.getDailyQuestDescResId("quest_unknown"))
    }

    @Test
    fun testTrackMapping() {
        val tracks = listOf("front-end", "back-end", "python", "c-lang", "cpp-lang")
        for (trackId in tracks) {
            assertNotNull("Track name resId for $trackId must not be null", DataLabelMappings.getTrackNameResId(trackId))
            assertNotNull("Track desc resId for $trackId must not be null", DataLabelMappings.getTrackDescResId(trackId))
        }

        assertNull(DataLabelMappings.getTrackNameResId("unknown-track"))
        assertNull(DataLabelMappings.getTrackDescResId("unknown-track"))
    }

    @Test
    fun testCourseMapping() {
        val courses = listOf(
            "fe-web-foundation-00",
            "fe-css-02",
            "fe-js-03",
            "be-node-01",
            "py-fund-01",
            "c-fund-01",
            "cpp-fund-01"
        )
        for (courseId in courses) {
            assertNotNull("Course title resId for $courseId must not be null", DataLabelMappings.getCourseTitleResId(courseId))
            assertNotNull("Course desc resId for $courseId must not be null", DataLabelMappings.getCourseDescResId(courseId))
        }

        assertNull(DataLabelMappings.getCourseTitleResId("unknown-course"))
        assertNull(DataLabelMappings.getCourseDescResId("unknown-course"))
    }

    @Test
    fun testCourseSectionUtilsLocalizedResIds() {
        assertEquals(
            R.string.section_title_fe_intro_html,
            com.example.data.util.CourseSectionUtils.getSectionTitleResId("fe-web-foundation-00", com.example.data.models.ModuleSection.INTI)
        )
        assertEquals(
            R.string.section_title_fe_why_modern,
            com.example.data.util.CourseSectionUtils.getSectionTitleResId("fe-css-02", com.example.data.models.ModuleSection.PENDAHULUAN)
        )
        assertEquals(
            R.string.section_title_py_flow,
            com.example.data.util.CourseSectionUtils.getSectionTitleResId("py-fund-01", com.example.data.models.ModuleSection.INTI)
        )
        assertEquals(
            R.string.section_title_inti,
            com.example.data.util.CourseSectionUtils.getSectionTitleResId("unknown-course", com.example.data.models.ModuleSection.INTI)
        )
    }
}
