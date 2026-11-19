package com.example.ui.screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForwardIos
import androidx.compose.material.icons.automirrored.filled.Logout
import androidx.compose.material.icons.filled.Bookmark
import androidx.compose.material.icons.filled.DarkMode
import androidx.compose.material.icons.filled.DeleteForever
import androidx.compose.material.icons.filled.Language
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.RestartAlt
import androidx.compose.material.icons.filled.WorkspacePremium
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.RadioButton
import androidx.compose.material3.RadioButtonDefaults
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
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
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.R
import com.example.ui.components.AppDialog
import com.example.ui.theme.AmberGold
import com.example.ui.theme.CyanAccent
import com.example.ui.theme.ErrorRed
import com.example.ui.theme.TealPrimary
import com.example.util.localization.AppLanguage

@Composable
fun ProfileAchievementsSection(
    certificateCount: Int,
    onNavigateToCertificates: () -> Unit,
    onNavigateToBookmarks: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(modifier = modifier.fillMaxWidth(), verticalArrangement = Arrangement.spacedBy(8.dp)) {
        Text(
            text = stringResource(R.string.profile_achievements_title),
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.onBackground
        )

        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
            border = BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.5f)),
            elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
        ) {
            Column {
                ProfileMenuItem(
                    icon = Icons.Default.WorkspacePremium,
                    iconTint = AmberGold,
                    title = stringResource(R.string.profile_certificates_title),
                    subtitle = "$certificateCount ${stringResource(R.string.profile_certs_count_label)} • ${stringResource(R.string.profile_certificates_subtitle)}",
                    onClick = onNavigateToCertificates
                )

                ProfileMenuItem(
                    icon = Icons.Default.Bookmark,
                    iconTint = MaterialTheme.colorScheme.primary,
                    title = stringResource(R.string.profile_bookmarks_title),
                    subtitle = stringResource(R.string.profile_bookmarks_subtitle),
                    onClick = onNavigateToBookmarks
                )
            }
        }
    }
}

@Composable
fun ProfilePreferencesSection(
    isDark: Boolean,
    currentLanguage: AppLanguage = AppLanguage.INDONESIAN,
    onToggleDarkMode: (Boolean) -> Unit,
    onSelectLanguageClick: () -> Unit = {},
    onNavigateToNotifications: () -> Unit,
    onResetOnboarding: () -> Unit,
    onLogoutClick: () -> Unit,
    onDeleteAccountClick: () -> Unit = {},
    modifier: Modifier = Modifier
) {
    Column(modifier = modifier.fillMaxWidth(), verticalArrangement = Arrangement.spacedBy(8.dp)) {
        Text(
            text = stringResource(R.string.profile_settings_title),
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.onBackground
        )

        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
            border = BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.5f)),
            elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
        ) {
            Column {
                ProfileMenuItem(
                    icon = Icons.Default.Notifications,
                    iconTint = CyanAccent,
                    title = stringResource(R.string.profile_notifications_title),
                    subtitle = stringResource(R.string.profile_notifications_subtitle),
                    onClick = onNavigateToNotifications
                )

                val languageSubtitle = if (currentLanguage.nativeName.equals(currentLanguage.displayName, ignoreCase = true)) {
                    "${currentLanguage.flagEmoji} ${currentLanguage.nativeName}"
                } else {
                    "${currentLanguage.flagEmoji} ${currentLanguage.nativeName} (${currentLanguage.displayName})"
                }
                ProfileMenuItem(
                    icon = Icons.Default.Language,
                    iconTint = Color(0xFF3B82F6),
                    title = stringResource(R.string.profile_language_title),
                    subtitle = languageSubtitle,
                    onClick = onSelectLanguageClick
                )

                // Dark Mode Toggle Row
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(
                        modifier = Modifier
                            .weight(1f)
                            .padding(end = 12.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = Icons.Default.DarkMode,
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.primary,
                            modifier = Modifier.size(24.dp)
                        )
                        Spacer(modifier = Modifier.width(14.dp))
                        Column {
                            Text(
                                text = stringResource(R.string.profile_dark_mode_title),
                                style = MaterialTheme.typography.bodyMedium,
                                fontWeight = FontWeight.SemiBold,
                                maxLines = 1,
                                overflow = androidx.compose.ui.text.style.TextOverflow.Ellipsis
                            )
                            Text(
                                text = if (isDark) stringResource(R.string.profile_dark_mode_active) else stringResource(R.string.profile_light_mode_active),
                                style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.onSurfaceVariant,
                                maxLines = 1,
                                overflow = androidx.compose.ui.text.style.TextOverflow.Ellipsis
                            )
                        }
                    }

                    Switch(
                        checked = isDark,
                        onCheckedChange = { onToggleDarkMode(it) },
                        colors = SwitchDefaults.colors(
                            checkedThumbColor = Color.White,
                            checkedTrackColor = TealPrimary
                        )
                    )
                }

                // Onboarding restart
                ProfileMenuItem(
                    icon = Icons.Default.RestartAlt,
                    iconTint = MaterialTheme.colorScheme.onSurfaceVariant,
                    title = stringResource(R.string.profile_restart_onboarding_title),
                    subtitle = stringResource(R.string.profile_restart_onboarding_subtitle),
                    onClick = onResetOnboarding
                )

                HorizontalDivider(
                    color = MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.4f),
                    modifier = Modifier.padding(horizontal = 16.dp, vertical = 2.dp)
                )

                // Logout item
                ProfileMenuItem(
                    icon = Icons.AutoMirrored.Filled.Logout,
                    iconTint = Color(0xFFEF4444),
                    title = stringResource(R.string.profile_logout_title),
                    subtitle = stringResource(R.string.profile_logout_subtitle),
                    onClick = onLogoutClick
                )
            }
        }

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = stringResource(R.string.profile_danger_zone_title),
            style = MaterialTheme.typography.titleSmall,
            fontWeight = FontWeight.Bold,
            color = ErrorRed
        )

        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = ErrorRed.copy(alpha = 0.05f)),
            border = BorderStroke(1.dp, ErrorRed.copy(alpha = 0.35f)),
            elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
        ) {
            ProfileMenuItem(
                icon = Icons.Default.DeleteForever,
                iconTint = ErrorRed,
                title = stringResource(R.string.profile_delete_account_title),
                subtitle = stringResource(R.string.profile_delete_account_subtitle),
                onClick = onDeleteAccountClick
            )
        }
    }
}

@Composable
fun ProfileMenuItem(
    icon: ImageVector,
    iconTint: Color,
    title: String,
    subtitle: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .clickable { onClick() }
            .padding(16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            imageVector = icon,
            contentDescription = null,
            tint = iconTint,
            modifier = Modifier.size(24.dp)
        )

        Spacer(modifier = Modifier.width(14.dp))

        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = title,
                style = MaterialTheme.typography.bodyMedium,
                fontWeight = FontWeight.SemiBold,
                color = MaterialTheme.colorScheme.onSurface,
                maxLines = 1,
                overflow = androidx.compose.ui.text.style.TextOverflow.Ellipsis
            )
            Text(
                text = subtitle,
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                maxLines = 2,
                overflow = androidx.compose.ui.text.style.TextOverflow.Ellipsis
            )
        }

        Icon(
            imageVector = Icons.AutoMirrored.Filled.ArrowForwardIos,
            contentDescription = null,
            tint = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.4f),
            modifier = Modifier.size(16.dp)
        )
    }
}

@Composable
fun ProfileLogoutDialog(
    onDismiss: () -> Unit,
    onConfirm: () -> Unit
) {
    AppDialog(
        onDismissRequest = onDismiss,
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
                    modifier = Modifier.size(26.dp)
                )
            }
        },
        title = {
            Text(
                text = stringResource(R.string.profile_logout_confirm_title),
                fontWeight = FontWeight.Bold,
                style = MaterialTheme.typography.titleMedium
            )
        },
        text = {
            Text(
                text = stringResource(R.string.profile_logout_confirm_message),
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                textAlign = androidx.compose.ui.text.style.TextAlign.Center,
                lineHeight = 20.sp
            )
        },
        confirmButton = {
            Button(
                onClick = onConfirm,
                shape = RoundedCornerShape(12.dp),
                colors = ButtonDefaults.buttonColors(containerColor = ErrorRed)
            ) {
                Text(stringResource(R.string.profile_logout_title), color = Color.White, fontWeight = FontWeight.Bold)
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

@Composable
fun ProfileDeleteAccountDialog(
    onDismiss: () -> Unit,
    onConfirm: () -> Unit
) {
    var confirmInput by remember { mutableStateOf("") }
    val requiredKeyword = stringResource(R.string.profile_delete_confirm_keyword)
    val isConfirmed = confirmInput.trim().equals(requiredKeyword, ignoreCase = true) ||
        confirmInput.trim().equals("DELETE", ignoreCase = true) ||
        confirmInput.trim().equals("HAPUS", ignoreCase = true)

    AppDialog(
        onDismissRequest = onDismiss,
        icon = {
            Box(
                modifier = Modifier
                    .size(52.dp)
                    .clip(CircleShape)
                    .background(ErrorRed.copy(alpha = 0.15f)),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Default.DeleteForever,
                    contentDescription = null,
                    tint = ErrorRed,
                    modifier = Modifier.size(28.dp)
                )
            }
        },
        title = {
            Text(
                text = stringResource(R.string.profile_delete_account_dialog_title),
                fontWeight = FontWeight.Bold,
                style = MaterialTheme.typography.titleMedium
            )
        },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                Text(
                    text = stringResource(R.string.profile_delete_account_dialog_desc),
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    textAlign = androidx.compose.ui.text.style.TextAlign.Center,
                    lineHeight = 20.sp
                )
                OutlinedTextField(
                    value = confirmInput,
                    onValueChange = { confirmInput = it },
                    label = { Text(stringResource(R.string.profile_delete_confirm_input_label)) },
                    singleLine = true,
                    shape = RoundedCornerShape(12.dp),
                    modifier = Modifier.fillMaxWidth()
                )
            }
        },
        confirmButton = {
            Button(
                onClick = onConfirm,
                enabled = isConfirmed,
                shape = RoundedCornerShape(12.dp),
                colors = ButtonDefaults.buttonColors(containerColor = ErrorRed)
            ) {
                Text(stringResource(R.string.profile_delete_account_confirm), color = Color.White, fontWeight = FontWeight.Bold)
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

@Composable
fun ProfileLanguageDialog(
    currentLanguage: AppLanguage,
    onDismiss: () -> Unit,
    onSelectLanguage: (AppLanguage) -> Unit
) {
    var selectedLanguage by remember { mutableStateOf(currentLanguage) }

    AppDialog(
        onDismissRequest = onDismiss,
        icon = {
            Box(
                modifier = Modifier
                    .size(52.dp)
                    .clip(CircleShape)
                    .background(Color(0xFF3B82F6).copy(alpha = 0.15f)),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Default.Language,
                    contentDescription = null,
                    tint = Color(0xFF3B82F6),
                    modifier = Modifier.size(28.dp)
                )
            }
        },
        title = {
            Text(
                text = stringResource(R.string.profile_language_dialog_title),
                fontWeight = FontWeight.Bold,
                style = MaterialTheme.typography.titleLarge
            )
        },
        text = {
            Column(
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Text(
                    text = stringResource(R.string.profile_language_dialog_subtitle),
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                Spacer(modifier = Modifier.height(4.dp))
                AppLanguage.entries.forEach { lang ->
                    val isSelected = lang == selectedLanguage
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable {
                                selectedLanguage = lang
                            },
                        shape = RoundedCornerShape(12.dp),
                        colors = CardDefaults.cardColors(
                            containerColor = if (isSelected) {
                                MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.45f)
                            } else {
                                MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.35f)
                            }
                        ),
                        border = if (isSelected) {
                            BorderStroke(1.5.dp, TealPrimary)
                        } else {
                            BorderStroke(0.5.dp, MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.3f))
                        },
                        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 14.dp, vertical = 12.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = lang.flagEmoji,
                                style = MaterialTheme.typography.titleLarge,
                                modifier = Modifier.padding(end = 12.dp)
                            )
                            Column(modifier = Modifier.weight(1f)) {
                                Text(
                                    text = lang.nativeName,
                                    style = MaterialTheme.typography.bodyMedium,
                                    fontWeight = if (isSelected) FontWeight.Bold else FontWeight.SemiBold,
                                    color = if (isSelected) TealPrimary else MaterialTheme.colorScheme.onSurface
                                )
                                Text(
                                    text = "${lang.displayName} • ${lang.subLabel}",
                                    style = MaterialTheme.typography.bodySmall,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                            }
                            RadioButton(
                                selected = isSelected,
                                onClick = { selectedLanguage = lang },
                                colors = RadioButtonDefaults.colors(
                                    selectedColor = TealPrimary
                                )
                            )
                        }
                    }
                }
            }
        },
        confirmButton = {
            Button(
                onClick = {
                    onSelectLanguage(selectedLanguage)
                    onDismiss()
                },
                shape = RoundedCornerShape(12.dp),
                colors = ButtonDefaults.buttonColors(containerColor = TealPrimary)
            ) {
                Text(stringResource(R.string.common_apply), color = Color.White, fontWeight = FontWeight.Bold)
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

