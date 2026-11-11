package com.example.ui.screens.home

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
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
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.CourseEntity
import com.example.data.model.ProfileEntity
import com.example.data.model.TrackEntity
import com.example.data.model.UserProgressEntity
import com.example.ui.components.CourseCard
import com.example.ui.components.StatCard
import com.example.ui.components.XPProgressBar
import com.example.ui.theme.*

@Composable
fun HomeScreen(
    profile: ProfileEntity?,
    tracks: List<TrackEntity>,
    courses: List<CourseEntity>,
    userProgressList: List<UserProgressEntity>,
    unreadNotifCount: Int,
    unreadChatCount: Int,
    onNavigateToCourse: (String) -> Unit,
    onNavigateToCatalog: (String?) -> Unit,
    onNavigateToNotifications: () -> Unit,
    onNavigateToChat: () -> Unit,
    onNavigateToAiTutor: () -> Unit,
    onNavigateToForum: () -> Unit,
    modifier: Modifier = Modifier
) {
    var selectedTrackFilter by remember { mutableStateOf<String?>(null) }

    val userTracksList = remember(profile?.selectedTracks) {
        val parsed = profile?.selectedTracks?.split(",")?.map { it.trim() }?.filter { it.isNotBlank() && it != "ui-ux" } ?: emptyList()
        if (parsed.isEmpty()) listOf("front-end") else parsed
    }

    val trackCourseCounts = remember(courses) {
        courses.groupBy { it.trackId }.mapValues { it.value.size }
    }
    // Sort tracks prioritizing user's selected tracks and filtering empty tracks
    val sortedTracks = remember(tracks, userTracksList, trackCourseCounts) {
        tracks.filter { (trackCourseCounts[it.id] ?: 0) > 0 }
            .sortedByDescending { userTracksList.contains(it.id) }
    }

    // Filter courses
    val filteredCourses = remember(courses, selectedTrackFilter) {
        if (selectedTrackFilter == null) courses
        else courses.filter { it.trackId == selectedTrackFilter }
    }

    // Course progress map
    val progressByCourse = remember(userProgressList) {
        userProgressList.groupBy { it.courseId }.mapValues { (_, list) ->
            // Assume 2 modules per course for percentage calc
            (list.size * 50).coerceAtMost(100)
        }
    }

    // Continue course (first course with progress or first course)
    val continueCourse = remember(courses, progressByCourse) {
        courses.firstOrNull { (progressByCourse[it.id] ?: 0) in 1..99 } ?: courses.firstOrNull()
    }

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .background(BackgroundDark),
        contentPadding = PaddingValues(bottom = 90.dp)
    ) {
        // Top Header
        item(key = "top_header") {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp, vertical = 16.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .size(40.dp)
                            .clip(RoundedCornerShape(12.dp))
                            .background(Brush.linearGradient(listOf(PrimaryPurple, SecondaryTeal))),
                        contentAlignment = Alignment.Center
                    ) {
                        Text("</>", fontSize = 18.sp, fontWeight = FontWeight.Bold, color = Color.White)
                    }
                    Column {
                        Text(
                            text = "Halo, ${profile?.displayName ?: "Siswa"}! 👋",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold,
                            color = TextPrimaryDark
                        )
                        Text(
                            text = profile?.goal ?: "Mulai Belajar Koding",
                            style = MaterialTheme.typography.labelSmall,
                            color = TextSecondaryDark
                        )
                    }
                }

                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    // Chat Icon with Badge
                    IconButton(
                        onClick = onNavigateToChat,
                        modifier = Modifier
                            .size(42.dp)
                            .clip(CircleShape)
                            .background(SurfaceDark)
                    ) {
                        BadgedBox(
                            badge = {
                                if (unreadChatCount > 0) {
                                    Badge(containerColor = SecondaryTeal) {
                                        Text("$unreadChatCount", color = BackgroundDark, fontSize = 10.sp)
                                    }
                                }
                            }
                        ) {
                            Icon(
                                imageVector = Icons.Outlined.ChatBubbleOutline,
                                contentDescription = "Pesan Chat",
                                tint = TextPrimaryDark,
                                modifier = Modifier.size(20.dp)
                            )
                        }
                    }

                    // Notification Icon with Badge
                    IconButton(
                        onClick = onNavigateToNotifications,
                        modifier = Modifier
                            .size(42.dp)
                            .clip(CircleShape)
                            .background(SurfaceDark)
                    ) {
                        BadgedBox(
                            badge = {
                                if (unreadNotifCount > 0) {
                                    Badge(containerColor = AccentError) {
                                        Text("$unreadNotifCount", color = Color.White, fontSize = 10.sp)
                                    }
                                }
                            }
                        ) {
                            Icon(
                                imageVector = Icons.Outlined.Notifications,
                                contentDescription = "Notifikasi",
                                tint = TextPrimaryDark,
                                modifier = Modifier.size(20.dp)
                            )
                        }
                    }
                }
            }
        }

        // Gamification Stats Row (XP Bar & Streak Card)
        item(key = "gamification_stats") {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                XPProgressBar(
                    totalXp = profile?.totalXp ?: 0,
                    level = profile?.level ?: 1
                )

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    StatCard(
                        title = "Streak Belajar",
                        value = "${profile?.currentStreak ?: 0} Hari",
                        icon = Icons.Filled.LocalFireDepartment,
                        accentColor = AccentStreak,
                        modifier = Modifier.weight(1f)
                    )
                    StatCard(
                        title = "Modul Selesai",
                        value = "${userProgressList.size} Modul",
                        icon = Icons.Filled.CheckCircle,
                        accentColor = AccentSuccess,
                        modifier = Modifier.weight(1f)
                    )
                }
            }
        }

        // Daily Quest Card Banner
        item(key = "daily_quest") {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp, vertical = 14.dp),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = SurfaceDark),
                border = CardDefaults.outlinedCardBorder().copy(brush = Brush.horizontalGradient(listOf(PrimaryPurpleDark, SecondaryTealDark)))
            ) {
                Row(
                    modifier = Modifier.padding(16.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .size(46.dp)
                            .clip(RoundedCornerShape(12.dp))
                            .background(Brush.linearGradient(listOf(PrimaryPurple, SecondaryTeal))),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(text = "🎯", fontSize = 24.sp)
                    }
                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = "Quest Harian: Selesaikan 1 Modul",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold,
                            color = TextPrimaryDark
                        )
                        Text(
                            text = "Dapatkan +50 XP bonus dan pertahankan streak belajarmu!",
                            style = MaterialTheme.typography.bodySmall,
                            color = TextSecondaryDark
                        )
                    }
                }
            }
        }

        // Quick Tools: AI Tutor & Live Playground Shortcuts
        item(key = "quick_tools") {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Card(
                    modifier = Modifier
                        .weight(1f)
                        .clickable { onNavigateToAiTutor() },
                    shape = RoundedCornerShape(14.dp),
                    colors = CardDefaults.cardColors(containerColor = SurfaceDark),
                    border = CardDefaults.outlinedCardBorder().copy(brush = Brush.horizontalGradient(listOf(PrimaryPurple, PrimaryPurpleDark)))
                ) {
                    Row(
                        modifier = Modifier.padding(12.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        Text(text = "🤖", fontSize = 22.sp)
                        Column {
                            Text("AI Tutor", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold, color = TextPrimaryDark)
                            Text("Tanya & Debug", style = MaterialTheme.typography.labelSmall, color = SecondaryTeal)
                        }
                    }
                }

                Card(
                    modifier = Modifier
                        .weight(1f)
                        .clickable { onNavigateToForum() },
                    shape = RoundedCornerShape(14.dp),
                    colors = CardDefaults.cardColors(containerColor = SurfaceDark),
                    border = CardDefaults.outlinedCardBorder().copy(brush = Brush.horizontalGradient(listOf(SecondaryTeal, SecondaryTealDark)))
                ) {
                    Row(
                        modifier = Modifier.padding(12.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        Text(text = "💬", fontSize = 22.sp)
                        Column {
                            Text("Forum Diskusi", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold, color = TextPrimaryDark)
                            Text("Tanya Komunitas", style = MaterialTheme.typography.labelSmall, color = AccentWarning)
                        }
                    }
                }
            }
        }

        // Continue Course Card (if any)
        if (continueCourse != null) {
            item(key = "continue_course") {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 20.dp, vertical = 16.dp)
                ) {
                    Text(
                        text = "Lanjutkan Belajar 📖",
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Bold,
                        color = TextPrimaryDark
                    )
                    Spacer(modifier = Modifier.height(10.dp))

                    val percent = progressByCourse[continueCourse.id] ?: 0
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable { onNavigateToCourse(continueCourse.id) },
                        shape = RoundedCornerShape(16.dp),
                        colors = CardDefaults.cardColors(containerColor = SurfaceDark),
                        border = CardDefaults.outlinedCardBorder().copy(brush = Brush.horizontalGradient(listOf(PrimaryPurple, SecondaryTeal)))
                    ) {
                        Column(modifier = Modifier.padding(16.dp)) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Surface(
                                    shape = RoundedCornerShape(8.dp),
                                    color = PrimaryPurple.copy(alpha = 0.2f)
                                ) {
                                    Text(
                                        text = continueCourse.level,
                                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                                        style = MaterialTheme.typography.labelSmall,
                                        color = SecondaryTeal,
                                        fontWeight = FontWeight.Bold
                                    )
                                }

                                Text(
                                    text = if (percent > 0) "Progres: $percent%" else "Belum dimulai",
                                    style = MaterialTheme.typography.labelSmall,
                                    color = TextSecondaryDark,
                                    fontWeight = FontWeight.SemiBold
                                )
                            }

                            Spacer(modifier = Modifier.height(8.dp))

                            Text(
                                text = continueCourse.title,
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.Bold,
                                color = TextPrimaryDark
                            )

                            Text(
                                text = continueCourse.description,
                                style = MaterialTheme.typography.bodySmall,
                                color = TextSecondaryDark,
                                maxLines = 2,
                                overflow = TextOverflow.Ellipsis
                            )

                            Spacer(modifier = Modifier.height(14.dp))

                            Button(
                                onClick = { onNavigateToCourse(continueCourse.id) },
                                modifier = Modifier.fillMaxWidth(),
                                shape = RoundedCornerShape(10.dp),
                                colors = ButtonDefaults.buttonColors(containerColor = PrimaryPurple)
                            ) {
                                Icon(Icons.Default.PlayArrow, contentDescription = null, modifier = Modifier.size(18.dp))
                                Spacer(modifier = Modifier.width(6.dp))
                                Text(
                                    text = if (percent > 0) "Lanjutkan Modul Berikutnya" else "Mulai Pelajari Kursus",
                                    fontWeight = FontWeight.Bold
                                )
                            }
                        }
                    }
                }
            }
        }

        // Track Filter Chips
        item(key = "track_chips") {
            Column(modifier = Modifier.fillMaxWidth()) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 20.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Jalur Belajar (Tracks)",
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Bold,
                        color = TextPrimaryDark
                    )
                    Text(
                        text = "Lihat Semua",
                        style = MaterialTheme.typography.labelLarge,
                        color = SecondaryTeal,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.clickable { onNavigateToCatalog(null) }
                    )
                }

                Spacer(modifier = Modifier.height(10.dp))

                LazyRow(
                    contentPadding = PaddingValues(horizontal = 20.dp),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    item(key = "all_tracks") {
                        FilterChip(
                            selected = selectedTrackFilter == null,
                            onClick = { selectedTrackFilter = null },
                            label = { Text("✨ Semua Track") },
                            colors = FilterChipDefaults.filterChipColors(
                                selectedContainerColor = PrimaryPurple,
                                selectedLabelColor = Color.White,
                                containerColor = SurfaceDark,
                                labelColor = TextSecondaryDark
                            )
                        )
                    }

                    items(sortedTracks, key = { it.id }) { track ->
                        val isSelected = selectedTrackFilter == track.id
                        FilterChip(
                            selected = isSelected,
                            onClick = { selectedTrackFilter = if (isSelected) null else track.id },
                            label = { Text("${track.icon} ${track.name}") },
                            colors = FilterChipDefaults.filterChipColors(
                                selectedContainerColor = PrimaryPurple,
                                selectedLabelColor = Color.White,
                                containerColor = SurfaceDark,
                                labelColor = TextSecondaryDark
                            )
                        )
                    }
                }
            }
        }

        // Courses List
        item(key = "courses_header") {
            Spacer(modifier = Modifier.height(18.dp))
            Text(
                text = "Katalog Kursus Pilihan",
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold,
                color = TextPrimaryDark,
                modifier = Modifier.padding(horizontal = 20.dp)
            )
            Spacer(modifier = Modifier.height(10.dp))
        }

        items(filteredCourses, key = { it.id }) { course ->
            CourseCard(
                course = course,
                progressPercent = progressByCourse[course.id],
                onClick = { onNavigateToCourse(course.id) },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp, vertical = 6.dp)
            )
        }
    }
}
