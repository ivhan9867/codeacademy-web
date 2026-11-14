package com.example.ai

import android.util.Log
import com.example.BuildConfig
import com.google.firebase.Firebase
import com.google.firebase.ai.ai
import com.google.firebase.ai.type.content
import com.google.firebase.ai.type.generationConfig
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.json.JSONArray
import org.json.JSONObject
import java.io.BufferedReader
import java.io.InputStreamReader
import java.io.OutputStreamWriter
import java.net.HttpURLConnection
import java.net.URL

data class AiTutorResult(
    val text: String,
    val isLive: Boolean,
    val provider: String,
    val errorDetail: String? = null
)

/**
 * Dual-Engine AI Tutor Service:
 * 1. Direct Gemini 2.0 Flash REST API using GEMINI_API_KEY from `.env` (Secrets Gradle Plugin).
 * 2. Firebase AI Logic SDK (`com.google.firebase:firebase-ai`) fallback if key is unset.
 * 3. Context-aware localized offline fallback if both cloud providers are unavailable.
 */
class GeminiTutorService {

    companion object {
        private const val TAG = "GeminiTutorService"
        private val DIRECT_GEMINI_MODELS = listOf(
            "gemini-3-flash-preview",
            "gemini-flash-lite-latest",
            "gemini-3.5-flash-lite",
            "gemini-3.8-flash",
            "gemini-flash-latest"
        )
        private fun directEndpoint(model: String) =
            "https://generativelanguage.googleapis.com/v1beta/models/$model:generateContent"

        fun resolveLanguageName(language: String?): String {
            return when (language?.lowercase()?.trim()) {
                "en" -> "English"
                "ja" -> "Japanese"
                "zh" -> "Chinese"
                else -> "Bahasa Indonesia"
            }
        }

        fun buildSystemPrompt(
            courseTitle: String,
            moduleTitle: String,
            moduleContent: String? = null,
            currentPlaygroundCode: String? = null,
            language: String = "id"
        ): String {
            val langName = resolveLanguageName(language)
            val contextAdditions = buildString {
                if (!moduleContent.isNullOrBlank()) {
                    append("\nKonten modul yang sedang dibaca murid:\n\"\"\"\n${moduleContent.take(1500)}\n\"\"\"\n")
                }
                if (!currentPlaygroundCode.isNullOrBlank()) {
                    append("\nKode yang sedang ditulis murid di Playground saat ini:\n\"\"\"\n${currentPlaygroundCode.take(1500)}\n\"\"\"\n")
                    append("\nInstruksi khusus: Berikan hint atau petunjuk terarah berdasarkan kode murid saat ini jika relevan dengan pertanyaannya (jangan langsung berikan kode jadi utuh, melainkan arahkan siswa untuk menemukan dan memperbaiki letak kekeliruannya sendiri, misalnya: 'coba periksa tag penutup atau atribut pada baris...').\n")
                }
            }

            return """
Anda adalah AI Tutor resmi untuk CodeAcademy Indonesia, platform pembelajaran web development mobile-first.
Tugas Anda:
1. Menjawab pertanyaan seputar pemrograman Web Development (HTML, CSS, dan JavaScript) dalam $langName yang ramah, jelas, edukatif, dan mudah dipahami siswa.
2. Konteks belajar pengguna saat ini: Kelas "$courseTitle", Modul "$moduleTitle".$contextAdditions
3. Berikan hint atau petunjuk terarah daripada langsung memberikan kode utuh, agar siswa dapat belajar memecahkan masalah sendiri.
4. Berikan contoh kode ringkas bila relevan menggunakan Markdown code block (misal ```html atau ```javascript).
5. Hindari topik di luar pemrograman. Berikan semangat belajar dan tips praktis.
            """.trimIndent()
        }
    }

    suspend fun askTutor(
        userMessage: String,
        courseTitle: String,
        moduleTitle: String,
        moduleContent: String? = null,
        currentPlaygroundCode: String? = null,
        language: String = "id"
    ): String {
        return askTutorDetailed(
            userMessage = userMessage,
            courseTitle = courseTitle,
            moduleTitle = moduleTitle,
            moduleContent = moduleContent,
            currentPlaygroundCode = currentPlaygroundCode,
            language = language
        ).text
    }

    suspend fun askTutorDetailed(
        userMessage: String,
        courseTitle: String,
        moduleTitle: String,
        moduleContent: String? = null,
        currentPlaygroundCode: String? = null,
        language: String = "id"
    ): AiTutorResult = withContext(Dispatchers.IO) {
        val systemPrompt = buildSystemPrompt(
            courseTitle = courseTitle,
            moduleTitle = moduleTitle,
            moduleContent = moduleContent,
            currentPlaygroundCode = currentPlaygroundCode,
            language = language
        )

        var directError: String? = null
        var firebaseError: String? = null

        // 1. Primary: Direct Gemini REST API via BuildConfig.GEMINI_API_KEY
        val apiKey = try {
            BuildConfig.GEMINI_API_KEY.trim().trim('"', '\'')
        } catch (t: Throwable) {
            directError = "BuildConfig.GEMINI_API_KEY error: ${t.message}"
            ""
        }

        if (apiKey.isBlank()) {
            if (directError == null) {
                directError = "GEMINI_API_KEY kosong di BuildConfig (.env tidak terbaca saat build)"
            }
        } else {
            for (modelName in DIRECT_GEMINI_MODELS) {
                try {
                    val directReply = callDirectGeminiApi(
                        modelName = modelName,
                        apiKey = apiKey,
                        systemPrompt = systemPrompt,
                        userMessage = userMessage
                    )
                    if (!directReply.isNullOrBlank()) {
                        return@withContext AiTutorResult(
                            text = directReply,
                            isLive = true,
                            provider = "Gemini Live"
                        )
                    }
                } catch (e: Throwable) {
                    directError = "$modelName (key=${apiKey.take(6)}..len=${apiKey.length}): ${e.message}"
                    Log.e(TAG, "Direct Gemini API ($modelName) failed", e)
                }
            }
        }

        // 2. Secondary: Firebase AI Logic SDK
        try {
            val generativeModel = Firebase.ai.generativeModel(
                modelName = "gemini-3-flash-preview",
                systemInstruction = content {
                    text(systemPrompt)
                },
                generationConfig = generationConfig {
                    temperature = 0.7f
                    maxOutputTokens = 800
                }
            )

            val response = generativeModel.generateContent(userMessage)
            val replyText = response.text
            if (!replyText.isNullOrBlank()) {
                return@withContext AiTutorResult(
                    text = replyText,
                    isLive = true,
                    provider = "Firebase AI Live"
                )
            }
        } catch (e: Throwable) {
            firebaseError = "FirebaseAI: ${e.message}"
            Log.e(TAG, firebaseError, e)
        }

        val combinedError = listOfNotNull(directError, firebaseError).joinToString(" | ")
        val fallbackBase = generateSmartFallbackResponse(userMessage, courseTitle, moduleTitle, moduleContent)
        val diagnosticNote = if (combinedError.isNotBlank()) "\n\n---\n_Info koneksi: ${combinedError.take(220)}_" else ""

        // 3. Fallback: Smart localized engine (clearly flagged as offline)
        return@withContext AiTutorResult(
            text = fallbackBase + diagnosticNote,
            isLive = false,
            provider = "Mode Offline",
            errorDetail = combinedError
        )
    }

    private fun callDirectGeminiApi(
        modelName: String,
        apiKey: String,
        systemPrompt: String,
        userMessage: String
    ): String? {
        val url = URL("${directEndpoint(modelName)}?key=$apiKey")
        val conn = (url.openConnection() as HttpURLConnection).apply {
            requestMethod = "POST"
            connectTimeout = 10_000
            readTimeout = 20_000
            doOutput = true
            setRequestProperty("Content-Type", "application/json; charset=UTF-8")
        }

        val payload = JSONObject().apply {
            put("system_instruction", JSONObject().apply {
                put("parts", JSONArray().apply {
                    put(JSONObject().apply { put("text", systemPrompt) })
                })
            })
            put("contents", JSONArray().apply {
                put(JSONObject().apply {
                    put("role", "user")
                    put("parts", JSONArray().apply {
                        put(JSONObject().apply { put("text", userMessage) })
                    })
                })
            })
            put("generationConfig", JSONObject().apply {
                put("temperature", 0.7)
                put("maxOutputTokens", 800)
            })
        }

        return try {
            OutputStreamWriter(conn.outputStream, Charsets.UTF_8).use { writer ->
                writer.write(payload.toString())
                writer.flush()
            }

            val code = conn.responseCode
            val stream = if (code in 200..299) conn.inputStream else conn.errorStream
            val body = BufferedReader(InputStreamReader(stream, Charsets.UTF_8)).use { it.readText() }

            if (code !in 200..299) {
                throw IllegalStateException("HTTP $code: $body")
            }

            val json = JSONObject(body)
            val candidates = json.optJSONArray("candidates") ?: return null
            if (candidates.length() == 0) return null
            val contentObj = candidates.getJSONObject(0).optJSONObject("content") ?: return null
            val parts = contentObj.optJSONArray("parts") ?: return null
            if (parts.length() == 0) return null
            parts.getJSONObject(0).optString("text").takeIf { it.isNotBlank() }
        } finally {
            conn.disconnect()
        }
    }

    private fun generateSmartFallbackResponse(
        query: String,
        courseTitle: String,
        moduleTitle: String,
        moduleContent: String? = null
    ): String {
        val q = query.lowercase()
        val moduleHint = moduleContent
            ?.lineSequence()
            ?.map { it.trim() }
            ?.firstOrNull { it.length > 40 && !it.startsWith("#") && !it.startsWith("```") && !it.startsWith("---") }
            ?.let { "\n\n**Ringkasan Modul ($moduleTitle):**\n> $it" }
            .orEmpty()

        return when {
            q.contains("flexbox") || q.contains("flex") -> """
**CSS Flexbox** adalah model tata letak satu dimensi yang memudahkan penyusunan elemen secara horizontal (baris) atau vertikal (kolom).

```css
.wadah {
  display: flex;
  justify-content: center;
  align-items: center;
  gap: 16px;
}
```
*Tips CodeAcademy:* Gunakan `justify-content: space-between;` untuk navbar yang rapi di layar ponsel.
            """.trimIndent()

            q.contains("grid") -> """
**CSS Grid** ideal untuk tata letak dua dimensi (baris dan kolom sekaligus).

```css
.galeri {
  display: grid;
  grid-template-columns: repeat(auto-fit, minmax(160px, 1fr));
  gap: 12px;
}
```
Kombinasi `auto-fit` dan `minmax()` membuat jumlah kolom otomatis responsif tanpa media query.
            """.trimIndent()

            q.contains("html") || q.contains("tag") || q.contains("elemen") -> """
Di HTML, elemen disusun dari **Tag Pembuka**, **Konten**, dan **Tag Penutup**.

```html
<main>
  <button class="btn-utama">Mulai Belajar</button>
</main>
```
Dalam modul **$moduleTitle**, gunakan tag semantik seperti `<header>`, `<main>`, `<section>`, dan `<footer>` agar struktur halaman rapi dan aksesibel.
            """.trimIndent()

            q.contains("js") || q.contains("javascript") || q.contains("variabel") || q.contains("dom") -> """
Dalam JavaScript modern (ES6+), utamakan `const` untuk nilai tetap dan `let` untuk nilai yang berubah, serta `querySelector` untuk manipulasi DOM:

```javascript
const tombol = document.querySelector("#btn-aksi");
let hitung = 0;

tombol.addEventListener("click", () => {
  hitung += 1;
  tombol.textContent = `Diklik ${'$'}{hitung}x`;
});
```
            """.trimIndent()

            q.contains("contoh kode") || q.contains("kode") -> """
Berikut contoh kode interaktif yang relevan dengan **$moduleTitle**:

```html
<div class="card">
  <h2>Halo dari CodeAcademy</h2>
  <p>Tekan tombol di bawah untuk menguji aksi JavaScript.</p>
  <button onclick="console.log('Berjalan!')">Uji Kode</button>
</div>
```
Kamu bisa langsung mengujinya lewat tombol **Coba di Playground**.
            """.trimIndent()

            else -> """
*(Mode Offline — koneksi ke Gemini AI sedang tidak tersedia)*

Untuk modul **$moduleTitle** di kelas **$courseTitle**, kamu bisa mencoba langkah berikut:$moduleHint

1. Uji langsung cuplikan kode di **Playground** lalu cek tab **Preview Web** & **Output Console**.
2. Periksa penulisan tag pembuka/penutup HTML, titik koma (`;`) CSS, dan nama selector di JavaScript.
3. Jika sudah terhubung internet, kirim ulang pertanyaanmu agar Gemini 2.0 menganalisisnya secara langsung.
            """.trimIndent()
        }
    }
}
