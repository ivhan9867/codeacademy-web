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
import androidx.compose.material.icons.rounded.Code
import androidx.compose.material.icons.rounded.Devices
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
import com.example.ui.components.CodeAcademyMascot
import com.example.ui.components.MascotMood
import com.example.ui.theme.TealPrimary

@Composable
fun WelcomeStep(
    userName: String = "",
    areAnimationsEnabled: Boolean = true,
    modifier: Modifier = Modifier
) {
    val isDark = MaterialTheme.colorScheme.background.luminance() < 0.5f
    val pillBg = if (isDark) TealPrimary.copy(alpha = 0.2f) else Color(0xFFD9EEEC)
    val pillText = if (isDark) Color(0xFF9DE3DF) else Color(0xFF104246)
    val cardBg = if (isDark) MaterialTheme.colorScheme.surface else Color.White
    val borderCol = if (isDark) MaterialTheme.colorScheme.outlineVariant else Color(0xFFD5E3E2)

    Column(
        modifier = modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(horizontal = 4.dp, vertical = 8.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Surface(
            shape = RoundedCornerShape(999.dp),
            color = pillBg
        ) {
            Text(
                text = "CodeAcademy • Web Development",
                style = MaterialTheme.typography.labelMedium,
                fontWeight = FontWeight.Bold,
                color = pillText,
                modifier = Modifier.padding(horizontal = 14.dp, vertical = 6.dp)
            )
        }

        Spacer(modifier = Modifier.height(18.dp))

        CodeAcademyMascot(
            size = 172.dp,
            mood = MascotMood.HAPPY,
            showWebBadges = true
        )

        Spacer(modifier = Modifier.height(18.dp))

        Text(
            text = if (userName.isNotBlank()) {
                "Selamat datang, $userName"
            } else {
                "Belajar Web Dev dengan Tenang"
            },
            style = MaterialTheme.typography.headlineSmall,
            fontWeight = FontWeight.ExtraBold,
            color = MaterialTheme.colorScheme.onBackground,
            textAlign = TextAlign.Center
        )

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = "Kuasai HTML semantik, layout CSS modern, dan JavaScript interaktif langkah demi langkah langsung dari ponselmu.",
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            textAlign = TextAlign.Center,
            lineHeight = 22.sp,
            modifier = Modifier.padding(horizontal = 10.dp)
        )

        Spacer(modifier = Modifier.height(22.dp))

        Column(
            verticalArrangement = Arrangement.spacedBy(10.dp),
            modifier = Modifier.fillMaxWidth()
        ) {
            WelcomeHighlightRow(
                icon = Icons.Rounded.Code,
                title = "Materi Terstruktur & Ringkas",
                subtitle = "156 modul terbagi jelas dari nol hingga proyek nyata",
                cardBg = cardBg,
                borderCol = borderCol,
                iconBg = pillBg,
                iconTint = pillText
            )
            WelcomeHighlightRow(
                icon = Icons.Rounded.Devices,
                title = "Playground & AI Tutor Terintegrasi",
                subtitle = "Tulis kode HTML/CSS/JS dan lihat hasilnya seketika",
                cardBg = cardBg,
                borderCol = borderCol,
                iconBg = pillBg,
                iconTint = pillText
            )
            WelcomeHighlightRow(
                icon = Icons.Rounded.Verified,
                title = "Penilaian Fasilitator & Sertifikat",
                subtitle = "Proyek akhir ditinjau langsung dengan ID verifikasi online",
                cardBg = cardBg,
                borderCol = borderCol,
                iconBg = pillBg,
                iconTint = pillText
            )
        }
    }
}

@Composable
private fun WelcomeHighlightRow(
    icon: ImageVector,
    title: String,
    subtitle: String,
    cardBg: Color,
    borderCol: Color,
    iconBg: Color,
    iconTint: Color
) {
    Surface(
        shape = RoundedCornerShape(14.dp),
        color = cardBg,
        border = BorderStroke(1.dp, borderCol),
        modifier = Modifier.fillMaxWidth()
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 14.dp, vertical = 12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(38.dp)
                    .clip(CircleShape)
                    .background(iconBg),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = icon,
                    contentDescription = null,
                    tint = iconTint,
                    modifier = Modifier.size(20.dp)
                )
            }
            Spacer(modifier = Modifier.width(12.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = title,
                    style = MaterialTheme.typography.titleSmall,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSurface
                )
                Text(
                    text = subtitle,
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }
    }
}
