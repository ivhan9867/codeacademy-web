package com.example.ui.screens.catalog

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
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
import com.example.data.model.ModuleEntity
import com.example.data.model.TrackEntity
import com.example.data.model.UserProgressEntity
import com.example.ui.components.CourseCard
import com.example.ui.theme.*

@Composable
fun CatalogScreen(
    tracks: List<TrackEntity>,
    courses: List<CourseEntity>,
    initialTrackId: String? = null,
    userProgressList: List<UserProgressEntity>,
    onNavigateToCourse: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    var searchQuery by remember { mutableStateOf("") }
    var selectedTrack by remember { mutableStateOf(initialTrackId) }
    var selectedLevel by remember { mutableStateOf<String?>(null) }

    val levels = listOf("Pemula", "Menengah", "Lanjut")

    val filteredCourses = remember(courses, searchQuery, selectedTrack, selectedLevel) {
        courses.filter { course ->
            val matchesQuery = searchQuery.isBlank() ||
                    course.title.contains(searchQuery, ignoreCase = true) ||
                    course.description.contains(searchQuery, ignoreCase = true) ||
                    course.instructor.contains(searchQuery, ignoreCase = true)
            val matchesTrack = selectedTrack == null || course.trackId == selectedTrack
            val matchesLevel = selectedLevel == null || course.level.equals(selectedLevel, ignoreCase = true)
            matchesQuery && matchesTrack && matchesLevel
        }
    }

    val progressByCourse = remember(userProgressList) {
        userProgressList.groupBy { it.courseId }.mapValues { (_, list) ->
            (list.size * 50).coerceAtMost(100)
        }
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(BackgroundDark)
            .padding(top = 16.dp)
    ) {
        // Title
        Text(
            text = "Katalog & Eksplorasi 📚",
            style = MaterialTheme.typography.displayMedium,
            fontWeight = FontWeight.Bold,
            color = TextPrimaryDark,
            modifier = Modifier.padding(horizontal = 20.dp)
        )

        Spacer(modifier = Modifier.height(14.dp))

        // Search Bar
        OutlinedTextField(
            value = searchQuery,
            onValueChange = { searchQuery = it },
            placeholder = { Text("Cari materi, judul, atau bahasa...", color = TextMutedDark) },
            leadingIcon = { Icon(Icons.Default.Search, contentDescription = null, tint = SecondaryTeal) },
            trailingIcon = {
                if (searchQuery.isNotEmpty()) {
                    IconButton(onClick = { searchQuery = "" }) {
                        Icon(Icons.Default.Close, contentDescription = "Clear", tint = TextSecondaryDark)
                    }
                }
            },
            singleLine = true,
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp),
            shape = RoundedCornerShape(14.dp),
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = PrimaryPurple,
                unfocusedBorderColor = BorderDark,
                focusedContainerColor = SurfaceDark,
                unfocusedContainerColor = SurfaceDark
            )
        )

        Spacer(modifier = Modifier.height(12.dp))

        val tracksWithCourses = remember(tracks, courses) {
            val coursesPerTrack = courses.groupBy { it.trackId }
            tracks.filter { (coursesPerTrack[it.id]?.size ?: 0) > 0 }
        }

        // Tracks Filter Chips
        LazyRow(
            contentPadding = PaddingValues(horizontal = 20.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            item(key = "track_all") {
                FilterChip(
                    selected = selectedTrack == null,
                    onClick = { selectedTrack = null },
                    label = { Text("Semua Track") },
                    colors = FilterChipDefaults.filterChipColors(
                        selectedContainerColor = PrimaryPurple,
                        selectedLabelColor = Color.White,
                        containerColor = SurfaceDark,
                        labelColor = TextSecondaryDark
                    )
                )
            }
            items(tracksWithCourses, key = { it.id }) { track ->
                val isSelected = selectedTrack == track.id
                FilterChip(
                    selected = isSelected,
                    onClick = { selectedTrack = if (isSelected) null else track.id },
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

        Spacer(modifier = Modifier.height(8.dp))

        // Level Filter Chips
        LazyRow(
            contentPadding = PaddingValues(horizontal = 20.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            item(key = "level_all") {
                FilterChip(
                    selected = selectedLevel == null,
                    onClick = { selectedLevel = null },
                    label = { Text("Semua Level") },
                    colors = FilterChipDefaults.filterChipColors(
                        selectedContainerColor = SecondaryTealDark,
                        selectedLabelColor = Color.White,
                        containerColor = SurfaceDark,
                        labelColor = TextSecondaryDark
                    )
                )
            }
            items(levels) { level ->
                val isSelected = selectedLevel == level
                FilterChip(
                    selected = isSelected,
                    onClick = { selectedLevel = if (isSelected) null else level },
                    label = { Text(level) },
                    colors = FilterChipDefaults.filterChipColors(
                        selectedContainerColor = SecondaryTealDark,
                        selectedLabelColor = Color.White,
                        containerColor = SurfaceDark,
                        labelColor = TextSecondaryDark
                    )
                )
            }
        }

        Spacer(modifier = Modifier.height(12.dp))

        // Courses List
        if (filteredCourses.isEmpty()) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f),
                contentAlignment = Alignment.Center
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(text = "🔍", fontSize = 42.sp)
                    Spacer(modifier = Modifier.height(12.dp))
                    Text(
                        text = "Tidak ada kursus yang cocok",
                        style = MaterialTheme.typography.titleMedium,
                        color = TextPrimaryDark,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = "Coba kata kunci atau filter lain.",
                        style = MaterialTheme.typography.bodyMedium,
                        color = TextSecondaryDark
                    )
                }
            }
        } else {
            LazyColumn(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f),
                contentPadding = PaddingValues(start = 20.dp, end = 20.dp, bottom = 90.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                items(filteredCourses, key = { it.id }) { course ->
                    CourseCard(
                        course = course,
                        progressPercent = progressByCourse[course.id],
                        onClick = { onNavigateToCourse(course.id) }
                    )
                }
            }
        }
    }
}

@Composable
fun CourseDetailScreen(
    course: CourseEntity?,
    modules: List<ModuleEntity>,
    userProgressList: List<UserProgressEntity>,
    isBookmarked: Boolean,
    onToggleBookmark: () -> Unit,
    onNavigateBack: () -> Unit,
    onNavigateToModule: (String) -> Unit,
    onNavigateToQuiz: (String) -> Unit,
    onNavigateToForumForCourse: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    if (course == null) {
        Box(modifier = Modifier.fillMaxSize().background(BackgroundDark), contentAlignment = Alignment.Center) {
            CircularProgressIndicator(color = PrimaryPurple)
        }
        return
    }

    val completedModuleIds = remember(userProgressList) {
        userProgressList.filter { it.isCompleted }.map { it.moduleId }.toSet()
    }

    val firstIncompleteModule = remember(modules, completedModuleIds) {
        modules.firstOrNull { !completedModuleIds.contains(it.id) } ?: modules.firstOrNull()
    }

    val allModulesCompleted = remember(modules, completedModuleIds) {
        modules.isNotEmpty() && modules.all { completedModuleIds.contains(it.id) }
    }

    Scaffold(
        modifier = modifier.fillMaxSize(),
        containerColor = BackgroundDark,
        bottomBar = {
            Surface(
                color = SurfaceDark,
                border = CardDefaults.outlinedCardBorder().copy(brush = Brush.verticalGradient(listOf(BorderDark, SurfaceDark))),
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 20.dp, vertical = 14.dp)
                        .navigationBarsPadding(),
                    horizontalArrangement = Arrangement.spacedBy(12.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    if (allModulesCompleted) {
                        Button(
                            onClick = { onNavigateToQuiz(course.id) },
                            modifier = Modifier.fillMaxWidth().height(50.dp),
                            shape = RoundedCornerShape(12.dp),
                            colors = ButtonDefaults.buttonColors(containerColor = AccentSuccess)
                        ) {
                            Icon(Icons.Filled.EmojiEvents, contentDescription = null, tint = Color.White)
                            Spacer(modifier = Modifier.width(8.dp))
                            Text("Ambil Evaluasi Quiz & Sertifikat 🎓", fontWeight = FontWeight.Bold)
                        }
                    } else if (firstIncompleteModule != null) {
                        Button(
                            onClick = { onNavigateToModule(firstIncompleteModule.id) },
                            modifier = Modifier.fillMaxWidth().height(50.dp),
                            shape = RoundedCornerShape(12.dp),
                            colors = ButtonDefaults.buttonColors(containerColor = PrimaryPurple)
                        ) {
                            Icon(Icons.Filled.PlayArrow, contentDescription = null, tint = Color.White)
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = if (completedModuleIds.isNotEmpty()) "Lanjutkan: ${firstIncompleteModule.title}" else "Mulai Belajar Kursus",
                                fontWeight = FontWeight.Bold,
                                maxLines = 1,
                                overflow = TextOverflow.Ellipsis
                            )
                        }
                    }
                }
            }
        }
    ) { paddingValues ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues),
            contentPadding = PaddingValues(bottom = 24.dp)
        ) {
            // Header Bar
            item(key = "header_bar") {
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

                    Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        IconButton(
                            onClick = { onNavigateToForumForCourse(course.id) },
                            modifier = Modifier.clip(CircleShape).background(SurfaceDark)
                        ) {
                            Icon(Icons.Outlined.Forum, contentDescription = "Forum Kursus", tint = SecondaryTeal)
                        }

                        IconButton(
                            onClick = onToggleBookmark,
                            modifier = Modifier.clip(CircleShape).background(SurfaceDark)
                        ) {
                            Icon(
                                imageVector = if (isBookmarked) Icons.Filled.Bookmark else Icons.Outlined.BookmarkBorder,
                                contentDescription = "Bookmark",
                                tint = if (isBookmarked) AccentWarning else TextPrimaryDark
                            )
                        }
                    }
                }
            }

            // Hero Detail
            item(key = "hero_detail") {
                Column(modifier = Modifier.padding(horizontal = 20.dp)) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Surface(
                            shape = RoundedCornerShape(8.dp),
                            color = PrimaryPurple.copy(alpha = 0.2f)
                        ) {
                            Text(
                                text = course.level,
                                modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp),
                                style = MaterialTheme.typography.labelSmall,
                                color = SecondaryTeal,
                                fontWeight = FontWeight.Bold
                            )
                        }

                        if (course.rating > 0f) {
                            Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                                Icon(Icons.Filled.Star, contentDescription = null, tint = AccentWarning, modifier = Modifier.size(16.dp))
                                Text(text = "${course.rating} Rating", style = MaterialTheme.typography.labelSmall, color = TextSecondaryDark, fontWeight = FontWeight.Bold)
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(10.dp))

                    Text(
                        text = course.title,
                        style = MaterialTheme.typography.displayMedium,
                        fontWeight = FontWeight.Bold,
                        color = TextPrimaryDark
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    Text(
                        text = course.description,
                        style = MaterialTheme.typography.bodyLarge,
                        color = TextSecondaryDark,
                        lineHeight = 22.sp
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    // Instructor Card
                    Card(
                        shape = RoundedCornerShape(14.dp),
                        colors = CardDefaults.cardColors(containerColor = SurfaceDark),
                        border = CardDefaults.outlinedCardBorder().copy(brush = Brush.horizontalGradient(listOf(BorderDark, BorderDark)))
                    ) {
                        Row(
                            modifier = Modifier.padding(14.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(12.dp)
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(44.dp)
                                    .clip(CircleShape)
                                    .background(Brush.linearGradient(listOf(PrimaryPurple, SecondaryTeal))),
                                contentAlignment = Alignment.Center
                            ) {
                                Text("👨‍🏫", fontSize = 22.sp)
                            }
                            Column {
                                Text(
                                    text = course.instructor,
                                    style = MaterialTheme.typography.titleMedium,
                                    fontWeight = FontWeight.Bold,
                                    color = TextPrimaryDark
                                )
                                Text(
                                    text = "Instruktur Kursus CodeAcademy",
                                    style = MaterialTheme.typography.labelSmall,
                                    color = TextSecondaryDark
                                )
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(24.dp))

                    Text(
                        text = "Silabus & Modul Pembelajaran (${modules.size} Modul)",
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Bold,
                        color = TextPrimaryDark
                    )

                    Spacer(modifier = Modifier.height(12.dp))
                }
            }

            // Modules list
            items(modules.sortedBy { it.order }, key = { it.id }) { module ->
                val isCompleted = completedModuleIds.contains(module.id)
                // Module is unlocked if it is the first or if previous module is completed
                val isUnlocked = module.order == 1 || completedModuleIds.contains(modules.find { it.order == module.order - 1 }?.id)

                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 20.dp, vertical = 6.dp)
                        .clickable(enabled = isUnlocked) { onNavigateToModule(module.id) },
                    shape = RoundedCornerShape(14.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = if (isUnlocked) SurfaceDark else SurfaceVariantDark.copy(alpha = 0.3f)
                    ),
                    border = CardDefaults.outlinedCardBorder().copy(
                        brush = if (isCompleted) Brush.horizontalGradient(listOf(AccentSuccess, AccentSuccess))
                        else if (isUnlocked) Brush.horizontalGradient(listOf(PrimaryPurple, BorderDark))
                        else Brush.horizontalGradient(listOf(BorderDark, BorderDark))
                    )
                ) {
                    Row(
                        modifier = Modifier.padding(14.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(14.dp)
                    ) {
                        Box(
                            modifier = Modifier
                                .size(36.dp)
                                .clip(CircleShape)
                                .background(
                                    if (isCompleted) AccentSuccess.copy(alpha = 0.2f)
                                    else if (isUnlocked) PrimaryPurple.copy(alpha = 0.2f)
                                    else Color.DarkGray.copy(alpha = 0.2f)
                                ),
                            contentAlignment = Alignment.Center
                        ) {
                            if (isCompleted) {
                                Icon(Icons.Filled.Check, contentDescription = "Selesai", tint = AccentSuccess, modifier = Modifier.size(20.dp))
                            } else if (isUnlocked) {
                                Text(
                                    text = "${module.order}",
                                    fontWeight = FontWeight.Bold,
                                    color = SecondaryTeal,
                                    fontSize = 14.sp
                                )
                            } else {
                                Icon(Icons.Filled.Lock, contentDescription = "Terkunci", tint = TextMutedDark, modifier = Modifier.size(18.dp))
                            }
                        }

                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                text = module.title,
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.Bold,
                                color = if (isUnlocked) TextPrimaryDark else TextMutedDark
                            )
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(8.dp)
                            ) {
                                Text(
                                    text = "⏱️ ${module.readingMinutes} mnt baca",
                                    style = MaterialTheme.typography.labelSmall,
                                    color = TextSecondaryDark
                                )

                                if (module.playgroundTask != null) {
                                    Surface(
                                        shape = RoundedCornerShape(4.dp),
                                        color = SecondaryTeal.copy(alpha = 0.15f)
                                    ) {
                                        Text(
                                            text = "⚡ Playground",
                                            style = MaterialTheme.typography.labelSmall,
                                            color = SecondaryTeal,
                                            modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                                        )
                                    }
                                }

                                if (module.hasArAsset) {
                                    Surface(
                                        shape = RoundedCornerShape(4.dp),
                                        color = AccentWarning.copy(alpha = 0.15f)
                                    ) {
                                        Text(
                                            text = "🔬 Visualisasi",
                                            style = MaterialTheme.typography.labelSmall,
                                            color = AccentWarning,
                                            modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                                        )
                                    }
                                }
                            }
                        }

                        Icon(
                            imageVector = Icons.Default.ChevronRight,
                            contentDescription = null,
                            tint = if (isUnlocked) TextSecondaryDark else TextMutedDark
                        )
                    }
                }
            }

            // Final Quiz Card
            item(key = "quiz_card") {
                Spacer(modifier = Modifier.height(12.dp))
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 20.dp)
                        .clickable { onNavigateToQuiz(course.id) },
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = SurfaceDark),
                    border = CardDefaults.outlinedCardBorder().copy(brush = Brush.horizontalGradient(listOf(SecondaryTeal, AccentWarning)))
                ) {
                    Row(
                        modifier = Modifier.padding(16.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(14.dp)
                    ) {
                        Box(
                            modifier = Modifier
                                .size(46.dp)
                                .clip(RoundedCornerShape(12.dp))
                                .background(Brush.linearGradient(listOf(SecondaryTeal, AccentWarning))),
                            contentAlignment = Alignment.Center
                        ) {
                            Text("🎓", fontSize = 24.sp)
                        }
                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                text = "Evaluasi Akhir & Sertifikat",
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.Bold,
                                color = TextPrimaryDark
                            )
                            Text(
                                text = "Lulus Quiz dengan skor ≥ 70% untuk menerbitkan Sertifikat Digital resmi!",
                                style = MaterialTheme.typography.bodySmall,
                                color = TextSecondaryDark
                            )
                        }
                    }
                }
            }
        }
    }
}
