package com.example.ui.components

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
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
import androidx.compose.material.icons.filled.LocalFireDepartment
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
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
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.R
import com.example.ui.theme.AmberGold
import com.example.ui.theme.CyanAccent
import com.example.ui.theme.FlameLight
import com.example.ui.theme.FlameOrange
import com.example.ui.theme.PurpleLevel
import com.example.ui.theme.TealDark
import com.example.ui.theme.TealLight
import com.example.ui.theme.TealPrimary

/**
 * Slide 1 Illustration: Character sitting at a desk with an open laptop and floating code badges.
 * Original clean flat design using solid shapes, vector geometry, and Compose UI elements.
 */
@Composable
fun IntroIllustration(modifier: Modifier = Modifier) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .height(260.dp),
        contentAlignment = Alignment.Center
    ) {
        // 1. Ambient Background Glow Circles
        Canvas(modifier = Modifier.fillMaxSize()) {
            val centerOffset = Offset(size.width / 2f, size.height * 0.52f)
            val glowRadius = size.height * 0.44f

            // Soft radial glow
            drawCircle(
                brush = Brush.radialGradient(
                    colors = listOf(
                        TealLight.copy(alpha = 0.22f),
                        CyanAccent.copy(alpha = 0.10f),
                        Color.Transparent
                    ),
                    center = centerOffset,
                    radius = glowRadius * 1.25f
                ),
                center = centerOffset,
                radius = glowRadius * 1.25f
            )

            // Flat decorative background circle
            drawCircle(
                color = TealPrimary.copy(alpha = 0.08f),
                center = centerOffset,
                radius = glowRadius * 0.88f
            )

            // Small decorative dots & sparkles
            drawCircle(color = CyanAccent.copy(alpha = 0.5f), center = Offset(size.width * 0.22f, size.height * 0.24f), radius = 4.dp.toPx())
            drawCircle(color = AmberGold.copy(alpha = 0.6f), center = Offset(size.width * 0.78f, size.height * 0.28f), radius = 5.dp.toPx())
            drawCircle(color = PurpleLevel.copy(alpha = 0.4f), center = Offset(size.width * 0.82f, size.height * 0.65f), radius = 3.dp.toPx())
            drawCircle(color = TealLight.copy(alpha = 0.5f), center = Offset(size.width * 0.18f, size.height * 0.68f), radius = 4.dp.toPx())
            // Additional floating geometric accents
            drawCircle(
                color = CyanAccent.copy(alpha = 0.30f),
                center = Offset(size.width * 0.14f, size.height * 0.35f),
                radius = 6.dp.toPx(),
                style = Stroke(width = 1.5.dp.toPx())
            )
            drawCircle(
                color = FlameOrange.copy(alpha = 0.25f),
                center = Offset(size.width * 0.85f, size.height * 0.40f),
                radius = 5.dp.toPx(),
                style = Stroke(width = 1.5.dp.toPx())
            )
            drawCircle(
                color = FlameOrange.copy(alpha = 0.50f),
                center = Offset(size.width * 0.30f, size.height * 0.18f),
                radius = 3.dp.toPx()
            )
            // Delicate half-circle arc top-right
            drawArc(
                color = CyanAccent.copy(alpha = 0.28f),
                startAngle = 30f,
                sweepAngle = 180f,
                useCenter = false,
                topLeft = Offset(size.width * 0.80f, size.height * 0.20f),
                size = Size(10.dp.toPx(), 10.dp.toPx()),
                style = Stroke(1.5.dp.toPx(), cap = StrokeCap.Round)
            )
            // Delicate half-circle arc mid-left
            drawArc(
                color = FlameOrange.copy(alpha = 0.28f),
                startAngle = 160f,
                sweepAngle = 180f,
                useCenter = false,
                topLeft = Offset(size.width * 0.12f, size.height * 0.48f),
                size = Size(11.dp.toPx(), 11.dp.toPx()),
                style = Stroke(1.5.dp.toPx(), cap = StrokeCap.Round)
            )
        }

        // 2. Vector Character & Laptop via Canvas
        Canvas(
            modifier = Modifier
                .size(240.dp, 200.dp)
                .align(Alignment.BottomCenter)
        ) {
            val w = size.width
            val h = size.height

            // --- Desk Surface ---
            val deskY = h * 0.82f
            drawRoundRect(
                color = Color(0xFF334155),
                topLeft = Offset(w * 0.08f, deskY),
                size = Size(w * 0.84f, 10.dp.toPx()),
                cornerRadius = CornerRadius(5.dp.toPx(), 5.dp.toPx())
            )
            // Desk edge highlight
            drawRoundRect(
                color = TealLight.copy(alpha = 0.4f),
                topLeft = Offset(w * 0.10f, deskY),
                size = Size(w * 0.80f, 2.dp.toPx()),
                cornerRadius = CornerRadius(1.dp.toPx(), 1.dp.toPx())
            )

            // --- Chair Backrest ---
            drawRoundRect(
                color = Color(0xFF1E293B),
                topLeft = Offset(w * 0.32f, h * 0.38f),
                size = Size(w * 0.36f, h * 0.42f),
                cornerRadius = CornerRadius(16.dp.toPx(), 16.dp.toPx())
            )

            // --- Character Body / Hoodie (Volumetric gradient) ---
            val bodyPath = Path().apply {
                moveTo(w * 0.35f, deskY)
                lineTo(w * 0.38f, h * 0.48f)
                quadraticTo(w * 0.50f, h * 0.42f, w * 0.62f, h * 0.48f)
                lineTo(w * 0.65f, deskY)
                close()
            }
            drawPath(
                path = bodyPath,
                brush = Brush.verticalGradient(
                    colors = listOf(TealLight, TealPrimary, Color(0xFF0F766E)),
                    startY = h * 0.42f,
                    endY = deskY
                )
            )

            // Hoodie collar detail (radial gradient)
            drawCircle(
                brush = Brush.radialGradient(
                    colors = listOf(TealDark, Color(0xFF042F2E)),
                    center = Offset(w * 0.50f, h * 0.46f),
                    radius = 12.dp.toPx()
                ),
                center = Offset(w * 0.50f, h * 0.46f),
                radius = 12.dp.toPx()
            )

            // --- Character Head & Hair ---
            // Neck
            drawRect(
                color = Color(0xFFFFDFBA),
                topLeft = Offset(w * 0.47f, h * 0.38f),
                size = Size(w * 0.06f, h * 0.08f)
            )
            // Face
            drawCircle(
                color = Color(0xFFFFDFBA),
                center = Offset(w * 0.50f, h * 0.33f),
                radius = 24.dp.toPx()
            )
            // Hair (Stylized modern silhouette with subtle gradient)
            val hairPath = Path().apply {
                moveTo(w * 0.42f, h * 0.34f)
                cubicTo(w * 0.40f, h * 0.22f, w * 0.60f, h * 0.22f, w * 0.58f, h * 0.34f)
                lineTo(w * 0.58f, h * 0.28f)
                quadraticTo(w * 0.50f, h * 0.26f, w * 0.42f, h * 0.31f)
                close()
            }
            drawPath(
                path = hairPath,
                brush = Brush.verticalGradient(
                    colors = listOf(Color(0xFF334155), Color(0xFF1E293B), Color(0xFF0F172A)),
                    startY = h * 0.22f,
                    endY = h * 0.34f
                )
            )

            // Cheerful smile & eyes
            drawCircle(color = Color(0xFF334155), center = Offset(w * 0.47f, h * 0.33f), radius = 2.dp.toPx())
            drawCircle(color = Color(0xFF334155), center = Offset(w * 0.53f, h * 0.33f), radius = 2.dp.toPx())
            drawArc(
                color = Color(0xFFE2A77E),
                startAngle = 10f,
                sweepAngle = 160f,
                useCenter = false,
                topLeft = Offset(w * 0.47f, h * 0.34f),
                size = Size(w * 0.06f, 6.dp.toPx())
            )

            // --- Open Laptop on Desk ---
            // Laptop base
            drawRoundRect(
                color = Color(0xFF64748B),
                topLeft = Offset(w * 0.38f, deskY - 4.dp.toPx()),
                size = Size(w * 0.24f, 6.dp.toPx()),
                cornerRadius = CornerRadius(2.dp.toPx(), 2.dp.toPx())
            )
            // Laptop screen bezel
            drawRoundRect(
                color = Color(0xFF0F172A),
                topLeft = Offset(w * 0.40f, deskY - 32.dp.toPx()),
                size = Size(w * 0.20f, 28.dp.toPx()),
                cornerRadius = CornerRadius(4.dp.toPx(), 4.dp.toPx())
            )
            // Laptop display glowing with code lines
            drawRoundRect(
                brush = Brush.verticalGradient(
                    colors = listOf(CyanAccent, TealLight)
                ),
                topLeft = Offset(w * 0.415f, deskY - 30.dp.toPx()),
                size = Size(w * 0.17f, 24.dp.toPx()),
                cornerRadius = CornerRadius(2.dp.toPx(), 2.dp.toPx())
            )
            // Code lines on display
            drawRoundRect(
                color = Color.White.copy(alpha = 0.85f),
                topLeft = Offset(w * 0.43f, deskY - 26.dp.toPx()),
                size = Size(w * 0.08f, 2.dp.toPx()),
                cornerRadius = CornerRadius(1.dp.toPx(), 1.dp.toPx())
            )
            drawRoundRect(
                color = AmberGold,
                topLeft = Offset(w * 0.44f, deskY - 21.dp.toPx()),
                size = Size(w * 0.11f, 2.dp.toPx()),
                cornerRadius = CornerRadius(1.dp.toPx(), 1.dp.toPx())
            )
            drawRoundRect(
                color = Color.White.copy(alpha = 0.85f),
                topLeft = Offset(w * 0.43f, deskY - 16.dp.toPx()),
                size = Size(w * 0.09f, 2.dp.toPx()),
                cornerRadius = CornerRadius(1.dp.toPx(), 1.dp.toPx())
            )

            // Mug on desk
            drawRoundRect(
                color = FlameOrange,
                topLeft = Offset(w * 0.66f, deskY - 14.dp.toPx()),
                size = Size(w * 0.05f, 14.dp.toPx()),
                cornerRadius = CornerRadius(2.dp.toPx(), 2.dp.toPx())
            )

            // Subtle drop shadow under laptop base for physical depth
            drawRoundRect(
                color = Color.Black.copy(alpha = 0.16f),
                topLeft = Offset(w * 0.38f, deskY - 2.dp.toPx()),
                size = Size(w * 0.24f, 5.dp.toPx()),
                cornerRadius = CornerRadius(2.dp.toPx(), 2.dp.toPx())
            )

            // --- Arms Typing on Keyboard (Directional Gradients) ---
            val leftArm = Path().apply {
                moveTo(w * 0.36f, deskY - 14.dp.toPx())
                lineTo(w * 0.40f, deskY - 2.dp.toPx())
                lineTo(w * 0.43f, deskY - 2.dp.toPx())
                lineTo(w * 0.39f, deskY - 16.dp.toPx())
                close()
            }
            drawPath(
                path = leftArm,
                brush = Brush.linearGradient(
                    colors = listOf(TealLight, TealPrimary),
                    start = Offset(w * 0.36f, deskY - 16.dp.toPx()),
                    end = Offset(w * 0.43f, deskY - 2.dp.toPx())
                )
            )
            val rightArm = Path().apply {
                moveTo(w * 0.64f, deskY - 14.dp.toPx())
                lineTo(w * 0.60f, deskY - 2.dp.toPx())
                lineTo(w * 0.57f, deskY - 2.dp.toPx())
                lineTo(w * 0.61f, deskY - 16.dp.toPx())
                close()
            }
            drawPath(
                path = rightArm,
                brush = Brush.linearGradient(
                    colors = listOf(TealLight, TealPrimary, Color(0xFF0F766E)),
                    start = Offset(w * 0.64f, deskY - 16.dp.toPx()),
                    end = Offset(w * 0.57f, deskY - 2.dp.toPx())
                )
            )
            // Hands on keyboard
            drawCircle(color = Color(0xFFFFDFBA), center = Offset(w * 0.415f, deskY - 3.dp.toPx()), radius = 3.5.dp.toPx())
            drawCircle(color = Color(0xFFFFDFBA), center = Offset(w * 0.585f, deskY - 3.dp.toPx()), radius = 3.5.dp.toPx())
        }

        // 3. Floating Code & Symbol Badges (Interactive look, flat/modern)
        // Top Left: </>
        Surface(
            modifier = Modifier
                .align(Alignment.TopStart)
                .padding(start = 24.dp, top = 20.dp)
                .shadow(6.dp, RoundedCornerShape(12.dp)),
            shape = RoundedCornerShape(12.dp),
            color = MaterialTheme.colorScheme.surface,
            border = androidx.compose.foundation.BorderStroke(1.dp, TealLight.copy(alpha = 0.5f))
        ) {
            Row(
                modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "</>",
                    fontFamily = FontFamily.Monospace,
                    fontWeight = FontWeight.Bold,
                    fontSize = 15.sp,
                    color = TealPrimary
                )
                Spacer(modifier = Modifier.width(6.dp))
                Text(
                    text = "HTML5",
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 11.sp,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }

        // Top Right: { }
        Surface(
            modifier = Modifier
                .align(Alignment.TopEnd)
                .padding(end = 24.dp, top = 28.dp)
                .shadow(6.dp, RoundedCornerShape(12.dp)),
            shape = RoundedCornerShape(12.dp),
            color = MaterialTheme.colorScheme.surface,
            border = androidx.compose.foundation.BorderStroke(1.dp, PurpleLevel.copy(alpha = 0.4f))
        ) {
            Row(
                modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "{ }",
                    fontFamily = FontFamily.Monospace,
                    fontWeight = FontWeight.Bold,
                    fontSize = 15.sp,
                    color = PurpleLevel
                )
                Spacer(modifier = Modifier.width(6.dp))
                Text(
                    text = "JS / CSS",
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 11.sp,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }

        // Bottom Left: const dev = true
        Surface(
            modifier = Modifier
                .align(Alignment.CenterStart)
                .padding(start = 16.dp, top = 80.dp)
                .shadow(4.dp, RoundedCornerShape(10.dp)),
            shape = RoundedCornerShape(10.dp),
            color = MaterialTheme.colorScheme.surface,
            border = androidx.compose.foundation.BorderStroke(1.dp, CyanAccent.copy(alpha = 0.35f))
        ) {
            Text(
                text = "⚡ const dev = true",
                fontFamily = FontFamily.Monospace,
                fontWeight = FontWeight.SemiBold,
                fontSize = 10.sp,
                color = CyanAccent,
                modifier = Modifier.padding(horizontal = 8.dp, vertical = 5.dp)
            )
        }

        // Bottom Right: Sparkle Badge
        Surface(
            modifier = Modifier
                .align(Alignment.CenterEnd)
                .padding(end = 18.dp, top = 80.dp)
                .shadow(4.dp, RoundedCornerShape(10.dp)),
            shape = RoundedCornerShape(10.dp),
            color = MaterialTheme.colorScheme.surface,
            border = androidx.compose.foundation.BorderStroke(1.dp, AmberGold.copy(alpha = 0.35f))
        ) {
            Row(
                modifier = Modifier.padding(horizontal = 8.dp, vertical = 5.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = Icons.Default.AutoAwesome,
                    contentDescription = null,
                    tint = AmberGold,
                    modifier = Modifier.size(12.dp)
                )
                Spacer(modifier = Modifier.width(4.dp))
                Text(
                    text = "Live Sandbox",
                    fontWeight = FontWeight.Bold,
                    fontSize = 10.sp,
                    color = AmberGold
                )
            }
        }
    }
}

/**
 * Slide 2 Illustration: Character celebrating with a trophy/badge, floating XP & streak badges.
 * Original clean flat design using solid shapes, vector geometry, and Compose UI elements.
 */
@Composable
fun GamifikasiIllustration(modifier: Modifier = Modifier) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .height(260.dp),
        contentAlignment = Alignment.Center
    ) {
        // 1. Ambient Background Warm Glow
        Canvas(modifier = Modifier.fillMaxSize()) {
            val centerOffset = Offset(size.width / 2f, size.height * 0.52f)
            val glowRadius = size.height * 0.44f

            // Soft radial glow in warm gold & flame
            drawCircle(
                brush = Brush.radialGradient(
                    colors = listOf(
                        AmberGold.copy(alpha = 0.24f),
                        FlameOrange.copy(alpha = 0.12f),
                        Color.Transparent
                    ),
                    center = centerOffset,
                    radius = glowRadius * 1.25f
                ),
                center = centerOffset,
                radius = glowRadius * 1.25f
            )

            // Inner warm tint circle
            drawCircle(
                color = AmberGold.copy(alpha = 0.08f),
                center = centerOffset,
                radius = glowRadius * 0.88f
            )

            // Floating celebration confetti/sparkles
            drawCircle(color = FlameOrange.copy(alpha = 0.6f), center = Offset(size.width * 0.20f, size.height * 0.22f), radius = 4.dp.toPx())
            drawCircle(color = AmberGold.copy(alpha = 0.7f), center = Offset(size.width * 0.76f, size.height * 0.25f), radius = 5.dp.toPx())
            drawCircle(color = TealLight.copy(alpha = 0.6f), center = Offset(size.width * 0.84f, size.height * 0.62f), radius = 4.dp.toPx())
            drawCircle(color = PurpleLevel.copy(alpha = 0.5f), center = Offset(size.width * 0.16f, size.height * 0.64f), radius = 3.dp.toPx())
            // Additional floating geometric accents
            drawCircle(
                color = CyanAccent.copy(alpha = 0.30f),
                center = Offset(size.width * 0.12f, size.height * 0.38f),
                radius = 6.dp.toPx(),
                style = Stroke(width = 1.5.dp.toPx())
            )
            drawCircle(
                color = FlameOrange.copy(alpha = 0.30f),
                center = Offset(size.width * 0.86f, size.height * 0.42f),
                radius = 5.dp.toPx(),
                style = Stroke(width = 1.5.dp.toPx())
            )
            drawCircle(
                color = AmberGold.copy(alpha = 0.55f),
                center = Offset(size.width * 0.32f, size.height * 0.18f),
                radius = 3.dp.toPx()
            )
            // Delicate half-circle arc top-right
            drawArc(
                color = AmberGold.copy(alpha = 0.35f),
                startAngle = 20f,
                sweepAngle = 180f,
                useCenter = false,
                topLeft = Offset(size.width * 0.78f, size.height * 0.16f),
                size = Size(10.dp.toPx(), 10.dp.toPx()),
                style = Stroke(1.5.dp.toPx(), cap = StrokeCap.Round)
            )
            // Delicate half-circle arc mid-left
            drawArc(
                color = CyanAccent.copy(alpha = 0.28f),
                startAngle = 150f,
                sweepAngle = 180f,
                useCenter = false,
                topLeft = Offset(size.width * 0.10f, size.height * 0.46f),
                size = Size(11.dp.toPx(), 11.dp.toPx()),
                style = Stroke(1.5.dp.toPx(), cap = StrokeCap.Round)
            )
        }

        // 2. Vector Character holding Golden Trophy via Canvas
        Canvas(
            modifier = Modifier
                .size(240.dp, 200.dp)
                .align(Alignment.BottomCenter)
        ) {
            val w = size.width
            val h = size.height

            // --- Podium Base ---
            val baseY = h * 0.86f
            drawRoundRect(
                color = Color(0xFF1E293B),
                topLeft = Offset(w * 0.25f, baseY),
                size = Size(w * 0.50f, 16.dp.toPx()),
                cornerRadius = CornerRadius(6.dp.toPx(), 6.dp.toPx())
            )
            drawRoundRect(
                color = AmberGold.copy(alpha = 0.4f),
                topLeft = Offset(w * 0.28f, baseY),
                size = Size(w * 0.44f, 2.dp.toPx()),
                cornerRadius = CornerRadius(1.dp.toPx(), 1.dp.toPx())
            )

            // --- Character Body in Purple Varsity Jacket (Volumetric gradient) ---
            val bodyPath = Path().apply {
                moveTo(w * 0.38f, baseY)
                lineTo(w * 0.40f, h * 0.54f)
                quadraticTo(w * 0.50f, h * 0.50f, w * 0.60f, h * 0.54f)
                lineTo(w * 0.62f, baseY)
                close()
            }
            drawPath(
                path = bodyPath,
                brush = Brush.verticalGradient(
                    colors = listOf(Color(0xFFA855F7), PurpleLevel, Color(0xFF581C87)),
                    startY = h * 0.50f,
                    endY = baseY
                )
            )

            // Jacket stripe
            drawRoundRect(
                color = Color.White.copy(alpha = 0.8f),
                topLeft = Offset(w * 0.485f, h * 0.52f),
                size = Size(w * 0.03f, baseY - h * 0.52f),
                cornerRadius = CornerRadius(1.dp.toPx(), 1.dp.toPx())
            )

            // --- Character Head & Hair ---
            // Neck
            drawRect(
                color = Color(0xFFFFDFBA),
                topLeft = Offset(w * 0.47f, h * 0.46f),
                size = Size(w * 0.06f, h * 0.06f)
            )
            // Face (smiling)
            drawCircle(
                color = Color(0xFFFFDFBA),
                center = Offset(w * 0.50f, h * 0.40f),
                radius = 22.dp.toPx()
            )
            // Hair (Stylized modern silhouette with gradient)
            val hairPath = Path().apply {
                moveTo(w * 0.42f, h * 0.40f)
                cubicTo(w * 0.41f, h * 0.28f, w * 0.59f, h * 0.28f, w * 0.58f, h * 0.40f)
                lineTo(w * 0.58f, h * 0.35f)
                quadraticTo(w * 0.50f, h * 0.32f, w * 0.42f, h * 0.37f)
                close()
            }
            drawPath(
                path = hairPath,
                brush = Brush.verticalGradient(
                    colors = listOf(Color(0xFF334155), Color(0xFF1E293B), Color(0xFF0F172A)),
                    startY = h * 0.28f,
                    endY = h * 0.40f
                )
            )

            // Joyful eyes (curved happy arcs) & smile
            drawArc(
                color = Color(0xFF334155),
                startAngle = 180f,
                sweepAngle = 180f,
                useCenter = false,
                topLeft = Offset(w * 0.46f, h * 0.39f),
                size = Size(w * 0.035f, 4.dp.toPx())
            )
            drawArc(
                color = Color(0xFF334155),
                startAngle = 180f,
                sweepAngle = 180f,
                useCenter = false,
                topLeft = Offset(w * 0.515f, h * 0.39f),
                size = Size(w * 0.035f, 4.dp.toPx())
            )
            // Big smile
            drawArc(
                color = Color(0xFFE2A77E),
                startAngle = 0f,
                sweepAngle = 180f,
                useCenter = true,
                topLeft = Offset(w * 0.47f, h * 0.42f),
                size = Size(w * 0.06f, 6.dp.toPx())
            )

            // --- Arms Holding Trophy (Directional gradients) ---
            // Left arm
            val leftArm = Path().apply {
                moveTo(w * 0.40f, h * 0.56f)
                lineTo(w * 0.33f, h * 0.42f)
                lineTo(w * 0.36f, h * 0.40f)
                lineTo(w * 0.42f, h * 0.54f)
                close()
            }
            drawPath(
                path = leftArm,
                brush = Brush.linearGradient(
                    colors = listOf(Color(0xFFA855F7), PurpleLevel, Color(0xFF6B21A8)),
                    start = Offset(w * 0.40f, h * 0.56f),
                    end = Offset(w * 0.33f, h * 0.40f)
                )
            )

            // Right arm
            val rightArm = Path().apply {
                moveTo(w * 0.60f, h * 0.56f)
                lineTo(w * 0.67f, h * 0.42f)
                lineTo(w * 0.64f, h * 0.40f)
                lineTo(w * 0.58f, h * 0.54f)
                close()
            }
            drawPath(
                path = rightArm,
                brush = Brush.linearGradient(
                    colors = listOf(Color(0xFFA855F7), PurpleLevel, Color(0xFF6B21A8)),
                    start = Offset(w * 0.60f, h * 0.56f),
                    end = Offset(w * 0.67f, h * 0.40f)
                )
            )

            // Hands
            drawCircle(color = Color(0xFFFFDFBA), center = Offset(w * 0.34f, h * 0.40f), radius = 6.dp.toPx())
            drawCircle(color = Color(0xFFFFDFBA), center = Offset(w * 0.66f, h * 0.40f), radius = 6.dp.toPx())

            // --- Golden Trophy Held Proudly ---
            val trophyY = h * 0.22f
            // Diffused drop shadow behind trophy for physical floating depth
            drawCircle(
                color = Color.Black.copy(alpha = 0.08f),
                center = Offset(w * 0.50f + 1.dp.toPx(), trophyY + 20.dp.toPx()),
                radius = 20.dp.toPx()
            )
            // Trophy Cup Body
            val cupPath = Path().apply {
                moveTo(w * 0.40f, trophyY)
                lineTo(w * 0.60f, trophyY)
                quadraticTo(w * 0.58f, trophyY + 28.dp.toPx(), w * 0.50f, trophyY + 34.dp.toPx())
                quadraticTo(w * 0.42f, trophyY + 28.dp.toPx(), w * 0.40f, trophyY)
                close()
            }
            drawPath(
                path = cupPath,
                brush = Brush.verticalGradient(
                    colors = listOf(AmberGold, FlameOrange)
                )
            )

            // Trophy Handles (Left and Right loops)
            drawArc(
                color = AmberGold,
                startAngle = 100f,
                sweepAngle = 160f,
                useCenter = false,
                topLeft = Offset(w * 0.35f, trophyY + 4.dp.toPx()),
                size = Size(14.dp.toPx(), 20.dp.toPx()),
                style = androidx.compose.ui.graphics.drawscope.Stroke(width = 3.dp.toPx())
            )
            drawArc(
                color = AmberGold,
                startAngle = 280f,
                sweepAngle = 160f,
                useCenter = false,
                topLeft = Offset(w * 0.59f, trophyY + 4.dp.toPx()),
                size = Size(14.dp.toPx(), 20.dp.toPx()),
                style = androidx.compose.ui.graphics.drawscope.Stroke(width = 3.dp.toPx())
            )

            // Trophy Stem & Base
            drawRect(
                color = FlameOrange,
                topLeft = Offset(w * 0.48f, trophyY + 34.dp.toPx()),
                size = Size(w * 0.04f, 8.dp.toPx())
            )
            drawRoundRect(
                color = AmberGold,
                topLeft = Offset(w * 0.44f, trophyY + 42.dp.toPx()),
                size = Size(w * 0.12f, 6.dp.toPx()),
                cornerRadius = CornerRadius(2.dp.toPx(), 2.dp.toPx())
            )

            // Star emblem on trophy
            drawCircle(
                color = Color.White.copy(alpha = 0.95f),
                center = Offset(w * 0.50f, trophyY + 16.dp.toPx()),
                radius = 5.dp.toPx()
            )
        }

        // 3. Floating Gamification Badges
        // Top Left: +50 XP Chip
        Surface(
            modifier = Modifier
                .align(Alignment.TopStart)
                .padding(start = 20.dp, top = 16.dp)
                .shadow(6.dp, RoundedCornerShape(12.dp)),
            shape = RoundedCornerShape(12.dp),
            color = MaterialTheme.colorScheme.surface,
            border = androidx.compose.foundation.BorderStroke(1.dp, AmberGold.copy(alpha = 0.5f))
        ) {
            Row(
                modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(
                    modifier = Modifier
                        .size(18.dp)
                        .clip(CircleShape)
                        .background(AmberGold),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.Star,
                        contentDescription = null,
                        tint = Color.White,
                        modifier = Modifier.size(12.dp)
                    )
                }
                Spacer(modifier = Modifier.width(6.dp))
                Text(
                    text = "+50 XP",
                    fontWeight = FontWeight.Bold,
                    fontSize = 13.sp,
                    color = AmberGold
                )
            }
        }

        // Top Right: Streak Fire Badge
        Surface(
            modifier = Modifier
                .align(Alignment.TopEnd)
                .padding(end = 20.dp, top = 22.dp)
                .shadow(6.dp, RoundedCornerShape(12.dp)),
            shape = RoundedCornerShape(12.dp),
            color = MaterialTheme.colorScheme.surface,
            border = androidx.compose.foundation.BorderStroke(1.dp, FlameOrange.copy(alpha = 0.4f))
        ) {
            Row(
                modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = Icons.Default.LocalFireDepartment,
                    contentDescription = null,
                    tint = FlameOrange,
                    modifier = Modifier.size(18.dp)
                )
                Spacer(modifier = Modifier.width(4.dp))
                Text(
                    text = stringResource(R.string.onboarding_illus_streak),
                    fontWeight = FontWeight.Bold,
                    fontSize = 12.sp,
                    color = FlameOrange
                )
            }
        }

        // Bottom Left: Level Up Chip
        Surface(
            modifier = Modifier
                .align(Alignment.CenterStart)
                .padding(start = 16.dp, top = 80.dp)
                .shadow(4.dp, RoundedCornerShape(10.dp)),
            shape = RoundedCornerShape(10.dp),
            color = MaterialTheme.colorScheme.surface,
            border = androidx.compose.foundation.BorderStroke(1.dp, PurpleLevel.copy(alpha = 0.35f))
        ) {
            Text(
                text = "🎖️ Level 2: Code Explorer",
                fontWeight = FontWeight.Bold,
                fontSize = 10.sp,
                color = PurpleLevel,
                modifier = Modifier.padding(horizontal = 8.dp, vertical = 5.dp)
            )
        }

        // Bottom Right: Juara Leaderboard
        Surface(
            modifier = Modifier
                .align(Alignment.CenterEnd)
                .padding(end = 18.dp, top = 80.dp)
                .shadow(4.dp, RoundedCornerShape(10.dp)),
            shape = RoundedCornerShape(10.dp),
            color = MaterialTheme.colorScheme.surface,
            border = androidx.compose.foundation.BorderStroke(1.dp, TealLight.copy(alpha = 0.4f))
        ) {
            Text(
                text = "🏆 ${stringResource(R.string.onboarding_illus_rank)}",
                fontWeight = FontWeight.Bold,
                fontSize = 10.sp,
                color = TealLight,
                modifier = Modifier.padding(horizontal = 8.dp, vertical = 5.dp)
            )
        }
    }
}
