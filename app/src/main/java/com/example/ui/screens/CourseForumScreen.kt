package com.example.ui.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.Reply
import androidx.compose.material.icons.automirrored.filled.Send
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.Forum
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
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

private fun levelTierEmoji(level: Int): String = when {
    level >= 20 -> "👑"
    level >= 10 -> "🚀"
    level >= 5 -> "📘"
    else -> "🌱"
}

@Composable
private fun levelTierLabel(level: Int): String = com.example.util.localization.DataLabelMappings.levelTierLabel(level)

@Immutable
data class ThreadedPostItem(
    val post: ForumPostEntity,
    val depth: Int,
    val indentationDp: Dp
)

private fun buildThreadedPostList(posts: List<ForumPostEntity>): List<ThreadedPostItem> {
    val repliesByParent = posts.filter { it.replyToPostId != null }
        .groupBy { it.replyToPostId }
    val topLevel = posts.filter { it.replyToPostId == null }
    val result = mutableListOf<ThreadedPostItem>()
    val visited = mutableSetOf<String>()

    fun appendWithReplies(post: ForumPostEntity, depth: Int) {
        if (post.postId in visited) return
        visited += post.postId
        val indent = when (depth) {
            0 -> 0.dp
            1 -> 16.dp
            else -> 28.dp // max indent for depth >= 2 to keep layout comfortable on mobile
        }
        result += ThreadedPostItem(
            post = post,
            depth = depth,
            indentationDp = indent
        )
        val directReplies = repliesByParent[post.postId].orEmpty().sortedBy { it.timestamp }
        directReplies.forEach { reply -> appendWithReplies(reply, (depth + 1).coerceAtMost(2)) }
    }

    topLevel.forEach { appendWithReplies(it, 0) }
    // Reply "yatim" (induknya belum termuat, misal kasus pagination) tetap ditampilkan di akhir dengan indent flat 16.dp
    posts.filter { it.postId !in visited }.forEach { orphan ->
        result += ThreadedPostItem(
            post = orphan,
            depth = 1,
            indentationDp = 16.dp
        )
    }
    return result
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
    val coroutineScope = rememberCoroutineScope()
    val postsFlow = remember(course.id) { socialViewModel.getCourseForumPosts(course.id) }
    val posts by postsFlow.collectAsStateWithLifecycle(initialValue = emptyList())
    val threadedPosts = remember(posts) { buildThreadedPostList(posts) }
    val userProfile by viewModel.userProfile.collectAsStateWithLifecycle()

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
                loadError = "Gagal memuat diskusi. Periksa koneksi internetmu."
            },
            onUpdate = { updatedPosts ->
                loadError = null
                // In case of real-time incoming posts, Room is reactive and postsFlow emits automatically
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
                            text = "Forum Diskusi",
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
                            contentDescription = "Kembali"
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
                                contentDescription = "Segarkan Forum"
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
                                        text = "Membalas ${replyTarget.authorName}",
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
                                        contentDescription = "Batal Balas",
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
                                    text = if (replyingToPost != null) "Tulis balasan..." else "Tulis pertanyaan atau diskusi...",
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
                                            coroutineScope.launch {
                                                listState.animateScrollToItem(0)
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
                                    contentDescription = "Kirim Post",
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
                    message = "Tanya apa aja soal materi kelas ini, atau bantu jawab pertanyaan siswa lain.",
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
                            text = "Ruang Diskusi Kelas",
                            style = MaterialTheme.typography.titleSmall,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.primary
                        )
                        Text(
                            text = "Tanya jawab seputar materi dan kode di kelas ini.",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }
            }

            if (loadError != null && posts.isEmpty()) {
                // Error state with retry
                ErrorRetryView(
                    message = loadError ?: "Gagal memuat diskusi. Periksa koneksi internetmu.",
                    onRetry = {
                        retryTrigger++
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f)
                        .padding(24.dp)
                )
            } else if (posts.isEmpty()) {
                // Empty state
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
                        Icon(
                            imageVector = Icons.Default.Forum,
                            contentDescription = null,
                            tint = TealLight.copy(alpha = 0.6f),
                            modifier = Modifier.size(64.dp)
                        )
                        Spacer(modifier = Modifier.height(16.dp))
                        Text(
                            text = "Belum Ada Diskusi",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold
                        )
                        Spacer(modifier = Modifier.height(6.dp))
                        Text(
                            text = "Jadilah yang pertama bertanya di kelas ${course.title}!",
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                            textAlign = androidx.compose.ui.text.style.TextAlign.Center
                        )
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
                        items = threadedPosts,
                        key = { it.post.postId }
                    ) { threadedItem ->
                        val post = threadedItem.post
                        ForumPostCard(
                            post = post,
                            indentationDp = threadedItem.indentationDp,
                            currentUserId = userProfile?.id?.toString() ?: "",
                            isHighlighted = highlightedPostId == post.postId,
                            onReplyClick = {
                                replyingToPost = post
                            },
                            onQuoteClick = { targetPostId ->
                                val targetIndex = threadedPosts.indexOfFirst { it.post.postId == targetPostId }
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
                                            text = "Muat diskusi lebih lama...",
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
    currentUserId: String,
    isHighlighted: Boolean = false,
    onReplyClick: () -> Unit = {},
    onQuoteClick: ((String) -> Unit)? = null,
    onToggleLike: () -> Unit,
    modifier: Modifier = Modifier
) {
    val isAuthorMe = post.authorId == currentUserId
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
            // Author info row
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth()
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

            // Footer (Reply button + Like button & count)
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.End,
                modifier = Modifier.fillMaxWidth()
            ) {
                TextButton(
                    onClick = onReplyClick,
                    modifier = Modifier.testTag("reply_button_${post.postId}"),
                    contentPadding = PaddingValues(horizontal = 8.dp, vertical = 4.dp)
                ) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.Reply,
                        contentDescription = "Balas",
                        modifier = Modifier.size(16.dp),
                        tint = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = "Balas",
                        style = MaterialTheme.typography.labelSmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }

                Spacer(modifier = Modifier.width(8.dp))

                IconButton(
                    onClick = onToggleLike,
                    modifier = Modifier
                        .size(36.dp)
                        .testTag("like_button_${post.postId}")
                ) {
                    Icon(
                        imageVector = if (post.isLikedByMe) Icons.Default.Favorite else Icons.Default.FavoriteBorder,
                        contentDescription = if (post.isLikedByMe) "Batal Suka" else "Sukai",
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
                    text = "Saya",
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
                    modifier = Modifier.padding(horizontal = 5.dp, vertical = 1.dp)
                ) {
                    Text(text = "🎓", fontSize = 10.sp)
                    Spacer(modifier = Modifier.width(2.dp))
                    Text(
                        text = "Mentor",
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
                    text = "${levelTierEmoji(authorLevel)} ${levelTierLabel(authorLevel)}",
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    fontWeight = FontWeight.Medium,
                    modifier = Modifier.padding(horizontal = 5.dp, vertical = 1.dp)
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
                text = "Koneksi Terputus",
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
                    text = "Coba Lagi",
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }
}

