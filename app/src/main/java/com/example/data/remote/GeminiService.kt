package com.example.data.remote

import android.util.Log
import com.example.data.remote.supabase.SupabaseConfig
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody
import org.json.JSONArray
import org.json.JSONObject
import java.util.concurrent.TimeUnit

object GeminiService {
    private const val TAG = "GeminiService"

    private val client = OkHttpClient.Builder()
        .connectTimeout(30, TimeUnit.SECONDS)
        .readTimeout(60, TimeUnit.SECONDS)
        .writeTimeout(30, TimeUnit.SECONDS)
        .build()

    /**
     * Ask AI Tutor via Supabase Edge Function (`ai-tutor`).
     * - The Edge Function securely holds the server-side GEMINI_API_KEY secret.
     * - Server verifies auth.uid() from Authorization Bearer token.
     * - Server enforces rate limiting (max 50 requests/user/day).
     * - If offline / connection fails, gracefully falls back to intelligent local tutor response.
     */
    suspend fun askAiTutor(
        userMessage: String,
        codeContext: String? = null,
        conversationHistory: List<Pair<String, Boolean>> = emptyList(), // Pair<Message, isUser>
        authToken: String? = null,
        language: String = "id"
    ): Result<String> = withContext(Dispatchers.IO) {
        try {
            val rootJson = JSONObject().apply {
                put("userMessage", userMessage)
                if (!codeContext.isNullOrBlank()) {
                    put("codeContext", codeContext)
                }
                put("language", language)

                val historyArray = JSONArray()
                conversationHistory.takeLast(6).forEach { (msg, isUser) ->
                    val hObj = JSONObject().apply {
                        put("message", msg)
                        put("isUser", isUser)
                    }
                    historyArray.put(hObj)
                }
                put("conversationHistory", historyArray)
            }

            val requestBody = rootJson.toString().toRequestBody("application/json".toMediaType())
            val bearerToken = if (!authToken.isNullOrBlank()) authToken else SupabaseConfig.SUPABASE_ANON_KEY

            val request = Request.Builder()
                .url(SupabaseConfig.AI_TUTOR_FUNCTION_URL)
                .header("Content-Type", "application/json")
                .header("apikey", SupabaseConfig.SUPABASE_ANON_KEY)
                .header("Authorization", "Bearer $bearerToken")
                .post(requestBody)
                .build()

            val response = client.newCall(request).execute()
            val responseBody = response.body?.string()

            if (response.isSuccessful && !responseBody.isNullOrBlank()) {
                val jsonRes = JSONObject(responseBody)
                val reply = jsonRes.optString("reply", "")
                if (reply.isNotBlank()) {
                    return@withContext Result.success(reply)
                }
            } else if (response.code == 429) {
                // Rate limit reached (50 requests/day enforced server-side)
                val errMsg = try {
                    if (!responseBody.isNullOrBlank()) {
                        JSONObject(responseBody).optString("error", "")
                    } else ""
                } catch (e: Exception) {
                    ""
                }
                val finalMsg = if (errMsg.isNotBlank()) {
                    "⏳ **Batas Kuota Harian AI Tutor**\n\n$errMsg"
                } else {
                    "⏳ **Batas Kuota Harian AI Tutor**\n\nKamu telah mencapai batas maksimal 50 pertanyaan/hari untuk akunmu. Kuota akan diperbarui kembali besok!"
                }
                return@withContext Result.success(finalMsg)
            } else {
                Log.w(TAG, "Edge Function non-success code: ${response.code}, body: $responseBody")
            }

            // Fallback to smart offline response if Edge function response was unparseable or error
            Result.success(getSmartOfflineTutorResponse(userMessage, codeContext))
        } catch (e: Exception) {
            Log.e(TAG, "Network error calling Supabase AI Tutor Edge Function, using smart offline fallback", e)
            // Graceful fallback to rich offline response when offline / network down
            Result.success(getSmartOfflineTutorResponse(userMessage, codeContext))
        }
    }

    /**
     * Smart local fallback responses for offline mode or network interruption.
     */
    fun getSmartOfflineTutorResponse(message: String, code: String?): String {
        val lower = message.lowercase()
        return when {
            lower.contains("flexbox") || lower.contains("center") || lower.contains("tengah") -> {
                "💡 **Penjelasan CSS Flexbox (Centering)**:\nUntuk menengahkan elemen secara horizontal dan vertikal:\n```css\n.container {\n  display: flex;\n  justify-content: center; /* Sumbu horizontal */\n  align-items: center;     /* Sumbu vertikal */\n  min-height: 100vh;\n}\n```\nDengan `gap: 16px;`, kamu bisa memberi jarak seragam antar anak elemen tanpa perlu margin!"
            }
            lower.contains("pointer") || lower.contains("alamat") || lower.contains("memori") -> {
                "⚡ **Konsep Pointer di C**:\nPointer adalah variabel yang menyimpan **alamat memori** variabel lain.\n- Operator `&` : Mengambil alamat memori.\n- Operator `*` : Melakukan dereference (mengakses isi di alamat tersebut).\n\n```c\nint angka = 42;\nint *ptr = &angka; // ptr menampung alamat angka\n*ptr = 100;        // nilai angka sekarang berubah jadi 100!\n```\nLihat juga simulasi skematik di fitur **Visualisasi Interaktif** untuk melihat peta memorinya secara visual!"
            }
            lower.contains("list comprehension") || lower.contains("python") -> {
                "🐍 **Python List Comprehension**:\nFormat ringkas untuk membuat list baru dalam 1 baris:\n```python\n# Mengambil angka genap dan mengkuadratkannya\nangka = [1, 2, 3, 4, 5, 6]\nkuadrat = [x**2 for x in angka if x % 2 == 0]\n# Hasil: [4, 16, 36]\n```\nSangat efisien dan ramah performa (Pythonic style)!"
            }
            lower.contains("debug") || lower.contains("error") || lower.contains("salah") -> {
                "🛠️ **Analisis Debugging CodeAcademy**:\n1. Pastikan tanda kurung kurawal `{}` dan titik koma `;` tertutup rapi.\n2. Cek apakah ada variabel yang belum dideklarasikan (`undefined variable`).\n3. Gunakan `console.log()` di JavaScript atau `printf()` di C / `print()` di Python untuk memantau alur data di setiap baris."
            }
            else -> {
                "🤖 **CodeAcademy AI Tutor**:\nHalo! Pertanyaan yang bagus mengenai \"$message\".\n\nTips terbaik: Mulailah dengan membagi masalah besar menjadi fungsi-fungsi kecil (Modular Programming). Ujicobakan langsung kodenya di tab **Playground** dan periksa outputnya secara real-time!\n\nJika ada potongan kode tertentu yang ingin diperiksa, kirimkan kodenya dan saya siap membantu mendebug!"
            }
        }
    }
}
