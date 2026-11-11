package com.example.ui.navigation

import androidx.compose.animation.*
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavType
import androidx.navigation.compose.*
import androidx.navigation.navArgument
import com.example.data.model.CertificateEntity
import com.example.data.model.QuizQuestionEntity
import com.example.ui.components.LevelUpDialog
import com.example.ui.screens.aitutor.AiTutorScreen
import com.example.ui.screens.ar.ArVisualizerScreen
import com.example.ui.screens.auth.LoginScreen
import com.example.ui.screens.auth.OnboardingScreen
import com.example.ui.screens.auth.RegisterScreen
import com.example.ui.screens.catalog.CatalogScreen
import com.example.ui.screens.catalog.CourseDetailScreen
import com.example.ui.screens.forum.ForumScreen
import com.example.ui.screens.forum.ThreadDetailScreen
import com.example.ui.screens.home.HomeScreen
import com.example.ui.screens.lesson.ModuleLessonScreen
import com.example.ui.screens.notifications.NotificationsScreen
import com.example.ui.screens.playground.PlaygroundScreen
import com.example.ui.screens.profile.CertificateDetailScreen
import com.example.ui.screens.profile.ProfileScreen
import com.example.ui.screens.quiz.QuizResultScreen
import com.example.ui.screens.quiz.QuizScreen
import com.example.ui.screens.social.ChatListScreen
import com.example.ui.screens.social.ChatRoomScreen
import com.example.ui.screens.social.FriendsScreen
import com.example.ui.theme.*
import com.example.ui.viewmodel.*
import kotlinx.coroutines.launch

sealed class BottomNavItem(
    val route: String,
    val title: String,
    val filledIcon: ImageVector,
    val outlinedIcon: ImageVector
) {
    data object Home : BottomNavItem("home", "Beranda", Icons.Filled.Home, Icons.Outlined.Home)
    data object Catalog : BottomNavItem("catalog", "Katalog", Icons.Filled.MenuBook, Icons.Outlined.MenuBook)
    data object Forum : BottomNavItem("forum", "Forum", Icons.Filled.Forum, Icons.Outlined.Forum)
    data object Profile : BottomNavItem("profile", "Profil", Icons.Filled.Person, Icons.Outlined.Person)
}

@Composable
fun CodeAcademyApp(authViewModel: AuthViewModel = viewModel()) {
    val navController = rememberNavController()
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route
    val coroutineScope = rememberCoroutineScope()

    val authSession by authViewModel.authSession.collectAsStateWithLifecycle()
    val profile by authViewModel.profile.collectAsStateWithLifecycle()

    val bottomNavItems = listOf(
        BottomNavItem.Home,
        BottomNavItem.Catalog,
        BottomNavItem.Forum,
        BottomNavItem.Profile
    )

    val showBottomBar = currentRoute in bottomNavItems.map { it.route }

    Scaffold(
        containerColor = BackgroundDark,
        bottomBar = {
            if (showBottomBar) {
                NavigationBar(
                    containerColor = SurfaceDark,
                    contentColor = SecondaryTeal,
                    tonalElevation = 8.dp,
                    modifier = Modifier.clip(RoundedCornerShape(topStart = 16.dp, topEnd = 16.dp))
                ) {
                    bottomNavItems.forEach { item ->
                        val selected = currentRoute == item.route
                        NavigationBarItem(
                            selected = selected,
                            onClick = {
                                navController.navigate(item.route) {
                                    popUpTo(navController.graph.findStartDestination().id) {
                                        saveState = true
                                    }
                                    launchSingleTop = true
                                    restoreState = true
                                }
                            },
                            icon = {
                                Icon(
                                    imageVector = if (selected) item.filledIcon else item.outlinedIcon,
                                    contentDescription = item.title
                                )
                            },
                            label = { Text(item.title, fontSize = 11.sp) },
                            colors = NavigationBarItemDefaults.colors(
                                selectedIconColor = Color.White,
                                selectedTextColor = SecondaryTeal,
                                indicatorColor = PrimaryPurple,
                                unselectedIconColor = TextSecondaryDark,
                                unselectedTextColor = TextSecondaryDark
                            )
                        )
                    }
                }
            }
        }
    ) { paddingValues ->
        val startDestination = if (authSession == null) {
            "login"
        } else if (profile?.isOnboarded == false) {
            "onboarding"
        } else {
            "home"
        }

        NavHost(
            navController = navController,
            startDestination = startDestination,
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            // ==========================================
            // Auth / Onboarding (AuthViewModel)
            // ==========================================
            composable("login") {
                val vm: AuthViewModel = viewModel()
                LoginScreen(
                    onPerformLogin = { email, password, onResult ->
                        vm.signInWithPassword(email, password, onResult)
                    },
                    onLoginSuccess = { isOnboarded ->
                        if (isOnboarded) {
                            navController.navigate("home") { popUpTo("login") { inclusive = true } }
                        } else {
                            navController.navigate("onboarding") { popUpTo("login") { inclusive = true } }
                        }
                    },
                    onNavigateToRegister = { navController.navigate("register") }
                )
            }

            composable("register") {
                val vm: AuthViewModel = viewModel()
                RegisterScreen(
                    onPerformRegister = { email, password, displayName, onResult ->
                        vm.signUp(email, password, displayName, onResult)
                    },
                    onRegisterSuccess = {
                        navController.navigate("onboarding") { popUpTo("register") { inclusive = true } }
                    },
                    onNavigateToLogin = { navController.popBackStack() }
                )
            }

            composable("onboarding") {
                val vm: AuthViewModel = viewModel()
                val tracks by vm.tracks.collectAsStateWithLifecycle()
                OnboardingScreen(
                    tracks = tracks,
                    onCompleteOnboarding = { selectedTracks, goal ->
                        vm.completeOnboarding(selectedTracks, goal)
                        navController.navigate("home") {
                            popUpTo("onboarding") { inclusive = true }
                        }
                    }
                )
            }

            // ==========================================
            // Home Screen (HomeViewModel)
            // ==========================================
            composable("home") {
                val vm: HomeViewModel = viewModel()
                val userProfile by vm.profile.collectAsStateWithLifecycle()
                val tracks by vm.tracks.collectAsStateWithLifecycle()
                val courses by vm.courses.collectAsStateWithLifecycle()
                val userProgressList by vm.userProgress.collectAsStateWithLifecycle()
                val unreadNotifCount by vm.unreadNotifCount.collectAsStateWithLifecycle()
                val unreadChatCount by vm.unreadChatCount.collectAsStateWithLifecycle()

                HomeScreen(
                    profile = userProfile,
                    tracks = tracks,
                    courses = courses,
                    userProgressList = userProgressList,
                    unreadNotifCount = unreadNotifCount,
                    unreadChatCount = unreadChatCount,
                    onNavigateToCourse = { courseId -> navController.navigate("course_detail/$courseId") },
                    onNavigateToCatalog = { trackId ->
                        if (trackId != null) navController.navigate("catalog?trackId=$trackId")
                        else navController.navigate("catalog")
                    },
                    onNavigateToNotifications = { navController.navigate("notifications") },
                    onNavigateToChat = { navController.navigate("chat_list") },
                    onNavigateToAiTutor = { navController.navigate("ai_tutor") },
                    onNavigateToForum = { navController.navigate("forum") }
                )
            }

            // ==========================================
            // Catalog Screen (CourseViewModel)
            // ==========================================
            composable(
                route = "catalog?trackId={trackId}",
                arguments = listOf(navArgument("trackId") {
                    type = NavType.StringType
                    nullable = true
                    defaultValue = null
                })
            ) { backStackEntry ->
                val vm: CourseViewModel = viewModel()
                val trackId = backStackEntry.arguments?.getString("trackId")
                val tracks by vm.tracks.collectAsStateWithLifecycle()
                val courses by vm.courses.collectAsStateWithLifecycle()
                val userProgressList by vm.userProgress.collectAsStateWithLifecycle()

                CatalogScreen(
                    tracks = tracks,
                    courses = courses,
                    initialTrackId = trackId,
                    userProgressList = userProgressList,
                    onNavigateToCourse = { courseId -> navController.navigate("course_detail/$courseId") }
                )
            }

            // ==========================================
            // Playground Screen (PlaygroundViewModel)
            // ==========================================
            composable(
                route = "playground/{courseId}/{moduleId}",
                arguments = listOf(
                    navArgument("courseId") { type = NavType.StringType },
                    navArgument("moduleId") { type = NavType.StringType }
                )
            ) { backStackEntry ->
                val vm: PlaygroundViewModel = viewModel()
                val courseId = backStackEntry.arguments?.getString("courseId") ?: ""
                val moduleId = backStackEntry.arguments?.getString("moduleId") ?: ""
                PlaygroundScreen(
                    courseId = courseId,
                    moduleId = moduleId,
                    viewModel = vm,
                    onNavigateBack = { navController.popBackStack() }
                )
            }

            // ==========================================
            // Forum Screen (ForumViewModel)
            // ==========================================
            composable(
                route = "forum?courseId={courseId}",
                arguments = listOf(navArgument("courseId") {
                    type = NavType.StringType
                    nullable = true
                    defaultValue = null
                })
            ) { backStackEntry ->
                val vm: ForumViewModel = viewModel()
                val courseId = backStackEntry.arguments?.getString("courseId")
                val threads by vm.forumThreads.collectAsStateWithLifecycle()
                val courses by vm.courses.collectAsStateWithLifecycle()

                ForumScreen(
                    threads = threads,
                    courses = courses,
                    initialCourseFilter = courseId,
                    onNavigateToThread = { threadId -> navController.navigate("forum_thread/$threadId") },
                    onToggleUpvote = { thread -> vm.toggleUpvoteThread(thread) },
                    onCreateThread = { cId, cTitle, title, body ->
                        vm.createForumThread(cId, cTitle, title, body)
                    }
                )
            }

            // ==========================================
            // Profile Screen (ProfileViewModel)
            // ==========================================
            composable("profile") {
                val vm: ProfileViewModel = viewModel()
                val userProfile by vm.profile.collectAsStateWithLifecycle()
                val badges by vm.allBadges.collectAsStateWithLifecycle()
                val userBadges by vm.userBadges.collectAsStateWithLifecycle()
                val certificates by vm.certificates.collectAsStateWithLifecycle()
                val bookmarks by vm.bookmarks.collectAsStateWithLifecycle()
                val playgroundDrafts by vm.playgroundDrafts.collectAsStateWithLifecycle()
                val courses by vm.courses.collectAsStateWithLifecycle()

                ProfileScreen(
                    profile = userProfile,
                    badges = badges,
                    userBadges = userBadges,
                    certificates = certificates,
                    bookmarks = bookmarks,
                    courses = courses,
                    playgroundDrafts = playgroundDrafts,
                    onViewCertificate = { certId -> navController.navigate("certificate_detail/$certId") },
                    onNavigateToCourse = { courseId -> navController.navigate("course_detail/$courseId") },
                    onOpenPlaygroundDraft = { cId, mId ->
                        navController.navigate("playground/$cId/$mId")
                    },
                    onLogout = {
                        vm.signOut {
                            navController.navigate("login") {
                                popUpTo(0) { inclusive = true }
                            }
                        }
                    },
                    onUpdateProfile = { name, bio -> vm.updateProfile(name, bio) }
                )
            }

            // ==========================================
            // Course Detail (CourseViewModel)
            // ==========================================
            composable(
                route = "course_detail/{courseId}",
                arguments = listOf(navArgument("courseId") { type = NavType.StringType })
            ) { backStackEntry ->
                val vm: CourseViewModel = viewModel()
                val courseId = backStackEntry.arguments?.getString("courseId") ?: ""
                val courses by vm.courses.collectAsStateWithLifecycle()
                val course = courses.find { it.id == courseId }
                val modules by vm.getModulesForCourse(courseId).collectAsStateWithLifecycle(emptyList())
                val userProgressList by vm.userProgress.collectAsStateWithLifecycle()
                val bookmarks by vm.bookmarks.collectAsStateWithLifecycle()
                val isBookmarked = bookmarks.any { it.itemId == courseId }

                CourseDetailScreen(
                    course = course,
                    modules = modules,
                    userProgressList = userProgressList,
                    isBookmarked = isBookmarked,
                    onToggleBookmark = { vm.toggleBookmark(courseId) },
                    onNavigateBack = { navController.popBackStack() },
                    onNavigateToModule = { moduleId -> navController.navigate("module_lesson/$courseId/$moduleId") },
                    onNavigateToQuiz = { cId -> navController.navigate("quiz/$cId") },
                    onNavigateToForumForCourse = { cId -> navController.navigate("forum?courseId=$cId") }
                )
            }

            // ==========================================
            // Module Lesson Screen (CourseViewModel)
            // ==========================================
            composable(
                route = "module_lesson/{courseId}/{moduleId}",
                arguments = listOf(
                    navArgument("courseId") { type = NavType.StringType },
                    navArgument("moduleId") { type = NavType.StringType }
                )
            ) { backStackEntry ->
                val vm: CourseViewModel = viewModel()
                val courseId = backStackEntry.arguments?.getString("courseId") ?: ""
                val moduleId = backStackEntry.arguments?.getString("moduleId") ?: ""
                val module by vm.getModuleById(moduleId).collectAsStateWithLifecycle(null)
                val userProgressList by vm.userProgress.collectAsStateWithLifecycle()
                val isCompleted = userProgressList.any { it.moduleId == moduleId && it.isCompleted }

                val allModules by vm.getModulesForCourse(courseId).collectAsStateWithLifecycle(emptyList())
                val currentOrder = module?.order ?: 1
                val nextModule = allModules.find { it.order == currentOrder + 1 }
                val isLastModule = nextModule == null

                val levelUpEvent by vm.levelUpEvent.collectAsStateWithLifecycle()
                if (levelUpEvent != null) {
                    LevelUpDialog(
                        newLevel = levelUpEvent!!,
                        onDismiss = { vm.dismissLevelUpDialog() }
                    )
                }

                ModuleLessonScreen(
                    module = module,
                    isCompleted = isCompleted,
                    isLastModule = isLastModule,
                    onNavigateBack = { navController.popBackStack() },
                    onMarkCompleted = {
                        vm.markModuleCompleted(courseId, moduleId)
                    },
                    onNextModule = {
                        if (nextModule != null) {
                            navController.navigate("module_lesson/$courseId/${nextModule.id}") {
                                popUpTo("module_lesson/$courseId/$moduleId") { inclusive = true }
                            }
                        } else {
                            navController.navigate("quiz/$courseId") {
                                popUpTo("module_lesson/$courseId/$moduleId") { inclusive = true }
                            }
                        }
                    },
                    onOpenPlayground = { cId, mId ->
                        navController.navigate("playground/$cId/$mId")
                    },
                    onOpenAr = { modId ->
                        navController.navigate("ar_visualizer/$modId")
                    },
                    onOpenAiTutorWithContext = { snippet ->
                        if (snippet.isNotBlank()) {
                            navController.navigate("ai_tutor?contextCode=${java.net.URLEncoder.encode(snippet, "UTF-8")}")
                        } else {
                            navController.navigate("ai_tutor")
                        }
                    }
                )
            }

            // ==========================================
            // Quiz Screen (QuizViewModel)
            // ==========================================
            composable(
                route = "quiz/{courseId}",
                arguments = listOf(navArgument("courseId") { type = NavType.StringType })
            ) { backStackEntry ->
                val vm: QuizViewModel = viewModel()
                val courseId = backStackEntry.arguments?.getString("courseId") ?: ""
                var course by remember { mutableStateOf<com.example.data.model.CourseEntity?>(null) }
                var questions by remember { mutableStateOf<List<QuizQuestionEntity>>(emptyList()) }
                var quizResult by remember { mutableStateOf<Triple<Int, Boolean, CertificateEntity?>?>(null) }
                var submittedAnswers by remember { mutableStateOf<Map<Int, Int>>(emptyMap()) }

                LaunchedEffect(courseId) {
                    course = vm.getCourse(courseId)
                    questions = vm.getQuizQuestions(courseId)
                }

                if (quizResult != null) {
                    val (score, isPassed, cert) = quizResult!!
                    QuizResultScreen(
                        scorePercent = score,
                        isPassed = isPassed,
                        questions = questions,
                        userAnswers = submittedAnswers,
                        certificate = cert,
                        onRetakeQuiz = {
                            quizResult = null
                            submittedAnswers = emptyMap()
                        },
                        onViewCertificate = { certId ->
                            navController.navigate("certificate_detail/$certId") {
                                popUpTo("quiz/$courseId") { inclusive = true }
                            }
                        },
                        onNavigateHome = {
                            navController.navigate("home") {
                                popUpTo("home") { inclusive = true }
                            }
                        }
                    )
                } else {
                    QuizScreen(
                        courseTitle = course?.title ?: "Kuis Evaluasi",
                        questions = questions,
                        onNavigateBack = { navController.popBackStack() },
                        onSubmitQuiz = { answers ->
                            submittedAnswers = answers
                            coroutineScope.launch {
                                quizResult = vm.submitQuiz(courseId, answers)
                            }
                        }
                    )
                }
            }

            // ==========================================
            // Certificate Detail (ProfileViewModel)
            // ==========================================
            composable(
                route = "certificate_detail/{certId}",
                arguments = listOf(navArgument("certId") { type = NavType.StringType })
            ) { backStackEntry ->
                val vm: ProfileViewModel = viewModel()
                val certId = backStackEntry.arguments?.getString("certId") ?: ""
                val certificates by vm.certificates.collectAsStateWithLifecycle()
                val cert = certificates.find { it.certId == certId }

                CertificateDetailScreen(
                    certificate = cert,
                    onNavigateBack = { navController.popBackStack() }
                )
            }

            // ==========================================
            // AI Tutor (AiTutorViewModel)
            // ==========================================
            composable(
                route = "ai_tutor?contextCode={contextCode}",
                arguments = listOf(navArgument("contextCode") {
                    type = NavType.StringType
                    nullable = true
                    defaultValue = null
                })
            ) { backStackEntry ->
                val vm: AiTutorViewModel = viewModel()
                val contextCode = backStackEntry.arguments?.getString("contextCode")
                val aiMessages by vm.aiMessages.collectAsStateWithLifecycle()
                val isAiGenerating by vm.isAiGenerating.collectAsStateWithLifecycle()

                AiTutorScreen(
                    messages = aiMessages,
                    isGenerating = isAiGenerating,
                    initialContextCode = contextCode,
                    onNavigateBack = { navController.popBackStack() },
                    onSendMessage = { msg, code -> vm.sendAiMessage(msg, code) }
                )
            }

            // ==========================================
            // Forum Thread Detail (ForumViewModel)
            // ==========================================
            composable(
                route = "forum_thread/{threadId}",
                arguments = listOf(navArgument("threadId") { type = NavType.StringType })
            ) { backStackEntry ->
                val vm: ForumViewModel = viewModel()
                val threadId = backStackEntry.arguments?.getString("threadId") ?: ""
                val thread by vm.getForumThreadById(threadId).collectAsStateWithLifecycle(null)
                val replies by vm.getForumReplies(threadId).collectAsStateWithLifecycle(emptyList())

                ThreadDetailScreen(
                    thread = thread,
                    replies = replies,
                    onNavigateBack = { navController.popBackStack() },
                    onToggleUpvote = { thread?.let { vm.toggleUpvoteThread(it) } },
                    onAddReply = { body -> vm.addForumReply(threadId, body) }
                )
            }

            // ==========================================
            // Notifications (HomeViewModel)
            // ==========================================
            composable("notifications") {
                val vm: HomeViewModel = viewModel()
                val notifications by vm.notifications.collectAsStateWithLifecycle()

                NotificationsScreen(
                    notifications = notifications,
                    onMarkAllAsRead = { vm.markAllNotificationsRead() },
                    onNotificationClick = { notif ->
                        if (!notif.targetRoute.isNullOrBlank()) {
                            navController.navigate(notif.targetRoute)
                        }
                    },
                    onNavigateBack = { navController.popBackStack() }
                )
            }

            // ==========================================
            // Friends & Social (SocialViewModel)
            // ==========================================
            composable("friends") {
                val vm: SocialViewModel = viewModel()
                val friendships by vm.friendships.collectAsStateWithLifecycle()

                FriendsScreen(
                    friends = friendships,
                    onAcceptFriend = { rId, aId -> vm.acceptFriendRequest(rId, aId) },
                    onAddFriend = { name -> vm.sendFriendRequest(name) },
                    onOpenChat = { convId -> navController.navigate("chat_room/$convId") },
                    onNavigateBack = { navController.popBackStack() }
                )
            }

            // ==========================================
            // Chat List & Room (ChatViewModel)
            // ==========================================
            composable("chat_list") {
                val vm: ChatViewModel = viewModel()
                val chatConversations by vm.chatConversations.collectAsStateWithLifecycle()

                ChatListScreen(
                    conversations = chatConversations,
                    onOpenConversation = { convId -> navController.navigate("chat_room/$convId") },
                    onNavigateBack = { navController.popBackStack() }
                )
            }

            composable(
                route = "chat_room/{convId}",
                arguments = listOf(navArgument("convId") { type = NavType.StringType })
            ) { backStackEntry ->
                val vm: ChatViewModel = viewModel()
                val convId = backStackEntry.arguments?.getString("convId") ?: ""
                val chatConversations by vm.chatConversations.collectAsStateWithLifecycle()
                val conversation = chatConversations.find { it.id == convId }
                val messages by vm.getChatMessages(convId).collectAsStateWithLifecycle(emptyList())
                val currentUserId by vm.currentUserId.collectAsStateWithLifecycle()

                ChatRoomScreen(
                    conversation = conversation,
                    messages = messages,
                    currentUserId = currentUserId,
                    onNavigateBack = { navController.popBackStack() },
                    onSendMessage = { body -> vm.sendChatMessage(convId, body) }
                )
            }

            // ==========================================
            // AR Visualizer (CourseViewModel)
            // ==========================================
            composable(
                route = "ar_visualizer/{moduleId}",
                arguments = listOf(navArgument("moduleId") { type = NavType.StringType })
            ) { backStackEntry ->
                val vm: CourseViewModel = viewModel()
                val moduleId = backStackEntry.arguments?.getString("moduleId") ?: ""
                var arAsset by remember { mutableStateOf<com.example.data.model.ArAssetEntity?>(null) }
                LaunchedEffect(moduleId) {
                    arAsset = vm.getArAssetForModule(moduleId)
                }

                ArVisualizerScreen(
                    arAsset = arAsset,
                    onNavigateBack = { navController.popBackStack() }
                )
            }
        }
    }
}
