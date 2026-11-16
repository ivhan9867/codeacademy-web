package com.example.ui.screens

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsDraggedAsState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.Bolt
import androidx.compose.material.icons.filled.EmojiEvents
import androidx.compose.material.icons.filled.Lightbulb
import androidx.compose.material.icons.filled.LocalFireDepartment
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.School
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.graphics.luminance
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.example.R
import com.example.ui.components.PulsingStreakFlameIcon
import com.example.ui.theme.AmberGold
import com.example.ui.theme.CyanAccent
import com.example.ui.theme.CyanAccentText
import com.example.ui.theme.FlameOrange
import com.example.ui.theme.PurpleLevel
import com.example.ui.theme.TealDark
import com.example.ui.theme.TealLight
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlin.math.absoluteValue

/**
 * Interactive HorizontalPager Carousel at the top of HomeScreen.
 * Features:
 * - Native gestures and continuous swipe transition with zero lag.
 * - Smooth Auto-slide (4.5s) that automatically pauses while user is dragging manually and restarts upon release.
 * - Performant GPU graphicsLayer scale + alpha interpolation.
 * - Non-redundant content:
 *    Slide 0: Hero Promo Banner (Mobile-First Coding)
 *    Slide 1: XP Terkumpul Minggu Ini & Level Progress
 *    Slide 2: Rekomendasi Kelas Pilihan & Tips Belajar Harian
 * - Single-shot XP count-up animation only triggered when Slide 1 is active.
 * - Animations disabled on inactive slides to eliminate background recomposition/frame overhead.
 */
@Composable
fun HomeScreenTopCarousel(
    currentStreak: Int,
    longestStreak: Int,
    totalXp: Int,
    currentLevel: Int,
    onStartLearning: () -> Unit,
    modifier: Modifier = Modifier,
    isParentScrolling: Boolean = false,
    recommendedCourseTitle: String = "CSS Flexbox & Layout Modern",
    recommendedCourseDescription: String = "Kuasai tata letak responsif 2 dimensi langsung di smartphone dengan praktik instan.",
    onExploreRecommendation: () -> Unit = onStartLearning
) {
    val pagerState = rememberPagerState(pageCount = { 3 })
    val coroutineScope = rememberCoroutineScope()
    var manualSelectionTrigger by remember { mutableIntStateOf(0) }
    val isDragged by pagerState.interactionSource.collectIsDraggedAsState()
    val settledCurrentPage by remember { derivedStateOf { pagerState.currentPage } }

    // Auto-slide effect with pause on horizontal drag OR parent vertical scroll (listState.isScrollInProgress),
    // and restart on release / dot navigation so carousel animation never collides with LazyColumn scroll frames.
    LaunchedEffect(isDragged, isParentScrolling, manualSelectionTrigger) {
        if (!isDragged && !isParentScrolling) {
            while (true) {
                delay(4500L)
                if (!isDragged && !isParentScrolling) {
                    val nextPage = (pagerState.currentPage + 1) % 3
                    pagerState.animateScrollToPage(
                        page = nextPage,
                        animationSpec = tween(durationMillis = 550, easing = FastOutSlowInEasing)
                    )
                }
            }
        }
    }

    // Target weekly XP (or derived from total XP, default 180 + totalXp % 100)
    val targetWeeklyXp = remember(totalXp) {
        if (totalXp > 0) (totalXp.coerceAtMost(350)) else 180
    }

    Column(
        modifier = modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        HorizontalPager(
            state = pagerState,
            key = { page ->
                when (page) {
                    0 -> "carousel_slide_hero_promo"
                    1 -> "carousel_slide_weekly_xp"
                    2 -> "carousel_slide_recommendation"
                    else -> "carousel_slide_$page"
                }
            },
            modifier = Modifier
                .fillMaxWidth()
                .heightIn(min = 188.dp)
                .testTag("home_top_carousel_pager"),
            contentPadding = PaddingValues(horizontal = 0.dp),
            pageSpacing = 12.dp
        ) { page ->
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .graphicsLayer {
                        val offsetFraction = pagerState.currentPageOffsetFraction
                        if (offsetFraction == 0f && pagerState.currentPage == page) {
                            // Fast-path when settled on active page: zero interpolation overhead during vertical scroll
                            scaleX = 1f
                            scaleY = 1f
                            alpha = 1f
                        } else {
                            // Calculate offset relative to active page only while pager is transitioning
                            val pageOffset = ((pagerState.currentPage - page) + offsetFraction).absoluteValue
                            val fraction = (1f - pageOffset.coerceIn(0f, 1f))
                            // Scale from 0.92 to 1.0, Alpha from 0.60 to 1.0
                            val scale = 0.92f + 0.08f * fraction
                            val pageAlpha = 0.60f + 0.40f * fraction
                            scaleX = scale
                            scaleY = scale
                            alpha = pageAlpha
                        }
                    }
            ) {
                when (page) {
                    0 -> {
                        HeroPromoBannerCard(onStartLearning = onStartLearning)
                    }
                    1 -> {
                        WeeklyXpCard(
                            isActivePage = settledCurrentPage == 1,
                            targetWeeklyXp = targetWeeklyXp,
                            targetGoalXp = (targetWeeklyXp + 70).coerceAtLeast(250),
                            currentLevel = currentLevel,
                            totalXp = totalXp
                        )
                    }
                    2 -> {
                        RecommendedCourseCarouselCard(
                            title = recommendedCourseTitle,
                            description = recommendedCourseDescription,
                            onExplore = onExploreRecommendation
                        )
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(10.dp))

        // Animated Dot / Pill Indicators
        Row(
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.fillMaxWidth()
        ) {
            repeat(3) { idx ->
                val isSelected = settledCurrentPage == idx
                val animatedWidth by animateDpAsState(
                    targetValue = if (isSelected) 24.dp else 7.dp,
                    animationSpec = spring(
                        dampingRatio = Spring.DampingRatioMediumBouncy,
                        stiffness = Spring.StiffnessMedium
                    ),
                    label = "carousel_dot_width_$idx"
                )
                val animatedColor by animateColorAsState(
                    targetValue = if (isSelected) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.28f),
                    animationSpec = tween(durationMillis = 250),
                    label = "carousel_dot_color_$idx"
                )

                Box(
                    modifier = Modifier
                        .padding(horizontal = 3.5.dp)
                        .height(7.dp)
                        .width(animatedWidth)
                        .clip(CircleShape)
                        .background(animatedColor)
                        .testTag("carousel_dot_$idx")
                        .clickable(
                            interactionSource = remember { MutableInteractionSource() },
                            indication = null
                        ) {
                            manualSelectionTrigger++
                            coroutineScope.launch {
                                pagerState.animateScrollToPage(idx)
                            }
                        }
                )
            }
        }
    }
}

/**
 * Slide 0: Promo Banner Hero Card
 */
@Composable
fun HeroPromoBannerCard(
    onStartLearning: () -> Unit,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val heroOverlayGradient = remember {
        Brush.horizontalGradient(
            colors = listOf(
                TealDark.copy(alpha = 0.96f),
                TealDark.copy(alpha = 0.86f),
                Color(0xFF0F172A).copy(alpha = 0.72f)
            )
        )
    }

    val imageRequest = remember(context) {
        ImageRequest.Builder(context)
            .data(R.drawable.hero_banner)
            .memoryCacheKey("res_${R.drawable.hero_banner}")
            .crossfade(true)
            .build()
    }
    val heroPainter = painterResource(id = R.drawable.hero_banner)

    Card(
        modifier = modifier.fillMaxSize(),
        shape = RoundedCornerShape(20.dp),
        border = BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.5f)),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
    ) {
        Box(modifier = Modifier.fillMaxSize()) {
            AsyncImage(
                model = imageRequest,
                placeholder = heroPainter,
                error = heroPainter,
                contentDescription = androidx.compose.ui.res.stringResource(com.example.R.string.carousel_hero_cd),
                contentScale = ContentScale.Crop,
                modifier = Modifier.fillMaxSize()
            )

            // Multi-stop gradient overlay
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(heroOverlayGradient)
            )

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(18.dp),
                verticalArrangement = Arrangement.Center
            ) {
                Surface(
                    color = CyanAccent.copy(alpha = 0.28f),
                    shape = RoundedCornerShape(8.dp)
                ) {
                    Text(
                        text = androidx.compose.ui.res.stringResource(com.example.R.string.carousel_hero_badge),
                        color = CyanLight,
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 3.dp)
                    )
                }

                Spacer(modifier = Modifier.height(6.dp))

                Text(
                    text = androidx.compose.ui.res.stringResource(com.example.R.string.carousel_hero_title),
                    color = Color.White,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    lineHeight = 22.sp
                )

                Spacer(modifier = Modifier.height(10.dp))

                Button(
                    onClick = onStartLearning,
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color.White,
                        contentColor = TealDark
                    ),
                    shape = RoundedCornerShape(10.dp),
                    contentPadding = PaddingValues(horizontal = 14.dp, vertical = 6.dp),
                    modifier = Modifier
                        .height(34.dp)
                        .testTag("btn_hero_start_learning")
                ) {
                    Text(
                        text = androidx.compose.ui.res.stringResource(com.example.R.string.carousel_hero_btn_start),
                        color = TealDark,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Icon(
                        imageVector = Icons.Default.PlayArrow,
                        contentDescription = null,
                        tint = TealDark,
                        modifier = Modifier.size(14.dp)
                    )
                }
            }
        }
    }
}

/**
 * Slide 1: XP Terkumpul Minggu Ini Card
 * Polished in both Light and Dark mode with soft gradients, decorative floating shapes,
 * animated XP numbers, and goal tracking.
 */
@Composable
fun WeeklyXpCard(
    targetWeeklyXp: Int,
    targetGoalXp: Int,
    currentLevel: Int,
    totalXp: Int,
    modifier: Modifier = Modifier,
    isActivePage: Boolean = true
) {
    val isDark = MaterialTheme.colorScheme.surface.luminance() < 0.5f

    // Single-shot XP count-up animation only triggered when page is actively viewed
    val xpAnim = remember { Animatable(0f) }
    var hasAnimated by remember { mutableStateOf(false) }

    LaunchedEffect(isActivePage) {
        if (isActivePage && !hasAnimated) {
            hasAnimated = true
            xpAnim.animateTo(
                targetValue = targetWeeklyXp.toFloat(),
                animationSpec = tween(durationMillis = 650, easing = FastOutSlowInEasing)
            )
        }
    }

    val displayXp = if (hasAnimated) xpAnim.value.toInt() else targetWeeklyXp

    Card(
        modifier = modifier.fillMaxSize(),
        shape = RoundedCornerShape(20.dp),
        border = BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
    ) {
        Box(
            modifier = Modifier.fillMaxSize()
        ) {
            // Decorative background element: Large semi-transparent rotated star/trophy
            Icon(
                imageVector = Icons.Default.EmojiEvents,
                contentDescription = null,
                tint = AmberGold.copy(alpha = if (isDark) 0.08f else 0.10f),
                modifier = Modifier
                    .size(130.dp)
                    .align(Alignment.BottomEnd)
                    .offset(x = 24.dp, y = 24.dp)
                    .rotate(-15f)
            )

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 18.dp, vertical = 14.dp),
                verticalArrangement = Arrangement.SpaceBetween
            ) {
                // Top header row
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Surface(
                        color = AmberGold.copy(alpha = 0.18f),
                        shape = RoundedCornerShape(8.dp)
                    ) {
                        Row(
                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 3.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(
                                imageVector = Icons.Default.Bolt,
                                contentDescription = null,
                                tint = AmberGold,
                                modifier = Modifier.size(12.dp)
                            )
                            Spacer(modifier = Modifier.width(4.dp))
                            Text(
                                text = androidx.compose.ui.res.stringResource(com.example.R.string.carousel_weekly_xp_badge),
                                color = AmberGold,
                                fontSize = 10.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }

                    Surface(
                        color = PurpleLevel.copy(alpha = 0.18f),
                        shape = RoundedCornerShape(8.dp)
                    ) {
                        Text(
                            text = androidx.compose.ui.res.stringResource(com.example.R.string.carousel_weekly_xp_level, currentLevel),
                            color = PurpleLevel,
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 3.dp)
                        )
                    }
                }

                // Main Stats Row
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(
                        modifier = Modifier
                            .size(44.dp)
                            .clip(CircleShape)
                            .background(AmberGold.copy(alpha = 0.18f)),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.Star,
                            contentDescription = null,
                            tint = AmberGold,
                            modifier = Modifier.size(24.dp)
                        )
                    }

                    Spacer(modifier = Modifier.width(12.dp))

                    Column {
                        Text(
                            text = androidx.compose.ui.res.stringResource(com.example.R.string.carousel_weekly_xp_plus_format, displayXp),
                            style = MaterialTheme.typography.headlineMedium.copy(
                                fontWeight = FontWeight.ExtraBold,
                                color = AmberGold
                            ),
                            fontSize = 24.sp
                        )
                        Text(
                            text = androidx.compose.ui.res.stringResource(com.example.R.string.carousel_weekly_xp_account_total, totalXp),
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                            fontWeight = FontWeight.Medium
                        )
                    }
                }

                // Bottom progress / motivation bar
                Column {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(
                            text = androidx.compose.ui.res.stringResource(com.example.R.string.carousel_weekly_xp_target_goal, targetGoalXp),
                            fontSize = 11.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                        val pct = ((displayXp.toFloat() / targetGoalXp.toFloat()) * 100).toInt().coerceIn(0, 100)
                        Text(
                            text = androidx.compose.ui.res.stringResource(com.example.R.string.carousel_weekly_xp_percent, pct),
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold,
                            color = AmberGold
                        )
                    }
                    Spacer(modifier = Modifier.height(4.dp))
                    val progressFraction = (displayXp.toFloat() / targetGoalXp.toFloat()).coerceIn(0f, 1f)
                    LinearProgressIndicator(
                        progress = { progressFraction },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(5.dp)
                            .clip(RoundedCornerShape(3.dp)),
                        color = AmberGold,
                        trackColor = if (isDark) MaterialTheme.colorScheme.surfaceVariant else Color(0xFFFFEDD5),
                        drawStopIndicator = {}
                    )
                }
            }
        }
    }
}

/**
 * Overload for backward compatibility
 */
@Composable
fun WeeklyXpCard(
    animatedXp: Int,
    targetGoalXp: Int,
    currentLevel: Int,
    totalXp: Int,
    modifier: Modifier = Modifier
) {
    WeeklyXpCard(
        targetWeeklyXp = animatedXp,
        targetGoalXp = targetGoalXp,
        currentLevel = currentLevel,
        totalXp = totalXp,
        modifier = modifier,
        isActivePage = true
    )
}

/**
 * Slide 2: Rekomendasi Kelas & Tips Belajar Harian Card
 * Replaces the redundant streak card with highly relevant, actionable learning content.
 */
@Composable
fun RecommendedCourseCarouselCard(
    onExplore: () -> Unit,
    modifier: Modifier = Modifier,
    title: String = "CSS Flexbox & Layout Modern",
    description: String = "Kuasai tata letak responsif 2 dimensi langsung di smartphone dengan praktik instan.",
    durationMinutes: Int = 15,
    rewardXp: Int = 50
) {
    val isDark = MaterialTheme.colorScheme.surface.luminance() < 0.5f

    Card(
        modifier = modifier.fillMaxSize(),
        shape = RoundedCornerShape(20.dp),
        border = BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
    ) {
        Box(
            modifier = Modifier.fillMaxSize()
        ) {
            // Decorative background element: Large semi-transparent icon
            Icon(
                imageVector = Icons.Default.AutoAwesome,
                contentDescription = null,
                tint = TealLight.copy(alpha = if (isDark) 0.08f else 0.10f),
                modifier = Modifier
                    .size(135.dp)
                    .align(Alignment.BottomEnd)
                    .offset(x = 20.dp, y = 20.dp)
                    .rotate(15f)
            )

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 18.dp, vertical = 14.dp),
                verticalArrangement = Arrangement.SpaceBetween
            ) {
                // Top header row
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Surface(
                        color = TealLight.copy(alpha = 0.18f),
                        shape = RoundedCornerShape(8.dp)
                    ) {
                        Row(
                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 3.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(
                                imageVector = Icons.Default.Lightbulb,
                                contentDescription = null,
                                tint = MaterialTheme.colorScheme.primary,
                                modifier = Modifier.size(12.dp)
                            )
                            Spacer(modifier = Modifier.width(4.dp))
                            Text(
                                text = androidx.compose.ui.res.stringResource(com.example.R.string.carousel_recommended_badge),
                                color = MaterialTheme.colorScheme.primary,
                                fontSize = 10.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }

                    Surface(
                        color = AmberGold.copy(alpha = 0.18f),
                        shape = RoundedCornerShape(8.dp)
                    ) {
                        Text(
                            text = androidx.compose.ui.res.stringResource(com.example.R.string.carousel_recommended_reward, rewardXp),
                            color = AmberGold,
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 3.dp)
                        )
                    }
                }

                // Main Info Row
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(
                        modifier = Modifier
                            .size(44.dp)
                            .clip(CircleShape)
                            .background(TealLight.copy(alpha = 0.18f)),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.School,
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.primary,
                            modifier = Modifier.size(24.dp)
                        )
                    }

                    Spacer(modifier = Modifier.width(12.dp))

                    Column {
                        Text(
                            text = title,
                            style = MaterialTheme.typography.titleMedium.copy(
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.onSurface
                            ),
                            fontSize = 15.sp,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )
                        Text(
                            text = description,
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                            maxLines = 2,
                            overflow = TextOverflow.Ellipsis,
                            lineHeight = 16.sp
                        )
                    }
                }

                // Bottom row: Info tags & CTA
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Surface(
                            color = if (isDark) MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.6f) else MaterialTheme.colorScheme.surfaceVariant,
                            shape = RoundedCornerShape(6.dp)
                        ) {
                            Text(
                                text = androidx.compose.ui.res.stringResource(com.example.R.string.carousel_recommended_duration, durationMinutes),
                                fontSize = 11.sp,
                                fontWeight = FontWeight.SemiBold,
                                color = MaterialTheme.colorScheme.onSurface,
                                modifier = Modifier.padding(horizontal = 7.dp, vertical = 2.dp)
                            )
                        }

                        Spacer(modifier = Modifier.width(6.dp))

                        Surface(
                            color = CyanAccent.copy(alpha = 0.15f),
                            shape = RoundedCornerShape(6.dp)
                        ) {
                            Text(
                                text = androidx.compose.ui.res.stringResource(com.example.R.string.carousel_recommended_hands_on),
                                fontSize = 11.sp,
                                fontWeight = FontWeight.SemiBold,
                                color = CyanAccentText,
                                modifier = Modifier.padding(horizontal = 7.dp, vertical = 2.dp)
                            )
                        }
                    }

                    Button(
                        onClick = onExplore,
                        colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary),
                        shape = RoundedCornerShape(8.dp),
                        contentPadding = PaddingValues(horizontal = 12.dp, vertical = 4.dp),
                        modifier = Modifier
                            .height(30.dp)
                            .testTag("btn_carousel_recommended_start")
                    ) {
                        Text(
                            text = androidx.compose.ui.res.stringResource(com.example.R.string.carousel_recommended_btn_start),
                            color = Color.Black,
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold
                        )
                        Spacer(modifier = Modifier.width(3.dp))
                        Icon(
                            imageVector = Icons.Default.PlayArrow,
                            contentDescription = null,
                            tint = Color.Black,
                            modifier = Modifier.size(13.dp)
                        )
                    }
                }
            }
        }
    }
}

/**
 * Retained for backward compatibility and specialized use cases.
 * PulsingStreakFlameIcon animation is strictly throttled to only run when isActivePage is true.
 */
@Composable
fun StreakCarouselCard(
    currentStreak: Int,
    longestStreak: Int,
    modifier: Modifier = Modifier,
    isActivePage: Boolean = false
) {
    val isDark = MaterialTheme.colorScheme.surface.luminance() < 0.5f

    Card(
        modifier = modifier.fillMaxSize(),
        shape = RoundedCornerShape(20.dp),
        border = BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
    ) {
        Box(
            modifier = Modifier.fillMaxSize()
        ) {
            Icon(
                imageVector = Icons.Default.LocalFireDepartment,
                contentDescription = null,
                tint = FlameOrange.copy(alpha = if (isDark) 0.08f else 0.10f),
                modifier = Modifier
                    .size(135.dp)
                    .align(Alignment.BottomEnd)
                    .offset(x = 20.dp, y = 20.dp)
                    .rotate(10f)
            )

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 18.dp, vertical = 14.dp),
                verticalArrangement = Arrangement.SpaceBetween
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Surface(
                        color = FlameOrange.copy(alpha = 0.18f),
                        shape = RoundedCornerShape(8.dp)
                    ) {
                        Row(
                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 3.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            PulsingStreakFlameIcon(
                                isStreakActive = isActivePage && currentStreak > 0,
                                size = 14.dp,
                                tint = FlameOrange
                            )
                            Spacer(modifier = Modifier.width(4.dp))
                            Text(
                                text = androidx.compose.ui.res.stringResource(com.example.R.string.carousel_streak_badge),
                                color = FlameOrange,
                                fontSize = 10.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }

                    Surface(
                        color = TealLight.copy(alpha = 0.18f),
                        shape = RoundedCornerShape(8.dp)
                    ) {
                        Text(
                            text = androidx.compose.ui.res.stringResource(com.example.R.string.carousel_streak_rate),
                            color = MaterialTheme.colorScheme.primary,
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 3.dp)
                        )
                    }
                }

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(
                        modifier = Modifier
                            .size(44.dp)
                            .clip(CircleShape)
                            .background(FlameOrange.copy(alpha = 0.18f)),
                        contentAlignment = Alignment.Center
                    ) {
                        PulsingStreakFlameIcon(
                            isStreakActive = isActivePage && currentStreak > 0,
                            size = 26.dp,
                            tint = FlameOrange
                        )
                    }

                    Spacer(modifier = Modifier.width(12.dp))

                    Column {
                        Text(
                            text = androidx.compose.ui.res.stringResource(com.example.R.string.carousel_streak_days_count, currentStreak),
                            style = MaterialTheme.typography.headlineMedium.copy(
                                fontWeight = FontWeight.ExtraBold,
                                color = FlameOrange
                            ),
                            fontSize = 20.sp
                        )
                        Text(
                            text = androidx.compose.ui.res.stringResource(com.example.R.string.carousel_streak_best_record, longestStreak),
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                            fontWeight = FontWeight.Medium
                        )
                    }
                }

                Surface(
                    color = if (isDark) MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.7f) else MaterialTheme.colorScheme.surface.copy(alpha = 0.85f),
                    shape = RoundedCornerShape(10.dp)
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 10.dp, vertical = 6.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = if (currentStreak > 0) {
                                androidx.compose.ui.res.stringResource(com.example.R.string.carousel_streak_active_msg)
                            } else {
                                androidx.compose.ui.res.stringResource(com.example.R.string.carousel_streak_inactive_msg)
                            },
                            style = MaterialTheme.typography.labelSmall,
                            color = MaterialTheme.colorScheme.onSurface,
                            fontWeight = FontWeight.Medium,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )
                    }
                }
            }
        }
    }
}
