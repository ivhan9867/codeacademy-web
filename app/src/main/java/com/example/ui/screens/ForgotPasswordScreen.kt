package com.example.ui.screens

import android.app.Activity
import android.content.res.Configuration
import android.provider.Settings
import androidx.activity.compose.BackHandler
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.slideInVertically
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.ErrorOutline
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.LockReset
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Shield
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
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
import androidx.compose.ui.draw.clip
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
import androidx.compose.ui.zIndex
import androidx.core.view.WindowCompat
import com.example.data.CodeAcademyRepository
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
import com.example.ui.theme.SuccessGreen
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

// ========================================================================================
// CATATAN ARSITEKTUR TEMA — SENGAJA FIXED (JANGAN DIUBAH KE MaterialTheme.colorScheme)
// ========================================================================================
// ForgotPasswordScreen, RegisterScreen, dan LoginScreen SENGAJA menggunakan tema warna tetap
// (Fixed Brand Theme: latar hangat Cream `LightBg`/`LightSurface` + aksen Oranye `FlameOrange`/`CyanAccent`)
// dan TIDAK mengikuti Dark Mode sistem maupun toggle tema aplikasi.
//
// Desain visual: Editorial Illustrated Style (karakter flat vector Canvas/Path + headline besar
// dengan highlight warna oranye + tombol CTA solid gelap kontras tegas).
// ========================================================================================

@Composable
fun ForgotPasswordScreen(
    onNavigateBack: () -> Unit,
    modifier: Modifier = Modifier,
    onResetPasswordSubmit: (suspend (email: String, displayName: String, newPassword: String) -> Result<Unit>)? = null
) {
    BackHandler(onBack = onNavigateBack)

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

    // Staged entrance animation states (serupa dengan LoginScreen & RegisterScreen)
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

    val repository = remember(context) { CodeAcademyRepository(context) }
    val coroutineScope = rememberCoroutineScope()
    val focusManager = LocalFocusManager.current
    val density = LocalDensity.current

    // Form states
    var email by rememberSaveable { mutableStateOf("") }
    var displayName by rememberSaveable { mutableStateOf("") }
    var newPassword by rememberSaveable { mutableStateOf("") }
    var confirmPassword by rememberSaveable { mutableStateOf("") }

    var isPasswordVisible by rememberSaveable { mutableStateOf(false) }
    var isConfirmPasswordVisible by rememberSaveable { mutableStateOf(false) }

    var isLoading by remember { mutableStateOf(false) }
    var errorMessage by remember { mutableStateOf<String?>(null) }
    var successMessage by remember { mutableStateOf<String?>(null) }

    val errorEmpty = stringResource(R.string.forgot_password_error_empty_fields)
    val errorMinLength = stringResource(R.string.forgot_password_error_min_length)
    val errorMismatch = stringResource(R.string.forgot_password_error_mismatch)
    val successMsg = stringResource(R.string.forgot_password_success_message)
    val errorDefaultFailure = stringResource(R.string.forgot_password_error_default_failure)

    val submitResetPassword: () -> Unit = {
        val trimmedEmail = email.trim()
        val trimmedName = displayName.trim()
        val trimmedPassword = newPassword
        val trimmedConfirm = confirmPassword

        when {
            trimmedEmail.isBlank() || trimmedName.isBlank() || trimmedPassword.isBlank() || trimmedConfirm.isBlank() -> {
                errorMessage = errorEmpty
                successMessage = null
            }
            trimmedPassword.length < 6 -> {
                errorMessage = errorMinLength
                successMessage = null
            }
            trimmedPassword != trimmedConfirm -> {
                errorMessage = errorMismatch
                successMessage = null
            }
            else -> {
                errorMessage = null
                isLoading = true
                coroutineScope.launch {
                    val result = onResetPasswordSubmit?.invoke(trimmedEmail, trimmedName, trimmedPassword)
                        ?: repository.resetLocalAccountPassword(trimmedEmail, trimmedName, trimmedPassword)
                    isLoading = false
                    if (result.isSuccess) {
                        successMessage = successMsg
                        errorMessage = null
                        delay(1500)
                        onNavigateBack()
                    } else {
                        errorMessage = result.exceptionOrNull()?.message ?: errorDefaultFailure
                        successMessage = null
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
        // Back Button floating di kiri atas
        IconButton(
            onClick = onNavigateBack,
            modifier = Modifier
                .align(Alignment.TopStart)
                .padding(top = 12.dp, start = 12.dp)
                .size(44.dp)
                .zIndex(10f)
                .background(LightSurface, CircleShape)
                .border(BorderStroke(1.dp, LightBorder), CircleShape)
                .testTag("forgot_password_back_button")
        ) {
            Icon(
                imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                contentDescription = stringResource(R.string.forgot_password_back_button_desc),
                tint = LightTextPrimary
            )
        }

        Column(
            modifier = Modifier
                .fillMaxSize()
                .imePadding()
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 22.dp)
                .padding(top = 16.dp, bottom = 32.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // 1. Editorial Hero Section: Ilustrasi Karakter Flat Vector (Berpikir / Mencari Kunci) + Headline Besar
            ForgotPasswordHeroHeader(
                isHeaderVisible = isHeaderVisible,
                isHeadlineVisible = isHeadlineVisible,
                areAnimationsEnabled = areAnimationsEnabled
            )

            Spacer(modifier = Modifier.height(18.dp))

            // 2. Reset Password Form Card dengan staged slideUp + fade-in
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
                ForgotPasswordFormCard(
                    email = email,
                    onEmailChange = {
                        email = it
                        if (errorMessage != null) errorMessage = null
                    },
                    displayName = displayName,
                    onDisplayNameChange = {
                        displayName = it
                        if (errorMessage != null) errorMessage = null
                    },
                    newPassword = newPassword,
                    onNewPasswordChange = {
                        newPassword = it
                        if (errorMessage != null) errorMessage = null
                    },
                    confirmPassword = confirmPassword,
                    onConfirmPasswordChange = {
                        confirmPassword = it
                        if (errorMessage != null) errorMessage = null
                    },
                    isPasswordVisible = isPasswordVisible,
                    onTogglePasswordVisibility = { isPasswordVisible = !isPasswordVisible },
                    isConfirmPasswordVisible = isConfirmPasswordVisible,
                    onToggleConfirmPasswordVisibility = { isConfirmPasswordVisible = !isConfirmPasswordVisible },
                    isLoading = isLoading,
                    errorMessage = errorMessage,
                    successMessage = successMessage,
                    onSubmit = submitResetPassword,
                    focusManager = focusManager
                )
            }

            Spacer(modifier = Modifier.height(24.dp))

            // 3. Footer link kembali ke Login
            ForgotPasswordFooter(onNavigateBack = onNavigateBack)
        }
    }
}

/**
 * Editorial Hero Header untuk Lupa Kata Sandi:
 * - Ilustrasi karakter flat vector baru: siswa sedang berpikir dan menemukan kembali kunci akun/kata sandi.
 * - Headline besar berani: "Lupa kata sandimu?" dengan kata "sandimu?" di-highlight warna FlameOrange.
 * - Konsisten dengan LoginScreen & RegisterScreen (solid colors, cream LightBg, aksen FlameOrange & CyanAccent).
 */
@Composable
private fun ForgotPasswordHeroHeader(
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

        // 1. Flat Vector Character Illustration (Pose Berpikir / Menemukan Kunci)
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
            ForgotPasswordHeroIllustration()
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
            val headlinePrefix = stringResource(R.string.forgot_password_headline_prefix)
            val headlineHighlight = stringResource(R.string.forgot_password_headline_highlight)
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
                    text = stringResource(R.string.forgot_password_subtitle),
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
 * Ilustrasi Karakter Flat Vector Sedang Berpikir & Menemukan Kunci Pemulihan:
 * Menggunakan teknik Canvas + Path yang konsisten dengan `OnboardingIllustrations.kt`, `LoginHeroIllustration`,
 * dan `RegisterHeroIllustration` (proporsi kepala ~21dp, skin color `Color(0xFFFFDFBA)`,
 * rambut `Color(0xFF1E293B)`, pakaian solid FlameOrange, pose tangan menyentuh dagu penasaran,
 * dan kunci emas bersinar di samping karakter).
 */
@Composable
fun ForgotPasswordHeroIllustration(modifier: Modifier = Modifier) {
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
            val centerOffset = Offset(w * 0.48f, h * 0.54f)
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
                color = CyanAccent.copy(alpha = 0.35f),
                center = Offset(w * 0.23f, h * 0.28f),
                radius = 5.dp.toPx(),
                style = Stroke(width = 1.5.dp.toPx())
            )
            // Small solid accent dot (top-left)
            drawCircle(
                color = CyanAccent.copy(alpha = 0.75f),
                center = Offset(w * 0.19f, h * 0.23f),
                radius = 2.5.dp.toPx()
            )

            // 2. Diamond accent near halo rim (harmonized with character and golden key)
            val diamondCenter = Offset(w * 0.69f, h * 0.23f)
            val dWidth = 4.dp.toPx()
            val dHeight = 6.dp.toPx()
            val diamondPath = Path().apply {
                moveTo(diamondCenter.x, diamondCenter.y - dHeight)
                lineTo(diamondCenter.x + dWidth, diamondCenter.y)
                lineTo(diamondCenter.x, diamondCenter.y + dHeight)
                lineTo(diamondCenter.x - dWidth, diamondCenter.y)
                close()
            }
            drawPath(path = diamondPath, color = FlameOrange.copy(alpha = 0.80f))

            // 3. Subtle thought bubble dots above character
            drawCircle(
                color = CyanAccent.copy(alpha = 0.40f),
                center = Offset(w * 0.56f, h * 0.18f),
                radius = 3.dp.toPx()
            )
            drawCircle(
                color = FlameOrange.copy(alpha = 0.60f),
                center = Offset(w * 0.61f, h * 0.15f),
                radius = 4.5.dp.toPx()
            )

            // 4. Warm solid circular accent dot (mid-left)
            drawCircle(
                color = FlameOrange.copy(alpha = 0.70f),
                center = Offset(w * 0.22f, h * 0.50f),
                radius = 3.5.dp.toPx()
            )

            // 5. Additional floating geometric accents (translucent arcs & outline circles)
            // Delicate half-circle arc at top-right
            drawArc(
                color = CyanAccent.copy(alpha = 0.28f),
                startAngle = 45f,
                sweepAngle = 180f,
                useCenter = false,
                topLeft = Offset(w * 0.82f, h * 0.32f),
                size = Size(10.dp.toPx(), 10.dp.toPx()),
                style = Stroke(1.5.dp.toPx(), cap = StrokeCap.Round)
            )
            // Translucent ring near right side of key
            drawCircle(
                color = FlameOrange.copy(alpha = 0.25f),
                center = Offset(w * 0.85f, h * 0.54f),
                radius = 4.dp.toPx(),
                style = Stroke(1.2.dp.toPx())
            )
            // Soft cyan accent dot at lower left
            drawCircle(
                color = CyanAccent.copy(alpha = 0.35f),
                center = Offset(w * 0.16f, h * 0.42f),
                radius = 3.dp.toPx()
            )

            // --- 2. Floating Golden Key of Recovery (Aha moment) ---
            val keyCenterX = w * 0.71f
            val keyCenterY = h * 0.44f

            // Golden glow surrounding key
            drawCircle(
                color = AmberGold.copy(alpha = 0.18f),
                center = Offset(keyCenterX, keyCenterY),
                radius = 22.dp.toPx()
            )
            drawCircle(
                color = AmberGold.copy(alpha = 0.35f),
                center = Offset(keyCenterX, keyCenterY),
                radius = 14.dp.toPx()
            )

            // Key Head (Ring)
            drawCircle(
                color = AmberGold,
                center = Offset(keyCenterX, keyCenterY - 12.dp.toPx()),
                radius = 8.5.dp.toPx(),
                style = Stroke(width = 3.2.dp.toPx())
            )
            // Key Hole cutout
            drawCircle(
                color = LightBg,
                center = Offset(keyCenterX, keyCenterY - 12.dp.toPx()),
                radius = 3.5.dp.toPx()
            )

            // Key Stem
            drawLine(
                color = AmberGold,
                start = Offset(keyCenterX, keyCenterY - 4.dp.toPx()),
                end = Offset(keyCenterX, keyCenterY + 18.dp.toPx()),
                strokeWidth = 3.5.dp.toPx(),
                cap = StrokeCap.Round
            )
            // Key Teeth 1
            drawLine(
                color = AmberGold,
                start = Offset(keyCenterX, keyCenterY + 11.dp.toPx()),
                end = Offset(keyCenterX + 6.5.dp.toPx(), keyCenterY + 11.dp.toPx()),
                strokeWidth = 3.dp.toPx(),
                cap = StrokeCap.Round
            )
            // Key Teeth 2
            drawLine(
                color = AmberGold,
                start = Offset(keyCenterX, keyCenterY + 16.dp.toPx()),
                end = Offset(keyCenterX + 5.dp.toPx(), keyCenterY + 16.dp.toPx()),
                strokeWidth = 3.dp.toPx(),
                cap = StrokeCap.Round
            )

            // Sparkle highlights on golden key
            val keySparkle = Offset(keyCenterX - 7.dp.toPx(), keyCenterY - 18.dp.toPx())
            val sparkleRad = 3.dp.toPx()
            drawLine(
                color = Color.White,
                start = Offset(keySparkle.x - sparkleRad, keySparkle.y),
                end = Offset(keySparkle.x + sparkleRad, keySparkle.y),
                strokeWidth = 1.5.dp.toPx(),
                cap = StrokeCap.Round
            )
            drawLine(
                color = Color.White,
                start = Offset(keySparkle.x, keySparkle.y - sparkleRad),
                end = Offset(keySparkle.x, keySparkle.y + sparkleRad),
                strokeWidth = 1.5.dp.toPx(),
                cap = StrokeCap.Round
            )

            // --- 3. Comfortable Study Chair & Cushion Base (Behind & Under Character) ---
            // Chair backrest behind character
            drawRoundRect(
                color = Color(0xFF1E293B),
                topLeft = Offset(w * 0.30f, h * 0.42f),
                size = Size(w * 0.32f, h * 0.38f),
                cornerRadius = CornerRadius(14.dp.toPx(), 14.dp.toPx())
            )
            // Soft lounge cushion base
            drawRoundRect(
                brush = Brush.verticalGradient(
                    colors = listOf(Color(0xFFF8F5EE), Color(0xFFEAE2D5)),
                    startY = h * 0.72f,
                    endY = h * 0.94f
                ),
                topLeft = Offset(w * 0.18f, h * 0.72f),
                size = Size(w * 0.58f, h * 0.22f),
                cornerRadius = CornerRadius(14.dp.toPx())
            )
            // Cushion top highlight
            drawLine(
                color = Color(0xFFDCD4C5),
                start = Offset(w * 0.20f, h * 0.72f),
                end = Offset(w * 0.74f, h * 0.72f),
                strokeWidth = 2.dp.toPx()
            )

            // --- 4. Character Pants / Legs in Seated Position ---
            val pantsPath = Path().apply {
                moveTo(w * 0.32f, h * 0.68f)
                quadraticTo(w * 0.44f, h * 0.70f, w * 0.58f, h * 0.72f)
                lineTo(w * 0.58f, h * 0.86f)
                lineTo(w * 0.44f, h * 0.86f)
                quadraticTo(w * 0.38f, h * 0.82f, w * 0.30f, h * 0.80f)
                close()
            }
            drawPath(
                path = pantsPath,
                brush = Brush.verticalGradient(
                    colors = listOf(Color(0xFF334155), Color(0xFF1E293B)),
                    startY = h * 0.68f,
                    endY = h * 0.86f
                )
            )

            // --- 5. Character Torso & Clothes (Flowing naturally into seated pose) ---
            // Torso / Hoodie in FlameOrange with Volume Gradient (angled slightly toward the key)
            val bodyPath = Path().apply {
                moveTo(w * 0.38f, h * 0.45f) // shoulder left
                lineTo(w * 0.32f, h * 0.72f) // bottom left
                quadraticTo(w * 0.45f, h * 0.75f, w * 0.58f, h * 0.72f) // rounded waistline overlapping pants
                lineTo(w * 0.56f, h * 0.45f) // shoulder right
                close()
            }
            drawPath(
                path = bodyPath,
                brush = Brush.verticalGradient(
                    colors = listOf(Color(0xFFFF6D3B), FlameOrange, Color(0xFFC2410C)),
                    startY = h * 0.45f,
                    endY = h * 0.75f
                )
            )

            // Hoodie Collar Detail
            drawCircle(
                color = Color(0xFFC2410C),
                center = Offset(w * 0.465f, h * 0.45f),
                radius = 9.dp.toPx()
            )

            // Hoodie Drawstrings (White)
            drawLine(
                color = Color.White.copy(alpha = 0.90f),
                start = Offset(w * 0.45f, h * 0.46f),
                end = Offset(w * 0.45f, h * 0.53f),
                strokeWidth = 2.dp.toPx(),
                cap = StrokeCap.Round
            )
            drawLine(
                color = Color.White.copy(alpha = 0.90f),
                start = Offset(w * 0.48f, h * 0.46f),
                end = Offset(w * 0.48f, h * 0.53f),
                strokeWidth = 2.dp.toPx(),
                cap = StrokeCap.Round
            )

            // --- 6. Neck & Head (Thoughtful Pose) ---
            // Neck
            drawRect(
                color = Color(0xFFFFDFBA),
                topLeft = Offset(w * 0.44f, h * 0.38f),
                size = Size(w * 0.055f, h * 0.08f)
            )

            // Head / Face
            drawCircle(
                color = Color(0xFFFFDFBA),
                center = Offset(w * 0.465f, h * 0.33f),
                radius = 21.dp.toPx()
            )
            // Left ear
            drawCircle(
                color = Color(0xFFFFDFBA),
                center = Offset(w * 0.385f, h * 0.34f),
                radius = 4.5.dp.toPx()
            )
            // Right ear
            drawCircle(
                color = Color(0xFFFFDFBA),
                center = Offset(w * 0.545f, h * 0.34f),
                radius = 4.5.dp.toPx()
            )

            // Hair (Stylized modern silhouette with smooth continuous curves and depth gradient)
            val hairPath = Path().apply {
                moveTo(w * 0.375f, h * 0.34f)
                cubicTo(w * 0.35f, h * 0.19f, w * 0.59f, h * 0.19f, w * 0.555f, h * 0.32f)
                quadraticTo(w * 0.465f, h * 0.25f, w * 0.375f, h * 0.34f)
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

            // Thoughtful, curious expression (looking toward the key with a slight smile)
            // Left eyebrow (thoughtfully arched)
            drawArc(
                color = Color(0xFF1E293B),
                startAngle = 200f,
                sweepAngle = 140f,
                useCenter = false,
                topLeft = Offset(w * 0.43f, h * 0.30f),
                size = Size(w * 0.035f, 3.dp.toPx()),
                style = Stroke(width = 1.5.dp.toPx(), cap = StrokeCap.Round)
            )
            // Right eyebrow (slightly raised)
            drawArc(
                color = Color(0xFF1E293B),
                startAngle = 190f,
                sweepAngle = 140f,
                useCenter = false,
                topLeft = Offset(w * 0.48f, h * 0.29f),
                size = Size(w * 0.035f, 3.dp.toPx()),
                style = Stroke(width = 1.5.dp.toPx(), cap = StrokeCap.Round)
            )

            // Left eye (looking up toward the key)
            drawCircle(
                color = Color(0xFF334155),
                center = Offset(w * 0.445f, h * 0.325f),
                radius = 2.2.dp.toPx()
            )
            // Right eye (looking up toward the key)
            drawCircle(
                color = Color(0xFF334155),
                center = Offset(w * 0.495f, h * 0.325f),
                radius = 2.2.dp.toPx()
            )

            // Warm wry smile / "aha" expression
            drawArc(
                color = Color(0xFFE2A77E),
                startAngle = 10f,
                sweepAngle = 150f,
                useCenter = false,
                topLeft = Offset(w * 0.455f, h * 0.345f),
                size = Size(w * 0.045f, 4.5.dp.toPx()),
                style = Stroke(width = 1.8.dp.toPx(), cap = StrokeCap.Round)
            )
            // Friendly blush on cheeks
            drawCircle(
                color = Color(0xFFFFB4A2).copy(alpha = 0.55f),
                center = Offset(w * 0.425f, h * 0.355f),
                radius = 3.2.dp.toPx()
            )
            drawCircle(
                color = Color(0xFFFFB4A2).copy(alpha = 0.55f),
                center = Offset(w * 0.515f, h * 0.355f),
                radius = 3.2.dp.toPx()
            )

            // --- 6. Arms: One touching chin in thinking pose, one gesturing toward the key ---
            // Left arm (thinking pose, hand touching chin - Directional gradient)
            val leftArm = Path().apply {
                moveTo(w * 0.38f, h * 0.48f)
                lineTo(w * 0.32f, h * 0.62f)
                lineTo(w * 0.36f, h * 0.64f)
                lineTo(w * 0.42f, h * 0.52f)
                close()
            }
            drawPath(
                path = leftArm,
                brush = Brush.linearGradient(
                    colors = listOf(Color(0xFFFF6D3B), FlameOrange),
                    start = Offset(w * 0.38f, h * 0.48f),
                    end = Offset(w * 0.32f, h * 0.62f)
                )
            )
            // Forearm coming up to chin
            val leftForearm = Path().apply {
                moveTo(w * 0.34f, h * 0.63f)
                lineTo(w * 0.43f, h * 0.40f)
                lineTo(w * 0.46f, h * 0.42f)
                lineTo(w * 0.38f, h * 0.65f)
                close()
            }
            drawPath(
                path = leftForearm,
                brush = Brush.linearGradient(
                    colors = listOf(FlameOrange, Color(0xFFC2410C)),
                    start = Offset(w * 0.34f, h * 0.63f),
                    end = Offset(w * 0.43f, h * 0.40f)
                )
            )
            // Hand at chin (thinking gesture)
            drawCircle(
                color = Color(0xFFFFDFBA),
                center = Offset(w * 0.44f, h * 0.39f),
                radius = 4.8.dp.toPx()
            )

            // Right arm (gesturing / reaching up toward the floating golden key - Directional gradient)
            val rightArm = Path().apply {
                moveTo(w * 0.56f, h * 0.48f)
                lineTo(w * 0.63f, h * 0.58f)
                lineTo(w * 0.66f, h * 0.56f)
                lineTo(w * 0.59f, h * 0.47f)
                close()
            }
            drawPath(
                path = rightArm,
                brush = Brush.linearGradient(
                    colors = listOf(Color(0xFFFF6D3B), FlameOrange, Color(0xFFC2410C)),
                    start = Offset(w * 0.56f, h * 0.48f),
                    end = Offset(w * 0.63f, h * 0.58f)
                )
            )
            // Right hand near the key
            drawCircle(
                color = Color(0xFFFFDFBA),
                center = Offset(w * 0.65f, h * 0.54f),
                radius = 5.dp.toPx()
            )
        }

        // 2. Editorial Pill Badges (Surface overlay with clean border & floating shadow)
        // Bottom-right badge: Akun Aman
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
                    imageVector = Icons.Default.Shield,
                    contentDescription = null,
                    tint = FlameOrange,
                    modifier = Modifier.size(13.dp)
                )
                Spacer(modifier = Modifier.width(5.dp))
                Text(
                    text = stringResource(R.string.forgot_password_badge_secure_account),
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 11.sp,
                    color = LightTextPrimary
                )
            }
        }
    }
}

/**
 * Form Card yang membungkus field Email, Nama Lengkap, Sandi Baru, Konfirmasi Sandi Baru, Banner Notifikasi, dan Tombol Simpan.
 * Mengikuti pola LoginFormCard & RegisterFormCard (solid 3dp FlameOrange brand bar, border LightBorder, background LightSurface).
 * SENGAJA FIXED: Tidak menggunakan MaterialTheme.colorScheme.surface.
 */
@Composable
private fun ForgotPasswordFormCard(
    email: String,
    onEmailChange: (String) -> Unit,
    displayName: String,
    onDisplayNameChange: (String) -> Unit,
    newPassword: String,
    onNewPasswordChange: (String) -> Unit,
    confirmPassword: String,
    onConfirmPasswordChange: (String) -> Unit,
    isPasswordVisible: Boolean,
    onTogglePasswordVisibility: () -> Unit,
    isConfirmPasswordVisible: Boolean,
    onToggleConfirmPasswordVisibility: () -> Unit,
    isLoading: Boolean,
    errorMessage: String?,
    successMessage: String?,
    onSubmit: () -> Unit,
    focusManager: androidx.compose.ui.focus.FocusManager,
    modifier: Modifier = Modifier
) {
    val fieldColors = forgotPasswordFixedFieldColors()

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
                // Success Notice Banner
                if (successMessage != null) {
                    ForgotPasswordSuccessBanner(message = successMessage)
                }

                // Error Notice Banner
                if (errorMessage != null) {
                    ForgotPasswordErrorBanner(message = errorMessage)
                }

                // 1. Email Field
                OutlinedTextField(
                    value = email,
                    onValueChange = onEmailChange,
                    label = { Text(stringResource(R.string.forgot_password_email_label)) },
                    placeholder = { Text(stringResource(R.string.forgot_password_email_placeholder)) },
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
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("forgot_password_email_input"),
                    shape = RoundedCornerShape(14.dp),
                    colors = fieldColors
                )

                // 2. Full Name Field
                OutlinedTextField(
                    value = displayName,
                    onValueChange = onDisplayNameChange,
                    label = { Text(stringResource(R.string.forgot_password_name_label)) },
                    placeholder = { Text(stringResource(R.string.forgot_password_name_placeholder)) },
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
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("forgot_password_name_input"),
                    shape = RoundedCornerShape(14.dp),
                    colors = fieldColors
                )

                // 3. New Password Field
                OutlinedTextField(
                    value = newPassword,
                    onValueChange = onNewPasswordChange,
                    label = { Text(stringResource(R.string.forgot_password_new_password_label)) },
                    placeholder = { Text(stringResource(R.string.forgot_password_new_password_placeholder)) },
                    leadingIcon = {
                        Icon(
                            imageVector = Icons.Default.Lock,
                            contentDescription = null
                        )
                    },
                    trailingIcon = {
                        IconButton(
                            onClick = onTogglePasswordVisibility,
                            modifier = Modifier.testTag("forgot_password_toggle")
                        ) {
                            Icon(
                                imageVector = if (isPasswordVisible) Icons.Default.Visibility else Icons.Default.VisibilityOff,
                                contentDescription = if (isPasswordVisible) stringResource(R.string.forgot_password_hide_password) else stringResource(R.string.forgot_password_show_password)
                            )
                        }
                    },
                    singleLine = true,
                    visualTransformation = if (isPasswordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Password,
                        imeAction = ImeAction.Next
                    ),
                    keyboardActions = KeyboardActions(
                        onNext = { focusManager.moveFocus(FocusDirection.Down) }
                    ),
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("forgot_password_new_password_input"),
                    shape = RoundedCornerShape(14.dp),
                    colors = fieldColors
                )

                // 4. Confirm New Password Field
                OutlinedTextField(
                    value = confirmPassword,
                    onValueChange = onConfirmPasswordChange,
                    label = { Text(stringResource(R.string.forgot_password_confirm_password_label)) },
                    placeholder = { Text(stringResource(R.string.forgot_password_confirm_password_placeholder)) },
                    leadingIcon = {
                        Icon(
                            imageVector = Icons.Default.Lock,
                            contentDescription = null
                        )
                    },
                    trailingIcon = {
                        IconButton(
                            onClick = onToggleConfirmPasswordVisibility,
                            modifier = Modifier.testTag("forgot_password_confirm_toggle")
                        ) {
                            Icon(
                                imageVector = if (isConfirmPasswordVisible) Icons.Default.Visibility else Icons.Default.VisibilityOff,
                                contentDescription = if (isConfirmPasswordVisible) stringResource(R.string.forgot_password_confirm_hide_password) else stringResource(R.string.forgot_password_confirm_show_password)
                            )
                        }
                    },
                    singleLine = true,
                    visualTransformation = if (isConfirmPasswordVisible) VisualTransformation.None else PasswordVisualTransformation(),
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
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("forgot_password_confirm_input"),
                    shape = RoundedCornerShape(14.dp),
                    colors = fieldColors
                )

                Spacer(modifier = Modifier.height(4.dp))

                // Primary Action: Simpan Kata Sandi Baru (warna gelap solid dengan teks terang, konsisten dengan Login & Register)
                ForgotPasswordSubmitButton(
                    loading = isLoading,
                    onClick = onSubmit
                )
            }
        }
    }
}

/**
 * SENGAJA FIXED: Tombol utama Simpan Kata Sandi Baru menggunakan warna gelap solid (`Color(0xFF1E293B)`)
 * dengan teks putih (`Color.White`), memberikan kontras tegas dan bold khas editorial illustrated app,
 * sama persis dengan tombol `LoginSubmitButton` dan `RegisterSubmitButton`.
 */
@Composable
private fun ForgotPasswordSubmitButton(loading: Boolean, onClick: () -> Unit) {
    Surface(
        onClick = onClick,
        enabled = !loading,
        shape = RoundedCornerShape(16.dp),
        color = Color(0xFF1E293B), // Kontras tegas gelap solid khas gaya editorial (bukan teal)
        modifier = Modifier
            .fillMaxWidth()
            .height(52.dp)
            .testTag("forgot_password_submit_button")
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
                        text = stringResource(R.string.forgot_password_submit_button),
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Icon(
                        imageVector = Icons.Default.LockReset,
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
private fun ForgotPasswordSuccessBanner(message: String) {
    Surface(
        color = SuccessGreen.copy(alpha = 0.1f),
        shape = RoundedCornerShape(12.dp),
        border = BorderStroke(1.dp, SuccessGreen.copy(alpha = 0.3f)),
        modifier = Modifier.fillMaxWidth()
    ) {
        Row(
            modifier = Modifier.padding(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = Icons.Default.CheckCircle,
                contentDescription = null,
                tint = SuccessGreen,
                modifier = Modifier.size(18.dp)
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                text = message,
                style = MaterialTheme.typography.bodySmall,
                color = Color(0xFF065F46),
                fontWeight = FontWeight.SemiBold
            )
        }
    }
}

@Composable
private fun ForgotPasswordErrorBanner(message: String) {
    Surface(
        color = ErrorRed.copy(alpha = 0.1f),
        shape = RoundedCornerShape(12.dp),
        border = BorderStroke(1.dp, ErrorRed.copy(alpha = 0.3f)),
        modifier = Modifier.fillMaxWidth()
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
private fun ForgotPasswordFooter(
    onNavigateBack: () -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center
    ) {
        Text(
            text = stringResource(R.string.forgot_password_footer_remember),
            style = MaterialTheme.typography.bodyMedium,
            color = LightTextSecondary // SENGAJA FIXED
        )
        Spacer(modifier = Modifier.width(4.dp))
        Text(
            text = stringResource(R.string.forgot_password_footer_login_link),
            style = MaterialTheme.typography.bodyMedium,
            fontWeight = FontWeight.Bold,
            color = FlameOrange, // SENGAJA FIXED
            modifier = Modifier
                .clickable { onNavigateBack() }
                .padding(4.dp)
                .testTag("forgot_password_login_link")
        )
    }
}

/**
 * SENGAJA FIXED: Warna field input menggunakan token cream (`LightSurface`/`LightSurfaceVariant`)
 * dengan aksen fokus oranye (`FlameOrange`), tidak mengikuti tema gelap sistem/app.
 */
@Composable
private fun forgotPasswordFixedFieldColors(): TextFieldColors {
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

@Preview(name = "Forgot Password - Fixed Brand Theme (Light System)", showBackground = true, uiMode = Configuration.UI_MODE_NIGHT_NO)
@Composable
private fun ForgotPasswordScreenLightPreview() {
    CodeAcademyTheme(darkTheme = false) {
        ForgotPasswordScreen(
            onNavigateBack = {}
        )
    }
}

@Preview(name = "Forgot Password - Fixed Brand Theme (Dark System)", showBackground = true, uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
private fun ForgotPasswordScreenDarkPreview() {
    CodeAcademyTheme(darkTheme = true) {
        ForgotPasswordScreen(
            onNavigateBack = {}
        )
    }
}
