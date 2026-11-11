package com.example.ui.components

import android.annotation.SuppressLint
import android.os.Handler
import android.os.Looper
import android.webkit.JavascriptInterface
import android.webkit.WebChromeClient
import android.webkit.WebResourceError
import android.webkit.WebResourceRequest
import android.webkit.WebSettings
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.unit.dp
import androidx.compose.foundation.layout.size
import androidx.compose.ui.viewinterop.AndroidView
import org.json.JSONObject

class PythonBridge(
    private val mainHandler: Handler,
    private val onStdout: (String) -> Unit,
    private val onStderr: (String) -> Unit,
    private val onStatus: (String) -> Unit,
    private val onFinished: (Boolean, Long) -> Unit
) {
    @JavascriptInterface
    fun onStdout(msg: String) {
        mainHandler.post { onStdout(msg) }
    }

    @JavascriptInterface
    fun onStderr(msg: String) {
        mainHandler.post { onStderr(msg) }
    }

    @JavascriptInterface
    fun onStatus(status: String) {
        mainHandler.post { onStatus(status) }
    }

    @JavascriptInterface
    fun onFinished(success: Boolean, timeMs: Long) {
        mainHandler.post { onFinished(success, timeMs) }
    }
}

/**
 * Headless WebView sandbox that runs Python code via pinned Pyodide v0.26.4.
 * Uses cooperative watchdog with sys.settrace to limit execution time to 5 seconds
 * and prevent infinite loops (like while True) from hanging the application.
 */
@SuppressLint("SetJavaScriptEnabled")
@Composable
fun PythonSandboxWebView(
    runTrigger: Int,
    codeToRun: String,
    reloadTrigger: Int,
    onStdout: (String) -> Unit,
    onStderr: (String) -> Unit,
    onStatus: (String) -> Unit,
    onFinished: (Boolean, Long) -> Unit,
    modifier: Modifier = Modifier
) {
    val mainHandler = remember { Handler(Looper.getMainLooper()) }
    var webViewRef = remember { mutableMapOf<String, WebView?>() }

    val pyodideHtml = remember {
        """
        <!DOCTYPE html>
        <html>
        <head>
          <meta charset="utf-8">
          <script src="https://cdn.jsdelivr.net/pyodide/v0.26.4/full/pyodide.js"></script>
          <script>
            let pyodide = null;
            let isReady = false;

            async function initPyodide() {
              try {
                if (window.AndroidBridge) window.AndroidBridge.onStatus("LOADING");
                pyodide = await loadPyodide({
                  indexURL: "https://cdn.jsdelivr.net/pyodide/v0.26.4/full/"
                });
                isReady = true;
                if (window.AndroidBridge) window.AndroidBridge.onStatus("READY");
              } catch (err) {
                const msg = err ? (err.message || String(err)) : "Gagal memuat engine Python";
                if (window.AndroidBridge) window.AndroidBridge.onStatus("LOAD_ERROR:" + msg);
              }
            }

            async function runCode(codeStr) {
              if (!isReady || !pyodide) {
                if (window.AndroidBridge) {
                  window.AndroidBridge.onStderr("Engine Pyodide belum siap. Mohon tunggu proses inisialisasi selesai.\n");
                  window.AndroidBridge.onFinished(false, 0);
                }
                return;
              }

              const t0 = Date.now();
              try {
                pyodide.setStdout({
                  batched: (msg) => {
                    if (window.AndroidBridge) window.AndroidBridge.onStdout(msg);
                  }
                });
                pyodide.setStderr({
                  batched: (msg) => {
                    if (window.AndroidBridge) window.AndroidBridge.onStderr(msg);
                  }
                });

                pyodide.globals.set("__user_code__", codeStr);

                const runnerScript = `
import sys, time, traceback

class _WatchdogTimeout(Exception):
    pass

_t0 = time.time()
def _trace_fn(frame, event, arg):
    if time.time() - _t0 > 5.0:
        raise _WatchdogTimeout("Batas waktu eksekusi 5 detik terlampaui!")
    return _trace_fn

sys.settrace(_trace_fn)
try:
    exec(__user_code__, {'__name__': '__main__'})
except _WatchdogTimeout as e:
    print("\\n⚠️ Batas Waktu Terlampaui (Timeout):", file=sys.stderr)
    print("Program dihentikan karena berjalan lebih dari 5 detik. Periksa apakah terdapat perulangan tanpa henti (infinite loop seperti while True tanpa break).", file=sys.stderr)
    raise
except Exception as e:
    tb = traceback.format_exc()
    err_cls = type(e).__name__
    tips = {
        'SyntaxError': 'Periksa titik dua (:), pasangan tanda kurung, tanda petik string, atau salah ketik kata kunci perintah.',
        'IndentationError': 'Lekukan blok kode (indentasi) tidak konsisten. Gunakan 4 spasi konsisten untuk setiap blok.',
        'NameError': 'Variabel atau fungsi belum didefinisikan atau ada salah ketik (typo).',
        'TypeError': 'Operasi dilakukan pada tipe data yang tidak sesuai.',
        'IndexError': 'Indeks list di luar jangkauan (melebihi jumlah item koleksi).',
        'KeyError': 'Kunci (key) tidak ditemukan dalam dictionary.',
        'ZeroDivisionError': 'Pembagian dengan angka nol tidak diperbolehkan dalam matematika maupun Python.',
        'ValueError': 'Nilai yang diberikan tidak valid untuk fungsi atau operasi tersebut.'
    }.get(err_cls, 'Periksa kembali alur logika dan variabel kode kamu.')

    print(f"\\nTraceback (most recent call last):\\n{tb.strip()}", file=sys.stderr)
    print(f"💡 Tips Solusi ({err_cls}): {tips}", file=sys.stderr)
    raise
finally:
    sys.settrace(None)
`;
                await pyodide.runPythonAsync(runnerScript);
                const elapsed = Date.now() - t0;
                if (window.AndroidBridge) window.AndroidBridge.onFinished(true, elapsed);
              } catch (err) {
                const elapsed = Date.now() - t0;
                if (window.AndroidBridge) window.AndroidBridge.onFinished(false, elapsed);
              }
            }

            window.addEventListener("DOMContentLoaded", () => {
              initPyodide();
            });
          </script>
        </head>
        <body style="background:#0D1117;"></body>
        </html>
        """.trimIndent()
    }

    LaunchedEffect(reloadTrigger) {
        if (reloadTrigger > 0) {
            val wv = webViewRef["instance"]
            onStatus("LOADING")
            wv?.loadDataWithBaseURL("https://pyodide-runner.local/", pyodideHtml, "text/html", "UTF-8", null)
        }
    }

    LaunchedEffect(runTrigger) {
        if (runTrigger > 0 && codeToRun.isNotBlank()) {
            val wv = webViewRef["instance"]
            val quoted = JSONObject.quote(codeToRun)
            wv?.evaluateJavascript("runCode($quoted);", null)
        }
    }

    AndroidView(
        factory = { ctx ->
            WebView(ctx).apply {
                settings.javaScriptEnabled = true
                settings.domStorageEnabled = true
                settings.cacheMode = WebSettings.LOAD_DEFAULT
                settings.allowFileAccess = true

                val bridge = PythonBridge(
                    mainHandler = mainHandler,
                    onStdout = onStdout,
                    onStderr = onStderr,
                    onStatus = onStatus,
                    onFinished = onFinished
                )
                addJavascriptInterface(bridge, "AndroidBridge")

                webViewClient = object : WebViewClient() {
                    override fun onReceivedError(view: WebView?, request: WebResourceRequest?, error: WebResourceError?) {
                        super.onReceivedError(view, request, error)
                        if (request?.isForMainFrame == true || request?.url?.toString()?.contains("pyodide") == true) {
                            mainHandler.post {
                                onStatus("LOAD_ERROR: Gagal memuat Pyodide dari CDN. Pastikan perangkat terhubung ke internet.")
                            }
                        }
                    }
                }

                webChromeClient = WebChromeClient()

                loadDataWithBaseURL("https://pyodide-runner.local/", pyodideHtml, "text/html", "UTF-8", null)
                webViewRef["instance"] = this
            }
        },
        modifier = modifier
            .size(1.dp)
            .alpha(0.01f)
    )

    DisposableEffect(Unit) {
        onDispose {
            webViewRef["instance"]?.destroy()
            webViewRef["instance"] = null
        }
    }
}
