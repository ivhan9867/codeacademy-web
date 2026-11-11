package com.example.ui.screens

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.widget.Toast
import androidx.compose.animation.AnimatedVisibility
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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.AssignmentTurnedIn
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.ContentCopy
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Psychology
import androidx.compose.material.icons.filled.RadioButtonUnchecked
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Terminal
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.TabRowDefaults
import androidx.compose.material3.TabRowDefaults.tabIndicatorOffset
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.icons.filled.Assignment
import androidx.compose.material.icons.filled.CloudOff
import androidx.compose.material.icons.filled.DeleteOutline
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.mutableLongStateOf
import com.example.ui.components.PythonSandboxWebView
import com.example.ui.components.codeHighlightTransformation
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.ui.MainViewModel
import com.example.ui.components.CoachMarkBubble
import com.example.ui.components.CodeSandboxWebView
import com.example.ui.components.AppDialog
import com.example.ui.components.AppConfirmationDialog
import com.example.ui.theme.AmberGold
import com.example.ui.theme.CodeDarkBg
import com.example.ui.theme.CodeDarkBorder
import com.example.ui.theme.CodeDarkSurface
import com.example.ui.theme.CyanAccent
import com.example.ui.theme.CyanAccentText
import com.example.ui.theme.FlameOrange
import com.example.ui.theme.SuccessGreen
import com.example.ui.theme.TealLight
import com.example.ui.theme.TealPrimary

data class PythonLogEntry(
    val text: String,
    val isStderr: Boolean = false,
    val isPrompt: Boolean = false,
    val isSystem: Boolean = false
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PlaygroundScreen(
    courseId: String,
    moduleId: String?,
    initialCode: String? = null,
    initialLanguage: String? = null,
    onBack: () -> Unit,
    onOpenAiTutor: (String) -> Unit,
    modifier: Modifier = Modifier,
    viewModel: MainViewModel = viewModel(
        viewModelStoreOwner = (LocalContext.current as? androidx.activity.ComponentActivity)
            ?: checkNotNull(androidx.lifecycle.viewmodel.compose.LocalViewModelStoreOwner.current)
    )
) {
    val context = LocalContext.current
    val coroutineScope = rememberCoroutineScope()
    val userProfile by viewModel.userProfile.collectAsStateWithLifecycle()

    val currentModule = remember(courseId, moduleId) {
        if (!moduleId.isNullOrBlank()) {
            viewModel.getCourseById(courseId)?.modules?.find { it.id == moduleId }
        } else null
    }
    val isContinuation = currentModule?.isProjectContinuation == true
    val verificationRules = remember(currentModule) { currentModule?.verificationRules.orEmpty() }

    // Starter HTML/CSS/JS template
    val defaultHtml = remember {
        """<div class="card">
  <h2>Halo CodeAcademy! 👋</h2>
  <p>Coba ubah kode HTML, CSS, atau JS di sini lalu tekan tombol <b>Run</b>.</p>
  <button id="btn" onclick="handleClick()">Klik Saya!</button>
  <div id="result"></div>
</div>"""
    }

    val defaultCss = remember {
        """body {
  font-family: 'Segoe UI', sans-serif;
  background: #0f172a;
  color: #f8fafc;
  display: flex;
  justify-content: center;
  align-items: center;
  min-height: 90vh;
  margin: 0;
  padding: 16px;
}
.card {
  background: #1e293b;
  padding: 24px;
  border-radius: 16px;
  box-shadow: 0 10px 25px rgba(0,0,0,0.5);
  border: 1px solid #334155;
  text-align: center;
  max-width: 320px;
}
h2 {
  color: #14b8a6;
  margin-top: 0;
}
p {
  color: #94a3b8;
  font-size: 14px;
}
button {
  background: #0d9488;
  color: white;
  border: none;
  padding: 10px 20px;
  border-radius: 8px;
  font-weight: bold;
  cursor: pointer;
  transition: 0.2s;
}
button:active {
  transform: scale(0.95);
}
#result {
  margin-top: 14px;
  font-weight: bold;
  color: #38bdf8;
}"""
    }

    val defaultJs = remember {
        """let clickCount = 0;

function handleClick() {
  clickCount++;
  const result = document.getElementById('result');
  result.innerText = 'Tombol diklik ' + clickCount + ' kali! 🚀';
  console.log('User clicked button. Total clicks: ' + clickCount);
}"""
    }

    val isPythonMode = remember(initialLanguage, currentModule, courseId) {
        val lang = (initialLanguage ?: currentModule?.codeLanguage).orEmpty().lowercase().trim()
        lang == "python" || lang == "py" || courseId.startsWith("py-") || courseId.contains("python")
    }

    val defaultPythonCode = remember {
        """# Tulis kode Python kamu di sini
nama = "Siswa CodeAcademy"
print(f"Halo, {nama}! Selamat datang di Playground Python 🐍")
print("Coba ubah kode ini lalu tekan tombol Jalankan!")
""".trimIndent()
    }

    val starterPythonCode = remember(initialCode, currentModule) {
        initialCode?.takeIf { it.isNotBlank() }
            ?: currentModule?.codeSnippet?.takeIf { it.isNotBlank() }
            ?: defaultPythonCode
    }

    var pythonCode by remember(starterPythonCode) {
        mutableStateOf(starterPythonCode)
    }

    val pythonLogs = remember { mutableStateListOf<PythonLogEntry>() }
    var isPythonRunning by remember { mutableStateOf(false) }
    var pyodideStatus by remember { mutableStateOf("LOADING") }
    var runPythonTrigger by remember { mutableIntStateOf(0) }
    var reloadPyodideTrigger by remember { mutableIntStateOf(0) }
    var lastExecutionTimeMs by remember { mutableLongStateOf(0L) }

    // Bahasa contoh ditentukan dari initialLanguage (dikirim dari modul) kalau ada.
    // Kalau tidak ada (mis. tombol "Playground" bebas tanpa kode), baru pakai tebakan dari isi teks.
    val resolvedLanguage = remember(initialCode, initialLanguage) {
        when {
            !initialLanguage.isNullOrBlank() -> initialLanguage.lowercase()
            initialCode.isNullOrBlank() -> null
            else -> {
                val trimmed = initialCode.trim()
                when {
                    (trimmed.contains("{") && trimmed.contains("}")) &&
                        (trimmed.contains("color:") || trimmed.contains("background") || trimmed.contains("font-") || trimmed.contains("padding:") || trimmed.contains("margin:") || trimmed.contains("display:")) -> "css"
                    trimmed.contains("function ") || trimmed.contains("console.log") || trimmed.contains("const ") || trimmed.contains("let ") || trimmed.contains("var ") || trimmed.contains("=>") -> "js"
                    else -> "html"
                }
            }
        }
    }

    val initialTab = remember(resolvedLanguage) {
        when (resolvedLanguage) {
            "css" -> 1
            "js", "javascript" -> 2
            else -> 0
        }
    }

    // true kalau Playground dibuka membawa SATU contoh spesifik dari sebuah modul
    // (bukan dibuka bebas lewat menu Playground tanpa kode).
    val isSingleLanguageExample = !initialCode.isNullOrBlank()

    var htmlCode by remember(initialCode, currentModule) {
        mutableStateOf(
            when {
                !currentModule?.starterHtml.isNullOrBlank() -> currentModule!!.starterHtml
                initialTab == 0 && !initialCode.isNullOrBlank() -> initialCode
                !isSingleLanguageExample -> defaultHtml
                else -> ""
            }
        )
    }
    var cssCode by remember(initialCode, currentModule) {
        mutableStateOf(
            when {
                initialTab == 1 && !initialCode.isNullOrBlank() -> initialCode
                !currentModule?.starterCss.isNullOrBlank() -> currentModule!!.starterCss
                !isSingleLanguageExample -> defaultCss
                else -> ""
            }
        )
    }
    var jsCode by remember(initialCode, currentModule) {
        mutableStateOf(
            when {
                initialTab == 2 && !initialCode.isNullOrBlank() -> initialCode
                !currentModule?.starterJs.isNullOrBlank() -> currentModule!!.starterJs
                !isSingleLanguageExample -> defaultJs
                else -> ""
            }
        )
    }

    // Live executed state
    var executedHtml by remember(initialCode, currentModule) { mutableStateOf(htmlCode) }
    var executedCss by remember(initialCode, currentModule) { mutableStateOf(cssCode) }
    var executedJs by remember(initialCode, currentModule) { mutableStateOf(jsCode) }

    // Load saved project draft if isProjectContinuation is active
    LaunchedEffect(courseId, moduleId, isContinuation) {
        if (isContinuation) {
            val draft = viewModel.getPlaygroundDraft(courseId)
            if (draft != null && (draft.htmlCode.isNotBlank() || draft.cssCode.isNotBlank() || draft.jsCode.isNotBlank())) {
                htmlCode = draft.htmlCode
                cssCode = draft.cssCode
                jsCode = draft.jsCode
                executedHtml = draft.htmlCode
                executedCss = draft.cssCode
                executedJs = draft.jsCode
            }
        }
    }

    // Auto-save draft with debounce (1.5s)
    LaunchedEffect(htmlCode, cssCode, jsCode) {
        delay(1500L)
        if (courseId.isNotBlank()) {
            viewModel.savePlaygroundDraft(courseId, htmlCode, cssCode, jsCode)
        }
    }

    // Save draft when disposing / leaving
    DisposableEffect(courseId) {
        onDispose {
            if (courseId.isNotBlank()) {
                viewModel.savePlaygroundDraft(courseId, htmlCode, cssCode, jsCode)
            }
        }
    }

    // Verification rules checking
    val rulesPassedStatus = remember(htmlCode, cssCode, jsCode, verificationRules) {
        verificationRules.map { rule ->
            val passed = htmlCode.contains(rule, ignoreCase = true) ||
                cssCode.contains(rule, ignoreCase = true) ||
                jsCode.contains(rule, ignoreCase = true)
            rule to passed
        }
    }
    val allRulesPassed = verificationRules.isNotEmpty() && rulesPassedStatus.all { it.second }
    val rulesPassedCount = rulesPassedStatus.count { it.second }

    LaunchedEffect(allRulesPassed, moduleId) {
        if (allRulesPassed && !moduleId.isNullOrBlank()) {
            viewModel.markModuleVerificationPassed(moduleId)
        }
    }

    // Tab: 0 = HTML, 1 = CSS, 2 = JS, 3 = Preview
    var selectedTab by remember(initialCode) { mutableIntStateOf(initialTab) }
    val consoleLogs = remember { mutableStateListOf<String>() }
    var showConsole by remember { mutableStateOf(false) }

    var showResetDialog by remember { mutableStateOf(false) }

    if (showResetDialog) {
        AppDialog(
            onDismissRequest = { showResetDialog = false },
            icon = {
                Box(
                    modifier = Modifier
                        .size(48.dp)
                        .clip(CircleShape)
                        .background(FlameOrange.copy(alpha = 0.15f)),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.Refresh,
                        contentDescription = null,
                        tint = FlameOrange,
                        modifier = Modifier.size(26.dp)
                    )
                }
            },
            title = {
                Text(
                    text = "Reset ke Contoh Modul?",
                    fontWeight = FontWeight.Bold,
                    style = MaterialTheme.typography.titleMedium
                )
            },
            text = {
                Text(
                    text = "Draft kode project kamu saat ini akan dikembalikan ke kode awal bawaan modul ini. Yakin ingin melanjutkan?",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    textAlign = TextAlign.Center
                )
            },
            confirmButton = {
                Button(
                    onClick = {
                        showResetDialog = false
                        coroutineScope.launch {
                            viewModel.clearPlaygroundDraft(courseId)
                            val starterH = currentModule?.starterHtml?.ifBlank { null }
                                ?: if (initialTab == 0) initialCode else null
                            val starterC = currentModule?.starterCss?.ifBlank { null }
                                ?: if (initialTab == 1) initialCode else null
                            val starterJ = currentModule?.starterJs?.ifBlank { null }
                                ?: if (initialTab == 2) initialCode else null

                            htmlCode = starterH ?: (if (!isSingleLanguageExample) defaultHtml else "")
                            cssCode = starterC ?: (if (!isSingleLanguageExample) defaultCss else "")
                            jsCode = starterJ ?: (if (!isSingleLanguageExample) defaultJs else "")
                            executedHtml = htmlCode
                            executedCss = cssCode
                            executedJs = jsCode
                            consoleLogs.clear()
                            Toast.makeText(context, "Kode project direset ke contoh modul", Toast.LENGTH_SHORT).show()
                        }
                    },
                    shape = RoundedCornerShape(12.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = FlameOrange)
                ) {
                    Text("Reset Sekarang", color = Color.White, fontWeight = FontWeight.Bold)
                }
            },
            dismissButton = {
                OutlinedButton(
                    onClick = { showResetDialog = false },
                    shape = RoundedCornerShape(12.dp),
                    border = BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant)
                ) {
                    Text("Batal")
                }
            }
        )
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = if (isPythonMode) "Playground Python" else "Code Playground",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold
                    )
                },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Kembali"
                        )
                    }
                },
                actions = {
                    // Tanya AI button
                    IconButton(onClick = {
                        try {
                            val currentCodeContext = if (isPythonMode) {
                                "Python:\n$pythonCode".take(1500)
                            } else {
                                when (selectedTab) {
                                    0 -> "HTML:\n$htmlCode"
                                    1 -> "CSS:\n$cssCode"
                                    2 -> "JS:\n$jsCode"
                                    else -> "HTML:\n$htmlCode\nCSS:\n$cssCode\nJS:\n$jsCode"
                                }.take(1500)
                            }
                            onOpenAiTutor(currentCodeContext)
                        } catch (e: Exception) {
                            Toast.makeText(context, "Gagal membuka AI Tutor: ${e.localizedMessage}", Toast.LENGTH_SHORT).show()
                        }
                    }) {
                        Icon(
                            imageVector = Icons.Default.Psychology,
                            contentDescription = "Tanya AI",
                            tint = CyanAccentText
                        )
                    }

                    // Reset code button
                    IconButton(onClick = {
                        if (isPythonMode) {
                            pythonCode = starterPythonCode
                            pythonLogs.clear()
                            Toast.makeText(context, "Kode Python direset ke awal", Toast.LENGTH_SHORT).show()
                        } else if (isContinuation) {
                            showResetDialog = true
                        } else {
                            try {
                                if (isSingleLanguageExample) {
                                    htmlCode = if (initialTab == 0) (initialCode ?: "") else ""
                                    cssCode = if (initialTab == 1) (initialCode ?: "") else ""
                                    jsCode = if (initialTab == 2) (initialCode ?: "") else ""
                                } else {
                                    htmlCode = defaultHtml
                                    cssCode = defaultCss
                                    jsCode = defaultJs
                                }
                                executedHtml = htmlCode
                                executedCss = cssCode
                                executedJs = jsCode
                                consoleLogs.clear()
                                Toast.makeText(context, "Kode direset ke template", Toast.LENGTH_SHORT).show()
                            } catch (e: Exception) {
                                Toast.makeText(context, "Gagal mereset kode: ${e.localizedMessage}", Toast.LENGTH_SHORT).show()
                            }
                        }
                    }) {
                        Icon(
                            imageVector = Icons.Default.Refresh,
                            contentDescription = if (isContinuation) "Reset ke contoh modul ini" else "Reset",
                            tint = Color.White
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = CodeDarkSurface,
                    titleContentColor = Color.White,
                    navigationIconContentColor = Color.White,
                    actionIconContentColor = Color.White
                )
            )
        },
        modifier = modifier
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .background(CodeDarkBg)
        ) {
            if (userProfile != null && !viewModel.hasSeenCoachMark("playground_intro")) {
                CoachMarkBubble(
                    message = stringResource(R.string.playground_coach_mark_msg),
                    onDismiss = { viewModel.markCoachMarkSeen("playground_intro") },
                    modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
                )
            }

            if (isPythonMode) {
                // Python runner sandbox WebView (headless)
                PythonSandboxWebView(
                    runTrigger = runPythonTrigger,
                    codeToRun = pythonCode,
                    reloadTrigger = reloadPyodideTrigger,
                    onStdout = { msg ->
                        pythonLogs.add(PythonLogEntry(text = msg))
                    },
                    onStderr = { msg ->
                        pythonLogs.add(PythonLogEntry(text = msg, isStderr = true))
                    },
                    onStatus = { status ->
                        pyodideStatus = status
                    },
                    onFinished = { success, timeMs ->
                        isPythonRunning = false
                        lastExecutionTimeMs = timeMs
                        if (success) {
                            pythonLogs.add(PythonLogEntry(text = "\n[Program selesai dalam ${timeMs}ms]", isSystem = true))
                        } else {
                            pythonLogs.add(PythonLogEntry(text = "\n[Program berhenti karena error]", isSystem = true))
                        }
                    }
                )

                // Sub-header Action Bar
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(Color(0xFF161B22))
                        .padding(horizontal = 12.dp, vertical = 6.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(
                            text = "main.py",
                            color = Color(0xFFA5D6FF),
                            fontFamily = FontFamily.Monospace,
                            fontWeight = FontWeight.Bold,
                            fontSize = 12.sp
                        )

                        Spacer(modifier = Modifier.width(8.dp))

                        Surface(
                            color = when {
                                pyodideStatus == "READY" -> SuccessGreen.copy(alpha = 0.18f)
                                pyodideStatus.startsWith("LOAD_ERROR") -> Color(0xFFFF7B72).copy(alpha = 0.18f)
                                else -> AmberGold.copy(alpha = 0.18f)
                            },
                            shape = RoundedCornerShape(4.dp)
                        ) {
                            Text(
                                text = when {
                                    pyodideStatus == "READY" -> "Pyodide 0.26.4 ✓"
                                    pyodideStatus.startsWith("LOAD_ERROR") -> "Offline"
                                    else -> stringResource(R.string.playground_pyodide_loading)
                                },
                                color = when {
                                    pyodideStatus == "READY" -> SuccessGreen
                                    pyodideStatus.startsWith("LOAD_ERROR") -> Color(0xFFFF7B72)
                                    else -> AmberGold
                                },
                                fontSize = 10.sp,
                                fontWeight = FontWeight.SemiBold,
                                modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                            )
                        }
                    }

                    Row(verticalAlignment = Alignment.CenterVertically) {
                        // Bersihkan Terminal
                        IconButton(
                            onClick = {
                                pythonLogs.clear()
                                Toast.makeText(context, context.getString(R.string.playground_clear_terminal_cd), Toast.LENGTH_SHORT).show()
                            },
                            modifier = Modifier.size(32.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.DeleteOutline,
                                contentDescription = stringResource(R.string.playground_clear_terminal_cd),
                                tint = Color(0xFF8B949E),
                                modifier = Modifier.size(18.dp)
                            )
                        }

                        // Copy code
                        IconButton(
                            onClick = {
                                val clipboard = context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
                                val clip = ClipData.newPlainText("python_code", pythonCode)
                                clipboard.setPrimaryClip(clip)
                                Toast.makeText(context, context.getString(R.string.comp_code_copied_toast), Toast.LENGTH_SHORT).show()
                            },
                            modifier = Modifier.size(32.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.ContentCopy,
                                contentDescription = stringResource(R.string.playground_copy_cd),
                                tint = Color.White,
                                modifier = Modifier.size(16.dp)
                            )
                        }

                        // Reset
                        IconButton(
                            onClick = {
                                pythonCode = starterPythonCode
                                pythonLogs.clear()
                                Toast.makeText(context, context.getString(R.string.playground_reset_toast), Toast.LENGTH_SHORT).show()
                            },
                            modifier = Modifier.size(32.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.Refresh,
                                contentDescription = stringResource(R.string.playground_reset_cd),
                                tint = FlameOrange,
                                modifier = Modifier.size(18.dp)
                            )
                        }

                        Spacer(modifier = Modifier.width(4.dp))

                        // Jalankan button
                        Button(
                            onClick = {
                                if (pyodideStatus != "READY") {
                                    if (pyodideStatus.startsWith("LOAD_ERROR")) {
                                        reloadPyodideTrigger++
                                        Toast.makeText(context, "Mencoba memuat ulang Pyodide...", Toast.LENGTH_SHORT).show()
                                    } else {
                                        Toast.makeText(context, "Engine Python masih memuat, mohon tunggu sebentar...", Toast.LENGTH_SHORT).show()
                                    }
                                } else if (!isPythonRunning) {
                                    isPythonRunning = true
                                    pythonLogs.add(PythonLogEntry(text = "$ python main.py", isPrompt = true))
                                    runPythonTrigger++
                                }
                            },
                            enabled = !isPythonRunning,
                            colors = ButtonDefaults.buttonColors(
                                containerColor = SuccessGreen,
                                disabledContainerColor = SuccessGreen.copy(alpha = 0.5f)
                            ),
                            shape = RoundedCornerShape(8.dp),
                            contentPadding = PaddingValues(horizontal = 12.dp, vertical = 6.dp)
                        ) {
                            if (isPythonRunning) {
                                CircularProgressIndicator(
                                    modifier = Modifier.size(14.dp),
                                    color = Color.White,
                                    strokeWidth = 2.dp
                                )
                            } else {
                                Icon(
                                    imageVector = Icons.Default.PlayArrow,
                                    contentDescription = null,
                                    tint = Color.White,
                                    modifier = Modifier.size(16.dp)
                                )
                            }
                            Spacer(modifier = Modifier.width(4.dp))
                            Text(
                                text = if (isPythonRunning) stringResource(R.string.playground_btn_running) else stringResource(R.string.playground_btn_run_code),
                                color = Color.White,
                                fontWeight = FontWeight.Bold,
                                fontSize = 12.sp
                            )
                        }
                    }
                }

                // Tantangan Modul (jika ada playgroundTask)
                if (!currentModule?.playgroundTask.isNullOrBlank()) {
                    Surface(
                        color = FlameOrange.copy(alpha = 0.12f),
                        border = BorderStroke(1.dp, FlameOrange.copy(alpha = 0.35f)),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 14.dp, vertical = 8.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(
                                imageVector = Icons.Default.Assignment,
                                contentDescription = null,
                                tint = FlameOrange,
                                modifier = Modifier.size(18.dp)
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Column(modifier = Modifier.weight(1f)) {
                                Text(
                                    text = stringResource(R.string.playground_task_title),
                                    color = FlameOrange,
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.Bold
                                )
                                Text(
                                    text = currentModule!!.playgroundTask!!,
                                    color = Color.White,
                                    fontSize = 12.sp,
                                    lineHeight = 16.sp
                                )
                            }
                        }
                    }
                }

                // Top: Python Code Editor
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f)
                ) {
                    CodeEditorField(
                        value = pythonCode,
                        onValueChange = { pythonCode = it },
                        language = "python"
                    )
                }

                // Bottom: Terminal Panel
                Surface(
                    color = Color(0xFF161B22),
                    border = BorderStroke(1.dp, CodeDarkBorder),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column {
                        // Terminal Header
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 14.dp, vertical = 8.dp),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(
                                    imageVector = Icons.Default.Terminal,
                                    contentDescription = stringResource(R.string.playground_output_console_title),
                                    tint = CyanAccent,
                                    modifier = Modifier.size(16.dp)
                                )
                                Spacer(modifier = Modifier.width(8.dp))
                                Text(
                                    text = stringResource(R.string.playground_output_console_title),
                                    color = Color.White,
                                    fontSize = 12.sp,
                                    fontWeight = FontWeight.SemiBold
                                )
                                if (isPythonRunning) {
                                    Spacer(modifier = Modifier.width(8.dp))
                                    Text(
                                        text = stringResource(R.string.playground_max_time_hint),
                                        color = AmberGold,
                                        fontSize = 11.sp
                                    )
                                }
                            }

                            TextButton(
                                onClick = { pythonLogs.clear() },
                                contentPadding = PaddingValues(horizontal = 8.dp, vertical = 2.dp)
                            ) {
                                Text(
                                    text = stringResource(R.string.playground_clear_terminal_cd),
                                    color = Color(0xFF8B949E),
                                    fontSize = 11.sp
                                )
                            }
                        }

                        // Terminal Body
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(210.dp)
                                .background(Color(0xFF0D1117))
                                .padding(10.dp)
                        ) {
                            if (pyodideStatus.startsWith("LOAD_ERROR")) {
                                Column(
                                    modifier = Modifier
                                        .fillMaxSize()
                                        .padding(8.dp),
                                    horizontalAlignment = Alignment.CenterHorizontally,
                                    verticalArrangement = Arrangement.Center
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.CloudOff,
                                        contentDescription = null,
                                        tint = Color(0xFFFF7B72),
                                        modifier = Modifier.size(28.dp)
                                    )
                                    Spacer(modifier = Modifier.height(6.dp))
                                    Text(
                                        text = stringResource(R.string.playground_python_error_title),
                                        color = Color.White,
                                        fontSize = 12.sp,
                                        fontWeight = FontWeight.Bold
                                    )
                                    Spacer(modifier = Modifier.height(2.dp))
                                    Text(
                                        text = stringResource(R.string.playground_python_error_desc),
                                        color = Color(0xFF8B949E),
                                        fontSize = 11.sp,
                                        textAlign = TextAlign.Center
                                    )
                                    Spacer(modifier = Modifier.height(8.dp))
                                    OutlinedButton(
                                        onClick = {
                                            reloadPyodideTrigger++
                                        },
                                        shape = RoundedCornerShape(8.dp),
                                        border = BorderStroke(1.dp, TealPrimary),
                                        contentPadding = PaddingValues(horizontal = 12.dp, vertical = 4.dp)
                                    ) {
                                        Text(stringResource(R.string.comp_error_retry), color = TealLight, fontSize = 11.sp)
                                    }
                                }
                            } else if (pyodideStatus == "LOADING" && pythonLogs.isEmpty()) {
                                Row(
                                    modifier = Modifier
                                        .fillMaxSize()
                                        .padding(16.dp),
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.Center
                                ) {
                                    CircularProgressIndicator(
                                        color = TealPrimary,
                                        modifier = Modifier.size(18.dp),
                                        strokeWidth = 2.dp
                                    )
                                    Spacer(modifier = Modifier.width(10.dp))
                                    Text(
                                        text = stringResource(R.string.playground_python_preparing_msg),
                                        color = Color(0xFF8B949E),
                                        fontSize = 11.sp,
                                        fontFamily = FontFamily.Monospace
                                    )
                                }
                            } else if (pythonLogs.isEmpty()) {
                                Text(
                                    text = stringResource(R.string.playground_python_empty_msg),
                                    color = Color(0xFF8B949E),
                                    fontSize = 11.sp,
                                    fontFamily = FontFamily.Monospace,
                                    modifier = Modifier.padding(4.dp)
                                )
                            } else {
                                val listState = rememberLazyListState()
                                LaunchedEffect(pythonLogs.size) {
                                    listState.animateScrollToItem(pythonLogs.size - 1)
                                }
                                LazyColumn(
                                    state = listState,
                                    modifier = Modifier.fillMaxSize()
                                ) {
                                    items(pythonLogs.size) { idx ->
                                        val log = pythonLogs[idx]
                                        val textColor = when {
                                            log.isPrompt -> CyanAccent
                                            log.isStderr -> Color(0xFFFF7B72)
                                            log.isSystem -> Color(0xFF8B949E)
                                            else -> Color(0xFF7EE787)
                                        }
                                        Text(
                                            text = log.text,
                                            color = textColor,
                                            fontSize = 11.sp,
                                            fontFamily = FontFamily.Monospace,
                                            lineHeight = 16.sp
                                        )
                                    }
                                }
                            }
                        }
                    }
                }
            } else {
                // Editor / Preview Tabs
                TabRow(
                selectedTabIndex = selectedTab,
                containerColor = CodeDarkSurface,
                contentColor = MaterialTheme.colorScheme.primary,
                indicator = { tabPositions ->
                    TabRowDefaults.SecondaryIndicator(
                        modifier = Modifier.tabIndicatorOffset(tabPositions[selectedTab]),
                        color = MaterialTheme.colorScheme.primary
                    )
                }
            ) {
                listOf("HTML", "CSS", "JS", stringResource(R.string.playground_output_web_preview)).forEachIndexed { index, title ->
                    Tab(
                        selected = selectedTab == index,
                        onClick = {
                            try {
                                selectedTab = index
                                if (index == 3) {
                                    // Auto-run when switching to Live Preview
                                    executedHtml = htmlCode
                                    executedCss = cssCode
                                    executedJs = jsCode
                                }
                            } catch (e: Exception) {
                                Toast.makeText(context, "${e.localizedMessage}", Toast.LENGTH_SHORT).show()
                            }
                        },
                        text = {
                            Text(
                                text = title,
                                fontWeight = if (selectedTab == index) FontWeight.Bold else FontWeight.Medium,
                                fontSize = 12.sp,
                                color = if (selectedTab == index) MaterialTheme.colorScheme.primary else Color(0xFF8B949E)
                            )
                        }
                    )
                }
            }

            // Sub-header Action Bar
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color(0xFF161B22))
                    .padding(horizontal = 12.dp, vertical = 6.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        text = when (selectedTab) {
                            0 -> "index.html"
                            1 -> "style.css"
                            2 -> "script.js"
                            else -> stringResource(R.string.playground_output_web_preview)
                        },
                        color = Color(0xFFA5D6FF),
                        fontFamily = FontFamily.Monospace,
                        fontSize = 11.sp
                    )

                    Spacer(modifier = Modifier.width(8.dp))

                    Surface(
                        color = Color(0xFF21262D),
                        shape = RoundedCornerShape(4.dp)
                    ) {
                        Text(
                            text = "GitHub Dark",
                            color = Color(0xFF8B949E),
                            fontSize = 10.sp,
                            modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                        )
                    }
                }

                Row(verticalAlignment = Alignment.CenterVertically) {
                    // Copy code
                    IconButton(
                        onClick = {
                            val codeToCopy = when (selectedTab) {
                                0 -> htmlCode
                                1 -> cssCode
                                2 -> jsCode
                                else -> "$htmlCode\n\n<style>\n$cssCode\n</style>\n\n<script>\n$jsCode\n</script>"
                            }
                            val clipboard = context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
                            val clip = ClipData.newPlainText("code", codeToCopy)
                            clipboard.setPrimaryClip(clip)
                            Toast.makeText(context, context.getString(R.string.comp_code_copied_toast), Toast.LENGTH_SHORT).show()
                        },
                        modifier = Modifier.size(32.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.ContentCopy,
                            contentDescription = stringResource(R.string.comp_copy),
                            tint = Color.White,
                            modifier = Modifier.size(16.dp)
                        )
                    }

                    Spacer(modifier = Modifier.width(6.dp))

                    // Run button
                    Button(
                        onClick = {
                            try {
                                executedHtml = htmlCode
                                executedCss = cssCode
                                executedJs = jsCode
                                selectedTab = 3 // Switch to preview
                                Toast.makeText(context, context.getString(R.string.playground_btn_run_code), Toast.LENGTH_SHORT).show()
                            } catch (e: Exception) {
                                Toast.makeText(context, "${e.localizedMessage}", Toast.LENGTH_SHORT).show()
                            }
                        },
                        colors = ButtonDefaults.buttonColors(
                            containerColor = SuccessGreen
                        ),
                        shape = RoundedCornerShape(8.dp),
                        contentPadding = PaddingValues(horizontal = 12.dp, vertical = 6.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.PlayArrow,
                            contentDescription = null,
                            tint = Color.White,
                            modifier = Modifier.size(16.dp)
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            text = stringResource(R.string.playground_btn_run_code),
                            color = Color.White,
                            fontWeight = FontWeight.Bold,
                            fontSize = 12.sp
                        )
                    }
                }
            }

            // Main Editor or Preview Container
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
            ) {
                when (selectedTab) {
                    0 -> {
                        // HTML Editor
                        CodeEditorField(
                            value = htmlCode,
                            onValueChange = { htmlCode = it },
                            language = "html"
                        )
                    }
                    1 -> {
                        // CSS Editor
                        CodeEditorField(
                            value = cssCode,
                            onValueChange = { cssCode = it },
                            language = "css"
                        )
                    }
                    2 -> {
                        // JS Editor
                        CodeEditorField(
                            value = jsCode,
                            onValueChange = { jsCode = it },
                            language = "js"
                        )
                    }
                    3 -> {
                        // Live Preview WebView
                        CodeSandboxWebView(
                            html = executedHtml,
                            css = executedCss,
                            js = executedJs,
                            onConsoleLog = { logMsg ->
                                try {
                                    if (consoleLogs.size >= 100) {
                                        consoleLogs.removeAt(0)
                                    }
                                    consoleLogs.add(logMsg)
                                } catch (_: Throwable) {
                                    // Ignore list modification race during clear/dispose
                                }
                            },
                            modifier = Modifier.fillMaxSize()
                        )
                    }
                }
            }

            // Target Latihan Verification Checklist
            if (verificationRules.isNotEmpty()) {
                Surface(
                    color = if (allRulesPassed) Color(0xFF0F2E1E) else Color(0xFF161B22),
                    border = androidx.compose.foundation.BorderStroke(
                        1.dp,
                        if (allRulesPassed) SuccessGreen.copy(alpha = 0.5f) else CodeDarkBorder
                    ),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 14.dp, vertical = 8.dp)
                    ) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(
                                    imageVector = if (allRulesPassed) Icons.Default.CheckCircle else Icons.Default.AssignmentTurnedIn,
                                    contentDescription = null,
                                    tint = if (allRulesPassed) SuccessGreen else CyanAccent,
                                    modifier = Modifier.size(16.dp)
                                )
                                Spacer(modifier = Modifier.width(8.dp))
                                Text(
                                    text = if (allRulesPassed) stringResource(R.string.playground_verify_done_format, rulesPassedCount, verificationRules.size) else stringResource(R.string.playground_verify_target_format, rulesPassedCount, verificationRules.size),
                                    color = if (allRulesPassed) SuccessGreen else Color.White,
                                    fontSize = 12.sp,
                                    fontWeight = FontWeight.Bold
                                )
                            }
                            if (allRulesPassed) {
                                Surface(
                                    color = SuccessGreen.copy(alpha = 0.2f),
                                    shape = RoundedCornerShape(12.dp)
                                ) {
                                    Text(
                                        text = stringResource(R.string.playground_verify_ready_badge),
                                        color = SuccessGreen,
                                        fontSize = 10.sp,
                                        fontWeight = FontWeight.Bold,
                                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 2.dp)
                                    )
                                }
                            }
                        }

                        Spacer(modifier = Modifier.height(4.dp))

                        rulesPassedStatus.forEach { (rule, passed) ->
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                modifier = Modifier.padding(vertical = 2.dp)
                            ) {
                                Icon(
                                    imageVector = if (passed) Icons.Default.CheckCircle else Icons.Default.RadioButtonUnchecked,
                                    contentDescription = if (passed) stringResource(R.string.comp_completed_badge) else stringResource(R.string.comp_locked_badge),
                                    tint = if (passed) SuccessGreen else Color(0xFF8B949E),
                                    modifier = Modifier.size(14.dp)
                                )
                                Spacer(modifier = Modifier.width(6.dp))
                                Text(
                                    text = stringResource(R.string.playground_verify_contains_format, rule),
                                    color = if (passed) Color(0xFFC9D1D9) else Color(0xFF8B949E),
                                    fontSize = 11.sp,
                                    fontFamily = FontFamily.Monospace
                                )
                            }
                        }
                    }
                }
            }

            // Console toggle and panel
            Surface(
                color = Color(0xFF161B22),
                border = androidx.compose.foundation.BorderStroke(1.dp, CodeDarkBorder),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable { showConsole = !showConsole }
                            .padding(horizontal = 14.dp, vertical = 8.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                imageVector = Icons.Default.Terminal,
                                contentDescription = stringResource(R.string.playground_output_console_title),
                                tint = MaterialTheme.colorScheme.primary,
                                modifier = Modifier.size(16.dp)
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = stringResource(R.string.playground_console_header_format, consoleLogs.size),
                                color = Color.White,
                                fontSize = 12.sp,
                                fontWeight = FontWeight.SemiBold
                            )
                        }

                        Text(
                            text = if (showConsole) stringResource(R.string.playground_console_close) else stringResource(R.string.playground_console_open),
                            color = Color(0xFF8B949E),
                            fontSize = 11.sp
                        )
                    }

                    AnimatedVisibility(visible = showConsole) {
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(120.dp)
                                .background(Color(0xFF0D1117))
                                .padding(12.dp)
                        ) {
                            if (consoleLogs.isEmpty()) {
                                Text(
                                    text = stringResource(R.string.playground_console_empty_msg),
                                    color = Color(0xFF8B949E),
                                    fontSize = 11.sp,
                                    fontFamily = FontFamily.Monospace
                                )
                            } else {
                                consoleLogs.forEach { log ->
                                    Text(
                                        text = "> $log",
                                        color = Color(0xFF7EE787),
                                        fontSize = 11.sp,
                                        fontFamily = FontFamily.Monospace
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

@Composable
fun CodeEditorField(
    value: String,
    onValueChange: (String) -> Unit,
    language: String = "html",
    modifier: Modifier = Modifier
) {
    val transformation = remember(language) { codeHighlightTransformation(language) }
    Box(
        modifier = modifier
            .fillMaxSize()
            .background(CodeDarkBg)
            .padding(12.dp)
    ) {
        BasicTextField(
            value = value,
            onValueChange = onValueChange,
            textStyle = TextStyle(
                fontFamily = FontFamily.Monospace,
                fontSize = 13.sp,
                color = Color(0xFFE6EDF3),
                lineHeight = 22.sp
            ),
            visualTransformation = transformation,
            cursorBrush = SolidColor(MaterialTheme.colorScheme.primary),
            modifier = Modifier.fillMaxSize()
        )
    }
}
