package com.example.ui.screens

import android.widget.Toast
import androidx.compose.animation.Crossfade
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.AssignmentTurnedIn
import androidx.compose.material.icons.filled.Bolt
import androidx.compose.material.icons.filled.Bookmark
import androidx.compose.material.icons.filled.BookmarkBorder
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Code
import androidx.compose.material.icons.filled.EditNote
import androidx.compose.material.icons.filled.Forum
import androidx.compose.material.icons.filled.HourglassTop
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.MenuBook
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.PlayCircle
import androidx.compose.material.icons.filled.Quiz
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.WorkspacePremium
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.R
import com.example.data.local.BookmarkEntity
import com.example.data.local.ModuleProgressEntity
import com.example.data.local.ProjectSubmissionEntity
import com.example.data.models.Course
import com.example.data.models.CourseModule
import com.example.data.models.ModuleSection
import com.example.data.models.ProjectSubmissionStatus
import com.example.data.models.SectionQuiz
import com.example.data.util.CourseSectionUtils
import com.example.data.util.CourseUnlockUtils
import com.example.data.util.ModuleLockState
import com.example.ui.components.CourseLevelBadge
import com.example.ui.components.MarkdownText
import com.example.ui.theme.AmberGold
import com.example.ui.theme.CyanAccent
import com.example.ui.theme.FlameOrange
import com.example.ui.theme.SuccessGreen
import com.example.ui.theme.TealLight
import com.example.ui.theme.TealPrimary
import com.example.util.localization.DataLabelMappings

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ClassDetailScreen(
    course: Course,
    progressList: List<ModuleProgressEntity>,
    bookmarks: List<BookmarkEntity>,
    passedQuizIds: Set<String> = emptySet(),
    projectSubmission: ProjectSubmissionEntity? = null,
    hasCertificate: Boolean = false,
    onBack: () -> Unit,
    onModuleClick: (String) -> Unit,
    onStartQuiz: () -> Unit,
    onToggleBookmark: () -> Unit,
    onOpenForum: () -> Unit = {},
    onStartSectionQuiz: (String) -> Unit = {},
    onOpenCheatSheet: () -> Unit = {},
    onStartPlacementTest: () -> Unit = {},
    onOpenFinalProject: () -> Unit = {},
    onViewCertificate: () -> Unit = {},
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val completedModuleIds = remember(progressList, course.id) {
        progressList.filter { it.courseId == course.id && it.isCompleted }.map { it.moduleId }.toSet()
    }
    val completedCount = remember(completedModuleIds, course.modules) {
        course.modules.count { it.id in completedModuleIds }
    }
    val progressFraction = remember(completedCount, course.modules) {
        if (course.modules.isNotEmpty()) {
            completedCount.toFloat() / course.modules.size.toFloat()
        } else 0f
    }

    val isBookmarked = remember(bookmarks, course.id) {
        bookmarks.any { it.itemId == course.id && (it.type == "CLASS" || it.type == "COURSE") }
    }

    val sectionsWithModules = remember(course.modules) {
        ModuleSection.values()
            .map { section ->
                val mods = course.modules.filter { it.section == section }.sortedBy { it.order }
                section to mods
            }
            .filter { (_, mods) -> mods.isNotEmpty() }
    }
    val sortedModules = remember(course.modules) {
        course.modules.sortedBy { it.order }
    }

    // First uncompleted module or first module
    val nextModule = remember(completedModuleIds, course.modules) {
        course.modules.firstOrNull { it.id !in completedModuleIds } ?: course.modules.firstOrNull()
    }

    val submissionStatus = remember(projectSubmission?.status) {
        runCatching {
            ProjectSubmissionStatus.valueOf(projectSubmission?.status ?: "LOCKED")
        }.getOrDefault(ProjectSubmissionStatus.LOCKED)
    }

    val isFinalQuizUnlocked = remember(course, completedModuleIds, passedQuizIds) {
        CourseUnlockUtils.isFinalQuizUnlocked(course, completedModuleIds, passedQuizIds)
    }
    val finalQuizBestScore = projectSubmission?.finalQuizScore ?: 0
    val isFinalQuizPassed = finalQuizBestScore >= 70 || submissionStatus != ProjectSubmissionStatus.LOCKED
    val isFinalProjectUnlocked = remember(course, completedModuleIds, passedQuizIds, finalQuizBestScore, submissionStatus) {
        CourseUnlockUtils.isFinalProjectUnlocked(
            course = course,
            completedModuleIds = completedModuleIds,
            passedQuizIds = passedQuizIds,
            finalQuizPassed = isFinalQuizPassed
        ) || submissionStatus != ProjectSubmissionStatus.LOCKED
    }
    val isCertReady = hasCertificate || CourseUnlockUtils.isCertificateUnlocked(
        finalQuizPassed = isFinalQuizPassed,
        submissionStatus = submissionStatus
    )

    val toastCompleteModulesFirst = stringResource(R.string.class_detail_toast_complete_modules_first)
    val toastPassQuizFirst = stringResource(R.string.final_project_card_locked_hint)

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = stringResource(R.string.class_detail_title),
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold
                    )
                },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = stringResource(R.string.class_detail_back_cd)
                        )
                    }
                },
                actions = {
                    if (!course.cheatSheetMarkdown.isNullOrBlank()) {
                        IconButton(
                            onClick = onOpenCheatSheet,
                            modifier = Modifier.testTag("class_topbar_cheatsheet_button")
                        ) {
                            Icon(
                                imageVector = Icons.Default.MenuBook,
                                contentDescription = stringResource(R.string.class_detail_cheatsheet_cd),
                                tint = MaterialTheme.colorScheme.primary
                            )
                        }
                    }
                    IconButton(
                        onClick = onOpenForum,
                        modifier = Modifier.testTag("class_topbar_forum_button")
                    ) {
                        Icon(
                            imageVector = Icons.Default.Forum,
                            contentDescription = stringResource(R.string.class_detail_forum_cd),
                            tint = MaterialTheme.colorScheme.primary
                        )
                    }
                    IconButton(onClick = onToggleBookmark) {
                        Icon(
                            imageVector = if (isBookmarked) Icons.Default.Bookmark else Icons.Default.BookmarkBorder,
                            contentDescription = stringResource(R.string.class_detail_bookmark_cd),
                            tint = if (isBookmarked) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurface
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.surface
                )
            )
        },
        bottomBar = {
            Surface(
                color = MaterialTheme.colorScheme.surface,
                shadowElevation = 8.dp,
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .navigationBarsPadding()
                        .padding(16.dp),
                    horizontalArrangement = Arrangement.spacedBy(12.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = if (completedCount == course.modules.size) {
                                stringResource(R.string.class_detail_completed_label)
                            } else {
                                stringResource(R.string.class_detail_modules_progress_format, completedCount, course.modules.size)
                            },
                            style = MaterialTheme.typography.labelSmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                        Text(
                            text = stringResource(R.string.class_detail_percent_completed_format, (progressFraction * 100).toInt()),
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold,
                            color = if (completedCount == course.modules.size) SuccessGreen else MaterialTheme.colorScheme.primary
                        )
                    }

                    com.example.ui.components.PrimaryButton(
                        text = if (completedCount == 0) {
                            stringResource(R.string.home_start_learning_button)
                        } else {
                            stringResource(R.string.home_continue_button)
                        },
                        onClick = {
                            if (nextModule != null) {
                                onModuleClick(nextModule.id)
                            }
                        },
                        icon = Icons.Default.PlayArrow
                    )
                }
            }
        },
        modifier = modifier
    ) { innerPadding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(horizontal = 16.dp),
            contentPadding = PaddingValues(bottom = 24.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Course Header Card
            item(key = "course_header_card") {
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
                        // Level and Rating
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            CourseLevelBadge(level = course.level, fontSize = 12.sp)

                            if (course.rating > 0f || course.studentsCount > 0) {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    if (course.rating > 0f) {
                                        Icon(
                                            imageVector = Icons.Default.Star,
                                            contentDescription = null,
                                            tint = AmberGold,
                                            modifier = Modifier.size(18.dp)
                                        )
                                        Spacer(modifier = Modifier.width(4.dp))
                                        Text(
                                            text = "${course.rating}",
                                            fontWeight = FontWeight.Bold,
                                            style = MaterialTheme.typography.bodyMedium
                                        )
                                    }
                                    if (course.studentsCount > 0) {
                                        if (course.rating > 0f) {
                                            Spacer(modifier = Modifier.width(8.dp))
                                        }
                                        Text(
                                            text = stringResource(R.string.class_detail_students_count_format, course.studentsCount),
                                            style = MaterialTheme.typography.bodySmall,
                                            color = MaterialTheme.colorScheme.onSurfaceVariant
                                        )
                                    }
                                }
                            }
                        }

                        Spacer(modifier = Modifier.height(14.dp))

                        // Title
                        Text(
                            text = DataLabelMappings.courseTitle(course),
                            style = MaterialTheme.typography.headlineSmall,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.onSurface
                        )

                        Spacer(modifier = Modifier.height(8.dp))

                        MarkdownText(
                            text = DataLabelMappings.courseDescription(course),
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                            lineHeight = 22.sp
                        )

                        Spacer(modifier = Modifier.height(16.dp))

                        // Progress bar
                        Column {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Text(
                                    text = stringResource(R.string.class_detail_learning_progress_label),
                                    style = MaterialTheme.typography.labelSmall,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                                Text(
                                    text = stringResource(
                                        R.string.class_detail_modules_fraction_percent_format,
                                        completedCount,
                                        course.modules.size,
                                        (progressFraction * 100).toInt()
                                    ),
                                    style = MaterialTheme.typography.labelSmall,
                                    fontWeight = FontWeight.Bold,
                                    color = MaterialTheme.colorScheme.primary
                                )
                            }
                            Spacer(modifier = Modifier.height(6.dp))
                            LinearProgressIndicator(
                                progress = { progressFraction },
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(8.dp)
                                    .clip(RoundedCornerShape(4.dp)),
                                color = MaterialTheme.colorScheme.primary,
                                trackColor = MaterialTheme.colorScheme.surfaceVariant
                            )
                        }

                        Spacer(modifier = Modifier.height(16.dp))

                        // Instructor & Prerequisites
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier
                                .fillMaxWidth()
                                .clip(RoundedCornerShape(12.dp))
                                .background(MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f))
                                .padding(12.dp)
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(40.dp)
                                    .clip(CircleShape)
                                    .background(CyanAccent.copy(alpha = 0.2f)),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    text = course.instructor.take(1),
                                    fontWeight = FontWeight.Bold,
                                    color = MaterialTheme.colorScheme.primary,
                                    style = MaterialTheme.typography.titleMedium
                                )
                            }

                            Spacer(modifier = Modifier.width(12.dp))

                            Column {
                                Text(
                                    text = stringResource(R.string.class_detail_instructor_label),
                                    style = MaterialTheme.typography.labelSmall,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                                Text(
                                    text = course.instructor,
                                    style = MaterialTheme.typography.bodySmall,
                                    fontWeight = FontWeight.SemiBold,
                                    color = MaterialTheme.colorScheme.onSurface
                                )
                            }
                        }
                    }
                }
            }

            // Placement Test (Fast-Track) Banner (Percontohan untuk fe-web-foundation-00)
            if (completedCount == 0 && course.id == "fe-web-foundation-00" && course.sectionQuizzes.isNotEmpty()) {
                item(key = "placement_test_banner") {
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable(onClick = onStartPlacementTest)
                            .testTag("class_placement_test_banner"),
                        shape = RoundedCornerShape(16.dp),
                        colors = CardDefaults.cardColors(
                            containerColor = MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.45f)
                        ),
                        border = BorderStroke(1.5.dp, MaterialTheme.colorScheme.primary.copy(alpha = 0.4f)),
                        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(16.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(44.dp)
                                    .clip(CircleShape)
                                    .background(MaterialTheme.colorScheme.primary),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Bolt,
                                    contentDescription = null,
                                    tint = Color.White,
                                    modifier = Modifier.size(24.dp)
                                )
                            }

                            Spacer(modifier = Modifier.width(14.dp))

                            Column(modifier = Modifier.weight(1f)) {
                                Text(
                                    text = stringResource(R.string.class_detail_placement_title),
                                    style = MaterialTheme.typography.titleSmall,
                                    fontWeight = FontWeight.Bold,
                                    color = MaterialTheme.colorScheme.onSurface
                                )
                                Spacer(modifier = Modifier.height(2.dp))
                                Text(
                                    text = stringResource(R.string.class_detail_placement_desc),
                                    style = MaterialTheme.typography.bodySmall,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                            }

                            Spacer(modifier = Modifier.width(8.dp))

                            Icon(
                                imageVector = Icons.AutoMirrored.Filled.ArrowForward,
                                contentDescription = stringResource(R.string.class_detail_placement_start_cd),
                                tint = MaterialTheme.colorScheme.primary,
                                modifier = Modifier.size(20.dp)
                            )
                        }
                    }
                }
            }

            // Forum Diskusi Kelas Banner
            item(key = "forum_discussion_banner") {
                Card(
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.primary.copy(alpha = 0.15f)
                    ),
                    border = BorderStroke(1.dp, MaterialTheme.colorScheme.primary.copy(alpha = 0.3f)),
                    elevation = CardDefaults.cardElevation(defaultElevation = 0.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(16.dp))
                        .clickable { onOpenForum() }
                        .testTag("class_forum_banner_button")
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.padding(16.dp)
                    ) {
                        Box(
                            contentAlignment = Alignment.Center,
                            modifier = Modifier
                                .size(44.dp)
                                .clip(RoundedCornerShape(12.dp))
                                .background(TealLight.copy(alpha = 0.2f))
                        ) {
                            Icon(
                                imageVector = Icons.Default.Forum,
                                contentDescription = null,
                                tint = MaterialTheme.colorScheme.primary,
                                modifier = Modifier.size(24.dp)
                            )
                        }

                        Spacer(modifier = Modifier.width(14.dp))

                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                text = stringResource(R.string.class_detail_forum_title),
                                style = MaterialTheme.typography.titleSmall,
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.primary
                            )
                            Text(
                                text = stringResource(R.string.class_detail_forum_desc),
                                style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }

                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowForward,
                            contentDescription = stringResource(R.string.class_detail_forum_open_cd),
                            tint = MaterialTheme.colorScheme.primary,
                            modifier = Modifier.size(20.dp)
                        )
                    }
                }
            }

            // Dynamic Course Sections
            sectionsWithModules.forEachIndexed { sIndex, (section, modulesInSection) ->
                if (sIndex > 0) {
                    item(key = "divider_before_${section.name.lowercase()}") {
                        HorizontalDivider(
                            modifier = Modifier.padding(vertical = 4.dp),
                            color = MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.6f),
                            thickness = 1.dp
                        )
                    }
                }

                item(key = "section_${section.name.lowercase()}_header") {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column(modifier = Modifier.weight(1f, fill = false)) {
                            Text(
                                text = CourseSectionUtils.localizedSectionTitle(course, section),
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.onBackground
                            )
                            Text(
                                text = CourseSectionUtils.localizedSectionSubtitle(course, section),
                                style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                        Spacer(modifier = Modifier.width(8.dp))
                        if (section == ModuleSection.PERSIAPAN) {
                            Surface(
                                shape = RoundedCornerShape(6.dp),
                                color = TealLight.copy(alpha = 0.15f)
                            ) {
                                Text(
                                    text = stringResource(R.string.class_detail_pill_prep),
                                    style = MaterialTheme.typography.labelSmall,
                                    color = MaterialTheme.colorScheme.primary,
                                    fontWeight = FontWeight.Bold,
                                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 3.dp)
                                )
                            }
                        } else if (section == ModuleSection.PENDAHULUAN) {
                            Surface(
                                shape = RoundedCornerShape(6.dp),
                                color = TealLight.copy(alpha = 0.15f)
                            ) {
                                Text(
                                    text = stringResource(R.string.class_detail_pill_intro),
                                    style = MaterialTheme.typography.labelSmall,
                                    color = MaterialTheme.colorScheme.primary,
                                    fontWeight = FontWeight.Bold,
                                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 3.dp)
                                )
                            }
                        } else {
                            val durationMin = if (course.id == "fe-web-foundation-00" && section == ModuleSection.INTI) {
                                course.durationMinutes
                            } else {
                                modulesInSection.sumOf { it.readingMinutes }.takeIf { it > 0 } ?: course.durationMinutes
                            }
                            Text(
                                text = stringResource(R.string.class_detail_est_minutes_format, durationMin),
                                style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                    }
                }

                itemsIndexed(modulesInSection, key = { _, module -> module.id }) { modIndex, module ->
                    val lockState = remember(course.id, module.id, completedModuleIds, passedQuizIds, sortedModules) {
                        CourseUnlockUtils.getModuleLockState(course, module, completedModuleIds, passedQuizIds, sortedModules)
                    }
                    val prevTitle = remember(course.id, module.id, completedModuleIds, passedQuizIds, sortedModules) {
                        CourseUnlockUtils.getPreviousRequirementTitle(course, module.id, completedModuleIds, passedQuizIds, sortedModules)
                    }
                    if (section == ModuleSection.PERSIAPAN || section == ModuleSection.PENDAHULUAN) {
                        IntroModuleItemCard(
                            module = module,
                            lockState = lockState,
                            previousRequirementTitle = prevTitle,
                            onClick = { onModuleClick(module.id) }
                        )
                    } else {
                        ModuleItemCard(
                            module = module,
                            index = modIndex + 1,
                            lockState = lockState,
                            previousRequirementTitle = prevTitle,
                            onClick = { onModuleClick(module.id) }
                        )
                    }
                }

                val sectionQuiz = course.sectionQuizzes.firstOrNull { it.section == section }
                if (sectionQuiz != null) {
                    item(key = "section_quiz_${sectionQuiz.id}") {
                        val isSectionQuizUnlocked = remember(course, completedModuleIds, passedQuizIds) {
                            CourseUnlockUtils.isSectionQuizUnlocked(course, sectionQuiz.id, completedModuleIds, passedQuizIds)
                        }
                        val isSectionQuizPassed = remember(sectionQuiz.id, passedQuizIds) {
                            sectionQuiz.id in passedQuizIds
                        }
                        SectionQuizCard(
                            quiz = sectionQuiz,
                            course = course,
                            isUnlocked = isSectionQuizUnlocked,
                            isPassed = isSectionQuizPassed,
                            onStartSectionQuiz = onStartSectionQuiz
                        )
                    }
                }
            }

            item(key = "divider_before_evaluation") {
                HorizontalDivider(
                    modifier = Modifier.padding(vertical = 4.dp),
                    color = MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.6f),
                    thickness = 1.dp
                )
            }

            // Section 3: Evaluasi Akhir, Proyek & Sertifikasi
            item(key = "section_evaluation_header") {
                Column {
                    Text(
                        text = stringResource(R.string.class_detail_eval_section_title),
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onBackground
                    )
                    Text(
                        text = stringResource(R.string.class_detail_eval_section_subtitle),
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }

            // 1. Evaluation / Final Quiz Card
            item(key = "final_quiz_card") {
                val targetContainer = when {
                    !isFinalQuizUnlocked -> MaterialTheme.colorScheme.surfaceVariant
                    isFinalQuizPassed -> SuccessGreen.copy(alpha = 0.12f)
                    else -> MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.7f)
                }
                val targetBorderColor = when {
                    !isFinalQuizUnlocked -> MaterialTheme.colorScheme.outline
                    isFinalQuizPassed -> SuccessGreen.copy(alpha = 0.4f)
                    else -> TealPrimary
                }
                val targetBorderWidth = if (isFinalQuizUnlocked && !isFinalQuizPassed) 1.5.dp else 1.dp

                val animatedContainer by animateColorAsState(targetContainer, animationSpec = tween(250), label = "final_quiz_container")
                val animatedBorderColor by animateColorAsState(targetBorderColor, animationSpec = tween(250), label = "final_quiz_border")

                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable {
                            if (!isFinalQuizUnlocked) {
                                Toast.makeText(context, toastCompleteModulesFirst, Toast.LENGTH_SHORT).show()
                            } else {
                                onStartQuiz()
                            }
                        },
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = animatedContainer),
                    border = BorderStroke(targetBorderWidth, animatedBorderColor),
                    elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Box(
                            modifier = Modifier
                                .size(48.dp)
                                .clip(RoundedCornerShape(12.dp))
                                .background(
                                    when {
                                        !isFinalQuizUnlocked -> MaterialTheme.colorScheme.surface
                                        isFinalQuizPassed -> SuccessGreen
                                        else -> TealPrimary
                                    }
                                )
                                .border(
                                    width = if (isFinalQuizUnlocked) 0.dp else 1.dp,
                                    color = if (isFinalQuizUnlocked) Color.Transparent else MaterialTheme.colorScheme.outlineVariant,
                                    shape = RoundedCornerShape(12.dp)
                                ),
                            contentAlignment = Alignment.Center
                        ) {
                            Crossfade(
                                targetState = isFinalQuizUnlocked to isFinalQuizPassed,
                                animationSpec = tween(durationMillis = 200),
                                label = "final_quiz_icon_crossfade"
                            ) { (unlocked, passed) ->
                                if (!unlocked) {
                                    Icon(
                                        imageVector = Icons.Default.Lock,
                                        contentDescription = stringResource(R.string.badge_status_locked),
                                        tint = MaterialTheme.colorScheme.onSurfaceVariant,
                                        modifier = Modifier.size(24.dp)
                                    )
                                } else if (passed) {
                                    Icon(
                                        imageVector = Icons.Default.CheckCircle,
                                        contentDescription = stringResource(R.string.progress_completed_label),
                                        tint = Color.White,
                                        modifier = Modifier.size(28.dp)
                                    )
                                } else {
                                    Icon(
                                        imageVector = Icons.Default.Quiz,
                                        contentDescription = null,
                                        tint = Color.White,
                                        modifier = Modifier.size(28.dp)
                                    )
                                }
                            }
                        }

                        Spacer(modifier = Modifier.width(14.dp))

                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                text = stringResource(R.string.class_detail_final_quiz_title),
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.Bold,
                                color = when {
                                    !isFinalQuizUnlocked -> MaterialTheme.colorScheme.onSurfaceVariant
                                    isFinalQuizPassed -> MaterialTheme.colorScheme.onSurface
                                    else -> MaterialTheme.colorScheme.onPrimaryContainer
                                }
                            )
                            if (!isFinalQuizUnlocked) {
                                Spacer(modifier = Modifier.height(2.dp))
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Icon(
                                        imageVector = Icons.Default.Lock,
                                        contentDescription = null,
                                        tint = MaterialTheme.colorScheme.onSurfaceVariant,
                                        modifier = Modifier.size(11.dp)
                                    )
                                    Spacer(modifier = Modifier.width(3.dp))
                                    Text(
                                        text = stringResource(R.string.class_detail_final_quiz_locked_hint),
                                        style = MaterialTheme.typography.labelSmall,
                                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                                        fontSize = 11.sp
                                    )
                                }
                            } else if (isFinalQuizPassed) {
                                val displayScore = if (finalQuizBestScore > 0) finalQuizBestScore else 80
                                Text(
                                    text = stringResource(R.string.class_detail_final_quiz_passed_format, displayScore),
                                    style = MaterialTheme.typography.bodySmall,
                                    color = SuccessGreen,
                                    fontWeight = FontWeight.Bold
                                )
                            } else {
                                Text(
                                    text = stringResource(R.string.class_detail_final_quiz_info_format, course.quizQuestions.size),
                                    style = MaterialTheme.typography.bodySmall,
                                    color = MaterialTheme.colorScheme.onPrimaryContainer.copy(alpha = 0.8f)
                                )
                            }
                        }

                        if (!isFinalQuizUnlocked) {
                            Surface(
                                shape = RoundedCornerShape(10.dp),
                                color = MaterialTheme.colorScheme.surface,
                                border = BorderStroke(1.dp, MaterialTheme.colorScheme.outline)
                            ) {
                                Row(
                                    modifier = Modifier.padding(horizontal = 12.dp, vertical = 7.dp),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.Lock,
                                        contentDescription = null,
                                        modifier = Modifier.size(13.dp),
                                        tint = MaterialTheme.colorScheme.onSurfaceVariant
                                    )
                                    Spacer(modifier = Modifier.width(4.dp))
                                    Text(
                                        text = stringResource(R.string.badge_status_locked),
                                        fontSize = 12.sp,
                                        fontWeight = FontWeight.SemiBold,
                                        color = MaterialTheme.colorScheme.onSurfaceVariant
                                    )
                                }
                            }
                        } else {
                            Button(
                                onClick = onStartQuiz,
                                colors = ButtonDefaults.buttonColors(
                                    containerColor = if (isFinalQuizPassed) SuccessGreen else TealPrimary
                                ),
                                shape = RoundedCornerShape(10.dp),
                                contentPadding = PaddingValues(horizontal = 14.dp, vertical = 8.dp)
                            ) {
                                Text(
                                    text = if (isFinalQuizPassed) {
                                        stringResource(R.string.class_detail_btn_retake)
                                    } else {
                                        stringResource(R.string.class_detail_btn_start)
                                    },
                                    fontSize = 13.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = Color.White
                                )
                            }
                        }
                    }
                }
            }

            // 2. Final Project Submission Card (Mandatory Gate Before Certificate)
            item(key = "final_project_card") {
                val projectContainer = when {
                    !isFinalProjectUnlocked -> MaterialTheme.colorScheme.surfaceVariant
                    submissionStatus == ProjectSubmissionStatus.APPROVED -> SuccessGreen.copy(alpha = 0.12f)
                    submissionStatus == ProjectSubmissionStatus.REVISION_REQUESTED -> FlameOrange.copy(alpha = 0.12f)
                    submissionStatus == ProjectSubmissionStatus.PENDING_REVIEW -> AmberGold.copy(alpha = 0.14f)
                    else -> MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.7f)
                }
                val projectBorder = when {
                    !isFinalProjectUnlocked -> MaterialTheme.colorScheme.outline
                    submissionStatus == ProjectSubmissionStatus.APPROVED -> SuccessGreen.copy(alpha = 0.45f)
                    submissionStatus == ProjectSubmissionStatus.REVISION_REQUESTED -> FlameOrange.copy(alpha = 0.5f)
                    submissionStatus == ProjectSubmissionStatus.PENDING_REVIEW -> AmberGold.copy(alpha = 0.5f)
                    else -> TealPrimary
                }
                val projectIconBg = when {
                    !isFinalProjectUnlocked -> MaterialTheme.colorScheme.surface
                    submissionStatus == ProjectSubmissionStatus.APPROVED -> SuccessGreen
                    submissionStatus == ProjectSubmissionStatus.REVISION_REQUESTED -> FlameOrange
                    submissionStatus == ProjectSubmissionStatus.PENDING_REVIEW -> AmberGold
                    else -> TealPrimary
                }

                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("class_final_project_card")
                        .clickable {
                            if (!isFinalProjectUnlocked) {
                                Toast.makeText(context, toastPassQuizFirst, Toast.LENGTH_SHORT).show()
                            } else {
                                onOpenFinalProject()
                            }
                        },
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = projectContainer),
                    border = BorderStroke(if (isFinalProjectUnlocked) 1.5.dp else 1.dp, projectBorder),
                    elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Box(
                            modifier = Modifier
                                .size(48.dp)
                                .clip(RoundedCornerShape(12.dp))
                                .background(projectIconBg)
                                .border(
                                    width = if (isFinalProjectUnlocked) 0.dp else 1.dp,
                                    color = if (isFinalProjectUnlocked) Color.Transparent else MaterialTheme.colorScheme.outlineVariant,
                                    shape = RoundedCornerShape(12.dp)
                                ),
                            contentAlignment = Alignment.Center
                        ) {
                            if (!isFinalProjectUnlocked) {
                                Icon(
                                    imageVector = Icons.Default.Lock,
                                    contentDescription = stringResource(R.string.badge_status_locked),
                                    tint = MaterialTheme.colorScheme.onSurfaceVariant,
                                    modifier = Modifier.size(24.dp)
                                )
                            } else {
                                val iconVec = when (submissionStatus) {
                                    ProjectSubmissionStatus.APPROVED -> Icons.Default.AssignmentTurnedIn
                                    ProjectSubmissionStatus.PENDING_REVIEW -> Icons.Default.HourglassTop
                                    ProjectSubmissionStatus.REVISION_REQUESTED -> Icons.Default.EditNote
                                    else -> Icons.Default.Code
                                }
                                Icon(
                                    imageVector = iconVec,
                                    contentDescription = null,
                                    tint = Color.White,
                                    modifier = Modifier.size(26.dp)
                                )
                            }
                        }

                        Spacer(modifier = Modifier.width(14.dp))

                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                text = stringResource(R.string.final_project_card_title),
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.Bold,
                                color = if (isFinalProjectUnlocked) {
                                    MaterialTheme.colorScheme.onSurface
                                } else {
                                    MaterialTheme.colorScheme.onSurfaceVariant
                                }
                            )
                            Spacer(modifier = Modifier.height(2.dp))
                            if (!isFinalProjectUnlocked) {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Icon(
                                        imageVector = Icons.Default.Lock,
                                        contentDescription = null,
                                        tint = MaterialTheme.colorScheme.onSurfaceVariant,
                                        modifier = Modifier.size(11.dp)
                                    )
                                    Spacer(modifier = Modifier.width(3.dp))
                                    Text(
                                        text = stringResource(R.string.final_project_card_locked_hint),
                                        style = MaterialTheme.typography.labelSmall,
                                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                                        fontSize = 11.sp
                                    )
                                }
                            } else {
                                val subText = when (submissionStatus) {
                                    ProjectSubmissionStatus.APPROVED -> stringResource(
                                        R.string.final_project_card_approved_desc,
                                        projectSubmission?.facilitatorScore ?: 90
                                    )
                                    ProjectSubmissionStatus.REVISION_REQUESTED -> stringResource(
                                        R.string.final_project_card_revision_desc,
                                        projectSubmission?.facilitatorScore ?: 60
                                    )
                                    ProjectSubmissionStatus.PENDING_REVIEW -> stringResource(
                                        R.string.final_project_card_pending_desc,
                                        (projectSubmission?.attemptNumber ?: 1).coerceAtLeast(1)
                                    )
                                    else -> stringResource(R.string.final_project_card_ready_desc)
                                }
                                val subColor = when (submissionStatus) {
                                    ProjectSubmissionStatus.APPROVED -> SuccessGreen
                                    ProjectSubmissionStatus.REVISION_REQUESTED -> FlameOrange
                                    else -> MaterialTheme.colorScheme.onSurfaceVariant
                                }
                                Text(
                                    text = subText,
                                    style = MaterialTheme.typography.bodySmall,
                                    color = subColor,
                                    fontWeight = if (submissionStatus == ProjectSubmissionStatus.APPROVED || submissionStatus == ProjectSubmissionStatus.REVISION_REQUESTED) {
                                        FontWeight.Bold
                                    } else {
                                        FontWeight.Normal
                                    }
                                )
                            }
                        }

                        if (!isFinalProjectUnlocked) {
                            Surface(
                                shape = RoundedCornerShape(10.dp),
                                color = MaterialTheme.colorScheme.surface,
                                border = BorderStroke(1.dp, MaterialTheme.colorScheme.outline)
                            ) {
                                Row(
                                    modifier = Modifier.padding(horizontal = 12.dp, vertical = 7.dp),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.Lock,
                                        contentDescription = null,
                                        modifier = Modifier.size(13.dp),
                                        tint = MaterialTheme.colorScheme.onSurfaceVariant
                                    )
                                    Spacer(modifier = Modifier.width(4.dp))
                                    Text(
                                        text = stringResource(R.string.badge_status_locked),
                                        fontSize = 12.sp,
                                        fontWeight = FontWeight.SemiBold,
                                        color = MaterialTheme.colorScheme.onSurfaceVariant
                                    )
                                }
                            }
                        } else {
                            Button(
                                onClick = onOpenFinalProject,
                                colors = ButtonDefaults.buttonColors(
                                    containerColor = projectIconBg
                                ),
                                shape = RoundedCornerShape(10.dp),
                                contentPadding = PaddingValues(horizontal = 14.dp, vertical = 8.dp)
                            ) {
                                Text(
                                    text = stringResource(R.string.class_detail_btn_open),
                                    fontSize = 13.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = Color.White
                                )
                            }
                        }
                    }
                }
            }

            // 3. Certificate Banner Card
            item(key = "certificate_banner_card") {
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .then(
                            if (isCertReady) Modifier.clickable { onViewCertificate() } else Modifier
                        ),
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = if (isCertReady) SuccessGreen.copy(alpha = 0.14f) else AmberGold.copy(alpha = 0.12f)
                    ),
                    border = BorderStroke(
                        1.dp,
                        if (isCertReady) SuccessGreen.copy(alpha = 0.45f) else AmberGold.copy(alpha = 0.3f)
                    ),
                    elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = Icons.Default.WorkspacePremium,
                            contentDescription = stringResource(R.string.profile_certs_count_label),
                            tint = if (isCertReady) SuccessGreen else AmberGold,
                            modifier = Modifier.size(36.dp)
                        )
                        Spacer(modifier = Modifier.width(12.dp))
                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                text = stringResource(R.string.class_detail_cert_title),
                                style = MaterialTheme.typography.titleSmall,
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.onSurface
                            )
                            Text(
                                text = if (isCertReady) {
                                    stringResource(R.string.class_detail_cert_ready_desc)
                                } else {
                                    stringResource(R.string.class_detail_cert_desc)
                                },
                                style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                        if (isCertReady) {
                            Spacer(modifier = Modifier.width(8.dp))
                            Button(
                                onClick = onViewCertificate,
                                colors = ButtonDefaults.buttonColors(containerColor = SuccessGreen),
                                shape = RoundedCornerShape(10.dp),
                                contentPadding = PaddingValues(horizontal = 12.dp, vertical = 8.dp)
                            ) {
                                Text(
                                    text = stringResource(R.string.class_detail_cert_view_btn),
                                    fontSize = 12.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = Color.White
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun SectionQuizCard(
    quiz: SectionQuiz,
    course: Course? = null,
    isUnlocked: Boolean,
    isPassed: Boolean,
    onStartSectionQuiz: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val targetContainer = when {
        !isUnlocked -> MaterialTheme.colorScheme.surfaceVariant
        isPassed -> SuccessGreen.copy(alpha = 0.12f)
        else -> MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.7f)
    }
    val targetBorderColor = when {
        !isUnlocked -> MaterialTheme.colorScheme.outline
        isPassed -> SuccessGreen.copy(alpha = 0.4f)
        else -> TealPrimary
    }
    val targetBorderWidth = if (isUnlocked && !isPassed) 1.5.dp else 1.dp

    val animatedContainer by animateColorAsState(targetContainer, animationSpec = tween(250), label = "section_quiz_container_${quiz.id}")
    val animatedBorderColor by animateColorAsState(targetBorderColor, animationSpec = tween(250), label = "section_quiz_border_${quiz.id}")

    val sectionName = CourseSectionUtils.localizedSectionTitle(course, quiz.section)
    val toastLockedMsg = stringResource(R.string.class_detail_section_quiz_toast_locked, sectionName)

    Card(
        modifier = modifier
            .fillMaxWidth()
            .clickable {
                if (!isUnlocked) {
                    Toast.makeText(context, toastLockedMsg, Toast.LENGTH_SHORT).show()
                } else {
                    onStartSectionQuiz(quiz.id)
                }
            },
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = animatedContainer),
        border = BorderStroke(targetBorderWidth, animatedBorderColor),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(48.dp)
                    .clip(RoundedCornerShape(12.dp))
                    .background(
                        when {
                            !isUnlocked -> MaterialTheme.colorScheme.surface
                            isPassed -> SuccessGreen
                            else -> TealPrimary
                        }
                    )
                    .border(
                        width = if (!isUnlocked) 1.dp else 0.dp,
                        color = if (!isUnlocked) MaterialTheme.colorScheme.outlineVariant else Color.Transparent,
                        shape = RoundedCornerShape(12.dp)
                    ),
                contentAlignment = Alignment.Center
            ) {
                Crossfade(
                    targetState = isUnlocked to isPassed,
                    animationSpec = tween(durationMillis = 200),
                    label = "section_quiz_icon_crossfade"
                ) { (unlockedState, passedState) ->
                    if (!unlockedState) {
                        Icon(
                            imageVector = Icons.Default.Lock,
                            contentDescription = stringResource(R.string.badge_status_locked),
                            tint = MaterialTheme.colorScheme.onSurfaceVariant,
                            modifier = Modifier.size(24.dp)
                        )
                    } else if (passedState) {
                        Icon(
                            imageVector = Icons.Default.CheckCircle,
                            contentDescription = stringResource(R.string.progress_completed_label),
                            tint = Color.White,
                            modifier = Modifier.size(28.dp)
                        )
                    } else {
                        Icon(
                            imageVector = Icons.Default.Quiz,
                            contentDescription = null,
                            tint = Color.White,
                            modifier = Modifier.size(28.dp)
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.width(14.dp))

            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = quiz.title,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = when {
                        !isUnlocked -> MaterialTheme.colorScheme.onSurfaceVariant
                        isPassed -> MaterialTheme.colorScheme.onSurface
                        else -> MaterialTheme.colorScheme.onPrimaryContainer
                    }
                )
                if (!isUnlocked) {
                    Spacer(modifier = Modifier.height(2.dp))
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.Default.Lock,
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.onSurfaceVariant,
                            modifier = Modifier.size(11.dp)
                        )
                        Spacer(modifier = Modifier.width(3.dp))
                        Text(
                            text = stringResource(R.string.class_detail_section_quiz_locked_hint, sectionName),
                            style = MaterialTheme.typography.labelSmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                            fontSize = 11.sp
                        )
                    }
                } else if (isPassed) {
                    Text(
                        text = stringResource(R.string.class_detail_section_quiz_passed_label),
                        style = MaterialTheme.typography.bodySmall,
                        color = SuccessGreen,
                        fontWeight = FontWeight.Bold
                    )
                } else {
                    Text(
                        text = stringResource(
                            R.string.class_detail_section_quiz_info_format,
                            quiz.questionsPerAttempt,
                            quiz.passingPercent,
                            quiz.durationMinutes
                        ),
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onPrimaryContainer.copy(alpha = 0.8f)
                    )
                }
            }

            if (!isUnlocked) {
                Surface(
                    shape = RoundedCornerShape(10.dp),
                    color = MaterialTheme.colorScheme.surface,
                    border = BorderStroke(1.dp, MaterialTheme.colorScheme.outline)
                ) {
                    Row(
                        modifier = Modifier.padding(horizontal = 12.dp, vertical = 7.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = Icons.Default.Lock,
                            contentDescription = null,
                            modifier = Modifier.size(13.dp),
                            tint = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            text = stringResource(R.string.badge_status_locked),
                            fontSize = 12.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }
            } else {
                Button(
                    onClick = { onStartSectionQuiz(quiz.id) },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = if (isPassed) SuccessGreen else TealPrimary
                    ),
                    shape = RoundedCornerShape(10.dp),
                    contentPadding = PaddingValues(horizontal = 14.dp, vertical = 8.dp)
                ) {
                    Text(
                        text = if (isPassed) {
                            stringResource(R.string.class_detail_btn_retake)
                        } else {
                            stringResource(R.string.class_detail_btn_open)
                        },
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )
                }
            }
        }
    }
}

/**
 * Consolidated styling and colors for Module Items (ModuleItemCard & IntroModuleItemCard)
 * Ensures consistent solid colors, accessible contrast, and smooth animateColorAsState transitions.
 */
data class ModuleCardColors(
    val containerColor: Color,
    val borderColor: Color,
    val borderWidth: Dp,
    val iconContainerColor: Color,
    val iconContainerBorderColor: Color,
    val iconTint: Color,
    val titleColor: Color,
    val subtitleColor: Color,
    val requirementTextColor: Color,
    val trailingIconTint: Color
)

@Composable
private fun rememberModuleCardColors(
    lockState: ModuleLockState,
    isIntro: Boolean = false
): ModuleCardColors {
    val isLocked = lockState == ModuleLockState.LOCKED
    val isCurrent = lockState == ModuleLockState.CURRENT_UNLOCKED
    val isCompleted = lockState == ModuleLockState.COMPLETED

    // 1. Container: Solid surfaceVariant when locked (no alpha), surface when active/completed
    val targetContainer = when {
        isLocked -> MaterialTheme.colorScheme.surfaceVariant
        isCompleted -> MaterialTheme.colorScheme.surface.copy(alpha = 0.9f)
        else -> MaterialTheme.colorScheme.surface
    }

    // 2. Border: 1.dp solid outline when locked, 1.5.dp teal when active
    val targetBorderColor = when {
        isLocked -> MaterialTheme.colorScheme.outline
        isCurrent -> TealPrimary
        else -> Color.Transparent
    }
    val borderWidth = if (isCurrent) 1.5.dp else 1.dp

    // 3. Icon circular container & border
    val targetIconContainer = when {
        isLocked -> MaterialTheme.colorScheme.surface
        isCompleted -> SuccessGreen.copy(alpha = 0.15f)
        isIntro -> TealLight.copy(alpha = 0.15f)
        else -> TealPrimary.copy(alpha = 0.15f)
    }
    val targetIconContainerBorder = if (isLocked) {
        MaterialTheme.colorScheme.outlineVariant
    } else {
        Color.Transparent
    }

    // 4. Icon tint
    val targetIconTint = when {
        isLocked -> MaterialTheme.colorScheme.onSurfaceVariant
        isCompleted -> SuccessGreen
        isIntro -> MaterialTheme.colorScheme.primary
        else -> TealPrimary
    }

    // 5. Title & subtitle colors (onSurfaceVariant guarantees >= 4.5:1 contrast on surfaceVariant)
    val targetTitleColor = when {
        isLocked -> MaterialTheme.colorScheme.onSurfaceVariant
        else -> MaterialTheme.colorScheme.onSurface
    }
    val targetSubtitleColor = MaterialTheme.colorScheme.onSurfaceVariant
    val targetReqColor = MaterialTheme.colorScheme.onSurfaceVariant

    // 6. Trailing indicator tint
    val targetTrailingTint = when {
        isLocked -> MaterialTheme.colorScheme.onSurfaceVariant
        isCompleted -> SuccessGreen
        isIntro -> MaterialTheme.colorScheme.primary
        else -> TealPrimary
    }

    val animatedContainer by animateColorAsState(targetContainer, animationSpec = tween(250), label = "module_container")
    val animatedBorderColor by animateColorAsState(targetBorderColor, animationSpec = tween(250), label = "module_border")
    val animatedIconContainer by animateColorAsState(targetIconContainer, animationSpec = tween(250), label = "module_icon_container")
    val animatedIconContainerBorder by animateColorAsState(targetIconContainerBorder, animationSpec = tween(250), label = "module_icon_border")
    val animatedIconTint by animateColorAsState(targetIconTint, animationSpec = tween(250), label = "module_icon_tint")
    val animatedTitleColor by animateColorAsState(targetTitleColor, animationSpec = tween(250), label = "module_title_color")
    val animatedSubtitleColor by animateColorAsState(targetSubtitleColor, animationSpec = tween(250), label = "module_subtitle_color")
    val animatedReqColor by animateColorAsState(targetReqColor, animationSpec = tween(250), label = "module_req_color")
    val animatedTrailingTint by animateColorAsState(targetTrailingTint, animationSpec = tween(250), label = "module_trailing_tint")

    return ModuleCardColors(
        containerColor = animatedContainer,
        borderColor = animatedBorderColor,
        borderWidth = borderWidth,
        iconContainerColor = animatedIconContainer,
        iconContainerBorderColor = animatedIconContainerBorder,
        iconTint = animatedIconTint,
        titleColor = animatedTitleColor,
        subtitleColor = animatedSubtitleColor,
        requirementTextColor = animatedReqColor,
        trailingIconTint = animatedTrailingTint
    )
}

@Composable
fun ModuleItemCard(
    module: CourseModule,
    index: Int,
    lockState: ModuleLockState,
    previousRequirementTitle: String?,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    ModuleItemCardContent(
        module = module,
        lockState = lockState,
        previousRequirementTitle = previousRequirementTitle,
        onClick = onClick,
        isIntro = false,
        modifier = modifier
    )
}

@Composable
fun IntroModuleItemCard(
    module: CourseModule,
    lockState: ModuleLockState,
    previousRequirementTitle: String?,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    ModuleItemCardContent(
        module = module,
        lockState = lockState,
        previousRequirementTitle = previousRequirementTitle,
        onClick = onClick,
        isIntro = true,
        modifier = modifier
    )
}

@Composable
private fun ModuleItemCardContent(
    module: CourseModule,
    lockState: ModuleLockState,
    previousRequirementTitle: String?,
    onClick: () -> Unit,
    isIntro: Boolean,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val colors = rememberModuleCardColors(lockState = lockState, isIntro = isIntro)
    val toastModuleLocked = stringResource(R.string.class_detail_module_toast_locked)

    Card(
        modifier = modifier
            .fillMaxWidth()
            .clickable {
                if (lockState == ModuleLockState.LOCKED) {
                    Toast.makeText(context, toastModuleLocked, Toast.LENGTH_SHORT).show()
                } else {
                    onClick()
                }
            },
        shape = RoundedCornerShape(14.dp),
        colors = CardDefaults.cardColors(containerColor = colors.containerColor),
        border = BorderStroke(colors.borderWidth, colors.borderColor),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(14.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Icon / Number indicator with Crossfade
            Box(
                modifier = Modifier
                    .size(36.dp)
                    .clip(CircleShape)
                    .background(colors.iconContainerColor)
                    .border(
                        width = if (lockState == ModuleLockState.LOCKED) 1.dp else 0.dp,
                        color = colors.iconContainerBorderColor,
                        shape = CircleShape
                    ),
                contentAlignment = Alignment.Center
            ) {
                Crossfade(
                    targetState = lockState,
                    animationSpec = tween(durationMillis = 200),
                    label = "module_icon_crossfade_${module.id}"
                ) { state ->
                    when (state) {
                        ModuleLockState.COMPLETED -> {
                            Icon(
                                imageVector = Icons.Default.CheckCircle,
                                contentDescription = stringResource(R.string.progress_completed_label),
                                tint = colors.iconTint,
                                modifier = Modifier.size(20.dp)
                            )
                        }
                        ModuleLockState.CURRENT_UNLOCKED -> {
                            Icon(
                                imageVector = Icons.Default.PlayCircle,
                                contentDescription = if (isIntro) {
                                    stringResource(R.string.class_detail_pill_intro)
                                } else {
                                    stringResource(R.string.home_continue_button)
                                },
                                tint = colors.iconTint,
                                modifier = Modifier.size(22.dp)
                            )
                        }
                        ModuleLockState.LOCKED -> {
                            Icon(
                                imageVector = Icons.Default.Lock,
                                contentDescription = stringResource(R.string.badge_status_locked),
                                tint = colors.iconTint,
                                modifier = Modifier.size(18.dp)
                            )
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.width(12.dp))

            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = module.title,
                    style = MaterialTheme.typography.bodyMedium,
                    fontWeight = FontWeight.SemiBold,
                    color = colors.titleColor
                )
                if (lockState == ModuleLockState.LOCKED && previousRequirementTitle != null) {
                    Spacer(modifier = Modifier.height(2.dp))
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.Default.Lock,
                            contentDescription = null,
                            tint = colors.requirementTextColor,
                            modifier = Modifier.size(11.dp)
                        )
                        Spacer(modifier = Modifier.width(3.dp))
                        Text(
                            text = stringResource(R.string.class_detail_module_locked_req_format, previousRequirementTitle),
                            style = MaterialTheme.typography.labelSmall,
                            color = colors.requirementTextColor,
                            fontSize = 11.sp,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )
                    }
                } else {
                    Spacer(modifier = Modifier.height(2.dp))
                    Text(
                        text = if (isIntro) {
                            stringResource(R.string.class_detail_module_intro_read_format, module.readingMinutes)
                        } else {
                            stringResource(R.string.class_detail_module_read_format, module.readingMinutes)
                        },
                        style = MaterialTheme.typography.bodySmall,
                        color = colors.subtitleColor
                    )
                }
            }

            when (lockState) {
                ModuleLockState.COMPLETED -> {
                    Icon(
                        imageVector = Icons.Default.Check,
                        contentDescription = stringResource(R.string.progress_completed_label),
                        tint = colors.trailingIconTint,
                        modifier = Modifier.size(18.dp)
                    )
                }
                ModuleLockState.CURRENT_UNLOCKED -> {
                    Icon(
                        imageVector = Icons.Default.PlayArrow,
                        contentDescription = if (isIntro) {
                            stringResource(R.string.class_detail_btn_open)
                        } else {
                            stringResource(R.string.class_detail_btn_start)
                        },
                        tint = colors.trailingIconTint,
                        modifier = Modifier.size(20.dp)
                    )
                }
                ModuleLockState.LOCKED -> {
                    Icon(
                        imageVector = Icons.Default.Lock,
                        contentDescription = stringResource(R.string.badge_status_locked),
                        tint = colors.trailingIconTint,
                        modifier = Modifier.size(16.dp)
                    )
                }
            }
        }
    }
}
