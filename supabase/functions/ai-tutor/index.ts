import { serve } from "https://deno.land/std@0.168.0/http/server.ts"
import { createClient } from "https://esm.sh/@supabase/supabase-js@2"

const corsHeaders = {
  "Access-Control-Allow-Origin": "*",
  "Access-Control-Allow-Headers": "authorization, x-client-info, apikey, content-type",
  "Access-Control-Allow-Methods": "POST, OPTIONS",
}

const DAILY_REQUEST_LIMIT = 50

function resolveLanguageName(lang?: string): string {
  switch ((lang || "id").toLowerCase()) {
    case "en":
      return "English"
    case "ja":
      return "Japanese (日本語)"
    case "zh":
      return "Chinese (中文)"
    default:
      return "Indonesian (Bahasa Indonesia)"
  }
}

function buildSystemPrompt(language?: string): string {
  const targetLangName = resolveLanguageName(language)
  return `Kamu adalah 'CodeAcademy AI Tutor', seorang tutor koding interaktif yang sabar, ramah, dan solutif.

ATURAN UTAMA BAHASA:
1. Jawab SELALU dan SEPENUHNYA dalam ${targetLangName}.
2. Semua pesan, penjelasan, petunjuk (hint), dan komentar kode harus ditulis dalam ${targetLangName}.

PEDOMAN BANTUAN & PEDAGOGIS:
1. Berikan penjelasan yang terstruktur, singkat, dan jelas.
2. Jangan memberikan solusi kode utuh secara langsung jika pengguna sedang mencoba memecahkan masalah atau latihan; berikan hint terarah dan bimbing pengguna langkah demi langkah.
3. Sertakan contoh potongan kode (code snippet) yang mudah dipahami jika relevan, dengan format Markdown code block.
4. Batasi topik pembahasan pada pemrograman, pengembangan perangkat lunak, dan ilmu komputer (Web Development HTML/CSS/JS, Kotlin, Android, Python, C, C++). Jika pengguna bertanya di luar topik ini, tolak dengan sopan dalam ${targetLangName}.`
}

serve(async (req: Request) => {
  // Handle CORS preflight request
  if (req.method === "OPTIONS") {
    return new Response("ok", { headers: corsHeaders })
  }

  try {
    const supabaseUrl = Deno.env.get("SUPABASE_URL") ?? ""
    const supabaseAnonKey = Deno.env.get("SUPABASE_ANON_KEY") ?? ""
    const supabaseServiceRoleKey = Deno.env.get("SUPABASE_SERVICE_ROLE_KEY") ?? supabaseAnonKey
    const geminiApiKey = Deno.env.get("GEMINI_API_KEY") ?? ""

    // 1. Verify User Authentication via Authorization header
    const authHeader = req.headers.get("Authorization")
    if (!authHeader) {
      return new Response(
        JSON.stringify({
          error: "Akses ditolak: Token autentikasi tidak ditemukan. Silakan login terlebih dahulu.",
        }),
        { status: 401, headers: { ...corsHeaders, "Content-Type": "application/json" } }
      )
    }

    // Supabase client scoped with user's JWT
    const supabaseClient = createClient(supabaseUrl, supabaseAnonKey, {
      global: { headers: { Authorization: authHeader } },
    })

    const {
      data: { user },
      error: userError,
    } = await supabaseClient.auth.getUser()

    if (userError || !user) {
      return new Response(
        JSON.stringify({
          error: "Sesi login tidak valid atau telah kedaluwarsa. Silakan login kembali.",
        }),
        { status: 401, headers: { ...corsHeaders, "Content-Type": "application/json" } }
      )
    }

    const userId = user.id

    // 2. Server-side Rate Limiting (Verified auth.uid(), Max 50 requests/user/day)
    const todayStr = new Date().toISOString().slice(0, 10) // YYYY-MM-DD
    const adminClient = createClient(supabaseUrl, supabaseServiceRoleKey)

    const { data: limitRecord } = await adminClient
      .from("ai_rate_limits")
      .select("request_count")
      .eq("user_id", userId)
      .eq("usage_date", todayStr)
      .maybeSingle()

    const currentCount = limitRecord?.request_count ?? 0

    if (currentCount >= DAILY_REQUEST_LIMIT) {
      return new Response(
        JSON.stringify({
          error: `Batas kuota harian AI Tutor tercapai (${DAILY_REQUEST_LIMIT} request/hari). Silakan kembali besok!`,
          rateLimitExceeded: true,
          limit: DAILY_REQUEST_LIMIT,
          used: currentCount,
        }),
        { status: 429, headers: { ...corsHeaders, "Content-Type": "application/json" } }
      )
    }

    // 3. Parse Request Payload
    const body = await req.json()
    const userMessage: string = body.userMessage || ""
    const codeContext: string | null = body.codeContext || null
    const conversationHistory: Array<{ message: string; isUser: boolean }> = body.conversationHistory || []
    const language: string = body.language || "id"

    if (!userMessage.trim()) {
      return new Response(
        JSON.stringify({ error: "Pesan tidak boleh kosong." }),
        { status: 400, headers: { ...corsHeaders, "Content-Type": "application/json" } }
      )
    }

    // Check GEMINI_API_KEY server-side secret
    if (!geminiApiKey || geminiApiKey === "MY_GEMINI_API_KEY") {
      return new Response(
        JSON.stringify({
          error: "GEMINI_API_KEY belum dikonfigurasi di Supabase Edge Function Secrets.",
        }),
        { status: 503, headers: { ...corsHeaders, "Content-Type": "application/json" } }
      )
    }

    // 4. Forward to Gemini API (gemini-3.5-flash)
    const geminiUrl = `https://generativelanguage.googleapis.com/v1beta/models/gemini-3.5-flash:generateContent?key=${geminiApiKey}`

    const systemPrompt = buildSystemPrompt(language)

    const contents = []

    // History turns (last 6 messages)
    for (const h of conversationHistory.slice(-6)) {
      contents.push({
        role: h.isUser ? "user" : "model",
        parts: [{ text: h.message }],
      })
    }

    // Current user turn
    let currentPrompt = userMessage
    if (codeContext && codeContext.trim().length > 0) {
      currentPrompt += `\n\nBerikut konteks kode saya:\n\`\`\`\n${codeContext}\n\`\`\``
    }

    contents.push({
      role: "user",
      parts: [{ text: currentPrompt }],
    })

    const geminiPayload = {
      contents: contents,
      systemInstruction: {
        parts: [{ text: systemPrompt }],
      },
    }

    const geminiRes = await fetch(geminiUrl, {
      method: "POST",
      headers: { "Content-Type": "application/json" },
      body: JSON.stringify(geminiPayload),
    })

    if (!geminiRes.ok) {
      const errText = await geminiRes.text()
      console.error("Gemini API Error:", errText)
      return new Response(
        JSON.stringify({
          error: "Gagal memproses respons dari Gemini API.",
          details: errText,
        }),
        { status: 502, headers: { ...corsHeaders, "Content-Type": "application/json" } }
      )
    }

    const geminiJson = await geminiRes.json()
    const candidates = geminiJson.candidates || []
    const replyText = candidates[0]?.content?.parts?.[0]?.text || "Tidak ada respons dari tutor."

    // 5. Increment Rate Limit Counter
    const newCount = currentCount + 1
    await adminClient.from("ai_rate_limits").upsert({
      user_id: userId,
      usage_date: todayStr,
      request_count: newCount,
      last_request_at: new Date().toISOString(),
    })

    return new Response(
      JSON.stringify({
        reply: replyText,
        usageToday: newCount,
        remainingRequests: DAILY_REQUEST_LIMIT - newCount,
      }),
      {
        status: 200,
        headers: { ...corsHeaders, "Content-Type": "application/json" },
      }
    )
  } catch (err: any) {
    console.error("Internal Function Error:", err)
    return new Response(
      JSON.stringify({ error: err.message || "Terjadi kesalahan internal pada server." }),
      { status: 500, headers: { ...corsHeaders, "Content-Type": "application/json" } }
    )
  }
})
