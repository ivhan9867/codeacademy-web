package com.example.ui.components

import android.provider.Settings
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.Logout
import androidx.compose.material.icons.filled.Bookmark
import androidx.compose.material.icons.filled.Code
import androidx.compose.material.icons.filled.HelpOutline
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Psychology
import androidx.compose.material.icons.filled.School
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.Whatshot
import androidx.compose.material.icons.filled.WorkspacePremium
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Badge
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.DividerDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.NavigationDrawerItem
import androidx.compose.material3.NavigationDrawerItemDefaults
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawWithCache
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.R
import com.example.data.local.UserProfileEntity
import com.example.ui.theme.AmberGold
import com.example.ui.theme.ErrorRed
import com.example.ui.theme.FlameOrange
import kotlin.math.PI
import kotlin.math.sin
import kotlin.math.sqrt

/**
 * Navigation Drawer Content for CodeAcademy following Dicoding drawer concept:
 * 1. Brand bar (logo + "CodeAcademy")
 * 2. Time-based sky scenery banner (Canvas gradient, celestial rise, clouds, stars) with circular close button
 * 3. Compact navigation menu list with active page pill markers
 */
@Composable
fun AppNavigationDrawer(
    userProfile: UserProfileEntity?,
    unreadNotifsCount: Int,
    onCloseDrawer: () -> Unit,
    onNavigate: (String) -> Unit,
    onLogout: () -> Unit,
    modifier: Modifier = Modifier,
    currentRoute: String? = "home",
    isOpen: Boolean = false
) {
    var showLogoutDialog by remember { mutableStateOf(false) }

    // Current hour of day (0-23)
    val currentHour = remember {
        try {
            java.time.LocalTime.now().hour
        } catch (_: Throwable) {
            java.util.Calendar.getInstance().get(java.util.Calendar.HOUR_OF_DAY)
        }
    }

    // Sky Palette according to specifications:
    // Pagi: 05-10 -> #F6C98B to #BFE3E0, low sun on left
    // Siang: 11-14 -> #7EC8E3 to #DFF3F0, high sun
    // Sore: 15-17 -> #F2A65A to #C0688A, large low sun on right
    // Malam: 18-04 -> #1E2350 to #4B3A8C, moon and twinkling stars
    val timePeriod = remember(currentHour) {
        when (currentHour) {
            in 5..10 -> SkyPeriod.PAGI
            in 11..14 -> SkyPeriod.SIANG
            in 15..17 -> SkyPeriod.SORE
            else -> SkyPeriod.MALAM
        }
    }

    val context = LocalContext.current
    val greetingText = remember(timePeriod, userProfile?.name, context) {
        val name = userProfile?.name?.takeIf { it.isNotBlank() } ?: context.getString(com.example.R.string.home_default_user)
        when (timePeriod) {
            SkyPeriod.PAGI -> context.getString(com.example.R.string.drawer_greeting_pagi, name)
            SkyPeriod.SIANG -> context.getString(com.example.R.string.drawer_greeting_siang, name)
            SkyPeriod.SORE -> context.getString(com.example.R.string.drawer_greeting_sore, name)
            SkyPeriod.MALAM -> context.getString(com.example.R.string.drawer_greeting_malam, name)
        }
    }

    // Check system animation settings (respect 0 scale -> static display)
    val animationsEnabled = remember(context) {
        try {
            Settings.Global.getFloat(
                context.contentResolver,
                Settings.Global.ANIMATOR_DURATION_SCALE,
                1.0f
            ) > 0f
        } catch (_: Throwable) {
            true
        }
    }

    // Sky Palette 3 stops per period with smooth animateColorAsState transition
    val (targetColorTop, targetColorMid, targetColorBottom) = when (timePeriod) {
        SkyPeriod.PAGI -> Triple(Color(0xFFE27B58), Color(0xFFF6C98B), Color(0xFFBFE3E0))
        SkyPeriod.SIANG -> Triple(Color(0xFF2B79D9), Color(0xFF68BCE5), Color(0xFFDFF3F0))
        SkyPeriod.SORE -> Triple(Color(0xFF53245C), Color(0xFFC0688A), Color(0xFFF2A65A))
        SkyPeriod.MALAM -> Triple(Color(0xFF0D122B), Color(0xFF1E2350), Color(0xFF3F3074))
    }

    val animatedColorTop by animateColorAsState(
        targetValue = targetColorTop,
        animationSpec = tween(durationMillis = 800),
        label = "sky_color_top"
    )
    val animatedColorMid by animateColorAsState(
        targetValue = targetColorMid,
        animationSpec = tween(durationMillis = 800),
        label = "sky_color_mid"
    )
    val animatedColorBottom by animateColorAsState(
        targetValue = targetColorBottom,
        animationSpec = tween(durationMillis = 800),
        label = "sky_color_bottom"
    )

    // Celestial rise animation (one-shot ~600ms with easing when drawer opens)
    val celestialRiseState = if (animationsEnabled) {
        animateFloatAsState(
            targetValue = if (isOpen) 1f else 0f,
            animationSpec = tween(durationMillis = 600, easing = FastOutSlowInEasing),
            label = "celestial_rise"
        )
    } else {
        remember(isOpen) { mutableFloatStateOf(if (isOpen) 1f else 0f) }
    }

    // Unified infinite transition: ONLY active when drawer is open and animations are enabled
    val isRunningInfinite = isOpen && animationsEnabled
    val infiniteTransition = rememberInfiniteTransition(label = "sky_scenery_infinite")
    val phaseState = if (isRunningInfinite) {
        infiniteTransition.animateFloat(
            initialValue = 0f,
            targetValue = 1f,
            animationSpec = infiniteRepeatable(
                animation = tween(durationMillis = 8000, easing = LinearEasing),
                repeatMode = RepeatMode.Restart
            ),
            label = "scenery_phase"
        )
    } else {
        remember { mutableFloatStateOf(0f) }
    }

    ModalDrawerSheet(
        modifier = modifier.width(310.dp),
        drawerContainerColor = MaterialTheme.colorScheme.surface,
        drawerTonalElevation = 2.dp
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
        ) {
            // ================= 1. BRAND BAR =================
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .statusBarsPadding()
                    .padding(horizontal = 20.dp, vertical = 14.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Image(
                    painter = painterResource(id = R.drawable.ic_codeacademy_logo),
                    contentDescription = null,
                    modifier = Modifier
                        .size(28.dp)
                        .clip(RoundedCornerShape(6.dp))
                )
                Spacer(modifier = Modifier.width(10.dp))
                Text(
                    text = "CodeAcademy",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSurface
                )
            }

            // ================= 2. SKY SCENERY BANNER (~150dp) =================
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(150.dp)
            ) {
                // Vector Canvas Scenery Background using Modifier.drawWithCache
                Spacer(
                    modifier = Modifier
                        .fillMaxSize()
                        .drawWithCache {
                            val w = size.width
                            val h = size.height

                            val skyBrush = Brush.verticalGradient(
                                colors = listOf(animatedColorTop, animatedColorMid, animatedColorBottom),
                                startY = 0f,
                                endY = h
                            )

                            val backHillPath = Path().apply {
                                moveTo(0f, h * 0.74f)
                                cubicTo(w * 0.25f, h * 0.66f, w * 0.50f, h * 0.72f, w * 0.72f, h * 0.67f)
                                cubicTo(w * 0.86f, h * 0.64f, w * 0.94f, h * 0.68f, w, h * 0.70f)
                                lineTo(w, h)
                                lineTo(0f, h)
                                close()
                            }

                            val frontHillPath = Path().apply {
                                moveTo(0f, h * 0.83f)
                                cubicTo(w * 0.22f, h * 0.78f, w * 0.44f, h * 0.86f, w * 0.66f, h * 0.80f)
                                cubicTo(w * 0.82f, h * 0.76f, w * 0.92f, h * 0.82f, w, h * 0.79f)
                                lineTo(w, h)
                                lineTo(0f, h)
                                close()
                            }

                            onDrawBehind {
                                drawSkyScenery(
                                    timePeriod = timePeriod,
                                    skyBrush = skyBrush,
                                    backHillPath = backHillPath,
                                    frontHillPath = frontHillPath,
                                    celestialProgress = celestialRiseState.value,
                                    phase = phaseState.value
                                )
                            }
                        }
                )

                // Subtle bottom scrim gradient for sharp text contrast
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(
                            Brush.verticalGradient(
                                colors = listOf(
                                    Color.Transparent,
                                    Color.Black.copy(alpha = 0.20f),
                                    Color.Black.copy(alpha = 0.58f)
                                ),
                                startY = 50f,
                                endY = Float.POSITIVE_INFINITY
                            )
                        )
                )

                // Top-Right Circular Close Button
                Box(
                    modifier = Modifier
                        .align(Alignment.TopEnd)
                        .padding(top = 10.dp, end = 12.dp)
                ) {
                    IconButton(
                        onClick = onCloseDrawer,
                        modifier = Modifier
                            .size(34.dp)
                            .clip(CircleShape)
                            .background(Color.Black.copy(alpha = 0.28f))
                            .testTag("drawer_close_button")
                    ) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = androidx.compose.ui.res.stringResource(com.example.R.string.drawer_close_cd),
                            tint = Color.White,
                            modifier = Modifier.size(18.dp)
                        )
                    }
                }

                // Bottom-Left Greeting & Level/XP Info
                Column(
                    modifier = Modifier
                        .align(Alignment.BottomStart)
                        .padding(horizontal = 16.dp, vertical = 12.dp)
                ) {
                    Text(
                        text = greetingText,
                        style = MaterialTheme.typography.bodyMedium,
                        fontWeight = FontWeight.Bold,
                        color = Color.White,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )

                    Spacer(modifier = Modifier.height(2.dp))

                    Text(
                        text = androidx.compose.ui.res.stringResource(
                            com.example.R.string.drawer_user_level_xp,
                            userProfile?.level ?: 1,
                            userProfile?.totalXp ?: 0
                        ),
                        style = MaterialTheme.typography.labelSmall,
                        fontWeight = FontWeight.Medium,
                        color = Color.White.copy(alpha = 0.90f)
                    )
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            // ================= 3. NAVIGATION MENU ITEMS =================
            // Top Primary Group: Beranda, Cari Materi, Progres Belajar
            DrawerAnimatedMenuItem(
                index = 0,
                isOpen = isOpen,
                icon = Icons.Default.Home,
                label = androidx.compose.ui.res.stringResource(com.example.R.string.drawer_menu_home),
                isSelected = currentRoute == "home",
                testTag = "drawer_item_home",
                onClick = {
                    onCloseDrawer()
                    onNavigate("home")
                }
            )

            DrawerAnimatedMenuItem(
                index = 1,
                isOpen = isOpen,
                icon = Icons.Default.Search,
                label = androidx.compose.ui.res.stringResource(com.example.R.string.drawer_menu_search),
                isSelected = currentRoute == "search",
                testTag = "drawer_item_search",
                onClick = {
                    onCloseDrawer()
                    onNavigate("search")
                }
            )

            DrawerAnimatedMenuItem(
                index = 2,
                isOpen = isOpen,
                icon = Icons.Default.School,
                label = androidx.compose.ui.res.stringResource(com.example.R.string.drawer_menu_progress),
                isSelected = currentRoute == "progress",
                testTag = "drawer_item_progress",
                onClick = {
                    onCloseDrawer()
                    onNavigate("progress")
                }
            )

            HorizontalDivider(
                modifier = Modifier.padding(horizontal = 16.dp, vertical = 6.dp),
                color = DividerDefaults.color.copy(alpha = 0.4f)
            )

            // Section: Aktivitas
            DrawerSectionHeader(title = androidx.compose.ui.res.stringResource(com.example.R.string.drawer_section_activity))

            DrawerAnimatedMenuItem(
                index = 2,
                isOpen = isOpen,
                icon = Icons.Default.Notifications,
                label = androidx.compose.ui.res.stringResource(com.example.R.string.drawer_menu_notifications),
                badgeCount = unreadNotifsCount,
                isSelected = currentRoute == "notifications",
                testTag = "drawer_item_notifications",
                onClick = {
                    onCloseDrawer()
                    onNavigate("notifications")
                }
            )

            DrawerAnimatedMenuItem(
                index = 3,
                isOpen = isOpen,
                icon = Icons.Default.WorkspacePremium,
                label = androidx.compose.ui.res.stringResource(com.example.R.string.drawer_menu_certificates),
                isSelected = currentRoute == "certificateList",
                testTag = "drawer_item_certificates",
                onClick = {
                    onCloseDrawer()
                    onNavigate("certificateList")
                }
            )

            DrawerAnimatedMenuItem(
                index = 4,
                isOpen = isOpen,
                icon = Icons.Default.Bookmark,
                label = androidx.compose.ui.res.stringResource(com.example.R.string.drawer_menu_bookmarks),
                isSelected = currentRoute == "bookmarks",
                testTag = "drawer_item_bookmarks",
                onClick = {
                    onCloseDrawer()
                    onNavigate("bookmarks")
                }
            )

            DrawerAnimatedMenuItem(
                index = 5,
                isOpen = isOpen,
                icon = Icons.Default.Whatshot,
                label = androidx.compose.ui.res.stringResource(com.example.R.string.drawer_menu_diskusi_hangat),
                isSelected = currentRoute == "diskusiHangat",
                testTag = "drawer_item_diskusi_hangat",
                onClick = {
                    onCloseDrawer()
                    onNavigate("diskusiHangat")
                }
            )

            HorizontalDivider(
                modifier = Modifier.padding(horizontal = 16.dp, vertical = 6.dp),
                color = DividerDefaults.color.copy(alpha = 0.4f)
            )

            // Section: Tools
            DrawerSectionHeader(title = androidx.compose.ui.res.stringResource(com.example.R.string.drawer_section_tools))

            DrawerAnimatedMenuItem(
                index = 6,
                isOpen = isOpen,
                icon = Icons.Default.Code,
                label = androidx.compose.ui.res.stringResource(com.example.R.string.drawer_menu_playground),
                isSelected = currentRoute?.startsWith("playground") == true,
                testTag = "drawer_item_playground",
                onClick = {
                    onCloseDrawer()
                    onNavigate("playground/fe-web-foundation-00")
                }
            )

            DrawerAnimatedMenuItem(
                index = 7,
                isOpen = isOpen,
                icon = Icons.Default.Psychology,
                label = androidx.compose.ui.res.stringResource(com.example.R.string.drawer_menu_ai_tutor),
                isSelected = currentRoute?.startsWith("aiTutor") == true,
                testTag = "drawer_item_ai_tutor",
                onClick = {
                    onCloseDrawer()
                    onNavigate("aiTutor?courseTitle=Materi&moduleTitle=General")
                }
            )

            HorizontalDivider(
                modifier = Modifier.padding(horizontal = 16.dp, vertical = 6.dp),
                color = DividerDefaults.color.copy(alpha = 0.4f)
            )

            // Section: Lainnya
            DrawerSectionHeader(title = androidx.compose.ui.res.stringResource(com.example.R.string.drawer_section_other))

            DrawerAnimatedMenuItem(
                index = 8,
                isOpen = isOpen,
                icon = Icons.Default.Settings,
                label = androidx.compose.ui.res.stringResource(com.example.R.string.drawer_menu_settings),
                isSelected = currentRoute == "profile",
                testTag = "drawer_item_settings",
                onClick = {
                    onCloseDrawer()
                    onNavigate("profile")
                }
            )

            DrawerAnimatedMenuItem(
                index = 9,
                isOpen = isOpen,
                icon = Icons.Default.HelpOutline,
                label = androidx.compose.ui.res.stringResource(com.example.R.string.drawer_menu_help),
                isSelected = currentRoute == "help",
                testTag = "drawer_item_about",
                onClick = {
                    onCloseDrawer()
                    onNavigate("help")
                }
            )

            HorizontalDivider(
                modifier = Modifier.padding(horizontal = 16.dp, vertical = 6.dp),
                color = DividerDefaults.color.copy(alpha = 0.4f)
            )

            // Keluar / Logout
            DrawerAnimatedMenuItem(
                index = 10,
                isOpen = isOpen,
                icon = Icons.AutoMirrored.Filled.Logout,
                label = androidx.compose.ui.res.stringResource(com.example.R.string.drawer_menu_logout),
                isDestructive = true,
                isSelected = false,
                testTag = "drawer_item_logout",
                onClick = {
                    showLogoutDialog = true
                }
            )

            Spacer(modifier = Modifier.height(20.dp))
        }
    }

    // --- Logout Confirmation Dialog ---
    if (showLogoutDialog) {
        AppDialog(
            onDismissRequest = { showLogoutDialog = false },
            icon = {
                Box(
                    modifier = Modifier
                        .size(52.dp)
                        .clip(CircleShape)
                        .background(ErrorRed.copy(alpha = 0.12f)),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.Logout,
                        contentDescription = null,
                        tint = ErrorRed,
                        modifier = Modifier.size(24.dp)
                    )
                }
            },
            title = {
                Text(
                    text = androidx.compose.ui.res.stringResource(com.example.R.string.drawer_logout_dialog_title),
                    fontWeight = FontWeight.Bold,
                    style = MaterialTheme.typography.titleMedium
                )
            },
            text = {
                Text(
                    text = androidx.compose.ui.res.stringResource(com.example.R.string.drawer_logout_dialog_message),
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    textAlign = TextAlign.Center
                )
            },
            confirmButton = {
                Button(
                    onClick = {
                        showLogoutDialog = false
                        onCloseDrawer()
                        onLogout()
                    },
                    shape = RoundedCornerShape(12.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = ErrorRed),
                    modifier = Modifier.testTag("drawer_confirm_logout_button")
                ) {
                    Text(
                        text = androidx.compose.ui.res.stringResource(com.example.R.string.drawer_logout_dialog_confirm),
                        color = Color.White,
                        fontWeight = FontWeight.Bold
                    )
                }
            },
            dismissButton = {
                OutlinedButton(
                    onClick = { showLogoutDialog = false },
                    shape = RoundedCornerShape(12.dp),
                    border = BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant),
                    modifier = Modifier.testTag("drawer_cancel_logout_button")
                ) {
                    Text(text = androidx.compose.ui.res.stringResource(com.example.R.string.common_cancel))
                }
            }
        )
    }
}

/**
 * Sky period classification
 */
private enum class SkyPeriod {
    PAGI, SIANG, SORE, MALAM
}

private data class StarSpec(
    val xNorm: Float,
    val yNorm: Float,
    val radiusDp: Float,
    val phaseOffset: Float,
    val freq: Float,
    val isCross: Boolean = false
)

private val NIGHT_STARS = listOf(
    StarSpec(0.08f, 0.20f, 1.2f, 0.1f, 3f),
    StarSpec(0.16f, 0.12f, 1.6f, 0.4f, 4f, isCross = true),
    StarSpec(0.24f, 0.28f, 1.1f, 0.7f, 2f),
    StarSpec(0.33f, 0.15f, 1.5f, 0.2f, 5f),
    StarSpec(0.42f, 0.25f, 1.0f, 0.9f, 3f),
    StarSpec(0.48f, 0.10f, 1.8f, 0.5f, 4f, isCross = true),
    StarSpec(0.56f, 0.22f, 1.2f, 0.3f, 2f),
    StarSpec(0.63f, 0.14f, 1.4f, 0.8f, 3f),
    StarSpec(0.70f, 0.26f, 1.1f, 0.6f, 5f),
    StarSpec(0.82f, 0.18f, 1.7f, 0.2f, 4f, isCross = true),
    StarSpec(0.88f, 0.32f, 1.2f, 0.5f, 3f),
    StarSpec(0.93f, 0.15f, 1.0f, 0.0f, 2f),
    StarSpec(0.12f, 0.42f, 1.1f, 0.8f, 3f),
    StarSpec(0.28f, 0.46f, 1.3f, 0.3f, 4f),
    StarSpec(0.45f, 0.40f, 1.0f, 0.7f, 2f),
    StarSpec(0.60f, 0.44f, 1.2f, 0.1f, 5f),
    StarSpec(0.76f, 0.48f, 1.1f, 0.4f, 3f),
    StarSpec(0.85f, 0.52f, 1.0f, 0.9f, 4f)
)

private data class CloudPalette(
    val backAlpha: Float,
    val midAlpha: Float,
    val frontAlpha: Float,
    val color: Color
)

/**
 * Custom Canvas drawing for sky scenery
 * (3-stop gradient, radial glow, layered halos, organic parallax clouds, period details, stars & meteor)
 */
private fun DrawScope.drawSkyScenery(
    timePeriod: SkyPeriod,
    skyBrush: Brush,
    backHillPath: Path,
    frontHillPath: Path,
    celestialProgress: Float,
    phase: Float
) {
    val w = size.width
    val h = size.height

    // 1. Sky Gradient (3-stop cached vertical gradient)
    drawRect(brush = skyBrush)

    // 2. Night Stars (Twinkling with individual phases) & Shooting Star (1x per ~8s)
    if (timePeriod == SkyPeriod.MALAM) {
        for (star in NIGHT_STARS) {
            val twinkle = (sin((phase * star.freq + star.phaseOffset) * 2.0 * PI).toFloat() + 1f) * 0.5f
            val alpha = 0.22f + 0.75f * twinkle
            val starCenter = Offset(w * star.xNorm, h * star.yNorm)
            val r = star.radiusDp.dp.toPx()

            drawCircle(
                color = Color.White.copy(alpha = alpha),
                radius = r,
                center = starCenter
            )

            if (star.isCross && twinkle > 0.65f) {
                val crossAlpha = (twinkle - 0.65f) / 0.35f * 0.70f
                val crossLen = r * 2.5f
                drawLine(
                    color = Color.White.copy(alpha = crossAlpha),
                    start = Offset(starCenter.x - crossLen, starCenter.y),
                    end = Offset(starCenter.x + crossLen, starCenter.y),
                    strokeWidth = 1.dp.toPx()
                )
                drawLine(
                    color = Color.White.copy(alpha = crossAlpha),
                    start = Offset(starCenter.x, starCenter.y - crossLen),
                    end = Offset(starCenter.x, starCenter.y + crossLen),
                    strokeWidth = 1.dp.toPx()
                )
            }
        }

        // Shooting Star: triggers once every ~8 seconds (during phase 0.68f..0.82f)
        if (phase in 0.68f..0.82f) {
            val meteorProgress = (phase - 0.68f) / 0.14f
            val startX = w * 0.85f
            val startY = h * 0.06f
            val endX = w * 0.25f
            val endY = h * 0.46f

            val headX = startX + (endX - startX) * meteorProgress
            val headY = startY + (endY - startY) * meteorProgress

            val dx = endX - startX
            val dy = endY - startY
            val dist = sqrt(dx * dx + dy * dy)
            val normDx = dx / dist
            val normDy = dy / dist
            val tailLen = 38.dp.toPx()

            val tailX = headX - normDx * tailLen
            val tailY = headY - normDy * tailLen

            val meteorAlpha = when {
                meteorProgress < 0.2f -> meteorProgress / 0.2f
                meteorProgress > 0.8f -> (1f - meteorProgress) / 0.2f
                else -> 1f
            }

            val meteorBrush = Brush.linearGradient(
                colors = listOf(
                    Color.White.copy(alpha = 0f),
                    Color.White.copy(alpha = 0.85f * meteorAlpha)
                ),
                start = Offset(tailX, tailY),
                end = Offset(headX, headY)
            )

            drawLine(
                brush = meteorBrush,
                start = Offset(tailX, tailY),
                end = Offset(headX, headY),
                strokeWidth = 2.dp.toPx(),
                cap = StrokeCap.Round
            )
            drawCircle(
                color = Color.White.copy(alpha = 0.95f * meteorAlpha),
                radius = 1.6.dp.toPx(),
                center = Offset(headX, headY)
            )
        }
    }

    // 3. Celestial Body (Sun / Moon) rising with celestialProgress (600ms easing)
    val riseOffsetY = (1f - celestialProgress) * 45.dp.toPx()

    when (timePeriod) {
        SkyPeriod.PAGI -> {
            val sunCenter = Offset(w * 0.22f, h * 0.58f + riseOffsetY)
            val sunRadius = 18.dp.toPx()

            // Soft radial glow (no blur)
            drawCircle(
                brush = Brush.radialGradient(
                    colors = listOf(
                        Color(0xFFFFF9C4).copy(alpha = 0.45f),
                        Color(0xFFFFE082).copy(alpha = 0.16f),
                        Color.Transparent
                    ),
                    center = sunCenter,
                    radius = sunRadius * 3.8f
                ),
                radius = sunRadius * 3.8f,
                center = sunCenter
            )

            // Multi-layered halos
            drawCircle(color = Color(0xFFFFD54F).copy(alpha = 0.12f), radius = sunRadius * 2.0f, center = sunCenter)
            drawCircle(color = Color(0xFFFFD54F).copy(alpha = 0.24f), radius = sunRadius * 1.5f, center = sunCenter)
            drawCircle(color = Color(0xFFFFE082).copy(alpha = 0.38f), radius = sunRadius * 1.22f, center = sunCenter)
            // Sun disc
            drawCircle(color = Color(0xFFFFD54F), radius = sunRadius, center = sunCenter)
        }
        SkyPeriod.SIANG -> {
            val sunCenter = Offset(w * 0.65f, h * 0.30f + riseOffsetY)
            val sunRadius = 19.dp.toPx()

            // Soft radial glow
            drawCircle(
                brush = Brush.radialGradient(
                    colors = listOf(
                        Color(0xFFFFFFFF).copy(alpha = 0.50f),
                        Color(0xFFFFF59D).copy(alpha = 0.20f),
                        Color.Transparent
                    ),
                    center = sunCenter,
                    radius = sunRadius * 4.0f
                ),
                radius = sunRadius * 4.0f,
                center = sunCenter
            )

            // Multi-layered halos
            drawCircle(color = Color(0xFFFFFDE7).copy(alpha = 0.15f), radius = sunRadius * 2.1f, center = sunCenter)
            drawCircle(color = Color(0xFFFFF9C4).copy(alpha = 0.28f), radius = sunRadius * 1.55f, center = sunCenter)
            drawCircle(color = Color(0xFFFFF59D).copy(alpha = 0.42f), radius = sunRadius * 1.25f, center = sunCenter)
            // Sun disc
            drawCircle(color = Color(0xFFFFF176), radius = sunRadius, center = sunCenter)

            // Lens flare: sun sparkle rays & diagonal refraction spots
            val rayLen = sunRadius * 1.6f
            drawLine(
                color = Color.White.copy(alpha = 0.35f),
                start = Offset(sunCenter.x - rayLen, sunCenter.y),
                end = Offset(sunCenter.x + rayLen, sunCenter.y),
                strokeWidth = 1.2.dp.toPx()
            )
            drawLine(
                color = Color.White.copy(alpha = 0.35f),
                start = Offset(sunCenter.x, sunCenter.y - rayLen),
                end = Offset(sunCenter.x, sunCenter.y + rayLen),
                strokeWidth = 1.2.dp.toPx()
            )

            val flareTarget = Offset(w * 0.12f, h * 0.88f)
            val flareVec = flareTarget - sunCenter
            drawCircle(
                color = Color(0xFFFFF9C4).copy(alpha = 0.24f),
                radius = 4.dp.toPx(),
                center = sunCenter + flareVec * 0.24f
            )
            drawCircle(
                color = Color(0xFF80DEEA).copy(alpha = 0.18f),
                radius = 7.dp.toPx(),
                center = sunCenter + flareVec * 0.46f
            )
            drawCircle(
                color = Color(0xFFFFCC80).copy(alpha = 0.16f),
                radius = 9.dp.toPx(),
                center = sunCenter + flareVec * 0.68f
            )
        }
        SkyPeriod.SORE -> {
            val sunCenter = Offset(w * 0.78f, h * 0.54f + riseOffsetY)
            val sunRadius = 24.dp.toPx()

            // Soft radial sunset glow
            drawCircle(
                brush = Brush.radialGradient(
                    colors = listOf(
                        Color(0xFFFFCC80).copy(alpha = 0.48f),
                        Color(0xFFFFAB91).copy(alpha = 0.20f),
                        Color.Transparent
                    ),
                    center = sunCenter,
                    radius = sunRadius * 3.6f
                ),
                radius = sunRadius * 3.6f,
                center = sunCenter
            )

            // Layered halos
            drawCircle(color = Color(0xFFFFAB91).copy(alpha = 0.14f), radius = sunRadius * 1.9f, center = sunCenter)
            drawCircle(color = Color(0xFFFFB74D).copy(alpha = 0.25f), radius = sunRadius * 1.45f, center = sunCenter)
            drawCircle(color = Color(0xFFFFA726).copy(alpha = 0.40f), radius = sunRadius * 1.2f, center = sunCenter)
            // Sun disc
            drawCircle(color = Color(0xFFFF9800), radius = sunRadius, center = sunCenter)
        }
        SkyPeriod.MALAM -> {
            val moonCenter = Offset(w * 0.76f, h * 0.32f + riseOffsetY)
            val moonRadius = 17.dp.toPx()

            // Soft moonlight radial glow
            drawCircle(
                brush = Brush.radialGradient(
                    colors = listOf(
                        Color(0xFFE8EAF6).copy(alpha = 0.32f),
                        Color(0xFF9FA8DA).copy(alpha = 0.12f),
                        Color.Transparent
                    ),
                    center = moonCenter,
                    radius = moonRadius * 3.5f
                ),
                radius = moonRadius * 3.5f,
                center = moonCenter
            )

            // Moon halos
            drawCircle(color = Color(0xFFFFF9C4).copy(alpha = 0.10f), radius = moonRadius * 1.85f, center = moonCenter)
            drawCircle(color = Color(0xFFFFF9C4).copy(alpha = 0.22f), radius = moonRadius * 1.38f, center = moonCenter)
            // Moon disc
            drawCircle(color = Color(0xFFFFFDE7), radius = moonRadius, center = moonCenter)
            // Moon craters
            drawCircle(
                color = Color(0xFF90A4AE).copy(alpha = 0.36f),
                radius = moonRadius * 0.22f,
                center = Offset(moonCenter.x - moonRadius * 0.34f, moonCenter.y - moonRadius * 0.20f)
            )
            drawCircle(
                color = Color(0xFF90A4AE).copy(alpha = 0.30f),
                radius = moonRadius * 0.16f,
                center = Offset(moonCenter.x + moonRadius * 0.24f, moonCenter.y + moonRadius * 0.24f)
            )
            drawCircle(
                color = Color(0xFF90A4AE).copy(alpha = 0.26f),
                radius = moonRadius * 0.14f,
                center = Offset(moonCenter.x - moonRadius * 0.12f, moonCenter.y + moonRadius * 0.36f)
            )
        }
    }

    // 4. Parallax Clouds: 3 layers with organic shapes & distinct speeds/opacity derived from single phase
    val c1Offset = sin((phase + 0.10f) * 2.0 * PI).toFloat() * 14.dp.toPx()
    val c2Offset = sin((phase + 0.55f) * 2.0 * PI).toFloat() * 22.dp.toPx()
    val c3Offset = sin((phase + 0.85f) * 2.0 * PI).toFloat() * 16.dp.toPx()

    val cloudPalette = when (timePeriod) {
        SkyPeriod.PAGI -> CloudPalette(0.24f, 0.40f, 0.30f, Color.White)
        SkyPeriod.SIANG -> CloudPalette(0.38f, 0.58f, 0.46f, Color.White)
        SkyPeriod.SORE -> CloudPalette(0.20f, 0.36f, 0.26f, Color(0xFFFFE0B2))
        SkyPeriod.MALAM -> CloudPalette(0.10f, 0.18f, 0.12f, Color(0xFFCFD8DC))
    }

    // Layer 1 (Back / slow)
    drawOrganicCloud(
        centerX = w * 0.26f + c1Offset,
        centerY = h * 0.42f,
        baseRadius = 10.dp.toPx(),
        color = cloudPalette.color.copy(alpha = cloudPalette.backAlpha)
    )

    // Layer 2 (Mid / main)
    drawOrganicCloud(
        centerX = w * 0.72f + c2Offset,
        centerY = h * 0.58f,
        baseRadius = 13.dp.toPx(),
        color = cloudPalette.color.copy(alpha = cloudPalette.midAlpha)
    )

    // Layer 3 (Front / accent)
    drawOrganicCloud(
        centerX = w * 0.44f + c3Offset,
        centerY = h * 0.68f,
        baseRadius = 11.dp.toPx(),
        color = cloudPalette.color.copy(alpha = cloudPalette.frontAlpha)
    )

    // 5. Period Specific Terrains: Hills & Morning Mist
    when (timePeriod) {
        SkyPeriod.PAGI -> {
            // Background hill
            drawPath(path = backHillPath, color = Color(0xFF426A66).copy(alpha = 0.36f))
            // Delicate morning mist between hill layers
            drawRect(
                brush = Brush.verticalGradient(
                    colors = listOf(Color.Transparent, Color(0xFFE0F2F1).copy(alpha = 0.45f)),
                    startY = h * 0.68f,
                    endY = h * 0.86f
                ),
                topLeft = Offset(0f, h * 0.68f),
                size = Size(w, h * 0.18f)
            )
            // Foreground hill
            drawPath(path = frontHillPath, color = Color(0xFF264F4B).copy(alpha = 0.54f))
            // Lower morning mist
            drawRect(
                brush = Brush.verticalGradient(
                    colors = listOf(Color.Transparent, Color(0xFFC8E6C9).copy(alpha = 0.38f)),
                    startY = h * 0.82f,
                    endY = h
                ),
                topLeft = Offset(0f, h * 0.82f),
                size = Size(w, h * 0.18f)
            )
        }
        SkyPeriod.SORE -> {
            // Warm sunset silhouette hills
            drawPath(path = backHillPath, color = Color(0xFF38153B).copy(alpha = 0.50f))
            drawPath(path = frontHillPath, color = Color(0xFF200924).copy(alpha = 0.76f))
        }
        else -> {}
    }
}

/**
 * Draws an organic cloud composed of overlapping rounded puffs and a smoothed base
 */
private fun DrawScope.drawOrganicCloud(
    centerX: Float,
    centerY: Float,
    baseRadius: Float,
    color: Color
) {
    val r = baseRadius
    drawCircle(color = color, radius = r * 0.65f, center = Offset(centerX - r * 1.55f, centerY + r * 0.18f))
    drawCircle(color = color, radius = r * 0.88f, center = Offset(centerX - r * 0.82f, centerY - r * 0.08f))
    drawCircle(color = color, radius = r * 1.05f, center = Offset(centerX, centerY - r * 0.28f))
    drawCircle(color = color, radius = r * 0.82f, center = Offset(centerX + r * 0.82f, centerY - r * 0.10f))
    drawCircle(color = color, radius = r * 0.60f, center = Offset(centerX + r * 1.50f, centerY + r * 0.20f))
    drawRoundRect(
        color = color,
        topLeft = Offset(centerX - r * 1.65f, centerY),
        size = Size(r * 3.30f, r * 0.72f),
        cornerRadius = CornerRadius(r * 0.36f, r * 0.36f)
    )
}

@Composable
private fun DrawerSectionHeader(title: String) {
    Text(
        text = title,
        style = MaterialTheme.typography.labelMedium,
        fontWeight = FontWeight.SemiBold,
        color = MaterialTheme.colorScheme.onSurfaceVariant,
        modifier = Modifier.padding(start = 20.dp, end = 20.dp, top = 10.dp, bottom = 4.dp)
    )
}

@Composable
private fun DrawerAnimatedMenuItem(
    index: Int,
    isOpen: Boolean,
    icon: ImageVector,
    label: String,
    isSelected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    badgeCount: Int = 0,
    isDestructive: Boolean = false,
    testTag: String = ""
) {
    val itemAlpha by animateFloatAsState(
        targetValue = if (isOpen) 1f else 0f,
        animationSpec = tween(
            durationMillis = 220,
            delayMillis = (index * 30).coerceAtMost(300),
            easing = FastOutSlowInEasing
        ),
        label = "item_alpha_$index"
    )
    val itemOffsetY by animateDpAsState(
        targetValue = if (isOpen) 0.dp else 8.dp,
        animationSpec = tween(
            durationMillis = 220,
            delayMillis = (index * 30).coerceAtMost(300),
            easing = FastOutSlowInEasing
        ),
        label = "item_offset_$index"
    )

    val contentColor = when {
        isDestructive -> ErrorRed
        isSelected -> MaterialTheme.colorScheme.onPrimaryContainer
        else -> MaterialTheme.colorScheme.onSurface
    }

    val iconColor = when {
        isDestructive -> ErrorRed
        isSelected -> MaterialTheme.colorScheme.onPrimaryContainer
        else -> MaterialTheme.colorScheme.onSurfaceVariant
    }

    NavigationDrawerItem(
        icon = {
            Icon(
                imageVector = icon,
                contentDescription = label,
                tint = iconColor,
                modifier = Modifier.size(22.dp)
            )
        },
        label = {
            Text(
                text = label,
                style = MaterialTheme.typography.bodyMedium.copy(
                    fontWeight = if (isSelected || isDestructive) FontWeight.SemiBold else FontWeight.Normal
                ),
                color = contentColor
            )
        },
        badge = if (badgeCount > 0) {
            {
                Badge(
                    containerColor = FlameOrange,
                    contentColor = Color.White
                ) {
                    Text(
                        text = if (badgeCount > 99) "99+" else "$badgeCount",
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        } else null,
        selected = isSelected,
        onClick = onClick,
        colors = NavigationDrawerItemDefaults.colors(
            selectedContainerColor = MaterialTheme.colorScheme.primaryContainer,
            unselectedContainerColor = Color.Transparent,
            selectedIconColor = iconColor,
            unselectedIconColor = iconColor,
            selectedTextColor = contentColor,
            unselectedTextColor = contentColor
        ),
        shape = RoundedCornerShape(24.dp),
        modifier = modifier
            .fillMaxWidth()
            .height(48.dp)
            .padding(horizontal = 12.dp, vertical = 2.dp)
            .offset(y = itemOffsetY)
            .testTag(testTag)
    )
}
