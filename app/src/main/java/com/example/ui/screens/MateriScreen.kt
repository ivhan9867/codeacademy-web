package com.example.ui.screens

import android.widget.Toast
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.ui.platform.testTag
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.AssignmentTurnedIn
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.Bookmark
import androidx.compose.material.icons.filled.BookmarkBorder
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Code
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Psychology
import androidx.compose.ui.res.stringResource
import com.example.R
import com.example.util.localization.AppLanguage
import com.example.util.localization.LocalAppLanguage
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.local.BookmarkEntity
import com.example.data.local.ModuleProgressEntity
import com.example.data.models.Course
import com.example.data.models.CourseModule
import com.example.data.models.ModuleSection
import com.example.data.util.CourseSectionUtils
import com.example.data.util.CourseUnlockUtils
import com.example.data.util.ModuleLockState
import com.example.ui.components.CodeBlockView
import com.example.ui.components.MarkdownContent
import com.example.ui.components.PrimaryButton
import com.example.ui.theme.CyanAccent
import com.example.ui.theme.FlameOrange
import com.example.ui.theme.SuccessGreen
import com.example.ui.theme.TealDark
import com.example.ui.theme.TealLight
import com.example.ui.theme.TealPrimary
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MateriScreen(
    course: Course,
    moduleId: String,
    progressList: List<ModuleProgressEntity>,
    bookmarks: List<BookmarkEntity>,
    passedQuizIds: Set<String> = emptySet(),
    verifiedModuleIds: Set<String> = emptySet(),
    onBack: () -> Unit,
    onSelectModule: (String) -> Unit,
    onCompleteModule: (String, String) -> Unit,
    onOpenPlayground: (String, String, String?, String?) -> Unit,
    onOpenAiTutor: (String, String) -> Unit,
    onToggleBookmark: (CourseModule) -> Unit,
    onTranslateToEnglish: suspend (String, String) -> String? = { _, _ -> null },
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val completedModuleIds = remember(progressList, course.id) {
        progressList.filter { it.courseId == course.id && it.isCompleted }.map { it.moduleId }.toSet()
    }
    val currentModuleIndex = remember(course.modules, moduleId) {
        course.modules.indexOfFirst { it.id == moduleId }.coerceAtLeast(0)
    }
    val currentModule = remember(course.modules, currentModuleIndex) {
        course.modules.getOrElse(currentModuleIndex) { course.modules.first() }
    }

    val isUnlocked = remember(course, currentModule.id, completedModuleIds, passedQuizIds) {
        CourseUnlockUtils.isModuleUnlocked(course, currentModule.id, completedModuleIds, passedQuizIds)
    }

    if (!isUnlocked) {
        val reqTitle = remember(course, currentModule.id, completedModuleIds, passedQuizIds) {
            CourseUnlockUtils.getPreviousRequirementTitle(course, currentModule.id, completedModuleIds, passedQuizIds)
        }
        Scaffold(
            topBar = {
                TopAppBar(
                    title = {
                        Text(
                            text = currentModule.title,
                            maxLines = 1,
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold
                        )
                    },
                    navigationIcon = {
                        IconButton(onClick = onBack) {
                            Icon(
                                imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                                contentDescription = stringResource(R.string.comp_back_cd)
                            )
                        }
                    },
                    colors = TopAppBarDefaults.topAppBarColors(
                        containerColor = MaterialTheme.colorScheme.surface
                    )
                )
            },
            modifier = modifier
        ) { padding ->
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding)
                    .padding(24.dp),
                contentAlignment = Alignment.Center
            ) {
                Card(
                    shape = RoundedCornerShape(20.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f)
                    ),
                    border = BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.4f)),
                    elevation = CardDefaults.cardElevation(defaultElevation = 0.dp),
                    modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp)
                ) {
                    Column(
                        modifier = Modifier.padding(24.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Box(
                            modifier = Modifier
                                .size(64.dp)
                                .clip(CircleShape)
                                .background(MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.35f)),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.Default.Lock,
                                contentDescription = stringResource(R.string.comp_locked_badge),
                                tint = MaterialTheme.colorScheme.onSurfaceVariant,
                                modifier = Modifier.size(32.dp)
                            )
                        }
                        Spacer(modifier = Modifier.height(16.dp))
                        Text(
                            text = stringResource(R.string.materi_locked_title),
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = if (!reqTitle.isNullOrBlank()) {
                                stringResource(R.string.materi_locked_req_format, reqTitle)
                            } else {
                                stringResource(R.string.materi_locked_prev_desc)
                            },
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                            textAlign = TextAlign.Center
                        )
                        Spacer(modifier = Modifier.height(20.dp))
                        PrimaryButton(
                            text = stringResource(R.string.materi_locked_btn_back),
                            onClick = onBack,
                            icon = Icons.AutoMirrored.Filled.ArrowBack
                        )
                    }
                }
            }
        }
        return
    }

    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val scope = rememberCoroutineScope()

    val isBookmarked = remember(bookmarks, currentModule.id) {
        bookmarks.any { it.itemId == currentModule.id && it.type == "MODULE" }
    }
    val isCurrentCompleted = remember(completedModuleIds, currentModule.id) {
        currentModule.id in completedModuleIds
    }
    val hasVerificationRules = remember(currentModule.verificationRules) {
        currentModule.verificationRules.isNotEmpty()
    }
    val isVerified = remember(hasVerificationRules, isCurrentCompleted, verifiedModuleIds, currentModule.id) {
        !hasVerificationRules || isCurrentCompleted || verifiedModuleIds.contains(currentModule.id)
    }

    val hasPrevious = remember(currentModuleIndex) { currentModuleIndex > 0 }
    val hasNext = remember(currentModuleIndex, course.modules.size) { currentModuleIndex < course.modules.size - 1 }

    ModalNavigationDrawer(
        drawerState = drawerState,
        drawerContent = {
            ModalDrawerSheet(
                modifier = Modifier
                    .fillMaxHeight()
                    .width(310.dp)
            ) {
                // Sidebar Header
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(TealDark)
                        .padding(20.dp)
                ) {
                    Text(
                        text = stringResource(R.string.materi_drawer_title),
                        color = Color.White,
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = course.title,
                        color = MaterialTheme.colorScheme.primary,
                        style = MaterialTheme.typography.bodySmall
                    )
                }

                HorizontalDivider()

                val moduleSections = remember(course.modules) {
                    course.modules.groupBy { it.section }
                }
                val sectionLabel: (ModuleSection) -> String = { section ->
                    CourseSectionUtils.localizedSectionTitle(course, section, context)
                }

                LazyColumn(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(vertical = 4.dp)
                ) {
                    moduleSections.forEach { (section, modulesInSection) ->
                        item(key = "drawer_section_${section.name}") {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(horizontal = 16.dp, vertical = 10.dp)
                            ) {
                                Box(
                                    modifier = Modifier
                                        .width(3.dp)
                                        .height(14.dp)
                                        .background(TealPrimary, RoundedCornerShape(2.dp))
                                )
                                Spacer(modifier = Modifier.width(8.dp))
                                Text(
                                    text = sectionLabel(section).uppercase(),
                                    style = MaterialTheme.typography.labelMedium,
                                    fontWeight = FontWeight.Bold,
                                    letterSpacing = 0.5.sp,
                                    color = TealPrimary
                                )
                            }
                        }

                        itemsIndexed(modulesInSection, key = { _, mod -> mod.id }) { idx, mod ->
                            val isSelected = mod.id == currentModule.id
                            val modLockState = CourseUnlockUtils.getModuleLockState(course, mod, completedModuleIds, passedQuizIds)
                            val isDone = modLockState == ModuleLockState.COMPLETED
                            val isModLocked = modLockState == ModuleLockState.LOCKED

                            Card(
                                shape = RoundedCornerShape(10.dp),
                                colors = CardDefaults.cardColors(
                                    containerColor = if (isSelected) TealLight.copy(alpha = 0.18f) else MaterialTheme.colorScheme.surface
                                ),
                                border = BorderStroke(
                                    width = if (isSelected) 1.2.dp else 1.dp,
                                    color = if (isSelected) TealPrimary.copy(alpha = 0.5f) else MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.5f)
                                ),
                                elevation = CardDefaults.cardElevation(defaultElevation = 0.dp),
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(horizontal = 12.dp, vertical = 4.dp)
                                    .clickable {
                                        if (isModLocked) {
                                            Toast.makeText(context, context.getString(R.string.materi_complete_prev_toast), Toast.LENGTH_SHORT).show()
                                        } else {
                                            scope.launch { drawerState.close() }
                                            onSelectModule(mod.id)
                                        }
                                    }
                            ) {
                                Row(
                                    modifier = Modifier.padding(horizontal = 12.dp, vertical = 10.dp),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Box(
                                        modifier = Modifier
                                            .size(26.dp)
                                            .clip(CircleShape)
                                            .background(
                                                when {
                                                    isSelected -> TealPrimary
                                                    isDone -> SuccessGreen
                                                    isModLocked -> MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f)
                                                    else -> MaterialTheme.colorScheme.surfaceVariant
                                                }
                                            ),
                                        contentAlignment = Alignment.Center
                                    ) {
                                        when {
                                            isDone -> {
                                                Icon(
                                                    imageVector = Icons.Default.CheckCircle,
                                                    contentDescription = null,
                                                    tint = Color.White,
                                                    modifier = Modifier.size(14.dp)
                                                )
                                            }
                                            isModLocked -> {
                                                Icon(
                                                    imageVector = Icons.Default.Lock,
                                                    contentDescription = null,
                                                    tint = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.6f),
                                                    modifier = Modifier.size(13.dp)
                                                )
                                            }
                                            else -> {
                                                Text(
                                                    text = "${idx + 1}",
                                                    fontSize = 10.sp,
                                                    fontWeight = FontWeight.Bold,
                                                    color = if (isSelected) Color.White else MaterialTheme.colorScheme.onSurfaceVariant
                                                )
                                            }
                                        }
                                    }

                                    Spacer(modifier = Modifier.width(10.dp))

                                    Column(modifier = Modifier.weight(1f)) {
                                        Text(
                                            text = mod.title,
                                            style = MaterialTheme.typography.bodySmall,
                                            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
                                            color = when {
                                                isSelected -> MaterialTheme.colorScheme.primary
                                                isModLocked -> MaterialTheme.colorScheme.onSurface.copy(alpha = 0.45f)
                                                else -> MaterialTheme.colorScheme.onSurface
                                            },
                                            maxLines = 2,
                                            overflow = TextOverflow.Ellipsis
                                        )
                                        Spacer(modifier = Modifier.height(1.dp))
                                        Text(
                                            text = stringResource(R.string.materi_reading_min_short_format, mod.readingMinutes),
                                            fontSize = 10.sp,
                                            color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = if (isModLocked) 0.45f else 0.8f)
                                        )
                                    }
                                }
                            }
                        }

                        item(key = "drawer_section_spacer_${section.name}") {
                            Spacer(modifier = Modifier.height(4.dp))
                        }
                    }
                }
            }
        },
        modifier = modifier
    ) {
        Scaffold(
            topBar = {
                TopAppBar(
                    title = {
                        Column {
                            Text(
                                text = stringResource(R.string.materi_module_step_format, currentModuleIndex + 1, course.modules.size),
                                style = MaterialTheme.typography.labelSmall,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                            Text(
                                text = currentModule.title,
                                style = MaterialTheme.typography.titleSmall,
                                fontWeight = FontWeight.Bold,
                                maxLines = 1,
                                overflow = androidx.compose.ui.text.style.TextOverflow.Ellipsis
                            )
                        }
                    },
                    navigationIcon = {
                        IconButton(onClick = onBack) {
                            Icon(
                                imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                                contentDescription = stringResource(R.string.comp_back_cd)
                            )
                        }
                    },
                    actions = {
                        IconButton(onClick = { onToggleBookmark(currentModule) }) {
                            Icon(
                                imageVector = if (isBookmarked) Icons.Default.Bookmark else Icons.Default.BookmarkBorder,
                                contentDescription = stringResource(R.string.materi_bookmark_add_cd),
                                tint = if (isBookmarked) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurface
                            )
                        }
                        IconButton(onClick = { scope.launch { drawerState.open() } }) {
                            Icon(
                                imageVector = Icons.Default.Menu,
                                contentDescription = stringResource(R.string.materi_drawer_cd)
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
                            .padding(horizontal = 16.dp, vertical = 12.dp),
                        horizontalArrangement = Arrangement.spacedBy(10.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        if (hasPrevious) {
                            com.example.ui.components.SecondaryButton(
                                text = stringResource(R.string.materi_btn_prev),
                                onClick = {
                                    val prevMod = course.modules[currentModuleIndex - 1]
                                    onSelectModule(prevMod.id)
                                },
                                icon = Icons.AutoMirrored.Filled.ArrowBack,
                                modifier = Modifier.weight(0.42f)
                            )
                        } else {
                            Spacer(modifier = Modifier.width(1.dp))
                        }

                        if (!isVerified) {
                            Column(
                                horizontalAlignment = Alignment.End,
                                modifier = if (hasPrevious) Modifier.weight(0.58f) else Modifier.fillMaxWidth()
                            ) {
                                val missingRulesText = currentModule.verificationRules.joinToString(", ")
                                Text(
                                    text = stringResource(R.string.materi_verify_rule_notice_format, missingRulesText),
                                    style = MaterialTheme.typography.labelSmall,
                                    color = FlameOrange,
                                    textAlign = androidx.compose.ui.text.style.TextAlign.End,
                                    modifier = Modifier
                                        .padding(bottom = 4.dp)
                                        .widthIn(max = 220.dp)
                                        .testTag("verification_rule_notice")
                                )
                                Button(
                                    onClick = {
                                        onOpenPlayground(
                                            course.id,
                                            currentModule.id,
                                            currentModule.codeSnippet ?: currentModule.starterHtml,
                                            currentModule.codeLanguage
                                        )
                                    },
                                    colors = ButtonDefaults.buttonColors(
                                        containerColor = FlameOrange,
                                        contentColor = Color.White
                                    ),
                                    shape = RoundedCornerShape(12.dp),
                                    modifier = Modifier.testTag("open_playground_button")
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.Code,
                                        contentDescription = null,
                                        modifier = Modifier.size(16.dp)
                                    )
                                    Spacer(modifier = Modifier.width(6.dp))
                                    Text(
                                        text = stringResource(R.string.materi_btn_open_playground),
                                        style = MaterialTheme.typography.labelLarge
                                    )
                                }
                            }
                        } else {
                            com.example.ui.components.PrimaryButton(
                                text = if (hasNext) stringResource(R.string.materi_btn_finish_and_next) else stringResource(R.string.materi_btn_finish_course),
                                onClick = {
                                    onCompleteModule(course.id, currentModule.id)
                                    if (hasNext) {
                                        val nextMod = course.modules[currentModuleIndex + 1]
                                        onSelectModule(nextMod.id)
                                    } else {
                                        onBack()
                                    }
                                },
                                icon = if (hasNext) Icons.AutoMirrored.Filled.ArrowForward else Icons.Default.Check,
                                containerColor = MaterialTheme.colorScheme.primary,
                                contentColor = MaterialTheme.colorScheme.onPrimary,
                                modifier = Modifier
                                    .then(if (hasPrevious) Modifier.weight(0.58f) else Modifier.fillMaxWidth())
                                    .testTag("finish_and_continue_button")
                            )
                        }
                    }
                }
            }
        ) { paddingValues ->
            val currentLanguage = LocalAppLanguage.current
            var translatedContent by remember(currentModule.id, currentLanguage) { mutableStateOf<String?>(null) }
            var isTranslating by remember(currentModule.id, currentLanguage) { mutableStateOf(false) }

            LaunchedEffect(currentModule.id, currentLanguage) {
                if (currentLanguage != AppLanguage.INDONESIAN) {
                    isTranslating = true
                    translatedContent = try {
                        onTranslateToEnglish(currentModule.id, currentModule.content)
                    } catch (_: Throwable) {
                        null
                    }
                    isTranslating = false
                } else {
                    translatedContent = null
                    isTranslating = false
                }
            }

            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
                    .padding(horizontal = 16.dp),
                contentPadding = PaddingValues(top = 16.dp, bottom = 80.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                // Language / Auto-translation status banner if language is not Indonesian
                if (currentLanguage != AppLanguage.INDONESIAN) {
                    item(key = "language_content_notice_banner") {
                        Card(
                            modifier = Modifier
                                .fillMaxWidth()
                                .testTag("materi_language_notice_banner"),
                            shape = RoundedCornerShape(12.dp),
                            colors = CardDefaults.cardColors(
                                containerColor = MaterialTheme.colorScheme.secondaryContainer.copy(alpha = 0.5f)
                            ),
                            border = BorderStroke(1.dp, MaterialTheme.colorScheme.secondary.copy(alpha = 0.3f))
                        ) {
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(horizontal = 14.dp, vertical = 10.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                if (isTranslating) {
                                    CircularProgressIndicator(
                                        modifier = Modifier.size(16.dp),
                                        strokeWidth = 2.dp,
                                        color = MaterialTheme.colorScheme.secondary
                                    )
                                } else {
                                    Icon(
                                        imageVector = if (translatedContent != null) Icons.Default.AutoAwesome else Icons.Default.Info,
                                        contentDescription = null,
                                        tint = MaterialTheme.colorScheme.secondary,
                                        modifier = Modifier.size(18.dp)
                                    )
                                }
                                Spacer(modifier = Modifier.width(10.dp))
                                Text(
                                    text = when {
                                        isTranslating -> stringResource(R.string.materi_translating_en)
                                        translatedContent != null -> stringResource(R.string.materi_translated_badge)
                                        else -> stringResource(R.string.materi_content_id_only)
                                    },
                                    style = MaterialTheme.typography.bodySmall,
                                    color = MaterialTheme.colorScheme.onSecondaryContainer
                                )
                            }
                        }
                    }
                }

                // Interactive tools banner: Playground & AI Tutor
                item(key = "interactive_tools_banner") {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        Card(
                            modifier = Modifier
                                .weight(1f)
                                .clickable {
                                    onOpenPlayground(
                                        course.id,
                                        currentModule.id,
                                        currentModule.codeSnippet ?: currentModule.starterHtml,
                                        currentModule.codeLanguage
                                    )
                                },
                            shape = RoundedCornerShape(14.dp),
                            colors = CardDefaults.cardColors(
                                containerColor = TealPrimary.copy(alpha = 0.12f)
                            ),
                            border = BorderStroke(1.dp, TealPrimary.copy(alpha = 0.25f)),
                            elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
                        ) {
                            Row(
                                modifier = Modifier.padding(12.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Code,
                                    contentDescription = null,
                                    tint = MaterialTheme.colorScheme.primary,
                                    modifier = Modifier.size(20.dp)
                                )
                                Spacer(modifier = Modifier.width(8.dp))
                                Text(
                                    text = "Playground",
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 12.sp,
                                    color = MaterialTheme.colorScheme.primary
                                )
                            }
                        }

                        Card(
                            modifier = Modifier
                                .weight(1f)
                                .clickable { onOpenAiTutor(course.title, currentModule.title) },
                            shape = RoundedCornerShape(14.dp),
                            colors = CardDefaults.cardColors(
                                containerColor = CyanAccent.copy(alpha = 0.12f)
                            ),
                            border = BorderStroke(1.dp, CyanAccent.copy(alpha = 0.25f)),
                            elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
                        ) {
                            Row(
                                modifier = Modifier.padding(12.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Psychology,
                                    contentDescription = null,
                                    tint = CyanAccent,
                                    modifier = Modifier.size(20.dp)
                                )
                                Spacer(modifier = Modifier.width(8.dp))
                                Text(
                                    text = stringResource(R.string.materi_ai_tutor_label),
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 12.sp,
                                    color = MaterialTheme.colorScheme.onSurface
                                )
                            }
                        }
                    }
                }

                // Article Content Card
                item(key = "article_content_card") {
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(18.dp),
                        colors = CardDefaults.cardColors(
                            containerColor = MaterialTheme.colorScheme.surface
                        ),
                        border = BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.5f)),
                        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
                    ) {
                        Column(modifier = Modifier.padding(18.dp)) {
                            // Section badge & Reading time
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Surface(
                                    color = TealLight.copy(alpha = 0.15f),
                                    shape = RoundedCornerShape(6.dp)
                                ) {
                                    Text(
                                        text = course.title,
                                        color = MaterialTheme.colorScheme.primary,
                                        fontSize = 11.sp,
                                        fontWeight = FontWeight.Bold,
                                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                                    )
                                }

                                Text(
                                    text = stringResource(R.string.materi_reading_min_long_format, currentModule.readingMinutes),
                                    style = MaterialTheme.typography.bodySmall,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                            }

                            Spacer(modifier = Modifier.height(14.dp))

                            // Learning Objectives Card (if present)
                            if (currentModule.learningObjectives.isNotEmpty()) {
                                Surface(
                                    modifier = Modifier.fillMaxWidth(),
                                    shape = RoundedCornerShape(12.dp),
                                    color = MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.35f),
                                    border = BorderStroke(1.dp, MaterialTheme.colorScheme.primary.copy(alpha = 0.2f))
                                ) {
                                    Column(modifier = Modifier.padding(14.dp)) {
                                        Row(verticalAlignment = Alignment.CenterVertically) {
                                            Icon(
                                                imageVector = Icons.Default.CheckCircle,
                                                contentDescription = null,
                                                tint = MaterialTheme.colorScheme.primary,
                                                modifier = Modifier.size(16.dp)
                                            )
                                            Spacer(modifier = Modifier.width(8.dp))
                                            Text(
                                                text = stringResource(R.string.materi_objectives_title),
                                                style = MaterialTheme.typography.labelMedium,
                                                fontWeight = FontWeight.Bold,
                                                color = MaterialTheme.colorScheme.primary
                                            )
                                        }
                                        Spacer(modifier = Modifier.height(8.dp))
                                        currentModule.learningObjectives.forEach { objective ->
                                            Row(
                                                modifier = Modifier.padding(vertical = 2.dp),
                                                verticalAlignment = Alignment.Top
                                            ) {
                                                Text(
                                                    text = "•",
                                                    style = MaterialTheme.typography.bodySmall,
                                                    fontWeight = FontWeight.Bold,
                                                    color = MaterialTheme.colorScheme.primary,
                                                    modifier = Modifier.padding(end = 6.dp)
                                                )
                                                Text(
                                                    text = objective,
                                                    style = MaterialTheme.typography.bodySmall,
                                                    color = MaterialTheme.colorScheme.onSurface
                                                )
                                            }
                                        }
                                    }
                                }
                                Spacer(modifier = Modifier.height(14.dp))
                            }

                            // Formatted Markdown Content with Glossary Tooltip
                            MarkdownContent(
                                content = translatedContent ?: currentModule.content,
                                courseId = course.id
                            )

                            // Code Snippet with Copy Button
                            if (!currentModule.codeSnippet.isNullOrBlank()) {
                                Spacer(modifier = Modifier.height(16.dp))

                                Text(
                                    text = stringResource(R.string.materi_code_example_title),
                                    style = MaterialTheme.typography.titleSmall,
                                    fontWeight = FontWeight.Bold,
                                    color = MaterialTheme.colorScheme.onSurface
                                )

                                Spacer(modifier = Modifier.height(8.dp))

                                CodeBlockView(
                                    code = currentModule.codeSnippet ?: "",
                                    language = currentModule.codeLanguage ?: "html",
                                    onTryInPlayground = { snippetCode ->
                                        onOpenPlayground(course.id, currentModule.id, snippetCode, currentModule.codeLanguage)
                                    }
                                )
                            }
                        }
                    }
                }

                // Callout Tip Card
                item(key = "callout_tip_card") {
                    val cardBg = if (hasVerificationRules && !isVerified) FlameOrange.copy(alpha = 0.08f) else CyanAccent.copy(alpha = 0.08f)
                    val cardBorder = if (hasVerificationRules && !isVerified) FlameOrange.copy(alpha = 0.3f) else CyanAccent.copy(alpha = 0.2f)
                    val iconTint = if (hasVerificationRules && !isVerified) FlameOrange else CyanAccent

                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(14.dp),
                        colors = CardDefaults.cardColors(
                            containerColor = cardBg
                        ),
                        border = BorderStroke(1.dp, cardBorder),
                        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
                    ) {
                        Row(
                            modifier = Modifier.padding(14.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(
                                imageVector = if (hasVerificationRules && !isVerified) Icons.Default.AssignmentTurnedIn else Icons.Default.AutoAwesome,
                                contentDescription = null,
                                tint = iconTint,
                                modifier = Modifier.size(24.dp)
                            )
                            Spacer(modifier = Modifier.width(12.dp))
                            Text(
                                text = if (hasVerificationRules && !isVerified) {
                                    stringResource(R.string.materi_callout_verify_desc)
                                } else {
                                    stringResource(R.string.materi_callout_tip_desc)
                                },
                                style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.onSurface
                            )
                        }
                    }
                }
            }
        }
    }
}
