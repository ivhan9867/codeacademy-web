# Supabase Edge Function: `ai-tutor`

Edge Function ini bertindak sebagai perantara aman (*secure server-side proxy*) antara aplikasi CodeAcademy dan Google Gemini API.

## Fitur Utama

1. **Server-Side Secret Management**:
   - `GEMINI_API_KEY` disimpan secara aman di Supabase Secrets (bukan di client/APK).
2. **Server-Side Authentication Verification**:
   - Memverifikasi token JWT dari `Authorization: Bearer <token>` menggunakan `supabase.auth.getUser()`.
   - Mengidentifikasi `auth.uid()` yang terverifikasi di server.
3. **Server-Side Rate Limiting**:
   - Membatasi penggunaan AI Tutor maksimal **50 request per user per hari**.
   - Melacak jumlah pemanggilan harian di tabel `public.ai_rate_limits`.
   - Mengembalikan HTTP 429 (*Too Many Requests*) saat kuota harian habis.
4. **Resilient Client Fallback**:
   - Client Android (`GeminiService.kt`) memiliki fallback offline (`getSmartOfflineTutorResponse`) ketika terjadi gangguan koneksi jaringan.

## Cara Deploy ke Supabase

1. Pasang secret `GEMINI_API_KEY`:
```bash
supabase secrets set GEMINI_API_KEY="YOUR_GEMINI_API_KEY"
```

2. Deploy function:
```bash
supabase functions deploy ai-tutor --no-verify-jwt
```
*(Catatan: `--no-verify-jwt` memungkinkan fungsi memproses dan memvalidasi token JWT secara kustom di dalam kode handler Deno).*

3. Pastikan tabel `ai_rate_limits` telah dibuat di Postgres Database:
```sql
CREATE TABLE IF NOT EXISTS public.ai_rate_limits (
    user_id TEXT NOT NULL,
    usage_date TEXT NOT NULL,
    request_count INT DEFAULT 1,
    last_request_at TEXT,
    PRIMARY KEY (user_id, usage_date)
);

ALTER TABLE public.ai_rate_limits ENABLE ROW LEVEL SECURITY;
CREATE POLICY "Users can read own rate limits" ON public.ai_rate_limits FOR SELECT USING (auth.uid()::text = user_id);
```
