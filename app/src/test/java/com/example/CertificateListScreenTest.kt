package com.example

import android.app.Application
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performScrollTo
import androidx.test.core.app.ApplicationProvider
import com.example.data.CodeAcademyRepository
import com.example.data.local.AppDatabase
import com.example.data.local.CertificateEntity
import com.example.ui.MainViewModel
import com.example.ui.screens.CertificateListScreen
import com.example.ui.theme.CodeAcademyTheme
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config

@OptIn(ExperimentalCoroutinesApi::class)
@RunWith(RobolectricTestRunner::class)
@Config(sdk = [36], qualifiers = "id")
class CertificateListScreenTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    private val testDispatcher = StandardTestDispatcher()
    private lateinit var app: Application

    @Before
    fun setUp() {
        Dispatchers.setMain(testDispatcher)
        app = ApplicationProvider.getApplicationContext()
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun testCertificateList_emptyStateForNewAccount() = runTest(testDispatcher) {
        val repo = CodeAcademyRepository(app)
        repo.ensureInitialUserSeeded()

        // Register new account with 0 certificates
        repo.logoutLocalAccount()
        val reg = repo.registerLocalAccount("Budi Baru", "budi.cert@test.com", "password123")
        assertTrue(reg.isSuccess)

        val viewModel = MainViewModel(app)

        // Verify active account has 0 certificates
        advanceUntilIdle()
        val activeCerts = viewModel.certificates.value
        assertEquals(0, activeCerts.size)
    }

    @Test
    fun testCertificateListWithCertificates_showsCertificatesInViewModel() = runTest(testDispatcher) {
        val repo = CodeAcademyRepository(app)
        repo.ensureInitialUserSeeded()

        // Register and login account
        repo.logoutLocalAccount()
        val reg = repo.registerLocalAccount("Citra Kartika", "citra.cert@test.com", "password123")
        assertTrue(reg.isSuccess)
        val accountId = reg.getOrThrow().id

        // Insert a certificate for this account in Room
        val certDao = AppDatabase.getDatabase(app).certificateDao()
        val testCert = CertificateEntity(
            accountId = accountId,
            certId = "CA-CERT-2026-FE01-9988",
            courseId = "fe-html-01",
            courseTitle = "HTML Fundamentals",
            userName = "Citra Kartika",
            issueDate = "22 Sep 2026",
            scorePercent = 95
        )
        certDao.insertCertificate(testCert)

        val viewModel = MainViewModel(app)
        advanceUntilIdle()

        val activeCerts = viewModel.certificates.filter { it.isNotEmpty() }.first()
        assertEquals(1, activeCerts.size)
        assertEquals("CA-CERT-2026-FE01-9988", activeCerts[0].certId)
        assertEquals("HTML Fundamentals", activeCerts[0].courseTitle)
    }

    @Test
    fun testCertificateEmptyStateUI() {
        var exploredClicked = false
        var backClicked = false

        composeTestRule.setContent {
            CodeAcademyTheme {
                CertificateListScreen(
                    certificates = emptyList(),
                    onBack = { backClicked = true },
                    onExploreCourses = { exploredClicked = true },
                    onViewCertificate = {}
                )
            }
        }

        // Verify Title and Description in Empty State
        composeTestRule.onNodeWithText("Belum Ada Sertifikat").assertIsDisplayed()
        composeTestRule.onNodeWithText("Selesaikan seluruh modul, lulus kuis akhir (minimal 70%), dan dapatkan persetujuan proyek akhir dari fasilitator untuk menerbitkan sertifikat resmi.").assertIsDisplayed()

        // Verify CTA button "Jelajahi Kelas" is displayed and clickable
        composeTestRule.onNodeWithTag("btn_explore_courses").performScrollTo().assertIsDisplayed()
        composeTestRule.onNodeWithTag("btn_explore_courses").performClick()
        composeTestRule.waitForIdle()
        assertTrue("CTA button must trigger onExploreCourses callback", exploredClicked)
    }

    @Test
    fun testCertificatePopulatedStateUI() {
        var viewedCertId: String? = null
        val dummyCerts = listOf(
            CertificateEntity(
                accountId = 10L,
                certId = "CA-CERT-TEST-001",
                courseId = "fe-html-01",
                courseTitle = "HTML Fundamentals",
                userName = "Andi Wijaya",
                issueDate = "22 Sep 2026",
                scorePercent = 90
            )
        )

        composeTestRule.setContent {
            CodeAcademyTheme {
                CertificateListScreen(
                    certificates = dummyCerts,
                    onBack = {},
                    onExploreCourses = {},
                    onViewCertificate = { viewedCertId = it }
                )
            }
        }

        // Empty state must NOT be displayed
        composeTestRule.onNodeWithTag("empty_certificate_title").assertDoesNotExist()

        // Certificate item card must be displayed with course info
        composeTestRule.onNodeWithText("HTML Fundamentals").assertIsDisplayed()
        composeTestRule.onNodeWithText("90%").assertIsDisplayed()
        composeTestRule.onNodeWithText("22 Sep 2026").assertIsDisplayed()
        composeTestRule.onNodeWithTag("btn_view_certificate_CA-CERT-TEST-001").assertIsDisplayed()

        // Click View Certificate button
        composeTestRule.onNodeWithTag("btn_view_certificate_CA-CERT-TEST-001").performClick()
        composeTestRule.waitForIdle()
        assertEquals("CA-CERT-TEST-001", viewedCertId)
    }
}
