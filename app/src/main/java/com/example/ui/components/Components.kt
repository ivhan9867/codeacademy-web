package com.example.ui.components

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.webkit.ConsoleMessage
import android.webkit.WebChromeClient
import android.webkit.WebView
import android.widget.Toast
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.ContentCopy
import androidx.compose.material.icons.filled.EmojiEvents
import androidx.compose.material.icons.filled.LocalFireDepartment
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Badge
import androidx.compose.material3.BadgedBox
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.example.R
import com.example.ui.theme.ErrorRed
import com.example.ui.theme.AppButtonHeight
import com.example.ui.theme.AppButtonShape
import com.example.ui.theme.Dimens
import androidx.compose.ui.graphics.Shape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.data.models.GamificationLevels
import com.example.ui.theme.AmberGold
import com.example.ui.theme.CodeDarkBg
import com.example.ui.theme.CodeDarkBorder
import com.example.ui.theme.CodeDarkSurface
import com.example.ui.theme.CyanAccent
import com.example.ui.theme.FlameOrange
import com.example.ui.theme.PurpleLevel
import com.example.ui.theme.SuccessGreen
import com.example.ui.theme.TealLight
import com.example.ui.theme.TealPrimary
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.StateFlow

@Composable
fun NotificationBadge(
    unreadNotifsFlow: StateFlow<Int>,
    modifier: Modifier = Modifier
) {
    val unreadCount by unreadNotifsFlow.collectAsStateWithLifecycle()
    if (unreadCount > 0) {
        Badge(
            containerColor = FlameOrange,
            contentColor = Color.White,
            modifier = modifier
        ) {
            Text(
                text = if (unreadCount > 99) "99+" else "$unreadCount",
                fontSize = 10.sp,
                fontWeight = FontWeight.Bold
            )
        }
    }
}

@Composable
fun TopHeaderBar(
    userName: String,
    totalXp: Int,
    currentLevel: Int,
    unreadNotifs: Int = 0,
    unreadNotifsFlow: StateFlow<Int>? = null,
    isLoading: Boolean = false,
    onNotifClick: () -> Unit,
    onProfileClick: () -> Unit = {},
    onMenuClick: (() -> Unit)? = null,
    modifier: Modifier = Modifier
) {
    val levelInfo = GamificationLevels.getLevelForXp(totalXp)
    val xpProgress = GamificationLevels.getLevelProgress(totalXp)
    val initialLetter = userName.trim().take(1).uppercase().ifEmpty { "U" }

    // XP Progress bar fills smoothly from 0 to actual progress upon first display (single-shot, ~800ms)
    var isXpProgressStarted by remember { mutableStateOf(false) }
    LaunchedEffect(Unit) {
        isXpProgressStarted = true
    }
    val animatedXpProgress by animateFloatAsState(
        targetValue = if (isXpProgressStarted) xpProgress else 0f,
        animationSpec = tween(durationMillis = 800, easing = FastOutSlowInEasing),
        label = "header_xp_progress_anim"
    )

    val headerSurfaceColor = MaterialTheme.colorScheme.surface
    val headerBgBrush = remember(headerSurfaceColor) {
        Brush.verticalGradient(
            colors = listOf(
                headerSurfaceColor,
                headerSurfaceColor.copy(alpha = 0.95f)
            )
        )
    }

    Column(
        modifier = modifier
            .fillMaxWidth()
            .background(headerBgBrush)
            .padding(horizontal = 16.dp, vertical = 12.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.weight(1f, fill = false)
            ) {
                if (onMenuClick != null) {
                    IconButton(
                        onClick = onMenuClick,
                        modifier = Modifier
                            .testTag("btn_menu_home")
                            .size(40.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Menu,
                            contentDescription = stringResource(R.string.catalog_nav_menu_cd),
                            tint = MaterialTheme.colorScheme.onSurface
                        )
                    }
                    Spacer(modifier = Modifier.width(6.dp))
                }

                // Profile Avatar & Greeting (Clickable to switch to Profile tab)
                Row(
                    modifier = Modifier
                        .clip(RoundedCornerShape(12.dp))
                        .clickable(
                            role = Role.Button,
                            onClickLabel = stringResource(R.string.comp_open_profile_label),
                            onClick = onProfileClick
                        )
                        .padding(vertical = 4.dp, horizontal = 2.dp)
                        .testTag("header_profile_section"),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                // Avatar circle matching ProfileScreen visual identity (TealPrimary background + white bold initial)
                if (isLoading) {
                    Box(
                        modifier = Modifier
                            .size(42.dp)
                            .clip(CircleShape)
                            .background(rememberShimmerBrush())
                            .testTag("header_avatar_button")
                    )
                } else {
                    Box(
                        modifier = Modifier
                            .size(42.dp)
                            .clip(CircleShape)
                            .background(TealPrimary)
                            .border(1.dp, TealLight.copy(alpha = 0.25f), CircleShape)
                            .testTag("header_avatar_button"),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = initialLetter,
                            color = Color.White,
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }

                Spacer(modifier = Modifier.width(10.dp))

                // Greeting & Name with overflow protection to prevent squeezing other elements
                Column(modifier = Modifier.weight(1f, fill = false)) {
                    Text(
                        text = stringResource(R.string.comp_welcome_greeting),
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    if (isLoading) {
                        Box(
                            modifier = Modifier
                                .width(120.dp)
                                .height(18.dp)
                                .clip(RoundedCornerShape(4.dp))
                                .background(rememberShimmerBrush())
                        )
                    } else {
                        Text(
                            text = userName,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis,
                            style = MaterialTheme.typography.titleMedium.copy(
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.onSurface
                            )
                        )
                    }
                }
            }
            }

            Spacer(modifier = Modifier.width(8.dp))

            // Top action button: Notification Bell with unread count
            Row(verticalAlignment = Alignment.CenterVertically) {
                IconButton(onClick = onNotifClick) {
                    BadgedBox(
                        badge = {
                            if (unreadNotifsFlow != null) {
                                NotificationBadge(unreadNotifsFlow = unreadNotifsFlow)
                            } else if (unreadNotifs > 0) {
                                Badge(containerColor = FlameOrange) {
                                    Text(
                                        text = "$unreadNotifs",
                                        color = Color.White,
                                        fontSize = 10.sp
                                    )
                                }
                            }
                        }
                    ) {
                        Icon(
                            imageVector = Icons.Default.Notifications,
                            contentDescription = stringResource(R.string.comp_notifications_cd),
                            tint = MaterialTheme.colorScheme.onSurface
                        )
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(10.dp))

        // Gamification Header Strip (Level Badge + XP Bar)
        if (isLoading) {
            val shimmerBrush = rememberShimmerBrush()
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(12.dp))
                    .background(MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.6f))
                    .padding(horizontal = 12.dp, vertical = 8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(
                    modifier = Modifier
                        .width(48.dp)
                        .height(24.dp)
                        .clip(RoundedCornerShape(8.dp))
                        .background(shimmerBrush)
                )

                Spacer(modifier = Modifier.width(8.dp))

                Column(modifier = Modifier.weight(1f)) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth(0.45f)
                            .height(12.dp)
                            .clip(RoundedCornerShape(3.dp))
                            .background(shimmerBrush)
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(6.dp)
                            .clip(RoundedCornerShape(3.dp))
                            .background(shimmerBrush)
                    )
                }
            }
        } else {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(12.dp))
                    .background(MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.6f))
                    .padding(horizontal = 12.dp, vertical = 8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Level Pill
                Surface(
                    color = PurpleLevel,
                    shape = RoundedCornerShape(8.dp)
                ) {
                    Text(
                        text = "Lv. $currentLevel",
                        color = Color.White,
                        fontWeight = FontWeight.Bold,
                        fontSize = 11.sp,
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                    )
                }

                Spacer(modifier = Modifier.width(8.dp))

                Column(modifier = Modifier.weight(1f)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(
                            text = com.example.util.localization.DataLabelMappings.xpLevelName(levelInfo.level, levelInfo.name),
                            style = MaterialTheme.typography.labelSmall,
                            fontWeight = FontWeight.SemiBold,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                        Text(
                            text = "$totalXp XP",
                            style = MaterialTheme.typography.labelSmall,
                            fontWeight = FontWeight.Bold,
                            color = AmberGold
                        )
                    }

                    Spacer(modifier = Modifier.height(4.dp))

                    LinearProgressIndicator(
                        progress = { animatedXpProgress },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(6.dp)
                            .clip(RoundedCornerShape(3.dp)),
                        color = AmberGold,
                        trackColor = MaterialTheme.colorScheme.surface
                    )
                }
            }
        }
    }
}

/**
 * Leaf composable for flame icon.
 * Performs a gentle 2-cycle celebratory pulse when streak is active on entry/update, then settles quietly to avoid GPU frame drain.
 * Uses graphicsLayer to avoid layout and recomposition overhead.
 */
@Composable
fun PulsingStreakFlameIcon(
    isStreakActive: Boolean,
    modifier: Modifier = Modifier,
    size: Dp = 24.dp,
    tint: Color = FlameOrange
) {
    val scale = remember { Animatable(1f) }
    var hasPulsed by remember { mutableStateOf(false) }

    LaunchedEffect(isStreakActive) {
        if (isStreakActive && !hasPulsed) {
            hasPulsed = true
            repeat(2) {
                scale.animateTo(
                    targetValue = 1.18f,
                    animationSpec = tween(durationMillis = 350, easing = FastOutSlowInEasing)
                )
                scale.animateTo(
                    targetValue = 1f,
                    animationSpec = tween(durationMillis = 350, easing = FastOutSlowInEasing)
                )
            }
        } else if (!isStreakActive) {
            scale.snapTo(1f)
        }
    }

    Icon(
        imageVector = Icons.Default.LocalFireDepartment,
        contentDescription = "Streak",
        tint = tint,
        modifier = modifier
            .size(size)
            .graphicsLayer {
                scaleX = scale.value
                scaleY = scale.value
            }
    )
}

@Composable
fun StreakFlameGrid(
    currentStreak: Int,
    longestStreak: Int,
    modifier: Modifier = Modifier
) {
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
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(
                        modifier = Modifier
                            .size(36.dp)
                            .clip(CircleShape)
                            .background(FlameOrange.copy(alpha = 0.15f)),
                        contentAlignment = Alignment.Center
                    ) {
                        PulsingStreakFlameIcon(
                            isStreakActive = currentStreak > 0,
                            size = 24.dp,
                            tint = FlameOrange
                        )
                    }
                    Spacer(modifier = Modifier.width(10.dp))
                    Column {
                        Text(
                            text = stringResource(R.string.comp_streak_days_format, currentStreak),
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                        Text(
                            text = stringResource(R.string.comp_streak_best_format, longestStreak),
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(14.dp))

            // 7-day grid (Sen - Min) mapped to actual current calendar week
            val days = listOf("Sen", "Sel", "Rab", "Kam", "Jum", "Sab", "Min")
            val todayCalDay = java.util.Calendar.getInstance().get(java.util.Calendar.DAY_OF_WEEK)
            val todayMonIndex = when (todayCalDay) {
                java.util.Calendar.MONDAY -> 0
                java.util.Calendar.TUESDAY -> 1
                java.util.Calendar.WEDNESDAY -> 2
                java.util.Calendar.THURSDAY -> 3
                java.util.Calendar.FRIDAY -> 4
                java.util.Calendar.SATURDAY -> 5
                else -> 6
            }
            val activeCount = currentStreak.coerceIn(0, 7)
            val activeIndices = (0 until activeCount).map { offset ->
                ((todayMonIndex - offset) + 7) % 7
            }.toSet()

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                days.forEachIndexed { index, day ->
                    val isActive = index in activeIndices
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Box(
                            modifier = Modifier
                                .size(38.dp)
                                .clip(RoundedCornerShape(10.dp))
                                .background(
                                    if (isActive) FlameOrange else MaterialTheme.colorScheme.surfaceVariant
                                ),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.Default.LocalFireDepartment,
                                contentDescription = null,
                                tint = if (isActive) Color.White else MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.4f),
                                modifier = Modifier.size(20.dp)
                            )
                        }
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = day,
                            style = MaterialTheme.typography.labelSmall,
                            color = if (isActive) FlameOrange else MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun DailyQuestCard(
    title: String,
    description: String,
    rewardXp: Int,
    progress: Int,
    target: Int,
    isCompleted: Boolean,
    onClaimOrDo: () -> Unit,
    modifier: Modifier = Modifier
) {
    // Single-shot scale bounce animation when quest is marked complete during session
    val checkmarkScale = remember { Animatable(1f) }
    var initialRender by remember { mutableStateOf(true) }

    LaunchedEffect(isCompleted) {
        if (initialRender) {
            initialRender = false
            return@LaunchedEffect
        }
        if (isCompleted) {
            checkmarkScale.snapTo(0.25f)
            checkmarkScale.animateTo(
                targetValue = 1f,
                animationSpec = spring(
                    dampingRatio = Spring.DampingRatioMediumBouncy,
                    stiffness = Spring.StiffnessMedium
                )
            )
        } else {
            checkmarkScale.snapTo(1f)
        }
    }

    Card(
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = if (isCompleted) TealPrimary.copy(alpha = 0.12f) else MaterialTheme.colorScheme.surface
        ),
        border = BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.5f)),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(44.dp)
                    .clip(CircleShape)
                    .background(if (isCompleted) SuccessGreen.copy(alpha = 0.15f) else AmberGold.copy(alpha = 0.15f)),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = if (isCompleted) Icons.Default.CheckCircle else Icons.Default.Star,
                    contentDescription = null,
                    tint = if (isCompleted) SuccessGreen else AmberGold,
                    modifier = Modifier
                        .size(26.dp)
                        .graphicsLayer {
                            if (isCompleted) {
                                scaleX = checkmarkScale.value
                                scaleY = checkmarkScale.value
                            }
                        }
                )
            }

            Spacer(modifier = Modifier.width(12.dp))

            Column(modifier = Modifier.weight(1f)) {
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = stringResource(R.string.comp_daily_quest_label),
                        style = MaterialTheme.typography.labelMedium,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.primary
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Surface(
                        color = AmberGold.copy(alpha = 0.15f),
                        shape = RoundedCornerShape(6.dp)
                    ) {
                        Text(
                            text = "+$rewardXp XP",
                            color = AmberGold,
                            fontWeight = FontWeight.Bold,
                            fontSize = 11.sp,
                            modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                        )
                    }
                }

                Text(
                    text = title,
                    style = MaterialTheme.typography.bodyMedium,
                    fontWeight = FontWeight.SemiBold,
                    color = MaterialTheme.colorScheme.onSurface,
                    maxLines = 1,
                    overflow = androidx.compose.ui.text.style.TextOverflow.Ellipsis
                )
                Text(
                    text = description,
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    maxLines = 2,
                    overflow = androidx.compose.ui.text.style.TextOverflow.Ellipsis
                )

                Spacer(modifier = Modifier.height(6.dp))

                val progFraction = (progress.toFloat() / target.toFloat()).coerceIn(0f, 1f)
                LinearProgressIndicator(
                    progress = { if (isCompleted) 1f else progFraction },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(6.dp)
                        .clip(RoundedCornerShape(3.dp)),
                    color = if (isCompleted) SuccessGreen else MaterialTheme.colorScheme.primary,
                    trackColor = MaterialTheme.colorScheme.surfaceVariant
                )
            }

            Spacer(modifier = Modifier.width(12.dp))

            Surface(
                shape = RoundedCornerShape(10.dp),
                color = if (isCompleted) SuccessGreen else TealPrimary,
                modifier = Modifier.clickable { onClaimOrDo() }
            ) {
                Row(
                    modifier = Modifier.padding(horizontal = 12.dp, vertical = 8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = if (isCompleted) stringResource(R.string.comp_completed_badge) else stringResource(R.string.comp_daily_quest_do),
                        color = Color.White,
                        fontWeight = FontWeight.Bold,
                        fontSize = 12.sp
                    )
                    if (isCompleted) {
                        Spacer(modifier = Modifier.width(4.dp))
                        Icon(
                            imageVector = Icons.Default.Check,
                            contentDescription = null,
                            tint = Color.White,
                            modifier = Modifier.size(16.dp)
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun InfoCarousel(
    streakDays: Int,
    weeklyStudyMinutes: Int = 85,
    weeklyXp: Int = 180,
    modifier: Modifier = Modifier
) {
    var activeSlide by remember { mutableIntStateOf(0) }

    LaunchedEffect(Unit) {
        while (true) {
            delay(4000)
            activeSlide = (activeSlide + 1) % 3
        }
    }

    val slides = listOf(
        Triple(stringResource(R.string.comp_carousel_streak_title), stringResource(R.string.comp_streak_days_format, streakDays), Icons.Default.LocalFireDepartment to FlameOrange),
        Triple(stringResource(R.string.comp_carousel_study_title), stringResource(R.string.comp_carousel_study_val_format, weeklyStudyMinutes), Icons.Default.EmojiEvents to CyanAccent),
        Triple(stringResource(R.string.comp_carousel_xp_title), "+$weeklyXp XP", Icons.Default.Star to AmberGold)
    )

    Card(
        modifier = modifier
            .fillMaxWidth()
            .clickable { activeSlide = (activeSlide + 1) % 3 },
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.7f)
        ),
        border = BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.5f)),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            val current = slides[activeSlide]
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Column {
                    Text(
                        text = current.first,
                        style = MaterialTheme.typography.labelSmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    Text(
                        text = current.second,
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        color = current.third.second
                    )
                }

                Box(
                    modifier = Modifier
                        .size(40.dp)
                        .clip(CircleShape)
                        .background(current.third.second.copy(alpha = 0.15f)),
                    contentAlignment = Alignment.Center
                ) {
                    if (activeSlide == 0 && streakDays > 0) {
                        PulsingStreakFlameIcon(
                            isStreakActive = true,
                            size = 24.dp,
                            tint = current.third.second
                        )
                    } else {
                        Icon(
                            imageVector = current.third.first,
                            contentDescription = null,
                            tint = current.third.second
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            // Dot indicators
            Row(
                horizontalArrangement = Arrangement.Center,
                modifier = Modifier.fillMaxWidth()
            ) {
                repeat(3) { idx ->
                    Box(
                        modifier = Modifier
                            .padding(horizontal = 3.dp)
                            .width(if (idx == activeSlide) 16.dp else 6.dp)
                            .height(6.dp)
                            .clip(CircleShape)
                            .background(
                                if (idx == activeSlide) TealLight else MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.3f)
                            )
                    )
                }
            }
        }
    }
}

@Composable
fun CodeBlockView(
    code: String,
    language: String = "html",
    modifier: Modifier = Modifier,
    onTryInPlayground: (String) -> Unit = {}
) {
    val context = LocalContext.current

    Surface(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(12.dp))
            .border(1.dp, CodeDarkBorder, RoundedCornerShape(12.dp)),
        color = CodeDarkBg
    ) {
        Column {
            // Header bar with language tag & Action buttons (Playground & Copy)
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(CodeDarkSurface)
                    .padding(horizontal = 14.dp, vertical = 8.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = language.uppercase(),
                    color = MaterialTheme.colorScheme.primary,
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Bold,
                    fontFamily = FontFamily.Monospace
                )

                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    // Tombol "Coba di Playground"
                    Row(
                        modifier = Modifier
                            .clip(RoundedCornerShape(6.dp))
                            .clickable { onTryInPlayground(code) }
                            .padding(horizontal = 8.dp, vertical = 4.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = Icons.Default.PlayArrow,
                            contentDescription = stringResource(R.string.comp_try_in_playground),
                            tint = MaterialTheme.colorScheme.primary,
                            modifier = Modifier.size(14.dp)
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            text = stringResource(R.string.comp_try_in_playground),
                            color = MaterialTheme.colorScheme.primary,
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Medium
                        )
                    }

                    // Tombol "Salin"
                    Row(
                        modifier = Modifier
                            .clip(RoundedCornerShape(6.dp))
                            .clickable {
                                val clipboard = context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
                                val clip = ClipData.newPlainText("code", code)
                                clipboard.setPrimaryClip(clip)
                                Toast.makeText(context, context.getString(R.string.comp_code_copied_toast), Toast.LENGTH_SHORT).show()
                            }
                            .padding(horizontal = 8.dp, vertical = 4.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = Icons.Default.ContentCopy,
                            contentDescription = stringResource(R.string.comp_copy),
                            tint = Color.White,
                            modifier = Modifier.size(14.dp)
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            text = stringResource(R.string.comp_copy),
                            color = Color.White,
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Medium
                        )
                    }
                }
            }

            // Code content
            Text(
                text = remember(code, language) { highlightCode(code, language) },
                color = Color(0xFFE2E8F0),
                fontSize = 13.sp,
                fontFamily = FontFamily.Monospace,
                lineHeight = 20.sp,
                modifier = Modifier.padding(14.dp)
            )
        }
    }
}

@Composable
fun MultiLanguageCodeBlockView(
    snippets: Map<String, String>,
    modifier: Modifier = Modifier,
    onTryInPlayground: (Map<String, String>) -> Unit = {}
) {
    val context = LocalContext.current
    val languageOrder = listOf("html", "css", "js")
    val availableLanguages = remember(snippets) {
        languageOrder.filter { snippets.containsKey(it) }
    }
    var selectedIndex by remember(snippets) { mutableStateOf(0) }
    val selectedLanguage = availableLanguages.getOrElse(selectedIndex) { availableLanguages.first() }
    val selectedCode = snippets[selectedLanguage].orEmpty()

    Surface(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(12.dp))
            .border(1.dp, CodeDarkBorder, RoundedCornerShape(12.dp)),
        color = CodeDarkBg
    ) {
        Column {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(CodeDarkSurface)
                    .padding(horizontal = 10.dp, vertical = 6.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                    availableLanguages.forEachIndexed { index, lang ->
                        val isSelected = index == selectedIndex
                        Surface(
                            modifier = Modifier.clickable { selectedIndex = index },
                            shape = RoundedCornerShape(6.dp),
                            color = if (isSelected) MaterialTheme.colorScheme.primary.copy(alpha = 0.2f) else Color.Transparent
                        ) {
                            Text(
                                text = lang.uppercase(),
                                color = if (isSelected) MaterialTheme.colorScheme.primary else Color(0xFF8B949E),
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Bold,
                                fontFamily = FontFamily.Monospace,
                                modifier = Modifier.padding(horizontal = 10.dp, vertical = 5.dp)
                            )
                        }
                    }
                }

                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Row(
                        modifier = Modifier
                            .clip(RoundedCornerShape(6.dp))
                            .clickable { onTryInPlayground(snippets) }
                            .padding(horizontal = 8.dp, vertical = 4.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = Icons.Default.PlayArrow,
                            contentDescription = stringResource(R.string.comp_try_in_playground),
                            tint = MaterialTheme.colorScheme.primary,
                            modifier = Modifier.size(14.dp)
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            text = stringResource(R.string.comp_try_in_playground),
                            color = MaterialTheme.colorScheme.primary,
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Medium
                        )
                    }

                    Row(
                        modifier = Modifier
                            .clip(RoundedCornerShape(6.dp))
                            .clickable {
                                val clipboard = context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
                                val clip = ClipData.newPlainText("code", selectedCode)
                                clipboard.setPrimaryClip(clip)
                                Toast.makeText(context, context.getString(R.string.comp_lang_code_copied_toast_format, selectedLanguage), Toast.LENGTH_SHORT).show()
                            }
                            .padding(horizontal = 8.dp, vertical = 4.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = Icons.Default.ContentCopy,
                            contentDescription = stringResource(R.string.comp_copy),
                            tint = Color.White,
                            modifier = Modifier.size(14.dp)
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            text = stringResource(R.string.comp_copy),
                            color = Color.White,
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Medium
                        )
                    }
                }
            }

            Text(
                text = remember(selectedCode, selectedLanguage) { highlightCode(selectedCode, selectedLanguage) },
                color = Color(0xFFE2E8F0),
                fontSize = 13.sp,
                fontFamily = FontFamily.Monospace,
                lineHeight = 20.sp,
                modifier = Modifier.padding(14.dp)
            )
        }
    }
}

@Composable
fun CodeSandboxWebView(
    html: String,
    css: String,
    js: String,
    onConsoleLog: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    val fullHtmlDoc = remember(html, css, js) {
        """
        <!DOCTYPE html>
        <html>
        <head>
          <meta charset="utf-8">
          <meta name="viewport" content="width=device-width, initial-scale=1">
          <style>
            ${css.ifBlank { "/* Empty CSS */" }}
          </style>
          <script>
            // Intercept console.log to communicate back
            const _origLog = console.log;
            console.log = function(...args) {
              _origLog.apply(console, args);
            };
          </script>
        </head>
        <body>
          ${html.ifBlank { "<!-- Empty HTML -->" }}
          <script>
            try {
              $js
            } catch(e) {
              console.log("Error: " + e.message);
            }
          </script>
        </body>
        </html>
        """.trimIndent()
    }

    val mainHandler = remember { android.os.Handler(android.os.Looper.getMainLooper()) }

    AndroidView(
        factory = { ctx ->
            WebView(ctx).apply {
                try {
                    settings.javaScriptEnabled = true
                    settings.domStorageEnabled = true
                    settings.allowFileAccess = false
                    settings.allowContentAccess = false
                    webViewClient = object : android.webkit.WebViewClient() {
                        override fun shouldOverrideUrlLoading(
                            view: WebView?,
                            request: android.webkit.WebResourceRequest?
                        ): Boolean {
                            val url = request?.url?.toString().orEmpty()
                            // Keep sandbox isolated: block navigating away to external websites
                            return !url.startsWith("https://localhost/") && !url.startsWith("about:blank")
                        }
                    }
                    webChromeClient = object : WebChromeClient() {
                        override fun onConsoleMessage(consoleMessage: ConsoleMessage?): Boolean {
                            val msg = consoleMessage?.message()
                            if (!msg.isNullOrBlank()) {
                                mainHandler.post {
                                    try {
                                        onConsoleLog(msg)
                                    } catch (_: Throwable) {
                                        // Ignore UI update error if screen is disposed
                                    }
                                }
                            }
                            return true
                        }
                    }
                } catch (_: Throwable) {
                    // Fallback for WebView initialization error
                }
            }
        },
        update = { webView ->
            try {
                webView.loadDataWithBaseURL("https://localhost/", fullHtmlDoc, "text/html", "UTF-8", null)
            } catch (_: Throwable) {
                // Fallback for WebView load error
            }
        },
        onReset = { webView ->
            try {
                webView.stopLoading()
            } catch (_: Throwable) {}
        },
        onRelease = { webView ->
            try {
                webView.stopLoading()
                webView.webChromeClient = null
                webView.destroy()
            } catch (_: Throwable) {}
        },
        modifier = modifier
    )
}

/**
 * WCAG 2.1 AA Compliant Level Badge
 * Pemula: solid #134E4A, text #5EEAD4
 * Menengah: solid #1E3A5F, text #93C5FD
 * Lanjut: solid #4C1D24, text #FCA5A5
 */
@Composable
fun CourseLevelBadge(
    level: String,
    modifier: Modifier = Modifier,
    fontSize: androidx.compose.ui.unit.TextUnit = 11.sp
) {
    val (bgColor, textColor) = when (level.trim().lowercase()) {
        "pemula" -> com.example.ui.theme.ChipPemulaBg to com.example.ui.theme.ChipPemulaText
        "menengah" -> com.example.ui.theme.ChipMenengahBg to com.example.ui.theme.ChipMenengahText
        "lanjut" -> com.example.ui.theme.ChipLanjutBg to com.example.ui.theme.ChipLanjutText
        else -> com.example.ui.theme.ChipNeutralBg to com.example.ui.theme.ChipNeutralText
    }

    Surface(
        color = bgColor,
        shape = RoundedCornerShape(6.dp),
        modifier = modifier
    ) {
        Text(
            text = com.example.util.localization.DataLabelMappings.courseLevelLabel(level),
            color = textColor,
            fontSize = fontSize,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(horizontal = 8.dp, vertical = 3.dp)
        )
    }
}

/**
 * WCAG 2.1 AA Compliant Neutral Info Badge (e.g. "1/3 Modul", "0/3 Kelas")
 * Background: solid #1E293B, Text: #E2E8F0
 */
@Composable
fun NeutralInfoBadge(
    text: String,
    modifier: Modifier = Modifier,
    fontSize: androidx.compose.ui.unit.TextUnit = 11.sp
) {
    Surface(
        color = com.example.ui.theme.ChipNeutralBg,
        shape = RoundedCornerShape(6.dp),
        modifier = modifier
    ) {
        Text(
            text = text,
            color = com.example.ui.theme.ChipNeutralText,
            fontSize = fontSize,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(horizontal = 8.dp, vertical = 3.dp)
        )
    }
}

/**
 * Standardized Primary Button for main screen actions
 */
@Composable
fun PrimaryButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    loading: Boolean = false,
    icon: ImageVector? = null,
    containerColor: Color = MaterialTheme.colorScheme.primary,
    contentColor: Color = MaterialTheme.colorScheme.onPrimary
) {
    Button(
        onClick = onClick,
        enabled = enabled && !loading,
        shape = AppButtonShape,
        colors = ButtonDefaults.buttonColors(
            containerColor = containerColor,
            contentColor = contentColor,
            disabledContainerColor = containerColor.copy(alpha = 0.5f),
            disabledContentColor = contentColor.copy(alpha = 0.6f)
        ),
        elevation = ButtonDefaults.buttonElevation(defaultElevation = 2.dp),
        modifier = modifier.height(AppButtonHeight)
    ) {
        if (loading) {
            CircularProgressIndicator(
                modifier = Modifier.size(22.dp),
                color = contentColor,
                strokeWidth = 2.5.dp
            )
        } else {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center
            ) {
                if (icon != null) {
                    Icon(
                        imageVector = icon,
                        contentDescription = null,
                        modifier = Modifier.size(20.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                }
                Text(
                    text = text,
                    style = MaterialTheme.typography.labelLarge,
                    fontWeight = FontWeight.Bold,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
            }
        }
    }
}

/**
 * Standardized Secondary / Outlined Button for secondary screen actions
 */
@Composable
fun SecondaryButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    loading: Boolean = false,
    icon: ImageVector? = null,
    borderColor: Color = MaterialTheme.colorScheme.primary,
    contentColor: Color = MaterialTheme.colorScheme.primary
) {
    OutlinedButton(
        onClick = onClick,
        enabled = enabled && !loading,
        shape = AppButtonShape,
        border = androidx.compose.foundation.BorderStroke(1.dp, if (enabled) borderColor.copy(alpha = 0.7f) else borderColor.copy(alpha = 0.3f)),
        colors = ButtonDefaults.outlinedButtonColors(
            contentColor = contentColor,
            disabledContentColor = contentColor.copy(alpha = 0.4f)
        ),
        modifier = modifier.height(AppButtonHeight)
    ) {
        if (loading) {
            CircularProgressIndicator(
                modifier = Modifier.size(22.dp),
                color = contentColor,
                strokeWidth = 2.5.dp
            )
        } else {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center
            ) {
                if (icon != null) {
                    Icon(
                        imageVector = icon,
                        contentDescription = null,
                        modifier = Modifier.size(20.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                }
                Text(
                    text = text,
                    style = MaterialTheme.typography.labelLarge,
                    fontWeight = FontWeight.Bold,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
            }
        }
    }
}

/**
 * Standardized Success Button (e.g. for module or quiz completion)
 */
@Composable
fun SuccessButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    loading: Boolean = false,
    icon: ImageVector? = null
) {
    PrimaryButton(
        text = text,
        onClick = onClick,
        modifier = modifier,
        enabled = enabled,
        loading = loading,
        icon = icon,
        containerColor = SuccessGreen,
        contentColor = Color.White
    )
}

/**
 * Standardized, polished dialog container adhering to CodeAcademy's card and button design language.
 * Replaces default system AlertDialog with a cohesive Dimens.DialogDefaultShape rounded container, subtle outline border,
 * theme-adaptive surface styling, optional badge icon, and standardized action buttons.
 */
@Composable
fun AppDialog(
    onDismissRequest: () -> Unit,
    modifier: Modifier = Modifier,
    shape: Shape = Dimens.DialogDefaultShape,
    icon: (@Composable () -> Unit)? = null,
    title: (@Composable () -> Unit)? = null,
    text: (@Composable () -> Unit)? = null,
    confirmButton: (@Composable () -> Unit)? = null,
    dismissButton: (@Composable () -> Unit)? = null,
    properties: DialogProperties = DialogProperties()
) {
    Dialog(
        onDismissRequest = onDismissRequest,
        properties = properties
    ) {
        Card(
            modifier = modifier
                .fillMaxWidth()
                .padding(horizontal = 8.dp),
            shape = shape,
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.surface
            ),
            border = BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.5f)),
            elevation = CardDefaults.cardElevation(defaultElevation = 6.dp)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(24.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                if (icon != null) {
                    Box(
                        modifier = Modifier.padding(bottom = 16.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        icon()
                    }
                }

                if (title != null) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(bottom = 12.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        title()
                    }
                }

                if (text != null) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(bottom = 20.dp)
                    ) {
                        text()
                    }
                }

                if (confirmButton != null || dismissButton != null) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(10.dp, Alignment.End),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        if (dismissButton != null) {
                            Box(modifier = Modifier.weight(1f, fill = false)) {
                                dismissButton()
                            }
                        }
                        if (confirmButton != null) {
                            Box(modifier = Modifier.weight(1f, fill = false)) {
                                confirmButton()
                            }
                        }
                    }
                }
            }
        }
    }
}

/**
 * Convenience dialog wrapper for standard confirmation flows (e.g. quit, reset, discard, logout).
 */
@Composable
fun AppConfirmationDialog(
    onDismissRequest: () -> Unit,
    title: String,
    message: String,
    confirmText: String,
    onConfirm: () -> Unit,
    modifier: Modifier = Modifier,
    dismissText: String? = null,
    icon: (@Composable () -> Unit)? = null,
    confirmColor: Color = TealPrimary,
    confirmTextColor: Color = Color.White,
    dismissColor: Color = MaterialTheme.colorScheme.onSurfaceVariant,
    isDestructive: Boolean = false,
    confirmTestTag: String? = null,
    dismissTestTag: String? = null
) {
    val actualConfirmColor = if (isDestructive) ErrorRed else confirmColor
    val resolvedDismissText = dismissText ?: stringResource(R.string.comp_cancel)

    AppDialog(
        onDismissRequest = onDismissRequest,
        modifier = modifier,
        icon = icon,
        title = {
            Text(
                text = title,
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onSurface
            )
        },
        text = {
            Text(
                text = message,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                lineHeight = 20.sp
            )
        },
        confirmButton = {
            Button(
                onClick = onConfirm,
                shape = Dimens.AppButtonShape,
                colors = ButtonDefaults.buttonColors(
                    containerColor = actualConfirmColor,
                    contentColor = confirmTextColor
                ),
                modifier = if (confirmTestTag != null) Modifier.testTag(confirmTestTag) else Modifier
            ) {
                Text(
                    text = confirmText,
                    fontWeight = FontWeight.Bold,
                    fontSize = 13.sp
                )
            }
        },
        dismissButton = {
            OutlinedButton(
                onClick = onDismissRequest,
                shape = Dimens.AppButtonShape,
                border = BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant),
                colors = ButtonDefaults.outlinedButtonColors(
                    contentColor = dismissColor
                ),
                modifier = if (dismissTestTag != null) Modifier.testTag(dismissTestTag) else Modifier
            ) {
                Text(
                    text = resolvedDismissText,
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 13.sp
                )
            }
        }
    )
}



