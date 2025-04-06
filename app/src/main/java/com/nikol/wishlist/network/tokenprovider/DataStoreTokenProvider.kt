package com.nikol.wishlist.network.tokenprovider

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.first
import javax.inject.Inject
import javax.inject.Singleton

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "tokens")

@Singleton
class DataStoreTokenProvider @Inject constructor(
    @ApplicationContext private val context: Context
) : AuthTokenProvider {

    companion object {
        private val ACCESS_TOKEN_KEY = stringPreferencesKey("access_token")
        private val REFRESH_TOKEN_KEY = stringPreferencesKey("refresh_token")
    }

    override suspend fun getAccessToken(): String? {
        return runCatching {
            val preferences = context.dataStore.data.first()
            preferences[ACCESS_TOKEN_KEY]
        }.getOrNull()
    }

    override suspend fun getRefreshToken(): String? {
        return runCatching {
            val preferences = context.dataStore.data.first()
            preferences[REFRESH_TOKEN_KEY]
        }.getOrNull()
    }

    override suspend fun saveTokens(accessToken: String, refreshToken: String) {
        context.dataStore.edit { preferences ->
            preferences[ACCESS_TOKEN_KEY] = accessToken
            preferences[REFRESH_TOKEN_KEY] = refreshToken
        }
    }

    override suspend fun clearTokens() {
        context.dataStore.edit { preferences ->
            preferences.remove(ACCESS_TOKEN_KEY)
            preferences.remove(REFRESH_TOKEN_KEY)
        }
    }
}