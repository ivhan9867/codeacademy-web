package com.example.ui.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.ime
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.Send
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.DeleteOutline
import androidx.compose.material.icons.filled.Psychology
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.res.stringResource
import com.example.R
import com.example.data.models.ChatMessage
import com.example.ui.components.CodeBlockView
import com.example.ui.components.MarkdownText
import com.example.ui.theme.CyanAccent
import com.example.ui.theme.TealDark
import com.example.ui.theme.TealPrimary

data class PromptSuggestion(
    val id: String,
    val text: String
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AiTutorScreen(
    courseTitle: String,
    moduleTitle: String,
    messages: List<ChatMessage>,
    isThinking: Boolean,
    connectionStatus: String = "Gemini 2.0",
    onSendMessage: (String) -> Unit,
    onBack: () -> Unit,
    onClearChat: () -> Unit = {},
    onTryInPlayground: (String, String) -> Unit = { _, _ -> },
    modifier: Modifier = Modifier
) {
    var textInput by remember { mutableStateOf("") }
    val listState = rememberLazyListState()

    // Contextual suggestion prompt chips with stable unique IDs
    val pAnalogy = stringResource(R.string.ai_prompt_analogy_format, moduleTitle)
    val pExample = stringResource(R.string.ai_prompt_example_format, courseTitle)
    val pMistakes = stringResource(R.string.ai_prompt_mistakes)
    val pTroubleshoot = stringResource(R.string.ai_prompt_troubleshoot)
    val suggestions = remember(pAnalogy, pExample, pMistakes, pTroubleshoot) {
        listOf(
            PromptSuggestion("analogi", pAnalogy),
            PromptSuggestion("contoh_kode", pExample),
            PromptSuggestion("kesalahan_pemula", pMistakes),
            PromptSuggestion("troubleshoot", pTroubleshoot)
        )
    }

    val imeBottomPx = WindowInsets.ime.getBottom(androidx.compose.ui.platform.LocalDensity.current)
    LaunchedEffect(messages.size, isThinking, imeBottomPx > 0) {
        if (messages.isNotEmpty()) {
            try {
                listState.animateScrollToItem((messages.size - 1).coerceAtLeast(0))
            } catch (_: Throwable) {
                // Ignore scroll animation interruption during screen transition
            }
        }
    }

    Scaffold(
        contentWindowInsets = androidx.compose.foundation.layout.WindowInsets(0.dp),
        topBar = {
            TopAppBar(
                windowInsets = androidx.compose.foundation.layout.WindowInsets(0.dp),
                title = {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        com.example.ui.components.CodeAcademyMascot(
                            size = 36.dp,
                            mood = com.example.ui.components.MascotMood.HAPPY,
                            showWebBadges = false
                        )
                        Spacer(modifier = Modifier.width(10.dp))
                        Column {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(6.dp)
                            ) {
                                Text(
                                    text = stringResource(R.string.ai_tutor_title),
                                    style = MaterialTheme.typography.titleMedium,
                                    fontWeight = FontWeight.Bold
                                )
                                Surface(
                                    shape = RoundedCornerShape(999.dp),
                                    color = if (connectionStatus.contains("Offline", ignoreCase = true)) {
                                        MaterialTheme.colorScheme.errorContainer
                                    } else {
                                        CyanAccent.copy(alpha = 0.18f)
                                    }
                                ) {
                                    Text(
                                        text = connectionStatus,
                                        fontSize = 10.sp,
                                        fontWeight = FontWeight.SemiBold,
                                        maxLines = 1,
                                        softWrap = false,
                                        color = if (connectionStatus.contains("Offline", ignoreCase = true)) {
                                            MaterialTheme.colorScheme.onErrorContainer
                                        } else {
                                            TealPrimary
                                        },
                                        modifier = Modifier.padding(horizontal = 7.dp, vertical = 2.dp)
                                    )
                                }
                            }
                            Text(
                                text = "$courseTitle • $moduleTitle",
                                style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.onSurfaceVariant,
                                maxLines = 1,
                                overflow = TextOverflow.Ellipsis
                            )
                        }
                    }
                },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = stringResource(R.string.common_close)
                        )
                    }
                },
                actions = {
                    if (messages.size > 1) {
                        var showClearConfirm by remember { mutableStateOf(false) }
                        IconButton(onClick = { showClearConfirm = true }) {
                            Icon(
                                imageVector = Icons.Default.DeleteOutline,
                                contentDescription = stringResource(R.string.ai_clear_chat_cd)
                            )
                        }
                        if (showClearConfirm) {
                            androidx.compose.material3.AlertDialog(
                                onDismissRequest = { showClearConfirm = false },
                                title = { Text(stringResource(R.string.ai_clear_chat_confirm_title), fontWeight = FontWeight.Bold) },
                                text = { Text(stringResource(R.string.ai_clear_chat_confirm_desc)) },
                                confirmButton = {
                                    androidx.compose.material3.TextButton(
                                        onClick = {
                                            showClearConfirm = false
                                            onClearChat()
                                        }
                                    ) {
                                        Text(
                                            stringResource(R.string.ai_clear_chat_confirm_btn),
                                            color = MaterialTheme.colorScheme.error,
                                            fontWeight = FontWeight.Bold
                                        )
                                    }
                                },
                                dismissButton = {
                                    androidx.compose.material3.TextButton(onClick = { showClearConfirm = false }) {
                                        Text(stringResource(R.string.common_cancel))
                                    }
                                }
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
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(MaterialTheme.colorScheme.surface)
                    .navigationBarsPadding()
                    .imePadding()
                    .padding(horizontal = 16.dp, vertical = 8.dp)
            ) {
                // Quick suggested prompts
                LazyRow(
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    contentPadding = PaddingValues(bottom = 8.dp)
                ) {
                    items(suggestions, key = { it.id }) { prompt ->
                        Surface(
                            shape = RoundedCornerShape(16.dp),
                            color = MaterialTheme.colorScheme.surfaceVariant,
                            modifier = Modifier.clickable {
                                onSendMessage(prompt.text)
                            }
                        ) {
                            Row(
                                modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Icon(
                                    imageVector = Icons.Default.AutoAwesome,
                                    contentDescription = null,
                                    tint = MaterialTheme.colorScheme.primary,
                                    modifier = Modifier.size(14.dp)
                                )
                                Spacer(modifier = Modifier.width(6.dp))
                                Text(
                                    text = prompt.text,
                                    fontSize = 11.sp,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                            }
                        }
                    }
                }

                // Chat Input Field
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    OutlinedTextField(
                        value = textInput,
                        onValueChange = { textInput = it },
                        placeholder = { Text(stringResource(R.string.ai_tutor_placeholder), fontSize = 13.sp) },
                        shape = RoundedCornerShape(24.dp),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = MaterialTheme.colorScheme.primary,
                            unfocusedBorderColor = MaterialTheme.colorScheme.surfaceVariant,
                            focusedContainerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f),
                            unfocusedContainerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f)
                        ),
                        modifier = Modifier.weight(1f),
                        maxLines = 4
                    )

                    Spacer(modifier = Modifier.width(8.dp))

                    IconButton(
                        onClick = {
                            if (textInput.isNotBlank()) {
                                val msg = textInput
                                textInput = ""
                                onSendMessage(msg)
                            }
                        },
                        enabled = textInput.isNotBlank() && !isThinking,
                        modifier = Modifier
                            .size(46.dp)
                            .clip(CircleShape)
                            .background(if (textInput.isNotBlank()) TealPrimary else MaterialTheme.colorScheme.surfaceVariant)
                    ) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.Send,
                            contentDescription = stringResource(R.string.ai_tutor_send_button),
                            tint = if (textInput.isNotBlank()) Color.White else MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = stringResource(R.string.ai_disclaimer_note),
                    fontSize = 10.sp,
                    color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.75f),
                    modifier = Modifier.fillMaxWidth(),
                    textAlign = androidx.compose.ui.text.style.TextAlign.Center
                )
            }
        },
        modifier = modifier
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
            items(messages, key = { it.id }) { message ->
                ChatMessageBubble(
                    message = message,
                    onTryInPlayground = onTryInPlayground
                )
            }

            if (isThinking) {
                item {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        CircularProgressIndicator(
                            color = MaterialTheme.colorScheme.primary,
                            modifier = Modifier.size(20.dp),
                            strokeWidth = 2.dp
                        )
                        Spacer(modifier = Modifier.width(10.dp))
                        Text(
                            text = stringResource(R.string.ai_tutor_thinking),
                            fontSize = 12.sp,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun ChatMessageBubble(
    message: ChatMessage,
    onTryInPlayground: (String, String) -> Unit = { _, _ -> },
    modifier: Modifier = Modifier
) {
    val isUser = message.isUser

    Row(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = if (isUser) Arrangement.End else Arrangement.Start,
        verticalAlignment = Alignment.Top
    ) {
        if (!isUser) {
            Box(
                modifier = Modifier
                    .size(32.dp)
                    .clip(CircleShape)
                    .background(CyanAccent.copy(alpha = 0.2f)),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Default.Psychology,
                    contentDescription = "AI",
                    tint = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.size(18.dp)
                )
            }
            Spacer(modifier = Modifier.width(8.dp))
        }

        Surface(
            shape = RoundedCornerShape(
                topStart = 16.dp,
                topEnd = 16.dp,
                bottomStart = if (isUser) 16.dp else 4.dp,
                bottomEnd = if (isUser) 4.dp else 16.dp
            ),
            color = if (isUser) TealPrimary else MaterialTheme.colorScheme.surfaceVariant,
            modifier = Modifier
                .weight(1f, fill = false)
                .padding(
                    start = if (isUser) 48.dp else 0.dp,
                    end = if (isUser) 0.dp else 32.dp
                )
        ) {
            Column(modifier = Modifier.padding(14.dp)) {
                // If message has code block ``` ... ```
                if (message.text.contains("```")) {
                    val parts = message.text.split("```")
                    parts.forEachIndexed { index, part ->
                        if (index % 2 == 1) {
                            // Code section
                            val lines = part.trim().lines()
                            val lang = if (lines.isNotEmpty() && lines[0].length < 15 && !lines[0].contains(" ")) lines[0] else "code"
                            val codeBody = if (lang != "code") lines.drop(1).joinToString("\n") else part.trim()
                            CodeBlockView(
                                code = codeBody,
                                language = lang,
                                onTryInPlayground = { snippet -> onTryInPlayground(snippet, lang) },
                                modifier = Modifier.padding(vertical = 4.dp)
                            )
                        } else if (part.trim().isNotEmpty()) {
                            MarkdownText(
                                text = part.trim(),
                                style = MaterialTheme.typography.bodyMedium.copy(fontSize = 13.sp),
                                color = if (isUser) Color.White else MaterialTheme.colorScheme.onSurface,
                                lineHeight = 20.sp,
                                codeBackgroundColor = if (isUser) Color.White.copy(alpha = 0.25f) else MaterialTheme.colorScheme.secondaryContainer.copy(alpha = 0.6f),
                                codeTextColor = if (isUser) Color.White else MaterialTheme.colorScheme.primary
                            )
                        }
                    }
                } else {
                    MarkdownText(
                        text = message.text,
                        style = MaterialTheme.typography.bodyMedium.copy(fontSize = 13.sp),
                        color = if (isUser) Color.White else MaterialTheme.colorScheme.onSurface,
                        lineHeight = 20.sp,
                        codeBackgroundColor = if (isUser) Color.White.copy(alpha = 0.25f) else MaterialTheme.colorScheme.secondaryContainer.copy(alpha = 0.6f),
                        codeTextColor = if (isUser) Color.White else MaterialTheme.colorScheme.primary
                    )
                }
            }
        }
    }
}
