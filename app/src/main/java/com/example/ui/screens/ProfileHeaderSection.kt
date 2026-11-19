package com.example.ui.screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.rounded.AutoAwesome
import androidx.compose.material.icons.rounded.PushPin
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.R
import com.example.data.CourseCatalog
import com.example.data.models.BadgeItem
import com.example.data.models.GamificationLevels
import com.example.ui.components.AppDialog
import com.example.ui.theme.AmberGold
import com.example.ui.theme.CustomProfileAvatar
import com.example.ui.theme.CyanAccentText
import com.example.ui.theme.FlameOrange
import com.example.ui.theme.ProfileCustomizationCatalog
import com.example.ui.theme.PurpleLevel
import com.example.ui.theme.TealPrimary

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun ProfileIdentityCard(
    name: String,
    email: String,
    bio: String,
    totalXp: Int,
    streak: Int,
    certificateCount: Int,
    onEditClick: () -> Unit,
    modifier: Modifier = Modifier,
    bannerTheme: String = "teal_ocean",
    avatarStyle: String = "initial",
    avatarFrame: String = "classic",
    headlineTitle: String = "WebDev Learner",
    skillTags: List<String> = emptyList(),
    featuredBadgeId: String = ""
) {
    val levelInfo = remember(totalXp) { GamificationLevels.getLevelForXp(totalXp) }
    val bannerPreset = remember(bannerTheme) { ProfileCustomizationCatalog.bannerFor(bannerTheme) }
    val cleanName = remember(name) { com.example.util.NameUtils.sanitizeDisplayName(name) }
    val featuredBadge = remember(featuredBadgeId) {
        if (featuredBadgeId.isBlank()) null
        else CourseCatalog.allBadges.firstOrNull { it.id == featuredBadgeId }
    }

    Card(
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(22.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        ),
        border = BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.55f)),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
    ) {
        Column {
            // 1. Custom Gradient Cover Banner
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(82.dp)
                    .background(bannerPreset.toBrush())
                    .padding(horizontal = 16.dp, vertical = 12.dp)
            ) {
                Surface(
                    color = Color.Black.copy(alpha = 0.25f),
                    shape = RoundedCornerShape(999.dp),
                    modifier = Modifier.align(Alignment.TopStart)
                ) {
                    Text(
                        text = headlineTitle.ifBlank { "WebDev Learner" },
                        color = bannerPreset.accentColor,
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                        modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp)
                    )
                }

                Surface(
                    onClick = onEditClick,
                    color = Color.White.copy(alpha = 0.20f),
                    shape = RoundedCornerShape(999.dp),
                    modifier = Modifier.align(Alignment.TopEnd)
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.padding(horizontal = 10.dp, vertical = 5.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Edit,
                            contentDescription = "Edit Profil",
                            tint = Color.White,
                            modifier = Modifier.size(14.dp)
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            text = stringResource(R.string.common_edit),
                            color = Color.White,
                            fontSize = 11.sp,
                            fontWeight = FontWeight.SemiBold
                        )
                    }
                }
            }

            Column(modifier = Modifier.padding(horizontal = 18.dp, vertical = 0.dp)) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .offset(y = (-26).dp),
                    verticalAlignment = Alignment.Bottom
                ) {
                    CustomProfileAvatar(
                        name = cleanName,
                        avatarStyle = avatarStyle,
                        avatarFrame = avatarFrame,
                        bannerTheme = bannerTheme,
                        size = 72.dp
                    )

                    Spacer(modifier = Modifier.width(14.dp))

                    Column(
                        modifier = Modifier
                            .weight(1f)
                            .padding(top = 28.dp)
                    ) {
                        Text(
                            text = cleanName,
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.onSurface,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )
                        Text(
                            text = email,
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )
                    }
                }

                // Level & Featured Badge Showcase Row
                FlowRow(
                    horizontalArrangement = Arrangement.spacedBy(6.dp),
                    verticalArrangement = Arrangement.spacedBy(6.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .offset(y = (-12).dp)
                ) {
                    Surface(
                        color = PurpleLevel.copy(alpha = 0.15f),
                        shape = RoundedCornerShape(999.dp)
                    ) {
                        Text(
                            text = "Lv. ${levelInfo.level} • ${com.example.util.localization.DataLabelMappings.xpLevelName(levelInfo.level, levelInfo.name)}",
                            color = PurpleLevel,
                            fontWeight = FontWeight.Bold,
                            fontSize = 11.sp,
                            modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp)
                        )
                    }

                    if (featuredBadge != null) {
                        Surface(
                            color = AmberGold.copy(alpha = 0.15f),
                            border = BorderStroke(1.dp, AmberGold.copy(alpha = 0.35f)),
                            shape = RoundedCornerShape(999.dp)
                        ) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                modifier = Modifier.padding(horizontal = 9.dp, vertical = 4.dp)
                            ) {
                                Icon(
                                    imageVector = badgeVectorIcon(featuredBadge.id),
                                    contentDescription = null,
                                    tint = AmberGold,
                                    modifier = Modifier.size(13.dp)
                                )
                                Spacer(modifier = Modifier.width(4.dp))
                                Text(
                                    text = com.example.util.localization.DataLabelMappings.badgeName(featuredBadge.id, featuredBadge.name),
                                    color = AmberGold,
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 11.sp
                                )
                            }
                        }
                    }

                    skillTags.take(3).forEach { tag ->
                        Surface(
                            color = bannerPreset.startColor.copy(alpha = 0.10f),
                            border = BorderStroke(1.dp, bannerPreset.endColor.copy(alpha = 0.25f)),
                            shape = RoundedCornerShape(999.dp)
                        ) {
                            Text(
                                text = tag,
                                fontSize = 10.5.sp,
                                fontWeight = FontWeight.SemiBold,
                                color = MaterialTheme.colorScheme.onSurface,
                                modifier = Modifier.padding(horizontal = 9.dp, vertical = 4.dp)
                            )
                        }
                    }
                }

                Text(
                    text = "\"$bio\"",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    fontStyle = FontStyle.Italic,
                    maxLines = 3,
                    overflow = TextOverflow.Ellipsis,
                    modifier = Modifier.padding(bottom = 14.dp)
                )

                // 3 Kolom Stat Ringkas
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 18.dp)
                        .clip(RoundedCornerShape(14.dp))
                        .background(MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f))
                        .padding(12.dp),
                    horizontalArrangement = Arrangement.SpaceAround
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text(
                            text = "$totalXp",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold,
                            color = AmberGold
                        )
                        Text(
                            text = stringResource(R.string.profile_total_xp_label),
                            style = MaterialTheme.typography.labelSmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text(
                            text = "$streak",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold,
                            color = FlameOrange
                        )
                        Text(
                            text = stringResource(R.string.profile_streak_label),
                            style = MaterialTheme.typography.labelSmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text(
                            text = "$certificateCount",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold,
                            color = CyanAccentText
                        )
                        Text(
                            text = stringResource(R.string.profile_certs_count_label),
                            style = MaterialTheme.typography.labelSmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun ProfileEditDialog(
    initialName: String,
    initialBio: String,
    onDismiss: () -> Unit,
    onSave: (String, String) -> Unit,
    initialBannerTheme: String = "teal_ocean",
    initialAvatarStyle: String = "initial",
    initialAvatarFrame: String = "classic",
    initialHeadlineTitle: String = "WebDev Learner",
    initialSkillTags: List<String> = emptyList(),
    initialFeaturedBadgeId: String = "",
    unlockedBadges: List<BadgeItem> = emptyList(),
    onSaveFull: ((String, String, String, String, String, String, List<String>, String) -> Unit)? = null
) {
    var editName by remember { mutableStateOf(initialName) }
    var editBio by remember { mutableStateOf(initialBio.take(100)) }
    var editBanner by remember { mutableStateOf(initialBannerTheme.ifBlank { "teal_ocean" }) }
    var editAvatarStyle by remember { mutableStateOf(initialAvatarStyle.ifBlank { "initial" }) }
    var editFrame by remember { mutableStateOf(initialAvatarFrame.ifBlank { "classic" }) }
    var editHeadline by remember { mutableStateOf(initialHeadlineTitle.ifBlank { "WebDev Learner" }.take(32)) }
    var editSkills by remember { mutableStateOf(initialSkillTags.take(3)) }
    var editFeaturedBadge by remember { mutableStateOf(initialFeaturedBadgeId) }

    val trimmedName = editName.trim()
    val sanitizedName = com.example.util.NameUtils.sanitizeDisplayName(trimmedName)
    val isNameValid = trimmedName.length >= 2 &&
        !trimmedName.first().isDigit() &&
        sanitizedName.length >= 2
    val hasChanges = trimmedName != initialName.trim() ||
        editBio.trim() != initialBio.trim() ||
        editBanner != initialBannerTheme ||
        editAvatarStyle != initialAvatarStyle ||
        editFrame != initialAvatarFrame ||
        editHeadline.trim() != initialHeadlineTitle.trim() ||
        editSkills != initialSkillTags ||
        editFeaturedBadge != initialFeaturedBadgeId
    val canSave = isNameValid && hasChanges

    val previewBanner = ProfileCustomizationCatalog.bannerFor(editBanner)

    AppDialog(
        onDismissRequest = onDismiss,
        title = {
            Text(
                text = stringResource(R.string.profile_edit_title),
                fontWeight = FontWeight.Bold,
                style = MaterialTheme.typography.titleMedium
            )
        },
        text = {
            Column(
                verticalArrangement = Arrangement.spacedBy(12.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .heightIn(max = 460.dp)
                    .verticalScroll(rememberScrollState())
            ) {
                // Live Preview Mini Identity Card
                Surface(
                    shape = RoundedCornerShape(16.dp),
                    color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.45f),
                    border = BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.6f)),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(46.dp)
                                .background(previewBanner.toBrush())
                                .padding(horizontal = 12.dp, vertical = 8.dp)
                        ) {
                            Text(
                                text = editHeadline.trim().ifBlank { "WebDev Learner" },
                                color = previewBanner.accentColor,
                                fontSize = 10.5.sp,
                                fontWeight = FontWeight.Bold,
                                maxLines = 1,
                                overflow = TextOverflow.Ellipsis,
                                modifier = Modifier.align(Alignment.CenterEnd)
                            )
                        }
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.padding(horizontal = 12.dp, vertical = 10.dp)
                        ) {
                            CustomProfileAvatar(
                                name = sanitizedName.ifBlank { "S" },
                                avatarStyle = editAvatarStyle,
                                avatarFrame = editFrame,
                                bannerTheme = editBanner,
                                size = 48.dp
                            )
                            Spacer(modifier = Modifier.width(12.dp))
                            Column(modifier = Modifier.weight(1f)) {
                                Text(
                                    text = sanitizedName.ifBlank { "Peserta" },
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 14.sp,
                                    maxLines = 1,
                                    overflow = TextOverflow.Ellipsis
                                )
                                if (editSkills.isNotEmpty()) {
                                    Text(
                                        text = editSkills.joinToString(" • "),
                                        fontSize = 11.sp,
                                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                                        maxLines = 1,
                                        overflow = TextOverflow.Ellipsis
                                    )
                                }
                            }
                        }
                    }
                }

                // Nama & Bio
                OutlinedTextField(
                    value = editName,
                    onValueChange = { if (it.length <= 40) editName = it },
                    label = { Text(stringResource(R.string.profile_edit_name_label)) },
                    isError = !isNameValid,
                    supportingText = if (!isNameValid) {
                        { Text(stringResource(R.string.profile_edit_name_error)) }
                    } else null,
                    singleLine = true,
                    shape = RoundedCornerShape(12.dp),
                    modifier = Modifier.fillMaxWidth()
                )

                OutlinedTextField(
                    value = editHeadline,
                    onValueChange = { if (it.length <= 32) editHeadline = it },
                    label = { Text(stringResource(R.string.profile_edit_headline_label)) },
                    placeholder = { Text(stringResource(R.string.profile_edit_headline_placeholder)) },
                    singleLine = true,
                    shape = RoundedCornerShape(12.dp),
                    modifier = Modifier.fillMaxWidth()
                )

                LazyRow(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                    items(ProfileCustomizationCatalog.headlinePresets) { preset ->
                        FilterChip(
                            selected = editHeadline == preset,
                            onClick = { editHeadline = preset },
                            label = { Text(preset, fontSize = 11.sp) }
                        )
                    }
                }

                OutlinedTextField(
                    value = editBio,
                    onValueChange = { if (it.length <= 100) editBio = it },
                    label = { Text(stringResource(R.string.profile_edit_bio_label)) },
                    supportingText = {
                        Text(
                            text = stringResource(R.string.profile_edit_bio_counter_format, editBio.length),
                            modifier = Modifier.fillMaxWidth(),
                            textAlign = TextAlign.End
                        )
                    },
                    maxLines = 3,
                    shape = RoundedCornerShape(12.dp),
                    modifier = Modifier.fillMaxWidth()
                )

                // Banner Theme Selector
                Text(
                    text = stringResource(R.string.profile_edit_banner_label),
                    style = MaterialTheme.typography.labelMedium,
                    fontWeight = FontWeight.Bold
                )
                LazyRow(
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    contentPadding = PaddingValues(vertical = 2.dp)
                ) {
                    items(ProfileCustomizationCatalog.bannerThemes) { theme ->
                        val selected = editBanner == theme.id
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally,
                            modifier = Modifier
                                .clip(RoundedCornerShape(12.dp))
                                .clickable { editBanner = theme.id }
                                .padding(4.dp)
                        ) {
                            Box(
                                contentAlignment = Alignment.Center,
                                modifier = Modifier
                                    .size(44.dp)
                                    .clip(CircleShape)
                                    .background(theme.toBrush())
                                    .border(
                                        width = if (selected) 2.5.dp else 1.dp,
                                        color = if (selected) MaterialTheme.colorScheme.primary else Color.White.copy(alpha = 0.4f),
                                        shape = CircleShape
                                    )
                            ) {
                                if (selected) {
                                    Icon(
                                        imageVector = Icons.Default.Check,
                                        contentDescription = null,
                                        tint = Color.White,
                                        modifier = Modifier.size(18.dp)
                                    )
                                }
                            }
                            Spacer(modifier = Modifier.height(3.dp))
                            Text(theme.label, fontSize = 10.sp, maxLines = 1)
                        }
                    }
                }

                // Avatar Icon Selector
                Text(
                    text = stringResource(R.string.profile_edit_avatar_label),
                    style = MaterialTheme.typography.labelMedium,
                    fontWeight = FontWeight.Bold
                )
                LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    items(ProfileCustomizationCatalog.avatarStyles) { styleItem ->
                        val selected = editAvatarStyle == styleItem.id
                        Surface(
                            onClick = { editAvatarStyle = styleItem.id },
                            shape = RoundedCornerShape(12.dp),
                            color = if (selected) TealPrimary.copy(alpha = 0.14f) else MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.4f),
                            border = BorderStroke(
                                width = if (selected) 1.5.dp else 1.dp,
                                color = if (selected) TealPrimary else MaterialTheme.colorScheme.outlineVariant
                            )
                        ) {
                            Column(
                                horizontalAlignment = Alignment.CenterHorizontally,
                                modifier = Modifier.padding(horizontal = 10.dp, vertical = 7.dp)
                            ) {
                                CustomProfileAvatar(
                                    name = sanitizedName.ifBlank { "S" },
                                    avatarStyle = styleItem.id,
                                    avatarFrame = editFrame,
                                    bannerTheme = editBanner,
                                    size = 34.dp
                                )
                                Spacer(modifier = Modifier.height(3.dp))
                                Text(styleItem.label, fontSize = 10.sp, fontWeight = FontWeight.Medium)
                            }
                        }
                    }
                }

                // Avatar Frame Selector
                Text(
                    text = stringResource(R.string.profile_edit_frame_label),
                    style = MaterialTheme.typography.labelMedium,
                    fontWeight = FontWeight.Bold
                )
                LazyRow(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                    items(ProfileCustomizationCatalog.avatarFrames) { frameItem ->
                        FilterChip(
                            selected = editFrame == frameItem.id,
                            onClick = { editFrame = frameItem.id },
                            label = { Text(frameItem.label, fontSize = 11.sp) }
                        )
                    }
                }

                // Skill Focus Tags (Max 3)
                Text(
                    text = stringResource(R.string.profile_edit_skills_label_format, editSkills.size),
                    style = MaterialTheme.typography.labelMedium,
                    fontWeight = FontWeight.Bold
                )
                FlowRow(
                    horizontalArrangement = Arrangement.spacedBy(6.dp),
                    verticalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    ProfileCustomizationCatalog.skillTagOptions.forEach { tag ->
                        val isSelected = tag in editSkills
                        FilterChip(
                            selected = isSelected,
                            onClick = {
                                editSkills = if (isSelected) {
                                    editSkills - tag
                                } else if (editSkills.size < 3) {
                                    editSkills + tag
                                } else {
                                    editSkills.drop(1) + tag
                                }
                            },
                            label = { Text(tag, fontSize = 11.sp) },
                            colors = FilterChipDefaults.filterChipColors(
                                selectedContainerColor = TealPrimary,
                                selectedLabelColor = Color.White
                            )
                        )
                    }
                }

                // Pin Featured Unlocked Badge
                if (unlockedBadges.isNotEmpty()) {
                    Text(
                        text = stringResource(R.string.profile_edit_featured_badge_label),
                        style = MaterialTheme.typography.labelMedium,
                        fontWeight = FontWeight.Bold
                    )
                    LazyRow(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                        item {
                            FilterChip(
                                selected = editFeaturedBadge.isBlank(),
                                onClick = { editFeaturedBadge = "" },
                                label = { Text(stringResource(R.string.profile_edit_featured_badge_none), fontSize = 11.sp) }
                            )
                        }
                        items(unlockedBadges, key = { it.id }) { badge ->
                            val selected = editFeaturedBadge == badge.id
                            FilterChip(
                                selected = selected,
                                onClick = { editFeaturedBadge = badge.id },
                                leadingIcon = {
                                    Icon(
                                        imageVector = badgeVectorIcon(badge.id),
                                        contentDescription = null,
                                        modifier = Modifier.size(14.dp)
                                    )
                                },
                                label = {
                                    Text(
                                        com.example.util.localization.DataLabelMappings.badgeName(badge.id, badge.name),
                                        fontSize = 11.sp
                                    )
                                }
                            )
                        }
                    }
                }
            }
        },
        confirmButton = {
            Button(
                onClick = {
                    val finalHeadline = editHeadline.trim().ifBlank { "WebDev Learner" }
                    if (onSaveFull != null) {
                        onSaveFull(
                            sanitizedName,
                            editBio.trim(),
                            editBanner,
                            editAvatarStyle,
                            editFrame,
                            finalHeadline,
                            editSkills,
                            editFeaturedBadge
                        )
                    } else {
                        onSave(sanitizedName, editBio.trim())
                    }
                    onDismiss()
                },
                enabled = canSave,
                shape = RoundedCornerShape(12.dp),
                colors = ButtonDefaults.buttonColors(containerColor = TealPrimary)
            ) {
                Text(stringResource(R.string.common_save), color = Color.White, fontWeight = FontWeight.Bold)
            }
        },
        dismissButton = {
            OutlinedButton(
                onClick = onDismiss,
                shape = RoundedCornerShape(12.dp),
                border = BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant)
            ) {
                Text(stringResource(R.string.common_cancel))
            }
        }
    )
}
