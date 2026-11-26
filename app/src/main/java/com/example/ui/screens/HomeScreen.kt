package com.example.ui.screens

import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.EmojiEvents
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.data.local.ModuleProgressEntity
import com.example.data.local.UserProfileEntity
import com.example.data.models.Course
import com.example.data.models.LearningTrack
import com.example.ui.MainViewModel
import com.example.ui.components.CoachMarkBubble
import com.example.ui.components.CodiDailyTipCard
import com.example.ui.components.DailyCodeChallengeCard
import com.example.ui.components.DailyQuestCard
import com.example.ui.components.HomeScreenSkeleton
import com.example.ui.components.StreakFlameGrid
import com.example.ui.components.TopHeaderBar
import com.example.ui.theme.AmberGold

@Composable
fun HomeTabScreen(
    viewModel: MainViewModel,
    onNavigateToNotifications: () -> Unit = {},
    onNavigateToProfile: () -> Unit = {},
    onNavigateToLeaderboard: () -> Unit = {},
    onCourseClick: (String) -> Unit = {},
    onTrackClick: (String) -> Unit = {},
    onQuestClick: () -> Unit = {},
    onMenuClick: () -> Unit = {},
    onAskAi: (String) -> Unit = {},
    onTryInPlayground: (String, String) -> Unit = { _, _ -> },
    modifier: Modifier = Modifier
) {
    val userProfile by viewModel.userProfile.collectAsStateWithLifecycle()
    val isProfileLoading = userProfile == null

    Column(modifier = modifier.fillMaxSize()) {
        TopHeaderBar(
            userName = userProfile?.name ?: androidx.compose.ui.res.stringResource(com.example.R.string.home_top_header_default_user),
            totalXp = userProfile?.totalXp ?: 0,
            currentLevel = userProfile?.level ?: 1,
            unreadNotifsFlow = viewModel.unreadNotifsCount,
            isLoading = isProfileLoading,
            onNotifClick = onNavigateToNotifications,
            onProfileClick = onNavigateToProfile,
            onMenuClick = onMenuClick
        )

        HomeScreen(
            viewModel = viewModel,
            onCourseClick = onCourseClick,
            onTrackClick = onTrackClick,
            onQuestClick = onQuestClick,
            onLeaderboardClick = onNavigateToLeaderboard,
            onAskAi = onAskAi,
            onTryInPlayground = onTryInPlayground,
            modifier = Modifier.weight(1f)
        )
    }
}

@Composable
fun HomeScreen(
    viewModel: MainViewModel,
    onCourseClick: (String) -> Unit = {},
    onTrackClick: (String) -> Unit = {},
    onQuestClick: () -> Unit = {},
    onLeaderboardClick: () -> Unit = {},
    onAskAi: (String) -> Unit = {},
    onTryInPlayground: (String, String) -> Unit = { _, _ -> },
    modifier: Modifier = Modifier
) {
    val userProfile by viewModel.userProfile.collectAsStateWithLifecycle()
    val progressList by viewModel.moduleProgressList.collectAsStateWithLifecycle()
    val liveCatalogCourses by viewModel.catalogCourses.collectAsStateWithLifecycle()
    val courses = liveCatalogCourses.ifEmpty { viewModel.getAllCourses() }
    val tracks = remember(courses) { viewModel.getAllTracks() }

    if (userProfile == null) {
        HomeScreenSkeleton(modifier = modifier)
    } else {
        HomeScreen(
            userName = userProfile?.name ?: androidx.compose.ui.res.stringResource(com.example.R.string.home_default_user),
            currentStreak = userProfile?.currentStreak ?: 1,
            longestStreak = userProfile?.longestStreak ?: 1,
            questTitle = com.example.util.localization.DataLabelMappings.dailyQuestTitle(userProfile?.dailyQuestId ?: "quest_mod", "Selesaikan 1 Modul Belajar"),
            questProgress = userProfile?.dailyQuestProgress ?: 0,
            questCompleted = userProfile?.dailyQuestCompleted ?: false,
            totalXp = userProfile?.totalXp ?: 0,
            currentLevel = userProfile?.level ?: 1,
            tracks = tracks,
            courses = courses,
            progressList = progressList,
            onCourseClick = onCourseClick,
            onTrackClick = onTrackClick,
            onQuestClick = onQuestClick,
            onLeaderboardClick = onLeaderboardClick,
            onAskAi = onAskAi,
            onTryInPlayground = onTryInPlayground,
            userProfile = userProfile,
            viewModel = viewModel,
            modifier = modifier
        )
    }
}

@Composable
fun HomeScreen(
    userName: String,
    currentStreak: Int,
    longestStreak: Int,
    questTitle: String,
    questProgress: Int,
    questCompleted: Boolean,
    tracks: List<LearningTrack>,
    courses: List<Course>,
    progressList: List<ModuleProgressEntity> = emptyList(),
    totalXp: Int = 0,
    currentLevel: Int = 1,
    onCourseClick: (String) -> Unit,
    onTrackClick: (String) -> Unit,
    onQuestClick: () -> Unit,
    onLeaderboardClick: () -> Unit = {},
    onAskAi: (String) -> Unit = {},
    onTryInPlayground: (String, String) -> Unit = { _, _ -> },
    userProfile: UserProfileEntity? = null,
    viewModel: MainViewModel? = null,
    modifier: Modifier = Modifier
) {
    val primaryUserTrack = remember(userProfile?.selectedTracks) {
        userProfile?.selectedTracks?.split(",")?.firstOrNull()?.trim()?.takeIf { it.isNotEmpty() } ?: "front-end"
    }

    val topCourses = remember(courses, progressList, primaryUserTrack) {
        val completedKeys = progressList.filter { it.isCompleted }.map { it.courseId to it.moduleId }.toSet()
        val inProgress = courses.filter { course ->
            val total = course.modules.size
            val completed = course.modules.count { (course.id to it.id) in completedKeys }
            completed in 1 until total
        }
        if (inProgress.isNotEmpty()) {
            inProgress.take(2)
        } else {
            val trackCourses = courses.filter { it.trackId == primaryUserTrack }
            if (trackCourses.isNotEmpty()) trackCourses.take(2) else courses.take(2)
        }
    }

    val recFoundationTitle = stringResource(R.string.course_fe_web_foundation_00_title)
    val recFoundationDesc = stringResource(R.string.home_rec_foundation_desc)
    val recCssTitle = stringResource(R.string.course_fe_css_02_title)
    val recCssDesc = stringResource(R.string.home_rec_css_desc)
    val recJsTitle = stringResource(R.string.course_fe_js_03_title)
    val recJsDesc = stringResource(R.string.home_rec_js_desc)
    val (recommendedCourseId, recTitle, recDesc) = remember(
        courses, progressList, recFoundationTitle, recFoundationDesc, recCssTitle, recCssDesc, recJsTitle, recJsDesc
    ) {
        val completedIds = progressList.filter { it.isCompleted }.map { it.courseId }.toSet()
        when {
            "fe-web-foundation-00" !in completedIds -> Triple("fe-web-foundation-00", recFoundationTitle, recFoundationDesc)
            "fe-css-02" !in completedIds -> Triple("fe-css-02", recCssTitle, recCssDesc)
            else -> Triple("fe-js-03", recJsTitle, recJsDesc)
        }
    }

    // Precompute progress fraction per track for smooth rendering
    val trackProgressMap = remember(tracks, courses, progressList) {
        val completedKeys = progressList.filter { it.isCompleted }.map { it.courseId to it.moduleId }.toSet()
        tracks.filter { it.totalCourses > 0 }.associate { track ->
            val trackCourses = courses.filter { it.trackId == track.id }
            val totalModules = trackCourses.sumOf { it.modules.size }
            val completedModules = trackCourses.sumOf { course ->
                course.modules.count { mod ->
                    (course.id to mod.id) in completedKeys
                }
            }
            val fraction = if (totalModules > 0) completedModules.toFloat() / totalModules.toFloat() else 0f
            track.id to fraction
        }
    }

    // Smooth entrance transition at container level with state persistence across backgrounding
    var isEntered by rememberSaveable { mutableStateOf(false) }
    LaunchedEffect(Unit) {
        isEntered = true
    }

    val entranceAlpha by animateFloatAsState(
        targetValue = if (isEntered) 1f else 0f,
        animationSpec = tween(
            durationMillis = 260,
            easing = FastOutSlowInEasing
        ),
        label = "home_entrance_alpha"
    )
    // Lepas modifier graphicsLayer sepenuhnya setelah animasi masuk (260ms) selesai (alpha >= 0.999f)
    // agar LazyColumn tidak mempertahankan RenderNode / offscreen compositing layer ekstra saat di-scroll.
    val isEntranceTransitionFinished = isEntered && entranceAlpha >= 0.999f

    val listState = rememberLazyListState()
    val isScrolling = listState.isScrollInProgress

    Box(modifier = modifier.fillMaxSize()) {
        LazyColumn(
            state = listState,
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 16.dp)
                .then(
                    if (!isEntranceTransitionFinished) {
                        Modifier.graphicsLayer { alpha = entranceAlpha }
                    } else {
                        Modifier
                    }
                ),
            contentPadding = PaddingValues(bottom = 96.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
        // Step 1 CoachMark (muncul pertama kali di bawah TopHeaderBar, menunjuk ke ikon hamburger menu)
        if (viewModel != null && userProfile != null && !viewModel.hasSeenCoachMark("home_intro")) {
            item(key = "coach_mark_home_intro") {
                CoachMarkBubble(
                    message = androidx.compose.ui.res.stringResource(com.example.R.string.home_coachmark_intro),
                    onDismiss = { viewModel.markCoachMarkSeen("home_intro") }
                )
            }
        }

        // Redesigned Carousel at Top (Banner Promo, XP Terkumpul Minggu Ini, Rekomendasi Kelas)
        item(key = "home_top_carousel") {
            val startCourseId = topCourses.firstOrNull()?.id ?: "fe-web-foundation-00"
            HomeScreenTopCarousel(
                currentStreak = currentStreak,
                longestStreak = longestStreak,
                totalXp = totalXp,
                currentLevel = currentLevel,
                onStartLearning = { onCourseClick(startCourseId) },
                isParentScrolling = isScrolling,
                recommendedCourseTitle = recTitle,
                recommendedCourseDescription = recDesc,
                onExploreRecommendation = { onCourseClick(recommendedCourseId) }
            )
        }

        // Streak 7-Day Flame Grid
        item(key = "streak_flame_grid") {
            StreakFlameGrid(
                currentStreak = currentStreak,
                longestStreak = longestStreak
            )
        }

        // Step 2 CoachMark (muncul berurutan SETELAH coach mark pertama di-dismiss, menunjuk ke Misi Harian & Papan Peringkat Pelajar)
        if (
            viewModel != null &&
            userProfile != null &&
            viewModel.hasSeenCoachMark("home_intro") &&
            !viewModel.hasSeenCoachMark("home_quest_intro")
        ) {
            item(key = "coach_mark_home_quest_intro") {
                CoachMarkBubble(
                    message = androidx.compose.ui.res.stringResource(com.example.R.string.home_coachmark_quest_intro),
                    onDismiss = { viewModel.markCoachMarkSeen("home_quest_intro") }
                )
            }
        }

        // Misi Harian Card
        item(key = "daily_quest_card") {
            DailyQuestCard(
                title = questTitle,
                description = com.example.util.localization.DataLabelMappings.dailyQuestDesc(userProfile?.dailyQuestId ?: "quest_mod", "Selesaikan untuk mempertahankan streak belajarmu!"),
                rewardXp = 50,
                progress = questProgress,
                target = 1,
                isCompleted = questCompleted,
                onClaimOrDo = onQuestClick
            )
        }

        // Codi Daily Tip Card (Interactive Mascot in Home)
        item(key = "codi_daily_tip_card") {
            CodiDailyTipCard(
                onAskAi = onAskAi,
                onTryInPlayground = onTryInPlayground
            )
        }

        // Daily 1-Minute Code Challenge (+15 XP)
        item(key = "daily_code_challenge_card") {
            val isChallengeDone by (viewModel?.isDailyMicroChallengeCompletedToday
                ?: kotlinx.coroutines.flow.MutableStateFlow(false)).collectAsStateWithLifecycle()
            DailyCodeChallengeCard(
                isCompletedToday = isChallengeDone,
                onClaimReward = { viewModel?.claimDailyMicroChallenge(15) }
            )
        }

        // Leaderboard Banner Card
        item(key = "home_leaderboard_banner") {
            Card(
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.surface
                ),
                elevation = CardDefaults.cardElevation(defaultElevation = 0.dp),
                border = BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.6f)),
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(16.dp))
                    .clickable { onLeaderboardClick() }
                    .testTag("home_leaderboard_banner_button")
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.padding(16.dp)
                ) {
                    Box(
                        contentAlignment = Alignment.Center,
                        modifier = Modifier
                            .size(44.dp)
                            .clip(RoundedCornerShape(12.dp))
                            .background(AmberGold.copy(alpha = 0.15f))
                    ) {
                        Icon(
                            imageVector = Icons.Default.EmojiEvents,
                            contentDescription = null,
                            tint = AmberGold,
                            modifier = Modifier.size(24.dp)
                        )
                    }

                    Spacer(modifier = Modifier.width(14.dp))

                    Column(modifier = Modifier.weight(1f)) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text(
                                text = androidx.compose.ui.res.stringResource(com.example.R.string.home_leaderboard_banner_title),
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.onSurface
                            )
                            Spacer(modifier = Modifier.width(6.dp))
                            Surface(
                                shape = RoundedCornerShape(4.dp),
                                color = AmberGold.copy(alpha = 0.18f)
                            ) {
                                Text(
                                    text = androidx.compose.ui.res.stringResource(com.example.R.string.home_leaderboard_banner_top50),
                                    style = MaterialTheme.typography.labelSmall,
                                    color = AmberGold,
                                    fontWeight = FontWeight.Bold,
                                    modifier = Modifier.padding(horizontal = 5.dp, vertical = 1.5.dp)
                                )
                            }
                        }
                        Spacer(modifier = Modifier.height(2.dp))
                        Text(
                            text = androidx.compose.ui.res.stringResource(com.example.R.string.home_leaderboard_banner_desc),
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }

                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowForward,
                        contentDescription = androidx.compose.ui.res.stringResource(com.example.R.string.home_leaderboard_banner_cd),
                        tint = MaterialTheme.colorScheme.onSurfaceVariant,
                        modifier = Modifier.size(20.dp)
                    )
                }
            }
        }

        // Section: Lanjutkan Belajar
        item(key = "continue_learning_header") {
            val trackToOpen = primaryUserTrack
            HomeScreenContinueLearningHeader(
                onSeeAllClick = { onTrackClick(trackToOpen) }
            )
        }

        // Active Course Cards
        items(topCourses, key = { it.id }) { course ->
            ActiveCourseCard(
                course = course,
                onClick = { onCourseClick(course.id) }
            )
        }

        // Section: Jelajahi Jalur Belajar
        item(key = "tracks_section") {
            HomeScreenTrackSection(
                tracks = tracks,
                trackProgressMap = trackProgressMap,
                onTrackClick = onTrackClick
            )
        }
        }
    }
}

/**
 * Lightweight entrance wrapper retained for compatibility.
 * Employs zero overhead during LazyColumn scrolling.
 */
@Composable
fun StaggeredCardEntrance(
    orderIndex: Int,
    isEntered: Boolean,
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit
) {
    Box(modifier = modifier) {
        content()
    }
}
