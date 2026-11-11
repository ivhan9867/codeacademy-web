package com.example.ui.screens.onboarding

import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
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
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Terminal
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.AmberGold
import com.example.ui.theme.CodeDarkBg
import com.example.ui.theme.CodeDarkBorder
import com.example.ui.theme.CodeDarkSurface
import com.example.ui.theme.CodeSyntaxAttr
import com.example.ui.theme.CodeSyntaxKeyword
import com.example.ui.theme.CodeSyntaxString
import com.example.ui.theme.CodeSyntaxTag
import com.example.ui.theme.CyanAccent
import com.example.ui.theme.FlameOrange
import com.example.ui.theme.SuccessGreen
import com.example.ui.theme.TealLight
import com.example.ui.theme.TealPrimary
import kotlinx.coroutines.delay

/**
 * Step 2: Belajar Langsung Praktik.
 * Features an interactive mini code editor with simulated typing and instant live preview.
 */
@Composable
fun CodePreviewStep(
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
        // Dominant interactive code & live preview container (>= 42% screen height)
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
                .padding(vertical = 8.dp),
            contentAlignment = Alignment.Center
        ) {
            InteractiveCodeAndPreviewIllustration(
                areAnimationsEnabled = areAnimationsEnabled,
                modifier = Modifier.fillMaxSize()
            )
        }

        // Headline & Subtext (max 2 lines each)
        OnboardingStepHeader(
            title = "Belajar Sambil Praktik Langsung",
            subtitle = "Tulis kode nyata di editor interaktif dan saksikan hasilnya seketika tanpa setup rumit.",
            modifier = Modifier.padding(bottom = 16.dp)
        )
    }
}

@Composable
private fun InteractiveCodeAndPreviewIllustration(
    areAnimationsEnabled: Boolean,
    modifier: Modifier = Modifier
) {
    val fullTypedText = "Halo Dunia! 🚀"
    var typedLength by remember {
        mutableIntStateOf(if (areAnimationsEnabled) 0 else fullTypedText.length)
    }

    LaunchedEffect(areAnimationsEnabled) {
        if (!areAnimationsEnabled) {
            typedLength = fullTypedText.length
            return@LaunchedEffect
        }
        while (true) {
            typedLength = 0
            delay(500)
            for (i in 1..fullTypedText.length) {
                typedLength = i
                delay(120)
            }
            delay(1800)
        }
    }

    val infiniteTransition = rememberInfiniteTransition(label = "cursor_blink")
    val cursorAlpha by if (areAnimationsEnabled) {
        infiniteTransition.animateFloat(
            initialValue = 0f,
            targetValue = 1f,
            animationSpec = infiniteRepeatable(
                animation = tween(450, easing = LinearEasing),
                repeatMode = RepeatMode.Reverse
            ),
            label = "cursor"
        )
    } else {
        remember { androidx.compose.runtime.mutableFloatStateOf(1f) }
    }

    Box(
        modifier = modifier,
        contentAlignment = Alignment.Center
    ) {
        // Ambient background glow
        Canvas(modifier = Modifier.fillMaxSize()) {
            val center = Offset(size.width * 0.5f, size.height * 0.5f)
            drawCircle(
                brush = Brush.radialGradient(
                    colors = listOf(
                        TealLight.copy(alpha = 0.16f),
                        Color.Transparent
                    ),
                    center = center,
                    radius = size.minDimension * 0.5f
                ),
                center = center,
                radius = size.minDimension * 0.5f
            )
        }

        Column(
            modifier = Modifier
                .fillMaxWidth(0.92f)
                .shadow(12.dp, RoundedCornerShape(18.dp)),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            // 1. Mini Code Editor Window
            Surface(
                shape = RoundedCornerShape(16.dp),
                color = CodeDarkBg,
                border = BorderStroke(1.dp, CodeDarkBorder),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(14.dp)) {
                    // Editor Tab Header
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(6.dp)
                        ) {
                            Box(modifier = Modifier.size(9.dp).clip(CircleShape).background(FlameOrange))
                            Box(modifier = Modifier.size(9.dp).clip(CircleShape).background(AmberGold))
                            Box(modifier = Modifier.size(9.dp).clip(CircleShape).background(SuccessGreen))
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(
                                text = "index.html",
                                style = MaterialTheme.typography.labelSmall,
                                color = Color(0xFFB5ADA4),
                                fontFamily = FontFamily.Monospace,
                                fontWeight = FontWeight.Medium
                            )
                        }

                        Surface(
                            shape = RoundedCornerShape(6.dp),
                            color = TealPrimary.copy(alpha = 0.18f)
                        ) {
                            Row(
                                modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(4.dp)
                            ) {
                                Icon(
                                    imageVector = Icons.Default.PlayArrow,
                                    contentDescription = null,
                                    tint = TealLight,
                                    modifier = Modifier.size(12.dp)
                                )
                                Text(
                                    text = "Auto-Run",
                                    style = MaterialTheme.typography.labelSmall,
                                    color = TealLight,
                                    fontSize = 10.sp,
                                    fontWeight = FontWeight.Bold
                                )
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(10.dp))

                    // Line 1: <h1 class="judul">
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(text = "1 ", style = MaterialTheme.typography.bodySmall, color = Color(0xFF5A544E), fontFamily = FontFamily.Monospace)
                        Text(text = "<", style = MaterialTheme.typography.bodySmall, color = CodeSyntaxTag, fontFamily = FontFamily.Monospace)
                        Text(text = "h1 ", style = MaterialTheme.typography.bodySmall, color = CodeSyntaxTag, fontWeight = FontWeight.Bold, fontFamily = FontFamily.Monospace)
                        Text(text = "class", style = MaterialTheme.typography.bodySmall, color = CodeSyntaxAttr, fontFamily = FontFamily.Monospace)
                        Text(text = "=", style = MaterialTheme.typography.bodySmall, color = Color(0xFFE5DFD7), fontFamily = FontFamily.Monospace)
                        Text(text = "\"judul\"", style = MaterialTheme.typography.bodySmall, color = CodeSyntaxString, fontFamily = FontFamily.Monospace)
                        Text(text = ">", style = MaterialTheme.typography.bodySmall, color = CodeSyntaxTag, fontFamily = FontFamily.Monospace)
                    }

                    // Line 2: Typed text + Cursor
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(text = "2   ", style = MaterialTheme.typography.bodySmall, color = Color(0xFF5A544E), fontFamily = FontFamily.Monospace)
                        Text(
                            text = fullTypedText.substring(0, typedLength),
                            style = MaterialTheme.typography.bodySmall,
                            color = Color(0xFFF8F5F0),
                            fontWeight = FontWeight.SemiBold,
                            fontFamily = FontFamily.Monospace
                        )
                        Box(
                            modifier = Modifier
                                .width(2.dp)
                                .height(14.dp)
                                .background(CyanAccent.copy(alpha = cursorAlpha))
                        )
                    }

                    // Line 3: </h1>
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(text = "3 ", style = MaterialTheme.typography.bodySmall, color = Color(0xFF5A544E), fontFamily = FontFamily.Monospace)
                        Text(text = "</", style = MaterialTheme.typography.bodySmall, color = CodeSyntaxTag, fontFamily = FontFamily.Monospace)
                        Text(text = "h1", style = MaterialTheme.typography.bodySmall, color = CodeSyntaxTag, fontWeight = FontWeight.Bold, fontFamily = FontFamily.Monospace)
                        Text(text = ">", style = MaterialTheme.typography.bodySmall, color = CodeSyntaxTag, fontFamily = FontFamily.Monospace)
                    }
                }
            }

            // 2. Instant Live Preview Box
            Surface(
                shape = RoundedCornerShape(16.dp),
                color = MaterialTheme.colorScheme.surface,
                border = BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(14.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(6.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.Terminal,
                                contentDescription = null,
                                tint = TealPrimary,
                                modifier = Modifier.size(14.dp)
                            )
                            Text(
                                text = "Pratinjau Hasil",
                                style = MaterialTheme.typography.labelMedium,
                                fontWeight = FontWeight.Bold,
                                color = TealPrimary
                            )
                        }

                        Surface(
                            shape = RoundedCornerShape(12.dp),
                            color = SuccessGreen.copy(alpha = 0.12f)
                        ) {
                            Row(
                                modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(4.dp)
                            ) {
                                Box(modifier = Modifier.size(6.dp).clip(CircleShape).background(SuccessGreen))
                                Text(
                                    text = "Aktif",
                                    style = MaterialTheme.typography.labelSmall,
                                    color = SuccessGreen,
                                    fontSize = 10.sp,
                                    fontWeight = FontWeight.Bold
                                )
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(10.dp))

                    // Simulated rendered web view
                    Surface(
                        shape = RoundedCornerShape(10.dp),
                        color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.4f),
                        border = BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.5f)),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(12.dp),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Text(
                                text = if (typedLength > 0) fullTypedText.substring(0, typedLength) else "...",
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.onSurface
                            )

                            Spacer(modifier = Modifier.height(8.dp))

                            Surface(
                                shape = RoundedCornerShape(8.dp),
                                color = TealPrimary,
                                shadowElevation = 2.dp
                            ) {
                                Row(
                                    modifier = Modifier.padding(horizontal = 14.dp, vertical = 6.dp),
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.CheckCircle,
                                        contentDescription = null,
                                        tint = Color.White,
                                        modifier = Modifier.size(14.dp)
                                    )
                                    Text(
                                        text = "Berhasil Dijalankan!",
                                        style = MaterialTheme.typography.labelSmall,
                                        color = Color.White,
                                        fontWeight = FontWeight.Bold
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}
