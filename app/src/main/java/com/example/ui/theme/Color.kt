package com.example.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.luminance

// Primary CodeAcademy Brand Colors (Distinctive Aegean Pine / Petrol Viridian)
val TealPrimary = Color(0xFF1A6368)
val TealDark = Color(0xFF104246)
val TealLight = Color(0xFF389B9E)
val CyanAccent = Color(0xFFDF7B56) // Warm Coral-Copper accent
val CyanAccentText = Color(0xFFB85A3A) // Higher contrast text for warm cream backgrounds
val CyanLight = Color(0xFFF3AB90)

// Semantic Brand Aliases
val CoralAccent = CyanAccent
val CoralAccentText = CyanAccentText
val CoralLight = CyanLight

// Gamification Warm Accents
val FlameOrange = Color(0xFFD96340)
val FlameLight = Color(0xFFEA8666)
val AmberGold = Color(0xFFD49438)
val PurpleLevel = Color(0xFF7A64A8)
val PurpleDark = Color(0xFF553D80)
val SuccessGreen = Color(0xFF287948)
val ErrorRed = Color(0xFFC43A3A)

// Dark Theme Surfaces (Warm Espresso Charcoal / Warm Obsidian - not cold slate)
val DarkBg = Color(0xFF1A1816)
val DarkSurface = Color(0xFF262320)
val DarkSurfaceVariant = Color(0xFF3B3632)
val DarkTextPrimary = Color(0xFFF8F5F0)
val DarkTextSecondary = Color(0xFFB5ADA4)

// Light Theme Surfaces (Warm Linen & Sand - not cold slate)
val LightBg = Color(0xFFFAF7F2)
val LightSurface = Color(0xFFFBF8F1)
val LightSurfaceVariant = Color(0xFFF1ECE4)
val LightBorder = Color(0xFFE2DDD5)
val LightBorderSubtle = Color(0xFFECE7DF)
val LightTextPrimary = Color(0xFF24211D)
val LightTextSecondary = Color(0xFF6B655D)

// Code Sandbox
val CodeDarkBg = Color(0xFF161514)
val CodeDarkSurface = Color(0xFF201E1C)
val CodeDarkBorder = Color(0xFF3B3733)
val CodeSyntaxKeyword = Color(0xFFE06D53)
val CodeSyntaxString = Color(0xFFE8B058)
val CodeSyntaxTag = Color(0xFF5CB887)
val CodeSyntaxAttr = Color(0xFF6BB5C4)
val CodeSyntaxComment = Color(0xFF968E86)

// WCAG 2.1 AA Accessible Solid Badges / Chips (Dark Mode)
val ChipPemulaBg = Color(0xFF1E3835)
val ChipPemulaText = Color(0xFF6AD3CA)

val ChipMenengahBg = Color(0xFF3B2F22)
val ChipMenengahText = Color(0xFFF0BD7E)

val ChipLanjutBg = Color(0xFF3B2023)
val ChipLanjutText = Color(0xFFF59696)

val ChipNeutralBg = Color(0xFF2E2A26)
val ChipNeutralText = Color(0xFFE5DFD7)

val ChipOrientasiBg = Color(0xFF38322C)
val ChipOrientasiText = Color(0xFFE5DFD7)

// WCAG 2.1 AA Accessible Tonal Badges / Chips (Light Mode)
val ChipPemulaLightBg = Color(0xFFD8F3F0)
val ChipPemulaLightText = Color(0xFF0E4D4A)

val ChipMenengahLightBg = Color(0xFFFAE8D0)
val ChipMenengahLightText = Color(0xFF6B3E08)

val ChipLanjutLightBg = Color(0xFFFADBD9)
val ChipLanjutLightText = Color(0xFF7A1C1C)

val ChipNeutralLightBg = Color(0xFFEBE4DA)
val ChipNeutralLightText = Color(0xFF3E3832)

val ChipOrientasiLightBg = Color(0xFFE8E2D9)
val ChipOrientasiLightText = Color(0xFF3E3832)

@Composable
fun isAppDarkTheme(): Boolean = MaterialTheme.colorScheme.background.luminance() < 0.5f

@Composable
fun adaptiveLevelChipColors(level: String): Pair<Color, Color> {
    val isDark = isAppDarkTheme()
    return when (level.trim().lowercase()) {
        "orientasi" -> if (isDark) ChipOrientasiBg to ChipOrientasiText else ChipOrientasiLightBg to ChipOrientasiLightText
        "pemula" -> if (isDark) ChipPemulaBg to ChipPemulaText else ChipPemulaLightBg to ChipPemulaLightText
        "menengah" -> if (isDark) ChipMenengahBg to ChipMenengahText else ChipMenengahLightBg to ChipMenengahLightText
        "lanjut" -> if (isDark) ChipLanjutBg to ChipLanjutText else ChipLanjutLightBg to ChipLanjutLightText
        else -> adaptiveNeutralChipColors()
    }
}

@Composable
fun adaptiveNeutralChipColors(): Pair<Color, Color> {
    return if (isAppDarkTheme()) {
        ChipNeutralBg to ChipNeutralText
    } else {
        ChipNeutralLightBg to ChipNeutralLightText
    }
}
// Legacy / Thematic Color Compatibility Definitions
val BackgroundDark = DarkBg
val SurfaceDark = DarkSurface
val SurfaceVariantDark = DarkSurfaceVariant
val BorderDark = Color(0xFF3B3733)
val TextPrimaryDark = DarkTextPrimary
val TextSecondaryDark = DarkTextSecondary
val TextMutedDark = Color(0xFF8C847A)
val PrimaryPurple = TealLight
val PrimaryPurpleLight = TealLight
val PrimaryPurpleDark = TealDark
val SecondaryTeal = TealLight
val SecondaryTealDark = TealDark
val CodeBg = CodeDarkBg
val AccentError = ErrorRed
val AccentWarning = FlameOrange
val AccentSuccess = SuccessGreen
val AccentStreak = FlameOrange
val SyntaxString = CodeSyntaxString
val SyntaxFunction = CodeSyntaxTag
val SyntaxNumber = CodeSyntaxKeyword
