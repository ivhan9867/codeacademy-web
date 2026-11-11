package com.example.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase

@Database(
    entities = [
        UserProfileEntity::class,
        ModuleProgressEntity::class,
        BookmarkEntity::class,
        CertificateEntity::class,
        PlaygroundDraftEntity::class,
        NotificationEntity::class,
        LocalAccountEntity::class,
        ForumPostEntity::class,
        LeaderboardEntryCacheEntity::class,
        WeeklyChallengeEntity::class,
        SectionQuizAttemptEntity::class,
        ActiveSectionQuizSessionEntity::class,
        ProjectSubmissionEntity::class
    ],
    version = 13,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun userDao(): UserDao
    abstract fun moduleProgressDao(): ModuleProgressDao
    abstract fun bookmarkDao(): BookmarkDao
    abstract fun certificateDao(): CertificateDao
    abstract fun playgroundDao(): PlaygroundDao
    abstract fun notificationDao(): NotificationDao
    abstract fun localAccountDao(): LocalAccountDao
    abstract fun forumPostDao(): ForumPostDao
    abstract fun leaderboardDao(): LeaderboardDao
    abstract fun weeklyChallengeDao(): WeeklyChallengeDao
    abstract fun sectionQuizAttemptDao(): SectionQuizAttemptDao
    abstract fun activeSectionQuizSessionDao(): ActiveSectionQuizSessionDao
    abstract fun projectSubmissionDao(): ProjectSubmissionDao
    fun codeAcademyDao(): CodeAcademyDao = InMemoryCodeAcademyDao.instance

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        val MIGRATION_1_2 = object : Migration(1, 2) {
            override fun migrate(db: SupportSQLiteDatabase) {
                db.execSQL(
                    """
                    CREATE TABLE IF NOT EXISTS `local_accounts` (
                        `id` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,
                        `email` TEXT NOT NULL,
                        `passwordHash` TEXT NOT NULL,
                        `displayName` TEXT NOT NULL,
                        `createdAt` INTEGER NOT NULL
                    )
                    """.trimIndent()
                )
            }
        }

        val MIGRATION_2_3 = object : Migration(2, 3) {
            override fun migrate(db: SupportSQLiteDatabase) {
                // Ensure default account (Rafi Alfarizi) exists in local_accounts
                db.execSQL(
                    """
                    INSERT OR IGNORE INTO `local_accounts` (`id`, `email`, `passwordHash`, `displayName`, `createdAt`)
                    VALUES (1, 'rafi.alfarizi@example.com', 'ef92b778bafe771e89245b89ecbc08a44a4e166c06659911881f383d4473e94f', 'Rafi Alfarizi', 1700000000000)
                    """.trimIndent()
                )

                // Migrate user_profile to use accountId as primary key
                db.execSQL(
                    """
                    CREATE TABLE IF NOT EXISTS `user_profile_new` (
                        `accountId` INTEGER PRIMARY KEY NOT NULL,
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
                    INSERT INTO `user_profile_new` (
                        `accountId`, `name`, `email`, `bio`, `totalXp`, `level`,
                        `currentStreak`, `longestStreak`, `lastActiveDate`, `dailyQuestId`,
                        `dailyQuestProgress`, `dailyQuestCompleted`, `selectedTracks`, `goal`,
                        `isDarkMode`, `isOnboarded`
                    )
                    SELECT 
                        CAST(id AS INTEGER), `name`, `email`, `bio`, `totalXp`, `level`,
                        `currentStreak`, `longestStreak`, `lastActiveDate`, `dailyQuestId`,
                        `dailyQuestProgress`, `dailyQuestCompleted`, `selectedTracks`, `goal`,
                        `isDarkMode`, `isOnboarded`
                    FROM `user_profile`
                    """.trimIndent()
                )
                db.execSQL("DROP TABLE `user_profile`")
                db.execSQL("ALTER TABLE `user_profile_new` RENAME TO `user_profile`")

                // Migrate module_progress to include accountId in composite primary key
                db.execSQL(
                    """
                    CREATE TABLE IF NOT EXISTS `module_progress_new` (
                        `accountId` INTEGER NOT NULL,
                        `courseId` TEXT NOT NULL,
                        `moduleId` TEXT NOT NULL,
                        `isCompleted` INTEGER NOT NULL,
                        `completedAt` INTEGER NOT NULL,
                        PRIMARY KEY(`accountId`, `courseId`, `moduleId`)
                    )
                    """.trimIndent()
                )
                db.execSQL(
                    """
                    INSERT INTO `module_progress_new` (`accountId`, `courseId`, `moduleId`, `isCompleted`, `completedAt`)
                    SELECT 1, `courseId`, `moduleId`, `isCompleted`, `completedAt` FROM `module_progress`
                    """.trimIndent()
                )
                db.execSQL("DROP TABLE `module_progress`")
                db.execSQL("ALTER TABLE `module_progress_new` RENAME TO `module_progress`")

                // Migrate bookmarks to include accountId
                db.execSQL(
                    """
                    CREATE TABLE IF NOT EXISTS `bookmarks_new` (
                        `id` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,
                        `accountId` INTEGER NOT NULL,
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
                    INSERT INTO `bookmarks_new` (`id`, `accountId`, `type`, `itemId`, `title`, `subtitle`, `trackName`, `savedAt`)
                    SELECT `id`, 1, `type`, `itemId`, `title`, `subtitle`, `trackName`, `savedAt` FROM `bookmarks`
                    """.trimIndent()
                )
                db.execSQL("DROP TABLE `bookmarks`")
                db.execSQL("ALTER TABLE `bookmarks_new` RENAME TO `bookmarks`")
                db.execSQL("CREATE UNIQUE INDEX IF NOT EXISTS `index_bookmarks_accountId_itemId` ON `bookmarks` (`accountId`, `itemId`)")

                // Migrate certificates to include accountId
                db.execSQL(
                    """
                    CREATE TABLE IF NOT EXISTS `certificates_new` (
                        `accountId` INTEGER NOT NULL,
                        `certId` TEXT NOT NULL,
                        `courseId` TEXT NOT NULL,
                        `courseTitle` TEXT NOT NULL,
                        `userName` TEXT NOT NULL,
                        `issueDate` TEXT NOT NULL,
                        `scorePercent` INTEGER NOT NULL,
                        PRIMARY KEY(`accountId`, `certId`)
                    )
                    """.trimIndent()
                )
                db.execSQL(
                    """
                    INSERT INTO `certificates_new` (`accountId`, `certId`, `courseId`, `courseTitle`, `userName`, `issueDate`, `scorePercent`)
                    SELECT 1, `certId`, `courseId`, `courseTitle`, `userName`, `issueDate`, `scorePercent` FROM `certificates`
                    """.trimIndent()
                )
                db.execSQL("DROP TABLE `certificates`")
                db.execSQL("ALTER TABLE `certificates_new` RENAME TO `certificates`")

                // Migrate notifications to add accountId
                db.execSQL("ALTER TABLE `notifications` ADD COLUMN `accountId` INTEGER NOT NULL DEFAULT 1")

                // Migrate playground_drafts to include accountId
                db.execSQL(
                    """
                    CREATE TABLE IF NOT EXISTS `playground_drafts_new` (
                        `accountId` INTEGER NOT NULL,
                        `courseId` TEXT NOT NULL,
                        `htmlCode` TEXT NOT NULL,
                        `cssCode` TEXT NOT NULL,
                        `jsCode` TEXT NOT NULL,
                        `updatedAt` INTEGER NOT NULL,
                        PRIMARY KEY(`accountId`, `courseId`)
                    )
                    """.trimIndent()
                )
                db.execSQL(
                    """
                    INSERT INTO `playground_drafts_new` (`accountId`, `courseId`, `htmlCode`, `cssCode`, `jsCode`, `updatedAt`)
                    SELECT 1, `courseId`, `htmlCode`, `cssCode`, `jsCode`, `updatedAt` FROM `playground_drafts`
                    """.trimIndent()
                )
                db.execSQL("DROP TABLE `playground_drafts`")
                db.execSQL("ALTER TABLE `playground_drafts_new` RENAME TO `playground_drafts`")
            }
        }

        val MIGRATION_3_4 = object : Migration(3, 4) {
            override fun migrate(db: SupportSQLiteDatabase) {
                // 1. Inspect existing legacy data in user_profile
                var legacyId = 1L
                var legacyName = "Rafi Alfarizi"
                var legacyEmail = "rafi.alfarizi@example.com"

                try {
                    val cursor = db.query("SELECT * FROM user_profile LIMIT 1")
                    if (cursor.moveToFirst()) {
                        val accIdx = cursor.getColumnIndex("accountId")
                        val idIdx = cursor.getColumnIndex("id")
                        val nameIdx = cursor.getColumnIndex("name")
                        val emailIdx = cursor.getColumnIndex("email")

                        if (accIdx >= 0 && !cursor.isNull(accIdx)) {
                            legacyId = cursor.getLong(accIdx)
                        } else if (idIdx >= 0 && !cursor.isNull(idIdx)) {
                            legacyId = cursor.getLong(idIdx)
                        }
                        if (nameIdx >= 0 && !cursor.isNull(nameIdx)) {
                            val n = cursor.getString(nameIdx)
                            if (!n.isNullOrBlank()) legacyName = n
                        }
                        if (emailIdx >= 0 && !cursor.isNull(emailIdx)) {
                            val e = cursor.getString(emailIdx)
                            if (!e.isNullOrBlank()) legacyEmail = e
                        }
                    }
                    cursor.close()
                } catch (_: Exception) {}

                // 2. Ensure local_accounts table exists and has unique index on email
                db.execSQL(
                    """
                    CREATE TABLE IF NOT EXISTS `local_accounts` (
                        `id` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,
                        `email` TEXT NOT NULL,
                        `passwordHash` TEXT NOT NULL,
                        `displayName` TEXT NOT NULL,
                        `createdAt` INTEGER NOT NULL
                    )
                    """.trimIndent()
                )
                db.execSQL("CREATE UNIQUE INDEX IF NOT EXISTS `index_local_accounts_email` ON `local_accounts` (`email`)")

                // Ensure legacy account exists in local_accounts
                db.execSQL(
                    """
                    INSERT OR IGNORE INTO `local_accounts` (`id`, `email`, `passwordHash`, `displayName`, `createdAt`)
                    VALUES ($legacyId, '$legacyEmail', 'ef92b778bafe771e89245b89ecbc08a44a4e166c06659911881f383d4473e94f', '$legacyName', 1700000000000)
                    """.trimIndent()
                )

                // 3. Recreate user_profile with Foreign Key to local_accounts(id)
                db.execSQL(
                    """
                    CREATE TABLE IF NOT EXISTS `user_profile_v4` (
                        `accountId` INTEGER NOT NULL,
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
                        `isOnboarded` INTEGER NOT NULL,
                        PRIMARY KEY(`accountId`),
                        FOREIGN KEY(`accountId`) REFERENCES `local_accounts`(`id`) ON UPDATE NO ACTION ON DELETE CASCADE
                    )
                    """.trimIndent()
                )
                try {
                    db.execSQL(
                        """
                        INSERT OR IGNORE INTO `user_profile_v4` (
                            `accountId`, `name`, `email`, `bio`, `totalXp`, `level`,
                            `currentStreak`, `longestStreak`, `lastActiveDate`, `dailyQuestId`,
                            `dailyQuestProgress`, `dailyQuestCompleted`, `selectedTracks`, `goal`,
                            `isDarkMode`, `isOnboarded`
                        )
                        SELECT 
                            COALESCE(`accountId`, 1), `name`, `email`, `bio`, `totalXp`, `level`,
                            `currentStreak`, `longestStreak`, `lastActiveDate`, `dailyQuestId`,
                            `dailyQuestProgress`, `dailyQuestCompleted`, `selectedTracks`, `goal`,
                            `isDarkMode`, `isOnboarded`
                        FROM `user_profile`
                        """.trimIndent()
                    )
                } catch (_: Exception) {}
                db.execSQL("DROP TABLE IF EXISTS `user_profile`")
                db.execSQL("ALTER TABLE `user_profile_v4` RENAME TO `user_profile`")

                // 4. Recreate module_progress with Foreign Key and Index
                db.execSQL(
                    """
                    CREATE TABLE IF NOT EXISTS `module_progress_v4` (
                        `accountId` INTEGER NOT NULL,
                        `courseId` TEXT NOT NULL,
                        `moduleId` TEXT NOT NULL,
                        `isCompleted` INTEGER NOT NULL,
                        `completedAt` INTEGER NOT NULL,
                        PRIMARY KEY(`accountId`, `courseId`, `moduleId`),
                        FOREIGN KEY(`accountId`) REFERENCES `local_accounts`(`id`) ON UPDATE NO ACTION ON DELETE CASCADE
                    )
                    """.trimIndent()
                )
                try {
                    db.execSQL(
                        """
                        INSERT OR IGNORE INTO `module_progress_v4` (`accountId`, `courseId`, `moduleId`, `isCompleted`, `completedAt`)
                        SELECT COALESCE(`accountId`, 1), `courseId`, `moduleId`, `isCompleted`, `completedAt` FROM `module_progress`
                        """.trimIndent()
                    )
                } catch (_: Exception) {}
                db.execSQL("DROP TABLE IF EXISTS `module_progress`")
                db.execSQL("ALTER TABLE `module_progress_v4` RENAME TO `module_progress`")
                db.execSQL("CREATE INDEX IF NOT EXISTS `index_module_progress_accountId` ON `module_progress` (`accountId`)")

                // 5. Recreate bookmarks with Foreign Key and Indices
                db.execSQL(
                    """
                    CREATE TABLE IF NOT EXISTS `bookmarks_v4` (
                        `id` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,
                        `accountId` INTEGER NOT NULL,
                        `type` TEXT NOT NULL,
                        `itemId` TEXT NOT NULL,
                        `title` TEXT NOT NULL,
                        `subtitle` TEXT NOT NULL,
                        `trackName` TEXT NOT NULL,
                        `savedAt` INTEGER NOT NULL,
                        FOREIGN KEY(`accountId`) REFERENCES `local_accounts`(`id`) ON UPDATE NO ACTION ON DELETE CASCADE
                    )
                    """.trimIndent()
                )
                try {
                    db.execSQL(
                        """
                        INSERT OR IGNORE INTO `bookmarks_v4` (`id`, `accountId`, `type`, `itemId`, `title`, `subtitle`, `trackName`, `savedAt`)
                        SELECT `id`, COALESCE(`accountId`, 1), `type`, `itemId`, `title`, `subtitle`, `trackName`, `savedAt` FROM `bookmarks`
                        """.trimIndent()
                    )
                } catch (_: Exception) {}
                db.execSQL("DROP TABLE IF EXISTS `bookmarks`")
                db.execSQL("ALTER TABLE `bookmarks_v4` RENAME TO `bookmarks`")
                db.execSQL("CREATE INDEX IF NOT EXISTS `index_bookmarks_accountId` ON `bookmarks` (`accountId`)")
                db.execSQL("CREATE UNIQUE INDEX IF NOT EXISTS `index_bookmarks_accountId_itemId` ON `bookmarks` (`accountId`, `itemId`)")

                // 6. Recreate certificates with Foreign Key and Index
                db.execSQL(
                    """
                    CREATE TABLE IF NOT EXISTS `certificates_v4` (
                        `accountId` INTEGER NOT NULL,
                        `certId` TEXT NOT NULL,
                        `courseId` TEXT NOT NULL,
                        `courseTitle` TEXT NOT NULL,
                        `userName` TEXT NOT NULL,
                        `issueDate` TEXT NOT NULL,
                        `scorePercent` INTEGER NOT NULL,
                        PRIMARY KEY(`accountId`, `certId`),
                        FOREIGN KEY(`accountId`) REFERENCES `local_accounts`(`id`) ON UPDATE NO ACTION ON DELETE CASCADE
                    )
                    """.trimIndent()
                )
                try {
                    db.execSQL(
                        """
                        INSERT OR IGNORE INTO `certificates_v4` (`accountId`, `certId`, `courseId`, `courseTitle`, `userName`, `issueDate`, `scorePercent`)
                        SELECT COALESCE(`accountId`, 1), `certId`, `courseId`, `courseTitle`, `userName`, `issueDate`, `scorePercent` FROM `certificates`
                        """.trimIndent()
                    )
                } catch (_: Exception) {}
                db.execSQL("DROP TABLE IF EXISTS `certificates`")
                db.execSQL("ALTER TABLE `certificates_v4` RENAME TO `certificates`")
                db.execSQL("CREATE INDEX IF NOT EXISTS `index_certificates_accountId` ON `certificates` (`accountId`)")

                // 7. Recreate playground_drafts with Foreign Key and Index
                db.execSQL(
                    """
                    CREATE TABLE IF NOT EXISTS `playground_drafts_v4` (
                        `accountId` INTEGER NOT NULL,
                        `courseId` TEXT NOT NULL,
                        `htmlCode` TEXT NOT NULL,
                        `cssCode` TEXT NOT NULL,
                        `jsCode` TEXT NOT NULL,
                        `updatedAt` INTEGER NOT NULL,
                        PRIMARY KEY(`accountId`, `courseId`),
                        FOREIGN KEY(`accountId`) REFERENCES `local_accounts`(`id`) ON UPDATE NO ACTION ON DELETE CASCADE
                    )
                    """.trimIndent()
                )
                try {
                    db.execSQL(
                        """
                        INSERT OR IGNORE INTO `playground_drafts_v4` (`accountId`, `courseId`, `htmlCode`, `cssCode`, `jsCode`, `updatedAt`)
                        SELECT COALESCE(`accountId`, 1), `courseId`, `htmlCode`, `cssCode`, `jsCode`, `updatedAt` FROM `playground_drafts`
                        """.trimIndent()
                    )
                } catch (_: Exception) {}
                db.execSQL("DROP TABLE IF EXISTS `playground_drafts`")
                db.execSQL("ALTER TABLE `playground_drafts_v4` RENAME TO `playground_drafts`")
                db.execSQL("CREATE INDEX IF NOT EXISTS `index_playground_drafts_accountId` ON `playground_drafts` (`accountId`)")

                // 8. Recreate notifications with Foreign Key and Index
                db.execSQL(
                    """
                    CREATE TABLE IF NOT EXISTS `notifications_v4` (
                        `id` TEXT NOT NULL,
                        `accountId` INTEGER NOT NULL,
                        `title` TEXT NOT NULL,
                        `message` TEXT NOT NULL,
                        `type` TEXT NOT NULL,
                        `timestamp` TEXT NOT NULL,
                        `isRead` INTEGER NOT NULL,
                        PRIMARY KEY(`id`),
                        FOREIGN KEY(`accountId`) REFERENCES `local_accounts`(`id`) ON UPDATE NO ACTION ON DELETE CASCADE
                    )
                    """.trimIndent()
                )
                try {
                    db.execSQL(
                        """
                        INSERT OR IGNORE INTO `notifications_v4` (`id`, `accountId`, `title`, `message`, `type`, `timestamp`, `isRead`)
                        SELECT `id`, COALESCE(`accountId`, 1), `title`, `message`, `type`, `timestamp`, `isRead` FROM `notifications`
                        """.trimIndent()
                    )
                } catch (_: Exception) {}
                db.execSQL("DROP TABLE IF EXISTS `notifications`")
                db.execSQL("ALTER TABLE `notifications_v4` RENAME TO `notifications`")
                db.execSQL("CREATE INDEX IF NOT EXISTS `index_notifications_accountId` ON `notifications` (`accountId`)")
            }
        }

        val MIGRATION_4_5 = object : Migration(4, 5) {
            override fun migrate(db: SupportSQLiteDatabase) {
                // Add passwordSalt column to local_accounts table with default empty string
                db.execSQL("ALTER TABLE `local_accounts` ADD COLUMN `passwordSalt` TEXT NOT NULL DEFAULT ''")
            }
        }

        val MIGRATION_5_6 = object : Migration(5, 6) {
            override fun migrate(db: SupportSQLiteDatabase) {
                db.execSQL(
                    """
                    CREATE TABLE IF NOT EXISTS `forum_posts` (
                        `postId` TEXT PRIMARY KEY NOT NULL,
                        `courseId` TEXT NOT NULL,
                        `authorId` TEXT NOT NULL,
                        `authorName` TEXT NOT NULL,
                        `content` TEXT NOT NULL,
                        `timestamp` INTEGER NOT NULL,
                        `likeCount` INTEGER NOT NULL,
                        `isLikedByMe` INTEGER NOT NULL
                    )
                    """.trimIndent()
                )
                db.execSQL("CREATE INDEX IF NOT EXISTS `index_forum_posts_courseId` ON `forum_posts` (`courseId`)")
                db.execSQL("CREATE INDEX IF NOT EXISTS `index_forum_posts_timestamp` ON `forum_posts` (`timestamp`)")

                db.execSQL(
                    """
                    CREATE TABLE IF NOT EXISTS `leaderboard_cache` (
                        `userId` TEXT PRIMARY KEY NOT NULL,
                        `userName` TEXT NOT NULL,
                        `totalXp` INTEGER NOT NULL,
                        `level` INTEGER NOT NULL,
                        `rank` INTEGER NOT NULL,
                        `cachedAt` INTEGER NOT NULL
                    )
                    """.trimIndent()
                )
                db.execSQL("CREATE INDEX IF NOT EXISTS `index_leaderboard_cache_rank` ON `leaderboard_cache` (`rank`)")
            }
        }

        val MIGRATION_6_7 = object : Migration(6, 7) {
            override fun migrate(db: SupportSQLiteDatabase) {
                db.execSQL("DROP TABLE IF EXISTS `leaderboard_cache`")
                db.execSQL(
                    """
                    CREATE TABLE IF NOT EXISTS `leaderboard_cache` (
                        `scopeKey` TEXT NOT NULL DEFAULT 'weekly_global',
                        `userId` TEXT NOT NULL,
                        `userName` TEXT NOT NULL,
                        `totalXp` INTEGER NOT NULL,
                        `level` INTEGER NOT NULL,
                        `rank` INTEGER NOT NULL,
                        `cachedAt` INTEGER NOT NULL,
                        PRIMARY KEY(`scopeKey`, `userId`)
                    )
                    """.trimIndent()
                )
                db.execSQL("CREATE INDEX IF NOT EXISTS `index_leaderboard_cache_scopeKey_rank` ON `leaderboard_cache` (`scopeKey`, `rank`)")
                db.execSQL("CREATE INDEX IF NOT EXISTS `index_leaderboard_cache_rank` ON `leaderboard_cache` (`rank`)")

                db.execSQL(
                    """
                    CREATE TABLE IF NOT EXISTS `weekly_challenges` (
                        `id` TEXT PRIMARY KEY NOT NULL,
                        `title` TEXT NOT NULL,
                        `description` TEXT NOT NULL,
                        `targetCount` INTEGER NOT NULL,
                        `rewardXp` INTEGER NOT NULL,
                        `type` TEXT NOT NULL,
                        `startDate` INTEGER NOT NULL,
                        `endDate` INTEGER NOT NULL,
                        `isClaimed` INTEGER NOT NULL
                    )
                    """.trimIndent()
                )
                db.execSQL("CREATE INDEX IF NOT EXISTS `index_weekly_challenges_startDate` ON `weekly_challenges` (`startDate`)")
            }
        }

        val MIGRATION_7_8 = object : Migration(7, 8) {
            override fun migrate(db: SupportSQLiteDatabase) {
                db.execSQL(
                    """
                    CREATE TABLE IF NOT EXISTS `section_quiz_attempts` (
                        `id` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,
                        `userId` TEXT NOT NULL,
                        `quizId` TEXT NOT NULL,
                        `timestamp` INTEGER NOT NULL,
                        `scorePercent` INTEGER NOT NULL,
                        `passed` INTEGER NOT NULL,
                        `failStreak` INTEGER NOT NULL,
                        `questionIdsJson` TEXT NOT NULL,
                        `answersJson` TEXT NOT NULL
                    )
                    """.trimIndent()
                )
                db.execSQL("CREATE INDEX IF NOT EXISTS `index_section_quiz_attempts_userId_quizId` ON `section_quiz_attempts` (`userId`, `quizId`)")
                db.execSQL("CREATE INDEX IF NOT EXISTS `index_section_quiz_attempts_timestamp` ON `section_quiz_attempts` (`timestamp`)")
            }
        }

        val MIGRATION_8_9 = object : Migration(8, 9) {
            override fun migrate(db: SupportSQLiteDatabase) {
                db.execSQL("ALTER TABLE `forum_posts` ADD COLUMN `replyToPostId` TEXT DEFAULT NULL")
                db.execSQL("ALTER TABLE `forum_posts` ADD COLUMN `replyToAuthorName` TEXT DEFAULT NULL")
                db.execSQL("ALTER TABLE `forum_posts` ADD COLUMN `replyToContentSnippet` TEXT DEFAULT NULL")
            }
        }

        val MIGRATION_9_10 = object : Migration(9, 10) {
            override fun migrate(db: SupportSQLiteDatabase) {
                db.execSQL("ALTER TABLE `section_quiz_attempts` ADD COLUMN `isPlacementAttempt` INTEGER NOT NULL DEFAULT 0")
            }
        }

        val MIGRATION_10_11 = object : Migration(10, 11) {
            override fun migrate(db: SupportSQLiteDatabase) {
                db.execSQL("ALTER TABLE `section_quiz_attempts` ADD COLUMN `failReason` TEXT DEFAULT NULL")
                db.execSQL(
                    """
                    CREATE TABLE IF NOT EXISTS `active_section_quiz_sessions` (
                        `sessionKey` TEXT PRIMARY KEY NOT NULL,
                        `userId` TEXT NOT NULL,
                        `quizId` TEXT NOT NULL,
                        `courseId` TEXT NOT NULL,
                        `startTime` INTEGER NOT NULL,
                        `durationMinutes` INTEGER NOT NULL,
                        `violationCount` INTEGER NOT NULL,
                        `lastViolationTimestamp` INTEGER NOT NULL,
                        `questionIdsJson` TEXT NOT NULL,
                        `answersJson` TEXT NOT NULL,
                        `isTerminated` INTEGER NOT NULL,
                        `terminationReason` TEXT DEFAULT NULL
                    )
                    """.trimIndent()
                )
                db.execSQL("CREATE UNIQUE INDEX IF NOT EXISTS `index_active_section_quiz_sessions_userId_quizId` ON `active_section_quiz_sessions` (`userId`, `quizId`)")
            }
        }

        val MIGRATION_11_12 = object : Migration(11, 12) {
            override fun migrate(db: SupportSQLiteDatabase) {
                db.execSQL("ALTER TABLE `forum_posts` ADD COLUMN `authorLevel` INTEGER NOT NULL DEFAULT 1")
                db.execSQL("ALTER TABLE `forum_posts` ADD COLUMN `authorIsMentor` INTEGER NOT NULL DEFAULT 0")
            }
        }

        val MIGRATION_12_13 = object : Migration(12, 13) {
            override fun migrate(db: SupportSQLiteDatabase) {
                db.execSQL(
                    """
                    CREATE TABLE IF NOT EXISTS `project_submissions` (
                        `accountId` INTEGER NOT NULL,
                        `courseId` TEXT NOT NULL,
                        `submissionId` TEXT NOT NULL,
                        `studentName` TEXT NOT NULL,
                        `studentEmail` TEXT NOT NULL,
                        `courseTitle` TEXT NOT NULL,
                        `finalQuizScore` INTEGER NOT NULL DEFAULT 0,
                        `status` TEXT NOT NULL DEFAULT 'DRAFT',
                        `workspaceCode` TEXT NOT NULL DEFAULT '',
                        `projectUrl` TEXT NOT NULL DEFAULT '',
                        `studentNotes` TEXT NOT NULL DEFAULT '',
                        `checkedRubricIdsJson` TEXT NOT NULL DEFAULT '[]',
                        `attemptNumber` INTEGER NOT NULL DEFAULT 1,
                        `submittedAt` INTEGER NOT NULL DEFAULT 0,
                        `reviewedAt` INTEGER DEFAULT NULL,
                        `facilitatorName` TEXT DEFAULT NULL,
                        `facilitatorFeedback` TEXT DEFAULT NULL,
                        `facilitatorScore` INTEGER DEFAULT NULL,
                        `rubricScoresJson` TEXT NOT NULL DEFAULT '{}',
                        PRIMARY KEY(`accountId`, `courseId`),
                        FOREIGN KEY(`accountId`) REFERENCES `local_accounts`(`id`) ON UPDATE NO ACTION ON DELETE CASCADE
                    )
                    """.trimIndent()
                )
                db.execSQL("CREATE INDEX IF NOT EXISTS `index_project_submissions_accountId` ON `project_submissions` (`accountId`)")
                db.execSQL("CREATE INDEX IF NOT EXISTS `index_project_submissions_submissionId` ON `project_submissions` (`submissionId`)")
            }
        }

        fun getDatabase(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "codeacademy_db"
                )
                    .addMigrations(
                        MIGRATION_1_2,
                        MIGRATION_2_3,
                        MIGRATION_3_4,
                        MIGRATION_4_5,
                        MIGRATION_5_6,
                        MIGRATION_6_7,
                        MIGRATION_7_8,
                        MIGRATION_8_9,
                        MIGRATION_9_10,
                        MIGRATION_10_11,
                        MIGRATION_11_12,
                        MIGRATION_12_13
                    )
                    .build()
                INSTANCE = instance
                instance
            }
        }

        fun resetInstanceForTesting() {
            synchronized(this) {
                try {
                    INSTANCE?.close()
                } catch (_: Exception) {}
                INSTANCE = null
            }
        }
    }
}
