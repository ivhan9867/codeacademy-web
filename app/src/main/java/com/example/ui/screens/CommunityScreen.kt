package com.example.ui.screens

import android.provider.Settings
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.animateIntAsState
import androidx.compose.animation.core.snap
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
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
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.EmojiEvents
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Forum
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.MilitaryTech
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.Whatshot
import androidx.compose.material.icons.filled.WifiOff
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalInspectionMode
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.data.CourseCatalog
import com.example.data.local.ForumPostEntity
import com.example.data.local.LeaderboardEntryCacheEntity
import com.example.data.local.WeeklyChallengeEntity
import com.example.ui.MainViewModel
import com.example.ui.viewmodel.SocialViewModel
import com.example.ui.components.CoachMarkBubble
import com.example.ui.components.leaderboardSkeletonItems
import com.example.ui.components.rememberShimmerBrush
import com.example.ui.theme.AmberGold
import com.example.ui.theme.CyanAccent
import com.example.ui.theme.CyanAccentText
import com.example.ui.theme.Dimens
import com.example.ui.theme.FlameOrange
import com.example.ui.theme.SuccessGreen
import com.example.ui.theme.TealLight
import com.example.ui.theme.TealPrimary
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

private val PodiumCardShape = RoundedCornerShape(20.dp)
private val PodiumPedestalShape = RoundedCornerShape(topStart = 10.dp, topEnd = 10.dp)
private val PodiumPillShape = RoundedCornerShape(6.dp)

private val RowCardShape = RoundedCornerShape(12.dp)
private val RowPillShape = RoundedCornerShape(8.dp)
private val RowTagShape = RoundedCornerShape(4.dp)

private fun levelTierEmoji(level: Int): String = when {
    level >= 20 -> "👑"
    level >= 10 -> "🚀"
    level >= 5 -> "📘"
    else -> "🌱"
}

@Composable
private fun levelTierLabel(level: Int): String = com.example.util.localization.DataLabelMappings.levelTierLabel(level)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CommunityScreen(
    viewModel: MainViewModel,
    socialViewModel: SocialViewModel = viewModel(),
    onCourseForumClick: (String) -> Unit = {},
    onExploreCourses: () -> Unit = {},
    onOpenTrendingDiscussions: () -> Unit = onExploreCourses,
    onMenuClick: (() -> Unit)? = null,
    modifier: Modifier = Modifier
) {
    val coroutineScope = rememberCoroutineScope()
    val snackbarHostState = remember { SnackbarHostState() }
    val shimmerBrush = rememberShimmerBrush()

    // Multi-Scope Leaderboard state
    val selectedPeriod by socialViewModel.selectedLeaderboardPeriod.collectAsStateWithLifecycle()
    val selectedTrack by socialViewModel.selectedLeaderboardTrack.collectAsStateWithLifecycle()
    val leaderboardEntries by socialViewModel.leaderboardEntries.collectAsStateWithLifecycle()
    val leaderboardError by socialViewModel.leaderboardError.collectAsStateWithLifecycle()
    val isLeaderboardRefreshing by socialViewModel.isLeaderboardRefreshing.collectAsStateWithLifecycle()
    val userProfile by viewModel.userProfile.collectAsStateWithLifecycle()
    val calculatedRank by socialViewModel.myLeaderboardRank.collectAsStateWithLifecycle()

    // Weekly Challenge state
    val weeklyChallenge by socialViewModel.weeklyChallenge.collectAsStateWithLifecycle()
    val weeklyProgressCount by socialViewModel.weeklyChallengeProgress.collectAsStateWithLifecycle()

    val selectedPublicProfile by socialViewModel.selectedPublicProfile.collectAsStateWithLifecycle()
    val isLoadingPublicProfile by socialViewModel.isLoadingPublicProfile.collectAsStateWithLifecycle()

    selectedPublicProfile?.let { publicProfile ->
        com.example.ui.components.UserPublicProfileDialog(
            profile = publicProfile,
            isLoading = isLoadingPublicProfile,
            onDismiss = { socialViewModel.dismissPublicProfile() }
        )
    }

    val context = LocalContext.current
    val isInspectionMode = LocalInspectionMode.current
    val areAnimationsEnabled = remember(context, isInspectionMode) {
        if (isInspectionMode) {
            false
        } else {
            val scale = runCatching {
                Settings.Global.getFloat(
                    context.contentResolver,
                    Settings.Global.ANIMATOR_DURATION_SCALE,
                    1f
                )
            }.getOrDefault(1f)
            scale > 0f
        }
    }

    // Podium animation executed flag (persists across scroll and config change)
    var podiumHasAnimated by rememberSaveable { mutableStateOf(false) }

    // Reset podium animation flag whenever filter changes so animation triggers for new data
    LaunchedEffect(selectedPeriod, selectedTrack) {
        podiumHasAnimated = false
    }

    // Initial data refresh on first launch
    LaunchedEffect(Unit) {
        socialViewModel.refreshLeaderboard()
    }

    val top3 = remember(leaderboardEntries) { leaderboardEntries.take(3) }
    val remainingEntries = remember(leaderboardEntries) {
        if (leaderboardEntries.size > 3) leaderboardEntries.drop(3) else emptyList()
    }
    val currentUserId = remember(userProfile?.id) {
        socialViewModel.getCurrentForumUserId(userProfile?.id)
    }

    val myEntryInTop50 = remember(leaderboardEntries, currentUserId) {
        leaderboardEntries.find { it.userId == currentUserId }
    }
    val displayRank = myEntryInTop50?.rank ?: calculatedRank ?: 10
    val displayScore = myEntryInTop50?.totalXp ?: if (selectedPeriod == "streak") (userProfile?.currentStreak ?: 0) else (userProfile?.totalXp ?: 100)

    val periodOptions = listOf(
        "weekly" to "Mingguan",
        "monthly" to "Bulanan",
        "all_time" to "Sepanjang Waktu",
        "streak" to "Streak 🔥"
    )

    val trackOptions = listOf("global" to "Semua Track") + CourseCatalog.tracks.map { it.id to it.name }

    val listState = rememberLazyListState()

    // Sticky card only shows when user is NOT currently visible in viewport (or user is outside top 50)
    val isUserVisibleInViewport by remember(listState, myEntryInTop50) {
        derivedStateOf {
            if (myEntryInTop50 == null) {
                false
            } else {
                val userKey = if (myEntryInTop50.rank in 1..3) {
                    "leaderboard_podium"
                } else {
                    myEntryInTop50.userId
                }
                listState.layoutInfo.visibleItemsInfo.any { it.key == userKey }
            }
        }
    }

    val showStickyMyRank by remember {
        derivedStateOf { !isUserVisibleInViewport }
    }

    Scaffold(
        contentWindowInsets = androidx.compose.foundation.layout.WindowInsets(0.dp),
        snackbarHost = { SnackbarHost(snackbarHostState) },
        topBar = {
            TopAppBar(
                windowInsets = androidx.compose.foundation.layout.WindowInsets(0.dp),
                navigationIcon = {
                    if (onMenuClick != null) {
                        IconButton(
                            onClick = onMenuClick,
                            modifier = Modifier
                                .testTag("btn_menu_community")
                                .size(40.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.Menu,
                                contentDescription = androidx.compose.ui.res.stringResource(com.example.R.string.comm_menu_nav),
                                tint = MaterialTheme.colorScheme.onSurface
                            )
                        }
                    }
                },
                title = {
                    Column {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text(
                                text = androidx.compose.ui.res.stringResource(com.example.R.string.comm_leaderboard_title),
                                style = MaterialTheme.typography.titleLarge,
                                fontWeight = FontWeight.Bold
                            )
                            Spacer(modifier = Modifier.width(6.dp))
                            Icon(
                                imageVector = Icons.Default.EmojiEvents,
                                contentDescription = null,
                                tint = AmberGold,
                                modifier = Modifier.size(22.dp)
                            )
                        }
                        Text(
                            text = androidx.compose.ui.res.stringResource(com.example.R.string.comm_leaderboard_subtitle),
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                            maxLines = 1,
                            overflow = androidx.compose.ui.text.style.TextOverflow.Ellipsis
                        )
                    }
                },
                actions = {
                    IconButton(
                        onClick = {
                            socialViewModel.refreshLeaderboard(selectedPeriod, selectedTrack, force = true)
                        },
                        enabled = !isLeaderboardRefreshing,
                        modifier = Modifier.testTag("community_refresh_button")
                    ) {
                        if (isLeaderboardRefreshing) {
                            CircularProgressIndicator(
                                modifier = Modifier.size(20.dp),
                                strokeWidth = 2.dp,
                                color = MaterialTheme.colorScheme.primary
                            )
                        } else {
                            Icon(
                                imageVector = Icons.Default.Refresh,
                                contentDescription = androidx.compose.ui.res.stringResource(com.example.R.string.comm_refresh)
                            )
                        }
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.surface
                )
            )
        },
        bottomBar = {
            AnimatedVisibility(
                visible = showStickyMyRank,
                enter = slideInVertically(initialOffsetY = { it }) + fadeIn(),
                exit = slideOutVertically(targetOffsetY = { it }) + fadeOut()
            ) {
                // Sticky Current User Rank Card (Only rendered when user row is outside viewport)
                Surface(
                    tonalElevation = 8.dp,
                    shadowElevation = 8.dp,
                    color = MaterialTheme.colorScheme.surface,
                    border = BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.5f)),
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable(enabled = myEntryInTop50 != null) {
                            coroutineScope.launch {
                                try {
                                    if (myEntryInTop50 != null) {
                                        if (myEntryInTop50.rank in 1..3) {
                                            listState.animateScrollToItem(if (top3.isNotEmpty()) 1 else 0)
                                        } else {
                                            val remainingIdx = remainingEntries.indexOfFirst { it.userId == currentUserId }
                                            if (remainingIdx >= 0) {
                                                val offset = if (top3.isNotEmpty()) 2 else 1
                                                listState.animateScrollToItem((offset + remainingIdx).coerceAtLeast(0))
                                            }
                                        }
                                    }
                                } catch (_: Throwable) {}
                            }
                        }
                        .testTag("sticky_my_rank_card")
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier
                            .fillMaxWidth()
                            .navigationBarsPadding()
                            .padding(horizontal = 16.dp, vertical = 10.dp)
                    ) {
                        val isProfileEmpty = userProfile == null && myEntryInTop50 == null
                        Box(
                            contentAlignment = Alignment.Center,
                            modifier = Modifier
                                .size(36.dp)
                                .clip(CircleShape)
                                .then(
                                    if (isProfileEmpty) Modifier.background(shimmerBrush)
                                    else Modifier.background(TealLight.copy(alpha = 0.2f))
                                )
                        ) {
                            if (!isProfileEmpty) {
                                Text(
                                    text = "#$displayRank",
                                    style = MaterialTheme.typography.titleSmall,
                                    fontWeight = FontWeight.ExtraBold,
                                    color = MaterialTheme.colorScheme.primary
                                )
                            }
                        }

                        Spacer(modifier = Modifier.width(12.dp))

                        Column(modifier = Modifier.weight(1f)) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                if (isProfileEmpty) {
                                    Box(
                                        modifier = Modifier
                                            .width(72.dp)
                                            .height(16.dp)
                                            .clip(RoundedCornerShape(4.dp))
                                            .background(shimmerBrush)
                                    )
                                } else {
                                    Text(
                                        text = userProfile?.name ?: "Kamu",
                                        style = MaterialTheme.typography.bodyMedium,
                                        fontWeight = FontWeight.Bold,
                                        color = MaterialTheme.colorScheme.onSurface,
                                        maxLines = 1,
                                        overflow = TextOverflow.Ellipsis
                                    )
                                }
                                Spacer(modifier = Modifier.width(6.dp))
                                Surface(
                                    shape = RoundedCornerShape(4.dp),
                                    color = TealLight.copy(alpha = 0.2f)
                                ) {
                                    Text(
                                        text = "Kamu",
                                        style = MaterialTheme.typography.labelSmall,
                                        color = MaterialTheme.colorScheme.primary,
                                        modifier = Modifier.padding(horizontal = 4.dp, vertical = 1.dp)
                                    )
                                }
                            }
                            Text(
                                text = "Peringkat di filter ${periodOptions.find { it.first == selectedPeriod }?.second ?: "Mingguan"}",
                                style = MaterialTheme.typography.labelSmall,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }

                        Surface(
                            shape = RoundedCornerShape(8.dp),
                            color = if (selectedPeriod == "streak") FlameOrange.copy(alpha = 0.15f) else AmberGold.copy(alpha = 0.15f)
                        ) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp)
                            ) {
                                Icon(
                                    imageVector = if (selectedPeriod == "streak") Icons.Default.Whatshot else Icons.Default.Star,
                                    contentDescription = null,
                                    tint = if (selectedPeriod == "streak") FlameOrange else AmberGold,
                                    modifier = Modifier.size(14.dp)
                                )
                                Spacer(modifier = Modifier.width(4.dp))
                                if (isProfileEmpty) {
                                    Box(
                                        modifier = Modifier
                                            .width(46.dp)
                                            .height(14.dp)
                                            .clip(RoundedCornerShape(3.dp))
                                            .background(shimmerBrush)
                                    )
                                } else {
                                    Text(
                                        text = if (selectedPeriod == "streak") "$displayScore Hari" else "$displayScore XP",
                                        style = MaterialTheme.typography.labelMedium,
                                        fontWeight = FontWeight.Bold,
                                        color = if (selectedPeriod == "streak") FlameOrange else AmberGold
                                    )
                                }
                            }
                        }
                    }
                }
            }
        },
        modifier = modifier.fillMaxSize()
    ) { innerPadding ->
        LazyColumn(
            state = listState,
            contentPadding = PaddingValues(
                top = innerPadding.calculateTopPadding() + 8.dp,
                bottom = innerPadding.calculateBottomPadding() + 16.dp,
                start = 16.dp,
                end = 16.dp
            ),
            verticalArrangement = Arrangement.spacedBy(14.dp),
            modifier = Modifier
                .fillMaxSize()
                .testTag("community_main_list")
        ) {
            if (userProfile != null && !viewModel.hasSeenCoachMark("leaderboard_intro")) {
                item(key = "coach_mark_leaderboard") {
                    CoachMarkBubble(
                        message = androidx.compose.ui.res.stringResource(com.example.R.string.comm_coachmark),
                        onDismiss = { viewModel.markCoachMarkSeen("leaderboard_intro") }
                    )
                }
            }

            // 1. SECTION: LEADERBOARD MULTI-SCOPE HEADER & FILTERS (PRIORITAS 1 - UTAMA)
            item(key = "section_leaderboard_header") {
                LeaderboardFilterSection(
                    selectedPeriod = selectedPeriod,
                    selectedTrack = selectedTrack,
                    periodOptions = periodOptions,
                    trackOptions = trackOptions,
                    onSelectPeriod = { pKey ->
                        socialViewModel.setLeaderboardFilters(pKey, selectedTrack)
                    },
                    onSelectTrack = { tKey ->
                        socialViewModel.setLeaderboardFilters(selectedPeriod, tKey)
                    }
                )
            }

            // Error state, Loading Skeleton, Empty State, or Podium/List for Leaderboard
            if (leaderboardError != null && leaderboardEntries.isEmpty()) {
                item(key = "leaderboard_error_state") {
                    ErrorRetryView(
                        message = leaderboardError ?: androidx.compose.ui.res.stringResource(com.example.R.string.comm_load_error),
                        onRetry = {
                            socialViewModel.refreshLeaderboard(selectedPeriod, selectedTrack, force = true)
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 24.dp)
                    )
                }
            } else if (leaderboardEntries.isEmpty() && isLeaderboardRefreshing) {
                leaderboardSkeletonItems(shimmerBrush)
            } else if (leaderboardEntries.isEmpty()) {
                item(key = "leaderboard_empty_state") {
                    EmptyLeaderboardView(
                        selectedPeriodLabel = periodOptions.find { it.first == selectedPeriod }?.second ?: "periode ini",
                        onRefresh = {
                            socialViewModel.refreshLeaderboard(selectedPeriod, selectedTrack, force = true)
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 8.dp)
                    )
                }
            } else {
                // Podium for Top 3 (Handles 1, 2, or 3 entries gracefully)
                if (top3.isNotEmpty()) {
                    item(key = "leaderboard_podium") {
                        LeaderboardPodium(
                            top3 = top3,
                            currentUserId = currentUserId,
                            currentUserStreak = userProfile?.currentStreak ?: 0,
                            isStreak = selectedPeriod == "streak",
                            hasAlreadyAnimated = podiumHasAnimated,
                            areAnimationsEnabled = areAnimationsEnabled,
                            onAnimationComplete = { podiumHasAnimated = true },
                            modifier = Modifier.padding(bottom = 6.dp)
                        )
                    }
                }

                // Remaining entries in list (Rank 4 to 50) with stable userId key and subtle staggered entrance
                itemsIndexed(
                    items = remainingEntries,
                    key = { _, entry -> entry.userId }
                ) { index, entry ->
                    LeaderboardRowItem(
                        entry = entry,
                        isCurrentUser = entry.userId == currentUserId,
                        currentUserStreak = if (entry.userId == currentUserId) (userProfile?.currentStreak ?: 0) else 0,
                        isStreak = selectedPeriod == "streak",
                        itemIndex = index,
                        areAnimationsEnabled = areAnimationsEnabled,
                        hasAlreadyAnimated = podiumHasAnimated,
                        onClick = {
                            socialViewModel.openPublicProfile(
                                userId = entry.userId,
                                fallbackName = entry.userName,
                                fallbackLevel = entry.level
                            )
                        }
                    )
                }
            }

            // 2. SECTION: WEEKLY CHALLENGE / QUEST MINGGUAN
            item(key = "section_weekly_challenge") {
                WeeklyChallengeCard(
                    challenge = weeklyChallenge,
                    currentProgress = weeklyProgressCount,
                    onClaimReward = { cId ->
                        socialViewModel.claimWeeklyChallengeReward(cId) { success ->
                            coroutineScope.launch {
                                if (success) {
                                    snackbarHostState.showSnackbar(
                                        context.getString(
                                            com.example.R.string.comm_claim_success_format,
                                            weeklyChallenge?.rewardXp ?: 100
                                        )
                                    )
                                } else {
                                    snackbarHostState.showSnackbar(
                                        context.getString(com.example.R.string.comm_claim_error)
                                    )
                                }
                            }
                        }
                    },
                    onStartLearning = onExploreCourses
                )
            }

            // 3. SECTION: FORUM DISKUSI PER KELAS (AKSES LANGSUNG)
            item(key = "section_course_forums_hub") {
                val activeCourses = remember { CourseCatalog.courses }
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                    border = BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.5f)),
                    elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Box(
                                contentAlignment = Alignment.Center,
                                modifier = Modifier
                                    .size(34.dp)
                                    .clip(RoundedCornerShape(10.dp))
                                    .background(TealPrimary.copy(alpha = 0.14f))
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Forum,
                                    contentDescription = null,
                                    tint = TealPrimary,
                                    modifier = Modifier.size(18.dp)
                                )
                            }
                            Spacer(modifier = Modifier.width(10.dp))
                            Column(modifier = Modifier.weight(1f)) {
                                Text(
                                    text = androidx.compose.ui.res.stringResource(com.example.R.string.comm_course_forums_title),
                                    style = MaterialTheme.typography.titleSmall,
                                    fontWeight = FontWeight.Bold,
                                    color = MaterialTheme.colorScheme.onSurface
                                )
                                Text(
                                    text = androidx.compose.ui.res.stringResource(com.example.R.string.comm_course_forums_subtitle),
                                    style = MaterialTheme.typography.labelSmall,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                            }
                        }
                        Spacer(modifier = Modifier.height(12.dp))
                        activeCourses.forEach { c ->
                            Surface(
                                shape = RoundedCornerShape(12.dp),
                                color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.45f),
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(vertical = 4.dp)
                                    .clip(RoundedCornerShape(12.dp))
                                    .clickable { onCourseForumClick(c.id) }
                            ) {
                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    modifier = Modifier.padding(horizontal = 12.dp, vertical = 10.dp)
                                ) {
                                    Text(
                                        text = c.title,
                                        style = MaterialTheme.typography.bodyMedium,
                                        fontWeight = FontWeight.SemiBold,
                                        color = MaterialTheme.colorScheme.onSurface,
                                        maxLines = 1,
                                        overflow = TextOverflow.Ellipsis,
                                        modifier = Modifier.weight(1f)
                                    )
                                    Spacer(modifier = Modifier.width(8.dp))
                                    Icon(
                                        imageVector = Icons.AutoMirrored.Filled.ArrowForward,
                                        contentDescription = null,
                                        tint = TealPrimary,
                                        modifier = Modifier.size(16.dp)
                                    )
                                }
                            }
                        }
                    }
                }
            }

            // 4. SECTION: SOROTAN DISKUSI HANGAT CTA
            item(key = "section_trending_highlight") {
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable { onOpenTrendingDiscussions() },
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                    border = BorderStroke(1.dp, CyanAccent.copy(alpha = 0.35f)),
                    elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                text = androidx.compose.ui.res.stringResource(com.example.R.string.comm_trending_highlight_title),
                                style = MaterialTheme.typography.titleSmall,
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.onSurface
                            )
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(
                                text = androidx.compose.ui.res.stringResource(com.example.R.string.comm_trending_highlight_desc),
                                style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                            Spacer(modifier = Modifier.height(10.dp))
                            Text(
                                text = androidx.compose.ui.res.stringResource(com.example.R.string.comm_open_trending_btn) + " →",
                                style = MaterialTheme.typography.labelMedium,
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.primary
                            )
                        }
                    }
                }
            }

            item(key = "community_footer_spacer") {
                Spacer(modifier = Modifier.height(16.dp))
            }
        }
    }
}

@Composable
fun WeeklyChallengeCard(
    challenge: WeeklyChallengeEntity?,
    currentProgress: Int,
    onClaimReward: (String) -> Unit,
    onStartLearning: () -> Unit,
    modifier: Modifier = Modifier
) {
    if (challenge == null) return

    val target = challenge.targetCount.coerceAtLeast(1)
    val isCompleted = currentProgress >= target
    val progressFraction = (currentProgress.toFloat() / target.toFloat()).coerceIn(0f, 1f)

    Card(
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        ),
        border = BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.5f)),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(
                    modifier = Modifier
                        .weight(1f)
                        .padding(end = 8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(
                        modifier = Modifier
                            .size(34.dp)
                            .clip(CircleShape)
                            .background(AmberGold.copy(alpha = 0.2f)),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.MilitaryTech,
                            contentDescription = null,
                            tint = AmberGold,
                            modifier = Modifier.size(20.dp)
                        )
                    }
                    Spacer(modifier = Modifier.width(10.dp))
                    Column {
                        Text(
                            text = challenge.title,
                            style = MaterialTheme.typography.titleSmall,
                            fontWeight = FontWeight.Bold,
                            maxLines = 1,
                            overflow = androidx.compose.ui.text.style.TextOverflow.Ellipsis
                        )
                        Text(
                            text = androidx.compose.ui.res.stringResource(com.example.R.string.comm_weekly_reset),
                            style = MaterialTheme.typography.labelSmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }

                Surface(
                    shape = RoundedCornerShape(8.dp),
                    color = if (challenge.isClaimed) {
                        SuccessGreen.copy(alpha = 0.15f)
                    } else if (isCompleted) {
                        AmberGold.copy(alpha = 0.2f)
                    } else {
                        TealLight.copy(alpha = 0.15f)
                    }
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                    ) {
                        if (challenge.isClaimed) {
                            Text(
                                text = androidx.compose.ui.res.stringResource(com.example.R.string.comm_claim_done),
                                style = MaterialTheme.typography.labelSmall,
                                fontWeight = FontWeight.Bold,
                                color = SuccessGreen
                            )
                            Spacer(modifier = Modifier.width(4.dp))
                            Icon(
                                imageVector = Icons.Default.Check,
                                contentDescription = null,
                                tint = SuccessGreen,
                                modifier = Modifier.size(14.dp)
                            )
                        } else if (isCompleted) {
                            Text(
                                text = androidx.compose.ui.res.stringResource(com.example.R.string.comm_achieved),
                                style = MaterialTheme.typography.labelSmall,
                                fontWeight = FontWeight.Bold,
                                color = AmberGold
                            )
                        } else {
                            Text(
                                text = androidx.compose.ui.res.stringResource(com.example.R.string.comm_active),
                                style = MaterialTheme.typography.labelSmall,
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.primary
                            )
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            Text(
                text = challenge.description,
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )

            Spacer(modifier = Modifier.height(12.dp))

            // Progress Bar
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = androidx.compose.ui.res.stringResource(com.example.R.string.comm_progress_modules_format, minOf(currentProgress, target), target),
                    style = MaterialTheme.typography.labelMedium,
                    fontWeight = FontWeight.SemiBold
                )
                Text(
                    text = "+${challenge.rewardXp} XP",
                    style = MaterialTheme.typography.labelMedium,
                    fontWeight = FontWeight.ExtraBold,
                    color = AmberGold
                )
            }

            Spacer(modifier = Modifier.height(6.dp))

            LinearProgressIndicator(
                progress = { progressFraction },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(8.dp)
                    .clip(RoundedCornerShape(4.dp)),
                color = if (isCompleted) SuccessGreen else MaterialTheme.colorScheme.primary,
                trackColor = MaterialTheme.colorScheme.surfaceVariant
            )

            Spacer(modifier = Modifier.height(12.dp))

            // Action Button
            if (isCompleted && !challenge.isClaimed) {
                Button(
                    onClick = { onClaimReward(challenge.id) },
                    colors = ButtonDefaults.buttonColors(containerColor = AmberGold),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Icon(
                        imageVector = Icons.Default.CheckCircle,
                        contentDescription = null,
                        tint = Color.Black,
                        modifier = Modifier.size(16.dp)
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = androidx.compose.ui.res.stringResource(com.example.R.string.comm_claim_bonus_format, challenge.rewardXp),
                        color = Color.Black,
                        fontWeight = FontWeight.Bold
                    )
                }
            } else if (!challenge.isClaimed) {
                Button(
                    onClick = onStartLearning,
                    colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(
                        text = androidx.compose.ui.res.stringResource(com.example.R.string.comm_continue_module),
                        color = MaterialTheme.colorScheme.onPrimary,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }
    }
}

@Composable
fun ForumHighlightCard(
    post: ForumPostEntity,
    onClick: () -> Unit,
    onAuthorClick: ((ForumPostEntity) -> Unit)? = null,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .clickable { onClick() },
        shape = RoundedCornerShape(14.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        ),
        border = BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.5f)),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
    ) {
        Column(modifier = Modifier.padding(14.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = if (onAuthorClick != null && post.authorId.isNotBlank()) {
                        Modifier
                            .clip(RoundedCornerShape(8.dp))
                            .clickable { onAuthorClick(post) }
                            .padding(2.dp)
                    } else Modifier
                ) {
                    Box(
                        modifier = Modifier
                            .size(32.dp)
                            .clip(CircleShape)
                            .background(CyanAccent.copy(alpha = 0.2f)),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = post.authorName.firstOrNull()?.uppercaseChar()?.toString() ?: "S",
                            fontWeight = FontWeight.Bold,
                            color = CyanAccentText,
                            fontSize = 14.sp
                        )
                    }
                    Spacer(modifier = Modifier.width(8.dp))
                    Column {
                        Text(
                            text = post.authorName,
                            style = MaterialTheme.typography.bodySmall,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                        Text(
                            text = "Kelas: ${post.courseId}",
                            style = MaterialTheme.typography.labelSmall,
                            color = MaterialTheme.colorScheme.primary,
                            fontSize = 10.sp
                        )
                    }
                }

                Surface(
                    shape = RoundedCornerShape(6.dp),
                    color = Color(0xFFFF5722).copy(alpha = 0.12f)
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.padding(horizontal = 6.dp, vertical = 3.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Favorite,
                            contentDescription = null,
                            tint = Color(0xFFFF5722),
                            modifier = Modifier.size(12.dp)
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            text = "${post.likeCount}",
                            style = MaterialTheme.typography.labelSmall,
                            color = Color(0xFFFF5722),
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = post.content,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis
            )

            Spacer(modifier = Modifier.height(8.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.End,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = androidx.compose.ui.res.stringResource(com.example.R.string.comm_open_forum_cta),
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.primary,
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }
}

@Composable
fun EmptyLeaderboardView(
    selectedPeriodLabel: String,
    onRefresh: () -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .testTag("leaderboard_empty_card"),
        shape = Dimens.CardDefaultShape,
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        ),
        border = BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.4f)),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(28.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            com.example.ui.components.CodeAcademyMascot(
                size = 86.dp,
                mood = com.example.ui.components.MascotMood.HAPPY
            )
            Spacer(modifier = Modifier.height(10.dp))
            Text(
                text = androidx.compose.ui.res.stringResource(com.example.R.string.comm_empty_title),
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onSurface
            )
            Spacer(modifier = Modifier.height(6.dp))
            Text(
                text = androidx.compose.ui.res.stringResource(com.example.R.string.comm_empty_desc_format, selectedPeriodLabel),
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                textAlign = androidx.compose.ui.text.style.TextAlign.Center
            )
            Spacer(modifier = Modifier.height(16.dp))
            Button(
                onClick = onRefresh,
                shape = Dimens.AppButtonShape,
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    contentColor = Color.White
                )
            ) {
                Icon(
                    imageVector = Icons.Default.Refresh,
                    contentDescription = null,
                    modifier = Modifier.size(16.dp)
                )
                Spacer(modifier = Modifier.width(6.dp))
                Text(
                    text = "Perbarui Data",
                    style = MaterialTheme.typography.labelMedium,
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }
}

@Composable
private fun LeaderboardFilterSection(
    selectedPeriod: String,
    selectedTrack: String,
    periodOptions: List<Pair<String, String>>,
    trackOptions: List<Pair<String, String>>,
    onSelectPeriod: (String) -> Unit,
    onSelectTrack: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(modifier = modifier.fillMaxWidth()) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    imageVector = Icons.Default.EmojiEvents,
                    contentDescription = null,
                    tint = AmberGold,
                    modifier = Modifier.size(20.dp)
                )
                Spacer(modifier = Modifier.width(6.dp))
                Text(
                    text = "Papan Peringkat Pelajar",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )
            }
            Text(
                text = "Top 50",
                style = MaterialTheme.typography.labelMedium,
                color = MaterialTheme.colorScheme.primary,
                fontWeight = FontWeight.SemiBold
            )
        }

        Spacer(modifier = Modifier.height(10.dp))

        // Filter 1: Timeframe (Mingguan / Bulanan / Sepanjang Waktu / Streak) - Horizontally Scrollable
        LazyRow(
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            contentPadding = PaddingValues(horizontal = 2.dp),
            modifier = Modifier.fillMaxWidth()
        ) {
            items(periodOptions, key = { it.first }) { (periodKey, label) ->
                val isSelected = selectedPeriod == periodKey
                FilterChip(
                    selected = isSelected,
                    onClick = { onSelectPeriod(periodKey) },
                    label = {
                        Text(
                            text = label,
                            style = MaterialTheme.typography.labelMedium,
                            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal
                        )
                    },
                    colors = FilterChipDefaults.filterChipColors(
                        selectedContainerColor = MaterialTheme.colorScheme.primary.copy(alpha = 0.15f),
                        selectedLabelColor = MaterialTheme.colorScheme.primary
                    ),
                    modifier = Modifier.testTag("filter_period_$periodKey")
                )
            }
        }

        Spacer(modifier = Modifier.height(6.dp))

        // Filter 2: Track Scope (Global / Front-End / Back-End / dst) - Horizontally Scrollable
        LazyRow(
            horizontalArrangement = Arrangement.spacedBy(6.dp),
            contentPadding = PaddingValues(horizontal = 2.dp),
            modifier = Modifier.fillMaxWidth()
        ) {
            items(trackOptions, key = { it.first }) { (trackKey, trackLabel) ->
                val isSelected = selectedTrack == trackKey
                FilterChip(
                    selected = isSelected,
                    onClick = { onSelectTrack(trackKey) },
                    label = {
                        Text(
                            text = trackLabel,
                            style = MaterialTheme.typography.labelSmall,
                            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal
                        )
                    },
                    colors = FilterChipDefaults.filterChipColors(
                        selectedContainerColor = CyanAccent.copy(alpha = 0.2f),
                        selectedLabelColor = CyanAccent
                    ),
                    modifier = Modifier.testTag("filter_track_$trackKey")
                )
            }
        }
    }
}

@Composable
fun LeaderboardPodium(
    top3: List<LeaderboardEntryCacheEntity>,
    currentUserId: String,
    currentUserStreak: Int = 0,
    isStreak: Boolean = false,
    hasAlreadyAnimated: Boolean = false,
    areAnimationsEnabled: Boolean = true,
    onAnimationComplete: () -> Unit = {},
    modifier: Modifier = Modifier
) {
    val rank1 = top3.find { it.rank == 1 }
    val rank2 = top3.find { it.rank == 2 }
    val rank3 = top3.find { it.rank == 3 }

    // Centralized animation orchestration for the entire podium
    var rank3Revealed by remember(hasAlreadyAnimated, areAnimationsEnabled) {
        mutableStateOf(hasAlreadyAnimated || !areAnimationsEnabled)
    }
    var rank2Revealed by remember(hasAlreadyAnimated, areAnimationsEnabled) {
        mutableStateOf(hasAlreadyAnimated || !areAnimationsEnabled)
    }
    var rank1Revealed by remember(hasAlreadyAnimated, areAnimationsEnabled) {
        mutableStateOf(hasAlreadyAnimated || !areAnimationsEnabled)
    }

    LaunchedEffect(top3, hasAlreadyAnimated, areAnimationsEnabled) {
        if (!hasAlreadyAnimated && areAnimationsEnabled) {
            // Sequential reveal: Rank 3 -> Rank 2 -> Rank 1 (~150ms gap)
            if (rank3 != null) {
                rank3Revealed = true
                delay(150L)
            }
            if (rank2 != null) {
                rank2Revealed = true
                delay(150L)
            }
            if (rank1 != null) {
                rank1Revealed = true
                delay(150L)
            }
            // Notify completion once after all podium items are revealed
            onAnimationComplete()
        } else {
            rank3Revealed = true
            rank2Revealed = true
            rank1Revealed = true
            if (!hasAlreadyAnimated) {
                onAnimationComplete()
            }
        }
    }

    val outlineVariant = MaterialTheme.colorScheme.outlineVariant
    val podiumBorder = remember(outlineVariant) {
        BorderStroke(1.dp, outlineVariant.copy(alpha = 0.5f))
    }

    Card(
        modifier = modifier
            .fillMaxWidth()
            .testTag("leaderboard_podium_card"),
        shape = PodiumCardShape,
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        ),
        border = podiumBorder,
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = Icons.Default.EmojiEvents,
                    contentDescription = null,
                    tint = AmberGold,
                    modifier = Modifier.size(18.dp)
                )
                Spacer(modifier = Modifier.width(6.dp))
                Text(
                    text = "Podium Teratas",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSurface
                )
            }
            Spacer(modifier = Modifier.height(14.dp))

            // Classic 2 - 1 - 3 Podium layout (Rank 2 on left, Rank 1 in center, Rank 3 on right)
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 4.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalAlignment = Alignment.Bottom
            ) {
                // Rank 2 (Silver)
                if (rank2 != null) {
                    PodiumColumnItem(
                        entry = rank2,
                        rank = 2,
                        pedestalHeight = 85.dp,
                        accentColor = Color(0xFFB0BEC5),
                        isCurrentUser = rank2.userId == currentUserId,
                        currentUserStreak = currentUserStreak,
                        isStreak = isStreak,
                        isRevealed = rank2Revealed,
                        areAnimationsEnabled = areAnimationsEnabled,
                        modifier = Modifier.weight(1f)
                    )
                } else {
                    PodiumEmptySlot(
                        rank = 2,
                        pedestalHeight = 85.dp,
                        accentColor = Color(0xFFB0BEC5),
                        modifier = Modifier.weight(1f)
                    )
                }

                // Rank 1 (Gold) - Elevated pedestal & focal center column
                if (rank1 != null) {
                    PodiumColumnItem(
                        entry = rank1,
                        rank = 1,
                        pedestalHeight = 115.dp,
                        accentColor = AmberGold,
                        isCurrentUser = rank1.userId == currentUserId,
                        currentUserStreak = currentUserStreak,
                        isStreak = isStreak,
                        isRevealed = rank1Revealed,
                        areAnimationsEnabled = areAnimationsEnabled,
                        modifier = Modifier.weight(1.15f)
                    )
                } else {
                    PodiumEmptySlot(
                        rank = 1,
                        pedestalHeight = 115.dp,
                        accentColor = AmberGold,
                        modifier = Modifier.weight(1.15f)
                    )
                }

                // Rank 3 (Bronze)
                if (rank3 != null) {
                    PodiumColumnItem(
                        entry = rank3,
                        rank = 3,
                        pedestalHeight = 65.dp,
                        accentColor = Color(0xFFCD7F32),
                        isCurrentUser = rank3.userId == currentUserId,
                        currentUserStreak = currentUserStreak,
                        isStreak = isStreak,
                        isRevealed = rank3Revealed,
                        areAnimationsEnabled = areAnimationsEnabled,
                        modifier = Modifier.weight(1f)
                    )
                } else {
                    PodiumEmptySlot(
                        rank = 3,
                        pedestalHeight = 65.dp,
                        accentColor = Color(0xFFCD7F32),
                        modifier = Modifier.weight(1f)
                    )
                }
            }
        }
    }
}

@Composable
private fun PodiumEmptySlot(
    rank: Int,
    pedestalHeight: androidx.compose.ui.unit.Dp,
    accentColor: Color,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .padding(horizontal = 2.dp)
            .alpha(0.55f),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Box(
            modifier = Modifier
                .size(if (rank == 1) 52.dp else 44.dp)
                .clip(CircleShape)
                .background(MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.6f))
                .border(1.dp, accentColor.copy(alpha = 0.35f), CircleShape),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = "?",
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                fontSize = 15.sp
            )
        }
        Spacer(modifier = Modifier.height(6.dp))
        Text(
            text = androidx.compose.ui.res.stringResource(com.example.R.string.podium_waiting_slot),
            style = MaterialTheme.typography.labelSmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis
        )
        Spacer(modifier = Modifier.height(6.dp))
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(pedestalHeight)
                .clip(PodiumPedestalShape)
                .background(MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.45f))
                .border(1.dp, accentColor.copy(alpha = 0.2f), PodiumPedestalShape),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = "#$rank",
                fontWeight = FontWeight.Bold,
                fontSize = if (rank == 1) 20.sp else 16.sp,
                color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.6f)
            )
        }
    }
}

@Composable
private fun PodiumColumnItem(
    entry: LeaderboardEntryCacheEntity,
    rank: Int,
    pedestalHeight: androidx.compose.ui.unit.Dp,
    accentColor: Color,
    isCurrentUser: Boolean,
    currentUserStreak: Int = 0,
    isStreak: Boolean = false,
    isRevealed: Boolean = true,
    areAnimationsEnabled: Boolean = true,
    modifier: Modifier = Modifier
) {
    val animatedHeight by animateDpAsState(
        targetValue = if (isRevealed) pedestalHeight else 0.dp,
        animationSpec = if (areAnimationsEnabled) {
            spring(
                dampingRatio = Spring.DampingRatioMediumBouncy,
                stiffness = Spring.StiffnessLow
            )
        } else {
            snap()
        },
        label = "podium_height_$rank"
    )

    val animatedAlpha by animateFloatAsState(
        targetValue = if (isRevealed) 1f else 0f,
        animationSpec = if (areAnimationsEnabled) {
            spring(
                dampingRatio = Spring.DampingRatioNoBouncy,
                stiffness = Spring.StiffnessLow
            )
        } else {
            snap()
        },
        label = "podium_alpha_$rank"
    )

    // Animated count-up for score
    val animatedScore by animateIntAsState(
        targetValue = if (isRevealed) entry.totalXp else 0,
        animationSpec = if (areAnimationsEnabled) tween(durationMillis = 600) else snap(),
        label = "podium_score_$rank"
    )

    val pedestalBrush = remember(accentColor) {
        Brush.verticalGradient(
            colors = listOf(
                accentColor.copy(alpha = 0.38f),
                accentColor.copy(alpha = 0.12f)
            )
        )
    }

    val avatarBg = remember(accentColor, isCurrentUser) {
        if (isCurrentUser) TealLight.copy(alpha = 0.3f) else accentColor.copy(alpha = 0.2f)
    }

    val pillBg = remember(isStreak, accentColor) {
        if (isStreak) FlameOrange.copy(alpha = 0.15f) else accentColor.copy(alpha = 0.15f)
    }

    Column(
        modifier = modifier
            .padding(horizontal = 2.dp)
            .alpha(animatedAlpha),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Crown or Rank Badge
        Text(
            text = when (rank) {
                1 -> "👑"
                2 -> "🥈"
                else -> "🥉"
            },
            fontSize = if (rank == 1) 22.sp else 18.sp
        )

        Spacer(modifier = Modifier.height(2.dp))

        val cleanEntryName = remember(entry.userName) { com.example.util.NameUtils.sanitizeDisplayName(entry.userName) }
        val entryInitial = remember(cleanEntryName) { com.example.util.NameUtils.extractInitial(cleanEntryName, "U") }

        // Avatar with level-specific sizing and border
        Box(
            modifier = Modifier
                .size(if (rank == 1) 52.dp else 44.dp)
                .clip(CircleShape)
                .background(avatarBg)
                .border(
                    width = if (isCurrentUser || rank == 1) 2.dp else 1.5.dp,
                    color = if (isCurrentUser) MaterialTheme.colorScheme.primary else accentColor.copy(alpha = 0.65f),
                    shape = CircleShape
                ),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = entryInitial,
                fontWeight = FontWeight.Bold,
                color = if (isCurrentUser) MaterialTheme.colorScheme.primary else accentColor,
                fontSize = if (rank == 1) 18.sp else 15.sp
            )
        }

        Spacer(modifier = Modifier.height(4.dp))

        // Name with single-line ellipsis
        Text(
            text = if (isCurrentUser) "$cleanEntryName (Kamu)" else cleanEntryName,
            style = MaterialTheme.typography.labelMedium,
            fontWeight = FontWeight.Bold,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
            color = if (isCurrentUser) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurface,
            modifier = Modifier.padding(horizontal = 2.dp)
        )

        // Level
        Text(
            text = "${levelTierEmoji(entry.level)} ${levelTierLabel(entry.level)}",
            style = MaterialTheme.typography.labelSmall,
            fontSize = 9.sp,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis
        )

        // XP or Streak Pill
        Surface(
            shape = PodiumPillShape,
            color = pillBg,
            modifier = Modifier.padding(top = 2.dp)
        ) {
            Text(
                text = if (isStreak) "🔥 $animatedScore hr" else "$animatedScore XP",
                style = MaterialTheme.typography.labelSmall,
                color = if (isStreak) FlameOrange else accentColor,
                fontWeight = FontWeight.Bold,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
            )
        }

        if (isCurrentUser && currentUserStreak > 0 && !isStreak) {
            Surface(
                shape = PodiumPillShape,
                color = FlameOrange.copy(alpha = 0.15f),
                modifier = Modifier.padding(top = 2.dp)
            ) {
                Text(
                    text = "🔥 $currentUserStreak hr",
                    style = MaterialTheme.typography.labelSmall,
                    color = FlameOrange,
                    fontWeight = FontWeight.Bold,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                )
            }
        }

        Spacer(modifier = Modifier.height(6.dp))

        // Pedestal with remembered gradient and defined border
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(animatedHeight)
                .clip(PodiumPedestalShape)
                .background(pedestalBrush)
                .border(
                    BorderStroke(1.dp, accentColor.copy(alpha = 0.35f)),
                    PodiumPedestalShape
                ),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = "#$rank",
                fontWeight = FontWeight.Black,
                fontSize = if (rank == 1) 24.sp else 18.sp,
                color = accentColor
            )
        }
    }
}

@Composable
fun LeaderboardRowItem(
    entry: LeaderboardEntryCacheEntity,
    isCurrentUser: Boolean,
    currentUserStreak: Int = 0,
    isStreak: Boolean = false,
    itemIndex: Int = 0,
    areAnimationsEnabled: Boolean = true,
    hasAlreadyAnimated: Boolean = false,
    onClick: (() -> Unit)? = null,
    modifier: Modifier = Modifier
) {
    var itemRevealed by remember(hasAlreadyAnimated, areAnimationsEnabled) {
        mutableStateOf(hasAlreadyAnimated || !areAnimationsEnabled)
    }

    LaunchedEffect(entry.userId, hasAlreadyAnimated, areAnimationsEnabled) {
        if (!hasAlreadyAnimated && areAnimationsEnabled) {
            val delayMillis = (itemIndex.coerceAtMost(12) * 30L)
            if (delayMillis > 0) delay(delayMillis)
            itemRevealed = true
        } else {
            itemRevealed = true
        }
    }

    val itemAlpha by animateFloatAsState(
        targetValue = if (itemRevealed) 1f else 0f,
        animationSpec = if (areAnimationsEnabled) tween(durationMillis = 260) else snap(),
        label = "row_alpha_${entry.rank}"
    )

    val itemSlideOffsetY by animateFloatAsState(
        targetValue = if (itemRevealed) 0f else 12f,
        animationSpec = if (areAnimationsEnabled) tween(durationMillis = 260) else snap(),
        label = "row_slide_${entry.rank}"
    )

    // Animated count-up for score in rows
    val animatedScore by animateIntAsState(
        targetValue = if (itemRevealed) entry.totalXp else 0,
        animationSpec = if (areAnimationsEnabled) tween(durationMillis = 450) else snap(),
        label = "row_score_${entry.rank}"
    )

    val outlineVariant = MaterialTheme.colorScheme.outlineVariant
    val surfaceColor = MaterialTheme.colorScheme.surface
    val rowBorder = remember(isCurrentUser, outlineVariant) {
        if (isCurrentUser) {
            BorderStroke(1.5.dp, TealPrimary.copy(alpha = 0.6f))
        } else {
            BorderStroke(1.dp, outlineVariant.copy(alpha = 0.4f))
        }
    }
    val containerColor = remember(isCurrentUser, surfaceColor) {
        if (isCurrentUser) {
            TealPrimary.copy(alpha = 0.12f)
        } else {
            surfaceColor
        }
    }
    val pillBgColor = remember(isStreak) {
        if (isStreak) FlameOrange.copy(alpha = 0.15f) else AmberGold.copy(alpha = 0.15f)
    }
    val avatarBgColor = remember(isCurrentUser) {
        if (isCurrentUser) TealLight.copy(alpha = 0.25f) else CyanAccent.copy(alpha = 0.15f)
    }

    Card(
        modifier = modifier
            .fillMaxWidth()
            .offset(y = itemSlideOffsetY.dp)
            .alpha(itemAlpha)
            .then(if (onClick != null) Modifier.clickable(onClick = onClick) else Modifier)
            .testTag("leaderboard_row_${entry.rank}"),
        shape = RowCardShape,
        colors = CardDefaults.cardColors(
            containerColor = containerColor
        ),
        border = rowBorder,
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 14.dp, vertical = 10.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Rank Number
            Box(
                modifier = Modifier.size(28.dp),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "#${entry.rank}",
                    style = MaterialTheme.typography.titleSmall,
                    fontWeight = FontWeight.Bold,
                    color = if (isCurrentUser) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurfaceVariant
                )
            }

            Spacer(modifier = Modifier.width(10.dp))

            // Avatar circle
            val cleanRowName = remember(entry.userName) { com.example.util.NameUtils.sanitizeDisplayName(entry.userName) }
            val rowInitial = remember(cleanRowName) { com.example.util.NameUtils.extractInitial(cleanRowName, "U") }
            Box(
                modifier = Modifier
                    .size(38.dp)
                    .clip(CircleShape)
                    .background(avatarBgColor)
                    .then(
                        if (isCurrentUser) {
                            Modifier.border(1.5.dp, MaterialTheme.colorScheme.primary, CircleShape)
                        } else {
                            Modifier
                        }
                    ),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = rowInitial,
                    fontWeight = FontWeight.Bold,
                    color = if (isCurrentUser) MaterialTheme.colorScheme.primary else CyanAccentText,
                    fontSize = 14.sp
                )
            }

            Spacer(modifier = Modifier.width(12.dp))

            // User info
            Column(modifier = Modifier.weight(1f)) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        text = cleanRowName,
                        style = MaterialTheme.typography.bodyMedium,
                        fontWeight = if (isCurrentUser) FontWeight.Bold else FontWeight.Medium,
                        color = MaterialTheme.colorScheme.onSurface,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                    if (isCurrentUser) {
                        Spacer(modifier = Modifier.width(6.dp))
                        Surface(
                            shape = RowTagShape,
                            color = TealPrimary.copy(alpha = 0.2f)
                        ) {
                            Text(
                                text = "Kamu",
                                style = MaterialTheme.typography.labelSmall,
                                color = MaterialTheme.colorScheme.primary,
                                fontWeight = FontWeight.Bold,
                                modifier = Modifier.padding(horizontal = 4.dp, vertical = 1.dp)
                            )
                        }
                    }
                    if (isCurrentUser && currentUserStreak > 0 && !isStreak) {
                        Spacer(modifier = Modifier.width(6.dp))
                        Surface(
                            shape = RowTagShape,
                            color = FlameOrange.copy(alpha = 0.15f)
                        ) {
                            Text(
                                text = "🔥 $currentUserStreak hr",
                                style = MaterialTheme.typography.labelSmall,
                                color = FlameOrange,
                                fontWeight = FontWeight.Bold,
                                modifier = Modifier.padding(horizontal = 4.dp, vertical = 1.dp)
                            )
                        }
                    }
                }

                Text(
                    text = "${levelTierEmoji(entry.level)} Level ${entry.level} · ${levelTierLabel(entry.level)}",
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }

            // Score Pill (XP or Streak) with animated count-up
            Surface(
                shape = RowPillShape,
                color = pillBgColor
            ) {
                Row(
                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = if (isStreak) Icons.Default.Whatshot else Icons.Default.Star,
                        contentDescription = null,
                        tint = if (isStreak) FlameOrange else AmberGold,
                        modifier = Modifier.size(13.dp)
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = if (isStreak) "$animatedScore hari" else "$animatedScore XP",
                        style = MaterialTheme.typography.labelSmall,
                        color = if (isStreak) FlameOrange else AmberGold,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }
    }
}

