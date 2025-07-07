package com.nikol.wishlist.core.data.network

import com.nikol.wishlist.core.domain.tokenprovider.AuthTokenProvider
import com.nikol.wishlist.core.domain.auth.AuthInteractor
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AuthInteractorImpl @Inject constructor(
    private val tokenProvider: AuthTokenProvider,
    private val authApiService: AuthApiService,
): AuthInteractor {

    override suspend fun isUserLogin(): Boolean {
        val accessToken = tokenProvider.getAccessToken()
        val refreshToken = tokenProvider.getRefreshToken()
        return accessToken != null && refreshToken != null
    }

    override suspend fun logout() {
        tokenProvider.clearTokens()
    }

    override suspend fun register(
        username: String,
        password: String,
        email: String
    ): Boolean {
        val registerBody = RegisterBody(username, password, email)

        return try {
            val response = authApiService.register(registerBody)
            tokenProvider.saveTokens(response.accessToken, response.refreshToken)
            true
        } catch (e: Exception) {
            false
        }
    }

    override suspend fun login(
        username: String,
        password: String
    ): Boolean {
        return try {
            val response = authApiService.login(LoginBody(username, password))
            tokenProvider.saveTokens(response.accessToken, response.refreshToken)
            true
        } catch (e: Exception) {
            false
        }
    }
}