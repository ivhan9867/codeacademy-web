package com.example.ui.screens

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.animateFloatAsState
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
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Timer
import java.util.Locale
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
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
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.R
import com.example.data.models.Course
import com.example.ui.components.MarkdownText
import com.example.ui.theme.AmberGold
import com.example.ui.theme.TealDark
import com.example.ui.theme.TealLight
import com.example.ui.theme.TealPrimary
import kotlinx.coroutines.delay

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun QuizScreen(
    course: Course,
    onBack: () -> Unit,
    onSubmit: (Int) -> Unit, // passes scorePercent
    modifier: Modifier = Modifier
) {
    val questions = course.quizQuestions
    if (questions.isEmpty()) {
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            Text(stringResource(R.string.quiz_screen_empty))
        }
        return
    }

    var currentQuestionIndex by remember { mutableIntStateOf(0) }
    val userAnswers = remember { mutableStateMapOf<Int, Int>() } // questionIndex to selectedOptionIndex
    var showSubmitConfirmDialog by remember { mutableStateOf(false) }

    // Question Timer (counts up seconds spent)
    var secondsElapsed by remember { mutableIntStateOf(0) }
    LaunchedEffect(Unit) {
        while (true) {
            delay(1000)
            secondsElapsed++
        }
    }

    val currentQ = questions[currentQuestionIndex]
    val selectedOption = userAnswers[currentQuestionIndex]
    val animatedProgress by animateFloatAsState(
        targetValue = (currentQuestionIndex + 1).toFloat() / questions.size.toFloat(),
        label = "quizProgress"
    )

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Column {
                        Text(
                            text = stringResource(R.string.quiz_screen_top_title),
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold,
                            maxLines = 1
                        )
                        Text(
                            text = course.title,
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                            maxLines = 1
                        )
                    }
                },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(
                            imageVector = Icons.Default.Close,
                            contentDescription = stringResource(R.string.quiz_screen_back_cd)
                        )
                    }
                },
                actions = {
                    Surface(
                        color = TealPrimary.copy(alpha = 0.12f),
                        border = BorderStroke(1.dp, TealPrimary.copy(alpha = 0.25f)),
                        shape = RoundedCornerShape(999.dp),
                        modifier = Modifier.padding(end = 12.dp)
                    ) {
                        Row(
                            modifier = Modifier.padding(horizontal = 10.dp, vertical = 5.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(
                                imageVector = Icons.Default.Timer,
                                contentDescription = null,
                                tint = MaterialTheme.colorScheme.primary,
                                modifier = Modifier.size(15.dp)
                            )
                            Spacer(modifier = Modifier.width(4.dp))
                            val mins = secondsElapsed / 60
                            val secs = secondsElapsed % 60
                            Text(
                                text = String.format(Locale.US, "%02d:%02d", mins, secs),
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.primary
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
                    if (currentQuestionIndex > 0) {
                        com.example.ui.components.SecondaryButton(
                            text = stringResource(R.string.quiz_screen_btn_prev),
                            onClick = { currentQuestionIndex-- },
                            icon = Icons.AutoMirrored.Filled.ArrowBack,
                            modifier = Modifier.weight(0.45f)
                        )
                    }

                    val isLast = currentQuestionIndex == questions.size - 1
                    com.example.ui.components.PrimaryButton(
                        text = if (isLast) stringResource(R.string.quiz_screen_btn_submit) else stringResource(R.string.quiz_screen_btn_next),
                        onClick = {
                            if (isLast) {
                                showSubmitConfirmDialog = true
                            } else {
                                currentQuestionIndex++
                            }
                        },
                        icon = if (!isLast) Icons.AutoMirrored.Filled.ArrowForward else Icons.Default.Check,
                        modifier = Modifier.weight(if (currentQuestionIndex > 0) 0.55f else 1f)
                    )
                }
            }
        },
        modifier = modifier
    ) { paddingValues ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(horizontal = 16.dp),
            contentPadding = PaddingValues(top = 12.dp, bottom = 24.dp),
            verticalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            // Question Progress + Interactive Navigator
            item(key = "quiz_progress_header") {
                val unansweredCount = (questions.size - userAnswers.size).coerceAtLeast(0)
                Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                    Surface(
                        color = if (unansweredCount == 0) TealPrimary.copy(alpha = 0.12f) else MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.55f),
                        shape = RoundedCornerShape(10.dp),
                        border = BorderStroke(
                            1.dp,
                            if (unansweredCount == 0) TealPrimary.copy(alpha = 0.35f) else MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.5f)
                        ),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text(
                            text = if (unansweredCount == 0) {
                                stringResource(R.string.quiz_context_all_answered_format, 70, questions.size)
                            } else {
                                stringResource(R.string.quiz_context_banner_format, 70, questions.size, unansweredCount)
                            },
                            style = MaterialTheme.typography.labelSmall,
                            fontWeight = FontWeight.SemiBold,
                            color = if (unansweredCount == 0) TealPrimary else MaterialTheme.colorScheme.onSurfaceVariant,
                            modifier = Modifier.padding(horizontal = 12.dp, vertical = 7.dp)
                        )
                    }
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = stringResource(R.string.quiz_screen_question_progress_format, currentQuestionIndex + 1, questions.size),
                            style = MaterialTheme.typography.titleSmall,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.onBackground
                        )
                        Surface(
                            shape = RoundedCornerShape(999.dp),
                            color = MaterialTheme.colorScheme.secondaryContainer.copy(alpha = 0.65f)
                        ) {
                            Text(
                                text = stringResource(R.string.quiz_screen_answered_count_format, userAnswers.size, questions.size),
                                style = MaterialTheme.typography.labelSmall,
                                fontWeight = FontWeight.SemiBold,
                                color = MaterialTheme.colorScheme.onSecondaryContainer,
                                modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp)
                            )
                        }
                    }

                    LinearProgressIndicator(
                        progress = { animatedProgress },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(7.dp)
                            .clip(RoundedCornerShape(999.dp)),
                        color = TealPrimary,
                        trackColor = MaterialTheme.colorScheme.surfaceVariant
                    )

                    LazyRow(
                        horizontalArrangement = Arrangement.spacedBy(6.dp),
                        contentPadding = PaddingValues(vertical = 2.dp)
                    ) {
                        itemsIndexed(questions, key = { idx, q -> "nav_${q.id}_$idx" }) { idx, _ ->
                            val isCurrent = idx == currentQuestionIndex
                            val isAnswered = userAnswers.containsKey(idx)
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
                }
            }

            // Question Text Card
            item(key = "quiz_question_card_${currentQ.id}") {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(20.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.surface
                    ),
                    border = BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.55f)),
                    elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
                ) {
                    Column(modifier = Modifier.padding(20.dp)) {
                        Surface(
                            color = TealPrimary.copy(alpha = 0.12f),
                            shape = RoundedCornerShape(8.dp)
                        ) {
                            Text(
                                text = stringResource(R.string.quiz_screen_question_progress_format, currentQuestionIndex + 1, questions.size),
                                color = MaterialTheme.colorScheme.primary,
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Bold,
                                modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp)
                            )
                        }

                        Spacer(modifier = Modifier.height(14.dp))

                        MarkdownText(
                            text = currentQ.question,
                            style = MaterialTheme.typography.titleMedium.copy(fontSize = 16.sp, fontWeight = FontWeight.SemiBold),
                            color = MaterialTheme.colorScheme.onSurface,
                            lineHeight = 24.sp
                        )
                    }
                }
            }

            // Option Choices
            items(
                count = currentQ.options.size,
                key = { optIndex -> "q_${currentQ.id}_opt_$optIndex" }
            ) { optIndex ->
                val optionText = currentQ.options[optIndex]
                val isSelected = selectedOption == optIndex
                val optionLetter = ('A' + optIndex)
                val containerColor by animateColorAsState(
                    targetValue = if (isSelected) TealPrimary.copy(alpha = 0.12f) else MaterialTheme.colorScheme.surface,
                    label = "quizOptBg"
                )
                val borderColor by animateColorAsState(
                    targetValue = if (isSelected) TealPrimary else MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.6f),
                    label = "quizOptBorder"
                )

                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(16.dp))
                        .clickable {
                            userAnswers[currentQuestionIndex] = optIndex
                        },
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = containerColor),
                    border = BorderStroke(if (isSelected) 2.dp else 1.dp, borderColor),
                    elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp, vertical = 14.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Box(
                            modifier = Modifier
                                .size(34.dp)
                                .clip(CircleShape)
                                .background(
                                    if (isSelected) TealPrimary else MaterialTheme.colorScheme.surfaceVariant
                                ),
                            contentAlignment = Alignment.Center
                        ) {
                            if (isSelected) {
                                Icon(
                                    imageVector = Icons.Default.Check,
                                    contentDescription = null,
                                    tint = Color.White,
                                    modifier = Modifier.size(18.dp)
                                )
                            } else {
                                Text(
                                    text = "$optionLetter",
                                    fontWeight = FontWeight.Bold,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                                    fontSize = 14.sp
                                )
                            }
                        }

                        Spacer(modifier = Modifier.width(14.dp))

                        MarkdownText(
                            text = optionText,
                            style = MaterialTheme.typography.bodyMedium.copy(fontSize = 14.sp),
                            color = MaterialTheme.colorScheme.onSurface,
                            lineHeight = 20.sp,
                            modifier = Modifier.weight(1f)
                        )
                    }
                }
            }
        }
    }

    if (showSubmitConfirmDialog) {
        val answeredCount = userAnswers.size
        val unansweredCount = (questions.size - answeredCount).coerceAtLeast(0)
        com.example.ui.components.AppDialog(
            onDismissRequest = { showSubmitConfirmDialog = false },
            title = {
                Text(
                    text = stringResource(R.string.quiz_submit_dialog_title),
                    fontWeight = FontWeight.Bold,
                    style = MaterialTheme.typography.titleMedium
                )
            },
            text = {
                Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                    Text(
                        text = stringResource(R.string.quiz_submit_dialog_summary_format, answeredCount, questions.size),
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                    if (unansweredCount > 0) {
                        Surface(
                            color = com.example.ui.theme.ErrorRed.copy(alpha = 0.10f),
                            shape = RoundedCornerShape(10.dp),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Column(
                                modifier = Modifier.padding(10.dp),
                                verticalArrangement = Arrangement.spacedBy(6.dp)
                            ) {
                                Text(
                                    text = stringResource(R.string.quiz_submit_dialog_unanswered_warn_format, unansweredCount),
                                    color = com.example.ui.theme.ErrorRed,
                                    fontSize = 12.sp,
                                    lineHeight = 17.sp
                                )
                                androidx.compose.material3.TextButton(
                                    onClick = {
                                        val firstMissing = questions.indices.firstOrNull { !userAnswers.containsKey(it) }
                                        if (firstMissing != null) {
                                            currentQuestionIndex = firstMissing
                                        }
                                        showSubmitConfirmDialog = false
                                    },
                                    contentPadding = PaddingValues(0.dp)
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
                androidx.compose.material3.Button(
                    onClick = {
                        showSubmitConfirmDialog = false
                        var correctCount = 0
                        questions.forEachIndexed { idx, q ->
                            if (userAnswers[idx] == q.correctIndex) {
                                correctCount++
                            }
                        }
                        val scorePercent = ((correctCount.toFloat() / questions.size.toFloat()) * 100).toInt()
                        onSubmit(scorePercent)
                    },
                    shape = RoundedCornerShape(12.dp),
                    colors = androidx.compose.material3.ButtonDefaults.buttonColors(containerColor = TealPrimary)
                ) {
                    Text(
                        text = stringResource(R.string.quiz_submit_dialog_confirm),
                        color = Color.White,
                        fontWeight = FontWeight.Bold
                    )
                }
            },
            dismissButton = {
                androidx.compose.material3.OutlinedButton(
                    onClick = { showSubmitConfirmDialog = false },
                    shape = RoundedCornerShape(12.dp),
                    border = BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant)
                ) {
                    Text(stringResource(R.string.common_cancel))
                }
            }
        )
    }
}
