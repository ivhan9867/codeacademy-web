package com.example.ui.screens.onboarding

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Bolt
import androidx.compose.material.icons.rounded.Code
import androidx.compose.material.icons.rounded.Dashboard
import androidx.compose.material.icons.rounded.Verified
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.luminance
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.TealPrimary

private data class RoadmapStage(
    val stepNumber: String,
    val title: String,
    val subtitle: String,
    val meta: String,
    val icon: ImageVector,
    val accentBg: Color,
    val accentTint: Color
)

@Composable
fun RoadmapStep(
    modifier: Modifier = Modifier
) {
    val isDark = MaterialTheme.colorScheme.background.luminance() < 0.5f
    val cardBg = if (isDark) MaterialTheme.colorScheme.surface else Color.White
    val borderCol = if (isDark) MaterialTheme.colorScheme.outlineVariant else Color(0xFFD5E3E2)

    val stages = listOf(
        RoadmapStage(
            stepNumber = "01",
            title = "Fondasi Web: HTML & CSS Dasar",
            subtitle = "Struktur halaman semantik, formulir, box model, dan tipografi.",
            meta = "106 Modul • 5 Kuis Bab",
            icon = Icons.Rounded.Code,
            accentBg = if (isDark) TealPrimary.copy(alpha = 0.2f) else Color(0xFFD9EEEC),
            accentTint = if (isDark) Color(0xFF9DE3DF) else Color(0xFF104246)
        ),
        RoadmapStage(
            stepNumber = "02",
            title = "CSS Layout Modern",
            subtitle = "Tata letak responsif 1D Flexbox, 2D CSS Grid, dan media query.",
            meta = "24 Modul • 2 Kuis Bab",
            icon = Icons.Rounded.Dashboard,
            accentBg = if (isDark) Color(0xFF33271D) else Color(0xFFFDF0EB),
            accentTint = Color(0xFFE26D45)
        ),
        RoadmapStage(
            stepNumber = "03",
            title = "JavaScript Fundamentals",
            subtitle = "Logika ES6, struktur data array/objek, dan manipulasi DOM.",
            meta = "26 Modul • 2 Kuis Bab",
            icon = Icons.Rounded.Bolt,
            accentBg = if (isDark) Color(0xFF2E2A1A) else Color(0xFFFEF4E6),
            accentTint = Color(0xFFB45309)
        ),
        RoadmapStage(
            stepNumber = "04",
            title = "Proyek Akhir & Sertifikat",
            subtitle = "Kirim proyek dari Playground untuk dinilai fasilitator & terverifikasi online.",
            meta = "Cloud Sync • ID Kredensial",
            icon = Icons.Rounded.Verified,
            accentBg = if (isDark) Color(0xFF1B3324) else Color(0xFFE9F6EE),
            accentTint = Color(0xFF1F7A45)
        )
    )

    Column(
        modifier = modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(horizontal = 4.dp, vertical = 8.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = "Peta Perjalanan Belajarmu",
            style = MaterialTheme.typography.headlineSmall,
            fontWeight = FontWeight.ExtraBold,
            color = MaterialTheme.colorScheme.onBackground,
            textAlign = TextAlign.Center
        )
        Spacer(modifier = Modifier.height(6.dp))
        Text(
            text = "Kurikulum dirancang berjenjang untuk membawamu dari nol sampai siap membangun website responsif & interaktif.",
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            textAlign = TextAlign.Center,
            lineHeight = 20.sp
        )

        Spacer(modifier = Modifier.height(18.dp))

        Column(
            verticalArrangement = Arrangement.spacedBy(10.dp),
            modifier = Modifier.fillMaxWidth()
        ) {
            stages.forEach { stage ->
                Surface(
                    shape = RoundedCornerShape(14.dp),
                    color = cardBg,
                    border = BorderStroke(1.dp, borderCol),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Row(
                        modifier = Modifier.padding(14.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Box(
                            modifier = Modifier
                                .size(42.dp)
                                .clip(CircleShape)
                                .background(stage.accentBg),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = stage.icon,
                                contentDescription = null,
                                tint = stage.accentTint,
                                modifier = Modifier.size(22.dp)
                            )
                        }
                        Spacer(modifier = Modifier.width(12.dp))
                        Column(modifier = Modifier.weight(1f)) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(
                                    text = "TAHAP ${stage.stepNumber}",
                                    style = MaterialTheme.typography.labelSmall,
                                    fontWeight = FontWeight.Bold,
                                    color = stage.accentTint
                                )
                                Text(
                                    text = stage.meta,
                                    style = MaterialTheme.typography.labelSmall,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                            }
                            Spacer(modifier = Modifier.height(2.dp))
                            Text(
                                text = stage.title,
                                style = MaterialTheme.typography.titleSmall,
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.onSurface
                            )
                            Text(
                                text = stage.subtitle,
                                style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.onSurfaceVariant,
                                lineHeight = 17.sp
                            )
                        }
                    }
                }
            }
        }
    }
}
