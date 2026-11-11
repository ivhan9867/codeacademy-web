# Baseline & Verifikasi Internasionalisasi (i18n) - CodeAcademy

Dokumen ini mencatat baseline aplikasi CodeAcademy sebelum proses lokalisasi/i18n dilakukan, serta rekonsiliasi klasifikasi pohon aktif pada **TAHAP 1.5-A**.

---

## 1. Verifikasi Jalur Navigasi & Pohon Layar

- **Pohon Layar Aktif:**
  - `MainActivity.kt` menggunakan state-based `NavHost` dan secara langsung mengimpor serta merender layar top-level dari package `com.example.ui.screens.*` (seperti `HomeScreen.kt`, `ClassDetailScreen.kt`, `ProfileScreen.kt`, `MateriScreen.kt`, `AuthOnboardingScreen.kt`, dll.).
  - `MainViewModel.kt` (`com.example.ui.MainViewModel`) mengelola state navigasi dan berinteraksi langsung dengan `com.example.data.CodeAcademyRepository`.
  - **Koreksi TAHAP 1.5-A (Onboarding):** `AuthOnboardingScreen.kt` (layar aktif yang dipanggil langsung oleh `MainActivity.kt:560`) mengimpor `com.example.ui.screens.onboarding.OnboardingScreen` beserta konstanta key. Seluruh file langkah di package `com.example.ui.screens.onboarding.*` (`WelcomeStep`, `CodePreviewStep`, `CommunityStep`, `PlanSummaryStep`, `TrackSelectionStep`, `OnboardingComponents`) dipanggil oleh `OnboardingScreen` dan karenanya **secara transitif merupakan bagian dari Pohon Layar Aktif**.

- **Pohon Layar Tidak Terpakai (Unused Tree) & Pengecualian:**
  - File `CodeAcademyNavigation.kt` (`com.example.ui.navigation`) beserta layar subpackage (`com.example.ui.screens.auth.*`, `catalog.*`, `forum.*`, `home.*`, `profile.*`, `social.*`, `quiz.*`, `lesson.*`, `playground.*`, `aitutor.*`, `notifications.*`, `ar.*`, `leaderboard.*`) dan viewmodel subpackage `com.example.ui.viewmodel.*` (kecuali `SocialViewModel`) **TIDAK PERNAH DIIMPOR ATAU DIPANGGIL** oleh `MainActivity.kt` maupun alur aplikasi aktif yang berjalan.
  - **Koreksi Audit TAHAP 1.5-E (`data/repository` dan `data/model`):**
    Pernyataan sebelumnya bahwa kedua paket ini "tidak pernah diimpor" adalah keliru. Status yang benar adalah **"dipakai sebagian"**:
    1. `com.example.data.repository.CodeAcademyRepository` diimpor oleh `app/src/main/java/com/example/ui/viewmodel/SocialViewModel.kt:11` (ViewModel aktif; digunakan oleh `CommunityScreen.kt:102`, `DiskusiHangatScreen.kt:39`, dan `CourseForumScreen.kt:82`) untuk fitur friend request, authService, dan realtime sync.
       - *Verifikasi literal:* Terdapat 2 literal UI hardcoded di `CodeAcademyRepository.kt`, yaitu:
         - Baris 302: `title = "Level Up! Kamu Naik ke Level $newLevel 🎉"` di method `addXpAndCheckLevelUp`
         - Baris 394: `title = "Sertifikat Kelulusan Terbit! 🎓"` di method `submitQuiz`
       - *Verifikasi pemanggil:* Kedua method tersebut **TIDAK PERNAH DIPANGGIL** dari pohon aplikasi aktif. Alur aktif aplikasi memanggil method kuis di `MainActivity.kt:917` melalui `MainViewModel.kt:502` yang berinteraksi dengan `com.example.data.CodeAcademyRepository` (di root `data/`), bukan `com.example.data.repository.CodeAcademyRepository`. Method `submitQuiz` dan `addXpAndCheckLevelUp` di `data.repository` hanya dirujuk secara internal atau oleh ViewModel pohon tidak terpakai (`QuizViewModel.kt:31`, `CodeAcademyViewModel.kt:240`, `CodeAcademyNavigation.kt:474`).
    2. `com.example.data.model.*` diimpor oleh:
       - `app/src/main/java/com/example/data/remote/supabase/SupabaseModels.kt:3`
       - `app/src/main/java/com/example/data/remote/supabase/SupabaseRestClient.kt:4`
       - `app/src/main/java/com/example/ui/viewmodel/SocialViewModel.kt:10` (`FriendshipEntity`)
       - `app/src/main/java/com/example/ui/components/CommonComponents.kt:41-42` (`BadgeEntity`, `CourseEntity`)
       - File `data/model/Entities.kt` sendiri berisi deklarasi entity murni dan memiliki **0 literal teks UI**.
    - Oleh karena itu, kedua paket ini tetap **dikecualikan dari hitungan literal teks UI pohon aktif** karena literal UI di dalamnya tidak berada pada alur aktif atau bernilai nol.
  - **Bukti Referensi Kode:**
    - `grep -rn "CodeAcademyNavigation" app/src/` -> 0 referensi di pohon aktif.
    - `grep -rn "ui.navigation" app/src/` -> Hanya berada di `ui/navigation/NavRoutes.kt` dan `CodeAcademyNavigation.kt`.
    - `grep -rn "com.example.ui.screens.auth" app/src/` -> Hanya diimpor oleh `CodeAcademyNavigation.kt`.

---

## 2. Hasil Audit Literal Teks UI Hardcoded (`tools/find_hardcoded_strings.sh`)

### Pohon Aktif (Sebelum vs Sesudah Koreksi TAHAP 1.5-A)

| No | File | Sebelum Koreksi | Sesudah Koreksi | Keterangan Status |
|---|---|---|---|---|
| 1 | `app/src/main/java/com/example/ui/screens/ClassDetailScreen.kt` | 60 | 60 | Tetap di Pohon Aktif |
| 2 | `app/src/main/java/com/example/ui/screens/ProgressScreen.kt` | 44 | 44 | Tetap di Pohon Aktif |
| 3 | `app/src/main/java/com/example/ui/screens/CommunityScreen.kt` | 37 | 37 | Tetap di Pohon Aktif |
| 4 | `app/src/main/java/com/example/data/local/InitialDataSeed.kt` | 36 | 36 | Tetap di Pohon Aktif |
| 5 | `app/src/main/java/com/example/ui/screens/PlaygroundScreen.kt` | 34 | 34 | Tetap di Pohon Aktif |
| 6 | `app/src/main/java/com/example/ui/screens/PlacementTestScreen.kt` | 30 | 30 | Tetap di Pohon Aktif |
| 7 | `app/src/main/java/com/example/ui/components/AppNavigationDrawer.kt` | 30 | 30 | Tetap di Pohon Aktif |
| 8 | `app/src/main/java/com/example/ui/screens/HelpScreen.kt` | 26 | 26 | Tetap di Pohon Aktif |
| 9 | `app/src/main/java/com/example/ui/screens/CertificateListScreen.kt` | 23 | 23 | Tetap di Pohon Aktif |
| 10 | `app/src/main/java/com/example/ui/screens/SectionQuizRulesScreen.kt` | 23 | 23 | Tetap di Pohon Aktif |
| 11 | `app/src/main/java/com/example/ui/screens/SectionQuizScreen.kt` | 22 | 22 | Tetap di Pohon Aktif |
| 12 | `app/src/main/java/com/example/ui/components/Components.kt` | 22 | 22 | Tetap di Pohon Aktif |
| 13 | `app/src/main/java/com/example/ui/screens/HomeScreenTopCarousel.kt` | 21 | 21 | Tetap di Pohon Aktif |
| 14 | `app/src/main/java/com/example/ui/components/CommonComponents.kt` | 19 | 19 | Tetap di Pohon Aktif |
| 15 | `app/src/main/java/com/example/ui/screens/CourseForumScreen.kt` | 19 | 19 | Tetap di Pohon Aktif |
| 16 | `app/src/main/java/com/example/ui/screens/MateriScreen.kt` | 19 | 19 | Tetap di Pohon Aktif |
| 17 | `app/src/main/java/com/example/ui/screens/CourseDetailScreen.kt` | 18 | 18 | Tetap di Pohon Aktif |
| 18 | `app/src/main/java/com/example/ui/screens/CourseCatalogScreen.kt` | 17 | 17 | Tetap di Pohon Aktif |
| 19 | `app/src/main/java/com/example/ui/screens/onboarding/WelcomeStep.kt` | **0** | **16** | **Dipindahkan dari Tidak Terpakai ke Aktif** |
| 20 | `app/src/main/java/com/example/ui/screens/onboarding/CodePreviewStep.kt` | **0** | **16** | **Dipindahkan dari Tidak Terpakai ke Aktif** |
| 21 | `app/src/main/java/com/example/ui/screens/SectionQuizResultScreen.kt` | 15 | 15 | Tetap di Pohon Aktif |
| 22 | `app/src/main/java/com/example/ui/screens/CertificateScreen.kt` | 13 | 13 | Tetap di Pohon Aktif |
| 23 | `app/src/main/java/com/example/ui/screens/SearchScreen.kt` | 13 | 13 | Tetap di Pohon Aktif |
| 24 | `app/src/main/java/com/example/ui/components/CourseCards.kt` | 12 | 12 | Tetap di Pohon Aktif |
| 25 | `app/src/main/java/com/example/ui/screens/HasilEvaluasiScreen.kt` | 11 | 11 | Tetap di Pohon Aktif |
| 26 | `app/src/main/java/com/example/ui/screens/onboarding/CommunityStep.kt` | **0** | **11** | **Dipindahkan dari Tidak Terpakai ke Aktif** |
| 27 | `app/src/main/java/com/example/data/CodeAcademyRepository.kt` | 10 | 10 | Tetap di Pohon Aktif |
| 28 | `app/src/main/java/com/example/ui/components/OnboardingIllustrations.kt` | 10 | 10 | Tetap di Pohon Aktif |
| 29 | `app/src/main/java/com/example/ui/components/MarkdownView.kt` | 10 | 10 | Tetap di Pohon Aktif |
| 30 | `app/src/main/java/com/example/ui/screens/onboarding/PlanSummaryStep.kt` | **0** | **9** | **Dipindahkan dari Tidak Terpakai ke Aktif** |
| 31 | `app/src/main/java/com/example/ui/screens/QuizScreen.kt` | 9 | 9 | Tetap di Pohon Aktif |
| 32 | `app/src/main/java/com/example/MainActivity.kt` | 8 | 8 | Tetap di Pohon Aktif |
| 33 | `app/src/main/java/com/example/ui/screens/HomeScreenTrackList.kt` | 8 | 8 | Tetap di Pohon Aktif |
| 34 | `app/src/main/java/com/example/ui/screens/ProfileHeatmapSection.kt` | 7 | 7 | Tetap di Pohon Aktif |
| 35 | `app/src/main/java/com/example/ui/screens/DiskusiHangatScreen.kt` | 6 | 6 | Tetap di Pohon Aktif |
| 36 | `app/src/main/java/com/example/ui/screens/BookmarksScreen.kt` | 6 | 6 | Tetap di Pohon Aktif |
| 37 | `app/src/main/java/com/example/ui/screens/SplashScreen.kt` | 6 | 6 | Tetap di Pohon Aktif |
| 38 | `app/src/main/java/com/example/ui/screens/onboarding/TrackSelectionStep.kt` | **0** | **5** | **Dipindahkan dari Tidak Terpakai ke Aktif** |
| 39 | `app/src/main/java/com/example/ui/screens/ProfileHeaderSection.kt` | 5 | 5 | Tetap di Pohon Aktif |
| 40 | `app/src/main/java/com/example/data/CourseCatalog.kt` | 5 | 5 | Tetap di Pohon Aktif |
| 41 | `app/src/main/java/com/example/ui/screens/HomeScreen.kt` | 5 | 5 | Tetap di Pohon Aktif |
| 42 | `app/src/main/java/com/example/ui/screens/HomeScreenCourseList.kt` | 5 | 5 | Tetap di Pohon Aktif |
| 43 | `app/src/main/java/com/example/ui/screens/NotificationsScreen.kt` | 4 | 4 | Tetap di Pohon Aktif |
| 44 | `app/src/main/java/com/example/ui/screens/ProfileSettingsSection.kt` | 3 | 3 | Tetap di Pohon Aktif |
| 45 | `app/src/main/java/com/example/ui/screens/onboarding/OnboardingComponents.kt` | **0** | **3** | **Dipindahkan dari Tidak Terpakai ke Aktif** |
| 46 | `app/src/main/java/com/example/ui/screens/CheatSheetScreen.kt` | 3 | 3 | Tetap di Pohon Aktif |
| 47 | `app/src/main/java/com/example/ui/screens/LoginScreen.kt` | 3 | 3 | Tetap di Pohon Aktif |
| 48 | `app/src/main/java/com/example/ui/screens/ProfileBadgesSection.kt` | 3 | 3 | Tetap di Pohon Aktif |
| 49 | `app/src/main/java/com/example/ui/screens/AiTutorScreen.kt` | 2 | 2 | Tetap di Pohon Aktif |
| 50 | `app/src/main/java/com/example/ui/components/ShimmerPlaceholder.kt` | 2 | 2 | Tetap di Pohon Aktif |
| 51 | `app/src/main/java/com/example/ui/MainViewModel.kt` | 2 | 2 | Tetap di Pohon Aktif |
| 52 | `app/src/main/java/com/example/ui/components/CoachMarkBubble.kt` | 2 | 2 | Tetap di Pohon Aktif |
| 53 | `app/src/main/java/com/example/ui/screens/ProfileScreen.kt` | 1 | 1 | Tetap di Pohon Aktif |
| | **TOTAL POHON AKTIF** | **689** | **749** | **+60 literal teridentifikasi aktif** |

*(Catatan: Baseline awal saat tahap 1 dimulai sebelum migrasi login/register adalah 764. Setelah migrasi strings.xml login/register/ai-tutor, hitungan adalah 689 sebelum koreksi ini, dan menjadi 749 sesudah onboarding dimasukkan).*

---

### Pohon Diabaikan (Sebelum vs Sesudah Koreksi TAHAP 1.5-A & Audit 1.5-E)

| No | File | Sebelum Koreksi | Sesudah Koreksi | Keterangan Status | Terpakai oleh (Bukti `file:baris`) |
|---|---|---|---|---|---|
| 1 | `app/src/main/java/com/example/ui/screens/profile/ProfileScreens.kt` | 40 | 40 | Tetap Diabaikan | `ui/navigation/CodeAcademyNavigation.kt:40,41` (pohon tidak terpakai) |
| 2 | `app/src/main/java/com/example/ui/screens/auth/AuthScreens.kt` | 26 | 26 | Tetap Diabaikan | `ui/navigation/CodeAcademyNavigation.kt:29,30,31` (pohon tidak terpakai) |
| 3 | `app/src/main/java/com/example/ui/screens/catalog/CatalogScreens.kt` | 24 | 24 | Tetap Diabaikan | `ui/navigation/CodeAcademyNavigation.kt:32,33` (pohon tidak terpakai) |
| 4 | `app/src/main/java/com/example/ui/screens/forum/ForumScreens.kt` | 21 | 21 | Tetap Diabaikan | `ui/navigation/CodeAcademyNavigation.kt:34,35` (pohon tidak terpakai) |
| 5 | `app/src/main/java/com/example/ui/screens/home/HomeScreen.kt` | 20 | 20 | Tetap Diabaikan | `ui/navigation/CodeAcademyNavigation.kt:36` (pohon tidak terpakai) |
| 6 | `app/src/main/java/com/example/ui/screens/social/SocialScreens.kt` | 19 | 19 | Tetap Diabaikan | `ui/navigation/CodeAcademyNavigation.kt:44,45,46` (pohon tidak terpakai) |
| 7 | `app/src/main/java/com/example/ui/screens/quiz/QuizScreens.kt` | 16 | 16 | Tetap Diabaikan | `ui/navigation/CodeAcademyNavigation.kt:42,43` (pohon tidak terpakai) |
| 8 | `app/src/main/java/com/example/ui/screens/leaderboard/LeaderboardScreen.kt` | 13 | 13 | Tetap Diabaikan | *Tidak diimpor oleh file mana pun* (0 rujukan) |
| 9 | `app/src/main/java/com/example/ui/screens/lesson/ModuleLessonScreen.kt` | 11 | 11 | Tetap Diabaikan | `ui/navigation/CodeAcademyNavigation.kt:37` (pohon tidak terpakai) |
| 10 | `app/src/main/java/com/example/ui/screens/ar/ArVisualizerScreen.kt` | 9 | 9 | Tetap Diabaikan | `ui/navigation/CodeAcademyNavigation.kt:28` (pohon tidak terpakai) |
| 11 | `app/src/main/java/com/example/ui/screens/playground/PlaygroundScreen.kt` | 8 | 8 | Tetap Diabaikan | `ui/navigation/CodeAcademyNavigation.kt:39` (pohon tidak terpakai) |
| 12 | `app/src/main/java/com/example/ui/screens/aitutor/AiTutorScreen.kt` | 7 | 7 | Tetap Diabaikan | `ui/navigation/CodeAcademyNavigation.kt:27` (pohon tidak terpakai) |
| 13 | `app/src/main/java/com/example/ui/screens/notifications/NotificationsScreen.kt` | 5 | 5 | Tetap Diabaikan | `ui/navigation/CodeAcademyNavigation.kt:38` (pohon tidak terpakai) |
| 14 | `app/src/main/java/com/example/data/repository/CodeAcademyRepository.kt` | 2 | 2 | **Dipakai sebagian** (dikecualikan dari hitungan) | `ui/viewmodel/SocialViewModel.kt:11,36` (aktif); literal di baris 302 & 394 berada di method yang hanya dirujuk `QuizViewModel.kt:31`, `CodeAcademyViewModel.kt:240`, `CodeAcademyNavigation.kt:474` (tidak pernah dipanggil alur aktif) |
| 15 | `app/src/main/java/com/example/ui/screens/onboarding/WelcomeStep.kt` | 16 | **0** | **Dipindahkan ke Pohon Aktif** | `ui/screens/onboarding/OnboardingScreen.kt:23` (aktif) |
| 16 | `app/src/main/java/com/example/ui/screens/onboarding/CodePreviewStep.kt` | 16 | **0** | **Dipindahkan ke Pohon Aktif** | `ui/screens/onboarding/OnboardingScreen.kt:24` (aktif) |
| 17 | `app/src/main/java/com/example/ui/screens/onboarding/CommunityStep.kt` | 11 | **0** | **Dipindahkan ke Pohon Aktif** | `ui/screens/onboarding/OnboardingScreen.kt:25` (aktif) |
| 18 | `app/src/main/java/com/example/ui/screens/onboarding/PlanSummaryStep.kt` | 9 | **0** | **Dipindahkan ke Pohon Aktif** | `ui/screens/onboarding/OnboardingScreen.kt:26` (aktif) |
| 19 | `app/src/main/java/com/example/ui/screens/onboarding/TrackSelectionStep.kt` | 5 | **0** | **Dipindahkan ke Pohon Aktif** | `ui/screens/onboarding/OnboardingScreen.kt:27` (aktif) |
| 20 | `app/src/main/java/com/example/ui/screens/onboarding/OnboardingComponents.kt` | 3 | **0** | **Dipindahkan ke Pohon Aktif** | `ui/screens/onboarding/PlanSummaryStep.kt:23` (aktif) |
| *—* | `app/src/main/java/com/example/data/model/Entities.kt` | 0 | 0 | **Dipakai sebagian** (0 literal UI) | `data/remote/supabase/SupabaseModels.kt:3`, `SupabaseRestClient.kt:4`, `ui/viewmodel/SocialViewModel.kt:10`, `ui/components/CommonComponents.kt:41,42` |
| | **TOTAL POHON DIABAIKAN** | **281** | **221** | **-60 dipindahkan ke aktif** | |

**TOTAL SELURUH KODEBASE:** **970** (Pohon Aktif: 749, Pohon Diabaikan: 221)

---

## 3. Baseline Unit Test (`gradle :app:testDebugUnitTest`)

- **Pure JUnit Unit Tests (10 file test - 100% LULUS):**
  - `com.example.ExampleUnitTest` (LULUS)
  - `com.example.MarkdownParserTest` (LULUS)
  - `com.example.PasswordVisibilityLogicTest` (LULUS)
  - `com.example.CourseCatalogParserTest` (LULUS)
  - `com.example.CourseSectionUtilsTest` (LULUS)
  - `com.example.CodeSyntaxHighlighterTest` (LULUS)
  - `com.example.ModuleSearchTest` (LULUS)
  - `com.example.DataLabelMappingsTest` (LULUS)
  - `com.example.GeminiTutorPromptTest` (LULUS)
  - `com.example.StringParityAndFormatTest` (LULUS)

- **Robolectric Tests (9 file test - GAGAL akibat keterbatasan sandbox offline):**
  - `AccountIsolationAndMigrationTest.kt`
  - `AuthFlowIntegrationTest.kt`
  - `CertificateListScreenTest.kt`
  - `CourseVerificationAndDraftTest.kt`
  - `ExampleRobolectricTest.kt`
  - `GreetingScreenshotTest.kt`
  - `HomeScreenCarouselTest.kt`
  - `ProgressScreenTest.kt`
  - `SocialViewModelTest.kt`
  - *Sebab Kegagalan:* `java.lang.AssertionError at MavenArtifactFetcher.java:129` (`java.io.IOException` - lingkungan sandbox tidak memiliki akses internet ke Maven Central untuk mendownload JAR `android-all-instrumented` Robolectric).
