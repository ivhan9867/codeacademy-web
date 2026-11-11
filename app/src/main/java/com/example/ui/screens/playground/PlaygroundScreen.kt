package com.example.ui.screens.playground

import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
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
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.ui.theme.*
import com.example.ui.viewmodel.PlaygroundSessionState
import com.example.ui.viewmodel.PlaygroundViewModel
import kotlinx.coroutines.delay

@Composable
fun PlaygroundScreen(
    courseId: String,
    moduleId: String,
    viewModel: PlaygroundViewModel? = null,
    onNavigateBack: () -> Unit,
    modifier: Modifier = Modifier
) {
    LaunchedEffect(courseId, moduleId) {
        viewModel?.loadSession(courseId, moduleId)
    }

    val sessionState by viewModel?.sessionState?.collectAsStateWithLifecycle()
        ?: remember { mutableStateOf(PlaygroundSessionState()) }

    var isDraftLoaded by remember { mutableStateOf(false) }
    var selectedTab by remember { mutableIntStateOf(0) } // 0: HTML, 1: CSS, 2: JS, 3: Preview
    var showTaskBanner by remember { mutableStateOf(true) }

    var htmlCode by remember { mutableStateOf("") }
    var cssCode by remember { mutableStateOf("") }
    var jsCode by remember { mutableStateOf("") }

    LaunchedEffect(sessionState.hasLoaded) {
        if (sessionState.hasLoaded && !isDraftLoaded) {
            htmlCode = sessionState.initialHtml
            cssCode = sessionState.initialCss
            jsCode = sessionState.initialJs
            isDraftLoaded = true
        }
    }

    // Debounced autosave
    LaunchedEffect(htmlCode, cssCode, jsCode) {
        if (isDraftLoaded && (htmlCode.isNotBlank() || cssCode.isNotBlank() || jsCode.isNotBlank())) {
            delay(1500)
            viewModel?.saveDraft(courseId, moduleId, htmlCode, cssCode, jsCode)
        }
    }

    var compiledHtml by remember { mutableStateOf("") }

    // Debounced compiler for live preview
    LaunchedEffect(htmlCode, cssCode, jsCode) {
        delay(400)
        compiledHtml = """
            <!DOCTYPE html>
            <html>
            <head>
              <meta name="viewport" content="width=device-width, initial-scale=1.0">
              <style>$cssCode</style>
            </head>
            <body>
              $htmlCode
              <script>$jsCode</script>
            </body>
            </html>
        """.trimIndent()
    }

    Scaffold(
        modifier = modifier.fillMaxSize(),
        containerColor = BackgroundDark,
        topBar = {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(BackgroundDark)
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 10.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        IconButton(
                            onClick = onNavigateBack,
                            modifier = Modifier.clip(CircleShape).background(SurfaceDark)
                        ) {
                            Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Kembali", tint = TextPrimaryDark)
                        }

                        Column {
                            Text(
                                text = sessionState.module?.title ?: "Live Playground ⚡",
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.Bold,
                                color = TextPrimaryDark,
                                maxLines = 1
                            )
                            Text(
                                text = "${sessionState.course?.title ?: "Materi Kursus"} • Autosave Aktif",
                                style = MaterialTheme.typography.labelSmall,
                                color = SecondaryTeal,
                                maxLines = 1
                            )
                        }
                    }

                    // Reset code back to initial module lesson code
                    IconButton(
                        onClick = {
                            htmlCode = sessionState.initialHtml
                            cssCode = sessionState.initialCss
                            jsCode = sessionState.initialJs
                            viewModel?.saveDraft(courseId, moduleId, htmlCode, cssCode, jsCode)
                        },
                        modifier = Modifier.clip(CircleShape).background(SurfaceDark)
                    ) {
                        Icon(Icons.Default.RestartAlt, contentDescription = "Reset Kode Awal", tint = AccentWarning)
                    }
                }

                // Contextual Lesson Task Banner
                val task = sessionState.module?.playgroundTask
                if (!task.isNullOrBlank() && showTaskBanner) {
                    Surface(
                        color = SurfaceDark,
                        shape = RoundedCornerShape(12.dp),
                        border = CardDefaults.outlinedCardBorder().copy(
                            brush = Brush.horizontalGradient(listOf(AccentWarning, PrimaryPurple))
                        ),
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp, vertical = 4.dp)
                    ) {
                        Row(
                            modifier = Modifier.padding(12.dp),
                            verticalAlignment = Alignment.Top,
                            horizontalArrangement = Arrangement.spacedBy(10.dp)
                        ) {
                            Text("⚡", fontSize = 18.sp)
                            Column(modifier = Modifier.weight(1f)) {
                                Text(
                                    text = "Tantangan Praktik Modul Ini:",
                                    style = MaterialTheme.typography.labelMedium,
                                    fontWeight = FontWeight.Bold,
                                    color = AccentWarning
                                )
                                Spacer(modifier = Modifier.height(2.dp))
                                Text(
                                    text = task,
                                    style = MaterialTheme.typography.bodySmall,
                                    color = TextPrimaryDark,
                                    lineHeight = 16.sp
                                )
                            }
                            IconButton(
                                onClick = { showTaskBanner = false },
                                modifier = Modifier.size(24.dp)
                            ) {
                                Icon(Icons.Default.Close, contentDescription = "Tutup Info", tint = TextSecondaryDark, modifier = Modifier.size(16.dp))
                            }
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
        ) {
            // Tabs Row
            TabRow(
                selectedTabIndex = selectedTab,
                containerColor = SurfaceDark,
                contentColor = SecondaryTeal,
                indicator = {}
            ) {
                listOf("HTML", "CSS", "JS", "📱 Live Preview").forEachIndexed { index, title ->
                    val isSelected = selectedTab == index
                    Tab(
                        selected = isSelected,
                        onClick = { selectedTab = index },
                        text = {
                            Text(
                                text = title,
                                fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                                color = if (isSelected) SecondaryTeal else TextSecondaryDark
                            )
                        }
                    )
                }
            }

            // Editor / Preview Body
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
                    .background(CodeBg)
            ) {
                when (selectedTab) {
                    0 -> { // HTML Editor
                        OutlinedTextField(
                            value = htmlCode,
                            onValueChange = { htmlCode = it },
                            modifier = Modifier.fillMaxSize().padding(12.dp),
                            textStyle = TextStyle(
                                fontFamily = FontFamily.Monospace,
                                fontSize = 13.sp,
                                color = SyntaxString,
                                lineHeight = 19.sp
                            ),
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedBorderColor = Color.Transparent,
                                unfocusedBorderColor = Color.Transparent,
                                focusedContainerColor = CodeBg,
                                unfocusedContainerColor = CodeBg
                            )
                        )
                    }
                    1 -> { // CSS Editor
                        OutlinedTextField(
                            value = cssCode,
                            onValueChange = { cssCode = it },
                            modifier = Modifier.fillMaxSize().padding(12.dp),
                            textStyle = TextStyle(
                                fontFamily = FontFamily.Monospace,
                                fontSize = 13.sp,
                                color = SyntaxFunction,
                                lineHeight = 19.sp
                            ),
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedBorderColor = Color.Transparent,
                                unfocusedBorderColor = Color.Transparent,
                                focusedContainerColor = CodeBg,
                                unfocusedContainerColor = CodeBg
                            )
                        )
                    }
                    2 -> { // JS Editor
                        OutlinedTextField(
                            value = jsCode,
                            onValueChange = { jsCode = it },
                            modifier = Modifier.fillMaxSize().padding(12.dp),
                            textStyle = TextStyle(
                                fontFamily = FontFamily.Monospace,
                                fontSize = 13.sp,
                                color = SyntaxNumber,
                                lineHeight = 19.sp
                            ),
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedBorderColor = Color.Transparent,
                                unfocusedBorderColor = Color.Transparent,
                                focusedContainerColor = CodeBg,
                                unfocusedContainerColor = CodeBg
                            )
                        )
                    }
                    3 -> { // Live WebView Preview
                        AndroidView(
                            factory = { ctx ->
                                WebView(ctx).apply {
                                    settings.javaScriptEnabled = true
                                    settings.domStorageEnabled = true
                                    webViewClient = WebViewClient()
                                    setBackgroundColor(0xFF0F1115.toInt())
                                    loadDataWithBaseURL(null, compiledHtml, "text/html", "UTF-8", null)
                                }
                            },
                            update = { webView ->
                                webView.loadDataWithBaseURL(null, compiledHtml, "text/html", "UTF-8", null)
                            },
                            modifier = Modifier.fillMaxSize()
                        )
                    }
                }
            }

            // Bottom Quick Action Bar
            Surface(
                color = SurfaceDark,
                border = CardDefaults.outlinedCardBorder().copy(brush = Brush.verticalGradient(listOf(BorderDark, SurfaceDark))),
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 10.dp)
                        .navigationBarsPadding(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        Button(
                            onClick = { selectedTab = 3 },
                            shape = RoundedCornerShape(10.dp),
                            colors = ButtonDefaults.buttonColors(containerColor = SecondaryTealDark)
                        ) {
                            Icon(Icons.Filled.PlayArrow, contentDescription = null, tint = Color.White, modifier = Modifier.size(18.dp))
                            Spacer(modifier = Modifier.width(4.dp))
                            Text("Preview", fontWeight = FontWeight.Bold)
                        }

                        if (viewModel != null) {
                            Button(
                                onClick = {
                                    viewModel.saveDraft(courseId, moduleId, htmlCode, cssCode, jsCode)
                                },
                                shape = RoundedCornerShape(10.dp),
                                colors = ButtonDefaults.buttonColors(containerColor = PrimaryPurple)
                            ) {
                                Icon(Icons.Filled.Save, contentDescription = null, tint = Color.White, modifier = Modifier.size(18.dp))
                                Spacer(modifier = Modifier.width(4.dp))
                                Text("Simpan Draft", fontWeight = FontWeight.Bold)
                            }
                        }
                    }

                    OutlinedButton(
                        onClick = {
                            htmlCode = sessionState.initialHtml
                            cssCode = sessionState.initialCss
                            jsCode = sessionState.initialJs
                            viewModel?.saveDraft(courseId, moduleId, htmlCode, cssCode, jsCode)
                        },
                        shape = RoundedCornerShape(10.dp)
                    ) {
                        Text("Reset Awal", color = TextSecondaryDark)
                    }
                }
            }
        }
    }
}
