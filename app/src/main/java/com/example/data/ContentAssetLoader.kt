package com.example.data

import android.content.Context
import android.util.Log
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.BufferedReader
import java.io.IOException
import java.io.InputStreamReader

object ContentAssetLoader {
    private const val TAG = "ContentAssetLoader"
    private const val BASE_FOLDER = "content"

    suspend fun loadAll(context: Context): Map<String, String> = withContext(Dispatchers.IO) {
        val resultMap = mutableMapOf<String, String>()
        try {
            val rootList = context.assets.list(BASE_FOLDER)
            if (rootList.isNullOrEmpty()) {
                return@withContext emptyMap()
            }
            traverseAssetFolder(
                context = context,
                currentAssetPath = BASE_FOLDER,
                relativePath = "",
                resultMap = resultMap
            )
        } catch (e: IOException) {
            Log.w(TAG, "assets/$BASE_FOLDER directory not found or unreadable: ${e.message}")
        } catch (e: Exception) {
            Log.e(TAG, "Error loading asset contents: ${e.message}", e)
        }
        resultMap
    }

    private fun traverseAssetFolder(
        context: Context,
        currentAssetPath: String,
        relativePath: String,
        resultMap: MutableMap<String, String>
    ) {
        val items = try {
            context.assets.list(currentAssetPath) ?: emptyArray()
        } catch (e: IOException) {
            emptyArray()
        }

        for (item in items) {
            val nextAssetPath = if (currentAssetPath.isEmpty()) item else "$currentAssetPath/$item"
            val nextRelativePath = if (relativePath.isEmpty()) item else "$relativePath/$item"

            if (item.endsWith(".md", ignoreCase = true)) {
                try {
                    context.assets.open(nextAssetPath).use { inputStream ->
                        val content = BufferedReader(InputStreamReader(inputStream, Charsets.UTF_8)).readText()
                        resultMap[nextRelativePath] = content
                    }
                } catch (e: Exception) {
                    Log.w(TAG, "Failed to read markdown asset: $nextAssetPath", e)
                }
            } else {
                val subItems = try {
                    context.assets.list(nextAssetPath)
                } catch (e: Exception) {
                    null
                }
                if (!subItems.isNullOrEmpty()) {
                    traverseAssetFolder(context, nextAssetPath, nextRelativePath, resultMap)
                }
            }
        }
    }
}
