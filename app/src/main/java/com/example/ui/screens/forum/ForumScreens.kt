package com.example.ui.screens.forum

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.Send
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.CourseEntity
import com.example.data.model.ForumReplyEntity
import com.example.data.model.ForumThreadEntity
import com.example.ui.theme.*

@Composable
fun ForumScreen(
    threads: List<ForumThreadEntity>,
    courses: List<CourseEntity>,
    initialCourseFilter: String? = null,
    onNavigateToThread: (String) -> Unit,
    onToggleUpvote: (ForumThreadEntity) -> Unit,
    onCreateThread: (String?, String?, String, String) -> Unit,
    modifier: Modifier = Modifier
) {
    var selectedCourseFilter by remember { mutableStateOf(initialCourseFilter) }
    var showCreateDialog by remember { mutableStateOf(false) }

    val filteredThreads = remember(threads, selectedCourseFilter) {
        if (selectedCourseFilter == null) threads
        else threads.filter { it.courseId == selectedCourseFilter }
    }

    Scaffold(
        modifier = modifier.fillMaxSize(),
        containerColor = BackgroundDark,
        floatingActionButton = {
            FloatingActionButton(
                onClick = { showCreateDialog = true },
                containerColor = PrimaryPurple,
                contentColor = Color.White,
                shape = CircleShape,
                modifier = Modifier.padding(bottom = 70.dp)
            ) {
                Icon(Icons.Default.Add, contentDescription = "Buat Diskusi")
            }
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(top = 16.dp)
        ) {
            Text(
                text = "Forum Diskusi & Komunitas 💬",
                style = MaterialTheme.typography.displayMedium,
                fontWeight = FontWeight.Bold,
                color = TextPrimaryDark,
                modifier = Modifier.padding(horizontal = 20.dp)
            )

            Spacer(modifier = Modifier.height(14.dp))

            // Thread Filter Chips (Semua vs Kursus Tertentu)
            ScrollableTabRow(
                selectedTabIndex = if (selectedCourseFilter == null) 0 else (courses.indexOfFirst { it.id == selectedCourseFilter } + 1).coerceAtLeast(0),
                containerColor = BackgroundDark,
                contentColor = SecondaryTeal,
                edgePadding = 20.dp,
                indicator = {},
                divider = {}
            ) {
                Tab(
                    selected = selectedCourseFilter == null,
                    onClick = { selectedCourseFilter = null },
                    text = {
                        Text(
                            "🌐 Semua Forum",
                            fontWeight = if (selectedCourseFilter == null) FontWeight.Bold else FontWeight.Normal,
                            color = if (selectedCourseFilter == null) SecondaryTeal else TextSecondaryDark
                        )
                    }
                )

                courses.forEach { course ->
                    val isSelected = selectedCourseFilter == course.id
                    Tab(
                        selected = isSelected,
                        onClick = { selectedCourseFilter = course.id },
                        text = {
                            Text(
                                course.title.take(18) + "...",
                                fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                                color = if (isSelected) SecondaryTeal else TextSecondaryDark
                            )
                        }
                    )
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            if (filteredThreads.isEmpty()) {
                Box(
                    modifier = Modifier.fillMaxWidth().weight(1f),
                    contentAlignment = Alignment.Center
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text("💬", fontSize = 42.sp)
                        Spacer(modifier = Modifier.height(10.dp))
                        Text(
                            text = "Belum Ada Diskusi di Topik Ini",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold,
                            color = TextPrimaryDark
                        )
                        Text(
                            text = "Jadilah yang pertama membuka pertanyaan atau diskusi!",
                            style = MaterialTheme.typography.bodySmall,
                            color = TextSecondaryDark
                        )
                    }
                }
            } else {
                LazyColumn(
                    modifier = Modifier.fillMaxWidth().weight(1f),
                    contentPadding = PaddingValues(start = 20.dp, end = 20.dp, bottom = 120.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    items(filteredThreads, key = { it.id }) { thread ->
                        Card(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable { onNavigateToThread(thread.id) },
                            shape = RoundedCornerShape(16.dp),
                            colors = CardDefaults.cardColors(containerColor = SurfaceDark),
                            border = CardDefaults.outlinedCardBorder().copy(brush = Brush.horizontalGradient(listOf(BorderDark, SurfaceVariantDark)))
                        ) {
                            Column(modifier = Modifier.padding(16.dp)) {
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Surface(
                                        shape = RoundedCornerShape(6.dp),
                                        color = PrimaryPurple.copy(alpha = 0.15f)
                                    ) {
                                        Text(
                                            text = thread.courseTitle ?: "Forum Umum",
                                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 3.dp),
                                            style = MaterialTheme.typography.labelSmall,
                                            color = SecondaryTeal,
                                            fontWeight = FontWeight.Bold
                                        )
                                    }

                                    Text(
                                        text = thread.authorName,
                                        style = MaterialTheme.typography.labelSmall,
                                        color = TextSecondaryDark
                                    )
                                }

                                Spacer(modifier = Modifier.height(10.dp))

                                Text(
                                    text = thread.title,
                                    style = MaterialTheme.typography.titleMedium,
                                    fontWeight = FontWeight.Bold,
                                    color = TextPrimaryDark
                                )

                                Spacer(modifier = Modifier.height(6.dp))

                                Text(
                                    text = thread.body,
                                    style = MaterialTheme.typography.bodyMedium,
                                    color = TextSecondaryDark,
                                    maxLines = 2
                                )

                                Spacer(modifier = Modifier.height(12.dp))

                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Row(
                                        verticalAlignment = Alignment.CenterVertically,
                                        horizontalArrangement = Arrangement.spacedBy(4.dp),
                                        modifier = Modifier
                                            .clip(RoundedCornerShape(8.dp))
                                            .background(if (thread.hasUpvoted) PrimaryPurple.copy(alpha = 0.2f) else SurfaceVariantDark)
                                            .clickable { onToggleUpvote(thread) }
                                            .padding(horizontal = 10.dp, vertical = 5.dp)
                                    ) {
                                        Icon(
                                            imageVector = if (thread.hasUpvoted) Icons.Filled.ThumbUp else Icons.Outlined.ThumbUp,
                                            contentDescription = "Upvote",
                                            tint = if (thread.hasUpvoted) PrimaryPurpleLight else TextSecondaryDark,
                                            modifier = Modifier.size(16.dp)
                                        )
                                        Text(
                                            text = "${thread.upvotes} Upvotes",
                                            style = MaterialTheme.typography.labelSmall,
                                            color = if (thread.hasUpvoted) PrimaryPurpleLight else TextSecondaryDark,
                                            fontWeight = FontWeight.Bold
                                        )
                                    }

                                    Row(
                                        verticalAlignment = Alignment.CenterVertically,
                                        horizontalArrangement = Arrangement.spacedBy(4.dp)
                                    ) {
                                        Icon(Icons.Outlined.ChatBubbleOutline, contentDescription = null, tint = TextMutedDark, modifier = Modifier.size(16.dp))
                                        Text(
                                            text = "${thread.repliesCount} Balasan",
                                            style = MaterialTheme.typography.labelSmall,
                                            color = TextMutedDark
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

    // Create Thread Dialog
    if (showCreateDialog) {
        var newTitle by remember { mutableStateOf("") }
        var newBody by remember { mutableStateOf("") }
        var selectedCourseId by remember { mutableStateOf<String?>(selectedCourseFilter) }

        AlertDialog(
            onDismissRequest = { showCreateDialog = false },
            title = { Text("Buat Diskusi Baru", fontWeight = FontWeight.Bold, color = TextPrimaryDark) },
            text = {
                Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                    OutlinedTextField(
                        value = newTitle,
                        onValueChange = { newTitle = it },
                        label = { Text("Judul Pertanyaan") },
                        singleLine = true,
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedContainerColor = SurfaceDark,
                            unfocusedContainerColor = SurfaceDark
                        )
                    )
                    OutlinedTextField(
                        value = newBody,
                        onValueChange = { newBody = it },
                        label = { Text("Isi Diskusi / Pertanyaan Detail") },
                        modifier = Modifier.height(120.dp),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedContainerColor = SurfaceDark,
                            unfocusedContainerColor = SurfaceDark
                        )
                    )
                }
            },
            confirmButton = {
                Button(
                    onClick = {
                        if (newTitle.isNotBlank() && newBody.isNotBlank()) {
                            val c = courses.find { it.id == selectedCourseId }
                            onCreateThread(selectedCourseId, c?.title, newTitle, newBody)
                            showCreateDialog = false
                        }
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = PrimaryPurple)
                ) {
                    Text("Posting (+25 XP)")
                }
            },
            dismissButton = {
                TextButton(onClick = { showCreateDialog = false }) {
                    Text("Batal", color = TextSecondaryDark)
                }
            },
            containerColor = SurfaceDark
        )
    }
}

@Composable
fun ThreadDetailScreen(
    thread: ForumThreadEntity?,
    replies: List<ForumReplyEntity>,
    onNavigateBack: () -> Unit,
    onToggleUpvote: () -> Unit,
    onAddReply: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    if (thread == null) {
        Box(modifier = Modifier.fillMaxSize().background(BackgroundDark), contentAlignment = Alignment.Center) {
            Text("Diskusi tidak ditemukan.", color = TextSecondaryDark)
        }
        return
    }

    var replyText by remember { mutableStateOf("") }

    Scaffold(
        modifier = modifier.fillMaxSize(),
        containerColor = BackgroundDark,
        topBar = {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 12.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(
                    onClick = onNavigateBack,
                    modifier = Modifier.clip(CircleShape).background(SurfaceDark)
                ) {
                    Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Kembali", tint = TextPrimaryDark)
                }
                Text("Detail Diskusi", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold, color = TextPrimaryDark)
                Box(modifier = Modifier.size(40.dp))
            }
        },
        bottomBar = {
            Surface(
                color = SurfaceDark,
                border = CardDefaults.outlinedCardBorder().copy(brush = Brush.verticalGradient(listOf(BorderDark, SurfaceDark))),
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 10.dp)
                        .navigationBarsPadding(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    OutlinedTextField(
                        value = replyText,
                        onValueChange = { replyText = it },
                        placeholder = { Text("Tulis jawaban atau saran...", color = TextMutedDark, fontSize = 13.sp) },
                        modifier = Modifier.weight(1f),
                        shape = RoundedCornerShape(24.dp),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedContainerColor = BackgroundDark,
                            unfocusedContainerColor = BackgroundDark
                        )
                    )

                    IconButton(
                        onClick = {
                            if (replyText.isNotBlank()) {
                                onAddReply(replyText)
                                replyText = ""
                            }
                        },
                        enabled = replyText.isNotBlank(),
                        modifier = Modifier
                            .size(46.dp)
                            .clip(CircleShape)
                            .background(if (replyText.isNotBlank()) PrimaryPurple else SurfaceVariantDark)
                    ) {
                        Icon(Icons.AutoMirrored.Filled.Send, contentDescription = "Kirim Balasan", tint = Color.White, modifier = Modifier.size(20.dp))
                    }
                }
            }
        }
    ) { paddingValues ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(horizontal = 20.dp),
            contentPadding = PaddingValues(bottom = 20.dp),
            verticalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            // Main Thread Card
            item(key = "main_thread_card") {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = SurfaceDark),
                    border = CardDefaults.outlinedCardBorder().copy(brush = Brush.horizontalGradient(listOf(PrimaryPurple, SecondaryTeal)))
                ) {
                    Column(modifier = Modifier.padding(18.dp)) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = "Oleh ${thread.authorName}",
                                style = MaterialTheme.typography.labelSmall,
                                color = SecondaryTeal,
                                fontWeight = FontWeight.Bold
                            )
                            Text(
                                text = thread.courseTitle ?: "Forum Umum",
                                style = MaterialTheme.typography.labelSmall,
                                color = TextSecondaryDark
                            )
                        }

                        Spacer(modifier = Modifier.height(10.dp))

                        Text(
                            text = thread.title,
                            style = MaterialTheme.typography.titleLarge,
                            fontWeight = FontWeight.Bold,
                            color = TextPrimaryDark
                        )

                        Spacer(modifier = Modifier.height(10.dp))

                        Text(
                            text = thread.body,
                            style = MaterialTheme.typography.bodyLarge,
                            color = TextPrimaryDark,
                            lineHeight = 22.sp
                        )

                        Spacer(modifier = Modifier.height(14.dp))

                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(6.dp),
                            modifier = Modifier
                                .clip(RoundedCornerShape(8.dp))
                                .background(if (thread.hasUpvoted) PrimaryPurple.copy(alpha = 0.2f) else SurfaceVariantDark)
                                .clickable { onToggleUpvote() }
                                .padding(horizontal = 12.dp, vertical = 6.dp)
                        ) {
                            Icon(
                                imageVector = if (thread.hasUpvoted) Icons.Filled.ThumbUp else Icons.Outlined.ThumbUp,
                                contentDescription = null,
                                tint = if (thread.hasUpvoted) PrimaryPurpleLight else TextSecondaryDark,
                                modifier = Modifier.size(16.dp)
                            )
                            Text(
                                text = "${thread.upvotes} Upvotes",
                                style = MaterialTheme.typography.labelSmall,
                                color = if (thread.hasUpvoted) PrimaryPurpleLight else TextSecondaryDark,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }
                }
            }

            // Replies Header
            item(key = "replies_header") {
                Text(
                    text = "Balasan Diskusi (${replies.size})",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = TextPrimaryDark
                )
            }

            // Replies list
            items(replies, key = { it.id }) { reply ->
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(14.dp),
                    colors = CardDefaults.cardColors(containerColor = SurfaceDark),
                    border = CardDefaults.outlinedCardBorder().copy(brush = Brush.horizontalGradient(listOf(BorderDark, BorderDark)))
                ) {
                    Column(modifier = Modifier.padding(14.dp)) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = reply.authorName,
                                style = MaterialTheme.typography.labelSmall,
                                fontWeight = FontWeight.Bold,
                                color = if (reply.authorName.contains("Instructor")) AccentWarning else SecondaryTeal
                            )
                        }

                        Spacer(modifier = Modifier.height(6.dp))

                        Text(
                            text = reply.body,
                            style = MaterialTheme.typography.bodyMedium,
                            color = TextPrimaryDark,
                            lineHeight = 20.sp
                        )
                    }
                }
            }
        }
    }
}
