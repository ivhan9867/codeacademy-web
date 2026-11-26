package com.example.ui.screens

import android.content.Context
import android.net.Uri
import android.provider.OpenableColumns
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
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
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.Send
import androidx.compose.material.icons.filled.AssignmentTurnedIn
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.CloudDone
import androidx.compose.material.icons.filled.Code
import androidx.compose.material.icons.filled.Description
import androidx.compose.material.icons.filled.HourglassTop
import androidx.compose.material.icons.filled.Link
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.RateReview
import androidx.compose.material.icons.filled.Restore
import androidx.compose.material.icons.filled.Save
import androidx.compose.material.icons.filled.UploadFile
import androidx.compose.material.icons.filled.WorkspacePremium
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.example.ui.components.CodeSandboxWebView
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.ByteArrayOutputStream
import java.util.zip.ZipInputStream
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Slider
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.R
import com.example.data.CourseCatalogParser
import com.example.data.local.ProjectSubmissionEntity
import com.example.data.models.Course
import com.example.data.models.ProjectSubmissionStatus
import com.example.ui.theme.AmberGold
import com.example.ui.theme.FlameOrange
import com.example.ui.theme.SuccessGreen
import com.example.ui.theme.TealPrimary
import com.example.util.localization.DataLabelMappings
import kotlinx.coroutines.launch
import org.json.JSONArray

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

    // Sync local form fields if submission initially arrives from Room
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

    // Real-time Firestore listener so when Facilitator Web approves/requests revision, UI updates live
    DisposableEffect(course.id) {
        val registration = onAttachRealtimeListener()
        onDispose {
            registration?.remove()
        }
    }

    var uploadedFileName by remember(submission?.courseId) { mutableStateOf<String?>(null) }
    var uploadedFileSizeKb by remember(submission?.courseId) { mutableIntStateOf(0) }
    var showPreviewDialog by remember { mutableStateOf(false) }

    val isEditable = status != ProjectSubmissionStatus.PENDING_REVIEW
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

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Column {
                        Text(
                            text = stringResource(R.string.final_project_screen_title),
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold
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
                    IconButton(onClick = onBack) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = stringResource(R.string.class_detail_back_cd)
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.surface
                )
            )
        },
        bottomBar = {
            if (isEditable) {
                Surface(
                    color = MaterialTheme.colorScheme.surface,
                    tonalElevation = 4.dp,
                    shadowElevation = 8.dp,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp, vertical = 12.dp),
                        horizontalArrangement = Arrangement.spacedBy(12.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        OutlinedButton(
                            onClick = {
                                onSaveDraft(workspaceCode, projectUrl, studentNotes, checkedRubricIds)
                                Toast.makeText(context, toastDraftSaved, Toast.LENGTH_SHORT).show()
                            },
                            shape = RoundedCornerShape(12.dp),
                            contentPadding = PaddingValues(vertical = 12.dp, horizontal = 12.dp),
                            modifier = Modifier.weight(0.95f)
                        ) {
                            Icon(
                                imageVector = Icons.Default.Save,
                                contentDescription = null,
                                modifier = Modifier.size(18.dp)
                            )
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(
                                text = stringResource(R.string.final_project_btn_save_draft),
                                maxLines = 1,
                                overflow = TextOverflow.Ellipsis,
                                fontWeight = FontWeight.SemiBold
                            )
                        }

                        Button(
                            onClick = {
                                val allRubricsChecked = spec.rubrics.all { it.id in checkedRubricIds }
                                val hasContent = workspaceCode.isNotBlank() || projectUrl.isNotBlank()
                                if (isUrlInvalid) {
                                    Toast.makeText(context, context.getString(R.string.final_project_url_invalid), Toast.LENGTH_SHORT).show()
                                } else if (!allRubricsChecked || !hasContent) {
                                    Toast.makeText(context, toastCheckAllRubrics, Toast.LENGTH_SHORT).show()
                                } else {
                                    onSubmitProject(workspaceCode, projectUrl.trim(), studentNotes.trim(), checkedRubricIds)
                                    Toast.makeText(context, toastSubmitted, Toast.LENGTH_SHORT).show()
                                }
                            },
                            colors = ButtonDefaults.buttonColors(containerColor = TealPrimary),
                            shape = RoundedCornerShape(12.dp),
                            contentPadding = PaddingValues(vertical = 12.dp, horizontal = 14.dp),
                            modifier = Modifier
                                .weight(1.35f)
                                .testTag("final_project_submit_btn")
                        ) {
                            Icon(
                                imageVector = Icons.AutoMirrored.Filled.Send,
                                contentDescription = null,
                                tint = Color.White,
                                modifier = Modifier.size(18.dp)
                            )
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(
                                text = if (status == ProjectSubmissionStatus.REVISION_REQUESTED) {
                                    stringResource(R.string.final_project_btn_resubmit)
                                } else {
                                    stringResource(R.string.final_project_btn_submit)
                                },
                                color = Color.White,
                                fontWeight = FontWeight.Bold,
                                maxLines = 1,
                                overflow = TextOverflow.Ellipsis
                            )
                        }
                    }
                }
            }
        },
        modifier = modifier
    ) { innerPadding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .imePadding()
                .padding(horizontal = 16.dp),
            contentPadding = PaddingValues(top = 16.dp, bottom = 36.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // 1. Status & Summary Header Card
            item(key = "status_header_card") {
                val statusColor = when (status) {
                    ProjectSubmissionStatus.APPROVED -> SuccessGreen
                    ProjectSubmissionStatus.PENDING_REVIEW -> AmberGold
                    ProjectSubmissionStatus.REVISION_REQUESTED -> FlameOrange
                    else -> TealPrimary
                }
                val statusLabel = when (status) {
                    ProjectSubmissionStatus.APPROVED -> stringResource(R.string.final_project_status_approved)
                    ProjectSubmissionStatus.PENDING_REVIEW -> stringResource(R.string.final_project_status_pending)
                    ProjectSubmissionStatus.REVISION_REQUESTED -> stringResource(R.string.final_project_status_revision)
                    ProjectSubmissionStatus.LOCKED -> stringResource(R.string.final_project_status_locked)
                    ProjectSubmissionStatus.DRAFT -> stringResource(R.string.final_project_status_draft)
                }

                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(18.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = statusColor.copy(alpha = 0.12f)
                    ),
                    border = BorderStroke(1.dp, statusColor.copy(alpha = 0.4f)),
                    elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
                ) {
                    Column(modifier = Modifier.padding(18.dp)) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween,
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Surface(
                                color = statusColor,
                                shape = RoundedCornerShape(8.dp)
                            ) {
                                Text(
                                    text = statusLabel,
                                    color = Color.White,
                                    style = MaterialTheme.typography.labelMedium,
                                    fontWeight = FontWeight.Bold,
                                    modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp)
                                )
                            }

                            Text(
                                text = stringResource(
                                    R.string.final_project_attempt_badge,
                                    submission?.attemptNumber ?: 1,
                                    spec.minPassingScore,
                                    spec.xpReward
                                ),
                                style = MaterialTheme.typography.labelSmall,
                                color = MaterialTheme.colorScheme.onSurfaceVariant,
                                fontWeight = FontWeight.SemiBold
                            )
                        }

                        Spacer(modifier = Modifier.height(12.dp))

                        Text(
                            text = DataLabelMappings.finalProjectTitle(course.id, spec.title),
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.onSurface
                        )

                        Spacer(modifier = Modifier.height(6.dp))

                        val facScoreDisplay = submission?.facilitatorScore?.let { "$it/100" } ?: "-"
                        Text(
                            text = stringResource(
                                R.string.final_project_quiz_score_summary,
                                submission?.finalQuizScore ?: 80,
                                facScoreDisplay
                            ),
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )

                        Spacer(modifier = Modifier.height(14.dp))

                        // 3-Step Visual Progress Stepper (Draft -> Pending Review -> Approved)
                        val activeStep = when (status) {
                            ProjectSubmissionStatus.APPROVED -> 2
                            ProjectSubmissionStatus.PENDING_REVIEW -> 1
                            else -> 0
                        }
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            val stepLabels = listOf(
                                stringResource(R.string.final_project_status_draft),
                                stringResource(R.string.final_project_status_pending),
                                stringResource(R.string.final_project_status_approved)
                            )
                            stepLabels.forEachIndexed { idx, label ->
                                val isDone = idx <= activeStep
                                Column(
                                    horizontalAlignment = Alignment.CenterHorizontally,
                                    modifier = Modifier.weight(1f)
                                ) {
                                    Box(
                                        modifier = Modifier
                                            .size(22.dp)
                                            .clip(CircleShape)
                                            .background(if (isDone) statusColor else MaterialTheme.colorScheme.surfaceVariant),
                                        contentAlignment = Alignment.Center
                                    ) {
                                        Text(
                                            text = "${idx + 1}",
                                            fontSize = 11.sp,
                                            fontWeight = FontWeight.Bold,
                                            color = if (isDone) Color.White else MaterialTheme.colorScheme.onSurfaceVariant
                                        )
                                    }
                                    Spacer(modifier = Modifier.height(4.dp))
                                    Text(
                                        text = label,
                                        fontSize = 10.sp,
                                        fontWeight = if (isDone) FontWeight.Bold else FontWeight.Normal,
                                        color = if (isDone) MaterialTheme.colorScheme.onSurface else MaterialTheme.colorScheme.onSurfaceVariant,
                                        maxLines = 1,
                                        overflow = TextOverflow.Ellipsis
                                    )
                                }
                            }
                        }
                    }
                }
            }

            // 2. Pending Banner or Facilitator Feedback Card
            if (status == ProjectSubmissionStatus.PENDING_REVIEW) {
                item(key = "pending_review_banner") {
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(16.dp),
                        colors = CardDefaults.cardColors(
                            containerColor = AmberGold.copy(alpha = 0.14f)
                        ),
                        border = BorderStroke(1.dp, AmberGold.copy(alpha = 0.45f))
                    ) {
                        Row(
                            modifier = Modifier.padding(16.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(
                                imageVector = Icons.Default.HourglassTop,
                                contentDescription = null,
                                tint = AmberGold,
                                modifier = Modifier.size(32.dp)
                            )
                            Spacer(modifier = Modifier.width(12.dp))
                            Column {
                                Text(
                                    text = stringResource(R.string.final_project_pending_banner_title),
                                    style = MaterialTheme.typography.titleSmall,
                                    fontWeight = FontWeight.Bold,
                                    color = MaterialTheme.colorScheme.onSurface
                                )
                                Spacer(modifier = Modifier.height(4.dp))
                                Text(
                                    text = stringResource(R.string.final_project_pending_banner_desc),
                                    style = MaterialTheme.typography.bodySmall,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                                Spacer(modifier = Modifier.height(8.dp))
                                Surface(
                                    shape = RoundedCornerShape(999.dp),
                                    color = TealPrimary.copy(alpha = 0.14f)
                                ) {
                                    Row(
                                        verticalAlignment = Alignment.CenterVertically,
                                        modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp)
                                    ) {
                                        Icon(
                                            imageVector = Icons.Default.CloudDone,
                                            contentDescription = null,
                                            tint = TealPrimary,
                                            modifier = Modifier.size(14.dp)
                                        )
                                        Spacer(modifier = Modifier.width(6.dp))
                                        Text(
                                            text = stringResource(R.string.final_project_cloud_sync_badge),
                                            style = MaterialTheme.typography.labelSmall,
                                            fontWeight = FontWeight.SemiBold,
                                            color = TealPrimary
                                        )
                                    }
                                }
                            }
                        }
                    }
                }
            }

            if (status == ProjectSubmissionStatus.APPROVED || status == ProjectSubmissionStatus.REVISION_REQUESTED) {
                item(key = "facilitator_feedback_card") {
                    val isApproved = status == ProjectSubmissionStatus.APPROVED
                    val accent = if (isApproved) SuccessGreen else FlameOrange

                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(18.dp),
                        colors = CardDefaults.cardColors(
                            containerColor = accent.copy(alpha = 0.1f)
                        ),
                        border = BorderStroke(1.5.dp, accent.copy(alpha = 0.5f))
                    ) {
                        Column(modifier = Modifier.padding(18.dp)) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.SpaceBetween,
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Icon(
                                        imageVector = if (isApproved) Icons.Default.CheckCircle else Icons.Default.RateReview,
                                        contentDescription = null,
                                        tint = accent,
                                        modifier = Modifier.size(24.dp)
                                    )
                                    Spacer(modifier = Modifier.width(8.dp))
                                    Text(
                                        text = stringResource(R.string.final_project_feedback_card_title),
                                        style = MaterialTheme.typography.titleSmall,
                                        fontWeight = FontWeight.Bold,
                                        color = MaterialTheme.colorScheme.onSurface
                                    )
                                }

                                Surface(
                                    color = accent.copy(alpha = 0.2f),
                                    shape = RoundedCornerShape(8.dp)
                                ) {
                                    Text(
                                        text = stringResource(
                                            R.string.final_project_feedback_score_format,
                                            submission?.facilitatorScore ?: 0
                                        ),
                                        style = MaterialTheme.typography.labelMedium,
                                        fontWeight = FontWeight.Bold,
                                        color = accent,
                                        modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp)
                                    )
                                }
                            }

                            Spacer(modifier = Modifier.height(8.dp))

                            Text(
                                text = stringResource(
                                    R.string.final_project_feedback_reviewer_format,
                                    submission?.facilitatorName ?: course.instructor
                                ),
                                style = MaterialTheme.typography.labelSmall,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )

                            Spacer(modifier = Modifier.height(10.dp))

                            Surface(
                                color = MaterialTheme.colorScheme.surface,
                                shape = RoundedCornerShape(12.dp),
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Text(
                                    text = submission?.facilitatorFeedback?.takeIf { it.isNotBlank() }
                                        ?: stringResource(R.string.final_project_feedback_empty),
                                    style = MaterialTheme.typography.bodyMedium,
                                    color = MaterialTheme.colorScheme.onSurface,
                                    modifier = Modifier.padding(14.dp)
                                )
                            }

                            if (isApproved) {
                                Spacer(modifier = Modifier.height(14.dp))
                                Button(
                                    onClick = onViewCertificate,
                                    colors = ButtonDefaults.buttonColors(containerColor = AmberGold),
                                    shape = RoundedCornerShape(12.dp),
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .testTag("final_project_view_certificate_btn")
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.WorkspacePremium,
                                        contentDescription = null,
                                        tint = Color.Black
                                    )
                                    Spacer(modifier = Modifier.width(8.dp))
                                    Text(
                                        text = stringResource(R.string.class_detail_cert_view_btn),
                                        color = Color.Black,
                                        fontWeight = FontWeight.Bold
                                    )
                                }
                            }
                        }
                    }
                }
            }

            // 3. Case Study & Scenario Specification Card
            item(key = "scenario_card") {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                    border = BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.6f))
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text(
                            text = stringResource(R.string.final_project_scenario_header),
                            style = MaterialTheme.typography.titleSmall,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.primary
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = DataLabelMappings.finalProjectScenario(course.id, spec.scenario),
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onSurface,
                            lineHeight = 21.sp
                        )
                    }
                }
            }

            // 4. Rubric Self-Assessment Checklist Card
            item(key = "rubrics_card") {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                    border = BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.6f))
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                imageVector = Icons.Default.AssignmentTurnedIn,
                                contentDescription = null,
                                tint = MaterialTheme.colorScheme.primary,
                                modifier = Modifier.size(20.dp)
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = stringResource(R.string.final_project_rubric_header),
                                style = MaterialTheme.typography.titleSmall,
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.onSurface
                            )
                        }
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = stringResource(R.string.final_project_rubric_subtitle),
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )

                        Spacer(modifier = Modifier.height(10.dp))

                        val rubricTotal = spec.rubrics.size.coerceAtLeast(1)
                        val rubricDone = spec.rubrics.count { it.id in checkedRubricIds }
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(10.dp)
                        ) {
                            LinearProgressIndicator(
                                progress = { rubricDone.toFloat() / rubricTotal.toFloat() },
                                modifier = Modifier
                                    .weight(1f)
                                    .height(7.dp)
                                    .clip(RoundedCornerShape(999.dp)),
                                color = if (rubricDone == rubricTotal) SuccessGreen else TealPrimary,
                                trackColor = MaterialTheme.colorScheme.surfaceVariant
                            )
                            Text(
                                text = "$rubricDone/$rubricTotal",
                                style = MaterialTheme.typography.labelSmall,
                                fontWeight = FontWeight.Bold,
                                color = if (rubricDone == rubricTotal) SuccessGreen else TealPrimary
                            )
                        }

                        Spacer(modifier = Modifier.height(10.dp))

                        spec.rubrics.forEach { rubric ->
                            val isChecked = rubric.id in checkedRubricIds
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clip(RoundedCornerShape(10.dp))
                                    .clickable(enabled = isEditable) {
                                        checkedRubricIds = if (isChecked) {
                                            checkedRubricIds - rubric.id
                                        } else {
                                            checkedRubricIds + rubric.id
                                        }
                                    }
                                    .padding(vertical = 6.dp),
                                verticalAlignment = Alignment.Top
                            ) {
                                Checkbox(
                                    checked = isChecked,
                                    onCheckedChange = if (isEditable) {
                                        { checked ->
                                            checkedRubricIds = if (checked) {
                                                checkedRubricIds + rubric.id
                                            } else {
                                                checkedRubricIds - rubric.id
                                            }
                                        }
                                    } else null
                                )
                                Spacer(modifier = Modifier.width(6.dp))
                                Column(modifier = Modifier.weight(1f)) {
                                    Row(
                                        modifier = Modifier.fillMaxWidth(),
                                        horizontalArrangement = Arrangement.SpaceBetween,
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        Text(
                                            text = DataLabelMappings.rubricTitle(course.id, rubric.id, rubric.title),
                                            style = MaterialTheme.typography.bodyMedium,
                                            fontWeight = FontWeight.SemiBold,
                                            color = MaterialTheme.colorScheme.onSurface
                                        )
                                        Surface(
                                            color = TealPrimary.copy(alpha = 0.14f),
                                            shape = RoundedCornerShape(6.dp)
                                        ) {
                                            Text(
                                                text = stringResource(
                                                    R.string.final_project_rubric_weight_format,
                                                    rubric.weightPercent
                                                ),
                                                style = MaterialTheme.typography.labelSmall,
                                                color = TealPrimary,
                                                fontWeight = FontWeight.Bold,
                                                modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                                            )
                                        }
                                    }
                                    Spacer(modifier = Modifier.height(2.dp))
                                    Text(
                                        text = DataLabelMappings.rubricDescription(course.id, rubric.id, rubric.description),
                                        style = MaterialTheme.typography.bodySmall,
                                        color = MaterialTheme.colorScheme.onSurfaceVariant
                                    )
                                }
                            }
                        }
                    }
                }
            }

            // 5. Workspace Code Editor Input
            item(key = "workspace_code_card") {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                    border = BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.6f))
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                imageVector = Icons.Default.Code,
                                contentDescription = null,
                                tint = MaterialTheme.colorScheme.primary,
                                modifier = Modifier.size(20.dp)
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = stringResource(R.string.final_project_workspace_header),
                                style = MaterialTheme.typography.titleSmall,
                                fontWeight = FontWeight.Bold
                            )
                        }
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = stringResource(R.string.final_project_workspace_subtitle),
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )

                        Spacer(modifier = Modifier.height(10.dp))

                        Row(
                            horizontalArrangement = Arrangement.spacedBy(8.dp),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            OutlinedButton(
                                onClick = {
                                    coroutineScope.launch {
                                        val draft = onLoadPlaygroundDraft()
                                        if (!draft.isNullOrBlank()) {
                                            workspaceCode = draft
                                        }
                                    }
                                },
                                enabled = isEditable,
                                modifier = Modifier.weight(1f)
                            ) {
                                Text(
                                    text = stringResource(R.string.final_project_btn_load_playground),
                                    fontSize = 12.sp
                                )
                            }

                            OutlinedButton(
                                onClick = { workspaceCode = spec.starterCode },
                                enabled = isEditable,
                                modifier = Modifier.weight(1f)
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Restore,
                                    contentDescription = null,
                                    modifier = Modifier.size(16.dp)
                                )
                                Spacer(modifier = Modifier.width(4.dp))
                                Text(
                                    text = stringResource(R.string.final_project_btn_load_starter),
                                    fontSize = 12.sp
                                )
                            }
                        }

                        Spacer(modifier = Modifier.height(10.dp))

                        // Real File / ZIP Upload Zone
                        Surface(
                            onClick = {
                                if (isEditable) {
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
                                }
                            },
                            enabled = isEditable,
                            shape = RoundedCornerShape(14.dp),
                            color = TealPrimary.copy(alpha = 0.08f),
                            border = BorderStroke(1.2.dp, TealPrimary.copy(alpha = 0.38f)),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(horizontal = 14.dp, vertical = 12.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Box(
                                    modifier = Modifier
                                        .size(40.dp)
                                        .clip(RoundedCornerShape(10.dp))
                                        .background(TealPrimary.copy(alpha = 0.16f)),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.UploadFile,
                                        contentDescription = null,
                                        tint = TealPrimary,
                                        modifier = Modifier.size(22.dp)
                                    )
                                }
                                Spacer(modifier = Modifier.width(12.dp))
                                Column(modifier = Modifier.weight(1f)) {
                                    Text(
                                        text = stringResource(R.string.final_project_upload_title),
                                        style = MaterialTheme.typography.labelLarge,
                                        fontWeight = FontWeight.Bold,
                                        color = MaterialTheme.colorScheme.onSurface
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
                                    color = TealPrimary
                                ) {
                                    Text(
                                        text = stringResource(R.string.final_project_upload_btn),
                                        style = MaterialTheme.typography.labelSmall,
                                        fontWeight = FontWeight.Bold,
                                        color = Color.White,
                                        modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp)
                                    )
                                }
                            }
                        }

                        AnimatedVisibility(visible = uploadedFileName != null) {
                            uploadedFileName?.let { fname ->
                                Surface(
                                    shape = RoundedCornerShape(10.dp),
                                    color = SuccessGreen.copy(alpha = 0.12f),
                                    border = BorderStroke(1.dp, SuccessGreen.copy(alpha = 0.35f)),
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(top = 8.dp)
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
                                            text = "$fname (${uploadedFileSizeKb} KB)",
                                            style = MaterialTheme.typography.bodySmall,
                                            fontWeight = FontWeight.SemiBold,
                                            color = MaterialTheme.colorScheme.onSurface,
                                            maxLines = 1,
                                            overflow = TextOverflow.Ellipsis,
                                            modifier = Modifier.weight(1f)
                                        )
                                        if (isEditable) {
                                            IconButton(
                                                onClick = {
                                                    uploadedFileName = null
                                                    uploadedFileSizeKb = 0
                                                },
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

                        Spacer(modifier = Modifier.height(10.dp))

                        // Dark IDE Code Container
                        Surface(
                            shape = RoundedCornerShape(14.dp),
                            color = Color(0xFF0F172A),
                            border = BorderStroke(1.dp, Color(0xFF334155)),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Column {
                                Row(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .background(Color(0xFF1E293B))
                                        .padding(horizontal = 12.dp, vertical = 8.dp),
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Row(
                                        horizontalArrangement = Arrangement.spacedBy(6.dp),
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        Box(Modifier.size(9.dp).clip(CircleShape).background(Color(0xFFEF4444)))
                                        Box(Modifier.size(9.dp).clip(CircleShape).background(Color(0xFFF59E0B)))
                                        Box(Modifier.size(9.dp).clip(CircleShape).background(Color(0xFF10B981)))
                                        Spacer(modifier = Modifier.width(4.dp))
                                        Surface(
                                            shape = RoundedCornerShape(6.dp),
                                            color = Color(0xFF334155)
                                        ) {
                                            Text(
                                                text = spec.language.uppercase(),
                                                fontSize = 10.sp,
                                                fontWeight = FontWeight.Bold,
                                                color = Color(0xFF94A3B8),
                                                modifier = Modifier.padding(horizontal = 8.dp, vertical = 2.dp)
                                            )
                                        }
                                    }
                                    Surface(
                                        onClick = { showPreviewDialog = true },
                                        shape = RoundedCornerShape(8.dp),
                                        color = TealPrimary
                                    ) {
                                        Row(
                                            verticalAlignment = Alignment.CenterVertically,
                                            modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp)
                                        ) {
                                            Icon(
                                                imageVector = Icons.Default.PlayArrow,
                                                contentDescription = null,
                                                tint = Color.White,
                                                modifier = Modifier.size(14.dp)
                                            )
                                            Spacer(modifier = Modifier.width(3.dp))
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
                                    onValueChange = { workspaceCode = it },
                                    enabled = isEditable,
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
                                        disabledContainerColor = Color(0xFF0F172A),
                                        focusedBorderColor = Color.Transparent,
                                        unfocusedBorderColor = Color.Transparent,
                                        disabledBorderColor = Color.Transparent,
                                        cursorColor = Color(0xFF38BDF8),
                                        focusedTextColor = Color(0xFFE2E8F0),
                                        unfocusedTextColor = Color(0xFFE2E8F0),
                                        disabledTextColor = Color(0xFF94A3B8)
                                    ),
                                    minLines = 9,
                                    maxLines = 20,
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .testTag("final_project_code_input")
                                )
                            }
                        }
                    }
                }
            }

            // 6. External Project URL & Student Notes Card
            item(key = "url_and_notes_card") {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                    border = BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.6f))
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                imageVector = Icons.Default.Link,
                                contentDescription = null,
                                tint = MaterialTheme.colorScheme.primary,
                                modifier = Modifier.size(20.dp)
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = stringResource(R.string.final_project_url_header),
                                style = MaterialTheme.typography.titleSmall,
                                fontWeight = FontWeight.Bold
                            )
                        }
                        Spacer(modifier = Modifier.height(8.dp))
                        OutlinedTextField(
                            value = projectUrl,
                            onValueChange = { projectUrl = it },
                            enabled = isEditable,
                            singleLine = true,
                            isError = isUrlInvalid,
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

                        Spacer(modifier = Modifier.height(16.dp))

                        Text(
                            text = stringResource(R.string.final_project_notes_header),
                            style = MaterialTheme.typography.titleSmall,
                            fontWeight = FontWeight.Bold
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        OutlinedTextField(
                            value = studentNotes,
                            onValueChange = { studentNotes = it },
                            enabled = isEditable,
                            minLines = 3,
                            maxLines = 6,
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

        }
    }

    // Live Project Preview Dialog (renders student's HTML/CSS/JS before submission)
    if (showPreviewDialog) {
        val previewHtml = remember(workspaceCode) {
            val trimmed = workspaceCode.trim()
            if (trimmed.contains("<html", ignoreCase = true) || trimmed.contains("<body", ignoreCase = true) || trimmed.contains("<div", ignoreCase = true) || trimmed.contains("<header", ignoreCase = true)) {
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
                            .background(MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f))
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
                        htmlCode = previewHtml,
                        cssCode = "",
                        jsCode = "",
                        runTrigger = 1,
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
