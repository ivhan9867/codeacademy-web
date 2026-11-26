package com.example.ui.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.Reply
import androidx.compose.material.icons.automirrored.filled.Send
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.CloudOff
import androidx.compose.material.icons.filled.DeleteOutline
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.Forum
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.rounded.AutoAwesome
import androidx.compose.material.icons.rounded.ChatBubbleOutline
import androidx.compose.material.icons.rounded.ExpandLess
import androidx.compose.material.icons.rounded.ExpandMore
import androidx.compose.material.icons.rounded.School
import androidx.compose.ui.graphics.Brush
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material.icons.filled.WifiOff
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import com.example.R
import androidx.compose.runtime.mutableIntStateOf
import kotlinx.coroutines.delay
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.Immutable
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
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.data.local.ForumPostEntity
import com.example.data.models.Course
import com.example.ui.MainViewModel
import com.example.ui.viewmodel.SocialViewModel
import com.example.ui.components.CoachMarkBubble
import com.example.ui.theme.AmberGold
import com.example.ui.theme.CyanAccent
import com.example.ui.theme.SuccessGreen
import com.example.ui.theme.TealDark
import com.example.ui.theme.TealLight
import com.example.ui.theme.TealPrimary
import com.google.firebase.firestore.DocumentSnapshot
import com.example.util.localization.AppLanguage
import com.example.util.localization.toLocale
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

private val ForumCardShape = RoundedCornerShape(14.dp)
private val ForumHeaderCardShape = RoundedCornerShape(14.dp)
private val ForumBadgeShape = RoundedCornerShape(4.dp)
private val ForumQuoteBarShape = RoundedCornerShape(1.5.dp)
private val ForumHeaderIconShape = RoundedCornerShape(10.dp)
private val ForumInputBoxShape = RoundedCornerShape(20.dp)

private val SayaBadgeBg = TealLight.copy(alpha = 0.2f)
private val MentorBadgeBg = AmberGold.copy(alpha = 0.18f)
private val AvatarOtherBg = CyanAccent.copy(alpha = 0.7f)
private val LikeActiveRed = Color(0xFFFF5252)

@Composable
private fun levelTierLabel(level: Int): String = com.example.util.localization.DataLabelMappings.levelTierLabel(level)

@Immutable
data class ForumThreadGroup(
    val parent: ForumPostEntity,
    val replies: List<ForumPostEntity>
)

private fun resolveRootPostId(
    post: ForumPostEntity,
    postById: Map<String, ForumPostEntity>
): String {
    var current = post
    val visited = mutableSetOf<String>()
    while (current.replyToPostId != null && visited.add(current.postId)) {
        val parent = postById[current.replyToPostId] ?: break
        current = parent
    }
    return current.postId
}

private fun buildForumThreadGroups(posts: List<ForumPostEntity>): List<ForumThreadGroup> {
    val postById = posts.associateBy { it.postId }
    val topLevel = posts.filter { it.replyToPostId == null }
    val topLevelIds = topLevel.map { it.postId }.toSet()

    val repliesByRoot = mutableMapOf<String, MutableList<ForumPostEntity>>()
    val orphanReplies = mutableListOf<ForumPostEntity>()

    posts.filter { it.replyToPostId != null }.forEach { reply ->
        val rootId = resolveRootPostId(reply, postById)
        if (rootId in topLevelIds) {
            repliesByRoot.getOrPut(rootId) { mutableListOf() }.add(reply)
        } else {
            orphanReplies.add(reply)
        }
    }

    val groups = topLevel.map { parent ->
        ForumThreadGroup(
            parent = parent,
            replies = repliesByRoot[parent.postId].orEmpty()
                .sortedWith(compareByDescending<ForumPostEntity> { it.isVerifiedAnswer }.thenBy { it.timestamp })
        )
    }.toMutableList()

    orphanReplies.sortedByDescending { it.timestamp }.forEach { orphan ->
        groups.add(ForumThreadGroup(parent = orphan, replies = emptyList()))
    }
    return groups
}

private enum class ForumFilterMode {
    ALL, FACILITATOR, MINE, POPULAR
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CourseForumScreen(
    course: Course,
    viewModel: MainViewModel,
    socialViewModel: SocialViewModel = viewModel(),
    onBack: () -> Unit,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val coroutineScope = rememberCoroutineScope()
    val postsFlow = remember(course.id) { socialViewModel.getCourseForumPosts(course.id) }
    val posts by postsFlow.collectAsStateWithLifecycle(initialValue = emptyList())
    val postById = remember(posts) { posts.associateBy { it.postId } }
    val threadGroups = remember(posts) { buildForumThreadGroups(posts) }
    var expandedThreadIds by remember { mutableStateOf<Set<String>>(emptySet()) }
    var selectedFilter by remember { mutableStateOf(ForumFilterMode.ALL) }
    var postToDelete by remember { mutableStateOf<ForumPostEntity?>(null) }
    val userProfile by viewModel.userProfile.collectAsStateWithLifecycle()
    val currentUserId = remember(userProfile?.id) {
        socialViewModel.getCurrentForumUserId(userProfile?.id)
    }
    val filteredThreadGroups = remember(threadGroups, selectedFilter, currentUserId) {
        when (selectedFilter) {
            ForumFilterMode.ALL -> threadGroups
            ForumFilterMode.FACILITATOR -> threadGroups.filter { grp ->
                grp.parent.authorIsMentor || grp.replies.any { it.authorIsMentor }
            }
            ForumFilterMode.MINE -> threadGroups.filter { grp ->
                grp.parent.authorId == currentUserId || grp.replies.any { it.authorId == currentUserId }
            }
            ForumFilterMode.POPULAR -> threadGroups.sortedByDescending { it.parent.likeCount + it.replies.size }
        }
    }
    val selectedPublicProfile by socialViewModel.selectedPublicProfile.collectAsStateWithLifecycle()
    val isLoadingPublicProfile by socialViewModel.isLoadingPublicProfile.collectAsStateWithLifecycle()

    postToDelete?.let { targetDelete ->
        AlertDialog(
            onDismissRequest = { postToDelete = null },
            title = { Text(stringResource(R.string.forum_delete_title), fontWeight = FontWeight.Bold) },
            text = { Text(stringResource(R.string.forum_delete_desc)) },
            confirmButton = {
                Button(
                    onClick = {
                        socialViewModel.deleteForumPost(targetDelete.postId)
                        postToDelete = null
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.error)
                ) {
                    Text(stringResource(R.string.forum_delete_confirm), color = Color.White)
                }
            },
            dismissButton = {
                TextButton(onClick = { postToDelete = null }) {
                    Text(stringResource(R.string.common_cancel))
                }
            }
        )
    }

    selectedPublicProfile?.let { publicProfile ->
        com.example.ui.components.UserPublicProfileDialog(
            profile = publicProfile,
            isLoading = isLoadingPublicProfile,
            onDismiss = { socialViewModel.dismissPublicProfile() }
        )
    }

    var loadError by remember { mutableStateOf<String?>(null) }
    var loadErrorKind by remember { mutableStateOf<com.example.util.NetworkErrorKind?>(null) }
    var retryTrigger by remember { mutableIntStateOf(0) }

    var postInput by remember { mutableStateOf("") }
    var isPosting by remember { mutableStateOf(false) }
    var isRefreshing by remember { mutableStateOf(false) }
    var lastDocSnapshot by remember { mutableStateOf<DocumentSnapshot?>(null) }
    var canLoadMore by remember { mutableStateOf(true) }
    var isLoadingMore by remember { mutableStateOf(false) }

    var replyingToPost by remember { mutableStateOf<ForumPostEntity?>(null) }
    var highlightedPostId by remember { mutableStateOf<String?>(null) }

    val listState = rememberLazyListState()

    var authReady by remember {
        mutableStateOf(com.google.firebase.auth.FirebaseAuth.getInstance().currentUser != null)
    }

    LaunchedEffect(retryTrigger) {
        if (com.google.firebase.auth.FirebaseAuth.getInstance().currentUser == null) {
            authReady = socialViewModel.ensureCloudAuth()
        } else {
            authReady = true
        }
    }

    // 1. Strict Lifecycle-managed Firestore Real-Time Snapshot Listener inside DisposableEffect
    // CRITICAL: Listener MUST be removed in onDispose to prevent memory leaks and background consumption!
    DisposableEffect(course.id, retryTrigger, authReady) {
        if (!authReady) {
            return@DisposableEffect onDispose {}
        }
        val listenerRegistration = socialViewModel.attachForumListener(
            courseId = course.id,
            onError = { exception ->
                val kind = com.example.util.classifyFirestoreError(exception)
                loadErrorKind = kind
                loadError = context.getString(com.example.util.mapErrorKindToUi(kind).messageRes)
            },
            onUpdate = { updatedPosts ->
                loadError = null
                loadErrorKind = null
                socialViewModel.syncIncomingForumPosts(course.id, updatedPosts)
            }
        )

        onDispose {
            listenerRegistration?.remove()
        }
    }

    // 2. Initial Fetch from Firestore
    LaunchedEffect(course.id, retryTrigger) {
        isRefreshing = true
        socialViewModel.refreshCourseForum(course.id) { lastDoc ->
            lastDocSnapshot = lastDoc
            isRefreshing = false
        }
    }

    Scaffold(
        contentWindowInsets = androidx.compose.foundation.layout.WindowInsets(0.dp),
        topBar = {
            TopAppBar(
                windowInsets = androidx.compose.foundation.layout.WindowInsets(0.dp),
                title = {
                    Column {
                        Text(
                            text = androidx.compose.ui.res.stringResource(com.example.R.string.forum_title),
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            text = course.title,
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )
                    }
                },
                navigationIcon = {
                    IconButton(
                        onClick = onBack,
                        modifier = Modifier.testTag("forum_back_button")
                    ) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = androidx.compose.ui.res.stringResource(com.example.R.string.common_back)
                        )
                    }
                },
                actions = {
                    IconButton(
                        onClick = {
                            isRefreshing = true
                            socialViewModel.refreshCourseForum(course.id) { doc ->
                                lastDocSnapshot = doc
                                isRefreshing = false
                            }
                        },
                        enabled = !isRefreshing,
                        modifier = Modifier.testTag("forum_refresh_button")
                    ) {
                        if (isRefreshing) {
                            CircularProgressIndicator(
                                modifier = Modifier.size(20.dp),
                                strokeWidth = 2.dp,
                                color = MaterialTheme.colorScheme.primary
                            )
                        } else {
                            Icon(
                                imageVector = Icons.Default.Refresh,
                                contentDescription = androidx.compose.ui.res.stringResource(com.example.R.string.forum_refresh)
                            )
                        }
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.surface
                )
            )
        },
        bottomBar = {
            // Input Bar at bottom
            Surface(
                tonalElevation = 8.dp,
                shadowElevation = 8.dp,
                color = MaterialTheme.colorScheme.surface,
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .imePadding()
                ) {
                    replyingToPost?.let { replyTarget ->
                        val snippet = if (replyTarget.content.length > 60) replyTarget.content.take(57) + "..." else replyTarget.content
                        Surface(
                            color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.8f),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(horizontal = 16.dp, vertical = 6.dp)
                            ) {
                                Box(
                                    modifier = Modifier
                                        .width(3.dp)
                                        .height(30.dp)
                                        .background(MaterialTheme.colorScheme.primary, ForumQuoteBarShape)
                                )
                                Spacer(modifier = Modifier.width(8.dp))
                                Column(modifier = Modifier.weight(1f)) {
                                    Text(
                                        text = androidx.compose.ui.res.stringResource(com.example.R.string.forum_replying_to_format, replyTarget.authorName),
                                        style = MaterialTheme.typography.labelSmall,
                                        fontWeight = FontWeight.Bold,
                                        color = MaterialTheme.colorScheme.primary
                                    )
                                    Text(
                                        text = snippet,
                                        style = MaterialTheme.typography.bodySmall,
                                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                                        maxLines = 1,
                                        overflow = TextOverflow.Ellipsis
                                    )
                                }
                                IconButton(
                                    onClick = { replyingToPost = null },
                                    modifier = Modifier
                                        .size(28.dp)
                                        .testTag("cancel_reply_button")
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.Close,
                                        contentDescription = androidx.compose.ui.res.stringResource(com.example.R.string.forum_cancel_reply),
                                        tint = MaterialTheme.colorScheme.onSurfaceVariant,
                                        modifier = Modifier.size(16.dp)
                                    )
                                }
                            }
                        }
                    }

                    val isForumBlockedByError = loadError != null && posts.isEmpty()
                    if (isForumBlockedByError) {
                        Text(
                            text = androidx.compose.ui.res.stringResource(com.example.R.string.forum_input_disabled_error),
                            style = MaterialTheme.typography.labelSmall,
                            color = MaterialTheme.colorScheme.error,
                            modifier = Modifier.padding(start = 16.dp, end = 16.dp, top = 8.dp)
                        )
                    }

                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp, vertical = 10.dp)
                    ) {
                        OutlinedTextField(
                            value = postInput,
                            onValueChange = { if (it.length <= 4000) postInput = it },
                            enabled = !isForumBlockedByError,
                            supportingText = if (postInput.isNotEmpty()) {
                                {
                                    Text(
                                        text = "${postInput.length}/4000",
                                        style = MaterialTheme.typography.labelSmall,
                                        modifier = Modifier.fillMaxWidth(),
                                        textAlign = androidx.compose.ui.text.style.TextAlign.End
                                    )
                                }
                            } else null,
                            placeholder = {
                                Text(
                                    text = if (replyingToPost != null) {
                                        androidx.compose.ui.res.stringResource(com.example.R.string.forum_reply_placeholder)
                                    } else {
                                        androidx.compose.ui.res.stringResource(com.example.R.string.forum_post_placeholder)
                                    },
                                    style = MaterialTheme.typography.bodyMedium,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.7f)
                                )
                            },
                            maxLines = 4,
                            shape = ForumInputBoxShape,
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedBorderColor = MaterialTheme.colorScheme.primary,
                                unfocusedBorderColor = MaterialTheme.colorScheme.outline.copy(alpha = 0.3f),
                                focusedContainerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.4f),
                                unfocusedContainerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.2f)
                            ),
                            modifier = Modifier
                                .weight(1f)
                                .testTag("forum_post_input")
                        )

                        Spacer(modifier = Modifier.width(8.dp))

                        IconButton(
                            onClick = {
                                val content = postInput.trim()
                                if (content.isNotBlank() && !isPosting) {
                                    isPosting = true
                                    val replyTarget = replyingToPost
                                    val snippet = replyTarget?.let {
                                        if (it.content.length > 60) it.content.take(57) + "..." else it.content
                                    }
                                    socialViewModel.createForumPost(
                                        courseId = course.id,
                                        content = content,
                                        replyToPostId = replyTarget?.postId,
                                        replyToAuthorName = replyTarget?.authorName,
                                        replyToContentSnippet = snippet
                                    ) { success ->
                                        isPosting = false
                                        if (success) {
                                            postInput = ""
                                            replyingToPost = null
                                            if (replyTarget != null) {
                                                val rootId = resolveRootPostId(replyTarget, postById)
                                                expandedThreadIds = expandedThreadIds + rootId
                                            } else {
                                                coroutineScope.launch {
                                                    listState.animateScrollToItem(0)
                                                }
                                            }
                                        } else {
                                            android.widget.Toast.makeText(
                                                context,
                                                context.getString(com.example.R.string.forum_send_failed),
                                                android.widget.Toast.LENGTH_SHORT
                                            ).show()
                                        }
                                    }
                                }
                            },
                            enabled = postInput.isNotBlank() && !isPosting,
                            modifier = Modifier
                                .size(48.dp)
                                .clip(CircleShape)
                                .background(
                                    if (postInput.isNotBlank() && !isPosting) TealPrimary else MaterialTheme.colorScheme.surfaceVariant
                                )
                                .testTag("forum_send_button")
                        ) {
                            if (isPosting) {
                                CircularProgressIndicator(
                                    modifier = Modifier.size(20.dp),
                                    strokeWidth = 2.dp,
                                    color = Color.White
                                )
                            } else {
                                Icon(
                                    imageVector = Icons.AutoMirrored.Filled.Send,
                                    contentDescription = androidx.compose.ui.res.stringResource(com.example.R.string.forum_send_post),
                                    tint = if (postInput.isNotBlank()) Color.White else MaterialTheme.colorScheme.onSurfaceVariant
                                )
                            }
                        }
                    }
                }
            }
        },
        modifier = modifier.fillMaxSize()
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            if (userProfile != null && !viewModel.hasSeenCoachMark("forum_intro")) {
                CoachMarkBubble(
                    message = androidx.compose.ui.res.stringResource(com.example.R.string.forum_coachmark),
                    onDismiss = { viewModel.markCoachMarkSeen("forum_intro") },
                    modifier = Modifier.padding(horizontal = 16.dp, vertical = 6.dp)
                )
            }

            // Header Info Card
            Card(
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.35f)
                ),
                elevation = CardDefaults.cardElevation(defaultElevation = 0.dp),
                shape = ForumHeaderCardShape,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 8.dp)
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.padding(14.dp)
                ) {
                    Box(
                        contentAlignment = Alignment.Center,
                        modifier = Modifier
                            .size(40.dp)
                            .clip(ForumHeaderIconShape)
                            .background(MaterialTheme.colorScheme.primary.copy(alpha = 0.15f))
                    ) {
                        Icon(
                            imageVector = Icons.Default.Forum,
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.primary,
                            modifier = Modifier.size(22.dp)
                        )
                    }

                    Spacer(modifier = Modifier.width(12.dp))

                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = androidx.compose.ui.res.stringResource(com.example.R.string.forum_room_title),
                            style = MaterialTheme.typography.titleSmall,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.primary
                        )
                        Text(
                            text = androidx.compose.ui.res.stringResource(com.example.R.string.forum_room_subtitle),
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }
            }

            // Filter Chips Bar
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .horizontalScroll(rememberScrollState())
                    .padding(horizontal = 16.dp, vertical = 4.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                val forumChipColors = FilterChipDefaults.filterChipColors(
                    selectedContainerColor = TealPrimary,
                    selectedLabelColor = Color.White
                )
                FilterChip(
                    selected = selectedFilter == ForumFilterMode.ALL,
                    onClick = { selectedFilter = ForumFilterMode.ALL },
                    label = { Text(stringResource(R.string.forum_filter_all)) },
                    colors = forumChipColors
                )
                FilterChip(
                    selected = selectedFilter == ForumFilterMode.FACILITATOR,
                    onClick = { selectedFilter = ForumFilterMode.FACILITATOR },
                    label = { Text(stringResource(R.string.forum_filter_facilitator)) },
                    colors = forumChipColors
                )
                FilterChip(
                    selected = selectedFilter == ForumFilterMode.MINE,
                    onClick = { selectedFilter = ForumFilterMode.MINE },
                    label = { Text(stringResource(R.string.forum_filter_mine)) },
                    colors = forumChipColors
                )
                FilterChip(
                    selected = selectedFilter == ForumFilterMode.POPULAR,
                    onClick = { selectedFilter = ForumFilterMode.POPULAR },
                    label = { Text(stringResource(R.string.forum_filter_popular)) },
                    colors = forumChipColors
                )
            }

            if (loadError != null && posts.isEmpty()) {
                // Error state with retry
                ErrorRetryView(
                    message = loadError ?: androidx.compose.ui.res.stringResource(com.example.R.string.forum_load_error),
                    errorKind = loadErrorKind ?: com.example.util.NetworkErrorKind.OFFLINE,
                    onRetry = {
                        retryTrigger++
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f)
                        .padding(24.dp)
                )
            } else if (filteredThreadGroups.isEmpty()) {
                // Empty state (either no posts yet or filter yielded no match)
                Box(
                    contentAlignment = Alignment.Center,
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f)
                        .padding(24.dp)
                ) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center
                    ) {
                        com.example.ui.components.CodeAcademyMascot(
                            size = 86.dp,
                            mood = com.example.ui.components.MascotMood.WAVING
                        )
                        Spacer(modifier = Modifier.height(14.dp))
                        Text(
                            text = if (posts.isEmpty()) {
                                stringResource(R.string.forum_empty_title)
                            } else {
                                stringResource(R.string.forum_filter_empty)
                            },
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold
                        )
                        if (posts.isEmpty()) {
                            Spacer(modifier = Modifier.height(6.dp))
                            Text(
                                text = stringResource(R.string.forum_empty_desc),
                                style = MaterialTheme.typography.bodyMedium,
                                color = MaterialTheme.colorScheme.onSurfaceVariant,
                                textAlign = androidx.compose.ui.text.style.TextAlign.Center
                            )
                        }
                    }
                }
            } else {
                // LazyColumn with mandatory key = { it.post.postId }
                LazyColumn(
                    state = listState,
                    contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp),
                    verticalArrangement = Arrangement.spacedBy(10.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f)
                        .testTag("forum_list")
                ) {
                    items(
                        items = filteredThreadGroups,
                        key = { it.parent.postId }
                    ) { group ->
                        val post = group.parent
                        val isExpanded = post.postId in expandedThreadIds
                        ForumPostCard(
                            post = post,
                            replies = group.replies,
                            isThreadExpanded = isExpanded,
                            currentUserId = currentUserId,
                            isHighlighted = highlightedPostId == post.postId,
                            onToggleExpandReplies = {
                                expandedThreadIds = if (isExpanded) {
                                    expandedThreadIds - post.postId
                                } else {
                                    expandedThreadIds + post.postId
                                }
                            },
                            onReplyClick = {
                                replyingToPost = post
                            },
                            onReplyToPost = { targetPost ->
                                replyingToPost = targetPost
                            },
                            onQuoteClick = { targetPostId ->
                                val targetIndex = filteredThreadGroups.indexOfFirst { it.parent.postId == targetPostId }
                                if (targetIndex >= 0) {
                                    coroutineScope.launch {
                                        listState.animateScrollToItem(targetIndex)
                                        highlightedPostId = targetPostId
                                        delay(600L)
                                        highlightedPostId = null
                                    }
                                }
                            },
                            onToggleLike = {
                                socialViewModel.toggleLikeForumPost(
                                    postId = post.postId,
                                    currentLikes = post.likeCount,
                                    isLiked = !post.isLikedByMe
                                )
                            },
                            onToggleLikePost = { targetPost ->
                                socialViewModel.toggleLikeForumPost(
                                    postId = targetPost.postId,
                                    currentLikes = targetPost.likeCount,
                                    isLiked = !targetPost.isLikedByMe
                                )
                            },
                            onDeletePost = { targetPost ->
                                postToDelete = targetPost
                            },
                            onAuthorClick = { authorId ->
                                val target = if (post.authorId == authorId) post else group.replies.find { it.authorId == authorId }
                                socialViewModel.openPublicProfile(
                                    userId = authorId,
                                    fallbackName = target?.authorName ?: "Siswa",
                                    fallbackLevel = target?.authorLevel ?: 1,
                                    fallbackAccessRole = target?.authorAccessRole ?: "",
                                    fallbackIsMentor = target?.authorIsMentor ?: false
                                )
                            }
                        )
                    }

                    // Paginasi: Load more button / indicator
                    if (canLoadMore && lastDocSnapshot != null) {
                        item(key = "load_more_item") {
                            Box(
                                contentAlignment = Alignment.Center,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(vertical = 12.dp)
                            ) {
                                if (isLoadingMore) {
                                    CircularProgressIndicator(
                                        modifier = Modifier.size(24.dp),
                                        strokeWidth = 2.dp,
                                        color = MaterialTheme.colorScheme.primary
                                    )
                                } else {
                                    TextButton(
                                        onClick = {
                                            isLoadingMore = true
                                            socialViewModel.loadMoreForumPosts(course.id, lastDocSnapshot) { newDoc, count ->
                                                lastDocSnapshot = newDoc
                                                isLoadingMore = false
                                                if (count < 20 || newDoc == null) {
                                                    canLoadMore = false
                                                }
                                            }
                                        },
                                        modifier = Modifier.testTag("forum_load_more_button")
                                    ) {
                                        Text(
                                            text = stringResource(R.string.forum_load_more),
                                            style = MaterialTheme.typography.bodyMedium,
                                            color = MaterialTheme.colorScheme.primary,
                                            fontWeight = FontWeight.SemiBold
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
}

@Composable
fun ForumPostCard(
    post: ForumPostEntity,
    indentationDp: Dp = 0.dp,
    replies: List<ForumPostEntity> = emptyList(),
    isThreadExpanded: Boolean = false,
    currentUserId: String,
    isHighlighted: Boolean = false,
    onToggleExpandReplies: () -> Unit = {},
    onReplyClick: () -> Unit = {},
    onReplyToPost: ((ForumPostEntity) -> Unit)? = null,
    onQuoteClick: ((String) -> Unit)? = null,
    onToggleLike: () -> Unit,
    onToggleLikePost: ((ForumPostEntity) -> Unit)? = null,
    onDeletePost: ((ForumPostEntity) -> Unit)? = null,
    onAuthorClick: (String) -> Unit = {},
    modifier: Modifier = Modifier
) {
    val isAuthorMe = post.authorId == currentUserId
    val hasFacilitatorReply = remember(replies) { replies.any { it.authorIsMentor } }
    val timeCtx = LocalContext.current
    val appLang = com.example.util.localization.LocalAppLanguage.current
    val formattedTime = remember(post.timestamp, appLang) {
        formatTimestamp(post.timestamp, timeCtx, appLang)
    }

    val containerBgColor = if (isHighlighted) {
        MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.45f)
    } else {
        MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f)
    }

    Card(
        shape = ForumCardShape,
        colors = CardDefaults.cardColors(
            containerColor = containerBgColor
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp),
        modifier = modifier
            .fillMaxWidth()
            .padding(start = indentationDp)
            .testTag("forum_post_item_${post.postId}")
    ) {
        Column(modifier = Modifier.padding(14.dp)) {
            // Author info row (clickable to open public profile)
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(10.dp))
                    .clickable(enabled = post.authorId.isNotBlank()) {
                        onAuthorClick(post.authorId)
                    }
                    .padding(vertical = 2.dp)
            ) {
                // Avatar circle
                Box(
                    contentAlignment = Alignment.Center,
                    modifier = Modifier
                        .size(36.dp)
                        .clip(CircleShape)
                        .background(if (isAuthorMe) TealPrimary else AvatarOtherBg)
                ) {
                    val initial = post.authorName.firstOrNull()?.uppercaseChar()?.toString() ?: "S"
                    Text(
                        text = initial,
                        style = MaterialTheme.typography.titleSmall,
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )
                }

                Spacer(modifier = Modifier.width(10.dp))

                Column(modifier = Modifier.weight(1f)) {
                    @OptIn(ExperimentalLayoutApi::class)
                    FlowRow(
                        verticalArrangement = Arrangement.spacedBy(4.dp, Alignment.CenterVertically),
                        horizontalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        Text(
                            text = post.authorName,
                            style = MaterialTheme.typography.bodyMedium,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.onSurface,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )
                        ForumAuthorBadges(
                            isAuthorMe = isAuthorMe,
                            authorIsMentor = post.authorIsMentor,
                            authorAccessRole = post.authorAccessRole,
                            authorLevel = post.authorLevel
                        )
                    }
                    Text(
                        text = formattedTime,
                        style = MaterialTheme.typography.labelSmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }

                if (hasFacilitatorReply && !post.authorIsMentor) {
                    Surface(
                        shape = ForumBadgeShape,
                        color = TealPrimary.copy(alpha = 0.14f),
                        modifier = Modifier.padding(end = if (isAuthorMe && onDeletePost != null) 4.dp else 0.dp)
                    ) {
                        Text(
                            text = androidx.compose.ui.res.stringResource(com.example.R.string.forum_answered_by_facilitator),
                            style = MaterialTheme.typography.labelSmall,
                            fontWeight = FontWeight.SemiBold,
                            color = TealPrimary,
                            modifier = Modifier.padding(horizontal = 7.dp, vertical = 3.dp)
                        )
                    }
                }

                if (isAuthorMe && onDeletePost != null) {
                    IconButton(
                        onClick = { onDeletePost(post) },
                        modifier = Modifier
                            .size(32.dp)
                            .testTag("delete_post_${post.postId}")
                    ) {
                        Icon(
                            imageVector = Icons.Default.DeleteOutline,
                            contentDescription = androidx.compose.ui.res.stringResource(com.example.R.string.forum_delete_cd),
                            tint = MaterialTheme.colorScheme.error.copy(alpha = 0.85f),
                            modifier = Modifier.size(18.dp)
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            // Reply / Quote indicator box
            if (!post.replyToAuthorName.isNullOrBlank()) {
                ForumQuoteBox(
                    authorName = post.replyToAuthorName,
                    snippet = post.replyToContentSnippet ?: "",
                    onClick = if (post.replyToPostId != null) {
                        { onQuoteClick?.invoke(post.replyToPostId) }
                    } else null
                )
            }

            // Post content
            Text(
                text = post.content,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurface,
                lineHeight = 20.sp
            )

            Spacer(modifier = Modifier.height(10.dp))

            // Footer: Social-media style Comment count pill (left) + Balas & Like (right)
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier.fillMaxWidth()
            ) {
                if (replies.isNotEmpty()) {
                    Surface(
                        shape = RoundedCornerShape(20.dp),
                        color = if (isThreadExpanded) {
                            MaterialTheme.colorScheme.primary.copy(alpha = 0.14f)
                        } else {
                            MaterialTheme.colorScheme.surface.copy(alpha = 0.85f)
                        },
                        modifier = Modifier
                            .clip(RoundedCornerShape(20.dp))
                            .clickable(onClick = onToggleExpandReplies)
                            .testTag("toggle_replies_${post.postId}")
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.padding(horizontal = 10.dp, vertical = 5.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Rounded.ChatBubbleOutline,
                                contentDescription = androidx.compose.ui.res.stringResource(com.example.R.string.forum_show_replies_cd),
                                modifier = Modifier.size(15.dp),
                                tint = MaterialTheme.colorScheme.primary
                            )
                            Spacer(modifier = Modifier.width(5.dp))
                            Text(
                                text = androidx.compose.ui.res.stringResource(com.example.R.string.forum_replies_count_format, replies.size),
                                style = MaterialTheme.typography.labelSmall,
                                fontWeight = FontWeight.SemiBold,
                                color = MaterialTheme.colorScheme.primary
                            )
                            Spacer(modifier = Modifier.width(2.dp))
                            Icon(
                                imageVector = if (isThreadExpanded) Icons.Rounded.ExpandLess else Icons.Rounded.ExpandMore,
                                contentDescription = null,
                                modifier = Modifier.size(16.dp),
                                tint = MaterialTheme.colorScheme.primary
                            )
                        }
                    }
                } else {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.Rounded.ChatBubbleOutline,
                            contentDescription = null,
                            modifier = Modifier.size(14.dp),
                            tint = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.6f)
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            text = androidx.compose.ui.res.stringResource(com.example.R.string.forum_zero_replies),
                            style = MaterialTheme.typography.labelSmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.7f)
                        )
                    }
                }

                Row(verticalAlignment = Alignment.CenterVertically) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier
                            .clip(RoundedCornerShape(8.dp))
                            .clickable(onClick = onReplyClick)
                            .padding(horizontal = 8.dp, vertical = 6.dp)
                            .testTag("reply_button_${post.postId}")
                    ) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.Reply,
                            contentDescription = androidx.compose.ui.res.stringResource(com.example.R.string.forum_reply_btn),
                            modifier = Modifier.size(16.dp),
                            tint = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            text = androidx.compose.ui.res.stringResource(com.example.R.string.forum_reply_btn),
                            style = MaterialTheme.typography.labelSmall,
                            fontWeight = FontWeight.Medium,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }

                    Spacer(modifier = Modifier.width(4.dp))

                    IconButton(
                        onClick = onToggleLike,
                        modifier = Modifier
                            .size(34.dp)
                            .testTag("like_button_${post.postId}")
                    ) {
                        Icon(
                            imageVector = if (post.isLikedByMe) Icons.Default.Favorite else Icons.Default.FavoriteBorder,
                            contentDescription = if (post.isLikedByMe) {
                                androidx.compose.ui.res.stringResource(com.example.R.string.forum_unlike_cd)
                            } else {
                                androidx.compose.ui.res.stringResource(com.example.R.string.forum_like_cd)
                            },
                            tint = if (post.isLikedByMe) LikeActiveRed else MaterialTheme.colorScheme.onSurfaceVariant,
                            modifier = Modifier.size(18.dp)
                        )
                    }
                    Text(
                        text = post.likeCount.toString(),
                        style = MaterialTheme.typography.labelMedium,
                        fontWeight = FontWeight.SemiBold,
                        color = if (post.isLikedByMe) LikeActiveRed else MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }

            // Expandable Social-Media-Style Replies Drawer
            AnimatedVisibility(visible = isThreadExpanded && replies.isNotEmpty()) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 10.dp)
                ) {
                    HorizontalDivider(
                        color = MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.45f),
                        modifier = Modifier.padding(bottom = 10.dp)
                    )
                    Column(
                        verticalArrangement = Arrangement.spacedBy(8.dp),
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(start = 6.dp)
                    ) {
                        replies.forEach { reply ->
                            ForumReplyItem(
                                reply = reply,
                                currentUserId = currentUserId,
                                onReplyClick = { onReplyToPost?.invoke(reply) },
                                onToggleLike = { onToggleLikePost?.invoke(reply) },
                                onDeleteClick = if (reply.authorId == currentUserId && onDeletePost != null) {
                                    { onDeletePost(reply) }
                                } else null,
                                onAuthorClick = onAuthorClick
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun ForumReplyItem(
    reply: ForumPostEntity,
    currentUserId: String,
    onReplyClick: () -> Unit,
    onToggleLike: () -> Unit,
    onDeleteClick: (() -> Unit)? = null,
    onAuthorClick: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    val isAuthorMe = reply.authorId == currentUserId
    val replyTimeCtx = LocalContext.current
    val replyAppLang = com.example.util.localization.LocalAppLanguage.current
    val formattedTime = remember(reply.timestamp, replyAppLang) { formatTimestamp(reply.timestamp, replyTimeCtx, replyAppLang) }

    Row(
        modifier = modifier
            .fillMaxWidth()
            .height(IntrinsicSize.Min)
            .testTag("forum_reply_item_${reply.postId}")
    ) {
        // Vertical thread line matching exact reply height
        Box(
            modifier = Modifier
                .width(2.5.dp)
                .fillMaxHeight()
                .padding(vertical = 4.dp)
                .background(
                    color = if (reply.authorIsMentor) AmberGold.copy(alpha = 0.75f) else TealPrimary.copy(alpha = 0.45f),
                    shape = ForumQuoteBarShape
                )
        )
        Spacer(modifier = Modifier.width(10.dp))
        Surface(
            shape = RoundedCornerShape(12.dp),
            color = when {
                reply.isVerifiedAnswer -> SuccessGreen.copy(alpha = 0.08f)
                reply.authorIsMentor -> AmberGold.copy(alpha = 0.10f)
                else -> MaterialTheme.colorScheme.surface
            },
            border = when {
                reply.isVerifiedAnswer -> BorderStroke(1.dp, SuccessGreen.copy(alpha = 0.45f))
                reply.authorIsMentor -> BorderStroke(1.dp, AmberGold.copy(alpha = 0.35f))
                else -> BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.45f))
            },
            modifier = Modifier.weight(1f)
        ) {
            Column(modifier = Modifier.padding(10.dp)) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(8.dp))
                        .clickable(enabled = reply.authorId.isNotBlank()) {
                            onAuthorClick(reply.authorId)
                        }
                ) {
                    Box(
                        contentAlignment = Alignment.Center,
                        modifier = Modifier
                            .size(26.dp)
                            .clip(CircleShape)
                            .background(if (isAuthorMe) TealPrimary else AvatarOtherBg)
                    ) {
                        val initial = reply.authorName.firstOrNull()?.uppercaseChar()?.toString() ?: "S"
                        Text(
                            text = initial,
                            style = MaterialTheme.typography.labelSmall,
                            fontWeight = FontWeight.Bold,
                            color = Color.White
                        )
                    }
                    Spacer(modifier = Modifier.width(8.dp))
                    Column(modifier = Modifier.weight(1f)) {
                        @OptIn(ExperimentalLayoutApi::class)
                        FlowRow(
                            verticalArrangement = Arrangement.spacedBy(4.dp, Alignment.CenterVertically),
                            horizontalArrangement = Arrangement.spacedBy(6.dp)
                        ) {
                            Text(
                                text = reply.authorName,
                                style = MaterialTheme.typography.labelMedium,
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.onSurface,
                                maxLines = 1,
                                overflow = TextOverflow.Ellipsis
                            )
                            ForumAuthorBadges(
                                isAuthorMe = isAuthorMe,
                                authorIsMentor = reply.authorIsMentor,
                                authorAccessRole = reply.authorAccessRole,
                                authorLevel = reply.authorLevel
                            )
                        }
                        Text(
                            text = formattedTime,
                            style = MaterialTheme.typography.labelSmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                    if (isAuthorMe && onDeleteClick != null) {
                        IconButton(
                            onClick = onDeleteClick,
                            modifier = Modifier.size(28.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.DeleteOutline,
                                contentDescription = androidx.compose.ui.res.stringResource(com.example.R.string.forum_delete_cd),
                                tint = MaterialTheme.colorScheme.error.copy(alpha = 0.85f),
                                modifier = Modifier.size(16.dp)
                            )
                        }
                    }
                }

                if (!reply.replyToAuthorName.isNullOrBlank()) {
                    Spacer(modifier = Modifier.height(6.dp))
                    if (!reply.replyToContentSnippet.isNullOrBlank()) {
                        ForumQuoteBox(
                            authorName = reply.replyToAuthorName,
                            snippet = reply.replyToContentSnippet,
                            onClick = null
                        )
                    } else {
                        Text(
                            text = androidx.compose.ui.res.stringResource(com.example.R.string.forum_replying_at_format, reply.replyToAuthorName),
                            style = MaterialTheme.typography.labelSmall,
                            fontWeight = FontWeight.SemiBold,
                            color = MaterialTheme.colorScheme.primary
                        )
                    }
                }

                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = reply.content,
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurface,
                    lineHeight = 18.sp
                )

                if (reply.isVerifiedAnswer || reply.isHelpful) {
                    @OptIn(ExperimentalLayoutApi::class)
                    FlowRow(
                        horizontalArrangement = Arrangement.spacedBy(6.dp),
                        verticalArrangement = Arrangement.spacedBy(4.dp),
                        modifier = Modifier.padding(top = 6.dp)
                    ) {
                        if (reply.isVerifiedAnswer) {
                            Surface(
                                shape = ForumBadgeShape,
                                color = SuccessGreen.copy(alpha = 0.15f)
                            ) {
                                Text(
                                    text = androidx.compose.ui.res.stringResource(com.example.R.string.forum_badge_verified_answer),
                                    style = MaterialTheme.typography.labelSmall,
                                    fontWeight = FontWeight.Bold,
                                    color = SuccessGreen,
                                    modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                                )
                            }
                        }
                        if (reply.isHelpful) {
                            Surface(
                                shape = ForumBadgeShape,
                                color = AmberGold.copy(alpha = 0.16f)
                            ) {
                                Text(
                                    text = androidx.compose.ui.res.stringResource(com.example.R.string.forum_badge_helpful_answer),
                                    style = MaterialTheme.typography.labelSmall,
                                    fontWeight = FontWeight.Bold,
                                    color = AmberGold,
                                    modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                                )
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.height(4.dp))
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.End,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier
                            .clip(RoundedCornerShape(8.dp))
                            .clickable(onClick = onReplyClick)
                            .padding(horizontal = 8.dp, vertical = 5.dp)
                    ) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.Reply,
                            contentDescription = androidx.compose.ui.res.stringResource(com.example.R.string.forum_reply_btn),
                            modifier = Modifier.size(13.dp),
                            tint = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            text = androidx.compose.ui.res.stringResource(com.example.R.string.forum_reply_btn),
                            style = MaterialTheme.typography.labelSmall,
                            fontWeight = FontWeight.Medium,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                    IconButton(
                        onClick = onToggleLike,
                        modifier = Modifier.size(28.dp)
                    ) {
                        Icon(
                            imageVector = if (reply.isLikedByMe) Icons.Default.Favorite else Icons.Default.FavoriteBorder,
                            contentDescription = if (reply.isLikedByMe) {
                                androidx.compose.ui.res.stringResource(com.example.R.string.forum_unlike_cd)
                            } else {
                                androidx.compose.ui.res.stringResource(com.example.R.string.forum_like_cd)
                            },
                            tint = if (reply.isLikedByMe) LikeActiveRed else MaterialTheme.colorScheme.onSurfaceVariant,
                            modifier = Modifier.size(14.dp)
                        )
                    }
                    Text(
                        text = reply.likeCount.toString(),
                        style = MaterialTheme.typography.labelSmall,
                        fontWeight = FontWeight.SemiBold,
                        color = if (reply.isLikedByMe) LikeActiveRed else MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }
        }
    }
}

@Composable
private fun ForumQuoteBox(
    authorName: String,
    snippet: String,
    onClick: (() -> Unit)?,
    modifier: Modifier = Modifier
) {
    val primaryColor = MaterialTheme.colorScheme.primary
    val onSurfaceVariantColor = MaterialTheme.colorScheme.onSurfaceVariant
    val barColor = remember(primaryColor) {
        primaryColor.copy(alpha = 0.6f)
    }
    val authorTextColor = remember(primaryColor) {
        primaryColor.copy(alpha = 0.85f)
    }
    val snippetTextColor = remember(onSurfaceVariantColor) {
        onSurfaceVariantColor.copy(alpha = 0.75f)
    }

    Row(
        modifier = modifier
            .fillMaxWidth()
            .height(IntrinsicSize.Min)
            .then(
                if (onClick != null) Modifier.clickable(onClick = onClick) else Modifier
            )
            .padding(bottom = 6.dp)
    ) {
        Box(
            modifier = Modifier
                .width(2.5.dp)
                .fillMaxHeight()
                .background(barColor, ForumQuoteBarShape)
        )
        Spacer(modifier = Modifier.width(8.dp))
        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = authorName,
                style = MaterialTheme.typography.labelSmall,
                fontWeight = FontWeight.SemiBold,
                color = authorTextColor
            )
            Text(
                text = snippet,
                style = MaterialTheme.typography.bodySmall,
                color = snippetTextColor,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
        }
    }
}

@Composable
private fun ForumAuthorBadges(
    isAuthorMe: Boolean,
    authorIsMentor: Boolean,
    authorAccessRole: String = "",
    authorLevel: Int,
    modifier: Modifier = Modifier
) {
    val isSuperAdmin = authorAccessRole.equals("super_admin", ignoreCase = true)
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier
    ) {
        if (isAuthorMe) {
            Surface(
                shape = ForumBadgeShape,
                color = SayaBadgeBg
            ) {
                Text(
                    text = androidx.compose.ui.res.stringResource(com.example.R.string.forum_badge_me),
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.padding(horizontal = 4.dp, vertical = 1.dp)
                )
            }
            Spacer(modifier = Modifier.width(6.dp))
        }
        if (isSuperAdmin) {
            val adminTransition = androidx.compose.animation.core.rememberInfiniteTransition(label = "forum_admin_badge")
            val shimmerAlpha by adminTransition.animateFloat(
                initialValue = 0.72f,
                targetValue = 1.0f,
                animationSpec = androidx.compose.animation.core.infiniteRepeatable(
                    animation = androidx.compose.animation.core.tween(1400, easing = androidx.compose.animation.core.FastOutSlowInEasing),
                    repeatMode = androidx.compose.animation.core.RepeatMode.Reverse
                ),
                label = "forum_admin_alpha"
            )
            Surface(
                shape = ForumBadgeShape,
                color = Color(0xFF312E81).copy(alpha = 0.85f * shimmerAlpha),
                border = BorderStroke(
                    1.dp,
                    Brush.linearGradient(listOf(AmberGold, Color(0xFFEC4899), Color(0xFF38BDF8)))
                )
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.padding(horizontal = 7.dp, vertical = 2.dp)
                ) {
                    Icon(
                        imageVector = Icons.Rounded.AutoAwesome,
                        contentDescription = null,
                        tint = AmberGold,
                        modifier = Modifier.size(12.dp)
                    )
                    Spacer(modifier = Modifier.width(3.dp))
                    Text(
                        text = androidx.compose.ui.res.stringResource(com.example.R.string.forum_badge_super_admin),
                        style = MaterialTheme.typography.labelSmall,
                        color = Color(0xFFFDE68A),
                        fontWeight = FontWeight.ExtraBold
                    )
                }
            }
        } else if (authorIsMentor) {
            Surface(
                shape = ForumBadgeShape,
                color = Color(0xFF064E3B).copy(alpha = 0.18f),
                border = BorderStroke(1.dp, Color(0xFF10B981).copy(alpha = 0.45f))
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                ) {
                    Icon(
                        imageVector = Icons.Rounded.School,
                        contentDescription = null,
                        tint = Color(0xFF10B981),
                        modifier = Modifier.size(12.dp)
                    )
                    Spacer(modifier = Modifier.width(3.dp))
                    Text(
                        text = androidx.compose.ui.res.stringResource(com.example.R.string.forum_badge_facilitator),
                        style = MaterialTheme.typography.labelSmall,
                        color = Color(0xFF10B981),
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        } else {
            val levelBadgeBg = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.6f)
            Surface(
                shape = ForumBadgeShape,
                color = levelBadgeBg
            ) {
                Text(
                    text = "Lv.$authorLevel • ${levelTierLabel(authorLevel)}",
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    fontWeight = FontWeight.Medium,
                    modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                )
            }
        }
    }
}

private fun formatTimestamp(
    millis: Long,
    context: android.content.Context,
    language: AppLanguage = AppLanguage.INDONESIAN
): String {
    val diff = System.currentTimeMillis() - millis
    return when {
        diff < 60_000L -> context.getString(R.string.forum_time_just_now)
        diff < 3600_000L -> context.getString(R.string.forum_time_mins_ago_format, diff / 60_000L)
        diff < 86400_000L -> context.getString(R.string.forum_time_hours_ago_format, diff / 3600_000L)
        else -> SimpleDateFormat("dd MMM yyyy, HH:mm", language.toLocale()).format(Date(millis))
    }
}

@Composable
fun ErrorRetryView(
    message: String,
    onRetry: () -> Unit,
    modifier: Modifier = Modifier,
    errorKind: com.example.util.NetworkErrorKind = com.example.util.NetworkErrorKind.OFFLINE
) {
    val mappedUi = com.example.util.mapErrorKindToUi(errorKind)
    val icon = when (errorKind) {
        com.example.util.NetworkErrorKind.OFFLINE -> Icons.Default.WifiOff
        com.example.util.NetworkErrorKind.SESSION_OR_PERMISSION -> Icons.Default.Lock
        com.example.util.NetworkErrorKind.SERVER_OR_OTHER -> Icons.Default.CloudOff
    }
    Box(
        contentAlignment = Alignment.Center,
        modifier = modifier
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
            modifier = Modifier.padding(24.dp)
        ) {
            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier
                    .size(72.dp)
                    .clip(CircleShape)
                    .background(MaterialTheme.colorScheme.errorContainer.copy(alpha = 0.4f))
            ) {
                Icon(
                    imageVector = icon,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.error,
                    modifier = Modifier.size(36.dp)
                )
            }
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                text = androidx.compose.ui.res.stringResource(mappedUi.titleRes),
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onSurface
            )
            Spacer(modifier = Modifier.height(6.dp))
            Text(
                text = message,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                textAlign = androidx.compose.ui.text.style.TextAlign.Center
            )
            Spacer(modifier = Modifier.height(16.dp))
            Button(
                onClick = onRetry,
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.primary
                ),
                shape = RoundedCornerShape(12.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.Refresh,
                    contentDescription = null,
                    modifier = Modifier.size(18.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = androidx.compose.ui.res.stringResource(mappedUi.actionLabelRes),
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }
}

