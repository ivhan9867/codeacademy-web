package com.example.ui.screens

import android.app.Activity
import android.content.res.Configuration
import android.provider.Settings
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.slideInVertically
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
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.ErrorOutline
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextFieldColors
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalInspectionMode
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.SpanStyle
import com.example.R
import com.example.ui.components.CodeAcademyMascot
import com.example.ui.components.MascotMood
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.view.WindowCompat
import com.example.ui.theme.AmberGold
import com.example.ui.theme.CodeAcademyTheme
import com.example.ui.theme.CyanAccent
import com.example.ui.theme.CyanAccentText
import com.example.ui.theme.ErrorRed
import com.example.ui.theme.FlameOrange
import com.example.ui.theme.LightBg
import com.example.ui.theme.LightBorder
import com.example.ui.theme.LightSurface
import com.example.ui.theme.LightSurfaceVariant
import com.example.ui.theme.LightTextPrimary
import com.example.ui.theme.LightTextSecondary
import com.example.ui.theme.TealLight
import com.example.ui.theme.TealPrimary
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

// ========================================================================================
// CATATAN ARSITEKTUR TEMA — SENGAJA FIXED (JANGAN DIUBAH KE MaterialTheme.colorScheme)
// ========================================================================================
// LoginScreen dan RegisterScreen SENGAJA menggunakan tema warna tetap (Fixed Brand Theme:
// latar hangat Cream `LightBg`/`LightSurface` + aksen Oranye `FlameOrange`/`CyanAccent`)
// dan TIDAK mengikuti Dark Mode sistem maupun toggle tema aplikasi.
//
// Ini adalah keputusan desain produk yang disengaja (bukan bug atau kelupaan memakai
// MaterialTheme.colorScheme). Jangan mengganti token warna tetap di file ini menjadi
// MaterialTheme.colorScheme.* pada sesi refactoring atau audit warna berikutnya!
// ========================================================================================

@Composable
fun LoginScreen(
    onLoginSubmit: suspend (email: String, password: String) -> Result<Unit>,
    onNavigateToRegister: () -> Unit,
    modifier: Modifier = Modifier,
    onForgotPassword: () -> Unit = {},
    onGoogleSignIn: () -> Unit = {}
) {
    // Pastikan status bar & navigation bar berikon gelap (terbaca kontras di atas latar cream LightBg)
    val view = LocalView.current
    if (!view.isInEditMode) {
        SideEffect {
            val window = (view.context as? Activity)?.window ?: return@SideEffect
            val insetsController = WindowCompat.getInsetsController(window, view)
            insetsController.isAppearanceLightStatusBars = true
            insetsController.isAppearanceLightNavigationBars = true
        }
    }

    var email by rememberSaveable { mutableStateOf("") }
    var password by rememberSaveable { mutableStateOf("") }
    var isPasswordVisible by rememberSaveable { mutableStateOf(false) }
    var isLoading by remember { mutableStateOf(false) }
    var errorMessage by rememberSaveable { mutableStateOf<String?>(null) }

    val context = LocalContext.current
    val isInspectionMode = LocalInspectionMode.current
    val areAnimationsEnabled = remember(context, isInspectionMode) {
        if (isInspectionMode) {
            false
        } else {
            val scale = runCatching {
                Settings.Global.getFloat(
                    context.contentResolver,
                    Settings.Global.ANIMATOR_DURATION_SCALE,
                    1f
                )
            }.getOrDefault(1f)
            scale > 0f
        }
    }

    // Staged entrance animation states
    var isHeaderVisible by remember { mutableStateOf(!areAnimationsEnabled) }
    var isHeadlineVisible by remember { mutableStateOf(!areAnimationsEnabled) }
    var isCardVisible by remember { mutableStateOf(!areAnimationsEnabled) }

    LaunchedEffect(areAnimationsEnabled) {
        if (areAnimationsEnabled) {
            isHeaderVisible = true
            delay(90)
            isHeadlineVisible = true
            delay(110)
            isCardVisible = true
        }
    }

    val focusManager = LocalFocusManager.current
    val coroutineScope = rememberCoroutineScope()
    val density = LocalDensity.current

    val errorEmpty = stringResource(R.string.login_error_empty_fields)
    val errorInvalidEmail = stringResource(R.string.login_error_invalid_email)
    val errorPasswordMinLength = stringResource(R.string.login_error_password_min_length)
    val errorDefaultFailure = stringResource(R.string.login_error_default_failure)

    val submitLogin: () -> Unit = {
        val trimmedEmail = email.trim()
        when {
            trimmedEmail.isEmpty() || password.isEmpty() -> {
                errorMessage = errorEmpty
            }
            !trimmedEmail.contains("@") || !trimmedEmail.contains(".") -> {
                errorMessage = errorInvalidEmail
            }
            password.length < 6 -> {
                errorMessage = errorPasswordMinLength
            }
            else -> {
                isLoading = true
                errorMessage = null
                coroutineScope.launch {
                    val result = onLoginSubmit(trimmedEmail, password)
                    isLoading = false
                    result.onFailure { err ->
                        errorMessage = err.message ?: errorDefaultFailure
                    }
                }
            }
        }
    }

    // SENGAJA FIXED: Latar utama selalu menggunakan LightBg (Cream 0xFFFAF7F2)
    Box(
        modifier = modifier
            .fillMaxSize()
            .background(LightBg)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .imePadding()
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 22.dp)
                .padding(top = 16.dp, bottom = 32.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // 1. Editorial Hero Section: Flat vector character + Big bold headline
            LoginHeroHeader(
                isHeaderVisible = isHeaderVisible,
                isHeadlineVisible = isHeadlineVisible,
                areAnimationsEnabled = areAnimationsEnabled
            )

            Spacer(modifier = Modifier.height(18.dp))

            // 2. Simple uncluttered LoginFormCard with staged slideUp + fade-in
            val slideOffsetY24Px = remember(density) { with(density) { 24.dp.roundToPx() } }
            AnimatedVisibility(
                visible = isCardVisible,
                enter = if (areAnimationsEnabled) {
                    fadeIn(tween(420, easing = FastOutSlowInEasing)) +
                        slideInVertically(
                            initialOffsetY = { slideOffsetY24Px },
                            animationSpec = tween(420, easing = FastOutSlowInEasing)
                        )
                } else {
                    fadeIn(tween(0))
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                LoginFormCard(
                    email = email,
                    onEmailChange = {
                        email = it
                        errorMessage = null
                    },
                    password = password,
                    onPasswordChange = {
                        password = it
                        errorMessage = null
                    },
                    isPasswordVisible = isPasswordVisible,
                    onTogglePasswordVisibility = { isPasswordVisible = !isPasswordVisible },
                    isLoading = isLoading,
                    errorMessage = errorMessage,
                    onSubmit = submitLogin,
                    onForgotPassword = onForgotPassword,
                    onGoogleSignIn = onGoogleSignIn,
                    focusManager = focusManager
                )
            }

            Spacer(modifier = Modifier.height(24.dp))

            // 3. Footer link to Register
            LoginFooter(onNavigateToRegister = onNavigateToRegister)
        }
    }
}

/**
 * Editorial Hero Header:
 * - Ilustrasi karakter flat vector besar (siswa belajar coding dari smartphone secara santai di atas pouf/cushion).
 * - Headline besar berani di bawah ilustrasi dengan kata kunci ter-highlight warna aksen FlameOrange.
 * - Solid colors (cream LightBg + FlameOrange + CyanAccent), bukan gradasi rumit.
 */
@Composable
private fun LoginHeroHeader(
    isHeaderVisible: Boolean = true,
    isHeadlineVisible: Boolean = true,
    areAnimationsEnabled: Boolean = false
) {
    val density = LocalDensity.current

    Column(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        val slideOffsetY20Px = remember(density) { with(density) { (-16).dp.roundToPx() } }

        // 1. Flat Vector Character Illustration
        AnimatedVisibility(
            visible = isHeaderVisible,
            enter = if (areAnimationsEnabled) {
                fadeIn(tween(400, easing = FastOutSlowInEasing)) +
                    slideInVertically(
                        initialOffsetY = { slideOffsetY20Px },
                        animationSpec = tween(400, easing = FastOutSlowInEasing)
                    )
            } else {
                fadeIn(tween(0))
            }
        ) {
            CodeAcademyMascot(
                size = 128.dp,
                mood = MascotMood.HAPPY,
                showWebBadges = true,
                areAnimationsEnabled = areAnimationsEnabled
            )
        }

        Spacer(modifier = Modifier.height(14.dp))

        // 2. Bold Editorial Headline with Highlighted Keyword
        val slideOffsetY14Px = remember(density) { with(density) { 14.dp.roundToPx() } }
        AnimatedVisibility(
            visible = isHeadlineVisible,
            enter = if (areAnimationsEnabled) {
                fadeIn(tween(380, easing = FastOutSlowInEasing)) +
                    slideInVertically(
                        initialOffsetY = { slideOffsetY14Px },
                        animationSpec = tween(380, easing = FastOutSlowInEasing)
                    )
            } else {
                fadeIn(tween(0))
            }
        ) {
            val headlinePrefix = stringResource(R.string.login_headline_prefix)
            val headlineHighlight = stringResource(R.string.login_headline_highlight)
            val headline = remember(headlinePrefix, headlineHighlight) {
                buildAnnotatedString {
                    append(headlinePrefix)
                    withStyle(SpanStyle(color = FlameOrange)) {
                        append(headlineHighlight)
                    }
                }
            }

            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text(
                    text = headline,
                    style = MaterialTheme.typography.headlineLarge.copy(
                        fontSize = 32.sp,
                        lineHeight = 38.sp,
                        fontWeight = FontWeight.Black,
                        letterSpacing = (-0.8).sp
                    ),
                    color = LightTextPrimary, // SENGAJA FIXED: Teks gelap di atas latar cream
                    textAlign = TextAlign.Center,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 12.dp)
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = stringResource(R.string.login_subtitle),
                    style = MaterialTheme.typography.bodyMedium,
                    color = LightTextSecondary, // SENGAJA FIXED
                    textAlign = TextAlign.Center,
                    lineHeight = 20.sp,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 20.dp)
                )
            }
        }
    }
}

/**
 * Ilustrasi Karakter Flat Vector Belajar Coding dari Smartphone:
 * Menggunakan teknik Canvas + Path yang konsisten dengan `OnboardingIllustrations.kt`
 * (proporsi kepala ~21dp, skin color `Color(0xFFFFDFBA)`, rambut `Color(0xFF1E293B)`,
 * pakaian solid FlameOrange, celana gelap, dan smartphone dengan baris kode bercahaya).
 */
@Composable
fun LoginHeroIllustration(modifier: Modifier = Modifier) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .height(210.dp),
        contentAlignment = Alignment.Center
    ) {
        // 1. Ambient Background Shapes (Solid flat colors, editorial aesthetic)
        Canvas(modifier = Modifier.fillMaxSize()) {
            val centerOffset = Offset(size.width / 2f, size.height * 0.54f)
            val glowRadius = size.height * 0.44f

            // Soft flat warm circle background
            drawCircle(
                color = FlameOrange.copy(alpha = 0.08f),
                center = centerOffset,
                radius = glowRadius * 0.95f
            )

            // Flat decorative outer accent ring
            drawCircle(
                color = CyanAccent.copy(alpha = 0.20f),
                center = centerOffset,
                radius = glowRadius * 1.15f,
                style = Stroke(width = 1.5.dp.toPx())
            )

            // Small playful geometric accent dots (solid colors)
            drawCircle(
                color = FlameOrange,
                center = Offset(size.width * 0.16f, size.height * 0.26f),
                radius = 4.dp.toPx()
            )
            drawCircle(
                color = CyanAccent,
                center = Offset(size.width * 0.82f, size.height * 0.22f),
                radius = 5.dp.toPx()
            )
            drawCircle(
                color = AmberGold,
                center = Offset(size.width * 0.86f, size.height * 0.68f),
                radius = 3.5.dp.toPx()
            )
            drawCircle(
                color = TealLight.copy(alpha = 0.7f),
                center = Offset(size.width * 0.14f, size.height * 0.70f),
                radius = 3.dp.toPx()
            )

            // Additional floating geometric accents (editorial style)
            drawCircle(
                color = CyanAccent.copy(alpha = 0.30f),
                center = Offset(size.width * 0.22f, size.height * 0.44f),
                radius = 5.dp.toPx(),
                style = Stroke(width = 1.5.dp.toPx())
            )
            drawCircle(
                color = FlameOrange.copy(alpha = 0.25f),
                center = Offset(size.width * 0.78f, size.height * 0.48f),
                radius = 6.dp.toPx(),
                style = Stroke(width = 1.5.dp.toPx())
            )
            drawCircle(
                color = AmberGold.copy(alpha = 0.50f),
                center = Offset(size.width * 0.30f, size.height * 0.18f),
                radius = 3.dp.toPx()
            )
            // Delicate half-circle arc at top-left
            drawArc(
                color = CyanAccent.copy(alpha = 0.28f),
                startAngle = 140f,
                sweepAngle = 180f,
                useCenter = false,
                topLeft = Offset(size.width * 0.11f, size.height * 0.42f),
                size = Size(11.dp.toPx(), 11.dp.toPx()),
                style = Stroke(1.5.dp.toPx(), cap = StrokeCap.Round)
            )
            // Subtle half-circle arc at mid-right
            drawArc(
                color = FlameOrange.copy(alpha = 0.30f),
                startAngle = -30f,
                sweepAngle = 180f,
                useCenter = false,
                topLeft = Offset(size.width * 0.86f, size.height * 0.36f),
                size = Size(10.dp.toPx(), 10.dp.toPx()),
                style = Stroke(1.5.dp.toPx(), cap = StrokeCap.Round)
            )
            // Soft outline dot near lower left
            drawCircle(
                color = CyanAccent.copy(alpha = 0.25f),
                center = Offset(size.width * 0.26f, size.height * 0.76f),
                radius = 3.5.dp.toPx(),
                style = Stroke(1.2.dp.toPx())
            )
        }

        // 2. Vector Character Sitting Relaxed on Lounge Cushion Learning on Phone
        Canvas(
            modifier = Modifier
                .size(240.dp, 190.dp)
                .align(Alignment.BottomCenter)
        ) {
            val w = size.width
            val h = size.height

            // --- Modern Lounge Cushion / Seat with subtle gradient depth ---
            val cushionY = h * 0.62f
            val cushionPath = Path().apply {
                moveTo(w * 0.14f, h * 0.88f)
                quadraticTo(w * 0.10f, cushionY, w * 0.32f, cushionY - 10.dp.toPx())
                quadraticTo(w * 0.50f, cushionY - 8.dp.toPx(), w * 0.62f, cushionY + 12.dp.toPx())
                quadraticTo(w * 0.72f, h * 0.78f, w * 0.68f, h * 0.92f)
                quadraticTo(w * 0.40f, h * 0.96f, w * 0.14f, h * 0.88f)
                close()
            }
            drawPath(
                path = cushionPath,
                brush = Brush.verticalGradient(
                    colors = listOf(Color(0xFFF1F5F9), Color(0xFFE2E8F0)),
                    startY = cushionY - 10.dp.toPx(),
                    endY = h * 0.96f
                )
            )
            // Cushion contour detail
            val creasePath = Path().apply {
                moveTo(w * 0.22f, h * 0.80f)
                quadraticTo(w * 0.38f, h * 0.84f, w * 0.54f, h * 0.76f)
            }
            drawPath(
                path = creasePath,
                color = Color(0xFFCBD5E1),
                style = Stroke(width = 2.5.dp.toPx(), cap = StrokeCap.Round)
            )

            // --- Character Legs in Relaxed Sitting Position ---
            // Pants in dark slate with subtle depth gradient
            val legPath = Path().apply {
                moveTo(w * 0.38f, h * 0.68f)
                quadraticTo(w * 0.48f, h * 0.68f, w * 0.64f, h * 0.74f)
                quadraticTo(w * 0.70f, h * 0.78f, w * 0.68f, h * 0.88f)
                lineTo(w * 0.58f, h * 0.88f)
                quadraticTo(w * 0.58f, h * 0.80f, w * 0.42f, h * 0.76f)
                close()
            }
            drawPath(
                path = legPath,
                brush = Brush.verticalGradient(
                    colors = listOf(Color(0xFF334155), Color(0xFF1E293B)),
                    startY = h * 0.68f,
                    endY = h * 0.88f
                )
            )

            // White & Teal Sneaker
            drawRoundRect(
                color = Color.White,
                topLeft = Offset(w * 0.60f, h * 0.86f),
                size = Size(26.dp.toPx(), 9.dp.toPx()),
                cornerRadius = CornerRadius(3.dp.toPx(), 3.dp.toPx())
            )
            drawRoundRect(
                color = CyanAccent,
                topLeft = Offset(w * 0.60f, h * 0.89f),
                size = Size(26.dp.toPx(), 3.dp.toPx()),
                cornerRadius = CornerRadius(1.5.dp.toPx(), 1.5.dp.toPx())
            )

            // --- Character Torso / Hoodie in Vibrant FlameOrange with Volume Gradient ---
            val torsoPath = Path().apply {
                moveTo(w * 0.32f, h * 0.72f)
                lineTo(w * 0.35f, h * 0.47f)
                quadraticTo(w * 0.46f, h * 0.42f, w * 0.56f, h * 0.47f)
                lineTo(w * 0.57f, h * 0.71f)
                quadraticTo(w * 0.44f, h * 0.75f, w * 0.32f, h * 0.72f)
                close()
            }
            drawPath(
                path = torsoPath,
                brush = Brush.verticalGradient(
                    colors = listOf(Color(0xFFFF6D3B), FlameOrange, Color(0xFFC2410C)),
                    startY = h * 0.42f,
                    endY = h * 0.75f
                )
            )

            // Hoodie Pouch Pocket with subtle gradient depth
            val pocketPath = Path().apply {
                moveTo(w * 0.38f, h * 0.68f)
                lineTo(w * 0.41f, h * 0.61f)
                lineTo(w * 0.51f, h * 0.61f)
                lineTo(w * 0.54f, h * 0.68f)
                close()
            }
            drawPath(
                path = pocketPath,
                brush = Brush.verticalGradient(
                    colors = listOf(Color(0xFFEA580C), Color(0xFF9A3412)),
                    startY = h * 0.61f,
                    endY = h * 0.68f
                )
            )

            // Hoodie Collar Detail
            drawCircle(
                color = Color(0xFFC2410C),
                center = Offset(w * 0.45f, h * 0.46f),
                radius = 9.dp.toPx()
            )

            // Hoodie Drawstrings (White)
            drawLine(
                color = Color.White.copy(alpha = 0.90f),
                start = Offset(w * 0.43f, h * 0.47f),
                end = Offset(w * 0.43f, h * 0.54f),
                strokeWidth = 2.dp.toPx(),
                cap = StrokeCap.Round
            )
            drawLine(
                color = Color.White.copy(alpha = 0.90f),
                start = Offset(w * 0.47f, h * 0.47f),
                end = Offset(w * 0.47f, h * 0.53f),
                strokeWidth = 2.dp.toPx(),
                cap = StrokeCap.Round
            )

            // --- Neck & Head ---
            // Neck
            drawRect(
                color = Color(0xFFFFDFBA),
                topLeft = Offset(w * 0.425f, h * 0.39f),
                size = Size(w * 0.055f, h * 0.07f)
            )
            // Face
            drawCircle(
                color = Color(0xFFFFDFBA),
                center = Offset(w * 0.45f, h * 0.34f),
                radius = 21.dp.toPx()
            )
            // Ear
            drawCircle(
                color = Color(0xFFFFDFBA),
                center = Offset(w * 0.37f, h * 0.35f),
                radius = 4.5.dp.toPx()
            )

            // Hair (Stylized modern silhouette with rich gradient)
            val hairPath = Path().apply {
                moveTo(w * 0.36f, h * 0.35f)
                cubicTo(w * 0.34f, h * 0.22f, w * 0.54f, h * 0.21f, w * 0.53f, h * 0.33f)
                lineTo(w * 0.54f, h * 0.28f)
                quadraticTo(w * 0.46f, h * 0.26f, w * 0.38f, h * 0.31f)
                close()
            }
            drawPath(
                path = hairPath,
                brush = Brush.verticalGradient(
                    colors = listOf(Color(0xFF334155), Color(0xFF1E293B), Color(0xFF0F172A)),
                    startY = h * 0.21f,
                    endY = h * 0.35f
                )
            )

            // Cheerful, focused expression looking down at smartphone
            drawArc(
                color = Color(0xFF334155),
                startAngle = 180f,
                sweepAngle = 180f,
                useCenter = false,
                topLeft = Offset(w * 0.46f, h * 0.33f),
                size = Size(w * 0.035f, 3.5.dp.toPx()),
                style = Stroke(width = 1.8.dp.toPx(), cap = StrokeCap.Round)
            )
            drawArc(
                color = Color(0xFFE2A77E),
                startAngle = 10f,
                sweepAngle = 160f,
                useCenter = false,
                topLeft = Offset(w * 0.45f, h * 0.36f),
                size = Size(w * 0.05f, 5.dp.toPx()),
                style = Stroke(width = 1.8.dp.toPx(), cap = StrokeCap.Round)
            )
            // Friendly blush
            drawCircle(
                color = Color(0xFFFFB4A2).copy(alpha = 0.55f),
                center = Offset(w * 0.48f, h * 0.36f),
                radius = 3.5.dp.toPx()
            )

            // --- Arms Holding Smartphone (Directional gradients) ---
            // Left arm
            val leftArm = Path().apply {
                moveTo(w * 0.35f, h * 0.49f)
                lineTo(w * 0.44f, h * 0.57f)
                lineTo(w * 0.47f, h * 0.54f)
                lineTo(w * 0.38f, h * 0.47f)
                close()
            }
            drawPath(
                path = leftArm,
                brush = Brush.linearGradient(
                    colors = listOf(Color(0xFFFF6D3B), FlameOrange),
                    start = Offset(w * 0.35f, h * 0.47f),
                    end = Offset(w * 0.47f, h * 0.57f)
                )
            )

            // Right arm (overlapping slightly behind phone)
            val rightArm = Path().apply {
                moveTo(w * 0.54f, h * 0.49f)
                lineTo(w * 0.56f, h * 0.58f)
                lineTo(w * 0.52f, h * 0.59f)
                lineTo(w * 0.49f, h * 0.50f)
                close()
            }
            drawPath(
                path = rightArm,
                brush = Brush.linearGradient(
                    colors = listOf(Color(0xFFEA580C), Color(0xFFC2410C)),
                    start = Offset(w * 0.54f, h * 0.49f),
                    end = Offset(w * 0.52f, h * 0.59f)
                )
            )

            // Hands
            drawCircle(
                color = Color(0xFFFFDFBA),
                center = Offset(w * 0.465f, h * 0.58f),
                radius = 5.dp.toPx()
            )
            drawCircle(
                color = Color(0xFFFFDFBA),
                center = Offset(w * 0.545f, h * 0.59f),
                radius = 5.dp.toPx()
            )

            // --- Glowing Smartphone Running Code ---
            val phoneX = w * 0.47f
            val phoneY = h * 0.48f
            val phoneW = 20.dp.toPx()
            val phoneH = 36.dp.toPx()

            // Multi-layered soft drop shadow under smartphone for physical floating depth
            drawRoundRect(
                color = Color.Black.copy(alpha = 0.08f),
                topLeft = Offset(phoneX - 1.5.dp.toPx(), phoneY + 1.dp.toPx()),
                size = Size(phoneW + 3.dp.toPx(), phoneH + 4.dp.toPx()),
                cornerRadius = CornerRadius(6.dp.toPx(), 6.dp.toPx())
            )
            drawRoundRect(
                color = Color.Black.copy(alpha = 0.18f),
                topLeft = Offset(phoneX + 1.dp.toPx(), phoneY + 2.5.dp.toPx()),
                size = Size(phoneW, phoneH),
                cornerRadius = CornerRadius(4.dp.toPx(), 4.dp.toPx())
            )

            // Subtle glow beam from phone screen
            drawCircle(
                brush = Brush.radialGradient(
                    colors = listOf(CyanAccent.copy(alpha = 0.30f), Color.Transparent),
                    center = Offset(phoneX + phoneW / 2f, phoneY + phoneH / 2f),
                    radius = 28.dp.toPx()
                ),
                center = Offset(phoneX + phoneW / 2f, phoneY + phoneH / 2f),
                radius = 28.dp.toPx()
            )

            // Phone Bezel
            drawRoundRect(
                color = Color(0xFF0F172A),
                topLeft = Offset(phoneX, phoneY),
                size = Size(phoneW, phoneH),
                cornerRadius = CornerRadius(4.dp.toPx(), 4.dp.toPx())
            )
            // Phone Glowing Screen
            drawRoundRect(
                brush = Brush.verticalGradient(
                    colors = listOf(CyanAccent, TealPrimary)
                ),
                topLeft = Offset(phoneX + 1.5.dp.toPx(), phoneY + 2.dp.toPx()),
                size = Size(phoneW - 3.dp.toPx(), phoneH - 4.dp.toPx()),
                cornerRadius = CornerRadius(2.5.dp.toPx(), 2.5.dp.toPx())
            )
            // Syntax Code Lines on Phone Screen
            drawRoundRect(
                color = Color.White.copy(alpha = 0.90f),
                topLeft = Offset(phoneX + 3.dp.toPx(), phoneY + 6.dp.toPx()),
                size = Size(8.dp.toPx(), 2.dp.toPx()),
                cornerRadius = CornerRadius(1.dp.toPx(), 1.dp.toPx())
            )
            drawRoundRect(
                color = AmberGold,
                topLeft = Offset(phoneX + 5.dp.toPx(), phoneY + 11.dp.toPx()),
                size = Size(9.dp.toPx(), 2.dp.toPx()),
                cornerRadius = CornerRadius(1.dp.toPx(), 1.dp.toPx())
            )
            drawRoundRect(
                color = Color.White.copy(alpha = 0.90f),
                topLeft = Offset(phoneX + 3.dp.toPx(), phoneY + 16.dp.toPx()),
                size = Size(7.dp.toPx(), 2.dp.toPx()),
                cornerRadius = CornerRadius(1.dp.toPx(), 1.dp.toPx())
            )
            drawRoundRect(
                color = FlameOrange,
                topLeft = Offset(phoneX + 4.dp.toPx(), phoneY + 21.dp.toPx()),
                size = Size(10.dp.toPx(), 2.dp.toPx()),
                cornerRadius = CornerRadius(1.dp.toPx(), 1.dp.toPx())
            )
        }

        // 3. Floating Editorial Badges with subtle floating shadow
        // Top Left: Code Tag </>
        Surface(
            modifier = Modifier
                .align(Alignment.TopStart)
                .padding(start = 20.dp, top = 18.dp)
                .shadow(4.dp, RoundedCornerShape(10.dp)),
            shape = RoundedCornerShape(10.dp),
            color = LightSurface,
            border = BorderStroke(1.dp, FlameOrange.copy(alpha = 0.35f))
        ) {
            Row(
                modifier = Modifier.padding(horizontal = 9.dp, vertical = 5.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "</>",
                    fontFamily = FontFamily.Monospace,
                    fontWeight = FontWeight.Bold,
                    fontSize = 13.sp,
                    color = FlameOrange
                )
                Spacer(modifier = Modifier.width(5.dp))
                Text(
                    text = "Kotlin",
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 11.sp,
                    color = LightTextPrimary
                )
            }
        }

        // Top Right: Live streak / active badge
        Surface(
            modifier = Modifier
                .align(Alignment.TopEnd)
                .padding(end = 20.dp, top = 22.dp)
                .shadow(4.dp, RoundedCornerShape(10.dp)),
            shape = RoundedCornerShape(10.dp),
            color = LightSurface,
            border = BorderStroke(1.dp, CyanAccent.copy(alpha = 0.40f))
        ) {
            Row(
                modifier = Modifier.padding(horizontal = 9.dp, vertical = 5.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "{ }",
                    fontFamily = FontFamily.Monospace,
                    fontWeight = FontWeight.Bold,
                    fontSize = 13.sp,
                    color = CyanAccentText
                )
                Spacer(modifier = Modifier.width(5.dp))
                Text(
                    text = stringResource(R.string.login_badge_mobile_dev),
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 11.sp,
                    color = LightTextPrimary
                )
            }
        }
    }
}

/**
 * Form Card dengan permukaan tetap `LightSurface` dan aksen oranye `FlameOrange` / `CyanAccent`.
 * SENGAJA FIXED: Tidak menggunakan MaterialTheme.colorScheme.surface.
 */
@Composable
private fun LoginFormCard(
    email: String,
    onEmailChange: (String) -> Unit,
    password: String,
    onPasswordChange: (String) -> Unit,
    isPasswordVisible: Boolean,
    onTogglePasswordVisibility: () -> Unit,
    isLoading: Boolean,
    errorMessage: String?,
    onSubmit: () -> Unit,
    onForgotPassword: () -> Unit,
    onGoogleSignIn: () -> Unit,
    focusManager: androidx.compose.ui.focus.FocusManager
) {
    val fieldColors = loginFixedFieldColors()

    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(24.dp),
        // SENGAJA FIXED: Permukaan kartu menggunakan LightSurface & LightBorder tetap
        colors = CardDefaults.cardColors(
            containerColor = LightSurface
        ),
        border = BorderStroke(
            1.dp,
            LightBorder
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
    ) {
        Column(modifier = Modifier.fillMaxWidth()) {
            // Garis aksen oranye brand di bagian atas kartu (solid, tanpa gradient rumit)
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(3.dp)
                    .background(FlameOrange)
            )

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(22.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                if (errorMessage != null) {
                    LoginErrorBanner(message = errorMessage)
                }

                LoginEmailField(
                    value = email,
                    onValueChange = onEmailChange,
                    colors = fieldColors,
                    onNext = { focusManager.moveFocus(FocusDirection.Down) }
                )

                Column(modifier = Modifier.fillMaxWidth()) {
                    LoginPasswordField(
                        value = password,
                        onValueChange = onPasswordChange,
                        isVisible = isPasswordVisible,
                        onToggleVisibility = onTogglePasswordVisibility,
                        colors = fieldColors,
                        onDone = {
                            focusManager.clearFocus()
                            onSubmit()
                        }
                    )

                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 6.dp),
                        contentAlignment = Alignment.CenterEnd
                    ) {
                        Text(
                            text = stringResource(R.string.login_forgot_password),
                            style = MaterialTheme.typography.bodySmall,
                            fontWeight = FontWeight.SemiBold,
                            color = CyanAccentText, // SENGAJA FIXED: Aksen oranye kontras di atas cream
                            modifier = Modifier
                                .clickable(onClick = onForgotPassword)
                                .padding(vertical = 4.dp, horizontal = 2.dp)
                                .testTag("forgot_password_link")
                        )
                    }
                }

                // Primary Action: Masuk ke Akun (warna gelap solid dengan teks terang khas editorial)
                LoginSubmitButton(
                    loading = isLoading,
                    onClick = onSubmit
                )

                // Minimalist Divider with clear hierarchy
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 2.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    HorizontalDivider(
                        modifier = Modifier.weight(1f),
                        thickness = 0.8.dp,
                        color = LightBorder
                    )
                    Text(
                        text = stringResource(R.string.login_divider_or),
                        style = MaterialTheme.typography.labelSmall,
                        color = LightTextSecondary,
                        modifier = Modifier.padding(horizontal = 14.dp)
                    )
                    HorizontalDivider(
                        modifier = Modifier.weight(1f),
                        thickness = 0.8.dp,
                        color = LightBorder
                    )
                }

                // Secondary Action: Google Sign-In (slimmer, outlined, clearly secondary)
                OutlinedButton(
                    onClick = onGoogleSignIn,
                    shape = RoundedCornerShape(14.dp),
                    border = BorderStroke(
                        1.dp,
                        LightBorder
                    ),
                    colors = ButtonDefaults.outlinedButtonColors(
                        containerColor = LightSurfaceVariant.copy(alpha = 0.35f)
                    ),
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(44.dp)
                        .testTag("google_signin_button")
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.Center
                    ) {
                        GoogleLogoIcon(modifier = Modifier.size(18.dp))
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = stringResource(R.string.login_google_signin),
                            style = MaterialTheme.typography.bodyMedium,
                            fontWeight = FontWeight.Medium,
                            color = LightTextPrimary // SENGAJA FIXED
                        )
                    }
                }
            }
        }
    }
}

/**
 * SENGAJA FIXED: Warna field input menggunakan token cream (`LightSurface`/`LightSurfaceVariant`)
 * dengan aksen fokus oranye (`FlameOrange`), tidak mengikuti tema gelap sistem/app.
 */
@Composable
private fun loginFixedFieldColors(): TextFieldColors {
    return OutlinedTextFieldDefaults.colors(
        focusedBorderColor = FlameOrange,
        unfocusedBorderColor = LightBorder,
        focusedContainerColor = LightSurface,
        unfocusedContainerColor = LightSurfaceVariant.copy(alpha = 0.45f),
        focusedLabelColor = FlameOrange,
        unfocusedLabelColor = LightTextSecondary,
        focusedTextColor = LightTextPrimary,
        unfocusedTextColor = LightTextPrimary,
        focusedLeadingIconColor = FlameOrange,
        unfocusedLeadingIconColor = LightTextSecondary,
        focusedTrailingIconColor = FlameOrange,
        unfocusedTrailingIconColor = LightTextSecondary,
        focusedPlaceholderColor = LightTextSecondary.copy(alpha = 0.6f),
        unfocusedPlaceholderColor = LightTextSecondary.copy(alpha = 0.6f),
        cursorColor = FlameOrange
    )
}

@Composable
private fun LoginErrorBanner(message: String) {
    Surface(
        color = ErrorRed.copy(alpha = 0.1f),
        shape = RoundedCornerShape(12.dp),
        border = BorderStroke(1.dp, ErrorRed.copy(alpha = 0.3f)),
        modifier = Modifier
            .fillMaxWidth()
            .testTag("login_error_banner")
    ) {
        Row(
            modifier = Modifier.padding(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = Icons.Default.ErrorOutline,
                contentDescription = null,
                tint = ErrorRed,
                modifier = Modifier.size(18.dp)
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                text = message,
                style = MaterialTheme.typography.bodySmall,
                color = ErrorRed,
                fontWeight = FontWeight.SemiBold
            )
        }
    }
}

@Composable
private fun LoginEmailField(
    value: String,
    onValueChange: (String) -> Unit,
    colors: TextFieldColors,
    onNext: () -> Unit
) {
    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        label = { Text(stringResource(R.string.login_email_label)) },
        placeholder = { Text(stringResource(R.string.login_email_placeholder)) },
        leadingIcon = {
            Icon(
                imageVector = Icons.Default.Email,
                contentDescription = null
            )
        },
        singleLine = true,
        keyboardOptions = KeyboardOptions(
            keyboardType = KeyboardType.Email,
            imeAction = ImeAction.Next
        ),
        keyboardActions = KeyboardActions(onNext = { onNext() }),
        shape = RoundedCornerShape(14.dp),
        colors = colors,
        modifier = Modifier
            .fillMaxWidth()
            .testTag("login_email_input")
    )
}

@Composable
private fun LoginPasswordField(
    value: String,
    onValueChange: (String) -> Unit,
    isVisible: Boolean,
    onToggleVisibility: () -> Unit,
    colors: TextFieldColors,
    onDone: () -> Unit
) {
    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        label = { Text(stringResource(R.string.login_password_label)) },
        placeholder = { Text(stringResource(R.string.login_password_placeholder)) },
        leadingIcon = {
            Icon(
                imageVector = Icons.Default.Lock,
                contentDescription = null
            )
        },
        trailingIcon = {
            IconButton(
                onClick = onToggleVisibility,
                modifier = Modifier.testTag("login_password_toggle")
            ) {
                Icon(
                    imageVector = if (isVisible) Icons.Default.Visibility else Icons.Default.VisibilityOff,
                    contentDescription = if (isVisible) stringResource(R.string.login_hide_password) else stringResource(R.string.login_show_password)
                )
            }
        },
        visualTransformation = if (isVisible) VisualTransformation.None else PasswordVisualTransformation(),
        singleLine = true,
        keyboardOptions = KeyboardOptions(
            keyboardType = KeyboardType.Password,
            imeAction = ImeAction.Done
        ),
        keyboardActions = KeyboardActions(onDone = { onDone() }),
        shape = RoundedCornerShape(14.dp),
        colors = colors,
        modifier = Modifier
            .fillMaxWidth()
            .testTag("login_password_input")
    )
}

/**
 * SENGAJA FIXED: Tombol utama Login menggunakan warna gelap solid (`Color(0xFF1E293B)`)
 * dengan teks putih (`Color.White`), memberikan kontras tegas dan bold khas editorial illustrated app.
 */
@Composable
private fun LoginSubmitButton(loading: Boolean, onClick: () -> Unit) {
    Surface(
        onClick = onClick,
        enabled = !loading,
        shape = RoundedCornerShape(16.dp),
        color = Color(0xFF1E293B), // Kontras tegas gelap solid khas gaya editorial
        modifier = Modifier
            .fillMaxWidth()
            .height(52.dp)
            .testTag("login_submit_button")
    ) {
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier.fillMaxSize()
        ) {
            if (loading) {
                CircularProgressIndicator(
                    color = Color.White,
                    strokeWidth = 2.5.dp,
                    modifier = Modifier.size(22.dp)
                )
            } else {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center
                ) {
                    Text(
                        text = stringResource(R.string.login_submit_button),
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowForward,
                        contentDescription = null,
                        tint = Color.White,
                        modifier = Modifier.size(20.dp)
                    )
                }
            }
        }
    }
}

@Composable
private fun LoginFooter(onNavigateToRegister: () -> Unit) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center
    ) {
        Text(
            text = stringResource(R.string.login_footer_no_account),
            style = MaterialTheme.typography.bodyMedium,
            color = LightTextSecondary // SENGAJA FIXED
        )
        TextButton(
            onClick = onNavigateToRegister,
            modifier = Modifier.testTag("navigate_to_register_button")
        ) {
            Text(
                text = stringResource(R.string.login_footer_register_now),
                style = MaterialTheme.typography.bodyMedium,
                fontWeight = FontWeight.Bold,
                color = FlameOrange // SENGAJA FIXED: Aksen oranye
            )
        }
    }
}

/**
 * Clean multi-color Google "G" vector drawn on Canvas for the secondary sign-in button.
 */
@Composable
internal fun GoogleLogoIcon(modifier: Modifier = Modifier) {
    Canvas(modifier = modifier) {
        val strokeWidth = size.minDimension * 0.20f
        val radius = (size.minDimension - strokeWidth) / 2f
        val topLeft = androidx.compose.ui.geometry.Offset(strokeWidth / 2f, strokeWidth / 2f)
        val arcSize = androidx.compose.ui.geometry.Size(radius * 2f, radius * 2f)

        // Red top arc
        drawArc(
            color = Color(0xFFEA4335),
            startAngle = -145f,
            sweepAngle = 105f,
            useCenter = false,
            topLeft = topLeft,
            size = arcSize,
            style = Stroke(width = strokeWidth)
        )
        // Yellow left arc
        drawArc(
            color = Color(0xFFFBBC05),
            startAngle = 145f,
            sweepAngle = 70f,
            useCenter = false,
            topLeft = topLeft,
            size = arcSize,
            style = Stroke(width = strokeWidth)
        )
        // Green bottom arc
        drawArc(
            color = Color(0xFF34A853),
            startAngle = 45f,
            sweepAngle = 100f,
            useCenter = false,
            topLeft = topLeft,
            size = arcSize,
            style = Stroke(width = strokeWidth)
        )
        // Blue right arc + crossbar
        drawArc(
            color = Color(0xFF4285F4),
            startAngle = -10f,
            sweepAngle = 55f,
            useCenter = false,
            topLeft = topLeft,
            size = arcSize,
            style = Stroke(width = strokeWidth)
        )
        drawRect(
            color = Color(0xFF4285F4),
            topLeft = androidx.compose.ui.geometry.Offset(size.width * 0.50f, size.height * 0.42f),
            size = androidx.compose.ui.geometry.Size(size.width * 0.45f, strokeWidth * 0.90f)
        )
    }
}

@Preview(name = "Login - Editorial Illustrated Theme (Light)", showBackground = true, uiMode = Configuration.UI_MODE_NIGHT_NO)
@Composable
private fun LoginScreenLightPreview() {
    CodeAcademyTheme(darkTheme = false) {
        LoginScreen(
            onLoginSubmit = { _, _ -> Result.success(Unit) },
            onNavigateToRegister = {}
        )
    }
}

@Preview(name = "Login - Editorial Illustrated Theme (Dark System)", showBackground = true, uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
private fun LoginScreenDarkPreview() {
    CodeAcademyTheme(darkTheme = true) {
        LoginScreen(
            onLoginSubmit = { _, _ -> Result.success(Unit) },
            onNavigateToRegister = {}
        )
    }
}
