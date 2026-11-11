package com.example.ui.screens.lesson

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.ModuleEntity
import com.example.ui.components.CodeBlockView
import com.example.ui.theme.*

@Composable
fun ModuleLessonScreen(
    module: ModuleEntity?,
    isCompleted: Boolean,
    isLastModule: Boolean,
    onNavigateBack: () -> Unit,
    onMarkCompleted: () -> Unit,
    onNextModule: () -> Unit,
    onOpenPlayground: (courseId: String, moduleId: String) -> Unit,
    onOpenAr: (String) -> Unit,
    onOpenAiTutorWithContext: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    if (module == null) {
        Box(modifier = Modifier.fillMaxSize().background(BackgroundDark), contentAlignment = Alignment.Center) {
            CircularProgressIndicator(color = PrimaryPurple)
        }
        return
    }

    val scrollState = rememberScrollState()

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

                Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    Surface(
                        shape = RoundedCornerShape(8.dp),
                        color = SurfaceDark
                    ) {
                        Row(
                            modifier = Modifier.padding(horizontal = 10.dp, vertical = 5.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(4.dp)
                        ) {
                            Icon(Icons.Outlined.Timer, contentDescription = null, tint = SecondaryTeal, modifier = Modifier.size(16.dp))
                            Text(
                                text = "${module.readingMinutes} mnt baca",
                                style = MaterialTheme.typography.labelSmall,
                                color = SecondaryTeal,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }

                    IconButton(
                        onClick = { onOpenAiTutorWithContext(module.codeSnippet ?: module.title) },
                        modifier = Modifier.clip(CircleShape).background(SurfaceDark)
                    ) {
                        Text("🤖", fontSize = 18.sp)
                    }
                }
            }
        },
        bottomBar = {
            Surface(
                color = SurfaceDark,
                border = CardDefaults.outlinedCardBorder().copy(brush = Brush.verticalGradient(listOf(BorderDark, SurfaceDark))),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 20.dp, vertical = 12.dp)
                        .navigationBarsPadding(),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    if (!isCompleted) {
                        Button(
                            onClick = onMarkCompleted,
                            modifier = Modifier.fillMaxWidth().height(48.dp),
                            shape = RoundedCornerShape(12.dp),
                            colors = ButtonDefaults.buttonColors(containerColor = PrimaryPurple)
                        ) {
                            Icon(Icons.Filled.CheckCircle, contentDescription = null, tint = Color.White)
                            Spacer(modifier = Modifier.width(8.dp))
                            Text("Tandai Selesai & Dapatkan +50 XP 🚀", fontWeight = FontWeight.Bold)
                        }
                    } else {
                        Button(
                            onClick = onNextModule,
                            modifier = Modifier.fillMaxWidth().height(48.dp),
                            shape = RoundedCornerShape(12.dp),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = if (isLastModule) AccentSuccess else SecondaryTealDark
                            )
                        ) {
                            Icon(
                                imageVector = if (isLastModule) Icons.Filled.EmojiEvents else Icons.Filled.ArrowForward,
                                contentDescription = null,
                                tint = Color.White
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = if (isLastModule) "Lanjut ke Quiz & Evaluasi 🎓" else "Lanjut ke Modul Berikutnya ➡️",
                                fontWeight = FontWeight.Bold
                            )
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
                .verticalScroll(scrollState)
        ) {
            Spacer(modifier = Modifier.height(8.dp))

            // Completed badge if already finished
            if (isCompleted) {
                Surface(
                    shape = RoundedCornerShape(8.dp),
                    color = AccentSuccess.copy(alpha = 0.15f),
                    border = CardDefaults.outlinedCardBorder().copy(brush = Brush.horizontalGradient(listOf(AccentSuccess, AccentSuccess))),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Row(
                        modifier = Modifier.padding(10.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Icon(Icons.Filled.CheckCircle, contentDescription = null, tint = AccentSuccess, modifier = Modifier.size(18.dp))
                        Text(
                            text = "Modul ini telah kamu selesaikan! (+50 XP diperoleh)",
                            style = MaterialTheme.typography.labelSmall,
                            color = AccentSuccess,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
                Spacer(modifier = Modifier.height(12.dp))
            }

            Text(
                text = module.title,
                style = MaterialTheme.typography.displayMedium,
                fontWeight = FontWeight.Bold,
                color = TextPrimaryDark
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Markdown / Lesson Content
            Text(
                text = module.content,
                style = MaterialTheme.typography.bodyLarge,
                color = TextPrimaryDark,
                lineHeight = 24.sp
            )

            // Code Snippet Block (if any)
            if (!module.codeSnippet.isNullOrBlank()) {
                Spacer(modifier = Modifier.height(20.dp))
                Text(
                    text = "Contoh Kode Implementasi:",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = SecondaryTeal
                )
                Spacer(modifier = Modifier.height(8.dp))
                CodeBlockView(
                    code = module.codeSnippet,
                    language = module.codeLanguage ?: "code"
                )
            }

            // Interactive Playground Card (if task is defined)
            if (!module.playgroundTask.isNullOrBlank()) {
                Spacer(modifier = Modifier.height(20.dp))
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = SurfaceDark),
                    border = CardDefaults.outlinedCardBorder().copy(brush = Brush.horizontalGradient(listOf(PrimaryPurple, SecondaryTeal)))
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(10.dp)
                        ) {
                            Text("⚡", fontSize = 24.sp)
                            Column {
                                Text(
                                    text = "Tantangan Live Playground",
                                    style = MaterialTheme.typography.titleMedium,
                                    fontWeight = FontWeight.Bold,
                                    color = TextPrimaryDark
                                )
                                Text(
                                    text = "Uji langsung pemahamanmu di Live Code Editor",
                                    style = MaterialTheme.typography.labelSmall,
                                    color = TextSecondaryDark
                                )
                            }
                        }

                        Spacer(modifier = Modifier.height(10.dp))

                        Text(
                            text = module.playgroundTask,
                            style = MaterialTheme.typography.bodyMedium,
                            color = AccentWarning
                        )

                        Spacer(modifier = Modifier.height(14.dp))

                        Button(
                            onClick = { onOpenPlayground(module.courseId, module.id) },
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(10.dp),
                            colors = ButtonDefaults.buttonColors(containerColor = SecondaryTealDark)
                        ) {
                            Icon(Icons.Filled.Code, contentDescription = null, tint = Color.White)
                            Spacer(modifier = Modifier.width(8.dp))
                            Text("Buka Live Playground & Eksekusi", fontWeight = FontWeight.Bold)
                        }
                    }
                }
            }

            // Interactive Visualizer Card (if module has visualization asset)
            if (module.hasArAsset) {
                Spacer(modifier = Modifier.height(20.dp))
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = SurfaceDark),
                    border = CardDefaults.outlinedCardBorder().copy(brush = Brush.horizontalGradient(listOf(AccentWarning, PrimaryPurple)))
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(10.dp)
                        ) {
                            Text("🔬", fontSize = 24.sp)
                            Column {
                                Text(
                                    text = "Visualisasi Interaktif Memori & Kode",
                                    style = MaterialTheme.typography.titleMedium,
                                    fontWeight = FontWeight.Bold,
                                    color = TextPrimaryDark
                                )
                                Text(
                                    text = "Visualisasikan alokasi memori, pointer & aliran eksekusi secara skematik interaktif",
                                    style = MaterialTheme.typography.labelSmall,
                                    color = TextSecondaryDark
                                )
                            }
                        }

                        Spacer(modifier = Modifier.height(12.dp))

                        Button(
                            onClick = { onOpenAr(module.id) },
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(10.dp),
                            colors = ButtonDefaults.buttonColors(containerColor = PrimaryPurple)
                        ) {
                            Icon(Icons.Filled.AccountTree, contentDescription = null, tint = Color.White)
                            Spacer(modifier = Modifier.width(8.dp))
                            Text("Buka Visualisasi Interaktif", fontWeight = FontWeight.Bold)
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(80.dp))
        }
    }
}
