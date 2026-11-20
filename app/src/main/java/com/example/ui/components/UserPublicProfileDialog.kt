package com.example.ui.components

import androidx.compose.animation.animateColor
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.AutoAwesome
import androidx.compose.material.icons.rounded.EmojiEvents
import androidx.compose.material.icons.rounded.LocalFireDepartment
import androidx.compose.material.icons.rounded.MenuBook
import androidx.compose.material.icons.rounded.Shield
import androidx.compose.material.icons.rounded.Verified
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.R
import com.example.data.CourseCatalog
import com.example.data.remote.PublicUserProfile
import com.example.ui.screens.badgeVectorIcon
import com.example.ui.theme.AmberGold
import com.example.ui.theme.CustomProfileAvatar
import com.example.ui.theme.FlameOrange
import com.example.ui.theme.ProfileCustomizationCatalog
import com.example.ui.theme.TealPrimary

@Composable
private fun badgeTitleForId(id: String): String {
    val base = CourseCatalog.allBadges.firstOrNull { it.id == id }
    return com.example.util.localization.DataLabelMappings.badgeName(id, base?.name ?: "Pencapaian")
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun UserPublicProfileDialog(
    profile: PublicUserProfile,
    isLoading: Boolean = false,
    onDismiss: () -> Unit
) {
    val isSuperAdmin = profile.accessRole.equals("super_admin", ignoreCase = true) ||
        profile.role.equals("super_admin", ignoreCase = true) ||
        profile.role.equals("Super Admin", ignoreCase = true) ||
        profile.userId == "super-admin"
    val isMentor = !isSuperAdmin && (
        profile.accessRole.equals("facilitator", ignoreCase = true) ||
            profile.role.equals("Fasilitator", ignoreCase = true) ||
            profile.role.equals("facilitator", ignoreCase = true) ||
            profile.userId == "facilitator-mentor"
        )

    val bannerPreset = remember(profile.bannerTheme, isMentor, isSuperAdmin) {
        when {
            isSuperAdmin -> ProfileCustomizationCatalog.bannerFor("aurora_purple")
            isMentor -> ProfileCustomizationCatalog.bannerFor("emerald_mint")
            else -> ProfileCustomizationCatalog.bannerFor(profile.bannerTheme)
        }
    }
    val featuredBadgeId = profile.featuredBadgeId.takeIf { id ->
        id.isNotBlank() && (id in profile.unlockedBadgeIds || isMentor || isSuperAdmin)
    }.orEmpty()

    AppDialog(
        onDismissRequest = onDismiss,
        modifier = Modifier.widthIn(max = 390.dp),
        text = {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.fillMaxWidth()
            ) {
                if (isLoading) {
                    LinearProgressIndicator(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(bottom = 10.dp),
                        color = TealPrimary
                    )
                }

                // Custom Gradient Showcase Banner (Animated Aurora for Super Admin, Prestige Shimmer for Facilitator)
                val bannerModifier = Modifier
                    .fillMaxWidth()
                    .height(86.dp)
                    .clip(RoundedCornerShape(16.dp))

                if (isSuperAdmin) {
                    val adminTransition = rememberInfiniteTransition(label = "super_admin_banner")
                    val c1 by adminTransition.animateColor(
                        initialValue = Color(0xFF1E1B4B),
                        targetValue = Color(0xFF5B21B6),
                        animationSpec = infiniteRepeatable(tween(2600, easing = LinearEasing), RepeatMode.Reverse),
                        label = "sa_c1"
                    )
                    val c2 by adminTransition.animateColor(
                        initialValue = Color(0xFF701A75),
                        targetValue = Color(0xFF0E7490),
                        animationSpec = infiniteRepeatable(tween(2600, easing = LinearEasing), RepeatMode.Reverse),
                        label = "sa_c2"
                    )
                    val sweepOffset by adminTransition.animateFloat(
                        initialValue = -250f,
                        targetValue = 950f,
                        animationSpec = infiniteRepeatable(tween(2800, easing = LinearEasing), RepeatMode.Restart),
                        label = "sa_sweep"
                    )
                    Box(
                        modifier = bannerModifier
                            .border(
                                BorderStroke(
                                    1.5.dp,
                                    Brush.linearGradient(listOf(AmberGold, Color(0xFFEC4899), Color(0xFF38BDF8)))
                                ),
                                RoundedCornerShape(16.dp)
                            )
                            .background(Brush.linearGradient(listOf(c1, c2)))
                    ) {
                        // Diagonal holographic light sweep
                        Box(
                            modifier = Modifier
                                .fillMaxSize()
                                .background(
                                    Brush.linearGradient(
                                        colors = listOf(
                                            Color.Transparent,
                                            Color.White.copy(alpha = 0.22f),
                                            AmberGold.copy(alpha = 0.16f),
                                            Color.Transparent
                                        ),
                                        start = Offset(sweepOffset, 0f),
                                        end = Offset(sweepOffset + 180f, 180f)
                                    )
                                )
                        )
                        Surface(
                            color = Color(0xFF0F172A).copy(alpha = 0.65f),
                            border = BorderStroke(1.dp, AmberGold.copy(alpha = 0.7f)),
                            shape = RoundedCornerShape(999.dp),
                            modifier = Modifier
                                .align(Alignment.TopEnd)
                                .padding(10.dp)
                        ) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                modifier = Modifier.padding(horizontal = 9.dp, vertical = 3.dp)
                            ) {
                                Icon(
                                    imageVector = Icons.Rounded.AutoAwesome,
                                    contentDescription = null,
                                    tint = AmberGold,
                                    modifier = Modifier.size(12.dp)
                                )
                                Spacer(modifier = Modifier.width(4.dp))
                                Text(
                                    text = stringResource(R.string.public_profile_badge_architect),
                                    fontSize = 10.sp,
                                    fontWeight = FontWeight.ExtraBold,
                                    color = Color(0xFFFDE68A),
                                    maxLines = 1,
                                    overflow = TextOverflow.Ellipsis
                                )
                            }
                        }
                    }
                } else if (isMentor) {
                    val mentorBrush = Brush.linearGradient(
                        listOf(Color(0xFF064E3B), Color(0xFF0F766E), Color(0xFF92400E))
                    )
                    Box(
                        modifier = bannerModifier
                            .border(
                                BorderStroke(1.dp, AmberGold.copy(alpha = 0.55f)),
                                RoundedCornerShape(16.dp)
                            )
                            .background(mentorBrush)
                            .padding(10.dp)
                    ) {
                        Surface(
                            color = Color(0xFF064E3B).copy(alpha = 0.72f),
                            border = BorderStroke(1.dp, Color(0xFF6EE7B7).copy(alpha = 0.6f)),
                            shape = RoundedCornerShape(999.dp),
                            modifier = Modifier.align(Alignment.TopEnd)
                        ) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                modifier = Modifier.padding(horizontal = 9.dp, vertical = 3.dp)
                            ) {
                                Icon(
                                    imageVector = Icons.Rounded.Verified,
                                    contentDescription = null,
                                    tint = Color(0xFF6EE7B7),
                                    modifier = Modifier.size(12.dp)
                                )
                                Spacer(modifier = Modifier.width(4.dp))
                                Text(
                                    text = stringResource(R.string.public_profile_badge_official_mentor),
                                    fontSize = 10.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = Color(0xFFA7F3D0),
                                    maxLines = 1,
                                    overflow = TextOverflow.Ellipsis
                                )
                            }
                        }
                    }
                } else {
                    Box(
                        modifier = bannerModifier
                            .background(bannerPreset.toBrush())
                            .padding(10.dp)
                    ) {
                        Surface(
                            color = Color.Black.copy(alpha = 0.28f),
                            shape = RoundedCornerShape(999.dp),
                            modifier = Modifier.align(Alignment.TopEnd)
                        ) {
                            Text(
                                text = profile.headlineTitle.ifBlank { "WebDev Learner" },
                                fontSize = 10.5.sp,
                                fontWeight = FontWeight.Bold,
                                color = bannerPreset.accentColor,
                                maxLines = 1,
                                overflow = TextOverflow.Ellipsis,
                                modifier = Modifier.padding(horizontal = 9.dp, vertical = 3.dp)
                            )
                        }
                    }
                }

                // Overlapping Custom Avatar + Frame
                Box(
                    modifier = Modifier.offset(y = (-34).dp),
                    contentAlignment = Alignment.Center
                ) {
                    CustomProfileAvatar(
                        name = profile.name,
                        avatarStyle = profile.avatarStyle,
                        avatarFrame = profile.avatarFrame,
                        bannerTheme = profile.bannerTheme,
                        isMentor = isMentor,
                        isSuperAdmin = isSuperAdmin,
                        size = 72.dp
                    )
                }

                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier
                        .fillMaxWidth()
                        .offset(y = (-24).dp)
                ) {
                    Text(
                        text = profile.name,
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        textAlign = TextAlign.Center
                    )

                    Spacer(modifier = Modifier.height(4.dp))

                    when {
                        isSuperAdmin -> {
                            Surface(
                                shape = RoundedCornerShape(999.dp),
                                color = Color(0xFF312E81).copy(alpha = 0.22f),
                                border = BorderStroke(
                                    1.2.dp,
                                    Brush.linearGradient(listOf(AmberGold, Color(0xFFEC4899), Color(0xFF38BDF8)))
                                )
                            ) {
                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    modifier = Modifier.padding(horizontal = 11.dp, vertical = 4.dp)
                                ) {
                                    Icon(
                                        imageVector = Icons.Rounded.Shield,
                                        contentDescription = null,
                                        tint = AmberGold,
                                        modifier = Modifier.size(13.dp)
                                    )
                                    Spacer(modifier = Modifier.width(5.dp))
                                    Text(
                                        text = stringResource(R.string.public_profile_role_super_admin),
                                        style = MaterialTheme.typography.labelSmall,
                                        fontWeight = FontWeight.ExtraBold,
                                        color = AmberGold
                                    )
                                }
                            }
                        }
                        isMentor -> {
                            Surface(
                                shape = RoundedCornerShape(999.dp),
                                color = Color(0xFF10B981).copy(alpha = 0.16f),
                                border = BorderStroke(1.dp, Color(0xFF10B981).copy(alpha = 0.5f))
                            ) {
                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp)
                                ) {
                                    Icon(
                                        imageVector = Icons.Rounded.Verified,
                                        contentDescription = null,
                                        tint = Color(0xFF10B981),
                                        modifier = Modifier.size(13.dp)
                                    )
                                    Spacer(modifier = Modifier.width(5.dp))
                                    Text(
                                        text = stringResource(R.string.public_profile_role_facilitator),
                                        style = MaterialTheme.typography.labelSmall,
                                        fontWeight = FontWeight.Bold,
                                        color = Color(0xFF10B981)
                                    )
                                }
                            }
                        }
                        else -> {
                            Surface(
                                shape = RoundedCornerShape(999.dp),
                                color = bannerPreset.startColor.copy(alpha = 0.14f)
                            ) {
                                Text(
                                    text = stringResource(R.string.public_profile_role_student_format, profile.level),
                                    style = MaterialTheme.typography.labelSmall,
                                    fontWeight = FontWeight.Bold,
                                    color = TealPrimary,
                                    modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp)
                                )
                            }
                        }
                    }

                    // Featured Badge Pin Showcase
                    if (featuredBadgeId.isNotBlank()) {
                        Spacer(modifier = Modifier.height(8.dp))
                        Surface(
                            shape = RoundedCornerShape(10.dp),
                            color = AmberGold.copy(alpha = 0.13f),
                            border = BorderStroke(1.dp, AmberGold.copy(alpha = 0.38f))
                        ) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                modifier = Modifier.padding(horizontal = 10.dp, vertical = 5.dp)
                            ) {
                                Icon(
                                    imageVector = badgeVectorIcon(featuredBadgeId),
                                    contentDescription = null,
                                    tint = AmberGold,
                                    modifier = Modifier.size(14.dp)
                                )
                                Spacer(modifier = Modifier.width(5.dp))
                                Text(
                                    text = "${stringResource(R.string.public_profile_showcase_pin)}: ${badgeTitleForId(featuredBadgeId)}",
                                    style = MaterialTheme.typography.labelSmall,
                                    fontWeight = FontWeight.Bold,
                                    color = AmberGold
                                )
                            }
                        }
                    }

                    // Skill Focus Chips
                    if (profile.skillTags.isNotEmpty()) {
                        Spacer(modifier = Modifier.height(8.dp))
                        FlowRow(
                            horizontalArrangement = Arrangement.spacedBy(6.dp, Alignment.CenterHorizontally),
                            verticalArrangement = Arrangement.spacedBy(6.dp),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            profile.skillTags.take(3).forEach { tag ->
                                Surface(
                                    shape = RoundedCornerShape(999.dp),
                                    color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.65f),
                                    border = BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.7f))
                                ) {
                                    Text(
                                        text = tag,
                                        style = MaterialTheme.typography.labelSmall,
                                        fontWeight = FontWeight.SemiBold,
                                        color = MaterialTheme.colorScheme.onSurface,
                                        modifier = Modifier.padding(horizontal = 9.dp, vertical = 3.dp)
                                    )
                                }
                            }
                        }
                    }

                    if (profile.bio.isNotBlank()) {
                        Spacer(modifier = Modifier.height(10.dp))
                        Text(
                            text = profile.bio,
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                            textAlign = TextAlign.Center,
                            lineHeight = 18.sp
                        )
                    }

                    Spacer(modifier = Modifier.height(14.dp))

                    // Stats Row (4 compact tiles)
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(6.dp),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        PublicStatTile(
                            icon = Icons.Rounded.AutoAwesome,
                            tint = AmberGold,
                            value = "${profile.totalXp}",
                            label = "XP",
                            modifier = Modifier.weight(1f)
                        )
                        PublicStatTile(
                            icon = Icons.Rounded.LocalFireDepartment,
                            tint = FlameOrange,
                            value = "${profile.currentStreak}h",
                            label = "Streak",
                            modifier = Modifier.weight(1f)
                        )
                        PublicStatTile(
                            icon = Icons.Rounded.MenuBook,
                            tint = Color(0xFF2563EB),
                            value = "${profile.completedCoursesCount}",
                            label = stringResource(R.string.public_profile_stats_courses),
                            modifier = Modifier.weight(1f)
                        )
                        PublicStatTile(
                            icon = Icons.Rounded.EmojiEvents,
                            tint = TealPrimary,
                            value = "${profile.unlockedBadgeIds.size}",
                            label = stringResource(R.string.public_profile_stats_badges),
                            modifier = Modifier.weight(1f)
                        )
                    }

                    Spacer(modifier = Modifier.height(14.dp))

                    // Unlocked Achievements
                    Column(modifier = Modifier.fillMaxWidth()) {
                        Text(
                            text = stringResource(R.string.public_profile_achievements_title),
                            style = MaterialTheme.typography.labelMedium,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                        Spacer(modifier = Modifier.height(8.dp))

                        if (profile.unlockedBadgeIds.isEmpty()) {
                            Surface(
                                shape = RoundedCornerShape(12.dp),
                                color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.45f),
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Text(
                                    text = stringResource(R.string.public_profile_no_badges),
                                    style = MaterialTheme.typography.bodySmall,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                                    textAlign = TextAlign.Center,
                                    modifier = Modifier.padding(12.dp)
                                )
                            }
                        } else {
                            FlowRow(
                                horizontalArrangement = Arrangement.spacedBy(6.dp),
                                verticalArrangement = Arrangement.spacedBy(6.dp),
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                profile.unlockedBadgeIds.forEach { badgeId ->
                                    Surface(
                                        shape = RoundedCornerShape(10.dp),
                                        color = TealPrimary.copy(alpha = 0.1f),
                                        border = BorderStroke(1.dp, TealPrimary.copy(alpha = 0.25f))
                                    ) {
                                        Row(
                                            verticalAlignment = Alignment.CenterVertically,
                                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 5.dp)
                                        ) {
                                            Icon(
                                                imageVector = badgeVectorIcon(badgeId),
                                                contentDescription = null,
                                                tint = TealPrimary,
                                                modifier = Modifier.size(14.dp)
                                            )
                                            Spacer(modifier = Modifier.width(4.dp))
                                            Text(
                                                text = badgeTitleForId(badgeId),
                                                style = MaterialTheme.typography.labelSmall,
                                                fontWeight = FontWeight.SemiBold,
                                                color = TealPrimary
                                            )
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        },
        confirmButton = {
            Button(
                onClick = onDismiss,
                colors = ButtonDefaults.buttonColors(containerColor = TealPrimary),
                shape = RoundedCornerShape(12.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(stringResource(R.string.common_close), fontWeight = FontWeight.Bold, color = Color.White)
            }
        }
    )
}

@Composable
private fun PublicStatTile(
    icon: ImageVector,
    tint: Color,
    value: String,
    label: String,
    modifier: Modifier = Modifier
) {
    Surface(
        shape = RoundedCornerShape(12.dp),
        color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.48f),
        border = BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.6f)),
        modifier = modifier
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.padding(vertical = 8.dp, horizontal = 4.dp)
        ) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = tint,
                modifier = Modifier.size(16.dp)
            )
            Spacer(modifier = Modifier.height(3.dp))
            Text(
                text = value,
                style = MaterialTheme.typography.titleSmall,
                fontWeight = FontWeight.ExtraBold,
                maxLines = 1
            )
            Text(
                text = label,
                style = MaterialTheme.typography.labelSmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                fontSize = 10.sp,
                maxLines = 1
            )
        }
    }
}
