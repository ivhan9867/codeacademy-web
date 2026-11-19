package com.example.ui.theme

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Bolt
import androidx.compose.material.icons.rounded.Code
import androidx.compose.material.icons.rounded.Palette
import androidx.compose.material.icons.rounded.Psychology
import androidx.compose.material.icons.rounded.RocketLaunch
import androidx.compose.material.icons.rounded.School
import androidx.compose.material.icons.rounded.Terminal
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.components.CodeAcademyMascot
import com.example.ui.components.MascotMood
import com.example.util.NameUtils

data class BannerThemePreset(
    val id: String,
    val label: String,
    val startColor: Color,
    val endColor: Color,
    val accentColor: Color
) {
    fun toBrush(): Brush = Brush.linearGradient(colors = listOf(startColor, endColor))
}

data class AvatarStylePreset(
    val id: String,
    val label: String,
    val icon: ImageVector?
)

data class AvatarFramePreset(
    val id: String,
    val label: String,
    val primaryColor: Color,
    val secondaryColor: Color? = null,
    val strokeWidth: Dp = 2.5.dp
)

object ProfileCustomizationCatalog {
    val bannerThemes: List<BannerThemePreset> = listOf(
        BannerThemePreset("teal_ocean", "Oceanic", Color(0xFF0F3D40), Color(0xFF1A6368), Color(0xFF5EEAD4)),
        BannerThemePreset("midnight_cyber", "Midnight", Color(0xFF181A1F), Color(0xFF312E81), Color(0xFFA5B4FC)),
        BannerThemePreset("lime_spark", "Huddly Lime", Color(0xFF181A1F), Color(0xFF3F6212), Color(0xFFC7F938)),
        BannerThemePreset("sunset_amber", "Warm Studio", Color(0xFF9A3412), Color(0xFFEA580C), Color(0xFFFDE68A)),
        BannerThemePreset("aurora_purple", "Nebula", Color(0xFF4C1D95), Color(0xFF0F766E), Color(0xFFF0ABFC)),
        BannerThemePreset("emerald_mint", "Mint Matrix", Color(0xFF064E3B), Color(0xFF0369A1), Color(0xFF6EE7B7))
    )

    val avatarStyles: List<AvatarStylePreset> = listOf(
        AvatarStylePreset("initial", "Inisial", null),
        AvatarStylePreset("terminal", "Terminal", Icons.Rounded.Terminal),
        AvatarStylePreset("code", "Code", Icons.Rounded.Code),
        AvatarStylePreset("bolt", "Spark", Icons.Rounded.Bolt),
        AvatarStylePreset("rocket", "Roket", Icons.Rounded.RocketLaunch),
        AvatarStylePreset("palette", "Desain", Icons.Rounded.Palette),
        AvatarStylePreset("psychology", "Logic", Icons.Rounded.Psychology),
        AvatarStylePreset("codi", "Codi", null)
    )

    val avatarFrames: List<AvatarFramePreset> = listOf(
        AvatarFramePreset("classic", "Klasik", Color.White, null, 2.dp),
        AvatarFramePreset("glow_teal", "Teal Glow", Color(0xFF2DD4BF), null, 3.dp),
        AvatarFramePreset("gold_ring", "Emas", Color(0xFFF59E0B), null, 3.dp),
        AvatarFramePreset("lime_neon", "Lime Neon", Color(0xFFC7F938), Color(0xFF181A1F), 3.dp),
        AvatarFramePreset("double_ring", "Nebula", Color(0xFFA855F7), Color(0xFF38BDF8), 3.dp)
    )

    val headlinePresets: List<String> = listOf(
        "WebDev Learner",
        "HTML & CSS Builder",
        "JS Craftsperson",
        "UI Layout Geek",
        "Frontend Explorer",
        "Code Builder"
    )

    val skillTagOptions: List<String> = listOf(
        "HTML5",
        "CSS Flexbox",
        "CSS Grid",
        "JavaScript",
        "DOM API",
        "UI Design",
        "Responsive",
        "Debugging"
    )

    fun bannerFor(id: String?): BannerThemePreset =
        bannerThemes.firstOrNull { it.id == id } ?: bannerThemes.first()

    fun frameFor(id: String?): AvatarFramePreset =
        avatarFrames.firstOrNull { it.id == id } ?: avatarFrames.first()

    fun avatarStyleFor(id: String?): AvatarStylePreset =
        avatarStyles.firstOrNull { it.id == id } ?: avatarStyles.first()

    fun parseSkillTags(raw: String?): List<String> =
        raw.orEmpty()
            .split(",")
            .map { it.trim() }
            .filter { it.isNotBlank() }
            .distinct()
            .take(3)
}

@Composable
fun CustomProfileAvatar(
    name: String,
    avatarStyle: String,
    avatarFrame: String,
    bannerTheme: String,
    isMentor: Boolean = false,
    size: Dp = 64.dp,
    modifier: Modifier = Modifier
) {
    val banner = ProfileCustomizationCatalog.bannerFor(bannerTheme)
    val frame = ProfileCustomizationCatalog.frameFor(avatarFrame)
    val stylePreset = ProfileCustomizationCatalog.avatarStyleFor(avatarStyle)
    val cleanName = NameUtils.sanitizeDisplayName(name)
    val initial = NameUtils.extractInitial(cleanName)

    val frameBrush = if (isMentor) {
        Brush.linearGradient(listOf(AmberGold, Color(0xFFFDE68A)))
    } else if (frame.secondaryColor != null) {
        Brush.linearGradient(listOf(frame.primaryColor, frame.secondaryColor))
    } else {
        Brush.linearGradient(listOf(frame.primaryColor, frame.primaryColor))
    }

    val innerBgBrush = if (isMentor) {
        Brush.linearGradient(listOf(AmberGold, FlameOrange))
    } else {
        banner.toBrush()
    }

    Box(
        contentAlignment = Alignment.Center,
        modifier = modifier
            .size(size)
            .clip(CircleShape)
            .border(BorderStroke(frame.strokeWidth, frameBrush), CircleShape)
            .padding(frame.strokeWidth)
            .clip(CircleShape)
            .background(innerBgBrush)
    ) {
        when {
            isMentor -> {
                Icon(
                    imageVector = Icons.Rounded.School,
                    contentDescription = null,
                    tint = Color.White,
                    modifier = Modifier.size(size * 0.48f)
                )
            }
            avatarStyle == "codi" -> {
                CodeAcademyMascot(
                    mood = MascotMood.HAPPY,
                    size = size * 0.78f,
                    modifier = Modifier.fillMaxSize().padding(4.dp)
                )
            }
            stylePreset.icon != null -> {
                Icon(
                    imageVector = stylePreset.icon,
                    contentDescription = null,
                    tint = Color.White,
                    modifier = Modifier.size(size * 0.48f)
                )
            }
            else -> {
                Text(
                    text = initial,
                    fontSize = (size.value * 0.42f).sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )
            }
        }
    }
}
