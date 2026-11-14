package com.example.ui.screens.onboarding

import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.Code
import androidx.compose.material.icons.filled.RocketLaunch
import androidx.compose.material.icons.filled.School
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.components.CodeAcademyMascot
import com.example.ui.components.MascotMood
import com.example.ui.theme.AmberGold
import com.example.ui.theme.CodeDarkBg
import com.example.ui.theme.CodeDarkBorder
import com.example.ui.theme.CodeDarkSurface
import com.example.ui.theme.CodeSyntaxKeyword
import com.example.ui.theme.CodeSyntaxString
import com.example.ui.theme.CodeSyntaxTag
import com.example.ui.theme.CyanAccent
import com.example.ui.theme.FlameOrange
import com.example.ui.theme.SuccessGreen
import com.example.ui.theme.TealDark
import com.example.ui.theme.TealLight
import com.example.ui.theme.TealPrimary

/**
 * Step 1: Welcome / Nilai Jual Utama.
 * Clean, inspiring visual with an oversized dominant illustration and punchy copy.
 */
@Composable
fun WelcomeStep(
    userName: String = "",
    areAnimationsEnabled: Boolean = true,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(horizontal = 8.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.SpaceBetween
    ) {
        // Dominant Hero Illustration (>= 42% screen height)
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
                .padding(vertical = 12.dp),
            contentAlignment = Alignment.Center
        ) {
            CodeAcademyMascot(
                size = 210.dp,
                mood = MascotMood.HAPPY,
                showWebBadges = true,
                areAnimationsEnabled = areAnimationsEnabled
            )
        }

        // Headline & Subtext (max 2 lines each)
        val title = if (userName.isNotBlank()) {
            "Selamat Datang, $userName!"
        } else {
            "Kuasai Web Dev dari Nol"
        }

        OnboardingStepHeader(
            title = title,
            subtitle = "Belajar HTML, CSS, dan JavaScript secara interaktif langkah demi langkah untuk membangun web pertamamu.",
            modifier = Modifier.padding(bottom = 16.dp)
        )
    }
}

/**
 * Editorial Compose Canvas & Vector Hero Illustration for Welcome Step.
 */
@Composable
private fun WelcomeHeroIllustration(
    areAnimationsEnabled: Boolean,
    modifier: Modifier = Modifier
) {
    val infiniteTransition = rememberInfiniteTransition(label = "welcome_infinite")
    val floatAnim by if (areAnimationsEnabled) {
        infiniteTransition.animateFloat(
            initialValue = -6f,
            targetValue = 6f,
            animationSpec = infiniteRepeatable(
                animation = tween(2400, easing = FastOutSlowInEasing),
                repeatMode = RepeatMode.Reverse
            ),
            label = "welcome_float"
        )
    } else {
        androidx.compose.runtime.remember { androidx.compose.runtime.mutableFloatStateOf(0f) }
    }

    BoxWithConstraints(
        modifier = modifier,
        contentAlignment = Alignment.Center
    ) {
        val width = maxWidth
        val height = maxHeight

        // Ambient glow backdrop
        Canvas(modifier = Modifier.fillMaxSize()) {
            val center = Offset(size.width / 2f, size.height * 0.48f)
            val glowRadius = size.minDimension * 0.46f

            // Soft radial glow
            drawCircle(
                brush = Brush.radialGradient(
                    colors = listOf(
                        TealLight.copy(alpha = 0.22f),
                        CyanAccent.copy(alpha = 0.08f),
                        Color.Transparent
                    ),
                    center = center,
                    radius = glowRadius * 1.3f
                ),
                center = center,
                radius = glowRadius * 1.3f
            )

            // Geometric orbiting ring
            drawCircle(
                color = TealPrimary.copy(alpha = 0.12f),
                center = center,
                radius = glowRadius * 0.95f,
                style = Stroke(
                    width = 2.dp.toPx(),
                    pathEffect = androidx.compose.ui.graphics.PathEffect.dashPathEffect(floatArrayOf(16f, 12f), 0f)
                )
            )

            // Sparkle accents
            drawCircle(color = CyanAccent.copy(alpha = 0.6f), center = Offset(size.width * 0.18f, size.height * 0.22f), radius = 4.dp.toPx())
            drawCircle(color = AmberGold.copy(alpha = 0.7f), center = Offset(size.width * 0.82f, size.height * 0.28f), radius = 5.dp.toPx())
            drawCircle(color = TealLight.copy(alpha = 0.6f), center = Offset(size.width * 0.16f, size.height * 0.70f), radius = 4.5.dp.toPx())
        }

        // Central Code Terminal & Learning Hub Card
        Surface(
            modifier = Modifier
                .fillMaxWidth(0.86f)
                .offset(y = floatAnim.dp)
                .shadow(16.dp, RoundedCornerShape(20.dp), ambientColor = TealPrimary.copy(alpha = 0.2f), spotColor = TealPrimary.copy(alpha = 0.3f)),
            shape = RoundedCornerShape(20.dp),
            color = CodeDarkBg,
            border = androidx.compose.foundation.BorderStroke(1.dp, CodeDarkBorder)
        ) {
            Column(
                modifier = Modifier.padding(18.dp)
            ) {
                // Window top bar
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                        Box(modifier = Modifier.size(10.dp).clip(CircleShape).background(FlameOrange))
                        Box(modifier = Modifier.size(10.dp).clip(CircleShape).background(AmberGold))
                        Box(modifier = Modifier.size(10.dp).clip(CircleShape).background(SuccessGreen))
                    }

                    Text(
                        text = "main.kt",
                        style = MaterialTheme.typography.labelSmall,
                        color = Color(0xFF968E86),
                        fontFamily = FontFamily.Monospace
                    )

                    Icon(
                        imageVector = Icons.Default.AutoAwesome,
                        contentDescription = null,
                        tint = AmberGold,
                        modifier = Modifier.size(14.dp)
                    )
                }

                Spacer(modifier = Modifier.height(14.dp))

                // Realistic interactive code preview lines
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        text = "1 ",
                        style = MaterialTheme.typography.bodySmall,
                        color = Color(0xFF5A544E),
                        fontFamily = FontFamily.Monospace
                    )
                    Text(
                        text = "fun ",
                        style = MaterialTheme.typography.bodySmall,
                        color = CodeSyntaxKeyword,
                        fontWeight = FontWeight.Bold,
                        fontFamily = FontFamily.Monospace
                    )
                    Text(
                        text = "mulaiBelajar() {",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurface,
                        fontFamily = FontFamily.Monospace
                    )
                }

                Spacer(modifier = Modifier.height(4.dp))

                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        text = "2   ",
                        style = MaterialTheme.typography.bodySmall,
                        color = Color(0xFF5A544E),
                        fontFamily = FontFamily.Monospace
                    )
                    Text(
                        text = "val ",
                        style = MaterialTheme.typography.bodySmall,
                        color = CodeSyntaxKeyword,
                        fontWeight = FontWeight.Bold,
                        fontFamily = FontFamily.Monospace
                    )
                    Text(
                        text = "skill = ",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurface,
                        fontFamily = FontFamily.Monospace
                    )
                    Text(
                        text = "\"Masa Depan\"",
                        style = MaterialTheme.typography.bodySmall,
                        color = CodeSyntaxString,
                        fontFamily = FontFamily.Monospace
                    )
                }

                Spacer(modifier = Modifier.height(4.dp))

                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        text = "3   ",
                        style = MaterialTheme.typography.bodySmall,
                        color = Color(0xFF5A544E),
                        fontFamily = FontFamily.Monospace
                    )
                    Text(
                        text = "levelUp(skill)",
                        style = MaterialTheme.typography.bodySmall,
                        color = CodeSyntaxTag,
                        fontFamily = FontFamily.Monospace
                    )
                }

                Spacer(modifier = Modifier.height(4.dp))

                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        text = "4 ",
                        style = MaterialTheme.typography.bodySmall,
                        color = Color(0xFF5A544E),
                        fontFamily = FontFamily.Monospace
                    )
                    Text(
                        text = "}",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurface,
                        fontFamily = FontFamily.Monospace
                    )
                }

                Spacer(modifier = Modifier.height(14.dp))

                // Output pill
                Surface(
                    shape = RoundedCornerShape(8.dp),
                    color = CodeDarkSurface,
                    border = androidx.compose.foundation.BorderStroke(1.dp, TealPrimary.copy(alpha = 0.4f)),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Row(
                        modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Box(
                            modifier = Modifier
                                .size(8.dp)
                                .clip(CircleShape)
                                .background(SuccessGreen)
                        )
                        Text(
                            text = "Output: Siap jadi software engineer! 🎉",
                            style = MaterialTheme.typography.labelSmall,
                            color = SuccessGreen,
                            fontWeight = FontWeight.SemiBold
                        )
                    }
                }
            }
        }

        // Floating Badge 1 (Top Left): "Praktik Langsung"
        Surface(
            shape = RoundedCornerShape(12.dp),
            color = MaterialTheme.colorScheme.surface,
            shadowElevation = 6.dp,
            border = androidx.compose.foundation.BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant),
            modifier = Modifier
                .align(Alignment.TopStart)
                .offset(x = 12.dp, y = 20.dp - (floatAnim * 0.7f).dp)
        ) {
            Row(
                modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.Code,
                    contentDescription = null,
                    tint = TealPrimary,
                    modifier = Modifier.size(16.dp)
                )
                Text(
                    text = "Praktik Langsung",
                    style = MaterialTheme.typography.labelSmall,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSurface
                )
            }
        }

        // Floating Badge 2 (Bottom Right): "Sertifikat Resmi"
        Surface(
            shape = RoundedCornerShape(12.dp),
            color = MaterialTheme.colorScheme.surface,
            shadowElevation = 6.dp,
            border = androidx.compose.foundation.BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant),
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .offset(x = (-12).dp, y = (-16).dp + (floatAnim * 0.8f).dp)
        ) {
            Row(
                modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.School,
                    contentDescription = null,
                    tint = AmberGold,
                    modifier = Modifier.size(16.dp)
                )
                Text(
                    text = "Sertifikat Resmi",
                    style = MaterialTheme.typography.labelSmall,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSurface
                )
            }
        }
    }
}
