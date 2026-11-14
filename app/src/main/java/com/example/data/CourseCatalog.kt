package com.example.data

import android.content.Context
import android.util.Log
import com.example.data.models.BadgeItem
import com.example.data.models.Course
import com.example.data.models.CourseModule
import com.example.data.models.DailyQuest
import com.example.data.models.LearningTrack
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.withContext
import java.io.File

/**
 * Data-driven CourseCatalog powered by JSON catalog definitions in assets/catalog/.
 * Track course counts (totalCourses) are calculated dynamically from parsed courses.
 */
object CourseCatalog {

    private val defaultTracks = listOf(
        LearningTrack(
            "front-end",
            "Front-End Web Development",
            "Pelajari cara membangun antarmuka web yang menarik, responsif, dan interaktif dengan HTML, CSS, dan JavaScript.",
            "🌐",
            0
        )
    )

    private val dynamicModules = mutableMapOf<String, List<CourseModule>>()
    private val _isAssetCoursesLoaded = MutableStateFlow(false)
    val isAssetCoursesLoaded: StateFlow<Boolean> = _isAssetCoursesLoaded.asStateFlow()

    private val _coursesState = MutableStateFlow<List<Course>>(emptyList())
    val coursesState: StateFlow<List<Course>> = _coursesState.asStateFlow()

    private var _tracks: List<LearningTrack> = defaultTracks
    val tracks: List<LearningTrack>
        get() = _tracks

    private var _baseCourses: List<Course> = emptyList()
    private var _contentPaths: Map<String, String> = emptyMap()

    fun setDynamicModules(courseId: String, modules: List<CourseModule>) {
        synchronized(dynamicModules) {
            dynamicModules[courseId] = modules
        }
        _coursesState.value = courses
    }

    fun getDynamicModules(courseId: String): List<CourseModule> {
        synchronized(dynamicModules) {
            val existing = dynamicModules[courseId]
            if (!existing.isNullOrEmpty()) {
                return existing
            }
        }
        val localLoaded = tryLoadFromLocalDirectory(courseId)
        if (localLoaded.isNotEmpty()) {
            synchronized(dynamicModules) {
                dynamicModules[courseId] = localLoaded
            }
            return localLoaded
        }
        return emptyList()
    }

    private fun tryLoadFromLocalDirectory(courseId: String): List<CourseModule> {
        val candidates = listOf(
            File("app/src/main/assets/content/$courseId"),
            File("src/main/assets/content/$courseId"),
            File("../app/src/main/assets/content/$courseId")
        )
        for (dir in candidates) {
            if (dir.exists() && dir.isDirectory) {
                val loaded = CourseContentParser.loadCourseModulesFromDirectory(dir, courseId)
                if (loaded.isNotEmpty()) {
                    return loaded
                }
            }
        }
        return emptyList()
    }

    private fun applyCatalogData(catalogData: CatalogData) {
        if (catalogData.tracks.isNotEmpty()) {
            _tracks = catalogData.tracks
        }
        _baseCourses = catalogData.courses
        _contentPaths = catalogData.contentPaths
        _coursesState.value = courses
    }

    /**
     * Fast synchronous loader for the JSON catalog definitions.
     */
    fun loadCatalogSync(context: Context) {
        try {
            val catalogData = CourseCatalogParser.loadCatalog(context)
            if (catalogData.tracks.isNotEmpty() || catalogData.courses.isNotEmpty()) {
                applyCatalogData(catalogData)
            }
        } catch (e: Exception) {
            Log.e("CourseCatalog", "Failed to load catalog synchronously: ${e.message}", e)
        }
    }

    /**
     * Suspended loader that reads JSON catalog definitions and loads
     * markdown modules from assets/content/ for courses that declare contentPath.
     */
    suspend fun loadAssetCourses(context: Context) = withContext(Dispatchers.IO) {
        try {
            val catalogData = CourseCatalogParser.loadCatalog(context)
            if (catalogData.tracks.isNotEmpty() || catalogData.courses.isNotEmpty()) {
                applyCatalogData(catalogData)
            }

            // Load markdown modules for courses declaring contentPath
            for (course in _baseCourses) {
                val contentPath = _contentPaths[course.id]
                if (!contentPath.isNullOrBlank()) {
                    val modules = CourseContentParser.loadCourseModules(context, course.id)
                    if (modules.isNotEmpty()) {
                        setDynamicModules(course.id, modules)
                        Log.d("CourseCatalog", "Loaded ${modules.size} modules from assets into CourseCatalog for ${course.id}")
                    }
                }
            }
        } catch (e: Exception) {
            Log.e("CourseCatalog", "Failed to load asset modules: ${e.message}", e)
        } finally {
            _isAssetCoursesLoaded.value = true
            _coursesState.value = courses
        }
    }

    val courses: List<Course>
        get() = _baseCourses.map { course ->
            val loaded = getDynamicModules(course.id)
            if (loaded.isNotEmpty()) {
                course.copy(modules = loaded)
            } else {
                course
            }
        }

    fun getCourseById(courseId: String): Course? {
        val targetId = if (courseId == "fe-html-01") "fe-web-foundation-00" else courseId
        return courses.find { it.id == targetId }
    }

    init {
        // Attempt to load from local directory for JVM unit tests and local execution
        val localDir = CourseCatalogParser.findLocalCatalogDirectory()
        if (localDir != null) {
            val localCatalog = CourseCatalogParser.loadCatalogFromDirectory(localDir)
            if (localCatalog.tracks.isNotEmpty()) {
                applyCatalogData(localCatalog)
            }
        }
        _coursesState.value = courses
        val initialFe = getDynamicModules("fe-web-foundation-00")
        if (initialFe.isNotEmpty()) {
            _isAssetCoursesLoaded.value = true
        }
    }

    val allBadges = listOf(
        BadgeItem("b1", "Langkah Pertama", "🏁", "Selesaikan modul belajar pertama kamu", false),
        BadgeItem("b2", "Pelajar Baru", "📚", "Selesaikan kelas pertama dengan tuntas", false),
        BadgeItem("b3", "Konsisten", "🔥", "Pertahankan streak belajar selama 7 hari", false),
        BadgeItem("b4", "Api Abadi", "⚡", "Pertahankan streak belajar selama 30 hari berturut-turut", false),
        BadgeItem("b5", "Petualang Web", "🗺️", "Pelajari seluruh kelas HTML, CSS, dan JavaScript", false),
        BadgeItem("b6", "Penguji Ulung", "✅", "Lulus evaluasi dengan nilai sempurna 100%", false),
        BadgeItem("b7", "Pejuang Misi", "⚔️", "Selesaikan misi harian selama 7 hari", false),
        BadgeItem("b8", "Code Master", "💻", "Capai Level 10 (Code Master)", false),
        BadgeItem("b9", "Kolektor", "🏆", "Dapatkan minimal 3 sertifikat kelulusan", false),
        BadgeItem("b10", "AI Enthusiast", "🤖", "Ajukan minimal 10 pertanyaan ke AI Tutor", false),
        BadgeItem("b11", "Penyelamat", "💾", "Simpan modul atau kelas ke daftar Tersimpan", false),
        BadgeItem("b12", "Web Master", "🚀", "Selesaikan seluruh proyek akhir Web Development", false)
    )

    val dailyQuestsPool = listOf(
        DailyQuest("quest_mod", "Selesaikan 1 Modul", "Baca dan pahami minimal 1 modul belajar hari ini", 50, 1),
        DailyQuest("quest_quiz", "Kerjakan 1 Kuis", "Uji pemahamanmu dengan menyelesaikan 1 kuis evaluasi", 50, 1),
        DailyQuest("quest_ai", "Tanya AI Tutor", "Diskusikan satu konsep coding dengan AI Tutor", 30, 1),
        DailyQuest("quest_play", "Jalankan Playground", "Uji kode HTML, CSS, atau JS di playground interaktif", 40, 1)
    )
}
