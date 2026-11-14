package com.example.ui.screens

import androidx.compose.animation.AnimatedVisibility
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
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Bookmark
import androidx.compose.material.icons.filled.BookmarkBorder
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.People
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Schedule
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.TabRowDefaults
import androidx.compose.material3.TabRowDefaults.tabIndicatorOffset
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.R
import com.example.data.local.BookmarkEntity
import com.example.data.local.ModuleProgressEntity
import com.example.data.models.Course
import com.example.data.models.LearningTrack
import com.example.ui.MainViewModel
import com.example.ui.components.CourseLevelBadge
import com.example.ui.components.NeutralInfoBadge
import com.example.ui.theme.AmberGold
import com.example.ui.theme.ChipLanjutBg
import com.example.ui.theme.ChipLanjutText
import com.example.ui.theme.ChipMenengahBg
import com.example.ui.theme.ChipMenengahText
import com.example.ui.theme.ChipNeutralBg
import com.example.ui.theme.ChipNeutralText
import com.example.ui.theme.ChipOrientasiBg
import com.example.ui.theme.ChipOrientasiText
import com.example.ui.theme.ChipPemulaBg
import com.example.ui.theme.ChipPemulaText
import com.example.ui.theme.CyanAccent
import com.example.ui.theme.SuccessGreen
import com.example.ui.theme.TealDark
import com.example.ui.theme.TealPrimary

@Composable
fun CourseCatalogScreen(
    viewModel: MainViewModel,
    onCourseClick: (String) -> Unit,
    onMenuClick: (() -> Unit)? = null,
    modifier: Modifier = Modifier
) {
    val searchKeyword by viewModel.searchKeyword.collectAsStateWithLifecycle()
    val selectedLevel by viewModel.selectedLevelFilter.collectAsStateWithLifecycle()
    val selectedCategory by viewModel.selectedCategoryFilter.collectAsStateWithLifecycle()
    val selectedTab by viewModel.selectedTabFilter.collectAsStateWithLifecycle()
    val progressList by viewModel.moduleProgressList.collectAsStateWithLifecycle()
    val bookmarks by viewModel.bookmarks.collectAsStateWithLifecycle()
    val filteredCourses by viewModel.filteredCourses.collectAsStateWithLifecycle()
    val courses = remember { viewModel.getAllCourses() }
    val tracks = remember { viewModel.getAllTracks() }
    val context = androidx.compose.ui.platform.LocalContext.current

    CourseCatalogScreen(
        courses = courses,
        tracks = tracks,
        progressList = progressList,
        bookmarks = bookmarks,
        preFilteredCourses = filteredCourses,
        searchQuery = searchKeyword,
        onSearchQueryChange = { viewModel.setSearchKeyword(it) },
        selectedTrackId = selectedCategory,
        onSelectTrackId = { viewModel.setSelectedCategoryFilter(it) },
        selectedLevel = selectedLevel,
        onSelectLevel = { viewModel.setSelectedLevelFilter(it ?: "Semua") },
        selectedTab = selectedTab,
        onSelectTab = { viewModel.setSelectedTabFilter(it) },
        onCourseClick = onCourseClick,
        onMenuClick = onMenuClick,
        onToggleBookmark = { course ->
            viewModel.toggleBookmark(
                type = "COURSE",
                itemId = course.id,
                title = course.title,
                subtitle = context.getString(R.string.course_bookmark_subtitle_format, course.modules.size, com.example.util.localization.DataLabelMappings.courseLevelLabel(course.level, context)),
                trackName = course.trackId
            )
        },
        modifier = modifier
    )
}

@Composable
fun CourseCatalogScreen(
    courses: List<Course>,
    tracks: List<LearningTrack>,
    progressList: List<ModuleProgressEntity>,
    bookmarks: List<BookmarkEntity>,
    onCourseClick: (String) -> Unit,
    onToggleBookmark: (Course) -> Unit,
    preFilteredCourses: List<Course>? = null,
    searchQuery: String = "",
    onSearchQueryChange: ((String) -> Unit)? = null,
    selectedTrackId: String? = null,
    onSelectTrackId: ((String?) -> Unit)? = null,
    selectedLevel: String? = null,
    onSelectLevel: ((String?) -> Unit)? = null,
    selectedTab: Int = 0,
    onSelectTab: ((Int) -> Unit)? = null,
    onMenuClick: (() -> Unit)? = null,
    modifier: Modifier = Modifier
) {
    var localSearchQuery by rememberSaveable { mutableStateOf("") }
    var localSelectedTrackId by rememberSaveable { mutableStateOf<String?>(null) }
    var localSelectedLevel by rememberSaveable { mutableStateOf<String?>(null) }
    var localSelectedTab by rememberSaveable { mutableIntStateOf(0) }

    val currentSearchQuery = if (onSearchQueryChange != null) searchQuery else localSearchQuery
    val currentSelectedTrackId = if (onSelectTrackId != null) selectedTrackId else localSelectedTrackId
    val currentSelectedLevel = if (onSelectLevel != null) selectedLevel else localSelectedLevel
    val currentSelectedTab = if (onSelectTab != null) selectedTab else localSelectedTab
    val listState = rememberLazyListState()

    val levels = listOf("Semua", "Orientasi", "Pemula", "Menengah", "Lanjut")

    // Cached Set of completed course modules for O(1) lookups during filtering and list item rendering
    val completedKeys = remember(progressList) {
        progressList.filter { it.isCompleted }.map { it.courseId to it.moduleId }.toSet()
    }
    val bookmarkedCourseIds = remember(bookmarks) {
        bookmarks.filter { it.type == "CLASS" || it.type == "COURSE" }.map { it.itemId }.toSet()
    }

    // Filter courses based on search, track, level, and tab with derivedStateOf to minimize recompositions
    val filteredCourses by remember(
        preFilteredCourses, courses, currentSearchQuery, currentSelectedTrackId, currentSelectedLevel, currentSelectedTab, completedKeys
    ) {
        derivedStateOf {
            preFilteredCourses ?: courses.filter { course ->
                val matchesQuery = currentSearchQuery.isBlank() ||
                        course.title.contains(currentSearchQuery, ignoreCase = true) ||
                        course.description.contains(currentSearchQuery, ignoreCase = true)

                val matchesTrack = currentSelectedTrackId == null || course.trackId == currentSelectedTrackId
                val matchesLevel = currentSelectedLevel == null || currentSelectedLevel == "Semua" || course.level == currentSelectedLevel

                // Progress check using cached O(1) set
                val completedCount = course.modules.count { mod ->
                    (course.id to mod.id) in completedKeys
                }
                val isCompleted = completedCount > 0 && completedCount == course.modules.size
                val isStarted = completedCount > 0 && !isCompleted

                val matchesTab = when (currentSelectedTab) {
                    1 -> isStarted // Sedang Dipelajari
                    2 -> isCompleted // Selesai
                    else -> true // Semua
                }

                matchesQuery && matchesTrack && matchesLevel && matchesTab
            }
        }
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp)
    ) {
        // Title
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.padding(top = 8.dp, bottom = 12.dp)
        ) {
            if (onMenuClick != null) {
                IconButton(
                    onClick = onMenuClick,
                    modifier = Modifier
                        .testTag("btn_menu_courses")
                        .size(40.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.Menu,
                        contentDescription = stringResource(R.string.catalog_nav_menu_cd),
                        tint = MaterialTheme.colorScheme.onBackground
                    )
                }
                Spacer(modifier = Modifier.width(6.dp))
            }
            Text(
                text = stringResource(R.string.catalog_explore_title),
                style = MaterialTheme.typography.headlineSmall,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onBackground
            )
        }

        // Search Bar
        OutlinedTextField(
            value = currentSearchQuery,
            onValueChange = { onSearchQueryChange?.invoke(it) ?: run { localSearchQuery = it } },
            placeholder = { Text(stringResource(R.string.catalog_search_placeholder), fontSize = 14.sp) },
            leadingIcon = {
                Icon(
                    imageVector = Icons.Default.Search,
                    contentDescription = stringResource(R.string.catalog_search_cd),
                    tint = MaterialTheme.colorScheme.onSurfaceVariant
                )
            },
            trailingIcon = {
                if (currentSearchQuery.isNotEmpty()) {
                    IconButton(onClick = { onSearchQueryChange?.invoke("") ?: run { localSearchQuery = "" } }) {
                        Icon(
                            imageVector = Icons.Default.Clear,
                            contentDescription = stringResource(R.string.catalog_clear_search_cd),
                            tint = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }
            },
            singleLine = true,
            shape = RoundedCornerShape(16.dp),
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = MaterialTheme.colorScheme.primary,
                unfocusedBorderColor = MaterialTheme.colorScheme.surfaceVariant,
                focusedContainerColor = MaterialTheme.colorScheme.surface,
                unfocusedContainerColor = MaterialTheme.colorScheme.surface
            ),
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(12.dp))

        // Tabs: Semua, Sedang Dipelajari, Selesai
        TabRow(
            selectedTabIndex = currentSelectedTab,
            containerColor = Color.Transparent,
            contentColor = MaterialTheme.colorScheme.primary,
            indicator = { tabPositions ->
                TabRowDefaults.SecondaryIndicator(
                    modifier = Modifier.tabIndicatorOffset(tabPositions[currentSelectedTab]),
                    color = MaterialTheme.colorScheme.primary
                )
            }
        ) {
            listOf(
                stringResource(R.string.catalog_filter_all),
                stringResource(R.string.catalog_filter_in_progress),
                stringResource(R.string.catalog_filter_completed)
            ).forEachIndexed { index, tabTitle ->
                Tab(
                    selected = currentSelectedTab == index,
                    onClick = { onSelectTab?.invoke(index) ?: run { localSelectedTab = index } },
                    text = {
                        Text(
                            text = tabTitle,
                            fontWeight = if (currentSelectedTab == index) FontWeight.Bold else FontWeight.Normal,
                            color = if (currentSelectedTab == index) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurfaceVariant,
                            fontSize = 13.sp
                        )
                    }
                )
            }
        }

        Spacer(modifier = Modifier.height(12.dp))

        val visibleTracks = remember(tracks) { tracks.filter { it.totalCourses > 0 } }

        // Track Horizontal Filter Chips (only shown when multiple tracks exist)
        if (visibleTracks.size > 1) {
            LazyRow(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                contentPadding = PaddingValues(bottom = 6.dp)
            ) {
                item(key = "track_all") {
                    FilterChip(
                        selected = currentSelectedTrackId == null,
                        onClick = { onSelectTrackId?.invoke(null) ?: run { localSelectedTrackId = null } },
                        label = { Text(stringResource(R.string.catalog_all_tracks), fontSize = 12.sp) },
                        colors = FilterChipDefaults.filterChipColors(
                            selectedContainerColor = TealPrimary,
                            selectedLabelColor = Color.White
                        )
                    )
                }
                items(visibleTracks, key = { it.id }) { track ->
                    FilterChip(
                        selected = currentSelectedTrackId == track.id,
                        onClick = {
                            val newTrack = if (currentSelectedTrackId == track.id) null else track.id
                            onSelectTrackId?.invoke(newTrack) ?: run { localSelectedTrackId = newTrack }
                        },
                        label = { Text("${track.iconEmoji} ${com.example.util.localization.DataLabelMappings.trackName(track)}", fontSize = 12.sp) },
                        colors = FilterChipDefaults.filterChipColors(
                            selectedContainerColor = TealPrimary,
                            selectedLabelColor = Color.White
                        )
                    )
                }
            }
        }

        // Level Filter Chips
        LazyRow(
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            contentPadding = PaddingValues(bottom = 10.dp)
        ) {
            items(levels, key = { it }) { lvl ->
                val isSelected = (currentSelectedLevel == null && lvl == "Semua") || currentSelectedLevel == lvl
                val (chipBg, chipText) = when (lvl) {
                    "Orientasi" -> ChipOrientasiBg to ChipOrientasiText
                    "Pemula" -> ChipPemulaBg to ChipPemulaText
                    "Menengah" -> ChipMenengahBg to ChipMenengahText
                    "Lanjut" -> ChipLanjutBg to ChipLanjutText
                    else -> TealPrimary to Color.White
                }
                FilterChip(
                    selected = isSelected,
                    onClick = {
                        val newLvl = if (lvl == "Semua") null else lvl
                        onSelectLevel?.invoke(newLvl) ?: run { localSelectedLevel = newLvl }
                    },
                    label = { Text(com.example.util.localization.DataLabelMappings.courseLevelLabel(lvl), fontSize = 11.sp, fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal) },
                    colors = FilterChipDefaults.filterChipColors(
                        selectedContainerColor = chipBg,
                        selectedLabelColor = chipText,
                        containerColor = ChipNeutralBg,
                        labelColor = ChipNeutralText
                    )
                )
            }
        }

        // Course List
        if (filteredCourses.isEmpty()) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f),
                contentAlignment = Alignment.Center
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Icon(
                        imageVector = Icons.Default.Search,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.6f),
                        modifier = Modifier.size(44.dp)
                    )
                    Spacer(modifier = Modifier.height(12.dp))
                    Text(
                        text = stringResource(R.string.catalog_empty_title),
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                    Text(
                        text = stringResource(R.string.catalog_empty_subtitle),
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }
        } else {
            LazyColumn(
                state = listState,
                verticalArrangement = Arrangement.spacedBy(14.dp),
                contentPadding = PaddingValues(bottom = 90.dp),
                modifier = Modifier.weight(1f)
            ) {
                items(filteredCourses, key = { it.id }) { course ->
                    val isBookmarked = course.id in bookmarkedCourseIds
                    val completedCount = course.modules.count { mod ->
                        (course.id to mod.id) in completedKeys
                    }
                    val isCompleted = completedCount > 0 && completedCount == course.modules.size
                    val isStarted = completedCount > 0 && !isCompleted

                    CourseCardItem(
                        course = course,
                        isBookmarked = isBookmarked,
                        completedModules = completedCount,
                        isCompleted = isCompleted,
                        isStarted = isStarted,
                        onCourseClick = { onCourseClick(course.id) },
                        onToggleBookmark = { onToggleBookmark(course) }
                    )
                }
            }
        }
    }
}

@Composable
fun CourseCardItem(
    course: Course,
    isBookmarked: Boolean,
    completedModules: Int,
    isCompleted: Boolean,
    isStarted: Boolean,
    onCourseClick: () -> Unit,
    onToggleBookmark: () -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .clickable { onCourseClick() },
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        ),
        border = BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.5f)),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.Top
            ) {
                // Track & Level Badges
                Row(verticalAlignment = Alignment.CenterVertically) {
                    CourseLevelBadge(level = course.level)

                    Spacer(modifier = Modifier.width(6.dp))

                    if (isCompleted) {
                        Surface(
                            color = ChipPemulaBg,
                            shape = RoundedCornerShape(6.dp)
                        ) {
                            Row(
                                modifier = Modifier.padding(horizontal = 8.dp, vertical = 3.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(
                                    text = stringResource(R.string.catalog_filter_completed),
                                    color = ChipPemulaText,
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.Bold
                                )
                                Spacer(modifier = Modifier.width(4.dp))
                                Icon(
                                    imageVector = Icons.Default.Check,
                                    contentDescription = null,
                                    tint = ChipPemulaText,
                                    modifier = Modifier.size(14.dp)
                                )
                            }
                        }
                    } else if (isStarted) {
                        NeutralInfoBadge(text = stringResource(R.string.catalog_progress_modules_format, completedModules, course.modules.size))
                    }
                }

                // Bookmark icon
                IconButton(
                    onClick = onToggleBookmark,
                    modifier = Modifier.size(28.dp)
                ) {
                    Icon(
                        imageVector = if (isBookmarked) Icons.Default.Bookmark else Icons.Default.BookmarkBorder,
                        contentDescription = stringResource(R.string.catalog_bookmark_course_cd),
                        tint = if (isBookmarked) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            // Title & Description
            Text(
                text = course.title,
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onSurface
            )

            Spacer(modifier = Modifier.height(4.dp))

            Text(
                text = com.example.util.localization.DataLabelMappings.courseDescription(course),
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                maxLines = 2
            )

            Spacer(modifier = Modifier.height(12.dp))

            // Metadata row: Rating, Duration, Students
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    if (course.rating > 0f) {
                        Icon(
                            imageVector = Icons.Default.Star,
                            contentDescription = stringResource(R.string.course_detail_rating_cd),
                            tint = AmberGold,
                            modifier = Modifier.size(16.dp)
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            text = "${course.rating}",
                            style = MaterialTheme.typography.bodySmall,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                        Spacer(modifier = Modifier.width(12.dp))
                    }
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.Default.Schedule,
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.onSurfaceVariant,
                            modifier = Modifier.size(14.dp)
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            text = stringResource(R.string.course_detail_duration_min_format, course.durationMinutes),
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                    if (course.studentsCount > 0) {
                        Spacer(modifier = Modifier.width(12.dp))
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                imageVector = Icons.Default.People,
                                contentDescription = null,
                                tint = MaterialTheme.colorScheme.onSurfaceVariant,
                                modifier = Modifier.size(14.dp)
                            )
                            Spacer(modifier = Modifier.width(4.dp))
                            Text(
                                text = "${course.studentsCount}",
                                style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                    }
                }
            }
        }
    }
}
