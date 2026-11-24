package com.example

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performTouchInput
import androidx.compose.ui.test.swipeLeft
import com.example.ui.screens.HomeScreenTopCarousel
import com.example.ui.theme.CodeAcademyTheme
import org.junit.Assert.assertTrue
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config

@RunWith(RobolectricTestRunner::class)
@Config(sdk = [36], qualifiers = "id")
class HomeScreenCarouselTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    @Test
    fun testCarouselDisplaysPage0InitialAndHandlesSwipeAndDotNavigation() {
        var startLearningClicked = false
        var recommendationClicked = false

        composeTestRule.setContent {
            CodeAcademyTheme {
                HomeScreenTopCarousel(
                    currentStreak = 5,
                    longestStreak = 12,
                    totalXp = 350,
                    currentLevel = 3,
                    onStartLearning = { startLearningClicked = true },
                    onExploreRecommendation = { recommendationClicked = true }
                )
            }
        }

        // Initially on Page 0: Promo Banner
        composeTestRule.onNodeWithText("MOBILE-FIRST CODING").assertIsDisplayed()
        composeTestRule.onNodeWithText("Belajar Coding Praktis\nSetiap Hari di Smartphone").assertIsDisplayed()
        composeTestRule.onNodeWithTag("btn_hero_start_learning").assertIsDisplayed()

        // Test start learning button click
        composeTestRule.onNodeWithTag("btn_hero_start_learning").performClick()
        composeTestRule.waitForIdle()
        assertTrue("Start learning CTA must be triggered", startLearningClicked)

        // Swipe Left on Pager to navigate to Page 1: XP Card
        composeTestRule.onNodeWithTag("home_top_carousel_pager").performTouchInput {
            swipeLeft()
        }
        composeTestRule.waitForIdle()

        // Verify Page 1 (XP Terkumpul Minggu Ini) is now displayed
        composeTestRule.onNodeWithText("XP TERKUMPUL MINGGU INI").assertIsDisplayed()
        composeTestRule.onNodeWithText("Level 3").assertIsDisplayed()
        composeTestRule.onNodeWithText("Total 350 XP terkumpul akun").assertIsDisplayed()

        // Click Dot 2 to navigate to Page 2 (Rekomendasi Kelas / Tips Belajar Card)
        composeTestRule.onNodeWithTag("carousel_dot_2").performClick()
        composeTestRule.waitForIdle()

        // Verify Page 2 (Rekomendasi Kelas & Tips Belajar) is now displayed without redundancy
        composeTestRule.onNodeWithText("REKOMENDASI BELAJAR").assertIsDisplayed()
        composeTestRule.onNodeWithText("CSS Flexbox & Layout Modern").assertIsDisplayed()
        composeTestRule.onNodeWithTag("btn_carousel_recommended_start").assertIsDisplayed()

        // Test recommendation CTA click
        composeTestRule.onNodeWithTag("btn_carousel_recommended_start").performClick()
        composeTestRule.waitForIdle()
        assertTrue("Recommendation CTA must be triggered", recommendationClicked)

        // Click Dot 0 to navigate back to Page 0 (Promo Banner)
        composeTestRule.onNodeWithTag("carousel_dot_0").performClick()
        composeTestRule.waitForIdle()
        composeTestRule.onNodeWithText("MOBILE-FIRST CODING").assertIsDisplayed()
    }
}
