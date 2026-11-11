package com.example.ui.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.model.CourseEntity
import com.example.data.model.ModuleEntity
import com.example.data.model.PlaygroundDraftEntity
import com.example.data.repository.CodeAcademyRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

data class PlaygroundSessionState(
    val courseId: String = "",
    val moduleId: String = "",
    val course: CourseEntity? = null,
    val module: ModuleEntity? = null,
    val initialHtml: String = "",
    val initialCss: String = "",
    val initialJs: String = "",
    val hasLoaded: Boolean = false
)

class PlaygroundViewModel(application: Application) : AndroidViewModel(application) {
    private val repository = CodeAcademyRepository.getInstance(application)

    val currentUserId: String?
        get() = repository.authService.currentUserId

    private val _sessionState = MutableStateFlow(PlaygroundSessionState())
    val sessionState: StateFlow<PlaygroundSessionState> = _sessionState.asStateFlow()

    private val _currentDraft = MutableStateFlow<PlaygroundDraftEntity?>(null)
    val currentDraft: StateFlow<PlaygroundDraftEntity?> = _currentDraft.asStateFlow()

    fun loadSession(courseId: String, moduleId: String) {
        val uid = currentUserId ?: ""
        viewModelScope.launch {
            val course = repository.getCourseById(courseId)
            val module = repository.getModuleById(moduleId)

            val draftKey = if (moduleId.isNotBlank()) "${courseId}_$moduleId" else courseId
            val draft = if (uid.isNotBlank()) repository.getPlaygroundDraft(uid, draftKey) else null

            val starterHtml = when {
                !draft?.htmlCode.isNullOrBlank() -> draft!!.htmlCode
                !module?.starterHtml.isNullOrBlank() -> module!!.starterHtml!!
                module?.codeLanguage == "html" && !module.codeSnippet.isNullOrBlank() -> module.codeSnippet!!
                else -> """
                    <div class="card">
                      <h2>${module?.title ?: "Praktik Koding"}</h2>
                      <p>${module?.playgroundTask ?: "Eksperimen langsung dengan HTML, CSS, dan JavaScript!"}</p>
                      <button id="actionBtn" onclick="jalankanAksi()">Klik Saya ✨</button>
                      <p id="hasil" style="margin-top: 12px; color: #00cec9;"></p>
                    </div>
                """.trimIndent()
            }

            val starterCss = when {
                !draft?.cssCode.isNullOrBlank() -> draft!!.cssCode
                !module?.starterCss.isNullOrBlank() -> module!!.starterCss!!
                module?.codeLanguage == "css" && !module.codeSnippet.isNullOrBlank() -> module.codeSnippet!!
                else -> """
                    body {
                      margin: 0;
                      padding: 24px;
                      background: #0f1115;
                      color: #f5f5f7;
                      font-family: system-ui, sans-serif;
                      display: flex;
                      justify-content: center;
                      align-items: center;
                      min-height: 80vh;
                    }
                    .card {
                      background: #1a1d23;
                      padding: 24px;
                      border-radius: 16px;
                      border: 1px solid #6c5ce7;
                      text-align: center;
                      max-width: 420px;
                      box-shadow: 0 10px 30px rgba(108, 92, 231, 0.25);
                    }
                    h2 { color: #00cec9; margin-top: 0; }
                    button {
                      background: #6c5ce7;
                      color: white;
                      border: none;
                      padding: 10px 20px;
                      border-radius: 8px;
                      font-size: 14px;
                      font-weight: bold;
                      cursor: pointer;
                    }
                """.trimIndent()
            }

            val starterJs = when {
                !draft?.jsCode.isNullOrBlank() -> draft!!.jsCode
                !module?.starterJs.isNullOrBlank() -> module!!.starterJs!!
                (module?.codeLanguage == "javascript" || module?.codeLanguage == "js") && !module.codeSnippet.isNullOrBlank() -> module.codeSnippet!!
                else -> """
                    function jalankanAksi() {
                      const hasil = document.getElementById('hasil');
                      hasil.innerText = '⚡ Kode interaktif berhasil dijalankan di Playground!';
                    }
                """.trimIndent()
            }

            _currentDraft.value = draft
            _sessionState.value = PlaygroundSessionState(
                courseId = courseId,
                moduleId = moduleId,
                course = course,
                module = module,
                initialHtml = starterHtml,
                initialCss = starterCss,
                initialJs = starterJs,
                hasLoaded = true
            )
        }
    }

    fun saveDraft(courseId: String, moduleId: String, html: String, css: String, js: String) {
        val uid = currentUserId ?: return
        val draftKey = if (moduleId.isNotBlank()) "${courseId}_$moduleId" else courseId
        viewModelScope.launch {
            val draft = PlaygroundDraftEntity(
                userId = uid,
                courseId = draftKey,
                htmlCode = html,
                cssCode = css,
                jsCode = js,
                updatedAt = System.currentTimeMillis()
            )
            repository.savePlaygroundDraft(draft)
            _currentDraft.value = draft
        }
    }

    // Legacy support
    fun loadDraft(courseId: String) {
        loadSession(courseId, "")
    }

    fun saveDraft(courseId: String, html: String, css: String, js: String) {
        saveDraft(courseId, "", html, css, js)
    }
}
