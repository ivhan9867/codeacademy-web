package com.example

import android.app.Application
import androidx.test.core.app.ApplicationProvider
import com.example.data.CodeAcademyRepository
import com.example.data.local.AppDatabase
import com.example.ui.InitialDestination
import com.example.ui.MainViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config

@OptIn(ExperimentalCoroutinesApi::class)
@RunWith(RobolectricTestRunner::class)
@Config(sdk = [36])
class AuthFlowIntegrationTest {

    private val testDispatcher = StandardTestDispatcher()
    private lateinit var app: Application

    @Before
    fun setUp() {
        Dispatchers.setMain(testDispatcher)
        app = ApplicationProvider.getApplicationContext()
        AppDatabase.resetInstanceForTesting()
        app.deleteDatabase("codeacademy_db")
    }

    @After
    fun tearDown() {
        AppDatabase.resetInstanceForTesting()
        Dispatchers.resetMain()
    }

    @Test
    fun testScenario1_freshInstallFlow_splashToLoginToOnboardingToHome() = runTest(testDispatcher) {
        val repo = CodeAcademyRepository(app)
        repo.ensureInitialUserSeeded()
        // Reset session to fresh install state
        repo.logoutLocalAccount()

        val viewModel = MainViewModel(app)

        // 1. Splash completes -> initial destination should be Login because isLoggedIn == false
        val initialDest = viewModel.checkInitialDestination()
        assertEquals(InitialDestination.LOGIN, initialDest)

        // 2. Register new account -> should return isOnboarded == false
        val regResult = viewModel.register("Budi Santoso", "budi1@test.com", "password123")
        assertTrue("Register should succeed", regResult.isSuccess)
        assertFalse("New account is not yet onboarded", regResult.getOrDefault(true))

        val isLoggedIn = repo.sessionManager.isLoggedIn.first()
        assertTrue("Session isLoggedIn must be true after register", isLoggedIn)

        // 3. Complete onboarding
        viewModel.completeOnboarding("front-end")
        advanceUntilIdle()

        val profile = repo.getUserProfileOnce(repo.getActiveAccountId())
        assertTrue("isOnboarded must be true after onboarding", profile?.isOnboarded == true)
    }

    @Test
    fun testScenario2_reopenAppWhileLoggedIn_splashDirectlyToHome() = runTest(testDispatcher) {
        val repo = CodeAcademyRepository(app)
        repo.ensureInitialUserSeeded()
        repo.sessionManager.saveSession(1L, "rafi.alfarizi@example.com", "Rafi Alfarizi")

        val viewModel = MainViewModel(app)

        // Splash completes -> should evaluate directly to MAIN
        val initialDest = viewModel.checkInitialDestination()
        assertEquals(InitialDestination.MAIN, initialDest)
    }

    @Test
    fun testScenario3_logoutAndReopenApp_splashDirectlyToLogin_thenLoginToHomeWithoutOnboarding() = runTest(testDispatcher) {
        val repo = CodeAcademyRepository(app)
        repo.ensureInitialUserSeeded()
        // Register local account in Room
        val regResult = repo.registerLocalAccount("Budi Tiga", "budi3@test.com", "password123")
        val accountId = regResult.getOrThrow().id
        val userDao = com.example.data.local.AppDatabase.getDatabase(app).userDao()
        userDao.updateOnboardingStatus(accountId, isOnboarded = true, selectedTracks = "front-end")
        repo.sessionManager.saveSession(accountId, "budi3@test.com", "Budi Tiga")

        val viewModel = MainViewModel(app)

        // Logout
        viewModel.logout()

        val isLoggedIn = repo.sessionManager.isLoggedIn.first()
        assertFalse("isLoggedIn must be false after logout", isLoggedIn)

        // Simulate app reopened: check initial destination
        val newSessionVm = MainViewModel(app)
        val initialDest = newSessionVm.checkInitialDestination()
        assertEquals(InitialDestination.LOGIN, initialDest)

        // Login again -> since already onboarded, must return isOnboarded == true
        val loginResult = newSessionVm.login("budi3@test.com", "password123")
        assertTrue("Login should succeed", loginResult.isSuccess)
        assertTrue("Already onboarded account returns true", loginResult.getOrDefault(false))
    }

    @Test
    fun testScenario4_replayingOnboardingFromProfile_resetsFlagAfterCompletion() = runTest(testDispatcher) {
        val repo = CodeAcademyRepository(app)
        repo.ensureInitialUserSeeded()
        repo.sessionManager.saveSession(1L, "rafi.alfarizi@example.com", "Rafi Alfarizi")

        val viewModel = MainViewModel(app)

        // User clicks "Ulangi Pengenalan Jalur"
        viewModel.startReplayingOnboarding()
        assertTrue(viewModel.isReplayingOnboarding)

        // Finish replaying onboarding
        viewModel.completeOnboarding("python")
        advanceUntilIdle()
        assertFalse("isReplayingOnboarding must be reset", viewModel.isReplayingOnboarding)
    }

    @Test
    fun testScenario5_loggedInDestination_isMain() = runTest(testDispatcher) {
        val repo = CodeAcademyRepository(app)
        repo.ensureInitialUserSeeded()
        repo.sessionManager.saveSession(1L, "rafi.alfarizi@example.com", "Rafi Alfarizi")

        val viewModel = MainViewModel(app)
        val dest = viewModel.checkInitialDestination()
        assertEquals(InitialDestination.MAIN, dest)
    }

    @Test
    fun testScenario6_multiUserIsolation_newAccountIsFresh_andOldAccountDataPreserved() = runTest(testDispatcher) {
        val repo = CodeAcademyRepository(app)
        repo.ensureInitialUserSeeded()

        // 1. Verify old account (Rafi Alfarizi, id=1) has Level 2, 120 XP, 4 days streak, and 1 completed module
        val oldProfile = repo.getUserProfileOnce(1L)
        assertEquals("Rafi Alfarizi", oldProfile?.name)
        assertEquals(2, oldProfile?.level)
        assertEquals(120, oldProfile?.totalXp)
        assertEquals(4, oldProfile?.currentStreak)
        assertTrue(oldProfile?.isOnboarded == true)

        // 2. Register a completely new account (Dewi Lestari)
        repo.logoutLocalAccount()
        val viewModel = MainViewModel(app)
        val regResult = viewModel.register("Dewi Lestari", "dewi6@test.com", "password123")
        assertTrue("Registration must succeed", regResult.isSuccess)
        assertFalse("Dewi is not yet onboarded", regResult.getOrDefault(true))

        // Verify active user profile for Dewi has 0 XP, Level 1, 0 streak, and isOnboarded == false initially
        advanceUntilIdle()
        val dewiProfile = viewModel.userProfile.filterNotNull().first()
        assertEquals("Dewi Lestari", dewiProfile.name)
        assertEquals(1, dewiProfile.level)
        assertEquals(0, dewiProfile.totalXp)
        assertEquals(0, dewiProfile.currentStreak)
        assertEquals(0, viewModel.moduleProgressList.value.size)

        // Dewi completes onboarding
        viewModel.completeOnboarding("front-end")

        // 3. Logout from Dewi
        viewModel.logout()

        // 4. Login back to old account (Rafi Alfarizi)
        val loginResult = viewModel.login("rafi.alfarizi@example.com", "password123")
        assertTrue("Login to old account must succeed", loginResult.isSuccess)
        assertTrue("Rafi is already onboarded", loginResult.getOrDefault(false))

        // 5. Verify old account data is completely preserved!
        advanceUntilIdle()
        val rafiActiveProfile = viewModel.userProfile.filterNotNull().first()
        assertEquals("Rafi Alfarizi", rafiActiveProfile.name)
        assertEquals(2, rafiActiveProfile.level)
        assertEquals(120, rafiActiveProfile.totalXp)
        assertEquals(4, rafiActiveProfile.currentStreak)
    }
}
