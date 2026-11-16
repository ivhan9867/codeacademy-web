package com.example.util

import android.content.Context
import android.content.pm.PackageManager
import android.content.pm.Signature
import android.os.Build
import java.security.MessageDigest

data class SigningFingerprintInfo(
    val packageName: String,
    val sha1Colon: String,
    val sha256Colon: String,
    val matchesRepoDebugSha1: Boolean
)

/**
 * Reads runtime APK signing certificate hashes (SHA-1 and SHA-256) so certificate mismatch
 * errors during Firebase Google Auth can be diagnosed directly on any build environment.
 */
object SigningCertificateHelper {
    private val KNOWN_SHA1_HEXES = setOf(
        "7df71a3caee3fe38849700587b524c7831e6227c",
        "55ad7d4a51f957ba87f674ca9b25c491ad810b5d"
    )

    fun getSigningFingerprints(context: Context): SigningFingerprintInfo {
        val packageName = context.packageName
        val signature = readFirstSignature(context, packageName)
        if (signature == null) {
            return SigningFingerprintInfo(
                packageName = packageName,
                sha1Colon = "Unavailable",
                sha256Colon = "Unavailable",
                matchesRepoDebugSha1 = false
            )
        }
        val certBytes = signature.toByteArray()
        val sha1Bytes = MessageDigest.getInstance("SHA-1").digest(certBytes)
        val sha256Bytes = MessageDigest.getInstance("SHA-256").digest(certBytes)
        val sha1Raw = sha1Bytes.joinToString("") { "%02x".format(it) }
        return SigningFingerprintInfo(
            packageName = packageName,
            sha1Colon = sha1Bytes.joinToString(":") { "%02X".format(it) },
            sha256Colon = sha256Bytes.joinToString(":") { "%02X".format(it) },
            matchesRepoDebugSha1 = KNOWN_SHA1_HEXES.any { it.equals(sha1Raw, ignoreCase = true) }
        )
    }

    @Suppress("DEPRECATION")
    private fun readFirstSignature(context: Context, packageName: String): Signature? {
        return runCatching {
            val pm = context.packageManager
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
                val info = pm.getPackageInfo(packageName, PackageManager.GET_SIGNING_CERTIFICATES)
                val signingInfo = info.signingInfo
                if (signingInfo != null) {
                    val certs = if (signingInfo.hasMultipleSigners()) {
                        signingInfo.apkContentsSigners
                    } else {
                        signingInfo.signingCertificateHistory
                    }
                    certs?.firstOrNull()
                } else {
                    null
                }
            } else {
                pm.getPackageInfo(packageName, PackageManager.GET_SIGNATURES)
                    .signatures
                    ?.firstOrNull()
            }
        }.getOrNull()
    }
}
