package com.example.ui.screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.isSystemInDarkTheme
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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.LocalFireDepartment
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import com.example.ui.components.AppDialog
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.local.ModuleProgressEntity
import com.example.ui.theme.FlameOrange
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale
import com.example.util.localization.LocalAppLanguage
import com.example.util.localization.toLocale

// Precomputed static shapes
private val BadgeCornerShape = RoundedCornerShape(8.dp)
private val CardShape = RoundedCornerShape(18.dp)

// Precomputed brand accent colors (consistent across themes)
private val HeaderBadgeBg = Color(0x26F97316) // FlameOrange @ 15%
private val HeaderHighlightBg = Color(0x1FF97316) // FlameOrange @ 12%
private val BusiestBorderColor = Color(0xFFFCD34D) // Amber 300 / Gold outline

// Data class for day in contribution heatmap
data class HeatmapDayInfo(
    val dayOffset: Int,
    val formattedDate: String,
    val dayName: String = "",
    val activityCount: Int,
    val intensity: Int, // 0, 1, 2, 3
    val isBusiestDay: Boolean = false
)

/**
 * Returns theme-aware solid colors for heatmap intensity levels (0..4)
 * Level 0 is derived from MaterialTheme surfaceVariant to render clean in both Light and Dark modes.
 */
@Composable
private fun rememberHeatmapCellColors(isDark: Boolean): Array<Color> {
    val emptyCellColor = MaterialTheme.colorScheme.surfaceVariant

    val lowIntensityColor = if (isDark) {
        Color(0xFF9A3412) // Deep Orange
    } else {
        Color(0xFFFED7AA) // Warm light peach/orange in Light theme
    }

    return remember(isDark, emptyCellColor, lowIntensityColor) {
        arrayOf(
            emptyCellColor,       // 0: Inactive (Theme-adaptive surfaceVariant)
            lowIntensityColor,    // 1: Low
            Color(0xFFFB923C),    // 2: Medium (Orange 400)
            Color(0xFFEA580C),    // 3: High (Orange 600)
            FlameOrange           // 4: Busiest / Highest (FlameOrange)
        )
    }
}

@Composable
fun ActivityHeatmapCard(
    progressList: List<ModuleProgressEntity>,
    streakDays: Int,
    onDayClick: (HeatmapDayInfo) -> Unit,
    modifier: Modifier = Modifier
) {
    val isDark = isSystemInDarkTheme()
    val cellColors = rememberHeatmapCellColors(isDark = isDark)
    val onSurfaceVariant = MaterialTheme.colorScheme.onSurfaceVariant
    val cardBorderColor = MaterialTheme.colorScheme.outline.copy(alpha = if (isDark) 0.35f else 0.6f)

    // Generate 12 weeks of historical days (84 days)
    val weeksCount = 12
    val daysCount = weeksCount * 7

    // State for toggling full 12-week history vs compact 7-day view (default: compact)
    var showFullHistory by remember { mutableStateOf(false) }

    val currentLanguage = LocalAppLanguage.current
    val heatmapData = remember(progressList, currentLanguage) {
        val locale = currentLanguage.toLocale()
        val sdf = SimpleDateFormat("EEE, d MMM yyyy", locale)
        val dayNameFormat = SimpleDateFormat("EEEE", locale)
        val keyFormat = SimpleDateFormat("yyyy-MM-dd", java.util.Locale.US)
        val countsByDate = progressList
            .filter { it.isCompleted && it.completedAt > 0L }
            .groupingBy { keyFormat.format(Date(it.completedAt)) }
            .eachCount()
        val maxDailyCount = countsByDate.values.maxOrNull() ?: 0
        val list = ArrayList<HeatmapDayInfo>(daysCount)

        for (i in (daysCount - 1) downTo 0) {
            val cal = Calendar.getInstance().apply {
                add(Calendar.DAY_OF_YEAR, -i)
            }
            val dateKey = keyFormat.format(cal.time)
            val formatted = sdf.format(cal.time)
            val dayName = dayNameFormat.format(cal.time)
            val activityCount = countsByDate[dateKey] ?: 0

            val intensity = when {
                activityCount >= 4 -> 4
                activityCount == 3 -> 3
                activityCount == 2 -> 2
                activityCount == 1 -> 1
                else -> 0
            }

            list.add(
                HeatmapDayInfo(
                    dayOffset = i,
                    formattedDate = formatted,
                    dayName = dayName,
                    activityCount = activityCount,
                    intensity = intensity,
                    isBusiestDay = (maxDailyCount > 0 && activityCount == maxDailyCount)
                )
            )
        }
        list
    }

    val totalContributions = remember(heatmapData) {
        heatmapData.sumOf { it.activityCount }
    }

    val busiestDay = remember(heatmapData) {
        heatmapData.filter { it.activityCount > 0 }.maxByOrNull { it.activityCount }
    }

    Card(
        modifier = modifier.fillMaxWidth(),
        shape = CardShape,
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        border = BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.5f)),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            // Header: Title with Flame Icon & Motivation
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Box(
                            modifier = Modifier
                                .size(28.dp)
                                .clip(CircleShape)
                                .background(HeaderBadgeBg),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.Default.LocalFireDepartment,
                                contentDescription = null,
                                tint = FlameOrange,
                                modifier = Modifier.size(18.dp)
                            )
                        }
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "Peta Semangat Belajarmu",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                    }

                    Spacer(modifier = Modifier.height(6.dp))

                    // Highlight pencapaian hari tersibuk
                    Surface(
                        color = HeaderHighlightBg,
                        shape = BadgeCornerShape
                    ) {
                        Row(
                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = if (busiestDay != null && busiestDay.activityCount > 0) {
                                    "Hari tersibuk: ${busiestDay.dayName}, ${busiestDay.activityCount} modul diselesaikan"
                                } else {
                                    "Selesaikan modul pertamamu untuk menyalakan peta semangat!"
                                },
                                style = MaterialTheme.typography.labelMedium,
                                fontWeight = FontWeight.SemiBold,
                                color = FlameOrange
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(4.dp))

                    Text(
                        text = "$totalContributions modul diselesaikan dalam 3 bulan terakhir",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }

            Spacer(modifier = Modifier.height(14.dp))

            if (!showFullHistory) {
                // Mode Ringkas (Default): 7 Hari Terakhir
                Compact7DaysHeatmap(
                    heatmapData = heatmapData,
                    cellColors = cellColors,
                    onDayClick = onDayClick
                )
            } else {
                // Mode Penuh: Single Canvas 12 Minggu (84 Sel dalam 1 Draw Pass)
                Full12WeeksHeatmapCanvas(
                    heatmapData = heatmapData,
                    weeksCount = weeksCount,
                    cellColors = cellColors,
                    onDayClick = onDayClick
                )
            }

            Spacer(modifier = Modifier.height(8.dp))

            // Toggle Button: Beralih antara mode 7 hari vs riwayat 12 minggu
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(8.dp))
                    .clickable { showFullHistory = !showFullHistory }
                    .padding(vertical = 6.dp),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = if (showFullHistory) "Tutup riwayat 12 minggu ↑" else "Lihat riwayat 12 minggu →",
                    style = MaterialTheme.typography.labelMedium,
                    color = MaterialTheme.colorScheme.primary,
                    fontWeight = FontWeight.SemiBold
                )
            }

            Spacer(modifier = Modifier.height(6.dp))

            // Legend row: "Kurang" -> Solid Color Boxes -> "Lebih"
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Ketuk kotak untuk melihat detail",
                    style = MaterialTheme.typography.labelSmall,
                    color = onSurfaceVariant,
                    fontSize = 10.sp
                )

                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    Text(
                        text = "Kurang",
                        fontSize = 10.sp,
                        color = onSurfaceVariant
                    )
                    Spacer(modifier = Modifier.width(2.dp))

                    for (colorIndex in 0..4) {
                        Box(
                            modifier = Modifier
                                .size(12.dp)
                                .clip(RoundedCornerShape(3.dp))
                                .background(cellColors[colorIndex])
                        )
                    }

                    Spacer(modifier = Modifier.width(2.dp))
                    Text(
                        text = "Lebih",
                        fontSize = 10.sp,
                        color = onSurfaceVariant
                    )
                }
            }
        }
    }
}

/**
 * Mode Ringkas: Menampilkan 7 hari terakhir dalam satu baris dengan label hari dan jumlah aktivitas.
 */
@Composable
private fun Compact7DaysHeatmap(
    heatmapData: List<HeatmapDayInfo>,
    cellColors: Array<Color>,
    onDayClick: (HeatmapDayInfo) -> Unit
) {
    val onSurfaceVariant = MaterialTheme.colorScheme.onSurfaceVariant
    val last7Days = remember(heatmapData) {
        heatmapData.takeLast(7)
    }

    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        last7Days.forEach { day ->
            val isBusiest = day.isBusiestDay && day.activityCount > 0
            val level = if (isBusiest) 4 else day.intensity.coerceIn(0, 3)
            val cellColor = cellColors[level]

            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier
                    .weight(1f)
                    .clip(RoundedCornerShape(8.dp))
                    .clickable(
                        interactionSource = remember { MutableInteractionSource() },
                        indication = null
                    ) { onDayClick(day) }
                    .padding(vertical = 4.dp)
            ) {
                Text(
                    text = day.dayName.take(3),
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Medium,
                    color = onSurfaceVariant
                )
                Spacer(modifier = Modifier.height(6.dp))
                Canvas(modifier = Modifier.size(28.dp)) {
                    val cornerRadiusPx = 6.dp.toPx()
                    drawRoundRect(
                        color = cellColor,
                        cornerRadius = CornerRadius(cornerRadiusPx, cornerRadiusPx)
                    )
                    if (isBusiest) {
                        drawRoundRect(
                            color = BusiestBorderColor,
                            cornerRadius = CornerRadius(cornerRadiusPx, cornerRadiusPx),
                            style = Stroke(width = 1.5.dp.toPx())
                        )
                    }
                }
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = if (day.activityCount > 0) "${day.activityCount}" else "-",
                    fontSize = 11.sp,
                    fontWeight = if (day.activityCount > 0) FontWeight.Bold else FontWeight.Normal,
                    color = if (day.activityCount > 0) FlameOrange else onSurfaceVariant
                )
            }
        }
    }
}

/**
 * Mode Penuh: Menggambar seluruh grid 84 sel (12 minggu x 7 hari) dalam SATU Canvas draw pass.
 * Menggunakan detectTapGestures untuk mendeteksi sel yang ditekan berdasarkan koordinat (x, y).
 */
@Composable
private fun Full12WeeksHeatmapCanvas(
    heatmapData: List<HeatmapDayInfo>,
    weeksCount: Int,
    cellColors: Array<Color>,
    onDayClick: (HeatmapDayInfo) -> Unit
) {
    val density = LocalDensity.current
    val onSurfaceVariant = MaterialTheme.colorScheme.onSurfaceVariant
    val cellSizeDp = 20.dp
    val spacingDp = 3.dp
    val dayLabels = remember { listOf("Min", "Sen", "Sel", "Rab", "Kam", "Jum", "Sab") }

    val cellSizePx = remember(density) { with(density) { cellSizeDp.toPx() } }
    val spacingPx = remember(density) { with(density) { spacingDp.toPx() } }
    val cornerRadiusPx = remember(density) { with(density) { 4.dp.toPx() } }
    val borderWidthPx = remember(density) { with(density) { 1.5.dp.toPx() } }

    val totalWidthDp = (weeksCount * 20 + (weeksCount - 1) * 3).dp
    val totalHeightDp = (7 * 20 + 6 * 3).dp

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .horizontalScroll(rememberScrollState()),
        verticalAlignment = Alignment.CenterVertically
    ) {
        // Fixed day of week labels on the left
        Column(
            verticalArrangement = Arrangement.spacedBy(3.dp),
            modifier = Modifier.padding(end = 6.dp)
        ) {
            for (dayIdx in 0..6) {
                Box(
                    modifier = Modifier.size(width = 20.dp, height = 20.dp),
                    contentAlignment = Alignment.Center
                ) {
                    if (dayIdx == 1 || dayIdx == 3 || dayIdx == 5) {
                        Text(
                            text = dayLabels[dayIdx],
                            fontSize = 9.sp,
                            color = onSurfaceVariant,
                            fontWeight = FontWeight.SemiBold
                        )
                    }
                }
            }
        }

        // Single Canvas Composable rendering all 84 cells in a single draw pass
        Canvas(
            modifier = Modifier
                .width(totalWidthDp)
                .height(totalHeightDp)
                .pointerInput(heatmapData) {
                    detectTapGestures { offset ->
                        val col = (offset.x / (cellSizePx + spacingPx)).toInt()
                        val row = (offset.y / (cellSizePx + spacingPx)).toInt()
                        if (col in 0 until weeksCount && row in 0..6) {
                            val cellX = col * (cellSizePx + spacingPx)
                            val cellY = row * (cellSizePx + spacingPx)
                            if (offset.x >= cellX && offset.x <= cellX + cellSizePx &&
                                offset.y >= cellY && offset.y <= cellY + cellSizePx
                            ) {
                                val idx = col * 7 + row
                                if (idx in heatmapData.indices) {
                                    onDayClick(heatmapData[idx])
                                }
                            }
                        }
                    }
                }
        ) {
            for (col in 0 until weeksCount) {
                for (row in 0..6) {
                    val idx = col * 7 + row
                    if (idx in heatmapData.indices) {
                        val day = heatmapData[idx]
                        val isBusiest = day.isBusiestDay && day.activityCount > 0
                        val level = if (isBusiest) 4 else day.intensity.coerceIn(0, 3)
                        val color = cellColors[level]

                        val x = col * (cellSizePx + spacingPx)
                        val y = row * (cellSizePx + spacingPx)

                        drawRoundRect(
                            color = color,
                            topLeft = Offset(x, y),
                            size = Size(cellSizePx, cellSizePx),
                            cornerRadius = CornerRadius(cornerRadiusPx, cornerRadiusPx)
                        )

                        if (isBusiest) {
                            drawRoundRect(
                                color = BusiestBorderColor,
                                topLeft = Offset(x, y),
                                size = Size(cellSizePx, cellSizePx),
                                cornerRadius = CornerRadius(cornerRadiusPx, cornerRadiusPx),
                                style = Stroke(width = borderWidthPx)
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun HeatmapDayDetailDialog(
    dayInfo: HeatmapDayInfo,
    onDismiss: () -> Unit
) {
    val emptyIconColor = MaterialTheme.colorScheme.onSurfaceVariant
    AppDialog(
        onDismissRequest = onDismiss,
        icon = {
            Box(
                modifier = Modifier
                    .size(56.dp)
                    .clip(CircleShape)
                    .background(
                        if (dayInfo.activityCount > 0) FlameOrange.copy(alpha = 0.15f) else MaterialTheme.colorScheme.surfaceVariant
                    ),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Default.LocalFireDepartment,
                    contentDescription = null,
                    tint = if (dayInfo.activityCount > 0) FlameOrange else emptyIconColor,
                    modifier = Modifier.size(30.dp)
                )
            }
        },
        title = {
            Text(
                text = dayInfo.formattedDate,
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center
            )
        },
        text = {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                if (dayInfo.isBusiestDay && dayInfo.activityCount > 0) {
                    Surface(
                        color = HeaderBadgeBg,
                        shape = BadgeCornerShape,
                        modifier = Modifier.padding(bottom = 10.dp)
                    ) {
                        Text(
                            text = "Hari Tersibuk!",
                            color = FlameOrange,
                            fontWeight = FontWeight.Bold,
                            fontSize = 12.sp,
                            modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp)
                        )
                    }
                }
                Text(
                    text = if (dayInfo.activityCount > 0) {
                        "${dayInfo.activityCount} modul diselesaikan (+${dayInfo.activityCount * 20} XP)"
                    } else {
                        "Tidak ada aktivitas belajar tercatat pada hari ini."
                    },
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurface,
                    textAlign = TextAlign.Center,
                    lineHeight = 20.sp
                )
            }
        },
        confirmButton = {
            Button(
                onClick = onDismiss,
                shape = RoundedCornerShape(12.dp),
                colors = ButtonDefaults.buttonColors(containerColor = FlameOrange)
            ) {
                Text("Mengerti", color = Color.White, fontWeight = FontWeight.Bold)
            }
        }
    )
}
