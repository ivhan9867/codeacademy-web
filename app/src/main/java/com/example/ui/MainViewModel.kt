package com.example.ui

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.CodeAcademyApplication
import com.example.ai.GeminiTutorService
import com.example.data.CodeAcademyRepository
import com.example.data.local.BookmarkEntity
import com.example.data.local.CertificateEntity
import com.example.data.local.ForumPostEntity
import com.example.data.local.LeaderboardEntryCacheEntity
import com.example.data.local.ModuleProgressEntity
import com.example.data.local.NotificationEntity
import com.example.data.local.PlaygroundDraftEntity
import com.example.data.local.ProjectSubmissionEntity
import com.example.data.local.SectionQuizAttemptEntity
import com.example.data.local.ActiveSectionQuizSessionEntity
import com.example.data.local.UserProfileEntity
import com.example.data.local.WeeklyChallengeEntity
import com.example.data.models.BadgeItem
import com.example.data.models.Certificate
import com.example.data.models.ChatMessage
import com.example.data.models.Course
import com.example.data.models.GamificationLevels
import com.example.data.models.LearningTrack
import com.example.data.models.ModuleSection
import com.example.data.models.QuizQuestion
import com.example.data.models.SearchResultItem
import com.example.data.models.SectionQuiz
import com.example.data.models.PlacementTestQuestion
import com.example.data.models.PlacementTestResult
import com.example.data.util.CourseUnlockUtils
import com.example.util.localization.AppLanguage
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.util.UUID

enum class InitialDestination {
    LOGIN,
    ONBOARDING,
    MAIN,
    BANNED
}

class MainViewModel(application: Application) : AndroidViewModel(application) {
    private val repo = CodeAcademyRepository(
        context = application,
        externalScope = (application as? CodeAcademyApplication)?.applicationScope ?: viewModelScope
    )
    private val aiService by lazy { GeminiTutorService() }

    private var _isReplayingOnboarding = false
    val isReplayingOnboarding: Boolean get() = _isReplayingOnboarding

    private val _banStatus = kotlinx.coroutines.flow.MutableStateFlow(com.example.data.remote.AccountBanStatus())
    val banStatus: StateFlow<com.example.data.remote.AccountBanStatus> = _banStatus

    private var banListenerRegistration: com.google.firebase.firestore.ListenerRegistration? = null

    fun startBanStatusMonitor() {
        viewModelScope.launch {
            val current = repo.checkCurrentAccountBanStatus()
            _banStatus.value = current
            banListenerRegistration?.remove()
            banListenerRegistration = repo.observeCurrentAccountBanStatus { updated ->
                _banStatus.value = updated
            }
        }
    }

    suspend fun refreshBanStatusAndResolveDestination(): InitialDestination {
        val status = repo.checkCurrentAccountBanStatus()
        _banStatus.value = status
        if (status.isBanned) return InitialDestination.BANNED
        return checkInitialDestination()
    }

    override fun onCleared() {
        super.onCleared()
        banListenerRegistration?.remove()
        banListenerRegistration = null
    }

    // Data from Room
    val userProfile: StateFlow<UserProfileEntity?> = repo.userProfile.stateIn(
        scope = viewModelScope,
        started = SharingStarted.Eagerly,
        initialValue = null
    )

    fun markCoachMarkSeen(coachMarkId: String) {
        viewModelScope.launch { repo.markCoachMarkSeen(coachMarkId) }
    }

    fun hasSeenCoachMark(coachMarkId: String): Boolean =
        userProfile.value?.seenCoachMarks?.split(",")?.contains(coachMarkId) == true

    // Dedicated boolean flow for dark mode to avoid full-tree recomposition on any profile update
    val isDarkMode: StateFlow<Boolean> = repo.userProfile
        .map { it?.isDarkMode ?: true }
        .distinctUntilChanged()
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = true
        )

    val appLanguage: StateFlow<AppLanguage> = repo.sessionManager.appLanguageCode
        .map { AppLanguage.fromCode(it) }
        .distinctUntilChanged()
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.Eagerly,
            initialValue = AppLanguage.fromCode(repo.sessionManager.getCachedLanguageCode())
        )

    fun setAppLanguage(language: AppLanguage) {
        viewModelScope.launch {
            repo.sessionManager.saveAppLanguage(language.code)
            com.example.data.CourseCatalog.loadAssetCourses(getApplication(), language.code)
            val currentChats = _chatMessages.value
            if (currentChats.size == 1 && !currentChats[0].isUser) {
                _chatMessages.value = listOf(
                    ChatMessage(
                        id = currentChats[0].id,
                        text = aiGreetingFor(language),
                        isUser = false
                    )
                )
            }
        }
    }

    private fun todayIsoDate(): String {
        return java.text.SimpleDateFormat("yyyy-MM-dd", java.util.Locale.US).format(java.util.Date())
    }

    val isDailyMicroChallengeCompletedToday: StateFlow<Boolean> =
        repo.sessionManager.lastMicroChallengeDate
            .map { it == todayIsoDate() }
            .distinctUntilChanged()
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(5000),
                initialValue = false
            )

    private var isClaimingMicroChallenge = false

    fun claimDailyMicroChallenge(xpReward: Int = 15) {
        if (isClaimingMicroChallenge) return
        isClaimingMicroChallenge = true
        viewModelScope.launch {
            try {
                val today = todayIsoDate()
                if (repo.sessionManager.lastMicroChallengeDate.first() == today) {
                    return@launch
                }
                repo.sessionManager.markMicroChallengeCompleted(today)
                val (_, leveledUp) = repo.addXp(xpReward, "Daily 1-Minute Code Challenge")
                if (leveledUp) {
                    userProfile.value?.let { user ->
                        val nextLevel = GamificationLevels.getLevelForXp(user.totalXp + xpReward)
                        if (nextLevel.level > user.level) {
                            _levelUpDialogInfo.value = Pair(nextLevel.level, nextLevel.name)
                        }
                    }
                }
            } finally {
                isClaimingMicroChallenge = false
            }
        }
    }

    val moduleProgressList: StateFlow<List<ModuleProgressEntity>> = repo.allProgress.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = emptyList()
    )

    val passedSectionQuizIds: StateFlow<Set<String>> = repo.passedSectionQuizIds.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = emptySet()
    )

    /**
     * Memeriksa apakah modul tertentu terbuka pada course yang sedang dibuka
     */
    fun isModuleUnlocked(course: Course, moduleId: String): Boolean {
        val completedIds = moduleProgressList.value
            .filter { it.courseId == course.id && it.isCompleted }
            .map { it.moduleId }
            .toSet()
        return CourseUnlockUtils.isModuleUnlocked(course, moduleId, completedIds, passedSectionQuizIds.value)
    }

    /**
     * Mengembalikan peta status unlock (moduleId -> unlocked) untuk seluruh modul pada course
     */
    fun getModuleUnlockMap(course: Course): Map<String, Boolean> {
        val completedIds = moduleProgressList.value
            .filter { it.courseId == course.id && it.isCompleted }
            .map { it.moduleId }
            .toSet()
        val passedQuizzes = passedSectionQuizIds.value
        return course.modules.associate { module ->
            module.id to CourseUnlockUtils.isModuleUnlocked(course, module.id, completedIds, passedQuizzes)
        }
    }

    val bookmarks: StateFlow<List<BookmarkEntity>> = repo.bookmarks.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = emptyList()
    )

    val certificates: StateFlow<List<CertificateEntity>> = repo.certificates.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = emptyList()
    )

    val projectSubmissions: StateFlow<List<ProjectSubmissionEntity>> = repo.projectSubmissions.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = emptyList()
    )

    fun getProjectSubmissionFlow(courseId: String): Flow<ProjectSubmissionEntity?> =
        repo.getProjectSubmissionFlow(courseId)

    val notifications: StateFlow<List<NotificationEntity>> = repo.notifications.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = emptyList()
    )

    val unreadNotifsCount: StateFlow<Int> = repo.unreadNotifsCount.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = 0
    )

    val badges: StateFlow<List<BadgeItem>> = repo.getBadges().stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = emptyList()
    )

    // Session State
    val isLoggedIn: StateFlow<Boolean> = repo.sessionManager.isLoggedIn.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = false
    )

    val currentAccountEmail: StateFlow<String> = repo.sessionManager.currentAccountEmail.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = ""
    )

    val currentDisplayName: StateFlow<String> = repo.sessionManager.currentDisplayName.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = ""
    )

    // Level up popup celebration
    private val _levelUpDialogInfo = MutableStateFlow<Pair<Int, String>?>(null)
    val levelUpDialogInfo: StateFlow<Pair<Int, String>?> = _levelUpDialogInfo.asStateFlow()

    private fun aiGreetingFor(lang: AppLanguage): String = when (lang) {
        AppLanguage.ENGLISH -> "Hi! I'm the CodeAcademy AI Tutor. Any coding concept or bug you'd like to explore today? 🚀"
        AppLanguage.JAPANESE -> "こんにちは！CodeAcademy AIチューターです。今日質問したいプログラミングの概念やコードはありますか？🚀"
        AppLanguage.CHINESE -> "你好！我是 CodeAcademy AI 导师。今天有什么编程概念或代码问题想问吗？🚀"
        AppLanguage.INDONESIAN -> "Halo! Saya AI Tutor CodeAcademy Indonesia. Ada konsep atau kode pemrograman yang ingin kamu tanyakan hari ini? 🚀"
    }

    // AI Tutor Chat state
    private val _chatMessages = MutableStateFlow<List<ChatMessage>>(
        listOf(
            ChatMessage(
                UUID.randomUUID().toString(),
                aiGreetingFor(appLanguage.value),
                false
            )
        )
    )
    val chatMessages: StateFlow<List<ChatMessage>> = _chatMessages.asStateFlow()

    private val _isAiThinking = MutableStateFlow(false)
    val isAiThinking: StateFlow<Boolean> = _isAiThinking.asStateFlow()

    private val _aiConnectionStatus = MutableStateFlow("Gemini Live")
    val aiConnectionStatus: StateFlow<String> = _aiConnectionStatus.asStateFlow()

    // Course Catalog Search & Filter States
    private val _searchKeyword = MutableStateFlow("")
    val searchKeyword: StateFlow<String> = _searchKeyword.asStateFlow()

    private val _selectedLevelFilter = MutableStateFlow("Semua")
    val selectedLevelFilter: StateFlow<String> = _selectedLevelFilter.asStateFlow()

    private val _selectedCategoryFilter = MutableStateFlow<String?>(null)
    val selectedCategoryFilter: StateFlow<String?> = _selectedCategoryFilter.asStateFlow()

    private val _selectedTabFilter = MutableStateFlow(0)
    val selectedTabFilter: StateFlow<Int> = _selectedTabFilter.asStateFlow()

    // Reactive catalog courses so module counts update immediately once asset courses finish loading
    val catalogCourses: StateFlow<List<Course>> = com.example.data.CourseCatalog.coursesState

    // Pre-filtered courses combined from live catalog, keyword (debounced 250ms), level, category, tab, and progress
    @OptIn(FlowPreview::class)
    val filteredCourses: StateFlow<List<Course>> = combine(
        combine(
            _searchKeyword.debounce(250L),
            _selectedLevelFilter,
            _selectedCategoryFilter,
            _selectedTabFilter
        ) { query, level, category, tab ->
            listOf(query, level, category, tab)
        },
        moduleProgressList,
        com.example.data.CourseCatalog.coursesState
    ) { filterArgs, progressList, liveCourses ->
        val query = filterArgs[0] as String
        val level = filterArgs[1] as String
        val category = filterArgs[2] as String?
        val tab = filterArgs[3] as Int
        val sourceCourses = liveCourses.ifEmpty { repo.getAllCourses() }
        val completedKeys = progressList.filter { it.isCompleted }.map { it.courseId to it.moduleId }.toSet()
        sourceCourses.filter { course ->
            val matchesQuery = query.isBlank() ||
                    course.title.contains(query, ignoreCase = true) ||
                    course.description.contains(query, ignoreCase = true)

            val matchesTrack = category == null || course.trackId == category
            val matchesLevel = level.isBlank() || level == "Semua" || level == "All" || level == "ALL" || course.level == level

            val completedCount = course.modules.count { mod ->
                (course.id to mod.id) in completedKeys
            }
            val isCompleted = completedCount > 0 && completedCount == course.modules.size
            val isStarted = completedCount > 0 && !isCompleted

            val matchesTab = when (tab) {
                1 -> isStarted // Sedang Dipelajari
                2 -> isCompleted // Selesai
                else -> true // Semua
            }

            matchesQuery && matchesTrack && matchesLevel && matchesTab
        }
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = repo.getAllCourses()
    )

    fun setSearchKeyword(keyword: String) {
        _searchKeyword.value = keyword
    }

    fun setSelectedLevelFilter(level: String?) {
        _selectedLevelFilter.value = level ?: "Semua"
    }

    fun setSelectedCategoryFilter(category: String?) {
        _selectedCategoryFilter.value = category
    }

    fun setSelectedTabFilter(tabIndex: Int) {
        _selectedTabFilter.value = tabIndex
    }

    // ================= Global Cross-Course Module Search =================
    private val _moduleSearchQuery = MutableStateFlow("")
    val moduleSearchQuery: StateFlow<String> = _moduleSearchQuery.asStateFlow()

    fun setModuleSearchQuery(query: String) {
        _moduleSearchQuery.value = query
    }

    val isCoursesLoaded: StateFlow<Boolean> get() = repo.isCoursesLoaded

    val isCoursesLoading: StateFlow<Boolean> = repo.isCoursesLoaded
        .map { isLoaded: Boolean -> !isLoaded }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.Eagerly,
            initialValue = !repo.isCoursesLoaded.value
        )

    @OptIn(FlowPreview::class)
    val moduleSearchResults: StateFlow<List<SearchResultItem>> = combine(
        _moduleSearchQuery.debounce(300L),
        repo.coursesFlow,
        repo.isCoursesLoaded
    ) { query: String, courses: List<Course>, isLoaded: Boolean ->
        val cleanQuery = query.trim()
        if (cleanQuery.isEmpty() || !isLoaded) {
            emptyList<SearchResultItem>()
        } else {
            performModuleSearch(courses, cleanQuery)
        }
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = emptyList()
    )

    private fun performModuleSearch(courses: List<Course>, query: String): List<SearchResultItem> {
        val results = mutableListOf<SearchResultItem>()
        for (course in courses) {
            for (module in course.modules) {
                val titleMatches = module.title.contains(query, ignoreCase = true)
                val contentMatchIndex = module.content.indexOf(query, ignoreCase = true)
                val contentMatches = contentMatchIndex != -1

                if (titleMatches || contentMatches) {
                    val snippet = if (contentMatches) {
                        extractSnippet(module.content, contentMatchIndex, query.length)
                    } else {
                        null
                    }

                    results.add(
                        SearchResultItem(
                            courseId = course.id,
                            courseTitle = course.title,
                            moduleId = module.id,
                            moduleTitle = module.title,
                            moduleOrder = module.order,
                            section = module.section,
                            matchedInTitle = titleMatches,
                            matchedInContent = contentMatches,
                            snippet = snippet,
                            query = query
                        )
                    )
                }
            }
        }
        return results
    }

    private fun extractSnippet(content: String, matchIndex: Int, queryLength: Int): String {
        val surroundingRadius = 30
        val startIdx = (matchIndex - surroundingRadius).coerceAtLeast(0)
        val endIdx = (matchIndex + queryLength + surroundingRadius).coerceAtMost(content.length)

        val rawSnippet = content.substring(startIdx, endIdx)
            .replace("\r\n", " ")
            .replace("\n", " ")
            .replace(Regex("\\s+"), " ")
            .trim()

        val prefix = if (startIdx > 0) "... " else ""
        val suffix = if (endIdx < content.length) " ..." else ""

        return "$prefix$rawSnippet$suffix"
    }

    /**
     * Unified readiness gate that combines memory image cache warming,
     * initial database / user profile seeding, and pre-fetching initial destination
     * before the splash screen transitions.
     */
    suspend fun prepareAppReadiness(context: android.content.Context): InitialDestination = coroutineScope {
        val imageWarmJob = async(Dispatchers.IO) {
            try {
                com.example.util.ImageCacheHelper.preloadAppImages(context)
            } catch (e: Exception) {
                android.util.Log.w("MainViewModel", "Image preload skipped: ${e.message}")
            }
        }
        val seedJob = async(Dispatchers.IO) {
            try {
                repo.ensureInitialUserSeeded()
            } catch (e: Exception) {
                android.util.Log.w("MainViewModel", "Initial seeding skipped: ${e.message}")
            }
        }
        val catalogJob = async(Dispatchers.IO) {
            try {
                if (!com.example.data.CourseCatalog.isAssetCoursesLoaded.value) {
                    com.example.data.CourseCatalog.loadAssetCourses(context, appLanguage.value.code)
                }
            } catch (e: Exception) {
                android.util.Log.w("MainViewModel", "Catalog preload skipped: ${e.message}")
            }
        }
        imageWarmJob.await()
        seedJob.await()
        catalogJob.await()

        // Resolve destination while still under splash gate
        checkInitialDestination()
    }

    fun startReplayingOnboarding() {
        _isReplayingOnboarding = true
    }

    fun cancelReplayingOnboarding() {
        _isReplayingOnboarding = false
    }

    suspend fun checkInitialDestination(): InitialDestination = withContext(Dispatchers.IO) {
        val loggedIn = repo.sessionManager.isLoggedIn.first()
        if (!loggedIn) {
            return@withContext InitialDestination.LOGIN
        }
        val activeId = repo.sessionManager.accountId.first()
        val profile = repo.getUserProfileOnce(activeId)
        val status = repo.checkCurrentAccountBanStatus(profile?.email)
        _banStatus.value = status
        if (status.isBanned) {
            return@withContext InitialDestination.BANNED
        }
        startBanStatusMonitor()
        val isOnboarded = profile?.isOnboarded ?: false
        if (isOnboarded) {
            InitialDestination.MAIN
        } else {
            InitialDestination.ONBOARDING
        }
    }

    suspend fun completeOnboarding(selectedTrackId: String) = withContext(Dispatchers.IO) {
        repo.completeOnboarding(selectedTrackId)
        _isReplayingOnboarding = false
    }

    suspend fun register(name: String, email: String, password: String): Result<Boolean> = withContext(Dispatchers.IO) {
        val result = repo.registerLocalAccount(name, email, password)
        if (result.isSuccess) {
            val account = result.getOrNull()
            val profile = repo.getUserProfileOnce(account?.id)
            val isOnboarded = profile?.isOnboarded ?: false
            Result.success(isOnboarded)
        } else {
            Result.failure(result.exceptionOrNull() ?: Exception("Registrasi gagal."))
        }
    }

    suspend fun login(email: String, password: String): Result<Boolean> = withContext(Dispatchers.IO) {
        val result = repo.loginLocalAccount(email, password)
        if (result.isSuccess) {
            val account = result.getOrNull()
            val ban = repo.checkCurrentAccountBanStatus(account?.email ?: email)
            _banStatus.value = ban
            startBanStatusMonitor()
            val profile = repo.getUserProfileOnce(account?.id)
            val isOnboarded = profile?.isOnboarded ?: false
            Result.success(isOnboarded)
        } else {
            Result.failure(result.exceptionOrNull() ?: Exception("Login gagal."))
        }
    }

    suspend fun loginWithGoogle(
        idToken: String,
        displayName: String?,
        email: String?
    ): Result<Boolean> {
        val res = repo.loginWithGoogleIdToken(idToken, displayName, email)
        if (res.isSuccess) {
            _banStatus.value = repo.checkCurrentAccountBanStatus(email)
            startBanStatusMonitor()
        }
        return res
    }

    suspend fun completeGoogleLogin(
        email: String?,
        displayName: String?
    ): Result<Boolean> {
        val res = repo.completeGoogleLoginForFirebaseUser(email, displayName)
        if (res.isSuccess) {
            _banStatus.value = repo.checkCurrentAccountBanStatus(email)
            startBanStatusMonitor()
        }
        return res
    }

    suspend fun logout() = withContext(Dispatchers.IO) {
        banListenerRegistration?.remove()
        banListenerRegistration = null
        _banStatus.value = com.example.data.remote.AccountBanStatus()
        repo.logoutLocalAccount()
    }

    suspend fun deleteAccount(): Result<Unit> = withContext(Dispatchers.IO) {
        repo.deleteCurrentAccount()
    }

    fun getAllTracks(): List<LearningTrack> = repo.getAllTracks()
    fun getAllCourses(): List<Course> = repo.getAllCourses()
    fun getCourseById(courseId: String): Course? = repo.getCourseById(courseId)

    fun completeModule(courseId: String, moduleId: String) {
        viewModelScope.launch {
            val isNewlyCompleted = repo.completeModule(courseId, moduleId)
            if (isNewlyCompleted) {
                // Check if user leveled up
                userProfile.value?.let { user ->
                    val nextLevel = GamificationLevels.getLevelForXp(user.totalXp)
                    if (nextLevel.level > user.level) {
                        _levelUpDialogInfo.value = Pair(nextLevel.level, nextLevel.name)
                    }
                }
            }
        }
    }

    // Module playground code verification state
    private val _verifiedModuleIds = MutableStateFlow<Set<String>>(emptySet())
    val verifiedModuleIds: StateFlow<Set<String>> = _verifiedModuleIds.asStateFlow()

    fun markModuleVerificationPassed(moduleId: String) {
        _verifiedModuleIds.value = _verifiedModuleIds.value + moduleId
    }

    fun isModuleVerificationPassed(moduleId: String): Boolean {
        return _verifiedModuleIds.value.contains(moduleId)
    }

    // Playground drafts management
    suspend fun getPlaygroundDraft(courseId: String): PlaygroundDraftEntity? {
        return repo.getPlaygroundDraft(courseId)
    }

    fun savePlaygroundDraft(courseId: String, html: String, css: String, js: String) {
        viewModelScope.launch {
            repo.savePlaygroundDraft(courseId, html, css, js)
        }
    }

    suspend fun clearPlaygroundDraft(courseId: String) {
        repo.clearPlaygroundDraft(courseId)
    }

    fun submitQuiz(courseId: String, scorePercent: Int, onResult: (Boolean, String?) -> Unit) {
        viewModelScope.launch {
            val cert = repo.submitQuiz(courseId, scorePercent)
            val isPassed = scorePercent >= 70
            onResult(isPassed, cert?.certId)
        }
    }

    fun ensureProjectSubmissionInitialized(courseId: String) {
        viewModelScope.launch {
            repo.ensureProjectSubmissionInitialized(courseId)
        }
    }

    fun saveProjectSubmissionDraft(
        courseId: String,
        workspaceCode: String,
        projectUrl: String,
        studentNotes: String,
        checkedRubricIds: Set<String>,
        onSaved: () -> Unit = {}
    ) {
        viewModelScope.launch {
            repo.saveProjectSubmissionDraft(courseId, workspaceCode, projectUrl, studentNotes, checkedRubricIds)
            onSaved()
        }
    }

    fun submitFinalProject(
        courseId: String,
        workspaceCode: String,
        projectUrl: String,
        studentNotes: String,
        checkedRubricIds: Set<String>,
        onResult: (Result<ProjectSubmissionEntity>) -> Unit = {}
    ) {
        viewModelScope.launch {
            val res = repo.submitFinalProject(courseId, workspaceCode, projectUrl, studentNotes, checkedRubricIds)
            onResult(res)
        }
    }

    fun simulateFacilitatorReview(
        courseId: String,
        approved: Boolean,
        facilitatorScore: Int,
        facilitatorFeedback: String,
        facilitatorName: String = "Tim Fasilitator CodeAcademy",
        rubricScores: Map<String, Int> = emptyMap(),
        onResult: (Result<Pair<ProjectSubmissionEntity, Certificate?>>) -> Unit = {}
    ) {
        viewModelScope.launch {
            val res = repo.simulateFacilitatorReview(
                courseId = courseId,
                approved = approved,
                facilitatorScore = facilitatorScore,
                facilitatorFeedback = facilitatorFeedback,
                facilitatorName = facilitatorName,
                rubricScores = rubricScores
            )
            onResult(res)
        }
    }

    fun attachProjectSubmissionListener(
        courseId: String
    ): com.google.firebase.firestore.ListenerRegistration? {
        val accountId = userProfile.value?.accountId ?: 1L
        return repo.firestoreService.attachProjectSubmissionListener(
            accountId = accountId,
            courseId = courseId,
            onUpdate = { remote ->
                viewModelScope.launch {
                    repo.handleRemoteSubmissionUpdate(remote)
                }
            }
        )
    }

    fun getSectionQuizAttempts(quizId: String): Flow<List<SectionQuizAttemptEntity>> {
        return repo.getSectionQuizAttempts(quizId)
    }

    suspend fun getSectionQuizAttemptById(id: Long): SectionQuizAttemptEntity? {
        return repo.getSectionQuizAttemptById(id)
    }

    suspend fun getCooldownRemainingSeconds(quizId: String): Long {
        return repo.getCooldownRemainingSeconds(quizId)
    }

    suspend fun generateSectionQuizQuestions(quiz: SectionQuiz): List<QuizQuestion> {
        return repo.generateSectionQuizQuestions(quiz)
    }

    suspend fun startSectionQuizSession(
        course: Course,
        quiz: SectionQuiz,
        questions: List<QuizQuestion>
    ): ActiveSectionQuizSessionEntity {
        return repo.startSectionQuizSession(course, quiz, questions)
    }

    suspend fun getActiveSectionQuizSession(quizId: String): ActiveSectionQuizSessionEntity? {
        return repo.getActiveSectionQuizSession(quizId)
    }

    suspend fun updateActiveSectionQuizAnswers(quizId: String, answers: Map<Int, String>) {
        repo.updateActiveSectionQuizAnswers(quizId, answers)
    }

    suspend fun recordSectionQuizViolation(
        course: Course,
        quiz: SectionQuiz,
        questions: List<QuizQuestion>,
        currentAnswers: Map<Int, String>
    ): Pair<Int, SectionQuizAttemptEntity?> {
        return repo.recordSectionQuizViolation(course, quiz, questions, currentAnswers)
    }

    suspend fun clearActiveSectionQuizSession(quizId: String) {
        repo.clearActiveSectionQuizSession(quizId)
    }

    suspend fun submitSectionQuiz(
        quiz: SectionQuiz,
        questions: List<QuizQuestion>,
        userAnswersByQuestionId: Map<Int, String>,
        failReason: String? = null
    ): SectionQuizAttemptEntity {
        return repo.submitSectionQuiz(quiz, questions, userAnswersByQuestionId, failReason)
    }

    suspend fun generatePlacementTestQuestions(course: Course): List<PlacementTestQuestion> {
        return repo.generatePlacementTestQuestions(course)
    }

    suspend fun submitPlacementTest(
        course: Course,
        questions: List<PlacementTestQuestion>,
        userAnswersByQuestionId: Map<Int, String>
    ): PlacementTestResult {
        return repo.submitPlacementTest(course, questions, userAnswersByQuestionId)
    }

    fun toggleBookmark(type: String, itemId: String, title: String, subtitle: String, trackName: String) {
        viewModelScope.launch {
            repo.toggleBookmark(type, itemId, title, subtitle, trackName)
        }
    }

    fun removeBookmark(itemId: String) {
        viewModelScope.launch {
            repo.removeBookmark(itemId)
        }
    }

    fun askAi(
        userQuery: String,
        courseTitle: String,
        moduleTitle: String,
        moduleContent: String? = null,
        currentPlaygroundCode: String? = null,
        imageUri: String? = null
    ) {
        if (_isAiThinking.value) return
        val query = userQuery.ifBlank {
            if (!imageUri.isNullOrBlank()) {
                getApplication<Application>().getString(com.example.R.string.ai_tutor_default_image_prompt)
            } else {
                "Halo AI Tutor"
            }
        }
        val userMsg = ChatMessage(
            id = UUID.randomUUID().toString(),
            text = query,
            isUser = true,
            imageUri = imageUri
        )
        _chatMessages.value = _chatMessages.value + userMsg
        _isAiThinking.value = true

        viewModelScope.launch {
            try {
                var codeContext = currentPlaygroundCode
                var contentContext = moduleContent
                val course = catalogCourses.value.ifEmpty { repo.getAllCourses() }.find { it.title.equals(courseTitle, ignoreCase = true) }
                if (contentContext.isNullOrBlank() && course != null) {
                    val mod = course.modules.find { it.title.equals(moduleTitle, ignoreCase = true) }
                    contentContext = mod?.content
                }
                if (codeContext.isNullOrBlank() && course != null) {
                    val draft = repo.getPlaygroundDraft(course.id)
                    if (draft != null) {
                        codeContext = listOfNotNull(
                            draft.htmlCode.takeIf { it.isNotBlank() }?.let { "<!-- HTML -->\n$it" },
                            draft.cssCode.takeIf { it.isNotBlank() }?.let { "/* CSS */\n$it" },
                            draft.jsCode.takeIf { it.isNotBlank() }?.let { "// JS\n$it" }
                        ).joinToString("\n\n")
                    }
                }

                val compressedImageBytes = if (!imageUri.isNullOrBlank()) {
                    loadCompressedImageBytes(imageUri)
                } else {
                    null
                }

                val result = aiService.askTutorDetailed(
                    userMessage = query,
                    courseTitle = courseTitle,
                    moduleTitle = moduleTitle,
                    moduleContent = contentContext,
                    currentPlaygroundCode = codeContext,
                    language = appLanguage.value.code,
                    imageBytes = compressedImageBytes,
                    imageMimeType = "image/jpeg"
                )
                _aiConnectionStatus.value = result.provider
                val aiMsg = ChatMessage(
                    id = UUID.randomUUID().toString(),
                    text = result.text,
                    isUser = false
                )
                _chatMessages.value = _chatMessages.value + aiMsg
            } catch (e: kotlinx.coroutines.CancellationException) {
                throw e
            } catch (e: Throwable) {
                android.util.Log.e("MainViewModel", "AI Tutor communication error", e)
                val fallbackMsg = getApplication<Application>().getString(
                    com.example.R.string.ai_tutor_error_communication,
                    e.localizedMessage ?: "offline"
                )
                val errorMsg = ChatMessage(
                    UUID.randomUUID().toString(),
                    fallbackMsg,
                    false
                )
                _chatMessages.value = _chatMessages.value + errorMsg
            } finally {
                _isAiThinking.value = false
            }
        }
    }

    private suspend fun loadCompressedImageBytes(uriString: String): ByteArray? =
        kotlinx.coroutines.withContext(kotlinx.coroutines.Dispatchers.IO) {
            try {
                val resolver = getApplication<Application>().contentResolver
                val uri = android.net.Uri.parse(uriString)
                val boundsOpts = android.graphics.BitmapFactory.Options().apply {
                    inJustDecodeBounds = true
                }
                resolver.openInputStream(uri)?.use { stream ->
                    android.graphics.BitmapFactory.decodeStream(stream, null, boundsOpts)
                }
                val maxDim = 1024
                var sampleSize = 1
                val origW = boundsOpts.outWidth
                val origH = boundsOpts.outHeight
                if (origW <= 0 || origH <= 0) return@withContext null
                if (origW > maxDim || origH > maxDim) {
                    while ((origW / sampleSize) > maxDim || (origH / sampleSize) > maxDim) {
                        sampleSize *= 2
                    }
                }
                val decodeOpts = android.graphics.BitmapFactory.Options().apply {
                    inSampleSize = sampleSize
                }
                val rawBitmap = resolver.openInputStream(uri)?.use { stream ->
                    android.graphics.BitmapFactory.decodeStream(stream, null, decodeOpts)
                } ?: return@withContext null

                val rotationDegrees = try {
                    resolver.openInputStream(uri)?.use { exifStream ->
                        val exif = android.media.ExifInterface(exifStream)
                        when (exif.getAttributeInt(
                            android.media.ExifInterface.TAG_ORIENTATION,
                            android.media.ExifInterface.ORIENTATION_NORMAL
                        )) {
                            android.media.ExifInterface.ORIENTATION_ROTATE_90 -> 90f
                            android.media.ExifInterface.ORIENTATION_ROTATE_180 -> 180f
                            android.media.ExifInterface.ORIENTATION_ROTATE_270 -> 270f
                            else -> 0f
                        }
                    } ?: 0f
                } catch (_: Throwable) {
                    0f
                }

                val uprightBitmap = if (rotationDegrees != 0f) {
                    val matrix = android.graphics.Matrix().apply { postRotate(rotationDegrees) }
                    val rotated = android.graphics.Bitmap.createBitmap(
                        rawBitmap, 0, 0, rawBitmap.width, rawBitmap.height, matrix, true
                    )
                    if (rotated !== rawBitmap) rawBitmap.recycle()
                    rotated
                } else {
                    rawBitmap
                }

                try {
                    val out = java.io.ByteArrayOutputStream()
                    uprightBitmap.compress(android.graphics.Bitmap.CompressFormat.JPEG, 82, out)
                    out.toByteArray()
                } finally {
                    uprightBitmap.recycle()
                }
            } catch (e: Throwable) {
                android.util.Log.w("MainViewModel", "Failed to compress attached image: ${e.message}")
                null
            }
        }

    fun clearAiChat() {
        _chatMessages.value = listOf(
            ChatMessage(
                UUID.randomUUID().toString(),
                aiGreetingFor(appLanguage.value),
                false
            )
        )
    }

    fun updateProfile(name: String, bio: String) {
        viewModelScope.launch {
            repo.updateProfile(name, bio)
        }
    }

    fun updateProfileCustomization(
        name: String,
        bio: String,
        bannerTheme: String,
        avatarStyle: String,
        avatarFrame: String,
        headlineTitle: String,
        skillTags: List<String>,
        featuredBadgeId: String
    ) {
        viewModelScope.launch {
            repo.updateProfileCustomization(
                name = name,
                bio = bio,
                bannerTheme = bannerTheme,
                avatarStyle = avatarStyle,
                avatarFrame = avatarFrame,
                headlineTitle = headlineTitle,
                skillTags = skillTags,
                featuredBadgeId = featuredBadgeId
            )
        }
    }

    fun toggleDarkMode(isDark: Boolean) {
        viewModelScope.launch {
            repo.toggleDarkMode(isDark)
        }
    }

    fun markNotifRead(id: String) {
        viewModelScope.launch {
            repo.markNotificationAsRead(id)
        }
    }

    fun markAllNotifsRead() {
        viewModelScope.launch {
            repo.markAllNotificationsAsRead()
        }
    }

    fun syncAnnouncements() {
        viewModelScope.launch {
            repo.syncCloudAnnouncements()
        }
    }

    init {
        viewModelScope.launch {
            repo.ensureCloudAuthSession()
        }
        syncAnnouncements()
    }

    fun dismissLevelUpDialog() {
        _levelUpDialogInfo.value = null
    }

    // ==========================================
    // RECENT BADGES (SHOWCASE)
    // ==========================================
    val recentUnlockedBadges: StateFlow<List<BadgeItem>> = badges.map { list ->
        val unlocked = list.filter { it.isUnlocked }
        if (unlocked.isNotEmpty()) unlocked.take(3) else list.take(3)
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = emptyList()
    )
}

