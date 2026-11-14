package com.example.ui.components

import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.keyframes
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
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
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

enum class MascotMood {
    HAPPY,
    CODING,
    WAVING
}

/**
 * Animated WebDev Mascot ("Codi") for CodeAcademy.
 * Features gentle idle levitation, natural eye blinking, glowing antenna pulse,
 * and optional orbiting HTML / CSS / JS pills.
 */
@Composable
fun CodeAcademyMascot(
    modifier: Modifier = Modifier,
    size: Dp = 140.dp,
    mood: MascotMood = MascotMood.HAPPY,
    showWebBadges: Boolean = false,
    areAnimationsEnabled: Boolean = true
) {
    val infiniteTransition = rememberInfiniteTransition(label = "mascot_idle")

    // Gentle floating bobbing animation
    val floatOffsetDp by if (areAnimationsEnabled) {
        infiniteTransition.animateFloat(
            initialValue = -5f,
            targetValue = 5f,
            animationSpec = infiniteRepeatable(
                animation = tween(durationMillis = 2200, easing = FastOutSlowInEasing),
                repeatMode = RepeatMode.Reverse
            ),
            label = "mascot_float"
        )
    } else {
        androidx.compose.runtime.remember { androidx.compose.runtime.mutableFloatStateOf(0f) }
    }

    // Antenna glow pulse
    val glowAlpha by if (areAnimationsEnabled) {
        infiniteTransition.animateFloat(
            initialValue = 0.25f,
            targetValue = 0.75f,
            animationSpec = infiniteRepeatable(
                animation = tween(durationMillis = 1400, easing = FastOutSlowInEasing),
                repeatMode = RepeatMode.Reverse
            ),
            label = "antenna_glow"
        )
    } else {
        androidx.compose.runtime.remember { androidx.compose.runtime.mutableFloatStateOf(0.5f) }
    }

    // Natural eye blink every ~3.8 seconds
    val eyeScaleY by if (areAnimationsEnabled) {
        infiniteTransition.animateFloat(
            initialValue = 1f,
            targetValue = 1f,
            animationSpec = infiniteRepeatable(
                animation = keyframes {
                    durationMillis = 3800
                    1f at 0
                    1f at 3400
                    0.12f at 3520
                    1f at 3640
                    1f at 3800
                },
                repeatMode = RepeatMode.Restart
            ),
            label = "eye_blink"
        )
    } else {
        androidx.compose.runtime.remember { androidx.compose.runtime.mutableFloatStateOf(1f) }
    }

    Box(
        modifier = modifier.size(size),
        contentAlignment = Alignment.Center
    ) {
        // Subtle Ambient Aura Behind Mascot
        Box(
            modifier = Modifier
                .size(size * 0.88f)
                .clip(CircleShape)
                .background(
                    Brush.radialGradient(
                        colors = listOf(
                            Color(0xFF389B9E).copy(alpha = 0.22f),
                            Color(0xFF1A6368).copy(alpha = 0.06f),
                            Color.Transparent
                        )
                    )
                )
        )

        // Main Vector Mascot Canvas
        Canvas(
            modifier = Modifier
                .size(size * 0.82f)
                .offset(y = floatOffsetDp.dp)
        ) {
            val w = this.size.width
            val h = this.size.height

            val navyDark = Color(0xFF104246)
            val tealPrimary = Color(0xFF1A6368)
            val tealLight = Color(0xFF389B9E)
            val coralAccent = Color(0xFFDF7B56)
            val visorBg = Color(0xFF0F172A)
            val cyanGlow = Color(0xFF2DD4BF)

            // 1. Antenna Glow & Stem
            val antennaX = w * 0.5f
            val antennaTipY = h * 0.09f
            drawCircle(
                color = coralAccent.copy(alpha = glowAlpha * 0.45f),
                radius = w * 0.095f,
                center = Offset(antennaX, antennaTipY)
            )
            drawLine(
                color = navyDark,
                start = Offset(antennaX, antennaTipY),
                end = Offset(antennaX, h * 0.22f),
                strokeWidth = w * 0.045f,
                cap = StrokeCap.Round
            )
            drawCircle(
                color = coralAccent,
                radius = w * 0.052f,
                center = Offset(antennaX, antennaTipY)
            )

            // 2. Side Headphones / Ears
            val earWidth = w * 0.09f
            val earHeight = h * 0.22f
            val earTop = h * 0.35f
            drawRoundRect(
                color = coralAccent,
                topLeft = Offset(w * 0.11f, earTop),
                size = Size(earWidth, earHeight),
                cornerRadius = CornerRadius(w * 0.05f, w * 0.05f)
            )
            drawRoundRect(
                color = coralAccent,
                topLeft = Offset(w * 0.80f, earTop),
                size = Size(earWidth, earHeight),
                cornerRadius = CornerRadius(w * 0.05f, w * 0.05f)
            )

            // 3. Head Shell
            val headLeft = w * 0.18f
            val headTop = h * 0.20f
            val headWidth = w * 0.64f
            val headHeight = h * 0.50f
            drawRoundRect(
                brush = Brush.verticalGradient(
                    colors = listOf(tealLight, tealPrimary, navyDark),
                    startY = headTop,
                    endY = headTop + headHeight
                ),
                topLeft = Offset(headLeft, headTop),
                size = Size(headWidth, headHeight),
                cornerRadius = CornerRadius(w * 0.14f, w * 0.14f)
            )

            // 4. Dark Visor Screen
            val visorLeft = w * 0.24f
            val visorTop = h * 0.27f
            val visorWidth = w * 0.52f
            val visorHeight = h * 0.34f
            drawRoundRect(
                color = visorBg,
                topLeft = Offset(visorLeft, visorTop),
                size = Size(visorWidth, visorHeight),
                cornerRadius = CornerRadius(w * 0.09f, w * 0.09f)
            )

            // Visor Top Glass Reflection
            drawRoundRect(
                color = Color.White.copy(alpha = 0.07f),
                topLeft = Offset(visorLeft + w * 0.03f, visorTop + h * 0.02f),
                size = Size(visorWidth - w * 0.06f, visorHeight * 0.32f),
                cornerRadius = CornerRadius(w * 0.06f, w * 0.06f)
            )

            // 5. Animated Blinking Eyes
            val eyeRadius = w * 0.045f
            val leftEyeCenter = Offset(w * 0.39f, h * 0.41f)
            val rightEyeCenter = Offset(w * 0.61f, h * 0.41f)

            if (mood == MascotMood.CODING) {
                // Happy ^_^ or < > coding eyes
                val leftChevron = Path().apply {
                    moveTo(w * 0.42f, h * 0.37f)
                    lineTo(w * 0.35f, h * 0.41f)
                    lineTo(w * 0.42f, h * 0.45f)
                }
                val rightChevron = Path().apply {
                    moveTo(w * 0.58f, h * 0.37f)
                    lineTo(w * 0.65f, h * 0.41f)
                    lineTo(w * 0.58f, h * 0.45f)
                }
                drawPath(
                    path = leftChevron,
                    color = cyanGlow,
                    style = Stroke(width = w * 0.032f, cap = StrokeCap.Round, join = StrokeJoin.Round)
                )
                drawPath(
                    path = rightChevron,
                    color = cyanGlow,
                    style = Stroke(width = w * 0.032f, cap = StrokeCap.Round, join = StrokeJoin.Round)
                )
            } else {
                // Expressive blinking oval eyes
                val currentEyeHeight = (eyeRadius * 2f) * eyeScaleY
                val eyeTopY = leftEyeCenter.y - (currentEyeHeight / 2f)

                drawOval(
                    color = cyanGlow,
                    topLeft = Offset(leftEyeCenter.x - eyeRadius, eyeTopY),
                    size = Size(eyeRadius * 2f, currentEyeHeight)
                )
                drawOval(
                    color = cyanGlow,
                    topLeft = Offset(rightEyeCenter.x - eyeRadius, eyeTopY),
                    size = Size(eyeRadius * 2f, currentEyeHeight)
                )
                if (eyeScaleY > 0.5f) {
                    drawCircle(
                        color = Color.White,
                        radius = eyeRadius * 0.35f,
                        center = Offset(leftEyeCenter.x + eyeRadius * 0.25f, leftEyeCenter.y - eyeRadius * 0.25f)
                    )
                    drawCircle(
                        color = Color.White,
                        radius = eyeRadius * 0.35f,
                        center = Offset(rightEyeCenter.x + eyeRadius * 0.25f, rightEyeCenter.y - eyeRadius * 0.25f)
                    )
                }
            }

            // 6. Friendly Smile Curve
            val smilePath = Path().apply {
                moveTo(w * 0.43f, h * 0.51f)
                quadraticTo(w * 0.50f, h * 0.56f, w * 0.57f, h * 0.51f)
            }
            drawPath(
                path = smilePath,
                color = coralAccent,
                style = Stroke(width = w * 0.028f, cap = StrokeCap.Round)
            )

            // 7. Body / Hoodie Torso
            val bodyLeft = w * 0.26f
            val bodyTop = h * 0.72f
            val bodyWidth = w * 0.48f
            val bodyHeight = h * 0.22f
            drawRoundRect(
                color = navyDark,
                topLeft = Offset(bodyLeft, bodyTop),
                size = Size(bodyWidth, bodyHeight),
                cornerRadius = CornerRadius(w * 0.10f, w * 0.10f)
            )

            // Chest Code Tag '< />'
            val tagPathLeft = Path().apply {
                moveTo(w * 0.43f, h * 0.79f)
                lineTo(w * 0.39f, h * 0.83f)
                lineTo(w * 0.43f, h * 0.87f)
            }
            val tagPathRight = Path().apply {
                moveTo(w * 0.57f, h * 0.79f)
                lineTo(w * 0.61f, h * 0.83f)
                lineTo(w * 0.57f, h * 0.87f)
            }
            drawPath(
                path = tagPathLeft,
                color = cyanGlow,
                style = Stroke(width = w * 0.025f, cap = StrokeCap.Round, join = StrokeJoin.Round)
            )
            drawLine(
                color = coralAccent,
                start = Offset(w * 0.48f, h * 0.875f),
                end = Offset(w * 0.52f, h * 0.785f),
                strokeWidth = w * 0.025f,
                cap = StrokeCap.Round
            )
            drawPath(
                path = tagPathRight,
                color = cyanGlow,
                style = Stroke(width = w * 0.025f, cap = StrokeCap.Round, join = StrokeJoin.Round)
            )
        }

        // Optional Orbiting WebDev Pills (HTML, CSS, JS)
        if (showWebBadges) {
            WebBadgePill(
                label = "HTML",
                bgColor = Color(0xFFE34F26),
                modifier = Modifier
                    .align(Alignment.TopStart)
                    .offset(x = (-4).dp, y = (floatOffsetDp * 0.7f + 8).dp)
            )
            WebBadgePill(
                label = "CSS",
                bgColor = Color(0xFF1572B6),
                modifier = Modifier
                    .align(Alignment.TopEnd)
                    .offset(x = 4.dp, y = (-floatOffsetDp * 0.7f + 16).dp)
            )
            WebBadgePill(
                label = "JS",
                bgColor = Color(0xFFD49438),
                modifier = Modifier
                    .align(Alignment.BottomEnd)
                    .offset(x = 2.dp, y = (floatOffsetDp * 0.5f - 10).dp)
            )
        }
    }
}

@Composable
private fun WebBadgePill(
    label: String,
    bgColor: Color,
    modifier: Modifier = Modifier
) {
    Surface(
        modifier = modifier.shadow(4.dp, RoundedCornerShape(10.dp)),
        shape = RoundedCornerShape(10.dp),
        color = bgColor
    ) {
        Text(
            text = label,
            color = Color.White,
            fontSize = 10.sp,
            fontWeight = FontWeight.ExtraBold,
            fontFamily = FontFamily.Monospace,
            modifier = Modifier
                .border(1.dp, Color.White.copy(alpha = 0.35f), RoundedCornerShape(10.dp))
                .padding(horizontal = 8.dp, vertical = 3.dp)
        )
    }
}
