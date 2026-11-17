package com.example.data

import android.content.Context
import com.example.data.local.AppDatabase
import com.example.data.local.BookmarkEntity
import com.example.data.local.CertificateEntity
import com.example.data.local.ForumPostEntity
import com.example.data.local.LeaderboardEntryCacheEntity
import com.example.data.local.LocalAccountEntity
import com.example.data.local.ModuleProgressEntity
import com.example.data.local.NotificationEntity
import com.example.data.local.PlaygroundDraftEntity
import com.example.data.local.ProjectSubmissionEntity
import com.example.data.local.SessionManager
import com.example.data.local.sessionDataStore
import com.example.data.local.SectionQuizAttemptEntity
import com.example.data.local.ActiveSectionQuizSessionEntity
import com.example.data.local.UserProfileEntity
import com.example.data.local.WeeklyChallengeEntity
import com.example.data.models.BadgeItem
import com.example.data.models.Certificate
import com.example.data.models.Course
import com.example.data.models.DailyQuest
import com.example.data.models.GamificationLevels
import com.example.data.models.LearningTrack
import com.example.data.models.ModuleSection
import com.example.data.models.NotificationItem
import com.example.data.models.ProjectSubmissionStatus
import com.example.data.models.QuizQuestion
import com.example.data.models.SectionQuiz
import com.example.data.models.PlacementTestQuestion
import com.example.data.models.PlacementSectionResult
import com.example.data.models.PlacementTestResult
import com.example.data.util.CourseUnlockUtils
import org.json.JSONArray
import com.example.CodeAcademyApplication
import com.example.util.PasswordHasher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import kotlinx.coroutines.withTimeout
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import java.util.UUID
import com.example.util.localization.toLocale

class CodeAcademyRepository(
    context: Context,
    private val externalScope: CoroutineScope = (context.applicationContext as? CodeAcademyApplication)?.applicationScope
        ?: CoroutineScope(SupervisorJob() + Dispatchers.IO)
) {
    private val appContext = context.applicationContext
    private val db = AppDatabase.getDatabase(context)
    private val userDao = db.userDao()
    private val progressDao = db.moduleProgressDao()
    private val bookmarkDao = db.bookmarkDao()
    private val certDao = db.certificateDao()
    private val playDao = db.playgroundDao()
    private val notifDao = db.notificationDao()
    private val accountDao = db.localAccountDao()
    private val forumPostDao = db.forumPostDao()
    private val leaderboardDao = db.leaderboardDao()
    private val weeklyChallengeDao = db.weeklyChallengeDao()
    private val sectionQuizAttemptDao = db.sectionQuizAttemptDao()
    private val activeSectionQuizSessionDao = db.activeSectionQuizSessionDao()
    private val projectSubmissionDao = db.projectSubmissionDao()
    val firestoreService = com.example.data.remote.FirestoreForumAndLeaderboardService()

    init {
        externalScope.launch {
            CourseCatalog.loadAssetCourses(context)
        }
    }
    val sessionManager = SessionManager(context)
    private val seedMutex = Mutex()

    @Volatile
    private var isSeeded = false

    suspend fun ensureInitialUserSeeded(): UserProfileEntity = withContext(Dispatchers.IO) {
        if (isSeeded) {
            val existing = userDao.getUserProfileOnce(1L)
            if (existing != null) return@withContext existing
        }
        seedMutex.withLock {
            if (isSeeded) {
                val existing = userDao.getUserProfileOnce(1L)
                if (existing != null) return@withLock existing
            }

            val existingAccount = accountDao.getAccountById(1L) ?: accountDao.getAccountByEmail("rafi.alfarizi@example.com")
            if (existingAccount == null) {
                val salt = PasswordHasher.generateSalt()
                val defaultAccount = LocalAccountEntity(
                    id = 1L,
                    email = "rafi.alfarizi@example.com",
                    passwordHash = PasswordHasher.hashPassword("password123", salt),
                    passwordSalt = salt,
                    displayName = "Rafi Alfarizi"
                )
                accountDao.insertAccount(defaultAccount)
            } else if (existingAccount.passwordSalt.isEmpty()) {
                val salt = PasswordHasher.generateSalt()
                val upgradedHash = PasswordHasher.hashPassword("password123", salt)
                accountDao.insertAccount(existingAccount.copy(passwordHash = upgradedHash, passwordSalt = salt))
            }

            val existing = userDao.getUserProfileOnce(1L)
            if (existing != null) {
                isSeeded = true
                return@withLock existing
            }

            val initialUser = UserProfileEntity(
                accountId = 1L,
                name = "Rafi Alfarizi",
                email = "rafi.alfarizi@example.com",
                bio = "Siswa SMA & Calon Web Developer 🚀",
                totalXp = 120,
                level = 2,
                currentStreak = 4,
                longestStreak = 5,
                lastActiveDate = getTodayDateString(),
                dailyQuestId = "quest_mod",
                dailyQuestProgress = 0,
                dailyQuestCompleted = false,
                selectedTracks = "front-end",
                goal = "Persiapan Karier",
                isDarkMode = true,
                isOnboarded = true
            )
            userDao.insertOrUpdateUser(initialUser)

            // Add starter notifications for account 1
            notifDao.insertNotification(
                NotificationEntity(
                    UUID.randomUUID().toString(),
                    1L,
                    "Selamat Datang di CodeAcademy!",
                    "Mulai belajar modul pertamamu dan bangun kebiasaan belajar setiap hari.",
                    "SYSTEM",
                    "Hari ini"
                )
            )
            notifDao.insertNotification(
                NotificationEntity(
                    UUID.randomUUID().toString(),
                    1L,
                    "Misi Harian Tersedia",
                    "Selesaikan 1 modul hari ini dan raih bonus +50 XP!",
                    "QUEST",
                    "Pagi ini"
                )
            )

            // Seed first module completed as starter progress for account 1
            progressDao.markModuleCompleted(
                ModuleProgressEntity(
                    accountId = 1L,
                    courseId = "fe-web-foundation-00",
                    moduleId = "web-foundation-prep-1",
                    isCompleted = true
                )
            )

            isSeeded = true
            initialUser
        }
    }

    init {
        externalScope.launch {
            ensureInitialUserSeeded()
        }
    }

    val activeAccountId: Flow<Long?> = sessionManager.accountId

    suspend fun getActiveAccountId(): Long {
        return sessionManager.accountId.first() ?: 1L
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    val userProfile: Flow<UserProfileEntity?> = activeAccountId.flatMapLatest { id ->
        if (id != null) userDao.getUserProfile(id) else flowOf(null)
    }

    suspend fun getUserProfileOnce(accountId: Long? = null): UserProfileEntity? = withContext(Dispatchers.IO) {
        ensureInitialUserSeeded()
        val targetId = accountId ?: sessionManager.accountId.first() ?: return@withContext null
        userDao.getUserProfileOnce(targetId)
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    val allProgress: Flow<List<ModuleProgressEntity>> = activeAccountId.flatMapLatest { id ->
        if (id != null) progressDao.getAllProgress(id) else flowOf(emptyList())
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    val bookmarks: Flow<List<BookmarkEntity>> = activeAccountId.flatMapLatest { id ->
        if (id != null) bookmarkDao.getAllBookmarks(id) else flowOf(emptyList())
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    val certificates: Flow<List<CertificateEntity>> = activeAccountId.flatMapLatest { id ->
        if (id != null) certDao.getAllCertificates(id) else flowOf(emptyList())
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    val projectSubmissions: Flow<List<ProjectSubmissionEntity>> = activeAccountId.flatMapLatest { id ->
        if (id != null) projectSubmissionDao.getAllSubmissions(id) else flowOf(emptyList())
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    fun getProjectSubmissionFlow(courseId: String): Flow<ProjectSubmissionEntity?> =
        activeAccountId.flatMapLatest { id ->
            if (id != null) projectSubmissionDao.getSubmission(id, courseId) else flowOf(null)
        }

    @OptIn(ExperimentalCoroutinesApi::class)
    val notifications: Flow<List<NotificationEntity>> = activeAccountId.flatMapLatest { id ->
        if (id != null) notifDao.getAllNotifications(id) else flowOf(emptyList())
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    val unreadNotifsCount: Flow<Int> = activeAccountId.flatMapLatest { id ->
        if (id != null) notifDao.getUnreadCount(id) else flowOf(0)
    }

    fun getAllTracks(): List<LearningTrack> = CourseCatalog.tracks
    fun getAllCourses(): List<Course> = CourseCatalog.courses
    val isCoursesLoaded: StateFlow<Boolean> get() = CourseCatalog.isAssetCoursesLoaded
    val coursesFlow: StateFlow<List<Course>> get() = CourseCatalog.coursesState

    fun getCourseById(courseId: String): Course? {
        val targetId = if (courseId == "fe-html-01") "fe-web-foundation-00" else courseId
        return CourseCatalog.courses.find { it.id == targetId }
    }

    fun isModuleCompleted(courseId: String, moduleId: String): Flow<Boolean> {
        return allProgress.map { list ->
            list.any { it.courseId == courseId && it.moduleId == moduleId && it.isCompleted }
        }
    }

    fun getCourseProgressPercent(courseId: String): Flow<Float> {
        val course = getCourseById(courseId) ?: return flowOf(0f)
        val totalModules = course.modules.size.coerceAtLeast(1)
        return allProgress.map { completed ->
            val count = completed.count { it.courseId == courseId && it.isCompleted }
            (count.toFloat() / totalModules.toFloat()).coerceIn(0f, 1f)
        }
    }

    suspend fun completeModule(courseId: String, moduleId: String): Boolean = withContext(Dispatchers.IO) {
        val accountId = getActiveAccountId()
        val isAlreadyDone = progressDao.isModuleCompleted(accountId, courseId, moduleId) ?: false
        if (!isAlreadyDone) {
            progressDao.markModuleCompleted(
                ModuleProgressEntity(
                    accountId = accountId,
                    courseId = courseId,
                    moduleId = moduleId,
                    isCompleted = true
                )
            )
            // Reward XP depending on module section
            val moduleObj = CourseCatalog.courses.find { it.id == courseId }?.modules?.find { it.id == moduleId }
            val xpReward = when (moduleObj?.section) {
                ModuleSection.PERSIAPAN, ModuleSection.PENDAHULUAN -> 5
                else -> 10
            }
            addXp(xpReward, "Menyelesaikan modul belajar")
            // Check quest
            incrementDailyQuestProgress()
            // Check streak
            touchStreak()
            true
        } else {
            false
        }
    }

    suspend fun touchStreak() = withContext(Dispatchers.IO) {
        val accountId = getActiveAccountId()
        val user = userDao.getUserProfileOnce(accountId) ?: return@withContext
        val today = getTodayDateString()
        if (user.lastActiveDate != today || user.currentStreak == 0) {
            val cal = java.util.Calendar.getInstance().apply { add(java.util.Calendar.DAY_OF_YEAR, -1) }
            val yesterday = SimpleDateFormat("yyyy-MM-dd", Locale.US).format(cal.time)
            val newStreak = when {
                user.lastActiveDate == today && user.currentStreak == 0 -> 1
                user.lastActiveDate == yesterday -> user.currentStreak + 1
                else -> 1
            }
            val newLongest = maxOf(newStreak, user.longestStreak)
            userDao.updateStreak(accountId, newStreak, newLongest, today)
        }
    }

    suspend fun addXp(amount: Int, reason: String): Pair<Int, Boolean> = withContext(Dispatchers.IO) {
        val accountId = getActiveAccountId()
        val user = userDao.getUserProfileOnce(accountId) ?: return@withContext Pair(0, false)
        touchStreak()
        val newXp = user.totalXp + amount
        val newLevelThreshold = GamificationLevels.getLevelForXp(newXp)
        val hasLeveledUp = newLevelThreshold.level > user.level

        userDao.updateXpAndLevel(accountId, newXp, newLevelThreshold.level)

        if (hasLeveledUp) {
            notifDao.insertNotification(
                NotificationEntity(
                    UUID.randomUUID().toString(),
                    accountId,
                    "🎉 Naik Level!",
                    "Selamat! Kamu telah mencapai Level ${newLevelThreshold.level} (${newLevelThreshold.name})!",
                    "LEVEL_UP",
                    "Baru saja"
                )
            )
        }
        Pair(newXp, hasLeveledUp)
    }

    private suspend fun incrementDailyQuestProgress() = withContext(Dispatchers.IO) {
        val accountId = getActiveAccountId()
        val user = userDao.getUserProfileOnce(accountId) ?: return@withContext
        if (!user.dailyQuestCompleted) {
            val newProgress = user.dailyQuestProgress + 1
            val isCompleted = newProgress >= 1
            userDao.updateDailyQuest(accountId, newProgress, isCompleted)
            if (isCompleted) {
                addXp(50, "Misi harian selesai")
                notifDao.insertNotification(
                    NotificationEntity(
                        UUID.randomUUID().toString(),
                        accountId,
                        "⚔️ Misi Harian Selesai!",
                        "Kamu mendapatkan bonus +50 XP karena menyelesaikan misi harian.",
                        "QUEST",
                        "Baru saja"
                    )
                )
            }
        }
    }

    suspend fun submitQuiz(courseId: String, scorePercent: Int): Certificate? = withContext(Dispatchers.IO) {
        val course = getCourseById(courseId) ?: return@withContext null
        val accountId = getActiveAccountId()
        // Complete quiz grants +20 XP
        addXp(20, "Menyelesaikan evaluasi")

        // Passing grade is 70%
        if (scorePercent >= 70) {
            // Passed grants +30 XP bonus
            addXp(30, "Lulus evaluasi dengan baik")

            val user = userDao.getUserProfileOnce(accountId)
            val studentName = user?.name ?: "Siswa CodeAcademy"
            val studentEmail = user?.email ?: ""

            val existingSub = projectSubmissionDao.getSubmissionOnce(accountId, course.id)
            val draftCode = playDao.getDraft(accountId, course.id)?.let { d ->
                listOf(d.htmlCode, d.cssCode, d.jsCode).filter { it.isNotBlank() }.joinToString("\n\n")
            }?.takeIf { it.isNotBlank() } ?: course.finalProject?.starterCode.orEmpty()

            val updatedSubmission = if (existingSub != null) {
                existingSub.copy(
                    finalQuizScore = maxOf(existingSub.finalQuizScore, scorePercent),
                    workspaceCode = existingSub.workspaceCode.ifBlank { draftCode }
                )
            } else {
                ProjectSubmissionEntity(
                    accountId = accountId,
                    courseId = course.id,
                    submissionId = "SUB-${accountId}-${course.id}",
                    studentName = studentName,
                    studentEmail = studentEmail,
                    courseTitle = course.title,
                    finalQuizScore = scorePercent,
                    status = ProjectSubmissionStatus.DRAFT.name,
                    workspaceCode = draftCode
                )
            }
            projectSubmissionDao.upsertSubmission(updatedSubmission)
            try {
                firestoreService.upsertProjectSubmission(updatedSubmission)
            } catch (_: Throwable) {
                // Offline-first fallback: sync later when submitting project
            }

            if (ProjectSubmissionStatus.fromString(updatedSubmission.status) == ProjectSubmissionStatus.APPROVED) {
                val facScore = updatedSubmission.facilitatorScore ?: 85
                val finalCertScore = CourseUnlockUtils.computeFinalCertificateScore(updatedSubmission.finalQuizScore, facScore)
                return@withContext issueCourseCertificate(accountId, course, studentName, finalCertScore)
            }

            notifDao.insertNotification(
                NotificationEntity(
                    UUID.randomUUID().toString(),
                    accountId,
                    "🚀 Proyek Akhir Terbuka!",
                    "Kamu lulus Kuis Evaluasi ${course.title} ($scorePercent%). Selesaikan Proyek Akhir untuk meraih Sertifikat Kelulusan!",
                    "SYSTEM",
                    "Baru saja"
                )
            )
            null
        } else {
            null
        }
    }

    private suspend fun issueCourseCertificate(
        accountId: Long,
        course: Course,
        studentName: String,
        finalScorePercent: Int,
        preferredCertId: String? = null
    ): Certificate {
        val existingCerts = certDao.getAllCertificates(accountId).firstOrNull().orEmpty()
        val existing = existingCerts.find { it.courseId == course.id }
        val certNumber = existing?.certId ?: preferredCertId ?: generateCertificateId(course.id)
        val certDate = existing?.issueDate
            ?: SimpleDateFormat("dd MMMM yyyy", com.example.util.localization.AppLanguage.INDONESIAN.toLocale()).format(Date())
        val bestScore = maxOf(existing?.scorePercent ?: 0, finalScorePercent)

        val entity = CertificateEntity(
            accountId = accountId,
            certId = certNumber,
            courseId = course.id,
            courseTitle = course.title,
            userName = studentName,
            issueDate = certDate,
            scorePercent = bestScore
        )
        certDao.insertCertificate(entity)
        try {
            firestoreService.upsertCertificate(entity)
        } catch (_: Throwable) {
            // Keep local Room copy if offline
        }

        if (existing == null) {
            notifDao.insertNotification(
                NotificationEntity(
                    UUID.randomUUID().toString(),
                    accountId,
                    "🏆 Sertifikat Diterbitkan!",
                    "Selamat! Proyek Akhirmu disetujui dan kamu resmi lulus kelas ${course.title} dengan nilai akhir $bestScore%!",
                    "BADGE",
                    "Baru saja"
                )
            )
        }

        return Certificate(
            certId = certNumber,
            courseId = course.id,
            courseTitle = course.title,
            userName = studentName,
            issueDate = certDate,
            scorePercent = bestScore
        )
    }

    suspend fun getProjectSubmissionOnce(courseId: String): ProjectSubmissionEntity? = withContext(Dispatchers.IO) {
        val accountId = getActiveAccountId()
        projectSubmissionDao.getSubmissionOnce(accountId, courseId)
    }

    suspend fun ensureProjectSubmissionInitialized(courseId: String): ProjectSubmissionEntity? = withContext(Dispatchers.IO) {
        val course = getCourseById(courseId) ?: return@withContext null
        val accountId = getActiveAccountId()
        val existing = projectSubmissionDao.getSubmissionOnce(accountId, course.id)
        if (existing != null) return@withContext existing

        val user = userDao.getUserProfileOnce(accountId)
        val draftCode = playDao.getDraft(accountId, course.id)?.let { d ->
            listOf(d.htmlCode, d.cssCode, d.jsCode).filter { it.isNotBlank() }.joinToString("\n\n")
        }?.takeIf { it.isNotBlank() } ?: course.finalProject?.starterCode.orEmpty()

        val created = ProjectSubmissionEntity(
            accountId = accountId,
            courseId = course.id,
            submissionId = "SUB-${accountId}-${course.id}",
            studentName = user?.name ?: "Siswa CodeAcademy",
            studentEmail = user?.email ?: "",
            courseTitle = course.title,
            finalQuizScore = 0,
            status = ProjectSubmissionStatus.LOCKED.name,
            workspaceCode = draftCode
        )
        projectSubmissionDao.upsertSubmission(created)
        created
    }

    suspend fun saveProjectSubmissionDraft(
        courseId: String,
        workspaceCode: String,
        projectUrl: String,
        studentNotes: String,
        checkedRubricIds: Set<String>
    ): ProjectSubmissionEntity? = withContext(Dispatchers.IO) {
        val base = ensureProjectSubmissionInitialized(courseId) ?: return@withContext null
        val checkedJson = JSONArray(checkedRubricIds.toList()).toString()
        val updated = base.copy(
            workspaceCode = workspaceCode,
            projectUrl = projectUrl.trim(),
            studentNotes = studentNotes.trim(),
            checkedRubricIdsJson = checkedJson
        )
        projectSubmissionDao.upsertSubmission(updated)
        updated
    }

    suspend fun submitFinalProject(
        courseId: String,
        workspaceCode: String,
        projectUrl: String,
        studentNotes: String,
        checkedRubricIds: Set<String>
    ): Result<ProjectSubmissionEntity> = withContext(Dispatchers.IO) {
        val course = getCourseById(courseId) ?: return@withContext Result.failure(IllegalArgumentException("Course not found"))
        val base = ensureProjectSubmissionInitialized(course.id)
            ?: return@withContext Result.failure(IllegalStateException("Submission could not be initialized"))

        val accountId = getActiveAccountId()
        val user = userDao.getUserProfileOnce(accountId)
        val checkedJson = JSONArray(checkedRubricIds.toList()).toString()
        val prevStatus = ProjectSubmissionStatus.fromString(base.status)
        val nextAttempt = if (prevStatus == ProjectSubmissionStatus.REVISION_REQUESTED) base.attemptNumber + 1 else base.attemptNumber

        val submitted = base.copy(
            studentName = user?.name ?: base.studentName,
            studentEmail = user?.email ?: base.studentEmail,
            courseTitle = course.title,
            status = ProjectSubmissionStatus.PENDING_REVIEW.name,
            workspaceCode = workspaceCode,
            projectUrl = projectUrl.trim(),
            studentNotes = studentNotes.trim(),
            checkedRubricIdsJson = checkedJson,
            attemptNumber = nextAttempt,
            submittedAt = System.currentTimeMillis()
        )
        projectSubmissionDao.upsertSubmission(submitted)

        // Sync to Firestore 'project_submissions' collection for Facilitator Web
        firestoreService.upsertProjectSubmission(submitted)

        notifDao.insertNotification(
            NotificationEntity(
                UUID.randomUUID().toString(),
                accountId,
                "📤 Proyek Akhir Terkirim",
                "Proyek akhir ${course.title} (Percobaan #$nextAttempt) telah dikirim dan sedang menunggu penilaian Fasilitator.",
                "SYSTEM",
                "Baru saja"
            )
        )

        Result.success(submitted)
    }

    suspend fun handleRemoteSubmissionUpdate(remote: ProjectSubmissionEntity) = withContext(Dispatchers.IO) {
        val local = projectSubmissionDao.getSubmissionOnce(remote.accountId, remote.courseId)
        projectSubmissionDao.upsertSubmission(remote)
        val oldStatus = local?.let { ProjectSubmissionStatus.fromString(it.status) }
        val newStatus = ProjectSubmissionStatus.fromString(remote.status)

        if (oldStatus != newStatus) {
            val course = getCourseById(remote.courseId) ?: return@withContext
            if (newStatus == ProjectSubmissionStatus.APPROVED) {
                addXp(course.finalProject?.xpReward ?: 100, "Lulus Proyek Akhir ${course.title}")
                val facScore = remote.facilitatorScore ?: 85
                val certScore = CourseUnlockUtils.computeFinalCertificateScore(remote.finalQuizScore, facScore)
                val preferredCertId = firestoreService.fetchPreferredCertIdForSubmission(remote.accountId, remote.courseId)
                issueCourseCertificate(remote.accountId, course, remote.studentName, certScore, preferredCertId)
            } else if (newStatus == ProjectSubmissionStatus.REVISION_REQUESTED) {
                notifDao.insertNotification(
                    NotificationEntity(
                        UUID.randomUUID().toString(),
                        remote.accountId,
                        "📝 Proyek Akhir Perlu Revisi",
                        "Fasilitator telah memeriksa proyek ${course.title} dan memberikan catatan perbaikan.",
                        "SYSTEM",
                        "Baru saja"
                    )
                )
            }
        }
    }

    suspend fun simulateFacilitatorReview(
        courseId: String,
        approved: Boolean,
        facilitatorScore: Int,
        facilitatorFeedback: String,
        facilitatorName: String = "Tim Fasilitator CodeAcademy",
        rubricScores: Map<String, Int> = emptyMap()
    ): Result<Pair<ProjectSubmissionEntity, Certificate?>> = withContext(Dispatchers.IO) {
        val course = getCourseById(courseId) ?: return@withContext Result.failure(IllegalArgumentException("Course not found"))
        val base = ensureProjectSubmissionInitialized(course.id)
            ?: return@withContext Result.failure(IllegalStateException("Submission not found"))

        val rubricJsonObj = org.json.JSONObject()
        rubricScores.forEach { (k, v) -> rubricJsonObj.put(k, v) }

        val targetStatus = if (approved) ProjectSubmissionStatus.APPROVED else ProjectSubmissionStatus.REVISION_REQUESTED
        val clampedScore = facilitatorScore.coerceIn(0, 100)
        val reviewed = base.copy(
            status = targetStatus.name,
            reviewedAt = System.currentTimeMillis(),
            facilitatorName = facilitatorName.ifBlank { course.instructor },
            facilitatorFeedback = facilitatorFeedback.trim(),
            facilitatorScore = clampedScore,
            rubricScoresJson = rubricJsonObj.toString()
        )

        projectSubmissionDao.upsertSubmission(reviewed)
        firestoreService.upsertProjectSubmission(reviewed)

        var issuedCert: Certificate? = null
        if (approved) {
            addXp(course.finalProject?.xpReward ?: 100, "Lulus Proyek Akhir ${course.title}")
            val finalCertScore = CourseUnlockUtils.computeFinalCertificateScore(reviewed.finalQuizScore, clampedScore)
            issuedCert = issueCourseCertificate(reviewed.accountId, course, reviewed.studentName, finalCertScore)
        } else {
            notifDao.insertNotification(
                NotificationEntity(
                    UUID.randomUUID().toString(),
                    reviewed.accountId,
                    "📝 Proyek Akhir Perlu Revisi",
                    "Fasilitator ${reviewed.facilitatorName} memberikan evaluasi pada proyek ${course.title}. Silakan periksa feedback dan kirim ulang.",
                    "SYSTEM",
                    "Baru saja"
                )
            )
        }

        Result.success(Pair(reviewed, issuedCert))
    }

    private fun generateCertificateId(courseId: String): String {
        val yearMonth = SimpleDateFormat("yyyyMM", Locale.US).format(Date())
        val shortCourse = courseId.replace("-", "").take(4).uppercase(Locale.US)
        val rand = UUID.randomUUID().toString().take(6).uppercase(Locale.US)
        return "CC-$yearMonth$shortCourse-$rand"
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    fun getSectionQuizAttempts(quizId: String): Flow<List<SectionQuizAttemptEntity>> =
        activeAccountId.flatMapLatest { id ->
            if (id != null) sectionQuizAttemptDao.getAttempts(id.toString(), quizId)
            else flowOf(emptyList())
        }

    @OptIn(ExperimentalCoroutinesApi::class)
    val passedSectionQuizIds: Flow<Set<String>> =
        activeAccountId.flatMapLatest { id ->
            if (id != null) sectionQuizAttemptDao.getPassedQuizIds(id.toString()).map { it.toSet() }
            else flowOf(emptySet())
        }

    suspend fun getPassedSectionQuizIdsOnce(): Set<String> = withContext(Dispatchers.IO) {
        val id = getActiveAccountId()
        sectionQuizAttemptDao.getPassedQuizIdsOnce(id.toString()).toSet()
    }

    suspend fun getSectionQuizAttemptsOnce(quizId: String): List<SectionQuizAttemptEntity> = withContext(Dispatchers.IO) {
        val userId = getActiveAccountId().toString()
        sectionQuizAttemptDao.getAttemptsOnce(userId, quizId)
    }

    suspend fun getSectionQuizAttemptById(id: Long): SectionQuizAttemptEntity? = withContext(Dispatchers.IO) {
        sectionQuizAttemptDao.getAttemptById(id)
    }

    suspend fun getCooldownRemainingSeconds(quizId: String): Long = withContext(Dispatchers.IO) {
        val userId = getActiveAccountId().toString()
        val attempts = sectionQuizAttemptDao.getAttemptsOnce(userId, quizId)
        if (attempts.any { it.passed }) {
            return@withContext 0L // Pengguna yang sudah lulus boleh mengulang bebas tanpa jeda
        }
        val lastAttempt = attempts.firstOrNull() ?: return@withContext 0L
        if (lastAttempt.passed) return@withContext 0L

        val now = System.currentTimeMillis()
        val elapsedMs = now - lastAttempt.timestamp
        if (elapsedMs > 24 * 60 * 60 * 1000L) {
            return@withContext 0L // Streak gagal reset jika kegagalan terakhir > 24 jam lalu
        }

        val failStreak = lastAttempt.failStreak
        // 1m -> 2m -> 4m -> 8m -> 16m -> max 30m
        val cooldownMinutes = minOf(30, 1 shl (failStreak - 1).coerceAtLeast(0))
        val totalCooldownMs = cooldownMinutes * 60 * 1000L
        val remainingMs = totalCooldownMs - elapsedMs
        if (remainingMs > 0) remainingMs / 1000L else 0L
    }

    suspend fun generateSectionQuizQuestions(quiz: SectionQuiz): List<QuizQuestion> = withContext(Dispatchers.IO) {
        val userId = getActiveAccountId().toString()
        val existingSession = activeSectionQuizSessionDao.getActiveSession(userId, quiz.id)
        if (existingSession != null && !existingSession.isTerminated) {
            try {
                val jsonArr = JSONArray(existingSession.questionIdsJson)
                val poolMap = quiz.questionPool.associateBy { it.id }
                val restored = (0 until jsonArr.length()).mapNotNull { poolMap[jsonArr.getInt(it)] }
                if (restored.isNotEmpty()) {
                    return@withContext restored
                }
            } catch (_: Exception) {
                // Fall through to generate fresh questions if session JSON is malformed
            }
        }

        val lastAttempt = sectionQuizAttemptDao.getLastAttempt(userId, quiz.id)
        val lastIds = if (lastAttempt != null) {
            try {
                val jsonArr = JSONArray(lastAttempt.questionIdsJson)
                (0 until jsonArr.length()).map { jsonArr.getInt(it) }.toSet()
            } catch (_: Exception) {
                emptySet()
            }
        } else emptySet()

        val pool = quiz.questionPool
        var picked: List<QuizQuestion>
        var attemptsCount = 0
        do {
            picked = pool.shuffled().take(quiz.questionsPerAttempt)
            attemptsCount++
        } while (attemptsCount < 5 && picked.map { it.id }.toSet() == lastIds && pool.size > quiz.questionsPerAttempt)

        // Shuffle opsi tiap soal dan petakan ulang correctIndex
        picked.map { q ->
            val correctOptionText = q.options.getOrElse(q.correctIndex) { "" }
            val shuffledOptions = q.options.shuffled()
            val newCorrectIdx = shuffledOptions.indexOf(correctOptionText).coerceAtLeast(0)
            q.copy(options = shuffledOptions, correctIndex = newCorrectIdx)
        }
    }

    suspend fun startSectionQuizSession(
        course: Course,
        quiz: SectionQuiz,
        questions: List<QuizQuestion>
    ): ActiveSectionQuizSessionEntity = withContext(Dispatchers.IO) {
        val userId = getActiveAccountId().toString()
        val sessionKey = "$userId-${quiz.id}"
        val existing = activeSectionQuizSessionDao.getActiveSession(userId, quiz.id)
        if (existing != null && !existing.isTerminated) {
            existing
        } else {
            val qIdsJson = JSONArray(questions.map { it.id }).toString()
            val newSession = ActiveSectionQuizSessionEntity(
                sessionKey = sessionKey,
                userId = userId,
                quizId = quiz.id,
                courseId = course.id,
                startTime = System.currentTimeMillis(),
                durationMinutes = quiz.durationMinutes,
                violationCount = 0,
                lastViolationTimestamp = 0L,
                questionIdsJson = qIdsJson,
                answersJson = "{}"
            )
            activeSectionQuizSessionDao.saveActiveSession(newSession)
            newSession
        }
    }

    suspend fun getActiveSectionQuizSession(quizId: String): ActiveSectionQuizSessionEntity? = withContext(Dispatchers.IO) {
        val userId = getActiveAccountId().toString()
        activeSectionQuizSessionDao.getActiveSession(userId, quizId)
    }

    suspend fun updateActiveSectionQuizAnswers(
        quizId: String,
        answers: Map<Int, String>
    ) = withContext(Dispatchers.IO) {
        val userId = getActiveAccountId().toString()
        val jsonObj = org.json.JSONObject()
        answers.forEach { (k, v) -> jsonObj.put(k.toString(), v) }
        activeSectionQuizSessionDao.updateAnswers(userId, quizId, jsonObj.toString())
    }

    suspend fun recordSectionQuizViolation(
        course: Course,
        quiz: SectionQuiz,
        questions: List<QuizQuestion>,
        currentAnswers: Map<Int, String>
    ): Pair<Int, SectionQuizAttemptEntity?> = withContext(Dispatchers.IO) {
        val userId = getActiveAccountId().toString()
        val now = System.currentTimeMillis()
        val sessionKey = "$userId-${quiz.id}"

        val currentSession = activeSectionQuizSessionDao.getActiveSession(userId, quiz.id)
        val newViolationCount = (currentSession?.violationCount ?: 0) + 1

        if (newViolationCount >= 2) {
            // Second violation -> Terminate quiz immediately and record failed attempt
            activeSectionQuizSessionDao.updateViolation(
                userId = userId,
                quizId = quiz.id,
                violationCount = newViolationCount,
                timestamp = now,
                isTerminated = true,
                terminationReason = "FOCUS_VIOLATION"
            )
            val failedAttempt = submitSectionQuiz(
                quiz = quiz,
                questions = questions,
                userAnswersByQuestionId = currentAnswers,
                failReason = "FOCUS_VIOLATION"
            )
            Pair(newViolationCount, failedAttempt)
        } else {
            // First violation -> Persist to Room and return warning count
            val qIdsJson = JSONArray(questions.map { it.id }).toString()
            val answersJsonObj = org.json.JSONObject()
            currentAnswers.forEach { (k, v) -> answersJsonObj.put(k.toString(), v) }

            val session = currentSession?.copy(
                violationCount = newViolationCount,
                lastViolationTimestamp = now,
                answersJson = answersJsonObj.toString()
            ) ?: ActiveSectionQuizSessionEntity(
                sessionKey = sessionKey,
                userId = userId,
                quizId = quiz.id,
                courseId = course.id,
                startTime = now,
                durationMinutes = quiz.durationMinutes,
                violationCount = newViolationCount,
                lastViolationTimestamp = now,
                questionIdsJson = qIdsJson,
                answersJson = answersJsonObj.toString()
            )
            activeSectionQuizSessionDao.saveActiveSession(session)
            Pair(newViolationCount, null)
        }
    }

    suspend fun clearActiveSectionQuizSession(quizId: String) = withContext(Dispatchers.IO) {
        val userId = getActiveAccountId().toString()
        activeSectionQuizSessionDao.clearActiveSession(userId, quizId)
    }

    suspend fun submitSectionQuiz(
        quiz: SectionQuiz,
        questions: List<QuizQuestion>,
        userAnswersByQuestionId: Map<Int, String>,
        failReason: String? = null
    ): SectionQuizAttemptEntity = withContext(Dispatchers.IO) {
        val userId = getActiveAccountId().toString()
        val lastAttempt = sectionQuizAttemptDao.getLastAttempt(userId, quiz.id)
        val now = System.currentTimeMillis()

        var correctCount = 0
        val answersJsonArray = JSONArray()
        val questionIdsJsonArray = JSONArray()

        for (q in questions) {
            questionIdsJsonArray.put(q.id)
            val selectedText = userAnswersByQuestionId[q.id] ?: ""
            answersJsonArray.put(selectedText)

            val correctOptionText = q.options.getOrElse(q.correctIndex) { "" }
            if (selectedText.isNotBlank() && selectedText == correctOptionText) {
                correctCount++
            }
        }

        val totalQuestions = questions.size.coerceAtLeast(1)
        val rawScorePercent = (correctCount * 100) / totalQuestions
        val isPassed = (failReason == null) && (rawScorePercent >= quiz.passingPercent)
        val finalScorePercent = if (failReason == "FOCUS_VIOLATION") 0 else rawScorePercent

        val failStreak = if (isPassed) {
            0
        } else {
            val wasRecent = lastAttempt != null && !lastAttempt.passed && (now - lastAttempt.timestamp <= 24 * 60 * 60 * 1000L)
            if (wasRecent) lastAttempt.failStreak + 1 else 1
        }

        val attempt = SectionQuizAttemptEntity(
            userId = userId,
            quizId = quiz.id,
            timestamp = now,
            scorePercent = finalScorePercent,
            passed = isPassed,
            failStreak = failStreak,
            questionIdsJson = questionIdsJsonArray.toString(),
            answersJson = answersJsonArray.toString(),
            failReason = failReason
        )

        val newId = sectionQuizAttemptDao.insertAttempt(attempt)

        // Clear active session upon submitting
        activeSectionQuizSessionDao.clearActiveSession(userId, quiz.id)

        // Add XP reward only on valid completion without violation
        if (failReason == null) {
            addXp(10, "Menyelesaikan kuis bagian")
            if (isPassed) {
                addXp(20, "Lulus ${quiz.title}")
                notifDao.insertNotification(
                    NotificationEntity(
                        UUID.randomUUID().toString(),
                        getActiveAccountId(),
                        "🎯 Lulus ${quiz.title}!",
                        "Selamat! Kamu berhasil lulus ${quiz.title} dengan nilai $finalScorePercent%!",
                        "SYSTEM",
                        "Baru saja"
                    )
                )
            }
        }

        attempt.copy(id = newId)
    }

    suspend fun generatePlacementTestQuestions(course: Course): List<PlacementTestQuestion> = withContext(Dispatchers.IO) {
        val questions = mutableListOf<PlacementTestQuestion>()
        val quizzes = course.sectionQuizzes

        if (quizzes.isNotEmpty()) {
            var globalId = 1
            for (quiz in quizzes) {
                // Ambil 3-4 soal per section
                val countToTake = when (quiz.section) {
                    ModuleSection.PERSIAPAN, ModuleSection.PENDAHULUAN -> 3
                    ModuleSection.INTI -> 4
                    ModuleSection.LANJUTAN -> 4
                    else -> 3
                }
                val pickedQuestions = quiz.questionPool.shuffled().take(countToTake)
                for (q in pickedQuestions) {
                    val correctText = q.options.getOrElse(q.correctIndex) { "" }
                    val shuffledOptions = q.options.shuffled()
                    val newCorrectIdx = shuffledOptions.indexOf(correctText).coerceAtLeast(0)
                    questions.add(
                        PlacementTestQuestion(
                            question = q.copy(
                                id = globalId++,
                                options = shuffledOptions,
                                correctIndex = newCorrectIdx
                            ),
                            section = quiz.section,
                            sectionTitle = quiz.title,
                            quizId = quiz.id
                        )
                    )
                }
            }
        }
        questions
    }

    suspend fun submitPlacementTest(
        course: Course,
        questions: List<PlacementTestQuestion>,
        userAnswersByQuestionId: Map<Int, String>
    ): PlacementTestResult = withContext(Dispatchers.IO) {
        val accountId = getActiveAccountId()
        val userId = accountId.toString()
        val now = System.currentTimeMillis()
        val alreadyPassedQuizzes = sectionQuizAttemptDao.getPassedQuizIdsOnce(userId).toSet()

        val groupedBySection = questions.groupBy { it.section }
        val sectionResults = mutableListOf<PlacementSectionResult>()

        var totalQuestions = 0
        var totalCorrect = 0
        var totalBonusXp = 0
        var totalFastTrackedModules = 0

        for ((section, sectionQuestions) in groupedBySection) {
            val quiz = course.sectionQuizzes.find { it.section == section }
            val passingPercent = quiz?.passingPercent ?: 80
            val quizId = quiz?.id ?: "${course.id}-placement-${section.name.lowercase()}"
            val sectionTitle = quiz?.title ?: "Bagian ${section.name}"

            var secCorrect = 0
            val qIdsJson = JSONArray()
            val answersJson = JSONArray()

            for (pq in sectionQuestions) {
                val q = pq.question
                qIdsJson.put(q.id)
                val userAnswer = userAnswersByQuestionId[q.id] ?: ""
                answersJson.put(userAnswer)
                val correctText = q.options.getOrElse(q.correctIndex) { "" }
                if (userAnswer.isNotBlank() && userAnswer == correctText) {
                    secCorrect++
                }
            }

            val secTotal = sectionQuestions.size.coerceAtLeast(1)
            val secScorePercent = (secCorrect * 100) / secTotal
            val isPassed = secScorePercent >= passingPercent

            totalQuestions += secTotal
            totalCorrect += secCorrect

            var bonusXp = 0
            var fastTrackedCount = 0

            if (isPassed) {
                val alreadyPassed = alreadyPassedQuizzes.contains(quizId)

                // 1. Simpan attempt placement test dengan passed = true
                val attempt = SectionQuizAttemptEntity(
                    userId = userId,
                    quizId = quizId,
                    timestamp = now,
                    scorePercent = secScorePercent,
                    passed = true,
                    failStreak = 0,
                    questionIdsJson = qIdsJson.toString(),
                    answersJson = answersJson.toString(),
                    isPlacementAttempt = true
                )
                sectionQuizAttemptDao.insertAttempt(attempt)

                // 2. Tandai semua modul di section ini sebagai isCompleted = true (soft-skip)
                val sectionModules = course.modules.filter { it.section == section }
                for (mod in sectionModules) {
                    progressDao.markModuleCompleted(
                        ModuleProgressEntity(
                            accountId = accountId,
                            courseId = course.id,
                            moduleId = mod.id,
                            isCompleted = true
                        )
                    )
                }
                fastTrackedCount = sectionModules.size
                totalFastTrackedModules += fastTrackedCount

                // 3. Beri XP bonus instan (hanya 1x per section)
                if (!alreadyPassed) {
                    bonusXp = 75 // Opsi 75 XP per section lulus
                    totalBonusXp += bonusXp
                    addXp(bonusXp, "Fast-track $sectionTitle (Tes Penempatan)")
                    notifDao.insertNotification(
                        NotificationEntity(
                            UUID.randomUUID().toString(),
                            accountId,
                            "⚡ Fast-Track Berhasil!",
                            "Kamu lulus $sectionTitle lewat Tes Penempatan! $fastTrackedCount modul dibuka dan kamu mendapat bonus +$bonusXp XP.",
                            "SYSTEM",
                            "Baru saja"
                        )
                    )
                }
            }

            sectionResults.add(
                PlacementSectionResult(
                    section = section,
                    sectionTitle = sectionTitle,
                    quizId = quizId,
                    correctCount = secCorrect,
                    totalQuestions = secTotal,
                    scorePercent = secScorePercent,
                    isPassed = isPassed,
                    fastTrackedModuleCount = fastTrackedCount,
                    bonusXpEarned = bonusXp
                )
            )
        }

        val overallScorePercent = if (totalQuestions > 0) (totalCorrect * 100) / totalQuestions else 0

        PlacementTestResult(
            courseId = course.id,
            courseTitle = course.title,
            totalQuestions = totalQuestions,
            totalCorrect = totalCorrect,
            overallScorePercent = overallScorePercent,
            totalBonusXpEarned = totalBonusXp,
            totalModulesFastTracked = totalFastTrackedModules,
            sectionResults = sectionResults
        )
    }

    suspend fun toggleBookmark(type: String, itemId: String, title: String, subtitle: String, trackName: String) = withContext(Dispatchers.IO) {
        val accountId = getActiveAccountId()
        val existing = bookmarkDao.isBookmarked(accountId, itemId).firstOrNull() ?: false
        if (existing) {
            bookmarkDao.deleteBookmarkByItemId(accountId, itemId)
        } else {
            bookmarkDao.insertBookmark(
                BookmarkEntity(
                    accountId = accountId,
                    type = type,
                    itemId = itemId,
                    title = title,
                    subtitle = subtitle,
                    trackName = trackName
                )
            )
        }
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    fun isItemBookmarked(itemId: String): Flow<Boolean> {
        return activeAccountId.flatMapLatest { id ->
            if (id != null) bookmarkDao.isBookmarked(id, itemId) else flowOf(false)
        }
    }

    suspend fun removeBookmark(itemId: String) = withContext(Dispatchers.IO) {
        val accountId = getActiveAccountId()
        bookmarkDao.deleteBookmarkByItemId(accountId, itemId)
    }

    suspend fun savePlaygroundDraft(courseId: String, html: String, css: String, js: String) = withContext(Dispatchers.IO) {
        val accountId = getActiveAccountId()
        playDao.saveDraft(
            PlaygroundDraftEntity(
                accountId = accountId,
                courseId = courseId,
                htmlCode = html,
                cssCode = css,
                jsCode = js
            )
        )
    }

    suspend fun getPlaygroundDraft(courseId: String): PlaygroundDraftEntity? = withContext(Dispatchers.IO) {
        val accountId = getActiveAccountId()
        playDao.getDraft(accountId, courseId)
    }

    suspend fun clearPlaygroundDraft(courseId: String) = withContext(Dispatchers.IO) {
        val accountId = getActiveAccountId()
        playDao.clearDraft(accountId, courseId)
    }

    suspend fun updateProfile(name: String, bio: String) = withContext(Dispatchers.IO) {
        val accountId = getActiveAccountId()
        val cleanName = com.example.util.NameUtils.sanitizeDisplayName(name)
        userDao.updateProfileInfo(accountId, cleanName, bio.trim())
        syncMyProfileToCloud()
    }

    suspend fun completeOnboarding(selectedTrackId: String) = withContext(Dispatchers.IO) {
        val accountId = getActiveAccountId()
        val tracksStr = if (selectedTrackId.isNotBlank()) selectedTrackId else "front-end"
        userDao.updateOnboardingStatus(accountId, isOnboarded = true, selectedTracks = tracksStr)
        val savedGoal = appContext.sessionDataStore.data.firstOrNull()
            ?.get(androidx.datastore.preferences.core.stringPreferencesKey("user_goal"))
        if (!savedGoal.isNullOrBlank()) {
            userDao.updateUserGoal(accountId, savedGoal)
        }
    }

    suspend fun markCoachMarkSeen(coachMarkId: String) = withContext(Dispatchers.IO) {
        val accountId = getActiveAccountId()
        val profile = userDao.getUserProfileOnce(accountId) ?: return@withContext
        val current = profile.seenCoachMarks.split(",").filter { it.isNotBlank() }.toMutableSet()
        if (coachMarkId !in current) {
            current.add(coachMarkId)
            userDao.updateSeenCoachMarks(accountId, current.joinToString(","))
        }
    }

    // Local Authentication
    suspend fun registerLocalAccount(
        name: String,
        email: String,
        password: String
    ): Result<LocalAccountEntity> = withContext(Dispatchers.IO) {
        val cleanEmail = email.trim().lowercase(Locale.ROOT)
        val cleanName = com.example.util.NameUtils.sanitizeDisplayName(name, fallback = cleanEmail.substringBefore("@"))

        ensureInitialUserSeeded()

        val existing = accountDao.getAccountByEmail(cleanEmail)
        if (existing != null) {
            return@withContext Result.failure(Exception("Email sudah terdaftar. Silakan gunakan email lain atau masuk."))
        }

        val salt = PasswordHasher.generateSalt()
        val passwordHash = PasswordHasher.hashPassword(password, salt)
        val newAccount = LocalAccountEntity(
            email = cleanEmail,
            passwordHash = passwordHash,
            passwordSalt = salt,
            displayName = cleanName
        )
        val accountId = accountDao.insertAccount(newAccount)
        val created = newAccount.copy(id = accountId)

        // Create clean UserProfileEntity for this new account
        val newProfile = UserProfileEntity(
            accountId = accountId,
            name = cleanName,
            email = cleanEmail,
            bio = "Siswa Baru di CodeAcademy 🚀",
            totalXp = 0,
            level = 1,
            currentStreak = 0,
            longestStreak = 0,
            lastActiveDate = "",
            dailyQuestId = "quest_mod",
            dailyQuestProgress = 0,
            dailyQuestCompleted = false,
            selectedTracks = "",
            goal = "Eksplorasi Koding",
            isDarkMode = true,
            isOnboarded = false // Fresh account must go through onboarding!
        )
        userDao.insertOrUpdateUser(newProfile)

        // Starter notification for new account
        notifDao.insertNotification(
            NotificationEntity(
                UUID.randomUUID().toString(),
                accountId,
                "Selamat Datang di CodeAcademy!",
                "Hai $cleanName, selamat datang! Selesaikan pengenalan jalur belajarmu untuk memulai modul pertama.",
                "SYSTEM",
                "Baru saja"
            )
        )

        // Save session locally
        sessionManager.saveSession(
            accountId = accountId,
            email = cleanEmail,
            displayName = cleanName
        )

        Result.success(created)
    }

    suspend fun loginLocalAccount(
        email: String,
        password: String
    ): Result<LocalAccountEntity> = withContext(Dispatchers.IO) {
        ensureInitialUserSeeded()
        val cleanEmail = email.trim().lowercase(Locale.ROOT)
        val account = accountDao.getAccountByEmail(cleanEmail)

        if (account == null || !PasswordHasher.verifyPassword(password, account.passwordHash, account.passwordSalt)) {
            // Generic message for security practice
            return@withContext Result.failure(Exception("Email atau kata sandi salah."))
        }

        // If account had legacy un-salted hash, upgrade to salted PBKDF2 hash on successful login
        if (account.passwordSalt.isEmpty()) {
            val newSalt = PasswordHasher.generateSalt()
            val newHash = PasswordHasher.hashPassword(password, newSalt)
            accountDao.insertAccount(account.copy(passwordHash = newHash, passwordSalt = newSalt))
        }

        // Save session locally
        sessionManager.saveSession(
            accountId = account.id,
            email = account.email,
            displayName = account.displayName
        )

        Result.success(account)
    }

    suspend fun loginWithGoogleIdToken(
        idToken: String,
        fallbackName: String?,
        fallbackEmail: String?
    ): Result<Boolean> = withContext(Dispatchers.IO) {
        try {
            val credential = com.google.firebase.auth.GoogleAuthProvider.getCredential(idToken, null)
            val auth = com.google.firebase.auth.FirebaseAuth.getInstance()
            val currentUser = auth.currentUser
            val authResult = if (currentUser != null && currentUser.isAnonymous) {
                val oldAnonUid = currentUser.uid
                try {
                    currentUser.linkWithCredential(credential).await()
                } catch (_: com.google.firebase.auth.FirebaseAuthUserCollisionException) {
                    val signedIn = auth.signInWithCredential(credential).await()
                    if (signedIn.user?.uid != oldAnonUid) {
                        runCatching {
                            com.google.firebase.firestore.FirebaseFirestore.getInstance()
                                .collection("users")
                                .document(oldAnonUid)
                                .delete()
                        }
                    }
                    signedIn
                }
            } else {
                auth.signInWithCredential(credential).await()
            }
            val firebaseUser = authResult.user
            completeGoogleLoginForFirebaseUser(
                email = firebaseUser?.email ?: fallbackEmail,
                displayName = firebaseUser?.displayName ?: fallbackName
            )
        } catch (e: Exception) {
            Result.failure(Exception("Gagal masuk dengan Google: ${e.message ?: "kesalahan autentikasi"}"))
        }
    }

    suspend fun completeGoogleLoginForFirebaseUser(
        email: String?,
        displayName: String?
    ): Result<Boolean> = withContext(Dispatchers.IO) {
        try {
            ensureInitialUserSeeded()
            val firebaseUser = runCatching {
                com.google.firebase.auth.FirebaseAuth.getInstance().currentUser
            }.getOrNull()
            val resolvedEmail = email?.trim()?.takeIf { it.isNotBlank() }
                ?: firebaseUser?.email?.trim()?.takeIf { it.isNotBlank() }
                ?: firebaseUser?.providerData?.firstNotNullOfOrNull { info ->
                    info.email?.trim()?.takeIf { it.isNotBlank() }
                }
            val cleanEmail = resolvedEmail?.lowercase(Locale.ROOT)
                ?: return@withContext Result.failure(Exception("Email akun Google tidak tersedia."))
            val resolvedName = displayName?.trim()?.takeIf { it.isNotBlank() }
                ?: firebaseUser?.displayName?.trim()?.takeIf { it.isNotBlank() }
            val cleanName = com.example.util.NameUtils.sanitizeDisplayName(
                resolvedName,
                fallback = cleanEmail.substringBefore("@")
            )

            val existing = accountDao.getAccountByEmail(cleanEmail)
            val accountId = if (existing != null) {
                sessionManager.saveSession(
                    accountId = existing.id,
                    email = existing.email,
                    displayName = existing.displayName
                )
                existing.id
            } else {
                val regResult = registerLocalAccount(
                    name = cleanName,
                    email = cleanEmail,
                    password = UUID.randomUUID().toString()
                )
                if (regResult.isFailure) {
                    return@withContext Result.failure(
                        regResult.exceptionOrNull() ?: Exception("Gagal membuat akun lokal Google.")
                    )
                }
                regResult.getOrThrow().id
            }

            val profile = userDao.getUserProfileOnce(accountId)
            Result.success(profile?.isOnboarded == true)
        } catch (e: Exception) {
            Result.failure(Exception("Gagal masuk dengan Google: ${e.message ?: "kesalahan autentikasi"}"))
        }
    }

    suspend fun resetLocalAccountPassword(
        email: String,
        displayName: String,
        newPassword: String
    ): Result<Unit> = withContext(Dispatchers.IO) {
        val cleanEmail = email.trim().lowercase(Locale.ROOT)
        val account = accountDao.getAccountByEmail(cleanEmail)
        if (account == null || !account.displayName.trim().equals(displayName.trim(), ignoreCase = true)) {
            return@withContext Result.failure(Exception("Data tidak cocok dengan akun manapun."))
        }
        val newSalt = PasswordHasher.generateSalt()
        val newHash = PasswordHasher.hashPassword(newPassword, newSalt)
        accountDao.insertAccount(account.copy(passwordHash = newHash, passwordSalt = newSalt))
        Result.success(Unit)
    }

    private fun signOutCloudSessions() {
        runCatching {
            com.google.firebase.auth.FirebaseAuth.getInstance().signOut()
        }
    }

    suspend fun logoutLocalAccount() = withContext(Dispatchers.IO) {
        signOutCloudSessions()
        sessionManager.clearSession()
    }

    suspend fun deleteCurrentAccount(): Result<Unit> = withContext(Dispatchers.IO) {
        val accountId = sessionManager.accountId.first() ?: return@withContext Result.failure(Exception("Tidak ada akun aktif."))
        // Best-effort cleanup of own Firestore profile document while still signed in
        runCatching {
            val uid = com.google.firebase.auth.FirebaseAuth.getInstance().currentUser?.uid
            if (!uid.isNullOrBlank()) {
                com.google.firebase.firestore.FirebaseFirestore.getInstance()
                    .collection("users")
                    .document(uid)
                    .delete()
            }
        }
        signOutCloudSessions()
        accountDao.deleteAccountById(accountId)
        userDao.deleteUserProfile(accountId)
        progressDao.deleteAllForAccount(accountId)
        bookmarkDao.deleteAllForAccount(accountId)
        certDao.deleteAllForAccount(accountId)
        playDao.deleteAllForAccount(accountId)
        notifDao.deleteAllForAccount(accountId)
        projectSubmissionDao.deleteAllForAccount(accountId)
        sessionManager.clearSession()
        Result.success(Unit)
    }

    suspend fun hasAnyLocalAccount(): Boolean = withContext(Dispatchers.IO) {
        accountDao.getAccountCount() > 0
    }

    suspend fun toggleDarkMode(isDark: Boolean) = withContext(Dispatchers.IO) {
        val accountId = getActiveAccountId()
        userDao.setDarkMode(accountId, isDark)
    }

    suspend fun markNotificationAsRead(id: String) = withContext(Dispatchers.IO) {
        val accountId = getActiveAccountId()
        notifDao.markAsRead(accountId, id)
    }

    suspend fun markAllNotificationsAsRead() = withContext(Dispatchers.IO) {
        val accountId = getActiveAccountId()
        notifDao.markAllAsRead(accountId)
    }

    // Dynamic badges evaluation based on database stats
    fun getBadges(): Flow<List<BadgeItem>> {
        return combine(userProfile, allProgress, certificates, bookmarks) { user, progress, certs, bmarks ->
            val totalCompleted = progress.count { it.isCompleted }
            val streak = user?.currentStreak ?: 0
            val level = user?.level ?: 1
            val certCount = certs.size

            CourseCatalog.allBadges.map { badge ->
                val unlocked = when (badge.id) {
                    "b1" -> totalCompleted >= 1 // Langkah Pertama
                    "b2" -> totalCompleted >= 3 // Pelajar Baru
                    "b3" -> streak >= 7 // Konsisten 7 hari
                    "b4" -> streak >= 30 // Api Abadi 30 hari
                    "b5" -> progress.map { it.courseId }.distinct().size >= 2 // Petualang Web
                    "b6" -> certs.any { it.scorePercent == 100 } // Penguji Ulung
                    "b7" -> user?.dailyQuestCompleted == true && streak >= 7 // Pejuang Misi
                    "b8" -> level >= 10 // Code Master
                    "b9" -> certCount >= 3 // Kolektor
                    "b10" -> totalCompleted >= 3 && (user?.totalXp ?: 0) >= 60 // Pembelajar Aktif
                    "b11" -> bmarks.isNotEmpty() // Penyelamat
                    "b12" -> {
                        val webDevCourseIds = setOf("fe-web-foundation-00", "fe-css-02", "fe-js-03")
                        webDevCourseIds.all { cid ->
                            progress.any { it.courseId == cid && it.isCompleted }
                        }
                    } // Web Master
                    else -> false
                }
                badge.copy(isUnlocked = unlocked)
            }
        }
    }

    private fun getTodayDateString(): String {
        return SimpleDateFormat("yyyy-MM-dd", Locale.US).format(Date())
    }

    // ==========================================
    // FORUM DISKUSI PER KELAS (OFFLINE-FIRST + FIRESTORE)
    // ==========================================

    fun getForumPosts(courseId: String): Flow<List<ForumPostEntity>> {
        return forumPostDao.getPostsByCourse(courseId)
    }

    suspend fun refreshForumPosts(
        courseId: String,
        limit: Long = 20L
    ): Result<Pair<List<ForumPostEntity>, com.google.firebase.firestore.DocumentSnapshot?>> = withContext(Dispatchers.IO) {
        val result = firestoreService.fetchPosts(courseId, null, limit)
        if (result.isSuccess) {
            val (posts, _) = result.getOrThrow()
            if (posts.isNotEmpty()) {
                forumPostDao.insertPosts(posts)
            }
        }
        result
    }

    suspend fun loadMoreForumPosts(
        courseId: String,
        lastDoc: com.google.firebase.firestore.DocumentSnapshot?,
        limit: Long = 20L
    ): Result<Pair<List<ForumPostEntity>, com.google.firebase.firestore.DocumentSnapshot?>> = withContext(Dispatchers.IO) {
        val result = firestoreService.fetchPosts(courseId, lastDoc, limit)
        if (result.isSuccess) {
            val (posts, _) = result.getOrThrow()
            if (posts.isNotEmpty()) {
                forumPostDao.insertPosts(posts)
            }
        }
        result
    }

    suspend fun createForumPost(
        courseId: String,
        content: String,
        replyToPostId: String? = null,
        replyToAuthorName: String? = null,
        replyToContentSnippet: String? = null
    ): Result<ForumPostEntity> = withContext(Dispatchers.IO) {
        val accountId = getActiveAccountId()
        val user = userDao.getUserProfileOnce(accountId)
        val authorName = user?.name ?: "Siswa"
        val authorId = accountId.toString()
        val authorLevel = user?.level ?: 1

        val cloudResult = firestoreService.createPost(
            courseId = courseId,
            authorId = authorId,
            authorName = authorName,
            content = content,
            authorLevel = authorLevel,
            replyToPostId = replyToPostId,
            replyToAuthorName = replyToAuthorName,
            replyToContentSnippet = replyToContentSnippet
        )

        val entity = if (cloudResult.isSuccess) {
            cloudResult.getOrThrow()
        } else {
            // Local offline fallback
            ForumPostEntity(
                postId = UUID.randomUUID().toString(),
                courseId = courseId,
                authorId = authorId,
                authorName = authorName,
                content = content.trim(),
                timestamp = System.currentTimeMillis(),
                likeCount = 0,
                isLikedByMe = false,
                replyToPostId = replyToPostId,
                replyToAuthorName = replyToAuthorName,
                replyToContentSnippet = replyToContentSnippet,
                authorLevel = authorLevel,
                authorIsMentor = false
            )
        }

        forumPostDao.insertPost(entity)
        Result.success(entity)
    }

    suspend fun toggleLikePost(
        postId: String,
        currentLikes: Int,
        isLiked: Boolean
    ) = withContext(Dispatchers.IO) {
        val newLikes = if (isLiked) currentLikes + 1 else (currentLikes - 1).coerceAtLeast(0)
        forumPostDao.updateLikeStatus(postId, newLikes, isLiked)
        firestoreService.toggleLikePost(postId, isLiked)
    }

    // ==========================================
    // LEADERBOARD (CLOUD DATA + ROOM CACHE MULTI-SCOPE)
    // ==========================================

    fun getCachedLeaderboard(scopeKey: String = "weekly_global"): Flow<List<LeaderboardEntryCacheEntity>> {
        return leaderboardDao.getCachedLeaderboard(scopeKey)
    }

    suspend fun refreshLeaderboard(
        period: String = "weekly",
        trackId: String? = null
    ): Result<List<LeaderboardEntryCacheEntity>> = withContext(Dispatchers.IO) {
        val scopeKey = "${period}_${trackId ?: "global"}"
        // Sync our own user XP first
        syncMyProfileToCloud()

        val result = try {
            withTimeout(3500L) {
                firestoreService.fetchTopLeaderboard(period = period, trackId = trackId, limit = 50L)
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
        if (result.isSuccess) {
            val entries = result.getOrThrow()
            if (entries.isNotEmpty()) {
                leaderboardDao.replaceLeaderboard(scopeKey, entries)
                Result.success(entries)
            } else {
                // If cloud has no entries yet, seed default peers and current user into Room
                seedDefaultLeaderboardIfEmpty(scopeKey, period, trackId)
                val cached = leaderboardDao.getCachedLeaderboard(scopeKey).first()
                Result.success(cached)
            }
        } else {
            // Offline fallback: ensure default cache exists
            seedDefaultLeaderboardIfEmpty(scopeKey, period, trackId)
            val cached = leaderboardDao.getCachedLeaderboard(scopeKey).first()
            Result.success(cached)
        }
    }

    suspend fun calculateUserRank(
        userXp: Int,
        period: String = "weekly",
        trackId: String? = null
    ): Int = withContext(Dispatchers.IO) {
        val scopeKey = "${period}_${trackId ?: "global"}"
        val accountId = getActiveAccountId().toString()
        val cached = leaderboardDao.getCachedLeaderboard(scopeKey).first()
        val myCachedEntry = cached.find { it.userId == accountId }
        if (myCachedEntry != null) {
            return@withContext myCachedEntry.rank
        }

        // If outside top 50, use Firestore count() aggregation query
        val rankResult = firestoreService.calculateUserRank(userXp, period, trackId)
        if (rankResult.isSuccess) {
            rankResult.getOrThrow()
        } else {
            // Estimate based on local cache
            val higherInCache = cached.count { it.totalXp > userXp }
            maxOf(higherInCache + 1, 1)
        }
    }

    suspend fun syncMyProfileToCloud() = withContext(Dispatchers.IO) {
        try {
            val accountId = getActiveAccountId()
            val user = userDao.getUserProfileOnce(accountId) ?: return@withContext
            val cloudUserId = firestoreService.tryEnsureAnonymousOrCurrentUid() ?: accountId.toString()
            firestoreService.syncUserXp(
                userId = cloudUserId,
                userName = com.example.util.NameUtils.sanitizeDisplayName(user.name),
                totalXp = user.totalXp,
                level = user.level,
                currentStreak = user.currentStreak,
                xpThisWeek = user.totalXp,
                xpThisMonth = user.totalXp,
                primaryTrack = user.selectedTracks.split(",").firstOrNull()?.ifBlank { "front-end" } ?: "front-end"
            )
        } catch (e: Exception) {
            android.util.Log.e("CodeAcademyRepo", "Gagal sinkronisasi XP ke Firestore", e)
        }
    }

    private suspend fun seedDefaultLeaderboardIfEmpty(
        scopeKey: String,
        period: String,
        trackId: String?
    ) {
        val existing = leaderboardDao.getCachedLeaderboard(scopeKey).first()
        if (existing.isEmpty()) {
            val accountId = getActiveAccountId()
            val currentUser = userDao.getUserProfileOnce(accountId)
            val baseXp = currentUser?.totalXp ?: 100
            val myLevel = currentUser?.level ?: 1
            val myName = currentUser?.name ?: "Siswa"

            val isStreakMode = period == "streak"
            val multiplier = when (period) {
                "weekly" -> 0.35f
                "monthly" -> 0.75f
                else -> 1.0f
            }

            val trackPrefix = if (trackId != null && trackId != "global") " [$trackId]" else ""

            val basePeers = if (isStreakMode) {
                mutableListOf(
                    LeaderboardEntryCacheEntity(scopeKey, "peer_1", "Farhan Maulana$trackPrefix", 42, 7, 1),
                    LeaderboardEntryCacheEntity(scopeKey, "peer_2", "Siti Nurhaliza$trackPrefix", 35, 6, 2),
                    LeaderboardEntryCacheEntity(scopeKey, "peer_4", "Andi Wijaya$trackPrefix", 28, 4, 3),
                    LeaderboardEntryCacheEntity(scopeKey, "peer_5", "Clarissa Chen$trackPrefix", 21, 4, 4),
                    LeaderboardEntryCacheEntity(scopeKey, "peer_6", "Bima Nugraha$trackPrefix", 14, 3, 5),
                    LeaderboardEntryCacheEntity(scopeKey, "peer_7", "Alya Zahra$trackPrefix", 10, 3, 6),
                    LeaderboardEntryCacheEntity(scopeKey, "peer_8", "Dimas Pratama$trackPrefix", 7, 2, 7),
                    LeaderboardEntryCacheEntity(scopeKey, "peer_9", "Tiara Amanda$trackPrefix", 5, 2, 8),
                    LeaderboardEntryCacheEntity(scopeKey, "peer_10", "Reza Rahardian$trackPrefix", 3, 2, 9),
                )
            } else {
                mutableListOf(
                    LeaderboardEntryCacheEntity(scopeKey, "peer_1", "Farhan Maulana$trackPrefix", (3450 * multiplier).toInt() + 20, 7, 1),
                    LeaderboardEntryCacheEntity(scopeKey, "peer_2", "Siti Nurhaliza$trackPrefix", (2980 * multiplier).toInt() + 15, 6, 2),
                    LeaderboardEntryCacheEntity(scopeKey, "peer_4", "Andi Wijaya$trackPrefix", (1950 * multiplier).toInt() + 10, 4, 3),
                    LeaderboardEntryCacheEntity(scopeKey, "peer_5", "Clarissa Chen$trackPrefix", (1720 * multiplier).toInt() + 10, 4, 4),
                    LeaderboardEntryCacheEntity(scopeKey, "peer_6", "Bima Nugraha$trackPrefix", (1540 * multiplier).toInt() + 5, 3, 5),
                    LeaderboardEntryCacheEntity(scopeKey, "peer_7", "Alya Zahra$trackPrefix", (1310 * multiplier).toInt() + 5, 3, 6),
                    LeaderboardEntryCacheEntity(scopeKey, "peer_8", "Dimas Pratama$trackPrefix", (1120 * multiplier).toInt() + 5, 2, 7),
                    LeaderboardEntryCacheEntity(scopeKey, "peer_9", "Tiara Amanda$trackPrefix", (980 * multiplier).toInt() + 5, 2, 8),
                    LeaderboardEntryCacheEntity(scopeKey, "peer_10", "Reza Rahardian$trackPrefix", (760 * multiplier).toInt() + 5, 2, 9),
                )
            }
            val userScore = if (isStreakMode) {
                currentUser?.currentStreak ?: 0
            } else {
                (baseXp * multiplier).toInt().coerceAtLeast(20)
            }
            basePeers.add(
                LeaderboardEntryCacheEntity(
                    scopeKey = scopeKey,
                    userId = accountId.toString(),
                    userName = myName,
                    totalXp = userScore,
                    level = myLevel,
                    rank = 10
                )
            )
            val sorted = basePeers.sortedByDescending { it.totalXp }.mapIndexed { idx, it ->
                it.copy(rank = idx + 1)
            }
            leaderboardDao.replaceLeaderboard(scopeKey, sorted)
        }
    }

    // ==========================================
    // WEEKLY CHALLENGES (ROOM + REAL PROGRESS)
    // ==========================================

    val activeWeeklyChallenge: Flow<WeeklyChallengeEntity?> = weeklyChallengeDao.getActiveWeeklyChallenge()

    suspend fun ensureWeeklyChallengeSeeded(): WeeklyChallengeEntity = withContext(Dispatchers.IO) {
        val existing = weeklyChallengeDao.getActiveWeeklyChallengeOnce()
        if (existing != null) return@withContext existing

        val defaultChallenge = com.example.data.local.WeeklyChallengeEntity(
            "wc_2026_w38_mod3",
            "Tantangan Mingguan: Master Modul",
            "Selesaikan 3 modul pembelajaran minggu ini untuk raih bonus +100 XP!",
            3,
            100,
            "MODULES_COMPLETED",
            System.currentTimeMillis() - 86400000L,
            System.currentTimeMillis() + 6 * 86400000L,
            false
        )
        weeklyChallengeDao.insertChallenge(defaultChallenge)
        defaultChallenge
    }

    suspend fun claimWeeklyChallengeReward(challengeId: String): Boolean = withContext(Dispatchers.IO) {
        val challenge = weeklyChallengeDao.getActiveWeeklyChallengeOnce() ?: return@withContext false
        if (challenge.id == challengeId && !challenge.isClaimed) {
            weeklyChallengeDao.markClaimed(challengeId)
            val accountId = getActiveAccountId()
            val user = userDao.getUserProfileOnce(accountId)
            if (user != null) {
                val newXp = user.totalXp + challenge.rewardXp
                val newLevel = GamificationLevels.getLevelForXp(newXp).level
                userDao.updateXpAndLevel(accountId, newXp, newLevel)
            }
            return@withContext true
        }
        false
    }

    // ==========================================
    // SOROTAN FORUM (HIGHLIGHTS)
    // ==========================================

    suspend fun getTopForumHighlights(limit: Long = 3L): List<ForumPostEntity> = withContext(Dispatchers.IO) {
        val cloudResult = firestoreService.fetchTopForumHighlights(limit)
        if (cloudResult.isSuccess) {
            return@withContext cloudResult.getOrThrow()
        }
        emptyList()
    }

    suspend fun ensureCloudAuthSession(): String? = withContext(Dispatchers.IO) {
        firestoreService.tryEnsureAnonymousOrCurrentUid()
    }

    suspend fun syncCloudAnnouncements() = withContext(Dispatchers.IO) {
        try {
            firestoreService.tryEnsureAnonymousOrCurrentUid()
            val accountId = getActiveAccountId()
            val existingIds = notifDao.getNotificationIdsForAccount(accountId).toSet()
            val result = firestoreService.fetchAnnouncements(20L)
            if (result.isSuccess) {
                val announcements = result.getOrThrow()
                val fmt = SimpleDateFormat("dd MMM yyyy, HH:mm", Locale.getDefault())
                announcements.forEach { ann ->
                    val notifId = "ann_${ann.id}_${accountId}"
                    if (notifId !in existingIds) {
                        val courseTag = ann.courseId?.takeIf { it.isNotBlank() && it != "all" }
                            ?.let { cid -> getCourseById(cid)?.title ?: cid }
                        val header = if (courseTag != null) "📢 [$courseTag] ${ann.title}" else "📢 ${ann.title}"
                        notifDao.insertNotification(
                            NotificationEntity(
                                id = notifId,
                                accountId = accountId,
                                title = header,
                                message = "${ann.message} — ${ann.authorName}",
                                type = "ANNOUNCEMENT",
                                timestamp = fmt.format(Date(ann.createdAt)),
                                isRead = false
                            )
                        )
                    }
                }
            }
        } catch (_: Throwable) {
            // Silently ignore when offline
        }
    }
}


