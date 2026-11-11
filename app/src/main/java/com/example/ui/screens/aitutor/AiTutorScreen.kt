package com.example.ui.screens.aitutor

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.Send
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.res.stringResource
import com.example.R
import com.example.data.model.AiChatMessageEntity
import com.example.ui.theme.*
import kotlinx.coroutines.launch

@Composable
fun AiTutorScreen(
    messages: List<AiChatMessageEntity>,
    isGenerating: Boolean,
    initialContextCode: String? = null,
    onNavigateBack: () -> Unit,
    onSendMessage: (String, String?) -> Unit,
    modifier: Modifier = Modifier
) {
    var inputText by remember { mutableStateOf("") }
    var attachedCode by remember { mutableStateOf(initialContextCode ?: "") }
    var showCodeDialog by remember { mutableStateOf(false) }

    val listState = rememberLazyListState()
    val coroutineScope = rememberCoroutineScope()

    LaunchedEffect(messages.size) {
        if (messages.isNotEmpty()) {
            listState.animateScrollToItem(messages.size - 1)
        }
    }

    val quickPrompts = listOf(
        "🐞 Bantu Debugging Kode",
        "💡 Jelaskan Cara Kerja Flexbox",
        "⚡ Optimasi Alur Program",
        "📝 Beri Contoh Pointer di C",
        "🐍 Tips List Comprehension"
    )

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
                Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                    IconButton(
                        onClick = onNavigateBack,
                        modifier = Modifier.clip(CircleShape).background(SurfaceDark)
                    ) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Kembali", tint = TextPrimaryDark)
                    }

                    Box(
                        modifier = Modifier
                            .size(36.dp)
                            .clip(CircleShape)
                            .background(Brush.linearGradient(listOf(PrimaryPurple, SecondaryTeal))),
                        contentAlignment = Alignment.Center
                    ) {
                        Text("🤖", fontSize = 18.sp)
                    }

                    Column {
                        Text(
                            text = stringResource(R.string.ai_tutor_title),
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold,
                            color = TextPrimaryDark
                        )
                        Text(
                            text = stringResource(R.string.ai_tutor_subtitle),
                            style = MaterialTheme.typography.labelSmall,
                            color = SecondaryTeal
                        )
                    }
                }
            }
        },
        bottomBar = {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(SurfaceDark)
                    .navigationBarsPadding()
                    .padding(horizontal = 16.dp, vertical = 10.dp)
            ) {
                // Quick prompt chips
                LazyRow(
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    modifier = Modifier.padding(bottom = 8.dp)
                ) {
                    items(quickPrompts) { prompt ->
                        Surface(
                            shape = RoundedCornerShape(20.dp),
                            color = SurfaceVariantDark,
                            border = CardDefaults.outlinedCardBorder().copy(brush = Brush.horizontalGradient(listOf(BorderDark, BorderDark))),
                            modifier = Modifier.clip(RoundedCornerShape(20.dp))
                        ) {
                            Text(
                                text = prompt,
                                modifier = Modifier
                                    .padding(horizontal = 12.dp, vertical = 6.dp)
                                    .clickable {
                                        onSendMessage(prompt.substring(3), if (attachedCode.isNotBlank()) attachedCode else null)
                                    },
                                style = MaterialTheme.typography.labelSmall,
                                color = SecondaryTeal
                            )
                        }
                    }
                }

                // Attached Code Indicator
                if (attachedCode.isNotBlank()) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(bottom = 6.dp)
                            .clip(RoundedCornerShape(8.dp))
                            .background(CodeBg)
                            .padding(horizontal = 10.dp, vertical = 4.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = stringResource(R.string.ai_tutor_code_attached, attachedCode.lines().size),
                            style = MaterialTheme.typography.labelSmall,
                            color = AccentWarning,
                            fontFamily = FontFamily.Monospace
                        )
                        IconButton(onClick = { attachedCode = "" }, modifier = Modifier.size(24.dp)) {
                            Icon(Icons.Default.Close, contentDescription = "Hapus", tint = TextSecondaryDark, modifier = Modifier.size(16.dp))
                        }
                    }
                }

                // Input Bar
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    IconButton(
                        onClick = { showCodeDialog = true },
                        modifier = Modifier.clip(CircleShape).background(SurfaceVariantDark)
                    ) {
                        Icon(Icons.Default.Code, contentDescription = stringResource(R.string.ai_tutor_attach_code), tint = SecondaryTeal)
                    }

                    OutlinedTextField(
                        value = inputText,
                        onValueChange = { inputText = it },
                        placeholder = { Text(stringResource(R.string.ai_tutor_placeholder), color = TextMutedDark, fontSize = 13.sp) },
                        modifier = Modifier.weight(1f),
                        maxLines = 3,
                        shape = RoundedCornerShape(24.dp),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = PrimaryPurple,
                            unfocusedBorderColor = BorderDark,
                            focusedContainerColor = BackgroundDark,
                            unfocusedContainerColor = BackgroundDark
                        )
                    )

                    IconButton(
                        onClick = {
                            if (inputText.isNotBlank() && !isGenerating) {
                                val msg = inputText
                                val code = if (attachedCode.isNotBlank()) attachedCode else null
                                inputText = ""
                                onSendMessage(msg, code)
                            }
                        },
                        enabled = inputText.isNotBlank() && !isGenerating,
                        modifier = Modifier
                            .size(46.dp)
                            .clip(CircleShape)
                            .background(if (inputText.isNotBlank()) PrimaryPurple else SurfaceVariantDark)
                    ) {
                        Icon(
                            Icons.AutoMirrored.Filled.Send,
                            contentDescription = stringResource(R.string.ai_tutor_send_button),
                            tint = if (inputText.isNotBlank()) Color.White else TextMutedDark,
                            modifier = Modifier.size(20.dp)
                        )
                    }
                }
            }
        }
    ) { paddingValues ->
        LazyColumn(
            state = listState,
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(horizontal = 16.dp),
            contentPadding = PaddingValues(top = 16.dp, bottom = 16.dp),
            verticalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            // Welcome AI bubble
            item(key = "ai_welcome") {
                Card(
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = SurfaceDark),
                    border = CardDefaults.outlinedCardBorder().copy(brush = Brush.horizontalGradient(listOf(PrimaryPurpleDark, SurfaceVariantDark))),
                    modifier = Modifier.fillMaxWidth(0.9f)
                ) {
                    Column(modifier = Modifier.padding(14.dp)) {
                        Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                            Text("🤖", fontSize = 18.sp)
                            Text(stringResource(R.string.ai_tutor_welcome_title), fontWeight = FontWeight.Bold, color = SecondaryTeal, style = MaterialTheme.typography.labelLarge)
                        }
                        Spacer(modifier = Modifier.height(6.dp))
                        Text(
                            text = stringResource(R.string.ai_tutor_welcome_desc),
                            style = MaterialTheme.typography.bodyMedium,
                            color = TextPrimaryDark,
                            lineHeight = 20.sp
                        )
                    }
                }
            }

            items(messages, key = { it.id }) { msg ->
                val isUser = msg.isUser
                Box(
                    modifier = Modifier.fillMaxWidth(),
                    contentAlignment = if (isUser) Alignment.CenterEnd else Alignment.CenterStart
                ) {
                    Card(
                        shape = RoundedCornerShape(
                            topStart = 16.dp,
                            topEnd = 16.dp,
                            bottomStart = if (isUser) 16.dp else 4.dp,
                            bottomEnd = if (isUser) 4.dp else 16.dp
                        ),
                        colors = CardDefaults.cardColors(
                            containerColor = if (isUser) PrimaryPurple else SurfaceDark
                        ),
                        border = if (!isUser) CardDefaults.outlinedCardBorder().copy(brush = Brush.horizontalGradient(listOf(BorderDark, BorderDark))) else null,
                        modifier = Modifier.fillMaxWidth(0.88f)
                    ) {
                        Column(modifier = Modifier.padding(14.dp)) {
                            if (!isUser) {
                                Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                                    Text("🤖", fontSize = 16.sp)
                                    Text("AI Tutor", fontWeight = FontWeight.Bold, color = SecondaryTeal, style = MaterialTheme.typography.labelSmall)
                                }
                                Spacer(modifier = Modifier.height(6.dp))
                            }

                            Text(
                                text = msg.message,
                                style = MaterialTheme.typography.bodyMedium,
                                color = if (isUser) Color.White else TextPrimaryDark,
                                lineHeight = 20.sp
                            )

                            if (!msg.codeSnippet.isNullOrBlank()) {
                                Spacer(modifier = Modifier.height(8.dp))
                                Surface(
                                    shape = RoundedCornerShape(8.dp),
                                    color = CodeBg,
                                    modifier = Modifier.fillMaxWidth()
                                ) {
                                    Text(
                                        text = msg.codeSnippet,
                                        style = MaterialTheme.typography.bodySmall,
                                        fontFamily = FontFamily.Monospace,
                                        color = SyntaxString,
                                        modifier = Modifier.padding(8.dp)
                                    )
                                }
                            }
                        }
                    }
                }
            }

            if (isGenerating) {
                item(key = "generating_indicator") {
                    Card(
                        shape = RoundedCornerShape(16.dp),
                        colors = CardDefaults.cardColors(containerColor = SurfaceDark),
                        modifier = Modifier.width(180.dp)
                    ) {
                        Row(
                            modifier = Modifier.padding(12.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            CircularProgressIndicator(color = SecondaryTeal, modifier = Modifier.size(16.dp), strokeWidth = 2.dp)
                            Text(stringResource(R.string.ai_tutor_thinking), style = MaterialTheme.typography.labelSmall, color = SecondaryTeal)
                        }
                    }
                }
            }
        }
    }

    // Attach Code Modal Dialog
    if (showCodeDialog) {
        var tempCode by remember { mutableStateOf(attachedCode) }
        AlertDialog(
            onDismissRequest = { showCodeDialog = false },
            title = { Text("Lampirkan Potongan Kode", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold, color = TextPrimaryDark) },
            text = {
                OutlinedTextField(
                    value = tempCode,
                    onValueChange = { tempCode = it },
                    placeholder = { Text("Tempel kode di sini...") },
                    modifier = Modifier.fillMaxWidth().height(200.dp),
                    textStyle = TextStyle(fontFamily = FontFamily.Monospace, fontSize = 12.sp, color = SyntaxString),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedContainerColor = CodeBg,
                        unfocusedContainerColor = CodeBg
                    )
                )
            },
            confirmButton = {
                Button(
                    onClick = {
                        attachedCode = tempCode
                        showCodeDialog = false
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = PrimaryPurple)
                ) {
                    Text("Lampirkan")
                }
            },
            dismissButton = {
                TextButton(onClick = { showCodeDialog = false }) {
                    Text("Batal", color = TextSecondaryDark)
                }
            },
            containerColor = SurfaceDark
        )
    }
}
