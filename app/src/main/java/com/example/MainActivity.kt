package com.example

import android.os.Bundle
import kotlinx.coroutines.tasks.await
import androidx.activity.ComponentActivity
import androidx.activity.compose.BackHandler
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.consumeWindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.EmojiEvents
import androidx.compose.material.icons.filled.Groups
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Insights
import androidx.compose.material.icons.filled.MenuBook
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.ScaffoldDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.rememberDrawerState
import com.example.ui.components.AppNavigationDrawer
import com.example.ui.components.AppDialog
import com.example.util.FirebaseAuthKeyResetHelper
import com.example.util.SigningCertificateHelper
import com.example.util.SigningFingerprintInfo
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.ui.InitialDestination
import com.example.ui.MainViewModel
import java.net.URLDecoder
import java.net.URLEncoder
import com.example.ui.screens.AccountBannedScreen
import com.example.ui.screens.AiTutorScreen
import com.example.ui.screens.AuthOnboardingScreen
import com.example.ui.screens.BookmarksScreen
import com.example.ui.screens.CertificateListScreen
import com.example.ui.screens.CertificateScreen
import com.example.ui.screens.CheatSheetScreen
import com.example.ui.screens.ClassDetailScreen
import com.example.ui.screens.CommunityScreen
import com.example.ui.screens.CourseCatalogScreen
import com.example.ui.screens.CourseForumScreen
import com.example.ui.screens.DiskusiHangatScreen
import com.example.ui.screens.FinalProjectSubmissionScreen
import com.example.ui.screens.ForgotPasswordScreen
import com.example.ui.screens.HasilEvaluasiScreen
import com.example.ui.screens.HelpScreen
import com.example.ui.screens.HomeTabScreen
import com.example.ui.screens.LoginScreen
import com.example.ui.screens.MateriScreen
import com.example.ui.screens.NotificationsScreen
import com.example.ui.screens.PlacementTestScreen
import com.example.ui.screens.PlaygroundScreen
import com.example.ui.screens.ProfileScreen
import com.example.ui.screens.ProgressScreen
import com.example.ui.screens.QuizScreen
import com.example.ui.screens.RegisterScreen
import com.example.ui.screens.SearchScreen
import com.example.ui.screens.SectionQuizResultScreen
import com.example.ui.screens.SectionQuizRulesScreen
import com.example.ui.screens.SectionQuizScreen
import com.example.ui.screens.SplashScreen
import com.example.data.models.QuizQuestion
import com.example.data.models.PlacementTestQuestion
import com.example.data.local.SectionQuizAttemptEntity
import com.example.ui.theme.AmberGold
import com.example.ui.theme.CodeAcademyTheme
import com.example.ui.theme.DarkBg
import com.example.ui.theme.LightBg
import com.example.ui.theme.TealLight
import com.example.util.localization.LocalAppLanguage
import com.example.util.localization.toLocale
import android.app.Activity
import android.content.res.Configuration
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.res.stringResource
import androidx.core.view.WindowCompat
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            val viewModel: MainViewModel = viewModel()
            val isDark by viewModel.isDarkMode.collectAsStateWithLifecycle()
            val currentLanguage by viewModel.appLanguage.collectAsStateWithLifecycle()

            val context = LocalContext.current
            val localizedContext = remember(currentLanguage, context) {
                val locale = currentLanguage.toLocale()
                val config = Configuration(context.resources.configuration)
                config.setLocale(locale)
                context.createConfigurationContext(config)
            }
            val localizedConfiguration = remember(localizedContext) {
                localizedContext.resources.configuration
            }

            CompositionLocalProvider(
                LocalContext provides localizedContext,
                LocalConfiguration provides localizedConfiguration,
                LocalAppLanguage provides currentLanguage,
                androidx.activity.compose.LocalActivityResultRegistryOwner provides this@MainActivity,
                androidx.activity.compose.LocalOnBackPressedDispatcherOwner provides this@MainActivity
            ) {
                CodeAcademyTheme(darkTheme = isDark) {
                    CodeAcademyApp(viewModel = viewModel, hostActivity = this@MainActivity)
                }
            }
        }
    }
}

private data class NavigationTab(
    val route: String,
    val label: String,
    val icon: ImageVector,
    val testTag: String
)

@Composable
fun CodeAcademyApp(
    viewModel: MainViewModel,
    navController: NavHostController = rememberNavController(),
    hostActivity: Activity? = null
) {
    val coroutineScope = rememberCoroutineScope()
    val levelUpDialog by viewModel.levelUpDialogInfo.collectAsStateWithLifecycle()
    val banStatus by viewModel.banStatus.collectAsStateWithLifecycle()
    val currentUserProfile by viewModel.userProfile.collectAsStateWithLifecycle()

    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route

    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val openDrawer: () -> Unit = {
        coroutineScope.launch {
            drawerState.open()
        }
    }

    val navigateToTab: (String) -> Unit = { route ->
        if (route == "home") {
            val popped = navController.popBackStack("home", inclusive = false)
            if (!popped) {
                navController.navigate("home") {
                    launchSingleTop = true
                }
            }
        } else {
            navController.navigate(route) {
                popUpTo("home") {
                    inclusive = false
                }
                launchSingleTop = true
            }
        }
    }

    val tabs = listOf(
        NavigationTab("home", stringResource(R.string.nav_home_label), Icons.Default.Home, "tab_beranda"),
        NavigationTab("courses", stringResource(R.string.nav_courses_label), Icons.Default.MenuBook, "tab_kelas"),
        NavigationTab("progress", stringResource(R.string.nav_progress_label), Icons.Default.Insights, "tab_progres"),
        NavigationTab("komunitas", stringResource(R.string.nav_leaderboard_label), Icons.Default.EmojiEvents, "tab_komunitas"),
        NavigationTab("profile", stringResource(R.string.nav_profile_label), Icons.Default.Person, "tab_profil")
    )

    val bottomBarRoutes = remember { setOf("home", "courses", "progress", "komunitas", "profile") }
    val isBottomBarVisible = currentRoute in bottomBarRoutes

    val tabRoutes = remember { setOf("home", "courses", "progress", "komunitas", "profile") }
    val tabOrder = remember { listOf("home", "courses", "progress", "komunitas", "profile") }
    val authRoutes = remember { setOf("splash", "login", "register", "onboarding", "forgot_password", "banned") }
    val context = androidx.compose.ui.platform.LocalContext.current
    val tabSlideOffsetPx = remember(context) { (12 * context.resources.displayMetrics.density).toInt() }

    // INTENTIONAL FIXED BRAND THEME FOR LOGIN, REGISTER, & FORGOT PASSWORD:
    // LoginScreen, RegisterScreen, and ForgotPasswordScreen intentionally use a fixed warm cream + orange brand theme
    // (LightBg/LightSurface) regardless of system/app dark mode. Ensure root Scaffold background
    // and system status/navigation bar icons match this fixed cream background on those routes.
    val isFixedLightAuthScreen = currentRoute == "login" || currentRoute == "register" || currentRoute == "forgot_password"
    val isDark by viewModel.isDarkMode.collectAsStateWithLifecycle()
    val view = LocalView.current
    if (!view.isInEditMode) {
        SideEffect {
            val window = (view.context as? Activity)?.window ?: return@SideEffect
            val insetsController = WindowCompat.getInsetsController(window, view)
            val useDarkSystemBarIcons = isFixedLightAuthScreen || !isDark
            insetsController.isAppearanceLightStatusBars = useDarkSystemBarIcons
            insetsController.isAppearanceLightNavigationBars = useDarkSystemBarIcons
        }
    }

    val animatedBgColor by animateColorAsState(
        targetValue = if (isFixedLightAuthScreen) LightBg else MaterialTheme.colorScheme.background,
        animationSpec = tween(durationMillis = 300, easing = FastOutSlowInEasing),
        label = "rootScaffoldBg"
    )
    val animatedSurfaceColor by animateColorAsState(
        targetValue = MaterialTheme.colorScheme.surface,
        animationSpec = tween(durationMillis = 300, easing = FastOutSlowInEasing),
        label = "rootNavSurface"
    )

    val isSplashScreen = currentRoute == "splash" || currentRoute == null

    BackHandler(enabled = drawerState.isOpen) {
        coroutineScope.launch {
            drawerState.close()
        }
    }

    ModalNavigationDrawer(
        drawerState = drawerState,
        gesturesEnabled = currentRoute in tabRoutes,
        drawerContent = {
            CodeAcademyDrawerContent(
                viewModel = viewModel,
                tabRoutes = tabRoutes,
                currentRoute = currentRoute,
                isOpen = drawerState.targetValue == DrawerValue.Open || drawerState.isOpen,
                onNavigateToTab = navigateToTab,
                onNavigate = { route -> navController.navigate(route) },
                onCloseDrawer = { coroutineScope.launch { drawerState.close() } },
                onLogout = {
                    coroutineScope.launch {
                        drawerState.close()
                        viewModel.logout()
                        navController.navigate("login") {
                            popUpTo(0) { inclusive = true }
                        }
                    }
                }
            )
        }
    ) {
        Scaffold(
        containerColor = if (isFixedLightAuthScreen) LightBg else animatedBgColor,
        contentWindowInsets = if (isSplashScreen) WindowInsets(0, 0, 0, 0) else ScaffoldDefaults.contentWindowInsets,
        bottomBar = {
            if (isBottomBarVisible) {
                NavigationBar(
                    containerColor = animatedSurfaceColor,
                    tonalElevation = 8.dp
                ) {
                    tabs.forEach { tab ->
                        val isSelected = currentRoute == tab.route
                        NavigationBarItem(
                            icon = { Icon(imageVector = tab.icon, contentDescription = tab.label) },
                            label = {
                                Text(
                                    tab.label,
                                    maxLines = 1,
                                    softWrap = false,
                                    overflow = androidx.compose.ui.text.style.TextOverflow.Ellipsis,
                                    fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal
                                )
                            },
                            selected = isSelected,
                            onClick = {
                                if (currentRoute != tab.route) {
                                    navigateToTab(tab.route)
                                }
                            },
                            colors = NavigationBarItemDefaults.colors(
                                selectedIconColor = Color.Black,
                                indicatorColor = TealLight,
                                selectedTextColor = TealLight,
                                unselectedIconColor = MaterialTheme.colorScheme.onSurfaceVariant,
                                unselectedTextColor = MaterialTheme.colorScheme.onSurfaceVariant
                            ),
                            modifier = Modifier.testTag(tab.testTag)
                        )
                    }
                }
            }
        },
        modifier = Modifier.fillMaxSize()
    ) { innerPadding ->
        val resolvedActivity = hostActivity ?: (view.context as? Activity)
        var isGoogleAuthLoading by remember { mutableStateOf(false) }
        var googleCertDialogInfo by remember { mutableStateOf<Pair<String, SigningFingerprintInfo>?>(null) }

        googleCertDialogInfo?.let { (errMsg, fpInfo) ->
            AlertDialog(
                onDismissRequest = { googleCertDialogInfo = null },
                title = {
                    Text(
                        text = context.getString(com.example.R.string.google_auth_cert_dialog_title),
                        fontWeight = FontWeight.Bold
                    )
                },
                text = {
                    androidx.compose.foundation.text.selection.SelectionContainer {
                        Text(
                            text = context.getString(
                                com.example.R.string.google_auth_cert_dialog_body_format,
                                errMsg,
                                fpInfo.packageName,
                                fpInfo.sha1Colon,
                                fpInfo.sha256Colon
                            ),
                            style = MaterialTheme.typography.bodySmall.copy(
                                fontFamily = androidx.compose.ui.text.font.FontFamily.Monospace
                            )
                        )
                    }
                },
                confirmButton = {
                    Button(
                        onClick = {
                            val clipboard = context.getSystemService(android.content.Context.CLIPBOARD_SERVICE)
                                as? android.content.ClipboardManager
                            val payload = "Package: ${fpInfo.packageName}\nSHA-1: ${fpInfo.sha1Colon}\nSHA-256: ${fpInfo.sha256Colon}"
                            clipboard?.setPrimaryClip(android.content.ClipData.newPlainText("CodeAcademy SHA Fingerprints", payload))
                            android.widget.Toast.makeText(
                                context,
                                context.getString(com.example.R.string.google_auth_cert_copied_toast),
                                android.widget.Toast.LENGTH_SHORT
                            ).show()
                        }
                    ) {
                        Text(context.getString(com.example.R.string.google_auth_cert_copy_btn))
                    }
                },
                dismissButton = {
                    androidx.compose.material3.TextButton(
                        onClick = { googleCertDialogInfo = null }
                    ) {
                        Text(context.getString(com.example.R.string.google_auth_cert_close_btn))
                    }
                }
            )
        }

        val extractGoogleEmail: (com.google.firebase.auth.AuthResult?) -> String? = { firebaseResult ->
            val fbUser = firebaseResult?.user
            val profileEmail = firebaseResult?.additionalUserInfo?.profile?.get("email") as? String
            fbUser?.email?.takeIf { it.isNotBlank() }
                ?: fbUser?.providerData?.firstNotNullOfOrNull { info -> info.email?.takeIf { it.isNotBlank() } }
                ?: profileEmail?.takeIf { it.isNotBlank() }
        }

        val handleGoogleAuthResult: (Result<Boolean>) -> Unit = { authResult ->
            if (authResult.isSuccess) {
                val targetRoute = when {
                    viewModel.banStatus.value.isBanned -> "banned"
                    authResult.getOrDefault(false) -> "home"
                    else -> "onboarding"
                }
                navController.navigate(targetRoute) {
                    popUpTo(navController.graph.id) { inclusive = true }
                }
            } else {
                android.widget.Toast.makeText(
                    context,
                    authResult.exceptionOrNull()?.message ?: context.getString(
                        com.example.R.string.google_auth_failed_format,
                        context.getString(com.example.R.string.google_auth_generic_error)
                    ),
                    android.widget.Toast.LENGTH_LONG
                ).show()
            }
        }

        // Recover any pending Firebase Google OAuth result if the Activity was recreated during sign-in
        LaunchedEffect(Unit) {
            val pendingTask = runCatching {
                com.google.firebase.auth.FirebaseAuth.getInstance().pendingAuthResult
            }.getOrNull()
            if (pendingTask != null) {
                isGoogleAuthLoading = true
                try {
                    val firebaseResult = pendingTask.await()
                    val authResult = viewModel.completeGoogleLogin(
                        email = extractGoogleEmail(firebaseResult),
                        displayName = firebaseResult.user?.displayName
                    )
                    handleGoogleAuthResult(authResult)
                } catch (e: kotlinx.coroutines.CancellationException) {
                    throw e
                } catch (e: Throwable) {
                    android.util.Log.w("MainActivity", "Pending Google OAuth recovery failed", e)
                } finally {
                    isGoogleAuthLoading = false
                }
            }
        }

        val launchGoogleAuth: (String) -> Unit = { _ ->
            if (!isGoogleAuthLoading) {
                val activity = resolvedActivity
                if (activity == null) {
                    android.widget.Toast.makeText(
                        context,
                        context.getString(
                            com.example.R.string.google_auth_failed_format,
                            context.getString(com.example.R.string.google_auth_generic_error)
                        ),
                        android.widget.Toast.LENGTH_LONG
                    ).show()
                } else {
                    isGoogleAuthLoading = true
                    coroutineScope.launch {
                        try {
                            // Path 1: Native Play Services Credential Manager (bypasses GenericIdpActivity / AndroidKeyStore Tink keyset)
                            val webClientIdRes = context.resources.getIdentifier(
                                "default_web_client_id",
                                "string",
                                context.packageName
                            )
                            val webClientId = if (webClientIdRes != 0) {
                                runCatching { context.getString(webClientIdRes) }.getOrNull().orEmpty()
                            } else {
                                ""
                            }

                            var userCancelledCredentialSheet = false
                            if (webClientId.isNotBlank()) {
                                val sheetOpenedAt = android.os.SystemClock.elapsedRealtime()
                                try {
                                    val googleIdOption = com.google.android.libraries.identity.googleid.GetSignInWithGoogleOption.Builder(webClientId)
                                        .build()
                                    val credentialRequest = androidx.credentials.GetCredentialRequest.Builder()
                                        .addCredentialOption(googleIdOption)
                                        .build()
                                    val credentialResponse = androidx.credentials.CredentialManager.create(activity)
                                        .getCredential(activity, credentialRequest)
                                    val cred = credentialResponse.credential
                                    if (cred.type == com.google.android.libraries.identity.googleid.GoogleIdTokenCredential.TYPE_GOOGLE_ID_TOKEN_CREDENTIAL) {
                                        val googleCred = com.google.android.libraries.identity.googleid.GoogleIdTokenCredential.createFrom(cred.data)
                                        val authResult = viewModel.loginWithGoogle(
                                            idToken = googleCred.idToken,
                                            displayName = googleCred.displayName,
                                            email = googleCred.id
                                        )
                                        handleGoogleAuthResult(authResult)
                                        return@launch
                                    }
                                } catch (e: kotlinx.coroutines.CancellationException) {
                                    throw e
                                } catch (e: androidx.credentials.exceptions.GetCredentialCancellationException) {
                                    val elapsedMs = android.os.SystemClock.elapsedRealtime() - sheetOpenedAt
                                    if (elapsedMs >= 450L) {
                                        userCancelledCredentialSheet = true
                                    } else {
                                        android.util.Log.w("MainActivity", "CredentialManager instant cancel (${elapsedMs}ms), falling back to OAuthProvider", e)
                                    }
                                } catch (e: Throwable) {
                                    android.util.Log.w("MainActivity", "CredentialManager unavailable/failed, falling back to OAuthProvider: ${e.message}", e)
                                }
                            }

                            if (userCancelledCredentialSheet) {
                                android.widget.Toast.makeText(
                                    context,
                                    context.getString(com.example.R.string.google_auth_cancelled),
                                    android.widget.Toast.LENGTH_SHORT
                                ).show()
                                return@launch
                            }

                            // Path 2: Firebase OAuthProvider fallback with self-healing Tink crypto keyset reset & 1 automatic retry
                            val startOAuthFlow: suspend () -> com.google.firebase.auth.AuthResult = {
                                val provider = com.google.firebase.auth.OAuthProvider.newBuilder("google.com")
                                    .setScopes(listOf("email", "profile"))
                                    .addCustomParameter("prompt", "select_account")
                                    .build()
                                com.google.firebase.auth.FirebaseAuth.getInstance()
                                    .startActivityForSignInWithProvider(activity, provider)
                                    .await()
                            }

                            val firebaseResult = try {
                                startOAuthFlow()
                            } catch (e: Throwable) {
                                if (FirebaseAuthKeyResetHelper.isKeystoreOrPublicKeyError(e.message)) {
                                    android.util.Log.w("MainActivity", "Detected Firebase Tink/KeyStore public key error; clearing crypto prefs & retrying once", e)
                                    FirebaseAuthKeyResetHelper.clearCorruptedAuthKeysets(activity)
                                    startOAuthFlow()
                                } else {
                                    throw e
                                }
                            }

                            val authResult = viewModel.completeGoogleLogin(
                                email = extractGoogleEmail(firebaseResult),
                                displayName = firebaseResult.user?.displayName
                            )
                            handleGoogleAuthResult(authResult)
                        } catch (e: kotlinx.coroutines.CancellationException) {
                            throw e
                        } catch (e: com.google.firebase.auth.FirebaseAuthUserCollisionException) {
                            android.util.Log.w("MainActivity", "Google auth collision", e)
                            android.widget.Toast.makeText(
                                context,
                                context.getString(
                                    com.example.R.string.google_auth_failed_format,
                                    e.localizedMessage ?: "Akun sudah terdaftar dengan metode lain"
                                ),
                                android.widget.Toast.LENGTH_LONG
                            ).show()
                        } catch (e: Throwable) {
                            val errMsg = e.message.orEmpty()
                            val isCertHashError = errMsg.contains("certificate hash", ignoreCase = true) ||
                                errMsg.contains("INVALID_CERT_HASH", ignoreCase = true) ||
                                errMsg.contains("17499", ignoreCase = true) ||
                                errMsg.contains("17028", ignoreCase = true) ||
                                errMsg.contains("DEVELOPER_ERROR", ignoreCase = true)
                            val isKeystoreError = FirebaseAuthKeyResetHelper.isKeystoreOrPublicKeyError(errMsg)
                            val isUserCancelled = !isCertHashError && !isKeystoreError && (
                                errMsg.contains("canceled by the user", ignoreCase = true) ||
                                errMsg.contains("cancelled by the user", ignoreCase = true) ||
                                errMsg.contains("WEB_CONTEXT_CANCELED", ignoreCase = true)
                            )
                            android.util.Log.w("MainActivity", "Google OAuth failed: ${e::class.simpleName} - $errMsg", e)
                            if (isCertHashError) {
                                googleCertDialogInfo = errMsg.ifBlank { "certificate_hash_error" } to
                                    SigningCertificateHelper.getSigningFingerprints(activity)
                            } else {
                                val toastText = when {
                                    isUserCancelled -> context.getString(com.example.R.string.google_auth_cancelled)
                                    isKeystoreError -> context.getString(com.example.R.string.google_auth_keystore_recovery_hint)
                                    else -> context.getString(
                                        com.example.R.string.google_auth_failed_format,
                                        errMsg.ifBlank {
                                            context.getString(com.example.R.string.google_auth_generic_error)
                                        }
                                    )
                                }
                                android.widget.Toast.makeText(
                                    context,
                                    toastText,
                                    android.widget.Toast.LENGTH_LONG
                                ).show()
                            }
                        } finally {
                            isGoogleAuthLoading = false
                        }
                    }
                }
            }
        }

        LaunchedEffect(banStatus.isBanned, currentRoute) {
            if (banStatus.isBanned && currentRoute != null && currentRoute !in setOf("splash", "login", "register", "forgot_password", "banned")) {
                navController.navigate("banned") {
                    popUpTo(navController.graph.id) { inclusive = true }
                }
            }
        }

        NavHost(
            navController = navController,
            startDestination = "splash",
            enterTransition = {
                val initialRoute = initialState.destination.route
                val targetRoute = targetState.destination.route
                when {
                    initialRoute == "splash" && (targetRoute == "login" || targetRoute == "onboarding") -> {
                        fadeIn(tween(400)) + scaleIn(
                            initialScale = 0.94f,
                            animationSpec = tween(400, easing = FastOutSlowInEasing)
                        )
                    }
                    (initialRoute == "register" || initialRoute == "login") && targetRoute == "onboarding" -> {
                        fadeIn(tween(450, easing = FastOutSlowInEasing)) + scaleIn(
                            initialScale = 0.96f,
                            animationSpec = tween(450, easing = FastOutSlowInEasing)
                        )
                    }
                    initialRoute in tabRoutes && targetRoute in tabRoutes -> {
                        val initialIndex = tabOrder.indexOf(initialRoute)
                        val targetIndex = tabOrder.indexOf(targetRoute)
                        val isForward = targetIndex >= initialIndex
                        val offset = if (isForward) tabSlideOffsetPx else -tabSlideOffsetPx
                        slideInHorizontally(
                            initialOffsetX = { offset },
                            animationSpec = tween(220, easing = FastOutSlowInEasing)
                        ) + fadeIn(animationSpec = tween(220))
                    }
                    initialRoute in authRoutes || targetRoute in authRoutes -> {
                        fadeIn(animationSpec = tween(300))
                    }
                    else -> {
                        slideIntoContainer(
                            towards = AnimatedContentTransitionScope.SlideDirection.Left,
                            animationSpec = tween(280, easing = FastOutSlowInEasing)
                        ) + fadeIn(animationSpec = tween(280))
                    }
                }
            },
            exitTransition = {
                val initialRoute = initialState.destination.route
                val targetRoute = targetState.destination.route
                when {
                    initialRoute == "splash" -> {
                        fadeOut(tween(250)) + scaleOut(
                            targetScale = 1.04f,
                            animationSpec = tween(250)
                        )
                    }
                    initialRoute in tabRoutes && targetRoute in tabRoutes -> {
                        val initialIndex = tabOrder.indexOf(initialRoute)
                        val targetIndex = tabOrder.indexOf(targetRoute)
                        val isForward = targetIndex >= initialIndex
                        val offset = if (isForward) -tabSlideOffsetPx else tabSlideOffsetPx
                        slideOutHorizontally(
                            targetOffsetX = { offset },
                            animationSpec = tween(200, easing = FastOutSlowInEasing)
                        ) + fadeOut(animationSpec = tween(200))
                    }
                    initialRoute in authRoutes || targetRoute in authRoutes -> {
                        fadeOut(animationSpec = tween(300))
                    }
                    else -> {
                        slideOutOfContainer(
                            towards = AnimatedContentTransitionScope.SlideDirection.Left,
                            animationSpec = tween(280, easing = FastOutSlowInEasing)
                        ) + fadeOut(animationSpec = tween(280))
                    }
                }
            },
            popEnterTransition = {
                val initialRoute = initialState.destination.route
                val targetRoute = targetState.destination.route
                when {
                    initialRoute in tabRoutes && targetRoute in tabRoutes -> {
                        val initialIndex = tabOrder.indexOf(initialRoute)
                        val targetIndex = tabOrder.indexOf(targetRoute)
                        val isForward = targetIndex >= initialIndex
                        val offset = if (isForward) tabSlideOffsetPx else -tabSlideOffsetPx
                        slideInHorizontally(
                            initialOffsetX = { offset },
                            animationSpec = tween(220, easing = FastOutSlowInEasing)
                        ) + fadeIn(animationSpec = tween(220))
                    }
                    initialRoute in authRoutes || targetRoute in authRoutes -> {
                        fadeIn(animationSpec = tween(300))
                    }
                    else -> {
                        slideIntoContainer(
                            towards = AnimatedContentTransitionScope.SlideDirection.Right,
                            animationSpec = tween(280, easing = FastOutSlowInEasing)
                        ) + fadeIn(animationSpec = tween(280))
                    }
                }
            },
            popExitTransition = {
                val initialRoute = initialState.destination.route
                val targetRoute = targetState.destination.route
                when {
                    initialRoute in tabRoutes && targetRoute in tabRoutes -> {
                        val initialIndex = tabOrder.indexOf(initialRoute)
                        val targetIndex = tabOrder.indexOf(targetRoute)
                        val isForward = targetIndex >= initialIndex
                        val offset = if (isForward) -tabSlideOffsetPx else tabSlideOffsetPx
                        slideOutHorizontally(
                            targetOffsetX = { offset },
                            animationSpec = tween(200, easing = FastOutSlowInEasing)
                        ) + fadeOut(animationSpec = tween(200))
                    }
                    initialRoute in authRoutes || targetRoute in authRoutes -> {
                        fadeOut(animationSpec = tween(300))
                    }
                    else -> {
                        slideOutOfContainer(
                            towards = AnimatedContentTransitionScope.SlideDirection.Right,
                            animationSpec = tween(280, easing = FastOutSlowInEasing)
                        ) + fadeOut(animationSpec = tween(280))
                    }
                }
            },
            modifier = Modifier
                .fillMaxSize()
                .padding(if (isSplashScreen) PaddingValues(0.dp) else innerPadding)
                .consumeWindowInsets(if (isSplashScreen) PaddingValues(0.dp) else innerPadding)
        ) {
            // 1. Splash
            composable("splash") {
                var resolvedDestination by remember { mutableStateOf<InitialDestination?>(null) }
                SplashScreen(
                    onPrepare = {
                        val destination = viewModel.prepareAppReadiness(context)
                        resolvedDestination = destination
                    },
                    onSplashFinished = {
                        val destination = resolvedDestination ?: InitialDestination.MAIN
                        when (destination) {
                            InitialDestination.LOGIN -> {
                                navController.navigate("login") {
                                    popUpTo("splash") { inclusive = true }
                                }
                            }
                            InitialDestination.ONBOARDING -> {
                                navController.navigate("onboarding") {
                                    popUpTo("splash") { inclusive = true }
                                }
                            }
                            InitialDestination.MAIN -> {
                                navController.navigate("home") {
                                    popUpTo("splash") { inclusive = true }
                                }
                            }
                            InitialDestination.BANNED -> {
                                navController.navigate("banned") {
                                    popUpTo("splash") { inclusive = true }
                                }
                            }
                        }
                    }
                )
            }

            // 1b. Account Suspended / Banned Screen
            composable("banned") {
                AccountBannedScreen(
                    userEmail = currentUserProfile?.email.orEmpty(),
                    userName = currentUserProfile?.name.orEmpty(),
                    banStatus = banStatus,
                    onRefreshStatus = {
                        val dest = viewModel.refreshBanStatusAndResolveDestination()
                        if (dest != InitialDestination.BANNED) {
                            val target = when (dest) {
                                InitialDestination.MAIN -> "home"
                                InitialDestination.ONBOARDING -> "onboarding"
                                else -> "login"
                            }
                            navController.navigate(target) {
                                popUpTo(navController.graph.id) { inclusive = true }
                            }
                            false
                        } else {
                            true
                        }
                    },
                    onLogout = {
                        coroutineScope.launch {
                            viewModel.logout()
                            navController.navigate("login") {
                                popUpTo(navController.graph.id) { inclusive = true }
                            }
                        }
                    }
                )
            }

            // 2. Login
            composable("login") {
                LoginScreen(
                    onLoginSubmit = { email, password ->
                        val result = viewModel.login(email, password)
                        if (result.isSuccess) {
                            val targetRoute = when {
                                viewModel.banStatus.value.isBanned -> "banned"
                                result.getOrDefault(false) -> "home"
                                else -> "onboarding"
                            }
                            navController.navigate(targetRoute) {
                                popUpTo("login") { inclusive = true }
                            }
                            Result.success(Unit)
                        } else {
                            Result.failure(result.exceptionOrNull() ?: Exception("Login gagal"))
                        }
                    },
                    onNavigateToRegister = {
                        navController.navigate("register")
                    },
                    onForgotPassword = {
                        navController.navigate("forgot_password")
                    },
                    onGoogleSignIn = { launchGoogleAuth("login") },
                    isGoogleLoading = isGoogleAuthLoading
                )
            }

            // Forgot Password
            composable("forgot_password") {
                ForgotPasswordScreen(
                    onNavigateBack = {
                        navController.popBackStack()
                    }
                )
            }

            // 3. Register
            composable("register") {
                RegisterScreen(
                    onRegisterSubmit = { name, email, password ->
                        val result = viewModel.register(name, email, password)
                        if (result.isSuccess) {
                            val isOnboarded = result.getOrDefault(false)
                            if (isOnboarded) {
                                navController.navigate("home") {
                                    popUpTo("register") { inclusive = true }
                                }
                            } else {
                                navController.navigate("onboarding") {
                                    popUpTo("register") { inclusive = true }
                                }
                            }
                            Result.success(Unit)
                        } else {
                            Result.failure(result.exceptionOrNull() ?: Exception("Registrasi gagal"))
                        }
                    },
                    onNavigateToLogin = {
                        navController.popBackStack()
                    },
                    onGoogleSignIn = { launchGoogleAuth("register") },
                    isGoogleLoading = isGoogleAuthLoading
                )
            }

            // 4. Onboarding
            composable("onboarding") {
                val userProfile by viewModel.userProfile.collectAsStateWithLifecycle()
                BackHandler(enabled = viewModel.isReplayingOnboarding) {
                    viewModel.cancelReplayingOnboarding()
                    navController.popBackStack()
                }

                AuthOnboardingScreen(
                    userName = userProfile?.name ?: "",
                    onFinishOnboarding = { selectedTrackId ->
                        coroutineScope.launch {
                            val wasReplaying = viewModel.isReplayingOnboarding
                            viewModel.completeOnboarding(selectedTrackId)
                            if (wasReplaying) {
                                navController.popBackStack()
                            } else {
                                navController.navigate("home") {
                                    popUpTo("onboarding") { inclusive = true }
                                }
                            }
                        }
                    }
                )
            }

            // 5. Tab: Home (Beranda)
            composable("home") {
                HomeTabScreen(
                    viewModel = viewModel,
                    onNavigateToNotifications = { navController.navigate("notifications") },
                    onNavigateToProfile = {
                        navigateToTab("profile")
                    },
                    onNavigateToLeaderboard = {
                        navigateToTab("komunitas")
                    },
                    onCourseClick = { courseId ->
                        navController.navigate("classDetail/$courseId")
                    },
                    onTrackClick = {
                        navigateToTab("courses")
                    },
                    onQuestClick = {
                        navController.navigate("classDetail/fe-web-foundation-00")
                    },
                    onAskAi = { topic ->
                        val encodedCourse = URLEncoder.encode("Front-End Web Development", "UTF-8")
                        val encodedMod = URLEncoder.encode(topic.ifBlank { "Tips Harian" }, "UTF-8")
                        navController.navigate("aiTutor?courseTitle=$encodedCourse&moduleTitle=$encodedMod")
                    },
                    onTryInPlayground = { snippet, lang ->
                        val encodedCode = URLEncoder.encode(snippet, "UTF-8")
                        navController.navigate("playground/fe-web-foundation-00?code=$encodedCode&lang=$lang")
                    },
                    onMenuClick = openDrawer
                )
            }

            // 6. Tab: Courses (Kelas)
            composable("courses") {
                BackHandler {
                    navigateToTab("home")
                }

                CourseCatalogScreen(
                    viewModel = viewModel,
                    onCourseClick = { courseId ->
                        navController.navigate("classDetail/$courseId")
                    },
                    onMenuClick = openDrawer
                )
            }

            // 7. Tab: Progress (Progres)
            composable("progress") {
                BackHandler {
                    navigateToTab("home")
                }

                ProgressScreen(
                    viewModel = viewModel,
                    onViewCertificates = { navController.navigate("certificateList") },
                    onCourseClick = { courseId -> navController.navigate("classDetail/$courseId") },
                    onMenuClick = openDrawer
                )
            }

            // 8. Tab: Komunitas (Community)
            composable("komunitas") {
                BackHandler {
                    navigateToTab("home")
                }

                CommunityScreen(
                    viewModel = viewModel,
                    onCourseForumClick = { courseId ->
                        navController.navigate("courseForum/$courseId")
                    },
                    onExploreCourses = {
                        navigateToTab("courses")
                    },
                    onOpenTrendingDiscussions = {
                        navController.navigate("diskusiHangat")
                    },
                    onMenuClick = openDrawer
                )
            }

            // 9. Tab: Profile (Profil)
            composable("profile") {
                BackHandler {
                    navigateToTab("home")
                }

                ProfileScreen(
                    viewModel = viewModel,
                    onNavigateToBookmarks = { navController.navigate("bookmarks") },
                    onNavigateToNotifications = { navController.navigate("notifications") },
                    onNavigateToCertificates = { navController.navigate("certificateList") },
                    onResetOnboarding = { navController.navigate("onboarding") },
                    onLogout = {
                        coroutineScope.launch {
                            viewModel.logout()
                            navController.navigate("login") {
                                popUpTo(0) { inclusive = true }
                            }
                        }
                    },
                    onDeleteAccount = {
                        coroutineScope.launch {
                            viewModel.deleteAccount()
                            navController.navigate("login") {
                                popUpTo(0) { inclusive = true }
                            }
                        }
                    },
                    onMenuClick = openDrawer
                )
            }

            // 9. Class Detail
            composable(
                route = "classDetail/{courseId}",
                arguments = listOf(
                    navArgument("courseId") { type = NavType.StringType }
                )
            ) { backStackEntry ->
                val courseId = backStackEntry.arguments?.getString("courseId") ?: ""
                val course = viewModel.getCourseById(courseId)
                if (course != null) {
                    val progressList by viewModel.moduleProgressList.collectAsStateWithLifecycle()
                    val bookmarks by viewModel.bookmarks.collectAsStateWithLifecycle()
                    val passedQuizIds by viewModel.passedSectionQuizIds.collectAsStateWithLifecycle()
                    val projectSubmission by viewModel.getProjectSubmissionFlow(course.id).collectAsStateWithLifecycle(initialValue = null)
                    val certificates by viewModel.certificates.collectAsStateWithLifecycle()
                    val certForCourse = certificates.find { it.courseId == course.id }
                    ClassDetailScreen(
                        course = course,
                        progressList = progressList,
                        bookmarks = bookmarks,
                        passedQuizIds = passedQuizIds,
                        projectSubmission = projectSubmission,
                        hasCertificate = certForCourse != null,
                        onBack = { navController.popBackStack() },
                        onModuleClick = { modId ->
                            navController.navigate("materi/${course.id}/$modId")
                        },
                        onStartQuiz = {
                            navController.navigate("quiz/${course.id}")
                        },
                        onToggleBookmark = {
                            viewModel.toggleBookmark(
                                "CLASS",
                                course.id,
                                course.title,
                                context.getString(R.string.course_bookmark_subtitle_format, course.modules.size, course.level),
                                course.trackId
                            )
                        },
                        onOpenForum = {
                            navController.navigate("courseForum/${course.id}")
                        },
                        onStartSectionQuiz = { quizId ->
                            navController.navigate("section_quiz_rules/${course.id}/$quizId")
                        },
                        onOpenCheatSheet = {
                            navController.navigate("cheatSheet/${course.id}")
                        },
                        onStartPlacementTest = {
                            navController.navigate("placement_test/${course.id}")
                        },
                        onOpenFinalProject = {
                            navController.navigate("finalProject/${course.id}")
                        },
                        onViewCertificate = {
                            if (certForCourse != null) {
                                navController.navigate("certificateView/${certForCourse.certId}")
                            } else {
                                navController.navigate("certificateList")
                            }
                        }
                    )
                }
            }

            // 9b. Cheat Sheet Screen
            composable(
                route = "cheatSheet/{courseId}",
                arguments = listOf(
                    navArgument("courseId") { type = NavType.StringType }
                )
            ) { backStackEntry ->
                val courseId = backStackEntry.arguments?.getString("courseId") ?: ""
                val course = viewModel.getCourseById(courseId)
                if (course != null) {
                    CheatSheetScreen(
                        course = course,
                        onBack = { navController.popBackStack() }
                    )
                }
            }

            // 9c. Placement Test Screen
            composable(
                route = "placement_test/{courseId}",
                arguments = listOf(
                    navArgument("courseId") { type = NavType.StringType }
                )
            ) { backStackEntry ->
                val courseId = backStackEntry.arguments?.getString("courseId") ?: ""
                val course = viewModel.getCourseById(courseId)
                if (course != null) {
                    var questions by remember(courseId) { mutableStateOf<List<PlacementTestQuestion>>(emptyList()) }
                    var isLoadingQuestions by remember(courseId) { mutableStateOf(true) }

                    LaunchedEffect(courseId) {
                        questions = viewModel.generatePlacementTestQuestions(course)
                        isLoadingQuestions = false
                    }

                    if (isLoadingQuestions) {
                        Box(
                            modifier = Modifier.fillMaxSize(),
                            contentAlignment = Alignment.Center
                        ) {
                            CircularProgressIndicator(color = MaterialTheme.colorScheme.primary)
                        }
                    } else {
                        PlacementTestScreen(
                            course = course,
                            questions = questions,
                            onSubmitTest = { answers ->
                                viewModel.submitPlacementTest(course, questions, answers)
                            },
                            onBack = { navController.popBackStack() }
                        )
                    }
                }
            }

            // 10. Materi
            composable(
                route = "materi/{courseId}/{moduleId}",
                arguments = listOf(
                    navArgument("courseId") { type = NavType.StringType },
                    navArgument("moduleId") { type = NavType.StringType }
                )
            ) { backStackEntry ->
                val courseId = backStackEntry.arguments?.getString("courseId") ?: ""
                val moduleId = backStackEntry.arguments?.getString("moduleId") ?: ""
                val course = viewModel.getCourseById(courseId)
                if (course != null) {
                    val progressList by viewModel.moduleProgressList.collectAsStateWithLifecycle()
                    val bookmarks by viewModel.bookmarks.collectAsStateWithLifecycle()
                    val passedQuizIds by viewModel.passedSectionQuizIds.collectAsStateWithLifecycle()
                    val verifiedModuleIds by viewModel.verifiedModuleIds.collectAsStateWithLifecycle()
                    MateriScreen(
                        course = course,
                        moduleId = moduleId,
                        progressList = progressList,
                        bookmarks = bookmarks,
                        passedQuizIds = passedQuizIds,
                        verifiedModuleIds = verifiedModuleIds,
                        onBack = { navController.popBackStack() },
                        onSelectModule = { modId ->
                            navController.navigate("materi/${course.id}/$modId") {
                                popUpTo("classDetail/${course.id}")
                            }
                        },
                        onCompleteModule = { cId, modId ->
                            viewModel.completeModule(cId, modId)
                        },
                        onOpenPlayground = { cId, modId, code, lang ->
                            val codeParam = if (!code.isNullOrBlank()) {
                                try {
                                    "&code=" + URLEncoder.encode(code, "UTF-8")
                                } catch (_: Exception) { "" }
                            } else ""
                            val langParam = if (!lang.isNullOrBlank()) "&lang=$lang" else ""
                            navController.navigate("playground/$cId?moduleId=$modId$codeParam$langParam")
                        },
                        onOpenAiTutor = { courseTitle, moduleTitle ->
                            try {
                                val encCourse = URLEncoder.encode(courseTitle, "UTF-8")
                                val encModule = URLEncoder.encode(moduleTitle, "UTF-8")
                                navController.navigate("aiTutor?courseTitle=$encCourse&moduleTitle=$encModule")
                            } catch (_: Exception) {
                                navController.navigate("aiTutor?courseTitle=Materi&moduleTitle=General")
                            }
                        },
                        onToggleBookmark = { module ->
                            viewModel.toggleBookmark(
                                type = "MODULE",
                                itemId = module.id,
                                title = module.title,
                                subtitle = course.title,
                                trackName = course.trackId
                            )
                        },
                        onTranslateToEnglish = { modId, markdown ->
                            viewModel.translateModuleToEnglish(modId, markdown)
                        }
                    )
                }
            }

            // 11. Playground
            composable(
                route = "playground/{courseId}?moduleId={moduleId}&code={code}&lang={lang}",
                arguments = listOf(
                    navArgument("courseId") { type = NavType.StringType },
                    navArgument("moduleId") {
                        type = NavType.StringType
                        nullable = true
                        defaultValue = null
                    },
                    navArgument("code") {
                        type = NavType.StringType
                        nullable = true
                        defaultValue = null
                    },
                    navArgument("lang") {
                        type = NavType.StringType
                        nullable = true
                        defaultValue = null
                    }
                )
            ) { backStackEntry ->
                val courseId = backStackEntry.arguments?.getString("courseId") ?: ""
                val moduleId = backStackEntry.arguments?.getString("moduleId")
                val rawCode = backStackEntry.arguments?.getString("code")
                val initialCode = rawCode?.let {
                    try { URLDecoder.decode(it, "UTF-8") } catch (_: Exception) { it }
                }
                val initialLanguage = backStackEntry.arguments?.getString("lang")
                val moduleAnalysisTitle = stringResource(R.string.common_code_analysis)
                val playgroundAiPromptCtx = LocalContext.current
                PlaygroundScreen(
                    courseId = courseId,
                    moduleId = moduleId,
                    initialCode = initialCode,
                    initialLanguage = initialLanguage,
                    onBack = { navController.popBackStack() },
                    onOpenAiTutor = { prompt ->
                        try {
                            val cObj = viewModel.getCourseById(courseId)
                            val courseTitle = cObj?.title ?: "Playground"
                            val moduleTitle = moduleAnalysisTitle

                            // Send prompt as initial user query to AI Tutor if not blank
                            if (prompt.isNotBlank()) {
                                viewModel.askAi(
                                    userQuery = playgroundAiPromptCtx.getString(R.string.playground_ai_prompt_format, prompt),
                                    courseTitle = courseTitle,
                                    moduleTitle = moduleTitle
                                )
                            }

                            val encCourse = URLEncoder.encode(courseTitle, "UTF-8")
                            val encModule = URLEncoder.encode(moduleTitle, "UTF-8")
                            navController.navigate("aiTutor?courseTitle=$encCourse&moduleTitle=$encModule")
                        } catch (_: Exception) {
                            try {
                                navController.navigate("aiTutor?courseTitle=Playground&moduleTitle=Analisis%20Kode")
                            } catch (_: Exception) {}
                        }
                    }
                )
            }

            // 12. Quiz
            composable(
                route = "quiz/{courseId}",
                arguments = listOf(
                    navArgument("courseId") { type = NavType.StringType }
                )
            ) { backStackEntry ->
                val courseId = backStackEntry.arguments?.getString("courseId") ?: ""
                val course = viewModel.getCourseById(courseId)
                if (course != null) {
                    QuizScreen(
                        course = course,
                        onBack = { navController.popBackStack() },
                        onSubmit = { scorePercent ->
                            viewModel.submitQuiz(course.id, scorePercent) { isPassed, _ ->
                                navController.navigate("hasilEvaluasi/${course.id}/$scorePercent/$isPassed") {
                                    popUpTo("classDetail/${course.id}")
                                }
                            }
                        }
                    )
                }
            }

            // 13. Hasil Evaluasi
            composable(
                route = "hasilEvaluasi/{courseId}/{scorePercent}/{isPassed}",
                arguments = listOf(
                    navArgument("courseId") { type = NavType.StringType },
                    navArgument("scorePercent") { type = NavType.IntType },
                    navArgument("isPassed") { type = NavType.BoolType }
                )
            ) { backStackEntry ->
                val courseId = backStackEntry.arguments?.getString("courseId") ?: ""
                val scorePercent = backStackEntry.arguments?.getInt("scorePercent") ?: 0
                val isPassed = backStackEntry.arguments?.getBoolean("isPassed") ?: false
                val course = viewModel.getCourseById(courseId)
                if (course != null) {
                    val certificates by viewModel.certificates.collectAsStateWithLifecycle()
                    val cert = certificates.find { it.courseId == course.id }
                    HasilEvaluasiScreen(
                        course = course,
                        scorePercent = scorePercent,
                        isPassed = isPassed,
                        isCertificateIssued = cert != null,
                        onRetakeQuiz = {
                            navController.navigate("quiz/${course.id}") {
                                popUpTo("classDetail/${course.id}")
                            }
                        },
                        onBackToCourse = {
                            navController.popBackStack()
                        },
                        onOpenFinalProject = {
                            navController.navigate("finalProject/${course.id}") {
                                popUpTo("classDetail/${course.id}")
                            }
                        },
                        onViewCertificate = {
                            if (cert != null) {
                                navController.navigate("certificateView/${cert.certId}") {
                                    popUpTo("classDetail/${course.id}")
                                }
                            } else {
                                navController.navigate("finalProject/${course.id}") {
                                    popUpTo("classDetail/${course.id}")
                                }
                            }
                        }
                    )
                }
            }

            // 13-Project. Final Project Submission Screen
            composable(
                route = "finalProject/{courseId}",
                arguments = listOf(
                    navArgument("courseId") { type = NavType.StringType }
                )
            ) { backStackEntry ->
                val courseId = backStackEntry.arguments?.getString("courseId") ?: ""
                val course = viewModel.getCourseById(courseId)
                if (course != null) {
                    val submission by viewModel.getProjectSubmissionFlow(course.id).collectAsStateWithLifecycle(initialValue = null)
                    val certificates by viewModel.certificates.collectAsStateWithLifecycle()

                    LaunchedEffect(course.id) {
                        viewModel.ensureProjectSubmissionInitialized(course.id)
                    }

                    FinalProjectSubmissionScreen(
                        course = course,
                        submission = submission,
                        onBack = { navController.popBackStack() },
                        onLoadPlaygroundDraft = {
                            viewModel.getPlaygroundDraft(course.id)?.let { draft ->
                                buildString {
                                    if (draft.htmlCode.isNotBlank()) append(draft.htmlCode)
                                    if (draft.cssCode.isNotBlank()) append("\n<style>\n${draft.cssCode}\n</style>")
                                    if (draft.jsCode.isNotBlank()) append("\n<script>\n${draft.jsCode}\n</script>")
                                }.ifBlank { null }
                            }
                        },
                        onSaveDraft = { code, repoUrl, notes, rubrics ->
                            viewModel.saveProjectSubmissionDraft(course.id, code, repoUrl, notes, rubrics)
                        },
                        onSubmitProject = { code, repoUrl, notes, rubrics ->
                            viewModel.submitFinalProject(course.id, code, repoUrl, notes, rubrics)
                        },
                        onSimulateFacilitatorReview = { approved, score, feedback, onCertReady ->
                            viewModel.simulateFacilitatorReview(
                                courseId = course.id,
                                approved = approved,
                                facilitatorScore = score,
                                facilitatorFeedback = feedback,
                                onResult = { result ->
                                    val cert = result.getOrNull()?.second
                                    onCertReady(cert?.certId)
                                }
                            )
                        },
                        onAttachRealtimeListener = {
                            viewModel.attachProjectSubmissionListener(course.id)
                        },
                        onViewCertificate = {
                            val cert = certificates.find { it.courseId == course.id }
                            if (cert != null) {
                                navController.navigate("certificateView/${cert.certId}") {
                                    popUpTo("classDetail/${course.id}")
                                }
                            } else {
                                navController.navigate("certificateList")
                            }
                        }
                    )
                }
            }

            // 13a. Section Quiz Rules & History
            composable(
                route = "section_quiz_rules/{courseId}/{quizId}",
                arguments = listOf(
                    navArgument("courseId") { type = NavType.StringType },
                    navArgument("quizId") { type = NavType.StringType }
                )
            ) { backStackEntry ->
                val courseId = backStackEntry.arguments?.getString("courseId") ?: ""
                val quizId = backStackEntry.arguments?.getString("quizId") ?: ""
                val course = viewModel.getCourseById(courseId)
                val quiz = course?.sectionQuizzes?.find { it.id == quizId }
                if (course != null && quiz != null) {
                    val attempts by viewModel.getSectionQuizAttempts(quizId).collectAsStateWithLifecycle(emptyList())
                    SectionQuizRulesScreen(
                        course = course,
                        quiz = quiz,
                        attempts = attempts,
                        onBack = { navController.popBackStack() },
                        onStartQuiz = {
                            navController.navigate("section_quiz/$courseId/$quizId")
                        },
                        onViewDetail = { attemptId ->
                            navController.navigate("section_quiz_result/$courseId/$quizId/$attemptId")
                        }
                    )
                }
            }

            // 13b. Section Quiz Question Screen
            composable(
                route = "section_quiz/{courseId}/{quizId}",
                arguments = listOf(
                    navArgument("courseId") { type = NavType.StringType },
                    navArgument("quizId") { type = NavType.StringType }
                )
            ) { backStackEntry ->
                val courseId = backStackEntry.arguments?.getString("courseId") ?: ""
                val quizId = backStackEntry.arguments?.getString("quizId") ?: ""
                val course = viewModel.getCourseById(courseId)
                val quiz = course?.sectionQuizzes?.find { it.id == quizId }
                if (course != null && quiz != null) {
                    var questions by remember { mutableStateOf<List<QuizQuestion>>(emptyList()) }
                    LaunchedEffect(quizId) {
                        questions = viewModel.generateSectionQuizQuestions(quiz)
                    }
                    SectionQuizScreen(
                        course = course,
                        quiz = quiz,
                        questions = questions,
                        viewModel = viewModel,
                        onBack = { navController.popBackStack() },
                        onSubmitSuccess = { attemptId ->
                            navController.navigate("section_quiz_result/$courseId/$quizId/$attemptId") {
                                popUpTo("section_quiz_rules/$courseId/$quizId")
                            }
                        }
                    )
                }
            }

            // 13c. Section Quiz Result Screen
            composable(
                route = "section_quiz_result/{courseId}/{quizId}/{attemptId}",
                arguments = listOf(
                    navArgument("courseId") { type = NavType.StringType },
                    navArgument("quizId") { type = NavType.StringType },
                    navArgument("attemptId") { type = NavType.LongType }
                )
            ) { backStackEntry ->
                val courseId = backStackEntry.arguments?.getString("courseId") ?: ""
                val quizId = backStackEntry.arguments?.getString("quizId") ?: ""
                val attemptId = backStackEntry.arguments?.getLong("attemptId") ?: 0L
                val course = viewModel.getCourseById(courseId)
                val quiz = course?.sectionQuizzes?.find { it.id == quizId }
                if (course != null && quiz != null) {
                    var attempt by remember { mutableStateOf<SectionQuizAttemptEntity?>(null) }
                    LaunchedEffect(attemptId) {
                        attempt = viewModel.getSectionQuizAttemptById(attemptId)
                    }
                    attempt?.let { att ->
                        SectionQuizResultScreen(
                            course = course,
                            quiz = quiz,
                            attempt = att,
                            onBackToCourse = {
                                navController.navigate("classDetail/$courseId") {
                                    popUpTo("classDetail/$courseId") { inclusive = true }
                                }
                            },
                            onBackToRules = {
                                navController.navigate("section_quiz_rules/$courseId/$quizId") {
                                    popUpTo("section_quiz_rules/$courseId/$quizId") { inclusive = true }
                                }
                            },
                            onRetakeQuiz = {
                                navController.navigate("section_quiz/$courseId/$quizId") {
                                    popUpTo("section_quiz_rules/$courseId/$quizId")
                                }
                            },
                            onNavigateToModule = { targetModuleId ->
                                navController.navigate("materi/$courseId/$targetModuleId")
                            }
                        )
                    }
                }
            }

            // 14. Certificate List
            composable("certificateList") {
                val certificates by viewModel.certificates.collectAsStateWithLifecycle()
                CertificateListScreen(
                    certificates = certificates,
                    onBack = { navController.popBackStack() },
                    onExploreCourses = {
                        navigateToTab("courses")
                    },
                    onViewCertificate = { certId ->
                        navController.navigate("certificateView/$certId")
                    }
                )
            }

            // 15. Certificate View
            composable(
                route = "certificateView/{certId}",
                arguments = listOf(
                    navArgument("certId") { type = NavType.StringType }
                )
            ) { backStackEntry ->
                val certId = backStackEntry.arguments?.getString("certId") ?: ""
                val certificates by viewModel.certificates.collectAsStateWithLifecycle()
                val cert = certificates.find { it.certId == certId }
                    ?: certificates.firstOrNull()
                CertificateScreen(
                    certificate = cert,
                    onBack = { navController.popBackStack() }
                )
            }

            // 16. AI Tutor
            composable(
                route = "aiTutor?courseTitle={courseTitle}&moduleTitle={moduleTitle}",
                arguments = listOf(
                    navArgument("courseTitle") {
                        type = NavType.StringType
                        defaultValue = "Fondasi Web: Dari Nol ke Baris Kode Pertama"
                    },
                    navArgument("moduleTitle") {
                        type = NavType.StringType
                        defaultValue = "Pengantar Web"
                    }
                )
            ) { backStackEntry ->
                val rawCourseTitle = backStackEntry.arguments?.getString("courseTitle") ?: "Fondasi Web: Dari Nol ke Baris Kode Pertama"
                val rawModuleTitle = backStackEntry.arguments?.getString("moduleTitle") ?: "Pengantar Web"
                val courseTitle = try { URLDecoder.decode(rawCourseTitle, "UTF-8") } catch (_: Exception) { rawCourseTitle }
                val moduleTitle = try { URLDecoder.decode(rawModuleTitle, "UTF-8") } catch (_: Exception) { rawModuleTitle }
                val chatMessages by viewModel.chatMessages.collectAsStateWithLifecycle()
                val isAiThinking by viewModel.isAiThinking.collectAsStateWithLifecycle()
                val aiConnectionStatus by viewModel.aiConnectionStatus.collectAsStateWithLifecycle()

                AiTutorScreen(
                    courseTitle = courseTitle,
                    moduleTitle = moduleTitle,
                    messages = chatMessages,
                    isThinking = isAiThinking,
                    connectionStatus = aiConnectionStatus,
                    onSendMessage = { query, imageUri ->
                        viewModel.askAi(query, courseTitle, moduleTitle, imageUri = imageUri)
                    },
                    onClearChat = { viewModel.clearAiChat() },
                    onTryInPlayground = { snippet, lang ->
                        val encodedCode = URLEncoder.encode(snippet, "UTF-8")
                        val normalizedLang = when (lang.lowercase()) {
                            "css" -> "css"
                            "js", "javascript" -> "javascript"
                            else -> "html"
                        }
                        navController.navigate("playground/fe-web-foundation-00?code=$encodedCode&lang=$normalizedLang")
                    },
                    onBack = { navController.popBackStack() }
                )
            }

            // 17. Bookmarks
            composable("bookmarks") {
                val bookmarks by viewModel.bookmarks.collectAsStateWithLifecycle()
                val courses by viewModel.catalogCourses.collectAsStateWithLifecycle()
                BookmarksScreen(
                    bookmarks = bookmarks,
                    onBack = { navController.popBackStack() },
                    onOpenBookmark = { item ->
                        if (item.type == "CLASS" || item.type == "COURSE") {
                            navController.navigate("classDetail/${item.itemId}")
                        } else {
                            val parentCourse = courses.find { c -> c.modules.any { it.id == item.itemId } }
                            if (parentCourse != null) {
                                navController.navigate("materi/${parentCourse.id}/${item.itemId}")
                            } else {
                                navigateToTab("courses")
                            }
                        }
                    },
                    onRemoveBookmark = { itemId ->
                        viewModel.removeBookmark(itemId)
                    }
                )
            }

            // Diskusi Hangat (Forum Highlights)
            composable("diskusiHangat") {
                DiskusiHangatScreen(
                    viewModel = viewModel,
                    onBack = { navController.popBackStack() },
                    onCourseForumClick = { courseId ->
                        navController.navigate("courseForum/$courseId")
                    }
                )
            }

            // 18. Notifications
            composable("notifications") {
                NotificationsScreen(
                    viewModel = viewModel,
                    onBack = { navController.popBackStack() }
                )
            }

            // 19. Forum Diskusi per Kelas
            composable(
                route = "courseForum/{courseId}",
                arguments = listOf(
                    navArgument("courseId") { type = NavType.StringType }
                )
            ) { backStackEntry ->
                val courseId = backStackEntry.arguments?.getString("courseId") ?: ""
                val course = viewModel.getCourseById(courseId)
                if (course != null) {
                    CourseForumScreen(
                        course = course,
                        viewModel = viewModel,
                        onBack = { navController.popBackStack() }
                    )
                } else {
                    androidx.compose.foundation.layout.Box(
                        modifier = androidx.compose.ui.Modifier.fillMaxSize(),
                        contentAlignment = androidx.compose.ui.Alignment.Center
                    ) {
                        androidx.compose.foundation.layout.Column(
                            horizontalAlignment = androidx.compose.ui.Alignment.CenterHorizontally,
                            modifier = androidx.compose.ui.Modifier.padding(24.dp)
                        ) {
                            androidx.compose.material3.Text(
                                text = androidx.compose.ui.res.stringResource(R.string.forum_course_not_found),
                                style = androidx.compose.material3.MaterialTheme.typography.titleMedium
                            )
                            androidx.compose.foundation.layout.Spacer(modifier = androidx.compose.ui.Modifier.height(12.dp))
                            androidx.compose.material3.Button(onClick = { navController.popBackStack() }) {
                                androidx.compose.material3.Text(androidx.compose.ui.res.stringResource(R.string.common_back))
                            }
                        }
                    }
                }
            }

            // 20. Search (Cari Materi Lintas Kelas)
            composable("search") {
                SearchScreen(
                    viewModel = viewModel,
                    onBack = { navController.popBackStack() },
                    onNavigateToModule = { courseId, moduleId ->
                        navController.navigate("materi/$courseId/$moduleId")
                    }
                )
            }

            // 21. Help & FAQ (Bantuan, Kebijakan Privasi, dan Kontak)
            composable("help") {
                HelpScreen(
                    onBack = { navController.popBackStack() }
                )
            }
        }
    }
}

    // Level-Up Celebration Dialog
    levelUpDialog?.let { (level, title) ->
        AppDialog(
            onDismissRequest = { viewModel.dismissLevelUpDialog() },
            icon = {
                Box(
                    modifier = Modifier
                        .size(64.dp)
                        .clip(CircleShape)
                        .background(AmberGold.copy(alpha = 0.2f)),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.EmojiEvents,
                        contentDescription = null,
                        tint = AmberGold,
                        modifier = Modifier.size(36.dp)
                    )
                }
            },
            title = {
                Text(
                    text = stringResource(R.string.level_up_dialog_title),
                    fontWeight = FontWeight.ExtraBold,
                    textAlign = TextAlign.Center,
                    style = MaterialTheme.typography.titleLarge,
                    modifier = Modifier.fillMaxWidth()
                )
            },
            text = {
                Column(horizontalAlignment = Alignment.CenterHorizontally, modifier = Modifier.fillMaxWidth()) {
                    Text(
                        text = stringResource(R.string.level_up_dialog_subtitle),
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    Spacer(modifier = Modifier.height(6.dp))
                    Surface(
                        color = AmberGold.copy(alpha = 0.15f),
                        shape = RoundedCornerShape(10.dp)
                    ) {
                        Text(
                            text = stringResource(
                                R.string.level_up_dialog_badge_format,
                                level,
                                com.example.util.localization.DataLabelMappings.xpLevelName(level, title)
                            ),
                            color = AmberGold,
                            fontWeight = FontWeight.Bold,
                            fontSize = 16.sp,
                            modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
                        )
                    }
                    Spacer(modifier = Modifier.height(12.dp))
                    Text(
                        text = stringResource(R.string.level_up_dialog_desc),
                        style = MaterialTheme.typography.bodySmall,
                        textAlign = TextAlign.Center,
                        color = MaterialTheme.colorScheme.onSurface,
                        lineHeight = 18.sp
                    )
                }
            },
            confirmButton = {
                Button(
                    onClick = { viewModel.dismissLevelUpDialog() },
                    shape = RoundedCornerShape(12.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = TealLight),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(stringResource(R.string.level_up_dialog_btn_continue), color = Color.Black, fontWeight = FontWeight.Bold)
                }
            }
        )
    }
}

@Composable
private fun CodeAcademyDrawerContent(
    viewModel: MainViewModel,
    tabRoutes: Set<String>,
    currentRoute: String?,
    isOpen: Boolean,
    onNavigateToTab: (String) -> Unit,
    onNavigate: (String) -> Unit,
    onCloseDrawer: () -> Unit,
    onLogout: () -> Unit
) {
    val userProfile by viewModel.userProfile.collectAsStateWithLifecycle()
    val unreadNotifsCount by viewModel.unreadNotifsCount.collectAsStateWithLifecycle()

    AppNavigationDrawer(
        userProfile = userProfile,
        unreadNotifsCount = unreadNotifsCount,
        currentRoute = currentRoute,
        isOpen = isOpen,
        onCloseDrawer = onCloseDrawer,
        onNavigate = { route ->
            onCloseDrawer()
            if (route in tabRoutes) {
                onNavigateToTab(route)
            } else {
                onNavigate(route)
            }
        },
        onLogout = onLogout
    )
}
