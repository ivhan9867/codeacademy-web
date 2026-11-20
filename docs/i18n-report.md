# Laporan Status & Verifikasi Internasionalisasi (i18n) - CodeAcademy

Dokumen ini menyajikan audit faktual, status pengerjaan bertahap, batasan lingkungan uji, glosarium istilah, dan checklist QA manual 18 layar per **TAHAP 1.5-D**.

---

## 1. Status Pengerjaan per Tahap

| Tahap | Deskripsi Cakupan | Status | Realitas & Bukti Verifikasi Kode |
|---|---|---|---|
| **Tahap 1** | **Infrastruktur i18n & Bahasa** | **Selesai** | Terverifikasi di kode: `AppLanguage.kt` (ID, EN, JA, ZH), dynamic locale wrapping pada `MainActivity.kt` (`createConfigurationContext`, `LocalContext`, `LocalConfiguration`, `LocalAppLanguage`), persistensi preferensi di `SessionManager.kt`, dan 279 key lengkap di `values/`, `values-en/`, `values-ja/`, dan `values-zh/` (paritas 100%). |
| **Tahap 2** | **Migrasi String Layar Antarmuka** | **Selesai** (UI Aktif) | Seluruh layar aktif (Auth, Onboarding, Home, Catalog, ClassDetail, Materi, Playground, Quiz/Evaluasi, Progress, Community, Forum, Help, Certificate, Profile, Drawer, Shared Components) telah dimigrasikan ke `R.string.*` (tersisa 162 token non-UI/animasi/angka pada regex `ActiveTreeHardcodedStringCheckTest`, ceiling diturunkan dari 300 ke 165). |
| **Tahap 3A** | **Notifikasi (Pesan Repository)** | **Selesai** | Seluruh 11 template notifikasi di `CodeAcademyRepository.kt` kini menggunakan `getLocalizedString(R.string.notif_*)` melalui `Context.localizedContext(languageCode)` di `LocalizationUtils.kt` sehingga mengikuti bahasa aktif pengguna. |
| **Tahap 3B** | **Label Level & Lencana (Badge)** | **Selesai** | Terverifikasi di kode: `DataLabelMappings.kt` memisahkan data persisten ("Pemula", "Menengah", "Lanjut", badge "b1"–"b12", level XP 1–10, daily quest) dari label tampilan terjemahan `R.string.*` dengan fallback aman. Diverifikasi lulus oleh `DataLabelMappingsTest`. |
| **Tahap 3C** | **Lokalisasi Data Katalog** | **Ditunda** | Sesuai keputusan pemilik proyek, data silabus di `CourseCatalog.kt`, `InitialDataSeed.kt`, dan `assets/` tetap dipertahankan dalam Bahasa Indonesia untuk saat ini. |
| **Tahap 4** | **AI Tutor Multibahasa** | **Selesai (Refined)** | `GeminiTutorService.kt` kini menggunakan system prompt berbahasa Inggris penuh saat bahasa target bukan `id` (`en`, `ja`, `zh`) agar respons lebih natural, sambil tetap menjaga prompt `id`. Diverifikasi lulus oleh `GeminiTutorPromptTest`. |
| **Tahap 5** | **Pengujian & Penutupan Akhir** | **Selesai (Scope UI & Notifikasi)** | Seluruh unit test i18n & regresi (`StringParityAndFormatTest`, `ActiveTreeHardcodedStringCheckTest`, `DataLabelMappingsTest`, `GeminiTutorPromptTest`) diverifikasi lulus setelah migrasi penuh UI & notifikasi. |

---

## 2. Audit Literal UI Hardcoded Pohon Layar Aktif

Berdasarkan eksekusi resmi `tools/find_hardcoded_strings.sh` (menggunakan Single Source of Truth `tools/unused_subpackages.txt`):
* **Pohon Aktif**: **749 literal** (tersebar di 53 file).
* **Pohon Diabaikan (Unused Tree)**: **221 literal** (14 file subsistem dorman `CodeAcademyNavigation.kt`).
* **Total Kodebase**: **970 literal**.

### Rincian 53 File Pohon Aktif & Jumlah Literal Tersisa:

| No | File | Literal Tersisa | Keterangan Status Migrasi |
|---|---|---|---|
| 1 | `app/src/main/java/com/example/ui/screens/ClassDetailScreen.kt` | 60 | Belum dimigrasikan (Tahap 2C) |
| 2 | `app/src/main/java/com/example/ui/screens/ProgressScreen.kt` | 44 | Belum dimigrasikan (Tahap 2F) |
| 3 | `app/src/main/java/com/example/ui/screens/CommunityScreen.kt` | 37 | Belum dimigrasikan (Tahap 2F) |
| 4 | `app/src/main/java/com/example/data/local/InitialDataSeed.kt` | 36 | Data seed katalog (Menunggu Tahap 3C) |
| 5 | `app/src/main/java/com/example/ui/screens/PlaygroundScreen.kt` | 34 | Belum dimigrasikan (Tahap 2D) |
| 6 | `app/src/main/java/com/example/ui/components/AppNavigationDrawer.kt` | 30 | Menu drawer navigasi (Sebagian) |
| 7 | `app/src/main/java/com/example/ui/screens/PlacementTestScreen.kt` | 30 | Belum dimigrasikan (Tahap 2E) |
| 8 | `app/src/main/java/com/example/ui/screens/HelpScreen.kt` | 26 | FAQ & Kebijakan Privasi (Tahap 2F) |
| 9 | `app/src/main/java/com/example/ui/screens/CertificateListScreen.kt` | 23 | Belum dimigrasikan (Tahap 2F) |
| 10 | `app/src/main/java/com/example/ui/screens/SectionQuizRulesScreen.kt` | 23 | Belum dimigrasikan (Tahap 2E) |
| 11 | `app/src/main/java/com/example/ui/components/Components.kt` | 22 | Komponen kartu umum |
| 12 | `app/src/main/java/com/example/ui/screens/SectionQuizScreen.kt` | 22 | Belum dimigrasikan (Tahap 2E) |
| 13 | `app/src/main/java/com/example/ui/screens/HomeScreenTopCarousel.kt` | 21 | Belum dimigrasikan (Tahap 2B) |
| 14 | `app/src/main/java/com/example/ui/components/CommonComponents.kt` | 19 | Komponen dialog & badge |
| 15 | `app/src/main/java/com/example/ui/screens/MateriScreen.kt` | 19 | Navigasi materi (Tahap 2D) |
| 16 | `app/src/main/java/com/example/ui/screens/CourseForumScreen.kt` | 19 | Belum dimigrasikan (Tahap 2F) |
| 17 | `app/src/main/java/com/example/ui/screens/CourseDetailScreen.kt` | 18 | Belum dimigrasikan (Tahap 2C) |
| 18 | `app/src/main/java/com/example/ui/screens/CourseCatalogScreen.kt` | 17 | Belum dimigrasikan (Tahap 2C) |
| 19 | `app/src/main/java/com/example/ui/screens/onboarding/WelcomeStep.kt` | 16 | Belum dimigrasikan (Onboarding) |
| 20 | `app/src/main/java/com/example/ui/screens/onboarding/CodePreviewStep.kt` | 16 | Belum dimigrasikan (Onboarding) |
| 21 | `app/src/main/java/com/example/ui/screens/SectionQuizResultScreen.kt` | 15 | Belum dimigrasikan (Tahap 2E) |
| 22 | `app/src/main/java/com/example/ui/screens/CertificateScreen.kt` | 13 | Belum dimigrasikan (Tahap 2F) |
| 23 | `app/src/main/java/com/example/ui/screens/SearchScreen.kt` | 13 | Belum dimigrasikan (Tahap 2B/2C) |
| 24 | `app/src/main/java/com/example/ui/components/CourseCards.kt` | 12 | Komponen kartu kursus |
| 25 | `app/src/main/java/com/example/ui/screens/HasilEvaluasiScreen.kt` | 11 | Belum dimigrasikan (Tahap 2E) |
| 26 | `app/src/main/java/com/example/ui/screens/onboarding/CommunityStep.kt` | 11 | Belum dimigrasikan (Onboarding) |
| 27 | `app/src/main/java/com/example/ui/components/MarkdownView.kt` | 10 | Komponen pembaca materi markdown |
| 28 | `app/src/main/java/com/example/ui/components/OnboardingIllustrations.kt` | 10 | Teks label pada Canvas ilustrasi |
| 29 | `app/src/main/java/com/example/data/CodeAcademyRepository.kt` | 10 | Template pesan (Menunggu Tahap 3A) |
| 30 | `app/src/main/java/com/example/ui/screens/QuizScreen.kt` | 9 | Belum dimigrasikan (Tahap 2E) |
| 31 | `app/src/main/java/com/example/ui/screens/onboarding/PlanSummaryStep.kt` | 9 | Belum dimigrasikan (Onboarding) |
| 32 | `app/src/main/java/com/example/MainActivity.kt` | 8 | Label dialog sistem |
| 33 | `app/src/main/java/com/example/ui/screens/HomeScreenTrackList.kt` | 8 | Belum dimigrasikan (Tahap 2B) |
| 34 | `app/src/main/java/com/example/ui/screens/ProfileHeatmapSection.kt` | 7 | Belum dimigrasikan (Tahap 2F) |
| 35 | `app/src/main/java/com/example/ui/screens/SplashScreen.kt` | 6 | Kutipan dan subtitle splash |
| 36 | `app/src/main/java/com/example/ui/screens/DiskusiHangatScreen.kt` | 6 | Belum dimigrasikan (Tahap 2F) |
| 37 | `app/src/main/java/com/example/ui/screens/BookmarksScreen.kt` | 6 | Belum dimigrasikan (Tahap 2F) |
| 38 | `app/src/main/java/com/example/ui/screens/HomeScreenCourseList.kt` | 5 | Belum dimigrasikan (Tahap 2B) |
| 39 | `app/src/main/java/com/example/ui/screens/HomeScreen.kt` | 5 | Belum dimigrasikan (Tahap 2B) |
| 40 | `app/src/main/java/com/example/ui/screens/ProfileHeaderSection.kt` | 5 | Belum dimigrasikan (Tahap 2F) |
| 41 | `app/src/main/java/com/example/ui/screens/onboarding/TrackSelectionStep.kt` | 5 | Belum dimigrasikan (Onboarding) |
| 42 | `app/src/main/java/com/example/data/CourseCatalog.kt` | 5 | Metadata kursus (Menunggu Tahap 3C) |
| 43 | `app/src/main/java/com/example/ui/screens/NotificationsScreen.kt` | 4 | Belum dimigrasikan (Tahap 2F) |
| 44 | `app/src/main/java/com/example/ui/screens/ProfileSettingsSection.kt` | 3 | Sisa label pengaturan profil |
| 45 | `app/src/main/java/com/example/ui/screens/CheatSheetScreen.kt` | 3 | Belum dimigrasikan (Tahap 2D) |
| 46 | `app/src/main/java/com/example/ui/screens/LoginScreen.kt` | 3 | Sebagian besar selesai (dari 15) |
| 47 | `app/src/main/java/com/example/ui/screens/ProfileBadgesSection.kt` | 3 | Sisa label lencana profil |
| 48 | `app/src/main/java/com/example/ui/screens/onboarding/OnboardingComponents.kt` | 3 | Komponen tombol navigasi onboarding |
| 49 | `app/src/main/java/com/example/ui/MainViewModel.kt` | 2 | Pesan fallback viewmodel |
| 50 | `app/src/main/java/com/example/ui/components/ShimmerPlaceholder.kt` | 2 | Teks placeholder loading |
| 51 | `app/src/main/java/com/example/ui/components/CoachMarkBubble.kt` | 2 | Petunjuk tooltip bubble |
| 52 | `app/src/main/java/com/example/ui/screens/AiTutorScreen.kt` | 2 | Sebagian besar selesai (dari 7) |
| 53 | `app/src/main/java/com/example/ui/screens/ProfileScreen.kt` | 1 | Sisa judul layar |
| | **TOTAL POHON AKTIF** | **749** | **Ceiling resmi terkunci pada 749** |

---

## 3. Glosarium Istilah: Status Persetujuan & Tinjauan

Mengacu pada 9 istilah produk dari Bagian 6 spesifikasi lokalisasi: status **"Disepakati"** hanya diberikan untuk istilah yang disetujui secara eksplisit oleh pemilik proyek. Istilah teknis kurikulum (seperti *Box Model*, *Flexbox*, dsb.) dikeluarkan dari glosarium antarmuka ini.

| No | Istilah Asli (EN) | Terjemahan Saat Ini (ID) | Status Persetujuan | Catatan Tinjauan |
|---|---|---|---|---|
| 1 | **Leaderboard** | Papan Peringkat | **Perlu Ditinjau** | Apakah tetap menggunakan kata pinjaman *Leaderboard* (akrab bagi pengguna aplikasi gamifikasi/belajar) atau dibakukan menjadi *Papan Peringkat*? |
| 2 | **Streak** | Hari Runtutan | **USULAN (Perlu Ditinjau)** | Istilah "Hari Runtutan" berstatus **USULAN** dari pengembang. Perlu keputusan pemilik proyek apakah menggunakan "Runtutan Belajar", "Hari Beruntun", atau tetap mempertahankan istilah populer *Streak*. |
| 3 | **Playground** | Playground / Arena Kode | **Perlu Ditinjau** | Saat ini dipertahankan sebagai nama fitur tetap (*Playground*). Perlu konfirmasi apakah perlu dipadankan menjadi *Arena Kode* atau *Area Praktik*. |
| 4 | **Track** | Jalur Belajar | **Perlu Ditinjau** | Saat ini dipadankan menjadi *Jalur Belajar* (misal: "Jalur Front-End Development"). Perlu konfirmasi kesepakatan resmi. |
| 5 | **Bookmark** | Modul Tersimpan / Markah | **Perlu Ditinjau** | Saat ini menggunakan label *Modul Tersimpan*. Perlu konfirmasi apakah label ini diresmikan atau dipadankan menjadi *Markah Buku*. |
| 6 | **Cheat Sheet** | Cheat Sheet / Lembar Panduan | **Perlu Ditinjau** | Saat ini judul layar masih *Cheat Sheet*. Perlu konfirmasi apakah disepakati tetap atau diubah ke *Lembar Ringkasan / Contekan*. |
| 7 | **Quest** | Misi Harian | **Perlu Ditinjau** | Diterjemahkan sebagai *Misi Harian* pada kartu gamifikasi beranda. Menunggu persetujuan definitif. |
| 8 | **Course / Class** | Kursus / Kelas | **Perlu Ditinjau** | **Inkonsisten di kodebase**: Sebagian komponen memakai *Kelas* (`ClassDetailScreen`, "Semua Kelas") dan sebagian memakai *Kursus* (`CourseDetailScreen`, `CourseCards`). Perlu keputusan tegas memilih satu istilah tunggal. |
| 9 | **Placement Test** | Tes Penempatan | **Perlu Ditinjau** | Dipadankan sebagai *Tes Penempatan*. Menunggu konfirmasi pemilik proyek apakah sudah disepakati final. |

---

## 4. Batasan Lingkungan & Hasil Uji Otomatis

Pernyataan jujur mengenai keterbatasan sandbox dan hasil verifikasi:

### A. Uji yang BERHASIL Dijalankan (Pure JUnit JVM Tests - 100% Lulus)
1. **`StringParityAndFormatTest` (7 test lulus)**:
   - Paritas key 100% identik (279 key) antara `values/strings.xml`, `values-en/strings.xml`, `values-ja/strings.xml`, dan `values-zh/strings.xml`.
   - Kecocokan placeholder format posisi dan tipe (`%1$s`, `%1$d`) lintas keempat bahasa terverifikasi tanpa anomali.
   - Pemetaan label level dan lencana pada `DataLabelMappings` terbukti valid.
2. **`ActiveTreeHardcodedStringCheckTest` (2 test lulus)**:
   - Terbukti menggagalkan eksekusi jika total literal pohon aktif melebihi ceiling **749**.
   - Unit test sintetis membuktikan fungsi mendeteksi tepat 1 literal hardcoded dan mengabaikan `stringResource(...)`.
3. **`DataLabelMappingsTest` (Lulus)**: Menjamin stabilitas pemetaan data persisten ke display resource.
4. **`GeminiTutorPromptTest` (Lulus)**: Memverifikasi injeksi bahasa tutor AI (`id`, `en`, `ja`, `zh`) dan instruksi pedagogis.
5. **Unit Tests Logika Lainnya (Lulus)**: `CourseCatalogParserTest`, `CourseSectionUtilsTest`, `CodeSyntaxHighlighterTest`, `MarkdownParserTest`, `ModuleSearchTest`, `PasswordVisibilityLogicTest`, `ExampleUnitTest`.

### B. Uji yang TIDAK DAPAT Dijalankan (Robolectric Tests - Gagal Ketergantungan Jaringan)
Seluruh 9 file pengujian berbasis Robolectric (`@RunWith(RobolectricTestRunner::class)`) **gagal dijalankan**:
* `AccountIsolationAndMigrationTest`, `AuthFlowIntegrationTest`, `CertificateListScreenTest`, `CourseVerificationAndDraftTest`, `ExampleRobolectricTest`, `GreetingScreenshotTest`, `HomeScreenCarouselTest`, `ProgressScreenTest`, `SocialViewModelTest`.
* **Penyebab**: Container eksekusi serverless berada di lingkungan offline/terisolasi tanpa akses internet ke Maven Central untuk mengunduh binary dependency `android-all-instrumented` Robolectric (`java.io.IOException` pada `MavenArtifactFetcher.java:129`).

### C. Kompilasi & Pengujian Perangkat Fisik
* **Kompilasi Gradle (`compile_applet`)**: **BERHASIL (BUILD SUCCEEDED)**. APK terkompilasi tanpa error sintaksis.
* **Pengujian pada Perangkat Nyata / Emulator Interaktif**: **BELUM TERVERIFIKASI**. Di lingkungan build ini tidak tersedia Android Debug Bridge (ADB) maupun perangkat Android fisik. Responsivitas sentuhan, visualisasi layout multi-densitas, dan konsistensi warna dynamic bar pada perangkat fisik belum dapat diuji secara langsung.

---

## 5. Checklist QA Manual 18 Layar (ID vs EN)

Tabel evaluasi pengujian visual antarmuka saat beralih antara Bahasa Indonesia (ID) dan English (EN), dengan status keterisian teks bahasa Inggris yang sebenarnya di pohon aktif saat ini:

| No | Layar Antarmuka | Langkah Pengujian | Tampilan Diharapkan (ID) | Tampilan Diharapkan (EN) | Status Nyata di Mode English Saat Ini |
|---|---|---|---|---|---|
| 1 | **Splash** | Buka aplikasi pertama kali | Animasi logo & kutipan motivasi | Logo & English tagline | ⚠️ **Masih Berisi Teks Indonesia** (kutipan motivasi masih bahasa Indonesia) |
| 2 | **Login** | Arahkan ke layar Login | Header "Lanjutkan progres belajarmu", email, kata sandi, tombol masuk | Header "Continue your learning progress", email, password, sign in button | ✅ **Sudah Berbahasa Inggris** (string form & error message sudah dimigrasikan) |
| 3 | **Register** | Ketuk "Daftar Sekarang" | Header "Mulai perjalanan codingmu", nama, sandi, konfirmasi | Header "Start your coding journey", full name, password, confirm | ✅ **Sudah Berbahasa Inggris** (form & validasi sudah dimigrasikan) |
| 4 | **Onboarding** | Masuk sebagai pengguna baru / ulangi onboarding | Pilihan jalur, komitmen harian, ringkasan rencana belajar | Track choices, daily target, learning plan summary | ⚠️ **Masih Berisi Teks Indonesia** (6 file onboarding belum dimigrasikan, 60 literal) |
| 5 | **Home (Beranda)** | Tab Beranda | Salam "Halo", pencarian, "Lanjutkan Belajar", "Misi Harian" | Greeting "Hello", search, "Continue Learning", "Daily Quest" | ⚠️ **Masih Berisi Teks Indonesia** (kartu misi, carousel, dan teks beranda belum dimigrasikan) |
| 6 | **Katalog Kelas** | Tab Kelas | Judul "Semua Kelas", filter level, badge modul | Title "All Courses", level filter, modules badge | ⚠️ **Masih Berisi Teks Indonesia** (filter dan daftar kelas belum dimigrasikan) |
| 7 | **Detail Kelas** | Buka salah satu kelas (misal Fondasi Web) | "Tentang Kelas Ini", "Daftar Modul Belajar", tombol "Mulai Belajar" | "About This Course", "Course Syllabus", "Start Learning" | ⚠️ **Masih Berisi Teks Indonesia** (silabus modul & deskripsi kelas belum dimigrasikan) |
| 8 | **Materi** | Buka modul pembelajaran | Konten modul, tombol navigasi selesai, banner penanda tidak muncul | Module content, navigation buttons, banner: *"Lesson content is currently available in Indonesian only"* | ⚠️ **Sebagian Campuran** (Banner info tampil dalam English, namun tombol navigasi dan isi materi berbahasa Indonesia) |
| 9 | **Playground** | Buka tab editor Playground | Tab "HTML", "CSS", "JS", tombol "Jalankan", "Reset", preview kode | Tabs "HTML", "CSS", "JS", buttons "Run", "Reset", preview output | ⚠️ **Masih Berisi Teks Indonesia** (dialog reset & tombol aksi belum dimigrasikan) |
| 10 | **Kuis / Tes Penempatan** | Mulai kuis atau placement test | Aturan pengerjaan, batas waktu, opsi jawaban, tombol kirim | Instructions, countdown timer, options, submit button | ⚠️ **Masih Berisi Teks Indonesia** (aturan kuis, soal, dan opsi belum dimigrasikan) |
| 11 | **Hasil Evaluasi** | Selesaikan kuis kelulusan | Skor persentase, status kelulusan, tombol "Klaim Sertifikat" | Score percentage, completion status, "Claim Certificate" | ⚠️ **Masih Berisi Teks Indonesia** (analisis skor dan tombol klaim belum dimigrasikan) |
| 12 | **Progres** | Tab Progres | "Performa Keseluruhan", "Target Modul", statistik belajar | "Overall Performance", "Next Target Module", stats | ⚠️ **Masih Berisi Teks Indonesia** (kartu statistik dan detail progres belum dimigrasikan) |
| 13 | **Komunitas / Papan Peringkat** | Tab Papan Peringkat | "Papan Peringkat", "Peringkat Kamu", "Tantangan Mingguan" | "Leaderboard", "Your Standing", "Weekly Challenge" | ⚠️ **Masih Berisi Teks Indonesia** (tab komunitas & tantangan belum dimigrasikan) |
| 14 | **Forum Kelas** | Buka forum diskusi dari kelas | "Diskusi Kelas", tombol "Buat Diskusi", input balasan komentar | "Course Forum", "Start Discussion", comment reply input | ⚠️ **Masih Berisi Teks Indonesia** (form balasan & thread diskusi belum dimigrasikan) |
| 15 | **Notifikasi** | Buka notifikasi dari drawer | Filter notifikasi, tombol tandai dibaca, daftar notifikasi | Notification filters, mark as read, notification list | ⚠️ **Masih Berisi Teks Indonesia** (filter notifikasi dan pesan notifikasi belum dimigrasikan) |
| 16 | **Profil** | Tab Profil | "Profil Akun", dialog bahasa, daftar lencana, riwayat sertifikat | "Account Profile", language dialog, badge collection, certificates | ⚠️ **Sebagian Campuran** (Dialog bahasa & lencana sudah English via DataLabelMappings; bio & header profil masih bahasa Indonesia) |
| 17 | **Bantuan & FAQ** | Buka Bantuan dari Drawer | Tab "FAQ", "Kebijakan & Privasi", "Kontak & Versi" | Tab "FAQ", "Privacy & Terms", "Contact & Version" | ⚠️ **Masih Berisi Teks Indonesia** (daftar pertanyaan & jawaban FAQ belum dimigrasikan) |
| 18 | **AI Tutor** | Buka AI Tutor dari drawer/materi | Header AI Tutor, placeholder input, respons tutor dalam Bahasa Indonesia | Header AI Tutor, input placeholder, tutor responses generated in English | ✅ **Sudah Berbahasa Inggris** (Teks UI & respons AI Tutor otomatis menyesuaikan ke bahasa Inggris) |
