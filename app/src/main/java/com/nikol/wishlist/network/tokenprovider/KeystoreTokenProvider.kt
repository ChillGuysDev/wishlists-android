package com.nikol.wishlist.network.tokenprovider

import android.content.Context
import android.security.keystore.KeyGenParameterSpec
import android.security.keystore.KeyProperties
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import java.security.KeyStore
import java.util.Base64
import javax.crypto.Cipher
import javax.crypto.KeyGenerator
import javax.crypto.SecretKey
import javax.crypto.spec.GCMParameterSpec
import javax.inject.Inject
import javax.inject.Singleton

// Extension property for DataStore
private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "secure_tokens")

@Singleton
class KeystoreTokenProvider @Inject constructor(
    @ApplicationContext private val context: Context,
) : AuthTokenProvider {

    companion object {
        private const val ANDROID_KEYSTORE = "AndroidKeyStore"
        private const val KEY_ALIAS = "auth_token_key"
        private const val ACCESS_TOKEN_KEY = "encrypted_access_token"
        private const val REFRESH_TOKEN_KEY = "encrypted_refresh_token"
        private const val ACCESS_IV_KEY = "access_token_iv"
        private const val REFRESH_IV_KEY = "refresh_token_iv"

        private const val cipherTransformation = "AES/GCM/NoPadding"
    }

    private val accessTokenKey = stringPreferencesKey(ACCESS_TOKEN_KEY)
    private val refreshTokenKey = stringPreferencesKey(REFRESH_TOKEN_KEY)
    private val accessIvKey = stringPreferencesKey(ACCESS_IV_KEY)
    private val refreshIvKey = stringPreferencesKey(REFRESH_IV_KEY)

    private fun getKey(): SecretKey {
        val keyStore = KeyStore.getInstance(ANDROID_KEYSTORE)
        keyStore.load(null)
        val entry = keyStore.getEntry(KEY_ALIAS, null) as KeyStore.SecretKeyEntry
        return entry.secretKey
    }

    private fun encrypt(text: String): Pair<String, String> {
        val cipher = Cipher.getInstance(cipherTransformation)
        cipher.init(Cipher.ENCRYPT_MODE, getKey())

        val iv = cipher.iv
        val encryptedBytes = cipher.doFinal(text.toByteArray())

        return Pair(
            Base64.getEncoder().encodeToString(encryptedBytes),
            Base64.getEncoder().encodeToString(iv)
        )
    }

    private fun decrypt(encryptedText: String, ivString: String): String {
        val encryptedBytes = Base64.getDecoder().decode(encryptedText)
        val iv = Base64.getDecoder().decode(ivString)

        val cipher = Cipher.getInstance(cipherTransformation)
        cipher.init(Cipher.DECRYPT_MODE, getKey(), GCMParameterSpec(128, iv))

        val decryptedBytes = cipher.doFinal(encryptedBytes)
        return String(decryptedBytes)
    }

    override fun getAccessToken(): String? {
        return runBlocking {
            try {
                val preferences = context.dataStore.data.first()
                val encryptedToken = preferences[accessTokenKey] ?: return@runBlocking null
                val iv = preferences[accessIvKey] ?: return@runBlocking null

                decrypt(encryptedToken, iv)
            } catch (e: Exception) {
                null
            }
        }
    }

    override fun getRefreshToken(): String? {
        return runBlocking {
            try {
                val preferences = context.dataStore.data.first()
                val encryptedToken = preferences[refreshTokenKey] ?: return@runBlocking null
                val iv = preferences[refreshIvKey] ?: return@runBlocking null

                decrypt(encryptedToken, iv)
            } catch (e: Exception) {
                null
            }
        }
    }

    override fun saveTokens(accessToken: String, refreshToken: String) {
        runBlocking {
            context.dataStore.edit { preferences ->
                val (encryptedAccessToken, accessIv) = encrypt(accessToken)
                val (encryptedRefreshToken, refreshIv) = encrypt(refreshToken)

                preferences[accessTokenKey] = encryptedAccessToken
                preferences[accessIvKey] = accessIv
                preferences[refreshTokenKey] = encryptedRefreshToken
                preferences[refreshIvKey] = refreshIv
            }
        }
    }

    override fun clearTokens() {
        runBlocking {
            context.dataStore.edit { preferences ->
                preferences.remove(accessTokenKey)
                preferences.remove(accessIvKey)
                preferences.remove(refreshTokenKey)
                preferences.remove(refreshIvKey)
            }
        }
    }
}
