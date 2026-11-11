package com.example.data.remote

import android.util.Log
import com.example.data.local.ForumPostEntity
import com.example.data.local.LeaderboardEntryCacheEntity
import com.example.data.local.ProjectSubmissionEntity
import com.google.firebase.firestore.AggregateSource
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ListenerRegistration
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.SetOptions
import kotlinx.coroutines.tasks.await
import java.util.UUID

class FirestoreForumAndLeaderboardService(
    private val customFirestore: FirebaseFirestore? = null
) {
    companion object {
        private const val TAG = "FirestoreForumService"
        const val FORUM_COLLECTION = "forum_posts"
        const val USERS_COLLECTION = "users"
        const val PROJECT_SUBMISSIONS_COLLECTION = "project_submissions"
        const val DEFAULT_PAGE_SIZE = 20L
    }

    private fun getFirestore(): FirebaseFirestore? {
        if (customFirestore != null) return customFirestore
        return try {
            FirebaseFirestore.getInstance()
        } catch (t: Throwable) {
            Log.w(TAG, "FirebaseFirestore instance not ready: ${t.message}")
            null
        }
    }

    // ==========================================
    // 1. FORUM DISKUSI PER KELAS
    // ==========================================

    /**
     * Fetch paginated forum posts for a specific course using Query.limit(20) and optional startAfter document.
     */
    suspend fun fetchPosts(
        courseId: String,
        lastVisibleDoc: DocumentSnapshot? = null,
        limit: Long = DEFAULT_PAGE_SIZE
    ): Result<Pair<List<ForumPostEntity>, DocumentSnapshot?>> {
        val firestore = getFirestore() ?: return Result.failure(IllegalStateException("Firestore tidak tersedia"))
        return try {
            var query = firestore.collection(FORUM_COLLECTION)
                .whereEqualTo("courseId", courseId)
                .orderBy("timestamp", Query.Direction.DESCENDING)
                .limit(limit)

            if (lastVisibleDoc != null) {
                query = query.startAfter(lastVisibleDoc)
            }

            val snapshot = query.get().await()
            val posts = snapshot.documents.mapNotNull { doc ->
                mapDocumentToPost(doc)
            }
            val newLastDoc = if (snapshot.documents.isNotEmpty()) {
                snapshot.documents.last()
            } else {
                null
            }
            Result.success(Pair(posts, newLastDoc))
        } catch (e: Exception) {
            Log.w(TAG, "Error fetching forum posts for course $courseId: ${e.message}")
            Result.failure(e)
        }
    }

    /**
     * Pasang real-time snapshot listener Firestore di dalam DisposableEffect.
     * Mengembalikan ListenerRegistration yang WAJIB di-remove() di onDispose.
     */
    fun attachRecentPostsListener(
        courseId: String,
        limit: Long = DEFAULT_PAGE_SIZE,
        onUpdate: (List<ForumPostEntity>) -> Unit,
        onError: (Exception) -> Unit = {}
    ): ListenerRegistration? {
        val firestore = getFirestore()
        if (firestore == null) {
            onError(IllegalStateException("Firestore tidak tersedia"))
            return null
        }
        return try {
            firestore.collection(FORUM_COLLECTION)
                .whereEqualTo("courseId", courseId)
                .orderBy("timestamp", Query.Direction.DESCENDING)
                .limit(limit)
                .addSnapshotListener { snapshot, error ->
                    if (error != null) {
                        Log.w(TAG, "SnapshotListener error for course $courseId: ${error.message}")
                        onError(error)
                        return@addSnapshotListener
                    }
                    if (snapshot != null) {
                        val posts = snapshot.documents.mapNotNull { doc ->
                            mapDocumentToPost(doc)
                        }
                        onUpdate(posts)
                    }
                }
        } catch (e: Exception) {
            Log.w(TAG, "Failed to attach snapshot listener: ${e.message}")
            onError(e)
            null
        }
    }

    /**
     * Kirim post baru ke forum kelas.
     */
    suspend fun createPost(
        courseId: String,
        authorId: String,
        authorName: String,
        content: String,
        authorLevel: Int = 1,
        replyToPostId: String? = null,
        replyToAuthorName: String? = null,
        replyToContentSnippet: String? = null
    ): Result<ForumPostEntity> {
        val firestore = getFirestore() ?: return Result.failure(IllegalStateException("Firestore tidak tersedia"))
        return try {
            val postId = UUID.randomUUID().toString()
            val timestamp = System.currentTimeMillis()
            val postData = hashMapOf<String, Any>(
                "postId" to postId,
                "courseId" to courseId,
                "authorId" to authorId,
                "authorName" to authorName,
                "content" to content.trim(),
                "timestamp" to timestamp,
                "likeCount" to 0,
                "authorLevel" to authorLevel
            )
            if (replyToPostId != null) postData["replyToPostId"] = replyToPostId
            if (replyToAuthorName != null) postData["replyToAuthorName"] = replyToAuthorName
            if (replyToContentSnippet != null) postData["replyToContentSnippet"] = replyToContentSnippet

            firestore.collection(FORUM_COLLECTION)
                .document(postId)
                .set(postData)
                .await()

            val entity = ForumPostEntity(
                postId = postId,
                courseId = courseId,
                authorId = authorId,
                authorName = authorName,
                content = content.trim(),
                timestamp = timestamp,
                likeCount = 0,
                isLikedByMe = false,
                replyToPostId = replyToPostId,
                replyToAuthorName = replyToAuthorName,
                replyToContentSnippet = replyToContentSnippet,
                authorLevel = authorLevel,
                authorIsMentor = false
            )
            Result.success(entity)
        } catch (e: Exception) {
            Log.e(TAG, "Error creating post in forum: ${e.message}")
            Result.failure(e)
        }
    }

    /**
     * Like / Unlike forum post secara efisien menggunakan FieldValue.increment.
     */
    suspend fun toggleLikePost(postId: String, shouldLike: Boolean): Result<Unit> {
        val firestore = getFirestore() ?: return Result.failure(IllegalStateException("Firestore tidak tersedia"))
        return try {
            val incrementValue = if (shouldLike) 1L else -1L
            firestore.collection(FORUM_COLLECTION)
                .document(postId)
                .update("likeCount", FieldValue.increment(incrementValue))
                .await()
            Result.success(Unit)
        } catch (e: Exception) {
            Log.w(TAG, "Error toggling like for post $postId: ${e.message}")
            Result.failure(e)
        }
    }

    // ==========================================
    // 2. LEADERBOARD (CLOUD DATA + QUERY EFISIEN)
    // ==========================================

    /**
     * Sinkronisasikan profil XP user saat bertambah atau saat login ke Firestore.
     * Menyimpan totalXp, xpThisWeek, xpThisMonth, currentStreak, dan primaryTrack untuk multi-scope leaderboard.
     */
    suspend fun syncUserXp(
        userId: String,
        userName: String,
        totalXp: Int,
        level: Int,
        currentStreak: Int = 0,
        xpThisWeek: Int = (totalXp % 350 + 50),
        xpThisMonth: Int = (totalXp % 900 + 150),
        primaryTrack: String = "front-end"
    ): Result<Unit> {
        val firestore = getFirestore() ?: return Result.failure(IllegalStateException("Firestore tidak tersedia"))
        return try {
            val data = hashMapOf(
                "userId" to userId,
                "name" to userName,
                "totalXp" to totalXp,
                "xpThisWeek" to xpThisWeek,
                "xpThisMonth" to xpThisMonth,
                "level" to level,
                "currentStreak" to currentStreak,
                "primaryTrack" to primaryTrack,
                "updatedAt" to System.currentTimeMillis()
            )
            firestore.collection(USERS_COLLECTION)
                .document(userId)
                .set(data, SetOptions.merge())
                .await()
            Result.success(Unit)
        } catch (e: Exception) {
            Log.w(TAG, "Error syncing user XP to Firestore: ${e.message}")
            Result.failure(e)
        }
    }

    /**
     * Query top leaderboard users diurutkan berdasarkan timeframe dan filter track.
     * Timeframe: "weekly" (default), "monthly", "all_time", "streak".
     * Scope track: "global" (semua), atau trackId ("front-end", "back-end", dll).
     */
    suspend fun fetchTopLeaderboard(
        period: String = "weekly",
        trackId: String? = null,
        limit: Long = 50L
    ): Result<List<LeaderboardEntryCacheEntity>> {
        val firestore = getFirestore() ?: return Result.failure(IllegalStateException("Firestore tidak tersedia"))
        val scopeKey = "${period}_${trackId ?: "global"}"
        return try {
            val sortField = when (period) {
                "weekly" -> "xpThisWeek"
                "monthly" -> "xpThisMonth"
                "streak" -> "currentStreak"
                else -> "totalXp"
            }

            var query: Query = firestore.collection(USERS_COLLECTION)
            if (trackId != null && trackId != "global") {
                query = query.whereEqualTo("primaryTrack", trackId)
            }
            query = query.orderBy(sortField, Query.Direction.DESCENDING).limit(limit)

            val snapshot = query.get().await()

            val entries = snapshot.documents.mapIndexedNotNull { index, doc ->
                val id = doc.getString("userId") ?: doc.id
                val name = doc.getString("name") ?: "Siswa"
                val xp = (doc.getLong(sortField) ?: doc.getLong("totalXp") ?: 0L).toInt()
                val level = (doc.getLong("level") ?: 1L).toInt()
                LeaderboardEntryCacheEntity(
                    scopeKey = scopeKey,
                    userId = id,
                    userName = name,
                    totalXp = xp,
                    level = level,
                    rank = index + 1,
                    cachedAt = System.currentTimeMillis()
                )
            }
            Result.success(entries)
        } catch (e: Exception) {
            Log.w(TAG, "Error fetching top leaderboard for scope $scopeKey: ${e.message}")
            Result.failure(e)
        }
    }

    /**
     * Hitung posisi peringkat user sendiri secara efisien jika berada di luar top 50,
     * menggunakan count() aggregation query Firestore tanpa fetch semua data.
     */
    suspend fun calculateUserRank(
        userXp: Int,
        period: String = "weekly",
        trackId: String? = null
    ): Result<Int> {
        val firestore = getFirestore() ?: return Result.failure(IllegalStateException("Firestore tidak tersedia"))
        return try {
            val sortField = when (period) {
                "weekly" -> "xpThisWeek"
                "monthly" -> "xpThisMonth"
                "streak" -> "currentStreak"
                else -> "totalXp"
            }

            var query: Query = firestore.collection(USERS_COLLECTION)
            if (trackId != null && trackId != "global") {
                query = query.whereEqualTo("primaryTrack", trackId)
            }
            val aggregateQuery = query.whereGreaterThan(sortField, userXp).count()

            val snapshot = aggregateQuery.get(AggregateSource.SERVER).await()
            val higherUsersCount = snapshot.count
            // Peringkat adalah jumlah user dengan XP lebih tinggi + 1
            Result.success((higherUsersCount + 1).toInt())
        } catch (e: Exception) {
            Log.w(TAG, "Error calculating user rank: ${e.message}")
            Result.failure(e)
        }
    }

    // ==========================================
    // 3. SOROTAN FORUM (TOP 3 DISKUSI TER-RAMAI)
    // ==========================================

    /**
     * Fetch 2-3 postingan forum dengan like terbanyak untuk ditampilkan di Sorotan Komunitas.
     * Tidak membutuhkan real-time listener (cukup one-time fetch saat layar dibuka).
     */
    suspend fun fetchTopForumHighlights(limit: Long = 3L): Result<List<ForumPostEntity>> {
        val firestore = getFirestore() ?: return Result.failure(IllegalStateException("Firestore tidak tersedia"))
        return try {
            val snapshot = firestore.collection(FORUM_COLLECTION)
                .orderBy("likeCount", Query.Direction.DESCENDING)
                .limit(limit)
                .get()
                .await()

            val posts = snapshot.documents.mapNotNull { doc ->
                mapDocumentToPost(doc)
            }
            Result.success(posts)
        } catch (e: Exception) {
            Log.w(TAG, "Error fetching forum highlights: ${e.message}")
            Result.failure(e)
        }
    }

    private fun mapDocumentToPost(doc: DocumentSnapshot): ForumPostEntity? {
        val postId = doc.getString("postId") ?: doc.id
        val courseId = doc.getString("courseId") ?: return null
        val authorId = doc.getString("authorId") ?: "unknown"
        val authorName = doc.getString("authorName") ?: "Siswa"
        val content = doc.getString("content") ?: ""
        val timestamp = doc.getLong("timestamp") ?: System.currentTimeMillis()
        val likeCount = (doc.getLong("likeCount") ?: 0L).toInt().coerceAtLeast(0)
        val replyToPostId = doc.getString("replyToPostId")
        val replyToAuthorName = doc.getString("replyToAuthorName")
        val replyToContentSnippet = doc.getString("replyToContentSnippet")
        val authorLevel = (doc.getLong("authorLevel") ?: 1L).toInt().coerceAtLeast(1)
        val authorIsMentor = doc.getBoolean("isMentor") ?: false

        return ForumPostEntity(
            postId = postId,
            courseId = courseId,
            authorId = authorId,
            authorName = authorName,
            content = content,
            timestamp = timestamp,
            likeCount = likeCount,
            isLikedByMe = false,
            replyToPostId = replyToPostId,
            replyToAuthorName = replyToAuthorName,
            replyToContentSnippet = replyToContentSnippet,
            authorLevel = authorLevel,
            authorIsMentor = authorIsMentor
        )
    }

    // ==========================================
    // 4. PROJECT SUBMISSIONS (MOBILE APP <-> FACILITATOR WEB)
    // ==========================================

    fun buildSubmissionDocId(accountId: Long, courseId: String): String = "${accountId}_${courseId}"

    suspend fun upsertProjectSubmission(submission: ProjectSubmissionEntity): Result<Unit> {
        val firestore = getFirestore() ?: return Result.failure(IllegalStateException("Firestore tidak tersedia"))
        return try {
            val docId = buildSubmissionDocId(submission.accountId, submission.courseId)
            val data = mapOf(
                "submissionId" to submission.submissionId,
                "accountId" to submission.accountId,
                "courseId" to submission.courseId,
                "studentName" to submission.studentName,
                "studentEmail" to submission.studentEmail,
                "courseTitle" to submission.courseTitle,
                "finalQuizScore" to submission.finalQuizScore,
                "status" to submission.status,
                "workspaceCode" to submission.workspaceCode,
                "projectUrl" to submission.projectUrl,
                "studentNotes" to submission.studentNotes,
                "checkedRubricIdsJson" to submission.checkedRubricIdsJson,
                "attemptNumber" to submission.attemptNumber,
                "submittedAt" to submission.submittedAt,
                "reviewedAt" to submission.reviewedAt,
                "facilitatorName" to submission.facilitatorName,
                "facilitatorFeedback" to submission.facilitatorFeedback,
                "facilitatorScore" to submission.facilitatorScore,
                "rubricScoresJson" to submission.rubricScoresJson,
                "updatedAt" to System.currentTimeMillis()
            )
            firestore.collection(PROJECT_SUBMISSIONS_COLLECTION)
                .document(docId)
                .set(data, SetOptions.merge())
                .await()
            Result.success(Unit)
        } catch (e: Exception) {
            Log.w(TAG, "Error upserting project submission: ${e.message}")
            Result.failure(e)
        }
    }

    suspend fun fetchProjectSubmission(accountId: Long, courseId: String): Result<ProjectSubmissionEntity?> {
        val firestore = getFirestore() ?: return Result.failure(IllegalStateException("Firestore tidak tersedia"))
        return try {
            val docId = buildSubmissionDocId(accountId, courseId)
            val doc = firestore.collection(PROJECT_SUBMISSIONS_COLLECTION)
                .document(docId)
                .get()
                .await()
            if (doc.exists()) {
                Result.success(mapDocumentToSubmission(doc))
            } else {
                Result.success(null)
            }
        } catch (e: Exception) {
            Log.w(TAG, "Error fetching project submission: ${e.message}")
            Result.failure(e)
        }
    }

    fun attachProjectSubmissionListener(
        accountId: Long,
        courseId: String,
        onUpdate: (ProjectSubmissionEntity) -> Unit,
        onError: (Exception) -> Unit = {}
    ): ListenerRegistration? {
        val firestore = getFirestore()
        if (firestore == null) {
            onError(IllegalStateException("Firestore tidak tersedia"))
            return null
        }
        return try {
            val docId = buildSubmissionDocId(accountId, courseId)
            firestore.collection(PROJECT_SUBMISSIONS_COLLECTION)
                .document(docId)
                .addSnapshotListener { snapshot, error ->
                    if (error != null) {
                        Log.w(TAG, "ProjectSubmission listener error: ${error.message}")
                        onError(error)
                        return@addSnapshotListener
                    }
                    if (snapshot != null && snapshot.exists()) {
                        mapDocumentToSubmission(snapshot)?.let { onUpdate(it) }
                    }
                }
        } catch (e: Exception) {
            Log.w(TAG, "Failed to attach project submission listener: ${e.message}")
            onError(e)
            null
        }
    }

    private fun mapDocumentToSubmission(doc: DocumentSnapshot): ProjectSubmissionEntity? {
        val accountId = doc.getLong("accountId") ?: return null
        val courseId = doc.getString("courseId") ?: return null
        val submissionId = doc.getString("submissionId") ?: doc.id
        val studentName = doc.getString("studentName") ?: "Siswa"
        val studentEmail = doc.getString("studentEmail") ?: ""
        val courseTitle = doc.getString("courseTitle") ?: courseId
        val finalQuizScore = (doc.getLong("finalQuizScore") ?: 0L).toInt()
        val status = doc.getString("status") ?: "DRAFT"
        val workspaceCode = doc.getString("workspaceCode") ?: ""
        val projectUrl = doc.getString("projectUrl") ?: ""
        val studentNotes = doc.getString("studentNotes") ?: ""
        val checkedRubricIdsJson = doc.getString("checkedRubricIdsJson") ?: "[]"
        val attemptNumber = (doc.getLong("attemptNumber") ?: 1L).toInt().coerceAtLeast(1)
        val submittedAt = doc.getLong("submittedAt") ?: 0L
        val reviewedAt = doc.getLong("reviewedAt")
        val facilitatorName = doc.getString("facilitatorName")
        val facilitatorFeedback = doc.getString("facilitatorFeedback")
        val facilitatorScore = doc.getLong("facilitatorScore")?.toInt()
        val rubricScoresJson = doc.getString("rubricScoresJson") ?: "{}"

        return ProjectSubmissionEntity(
            accountId = accountId,
            courseId = courseId,
            submissionId = submissionId,
            studentName = studentName,
            studentEmail = studentEmail,
            courseTitle = courseTitle,
            finalQuizScore = finalQuizScore,
            status = status,
            workspaceCode = workspaceCode,
            projectUrl = projectUrl,
            studentNotes = studentNotes,
            checkedRubricIdsJson = checkedRubricIdsJson,
            attemptNumber = attemptNumber,
            submittedAt = submittedAt,
            reviewedAt = reviewedAt,
            facilitatorName = facilitatorName,
            facilitatorFeedback = facilitatorFeedback,
            facilitatorScore = facilitatorScore,
            rubricScoresJson = rubricScoresJson
        )
    }
}
