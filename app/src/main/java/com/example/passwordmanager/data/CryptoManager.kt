package com.example.passwordmanager.data

import android.content.Context
import android.security.keystore.KeyGenParameterSpec
import android.security.keystore.KeyProperties
import android.security.keystore.KeyProperties.BLOCK_MODE_CBC
import androidx.core.content.edit

import java.security.KeyStore
import android.util.Base64
import java.security.SecureRandom
import javax.crypto.Cipher
import javax.crypto.KeyGenerator
import javax.crypto.SecretKey
import javax.crypto.spec.IvParameterSpec

private const val IV_SIZE = 16
private const val KEY_IV = "iv"
private const val AES_KEY_ALIAS = "secret"

class CryptoManager(private val context: Context) {
    private val prefs = context.getSharedPreferences("ivPrefs", Context.MODE_PRIVATE)
    private val keyStore = KeyStore.getInstance("AndroidKeyStore").apply {
        load(null)
    }

    private fun encrypt(text: String): ByteArray {
        val key = getKey()
        val iv = getIv()
        val cipher = Cipher.getInstance("AES/CBC/PKCS7Padding", "AndroidKeyStoreBCWorkaround")
        cipher.init(Cipher.ENCRYPT_MODE, key, iv)
        return cipher.doFinal(text.toBase64())
    }

    private fun getKey(): SecretKey {
        val existingKey = keyStore.getEntry(AES_KEY_ALIAS, null) as? KeyStore.SecretKeyEntry
        return existingKey?.secretKey ?: createKey()
    }

    private fun getIv(): IvParameterSpec {
        var ivBytes = prefs.getString(KEY_IV, null)?.toBase64()
        val iv = if (ivBytes != null) {
            IvParameterSpec(ivBytes)
        } else {
            ivBytes = ByteArray(IV_SIZE)
            SecureRandom().nextBytes(ivBytes)
            prefs.edit {
                putString(KEY_IV, ivBytes.encodeToString())
            }
            IvParameterSpec(ivBytes)
        }
        return iv
    }

    private fun createKey(): SecretKey {
        return KeyGenerator.getInstance(ALGORITHM).apply {
            init(
                KeyGenParameterSpec.Builder(
                    AES_KEY_ALIAS,
                    KeyProperties.PURPOSE_ENCRYPT or KeyProperties.PURPOSE_DECRYPT)
                    .setBlockModes(BLOCK_MODE_CBC)
                    .setKeySize(256)
                    .setEncryptionPaddings(PADDING)
                    .setUserAuthenticationRequired(false)
                    .setRandomizedEncryptionRequired(false)
                    .build()
            )
        }.generateKey()
    }

    fun createPassword(): String {
        val password = ByteArray(IV_SIZE)
        SecureRandom().nextBytes(password)

        return encrypt(password.encodeToString()).encodeToString()
    }

    fun decrypt(encryptedText: String): String {
        val key = getKey()
        val iv = getIv()
        val cipher = Cipher.getInstance(TRANSFORMATION, "AndroidKeyStoreBCWorkaround")
        cipher.init(Cipher.DECRYPT_MODE, key, iv)
        val encryptedTextByte = encryptedText.toBase64()
        return cipher.doFinal(encryptedTextByte).encodeToString()
    }


    companion object {
        private const val ALGORITHM = KeyProperties.KEY_ALGORITHM_AES
        private const val BLOCK_MODE = KeyProperties.BLOCK_MODE_CBC
        private const val PADDING = KeyProperties.ENCRYPTION_PADDING_PKCS7
        private const val TRANSFORMATION = "$ALGORITHM/$BLOCK_MODE/$PADDING"
        fun create(context: Context): CryptoManager {
            return CryptoManager(context = context)
        }
    }

    private fun String.toBase64(): ByteArray {
        return Base64.decode(this, Base64.NO_WRAP or Base64.NO_PADDING)
    }

    private fun ByteArray.encodeToString(): String {
        return Base64.encodeToString(this, Base64.NO_WRAP or Base64.NO_PADDING)
    }
}