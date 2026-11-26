package com.example.ui.screens

import android.Manifest
import android.content.pm.PackageManager
import android.net.Uri
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.rememberTransformableState
import androidx.compose.foundation.gestures.transformable
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
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.ime
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.selection.SelectionContainer
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.Send
import androidx.compose.material.icons.filled.AddPhotoAlternate
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.ContentCopy
import androidx.compose.material.icons.filled.DeleteOutline
import androidx.compose.material.icons.filled.PhotoCamera
import androidx.compose.material.icons.filled.PhotoLibrary
import androidx.compose.material.icons.filled.Psychology
import androidx.compose.material.icons.filled.ZoomIn
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalClipboardManager
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import coil.compose.AsyncImage
import java.io.File
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
    connectionStatus: String = "Gemini Flash",
    onSendMessage: (String, String?) -> Unit,
    onBack: () -> Unit,
    onClearChat: () -> Unit = {},
    onTryInPlayground: (String, String) -> Unit = { _, _ -> },
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    var textInput by remember { mutableStateOf("") }
    var selectedImageUri by remember { mutableStateOf<String?>(null) }
    var previewImageUri by remember { mutableStateOf<String?>(null) }
    var showAttachSheet by remember { mutableStateOf(false) }
    var pendingCameraUri by remember { mutableStateOf<Uri?>(null) }
    val defaultImagePrompt = stringResource(R.string.ai_tutor_default_image_prompt)
    val cameraPermissionDeniedMsg = stringResource(R.string.ai_tutor_camera_permission_denied)

    val imagePickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri ->
        if (uri != null) {
            selectedImageUri = uri.toString()
        }
    }

    val takePictureLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.TakePicture()
    ) { success ->
        if (success && pendingCameraUri != null) {
            selectedImageUri = pendingCameraUri.toString()
        }
    }

    fun launchCameraCapture() {
        try {
            val camDir = File(context.cacheDir, "ai_camera").apply { mkdirs() }
            val photoFile = File(camDir, "capture_${System.currentTimeMillis()}.jpg")
            val uri = FileProvider.getUriForFile(context, "${context.packageName}.fileprovider", photoFile)
            pendingCameraUri = uri
            takePictureLauncher.launch(uri)
        } catch (_: Throwable) {
            Toast.makeText(context, cameraPermissionDeniedMsg, Toast.LENGTH_SHORT).show()
        }
    }

    val cameraPermissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission()
    ) { granted ->
        if (granted) {
            launchCameraCapture()
        } else {
            Toast.makeText(context, cameraPermissionDeniedMsg, Toast.LENGTH_SHORT).show()
        }
    }
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
    val isKeyboardOpen = imeBottomPx > 0
    LaunchedEffect(messages.size, isThinking, isKeyboardOpen) {
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
                    .imePadding()
                    .padding(horizontal = 16.dp, vertical = 8.dp)
            ) {
                // Quick suggested prompts (auto-hide when keyboard is open for more chat space)
                AnimatedVisibility(visible = !isKeyboardOpen) {
                    LazyRow(
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        contentPadding = PaddingValues(bottom = 8.dp)
                    ) {
                        items(suggestions, key = { it.id }) { prompt ->
                            Surface(
                                shape = RoundedCornerShape(16.dp),
                                color = MaterialTheme.colorScheme.surfaceVariant,
                                modifier = Modifier.clickable {
                                    onSendMessage(prompt.text, null)
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
                }

                // Attached Image Preview Bar
                AnimatedVisibility(visible = selectedImageUri != null) {
                    selectedImageUri?.let { uriStr ->
                        Surface(
                            shape = RoundedCornerShape(14.dp),
                            color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.7f),
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(bottom = 8.dp)
                        ) {
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(horizontal = 10.dp, vertical = 8.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                AsyncImage(
                                    model = uriStr,
                                    contentDescription = null,
                                    contentScale = ContentScale.Crop,
                                    modifier = Modifier
                                        .size(48.dp)
                                        .clip(RoundedCornerShape(8.dp))
                                        .clickable { previewImageUri = uriStr }
                                )
                                Spacer(modifier = Modifier.width(10.dp))
                                Text(
                                    text = stringResource(R.string.ai_tutor_image_attached_label),
                                    style = MaterialTheme.typography.bodySmall,
                                    fontWeight = FontWeight.Medium,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                                    modifier = Modifier.weight(1f),
                                    maxLines = 1,
                                    overflow = TextOverflow.Ellipsis
                                )
                                IconButton(
                                    onClick = { selectedImageUri = null },
                                    modifier = Modifier.size(32.dp)
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.Close,
                                        contentDescription = stringResource(R.string.ai_tutor_remove_image_cd),
                                        tint = MaterialTheme.colorScheme.onSurfaceVariant,
                                        modifier = Modifier.size(18.dp)
                                    )
                                }
                            }
                        }
                    }
                }

                // Chat Input Field
                val canSend = (textInput.isNotBlank() || selectedImageUri != null) && !isThinking
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    IconButton(
                        onClick = { showAttachSheet = true },
                        enabled = !isThinking,
                        modifier = Modifier
                            .size(44.dp)
                            .clip(CircleShape)
                            .background(MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.7f))
                    ) {
                        Icon(
                            imageVector = Icons.Default.AddPhotoAlternate,
                            contentDescription = stringResource(R.string.ai_tutor_attach_image_cd),
                            tint = if (selectedImageUri != null) TealPrimary else MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }

                    Spacer(modifier = Modifier.width(8.dp))

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
                            if (canSend) {
                                val msg = textInput.ifBlank { defaultImagePrompt }
                                val img = selectedImageUri
                                textInput = ""
                                selectedImageUri = null
                                onSendMessage(msg, img)
                            }
                        },
                        enabled = canSend,
                        modifier = Modifier
                            .size(46.dp)
                            .clip(CircleShape)
                            .background(if (canSend) TealPrimary else MaterialTheme.colorScheme.surfaceVariant)
                    ) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.Send,
                            contentDescription = stringResource(R.string.ai_tutor_send_button),
                            tint = if (canSend) Color.White else MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }
                AnimatedVisibility(visible = !isKeyboardOpen) {
                    Column {
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = stringResource(R.string.ai_disclaimer_note),
                            fontSize = 10.sp,
                            color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.75f),
                            modifier = Modifier.fillMaxWidth(),
                            textAlign = androidx.compose.ui.text.style.TextAlign.Center
                        )
                    }
                }
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
                    onTryInPlayground = onTryInPlayground,
                    onPreviewImage = { uri -> previewImageUri = uri }
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

    if (showAttachSheet) {
        ModalBottomSheet(
            onDismissRequest = { showAttachSheet = false },
            containerColor = MaterialTheme.colorScheme.surface
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp)
                    .padding(bottom = 28.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Text(
                    text = stringResource(R.string.ai_tutor_attach_sheet_title),
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSurface
                )

                Surface(
                    onClick = {
                        showAttachSheet = false
                        val hasCamPerm = ContextCompat.checkSelfPermission(
                            context,
                            Manifest.permission.CAMERA
                        ) == PackageManager.PERMISSION_GRANTED
                        if (hasCamPerm) {
                            launchCameraCapture()
                        } else {
                            cameraPermissionLauncher.launch(Manifest.permission.CAMERA)
                        }
                    },
                    shape = RoundedCornerShape(16.dp),
                    color = TealPrimary.copy(alpha = 0.09f),
                    border = BorderStroke(1.dp, TealPrimary.copy(alpha = 0.3f)),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Row(
                        modifier = Modifier.padding(14.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Box(
                            modifier = Modifier
                                .size(42.dp)
                                .clip(RoundedCornerShape(12.dp))
                                .background(TealPrimary.copy(alpha = 0.16f)),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.Default.PhotoCamera,
                                contentDescription = null,
                                tint = TealPrimary,
                                modifier = Modifier.size(22.dp)
                            )
                        }
                        Spacer(modifier = Modifier.width(14.dp))
                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                text = stringResource(R.string.ai_tutor_attach_camera),
                                style = MaterialTheme.typography.titleSmall,
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.onSurface
                            )
                            Text(
                                text = stringResource(R.string.ai_tutor_attach_camera_desc),
                                style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                    }
                }

                Surface(
                    onClick = {
                        showAttachSheet = false
                        imagePickerLauncher.launch("image/*")
                    },
                    shape = RoundedCornerShape(16.dp),
                    color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.45f),
                    border = BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.5f)),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Row(
                        modifier = Modifier.padding(14.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Box(
                            modifier = Modifier
                                .size(42.dp)
                                .clip(RoundedCornerShape(12.dp))
                                .background(CyanAccent.copy(alpha = 0.18f)),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.Default.PhotoLibrary,
                                contentDescription = null,
                                tint = TealPrimary,
                                modifier = Modifier.size(22.dp)
                            )
                        }
                        Spacer(modifier = Modifier.width(14.dp))
                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                text = stringResource(R.string.ai_tutor_attach_gallery),
                                style = MaterialTheme.typography.titleSmall,
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.onSurface
                            )
                            Text(
                                text = stringResource(R.string.ai_tutor_attach_gallery_desc),
                                style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                    }
                }
            }
        }
    }

    previewImageUri?.let { uri ->
        FullScreenImagePreviewDialog(
            imageUri = uri,
            onDismiss = { previewImageUri = null }
        )
    }
}

@Composable
private fun FullScreenImagePreviewDialog(
    imageUri: String,
    onDismiss: () -> Unit
) {
    var scale by remember { mutableStateOf(1f) }
    var offset by remember { mutableStateOf(Offset.Zero) }
    val transformState = rememberTransformableState { zoomChange, panChange, _ ->
        val nextScale = (scale * zoomChange).coerceIn(1f, 4f)
        scale = nextScale
        offset = if (nextScale > 1f) offset + panChange else Offset.Zero
    }

    Dialog(
        onDismissRequest = onDismiss,
        properties = DialogProperties(usePlatformDefaultWidth = false)
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.Black.copy(alpha = 0.94f))
                .clickable { onDismiss() },
            contentAlignment = Alignment.Center
        ) {
            AsyncImage(
                model = imageUri,
                contentDescription = null,
                contentScale = ContentScale.Fit,
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp)
                    .graphicsLayer(
                        scaleX = scale,
                        scaleY = scale,
                        translationX = offset.x,
                        translationY = offset.y
                    )
                    .transformable(state = transformState)
            )
            IconButton(
                onClick = onDismiss,
                modifier = Modifier
                    .align(Alignment.TopEnd)
                    .statusBarsPadding()
                    .padding(16.dp)
                    .size(40.dp)
                    .clip(CircleShape)
                    .background(Color.Black.copy(alpha = 0.6f))
            ) {
                Icon(
                    imageVector = Icons.Default.Close,
                    contentDescription = stringResource(R.string.common_close),
                    tint = Color.White
                )
            }
        }
    }
}

@Composable
fun ChatMessageBubble(
    message: ChatMessage,
    onTryInPlayground: (String, String) -> Unit = { _, _ -> },
    onPreviewImage: (String) -> Unit = {},
    modifier: Modifier = Modifier
) {
    val isUser = message.isUser
    val clipboard = LocalClipboardManager.current

    Row(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = if (isUser) Arrangement.End else Arrangement.Start,
        verticalAlignment = Alignment.Top
    ) {
        if (!isUser) {
            Box(
                modifier = Modifier
                    .size(30.dp)
                    .clip(CircleShape)
                    .background(CyanAccent.copy(alpha = 0.2f)),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Default.Psychology,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.size(17.dp)
                )
            }
            Spacer(modifier = Modifier.width(8.dp))
        }

        Surface(
            shape = RoundedCornerShape(
                topStart = if (isUser) 18.dp else 6.dp,
                topEnd = 18.dp,
                bottomStart = 18.dp,
                bottomEnd = if (isUser) 6.dp else 18.dp
            ),
            color = if (isUser) TealPrimary else MaterialTheme.colorScheme.surface,
            border = if (isUser) null else BorderStroke(
                1.dp,
                MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.55f)
            ),
            tonalElevation = if (isUser) 0.dp else 1.dp,
            modifier = Modifier
                .weight(1f, fill = false)
                .padding(
                    start = if (isUser) 44.dp else 0.dp,
                    end = if (isUser) 0.dp else 8.dp
                )
        ) {
            Column(modifier = Modifier.padding(horizontal = 14.dp, vertical = 12.dp)) {
                if (!isUser) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(bottom = 6.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = stringResource(R.string.ai_tutor_title),
                            style = MaterialTheme.typography.labelSmall,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.primary
                        )
                        IconButton(
                            onClick = { clipboard.setText(AnnotatedString(message.text)) },
                            modifier = Modifier.size(24.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.ContentCopy,
                                contentDescription = null,
                                tint = MaterialTheme.colorScheme.onSurfaceVariant,
                                modifier = Modifier.size(14.dp)
                            )
                        }
                    }
                }

                if (!message.imageUri.isNullOrBlank()) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .heightIn(max = 210.dp)
                            .clip(RoundedCornerShape(12.dp))
                            .clickable { onPreviewImage(message.imageUri) }
                    ) {
                        AsyncImage(
                            model = message.imageUri,
                            contentDescription = null,
                            contentScale = ContentScale.Crop,
                            modifier = Modifier.fillMaxWidth()
                        )
                        Box(
                            modifier = Modifier
                                .align(Alignment.BottomEnd)
                                .padding(8.dp)
                                .clip(CircleShape)
                                .background(Color.Black.copy(alpha = 0.55f))
                                .padding(5.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.ZoomIn,
                                contentDescription = null,
                                tint = Color.White,
                                modifier = Modifier.size(16.dp)
                            )
                        }
                    }
                    Spacer(modifier = Modifier.height(8.dp))
                }

                SelectionContainer {
                    Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                        if (message.text.contains("```")) {
                            val parts = message.text.split("```")
                            parts.forEachIndexed { index, part ->
                                if (index % 2 == 1) {
                                    val lines = part.trim().lines()
                                    val lang = if (lines.isNotEmpty() && lines[0].length < 15 && !lines[0].contains(" ")) lines[0] else "code"
                                    val codeBody = if (lang != "code") lines.drop(1).joinToString("\n") else part.trim()
                                    CodeBlockView(
                                        code = codeBody,
                                        language = lang,
                                        onTryInPlayground = { snippet -> onTryInPlayground(snippet, lang) },
                                        modifier = Modifier.padding(vertical = 2.dp)
                                    )
                                } else if (part.trim().isNotEmpty()) {
                                    AiFormattedMarkdown(
                                        text = part.trim(),
                                        isUser = isUser
                                    )
                                }
                            }
                        } else {
                            AiFormattedMarkdown(
                                text = message.text,
                                isUser = isUser
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun AiFormattedMarkdown(
    text: String,
    isUser: Boolean
) {
    val textColor = if (isUser) Color.White else MaterialTheme.colorScheme.onSurface
    val codeBg = if (isUser) Color.White.copy(alpha = 0.25f) else MaterialTheme.colorScheme.secondaryContainer.copy(alpha = 0.65f)
    val codeText = if (isUser) Color.White else MaterialTheme.colorScheme.primary
    val bulletTint = if (isUser) Color.White else TealPrimary
    val numberedRegex = remember { Regex("^(\\d+\\.)\\s+(.*)$") }

    Column(verticalArrangement = Arrangement.spacedBy(5.dp)) {
        text.lines().forEach { rawLine ->
            val trimmed = rawLine.trim()
            when {
                trimmed.isEmpty() -> Spacer(modifier = Modifier.height(2.dp))
                trimmed == "---" || trimmed == "***" -> {
                    HorizontalDivider(
                        color = if (isUser) Color.White.copy(alpha = 0.3f)
                        else MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.5f),
                        modifier = Modifier.padding(vertical = 4.dp)
                    )
                }
                trimmed.startsWith("### ") || trimmed.startsWith("## ") || trimmed.startsWith("# ") -> {
                    val headingText = trimmed.trimStart('#').trim()
                    MarkdownText(
                        text = headingText,
                        style = MaterialTheme.typography.titleSmall.copy(
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Bold
                        ),
                        color = if (isUser) Color.White else MaterialTheme.colorScheme.primary,
                        lineHeight = 20.sp,
                        codeBackgroundColor = codeBg,
                        codeTextColor = codeText,
                        modifier = Modifier.padding(top = 4.dp)
                    )
                }
                trimmed.startsWith("- ") || trimmed.startsWith("* ") || trimmed.startsWith("• ") -> {
                    val itemBody = trimmed.drop(2).trim()
                    Row(
                        modifier = Modifier.fillMaxWidth().padding(start = 4.dp),
                        verticalAlignment = Alignment.Top
                    ) {
                        Text(
                            text = "•",
                            color = bulletTint,
                            fontWeight = FontWeight.Bold,
                            fontSize = 14.sp,
                            modifier = Modifier.padding(end = 8.dp)
                        )
                        MarkdownText(
                            text = itemBody,
                            style = MaterialTheme.typography.bodyMedium.copy(fontSize = 13.5.sp),
                            color = textColor,
                            lineHeight = 20.5.sp,
                            codeBackgroundColor = codeBg,
                            codeTextColor = codeText,
                            modifier = Modifier.weight(1f)
                        )
                    }
                }
                numberedRegex.matches(trimmed) -> {
                    val match = numberedRegex.find(trimmed)
                    val numLabel = match?.groupValues?.getOrNull(1) ?: ""
                    val itemBody = match?.groupValues?.getOrNull(2) ?: trimmed
                    Row(
                        modifier = Modifier.fillMaxWidth().padding(start = 2.dp),
                        verticalAlignment = Alignment.Top
                    ) {
                        Text(
                            text = numLabel,
                            color = bulletTint,
                            fontWeight = FontWeight.Bold,
                            fontSize = 13.sp,
                            modifier = Modifier.padding(end = 6.dp)
                        )
                        MarkdownText(
                            text = itemBody,
                            style = MaterialTheme.typography.bodyMedium.copy(fontSize = 13.5.sp),
                            color = textColor,
                            lineHeight = 20.5.sp,
                            codeBackgroundColor = codeBg,
                            codeTextColor = codeText,
                            modifier = Modifier.weight(1f)
                        )
                    }
                }
                trimmed.startsWith("> ") -> {
                    val quoteBody = trimmed.drop(2).trim()
                    Surface(
                        shape = RoundedCornerShape(8.dp),
                        color = if (isUser) Color.White.copy(alpha = 0.14f)
                        else MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.28f),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Row(modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp)) {
                            Box(
                                modifier = Modifier
                                    .width(3.dp)
                                    .height(18.dp)
                                    .clip(RoundedCornerShape(2.dp))
                                    .background(bulletTint)
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            MarkdownText(
                                text = quoteBody,
                                style = MaterialTheme.typography.bodySmall.copy(fontSize = 13.sp),
                                color = textColor,
                                lineHeight = 19.sp,
                                codeBackgroundColor = codeBg,
                                codeTextColor = codeText,
                                modifier = Modifier.weight(1f)
                            )
                        }
                    }
                }
                else -> {
                    MarkdownText(
                        text = trimmed,
                        style = MaterialTheme.typography.bodyMedium.copy(fontSize = 13.5.sp),
                        color = textColor,
                        lineHeight = 21.sp,
                        codeBackgroundColor = codeBg,
                        codeTextColor = codeText
                    )
                }
            }
        }
    }
}
