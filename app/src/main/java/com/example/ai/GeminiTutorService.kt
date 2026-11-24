package com.example.ai

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.util.Base64
import android.util.Log
import com.example.BuildConfig
import com.google.firebase.Firebase
import com.google.firebase.ai.ai
import com.google.firebase.ai.type.GenerativeBackend
import com.google.firebase.ai.type.content
import com.google.firebase.ai.type.generationConfig
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
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
            "gemini-flash-lite-latest",
            "gemini-3.5-flash-lite",
            "gemini-3.1-flash-lite",
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
            val isIndonesian = langName == "Bahasa Indonesia"

            val contextAdditions = buildString {
                if (!moduleContent.isNullOrBlank()) {
                    if (isIndonesian) {
                        append("\nKonten modul yang sedang dibaca murid:\n\"\"\"\n${moduleContent.take(1500)}\n\"\"\"\n")
                    } else {
                        append("\nModule content currently being read by the student:\n\"\"\"\n${moduleContent.take(1500)}\n\"\"\"\n")
                    }
                }
                if (!currentPlaygroundCode.isNullOrBlank()) {
                    if (isIndonesian) {
                        append("\nKode yang sedang ditulis murid di Playground saat ini:\n\"\"\"\n${currentPlaygroundCode.take(1500)}\n\"\"\"\n")
                        append("\nInstruksi khusus: Berikan hint atau petunjuk terarah berdasarkan kode murid saat ini jika relevan dengan pertanyaannya (jangan langsung berikan kode jadi utuh, melainkan arahkan siswa untuk menemukan dan memperbaiki letak kekeliruannya sendiri, misalnya: 'coba periksa tag penutup atau atribut pada baris...').\n")
                    } else {
                        append("\nCode currently written by the student in the Playground:\n\"\"\"\n${currentPlaygroundCode.take(1500)}\n\"\"\"\n")
                        append("\nSpecial instruction: Provide targeted hints or guidance based on the student's current code when relevant to their question (do not give away the complete solution directly; instead guide the student to find and fix the mistake themselves, e.g., 'try checking the closing tag or attribute on line...').\n")
                    }
                }
            }

            return if (isIndonesian) {
                """
Anda adalah AI Tutor resmi untuk CodeAcademy Indonesia, platform pembelajaran web development mobile-first.
Tugas Anda:
1. Menjawab pertanyaan seputar pemrograman Web Development (HTML, CSS, dan JavaScript) dalam $langName yang ramah, jelas, edukatif, dan mudah dipahami siswa.
2. Konteks belajar pengguna saat ini: Kelas "$courseTitle", Modul "$moduleTitle".$contextAdditions
3. Berikan hint atau petunjuk terarah daripada langsung memberikan kode utuh, agar siswa dapat belajar memecahkan masalah sendiri.
4. Format jawaban agar sangat rapi dan nyaman dibaca di layar HP: gunakan subjudul singkat (`###`), poin-poin ringkas (`-` atau `1.`), inline code (`` `tag` ``) untuk nama elemen/properti, dan blok kode Markdown (` ```html `, ` ```css `, ` ```javascript `) bila relevan. Hindari paragraf yang terlalu panjang.
5. Hindari topik di luar pemrograman. Berikan semangat belajar dan tips praktis.
6. Jika pengguna melampirkan gambar/screenshot kode, pesan error, atau tampilan UI, periksa detail kode/error yang terlihat pada gambar tersebut secara teliti, tunjukkan letak kesalahan sintaksis/logika/tata letak secara spesifik, dan bimbing siswa langkah demi langkah.
7. Jika pengguna hanya menyapa (misalnya "hai", "halo", "hi", "pagi", atau sapaan singkat tanpa pertanyaan teknis), balas dengan hangat dan singkat dalam 1-2 kalimat saja tanpa blok kode panjang.
                """.trimIndent()
            } else {
                """
You are the official AI Tutor for CodeAcademy, a mobile-first web development learning platform.
Your responsibilities:
1. Answer Web Development programming questions (HTML, CSS, and JavaScript) in friendly, clear, educational, and easy-to-understand $langName.
2. Current student learning context: Course "$courseTitle", Module "$moduleTitle".$contextAdditions
3. Provide guided hints rather than giving away the full final code, so the student learns to solve problems independently.
4. Format answers cleanly for mobile readability: use short section headings (`###`), concise bullet/numbered lists, inline backticks (`` `tag` ``) for code identifiers, and fenced Markdown code blocks (` ```html `, ` ```css `, ` ```javascript `) when relevant. Avoid dense walls of text.
5. Stay focused on programming topics. Offer encouragement and practical tips.
6. If an image/screenshot of code, error message, or UI layout is attached, carefully inspect the visible code/output in the image, pinpoint the exact syntax, logic, or layout issue, and guide the student step by step.
7. If the user only greets you (e.g., "hi", "hello", "hey", or a short greeting without a technical question), reply warmly and briefly in 1-2 sentences without a long code block.
                """.trimIndent()
            }
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
        language: String = "id",
        imageBytes: ByteArray? = null,
        imageMimeType: String = "image/jpeg"
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
                var attempts = 0
                val maxAttempts = if (modelName == "gemini-flash-lite-latest" || modelName == "gemini-3.5-flash-lite") 2 else 1
                while (attempts < maxAttempts) {
                    attempts++
                    try {
                        val directReply = callDirectGeminiApi(
                            modelName = modelName,
                            apiKey = apiKey,
                            systemPrompt = systemPrompt,
                            userMessage = userMessage,
                            imageBytes = imageBytes,
                            imageMimeType = imageMimeType,
                            maxOutputTokens = 1400
                        )
                        if (!directReply.isNullOrBlank()) {
                            return@withContext AiTutorResult(
                                text = directReply,
                                isLive = true,
                                provider = "Gemini Live"
                            )
                        }
                        break
                    } catch (e: Throwable) {
                        val msg = e.message ?: ""
                        directError = "$modelName: $msg"
                        Log.e(TAG, "Direct Gemini API ($modelName, attempt $attempts) failed", e)
                        val isTransient = msg.contains("503") || msg.contains("429") || msg.contains("UNAVAILABLE", ignoreCase = true)
                        if (isTransient && attempts < maxAttempts) {
                            delay(1200L)
                        } else {
                            break
                        }
                    }
                }
            }
        }

        // 2. Secondary: Firebase AI Logic SDK (Gemini Developer API - free tier)
        val decodedBitmap: Bitmap? = imageBytes?.let { bytes ->
            try { BitmapFactory.decodeByteArray(bytes, 0, bytes.size) } catch (_: Throwable) { null }
        }
        try {
            for (fbModel in listOf("gemini-flash-lite-latest", "gemini-3.5-flash-lite", "gemini-3.1-flash-lite", "gemini-3.8-flash")) {
                try {
                    val generativeModel = Firebase.ai(backend = GenerativeBackend.googleAI()).generativeModel(
                        modelName = fbModel,
                        systemInstruction = content {
                            text(systemPrompt)
                        },
                        generationConfig = generationConfig {
                            temperature = 0.7f
                            maxOutputTokens = 1400
                        }
                    )

                    val response = if (decodedBitmap != null) {
                        generativeModel.generateContent(
                            content {
                                image(decodedBitmap)
                                text(userMessage)
                            }
                        )
                    } else {
                        generativeModel.generateContent(userMessage)
                    }
                    val replyText = response.text
                    if (!replyText.isNullOrBlank()) {
                        return@withContext AiTutorResult(
                            text = replyText,
                            isLive = true,
                            provider = "Firebase AI Live"
                        )
                    }
                } catch (e: Throwable) {
                    firebaseError = "FirebaseAI ($fbModel): ${e.message}"
                    Log.e(TAG, firebaseError, e)
                }
            }
        } finally {
            decodedBitmap?.recycle()
        }

        val combinedError = listOfNotNull(directError, firebaseError).joinToString(" | ")
        val fallbackBase = generateSmartFallbackResponse(userMessage, courseTitle, moduleTitle, moduleContent, language)
        val isIndonesian = resolveLanguageName(language) == "Bahasa Indonesia"
        val diagnosticNote = if (combinedError.isNotBlank()) {
            val friendlyReason = when {
                combinedError.contains("503") || combinedError.contains("high demand", ignoreCase = true) || combinedError.contains("UNAVAILABLE", ignoreCase = true) ->
                    if (isIndonesian) "Server Gemini sedang sibuk (503). Silakan coba kirim ulang beberapa detik lagi."
                    else "Gemini servers are currently experiencing high demand (503). Please try again in a few seconds."
                combinedError.contains("429") || combinedError.contains("quota", ignoreCase = true) ->
                    if (isIndonesian) "Batas permintaan sementara tercapai (429). Tunggu sebentar lalu coba lagi."
                    else "Rate limit temporarily reached (429). Please wait a moment and try again."
                else -> combinedError.take(160)
            }
            val label = if (isIndonesian) "Info koneksi" else "Connection info"
            "\n\n---\n_${label}: ${friendlyReason}_"
        } else ""

        // 3. Fallback: Smart localized engine (clearly flagged as offline)
        return@withContext AiTutorResult(
            text = fallbackBase + diagnosticNote,
            isLive = false,
            provider = if (isIndonesian) "Mode Offline" else "Offline Mode",
            errorDetail = combinedError
        )
    }

    /**
     * Translates an Indonesian Markdown lesson into clean English while preserving all
     * Markdown syntax, headings, tables, callouts, and fenced code blocks.
     */
    suspend fun translateMarkdownToEnglish(markdown: String): String? = withContext(Dispatchers.IO) {
        val sys = "You are a technical translator for a Web Development learning app. Translate the following Indonesian Markdown lesson into clear, natural, beginner-friendly English. Preserve ALL Markdown formatting, headings, lists, tables, inline code, and fenced code blocks (keep HTML/CSS/JS code syntax intact; only translate comments or visible UI strings inside code if helpful). Return ONLY the translated Markdown without extra commentary."
        val apiKey = try {
            BuildConfig.GEMINI_API_KEY.trim().trim('"', '\'')
        } catch (_: Throwable) { "" }

        if (apiKey.isNotBlank()) {
            for (modelName in DIRECT_GEMINI_MODELS) {
                try {
                    val res = callDirectGeminiApi(
                        modelName = modelName,
                        apiKey = apiKey,
                        systemPrompt = sys,
                        userMessage = markdown,
                        maxOutputTokens = 4096,
                        temperature = 0.2
                    )
                    if (!res.isNullOrBlank()) return@withContext res
                } catch (_: Throwable) {}
            }
        }
        for (fbModel in listOf("gemini-flash-lite-latest", "gemini-3.5-flash-lite", "gemini-3.1-flash-lite", "gemini-3.8-flash")) {
            try {
                val model = Firebase.ai(backend = GenerativeBackend.googleAI()).generativeModel(
                    modelName = fbModel,
                    systemInstruction = content { text(sys) },
                    generationConfig = generationConfig {
                        temperature = 0.2f
                        maxOutputTokens = 4096
                    }
                )
                val reply = model.generateContent(markdown).text
                if (!reply.isNullOrBlank()) return@withContext reply
            } catch (_: Throwable) {}
        }
        null
    }

    private fun callDirectGeminiApi(
        modelName: String,
        apiKey: String,
        systemPrompt: String,
        userMessage: String,
        imageBytes: ByteArray? = null,
        imageMimeType: String = "image/jpeg",
        maxOutputTokens: Int = 800,
        temperature: Double = 0.7
    ): String? {
        val url = URL(directEndpoint(modelName))
        val conn = (url.openConnection() as HttpURLConnection).apply {
            requestMethod = "POST"
            connectTimeout = 10_000
            readTimeout = if (maxOutputTokens > 1000 || imageBytes != null) 45_000 else 25_000
            doOutput = true
            setRequestProperty("Content-Type", "application/json; charset=UTF-8")
            setRequestProperty("x-goog-api-key", apiKey)
        }

        val userParts = JSONArray().apply {
            if (imageBytes != null && imageBytes.isNotEmpty()) {
                val base64Data = Base64.encodeToString(imageBytes, Base64.NO_WRAP)
                put(JSONObject().apply {
                    put("inline_data", JSONObject().apply {
                        put("mime_type", imageMimeType)
                        put("data", base64Data)
                    })
                })
            }
            put(JSONObject().apply { put("text", userMessage) })
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
                    put("parts", userParts)
                })
            })
            put("generationConfig", JSONObject().apply {
                put("temperature", temperature)
                put("maxOutputTokens", maxOutputTokens)
            })
        }

        return try {
            OutputStreamWriter(conn.outputStream, Charsets.UTF_8).use { writer ->
                writer.write(payload.toString())
                writer.flush()
            }

            val code = conn.responseCode
            val stream = if (code in 200..299) conn.inputStream else conn.errorStream
            val body = stream?.let { BufferedReader(InputStreamReader(it, Charsets.UTF_8)).use { r -> r.readText() } } ?: ""

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
        moduleContent: String? = null,
        language: String = "id"
    ): String {
        val q = query.lowercase()
        val isIndonesian = resolveLanguageName(language) == "Bahasa Indonesia"
        val moduleHint = moduleContent
            ?.lineSequence()
            ?.map { it.trim() }
            ?.firstOrNull { it.length > 40 && !it.startsWith("#") && !it.startsWith("```") && !it.startsWith("---") }
            ?.let {
                if (isIndonesian) "\n\n**Ringkasan Modul ($moduleTitle):**\n> $it"
                else "\n\n**Module Summary ($moduleTitle):**\n> $it"
            }
            .orEmpty()

        if (!isIndonesian) {
            return when {
                q.contains("flexbox") || q.contains("flex") -> """
**CSS Flexbox** is a one-dimensional layout model for arranging elements horizontally (row) or vertically (column).

```css
.container {
  display: flex;
  justify-content: center;
  align-items: center;
  gap: 16px;
}
```
*CodeAcademy Tip:* Use `justify-content: space-between;` for clean mobile navbars.
                """.trimIndent()

                q.contains("grid") -> """
**CSS Grid** is ideal for two-dimensional layouts (rows and columns simultaneously).

```css
.gallery {
  display: grid;
  grid-template-columns: repeat(auto-fit, minmax(160px, 1fr));
  gap: 12px;
}
```
Combining `auto-fit` and `minmax()` makes columns automatically responsive without media queries.
                """.trimIndent()

                q.contains("html") || q.contains("tag") || q.contains("element") -> """
In HTML, elements consist of an **Opening Tag**, **Content**, and a **Closing Tag**.

```html
<main>
  <button class="btn-primary">Start Learning</button>
</main>
```
In **$moduleTitle**, prefer semantic tags like `<header>`, `<main>`, `<section>`, and `<footer>` for clean, accessible structure.
                """.trimIndent()

                q.contains("js") || q.contains("javascript") || q.contains("variable") || q.contains("dom") -> """
In modern JavaScript (ES6+), prefer `const` for fixed values, `let` for reassignable values, and `querySelector` for DOM manipulation:

```javascript
const btn = document.querySelector("#action-btn");
let count = 0;

btn.addEventListener("click", () => {
  count += 1;
  btn.textContent = `Clicked ${'$'}{count}x`;
});
```
                """.trimIndent()

                else -> """
*(Offline Mode — Gemini AI connection is currently unavailable)*

For module **$moduleTitle** in **$courseTitle**, try these steps:$moduleHint

1. Test your code snippet in **Playground** and inspect **Web Preview** or **JS Console**.
2. Double-check closing HTML tags, CSS semicolons (`;`), and JavaScript selectors.
3. Once back online, resend your question for a live Gemini AI explanation.
                """.trimIndent()
            }
        }

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
