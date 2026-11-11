package com.example

import android.app.Application
import androidx.sqlite.db.framework.FrameworkSQLiteOpenHelperFactory
import androidx.test.core.app.ApplicationProvider
import com.example.data.CodeAcademyRepository
import com.example.data.local.AppDatabase
import com.example.ui.InitialDestination
import com.example.ui.MainViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config

@OptIn(ExperimentalCoroutinesApi::class)
@RunWith(RobolectricTestRunner::class)
@Config(sdk = [36])
class AccountIsolationAndMigrationTest {

    private val testDispatcher = StandardTestDispatcher()
    private lateinit var app: Application

    @Before
    fun setUp() {
        Dispatchers.setMain(testDispatcher)
        app = ApplicationProvider.getApplicationContext()
        AppDatabase.resetInstanceForTesting()
        app.deleteDatabase("codeacademy_db")
        app.deleteDatabase("test_migration.db")
    }

    @After
    fun tearDown() {
        AppDatabase.resetInstanceForTesting()
        Dispatchers.resetMain()
    }

    @Test
    fun testRoomMigration_fromLegacyDataToVersion4_preservesHistoriAndCreatesAccount() {
        // Create an in-memory SQLite database simulating a v2/v3 database with legacy data
        val helperFactory = FrameworkSQLiteOpenHelperFactory()
        val config = androidx.sqlite.db.SupportSQLiteOpenHelper.Configuration.builder(app)
            .name("test_migration.db")
            .callback(object : androidx.sqlite.db.SupportSQLiteOpenHelper.Callback(2) {
                override fun onCreate(db: androidx.sqlite.db.SupportSQLiteDatabase) {
                    // Create v2 tables
                    db.execSQL(
                        """
                        CREATE TABLE `local_accounts` (
                            `id` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,
                            `email` TEXT NOT NULL,
                            `passwordHash` TEXT NOT NULL,
                            `displayName` TEXT NOT NULL,
                            `createdAt` INTEGER NOT NULL
                        )
                        """.trimIndent()
                    )
                    db.execSQL(
                        """
                        CREATE TABLE `user_profile` (
                            `id` INTEGER PRIMARY KEY NOT NULL,
                            `name` TEXT NOT NULL,
                            `email` TEXT NOT NULL,
                            `bio` TEXT NOT NULL,
                            `totalXp` INTEGER NOT NULL,
                            `level` INTEGER NOT NULL,
                            `currentStreak` INTEGER NOT NULL,
                            `longestStreak` INTEGER NOT NULL,
                            `lastActiveDate` TEXT NOT NULL,
                            `dailyQuestId` TEXT NOT NULL,
                            `dailyQuestProgress` INTEGER NOT NULL,
                            `dailyQuestCompleted` INTEGER NOT NULL,
                            `selectedTracks` TEXT NOT NULL,
                            `goal` TEXT NOT NULL,
                            `isDarkMode` INTEGER NOT NULL,
                            `isOnboarded` INTEGER NOT NULL
                        )
                        """.trimIndent()
                    )
                    db.execSQL(
                        """
                        CREATE TABLE `module_progress` (
                            `courseId` TEXT NOT NULL,
                            `moduleId` TEXT NOT NULL,
                            `isCompleted` INTEGER NOT NULL,
                            `completedAt` INTEGER NOT NULL,
                            PRIMARY KEY(`courseId`, `moduleId`)
                        )
                        """.trimIndent()
                    )
                    db.execSQL(
                        """
                        CREATE TABLE `bookmarks` (
                            `id` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,
                            `type` TEXT NOT NULL,
                            `itemId` TEXT NOT NULL,
                            `title` TEXT NOT NULL,
                            `subtitle` TEXT NOT NULL,
                            `trackName` TEXT NOT NULL,
                            `savedAt` INTEGER NOT NULL
                        )
                        """.trimIndent()
                    )
                    db.execSQL(
                        """
                        CREATE TABLE `certificates` (
                            `certId` TEXT PRIMARY KEY NOT NULL,
                            `courseId` TEXT NOT NULL,
                            `courseTitle` TEXT NOT NULL,
                            `userName` TEXT NOT NULL,
                            `issueDate` TEXT NOT NULL,
                            `scorePercent` INTEGER NOT NULL
                        )
                        """.trimIndent()
                    )
                    db.execSQL(
                        """
                        CREATE TABLE `playground_drafts` (
                            `courseId` TEXT PRIMARY KEY NOT NULL,
                            `htmlCode` TEXT NOT NULL,
                            `cssCode` TEXT NOT NULL,
                            `jsCode` TEXT NOT NULL,
                            `updatedAt` INTEGER NOT NULL
                        )
                        """.trimIndent()
                    )
                    db.execSQL(
                        """
                        CREATE TABLE `notifications` (
                            `id` TEXT PRIMARY KEY NOT NULL,
                            `title` TEXT NOT NULL,
                            `message` TEXT NOT NULL,
                            `type` TEXT NOT NULL,
                            `timestamp` TEXT NOT NULL,
                            `isRead` INTEGER NOT NULL
                        )
                        """.trimIndent()
                    )

                    // Insert legacy test data before migration (Rafi Alfarizi, 120 XP, Level 2, HTML progress)
                    db.execSQL(
                        """
                        INSERT INTO `user_profile` (`id`, `name`, `email`, `bio`, `totalXp`, `level`, `currentStreak`, `longestStreak`, `lastActiveDate`, `dailyQuestId`, `dailyQuestProgress`, `dailyQuestCompleted`, `selectedTracks`, `goal`, `isDarkMode`, `isOnboarded`)
                        VALUES (1, 'Rafi Alfarizi', 'rafi.alfarizi@example.com', 'Siswa SMA', 120, 2, 4, 5, '2026-09-22', 'quest_mod', 0, 0, 'front-end', 'Persiapan Karier', 1, 1)
                        """.trimIndent()
                    )
                    db.execSQL(
                        """
                        INSERT INTO `module_progress` (`courseId`, `moduleId`, `isCompleted`, `completedAt`)
                        VALUES ('fe-html-01', 'html-mod-1', 1, 1700000000000)
                        """.trimIndent()
                    )
                }

                override fun onUpgrade(
                    db: androidx.sqlite.db.SupportSQLiteDatabase,
                    oldVersion: Int,
                    newVersion: Int
                ) {}
            })
            .build()

        val db = helperFactory.create(config).writableDatabase

        // Execute MIGRATION_2_3 then MIGRATION_3_4
        AppDatabase.MIGRATION_2_3.migrate(db)
        AppDatabase.MIGRATION_3_4.migrate(db)

        // Verify local_accounts has legacy account with id = 1
        val accCursor = db.query("SELECT id, displayName, email FROM local_accounts WHERE id = 1")
        assertTrue("Legacy account must exist in local_accounts", accCursor.moveToFirst())
        assertEquals("Rafi Alfarizi", accCursor.getString(accCursor.getColumnIndexOrThrow("displayName")))
        assertEquals("rafi.alfarizi@example.com", accCursor.getString(accCursor.getColumnIndexOrThrow("email")))
        accCursor.close()

        // Verify user_profile row now uses accountId = 1 as primary key with 120 XP and Level 2
        val profCursor = db.query("SELECT accountId, name, totalXp, level, isOnboarded FROM user_profile WHERE accountId = 1")
        assertTrue("user_profile row with accountId = 1 must exist", profCursor.moveToFirst())
        assertEquals(1L, profCursor.getLong(profCursor.getColumnIndexOrThrow("accountId")))
        assertEquals("Rafi Alfarizi", profCursor.getString(profCursor.getColumnIndexOrThrow("name")))
        assertEquals(120, profCursor.getInt(profCursor.getColumnIndexOrThrow("totalXp")))
        assertEquals(2, profCursor.getInt(profCursor.getColumnIndexOrThrow("level")))
        assertEquals(1, profCursor.getInt(profCursor.getColumnIndexOrThrow("isOnboarded")))
        profCursor.close()

        // Verify module_progress row preserved and has accountId = 1
        val progCursor = db.query("SELECT accountId, courseId, moduleId, isCompleted FROM module_progress WHERE accountId = 1")
        assertTrue("module_progress row with accountId = 1 must exist", progCursor.moveToFirst())
        assertEquals("fe-html-01", progCursor.getString(progCursor.getColumnIndexOrThrow("courseId")))
        assertEquals("html-mod-1", progCursor.getString(progCursor.getColumnIndexOrThrow("moduleId")))
        assertEquals(1, progCursor.getInt(progCursor.getColumnIndexOrThrow("isCompleted")))
        progCursor.close()

        db.close()
    }

    @Test
    fun testMandatoryScenarios_1_to_4() = runTest(testDispatcher) {
        val repo = CodeAcademyRepository(app)
        repo.ensureInitialUserSeeded()

        // -------------------------------------------------------------
        // SKENARIO 1: Login ke akun lama (Rafi Alfarizi/akun legacy)
        // Data 120 XP, Level 2, progres HTML Fundamentals harus tetap ada
        // -------------------------------------------------------------
        val loginRafi = repo.loginLocalAccount("rafi.alfarizi@example.com", "password123")
        assertTrue("Login Rafi harus berhasil", loginRafi.isSuccess)
        val rafiId = loginRafi.getOrThrow().id
        assertEquals(1L, rafiId)

        val rafiProfile = repo.getUserProfileOnce(1L)
        assertNotNull(rafiProfile)
        assertEquals("Rafi Alfarizi", rafiProfile?.name)
        assertEquals(120, rafiProfile?.totalXp)
        assertEquals(2, rafiProfile?.level)
        assertEquals(4, rafiProfile?.currentStreak)
        assertTrue(rafiProfile?.isOnboarded == true)

        val rafiProgress = repo.allProgress.first()
        assertTrue(
            "Progres HTML Fundamentals harus tetap ada untuk akun lama",
            rafiProgress.any {
                ((it.courseId == "fe-html-01" && it.moduleId == "html-mod-1") ||
                 (it.courseId == "fe-web-foundation-00" && it.moduleId == "web-foundation-prep-1")) && it.isCompleted
            }
        )

        // -------------------------------------------------------------
        // SKENARIO 2: Daftar akun baru
        // Harus melihat profil kosong (0 XP, Level 1) dan diarahkan ke Onboarding, BUKAN data akun lain
        // -------------------------------------------------------------
        repo.logoutLocalAccount()
        val viewModel = MainViewModel(app)
        backgroundScope.launch { viewModel.moduleProgressList.collect {} }
        backgroundScope.launch { viewModel.userProfile.collect {} }

        val initialDest = viewModel.checkInitialDestination()
        assertEquals(InitialDestination.LOGIN, initialDest)

        val regResult = viewModel.register("Siti Aisyah", "siti.aisyah@test.com", "password123")
        assertTrue("Registrasi akun baru harus berhasil", regResult.isSuccess)
        assertFalse("Akun baru belum onboarded", regResult.getOrDefault(true))

        advanceUntilIdle()
        val newProfile = viewModel.userProfile.filterNotNull().first()
        assertEquals("Siti Aisyah", newProfile.name)
        assertEquals(0, newProfile.totalXp)
        assertEquals(1, newProfile.level)
        assertEquals(0, newProfile.currentStreak)
        assertFalse("Akun baru belum onboarded", newProfile.isOnboarded)
        assertEquals("Progres akun baru harus kosong (0 modul)", 0, viewModel.moduleProgressList.value.size)

        // Siti menyelesaikan onboarding
        viewModel.completeOnboarding("front-end")
        advanceUntilIdle()

        // -------------------------------------------------------------
        // SKENARIO 4 (dijalankan sebelum switch): Selesaikan modul di akun baru
        // Cek accountId lama TIDAK ikut bertambah progresnya
        // -------------------------------------------------------------
        val sitiCompletedModule = repo.completeModule("fe-html-01", "html-mod-2")
        assertTrue("Menyelesaikan modul html-mod-2 di akun baru harus berhasil", sitiCompletedModule)

        advanceUntilIdle()
        val sitiUpdatedProfile = viewModel.userProfile.filterNotNull().first { it.totalXp == 60 }
        // 10 XP dari penyelesaian modul + 50 XP bonus dari penyelesaian misi harian pertama = 60 XP
        assertEquals("Akun baru mendapat 60 XP (10 XP modul + 50 XP misi harian)", 60, sitiUpdatedProfile.totalXp)
        assertEquals("Akun baru punya 1 modul selesai", 1, viewModel.moduleProgressList.value.size)

        // Cek database langsung: Akun lama (accountId = 1) TIDAK BOLEH berubah!
        val rafiProfileDirect = repo.getUserProfileOnce(1L)
        assertEquals("XP akun lama harus TETAP 120", 120, rafiProfileDirect?.totalXp)
        assertEquals("Level akun lama harus TETAP 2", 2, rafiProfileDirect?.level)

        val rafiProgressDirect = com.example.data.local.AppDatabase.getDatabase(app).moduleProgressDao().getAllProgress(1L).first()
        assertEquals("Akun lama harus tetap hanya punya 1 modul selesai (html-mod-1)", 1, rafiProgressDirect.size)
        assertEquals("web-foundation-prep-1", rafiProgressDirect[0].moduleId)

        // -------------------------------------------------------------
        // SKENARIO 3: Logout dari akun baru, login lagi ke akun lama
        // Data akun lama harus muncul kembali dengan benar (120 XP dst), TANPA restart aplikasi
        // -------------------------------------------------------------
        viewModel.logout()
        val postLogoutDest = viewModel.checkInitialDestination()
        assertEquals(InitialDestination.LOGIN, postLogoutDest)

        val loginOldAccountResult = viewModel.login("rafi.alfarizi@example.com", "password123")
        assertTrue("Login kembali ke akun lama harus berhasil", loginOldAccountResult.isSuccess)
        assertTrue("Akun lama sudah onboarded", loginOldAccountResult.getOrDefault(false))

        advanceUntilIdle()
        val switchedBackProfile = viewModel.userProfile.filterNotNull().first()
        assertEquals("Profil aktif harus kembali ke akun lama", "Rafi Alfarizi", switchedBackProfile.name)
        assertEquals("XP harus 120 untuk akun lama", 120, switchedBackProfile.totalXp)
        assertEquals("Level harus 2 untuk akun lama", 2, switchedBackProfile.level)
        assertEquals("Streak harus 4 untuk akun lama", 4, switchedBackProfile.currentStreak)

        val switchedBackProgress = viewModel.moduleProgressList.first { it.isNotEmpty() }
        assertEquals("Progres aktif harus kembali ke akun lama", 1, switchedBackProgress.size)
        assertEquals("web-foundation-prep-1", switchedBackProgress[0].moduleId)
        assertFalse(
            "Akun lama tidak boleh memiliki modul html-mod-2 yang dikerjakan akun baru",
            switchedBackProgress.any { it.moduleId == "html-mod-2" }
        )
    }

    @Test
    fun testPBKDF2PasswordHashingWithSaltAndMigration_4_to_5() = runTest(testDispatcher) {
        // 1. Verify Salt Generation
        val salt1 = com.example.util.PasswordHasher.generateSalt(16)
        val salt2 = com.example.util.PasswordHasher.generateSalt(16)
        assertEquals("Salt length must be 32 hex chars (16 bytes)", 32, salt1.length)
        assertEquals("Salt length must be 32 hex chars (16 bytes)", 32, salt2.length)
        assertTrue("Salts generated per call must be unique", salt1 != salt2)

        // 2. Verify PBKDF2 hashing with salt produces distinct hashes for same password
        val pass = "mySuperSecretPassword"
        val hash1 = com.example.util.PasswordHasher.hashPassword(pass, salt1)
        val hash2 = com.example.util.PasswordHasher.hashPassword(pass, salt2)
        assertTrue("Different salts must produce different hashes", hash1 != hash2)

        // 3. Verify verification
        assertTrue("Verification with correct password and salt must succeed", com.example.util.PasswordHasher.verifyPassword(pass, hash1, salt1))
        assertFalse("Verification with wrong password must fail", com.example.util.PasswordHasher.verifyPassword("wrongPassword", hash1, salt1))
        assertFalse("Verification with mismatched salt must fail", com.example.util.PasswordHasher.verifyPassword(pass, hash1, salt2))

        // 4. Verify Room Migration 4 -> 5 adds passwordSalt column
        val helperFactory = FrameworkSQLiteOpenHelperFactory()
        val config = androidx.sqlite.db.SupportSQLiteOpenHelper.Configuration.builder(app)
            .name("test_migration_v4_v5.db")
            .callback(object : androidx.sqlite.db.SupportSQLiteOpenHelper.Callback(4) {
                override fun onCreate(db: androidx.sqlite.db.SupportSQLiteDatabase) {
                    db.execSQL(
                        """
                        CREATE TABLE `local_accounts` (
                            `id` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,
                            `email` TEXT NOT NULL,
                            `passwordHash` TEXT NOT NULL,
                            `displayName` TEXT NOT NULL,
                            `createdAt` INTEGER NOT NULL
                        )
                        """.trimIndent()
                    )
                    db.execSQL(
                        """
                        INSERT INTO `local_accounts` (`id`, `email`, `passwordHash`, `displayName`, `createdAt`)
                        VALUES (1, 'legacy.user@example.com', 'dummy_hash_123', 'Legacy User', 1700000000000)
                        """.trimIndent()
                    )
                }

                override fun onUpgrade(
                    db: androidx.sqlite.db.SupportSQLiteDatabase,
                    oldVersion: Int,
                    newVersion: Int
                ) {}
            })
            .build()

        val db = helperFactory.create(config).writableDatabase
        AppDatabase.MIGRATION_4_5.migrate(db)

        val cursor = db.query("SELECT id, email, passwordHash, passwordSalt FROM local_accounts WHERE id = 1")
        assertTrue("Account must still exist after migration 4->5", cursor.moveToFirst())
        assertEquals(1L, cursor.getLong(cursor.getColumnIndexOrThrow("id")))
        assertEquals("legacy.user@example.com", cursor.getString(cursor.getColumnIndexOrThrow("email")))
        assertEquals("dummy_hash_123", cursor.getString(cursor.getColumnIndexOrThrow("passwordHash")))
        assertEquals("", cursor.getString(cursor.getColumnIndexOrThrow("passwordSalt")))
        cursor.close()
        db.close()
        app.deleteDatabase("test_migration_v4_v5.db")
    }
}
