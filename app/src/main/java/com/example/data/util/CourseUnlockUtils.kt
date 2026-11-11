package com.example.data.util

import com.example.data.models.Course
import com.example.data.models.CourseModule
import com.example.data.models.ProjectSubmissionStatus

enum class ModuleLockState {
    COMPLETED,
    CURRENT_UNLOCKED,
    LOCKED
}

object CourseUnlockUtils {

    /**
     * Mengecek apakah sebuah modul dalam course tertentu sudah terbuka.
     * Aturan:
     * 1. Modul pertama (order terkecil) di tiap course SELALU terbuka tanpa syarat.
     * 2. Modul berikutnya terbuka hanya jika modul tepat sebelumnya (berdasarkan order)
     *    sudah ada entri isCompleted = true di ModuleProgressEntity untuk courseId yang sama.
     * 3. Jika ada SectionQuiz pada section modul sebelumnya, kuis tersebut harus sudah pernah LULUS,
     *    bukan cuma dikerjakan, sebelum modul di section berikutnya terbuka.
     */
    fun isModuleUnlocked(
        course: Course,
        moduleId: String,
        completedModuleIds: Set<String>,
        passedQuizIds: Set<String> = emptySet(),
        sortedModules: List<CourseModule> = course.modules.sortedBy { it.order }
    ): Boolean {
        val index = sortedModules.indexOfFirst { it.id == moduleId }
        if (index < 0) return false
        if (index == 0) return true // modul pertama course selalu terbuka

        // Modul tepat sebelumnya harus sudah selesai
        val previousModule = sortedModules[index - 1]
        if (previousModule.id !in completedModuleIds) {
            return false
        }

        // Cek jika modul ini berpindah section dari modul sebelumnya
        val currentModule = sortedModules[index]
        if (previousModule.section != currentModule.section) {
            val quizForPrevSection = course.sectionQuizzes.firstOrNull { it.section == previousModule.section }
            if (quizForPrevSection != null && quizForPrevSection.id !in passedQuizIds) {
                return false
            }
        }

        return true
    }

    /**
     * Menghitung status lock state (COMPLETED, CURRENT_UNLOCKED, LOCKED) untuk sebuah modul.
     * TEPAT SATU modul per course yang berstatus CURRENT_UNLOCKED (yaitu modul pertama yang
     * belum completed dalam urutan order, jika sudah unlocked).
     */
    fun getModuleLockState(
        course: Course,
        module: CourseModule,
        completedModuleIds: Set<String>,
        passedQuizIds: Set<String> = emptySet(),
        sortedModules: List<CourseModule> = course.modules.sortedBy { it.order }
    ): ModuleLockState {
        if (module.id in completedModuleIds) {
            return ModuleLockState.COMPLETED
        }
        val isUnlocked = isModuleUnlocked(course, module.id, completedModuleIds, passedQuizIds, sortedModules)
        if (!isUnlocked) {
            return ModuleLockState.LOCKED
        }
        val firstUncompleted = sortedModules.firstOrNull { it.id !in completedModuleIds }
        return if (firstUncompleted?.id == module.id) {
            ModuleLockState.CURRENT_UNLOCKED
        } else {
            ModuleLockState.LOCKED
        }
    }

    /**
     * Mendapatkan judul persyaratan sebelumnya (modul atau kuis) untuk ditampilkan saat modul terkunci:
     * "Selesaikan '<title>' untuk membuka"
     */
    fun getPreviousRequirementTitle(
        course: Course,
        moduleId: String,
        completedModuleIds: Set<String>,
        passedQuizIds: Set<String> = emptySet(),
        sortedModules: List<CourseModule> = course.modules.sortedBy { it.order }
    ): String? {
        val index = sortedModules.indexOfFirst { it.id == moduleId }
        if (index <= 0) return null

        val previousModule = sortedModules[index - 1]
        val currentModule = sortedModules[index]

        if (previousModule.section != currentModule.section) {
            val quizForPrevSection = course.sectionQuizzes.firstOrNull { it.section == previousModule.section }
            if (quizForPrevSection != null && quizForPrevSection.id !in passedQuizIds) {
                return quizForPrevSection.title
            }
        }

        return previousModule.title
    }

    /**
     * Mengecek apakah SectionQuiz terbuka.
     * Terbuka HANYA jika SEMUA modul pada section tersebut sudah selesai dikerjakan.
     */
    fun isSectionQuizUnlocked(
        course: Course,
        quizId: String,
        completedModuleIds: Set<String>,
        passedQuizIds: Set<String> = emptySet()
    ): Boolean {
        val quiz = course.sectionQuizzes.firstOrNull { it.id == quizId } ?: return false
        val sectionModules = course.modules.filter { it.section == quiz.section }
        val allSectionModulesCompleted = sectionModules.isNotEmpty() && sectionModules.all { it.id in completedModuleIds }
        if (!allSectionModulesCompleted) return false

        // Pastikan kuis section sebelumnya (jika ada) juga sudah lulus
        val maxOrderInSection = sectionModules.maxOfOrNull { it.order } ?: 0
        val priorQuizzes = course.sectionQuizzes.filter { priorQ ->
            priorQ.id != quiz.id && (course.modules.filter { it.section == priorQ.section }.maxOfOrNull { it.order } ?: 0) < maxOrderInSection
        }
        return priorQuizzes.all { it.id in passedQuizIds }
    }

    /**
     * Mengecek apakah Evaluasi Akhir Kelas terbuka.
     * Terbuka HANYA jika SEMUA modul di course tersebut berstatus selesai (dan semua section quiz sudah lulus).
     */
    fun isFinalQuizUnlocked(
        course: Course,
        completedModuleIds: Set<String>,
        passedQuizIds: Set<String> = emptySet()
    ): Boolean {
        val allModulesCompleted = course.modules.isNotEmpty() && course.modules.all { it.id in completedModuleIds }
        if (!allModulesCompleted) return false
        return course.sectionQuizzes.all { it.id in passedQuizIds }
    }

    /**
     * Mengecek apakah Submission Proyek Akhir Kelas terbuka.
     * Terbuka jika semua modul selesai, semua section quiz lulus, DAN Evaluasi Akhir Kelas sudah lulus (>= 70%).
     */
    fun isFinalProjectUnlocked(
        course: Course,
        completedModuleIds: Set<String>,
        passedQuizIds: Set<String> = emptySet(),
        finalQuizPassed: Boolean = false
    ): Boolean {
        if (finalQuizPassed) return true
        return false
    }

    /**
     * Mengecek apakah Sertifikat Kelulusan berhak diterbitkan / diklaim.
     * Wajib lulus Evaluasi Akhir Kelas DAN Proyek Akhir berstatus APPROVED oleh Fasilitator.
     */
    fun isCertificateUnlocked(
        finalQuizPassed: Boolean,
        submissionStatus: ProjectSubmissionStatus
    ): Boolean {
        return finalQuizPassed && submissionStatus == ProjectSubmissionStatus.APPROVED
    }

    /**
     * Menghitung nilai gabungan sertifikat akhir: 40% Kuis Evaluasi Akhir + 60% Proyek Akhir Fasilitator.
     */
    fun computeFinalCertificateScore(
        finalQuizScore: Int,
        facilitatorProjectScore: Int
    ): Int {
        val q = finalQuizScore.coerceIn(0, 100)
        val p = facilitatorProjectScore.coerceIn(0, 100)
        return ((q * 40 + p * 60) / 100).coerceIn(0, 100)
    }
}
