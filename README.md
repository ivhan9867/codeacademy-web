# CodeAcademy — Mobile Web Development Learning App

Aplikasi Android native berbasis **Kotlin & Jetpack Compose** untuk belajar Front-End Web Development (HTML, CSS, dan JavaScript) secara terstruktur, interaktif, dan *offline-first*.

---

## Ringkasan Fitur

- **Kurikulum Front-End Terstruktur (156 Modul Markdown)**
  - `fe-web-foundation-00` — Fondasi Web: Dari Nol ke Baris Kode Pertama (106 modul)
  - `fe-css-02` — CSS Layout Modern: Flexbox & Grid (24 modul)
  - `fe-js-03` — JavaScript Fundamentals (26 modul)
  - Dilengkapi 9 kuis per bagian (*Section Quiz*), 3 ujian akhir, 3 proyek akhir beserta rubrik penilaian, dan *Cheat Sheet* per kelas.
- **Interactive Code Playground**
  - Editor kode mobile dengan nomor baris, dukungan HTML/CSS/JavaScript, live preview via `WebView`, konsol JavaScript *headless*, serta verifikasi aturan kode otomatis langsung dari modul belajar.
- **AI Tutor "Codi" (Multimodal & Context-Aware)**
  - Memahami konteks kelas, modul yang sedang dibaca, dan draf kode Playground pengguna.
  - Mendukung lampiran gambar/screenshot kode atau error (kompresi otomatis di background + *full-screen pinch-to-zoom preview*).
  - Arsitektur *dual-engine*: Direct Gemini REST API (`gemini-flash-lite-latest` → `gemini-3.5-flash-lite` → `gemini-3.1-flash-lite`) dengan *auto-retry* dan fallback ke Firebase AI SDK serta respons offline lokal.
- **Lokalisasi Menyeluruh (ID / EN / JA / ZH)**
  - Antarmuka mendukung 4 bahasa dengan paritas 1.186 string resource yang dijaga oleh unit test.
  - Katalog kursus dua bahasa (`front-end.json` dan `front-end_en.json`) dengan *hot-switch* tanpa restart, ditambah terjemahan Markdown modul on-demand yang disimpan di cache lokal perangkat.
- **Gamifikasi, Komunitas & Sertifikat**
  - Sistem XP, 10 level, *streak* harian, misi harian, *Daily 1-Minute Code Challenge*, 12 lencana, dan sertifikat kelulusan yang dapat diverifikasi.
  - Forum diskusi per kelas, papan peringkat (*Leaderboard*), dan profil publik yang dapat dikustomisasi (tema banner, avatar, frame, *headline*, *skill tags*, dan badge staf/fasilitator).

---

## Tech Stack

| Layer | Teknologi |
| --- | --- |
| Bahasa & Build | Kotlin `2.2.10`, Android Gradle Plugin `9.1.1`, KSP `2.3.6`, Java 11 |
| UI & Navigasi | Jetpack Compose (BOM `2024.09.00`), Material 3, Navigation Compose `2.8.9`, Coil `2.7.0` |
| Penyimpanan Lokal | Room `2.7.0` (*offline-first*, migrasi hingga v16), DataStore Preferences `1.1.7` |
| Backend & Auth | Firebase BoM `34.17.0` (Auth, Credential Manager Google Sign-In, Firestore, App Check, Firebase AI), Retrofit + OkHttp + Moshi |
| Pengujian | JUnit 4, Robolectric `4.16.1`, Roborazzi `1.59.0`, Coroutines Test |
| Target SDK | `minSdk 24` · `targetSdk 36` · `compileSdk 36.1` |

---

## Struktur Proyek

```text
app/src/main/
├── assets/
│   ├── catalog/
│   │   ├── front-end.json          # Katalog kursus, kuis, & cheat sheet (ID)
│   │   └── front-end_en.json       # Katalog kursus, kuis, proyek akhir & cheat sheet (EN)
│   └── content/
│       ├── fe-web-foundation-00/   # 106 modul Markdown
│       ├── fe-css-02/              # 24 modul Markdown
│       └── fe-js-03/               # 26 modul Markdown
├── java/com/example/
│   ├── ai/                         # GeminiTutorService (REST + Firebase AI + Vision + Translate)
│   ├── data/
│   │   ├── local/                  # Room entities, DAO, migrasi DB, SessionManager
│   │   ├── models/                 # Model domain (Course, Module, Quiz, ChatMessage, dll.)
│   │   ├── remote/                 # Sinkronisasi Firestore & client remote
│   │   └── util/                   # CourseUnlockUtils, CourseSectionUtils, verifikasi kode
│   ├── ui/
│   │   ├── components/             # MarkdownView, CodeBlockView, CodeSandboxWebView, Mascot
│   │   ├── screens/                # Home, Courses, Materi, Playground, AiTutor, Forum, Profile, dll.
│   │   └── theme/                  # Material 3 light/dark theme & tipografi
│   └── util/localization/          # AppLanguage, DataLabelMappings, helper lokalisasi
└── res/
    ├── values/strings.xml          # Bahasa Indonesia (default)
    ├── values-en/strings.xml       # English
    ├── values-ja/strings.xml       # 日本語
    └── values-zh/strings.xml       # 中文
```

---

## Cara Menjalankan Secara Lokal

### Prasyarat
- **Android Studio** (Meerkat / Narwhal atau lebih baru) dengan JDK 17+ untuk Gradle daemon.
- Android SDK 36 terpasang.
- API key **Google Gemini** dari [Google AI Studio](https://aistudio.google.com/apikey).

### Langkah Setup

1. **Clone repository**
   ```bash
   git clone https://github.com/ivhan9867/PRJ-MobileCodeAcademy.git
   cd PRJ-MobileCodeAcademy
   ```

2. **Konfigurasi environment**
   Salin `.env.example` menjadi `.env` di root proyek, lalu isi API key Gemini Anda:
   ```dotenv
   GEMINI_API_KEY=your_gemini_api_key_here
   ```
   > File `.env` dibaca otomatis saat build oleh `secrets-gradle-plugin` ke `BuildConfig.GEMINI_API_KEY` dan diabaikan oleh `.gitignore`.

3. **Konfigurasi Firebase (opsional untuk fitur Cloud Sync & Google Sign-In)**
   Pastikan `app/google-services.json` sudah sesuai dengan project Firebase Anda. Tanpa konfigurasi cloud, mode lokal (*offline-first* Room) dan AI Tutor via `.env` tetap berjalan normal.

4. **Build & Jalankan**
   - Buka folder proyek di Android Studio, tunggu Gradle sync selesai, lalu jalankan konfigurasi `app` ke emulator atau perangkat Android (API 24+).
   - Atau lewat terminal:
     ```bash
     ./gradlew assembleDebug
     ```

---

## Pengujian (Unit Tests)

Beberapa suite unit test utama yang dapat dijalankan langsung lewat Gradle:

```bash
# Cek paritas 4 bahasa & format placeholder strings.xml
./gradlew testDebugUnitTest --tests "com.example.StringParityAndFormatTest"

# Cek parser katalog kursus & lokalisasi
./gradlew testDebugUnitTest --tests "com.example.CourseCatalogParserTest" --tests "com.example.DataLabelMappingsTest"

# Cek prompt AI Tutor & aturan bebas hardcoded string
./gradlew testDebugUnitTest --tests "com.example.GeminiTutorPromptTest" --tests "com.example.ActiveTreeHardcodedStringCheckTest"
```

---

## Catatan Keamanan

- Jangan pernah meng-commit `.env`, `local.properties`, keystore release (`*.jks`), atau kredensial pribadi ke repository publik.
- Untuk build rilis (`assembleRelease`), konfigurasikan environment variable `KEYSTORE_PATH`, `STORE_PASSWORD`, `KEY_PASSWORD`, dan `KEY_ALIAS` sesuai konfigurasi di `app/build.gradle.kts`.
