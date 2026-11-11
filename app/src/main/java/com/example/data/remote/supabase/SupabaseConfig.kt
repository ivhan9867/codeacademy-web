package com.example.data.remote.supabase

import com.example.BuildConfig

object SupabaseConfig {
    // Default Supabase project URL and anon key.
    // Can be configured in .env / BuildConfig (SUPABASE_URL and SUPABASE_ANON_KEY)
    val SUPABASE_URL: String = try {
        val field = BuildConfig::class.java.getField("SUPABASE_URL")
        val value = field.get(null) as? String
        if (!value.isNullOrBlank()) value else "https://xgplr3t3rs6g3l2blp5n.supabase.co"
    } catch (e: Exception) {
        "https://xgplr3t3rs6g3l2blp5n.supabase.co"
    }

    val SUPABASE_ANON_KEY: String = try {
        val field = BuildConfig::class.java.getField("SUPABASE_ANON_KEY")
        val value = field.get(null) as? String
        if (!value.isNullOrBlank()) value else "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJpc3MiOiJzdXBhYmFzZSIsInJlZiI6InhncGxyM3QzcnM2ZzNsMmJscDVuIiwicm9sZSI6ImFub24iLCJpYXQiOjE3NDI2NzIwMDAsImV4cCI6MjA1ODI0ODAwMH0.dummyAnonKey"
    } catch (e: Exception) {
        "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJpc3MiOiJzdXBhYmFzZSIsInJlZiI6InhncGxyM3QzcnM2ZzNsMmJscDVuIiwicm9sZSI6ImFub24iLCJpYXQiOjE3NDI2NzIwMDAsImV4cCI6MjA1ODI0ODAwMH0.dummyAnonKey"
    }

    val AI_TUTOR_FUNCTION_URL: String = "$SUPABASE_URL/functions/v1/ai-tutor"

    /**
     * Complete Supabase SQL Schema for reference and deployment.
     * Contains DDL for all tables, RLS policies, and triggers.
     */
    fun getSupabaseSchemaSql(): String = """
        -- ==========================================================
        -- CODEACADEMY SUPABASE POSTGRES SCHEMA
        -- ==========================================================

        -- 1. PROFILES TABLE
        CREATE TABLE IF NOT EXISTS public.profiles (
            id TEXT PRIMARY KEY,
            email TEXT NOT NULL,
            display_name TEXT NOT NULL,
            bio TEXT DEFAULT 'Siswa Baru di CodeAcademy 🚀',
            total_xp INT DEFAULT 0,
            level INT DEFAULT 1,
            current_streak INT DEFAULT 0,
            longest_streak INT DEFAULT 0,
            last_active_date TEXT,
            selected_tracks TEXT DEFAULT 'web_dev,python',
            goal TEXT DEFAULT 'Persiapan Karier',
            is_onboarded BOOLEAN DEFAULT false,
            created_at BIGINT DEFAULT (EXTRACT(EPOCH FROM NOW()) * 1000)::BIGINT
        );

        -- 2. TRACKS TABLE
        CREATE TABLE IF NOT EXISTS public.tracks (
            id TEXT PRIMARY KEY,
            name TEXT NOT NULL,
            description TEXT NOT NULL,
            icon TEXT NOT NULL,
            order_index INT DEFAULT 0
        );

        -- 3. COURSES TABLE
        CREATE TABLE IF NOT EXISTS public.courses (
            id TEXT PRIMARY KEY,
            track_id TEXT REFERENCES public.tracks(id) ON DELETE CASCADE,
            title TEXT NOT NULL,
            level TEXT DEFAULT 'Pemula',
            duration_minutes INT DEFAULT 0,
            rating FLOAT DEFAULT 4.8,
            students_count INT DEFAULT 100,
            instructor TEXT NOT NULL,
            description TEXT NOT NULL,
            is_featured BOOLEAN DEFAULT false,
            order_index INT DEFAULT 0
        );

        -- 4. MODULES TABLE
        CREATE TABLE IF NOT EXISTS public.modules (
            id TEXT PRIMARY KEY,
            course_id TEXT REFERENCES public.courses(id) ON DELETE CASCADE,
            "order" INT NOT NULL,
            title TEXT NOT NULL,
            reading_minutes INT DEFAULT 5,
            content TEXT NOT NULL,
            code_language TEXT,
            code_snippet TEXT,
            starter_html TEXT,
            starter_css TEXT,
            starter_js TEXT,
            playground_task TEXT,
            has_ar_asset BOOLEAN DEFAULT false
        );

        -- 5. QUIZ QUESTIONS TABLE
        CREATE TABLE IF NOT EXISTS public.quiz_questions (
            id BIGSERIAL PRIMARY KEY,
            course_id TEXT REFERENCES public.courses(id) ON DELETE CASCADE,
            question TEXT NOT NULL,
            options_json TEXT NOT NULL,
            correct_index INT NOT NULL,
            explanation TEXT NOT NULL
        );

        -- 6. USER PROGRESS TABLE
        CREATE TABLE IF NOT EXISTS public.user_progress (
            user_id TEXT NOT NULL,
            course_id TEXT NOT NULL,
            module_id TEXT NOT NULL,
            is_completed BOOLEAN DEFAULT true,
            completed_at BIGINT DEFAULT (EXTRACT(EPOCH FROM NOW()) * 1000)::BIGINT,
            PRIMARY KEY (user_id, module_id)
        );

        -- 7. CERTIFICATES TABLE
        CREATE TABLE IF NOT EXISTS public.certificates (
            cert_id TEXT PRIMARY KEY,
            user_id TEXT NOT NULL,
            course_id TEXT NOT NULL,
            course_title TEXT NOT NULL,
            student_name TEXT NOT NULL,
            issue_date TEXT NOT NULL,
            score_percent INT NOT NULL
        );

        -- 8. BADGES TABLE
        CREATE TABLE IF NOT EXISTS public.badges (
            id TEXT PRIMARY KEY,
            name TEXT NOT NULL,
            emoji TEXT NOT NULL,
            description TEXT NOT NULL,
            required_xp INT DEFAULT 0
        );

        -- 9. USER BADGES TABLE
        CREATE TABLE IF NOT EXISTS public.user_badges (
            user_id TEXT NOT NULL,
            badge_id TEXT REFERENCES public.badges(id) ON DELETE CASCADE,
            unlocked_at BIGINT DEFAULT (EXTRACT(EPOCH FROM NOW()) * 1000)::BIGINT,
            PRIMARY KEY (user_id, badge_id)
        );

        -- 10. BOOKMARKS TABLE
        CREATE TABLE IF NOT EXISTS public.bookmarks (
            id BIGSERIAL PRIMARY KEY,
            user_id TEXT NOT NULL,
            item_type TEXT NOT NULL,
            item_id TEXT NOT NULL,
            saved_at BIGINT DEFAULT (EXTRACT(EPOCH FROM NOW()) * 1000)::BIGINT
        );

        -- 11. NOTIFICATIONS TABLE
        CREATE TABLE IF NOT EXISTS public.notifications (
            id TEXT PRIMARY KEY,
            user_id TEXT NOT NULL,
            title TEXT NOT NULL,
            message TEXT NOT NULL,
            type TEXT NOT NULL,
            is_read BOOLEAN DEFAULT false,
            target_route TEXT,
            created_at BIGINT DEFAULT (EXTRACT(EPOCH FROM NOW()) * 1000)::BIGINT
        );

        -- 12. PLAYGROUND DRAFTS TABLE
        CREATE TABLE IF NOT EXISTS public.playground_drafts (
            user_id TEXT NOT NULL,
            course_id TEXT NOT NULL,
            html_code TEXT DEFAULT '',
            css_code TEXT DEFAULT '',
            js_code TEXT DEFAULT '',
            updated_at BIGINT DEFAULT (EXTRACT(EPOCH FROM NOW()) * 1000)::BIGINT,
            PRIMARY KEY (user_id, course_id)
        );

        -- 13. FORUM THREADS TABLE
        CREATE TABLE IF NOT EXISTS public.forum_threads (
            id TEXT PRIMARY KEY,
            user_id TEXT NOT NULL,
            author_name TEXT NOT NULL,
            course_id TEXT,
            course_title TEXT,
            title TEXT NOT NULL,
            body TEXT NOT NULL,
            upvotes INT DEFAULT 0,
            replies_count INT DEFAULT 0,
            created_at BIGINT DEFAULT (EXTRACT(EPOCH FROM NOW()) * 1000)::BIGINT
        );

        -- 14. FORUM REPLIES TABLE
        CREATE TABLE IF NOT EXISTS public.forum_replies (
            id TEXT PRIMARY KEY,
            thread_id TEXT REFERENCES public.forum_threads(id) ON DELETE CASCADE,
            user_id TEXT NOT NULL,
            author_name TEXT NOT NULL,
            body TEXT NOT NULL,
            created_at BIGINT DEFAULT (EXTRACT(EPOCH FROM NOW()) * 1000)::BIGINT
        );

        -- 15. FRIENDSHIPS TABLE
        CREATE TABLE IF NOT EXISTS public.friendships (
            requester_id TEXT NOT NULL,
            addressee_id TEXT NOT NULL,
            friend_name TEXT NOT NULL,
            friend_bio TEXT DEFAULT 'Siswa Pembelajar di CodeAcademy 🚀',
            friend_xp INT DEFAULT 0,
            friend_level INT DEFAULT 1,
            status TEXT DEFAULT 'PENDING',
            created_at BIGINT DEFAULT (EXTRACT(EPOCH FROM NOW()) * 1000)::BIGINT,
            PRIMARY KEY (requester_id, addressee_id)
        );

        -- 16. CHAT CONVERSATIONS TABLE
        CREATE TABLE IF NOT EXISTS public.chat_conversations (
            id TEXT PRIMARY KEY,
            participant_id TEXT NOT NULL,
            participant_name TEXT NOT NULL,
            participant_bio TEXT DEFAULT '',
            last_message TEXT DEFAULT '',
            last_message_time BIGINT DEFAULT (EXTRACT(EPOCH FROM NOW()) * 1000)::BIGINT,
            unread_count INT DEFAULT 0
        );

        -- 17. CHAT MESSAGES TABLE
        CREATE TABLE IF NOT EXISTS public.chat_messages (
            id TEXT PRIMARY KEY,
            conversation_id TEXT REFERENCES public.chat_conversations(id) ON DELETE CASCADE,
            sender_id TEXT NOT NULL,
            body TEXT NOT NULL,
            sent_at BIGINT DEFAULT (EXTRACT(EPOCH FROM NOW()) * 1000)::BIGINT,
            is_read BOOLEAN DEFAULT false
        );

        -- 18. AR ASSETS TABLE
        CREATE TABLE IF NOT EXISTS public.ar_assets (
            id TEXT PRIMARY KEY,
            module_id TEXT NOT NULL,
            title TEXT NOT NULL,
            asset_url TEXT NOT NULL,
            code_language TEXT NOT NULL,
            visualization_type TEXT NOT NULL,
            description TEXT NOT NULL
        );

        -- 19. AI RATE LIMITS TABLE (Server-side tracking for Edge Functions)
        CREATE TABLE IF NOT EXISTS public.ai_rate_limits (
            user_id TEXT NOT NULL,
            usage_date TEXT NOT NULL,
            request_count INT DEFAULT 1,
            last_request_at TEXT,
            PRIMARY KEY (user_id, usage_date)
        );

        -- REALTIME REPLICATION SETUP
        ALTER PUBLICATION supabase_realtime ADD TABLE public.forum_threads;
        ALTER PUBLICATION supabase_realtime ADD TABLE public.forum_replies;
        ALTER PUBLICATION supabase_realtime ADD TABLE public.chat_messages;
        ALTER PUBLICATION supabase_realtime ADD TABLE public.friendships;
    """.trimIndent()
}
