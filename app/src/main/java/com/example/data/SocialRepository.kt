package com.example.data

import android.content.Context
import com.example.CodeAcademyApplication
import com.example.data.local.AppDatabase
import com.example.data.local.ForumPostDao
import com.example.data.local.ForumPostEntity
import com.example.data.local.LeaderboardDao
import com.example.data.local.LeaderboardEntryCacheEntity
import com.example.data.local.ModuleProgressDao
import com.example.data.local.ModuleProgressEntity
import com.example.data.local.SessionManager
import com.example.data.local.UserDao
import com.example.data.local.UserProfileEntity
import com.example.data.local.WeeklyChallengeDao
import com.example.data.local.WeeklyChallengeEntity
import com.example.data.models.GamificationLevels
import com.example.data.remote.FirestoreForumAndLeaderboardService
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext
import kotlinx.coroutines.withTimeout
import java.util.UUID

class SocialRepository(
    val forumPostDao: ForumPostDao,
    val leaderboardDao: LeaderboardDao,
    val weeklyChallengeDao: WeeklyChallengeDao,
    val externalScope: CoroutineScope,
    val userDao: UserDao? = null,
    val moduleProgressDao: ModuleProgressDao? = null,
    val sessionManager: SessionManager? = null,
    val firestoreService: FirestoreForumAndLeaderboardService = FirestoreForumAndLeaderboardService()
) {

    private val firestore: FirebaseFirestore? by lazy {
        try {
            FirebaseFirestore.getInstance()
        } catch (_: Throwable) {
            null
        }
    }

    private suspend fun getActiveAccountId(): Long {
        return sessionManager?.accountId?.first() ?: 1L
    }

    // ==========================================
    // FORUM POSTS (PER-COURSE)
    // ==========================================

    fun observeCourseForumPosts(courseId: String): Flow<List<ForumPostEntity>> {
        return forumPostDao.getPostsByCourse(courseId)
    }

    fun getForumPosts(courseId: String): Flow<List<ForumPostEntity>> {
        return forumPostDao.getPostsByCourse(courseId)
    }

    suspend fun refreshForumPosts(
        courseId: String,
        limit: Long = 20L
    ): Result<Pair<List<ForumPostEntity>, DocumentSnapshot?>> = withContext(Dispatchers.IO) {
        val result = firestoreService.fetchPosts(courseId, null, limit)
        if (result.isSuccess) {
            val (posts, _) = result.getOrThrow()
            if (posts.isNotEmpty()) {
                forumPostDao.insertPosts(posts)
            } else {
                val localPosts = forumPostDao.getPostsByCourse(courseId).first()
                if (localPosts.isEmpty()) {
                    val seeds = getSeedForumPosts(courseId)
                    forumPostDao.insertPosts(seeds)
                }
            }
            result
        } else {
            val localPosts = forumPostDao.getPostsByCourse(courseId).first()
            if (localPosts.isEmpty()) {
                val seeds = getSeedForumPosts(courseId)
                forumPostDao.insertPosts(seeds)
            }
            result
        }
    }

    suspend fun loadMoreForumPosts(
        courseId: String,
        lastDoc: DocumentSnapshot?,
        limit: Long = 20L
    ): Result<Pair<List<ForumPostEntity>, DocumentSnapshot?>> = withContext(Dispatchers.IO) {
        val result = firestoreService.fetchPosts(courseId, lastDoc, limit)
        if (result.isSuccess) {
            val (posts, _) = result.getOrThrow()
            if (posts.isNotEmpty()) {
                forumPostDao.insertPosts(posts)
            }
        }
        result
    }

    suspend fun createForumPost(
        courseId: String,
        content: String,
        replyToPostId: String? = null,
        replyToAuthorName: String? = null,
        replyToContentSnippet: String? = null
    ): Result<ForumPostEntity> = withContext(Dispatchers.IO) {
        val accountId = getActiveAccountId()
        val user = userDao?.getUserProfileOnce(accountId)
        val authorName = user?.name ?: "Siswa"
        val authorId = accountId.toString()
        val authorLevel = user?.level ?: 1

        val cloudResult = firestoreService.createPost(
            courseId = courseId,
            authorId = authorId,
            authorName = authorName,
            content = content,
            authorLevel = authorLevel,
            replyToPostId = replyToPostId,
            replyToAuthorName = replyToAuthorName,
            replyToContentSnippet = replyToContentSnippet
        )

        val entity = if (cloudResult.isSuccess) {
            cloudResult.getOrThrow()
        } else {
            ForumPostEntity(
                postId = UUID.randomUUID().toString(),
                courseId = courseId,
                authorId = authorId,
                authorName = authorName,
                content = content.trim(),
                timestamp = System.currentTimeMillis(),
                likeCount = 0,
                isLikedByMe = false,
                replyToPostId = replyToPostId,
                replyToAuthorName = replyToAuthorName,
                replyToContentSnippet = replyToContentSnippet,
                authorLevel = authorLevel,
                authorIsMentor = false
            )
        }

        forumPostDao.insertPost(entity)
        Result.success(entity)
    }

    suspend fun toggleLikePost(
        postId: String,
        currentLikes: Int,
        isLiked: Boolean
    ) = withContext(Dispatchers.IO) {
        val newLikes = if (isLiked) currentLikes + 1 else (currentLikes - 1).coerceAtLeast(0)
        forumPostDao.updateLikeStatus(postId, newLikes, isLiked)
        firestoreService.toggleLikePost(postId, isLiked)
    }

    suspend fun toggleLikePost(post: ForumPostEntity) = withContext(Dispatchers.IO) {
        val newLiked = !post.isLikedByMe
        val newCount = (if (newLiked) post.likeCount + 1 else post.likeCount - 1).coerceAtLeast(0)
        forumPostDao.updateLikeStatus(post.postId, newCount, newLiked)
        firestoreService.toggleLikePost(post.postId, newLiked)
    }

    suspend fun syncIncomingForumPosts(
        courseId: String,
        incomingPosts: List<ForumPostEntity>
    ) = withContext(Dispatchers.IO) {
        val existingLikedIds = forumPostDao.getPostsByCourse(courseId).first()
            .filter { it.isLikedByMe }
            .map { it.postId }
            .toSet()
        val merged = incomingPosts.map { post ->
            if (post.postId in existingLikedIds) post.copy(isLikedByMe = true) else post
        }
        forumPostDao.replacePostsForCourse(courseId, merged)
    }

    private fun getSeedForumPosts(courseId: String): List<ForumPostEntity> {
        val now = System.currentTimeMillis()
        val course = CourseCatalog.courses.firstOrNull { it.id == courseId }
        val courseTitle = course?.title ?: "Kelas Ini"
        return listOf(
            ForumPostEntity(
                postId = "seed_${courseId}_1",
                courseId = courseId,
                authorId = "mentor_1",
                authorName = "Mentor Budi 👨‍🏫",
                content = "Selamat datang di Forum Diskusi $courseTitle! Silakan diskusikan materi, ajukan pertanyaan seputar modul, atau bagikan kode solusi kalian di sini.",
                timestamp = now - 86400000L,
                likeCount = 8,
                isLikedByMe = false
            ),
            ForumPostEntity(
                postId = "seed_${courseId}_2",
                courseId = courseId,
                authorId = "peer_2",
                authorName = "Siti Nurhaliza",
                content = "Tips untuk modul praktik: jangan lupa cek debugger/console dan selalu perhatikan sintaks kodenya ya teman-teman!",
                timestamp = now - 18000000L,
                likeCount = 4,
                isLikedByMe = false
            )
        )
    }

    // ==========================================
    // FORUM HIGHLIGHTS
    // ==========================================

    suspend fun getTopForumHighlights(limit: Long = 3L): List<ForumPostEntity> = withContext(Dispatchers.IO) {
        val cloudResult = firestoreService.fetchTopForumHighlights(limit)
        if (cloudResult.isSuccess && cloudResult.getOrThrow().isNotEmpty()) {
            return@withContext cloudResult.getOrThrow()
        }

        listOf(
            ForumPostEntity(
                postId = "highlight_1",
                courseId = "fe-web-foundation-00",
                authorId = "mentor_1",
                authorName = "Mentor Budi 👨‍🏫",
                content = "Tips penting: Gunakan semantic HTML seperti <header>, <main>, dan <footer> agar aplikasi web kamu ramah SEO dan mudah diakses screen reader!",
                timestamp = System.currentTimeMillis() - 7200000L,
                likeCount = 24,
                isLikedByMe = false
            ),
            ForumPostEntity(
                postId = "highlight_2",
                courseId = "fe-css-02",
                authorId = "peer_2",
                authorName = "Clarissa Chen",
                content = "Bagi yang bingung beda Flexbox dan CSS Grid: Flexbox cocok untuk layout 1 dimensi (baris/kolom), sedangkan Grid sangat powerful untuk layout 2 dimensi yang kompleks! 💡",
                timestamp = System.currentTimeMillis() - 14400000L,
                likeCount = 18,
                isLikedByMe = false
            ),
            ForumPostEntity(
                postId = "highlight_3",
                courseId = "py-foundations-01",
                authorId = "peer_1",
                authorName = "Farhan Maulana",
                content = "List comprehension di Python bikin kodenya ringkas banget! Jangan lupa cek efisiensi memori kalau data yang diproses sangat besar ya.",
                timestamp = System.currentTimeMillis() - 28800000L,
                likeCount = 15,
                isLikedByMe = false
            )
        )
    }

    // ==========================================
    // LEADERBOARD (CLOUD DATA + ROOM CACHE)
    // ==========================================

    fun observeLeaderboard(scopeKey: String = "weekly_global"): Flow<List<LeaderboardEntryCacheEntity>> {
        return leaderboardDao.getCachedLeaderboard(scopeKey)
    }

    fun getCachedLeaderboard(scopeKey: String = "weekly_global"): Flow<List<LeaderboardEntryCacheEntity>> {
        return leaderboardDao.getCachedLeaderboard(scopeKey)
    }

    suspend fun refreshLeaderboard(
        period: String = "weekly",
        trackId: String? = null
    ): Result<List<LeaderboardEntryCacheEntity>> = withContext(Dispatchers.IO) {
        val scopeKey = "${period}_${trackId ?: "global"}"
        syncMyProfileToCloud()

        val result = try {
            withTimeout(3500L) {
                firestoreService.fetchTopLeaderboard(period = period, trackId = trackId, limit = 50L)
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
        if (result.isSuccess) {
            val entries = result.getOrThrow()
            if (entries.isNotEmpty()) {
                leaderboardDao.replaceLeaderboard(scopeKey, entries)
                Result.success(entries)
            } else {
                seedDefaultLeaderboardIfEmpty(scopeKey, period, trackId)
                val cached = leaderboardDao.getCachedLeaderboard(scopeKey).first()
                Result.success(cached)
            }
        } else {
            seedDefaultLeaderboardIfEmpty(scopeKey, period, trackId)
            val cached = leaderboardDao.getCachedLeaderboard(scopeKey).first()
            Result.success(cached)
        }
    }

    suspend fun calculateUserRank(
        userXp: Int,
        period: String = "weekly",
        trackId: String? = null
    ): Int = withContext(Dispatchers.IO) {
        val scopeKey = "${period}_${trackId ?: "global"}"
        val accountId = getActiveAccountId().toString()
        val cached = leaderboardDao.getCachedLeaderboard(scopeKey).first()
        val myCachedEntry = cached.find { it.userId == accountId }
        if (myCachedEntry != null) {
            return@withContext myCachedEntry.rank
        }

        val rankResult = firestoreService.calculateUserRank(userXp, period, trackId)
        if (rankResult.isSuccess) {
            rankResult.getOrThrow()
        } else {
            val higherInCache = cached.count { it.totalXp > userXp }
            maxOf(higherInCache + 1, 1)
        }
    }

    private suspend fun syncMyProfileToCloud() = withContext(Dispatchers.IO) {
        try {
            val accountId = getActiveAccountId()
            val profile = userDao?.getUserProfileOnce(accountId)
            if (profile != null) {
                val allProgress = moduleProgressDao?.getAllProgress(accountId)?.first().orEmpty()
                val completedModules = allProgress.count { it.isCompleted }
                val estimatedUnlockedBadges = buildList {
                    if (completedModules >= 1) add("b1")
                    if (completedModules >= 3) add("b2")
                    if (profile.currentStreak >= 3) add("b3")
                    if (profile.totalXp >= 500) add("b4")
                    if (profile.currentStreak >= 7) add("b5")
                    if (profile.level >= 5) add("b8")
                    if (profile.totalXp >= 1000) add("b9")
                }
                firestoreService.syncUserXp(
                    userId = profile.id.toString(),
                    userName = profile.name,
                    totalXp = profile.totalXp,
                    level = profile.level,
                    currentStreak = profile.currentStreak,
                    bio = profile.bio,
                    role = "Siswa",
                    unlockedBadgeIds = estimatedUnlockedBadges
                )
            }
        } catch (e: Exception) {
            android.util.Log.e("SocialRepository", "Gagal sinkronisasi XP pengguna ke Firestore", e)
        }
    }

    suspend fun fetchPublicUserProfile(
        userId: String,
        fallbackName: String = "Siswa",
        fallbackLevel: Int = 1
    ): com.example.data.remote.PublicUserProfile = withContext(Dispatchers.IO) {
        val remote = firestoreService.fetchPublicUserProfile(userId)
        if (remote.isSuccess) {
            return@withContext remote.getOrThrow()
        }
        val activeId = getActiveAccountId()
        if (userId == activeId.toString()) {
            val local = userDao?.getUserProfileOnce(activeId)
            if (local != null) {
                return@withContext com.example.data.remote.PublicUserProfile(
                    userId = userId,
                    name = local.name,
                    bio = local.bio,
                    role = "Siswa",
                    level = local.level,
                    totalXp = local.totalXp,
                    currentStreak = local.currentStreak
                )
            }
        }
        val cachedEntry = leaderboardDao.getAllCachedLeaderboard().first().find { it.userId == userId }
        com.example.data.remote.PublicUserProfile(
            userId = userId,
            name = cachedEntry?.userName ?: fallbackName,
            bio = "Peserta aktif CodeAcademy WebDev Studio.",
            role = if (userId == "facilitator-mentor") "Fasilitator" else "Siswa",
            level = cachedEntry?.level ?: fallbackLevel,
            totalXp = cachedEntry?.totalXp ?: 0
        )
    }

    private suspend fun seedDefaultLeaderboardIfEmpty(
        scopeKey: String,
        period: String,
        trackId: String?
    ) {
        val existing = leaderboardDao.getCachedLeaderboard(scopeKey).first()
        if (existing.isEmpty()) {
            val accountId = getActiveAccountId()
            val currentUser = userDao?.getUserProfileOnce(accountId)
            val baseXp = currentUser?.totalXp ?: 100
            val myLevel = currentUser?.level ?: 1
            val myName = currentUser?.name ?: "Siswa"

            val isStreakMode = period == "streak"
            val multiplier = when (period) {
                "weekly" -> 0.35f
                "monthly" -> 0.75f
                else -> 1.0f
            }

            val trackPrefix = if (trackId != null && trackId != "global") " [$trackId]" else ""

            val basePeers = if (isStreakMode) {
                mutableListOf(
                    LeaderboardEntryCacheEntity(scopeKey, "peer_1", "Farhan Maulana$trackPrefix", 42, 7, 1),
                    LeaderboardEntryCacheEntity(scopeKey, "peer_2", "Siti Nurhaliza$trackPrefix", 35, 6, 2),
                    LeaderboardEntryCacheEntity(scopeKey, "peer_4", "Andi Wijaya$trackPrefix", 28, 4, 3),
                    LeaderboardEntryCacheEntity(scopeKey, "peer_5", "Clarissa Chen$trackPrefix", 21, 4, 4),
                    LeaderboardEntryCacheEntity(scopeKey, "peer_6", "Bima Nugraha$trackPrefix", 14, 3, 5),
                    LeaderboardEntryCacheEntity(scopeKey, "peer_7", "Alya Zahra$trackPrefix", 10, 3, 6),
                    LeaderboardEntryCacheEntity(scopeKey, "peer_8", "Dimas Pratama$trackPrefix", 7, 2, 7),
                    LeaderboardEntryCacheEntity(scopeKey, "peer_9", "Tiara Amanda$trackPrefix", 5, 2, 8),
                    LeaderboardEntryCacheEntity(scopeKey, "peer_10", "Reza Rahardian$trackPrefix", 3, 2, 9),
                )
            } else {
                mutableListOf(
                    LeaderboardEntryCacheEntity(scopeKey, "peer_1", "Farhan Maulana$trackPrefix", (3450 * multiplier).toInt() + 20, 7, 1),
                    LeaderboardEntryCacheEntity(scopeKey, "peer_2", "Siti Nurhaliza$trackPrefix", (2980 * multiplier).toInt() + 15, 6, 2),
                    LeaderboardEntryCacheEntity(scopeKey, "peer_4", "Andi Wijaya$trackPrefix", (1950 * multiplier).toInt() + 10, 4, 3),
                    LeaderboardEntryCacheEntity(scopeKey, "peer_5", "Clarissa Chen$trackPrefix", (1720 * multiplier).toInt() + 10, 4, 4),
                    LeaderboardEntryCacheEntity(scopeKey, "peer_6", "Bima Nugraha$trackPrefix", (1540 * multiplier).toInt() + 5, 3, 5),
                    LeaderboardEntryCacheEntity(scopeKey, "peer_7", "Alya Zahra$trackPrefix", (1310 * multiplier).toInt() + 5, 3, 6),
                    LeaderboardEntryCacheEntity(scopeKey, "peer_8", "Dimas Pratama$trackPrefix", (1120 * multiplier).toInt() + 5, 2, 7),
                    LeaderboardEntryCacheEntity(scopeKey, "peer_9", "Tiara Amanda$trackPrefix", (980 * multiplier).toInt() + 5, 2, 8),
                    LeaderboardEntryCacheEntity(scopeKey, "peer_10", "Reza Rahardian$trackPrefix", (760 * multiplier).toInt() + 5, 2, 9),
                )
            }
            val userScore = if (isStreakMode) {
                currentUser?.currentStreak ?: 0
            } else {
                (baseXp * multiplier).toInt().coerceAtLeast(20)
            }
            basePeers.add(
                LeaderboardEntryCacheEntity(
                    scopeKey = scopeKey,
                    userId = accountId.toString(),
                    userName = myName,
                    totalXp = userScore,
                    level = myLevel,
                    rank = 10
                )
            )
            val sorted = basePeers.sortedByDescending { it.totalXp }.mapIndexed { idx, it ->
                it.copy(rank = idx + 1)
            }
            leaderboardDao.replaceLeaderboard(scopeKey, sorted)
        }
    }

    // ==========================================
    // WEEKLY CHALLENGES
    // ==========================================

    val activeWeeklyChallenge: Flow<WeeklyChallengeEntity?> = weeklyChallengeDao.getActiveWeeklyChallenge()

    fun observeActiveWeeklyChallenge(): Flow<WeeklyChallengeEntity?> {
        return weeklyChallengeDao.getActiveWeeklyChallenge()
    }

    suspend fun claimWeeklyChallengeReward(challengeId: String): Boolean = withContext(Dispatchers.IO) {
        val challenge = weeklyChallengeDao.getActiveWeeklyChallengeOnce() ?: return@withContext false
        if (challenge.id == challengeId && !challenge.isClaimed) {
            weeklyChallengeDao.markClaimed(challengeId)
            val accountId = getActiveAccountId()
            val user = userDao?.getUserProfileOnce(accountId)
            if (user != null) {
                val newXp = user.totalXp + challenge.rewardXp
                val newLevel = GamificationLevels.getLevelForXp(newXp).level
                userDao?.updateXpAndLevel(accountId, newXp, newLevel)
            }
            return@withContext true
        }
        false
    }

    suspend fun claimWeeklyChallenge(challengeId: String) = withContext(Dispatchers.IO) {
        weeklyChallengeDao.markClaimed(challengeId)
    }

    // ==========================================
    // USER & MODULE DATA ACCESS
    // ==========================================

    @OptIn(ExperimentalCoroutinesApi::class)
    fun observeModuleProgressList(): Flow<List<ModuleProgressEntity>> {
        val manager = sessionManager ?: return flowOf(emptyList())
        val dao = moduleProgressDao ?: return flowOf(emptyList())
        return manager.accountId.flatMapLatest { id ->
            if (id != null) dao.getAllProgress(id) else flowOf(emptyList())
        }
    }

    suspend fun getUserProfileOnce(): UserProfileEntity? = withContext(Dispatchers.IO) {
        val accountId = getActiveAccountId()
        userDao?.getUserProfileOnce(accountId)
    }

    companion object {
        @Volatile
        private var INSTANCE: SocialRepository? = null

        fun getInstance(context: Context): SocialRepository {
            return INSTANCE ?: synchronized(this) {
                INSTANCE ?: run {
                    val appContext = context.applicationContext
                    val db = AppDatabase.getDatabase(appContext)
                    val app = appContext as? CodeAcademyApplication
                    val scope = app?.applicationScope
                        ?: CoroutineScope(SupervisorJob() + Dispatchers.IO)
                    SocialRepository(
                        forumPostDao = db.forumPostDao(),
                        leaderboardDao = db.leaderboardDao(),
                        weeklyChallengeDao = db.weeklyChallengeDao(),
                        externalScope = scope,
                        userDao = db.userDao(),
                        moduleProgressDao = db.moduleProgressDao(),
                        sessionManager = SessionManager(appContext),
                        firestoreService = FirestoreForumAndLeaderboardService()
                    ).also { INSTANCE = it }
                }
            }
        }
    }
}
