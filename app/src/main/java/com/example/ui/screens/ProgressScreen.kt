package com.example.ui.screens

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
import androidx.compose.material.icons.automirrored.filled.ArrowForwardIos
import androidx.compose.material.icons.automirrored.filled.TrendingUp
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.EmojiEvents
import androidx.compose.material.icons.filled.ExpandLess
import androidx.compose.material.icons.filled.ExpandMore
import androidx.compose.material.icons.filled.FilterList
import androidx.compose.material.icons.filled.LocalFireDepartment
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.MenuBook
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.School
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.WorkspacePremium
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.data.CourseCatalog
import com.example.data.local.CertificateEntity
import com.example.data.local.ModuleProgressEntity
import com.example.data.local.UserProfileEntity
import com.example.data.models.Course
import com.example.data.models.CourseModule
import com.example.data.models.GamificationLevels
import com.example.data.models.LearningTrack
import com.example.ui.MainViewModel
import com.example.ui.components.CoachMarkBubble
import com.example.ui.theme.AmberGold
import com.example.ui.theme.FlameOrange
import com.example.ui.theme.PurpleLevel
import com.example.ui.theme.SuccessGreen
import com.example.ui.theme.TealLight
import com.example.ui.theme.TealPrimary

data class TrackProgressData(
    val track: LearningTrack,
    val totalModules: Int,
    val completedModules: Int,
    val totalCourses: Int,
    val completedCourses: Int,
    val progressFraction: Float,
    val percent: Int
)

data class CompletedCourseProgress(
    val course: Course,
    val track: LearningTrack?,
    val completedModulesCount: Int,
    val totalModulesCount: Int,
    val totalXp: Int
)

data class ActionableTarget(
    val course: Course,
    val track: LearningTrack?,
    val nextModule: CourseModule?,
    val completedCount: Int,
    val totalCount: Int,
    val isInProgress: Boolean,
    val latestTimestamp: Long
)

@Composable
fun ProgressScreen(
    viewModel: MainViewModel,
    onViewCertificates: () -> Unit = {},
    onCourseClick: (String) -> Unit = {},
    onMenuClick: (() -> Unit)? = null,
    modifier: Modifier = Modifier
) {
    val userProfile by viewModel.userProfile.collectAsStateWithLifecycle()
    val progressList by viewModel.moduleProgressList.collectAsStateWithLifecycle()
    val certificates by viewModel.certificates.collectAsStateWithLifecycle()

    ProgressScreen(
        userProfile = userProfile,
        progressList = progressList,
        certificates = certificates,
        onViewCertificates = onViewCertificates,
        onCourseClick = onCourseClick,
        viewModel = viewModel,
        onMenuClick = onMenuClick,
        modifier = modifier
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProgressScreen(
    userProfile: UserProfileEntity?,
    progressList: List<ModuleProgressEntity>,
    certificates: List<CertificateEntity>,
    onViewCertificates: () -> Unit,
    onCourseClick: (String) -> Unit = {},
    viewModel: MainViewModel? = null,
    onMenuClick: (() -> Unit)? = null,
    modifier: Modifier = Modifier
) {
    val totalXp = userProfile?.totalXp ?: 0
    val levelInfo = remember(totalXp) { GamificationLevels.getLevelForXp(totalXp) }
    val xpProgress = remember(totalXp) { GamificationLevels.getLevelProgress(totalXp) }
    val currentStreak = userProfile?.currentStreak ?: 1

    // Precalculate all track statistics
    val allTrackProgress = remember(progressList) {
        val completedKeys = progressList.filter { it.isCompleted }.map { it.courseId to it.moduleId }.toSet()
        CourseCatalog.tracks.map { track ->
            val trackCourses = CourseCatalog.courses.filter { it.trackId == track.id }
            val totalModules = trackCourses.sumOf { it.modules.size }
            val completedModules = trackCourses.sumOf { course ->
                course.modules.count { mod ->
                    (course.id to mod.id) in completedKeys
                }
            }
            val completedCourses = trackCourses.count { course ->
                course.modules.isNotEmpty() && course.modules.all { mod ->
                    (course.id to mod.id) in completedKeys
                }
            }
            val fraction = if (totalModules > 0) completedModules.toFloat() / totalModules.toFloat() else 0f
            val roundedPercent = if (completedModules > 0 && totalModules > 0) {
                kotlin.math.round(fraction * 100f).toInt().coerceIn(1, 100)
            } else 0
            TrackProgressData(
                track = track,
                totalModules = totalModules,
                completedModules = completedModules,
                totalCourses = trackCourses.size,
                completedCourses = completedCourses,
                progressFraction = fraction,
                percent = roundedPercent
            )
        }
    }

    // Precalculate all fully completed courses
    val allCompletedCourses = remember(progressList) {
        val completedKeys = progressList.filter { it.isCompleted }.map { it.courseId to it.moduleId }.toSet()
        CourseCatalog.courses.mapNotNull { course ->
            val completedCount = course.modules.count { mod -> (course.id to mod.id) in completedKeys }
            if (course.modules.isNotEmpty() && completedCount == course.modules.size) {
                val track = CourseCatalog.tracks.find { it.id == course.trackId }
                CompletedCourseProgress(
                    course = course,
                    track = track,
                    completedModulesCount = completedCount,
                    totalModulesCount = course.modules.size,
                    totalXp = course.modules.size * 20
                )
            } else null
        }
    }

    // Rekomendasi modul / kelas berikutnya berdasarkan progres belajar terakhir
    val nextLearningTarget = remember(progressList) {
        val completedKeys = progressList.filter { it.isCompleted }.map { it.courseId to it.moduleId }.toSet()
        val inProgressList = CourseCatalog.courses.mapNotNull { course ->
            val completedCount = course.modules.count { (course.id to it.id) in completedKeys }
            if (completedCount in 1 until course.modules.size) {
                val latestTime = progressList.filter { it.courseId == course.id && it.isCompleted }
                    .maxOfOrNull { it.completedAt } ?: 0L
                val nextModule = course.modules.firstOrNull { (course.id to it.id) !in completedKeys }
                val track = CourseCatalog.tracks.find { it.id == course.trackId }
                ActionableTarget(
                    course = course,
                    track = track,
                    nextModule = nextModule,
                    completedCount = completedCount,
                    totalCount = course.modules.size,
                    isInProgress = true,
                    latestTimestamp = latestTime
                )
            } else null
        }.sortedByDescending { it.latestTimestamp }

        inProgressList.firstOrNull() ?: run {
            val nextCourse = CourseCatalog.courses.firstOrNull { course ->
                val completedCount = course.modules.count { (course.id to it.id) in completedKeys }
                completedCount < course.modules.size
            } ?: CourseCatalog.courses.first()

            val nextModule = nextCourse.modules.firstOrNull { (nextCourse.id to it.id) !in completedKeys }
                ?: nextCourse.modules.firstOrNull()
            val completedCount = nextCourse.modules.count { (nextCourse.id to it.id) in completedKeys }
            val track = CourseCatalog.tracks.find { it.id == nextCourse.trackId }

            ActionableTarget(
                course = nextCourse,
                track = track,
                nextModule = nextModule,
                completedCount = completedCount,
                totalCount = nextCourse.modules.size,
                isInProgress = completedCount > 0,
                latestTimestamp = 0L
            )
        }
    }

    val activeTracksCount = remember(allTrackProgress) {
        allTrackProgress.count { it.percent in 1..99 }
    }
    val completedTracksCount = remember(allTrackProgress) {
        allTrackProgress.count { it.percent >= 100 }
    }
    val leadingTrack = remember(allTrackProgress) {
        allTrackProgress.maxByOrNull { it.percent }
    }

    var showDetailTrackSheet by remember { mutableStateOf(false) }
    val sheetState = rememberModalBottomSheetState()
    var selectedFilterTab by rememberSaveable { mutableIntStateOf(0) } // 0: Sedang Dipelajari, 1: Selesai, 2: Semua Jalur

    // Pagination & Category filter state for Completed tab
    var completedCategoryFilter by rememberSaveable { mutableStateOf<String?>(null) } // null = Semua
    var completedDisplayLimit by rememberSaveable { mutableIntStateOf(4) } // Default N items
    var isGroupedByCategory by rememberSaveable { mutableStateOf(false) }

    // Recent activity expansion on main screen
    var showAllRecentActivity by rememberSaveable { mutableStateOf(false) }

    // Detail Progres Jalur & Kelas Bottom Sheet
    if (showDetailTrackSheet) {
        ModalBottomSheet(
            onDismissRequest = { showDetailTrackSheet = false },
            sheetState = sheetState,
            containerColor = MaterialTheme.colorScheme.surface
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp, vertical = 12.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = androidx.compose.ui.res.stringResource(com.example.R.string.prog_detail_title),
                            style = MaterialTheme.typography.titleLarge,
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            text = androidx.compose.ui.res.stringResource(com.example.R.string.prog_detail_subtitle),
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }

                    IconButton(
                        onClick = { showDetailTrackSheet = false },
                        modifier = Modifier.testTag("btn_close_track_sheet")
                    ) {
                        Icon(imageVector = Icons.Default.Close, contentDescription = androidx.compose.ui.res.stringResource(com.example.R.string.prog_close))
                    }
                }

                Spacer(modifier = Modifier.height(14.dp))

                // Unified Proportional Segmented Tabs: Sedang Dipelajari | Selesai | Semua Jalur
                Surface(
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("segmented_track_tabs"),
                    shape = RoundedCornerShape(12.dp),
                    color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f)
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(4.dp),
                        horizontalArrangement = Arrangement.spacedBy(4.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        val activeTabCount = if (activeTracksCount > 0) activeTracksCount else 1
                        val completedTabCount = if (completedTracksCount > 0) completedTracksCount else allCompletedCourses.size
                        val totalTracksCount = allTrackProgress.size

                        val tabItems = listOf(
                            Triple(0, "Sedang Dipelajari ($activeTabCount)", "tab_in_progress"),
                            Triple(1, "Selesai ($completedTabCount)", "tab_completed"),
                            Triple(2, "Semua Jalur ($totalTracksCount)", "tab_all_tracks")
                        )

                        tabItems.forEach { (index, tabLabel, tag) ->
                            val isSelected = selectedFilterTab == index
                            Surface(
                                onClick = {
                                    selectedFilterTab = index
                                    completedDisplayLimit = 4
                                },
                                modifier = Modifier
                                    .weight(1f)
                                    .fillMaxWidth()
                                    .height(38.dp)
                                    .testTag(tag),
                                color = if (isSelected) TealPrimary else Color.Transparent,
                                shape = RoundedCornerShape(8.dp)
                            ) {
                                Box(
                                    modifier = Modifier
                                        .fillMaxSize()
                                        .padding(horizontal = 4.dp),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Text(
                                        text = tabLabel,
                                        fontSize = 11.sp,
                                        fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
                                        color = if (isSelected) Color.White else MaterialTheme.colorScheme.onSurfaceVariant,
                                        maxLines = 1,
                                        overflow = TextOverflow.Ellipsis,
                                        textAlign = TextAlign.Center
                                    )
                                }
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.height(14.dp))

                when (selectedFilterTab) {
                    // TAB 0: Sedang Dipelajari
                    0 -> {
                        val activeTracks = remember(allTrackProgress) {
                            val list = allTrackProgress.filter { it.percent in 1..99 }
                            if (list.isEmpty()) listOfNotNull(allTrackProgress.firstOrNull()) else list
                        }

                        LazyColumn(
                            verticalArrangement = Arrangement.spacedBy(12.dp),
                            contentPadding = PaddingValues(bottom = 32.dp)
                        ) {
                            items(activeTracks, key = { it.track.id }) { item ->
                                TrackProgressCard(
                                    item = item,
                                    onViewCertificates = {
                                        showDetailTrackSheet = false
                                        onViewCertificates()
                                    },
                                    onStartTrack = { trackId ->
                                        showDetailTrackSheet = false
                                        val course = CourseCatalog.courses.find { it.trackId == trackId }
                                        if (course != null) onCourseClick(course.id)
                                    }
                                )
                            }
                        }
                    }

                    // TAB 1: Selesai (With Controlled Pagination, Category Filter & Grouping)
                    1 -> {
                        val completedTracks = remember(allTrackProgress) {
                            allTrackProgress.filter { it.percent >= 100 }
                        }

                        val filteredCompletedCourses = remember(allCompletedCourses, completedCategoryFilter) {
                            if (completedCategoryFilter == null) {
                                allCompletedCourses
                            } else {
                                allCompletedCourses.filter { it.course.trackId == completedCategoryFilter }
                            }
                        }

                        val visibleCompletedCourses = remember(filteredCompletedCourses, completedDisplayLimit) {
                            filteredCompletedCourses.take(completedDisplayLimit)
                        }

                        val groupedByTrack = remember(visibleCompletedCourses) {
                            visibleCompletedCourses.groupBy { it.track?.name ?: "Lainnya" }
                        }

                        val hasMore = filteredCompletedCourses.size > visibleCompletedCourses.size

                        if (completedTracks.isEmpty() && allCompletedCourses.isEmpty()) {
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(vertical = 36.dp),
                                contentAlignment = Alignment.Center
                            ) {
                                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                    Icon(
                                        imageVector = Icons.Default.School,
                                        contentDescription = null,
                                        tint = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.6f),
                                        modifier = Modifier.size(44.dp)
                                    )
                                    Spacer(modifier = Modifier.height(10.dp))
                                    Text(
                                        text = "Belum Ada Kelas yang Selesai",
                                        style = MaterialTheme.typography.titleMedium,
                                        fontWeight = FontWeight.Bold
                                    )
                                    Text(
                                        text = "Riwayat kelas dan sertifikatmu akan muncul di sini.",
                                        style = MaterialTheme.typography.bodySmall,
                                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                                        modifier = Modifier.padding(horizontal = 24.dp, vertical = 6.dp),
                                        textAlign = TextAlign.Center
                                    )
                                    Spacer(modifier = Modifier.height(12.dp))
                                    Button(
                                        onClick = {
                                            showDetailTrackSheet = false
                                            onCourseClick("fe-web-foundation-00")
                                        },
                                        colors = ButtonDefaults.buttonColors(containerColor = TealPrimary),
                                        shape = RoundedCornerShape(10.dp)
                                    ) {
                                        Icon(imageVector = Icons.Default.PlayArrow, contentDescription = null, tint = Color.White, modifier = Modifier.size(16.dp))
                                        Spacer(modifier = Modifier.width(6.dp))
                                        Text("Mulai Belajar Sekarang", color = Color.White, fontWeight = FontWeight.Bold, fontSize = 12.sp)
                                    }
                                }
                            }
                        } else {
                            LazyColumn(
                                verticalArrangement = Arrangement.spacedBy(12.dp),
                                contentPadding = PaddingValues(bottom = 32.dp)
                            ) {
                                // 1. Ringkasan & Kontrol Tampilan
                                item(key = "completed_summary_header") {
                                    Card(
                                        modifier = Modifier.fillMaxWidth(),
                                        shape = RoundedCornerShape(14.dp),
                                        colors = CardDefaults.cardColors(containerColor = TealPrimary.copy(alpha = 0.08f)),
                                        border = BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.5f)),
                                        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
                                    ) {
                                        Row(
                                            modifier = Modifier
                                                .fillMaxWidth()
                                                .padding(horizontal = 14.dp, vertical = 10.dp),
                                            horizontalArrangement = Arrangement.SpaceBetween,
                                            verticalAlignment = Alignment.CenterVertically
                                        ) {
                                            Row(verticalAlignment = Alignment.CenterVertically) {
                                                Icon(
                                                    imageVector = Icons.Default.CheckCircle,
                                                    contentDescription = null,
                                                    tint = SuccessGreen,
                                                    modifier = Modifier.size(18.dp)
                                                )
                                                Spacer(modifier = Modifier.width(8.dp))
                                                Text(
                                                    text = "${allCompletedCourses.size} Kelas Selesai • ${completedTracks.size} Jalur Tuntas",
                                                    style = MaterialTheme.typography.bodySmall,
                                                    fontWeight = FontWeight.Bold,
                                                    color = MaterialTheme.colorScheme.onSurface
                                                )
                                            }

                                            Surface(
                                                color = if (isGroupedByCategory) TealPrimary else MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.6f),
                                                shape = RoundedCornerShape(6.dp),
                                                modifier = Modifier.clickable { isGroupedByCategory = !isGroupedByCategory }
                                            ) {
                                                Row(
                                                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                                                    verticalAlignment = Alignment.CenterVertically
                                                ) {
                                                    Icon(
                                                        imageVector = Icons.Default.FilterList,
                                                        contentDescription = null,
                                                        tint = if (isGroupedByCategory) Color.White else MaterialTheme.colorScheme.onSurfaceVariant,
                                                        modifier = Modifier.size(12.dp)
                                                    )
                                                    Spacer(modifier = Modifier.width(4.dp))
                                                    Text(
                                                        text = if (isGroupedByCategory) "Grup Aktif" else "Grup Jalur",
                                                        fontSize = 10.sp,
                                                        fontWeight = FontWeight.Bold,
                                                        color = if (isGroupedByCategory) Color.White else MaterialTheme.colorScheme.onSurfaceVariant
                                                    )
                                                }
                                            }
                                        }
                                    }
                                }

                                // 2. Category / Track Chips Filter
                                if (allCompletedCourses.size > 2) {
                                    item(key = "completed_category_filter_chips") {
                                        LazyRow(
                                            horizontalArrangement = Arrangement.spacedBy(6.dp),
                                            contentPadding = PaddingValues(vertical = 2.dp)
                                        ) {
                                            item(key = "filter_all") {
                                                FilterChip(
                                                    selected = completedCategoryFilter == null,
                                                    onClick = {
                                                        completedCategoryFilter = null
                                                        completedDisplayLimit = 4
                                                    },
                                                    label = { Text("Semua (${allCompletedCourses.size})", fontSize = 11.sp) },
                                                    colors = FilterChipDefaults.filterChipColors(
                                                        selectedContainerColor = TealPrimary,
                                                        selectedLabelColor = Color.White
                                                    ),
                                                    modifier = Modifier.height(30.dp)
                                                )
                                            }

                                            val availableTracks = allCompletedCourses.mapNotNull { it.track }.distinctBy { it.id }
                                            items(availableTracks, key = { "filter_${it.id}" }) { track ->
                                                val countInTrack = allCompletedCourses.count { it.course.trackId == track.id }
                                                FilterChip(
                                                    selected = completedCategoryFilter == track.id,
                                                    onClick = {
                                                        completedCategoryFilter = if (completedCategoryFilter == track.id) null else track.id
                                                        completedDisplayLimit = 4
                                                    },
                                                    label = { Text("${track.iconEmoji} ${com.example.util.localization.DataLabelMappings.trackName(track)} ($countInTrack)", fontSize = 11.sp) },
                                                    colors = FilterChipDefaults.filterChipColors(
                                                        selectedContainerColor = TealPrimary,
                                                        selectedLabelColor = Color.White
                                                    ),
                                                    modifier = Modifier.height(30.dp)
                                                )
                                            }
                                        }
                                    }
                                }

                                // 3. Jalur 100% Selesai (Jika ada)
                                if (completedTracks.isNotEmpty() && completedCategoryFilter == null) {
                                    item(key = "completed_tracks_header") {
                                        Text(
                                            text = "Jalur Kelulusan Penuh (100%)",
                                            style = MaterialTheme.typography.titleSmall,
                                            fontWeight = FontWeight.Bold,
                                            color = SuccessGreen
                                        )
                                    }

                                    items(completedTracks, key = { "track_comp_${it.track.id}" }) { item ->
                                        TrackProgressCard(
                                            item = item,
                                            onViewCertificates = {
                                                showDetailTrackSheet = false
                                                onViewCertificates()
                                            },
                                            onStartTrack = { }
                                        )
                                    }
                                }

                                // 4. Daftar Kelas Selesai
                                if (filteredCompletedCourses.isNotEmpty()) {
                                    item(key = "completed_courses_section_header") {
                                        Row(
                                            modifier = Modifier.fillMaxWidth(),
                                            horizontalArrangement = Arrangement.SpaceBetween,
                                            verticalAlignment = Alignment.CenterVertically
                                        ) {
                                            Text(
                                                text = "Daftar Kelas Selesai",
                                                style = MaterialTheme.typography.titleSmall,
                                                fontWeight = FontWeight.Bold,
                                                color = MaterialTheme.colorScheme.onBackground
                                            )
                                            Text(
                                                text = "Menampilkan ${visibleCompletedCourses.size} dari ${filteredCompletedCourses.size}",
                                                style = MaterialTheme.typography.labelSmall,
                                                color = MaterialTheme.colorScheme.onSurfaceVariant
                                            )
                                        }
                                    }

                                    if (isGroupedByCategory) {
                                        groupedByTrack.forEach { (trackName, coursesInGroup) ->
                                            item(key = "group_header_$trackName") {
                                                Surface(
                                                    color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.4f),
                                                    shape = RoundedCornerShape(8.dp),
                                                    modifier = Modifier.fillMaxWidth()
                                                ) {
                                                    Row(
                                                        modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp),
                                                        verticalAlignment = Alignment.CenterVertically,
                                                        horizontalArrangement = Arrangement.SpaceBetween
                                                    ) {
                                                        Text(
                                                            text = trackName,
                                                            style = MaterialTheme.typography.labelMedium,
                                                            fontWeight = FontWeight.Bold,
                                                            color = MaterialTheme.colorScheme.primary
                                                        )
                                                        Text(
                                                            text = "${coursesInGroup.size} Kelas",
                                                            style = MaterialTheme.typography.labelSmall,
                                                            color = MaterialTheme.colorScheme.onSurfaceVariant
                                                        )
                                                    }
                                                }
                                            }

                                            items(coursesInGroup, key = { "comp_crs_${it.course.id}" }) { item ->
                                                CompletedCourseCard(
                                                    item = item,
                                                    onCourseClick = {
                                                        showDetailTrackSheet = false
                                                        onCourseClick(item.course.id)
                                                    }
                                                )
                                            }
                                        }
                                    } else {
                                        items(visibleCompletedCourses, key = { "comp_crs_${it.course.id}" }) { item ->
                                            CompletedCourseCard(
                                                item = item,
                                                onCourseClick = {
                                                    showDetailTrackSheet = false
                                                    onCourseClick(item.course.id)
                                                }
                                            )
                                        }
                                    }

                                    // 5. Pagination / Load More Controls
                                    item(key = "completed_pagination_controls") {
                                        Row(
                                            modifier = Modifier
                                                .fillMaxWidth()
                                                .padding(top = 4.dp),
                                            horizontalArrangement = Arrangement.spacedBy(8.dp),
                                            verticalAlignment = Alignment.CenterVertically
                                        ) {
                                            if (hasMore) {
                                                Button(
                                                    onClick = {
                                                        completedDisplayLimit += 4
                                                    },
                                                    colors = ButtonDefaults.buttonColors(containerColor = TealPrimary),
                                                    shape = RoundedCornerShape(10.dp),
                                                    modifier = Modifier
                                                        .weight(1f)
                                                        .height(38.dp)
                                                        .testTag("btn_load_more_completed")
                                                ) {
                                                    Icon(
                                                        imageVector = Icons.Default.ExpandMore,
                                                        contentDescription = null,
                                                        tint = Color.White,
                                                        modifier = Modifier.size(16.dp)
                                                    )
                                                    Spacer(modifier = Modifier.width(4.dp))
                                                    Text(
                                                        text = "Muat Lebih Banyak (+4)",
                                                        color = Color.White,
                                                        fontWeight = FontWeight.Bold,
                                                        fontSize = 12.sp
                                                    )
                                                }

                                                OutlinedButton(
                                                    onClick = {
                                                        completedDisplayLimit = filteredCompletedCourses.size
                                                    },
                                                    shape = RoundedCornerShape(10.dp),
                                                    modifier = Modifier
                                                        .height(38.dp)
                                                        .testTag("btn_show_all_completed")
                                                ) {
                                                    Text(
                                                        text = "Lihat Semua (${filteredCompletedCourses.size})",
                                                        fontSize = 12.sp,
                                                        fontWeight = FontWeight.SemiBold,
                                                        color = MaterialTheme.colorScheme.primary
                                                    )
                                                }
                                            } else if (filteredCompletedCourses.size > 4) {
                                                OutlinedButton(
                                                    onClick = {
                                                        completedDisplayLimit = 4
                                                    },
                                                    shape = RoundedCornerShape(10.dp),
                                                    modifier = Modifier
                                                        .fillMaxWidth()
                                                        .height(38.dp)
                                                        .testTag("btn_collapse_completed")
                                                ) {
                                                    Icon(
                                                        imageVector = Icons.Default.ExpandLess,
                                                        contentDescription = null,
                                                        tint = MaterialTheme.colorScheme.primary,
                                                        modifier = Modifier.size(16.dp)
                                                    )
                                                    Spacer(modifier = Modifier.width(4.dp))
                                                    Text(
                                                        text = "Tampilkan Lebih Sedikit",
                                                        fontSize = 12.sp,
                                                        fontWeight = FontWeight.SemiBold,
                                                        color = MaterialTheme.colorScheme.primary
                                                    )
                                                }
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }

                    // TAB 2: Semua Jalur
                    2 -> {
                        LazyColumn(
                            verticalArrangement = Arrangement.spacedBy(12.dp),
                            contentPadding = PaddingValues(bottom = 32.dp)
                        ) {
                            items(allTrackProgress, key = { it.track.id }) { item ->
                                TrackProgressCard(
                                    item = item,
                                    onViewCertificates = {
                                        showDetailTrackSheet = false
                                        onViewCertificates()
                                    },
                                    onStartTrack = { trackId ->
                                        showDetailTrackSheet = false
                                        val course = CourseCatalog.courses.find { it.trackId == trackId }
                                        if (course != null) onCourseClick(course.id)
                                    }
                                )
                            }
                        }
                    }
                }
            }
        }
    }

    // Calculate recent completed modules for the home activity feed
    val recentProgress = remember(progressList, showAllRecentActivity) {
        if (showAllRecentActivity) progressList else progressList.take(5)
    }

    val listState = rememberLazyListState()

    LazyColumn(
        state = listState,
        modifier = modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp),
        contentPadding = PaddingValues(top = 8.dp, bottom = 96.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // Title
        item(key = "title") {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth()
            ) {
                if (onMenuClick != null) {
                    IconButton(
                        onClick = onMenuClick,
                        modifier = Modifier
                            .testTag("btn_menu_progress")
                            .size(40.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Menu,
                            contentDescription = "Menu Navigasi",
                            tint = MaterialTheme.colorScheme.onBackground
                        )
                    }
                    Spacer(modifier = Modifier.width(6.dp))
                }
                Text(
                    text = androidx.compose.ui.res.stringResource(com.example.R.string.prog_title),
                    style = MaterialTheme.typography.headlineSmall,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onBackground
                )
            }
        }

        if (viewModel != null && userProfile != null && !viewModel.hasSeenCoachMark("progress_intro")) {
            item(key = "coach_mark_progress") {
                CoachMarkBubble(
                    message = androidx.compose.ui.res.stringResource(com.example.R.string.prog_coachmark),
                    onDismiss = { viewModel.markCoachMarkSeen("progress_intro") }
                )
            }
        }

        // Summary Card: Level, XP, Streak
        item(key = "level_card") {
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.surface
                ),
                border = BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.5f)),
                elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
            ) {
                Column(modifier = Modifier.padding(18.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Surface(
                                color = PurpleLevel,
                                shape = RoundedCornerShape(10.dp)
                            ) {
                                Text(
                                    text = "Lv. ${levelInfo.level}",
                                    color = Color.White,
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 14.sp,
                                    modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp)
                                )
                            }
                            Spacer(modifier = Modifier.width(10.dp))
                            Column {
                                Text(
                                    text = com.example.util.localization.DataLabelMappings.xpLevelName(levelInfo.level, levelInfo.name),
                                    style = MaterialTheme.typography.titleMedium,
                                    fontWeight = FontWeight.Bold,
                                    color = MaterialTheme.colorScheme.onSurface
                                )
                                Text(
                                    text = androidx.compose.ui.res.stringResource(com.example.R.string.prog_current_title_label),
                                    style = MaterialTheme.typography.bodySmall,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                            }
                        }

                        // Total XP
                        Surface(
                            color = AmberGold.copy(alpha = 0.15f),
                            shape = RoundedCornerShape(10.dp)
                        ) {
                            Row(
                                modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Star,
                                    contentDescription = null,
                                    tint = AmberGold,
                                    modifier = Modifier.size(16.dp)
                                )
                                Spacer(modifier = Modifier.width(4.dp))
                                Text(
                                    text = "$totalXp XP",
                                    fontWeight = FontWeight.Bold,
                                    color = AmberGold,
                                    fontSize = 13.sp
                                )
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    // Progress to next level
                    Column {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text(
                                text = androidx.compose.ui.res.stringResource(
                                    com.example.R.string.prog_to_next_level_format,
                                    levelInfo.level + 1
                                ),
                                style = MaterialTheme.typography.labelSmall,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                            Text(
                                text = "${(xpProgress * 100).toInt()}%",
                                style = MaterialTheme.typography.labelSmall,
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.primary
                            )
                        }
                        Spacer(modifier = Modifier.height(6.dp))
                        LinearProgressIndicator(
                            progress = { xpProgress },
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(8.dp)
                                .clip(RoundedCornerShape(4.dp)),
                            color = AmberGold,
                            trackColor = MaterialTheme.colorScheme.surfaceVariant,
                            drawStopIndicator = {}
                        )
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    // Quick stats: Streak & Certificates
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        Surface(
                            modifier = Modifier
                                .weight(1f)
                                .clip(RoundedCornerShape(12.dp)),
                            color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f)
                        ) {
                            Row(
                                modifier = Modifier.padding(12.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Icon(
                                    imageVector = Icons.Default.LocalFireDepartment,
                                    contentDescription = null,
                                    tint = FlameOrange,
                                    modifier = Modifier.size(24.dp)
                                )
                                Spacer(modifier = Modifier.width(8.dp))
                                Column {
                                    Text(
                                        text = androidx.compose.ui.res.stringResource(com.example.R.string.prog_streak_days_format, currentStreak),
                                        fontWeight = FontWeight.Bold,
                                        style = MaterialTheme.typography.titleSmall
                                    )
                                    Text(
                                        text = androidx.compose.ui.res.stringResource(com.example.R.string.prog_streak_active_label),
                                        style = MaterialTheme.typography.labelSmall,
                                        color = MaterialTheme.colorScheme.onSurfaceVariant
                                    )
                                }
                            }
                        }

                        Surface(
                            modifier = Modifier
                                .weight(1f)
                                .clip(RoundedCornerShape(12.dp))
                                .clickable { onViewCertificates() },
                            color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f)
                        ) {
                            Row(
                                modifier = Modifier.padding(12.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Icon(
                                    imageVector = Icons.Default.WorkspacePremium,
                                    contentDescription = null,
                                    tint = AmberGold,
                                    modifier = Modifier.size(24.dp)
                                )
                                Spacer(modifier = Modifier.width(8.dp))
                                Column {
                                    Text(
                                        text = androidx.compose.ui.res.stringResource(com.example.R.string.prog_cert_count_format, certificates.size),
                                        fontWeight = FontWeight.Bold,
                                        style = MaterialTheme.typography.titleSmall
                                    )
                                    Text(
                                        text = androidx.compose.ui.res.stringResource(com.example.R.string.prog_view_graduation),
                                        style = MaterialTheme.typography.labelSmall,
                                        color = MaterialTheme.colorScheme.primary
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }

        // Rekomendasi Target Belajar & CTA Lanjutkan Kelas (Menggantikan Duplikasi Leaderboard)
        item(key = "actionable_learning_target_card") {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { onCourseClick(nextLearningTarget.course.id) }
                    .testTag("card_actionable_learning_cta"),
                shape = RoundedCornerShape(18.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                elevation = CardDefaults.cardElevation(defaultElevation = 0.dp),
                border = BorderStroke(1.dp, MaterialTheme.colorScheme.primary.copy(alpha = 0.25f))
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(18.dp)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Box(
                                modifier = Modifier
                                    .size(42.dp)
                                    .clip(RoundedCornerShape(12.dp))
                                    .background(MaterialTheme.colorScheme.primary.copy(alpha = 0.15f)),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(
                                    imageVector = if (nextLearningTarget.isInProgress) Icons.Default.PlayArrow else Icons.Default.Star,
                                    contentDescription = null,
                                    tint = MaterialTheme.colorScheme.primary,
                                    modifier = Modifier.size(24.dp)
                                )
                            }
                            Spacer(modifier = Modifier.width(12.dp))
                            Column {
                                Text(
                                    text = if (nextLearningTarget.isInProgress) {
                                        androidx.compose.ui.res.stringResource(com.example.R.string.prog_continue_learning)
                                    } else {
                                        androidx.compose.ui.res.stringResource(com.example.R.string.prog_next_target_title)
                                    },
                                    style = MaterialTheme.typography.titleSmall,
                                    fontWeight = FontWeight.Bold,
                                    color = MaterialTheme.colorScheme.onSurface
                                )
                                Text(
                                    text = nextLearningTarget.track?.name ?: "CodeAcademy",
                                    style = MaterialTheme.typography.labelSmall,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                            }
                        }

                        Surface(
                            shape = RoundedCornerShape(6.dp),
                            color = if (nextLearningTarget.isInProgress) {
                                TealLight.copy(alpha = 0.18f)
                            } else {
                                AmberGold.copy(alpha = 0.18f)
                            }
                        ) {
                            Text(
                                text = if (nextLearningTarget.isInProgress) {
                                    val donePct = if (nextLearningTarget.completedCount > 0 && nextLearningTarget.totalCount > 0) {
                                        kotlin.math.round(nextLearningTarget.completedCount.toFloat() / nextLearningTarget.totalCount * 100f)
                                            .toInt()
                                            .coerceIn(1, 100)
                                    } else 0
                                    androidx.compose.ui.res.stringResource(
                                        com.example.R.string.prog_percent_done_format,
                                        donePct
                                    )
                                } else {
                                    androidx.compose.ui.res.stringResource(com.example.R.string.prog_start_now)
                                },
                                style = MaterialTheme.typography.labelSmall,
                                color = if (nextLearningTarget.isInProgress) MaterialTheme.colorScheme.primary else AmberGold,
                                fontWeight = FontWeight.Bold,
                                modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(14.dp))

                    Text(
                        text = nextLearningTarget.course.title,
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onSurface,
                        maxLines = 2,
                        overflow = TextOverflow.Ellipsis
                    )

                    if (nextLearningTarget.nextModule != null) {
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = "Modul ${nextLearningTarget.nextModule.order}: ${nextLearningTarget.nextModule.title} (${nextLearningTarget.nextModule.readingMinutes} mnt)",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    val progressFraction = if (nextLearningTarget.totalCount > 0) {
                        nextLearningTarget.completedCount.toFloat() / nextLearningTarget.totalCount.toFloat()
                    } else 0f

                    LinearProgressIndicator(
                        progress = { progressFraction },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(6.dp)
                            .clip(RoundedCornerShape(3.dp)),
                        color = MaterialTheme.colorScheme.primary,
                        trackColor = MaterialTheme.colorScheme.surfaceVariant,
                        drawStopIndicator = {}
                    )

                    Spacer(modifier = Modifier.height(14.dp))

                    Button(
                        onClick = { onCourseClick(nextLearningTarget.course.id) },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(44.dp)
                            .testTag("btn_continue_learning_progress"),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = MaterialTheme.colorScheme.primary
                        ),
                        shape = RoundedCornerShape(10.dp)
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.Center
                        ) {
                            Text(
                                text = if (nextLearningTarget.isInProgress) {
                                    androidx.compose.ui.res.stringResource(com.example.R.string.prog_continue_module)
                                } else {
                                    androidx.compose.ui.res.stringResource(com.example.R.string.prog_open_course)
                                },
                                fontWeight = FontWeight.SemiBold,
                                style = MaterialTheme.typography.labelLarge
                            )
                        }
                    }
                }
            }
        }

        // Ringkas "Progres per Jalur Belajar" Jadi SATU Entry Point Card
        item(key = "track_summary_entry") {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { showDetailTrackSheet = true }
                    .testTag("card_track_progress_entry"),
                shape = RoundedCornerShape(18.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                border = BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.5f)),
                elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
            ) {
                Column(modifier = Modifier.padding(18.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Box(
                                modifier = Modifier
                                    .size(44.dp)
                                    .clip(RoundedCornerShape(12.dp))
                                    .background(TealPrimary.copy(alpha = 0.15f)),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(
                                    imageVector = Icons.AutoMirrored.Filled.TrendingUp,
                                    contentDescription = null,
                                    tint = MaterialTheme.colorScheme.primary,
                                    modifier = Modifier.size(24.dp)
                                )
                            }

                            Spacer(modifier = Modifier.width(12.dp))

                            Column {
                                Text(
                                    text = androidx.compose.ui.res.stringResource(com.example.R.string.prog_by_track_title),
                                    style = MaterialTheme.typography.titleMedium,
                                    fontWeight = FontWeight.Bold,
                                    color = MaterialTheme.colorScheme.onSurface
                                )
                                Text(
                                    text = androidx.compose.ui.res.stringResource(
                                        com.example.R.string.prog_track_active_done_format,
                                        if (activeTracksCount > 0) activeTracksCount else 1,
                                        completedTracksCount
                                    ),
                                    style = MaterialTheme.typography.bodySmall,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                            }
                        }

                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowForwardIos,
                            contentDescription = "Buka Detail",
                            tint = MaterialTheme.colorScheme.primary,
                            modifier = Modifier.size(16.dp)
                        )
                    }

                    Spacer(modifier = Modifier.height(14.dp))

                    // Lead Track Info Box
                    Surface(
                        color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f),
                        shape = RoundedCornerShape(12.dp),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Row(
                            modifier = Modifier.padding(horizontal = 14.dp, vertical = 10.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Text(text = leadingTrack?.track?.iconEmoji ?: "🌐", fontSize = 18.sp)
                                Spacer(modifier = Modifier.width(8.dp))
                                Text(
                                    text = androidx.compose.ui.res.stringResource(
                                        com.example.R.string.prog_active_track_format,
                                        leadingTrack?.track?.name ?: "Front-End Development"
                                    ),
                                    style = MaterialTheme.typography.bodySmall,
                                    fontWeight = FontWeight.SemiBold
                                )
                            }

                            Text(
                                text = "${leadingTrack?.percent ?: 16}%",
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.primary,
                                fontSize = 13.sp
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(10.dp))

                    Text(
                        text = androidx.compose.ui.res.stringResource(com.example.R.string.prog_view_curriculum_detail),
                        style = MaterialTheme.typography.labelSmall,
                        color = MaterialTheme.colorScheme.primary,
                        fontWeight = FontWeight.SemiBold
                    )
                }
            }
        }

        // Section: Aktivitas Belajar Terbaru
        item(key = "section_recent_activity") {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = androidx.compose.ui.res.stringResource(com.example.R.string.prog_recent_activity_title),
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onBackground
                )
                Text(
                    text = androidx.compose.ui.res.stringResource(com.example.R.string.prog_modules_done_count_format, progressList.size),
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }

        if (progressList.isEmpty()) {
            item(key = "empty_activity") {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                    border = BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.5f)),
                    elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(24.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        com.example.ui.components.CodeAcademyMascot(
                            size = 82.dp,
                            mood = com.example.ui.components.MascotMood.CODING
                        )
                        Spacer(modifier = Modifier.height(10.dp))
                        Text(
                            text = androidx.compose.ui.res.stringResource(com.example.R.string.prog_no_modules_title),
                            fontWeight = FontWeight.Bold,
                            style = MaterialTheme.typography.bodyMedium
                        )
                        Text(
                            text = androidx.compose.ui.res.stringResource(com.example.R.string.prog_no_modules_desc),
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                            textAlign = TextAlign.Center
                        )
                    }
                }
            }
        } else {
            items(recentProgress, key = { "${it.courseId}_${it.moduleId}" }) { item ->
                val course = CourseCatalog.getCourseById(item.courseId) ?: CourseCatalog.courses.find { it.id == item.courseId }
                val module = course?.modules?.find { it.id == item.moduleId }

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    // Vertical timeline node & connector
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        modifier = Modifier.padding(end = 10.dp)
                    ) {
                        Box(
                            modifier = Modifier
                                .width(2.dp)
                                .height(10.dp)
                                .background(SuccessGreen.copy(alpha = 0.35f))
                        )
                        Box(
                            modifier = Modifier
                                .size(14.dp)
                                .clip(CircleShape)
                                .background(SuccessGreen)
                        )
                        Box(
                            modifier = Modifier
                                .width(2.dp)
                                .height(10.dp)
                                .background(SuccessGreen.copy(alpha = 0.35f))
                        )
                    }

                    Card(
                        modifier = Modifier
                            .weight(1f)
                            .clickable {
                                onCourseClick(if (item.courseId == "fe-html-01") "fe-web-foundation-00" else item.courseId)
                            },
                        shape = RoundedCornerShape(14.dp),
                        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                        border = BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.5f)),
                        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
                    ) {
                        Row(
                            modifier = Modifier.padding(14.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(38.dp)
                                    .clip(CircleShape)
                                    .background(SuccessGreen.copy(alpha = 0.15f)),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(
                                    imageVector = Icons.Default.CheckCircle,
                                    contentDescription = null,
                                    tint = SuccessGreen,
                                    modifier = Modifier.size(20.dp)
                                )
                            }

                            Spacer(modifier = Modifier.width(12.dp))

                            Column(modifier = Modifier.weight(1f)) {
                                Text(
                                    text = module?.title ?: "Coding Module",
                                    style = MaterialTheme.typography.bodyMedium,
                                    fontWeight = FontWeight.Bold,
                                    color = MaterialTheme.colorScheme.onSurface,
                                    maxLines = 1,
                                    overflow = TextOverflow.Ellipsis
                                )
                                Text(
                                    text = course?.title ?: "CodeAcademy",
                                    style = MaterialTheme.typography.bodySmall,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                                    maxLines = 1,
                                    overflow = TextOverflow.Ellipsis
                                )
                            }

                            Surface(
                                color = AmberGold.copy(alpha = 0.15f),
                                shape = RoundedCornerShape(6.dp)
                            ) {
                                Text(
                                    text = "+20 XP",
                                    color = AmberGold,
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 11.sp,
                                    modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                                )
                            }
                        }
                    }
                }
            }

            if (progressList.size > 5) {
                item(key = "toggle_recent_activity_view") {
                    OutlinedButton(
                        onClick = { showAllRecentActivity = !showAllRecentActivity },
                        shape = RoundedCornerShape(10.dp),
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(38.dp)
                    ) {
                        Icon(
                            imageVector = if (showAllRecentActivity) Icons.Default.ExpandLess else Icons.Default.ExpandMore,
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.primary,
                            modifier = Modifier.size(16.dp)
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            text = if (showAllRecentActivity) "Tampilkan 5 Aktivitas Terbaru" else "Lihat Semua (${progressList.size} Modul)",
                            fontSize = 12.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = MaterialTheme.colorScheme.primary
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun TrackProgressCard(
    item: TrackProgressData,
    onViewCertificates: () -> Unit,
    onStartTrack: (String) -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f)
        ),
        border = BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.5f)),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(text = item.track.iconEmoji, fontSize = 24.sp)
                    Spacer(modifier = Modifier.width(10.dp))
                    Column {
                        Text(
                            text = com.example.util.localization.DataLabelMappings.trackName(item.track),
                            style = MaterialTheme.typography.titleSmall,
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            text = "${item.completedCourses}/${item.totalCourses} Kelas • ${item.completedModules}/${item.totalModules} Modul",
                            style = MaterialTheme.typography.labelSmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }

                Surface(
                    color = if (item.percent >= 100) SuccessGreen.copy(alpha = 0.2f) else TealLight.copy(alpha = 0.15f),
                    shape = RoundedCornerShape(8.dp)
                ) {
                    Row(
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = if (item.percent >= 100) "Selesai" else "${item.percent}%",
                            color = if (item.percent >= 100) SuccessGreen else MaterialTheme.colorScheme.primary,
                            fontWeight = FontWeight.Bold,
                            fontSize = 12.sp
                        )
                        if (item.percent >= 100) {
                            Spacer(modifier = Modifier.width(4.dp))
                            Icon(
                                imageVector = Icons.Default.Check,
                                contentDescription = null,
                                tint = SuccessGreen,
                                modifier = Modifier.size(14.dp)
                            )
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            LinearProgressIndicator(
                progress = { item.progressFraction },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(8.dp)
                    .clip(RoundedCornerShape(4.dp)),
                color = if (item.percent >= 100) SuccessGreen else TealPrimary,
                trackColor = MaterialTheme.colorScheme.surface,
                drawStopIndicator = {}
            )

            Spacer(modifier = Modifier.height(14.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.End
            ) {
                if (item.percent >= 100) {
                    Button(
                        onClick = onViewCertificates,
                        colors = ButtonDefaults.buttonColors(containerColor = AmberGold),
                        shape = RoundedCornerShape(10.dp)
                    ) {
                        Icon(imageVector = Icons.Default.WorkspacePremium, contentDescription = null, tint = Color.Black, modifier = Modifier.size(16.dp))
                        Spacer(modifier = Modifier.width(6.dp))
                        Text("Lihat Sertifikat", color = Color.Black, fontWeight = FontWeight.Bold, fontSize = 12.sp)
                    }
                } else {
                    Button(
                        onClick = { onStartTrack(item.track.id) },
                        colors = ButtonDefaults.buttonColors(containerColor = TealPrimary),
                        shape = RoundedCornerShape(10.dp)
                    ) {
                        Icon(imageVector = Icons.Default.PlayArrow, contentDescription = null, tint = Color.White, modifier = Modifier.size(16.dp))
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(if (item.percent > 0) "Lanjutkan Belajar" else "Mulai Jalur Ini", color = Color.White, fontWeight = FontWeight.Bold, fontSize = 12.sp)
                    }
                }
            }
        }
    }
}

@Composable
private fun CompletedCourseCard(
    item: CompletedCourseProgress,
    onCourseClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onCourseClick() },
        shape = RoundedCornerShape(14.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        ),
        border = BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.5f)),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(14.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.weight(1f)
            ) {
                Box(
                    modifier = Modifier
                        .size(40.dp)
                        .clip(CircleShape)
                        .background(SuccessGreen.copy(alpha = 0.15f)),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = item.track?.iconEmoji ?: "📘",
                        fontSize = 20.sp
                    )
                }

                Spacer(modifier = Modifier.width(12.dp))

                Column {
                    Text(
                        text = item.course.title,
                        style = MaterialTheme.typography.titleSmall,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onSurface,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                    Spacer(modifier = Modifier.height(2.dp))
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Surface(
                            color = SuccessGreen.copy(alpha = 0.15f),
                            shape = RoundedCornerShape(4.dp)
                        ) {
                            Text(
                                text = "Selesai (${item.totalModulesCount} Modul)",
                                color = SuccessGreen,
                                fontSize = 10.sp,
                                fontWeight = FontWeight.Bold,
                                modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                            )
                        }
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = com.example.util.localization.DataLabelMappings.courseLevelLabel(item.course.level),
                            style = MaterialTheme.typography.labelSmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.width(8.dp))

            Surface(
                color = AmberGold.copy(alpha = 0.15f),
                shape = RoundedCornerShape(6.dp)
            ) {
                Text(
                    text = "+${item.totalXp} XP",
                    color = AmberGold,
                    fontWeight = FontWeight.Bold,
                    fontSize = 11.sp,
                    modifier = Modifier.padding(horizontal = 7.dp, vertical = 3.dp)
                )
            }
        }
    }
}
