package com.example.ui.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateColorAsState
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
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
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
import androidx.compose.material.icons.filled.DeleteOutline
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.Forum
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.rounded.ChatBubbleOutline
import androidx.compose.material.icons.rounded.ExpandLess
import androidx.compose.material.icons.rounded.ExpandMore
import androidx.compose.material.icons.rounded.School
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
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
            replies = repliesByRoot[parent.postId].orEmpty().sortedBy { it.timestamp }
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

    if (postToDelete != null) {
        val targetDelete = postToDelete!!
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

    // 1. Strict Lifecycle-managed Firestore Real-Time Snapshot Listener inside DisposableEffect
    // CRITICAL: Listener MUST be removed in onDispose to prevent memory leaks and background consumption!
    DisposableEffect(course.id, retryTrigger) {
        val listenerRegistration = socialViewModel.attachForumListener(
            courseId = course.id,
            onError = {
                loadError = context.getString(com.example.R.string.forum_load_error)
            },
            onUpdate = { updatedPosts ->
                loadError = null
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
        topBar = {
            TopAppBar(
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
                modifier = Modifier
                    .fillMaxWidth()
                    .navigationBarsPadding()
                    .imePadding()
            ) {
                Column(modifier = Modifier.fillMaxWidth()) {
                    if (replyingToPost != null) {
                        val replyTarget = replyingToPost!!
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

                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp, vertical = 10.dp)
                    ) {
                        OutlinedTextField(
                            value = postInput,
                            onValueChange = { if (it.length <= 500) postInput = it },
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
                                                val postById = posts.associateBy { it.postId }
                                                val rootId = resolveRootPostId(replyTarget, postById)
                                                expandedThreadIds = expandedThreadIds + rootId
                                            } else {
                                                coroutineScope.launch {
                                                    listState.animateScrollToItem(0)
                                                }
                                            }
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
                FilterChip(
                    selected = selectedFilter == ForumFilterMode.ALL,
                    onClick = { selectedFilter = ForumFilterMode.ALL },
                    label = { Text(stringResource(R.string.forum_filter_all)) }
                )
                FilterChip(
                    selected = selectedFilter == ForumFilterMode.FACILITATOR,
                    onClick = { selectedFilter = ForumFilterMode.FACILITATOR },
                    label = { Text(stringResource(R.string.forum_filter_facilitator)) }
                )
                FilterChip(
                    selected = selectedFilter == ForumFilterMode.MINE,
                    onClick = { selectedFilter = ForumFilterMode.MINE },
                    label = { Text(stringResource(R.string.forum_filter_mine)) }
                )
                FilterChip(
                    selected = selectedFilter == ForumFilterMode.POPULAR,
                    onClick = { selectedFilter = ForumFilterMode.POPULAR },
                    label = { Text(stringResource(R.string.forum_filter_popular)) }
                )
            }

            if (loadError != null && posts.isEmpty()) {
                // Error state with retry
                ErrorRetryView(
                    message = loadError ?: androidx.compose.ui.res.stringResource(com.example.R.string.forum_load_error),
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
                                    fallbackLevel = target?.authorLevel ?: 1
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
    val formattedTime = remember(post.timestamp) {
        formatTimestamp(post.timestamp)
    }

    val normalBg = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f)
    val highlightBg = MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.45f)
    val animatedBgColor by animateColorAsState(
        targetValue = if (isHighlighted) highlightBg else normalBg,
        animationSpec = tween(durationMillis = 600),
        label = "post_highlight_color"
    )

    Card(
        shape = ForumCardShape,
        colors = CardDefaults.cardColors(
            containerColor = animatedBgColor
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
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(
                            text = post.authorName,
                            style = MaterialTheme.typography.bodyMedium,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        ForumAuthorBadges(
                            isAuthorMe = isAuthorMe,
                            authorIsMentor = post.authorIsMentor,
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
                    TextButton(
                        onClick = onReplyClick,
                        modifier = Modifier.testTag("reply_button_${post.postId}"),
                        contentPadding = PaddingValues(horizontal = 8.dp, vertical = 4.dp)
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
    val formattedTime = remember(reply.timestamp) { formatTimestamp(reply.timestamp) }

    Row(
        modifier = modifier
            .fillMaxWidth()
            .testTag("forum_reply_item_${reply.postId}")
    ) {
        // Subtle vertical thread line
        Box(
            modifier = Modifier
                .width(2.5.dp)
                .height(64.dp)
                .background(
                    color = if (reply.authorIsMentor) AmberGold.copy(alpha = 0.7f) else TealPrimary.copy(alpha = 0.4f),
                    shape = ForumQuoteBarShape
                )
        )
        Spacer(modifier = Modifier.width(10.dp))
        Surface(
            shape = RoundedCornerShape(12.dp),
            color = if (reply.authorIsMentor) {
                AmberGold.copy(alpha = 0.08f)
            } else {
                MaterialTheme.colorScheme.surface.copy(alpha = 0.85f)
            },
            border = if (reply.authorIsMentor) {
                BorderStroke(1.dp, AmberGold.copy(alpha = 0.3f))
            } else null,
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
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text(
                                text = reply.authorName,
                                style = MaterialTheme.typography.labelMedium,
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.onSurface
                            )
                            Spacer(modifier = Modifier.width(6.dp))
                            ForumAuthorBadges(
                                isAuthorMe = isAuthorMe,
                                authorIsMentor = reply.authorIsMentor,
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
                    Text(
                        text = androidx.compose.ui.res.stringResource(com.example.R.string.forum_replying_at_format, reply.replyToAuthorName),
                        style = MaterialTheme.typography.labelSmall,
                        fontWeight = FontWeight.SemiBold,
                        color = MaterialTheme.colorScheme.primary
                    )
                }

                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = reply.content,
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurface,
                    lineHeight = 18.sp
                )

                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.End,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    TextButton(
                        onClick = onReplyClick,
                        contentPadding = PaddingValues(horizontal = 6.dp, vertical = 2.dp),
                        modifier = Modifier.height(28.dp)
                    ) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.Reply,
                            contentDescription = androidx.compose.ui.res.stringResource(com.example.R.string.forum_reply_btn),
                            modifier = Modifier.size(13.dp),
                            tint = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                        Spacer(modifier = Modifier.width(3.dp))
                        Text(
                            text = androidx.compose.ui.res.stringResource(com.example.R.string.forum_reply_btn),
                            style = MaterialTheme.typography.labelSmall,
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
            .then(
                if (onClick != null) Modifier.clickable(onClick = onClick) else Modifier
            )
            .padding(bottom = 6.dp)
    ) {
        Box(
            modifier = Modifier
                .width(2.5.dp)
                .height(34.dp)
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
    authorLevel: Int,
    modifier: Modifier = Modifier
) {
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
        if (authorIsMentor) {
            Surface(
                shape = ForumBadgeShape,
                color = MentorBadgeBg
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                ) {
                    Icon(
                        imageVector = Icons.Rounded.School,
                        contentDescription = null,
                        tint = AmberGold,
                        modifier = Modifier.size(12.dp)
                    )
                    Spacer(modifier = Modifier.width(3.dp))
                    Text(
                        text = androidx.compose.ui.res.stringResource(com.example.R.string.forum_badge_facilitator),
                        style = MaterialTheme.typography.labelSmall,
                        color = AmberGold,
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

private fun formatTimestamp(millis: Long, language: AppLanguage = AppLanguage.INDONESIAN): String {
    val diff = System.currentTimeMillis() - millis
    return when {
        diff < 60_000L -> "Baru saja"
        diff < 3600_000L -> "${diff / 60_000L} mnt lalu"
        diff < 86400_000L -> "${diff / 3600_000L} jam lalu"
        else -> SimpleDateFormat("dd MMM yyyy, HH:mm", language.toLocale()).format(Date(millis))
    }
}

@Composable
fun ErrorRetryView(
    message: String,
    onRetry: () -> Unit,
    modifier: Modifier = Modifier
) {
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
                    imageVector = Icons.Default.WifiOff,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.error,
                    modifier = Modifier.size(36.dp)
                )
            }
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                text = androidx.compose.ui.res.stringResource(com.example.R.string.forum_disconnected_title),
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
                    text = androidx.compose.ui.res.stringResource(com.example.R.string.forum_retry_btn),
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }
}

