package com.example.ui.screens

import android.content.Context
import android.net.Uri
import android.provider.OpenableColumns
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.automirrored.filled.Send
import androidx.compose.material.icons.filled.AssignmentTurnedIn
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.CloudDone
import androidx.compose.material.icons.filled.Code
import androidx.compose.material.icons.filled.Description
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.HourglassTop
import androidx.compose.material.icons.filled.Link
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.RadioButtonUnchecked
import androidx.compose.material.icons.filled.RateReview
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Restore
import androidx.compose.material.icons.filled.Save
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.StarBorder
import androidx.compose.material.icons.filled.UploadFile
import androidx.compose.material.icons.filled.WorkspacePremium
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CheckboxDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.example.R
import com.example.data.CourseCatalogParser
import com.example.data.local.ProjectSubmissionEntity
import com.example.data.models.Course
import com.example.data.models.FinalProjectSpec
import com.example.data.models.ProjectSubmissionStatus
import com.example.data.remote.SubmissionQueueInfo
import com.example.ui.components.CodeSandboxWebView
import com.example.ui.theme.AmberGold
import com.example.ui.theme.FlameOrange
import com.example.ui.theme.SuccessGreen
import com.example.ui.theme.TealPrimary
import com.example.util.localization.DataLabelMappings
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.json.JSONArray
import java.io.ByteArrayOutputStream
import java.util.zip.ZipInputStream
import kotlin.math.cos
import kotlin.math.roundToInt
import kotlin.math.sin

private enum class SubmissionViewMode {
    COMPOSE,
    WAITING,
    REVISION,
    APPROVED,
    RATING
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FinalProjectSubmissionScreen(
    course: Course,
    submission: ProjectSubmissionEntity?,
    onBack: () -> Unit,
    onLoadPlaygroundDraft: suspend () -> String?,
    onSaveDraft: (workspaceCode: String, projectUrl: String, studentNotes: String, checkedRubricIds: Set<String>) -> Unit,
    onSubmitProject: (workspaceCode: String, projectUrl: String, studentNotes: String, checkedRubricIds: Set<String>) -> Unit,
    onSimulateFacilitatorReview: (approved: Boolean, score: Int, feedback: String, onCertificateReady: (String?) -> Unit) -> Unit,
    onViewCertificate: () -> Unit,
    onAttachRealtimeListener: () -> com.google.firebase.firestore.ListenerRegistration? = { null },
    onFetchQueueInfo: suspend (submittedAt: Long) -> SubmissionQueueInfo? = { null },
    onSubmitRating: (rating: Int, comment: String, tags: List<String>, onDone: () -> Unit) -> Unit = { _, _, _, onDone -> onDone() },
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val coroutineScope = rememberCoroutineScope()

    val spec = remember(course) {
        course.finalProject ?: CourseCatalogParser.defaultFinalProjectSpecFor(
            course.id,
            course.title,
            course.description,
            course.trackId
        )
    }

    val status = remember(submission?.status) {
        ProjectSubmissionStatus.fromString(submission?.status)
    }

    var workspaceCode by remember(submission?.courseId) {
        mutableStateOf(submission?.workspaceCode?.ifBlank { spec.starterCode } ?: spec.starterCode)
    }
    var projectUrl by remember(submission?.courseId) {
        mutableStateOf(submission?.projectUrl.orEmpty())
    }
    var studentNotes by remember(submission?.courseId) {
        mutableStateOf(submission?.studentNotes.orEmpty())
    }
    var checkedRubricIds by remember(submission?.courseId) {
        mutableStateOf(parseCheckedRubricSet(submission?.checkedRubricIdsJson))
    }

    var composeStep by remember(submission?.courseId) { mutableIntStateOf(0) }
    var isEditingRevision by remember(submission?.courseId) { mutableStateOf(false) }
    var isRatingOpen by remember(submission?.courseId) { mutableStateOf(false) }
    var uploadedFileName by remember(submission?.courseId) { mutableStateOf<String?>(null) }
    var uploadedFileSizeKb by remember(submission?.courseId) { mutableIntStateOf(0) }
    var showPreviewDialog by remember { mutableStateOf(false) }

    var queueInfo by remember(submission?.courseId) { mutableStateOf<SubmissionQueueInfo?>(null) }
    var isCheckingQueue by remember { mutableStateOf(false) }

    // Sync local form fields when submission initially loads from Room
    LaunchedEffect(submission?.submissionId, submission?.updatedAtOrSubmitted()) {
        if (submission != null) {
            if (workspaceCode.isBlank() && submission.workspaceCode.isNotBlank()) {
                workspaceCode = submission.workspaceCode
            }
            if (projectUrl.isBlank() && submission.projectUrl.isNotBlank()) {
                projectUrl = submission.projectUrl
            }
            if (studentNotes.isBlank() && submission.studentNotes.isNotBlank()) {
                studentNotes = submission.studentNotes
            }
            val parsed = parseCheckedRubricSet(submission.checkedRubricIdsJson)
            if (checkedRubricIds.isEmpty() && parsed.isNotEmpty()) {
                checkedRubricIds = parsed
            }
        }
    }

    // Reset sub-modes when submission status transitions remotely or after submitting
    LaunchedEffect(status) {
        if (status != ProjectSubmissionStatus.REVISION_REQUESTED) {
            isEditingRevision = false
        }
        if (status != ProjectSubmissionStatus.APPROVED) {
            isRatingOpen = false
        }
    }

    // Fetch live queue position whenever entering PENDING_REVIEW
    LaunchedEffect(status, submission?.submittedAt) {
        if (status == ProjectSubmissionStatus.PENDING_REVIEW) {
            isCheckingQueue = true
            queueInfo = onFetchQueueInfo(submission?.submittedAt ?: System.currentTimeMillis())
            isCheckingQueue = false
        }
    }

    // Real-time Firestore listener so when Facilitator Web approves/requests revision, UI updates live
    DisposableEffect(course.id) {
        val registration = onAttachRealtimeListener()
        onDispose {
            registration?.remove()
        }
    }

    val viewMode = when {
        isRatingOpen && status == ProjectSubmissionStatus.APPROVED -> SubmissionViewMode.RATING
        status == ProjectSubmissionStatus.APPROVED -> SubmissionViewMode.APPROVED
        status == ProjectSubmissionStatus.PENDING_REVIEW -> SubmissionViewMode.WAITING
        status == ProjectSubmissionStatus.REVISION_REQUESTED && !isEditingRevision -> SubmissionViewMode.REVISION
        else -> SubmissionViewMode.COMPOSE
    }

    val toastDraftSaved = stringResource(R.string.final_project_toast_draft_saved)
    val toastCheckAllRubrics = stringResource(R.string.final_project_toast_check_all_rubrics)
    val toastSubmitted = stringResource(R.string.final_project_toast_submitted)
    val toastUploadError = stringResource(R.string.final_project_upload_error)
    val isUrlInvalid = projectUrl.isNotBlank() &&
        !projectUrl.trim().startsWith("https://", ignoreCase = true) &&
        !projectUrl.trim().startsWith("http://", ignoreCase = true)

    val projectFilePicker = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.OpenDocument()
    ) { uri ->
        if (uri != null) {
            coroutineScope.launch {
                val result = readUploadedProjectFile(context, uri)
                if (result != null) {
                    val (fileName, sizeKb, extractedCode) = result
                    workspaceCode = extractedCode
                    uploadedFileName = fileName
                    uploadedFileSizeKb = sizeKb
                    Toast.makeText(
                        context,
                        context.getString(R.string.final_project_upload_success_format, fileName, sizeKb),
                        Toast.LENGTH_SHORT
                    ).show()
                } else {
                    Toast.makeText(context, toastUploadError, Toast.LENGTH_LONG).show()
                }
            }
        }
    }

    val handleTopBack: () -> Unit = {
        when {
            viewMode == SubmissionViewMode.RATING -> isRatingOpen = false
            viewMode == SubmissionViewMode.COMPOSE && composeStep > 0 -> composeStep -= 1
            viewMode == SubmissionViewMode.COMPOSE && isEditingRevision -> isEditingRevision = false
            else -> onBack()
        }
    }

    Scaffold(
        containerColor = MaterialTheme.colorScheme.background,
        topBar = {
            TopAppBar(
                title = {
                    Column {
                        Text(
                            text = if (viewMode == SubmissionViewMode.RATING) {
                                stringResource(R.string.sub_rating_top_title)
                            } else {
                                stringResource(R.string.final_project_screen_title)
                            },
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.SemiBold
                        )
                        Text(
                            text = DataLabelMappings.courseTitle(course),
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )
                    }
                },
                navigationIcon = {
                    IconButton(onClick = handleTopBack) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = stringResource(R.string.class_detail_back_cd)
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.background
                )
            )
        },
        bottomBar = {
            if (viewMode == SubmissionViewMode.COMPOSE) {
                MinimalistComposeBottomBar(
                    step = composeStep,
                    isRevision = status == ProjectSubmissionStatus.REVISION_REQUESTED,
                    onSaveDraft = {
                        onSaveDraft(workspaceCode, projectUrl, studentNotes, checkedRubricIds)
                        Toast.makeText(context, toastDraftSaved, Toast.LENGTH_SHORT).show()
                    },
                    onPrevStep = { composeStep = (composeStep - 1).coerceAtLeast(0) },
                    onNextStep = { composeStep = (composeStep + 1).coerceAtMost(2) },
                    onSubmit = {
                        val allRubricsChecked = spec.rubrics.all { it.id in checkedRubricIds }
                        val hasContent = workspaceCode.isNotBlank() || projectUrl.isNotBlank()
                        if (isUrlInvalid) {
                            Toast.makeText(
                                context,
                                context.getString(R.string.final_project_url_invalid),
                                Toast.LENGTH_SHORT
                            ).show()
                        } else if (!allRubricsChecked || !hasContent) {
                            Toast.makeText(context, toastCheckAllRubrics, Toast.LENGTH_SHORT).show()
                        } else {
                            isEditingRevision = false
                            onSubmitProject(
                                workspaceCode,
                                projectUrl.trim(),
                                studentNotes.trim(),
                                checkedRubricIds
                            )
                            Toast.makeText(context, toastSubmitted, Toast.LENGTH_SHORT).show()
                        }
                    }
                )
            }
        },
        modifier = modifier
    ) { innerPadding ->
        AnimatedContent(
            targetState = viewMode,
            transitionSpec = {
                (fadeIn(tween(240)) + slideInVertically(tween(240)) { it / 18 })
                    .togetherWith(fadeOut(tween(180)))
            },
            label = "submission_view_mode",
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) { mode ->
            when (mode) {
                SubmissionViewMode.COMPOSE -> {
                    SubmissionComposeWizard(
                        course = course,
                        spec = spec,
                        submission = submission,
                        step = composeStep,
                        onStepChange = { composeStep = it },
                        workspaceCode = workspaceCode,
                        onWorkspaceCodeChange = { workspaceCode = it },
                        projectUrl = projectUrl,
                        onProjectUrlChange = { projectUrl = it },
                        isUrlInvalid = isUrlInvalid,
                        studentNotes = studentNotes,
                        onStudentNotesChange = { studentNotes = it },
                        checkedRubricIds = checkedRubricIds,
                        onToggleRubric = { id ->
                            checkedRubricIds = if (id in checkedRubricIds) {
                                checkedRubricIds - id
                            } else {
                                checkedRubricIds + id
                            }
                        },
                        uploadedFileName = uploadedFileName,
                        uploadedFileSizeKb = uploadedFileSizeKb,
                        onClearUpload = {
                            uploadedFileName = null
                            uploadedFileSizeKb = 0
                        },
                        onPickFile = {
                            projectFilePicker.launch(
                                arrayOf(
                                    "text/html",
                                    "text/css",
                                    "text/javascript",
                                    "application/javascript",
                                    "application/zip",
                                    "application/x-zip-compressed",
                                    "text/plain",
                                    "*/*"
                                )
                            )
                        },
                        onLoadPlayground = {
                            coroutineScope.launch {
                                val draft = onLoadPlaygroundDraft()
                                if (!draft.isNullOrBlank()) {
                                    workspaceCode = draft
                                }
                            }
                        },
                        onResetStarter = { workspaceCode = spec.starterCode },
                        onOpenPreview = { showPreviewDialog = true },
                        isRevision = status == ProjectSubmissionStatus.REVISION_REQUESTED
                    )
                }

                SubmissionViewMode.WAITING -> {
                    SubmissionWaitingQueueScreen(
                        submission = submission,
                        queueInfo = queueInfo,
                        isCheckingQueue = isCheckingQueue,
                        onRefreshQueue = {
                            coroutineScope.launch {
                                isCheckingQueue = true
                                queueInfo = onFetchQueueInfo(
                                    submission?.submittedAt ?: System.currentTimeMillis()
                                )
                                isCheckingQueue = false
                            }
                        },
                        onPreviewCode = { showPreviewDialog = true },
                        onBackToCourse = onBack
                    )
                }

                SubmissionViewMode.REVISION -> {
                    SubmissionRevisionScreen(
                        course = course,
                        submission = submission,
                        onEditAndResubmit = {
                            composeStep = 0
                            isEditingRevision = true
                        },
                        onPreviewCode = { showPreviewDialog = true }
                    )
                }

                SubmissionViewMode.APPROVED -> {
                    SubmissionApprovedScreen(
                        course = course,
                        submission = submission,
                        onViewCertificate = onViewCertificate,
                        onOpenRating = { isRatingOpen = true }
                    )
                }

                SubmissionViewMode.RATING -> {
                    SubmissionRatingScreen(
                        reviewerName = submission?.facilitatorName?.takeIf { it.isNotBlank() }
                            ?: course.instructor,
                        initialRating = submission?.studentRating ?: 0,
                        initialComment = submission?.studentRatingComment.orEmpty(),
                        initialTags = parseCheckedRubricSet(submission?.studentRatingTagsJson),
                        onSubmit = { rating, comment, tags ->
                            onSubmitRating(rating, comment, tags) {
                                Toast.makeText(
                                    context,
                                    context.getString(R.string.sub_rating_toast_saved),
                                    Toast.LENGTH_SHORT
                                ).show()
                                isRatingOpen = false
                            }
                        },
                        onSkip = { isRatingOpen = false }
                    )
                }
            }
        }
    }

    // Live Project Preview Dialog (renders student's HTML/CSS/JS before/after submission)
    if (showPreviewDialog) {
        val codeToPreview = workspaceCode.ifBlank { submission?.workspaceCode.orEmpty() }
        val previewHtml = remember(codeToPreview) {
            val trimmed = codeToPreview.trim()
            if (trimmed.contains("<html", ignoreCase = true) ||
                trimmed.contains("<body", ignoreCase = true) ||
                trimmed.contains("<div", ignoreCase = true) ||
                trimmed.contains("<header", ignoreCase = true)
            ) {
                trimmed
            } else {
                "<!DOCTYPE html><html><head><meta charset=\"UTF-8\"><style>body{font-family:sans-serif;padding:16px;}</style></head><body><pre id=\"out\"></pre><script>try{ $trimmed }catch(e){document.getElementById('out').textContent=e.toString();}</script></body></html>"
            }
        }
        Dialog(
            onDismissRequest = { showPreviewDialog = false },
            properties = DialogProperties(usePlatformDefaultWidth = false)
        ) {
            Surface(
                shape = RoundedCornerShape(20.dp),
                color = MaterialTheme.colorScheme.surface,
                modifier = Modifier
                    .fillMaxWidth(0.94f)
                    .fillMaxHeight(0.82f)
            ) {
                Column(modifier = Modifier.fillMaxSize()) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.4f))
                            .padding(horizontal = 16.dp, vertical = 10.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = stringResource(R.string.final_project_preview_title),
                            style = MaterialTheme.typography.titleSmall,
                            fontWeight = FontWeight.Bold
                        )
                        IconButton(
                            onClick = { showPreviewDialog = false },
                            modifier = Modifier.size(32.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.Close,
                                contentDescription = stringResource(R.string.common_close)
                            )
                        }
                    }
                    CodeSandboxWebView(
                        html = previewHtml,
                        css = "",
                        js = "",
                        runId = 1,
                        onConsoleLog = {},
                        modifier = Modifier
                            .fillMaxWidth()
                            .weight(1f)
                    )
                }
            }
        }
    }
}

// ============================================================================
// SCREEN 1: 3-STEP MINIMALIST COMPOSE WIZARD
// ============================================================================

@Composable
private fun MinimalistComposeBottomBar(
    step: Int,
    isRevision: Boolean,
    onSaveDraft: () -> Unit,
    onPrevStep: () -> Unit,
    onNextStep: () -> Unit,
    onSubmit: () -> Unit
) {
    Surface(
        color = MaterialTheme.colorScheme.surface,
        tonalElevation = 1.dp,
        shadowElevation = 6.dp,
        modifier = Modifier.fillMaxWidth()
    ) {
        Column {
            HorizontalDivider(color = MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.4f))
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp, vertical = 12.dp),
                horizontalArrangement = Arrangement.spacedBy(12.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                if (step == 0) {
                    OutlinedButton(
                        onClick = onSaveDraft,
                        shape = RoundedCornerShape(14.dp),
                        border = BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant),
                        contentPadding = PaddingValues(vertical = 13.dp, horizontal = 14.dp),
                        modifier = Modifier.weight(0.9f)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Save,
                            contentDescription = null,
                            modifier = Modifier.size(16.dp)
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = stringResource(R.string.final_project_btn_save_draft),
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis,
                            fontWeight = FontWeight.Medium
                        )
                    }
                } else {
                    OutlinedButton(
                        onClick = onPrevStep,
                        shape = RoundedCornerShape(14.dp),
                        border = BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant),
                        contentPadding = PaddingValues(vertical = 13.dp, horizontal = 14.dp),
                        modifier = Modifier.weight(0.85f)
                    ) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = null,
                            modifier = Modifier.size(16.dp)
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = stringResource(R.string.sub_btn_prev),
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis,
                            fontWeight = FontWeight.Medium
                        )
                    }
                }

                if (step < 2) {
                    Button(
                        onClick = onNextStep,
                        colors = ButtonDefaults.buttonColors(containerColor = TealPrimary),
                        shape = RoundedCornerShape(14.dp),
                        contentPadding = PaddingValues(vertical = 13.dp, horizontal = 16.dp),
                        modifier = Modifier.weight(1.3f)
                    ) {
                        Text(
                            text = stringResource(R.string.sub_btn_next),
                            color = Color.White,
                            fontWeight = FontWeight.SemiBold,
                            maxLines = 1
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowForward,
                            contentDescription = null,
                            tint = Color.White,
                            modifier = Modifier.size(16.dp)
                        )
                    }
                } else {
                    Button(
                        onClick = onSubmit,
                        colors = ButtonDefaults.buttonColors(containerColor = TealPrimary),
                        shape = RoundedCornerShape(14.dp),
                        contentPadding = PaddingValues(vertical = 13.dp, horizontal = 16.dp),
                        modifier = Modifier
                            .weight(1.4f)
                            .testTag("final_project_submit_btn")
                    ) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.Send,
                            contentDescription = null,
                            tint = Color.White,
                            modifier = Modifier.size(16.dp)
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = if (isRevision) {
                                stringResource(R.string.final_project_btn_resubmit)
                            } else {
                                stringResource(R.string.final_project_btn_submit)
                            },
                            color = Color.White,
                            fontWeight = FontWeight.SemiBold,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun SubmissionComposeWizard(
    course: Course,
    spec: FinalProjectSpec,
    submission: ProjectSubmissionEntity?,
    step: Int,
    onStepChange: (Int) -> Unit,
    workspaceCode: String,
    onWorkspaceCodeChange: (String) -> Unit,
    projectUrl: String,
    onProjectUrlChange: (String) -> Unit,
    isUrlInvalid: Boolean,
    studentNotes: String,
    onStudentNotesChange: (String) -> Unit,
    checkedRubricIds: Set<String>,
    onToggleRubric: (String) -> Unit,
    uploadedFileName: String?,
    uploadedFileSizeKb: Int,
    onClearUpload: () -> Unit,
    onPickFile: () -> Unit,
    onLoadPlayground: () -> Unit,
    onResetStarter: () -> Unit,
    onOpenPreview: () -> Unit,
    isRevision: Boolean
) {
    Column(modifier = Modifier.fillMaxSize()) {
        // Slim minimalist step indicator header
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp, vertical = 10.dp)
        ) {
            val stepTitles = listOf(
                stringResource(R.string.sub_step_1),
                stringResource(R.string.sub_step_2),
                stringResource(R.string.sub_step_3)
            )
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                stepTitles.forEachIndexed { idx, label ->
                    val isActive = idx == step
                    val isDone = idx < step
                    Surface(
                        onClick = { onStepChange(idx) },
                        shape = RoundedCornerShape(10.dp),
                        color = when {
                            isActive -> TealPrimary.copy(alpha = 0.12f)
                            isDone -> SuccessGreen.copy(alpha = 0.08f)
                            else -> MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.45f)
                        },
                        border = BorderStroke(
                            width = 1.dp,
                            color = when {
                                isActive -> TealPrimary.copy(alpha = 0.45f)
                                isDone -> SuccessGreen.copy(alpha = 0.3f)
                                else -> Color.Transparent
                            }
                        ),
                        modifier = Modifier.weight(1f)
                    ) {
                        Row(
                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 8.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.Center
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(18.dp)
                                    .clip(CircleShape)
                                    .background(
                                        when {
                                            isActive -> TealPrimary
                                            isDone -> SuccessGreen
                                            else -> MaterialTheme.colorScheme.outlineVariant
                                        }
                                    ),
                                contentAlignment = Alignment.Center
                            ) {
                                if (isDone) {
                                    Icon(
                                        imageVector = Icons.Default.Check,
                                        contentDescription = null,
                                        tint = Color.White,
                                        modifier = Modifier.size(11.dp)
                                    )
                                } else {
                                    Text(
                                        text = "${idx + 1}",
                                        fontSize = 10.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = Color.White
                                    )
                                }
                            }
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(
                                text = label,
                                style = MaterialTheme.typography.labelSmall,
                                fontWeight = if (isActive) FontWeight.Bold else FontWeight.Medium,
                                color = if (isActive) TealPrimary else MaterialTheme.colorScheme.onSurfaceVariant,
                                maxLines = 1,
                                overflow = TextOverflow.Ellipsis
                            )
                        }
                    }
                }
            }
            Spacer(modifier = Modifier.height(10.dp))
            LinearProgressIndicator(
                progress = { (step + 1) / 3f },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(3.dp)
                    .clip(RoundedCornerShape(999.dp)),
                color = TealPrimary,
                trackColor = MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.35f)
            )
        }

        // Animated step body
        AnimatedContent(
            targetState = step,
            transitionSpec = { fadeIn(tween(180)).togetherWith(fadeOut(tween(120))) },
            label = "compose_wizard_step",
            modifier = Modifier.weight(1f)
        ) { currentStep ->
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .imePadding()
                    .padding(horizontal = 20.dp),
                contentPadding = PaddingValues(top = 8.dp, bottom = 28.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                when (currentStep) {
                    0 -> {
                        // Optional slim revision reminder banner
                        if (isRevision && !submission?.facilitatorFeedback.isNullOrBlank()) {
                            item(key = "revision_reminder") {
                                Surface(
                                    shape = RoundedCornerShape(14.dp),
                                    color = FlameOrange.copy(alpha = 0.08f),
                                    border = BorderStroke(1.dp, FlameOrange.copy(alpha = 0.3f)),
                                    modifier = Modifier.fillMaxWidth()
                                ) {
                                    Column(modifier = Modifier.padding(14.dp)) {
                                        Text(
                                            text = stringResource(R.string.sub_rev_feedback_label),
                                            style = MaterialTheme.typography.labelSmall,
                                            fontWeight = FontWeight.Bold,
                                            color = FlameOrange
                                        )
                                        Spacer(modifier = Modifier.height(4.dp))
                                        Text(
                                            text = submission?.facilitatorFeedback.orEmpty(),
                                            style = MaterialTheme.typography.bodySmall,
                                            color = MaterialTheme.colorScheme.onSurface
                                        )
                                    }
                                }
                            }
                        }

                        // Project title & meta header
                        item(key = "brief_header") {
                            Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                                Text(
                                    text = stringResource(R.string.sub_step_counter_format, 1),
                                    style = MaterialTheme.typography.labelSmall,
                                    color = TealPrimary,
                                    fontWeight = FontWeight.SemiBold
                                )
                                Text(
                                    text = DataLabelMappings.finalProjectTitle(course.id, spec.title),
                                    style = MaterialTheme.typography.titleLarge,
                                    fontWeight = FontWeight.Bold,
                                    color = MaterialTheme.colorScheme.onSurface
                                )
                                Text(
                                    text = stringResource(
                                        R.string.final_project_attempt_badge,
                                        submission?.attemptNumber ?: 1,
                                        spec.minPassingScore,
                                        spec.xpReward
                                    ),
                                    style = MaterialTheme.typography.bodySmall,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                            }
                        }

                        // Case Study Card
                        item(key = "scenario_card") {
                            Surface(
                                shape = RoundedCornerShape(16.dp),
                                color = MaterialTheme.colorScheme.surface,
                                border = BorderStroke(
                                    1.dp,
                                    MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.55f)
                                ),
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Column(modifier = Modifier.padding(18.dp)) {
                                    Text(
                                        text = stringResource(R.string.final_project_scenario_header),
                                        style = MaterialTheme.typography.labelLarge,
                                        fontWeight = FontWeight.SemiBold,
                                        color = TealPrimary
                                    )
                                    Spacer(modifier = Modifier.height(8.dp))
                                    Text(
                                        text = DataLabelMappings.finalProjectScenario(course.id, spec.scenario),
                                        style = MaterialTheme.typography.bodyMedium,
                                        color = MaterialTheme.colorScheme.onSurface,
                                        lineHeight = 22.sp
                                    )
                                }
                            }
                        }

                        // Self-Check Rubric Card
                        item(key = "rubrics_card") {
                            val rubricTotal = spec.rubrics.size.coerceAtLeast(1)
                            val rubricDone = spec.rubrics.count { it.id in checkedRubricIds }
                            Surface(
                                shape = RoundedCornerShape(16.dp),
                                color = MaterialTheme.colorScheme.surface,
                                border = BorderStroke(
                                    1.dp,
                                    MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.55f)
                                ),
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Column(modifier = Modifier.padding(18.dp)) {
                                    Row(
                                        modifier = Modifier.fillMaxWidth(),
                                        horizontalArrangement = Arrangement.SpaceBetween,
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        Text(
                                            text = stringResource(R.string.final_project_rubric_header),
                                            style = MaterialTheme.typography.labelLarge,
                                            fontWeight = FontWeight.SemiBold,
                                            color = MaterialTheme.colorScheme.onSurface
                                        )
                                        Text(
                                            text = "$rubricDone/$rubricTotal",
                                            style = MaterialTheme.typography.labelMedium,
                                            fontWeight = FontWeight.Bold,
                                            color = if (rubricDone == rubricTotal) SuccessGreen else TealPrimary
                                        )
                                    }
                                    Spacer(modifier = Modifier.height(4.dp))
                                    Text(
                                        text = stringResource(R.string.final_project_rubric_subtitle),
                                        style = MaterialTheme.typography.bodySmall,
                                        color = MaterialTheme.colorScheme.onSurfaceVariant
                                    )
                                    Spacer(modifier = Modifier.height(12.dp))
                                    LinearProgressIndicator(
                                        progress = { rubricDone.toFloat() / rubricTotal.toFloat() },
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .height(5.dp)
                                            .clip(RoundedCornerShape(999.dp)),
                                        color = if (rubricDone == rubricTotal) SuccessGreen else TealPrimary,
                                        trackColor = MaterialTheme.colorScheme.surfaceVariant
                                    )
                                    Spacer(modifier = Modifier.height(12.dp))

                                    spec.rubrics.forEachIndexed { idx, rubric ->
                                        val isChecked = rubric.id in checkedRubricIds
                                        Row(
                                            modifier = Modifier
                                                .fillMaxWidth()
                                                .clip(RoundedCornerShape(10.dp))
                                                .clickable { onToggleRubric(rubric.id) }
                                                .padding(vertical = 6.dp),
                                            verticalAlignment = Alignment.Top
                                        ) {
                                            Checkbox(
                                                checked = isChecked,
                                                onCheckedChange = { onToggleRubric(rubric.id) },
                                                colors = CheckboxDefaults.colors(checkedColor = TealPrimary)
                                            )
                                            Spacer(modifier = Modifier.width(6.dp))
                                            Column(modifier = Modifier.weight(1f)) {
                                                Row(
                                                    modifier = Modifier.fillMaxWidth(),
                                                    horizontalArrangement = Arrangement.SpaceBetween,
                                                    verticalAlignment = Alignment.CenterVertically
                                                ) {
                                                    Text(
                                                        text = DataLabelMappings.rubricTitle(
                                                            course.id,
                                                            rubric.id,
                                                            rubric.title
                                                        ),
                                                        style = MaterialTheme.typography.bodyMedium,
                                                        fontWeight = FontWeight.Medium,
                                                        color = MaterialTheme.colorScheme.onSurface,
                                                        modifier = Modifier.weight(1f)
                                                    )
                                                    Spacer(modifier = Modifier.width(8.dp))
                                                    Text(
                                                        text = stringResource(
                                                            R.string.final_project_rubric_weight_format,
                                                            rubric.weightPercent
                                                        ),
                                                        style = MaterialTheme.typography.labelSmall,
                                                        color = TealPrimary,
                                                        fontWeight = FontWeight.SemiBold
                                                    )
                                                }
                                                Spacer(modifier = Modifier.height(2.dp))
                                                Text(
                                                    text = DataLabelMappings.rubricDescription(
                                                        course.id,
                                                        rubric.id,
                                                        rubric.description
                                                    ),
                                                    style = MaterialTheme.typography.bodySmall,
                                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                                )
                                            }
                                        }
                                        if (idx < spec.rubrics.lastIndex) {
                                            HorizontalDivider(
                                                color = MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.35f)
                                            )
                                        }
                                    }
                                }
                            }
                        }
                    }

                    1 -> {
                        item(key = "code_step_header") {
                            Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                                Text(
                                    text = stringResource(R.string.sub_step_counter_format, 2),
                                    style = MaterialTheme.typography.labelSmall,
                                    color = TealPrimary,
                                    fontWeight = FontWeight.SemiBold
                                )
                                Text(
                                    text = stringResource(R.string.final_project_workspace_header),
                                    style = MaterialTheme.typography.titleLarge,
                                    fontWeight = FontWeight.Bold
                                )
                                Text(
                                    text = stringResource(R.string.final_project_workspace_subtitle),
                                    style = MaterialTheme.typography.bodySmall,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                            }
                        }

                        // Quick actions: Load Playground & Starter
                        item(key = "code_actions") {
                            Row(
                                horizontalArrangement = Arrangement.spacedBy(10.dp),
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                OutlinedButton(
                                    onClick = onLoadPlayground,
                                    shape = RoundedCornerShape(12.dp),
                                    border = BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant),
                                    modifier = Modifier.weight(1f)
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.Code,
                                        contentDescription = null,
                                        modifier = Modifier.size(15.dp)
                                    )
                                    Spacer(modifier = Modifier.width(6.dp))
                                    Text(
                                        text = stringResource(R.string.final_project_btn_load_playground),
                                        fontSize = 12.sp,
                                        maxLines = 1,
                                        overflow = TextOverflow.Ellipsis
                                    )
                                }
                                OutlinedButton(
                                    onClick = onResetStarter,
                                    shape = RoundedCornerShape(12.dp),
                                    border = BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant),
                                    modifier = Modifier.weight(1f)
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.Restore,
                                        contentDescription = null,
                                        modifier = Modifier.size(15.dp)
                                    )
                                    Spacer(modifier = Modifier.width(6.dp))
                                    Text(
                                        text = stringResource(R.string.final_project_btn_load_starter),
                                        fontSize = 12.sp,
                                        maxLines = 1,
                                        overflow = TextOverflow.Ellipsis
                                    )
                                }
                            }
                        }

                        // Minimalist File / ZIP Upload Zone
                        item(key = "upload_zone") {
                            Surface(
                                onClick = onPickFile,
                                shape = RoundedCornerShape(16.dp),
                                color = MaterialTheme.colorScheme.surface,
                                border = BorderStroke(1.dp, TealPrimary.copy(alpha = 0.35f)),
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Row(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(16.dp),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Box(
                                        modifier = Modifier
                                            .size(40.dp)
                                            .clip(RoundedCornerShape(12.dp))
                                            .background(TealPrimary.copy(alpha = 0.1f)),
                                        contentAlignment = Alignment.Center
                                    ) {
                                        Icon(
                                            imageVector = Icons.Default.UploadFile,
                                            contentDescription = null,
                                            tint = TealPrimary,
                                            modifier = Modifier.size(20.dp)
                                        )
                                    }
                                    Spacer(modifier = Modifier.width(12.dp))
                                    Column(modifier = Modifier.weight(1f)) {
                                        Text(
                                            text = stringResource(R.string.final_project_upload_title),
                                            style = MaterialTheme.typography.labelLarge,
                                            fontWeight = FontWeight.SemiBold
                                        )
                                        Text(
                                            text = stringResource(R.string.final_project_upload_subtitle),
                                            style = MaterialTheme.typography.bodySmall,
                                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                                            maxLines = 2
                                        )
                                    }
                                    Spacer(modifier = Modifier.width(8.dp))
                                    Surface(
                                        shape = RoundedCornerShape(8.dp),
                                        color = TealPrimary.copy(alpha = 0.12f)
                                    ) {
                                        Text(
                                            text = stringResource(R.string.final_project_upload_btn),
                                            style = MaterialTheme.typography.labelSmall,
                                            fontWeight = FontWeight.Bold,
                                            color = TealPrimary,
                                            modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp)
                                        )
                                    }
                                }
                            }

                            AnimatedVisibility(visible = uploadedFileName != null) {
                                uploadedFileName?.let { fname ->
                                    Surface(
                                        shape = RoundedCornerShape(12.dp),
                                        color = SuccessGreen.copy(alpha = 0.1f),
                                        border = BorderStroke(1.dp, SuccessGreen.copy(alpha = 0.3f)),
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .padding(top = 10.dp)
                                    ) {
                                        Row(
                                            modifier = Modifier
                                                .fillMaxWidth()
                                                .padding(horizontal = 12.dp, vertical = 8.dp),
                                            verticalAlignment = Alignment.CenterVertically
                                        ) {
                                            Icon(
                                                imageVector = Icons.Default.Description,
                                                contentDescription = null,
                                                tint = SuccessGreen,
                                                modifier = Modifier.size(18.dp)
                                            )
                                            Spacer(modifier = Modifier.width(8.dp))
                                            Text(
                                                text = "$fname ($uploadedFileSizeKb KB)",
                                                style = MaterialTheme.typography.bodySmall,
                                                fontWeight = FontWeight.Medium,
                                                maxLines = 1,
                                                overflow = TextOverflow.Ellipsis,
                                                modifier = Modifier.weight(1f)
                                            )
                                            IconButton(
                                                onClick = onClearUpload,
                                                modifier = Modifier.size(26.dp)
                                            ) {
                                                Icon(
                                                    imageVector = Icons.Default.Close,
                                                    contentDescription = stringResource(R.string.final_project_upload_remove_cd),
                                                    tint = MaterialTheme.colorScheme.onSurfaceVariant,
                                                    modifier = Modifier.size(16.dp)
                                                )
                                            }
                                        }
                                    }
                                }
                            }
                        }

                        // Dark IDE Editor Container
                        item(key = "ide_editor") {
                            Surface(
                                shape = RoundedCornerShape(16.dp),
                                color = Color(0xFF0F172A),
                                border = BorderStroke(1.dp, Color(0xFF1E293B)),
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Column {
                                    Row(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .background(Color(0xFF1E293B))
                                            .padding(horizontal = 14.dp, vertical = 10.dp),
                                        horizontalArrangement = Arrangement.SpaceBetween,
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        Row(
                                            horizontalArrangement = Arrangement.spacedBy(6.dp),
                                            verticalAlignment = Alignment.CenterVertically
                                        ) {
                                            Box(Modifier.size(8.dp).clip(CircleShape).background(Color(0xFFEF4444)))
                                            Box(Modifier.size(8.dp).clip(CircleShape).background(Color(0xFFF59E0B)))
                                            Box(Modifier.size(8.dp).clip(CircleShape).background(Color(0xFF10B981)))
                                            Spacer(modifier = Modifier.width(6.dp))
                                            Text(
                                                text = spec.language.uppercase(),
                                                fontSize = 10.sp,
                                                fontWeight = FontWeight.Bold,
                                                color = Color(0xFF94A3B8)
                                            )
                                        }
                                        Surface(
                                            onClick = onOpenPreview,
                                            shape = RoundedCornerShape(8.dp),
                                            color = TealPrimary
                                        ) {
                                            Row(
                                                verticalAlignment = Alignment.CenterVertically,
                                                modifier = Modifier.padding(horizontal = 10.dp, vertical = 5.dp)
                                            ) {
                                                Icon(
                                                    imageVector = Icons.Default.PlayArrow,
                                                    contentDescription = null,
                                                    tint = Color.White,
                                                    modifier = Modifier.size(14.dp)
                                                )
                                                Spacer(modifier = Modifier.width(4.dp))
                                                Text(
                                                    text = stringResource(R.string.final_project_preview_btn),
                                                    fontSize = 11.sp,
                                                    fontWeight = FontWeight.Bold,
                                                    color = Color.White
                                                )
                                            }
                                        }
                                    }
                                    OutlinedTextField(
                                        value = workspaceCode,
                                        onValueChange = onWorkspaceCodeChange,
                                        placeholder = {
                                            Text(
                                                text = stringResource(R.string.final_project_code_placeholder),
                                                color = Color(0xFF64748B)
                                            )
                                        },
                                        textStyle = MaterialTheme.typography.bodySmall.copy(
                                            fontFamily = FontFamily.Monospace,
                                            lineHeight = 19.sp,
                                            fontSize = 12.5.sp,
                                            color = Color(0xFFE2E8F0)
                                        ),
                                        colors = OutlinedTextFieldDefaults.colors(
                                            focusedContainerColor = Color(0xFF0F172A),
                                            unfocusedContainerColor = Color(0xFF0F172A),
                                            focusedBorderColor = Color.Transparent,
                                            unfocusedBorderColor = Color.Transparent,
                                            cursorColor = Color(0xFF38BDF8),
                                            focusedTextColor = Color(0xFFE2E8F0),
                                            unfocusedTextColor = Color(0xFFE2E8F0)
                                        ),
                                        minLines = 10,
                                        maxLines = 22,
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .testTag("final_project_code_input")
                                    )
                                }
                            }
                        }
                    }

                    2 -> {
                        item(key = "submit_step_header") {
                            Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                                Text(
                                    text = stringResource(R.string.sub_step_counter_format, 3),
                                    style = MaterialTheme.typography.labelSmall,
                                    color = TealPrimary,
                                    fontWeight = FontWeight.SemiBold
                                )
                                Text(
                                    text = stringResource(R.string.final_project_url_header),
                                    style = MaterialTheme.typography.titleLarge,
                                    fontWeight = FontWeight.Bold
                                )
                            }
                        }

                        // Link & Notes Card
                        item(key = "url_and_notes") {
                            Surface(
                                shape = RoundedCornerShape(16.dp),
                                color = MaterialTheme.colorScheme.surface,
                                border = BorderStroke(
                                    1.dp,
                                    MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.55f)
                                ),
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Column(modifier = Modifier.padding(18.dp)) {
                                    Row(verticalAlignment = Alignment.CenterVertically) {
                                        Icon(
                                            imageVector = Icons.Default.Link,
                                            contentDescription = null,
                                            tint = TealPrimary,
                                            modifier = Modifier.size(18.dp)
                                        )
                                        Spacer(modifier = Modifier.width(8.dp))
                                        Text(
                                            text = stringResource(R.string.final_project_url_header),
                                            style = MaterialTheme.typography.labelLarge,
                                            fontWeight = FontWeight.SemiBold
                                        )
                                    }
                                    Spacer(modifier = Modifier.height(8.dp))
                                    OutlinedTextField(
                                        value = projectUrl,
                                        onValueChange = onProjectUrlChange,
                                        singleLine = true,
                                        isError = isUrlInvalid,
                                        shape = RoundedCornerShape(12.dp),
                                        supportingText = if (isUrlInvalid) {
                                            { Text(stringResource(R.string.final_project_url_invalid)) }
                                        } else null,
                                        placeholder = {
                                            Text(stringResource(R.string.final_project_url_placeholder))
                                        },
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .testTag("final_project_url_input")
                                    )

                                    Spacer(modifier = Modifier.height(18.dp))

                                    Text(
                                        text = stringResource(R.string.final_project_notes_header),
                                        style = MaterialTheme.typography.labelLarge,
                                        fontWeight = FontWeight.SemiBold
                                    )
                                    Spacer(modifier = Modifier.height(8.dp))
                                    OutlinedTextField(
                                        value = studentNotes,
                                        onValueChange = onStudentNotesChange,
                                        minLines = 3,
                                        maxLines = 6,
                                        shape = RoundedCornerShape(12.dp),
                                        placeholder = {
                                            Text(stringResource(R.string.final_project_notes_placeholder))
                                        },
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .testTag("final_project_notes_input")
                                    )
                                }
                            }
                        }

                        // Pre-Submit Readiness Check Card
                        item(key = "readiness_card") {
                            val hasCodeOrUrl = workspaceCode.isNotBlank() ||
                                (projectUrl.isNotBlank() && !isUrlInvalid)
                            val hasStructuredCode = workspaceCode.contains("<") ||
                                workspaceCode.contains("{") ||
                                workspaceCode.contains("function", ignoreCase = true) ||
                                workspaceCode.contains("const ", ignoreCase = true) ||
                                workspaceCode.contains("let ", ignoreCase = true) ||
                                (projectUrl.isNotBlank() && !isUrlInvalid)
                            val checkedCount = spec.rubrics.count { it.id in checkedRubricIds }
                            val allRubricsDone = spec.rubrics.isNotEmpty() && checkedCount == spec.rubrics.size
                            val isAllReady = hasCodeOrUrl && hasStructuredCode && allRubricsDone

                            Surface(
                                shape = RoundedCornerShape(16.dp),
                                color = if (isAllReady) SuccessGreen.copy(alpha = 0.06f) else MaterialTheme.colorScheme.surface,
                                border = BorderStroke(
                                    1.dp,
                                    if (isAllReady) SuccessGreen.copy(alpha = 0.35f)
                                    else MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.55f)
                                ),
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Column(
                                    modifier = Modifier.padding(18.dp),
                                    verticalArrangement = Arrangement.spacedBy(10.dp)
                                ) {
                                    Row(
                                        verticalAlignment = Alignment.CenterVertically,
                                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                                    ) {
                                        Icon(
                                            imageVector = if (isAllReady) Icons.Default.CheckCircle else Icons.Default.AssignmentTurnedIn,
                                            contentDescription = null,
                                            tint = if (isAllReady) SuccessGreen else TealPrimary,
                                            modifier = Modifier.size(18.dp)
                                        )
                                        Text(
                                            text = stringResource(R.string.final_project_precheck_title),
                                            style = MaterialTheme.typography.labelLarge,
                                            fontWeight = FontWeight.SemiBold,
                                            color = if (isAllReady) SuccessGreen else MaterialTheme.colorScheme.onSurface
                                        )
                                    }

                                    listOf(
                                        hasCodeOrUrl to if (hasCodeOrUrl) {
                                            stringResource(R.string.final_project_precheck_code_ok)
                                        } else {
                                            stringResource(R.string.final_project_precheck_code_missing)
                                        },
                                        hasStructuredCode to if (hasStructuredCode) {
                                            stringResource(R.string.final_project_precheck_structure_ok)
                                        } else {
                                            stringResource(R.string.final_project_precheck_structure_hint)
                                        },
                                        allRubricsDone to stringResource(
                                            R.string.final_project_precheck_rubric_format,
                                            checkedCount,
                                            spec.rubrics.size
                                        )
                                    ).forEach { (passed, label) ->
                                        Row(
                                            verticalAlignment = Alignment.CenterVertically,
                                            horizontalArrangement = Arrangement.spacedBy(10.dp)
                                        ) {
                                            Icon(
                                                imageVector = if (passed) Icons.Default.CheckCircle else Icons.Default.RadioButtonUnchecked,
                                                contentDescription = null,
                                                tint = if (passed) SuccessGreen else MaterialTheme.colorScheme.onSurfaceVariant,
                                                modifier = Modifier.size(16.dp)
                                            )
                                            Text(
                                                text = label,
                                                style = MaterialTheme.typography.bodySmall,
                                                color = if (passed) MaterialTheme.colorScheme.onSurface
                                                else MaterialTheme.colorScheme.onSurfaceVariant
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
}

// ============================================================================
// SCREEN 2: ANIMATED WAITING & LIVE QUEUE SCREEN (PENDING_REVIEW)
// ============================================================================

@Composable
private fun SubmissionWaitingQueueScreen(
    submission: ProjectSubmissionEntity?,
    queueInfo: SubmissionQueueInfo?,
    isCheckingQueue: Boolean,
    onRefreshQueue: () -> Unit,
    onPreviewCode: () -> Unit,
    onBackToCourse: () -> Unit
) {
    val infiniteTransition = rememberInfiniteTransition(label = "queue_pulse")
    val pulseScale by infiniteTransition.animateFloat(
        initialValue = 0.85f,
        targetValue = 1.18f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 2200, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "pulse_scale"
    )
    val orbitAngle by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 360f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 5200, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "orbit_angle"
    )

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(horizontal = 24.dp, vertical = 20.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Spacer(modifier = Modifier.height(8.dp))

        // Concentric pulsing orbital emblem
        Box(
            modifier = Modifier.size(164.dp),
            contentAlignment = Alignment.Center
        ) {
            Canvas(modifier = Modifier.fillMaxSize()) {
                val center = Offset(size.width / 2f, size.height / 2f)
                val maxR = size.minDimension / 2f

                // Outer soft pulsing ring
                drawCircle(
                    color = TealPrimary.copy(alpha = 0.08f),
                    radius = maxR * 0.86f * pulseScale.coerceAtMost(1.08f),
                    center = center
                )
                // Orbit track hairline
                drawCircle(
                    color = TealPrimary.copy(alpha = 0.22f),
                    radius = maxR * 0.74f,
                    center = center,
                    style = Stroke(width = 1.5.dp.toPx())
                )
                // Inner warm halo
                drawCircle(
                    color = AmberGold.copy(alpha = 0.12f),
                    radius = maxR * 0.52f,
                    center = center
                )
                // Orbiting luminous dot
                val rad = Math.toRadians(orbitAngle.toDouble())
                val dotX = center.x + (maxR * 0.74f) * cos(rad).toFloat()
                val dotY = center.y + (maxR * 0.74f) * sin(rad).toFloat()
                drawCircle(
                    color = TealPrimary,
                    radius = 5.dp.toPx(),
                    center = Offset(dotX, dotY)
                )
            }

            Surface(
                shape = CircleShape,
                color = MaterialTheme.colorScheme.surface,
                shadowElevation = 6.dp,
                border = BorderStroke(1.dp, TealPrimary.copy(alpha = 0.25f)),
                modifier = Modifier.size(72.dp)
            ) {
                Box(contentAlignment = Alignment.Center) {
                    Icon(
                        imageVector = Icons.Default.HourglassTop,
                        contentDescription = null,
                        tint = TealPrimary,
                        modifier = Modifier.size(32.dp)
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(22.dp))

        Text(
            text = stringResource(R.string.sub_wait_kicker),
            style = MaterialTheme.typography.labelSmall,
            fontWeight = FontWeight.Bold,
            letterSpacing = 1.4.sp,
            color = TealPrimary
        )

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = stringResource(R.string.sub_wait_title),
            style = MaterialTheme.typography.headlineSmall,
            fontWeight = FontWeight.Bold,
            textAlign = TextAlign.Center,
            color = MaterialTheme.colorScheme.onSurface
        )

        Spacer(modifier = Modifier.height(10.dp))

        Text(
            text = stringResource(R.string.sub_wait_desc),
            style = MaterialTheme.typography.bodyMedium,
            textAlign = TextAlign.Center,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            lineHeight = 22.sp,
            modifier = Modifier.padding(horizontal = 8.dp)
        )

        Spacer(modifier = Modifier.height(24.dp))

        // Live Queue Position Pill Card
        Surface(
            shape = RoundedCornerShape(18.dp),
            color = MaterialTheme.colorScheme.surface,
            border = BorderStroke(1.dp, TealPrimary.copy(alpha = 0.35f)),
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(
                modifier = Modifier.padding(18.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .size(9.dp)
                            .clip(CircleShape)
                            .background(if (queueInfo != null) SuccessGreen else AmberGold)
                    )
                    Text(
                        text = when {
                            isCheckingQueue -> stringResource(R.string.sub_wait_queue_checking)
                            queueInfo != null -> stringResource(
                                R.string.sub_wait_queue_badge_format,
                                queueInfo.position,
                                queueInfo.totalPending
                            )
                            else -> stringResource(R.string.sub_wait_queue_fallback)
                        },
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                }

                TextButton(
                    onClick = onRefreshQueue,
                    enabled = !isCheckingQueue,
                    contentPadding = PaddingValues(horizontal = 12.dp, vertical = 4.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.Refresh,
                        contentDescription = null,
                        tint = TealPrimary,
                        modifier = Modifier.size(16.dp)
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = stringResource(R.string.sub_wait_refresh_queue),
                        style = MaterialTheme.typography.labelMedium,
                        fontWeight = FontWeight.SemiBold,
                        color = TealPrimary
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(14.dp))

        // Compact metadata strip
        Surface(
            shape = RoundedCornerShape(14.dp),
            color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.35f),
            modifier = Modifier.fillMaxWidth()
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 12.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = stringResource(
                        R.string.sub_wait_meta_attempt_format,
                        submission?.attemptNumber ?: 1
                    ),
                    style = MaterialTheme.typography.labelMedium,
                    fontWeight = FontWeight.SemiBold,
                    color = MaterialTheme.colorScheme.onSurface
                )
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Default.CloudDone,
                        contentDescription = null,
                        tint = TealPrimary,
                        modifier = Modifier.size(15.dp)
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = stringResource(R.string.sub_wait_meta_synced),
                        style = MaterialTheme.typography.labelSmall,
                        fontWeight = FontWeight.Medium,
                        color = TealPrimary
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(26.dp))

        OutlinedButton(
            onClick = onPreviewCode,
            shape = RoundedCornerShape(14.dp),
            border = BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant),
            contentPadding = PaddingValues(vertical = 13.dp),
            modifier = Modifier.fillMaxWidth()
        ) {
            Icon(
                imageVector = Icons.Default.PlayArrow,
                contentDescription = null,
                modifier = Modifier.size(18.dp)
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                text = stringResource(R.string.sub_wait_preview_code),
                fontWeight = FontWeight.SemiBold
            )
        }

        Spacer(modifier = Modifier.height(10.dp))

        TextButton(
            onClick = onBackToCourse,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(
                text = stringResource(R.string.sub_wait_back_course),
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                fontWeight = FontWeight.Medium
            )
        }
    }
}

// ============================================================================
// SCREEN 3: CLEAN REVISION / REJECTED SCREEN (REVISION_REQUESTED)
// ============================================================================

@Composable
private fun SubmissionRevisionScreen(
    course: Course,
    submission: ProjectSubmissionEntity?,
    onEditAndResubmit: () -> Unit,
    onPreviewCode: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(horizontal = 24.dp, vertical = 20.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(modifier = Modifier.height(12.dp))

        // Calm soft-coral icon badge
        Box(
            modifier = Modifier
                .size(84.dp)
                .clip(CircleShape)
                .background(FlameOrange.copy(alpha = 0.12f)),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = Icons.Default.RateReview,
                contentDescription = null,
                tint = FlameOrange,
                modifier = Modifier.size(38.dp)
            )
        }

        Spacer(modifier = Modifier.height(18.dp))

        Text(
            text = stringResource(R.string.sub_rev_kicker),
            style = MaterialTheme.typography.labelSmall,
            fontWeight = FontWeight.Bold,
            letterSpacing = 1.4.sp,
            color = FlameOrange
        )

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = stringResource(R.string.sub_rev_title),
            style = MaterialTheme.typography.headlineSmall,
            fontWeight = FontWeight.Bold,
            textAlign = TextAlign.Center,
            color = MaterialTheme.colorScheme.onSurface
        )

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = stringResource(R.string.sub_rev_desc),
            style = MaterialTheme.typography.bodyMedium,
            textAlign = TextAlign.Center,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            lineHeight = 22.sp
        )

        Spacer(modifier = Modifier.height(22.dp))

        // Reviewer & score summary pill row
        Surface(
            shape = RoundedCornerShape(14.dp),
            color = MaterialTheme.colorScheme.surface,
            border = BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.55f)),
            modifier = Modifier.fillMaxWidth()
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 14.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = stringResource(
                        R.string.final_project_feedback_reviewer_format,
                        submission?.facilitatorName?.takeIf { it.isNotBlank() } ?: course.instructor
                    ),
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    modifier = Modifier.weight(1f),
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                Spacer(modifier = Modifier.width(10.dp))
                Surface(
                    shape = RoundedCornerShape(8.dp),
                    color = FlameOrange.copy(alpha = 0.14f)
                ) {
                    Text(
                        text = stringResource(
                            R.string.final_project_feedback_score_format,
                            submission?.facilitatorScore ?: 0
                        ),
                        style = MaterialTheme.typography.labelMedium,
                        fontWeight = FontWeight.Bold,
                        color = FlameOrange,
                        modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp)
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(14.dp))

        // Facilitator Feedback Callout Card
        Surface(
            shape = RoundedCornerShape(16.dp),
            color = MaterialTheme.colorScheme.surface,
            border = BorderStroke(1.dp, FlameOrange.copy(alpha = 0.35f)),
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(modifier = Modifier.padding(18.dp)) {
                Text(
                    text = stringResource(R.string.sub_rev_feedback_label),
                    style = MaterialTheme.typography.labelMedium,
                    fontWeight = FontWeight.Bold,
                    color = FlameOrange
                )
                Spacer(modifier = Modifier.height(10.dp))
                Text(
                    text = submission?.facilitatorFeedback?.takeIf { it.isNotBlank() }
                        ?: stringResource(R.string.final_project_feedback_empty),
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurface,
                    lineHeight = 23.sp
                )
            }
        }

        Spacer(modifier = Modifier.height(26.dp))

        Button(
            onClick = onEditAndResubmit,
            colors = ButtonDefaults.buttonColors(containerColor = FlameOrange),
            shape = RoundedCornerShape(14.dp),
            contentPadding = PaddingValues(vertical = 15.dp),
            modifier = Modifier.fillMaxWidth()
        ) {
            Icon(
                imageVector = Icons.Default.Edit,
                contentDescription = null,
                tint = Color.White,
                modifier = Modifier.size(18.dp)
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                text = stringResource(R.string.sub_rev_btn_fix),
                color = Color.White,
                fontWeight = FontWeight.Bold
            )
        }

        Spacer(modifier = Modifier.height(10.dp))

        TextButton(
            onClick = onPreviewCode,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(
                text = stringResource(R.string.sub_wait_preview_code),
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                fontWeight = FontWeight.Medium
            )
        }
    }
}

// ============================================================================
// SCREEN 4: PREMIUM APPROVED SCREEN (APPROVED)
// ============================================================================

@Composable
private fun SubmissionApprovedScreen(
    course: Course,
    submission: ProjectSubmissionEntity?,
    onViewCertificate: () -> Unit,
    onOpenRating: () -> Unit
) {
    val infiniteTransition = rememberInfiniteTransition(label = "approved_glow")
    val haloScale by infiniteTransition.animateFloat(
        initialValue = 0.94f,
        targetValue = 1.08f,
        animationSpec = infiniteRepeatable(
            animation = tween(1900, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "approved_halo"
    )

    val quizScore = submission?.finalQuizScore ?: 80
    val projectScore = submission?.facilitatorScore ?: 85
    val finalGrade = ((quizScore * 0.4) + (projectScore * 0.6)).roundToInt().coerceIn(0, 100)
    val existingRating = submission?.studentRating

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(horizontal = 24.dp, vertical = 18.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(modifier = Modifier.height(8.dp))

        // Animated emerald check badge
        Box(
            modifier = Modifier.size(104.dp),
            contentAlignment = Alignment.Center
        ) {
            Box(
                modifier = Modifier
                    .size(96.dp)
                    .scale(haloScale)
                    .clip(CircleShape)
                    .background(SuccessGreen.copy(alpha = 0.12f))
            )
            Box(
                modifier = Modifier
                    .size(70.dp)
                    .clip(CircleShape)
                    .background(SuccessGreen),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Default.Check,
                    contentDescription = null,
                    tint = Color.White,
                    modifier = Modifier.size(36.dp)
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = stringResource(R.string.sub_app_kicker),
            style = MaterialTheme.typography.labelSmall,
            fontWeight = FontWeight.Bold,
            letterSpacing = 1.4.sp,
            color = SuccessGreen
        )

        Spacer(modifier = Modifier.height(6.dp))

        Text(
            text = stringResource(R.string.sub_app_title),
            style = MaterialTheme.typography.headlineSmall,
            fontWeight = FontWeight.Bold,
            textAlign = TextAlign.Center,
            color = MaterialTheme.colorScheme.onSurface
        )

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = stringResource(R.string.sub_app_desc),
            style = MaterialTheme.typography.bodyMedium,
            textAlign = TextAlign.Center,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            lineHeight = 21.sp
        )

        Spacer(modifier = Modifier.height(22.dp))

        // Clean 3-column combined score breakdown card
        Surface(
            shape = RoundedCornerShape(18.dp),
            color = MaterialTheme.colorScheme.surface,
            border = BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.55f)),
            modifier = Modifier.fillMaxWidth()
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 18.dp, horizontal = 12.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                ScoreMetricColumn(
                    label = stringResource(R.string.sub_app_score_quiz),
                    value = "$quizScore",
                    valueColor = MaterialTheme.colorScheme.onSurface,
                    modifier = Modifier.weight(1f)
                )
                Box(
                    modifier = Modifier
                        .width(1.dp)
                        .height(34.dp)
                        .background(MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.5f))
                )
                ScoreMetricColumn(
                    label = stringResource(R.string.sub_app_score_project),
                    value = "$projectScore",
                    valueColor = MaterialTheme.colorScheme.onSurface,
                    modifier = Modifier.weight(1f)
                )
                Box(
                    modifier = Modifier
                        .width(1.dp)
                        .height(34.dp)
                        .background(MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.5f))
                )
                ScoreMetricColumn(
                    label = stringResource(R.string.sub_app_score_final),
                    value = "$finalGrade",
                    valueColor = SuccessGreen,
                    modifier = Modifier.weight(1f)
                )
            }
        }

        Spacer(modifier = Modifier.height(14.dp))

        // Facilitator feedback quote card
        Surface(
            shape = RoundedCornerShape(16.dp),
            color = MaterialTheme.colorScheme.surface,
            border = BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.55f)),
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = stringResource(
                        R.string.final_project_feedback_reviewer_format,
                        submission?.facilitatorName?.takeIf { it.isNotBlank() } ?: course.instructor
                    ),
                    style = MaterialTheme.typography.labelSmall,
                    fontWeight = FontWeight.SemiBold,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = submission?.facilitatorFeedback?.takeIf { it.isNotBlank() }
                        ?: stringResource(R.string.final_project_feedback_empty),
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurface,
                    lineHeight = 22.sp
                )
            }
        }

        Spacer(modifier = Modifier.height(20.dp))

        // Primary CTA: View Certificate
        Button(
            onClick = onViewCertificate,
            colors = ButtonDefaults.buttonColors(containerColor = SuccessGreen),
            shape = RoundedCornerShape(14.dp),
            contentPadding = PaddingValues(vertical = 15.dp),
            modifier = Modifier
                .fillMaxWidth()
                .testTag("final_project_view_certificate_btn")
        ) {
            Icon(
                imageVector = Icons.Default.WorkspacePremium,
                contentDescription = null,
                tint = Color.White,
                modifier = Modifier.size(18.dp)
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                text = stringResource(R.string.eval_result_btn_view_cert),
                color = Color.White,
                fontWeight = FontWeight.Bold
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Post-Approval Student Rating Card (or Thank-You Badge if already rated)
        if (existingRating == null) {
            Surface(
                onClick = onOpenRating,
                shape = RoundedCornerShape(16.dp),
                color = AmberGold.copy(alpha = 0.08f),
                border = BorderStroke(1.dp, AmberGold.copy(alpha = 0.4f)),
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(
                        modifier = Modifier
                            .size(42.dp)
                            .clip(CircleShape)
                            .background(AmberGold.copy(alpha = 0.18f)),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.Star,
                            contentDescription = null,
                            tint = AmberGold,
                            modifier = Modifier.size(22.dp)
                        )
                    }
                    Spacer(modifier = Modifier.width(12.dp))
                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = stringResource(R.string.sub_app_rate_prompt_title),
                            style = MaterialTheme.typography.labelLarge,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                        Spacer(modifier = Modifier.height(2.dp))
                        Text(
                            text = stringResource(R.string.sub_app_rate_prompt_desc),
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                    Spacer(modifier = Modifier.width(8.dp))
                    Surface(
                        shape = RoundedCornerShape(10.dp),
                        color = AmberGold
                    ) {
                        Text(
                            text = stringResource(R.string.sub_app_rate_prompt_btn),
                            style = MaterialTheme.typography.labelSmall,
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFF1F2937),
                            modifier = Modifier.padding(horizontal = 10.dp, vertical = 7.dp)
                        )
                    }
                }
            }
        } else {
            Surface(
                shape = RoundedCornerShape(16.dp),
                color = AmberGold.copy(alpha = 0.1f),
                border = BorderStroke(1.dp, AmberGold.copy(alpha = 0.35f)),
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 12.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        text = stringResource(R.string.sub_app_rated_thanks_format, existingRating),
                        style = MaterialTheme.typography.labelLarge,
                        fontWeight = FontWeight.SemiBold,
                        color = MaterialTheme.colorScheme.onSurface,
                        modifier = Modifier.weight(1f)
                    )
                    TextButton(
                        onClick = onOpenRating,
                        contentPadding = PaddingValues(horizontal = 8.dp, vertical = 2.dp)
                    ) {
                        Text(
                            text = stringResource(R.string.sub_app_rate_edit),
                            style = MaterialTheme.typography.labelMedium,
                            fontWeight = FontWeight.SemiBold,
                            color = TealPrimary
                        )
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))
    }
}

@Composable
private fun ScoreMetricColumn(
    label: String,
    value: String,
    valueColor: Color,
    modifier: Modifier = Modifier
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier
    ) {
        Text(
            text = value,
            style = MaterialTheme.typography.headlineSmall,
            fontWeight = FontWeight.Bold,
            color = valueColor
        )
        Spacer(modifier = Modifier.height(2.dp))
        Text(
            text = label,
            style = MaterialTheme.typography.labelSmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis
        )
    }
}

// ============================================================================
// SCREEN 5: POST-APPROVAL STUDENT RATING SCREEN
// ============================================================================

@OptIn(ExperimentalLayoutApi::class)
@Composable
private fun SubmissionRatingScreen(
    reviewerName: String,
    initialRating: Int,
    initialComment: String,
    initialTags: Set<String>,
    onSubmit: (rating: Int, comment: String, tags: List<String>) -> Unit,
    onSkip: () -> Unit
) {
    val context = LocalContext.current
    var selectedRating by remember { mutableIntStateOf(initialRating) }
    var comment by remember { mutableStateOf(initialComment) }
    var selectedTags by remember { mutableStateOf(initialTags) }
    var isSubmitting by remember { mutableStateOf(false) }

    val aspectOptions = listOf(
        "clear_feedback" to stringResource(R.string.sub_rating_tag_clear),
        "fast_review" to stringResource(R.string.sub_rating_tag_fast),
        "constructive_tips" to stringResource(R.string.sub_rating_tag_tips),
        "fair_grading" to stringResource(R.string.sub_rating_tag_fair),
        "very_motivating" to stringResource(R.string.sub_rating_tag_motivating)
    )

    val ratingLabel = when (selectedRating) {
        1 -> stringResource(R.string.sub_rating_label_1)
        2 -> stringResource(R.string.sub_rating_label_2)
        3 -> stringResource(R.string.sub_rating_label_3)
        4 -> stringResource(R.string.sub_rating_label_4)
        5 -> stringResource(R.string.sub_rating_label_5)
        else -> ""
    }

    val initialChar = reviewerName.trim().firstOrNull()?.uppercaseChar()?.toString() ?: "F"

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .imePadding()
            .padding(horizontal = 24.dp, vertical = 18.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(modifier = Modifier.height(6.dp))

        // Reviewer initial avatar
        Box(
            modifier = Modifier
                .size(64.dp)
                .clip(CircleShape)
                .background(TealPrimary.copy(alpha = 0.12f)),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = initialChar,
                style = MaterialTheme.typography.headlineSmall,
                fontWeight = FontWeight.Bold,
                color = TealPrimary
            )
        }

        Spacer(modifier = Modifier.height(14.dp))

        Text(
            text = stringResource(R.string.sub_rating_title),
            style = MaterialTheme.typography.headlineSmall,
            fontWeight = FontWeight.Bold,
            textAlign = TextAlign.Center,
            color = MaterialTheme.colorScheme.onSurface
        )

        Spacer(modifier = Modifier.height(4.dp))

        Text(
            text = stringResource(R.string.sub_rating_subtitle_format, reviewerName),
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )

        Spacer(modifier = Modifier.height(24.dp))

        // Animated 1-5 Star Picker
        Row(
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            for (star in 1..5) {
                val isFilled = star <= selectedRating
                val scale by animateFloatAsState(
                    targetValue = if (star == selectedRating) 1.2f else 1f,
                    animationSpec = spring(
                        dampingRatio = Spring.DampingRatioMediumBouncy,
                        stiffness = Spring.StiffnessMedium
                    ),
                    label = "star_scale_$star"
                )
                IconButton(
                    onClick = { selectedRating = star },
                    modifier = Modifier
                        .size(48.dp)
                        .scale(scale)
                ) {
                    Icon(
                        imageVector = if (isFilled) Icons.Default.Star else Icons.Default.StarBorder,
                        contentDescription = "$star",
                        tint = if (isFilled) AmberGold else MaterialTheme.colorScheme.outlineVariant,
                        modifier = Modifier.size(40.dp)
                    )
                }
            }
        }

        AnimatedVisibility(visible = ratingLabel.isNotBlank()) {
            Text(
                text = ratingLabel,
                style = MaterialTheme.typography.labelLarge,
                fontWeight = FontWeight.SemiBold,
                color = AmberGold,
                modifier = Modifier.padding(top = 8.dp)
            )
        }

        Spacer(modifier = Modifier.height(26.dp))

        // Selectable aspect chips
        Column(modifier = Modifier.fillMaxWidth()) {
            Text(
                text = stringResource(R.string.sub_rating_aspects_label),
                style = MaterialTheme.typography.labelLarge,
                fontWeight = FontWeight.SemiBold,
                color = MaterialTheme.colorScheme.onSurface
            )
            Spacer(modifier = Modifier.height(10.dp))
            FlowRow(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                aspectOptions.forEach { (key, label) ->
                    val isSelected = key in selectedTags
                    Surface(
                        onClick = {
                            selectedTags = if (isSelected) selectedTags - key else selectedTags + key
                        },
                        shape = RoundedCornerShape(999.dp),
                        color = if (isSelected) TealPrimary.copy(alpha = 0.14f)
                        else MaterialTheme.colorScheme.surface,
                        border = BorderStroke(
                            1.dp,
                            if (isSelected) TealPrimary else MaterialTheme.colorScheme.outlineVariant
                        )
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.padding(horizontal = 14.dp, vertical = 8.dp)
                        ) {
                            if (isSelected) {
                                Icon(
                                    imageVector = Icons.Default.Check,
                                    contentDescription = null,
                                    tint = TealPrimary,
                                    modifier = Modifier.size(14.dp)
                                )
                                Spacer(modifier = Modifier.width(5.dp))
                            }
                            Text(
                                text = label,
                                style = MaterialTheme.typography.labelMedium,
                                fontWeight = if (isSelected) FontWeight.SemiBold else FontWeight.Normal,
                                color = if (isSelected) TealPrimary else MaterialTheme.colorScheme.onSurface
                            )
                        }
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(22.dp))

        // Optional Comment
        Column(modifier = Modifier.fillMaxWidth()) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = stringResource(R.string.sub_rating_comment_label),
                    style = MaterialTheme.typography.labelLarge,
                    fontWeight = FontWeight.SemiBold,
                    color = MaterialTheme.colorScheme.onSurface
                )
                Text(
                    text = "${comment.length}/280",
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
            Spacer(modifier = Modifier.height(8.dp))
            OutlinedTextField(
                value = comment,
                onValueChange = { if (it.length <= 280) comment = it },
                minLines = 3,
                maxLines = 5,
                shape = RoundedCornerShape(14.dp),
                placeholder = {
                    Text(stringResource(R.string.sub_rating_comment_ph))
                },
                modifier = Modifier.fillMaxWidth()
            )
        }

        Spacer(modifier = Modifier.height(26.dp))

        Button(
            onClick = {
                if (selectedRating !in 1..5) {
                    Toast.makeText(
                        context,
                        context.getString(R.string.sub_rating_toast_pick_star),
                        Toast.LENGTH_SHORT
                    ).show()
                } else {
                    isSubmitting = true
                    onSubmit(selectedRating, comment.trim(), selectedTags.toList())
                }
            },
            enabled = !isSubmitting,
            colors = ButtonDefaults.buttonColors(containerColor = TealPrimary),
            shape = RoundedCornerShape(14.dp),
            contentPadding = PaddingValues(vertical = 15.dp),
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(
                text = stringResource(R.string.sub_rating_btn_submit),
                color = Color.White,
                fontWeight = FontWeight.Bold
            )
        }

        Spacer(modifier = Modifier.height(8.dp))

        TextButton(
            onClick = onSkip,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(
                text = stringResource(R.string.sub_rating_btn_skip),
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                fontWeight = FontWeight.Medium
            )
        }
    }
}

// ============================================================================
// FILE UPLOAD & HELPER UTILITIES
// ============================================================================

private suspend fun readUploadedProjectFile(
    context: Context,
    uri: Uri
): Triple<String, Int, String>? = withContext(Dispatchers.IO) {
    val maxBytes = 250 * 1024 // 250 KB safe ceiling for Firestore document
    val allowedExts = setOf("html", "htm", "css", "js", "json", "md", "txt")
    try {
        var displayName = "project_file"
        context.contentResolver.query(uri, arrayOf(OpenableColumns.DISPLAY_NAME), null, null, null)?.use { cursor ->
            if (cursor.moveToFirst()) {
                val idx = cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME)
                if (idx >= 0) {
                    val name = cursor.getString(idx)
                    if (!name.isNullOrBlank()) displayName = name
                }
            }
        }

        val isZip = displayName.endsWith(".zip", ignoreCase = true) ||
            (context.contentResolver.getType(uri)?.contains("zip", ignoreCase = true) == true)

        if (isZip) {
            val merged = StringBuilder()
            var totalBytes = 0
            context.contentResolver.openInputStream(uri)?.use { raw ->
                ZipInputStream(raw).use { zis ->
                    var entry = zis.nextEntry
                    while (entry != null) {
                        val name = entry.name
                        val ext = name.substringAfterLast('.', "").lowercase()
                        val isSafe = !entry.isDirectory &&
                            !name.contains("..") &&
                            !name.startsWith("__MACOSX") &&
                            !name.substringAfterLast('/').startsWith(".") &&
                            ext in allowedExts
                        if (isSafe && totalBytes < maxBytes) {
                            val buf = ByteArrayOutputStream()
                            val chunk = ByteArray(4096)
                            var read = zis.read(chunk)
                            while (read > 0 && totalBytes + buf.size() < maxBytes) {
                                buf.write(chunk, 0, read)
                                read = zis.read(chunk)
                            }
                            val fileText = buf.toString(Charsets.UTF_8.name()).trim()
                            if (fileText.isNotEmpty()) {
                                if (merged.isNotEmpty()) merged.append("\n\n")
                                merged.append("<!-- ===== ").append(name).append(" ===== -->\n")
                                merged.append(fileText)
                                totalBytes += buf.size()
                            }
                        }
                        zis.closeEntry()
                        entry = zis.nextEntry
                    }
                }
            }
            val finalCode = merged.toString()
            if (finalCode.isBlank()) return@withContext null
            Triple(displayName, (totalBytes / 1024).coerceAtLeast(1), finalCode)
        } else {
            val buf = ByteArrayOutputStream()
            context.contentResolver.openInputStream(uri)?.use { input ->
                val chunk = ByteArray(4096)
                var read = input.read(chunk)
                while (read > 0 && buf.size() < maxBytes) {
                    buf.write(chunk, 0, read)
                    read = input.read(chunk)
                }
            }
            val text = buf.toString(Charsets.UTF_8.name())
            if (text.isBlank()) return@withContext null
            Triple(displayName, (buf.size() / 1024).coerceAtLeast(1), text)
        }
    } catch (_: Throwable) {
        null
    }
}

private fun parseCheckedRubricSet(json: String?): Set<String> {
    if (json.isNullOrBlank()) return emptySet()
    return try {
        val arr = JSONArray(json)
        val set = mutableSetOf<String>()
        for (i in 0 until arr.length()) {
            val id = arr.optString(i)
            if (id.isNotBlank()) set.add(id)
        }
        set
    } catch (_: Exception) {
        emptySet()
    }
}

private fun ProjectSubmissionEntity.updatedAtOrSubmitted(): Long =
    reviewedAt ?: submittedAt
