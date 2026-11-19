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
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.ErrorOutline
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Person
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
import androidx.compose.ui.focus.FocusManager
import androidx.compose.ui.focus.onFocusChanged
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
import com.example.ui.theme.ErrorRed
import com.example.ui.theme.FlameOrange
import com.example.ui.theme.LightBg
import com.example.ui.theme.LightBorder
import com.example.ui.theme.LightSurface
import com.example.ui.theme.LightSurfaceVariant
import com.example.ui.theme.LightTextPrimary
import com.example.ui.theme.LightTextSecondary
import com.example.ui.theme.TealPrimary
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

// ========================================================================================
// CATATAN ARSITEKTUR TEMA — SENGAJA FIXED (JANGAN DIUBAH KE MaterialTheme.colorScheme)
// ========================================================================================
// RegisterScreen dan LoginScreen SENGAJA menggunakan tema warna tetap (Fixed Brand Theme:
// latar hangat Cream `LightBg`/`LightSurface` + aksen Oranye `FlameOrange`/`CyanAccent`)
// dan TIDAK mengikuti Dark Mode sistem maupun toggle tema aplikasi.
//
// Desain visual: Editorial Illustrated Style (karakter flat vector Canvas/Path + headline besar
// dengan highlight warna oranye + tombol CTA solid gelap kontras tegas).
// ========================================================================================

@Composable
fun RegisterScreen(
    onRegisterSubmit: suspend (name: String, email: String, password: String) -> Result<Unit>,
    onNavigateToLogin: () -> Unit,
    onGoogleSignIn: () -> Unit = {},
    isGoogleLoading: Boolean = false,
    modifier: Modifier = Modifier
) {
    // Pastikan ikon status bar & navigation bar selalu gelap (terbaca jelas di atas latar cream LightBg),
    // meskipun perangkat atau aplikasi sedang berada dalam mode gelap (Dark Mode).
    val view = LocalView.current
    if (!view.isInEditMode) {
        SideEffect {
            val window = (view.context as? Activity)?.window ?: return@SideEffect
            val insetsController = WindowCompat.getInsetsController(window, view)
            insetsController.isAppearanceLightStatusBars = true
            insetsController.isAppearanceLightNavigationBars = true
        }
    }

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

    // Staged entrance animation states (serupa dengan LoginScreen)
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

    var name by rememberSaveable { mutableStateOf("") }
    var email by rememberSaveable { mutableStateOf("") }
    var password by rememberSaveable { mutableStateOf("") }
    var confirmPassword by rememberSaveable { mutableStateOf("") }
    var isPasswordVisible by rememberSaveable { mutableStateOf(false) }
    var isConfirmPasswordVisible by rememberSaveable { mutableStateOf(false) }
    var isLoading by remember { mutableStateOf(false) }
    var errorMessage by rememberSaveable { mutableStateOf<String?>(null) }

    val focusManager = LocalFocusManager.current
    val coroutineScope = rememberCoroutineScope()
    val density = LocalDensity.current

    val errorEmpty = stringResource(R.string.register_error_empty_fields)
    val errorNameMinLength = stringResource(R.string.register_error_name_min_length)
    val errorInvalidEmail = stringResource(R.string.register_error_invalid_email)
    val errorPasswordMinLength = stringResource(R.string.register_error_password_min_length)
    val errorPasswordMismatch = stringResource(R.string.register_error_password_mismatch)
    val errorDefaultFailure = stringResource(R.string.register_error_default_failure)

    val submitRegister: () -> Unit = {
        val trimmedName = name.trim()
        val trimmedEmail = email.trim()
        when {
            trimmedName.isEmpty() || trimmedEmail.isEmpty() || password.isEmpty() -> {
                errorMessage = errorEmpty
            }
            trimmedName.length < 2 -> {
                errorMessage = errorNameMinLength
            }
            !trimmedEmail.contains("@") || !trimmedEmail.contains(".") -> {
                errorMessage = errorInvalidEmail
            }
            password.length < 6 -> {
                errorMessage = errorPasswordMinLength
            }
            password != confirmPassword -> {
                errorMessage = errorPasswordMismatch
            }
            else -> {
                isLoading = true
                errorMessage = null
                coroutineScope.launch {
                    val result = onRegisterSubmit(trimmedName, trimmedEmail, password)
                    isLoading = false
                    result.onFailure { err ->
                        errorMessage = err.message ?: errorDefaultFailure
                    }
                }
            }
        }
    }

    // SENGAJA FIXED: Latar belakang layar selalu menggunakan LightBg (Cream 0xFFFAF7F2)
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
                .padding(horizontal = 16.dp)
                .padding(top = 16.dp, bottom = 32.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // 1. Editorial Hero Section: Karakter flat vector baru (menulis/mendaftar) + Headline besar berani
            RegisterHeroHeader(
                isHeaderVisible = isHeaderVisible,
                isHeadlineVisible = isHeadlineVisible,
                areAnimationsEnabled = areAnimationsEnabled
            )

            Spacer(modifier = Modifier.height(18.dp))

            // 2. Register Form Card dengan staged slideUp + fade-in
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
                RegisterFormCard(
                    name = name,
                    onNameChange = {
                        name = it
                        errorMessage = null
                    },
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
                    confirmPassword = confirmPassword,
                    onConfirmPasswordChange = {
                        confirmPassword = it
                        errorMessage = null
                    },
                    isPasswordVisible = isPasswordVisible,
                    onTogglePasswordVisibility = { isPasswordVisible = !isPasswordVisible },
                    isConfirmPasswordVisible = isConfirmPasswordVisible,
                    onToggleConfirmPasswordVisibility = { isConfirmPasswordVisible = !isConfirmPasswordVisible },
                    isLoading = isLoading,
                    errorMessage = errorMessage,
                    onSubmit = submitRegister,
                    onGoogleSignIn = onGoogleSignIn,
                    isGoogleLoading = isGoogleLoading,
                    focusManager = focusManager
                )
            }

            Spacer(modifier = Modifier.height(24.dp))

            // 3. Footer link to Login
            RegisterFooter(onNavigateToLogin = onNavigateToLogin)
        }
    }
}

/**
 * Editorial Hero Header untuk Register:
 * - Ilustrasi karakter flat vector baru: siswa sedang mendaftar/menulis di meja belajar dengan stylus & form checklist.
 * - Headline besar berani: "Mulai perjalanan codingmu." dengan "codingmu." ter-highlight warna FlameOrange.
 * - Konsisten satu keluarga visual dengan LoginScreen (solid colors, cream LightBg, aksen FlameOrange & CyanAccent).
 */
@Composable
private fun RegisterHeroHeader(
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

        // 1. Flat Vector Character Illustration (Pose Mendaftar / Menulis)
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
                size = 120.dp,
                mood = MascotMood.CODING,
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
            val headlinePrefix = stringResource(R.string.register_headline_prefix)
            val headlineHighlight = stringResource(R.string.register_headline_highlight)
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
                    text = stringResource(R.string.register_subtitle),
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
 * Ilustrasi Karakter Flat Vector Mendaftar / Menulis di Meja:
 * Menggunakan teknik Canvas + Path yang konsisten dengan `OnboardingIllustrations.kt` dan `LoginHeroIllustration`
 * (proporsi kepala ~21dp, skin color `Color(0xFFFFDFBA)`, rambut `Color(0xFF1E293B)`,
 * pakaian solid FlameOrange, meja belajar minimalis, formulir pendaftaran interaktif, dan stylus CyanAccent).
 */
@Composable
fun RegisterHeroIllustration(modifier: Modifier = Modifier) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .height(210.dp),
        contentAlignment = Alignment.Center
    ) {
        // 1. Ambient Background Shapes & Character Canvas (Solid flat colors, editorial aesthetic)
        Canvas(modifier = Modifier.fillMaxSize()) {
            val w = size.width
            val h = size.height
            val centerOffset = Offset(w * 0.50f, h * 0.54f)
            val glowRadius = h * 0.44f

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
                radius = glowRadius * 1.08f,
                style = Stroke(width = 1.5.dp.toPx())
            )

            // Floating decorative geometric accents (editorial style)
            // 1. Soft outline ring (top-left near halo)
            drawCircle(
                color = FlameOrange.copy(alpha = 0.35f),
                center = Offset(w * 0.23f, h * 0.28f),
                radius = 5.dp.toPx(),
                style = Stroke(width = 1.5.dp.toPx())
            )
            // Small solid accent dot (top-left)
            drawCircle(
                color = FlameOrange.copy(alpha = 0.75f),
                center = Offset(w * 0.19f, h * 0.23f),
                radius = 2.5.dp.toPx()
            )

            // 2. Diamond accent near halo rim (top-right of character)
            val diamondCenter = Offset(w * 0.71f, h * 0.25f)
            val dWidth = 4.dp.toPx()
            val dHeight = 6.dp.toPx()
            val diamondPath = Path().apply {
                moveTo(diamondCenter.x, diamondCenter.y - dHeight)
                lineTo(diamondCenter.x + dWidth, diamondCenter.y)
                lineTo(diamondCenter.x, diamondCenter.y + dHeight)
                lineTo(diamondCenter.x - dWidth, diamondCenter.y)
                close()
            }
            drawPath(path = diamondPath, color = CyanAccent.copy(alpha = 0.80f))

            // 3. Warm solid accent dot (mid-right)
            drawCircle(
                color = FlameOrange.copy(alpha = 0.65f),
                center = Offset(w * 0.76f, h * 0.48f),
                radius = 3.5.dp.toPx()
            )

            // 4. Dot accent left
            drawCircle(
                color = CyanAccent.copy(alpha = 0.60f),
                center = Offset(w * 0.22f, h * 0.50f),
                radius = 3.dp.toPx()
            )

            // 5. Additional floating geometric accents (editorial style)
            drawCircle(
                color = CyanAccent.copy(alpha = 0.25f),
                center = Offset(w * 0.80f, h * 0.38f),
                radius = 5.dp.toPx(),
                style = Stroke(width = 1.5.dp.toPx())
            )
            drawCircle(
                color = AmberGold.copy(alpha = 0.50f),
                center = Offset(w * 0.32f, h * 0.18f),
                radius = 3.dp.toPx()
            )
            // Delicate half-circle arc at mid-left
            drawArc(
                color = CyanAccent.copy(alpha = 0.28f),
                startAngle = 60f,
                sweepAngle = 180f,
                useCenter = false,
                topLeft = Offset(w * 0.13f, h * 0.42f),
                size = Size(11.dp.toPx(), 11.dp.toPx()),
                style = Stroke(1.5.dp.toPx(), cap = StrokeCap.Round)
            )
            // Subtle half-circle arc at top-right
            drawArc(
                color = FlameOrange.copy(alpha = 0.28f),
                startAngle = 210f,
                sweepAngle = 180f,
                useCenter = false,
                topLeft = Offset(w * 0.84f, h * 0.26f),
                size = Size(10.dp.toPx(), 10.dp.toPx()),
                style = Stroke(1.5.dp.toPx(), cap = StrokeCap.Round)
            )
            // Soft mini outline dot near lower right desk
            drawCircle(
                color = CyanAccent.copy(alpha = 0.25f),
                center = Offset(w * 0.86f, h * 0.60f),
                radius = 4.dp.toPx(),
                style = Stroke(1.2.dp.toPx())
            )
        }

        // 2. Vector Character & Workspace via Canvas
        Canvas(
            modifier = Modifier
                .size(240.dp, 190.dp)
                .align(Alignment.BottomCenter)
        ) {
            val w = size.width
            val h = size.height

            // --- 1. Study Chair Backrest & Seat (Behind Character) ---
            drawRoundRect(
                color = Color(0xFF1E293B),
                topLeft = Offset(w * 0.33f, h * 0.40f),
                size = Size(w * 0.34f, h * 0.38f),
                cornerRadius = CornerRadius(14.dp.toPx(), 14.dp.toPx())
            )
            // Chair seat cushion edge
            drawRoundRect(
                color = Color(0xFF334155),
                topLeft = Offset(w * 0.30f, h * 0.70f),
                size = Size(w * 0.40f, 16.dp.toPx()),
                cornerRadius = CornerRadius(4.dp.toPx(), 4.dp.toPx())
            )

            // --- 2. Character Torso & Clothes (Extending naturally behind desk) with Volume Gradient ---
            // Torso / Hoodie in FlameOrange
            val bodyPath = Path().apply {
                moveTo(w * 0.41f, h * 0.45f) // shoulder left
                lineTo(w * 0.34f, h * 0.84f) // bottom left (extends well behind desk)
                lineTo(w * 0.66f, h * 0.84f) // bottom right (extends well behind desk)
                lineTo(w * 0.59f, h * 0.45f) // shoulder right
                close()
            }
            drawPath(
                path = bodyPath,
                brush = Brush.verticalGradient(
                    colors = listOf(Color(0xFFFF6D3B), FlameOrange, Color(0xFFC2410C)),
                    startY = h * 0.45f,
                    endY = h * 0.84f
                )
            )

            // Hoodie Collar Detail
            drawCircle(
                color = Color(0xFFC2410C),
                center = Offset(w * 0.50f, h * 0.45f),
                radius = 9.dp.toPx()
            )

            // Hoodie Drawstrings (White)
            drawLine(
                color = Color.White.copy(alpha = 0.90f),
                start = Offset(w * 0.485f, h * 0.46f),
                end = Offset(w * 0.485f, h * 0.53f),
                strokeWidth = 2.dp.toPx(),
                cap = StrokeCap.Round
            )
            drawLine(
                color = Color.White.copy(alpha = 0.90f),
                start = Offset(w * 0.515f, h * 0.46f),
                end = Offset(w * 0.515f, h * 0.53f),
                strokeWidth = 2.dp.toPx(),
                cap = StrokeCap.Round
            )

            // --- 3. Neck & Head ---
            // Neck
            drawRect(
                color = Color(0xFFFFDFBA),
                topLeft = Offset(w * 0.47f, h * 0.38f),
                size = Size(w * 0.06f, h * 0.08f)
            )

            // Head / Face
            drawCircle(
                color = Color(0xFFFFDFBA),
                center = Offset(w * 0.50f, h * 0.33f),
                radius = 21.dp.toPx()
            )
            // Left ear
            drawCircle(
                color = Color(0xFFFFDFBA),
                center = Offset(w * 0.42f, h * 0.34f),
                radius = 4.5.dp.toPx()
            )
            // Right ear
            drawCircle(
                color = Color(0xFFFFDFBA),
                center = Offset(w * 0.58f, h * 0.34f),
                radius = 4.5.dp.toPx()
            )

            // Hair (Stylized modern silhouette with smooth continuous bezier curves and depth gradient)
            val hairPath = Path().apply {
                moveTo(w * 0.41f, h * 0.34f)
                cubicTo(w * 0.38f, h * 0.19f, w * 0.62f, h * 0.19f, w * 0.59f, h * 0.32f)
                quadraticTo(w * 0.50f, h * 0.25f, w * 0.41f, h * 0.34f)
                close()
            }
            drawPath(
                path = hairPath,
                brush = Brush.verticalGradient(
                    colors = listOf(Color(0xFF334155), Color(0xFF1E293B), Color(0xFF0F172A)),
                    startY = h * 0.19f,
                    endY = h * 0.34f
                )
            )

            // Cheerful, focused expression looking down at the registration form
            // Left eye arc
            drawArc(
                color = Color(0xFF334155),
                startAngle = 180f,
                sweepAngle = 180f,
                useCenter = false,
                topLeft = Offset(w * 0.465f, h * 0.325f),
                size = Size(w * 0.032f, 3.5.dp.toPx()),
                style = Stroke(width = 1.8.dp.toPx(), cap = StrokeCap.Round)
            )
            // Right eye arc
            drawArc(
                color = Color(0xFF334155),
                startAngle = 180f,
                sweepAngle = 180f,
                useCenter = false,
                topLeft = Offset(w * 0.505f, h * 0.325f),
                size = Size(w * 0.032f, 3.5.dp.toPx()),
                style = Stroke(width = 1.8.dp.toPx(), cap = StrokeCap.Round)
            )
            // Warm smile
            drawArc(
                color = Color(0xFFE2A77E),
                startAngle = 15f,
                sweepAngle = 150f,
                useCenter = false,
                topLeft = Offset(w * 0.48f, h * 0.35f),
                size = Size(w * 0.045f, 4.5.dp.toPx()),
                style = Stroke(width = 1.8.dp.toPx(), cap = StrokeCap.Round)
            )
            // Friendly blush
            drawCircle(
                color = Color(0xFFFFB4A2).copy(alpha = 0.55f),
                center = Offset(w * 0.455f, h * 0.355f),
                radius = 3.2.dp.toPx()
            )
            drawCircle(
                color = Color(0xFFFFB4A2).copy(alpha = 0.55f),
                center = Offset(w * 0.545f, h * 0.355f),
                radius = 3.2.dp.toPx()
            )

            // --- 4. Study Desk & Workspace (Foreground Layer) ---
            // Minimalist modern study desk with soft gradient depth
            drawRoundRect(
                brush = Brush.verticalGradient(
                    colors = listOf(Color(0xFFF8F5EE), Color(0xFFEFE9DF)),
                    startY = h * 0.74f,
                    endY = h * 0.94f
                ),
                topLeft = Offset(w * 0.16f, h * 0.74f),
                size = Size(w * 0.68f, h * 0.20f),
                cornerRadius = CornerRadius(12.dp.toPx())
            )
            // Desk top edge line
            drawLine(
                color = Color(0xFFDFD7CA),
                start = Offset(w * 0.17f, h * 0.74f),
                end = Offset(w * 0.83f, h * 0.74f),
                strokeWidth = 2.dp.toPx()
            )

            // Cute small coffee/tea cup on desk (left)
            drawRoundRect(
                color = CyanAccent,
                topLeft = Offset(w * 0.22f, h * 0.69f),
                size = Size(w * 0.065f, h * 0.09f),
                cornerRadius = CornerRadius(4.dp.toPx())
            )
            drawRect(
                color = Color(0xFF0E7490),
                topLeft = Offset(w * 0.22f, h * 0.72f),
                size = Size(w * 0.065f, h * 0.035f)
            )
            // Steam
            drawArc(
                color = FlameOrange.copy(alpha = 0.55f),
                startAngle = 180f,
                sweepAngle = 180f,
                useCenter = false,
                topLeft = Offset(w * 0.235f, h * 0.635f),
                size = Size(w * 0.035f, 4.dp.toPx()),
                style = Stroke(1.5.dp.toPx(), cap = StrokeCap.Round)
            )

            // Digital Tablet / Registration Form Sheet on the desk
            // Multi-layered soft drop shadow for the registration sheet
            drawRoundRect(
                color = Color.Black.copy(alpha = 0.04f),
                topLeft = Offset(w * 0.36f - 1.dp.toPx(), h * 0.64f + 1.dp.toPx()),
                size = Size(w * 0.28f + 2.dp.toPx(), h * 0.22f + 3.dp.toPx()),
                cornerRadius = CornerRadius(8.dp.toPx())
            )
            drawRoundRect(
                color = Color.Black.copy(alpha = 0.08f),
                topLeft = Offset(w * 0.36f + 1.dp.toPx(), h * 0.64f + 2.dp.toPx()),
                size = Size(w * 0.28f, h * 0.22f),
                cornerRadius = CornerRadius(6.dp.toPx())
            )
            // Base sheet
            drawRoundRect(
                color = Color.White,
                topLeft = Offset(w * 0.36f, h * 0.64f),
                size = Size(w * 0.28f, h * 0.22f),
                cornerRadius = CornerRadius(6.dp.toPx())
            )
            // Crisp cyan border for the registration sheet
            drawRoundRect(
                color = CyanAccent.copy(alpha = 0.50f),
                topLeft = Offset(w * 0.36f, h * 0.64f),
                size = Size(w * 0.28f, h * 0.22f),
                cornerRadius = CornerRadius(6.dp.toPx()),
                style = Stroke(1.5.dp.toPx())
            )

            // Header accent bar on registration form
            drawLine(
                color = FlameOrange,
                start = Offset(w * 0.39f, h * 0.68f),
                end = Offset(w * 0.48f, h * 0.68f),
                strokeWidth = 2.5.dp.toPx(),
                cap = StrokeCap.Round
            )
            // Checklist bullet 1
            drawCircle(
                color = CyanAccent,
                center = Offset(w * 0.40f, h * 0.73f),
                radius = 2.5.dp.toPx()
            )
            // Form line 1
            drawLine(
                color = Color(0xFFCBD5E1),
                start = Offset(w * 0.43f, h * 0.73f),
                end = Offset(w * 0.59f, h * 0.73f),
                strokeWidth = 2.dp.toPx(),
                cap = StrokeCap.Round
            )
            // Checklist bullet 2
            drawCircle(
                color = FlameOrange,
                center = Offset(w * 0.40f, h * 0.78f),
                radius = 2.5.dp.toPx()
            )
            // Form line 2
            drawLine(
                color = Color(0xFFCBD5E1),
                start = Offset(w * 0.43f, h * 0.78f),
                end = Offset(w * 0.55f, h * 0.78f),
                strokeWidth = 2.dp.toPx(),
                cap = StrokeCap.Round
            )

            // --- 5. Arms & Hands Active in Writing / Registering (Directional Gradients) ---
            // Left arm (resting on left side of the registration sheet)
            val leftArm = Path().apply {
                moveTo(w * 0.41f, h * 0.48f)
                lineTo(w * 0.33f, h * 0.66f)
                lineTo(w * 0.37f, h * 0.68f)
                lineTo(w * 0.44f, h * 0.51f)
                close()
            }
            drawPath(
                path = leftArm,
                brush = Brush.linearGradient(
                    colors = listOf(Color(0xFFFF6D3B), FlameOrange),
                    start = Offset(w * 0.41f, h * 0.48f),
                    end = Offset(w * 0.33f, h * 0.66f)
                )
            )
            // Left hand holding down the paper
            drawCircle(
                color = Color(0xFFFFDFBA),
                center = Offset(w * 0.35f, h * 0.67f),
                radius = 5.dp.toPx()
            )

            // Right arm (extending toward the registration sheet, holding a stylus)
            val rightArm = Path().apply {
                moveTo(w * 0.59f, h * 0.48f)
                lineTo(w * 0.51f, h * 0.67f)
                lineTo(w * 0.55f, h * 0.69f)
                lineTo(w * 0.63f, h * 0.51f)
                close()
            }
            drawPath(
                path = rightArm,
                brush = Brush.linearGradient(
                    colors = listOf(Color(0xFFFF6D3B), FlameOrange, Color(0xFFC2410C)),
                    start = Offset(w * 0.59f, h * 0.48f),
                    end = Offset(w * 0.51f, h * 0.67f)
                )
            )
            // Right hand
            drawCircle(
                color = Color(0xFFFFDFBA),
                center = Offset(w * 0.53f, h * 0.68f),
                radius = 5.dp.toPx()
            )

            // Stylus / Pen (CyanAccent angled actively writing onto form)
            drawLine(
                color = CyanAccent,
                start = Offset(w * 0.55f, h * 0.65f),
                end = Offset(w * 0.48f, h * 0.73f),
                strokeWidth = 2.5.dp.toPx(),
                cap = StrokeCap.Round
            )
            // Stylus tip
            drawCircle(
                color = Color(0xFF0E7490),
                center = Offset(w * 0.48f, h * 0.73f),
                radius = 1.5.dp.toPx()
            )
        }

        // 2. Editorial Pill Badges with subtle floating shadow
        // Bottom-right badge: New Account / Verified pass
        Surface(
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(end = 12.dp, bottom = 12.dp)
                .shadow(4.dp, RoundedCornerShape(10.dp)),
            shape = RoundedCornerShape(10.dp),
            color = LightSurface,
            border = BorderStroke(1.dp, FlameOrange.copy(alpha = 0.35f))
        ) {
            Row(
                modifier = Modifier.padding(horizontal = 9.dp, vertical = 5.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = Icons.Default.CheckCircle,
                    contentDescription = null,
                    tint = FlameOrange,
                    modifier = Modifier.size(13.dp)
                )
                Spacer(modifier = Modifier.width(5.dp))
                Text(
                    text = stringResource(R.string.register_badge_new_account),
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 11.sp,
                    color = LightTextPrimary
                )
            }
        }
    }
}

/**
 * Form Card yang membungkus field Nama, Email, Sandi, Konfirmasi Sandi, Banner Error, dan Tombol Daftar.
 * Mengikuti pola LoginFormCard (solid 3dp FlameOrange brand bar, border LightBorder, background LightSurface).
 * SENGAJA FIXED: Tidak menggunakan MaterialTheme.colorScheme.surface.
 */
@Composable
private fun RegisterFormCard(
    name: String,
    onNameChange: (String) -> Unit,
    email: String,
    onEmailChange: (String) -> Unit,
    password: String,
    onPasswordChange: (String) -> Unit,
    confirmPassword: String,
    onConfirmPasswordChange: (String) -> Unit,
    isPasswordVisible: Boolean,
    onTogglePasswordVisibility: () -> Unit,
    isConfirmPasswordVisible: Boolean,
    onToggleConfirmPasswordVisibility: () -> Unit,
    isLoading: Boolean,
    errorMessage: String?,
    onSubmit: () -> Unit,
    onGoogleSignIn: () -> Unit = {},
    isGoogleLoading: Boolean = false,
    focusManager: FocusManager,
    modifier: Modifier = Modifier
) {
    val fieldColors = registerFixedFieldColors()

    Card(
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(24.dp),
        colors = CardDefaults.cardColors(containerColor = LightSurface),
        border = BorderStroke(1.dp, LightBorder),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
    ) {
        Column(modifier = Modifier.fillMaxWidth()) {
            // Garis aksen oranye brand di bagian atas kartu (solid, tanpa gradient rumit — konsisten LoginFormCard)
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
                // Error Banner
                if (errorMessage != null) {
                    RegisterErrorBanner(message = errorMessage)
                }

                // Name Field
                OutlinedTextField(
                    value = name,
                    onValueChange = onNameChange,
                    label = { Text(stringResource(R.string.register_name_label)) },
                    placeholder = { Text(stringResource(R.string.register_name_placeholder)) },
                    leadingIcon = {
                        Icon(
                            imageVector = Icons.Default.Person,
                            contentDescription = null
                        )
                    },
                    singleLine = true,
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Text,
                        imeAction = ImeAction.Next
                    ),
                    keyboardActions = KeyboardActions(
                        onNext = { focusManager.moveFocus(FocusDirection.Down) }
                    ),
                    shape = RoundedCornerShape(14.dp),
                    colors = fieldColors,
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("register_name_input")
                )

                // Email Field
                OutlinedTextField(
                    value = email,
                    onValueChange = onEmailChange,
                    label = { Text(stringResource(R.string.register_email_label)) },
                    placeholder = { Text(stringResource(R.string.register_email_placeholder)) },
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
                    keyboardActions = KeyboardActions(
                        onNext = { focusManager.moveFocus(FocusDirection.Down) }
                    ),
                    shape = RoundedCornerShape(14.dp),
                    colors = fieldColors,
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("register_email_input")
                )

                // Password Field
                OutlinedTextField(
                    value = password,
                    onValueChange = onPasswordChange,
                    label = { Text(stringResource(R.string.register_password_label)) },
                    placeholder = { Text(stringResource(R.string.register_password_placeholder)) },
                    leadingIcon = {
                        Icon(
                            imageVector = Icons.Default.Lock,
                            contentDescription = null
                        )
                    },
                    trailingIcon = {
                        IconButton(
                            onClick = onTogglePasswordVisibility,
                            modifier = Modifier.testTag("register_password_toggle")
                        ) {
                            Icon(
                                imageVector = if (isPasswordVisible) Icons.Default.Visibility else Icons.Default.VisibilityOff,
                                contentDescription = if (isPasswordVisible) stringResource(R.string.register_hide_password) else stringResource(R.string.register_show_password)
                            )
                        }
                    },
                    visualTransformation = if (isPasswordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                    singleLine = true,
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Password,
                        imeAction = ImeAction.Next
                    ),
                    keyboardActions = KeyboardActions(
                        onNext = { focusManager.moveFocus(FocusDirection.Down) }
                    ),
                    shape = RoundedCornerShape(14.dp),
                    colors = fieldColors,
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("register_password_input")
                )

                // Confirm Password Field
                OutlinedTextField(
                    value = confirmPassword,
                    onValueChange = onConfirmPasswordChange,
                    label = { Text(stringResource(R.string.register_confirm_password_label)) },
                    placeholder = { Text(stringResource(R.string.register_confirm_password_placeholder)) },
                    leadingIcon = {
                        Icon(
                            imageVector = Icons.Default.Lock,
                            contentDescription = null
                        )
                    },
                    trailingIcon = {
                        IconButton(
                            onClick = onToggleConfirmPasswordVisibility,
                            modifier = Modifier.testTag("register_confirm_password_toggle")
                        ) {
                            Icon(
                                imageVector = if (isConfirmPasswordVisible) Icons.Default.Visibility else Icons.Default.VisibilityOff,
                                contentDescription = if (isConfirmPasswordVisible) stringResource(R.string.register_confirm_hide_password) else stringResource(R.string.register_confirm_show_password)
                            )
                        }
                    },
                    visualTransformation = if (isConfirmPasswordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                    singleLine = true,
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Password,
                        imeAction = ImeAction.Done
                    ),
                    keyboardActions = KeyboardActions(
                        onDone = {
                            focusManager.clearFocus()
                            onSubmit()
                        }
                    ),
                    shape = RoundedCornerShape(14.dp),
                    colors = fieldColors,
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("register_confirm_password_input")
                )

                Spacer(modifier = Modifier.height(4.dp))

                // Primary Action: Daftar Akun (warna gelap solid dengan teks terang, konsisten dengan LoginSubmitButton)
                RegisterSubmitButton(
                    loading = isLoading,
                    onClick = onSubmit
                )

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

                OutlinedButton(
                    onClick = onGoogleSignIn,
                    enabled = !isLoading && !isGoogleLoading,
                    shape = RoundedCornerShape(14.dp),
                    border = BorderStroke(1.dp, LightBorder),
                    colors = ButtonDefaults.outlinedButtonColors(
                        containerColor = LightSurfaceVariant.copy(alpha = 0.35f)
                    ),
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(44.dp)
                        .testTag("register_google_signin_button")
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.Center
                    ) {
                        if (isGoogleLoading) {
                            CircularProgressIndicator(
                                modifier = Modifier.size(18.dp),
                                strokeWidth = 2.dp,
                                color = TealPrimary
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = stringResource(R.string.google_auth_processing),
                                style = MaterialTheme.typography.bodyMedium,
                                fontWeight = FontWeight.Medium,
                                color = LightTextPrimary
                            )
                        } else {
                            GoogleLogoIcon(modifier = Modifier.size(18.dp))
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = stringResource(R.string.register_google_signin),
                                style = MaterialTheme.typography.bodyMedium,
                                fontWeight = FontWeight.Medium,
                                color = LightTextPrimary
                            )
                        }
                    }
                }
            }
        }
    }
}

/**
 * SENGAJA FIXED: Tombol utama Daftar Akun menggunakan warna gelap solid (`Color(0xFF1E293B)`)
 * dengan teks putih (`Color.White`), memberikan kontras tegas dan bold khas editorial illustrated app,
 * sama persis dengan tombol `LoginSubmitButton`.
 */
@Composable
private fun RegisterSubmitButton(loading: Boolean, onClick: () -> Unit) {
    Surface(
        onClick = onClick,
        enabled = !loading,
        shape = RoundedCornerShape(16.dp),
        color = Color(0xFF1E293B), // Kontras tegas gelap solid khas gaya editorial
        modifier = Modifier
            .fillMaxWidth()
            .height(52.dp)
            .testTag("register_submit_button")
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
                        text = stringResource(R.string.register_submit_button),
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
private fun RegisterErrorBanner(message: String) {
    Surface(
        color = ErrorRed.copy(alpha = 0.1f),
        shape = RoundedCornerShape(12.dp),
        border = BorderStroke(1.dp, ErrorRed.copy(alpha = 0.3f)),
        modifier = Modifier
            .fillMaxWidth()
            .testTag("register_error_banner")
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
private fun RegisterFooter(
    onNavigateToLogin: () -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center
    ) {
        Text(
            text = stringResource(R.string.register_footer_has_account),
            style = MaterialTheme.typography.bodyMedium,
            color = LightTextSecondary // SENGAJA FIXED
        )
        TextButton(onClick = onNavigateToLogin) {
            Text(
                text = stringResource(R.string.register_footer_login_link),
                style = MaterialTheme.typography.bodyMedium,
                fontWeight = FontWeight.Bold,
                color = FlameOrange
            )
        }
    }
}

/**
 * SENGAJA FIXED: Warna field input menggunakan token cream (`LightSurface`/`LightSurfaceVariant`)
 * dengan aksen fokus oranye (`FlameOrange`), tidak mengikuti tema gelap sistem/app.
 */
@Composable
private fun registerFixedFieldColors(): TextFieldColors {
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

@Preview(name = "Register - Fixed Brand Theme (Light System)", showBackground = true, uiMode = Configuration.UI_MODE_NIGHT_NO)
@Composable
private fun RegisterScreenLightPreview() {
    CodeAcademyTheme(darkTheme = false) {
        RegisterScreen(
            onRegisterSubmit = { _, _, _ -> Result.success(Unit) },
            onNavigateToLogin = {}
        )
    }
}

@Preview(name = "Register - Fixed Brand Theme (Dark System)", showBackground = true, uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
private fun RegisterScreenDarkPreview() {
    CodeAcademyTheme(darkTheme = true) {
        RegisterScreen(
            onRegisterSubmit = { _, _, _ -> Result.success(Unit) },
            onNavigateToLogin = {}
        )
    }
}
