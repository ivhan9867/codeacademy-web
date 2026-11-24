package com.example

import android.app.Application
import android.content.Context
import coil.ImageLoader
import coil.ImageLoaderFactory
import coil.disk.DiskCache
import coil.memory.MemoryCache
import com.google.firebase.FirebaseApp
import com.google.firebase.appcheck.AppCheckProviderFactory
import com.google.firebase.appcheck.FirebaseAppCheck
import com.google.firebase.appcheck.playintegrity.PlayIntegrityAppCheckProviderFactory

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch
import com.example.data.CourseCatalog

/**
 * Custom Application class that configures:
 * 1. Global ImageLoader with lightweight 12% memory cache allocation (with weak references to prevent background process kill) and disk cache.
 * 2. Firebase App Check with Debug provider for development and Play Integrity provider for production.
 * 3. Application-level CoroutineScope tied to the app lifecycle for background tasks.
 */
class CodeAcademyApplication : Application(), ImageLoaderFactory {

    val applicationScope: CoroutineScope = CoroutineScope(SupervisorJob() + Dispatchers.IO)

    override fun onCreate() {
        super.onCreate()
        initFirebaseAppCheck(this)
        val initialLang = com.example.data.local.SessionManager(this).getCachedLanguageCode()
        CourseCatalog.loadCatalogSync(this, initialLang)
        applicationScope.launch {
            CourseCatalog.loadAssetCourses(this@CodeAcademyApplication, initialLang)
        }
    }

    override fun newImageLoader(): ImageLoader {
        return ImageLoader.Builder(this)
            .memoryCache {
                MemoryCache.Builder(this)
                    // Allocate 12% of available app memory (reduced from 25%) and disable strong references
                    // so bitmaps can be reclaimed by GC during memory pressure, preventing the OS
                    // from aggressively killing the process when kept in background.
                    .maxSizePercent(0.12)
                    .strongReferencesEnabled(false)
                    .build()
            }
            .diskCache {
                DiskCache.Builder()
                    .directory(cacheDir.resolve("image_cache"))
                    .maxSizeBytes(50L * 1024 * 1024) // 50 MB disk cache
                    .build()
            }
            .crossfade(true)
            .respectCacheHeaders(false)
            .build()
    }

    companion object {
        fun initFirebaseAppCheck(context: Context) {
            try {
                val app = try {
                    FirebaseApp.initializeApp(context)
                } catch (_: Throwable) {
                    null
                } ?: try {
                    if (FirebaseApp.getApps(context).isNotEmpty()) FirebaseApp.getInstance() else null
                } catch (_: Throwable) {
                    null
                }

                if (app != null) {
                    val appCheck = FirebaseAppCheck.getInstance(app)
                    if (BuildConfig.DEBUG) {
                        try {
                            val clazz = Class.forName("com.google.firebase.appcheck.debug.DebugAppCheckProviderFactory")
                            val method = clazz.getMethod("getInstance")
                            val factory = method.invoke(null) as AppCheckProviderFactory
                            appCheck.installAppCheckProviderFactory(factory)
                        } catch (_: Throwable) {
                            // Debug provider not available
                        }
                    } else {
                        try {
                            appCheck.installAppCheckProviderFactory(
                                PlayIntegrityAppCheckProviderFactory.getInstance()
                            )
                        } catch (_: Throwable) {
                            // Fallback provider if integrity key not bound
                        }
                    }
                }
            } catch (_: Throwable) {
                // Ignore initialization failures in test or unsupported environments
            }
        }
    }
}
