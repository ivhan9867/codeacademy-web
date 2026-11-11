package com.example.util

import java.security.MessageDigest
import java.security.SecureRandom
import javax.crypto.SecretKeyFactory
import javax.crypto.spec.PBEKeySpec

/**
 * Robust password hashing and verification utility using PBKDF2WithHmacSHA256
 * with unique cryptographically secure 16-byte random salts per account.
 */
object PasswordHasher {
    private const val DEFAULT_ITERATIONS = 10000
    private const val DEFAULT_KEY_LENGTH = 256
    private const val ALGORITHM = "PBKDF2WithHmacSHA256"

    /**
     * Generates a cryptographically secure random salt of the given byte length,
     * returned as a hexadecimal string.
     */
    fun generateSalt(byteLength: Int = 16): String {
        val random = SecureRandom()
        val saltBytes = ByteArray(byteLength)
        random.nextBytes(saltBytes)
        return saltBytes.joinToString("") { "%02x".format(it) }
    }

    /**
     * Computes a PBKDF2WithHmacSHA256 hash string for the given password and salt.
     * If salt is empty (legacy accounts prior to salt migration), falls back to legacy SHA-256.
     */
    fun hashPassword(
        password: String,
        saltHex: String = "",
        iterations: Int = DEFAULT_ITERATIONS,
        keyLength: Int = DEFAULT_KEY_LENGTH
    ): String {
        if (saltHex.isEmpty()) {
            return hashLegacySha256(password)
        }
        val saltBytes = hexToByteArray(saltHex)
        val spec = PBEKeySpec(password.toCharArray(), saltBytes, iterations, keyLength)
        val factory = SecretKeyFactory.getInstance(ALGORITHM)
        val hashBytes = factory.generateSecret(spec).encoded
        return hashBytes.joinToString("") { "%02x".format(it) }
    }

    /**
     * Verifies raw password against the stored hash using the user's passwordSalt.
     * If salt is empty, compares against legacy SHA-256 for backward compatibility.
     */
    fun verifyPassword(password: String, storedHash: String, saltHex: String = ""): Boolean {
        if (saltHex.isEmpty()) {
            val legacyHash = hashLegacySha256(password)
            return slowEquals(legacyHash, storedHash)
        }
        val computedHash = hashPassword(password, saltHex)
        return slowEquals(computedHash, storedHash)
    }

    /**
     * Constant-time string comparison to prevent timing side-channel attacks.
     */
    private fun slowEquals(a: String, b: String): Boolean {
        val aBytes = a.toByteArray(Charsets.UTF_8)
        val bBytes = b.toByteArray(Charsets.UTF_8)
        var diff = aBytes.size xor bBytes.size
        for (i in 0 until minOf(aBytes.size, bBytes.size)) {
            diff = diff or (aBytes[i].toInt() xor bBytes[i].toInt())
        }
        return diff == 0
    }

    /**
     * Legacy SHA-256 hash without salt for migration and backward compatibility.
     */
    fun hashLegacySha256(password: String): String {
        val digest = MessageDigest.getInstance("SHA-256")
        val hashBytes = digest.digest(password.toByteArray(Charsets.UTF_8))
        return hashBytes.joinToString("") { "%02x".format(it) }
    }

    private fun hexToByteArray(hex: String): ByteArray {
        val len = hex.length
        val data = ByteArray(len / 2)
        var i = 0
        while (i < len) {
            data[i / 2] = ((Character.digit(hex[i], 16) shl 4) + Character.digit(hex[i + 1], 16)).toByte()
            i += 2
        }
        return data
    }
}
