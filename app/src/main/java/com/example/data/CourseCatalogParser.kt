package com.example.data

import android.content.Context
import android.util.Log
import com.example.data.models.Course
import com.example.data.models.CourseModule
import com.example.data.models.FinalProjectSpec
import com.example.data.models.LearningTrack
import com.example.data.models.ModuleSection
import com.example.data.models.ProjectRubricItem
import com.example.data.models.QuizQuestion
import com.example.data.models.SectionQuiz
import com.squareup.moshi.Moshi
import java.io.BufferedReader
import java.io.File
import java.io.InputStreamReader

data class TrackCatalogResult(
    val track: LearningTrack,
    val courses: List<Course>,
    val courseContentPaths: Map<String, String> = emptyMap()
)

data class CatalogData(
    val tracks: List<LearningTrack>,
    val courses: List<Course>,
    val contentPaths: Map<String, String> = emptyMap()
)

/**
 * Parser for JSON catalog files located in assets/catalog/.
 * Follows the same pattern as CourseContentParser, loading track metadata,
 * courses, modules, quizzes, and references to markdown content folders.
 * Automatically computes totalCourses per LearningTrack from actual courses parsed.
 */
object CourseCatalogParser {
    private const val TAG = "CourseCatalogParser"
    private const val CATALOG_FOLDER = "catalog"

    private val moshi = Moshi.Builder().build()
    private val jsonAdapter = moshi.adapter(Any::class.java)

    val DEFAULT_TRACK_IDS = listOf(
        "front-end"
    )

    private fun logWarning(msg: String, throwable: Throwable? = null) {
        try {
            if (throwable != null) {
                Log.w(TAG, msg, throwable)
            } else {
                Log.w(TAG, msg)
            }
        } catch (_: Throwable) {
            System.err.println("[$TAG] WARNING: $msg")
            throwable?.printStackTrace(System.err)
        }
    }

    private fun logDebug(msg: String) {
        try {
            Log.d(TAG, msg)
        } catch (_: Throwable) {
            println("[$TAG] DEBUG: $msg")
        }
    }

    /**
     * Parses a single track JSON file content into TrackCatalogResult.
     * Computes LearningTrack.totalCourses dynamically from courses.size.
     */
    @Suppress("UNCHECKED_CAST")
    fun parseTrackCatalog(jsonContent: String): TrackCatalogResult? {
        return try {
            val root = jsonAdapter.fromJson(jsonContent) as? Map<String, Any?> ?: return null
            val trackId = root["id"] as? String ?: return null
            val trackName = root["name"] as? String ?: trackId
            val trackDescription = root["description"] as? String ?: ""
            val iconEmoji = root["iconEmoji"] as? String ?: "📚"

            val coursesJson = root["courses"] as? List<Map<String, Any?>> ?: emptyList()
            val courses = mutableListOf<Course>()
            val contentPaths = mutableMapOf<String, String>()

            for (cObj in coursesJson) {
                val courseId = cObj["id"] as? String ?: continue
                val cTrackId = cObj["trackId"] as? String ?: trackId
                val title = cObj["title"] as? String ?: ""
                val level = cObj["level"] as? String ?: "Pemula"
                val durationMinutes = (cObj["durationMinutes"] as? Number)?.toInt() ?: 60
                val rating = (cObj["rating"] as? Number)?.toFloat() ?: 0f
                val studentsCount = (cObj["studentsCount"] as? Number)?.toInt() ?: 0
                val description = cObj["description"] as? String ?: ""

                val prerequisites = (cObj["prerequisites"] as? List<*>)?.mapNotNull { it as? String } ?: emptyList()
                val instructor = cObj["instructor"] as? String ?: ""

                val contentPath = (cObj["contentPath"] as? String)?.takeIf { it.isNotBlank() }
                if (contentPath != null) {
                    contentPaths[courseId] = contentPath
                }

                // Parse inline modules if any
                val modules = mutableListOf<CourseModule>()
                val modulesJson = cObj["modules"] as? List<Map<String, Any?>>
                if (modulesJson != null) {
                    for ((mIndex, mObj) in modulesJson.withIndex()) {
                        val modId = mObj["id"] as? String ?: continue
                        val modCourseId = mObj["courseId"] as? String ?: courseId
                        val order = (mObj["order"] as? Number)?.toInt() ?: (mIndex + 1)
                        val modTitle = mObj["title"] as? String ?: ""
                        val readingMinutes = (mObj["readingMinutes"] as? Number)?.toInt() ?: 5
                        val content = mObj["content"] as? String ?: ""
                        val codeSnippet = mObj["codeSnippet"] as? String
                        val codeLanguage = mObj["codeLanguage"] as? String
                        val starterHtml = mObj["starterHtml"] as? String ?: ""
                        val starterCss = mObj["starterCss"] as? String ?: ""
                        val starterJs = mObj["starterJs"] as? String ?: ""
                        val playgroundTask = mObj["playgroundTask"] as? String
                        val sectionStr = mObj["section"] as? String ?: "INTI"
                        val section = try {
                            ModuleSection.valueOf(sectionStr.uppercase())
                        } catch (_: Exception) {
                            ModuleSection.INTI
                        }

                        val verificationRules = (mObj["verificationRules"] as? List<*>)?.mapNotNull { it as? String } ?: emptyList()
                        val isProjectContinuation = mObj["isProjectContinuation"] as? Boolean ?: false
                        val learningObjectives = (mObj["learningObjectives"] as? List<*>)?.mapNotNull { it as? String } ?: emptyList()

                        modules.add(
                            CourseModule(
                                id = modId,
                                courseId = modCourseId,
                                order = order,
                                title = modTitle,
                                readingMinutes = readingMinutes,
                                content = content,
                                codeSnippet = codeSnippet,
                                codeLanguage = codeLanguage,
                                starterHtml = starterHtml,
                                starterCss = starterCss,
                                starterJs = starterJs,
                                playgroundTask = playgroundTask,
                                section = section,
                                verificationRules = verificationRules,
                                isProjectContinuation = isProjectContinuation,
                                learningObjectives = learningObjectives
                            )
                        )
                    }
                }

                // Parse Quiz Questions
                val quizQuestions = mutableListOf<QuizQuestion>()
                val quizzesJson = cObj["quizQuestions"] as? List<Map<String, Any?>>
                if (quizzesJson != null) {
                    for ((qIndex, qObj) in quizzesJson.withIndex()) {
                        val qId = (qObj["id"] as? Number)?.toInt() ?: (qIndex + 1)
                        val question = qObj["question"] as? String ?: ""
                        val options = (qObj["options"] as? List<*>)?.mapNotNull { it as? String } ?: emptyList()
                        val correctIndex = (qObj["correctIndex"] as? Number)?.toInt() ?: 0
                        val explanation = qObj["explanation"] as? String ?: ""
                        val sourceModuleId = qObj["sourceModuleId"] as? String

                        quizQuestions.add(
                            QuizQuestion(
                                id = qId,
                                question = question,
                                options = options,
                                correctIndex = correctIndex,
                                explanation = explanation,
                                sourceModuleId = sourceModuleId
                            )
                        )
                    }
                }

                // Parse Section Quizzes
                val sectionQuizzes = mutableListOf<SectionQuiz>()
                val sqJson = cObj["sectionQuizzes"] as? List<Map<String, Any?>>
                if (sqJson != null) {
                    for (sqObj in sqJson) {
                        val sqId = sqObj["id"] as? String ?: continue
                        val sqCourseId = sqObj["courseId"] as? String ?: courseId
                        val sqSectionStr = sqObj["section"] as? String ?: "INTI"
                        val sqSection = try {
                            ModuleSection.valueOf(sqSectionStr.uppercase())
                        } catch (_: Exception) {
                            ModuleSection.INTI
                        }
                        val sqTitle = sqObj["title"] as? String ?: ""
                        val questionsPerAttempt = (sqObj["questionsPerAttempt"] as? Number)?.toInt() ?: 5
                        val passingPercent = (sqObj["passingPercent"] as? Number)?.toInt() ?: 80
                        val sqDuration = (sqObj["durationMinutes"] as? Number)?.toInt() ?: 15

                        val poolJson = sqObj["questionPool"] as? List<Map<String, Any?>>
                        val pool = mutableListOf<QuizQuestion>()
                        if (poolJson != null) {
                            for ((pqIndex, pqObj) in poolJson.withIndex()) {
                                val pqId = (pqObj["id"] as? Number)?.toInt() ?: (pqIndex + 1)
                                val pqQuestion = pqObj["question"] as? String ?: ""
                                val pqOptions = (pqObj["options"] as? List<*>)?.mapNotNull { it as? String } ?: emptyList()
                                val pqCorrect = (pqObj["correctIndex"] as? Number)?.toInt() ?: 0
                                val pqExpl = pqObj["explanation"] as? String ?: ""
                                val pqSrc = pqObj["sourceModuleId"] as? String

                                pool.add(
                                    QuizQuestion(
                                        id = pqId,
                                        question = pqQuestion,
                                        options = pqOptions,
                                        correctIndex = pqCorrect,
                                        explanation = pqExpl,
                                        sourceModuleId = pqSrc
                                    )
                                )
                            }
                        }

                        sectionQuizzes.add(
                            SectionQuiz(
                                id = sqId,
                                courseId = sqCourseId,
                                section = sqSection,
                                title = sqTitle,
                                questionPool = pool,
                                questionsPerAttempt = questionsPerAttempt,
                                passingPercent = passingPercent,
                                durationMinutes = sqDuration
                            )
                        )
                    }
                }

                val cheatSheetMarkdown = (cObj["cheatSheetMarkdown"] as? String)?.takeIf { it.isNotBlank() }
                val sectionTitles = (cObj["sectionTitles"] as? Map<*, *>)
                    ?.mapNotNull { (k, v) ->
                        val key = k as? String
                        val value = v as? String
                        if (key != null && value != null) key to value else null
                    }?.toMap() ?: emptyMap()

                val fpObj = cObj["finalProject"] as? Map<String, Any?>
                val finalProject = if (fpObj != null) {
                    val fpId = fpObj["id"] as? String ?: "fp-$courseId"
                    val fpTitle = fpObj["title"] as? String ?: title
                    val fpDesc = fpObj["description"] as? String ?: description
                    val fpScenario = fpObj["scenario"] as? String ?: fpDesc
                    val fpLang = fpObj["language"] as? String ?: defaultLanguageForTrack(cTrackId)
                    val fpStarter = fpObj["starterCode"] as? String ?: ""
                    val fpMinPassing = (fpObj["minPassingScore"] as? Number)?.toInt() ?: 75
                    val fpXp = (fpObj["xpReward"] as? Number)?.toInt() ?: 100
                    val rubricsJson = fpObj["rubrics"] as? List<Map<String, Any?>> ?: emptyList()
                    val rubrics = rubricsJson.mapIndexed { rIdx, rObj ->
                        ProjectRubricItem(
                            rObj["id"] as? String ?: "r_${rIdx + 1}",
                            rObj["title"] as? String ?: "R${rIdx + 1}",
                            rObj["description"] as? String ?: "",
                            (rObj["weightPercent"] as? Number)?.toInt() ?: 25
                        )
                    }.ifEmpty { defaultRubricsForCourse(courseId) }
                    FinalProjectSpec(
                        fpId,
                        courseId,
                        fpTitle,
                        fpDesc,
                        fpScenario,
                        fpLang,
                        fpStarter,
                        fpMinPassing,
                        fpXp,
                        rubrics
                    )
                } else {
                    defaultFinalProjectSpecFor(courseId, title, description, cTrackId)
                }

                courses.add(
                    Course(
                        id = courseId,
                        trackId = cTrackId,
                        title = title,
                        level = level,
                        durationMinutes = durationMinutes,
                        rating = rating,
                        studentsCount = studentsCount,
                        description = description,
                        prerequisites = prerequisites,
                        instructor = instructor,
                        modules = modules,
                        quizQuestions = quizQuestions,
                        sectionQuizzes = sectionQuizzes,
                        cheatSheetMarkdown = cheatSheetMarkdown,
                        sectionTitles = sectionTitles,
                        finalProject = finalProject
                    )
                )
            }

            val track = LearningTrack(
                id = trackId,
                name = trackName,
                description = trackDescription,
                iconEmoji = iconEmoji,
                totalCourses = courses.size
            )

            TrackCatalogResult(
                track = track,
                courses = courses,
                courseContentPaths = contentPaths
            )
        } catch (e: Exception) {
            logWarning("Error parsing track catalog JSON: ${e.message}", e)
            null
        }
    }

    /**
     * Loads catalog from Android assets folder 'catalog/'.
     */
    fun loadCatalog(context: Context): CatalogData {
        val tracks = mutableListOf<LearningTrack>()
        val courses = mutableListOf<Course>()
        val contentPaths = mutableMapOf<String, String>()

        val fileNames = try {
            context.assets.list(CATALOG_FOLDER)?.toList()?.filter { it.endsWith(".json", ignoreCase = true) }
        } catch (e: Exception) {
            null
        } ?: DEFAULT_TRACK_IDS.map { "$it.json" }

        val orderedFileNames = if (fileNames.isNotEmpty()) {
            val defaultOrder = DEFAULT_TRACK_IDS.map { "$it.json" }
            fileNames.sortedBy { name ->
                val idx = defaultOrder.indexOf(name)
                if (idx >= 0) idx else 999
            }
        } else {
            DEFAULT_TRACK_IDS.map { "$it.json" }
        }

        for (fileName in orderedFileNames) {
            val assetPath = "$CATALOG_FOLDER/$fileName"
            try {
                context.assets.open(assetPath).use { inputStream ->
                    val content = BufferedReader(InputStreamReader(inputStream, Charsets.UTF_8)).readText()
                    val result = parseTrackCatalog(content)
                    if (result != null) {
                        tracks.add(result.track)
                        courses.addAll(result.courses)
                        contentPaths.putAll(result.courseContentPaths)
                    }
                }
            } catch (e: Exception) {
                logWarning("Failed to read track catalog asset $assetPath: ${e.message}")
            }
        }

        logDebug("Loaded ${tracks.size} tracks and ${courses.size} courses from assets")
        return CatalogData(tracks = tracks, courses = courses, contentPaths = contentPaths)
    }

    /**
     * Loads catalog from local File directory (used for Unit/Robolectric Tests).
     */
    fun loadCatalogFromDirectory(dir: File): CatalogData {
        val tracks = mutableListOf<LearningTrack>()
        val courses = mutableListOf<Course>()
        val contentPaths = mutableMapOf<String, String>()

        if (!dir.exists() || !dir.isDirectory) {
            logWarning("Catalog directory ${dir.path} does not exist or is not a directory")
            return CatalogData(tracks = emptyList(), courses = emptyList())
        }

        val files = dir.listFiles { file -> file.isFile && file.extension.equals("json", ignoreCase = true) }
            ?: emptyArray()

        val orderedFiles = files.sortedBy { f ->
            val trackId = f.nameWithoutExtension
            val idx = DEFAULT_TRACK_IDS.indexOf(trackId)
            if (idx >= 0) idx else 999
        }

        for (f in orderedFiles) {
            try {
                val content = f.readText(Charsets.UTF_8)
                val result = parseTrackCatalog(content)
                if (result != null) {
                    tracks.add(result.track)
                    courses.addAll(result.courses)
                    contentPaths.putAll(result.courseContentPaths)
                }
            } catch (e: Exception) {
                logWarning("Failed to read catalog file ${f.path}: ${e.message}", e)
            }
        }

        return CatalogData(tracks = tracks, courses = courses, contentPaths = contentPaths)
    }

    fun findLocalCatalogDirectory(): File? {
        val candidates = listOf(
            File("app/src/main/assets/catalog"),
            File("src/main/assets/catalog"),
            File("../app/src/main/assets/catalog")
        )
        return candidates.firstOrNull { it.exists() && it.isDirectory }
    }

    fun defaultLanguageForTrack(trackId: String): String = when (trackId.lowercase()) {
        "front-end" -> "html-css-js"
        "back-end" -> "javascript"
        "python" -> "python"
        "c-lang" -> "c"
        "cpp-lang" -> "cpp"
        else -> "code"
    }

    fun defaultRubricsForCourse(courseId: String): List<ProjectRubricItem> = when (courseId.lowercase()) {
        "fe-web-foundation-00" -> listOf(
            ProjectRubricItem("r1", "Struktur HTML Semantik", "Menggunakan <!DOCTYPE html>, header, main, section, dan footer dengan benar.", 25),
            ProjectRubricItem("r2", "Konten & Navigasi Profil", "Menampilkan profil diri/portofolio lengkap dengan daftar keahlian dan tautan.", 25),
            ProjectRubricItem("r3", "Styling Dasar & Box Model CSS", "Menerapkan warna, tipografi, margin, padding, dan border yang rapi.", 25),
            ProjectRubricItem("r4", "Kerapian & Validitas Kode", "Penulisan tag bersarang (nesting) dan selector CSS bersih tanpa error sintaksis.", 25)
        )
        "fe-css-02" -> listOf(
            ProjectRubricItem("r1", "Navigasi & Header Flexbox", "Menggunakan display: flex, justify-content, dan align-items pada bar navigasi.", 25),
            ProjectRubricItem("r2", "Galeri Kartu CSS Grid", "Menggunakan display: grid dan grid-template-columns untuk susunan kartu proyek.", 25),
            ProjectRubricItem("r3", "Desain Responsif (Media Query)", "Menyesuaikan tata letak pada layar mobile dan desktop menggunakan @media.", 25),
            ProjectRubricItem("r4", "Konsistensi Spacing & Estetika", "Menggunakan gap, proporsi ruang kosong, dan kontras warna yang nyaman dibaca.", 25)
        )
        "fe-js-03" -> listOf(
            ProjectRubricItem("r1", "Deklarasi Variabel & Struktur Data", "Menggunakan const/let, array, dan objek untuk menyimpan daftar item aplikasi.", 25),
            ProjectRubricItem("r2", "Manipulasi DOM Dinamis", "Menampilkan, menambah, atau memperbarui elemen HTML secara dinamis melalui JavaScript.", 25),
            ProjectRubricItem("r3", "Penanganan Event Interaktif", "Menggunakan addEventListener untuk merespons klik tombol atau input pengguna.", 25),
            ProjectRubricItem("r4", "Validasi Input & Modularitas Fungsi", "Mencegah input kosong dan memisahkan logika ke dalam fungsi yang bersih.", 25)
        )
        "be-node-01" -> listOf(
            ProjectRubricItem("r1", "Modularisasi CommonJS / ESM", "Memisahkan fungsi utilitas/data dan mengimpornya dengan bersih.", 25),
            ProjectRubricItem("r2", "Operasi Asinkron & File/Data", "Menggunakan Promise atau async/await dengan penanganan error try/catch.", 25),
            ProjectRubricItem("r3", "Routing HTTP Server & Endpoint JSON", "Menangani request GET/POST dan mengembalikan status code serta payload JSON yang tepat.", 25),
            ProjectRubricItem("r4", "Penanganan Error & Edge Cases", "Mengembalikan respons 404/400 yang terstruktur saat route atau data tidak valid.", 25)
        )
        "py-fund-01" -> listOf(
            ProjectRubricItem("r1", "Variabel, Tipe Data & Percabangan", "Menggunakan tipe data yang tepat serta logika if/elif/else untuk aturan bisnis.", 25),
            ProjectRubricItem("r2", "Perulangan & Koleksi Data (List/Dict)", "Mengelola sekumpulan data menggunakan list dan dictionary secara efisien.", 25),
            ProjectRubricItem("r3", "Modularitas Fungsi (def & return)", "Membagi program menjadi fungsi-fungsi dengan parameter dan nilai balik yang jelas.", 25),
            ProjectRubricItem("r4", "Format Output & Penanganan Input", "Menampilkan ringkasan laporan/statistik data yang rapi dan tahan terhadap input kosong.", 25)
        )
        "c-fund-01" -> listOf(
            ProjectRubricItem("r1", "Tipe Data, Array & Fungsi", "Mendeklarasikan tipe data, array, dan fungsi modular sesuai standar bahasa C.", 25),
            ProjectRubricItem("r2", "Pemodelan Data dengan Struct", "Menggunakan struct untuk merepresentasikan entitas data secara terstruktur.", 25),
            ProjectRubricItem("r3", "Penggunaan Pointer & Alamat Memori", "Memanfaatkan pointer untuk mengakses atau memodifikasi data pada fungsi.", 25),
            ProjectRubricItem("r4", "Keamanan Memori & Format printf", "Menjaga batas indeks array, membebaskan alokasi dinamis (jika ada), dan mencetak output rapi.", 25)
        )
        "cpp-fund-01" -> listOf(
            ProjectRubricItem("r1", "Enkapsulasi Class & Konstruktor", "Merancang class dengan atribut private/protected, constructor, dan method publik.", 25),
            ProjectRubricItem("r2", "Pemanfaatan STL (std::vector & std::string)", "Mengelola koleksi objek dinamis menggunakan kontainer standar C++ STL.", 25),
            ProjectRubricItem("r3", "Pewarisan (Inheritance) & Polimorfisme", "Menerapkan class turunan atau virtual method sesuai konsep OOP.", 25),
            ProjectRubricItem("r4", "Manajemen Resource Modern (RAII / Referensi)", "Menggunakan const reference pada parameter fungsi dan menjaga kebersihan memori.", 25)
        )
        else -> listOf(
            ProjectRubricItem("r1", "Penerapan Konsep Inti Kelas", "Mengimplementasikan materi utama yang dipelajari pada kelas ini.", 25),
            ProjectRubricItem("r2", "Struktur & Logika Program", "Menyusun alur kode yang terstruktur, efisien, dan bebas dari bug utama.", 25),
            ProjectRubricItem("r3", "Kelengkapan Fitur Studi Kasus", "Memenuhi seluruh spesifikasi fitur yang diminta pada skenario proyek akhir.", 25),
            ProjectRubricItem("r4", "Kualitas, Dokumentasi & Kerapian Kode", "Menulis kode yang mudah dibaca dengan penamaan variabel yang jelas.", 25)
        )
    }

    fun defaultFinalProjectSpecFor(
        courseId: String,
        courseTitle: String,
        courseDescription: String,
        trackId: String
    ): FinalProjectSpec {
        val lang = defaultLanguageForTrack(trackId)
        val rubrics = defaultRubricsForCourse(courseId)
        return when (courseId.lowercase()) {
            "fe-web-foundation-00" -> FinalProjectSpec(
                "fp-fe-web-foundation-00",
                courseId,
                "Proyek Akhir: Halaman Portofolio & Profil Developer",
                "Bangun halaman web portofolio personal yang menerapkan struktur HTML5 semantik dan styling CSS dasar hingga Box Model.",
                "Buatlah satu halaman profil developer yang menampilkan: (1) Header berisi nama dan peranmu, (2) Bagian 'Tentang Saya' dan daftar keahlian menggunakan list HTML, (3) Bagian kartu karya/target belajar yang ditata menggunakan Box Model CSS (margin, padding, border, background), serta (4) Footer kontak.",
                lang,
                "<!DOCTYPE html>\n<html lang=\"id\">\n<head>\n  <meta charset=\"UTF-8\">\n  <title>Portofolio Saya</title>\n  <style>\n    body { font-family: sans-serif; margin: 0; padding: 24px; background: #f8fafc; color: #0f172a; }\n    .card { background: #ffffff; padding: 16px; border: 1px solid #cbd5e1; border-radius: 12px; margin-bottom: 16px; }\n  </style>\n</head>\n<body>\n  <header class=\"card\">\n    <h1>Nama Lengkap Anda</h1>\n    <p>Junior Web Developer</p>\n  </header>\n  <main>\n    <section class=\"card\">\n      <h2>Keahlian Utama</h2>\n      <ul>\n        <li>HTML5 Semantik</li>\n        <li>CSS Box Model</li>\n      </ul>\n    </section>\n  </main>\n</body>\n</html>",
                75,
                100,
                rubrics
            )
            "fe-css-02" -> FinalProjectSpec(
                "fp-fe-css-02",
                courseId,
                "Proyek Akhir: Dashboard Katalog Responsif dengan Flexbox & Grid",
                "Rancang tata letak halaman web modern yang memadukan navigasi Flexbox dan galeri kartu CSS Grid yang responsif.",
                "Bangun halaman showcase produk/kursus yang memiliki: (1) Navbar responsif menggunakan Flexbox (logo di kiri, menu di kanan), (2) Hero section ringkas, dan (3) Grid kartu minimal 3 item menggunakan CSS Grid (grid-template-columns) beserta aturan @media query agar tetap rapi di layar mobile.",
                lang,
                "<!DOCTYPE html>\n<html lang=\"id\">\n<head>\n  <meta charset=\"UTF-8\">\n  <style>\n    .navbar { display: flex; justify-content: space-between; align-items: center; padding: 16px; background: #0f172a; color: white; }\n    .grid-container { display: grid; grid-template-columns: repeat(auto-fit, minmax(220px, 1fr)); gap: 16px; padding: 16px; }\n    .card { padding: 16px; border-radius: 12px; background: #f1f5f9; }\n  </style>\n</head>\n<body>\n  <nav class=\"navbar\"><strong>CodeShowcase</strong><span>Menu</span></nav>\n  <section class=\"grid-container\">\n    <article class=\"card\"><h3>Proyek 1</h3><p>Deskripsi singkat proyek.</p></article>\n    <article class=\"card\"><h3>Proyek 2</h3><p>Deskripsi singkat proyek.</p></article>\n    <article class=\"card\"><h3>Proyek 3</h3><p>Deskripsi singkat proyek.</p></article>\n  </section>\n</body>\n</html>",
                75,
                100,
                rubrics
            )
            "fe-js-03" -> FinalProjectSpec(
                "fp-fe-js-03",
                courseId,
                "Proyek Akhir: Aplikasi Pengelola Target Belajar Interaktif (DOM & Event)",
                "Bangun aplikasi web interaktif menggunakan JavaScript ES6+, manipulasi DOM, dan Event Listener.",
                "Buatlah aplikasi To-Do / Target Belajar Interaktif yang memungkinkan pengguna: (1) Menambahkan target belajar baru lewat input teks dan tombol, (2) Memvalidasi agar input kosong tidak ditambahkan, (3) Menampilkan daftar target dari array JavaScript ke DOM beserta penghitung jumlah target selesai.",
                lang,
                "// Kode JavaScript Proyek Akhir (atau gabungkan HTML+JS)\nconst tasks = [\n  { id: 1, title: 'Selesaikan Modul DOM', completed: true },\n  { id: 2, title: 'Kerjakan Proyek Akhir JS', completed: false }\n];\n\nfunction addTask(title) {\n  if (!title || title.trim() === '') return false;\n  tasks.push({ id: Date.now(), title: title.trim(), completed: false });\n  return true;\n}\n\nconsole.log('Total tugas:', tasks.length);",
                75,
                100,
                rubrics
            )
            "be-node-01" -> FinalProjectSpec(
                "fp-be-node-01",
                courseId,
                "Proyek Akhir: Layanan Mini REST API & Pengelola Data Node.js",
                "Bangun modul dan server HTTP Node.js yang melayani endpoint data JSON secara asinkron.",
                "Buatlah skrip arsitektur server Node.js yang mencakup: (1) Modul/fungsi pengelolaan daftar buku atau kursus, (2) Pemrosesan asinkron (Promise / async-await) dengan penanganan try/catch, dan (3) Handler HTTP server untuk route GET /api/items dan POST /api/items beserta fallback 404 JSON.",
                lang,
                "const http = require('http');\n\nconst courses = [\n  { id: 'be-01', title: 'Node.js Fundamentals', status: 'active' }\n];\n\nasync function getCoursesAsync() {\n  return Promise.resolve(courses);\n}\n\nconst server = http.createServer(async (req, res) => {\n  res.setHeader('Content-Type', 'application/json');\n  if (req.method === 'GET' && req.url === '/api/courses') {\n    const data = await getCoursesAsync();\n    res.writeHead(200);\n    res.end(JSON.stringify({ status: 'success', data }));\n  } else {\n    res.writeHead(404);\n    res.end(JSON.stringify({ status: 'error', message: 'Route not found' }));\n  }\n});\n",
                75,
                100,
                rubrics
            )
            "py-fund-01" -> FinalProjectSpec(
                "fp-py-fund-01",
                courseId,
                "Proyek Akhir: Sistem Analisis & Rekapitulasi Nilai Akademik Python",
                "Terapkan struktur data list/dictionary, perulangan, percabangan, dan fungsi modular di Python.",
                "Buatlah program Python yang mengolah data nilai mahasiswa/siswa dalam bentuk list of dictionaries: (1) Fungsi menghitung rata-rata dan menentukan predikat kelulusan (A/B/C/Belum Lulus), (2) Fungsi mencari siswa dengan nilai tertinggi, dan (3) Mencetak laporan rekapitulasi yang rapi.",
                lang,
                "def hitung_predikat(skor):\n    if skor >= 85:\n        return 'A (Sangat Baik)'\n    elif skor >= 75:\n        return 'B (Lulus)'\n    else:\n        return 'Perlu Perbaikan'\n\ndata_siswa = [\n    {'nama': 'Rafi', 'skor': 90},\n    {'nama': 'Siti', 'skor': 82},\n    {'nama': 'Budi', 'skor': 76}\n]\n\nfor s in data_siswa:\n    print(f\"{s['nama']}: {s['skor']} -> {hitung_predikat(s['skor'])}\")\n",
                75,
                100,
                rubrics
            )
            "c-fund-01" -> FinalProjectSpec(
                "fp-c-fund-01",
                courseId,
                "Proyek Akhir: Manajemen Inventaris & Memori Berbasis Struct dan Pointer di C",
                "Rancang program bahasa C yang memodelkan data dengan struct, memanipulasinya lewat pointer, dan menampilkan laporan terformat.",
                "Buatlah program C yang mendefinisikan struct Item (id, nama, stok, harga), fungsi dengan parameter pointer (Item *item) untuk memperbarui stok/nilai, serta fungsi yang menghitung total nilai inventaris dari array struct dan mencetaknya dengan printf.",
                lang,
                "#include <stdio.h>\n\ntypedef struct {\n    int id;\n    char nama[32];\n    int stok;\n} Barang;\n\nvoid tambahStok(Barang *b, int jumlah) {\n    if (b != NULL && jumlah > 0) {\n        b->stok += jumlah;\n    }\n}\n\nint main() {\n    Barang item1 = {101, \"Modul Mikrokontroler\", 12};\n    tambahStok(&item1, 5);\n    printf(\"ID: %d | Nama: %s | Stok Akhir: %d\\n\", item1.id, item1.nama, item1.stok);\n    return 0;\n}\n",
                75,
                100,
                rubrics
            )
            "cpp-fund-01" -> FinalProjectSpec(
                "fp-cpp-fund-01",
                courseId,
                "Proyek Akhir: Sistem Perpustakaan Digital Berbasis OOP & STL C++",
                "Bangun sistem berorientasi objek menggunakan class, enkapsulasi, pewarisan/polimorfisme, dan std::vector di C++.",
                "Buatlah program C++ yang memiliki: (1) Class dasar/utama dengan atribut terenkapsulasi dan constructor, (2) Class turunan atau method virtual untuk menampilkan informasi spesifik, serta (3) Pengelolaan daftar objek di dalam std::vector beserta fungsi pencarian/rekap menggunakan const reference.",
                lang,
                "#include <iostream>\n#include <vector>\n#include <string>\n\nclass Koleksi {\nprotected:\n    std::string judul;\n    int tahun;\npublic:\n    Koleksi(const std::string& j, int t) : judul(j), tahun(t) {}\n    virtual ~Koleksi() = default;\n    virtual void tampilkanInfo() const {\n        std::cout << judul << \" (\" << tahun << \")\" << std::endl;\n    }\n};\n\nint main() {\n    std::vector<Koleksi> daftar;\n    daftar.emplace_back(\"Arsitektur C++ Modern\", 2026);\n    for (const auto& item : daftar) {\n        item.tampilkanInfo();\n    }\n    return 0;\n}\n",
                75,
                100,
                rubrics
            )
            else -> FinalProjectSpec(
                "fp-$courseId",
                courseId,
                "Proyek Akhir: $courseTitle",
                courseDescription,
                "Selesaikan proyek akhir komprehensif yang menggabungkan konsep inti dari kelas $courseTitle dan penuhi seluruh indikator rubrik penilaian.",
                lang,
                "",
                75,
                100,
                rubrics
            )
        }
    }
}
