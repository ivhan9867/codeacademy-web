package com.example.ui.screens.quiz

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.CertificateEntity
import com.example.data.model.QuizQuestionEntity
import com.example.ui.theme.*

@Composable
fun QuizScreen(
    courseTitle: String,
    questions: List<QuizQuestionEntity>,
    onNavigateBack: () -> Unit,
    onSubmitQuiz: (Map<Int, Int>) -> Unit,
    modifier: Modifier = Modifier
) {
    if (questions.isEmpty()) {
        Box(modifier = Modifier.fillMaxSize().background(BackgroundDark), contentAlignment = Alignment.Center) {
            Text("Tidak ada pertanyaan kuis untuk kursus ini.", color = TextSecondaryDark)
        }
        return
    }

    var currentQuestionIndex by remember { mutableIntStateOf(0) }
    val userAnswers = remember { mutableStateMapOf<Int, Int>() }

    val currentQuestion = questions[currentQuestionIndex]
    val options = remember(currentQuestion) { currentQuestion.getOptionsList() }

    Scaffold(
        modifier = modifier.fillMaxSize(),
        containerColor = BackgroundDark,
        topBar = {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(BackgroundDark)
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

                Text(
                    text = "Soal ${currentQuestionIndex + 1} dari ${questions.size}",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = SecondaryTeal
                )

                Box(modifier = Modifier.size(40.dp))
            }
        },
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
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    if (currentQuestionIndex > 0) {
                        OutlinedButton(
                            onClick = { currentQuestionIndex-- },
                            modifier = Modifier.weight(1f).height(48.dp),
                            shape = RoundedCornerShape(12.dp)
                        ) {
                            Text("Sebelumnya", color = TextPrimaryDark)
                        }
                    }

                    if (currentQuestionIndex < questions.size - 1) {
                        Button(
                            onClick = { currentQuestionIndex++ },
                            modifier = Modifier.weight(1f).height(48.dp),
                            shape = RoundedCornerShape(12.dp),
                            colors = ButtonDefaults.buttonColors(containerColor = PrimaryPurple)
                        ) {
                            Text("Berikutnya", fontWeight = FontWeight.Bold)
                        }
                    } else {
                        Button(
                            onClick = { onSubmitQuiz(userAnswers.toMap()) },
                            enabled = userAnswers.size == questions.size,
                            modifier = Modifier.weight(1f).height(48.dp),
                            shape = RoundedCornerShape(12.dp),
                            colors = ButtonDefaults.buttonColors(containerColor = AccentSuccess)
                        ) {
                            Text("Kirim Jawaban & Evaluasi 🚀", fontWeight = FontWeight.Bold)
                        }
                    }
                }
            }
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(horizontal = 20.dp)
        ) {
            // Linear Step Indicator
            LinearProgressIndicator(
                progress = { (currentQuestionIndex + 1) / questions.size.toFloat() },
                modifier = Modifier.fillMaxWidth().height(6.dp).clip(RoundedCornerShape(3.dp)),
                color = SecondaryTeal,
                trackColor = SurfaceVariantDark
            )

            Spacer(modifier = Modifier.height(20.dp))

            // Question Card
            Card(
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = SurfaceDark),
                border = CardDefaults.outlinedCardBorder().copy(brush = Brush.horizontalGradient(listOf(PrimaryPurpleDark, SurfaceVariantDark))),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(18.dp)) {
                    Text(
                        text = "Pertanyaan ${currentQuestionIndex + 1}",
                        style = MaterialTheme.typography.labelSmall,
                        color = SecondaryTeal,
                        fontWeight = FontWeight.Bold
                    )
                    Spacer(modifier = Modifier.height(6.dp))
                    Text(
                        text = currentQuestion.question,
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Bold,
                        color = TextPrimaryDark,
                        lineHeight = 26.sp
                    )
                }
            }

            Spacer(modifier = Modifier.height(18.dp))

            // Options List
            options.forEachIndexed { optIndex, optionText ->
                val isSelected = userAnswers[currentQuestionIndex] == optIndex
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 5.dp)
                        .clickable { userAnswers[currentQuestionIndex] = optIndex },
                    shape = RoundedCornerShape(14.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = if (isSelected) PrimaryPurple.copy(alpha = 0.2f) else SurfaceDark
                    ),
                    border = CardDefaults.outlinedCardBorder().copy(
                        brush = if (isSelected) Brush.horizontalGradient(listOf(PrimaryPurple, SecondaryTeal))
                        else Brush.horizontalGradient(listOf(BorderDark, BorderDark))
                    )
                ) {
                    Row(
                        modifier = Modifier.padding(16.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(14.dp)
                    ) {
                        Box(
                            modifier = Modifier
                                .size(32.dp)
                                .clip(CircleShape)
                                .background(if (isSelected) PrimaryPurple else SurfaceVariantDark),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = listOf("A", "B", "C", "D").getOrElse(optIndex) { "${optIndex + 1}" },
                                fontWeight = FontWeight.Bold,
                                color = if (isSelected) Color.White else TextSecondaryDark,
                                fontSize = 13.sp
                            )
                        }

                        Text(
                            text = optionText,
                            style = MaterialTheme.typography.bodyLarge,
                            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                            color = if (isSelected) TextPrimaryDark else TextSecondaryDark,
                            modifier = Modifier.weight(1f)
                        )

                        RadioButton(
                            selected = isSelected,
                            onClick = null,
                            colors = RadioButtonDefaults.colors(selectedColor = SecondaryTeal)
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun QuizResultScreen(
    scorePercent: Int,
    isPassed: Boolean,
    questions: List<QuizQuestionEntity>,
    userAnswers: Map<Int, Int>,
    certificate: CertificateEntity?,
    onRetakeQuiz: () -> Unit,
    onViewCertificate: (String) -> Unit,
    onNavigateHome: () -> Unit,
    modifier: Modifier = Modifier
) {
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
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    if (isPassed && certificate != null) {
                        Button(
                            onClick = { onViewCertificate(certificate.certId) },
                            modifier = Modifier.weight(1f).height(48.dp),
                            shape = RoundedCornerShape(12.dp),
                            colors = ButtonDefaults.buttonColors(containerColor = AccentSuccess)
                        ) {
                            Icon(Icons.Filled.WorkspacePremium, contentDescription = null, tint = Color.White)
                            Spacer(modifier = Modifier.width(6.dp))
                            Text("Buka Sertifikat 🎓", fontWeight = FontWeight.Bold)
                        }
                    } else {
                        Button(
                            onClick = onRetakeQuiz,
                            modifier = Modifier.weight(1f).height(48.dp),
                            shape = RoundedCornerShape(12.dp),
                            colors = ButtonDefaults.buttonColors(containerColor = PrimaryPurple)
                        ) {
                            Icon(Icons.Filled.Refresh, contentDescription = null, tint = Color.White)
                            Spacer(modifier = Modifier.width(6.dp))
                            Text("Ulangi Quiz", fontWeight = FontWeight.Bold)
                        }
                    }

                    OutlinedButton(
                        onClick = onNavigateHome,
                        modifier = Modifier.weight(1f).height(48.dp),
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        Text("Kembali ke Home", color = TextPrimaryDark)
                    }
                }
            }
        }
    ) { paddingValues ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(horizontal = 20.dp),
            contentPadding = PaddingValues(top = 20.dp, bottom = 24.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Hero Score Card
            item(key = "hero_score") {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(20.dp),
                    colors = CardDefaults.cardColors(containerColor = SurfaceDark),
                    border = CardDefaults.outlinedCardBorder().copy(
                        brush = Brush.linearGradient(
                            if (isPassed) listOf(AccentSuccess, SecondaryTeal)
                            else listOf(AccentError, AccentWarning)
                        )
                    )
                ) {
                    Column(
                        modifier = Modifier.padding(24.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            text = if (isPassed) "🎉 🎓 ✨" else "📚 ✍️",
                            fontSize = 36.sp
                        )
                        Spacer(modifier = Modifier.height(10.dp))
                        Text(
                            text = if (isPassed) "SELAMAT, ANDA LULUS!" else "BELUM MEMENUHI SYARAT KELULUSAN",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold,
                            color = if (isPassed) AccentSuccess else AccentError
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = "$scorePercent%",
                            style = MaterialTheme.typography.displayLarge,
                            fontWeight = FontWeight.ExtraBold,
                            color = TextPrimaryDark
                        )
                        Text(
                            text = if (isPassed) "Skor minimum kelulusan 70% telah terpenuhi (+150 XP diperoleh)" else "Skor minimum kelulusan adalah 70%. Silakan pelajari ulang materi dan ulangi kuis.",
                            style = MaterialTheme.typography.bodyMedium,
                            color = TextSecondaryDark,
                            textAlign = TextAlign.Center
                        )
                    }
                }
            }

            // Explanation Header
            item(key = "explanation_header") {
                Text(
                    text = "Pembahasan & Evaluasi Jawaban",
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold,
                    color = TextPrimaryDark
                )
            }

            // Questions Evaluation
            itemsIndexed(questions) { index, question ->
                val chosenIndex = userAnswers[index]
                val isCorrect = chosenIndex != null && chosenIndex == question.correctIndex
                val options = question.getOptionsList()

                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(14.dp),
                    colors = CardDefaults.cardColors(containerColor = SurfaceDark),
                    border = CardDefaults.outlinedCardBorder().copy(
                        brush = if (isCorrect) Brush.horizontalGradient(listOf(AccentSuccess, AccentSuccess))
                        else Brush.horizontalGradient(listOf(AccentError, AccentError))
                    )
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = "Soal ${index + 1}",
                                style = MaterialTheme.typography.labelSmall,
                                fontWeight = FontWeight.Bold,
                                color = if (isCorrect) AccentSuccess else AccentError
                            )
                            Surface(
                                shape = RoundedCornerShape(6.dp),
                                color = if (isCorrect) AccentSuccess.copy(alpha = 0.2f) else AccentError.copy(alpha = 0.2f)
                            ) {
                                Text(
                                    text = if (isCorrect) "Benar (+XP)" else "Salah",
                                    color = if (isCorrect) AccentSuccess else AccentError,
                                    style = MaterialTheme.typography.labelSmall,
                                    fontWeight = FontWeight.Bold,
                                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 2.dp)
                                )
                            }
                        }

                        Spacer(modifier = Modifier.height(8.dp))

                        Text(
                            text = question.question,
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold,
                            color = TextPrimaryDark
                        )

                        Spacer(modifier = Modifier.height(10.dp))

                        Text(
                            text = "Jawabanmu: ${if (chosenIndex != null) options.getOrElse(chosenIndex) { "-" } else "Tidak dijawab"}",
                            style = MaterialTheme.typography.bodyMedium,
                            color = if (isCorrect) AccentSuccess else AccentError
                        )

                        if (!isCorrect) {
                            Text(
                                text = "Jawaban Benar: ${options.getOrElse(question.correctIndex) { "-" }}",
                                style = MaterialTheme.typography.bodyMedium,
                                color = SecondaryTeal,
                                fontWeight = FontWeight.SemiBold
                            )
                        }

                        Spacer(modifier = Modifier.height(8.dp))

                        Surface(
                            shape = RoundedCornerShape(10.dp),
                            color = SurfaceVariantDark.copy(alpha = 0.6f),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Text(
                                text = "💡 Penjelasan: ${question.explanation}",
                                style = MaterialTheme.typography.bodySmall,
                                color = TextSecondaryDark,
                                modifier = Modifier.padding(10.dp),
                                lineHeight = 18.sp
                            )
                        }
                    }
                }
            }
        }
    }
}
