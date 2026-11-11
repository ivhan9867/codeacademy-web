package com.example.ui.screens.onboarding

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
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
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccessTime
import androidx.compose.material.icons.filled.CalendarToday
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Flag
import androidx.compose.material.icons.filled.LocalFireDepartment
import androidx.compose.material.icons.filled.MenuBook
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.CourseCatalog
import com.example.ui.theme.Dimens
import com.example.ui.theme.FlameOrange
import com.example.ui.theme.SuccessGreen
import com.example.ui.theme.TealPrimary

/**
 * Step 5: Target Belajar Harian & Ringkasan.
 * Lets user pick daily time commitment, learning objective, and shows a crisp personalized roadmap preview.
 */
@OptIn(ExperimentalLayoutApi::class)
@Composable
fun PlanSummaryStep(
    selectedTrack: String,
    selectedGoal: String,
    selectedDailyMinutes: Int,
    onSelectGoal: (String) -> Unit,
    onSelectMinutes: (Int) -> Unit,
    modifier: Modifier = Modifier
) {
    val goals = remember { listOf("Karier", "Skill Baru", "Hobi / Proyek") }
    val minuteOptions = remember {
        listOf(
            10 to "Santai",
            20 to "Ideal",
            30 to "Serius",
            45 to "Intensif"
        )
    }

    val matchedTrack = remember(selectedTrack) {
        CourseCatalog.tracks.firstOrNull { it.id == selectedTrack } ?: CourseCatalog.tracks.first()
    }

    val firstCourse = remember(selectedTrack) {
        CourseCatalog.courses.firstOrNull { it.trackId == selectedTrack }
    }

    val totalReadingMinutes = remember(firstCourse) {
        if (firstCourse != null) {
            val modulesSum = firstCourse.modules.sumOf { it.readingMinutes }
            if (modulesSum > 0) modulesSum else firstCourse.durationMinutes
        } else {
            0
        }
    }

    val estimatedDays = remember(totalReadingMinutes, selectedDailyMinutes) {
        if (totalReadingMinutes > 0 && selectedDailyMinutes > 0) {
            ((totalReadingMinutes + selectedDailyMinutes - 1) / selectedDailyMinutes).coerceAtLeast(1)
        } else {
            3
        }
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(horizontal = 8.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.SpaceBetween
    ) {
        // Main content area with scroll for smaller devices
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
                .verticalScroll(rememberScrollState())
                .padding(vertical = 4.dp),
            verticalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            // 1. Time commitment chips
            Column(modifier = Modifier.fillMaxWidth()) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.AccessTime,
                        contentDescription = null,
                        tint = TealPrimary,
                        modifier = Modifier.size(18.dp)
                    )
                    Text(
                        text = "Waktu Belajar Harian",
                        style = MaterialTheme.typography.titleSmall,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                }

                Spacer(modifier = Modifier.height(8.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    minuteOptions.forEach { (minutes, label) ->
                        val isSelected = selectedDailyMinutes == minutes
                        Surface(
                            shape = Dimens.CardDefaultShape,
                            color = if (isSelected) TealPrimary.copy(alpha = 0.12f) else MaterialTheme.colorScheme.surface,
                            border = BorderStroke(
                                width = if (isSelected) 2.dp else 1.dp,
                                color = if (isSelected) TealPrimary else MaterialTheme.colorScheme.outlineVariant
                            ),
                            modifier = Modifier
                                .weight(1f)
                                .height(58.dp)
                                .clip(Dimens.CardDefaultShape)
                                .clickable { onSelectMinutes(minutes) }
                                .testTag("time_chip_$minutes")
                        ) {
                            Column(
                                modifier = Modifier
                                    .fillMaxSize()
                                    .padding(vertical = 6.dp),
                                horizontalAlignment = Alignment.CenterHorizontally,
                                verticalArrangement = Arrangement.Center
                            ) {
                                Text(
                                    text = "$minutes mnt",
                                    style = MaterialTheme.typography.labelLarge,
                                    fontWeight = FontWeight.Bold,
                                    color = if (isSelected) TealPrimary else MaterialTheme.colorScheme.onSurface
                                )
                                Text(
                                    text = label,
                                    style = MaterialTheme.typography.labelSmall,
                                    fontSize = 10.sp,
                                    color = if (isSelected) TealPrimary else MaterialTheme.colorScheme.onSurfaceVariant
                                )
                            }
                        }
                    }
                }
            }

            // 2. Goal selector
            Column(modifier = Modifier.fillMaxWidth()) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.Flag,
                        contentDescription = null,
                        tint = TealPrimary,
                        modifier = Modifier.size(18.dp)
                    )
                    Text(
                        text = "Tujuan Utama",
                        style = MaterialTheme.typography.titleSmall,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                }

                Spacer(modifier = Modifier.height(8.dp))

                FlowRow(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    goals.forEach { goal ->
                        val isSelected = selectedGoal == goal
                        FilterChip(
                            selected = isSelected,
                            onClick = { onSelectGoal(goal) },
                            label = {
                                Text(
                                    text = goal,
                                    style = MaterialTheme.typography.labelMedium,
                                    fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium
                                )
                            },
                            shape = RoundedCornerShape(10.dp),
                            colors = FilterChipDefaults.filterChipColors(
                                selectedContainerColor = TealPrimary.copy(alpha = 0.15f),
                                selectedLabelColor = TealPrimary
                            ),
                            border = BorderStroke(
                                width = 1.dp,
                                color = if (isSelected) TealPrimary else MaterialTheme.colorScheme.outlineVariant
                            ),
                            modifier = Modifier.height(38.dp)
                        )
                    }
                }
            }

            // 3. Crisp Personalized Summary Card
            Card(
                shape = Dimens.CardDefaultShape,
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                border = BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(
                    modifier = Modifier.padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "Ringkasan Rencanamu",
                            style = MaterialTheme.typography.labelLarge,
                            fontWeight = FontWeight.Bold,
                            color = TealPrimary
                        )

                        Surface(
                            shape = RoundedCornerShape(12.dp),
                            color = FlameOrange.copy(alpha = 0.12f)
                        ) {
                            Row(
                                modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(4.dp)
                            ) {
                                Icon(
                                    imageVector = Icons.Default.LocalFireDepartment,
                                    contentDescription = null,
                                    tint = FlameOrange,
                                    modifier = Modifier.size(14.dp)
                                )
                                Text(
                                    text = "+50 XP Bonus",
                                    style = MaterialTheme.typography.labelSmall,
                                    fontWeight = FontWeight.Bold,
                                    color = FlameOrange
                                )
                            }
                        }
                    }

                    // Selected Track Row
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Text(text = matchedTrack.iconEmoji, fontSize = 18.sp)
                        Column {
                            Text(
                                text = matchedTrack.name,
                                style = MaterialTheme.typography.titleSmall,
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.onSurface
                            )
                            if (firstCourse != null) {
                                Text(
                                    text = "Kelas awal: ${firstCourse.title}",
                                    style = MaterialTheme.typography.labelSmall,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                            }
                        }
                    }

                    // Target Days
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.CalendarToday,
                            contentDescription = null,
                            tint = TealPrimary,
                            modifier = Modifier.size(16.dp)
                        )
                        Text(
                            text = "Target selesai kelas pertama: ~$estimatedDays hari ($selectedDailyMinutes menit/hari)",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }
            }
        }

        // Headline & Subtext (max 2 lines each)
        OnboardingStepHeader(
            title = "Rencanamu Siap!",
            subtitle = "Komitmen harian teratur membawa hasil maksimal. Kamu bisa mengubah target kapan saja di profil.",
            modifier = Modifier.padding(bottom = 16.dp)
        )
    }
}
