package com.example.util

import android.content.Context
import coil.imageLoader
import coil.request.ImageRequest
import com.example.R
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

object ImageCacheHelper {

    /**
     * Preloads and decodes heavy initial images into Coil's 25% memory cache.
     * Calling this while SplashScreen is active eliminates first-frame decode lag on HomeScreen.
     */
    suspend fun preloadAppImages(context: Context) = withContext(Dispatchers.IO) {
        val imageLoader = context.imageLoader
        val imagesToWarm = listOf(
            R.drawable.hero_banner,
            R.drawable.ic_codeacademy_logo
        )

        for (resId in imagesToWarm) {
            try {
                val request = ImageRequest.Builder(context)
                    .data(resId)
                    .memoryCacheKey("res_$resId")
                    .build()
                imageLoader.execute(request)
            } catch (_: Exception) {
                // Ignore silent preloading exceptions
            }
        }
    }
}
