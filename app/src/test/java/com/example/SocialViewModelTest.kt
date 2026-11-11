package com.example

import android.app.Application
import androidx.test.core.app.ApplicationProvider
import com.example.data.SocialRepository
import com.example.ui.viewmodel.SocialViewModel
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config

@RunWith(RobolectricTestRunner::class)
@Config(sdk = [34])
class SocialViewModelTest {

    private lateinit var app: Application
    private lateinit var socialViewModel: SocialViewModel

    @Before
    fun setup() {
        app = ApplicationProvider.getApplicationContext()
        socialViewModel = SocialViewModel(app)
    }

    @Test
    fun testLeaderboardFilterState() {
        assertEquals("weekly", socialViewModel.selectedLeaderboardPeriod.value)
        assertEquals("global", socialViewModel.selectedLeaderboardTrack.value)

        socialViewModel.setLeaderboardFilters("monthly", "front-end")
        assertEquals("monthly", socialViewModel.selectedLeaderboardPeriod.value)
        assertEquals("front-end", socialViewModel.selectedLeaderboardTrack.value)
    }

    @Test
    fun testForumHighlightsFlow() {
        assertNotNull(socialViewModel.forumHighlights.value)
        assertEquals(false, socialViewModel.isForumHighlightsLoading.value)
    }

    @Test
    fun testCourseForumPostsFlow() = runBlocking {
        val posts = socialViewModel.getCourseForumPosts("fe-web-foundation-00").first()
        assertNotNull(posts)
    }

    @Test
    fun testWeeklyChallengeInitialState() {
        assertNotNull(socialViewModel.weeklyChallenge)
        assertNotNull(socialViewModel.weeklyChallengeProgress)
    }
}
