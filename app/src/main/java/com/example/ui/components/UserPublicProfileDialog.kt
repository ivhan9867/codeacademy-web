package com.example.ui.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
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
import androidx.compose.material.icons.rounded.AutoAwesome
import androidx.compose.material.icons.rounded.EmojiEvents
import androidx.compose.material.icons.rounded.LocalFireDepartment
import androidx.compose.material.icons.rounded.School
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.remote.PublicUserProfile
import com.example.ui.screens.badgeVectorIcon
import com.example.ui.theme.AmberGold
import com.example.ui.theme.StreakOrange
import com.example.ui.theme.TealPrimary

private fun badgeTitleForId(id: String): String = when (id) {
    "b1" -> "Langkah Pertama"
    "b2" -> "Kode Pertamaku"
    "b3" -> "Rajin 3 Hari"
    "b4" -> "Penjelajah Track"
    "b5" -> "Api Semangat 7"
    "b6" -> "Aktif Diskusi"
    "b7" -> "Skor Sempurna"
    "b8" -> "Level 5"
    "b9" -> "Kolektor 1000 XP"
    "b10" -> "Master Web"
    else -> "Pencapaian"
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun UserPublicProfileDialog(
    profile: PublicUserProfile,
    isLoading: Boolean = false,
    onDismiss: () -> Unit
) {
    val isMentor = profile.role.equals("Fasilitator", ignoreCase = true) ||
        profile.userId == "facilitator-mentor"

    AppDialog(
        onDismissRequest = onDismiss,
        maxWidth = 380.dp
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .fillMaxWidth()
                .padding(20.dp)
        ) {
            if (isLoading) {
                LinearProgressIndicator(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 12.dp),
                    color = TealPrimary
                )
            }

            // Avatar
            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier
                    .size(64.dp)
                    .clip(CircleShape)
                    .background(if (isMentor) AmberGold else TealPrimary)
            ) {
                if (isMentor) {
                    Icon(
                        imageVector = Icons.Rounded.School,
                        contentDescription = null,
                        tint = Color.White,
                        modifier = Modifier.size(32.dp)
                    )
                } else {
                    val initial = profile.name.firstOrNull()?.uppercaseChar()?.toString() ?: "S"
                    Text(
                        text = initial,
                        style = MaterialTheme.typography.headlineSmall,
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            Text(
                text = profile.name,
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(4.dp))

            Surface(
                shape = RoundedCornerShape(999.dp),
                color = if (isMentor) AmberGold.copy(alpha = 0.16f) else TealPrimary.copy(alpha = 0.12f)
            ) {
                Text(
                    text = if (isMentor) "Fasilitator Resmi" else "Level ${profile.level} • Peserta",
                    style = MaterialTheme.typography.labelSmall,
                    fontWeight = FontWeight.Bold,
                    color = if (isMentor) AmberGold else TealPrimary,
                    modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp)
                )
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

            Spacer(modifier = Modifier.height(16.dp))

            // Stats Row
            Row(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                PublicStatTile(
                    icon = Icons.Rounded.AutoAwesome,
                    tint = AmberGold,
                    value = "${profile.totalXp}",
                    label = "Total XP",
                    modifier = Modifier.weight(1f)
                )
                PublicStatTile(
                    icon = Icons.Rounded.LocalFireDepartment,
                    tint = StreakOrange,
                    value = "${profile.currentStreak}h",
                    label = "Streak",
                    modifier = Modifier.weight(1f)
                )
                PublicStatTile(
                    icon = Icons.Rounded.EmojiEvents,
                    tint = TealPrimary,
                    value = "${profile.unlockedBadgeIds.size}",
                    label = "Lencana",
                    modifier = Modifier.weight(1f)
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Unlocked Achievements
            Column(modifier = Modifier.fillMaxWidth()) {
                Text(
                    text = "Pencapaian Terbuka",
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
                            text = "Belum ada lencana yang disinkronkan.",
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

            Spacer(modifier = Modifier.height(18.dp))

            Button(
                onClick = onDismiss,
                colors = ButtonDefaults.buttonColors(containerColor = TealPrimary),
                shape = RoundedCornerShape(12.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Tutup", fontWeight = FontWeight.Bold, color = Color.White)
            }
        }
    }
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
        color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.45f),
        modifier = modifier
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.padding(vertical = 10.dp, horizontal = 6.dp)
        ) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = tint,
                modifier = Modifier.size(18.dp)
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = value,
                style = MaterialTheme.typography.titleSmall,
                fontWeight = FontWeight.Bold
            )
            Text(
                text = label,
                style = MaterialTheme.typography.labelSmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}
