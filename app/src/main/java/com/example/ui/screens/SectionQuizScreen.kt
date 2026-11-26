package com.example.ui.screens

import android.app.Activity
import android.content.Context
import android.content.ContextWrapper
import androidx.activity.compose.BackHandler
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.animateFloatAsState
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
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.itemsIndexed
import java.util.Locale
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Timer
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.compose.LocalLifecycleOwner
import com.example.R
import com.example.data.local.ActiveSectionQuizSessionEntity
import com.example.data.models.Course
import com.example.data.models.QuizQuestion
import com.example.data.models.SectionQuiz
import com.example.ui.MainViewModel
import com.example.ui.components.MarkdownText
import com.example.ui.components.AppDialog
import com.example.ui.theme.ErrorRed
import com.example.ui.theme.FlameOrange
import com.example.ui.theme.TealLight
import com.example.ui.theme.TealPrimary
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.json.JSONObject

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SectionQuizScreen(
    course: Course,
    quiz: SectionQuiz,
    questions: List<QuizQuestion>,
    viewModel: MainViewModel,
    onBack: () -> Unit,
    onSubmitSuccess: (attemptId: Long) -> Unit,
    modifier: Modifier = Modifier
) {
    val activeQuestions = remember(quiz.id, questions) { questions }

    if (activeQuestions.isEmpty()) {
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            CircularProgressIndicator(color = MaterialTheme.colorScheme.primary)
        }
        return
    }

    val coroutineScope = rememberCoroutineScope()
    val context = LocalContext.current
    val lifecycleOwner = LocalLifecycleOwner.current

    var currentQuestionIndex by rememberSaveable { mutableIntStateOf(0) }
    var secondsRemaining by rememberSaveable { mutableIntStateOf(quiz.durationMinutes * 60) }
    var showExitDialog by rememberSaveable { mutableStateOf(false) }
    var showSubmitDialog by rememberSaveable { mutableStateOf(false) }
    var showFocusWarningDialog by rememberSaveable { mutableStateOf(false) }
    var isSubmitted by rememberSaveable { mutableStateOf(false) }
    var violationCount by rememberSaveable { mutableIntStateOf(0) }
    var terminatedAttemptId by rememberSaveable { mutableLongStateOf(0L) }

    // Map questionId -> selectedOptionText (dicocokkan berdasarkan ID soal & teks opsi)
    val userAnswers = remember { mutableStateMapOf<Int, String>() }

    // Initial session check from Room (mendukung resume sesi)
    LaunchedEffect(quiz.id) {
        val session = viewModel.startSectionQuizSession(course, quiz, activeQuestions)
        violationCount = session.violationCount
        if (session.answersJson.isNotBlank() && session.answersJson != "{}") {
            try {
                val json = JSONObject(session.answersJson)
                val keys = json.keys()
                while (keys.hasNext()) {
                    val k = keys.next()
                    val qId = k.toIntOrNull()
                    if (qId != null) {
                        userAnswers[qId] = json.getString(k)
                    }
                }
            } catch (_: Exception) {}
        }
        if (session.isTerminated) {
            val failedAttempt = viewModel.submitSectionQuiz(
                quiz = quiz,
                questions = activeQuestions,
                userAnswersByQuestionId = userAnswers.toMap(),
                failReason = "FOCUS_VIOLATION"
            )
            isSubmitted = true
            onSubmitSuccess(failedAttempt.id)
        } else if (session.violationCount == 1) {
            showFocusWarningDialog = true
        }
    }

    fun triggerSubmit(failReason: String? = null) {
        if (!isSubmitted) {
            isSubmitted = true
            coroutineScope.launch {
                val attempt = viewModel.submitSectionQuiz(
                    quiz = quiz,
                    questions = activeQuestions,
                    userAnswersByQuestionId = userAnswers.toMap(),
                    failReason = failReason
                )
                onSubmitSuccess(attempt.id)
            }
        }
    }

    // Lifecycle Observer for Focus Violation Detection
    DisposableEffect(lifecycleOwner) {
        val observer = LifecycleEventObserver { _, event ->
            when (event) {
                Lifecycle.Event.ON_STOP -> {
                    val isChangingConfig = context.findActivity()?.isChangingConfigurations == true
                    if (!isChangingConfig && !isSubmitted) {
                        // The user actually left the app (Home, Recent Apps, Notifications, etc.)
                        coroutineScope.launch {
                            val (newViolationCount, terminatedAttempt) = viewModel.recordSectionQuizViolation(
                                course = course,
                                quiz = quiz,
                                questions = activeQuestions,
                                currentAnswers = userAnswers.toMap()
                            )
                            violationCount = newViolationCount
                            if (terminatedAttempt != null) {
                                isSubmitted = true
                                terminatedAttemptId = terminatedAttempt.id
                            }
                        }
                    }
                }
                Lifecycle.Event.ON_START -> {
                    val isChangingConfig = context.findActivity()?.isChangingConfigurations == true
                    if (!isChangingConfig && !isSubmitted) {
                        if (terminatedAttemptId != 0L) {
                            isSubmitted = true
                            onSubmitSuccess(terminatedAttemptId)
                        } else if (violationCount >= 2) {
                            triggerSubmit(failReason = "FOCUS_VIOLATION")
                        } else if (violationCount == 1) {
                            showFocusWarningDialog = true
                        }
                    }
                }
                else -> {}
            }
        }
        lifecycleOwner.lifecycle.addObserver(observer)
        onDispose {
            lifecycleOwner.lifecycle.removeObserver(observer)
        }
    }

    // Countdown Timer (15 minutes -> 0)
    LaunchedEffect(isSubmitted) {
        while (secondsRemaining > 0 && !isSubmitted) {
            delay(1000)
            secondsRemaining--
        }
        if (secondsRemaining <= 0 && !isSubmitted) {
            // Waktu habis, otomatis submit
            triggerSubmit(failReason = null)
        }
    }

    BackHandler {
        showExitDialog = true
    }

    val currentQ = activeQuestions[currentQuestionIndex]
    val selectedOptionText = userAnswers[currentQ.id]
    val answeredCount = userAnswers.size
    val progressFraction = (currentQuestionIndex + 1).toFloat() / activeQuestions.size.toFloat()

    val isUrgentTime = secondsRemaining <= 60
    val timerColor = if (isUrgentTime) ErrorRed else MaterialTheme.colorScheme.primary

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Column {
                        Text(
                            text = quiz.title,
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            text = stringResource(R.string.section_quiz_question_number_format, currentQuestionIndex + 1, activeQuestions.size),
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                },
                navigationIcon = {
                    IconButton(onClick = { showExitDialog = true }) {
                        Icon(
                            imageVector = Icons.Default.Close,
                            contentDescription = stringResource(R.string.section_quiz_exit_dialog_confirm)
                        )
                    }
                },
                actions = {
                    // Countdown Timer badge at top
                    Surface(
                        shape = RoundedCornerShape(20.dp),
                        color = timerColor.copy(alpha = if (isUrgentTime) 0.2f else 0.12f),
                        border = BorderStroke(
                            1.dp,
                            timerColor.copy(alpha = if (isUrgentTime) 0.8f else 0.3f)
                        ),
                        modifier = Modifier.padding(end = 12.dp)
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.padding(horizontal = 10.dp, vertical = 5.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.Timer,
                                contentDescription = null,
                                tint = timerColor,
                                modifier = Modifier.size(16.dp)
                            )
                            Spacer(modifier = Modifier.width(4.dp))
                            val minutes = secondsRemaining / 60
                            val seconds = secondsRemaining % 60
                            Text(
                                text = String.format(Locale.US, "%02d:%02d", minutes, seconds),
                                style = MaterialTheme.typography.labelMedium,
                                fontWeight = FontWeight.Bold,
                                color = timerColor
                            )
                        }
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
                tonalElevation = 3.dp,
                shadowElevation = 6.dp,
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
                            if (currentQuestionIndex > 0) currentQuestionIndex--
                        },
                        enabled = currentQuestionIndex > 0,
                        shape = RoundedCornerShape(12.dp),
                        contentPadding = PaddingValues(horizontal = 14.dp, vertical = 12.dp),
                        modifier = Modifier.weight(0.45f)
                    ) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = null,
                            modifier = Modifier.size(16.dp)
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(stringResource(R.string.section_quiz_btn_prev), fontWeight = FontWeight.SemiBold)
                    }

                    if (currentQuestionIndex < activeQuestions.size - 1) {
                        Button(
                            onClick = { currentQuestionIndex++ },
                            colors = ButtonDefaults.buttonColors(containerColor = TealPrimary),
                            shape = RoundedCornerShape(12.dp),
                            contentPadding = PaddingValues(horizontal = 14.dp, vertical = 12.dp),
                            modifier = Modifier.weight(0.55f)
                        ) {
                            Text(stringResource(R.string.section_quiz_btn_next), color = Color.White, fontWeight = FontWeight.Bold)
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
                            onClick = { showSubmitDialog = true },
                            colors = ButtonDefaults.buttonColors(containerColor = TealPrimary),
                            shape = RoundedCornerShape(12.dp),
                            contentPadding = PaddingValues(horizontal = 14.dp, vertical = 12.dp),
                            modifier = Modifier.weight(0.55f)
                        ) {
                            Text(stringResource(R.string.section_quiz_btn_submit), color = Color.White, fontWeight = FontWeight.Bold)
                        }
                    }
                }
            }
        }
    ) { paddingValues ->
        val animatedProgress by animateFloatAsState(targetValue = progressFraction, label = "secQuizProgress")
        Column(
            modifier = modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            LinearProgressIndicator(
                progress = { animatedProgress },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(6.dp),
                color = TealPrimary,
                trackColor = MaterialTheme.colorScheme.surfaceVariant
            )

            // Context Strip: Passing threshold & remaining unanswered count
            val remainingUnanswered = (activeQuestions.size - answeredCount).coerceAtLeast(0)
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(MaterialTheme.colorScheme.surface)
                    .padding(horizontal = 16.dp, vertical = 6.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = if (remainingUnanswered == 0) {
                        stringResource(R.string.quiz_context_all_answered_format, quiz.passingPercent, activeQuestions.size)
                    } else {
                        stringResource(R.string.quiz_context_banner_format, quiz.passingPercent, activeQuestions.size, remainingUnanswered)
                    },
                    style = MaterialTheme.typography.labelSmall,
                    fontWeight = FontWeight.SemiBold,
                    color = if (remainingUnanswered == 0) TealPrimary else MaterialTheme.colorScheme.onSurfaceVariant
                )
            }

            // Interactive Question Number Navigator
            LazyRow(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(MaterialTheme.colorScheme.surface)
                    .padding(horizontal = 16.dp, vertical = 10.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                itemsIndexed(activeQuestions, key = { idx, q -> "sec_nav_${q.id}_$idx" }) { idx, q ->
                    val isAnswered = userAnswers.containsKey(q.id)
                    val isCurrent = idx == currentQuestionIndex
                    Surface(
                        onClick = { currentQuestionIndex = idx },
                        shape = RoundedCornerShape(10.dp),
                        color = when {
                            isCurrent -> TealPrimary
                            isAnswered -> TealPrimary.copy(alpha = 0.16f)
                            else -> MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.6f)
                        },
                        border = if (isCurrent) null else BorderStroke(
                            1.dp,
                            if (isAnswered) TealPrimary.copy(alpha = 0.4f) else MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.5f)
                        ),
                        modifier = Modifier.size(34.dp)
                    ) {
                        Box(contentAlignment = Alignment.Center) {
                            Text(
                                text = "${idx + 1}",
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Bold,
                                color = when {
                                    isCurrent -> Color.White
                                    isAnswered -> TealPrimary
                                    else -> MaterialTheme.colorScheme.onSurfaceVariant
                                }
                            )
                        }
                    }
                }
            }

            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 16.dp),
                contentPadding = PaddingValues(top = 16.dp, bottom = 24.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                // Question Card
                item {
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(16.dp),
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
                                Surface(
                                    shape = RoundedCornerShape(6.dp),
                                    color = MaterialTheme.colorScheme.primary.copy(alpha = 0.12f)
                                ) {
                                    Text(
                                        text = stringResource(R.string.section_quiz_question_number_format, currentQuestionIndex + 1, activeQuestions.size),
                                        style = MaterialTheme.typography.labelSmall,
                                        fontWeight = FontWeight.Bold,
                                        color = MaterialTheme.colorScheme.primary,
                                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                                    )
                                }
                                Text(
                                    text = if (selectedOptionText != null) "✓" else stringResource(R.string.section_quiz_result_unanswered),
                                    style = MaterialTheme.typography.labelSmall,
                                    color = if (selectedOptionText != null) TealLight else MaterialTheme.colorScheme.onSurfaceVariant
                                )
                            }

                            Spacer(modifier = Modifier.height(14.dp))

                            MarkdownText(
                                text = currentQ.question,
                                style = MaterialTheme.typography.titleMedium.copy(lineHeight = 24.sp),
                                color = MaterialTheme.colorScheme.onSurface
                            )
                        }
                    }
                }

                // Options Section Header
                item {
                    Text(
                        text = stringResource(R.string.section_quiz_rules_subtitle),
                        style = MaterialTheme.typography.labelMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }

                // Options List (Urutan opsi teracak stabil per attempt)
                items(
                    count = currentQ.options.size,
                    key = { optIdx -> "sec_q_${currentQ.id}_opt_$optIdx" }
                ) { optIdx ->
                    val optionText = currentQ.options[optIdx]
                    val isSelected = selectedOptionText == optionText
                    val optionLetters = listOf("A", "B", "C", "D", "E")
                    val optionLetter = optionLetters.getOrElse(optIdx) { "${optIdx + 1}" }

                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable {
                                userAnswers[currentQ.id] = optionText
                                coroutineScope.launch {
                                    viewModel.updateActiveSectionQuizAnswers(quiz.id, userAnswers.toMap())
                                }
                            }
                            .then(
                                if (isSelected) {
                                    Modifier.border(
                                        width = 2.dp,
                                        color = MaterialTheme.colorScheme.primary,
                                        shape = RoundedCornerShape(14.dp)
                                    )
                                } else Modifier
                            ),
                        shape = RoundedCornerShape(14.dp),
                        colors = CardDefaults.cardColors(
                            containerColor = if (isSelected) {
                                MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.5f)
                            } else {
                                MaterialTheme.colorScheme.surface
                            }
                        ),
                        border = if (isSelected) {
                            BorderStroke(1.5.dp, TealPrimary)
                        } else {
                            BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.5f))
                        },
                        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(14.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(32.dp)
                                    .clip(CircleShape)
                                    .background(
                                        if (isSelected) MaterialTheme.colorScheme.primary
                                        else MaterialTheme.colorScheme.surfaceVariant
                                    ),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    text = optionLetter,
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 13.sp,
                                    color = if (isSelected) Color.White else MaterialTheme.colorScheme.onSurfaceVariant
                                )
                            }

                            Spacer(modifier = Modifier.width(12.dp))

                            Text(
                                text = optionText,
                                style = MaterialTheme.typography.bodyMedium,
                                color = if (isSelected) {
                                    MaterialTheme.colorScheme.onPrimaryContainer
                                } else {
                                    MaterialTheme.colorScheme.onSurface
                                },
                                modifier = Modifier.weight(1f),
                                lineHeight = 20.sp
                            )
                        }
                    }
                }
            }
        }
    }

    // Dialog Peringatan Pelanggaran Fokus Pertama (1x toleransi)
    if (showFocusWarningDialog) {
        AppDialog(
            onDismissRequest = { /* Modal must be acknowledged */ },
            icon = {
                Box(
                    modifier = Modifier
                        .size(52.dp)
                        .clip(CircleShape)
                        .background(FlameOrange.copy(alpha = 0.15f)),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.Warning,
                        contentDescription = null,
                        tint = FlameOrange,
                        modifier = Modifier.size(28.dp)
                    )
                }
            },
            title = {
                Text(
                    text = stringResource(R.string.section_quiz_exit_dialog_title),
                    fontWeight = FontWeight.Bold,
                    style = MaterialTheme.typography.titleMedium
                )
            },
            text = {
                Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                    Text(
                        text = stringResource(R.string.section_quiz_exit_dialog_desc),
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        lineHeight = 18.sp
                    )
                }
            },
            confirmButton = {
                Button(
                    onClick = { showFocusWarningDialog = false },
                    shape = RoundedCornerShape(12.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = FlameOrange)
                ) {
                    Text(stringResource(R.string.section_quiz_exit_dialog_cancel), color = Color.White, fontWeight = FontWeight.Bold)
                }
            }
        )
    }

    // Dialog Konfirmasi Keluar Manual (TIDAK dihitung gagal)
    if (showExitDialog) {
        AppDialog(
            onDismissRequest = { showExitDialog = false },
            icon = {
                Box(
                    modifier = Modifier
                        .size(52.dp)
                        .clip(CircleShape)
                        .background(ErrorRed.copy(alpha = 0.12f)),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.Close,
                        contentDescription = null,
                        tint = ErrorRed,
                        modifier = Modifier.size(26.dp)
                    )
                }
            },
            title = {
                Text(
                    text = stringResource(R.string.section_quiz_exit_dialog_title),
                    fontWeight = FontWeight.Bold,
                    style = MaterialTheme.typography.titleMedium
                )
            },
            text = {
                Text(
                    text = stringResource(R.string.section_quiz_exit_dialog_desc),
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    textAlign = androidx.compose.ui.text.style.TextAlign.Center,
                    lineHeight = 20.sp
                )
            },
            confirmButton = {
                Button(
                    onClick = {
                        showExitDialog = false
                        coroutineScope.launch {
                            viewModel.clearActiveSectionQuizSession(quiz.id)
                            onBack()
                        }
                    },
                    shape = RoundedCornerShape(12.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = ErrorRed
                    )
                ) {
                    Text(stringResource(R.string.section_quiz_exit_dialog_confirm), color = Color.White, fontWeight = FontWeight.Bold)
                }
            },
            dismissButton = {
                OutlinedButton(
                    onClick = { showExitDialog = false },
                    shape = RoundedCornerShape(12.dp),
                    border = BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant)
                ) {
                    Text(stringResource(R.string.section_quiz_exit_dialog_cancel))
                }
            }
        )
    }

    // Dialog Konfirmasi Submit
    if (showSubmitDialog) {
        AppDialog(
            onDismissRequest = { showSubmitDialog = false },
            title = {
                Text(
                    text = stringResource(R.string.section_quiz_submit_dialog_title),
                    fontWeight = FontWeight.Bold,
                    style = MaterialTheme.typography.titleMedium
                )
            },
            text = {
                Column {
                    Text(
                        text = stringResource(R.string.quiz_screen_answered_count_format, answeredCount, activeQuestions.size),
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                    if (answeredCount < activeQuestions.size) {
                        Spacer(modifier = Modifier.height(10.dp))
                        Surface(
                            color = ErrorRed.copy(alpha = 0.10f),
                            shape = RoundedCornerShape(10.dp),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Column(
                                modifier = Modifier.padding(10.dp),
                                verticalArrangement = Arrangement.spacedBy(6.dp)
                            ) {
                                Text(
                                    text = stringResource(R.string.section_quiz_submit_dialog_unanswered_format, activeQuestions.size - answeredCount),
                                    color = ErrorRed,
                                    fontSize = 12.sp,
                                    lineHeight = 17.sp
                                )
                                TextButton(
                                    onClick = {
                                        val firstUnansweredIdx = activeQuestions.indexOfFirst { !userAnswers.containsKey(it.id) }
                                        if (firstUnansweredIdx >= 0) {
                                            currentQuestionIndex = firstUnansweredIdx
                                        }
                                        showSubmitDialog = false
                                    },
                                    contentPadding = PaddingValues(horizontal = 0.dp, vertical = 0.dp)
                                ) {
                                    Text(
                                        text = stringResource(R.string.quiz_submit_dialog_jump_unanswered),
                                        color = TealPrimary,
                                        fontWeight = FontWeight.Bold,
                                        fontSize = 12.sp
                                    )
                                }
                            }
                        }
                    }
                }
            },
            confirmButton = {
                Button(
                    onClick = {
                        showSubmitDialog = false
                        triggerSubmit(failReason = null)
                    },
                    shape = RoundedCornerShape(12.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = TealPrimary
                    )
                ) {
                    Text(stringResource(R.string.section_quiz_submit_dialog_confirm), color = Color.White, fontWeight = FontWeight.Bold)
                }
            },
            dismissButton = {
                OutlinedButton(
                    onClick = { showSubmitDialog = false },
                    shape = RoundedCornerShape(12.dp),
                    border = BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant)
                ) {
                    Text(stringResource(R.string.section_quiz_submit_dialog_cancel))
                }
            }
        )
    }
}

/**
 * Helper extension to safely unwrap Context hierarchy and find the host Activity.
 */
private fun Context.findActivity(): Activity? {
    var ctx = this
    while (ctx is ContextWrapper) {
        if (ctx is Activity) return ctx
        ctx = ctx.baseContext
    }
    return null
}
