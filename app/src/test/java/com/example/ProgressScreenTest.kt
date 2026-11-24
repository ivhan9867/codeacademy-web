package com.example

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.hasScrollAction
import androidx.compose.ui.test.hasTestTag
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performScrollToNode
import com.example.data.local.ModuleProgressEntity
import com.example.data.local.UserProfileEntity
import com.example.ui.screens.ProgressScreen
import com.example.ui.theme.CodeAcademyTheme
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config

@RunWith(RobolectricTestRunner::class)
@Config(sdk = [36], qualifiers = "id")
class ProgressScreenTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    @Test
    fun testProgressScreenDetailSheetAndSegmentedTabs() {
        val userProfile = UserProfileEntity(
            accountId = 1L,
            name = "Test User",
            email = "user@test.com",
            totalXp = 450,
            currentStreak = 7,
            longestStreak = 10,
            lastActiveDate = "2026-09-23"
        )

        val progressList = listOf(
            ModuleProgressEntity(1L, "fe-html-01", "html-mod-1", true, 1000L),
            ModuleProgressEntity(1L, "fe-html-01", "html-mod-2", true, 2000L),
            ModuleProgressEntity(1L, "fe-html-01", "html-mod-3", true, 3000L)
        )

        var clickedCourseId = ""
        var certificatesClicked = false

        composeTestRule.setContent {
            CodeAcademyTheme {
                ProgressScreen(
                    userProfile = userProfile,
                    progressList = progressList,
                    certificates = emptyList(),
                    onViewCertificates = { certificatesClicked = true },
                    onCourseClick = { clickedCourseId = it }
                )
            }
        }

        // Verify summary card
        composeTestRule.onNodeWithText("Progres Belajar").assertIsDisplayed()
        composeTestRule.onNodeWithText("450 XP").assertIsDisplayed()
        composeTestRule.onNodeWithText("7 Hari").assertIsDisplayed()

        // Open Bottom Sheet via Track Progress Entry Card
        composeTestRule.onNode(hasScrollAction()).performScrollToNode(hasTestTag("card_track_progress_entry"))
        composeTestRule.onNodeWithTag("card_track_progress_entry").performClick()
        composeTestRule.waitForIdle()

        // Verify Bottom Sheet Title and Segmented Tabs
        composeTestRule.onNodeWithText("Detail Progres Belajar", useUnmergedTree = true).assertIsDisplayed()
        composeTestRule.onNodeWithTag("segmented_track_tabs", useUnmergedTree = true).assertIsDisplayed()
        composeTestRule.onNodeWithTag("tab_in_progress", useUnmergedTree = true).assertIsDisplayed()
        composeTestRule.onNodeWithTag("tab_completed", useUnmergedTree = true).assertIsDisplayed()
        composeTestRule.onNodeWithTag("tab_all_tracks", useUnmergedTree = true).assertIsDisplayed()

        // Click Tab 2: Semua Jalur
        composeTestRule.onNodeWithTag("tab_all_tracks", useUnmergedTree = true).performClick()
        composeTestRule.waitForIdle()

        // Verify Track Cards in All Tracks
        composeTestRule.onNodeWithText("Front-End Development", useUnmergedTree = true).assertIsDisplayed()

        // Click Tab 1: Selesai
        composeTestRule.onNodeWithTag("tab_completed", useUnmergedTree = true).performClick()
        composeTestRule.waitForIdle()

        // Verify Tab 1 is displayed
        composeTestRule.onNodeWithTag("tab_completed", useUnmergedTree = true).assertIsDisplayed()

        // Close sheet
        composeTestRule.onNodeWithTag("btn_close_track_sheet", useUnmergedTree = true).performClick()
        composeTestRule.waitForIdle()
    }
}
