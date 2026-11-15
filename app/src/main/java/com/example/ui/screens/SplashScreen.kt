package com.example.ui.screens

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.LinearOutSlowInEasing
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.res.stringResource
import com.example.R
import com.example.ui.components.CodeAcademyMascot
import com.example.ui.components.MascotMood
import com.example.ui.theme.TealPrimary
import kotlinx.coroutines.async
import kotlinx.coroutines.delay

@Composable
fun SplashScreen(
    onPrepare: suspend () -> Unit = {},
    onSplashFinished: () -> Unit = {},
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    var startAnimation by remember { mutableStateOf(false) }

    // Smooth entry animation: fade in and subtle scale up
    val alphaAnim by animateFloatAsState(
        targetValue = if (startAnimation) 1f else 0f,
        animationSpec = tween(
            durationMillis = 650,
            easing = FastOutSlowInEasing
        ),
        label = "splashAlphaAnim"
    )

    val scaleAnim by animateFloatAsState(
        targetValue = if (startAnimation) 1f else 0.85f,
        animationSpec = tween(
            durationMillis = 700,
            easing = FastOutSlowInEasing
        ),
        label = "splashScaleAnim"
    )

    // One-time subtle shimmer/glow sweep for brand logo & title (single-shot, non-looping)
    val shimmerProgress = remember { Animatable(0f) }

    // Readiness gate: combines image pre-cache and DB initialization before proceeding
    LaunchedEffect(Unit) {
        startAnimation = true
        val startTime = System.currentTimeMillis()

        // Kick off one-shot subtle shimmer animation concurrently
        val shimmerJob = async {
            shimmerProgress.animateTo(
                targetValue = 1f,
                animationSpec = tween(durationMillis = 900, easing = LinearOutSlowInEasing)
            )
        }

        // Real asynchronous startup work: wait for onPrepare to fully finish
        val prepJob = async {
            try {
                onPrepare()
            } catch (_: Throwable) {}
        }

        prepJob.await()
        shimmerJob.await()

        // Minimum display threshold (800ms) to ensure smooth branded entry and prevent flash
        val elapsed = System.currentTimeMillis() - startTime
        val minDisplayDurationMs = 800L
        if (elapsed < minDisplayDurationMs) {
            delay(minDisplayDurationMs - elapsed)
        }

        onSplashFinished()
    }

    // Dynamic linear gradient brush for single-shot shimmer sweep on top of theme text
    val shimmerPos = shimmerProgress.value
    val onBgColor = MaterialTheme.colorScheme.onBackground
    val primaryColor = MaterialTheme.colorScheme.primary
    val titleGradient = Brush.linearGradient(
        colors = listOf(
            onBgColor,
            if (shimmerPos > 0.05f && shimmerPos < 0.95f) primaryColor else onBgColor,
            onBgColor
        ),
        start = androidx.compose.ui.geometry.Offset(x = shimmerPos * 300f - 100f, y = 0f),
        end = androidx.compose.ui.geometry.Offset(x = shimmerPos * 300f + 200f, y = 80f)
    )

    val splashBgGradient = Brush.verticalGradient(
        colors = listOf(
            MaterialTheme.colorScheme.surface,
            MaterialTheme.colorScheme.background,
            MaterialTheme.colorScheme.background
        )
    )

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(splashBgGradient),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
            modifier = Modifier
                .padding(32.dp)
                .alpha(alphaAnim)
                .scale(scaleAnim)
        ) {
            // Animated CodeAcademy WebDev Mascot ("Codi")
            CodeAcademyMascot(
                size = 152.dp,
                mood = MascotMood.HAPPY,
                showWebBadges = true,
                areAnimationsEnabled = true
            )

            Spacer(modifier = Modifier.height(18.dp))

            // App Name with crisp solid dark color and subtle shimmer accent
            Text(
                text = "CodeAcademy",
                style = TextStyle(
                    brush = titleGradient,
                    fontSize = 28.sp,
                    fontWeight = FontWeight.ExtraBold,
                    letterSpacing = 0.5.sp
                )
            )

            Spacer(modifier = Modifier.height(6.dp))

            // Tagline in theme secondary color
            Text(
                text = stringResource(R.string.splash_tagline),
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                fontWeight = FontWeight.Medium
            )

            Spacer(modifier = Modifier.height(36.dp))

            // Subtle loader indicating startup readiness
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center
            ) {
                CircularProgressIndicator(
                    modifier = Modifier.size(16.dp),
                    strokeWidth = 2.dp,
                    color = MaterialTheme.colorScheme.primary
                )
                Spacer(modifier = Modifier.width(10.dp))
                Text(
                    text = stringResource(R.string.splash_preparing),
                    fontSize = 12.sp,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }
    }
}
