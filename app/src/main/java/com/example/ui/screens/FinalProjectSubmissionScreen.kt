package com.example.ui.screens

import android.widget.Toast
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
import androidx.compose.material.icons.filled.Code
import androidx.compose.material.icons.filled.ExpandLess
import androidx.compose.material.icons.filled.ExpandMore
import androidx.compose.material.icons.filled.HourglassTop
import androidx.compose.material.icons.filled.Link
import androidx.compose.material.icons.filled.RateReview
import androidx.compose.material.icons.filled.Restore
import androidx.compose.material.icons.filled.Save
import androidx.compose.material.icons.filled.Science
import androidx.compose.material.icons.filled.WorkspacePremium
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
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

    val defaultPassFeedback = stringResource(R.string.final_project_simulator_default_pass_feedback)
    val defaultRevFeedback = stringResource(R.string.final_project_simulator_default_rev_feedback)
    var simulatedScore by remember { mutableIntStateOf(90) }
    var simulatedFeedback by remember(defaultPassFeedback) { mutableStateOf(defaultPassFeedback) }
    var isSimulatorExpanded by remember { mutableStateOf(false) }

    val isEditable = status != ProjectSubmissionStatus.PENDING_REVIEW
    val toastDraftSaved = stringResource(R.string.final_project_toast_draft_saved)
    val toastCheckAllRubrics = stringResource(R.string.final_project_toast_check_all_rubrics)
    val toastSubmitted = stringResource(R.string.final_project_toast_submitted)

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

                        Spacer(modifier = Modifier.height(12.dp))

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

                        OutlinedTextField(
                            value = workspaceCode,
                            onValueChange = { workspaceCode = it },
                            enabled = isEditable,
                            placeholder = {
                                Text(stringResource(R.string.final_project_code_placeholder))
                            },
                            textStyle = MaterialTheme.typography.bodySmall.copy(
                                fontFamily = FontFamily.Monospace,
                                lineHeight = 18.sp
                            ),
                            minLines = 8,
                            maxLines = 18,
                            modifier = Modifier
                                .fillMaxWidth()
                                .testTag("final_project_code_input")
                        )
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

            // 7. Save Draft & Submit Buttons
            item(key = "submission_action_buttons") {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    OutlinedButton(
                        onClick = {
                            onSaveDraft(workspaceCode, projectUrl, studentNotes, checkedRubricIds)
                            Toast.makeText(context, toastDraftSaved, Toast.LENGTH_SHORT).show()
                        },
                        enabled = isEditable,
                        shape = RoundedCornerShape(12.dp),
                        modifier = Modifier.weight(1f)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Save,
                            contentDescription = null,
                            modifier = Modifier.size(18.dp)
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(stringResource(R.string.final_project_btn_save_draft))
                    }

                    Button(
                        onClick = {
                            val allRubricsChecked = spec.rubrics.all { it.id in checkedRubricIds }
                            val hasContent = workspaceCode.isNotBlank() || projectUrl.isNotBlank()
                            if (!allRubricsChecked || !hasContent) {
                                Toast.makeText(context, toastCheckAllRubrics, Toast.LENGTH_SHORT).show()
                            } else {
                                onSubmitProject(workspaceCode, projectUrl, studentNotes, checkedRubricIds)
                                Toast.makeText(context, toastSubmitted, Toast.LENGTH_SHORT).show()
                            }
                        },
                        enabled = isEditable,
                        colors = ButtonDefaults.buttonColors(containerColor = TealPrimary),
                        shape = RoundedCornerShape(12.dp),
                        modifier = Modifier
                            .weight(1.3f)
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
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }

            // 8. Facilitator Review Section / Simulator Toggle (Debug-only)
            if (com.example.BuildConfig.DEBUG) {
                item(key = "facilitator_simulator_card") {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(18.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.55f)
                    ),
                    border = BorderStroke(1.dp, MaterialTheme.colorScheme.primary.copy(alpha = 0.35f))
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween,
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable { isSimulatorExpanded = !isSimulatorExpanded }
                        ) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                modifier = Modifier.weight(1f)
                            ) {
                                Box(
                                    modifier = Modifier
                                        .size(34.dp)
                                        .clip(CircleShape)
                                        .background(MaterialTheme.colorScheme.primary.copy(alpha = 0.15f)),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.Science,
                                        contentDescription = null,
                                        tint = MaterialTheme.colorScheme.primary,
                                        modifier = Modifier.size(20.dp)
                                    )
                                }
                                Spacer(modifier = Modifier.width(10.dp))
                                Column {
                                    Text(
                                        text = stringResource(R.string.final_project_simulator_title),
                                        style = MaterialTheme.typography.titleSmall,
                                        fontWeight = FontWeight.Bold,
                                        color = MaterialTheme.colorScheme.onSurface
                                    )
                                    Text(
                                        text = if (isSimulatorExpanded) "Ketuk untuk menutup simulasi" else "Ketuk untuk membuka pengujian offline",
                                        style = MaterialTheme.typography.labelSmall,
                                        color = MaterialTheme.colorScheme.primary
                                    )
                                }
                            }
                            IconButton(onClick = { isSimulatorExpanded = !isSimulatorExpanded }) {
                                Icon(
                                    imageVector = if (isSimulatorExpanded) Icons.Default.ExpandLess else Icons.Default.ExpandMore,
                                    contentDescription = null,
                                    tint = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                            }
                        }

                        AnimatedVisibility(visible = isSimulatorExpanded) {
                            Column(modifier = Modifier.padding(top = 10.dp)) {
                                Text(
                                    text = stringResource(R.string.final_project_simulator_desc),
                                    style = MaterialTheme.typography.bodySmall,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )

                                Spacer(modifier = Modifier.height(12.dp))
                                Text(
                                    text = stringResource(R.string.final_project_simulator_score_label, simulatedScore),
                                    style = MaterialTheme.typography.labelMedium,
                                    fontWeight = FontWeight.Bold,
                                    color = MaterialTheme.colorScheme.primary
                                )
                                Slider(
                                    value = simulatedScore.toFloat(),
                                    onValueChange = { simulatedScore = it.toInt().coerceIn(40, 100) },
                                    valueRange = 40f..100f
                                )

                                OutlinedTextField(
                                    value = simulatedFeedback,
                                    onValueChange = { simulatedFeedback = it },
                                    label = {
                                        Text(stringResource(R.string.final_project_simulator_feedback_label))
                                    },
                                    minLines = 2,
                                    maxLines = 4,
                                    modifier = Modifier.fillMaxWidth()
                                )

                                Spacer(modifier = Modifier.height(12.dp))

                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                                ) {
                                    OutlinedButton(
                                        onClick = {
                                            val revScore = minOf(simulatedScore, spec.minPassingScore - 5)
                                            val feedbackText = if (simulatedFeedback == defaultPassFeedback) {
                                                defaultRevFeedback
                                            } else {
                                                simulatedFeedback
                                            }
                                            onSimulateFacilitatorReview(false, revScore, feedbackText) {}
                                        },
                                        shape = RoundedCornerShape(10.dp),
                                        modifier = Modifier
                                            .weight(1f)
                                            .testTag("final_project_simulate_revision_btn")
                                    ) {
                                        Text(
                                            text = stringResource(R.string.final_project_simulator_btn_revision),
                                            fontSize = 12.sp
                                        )
                                    }

                                    Button(
                                        onClick = {
                                            val passScore = maxOf(simulatedScore, spec.minPassingScore)
                                            onSimulateFacilitatorReview(true, passScore, simulatedFeedback) { _ ->
                                                // Certificate is automatically generated and user can tap View Certificate
                                            }
                                        },
                                        colors = ButtonDefaults.buttonColors(containerColor = SuccessGreen),
                                        shape = RoundedCornerShape(10.dp),
                                        modifier = Modifier
                                            .weight(1f)
                                            .testTag("final_project_simulate_approve_btn")
                                    ) {
                                        Text(
                                            text = stringResource(R.string.final_project_simulator_btn_approve),
                                            color = Color.White,
                                            fontWeight = FontWeight.Bold,
                                            fontSize = 12.sp
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
