package com.nikol.wishlist.domain.auth

import com.nikol.wishlist.network.AuthApiService
import com.nikol.wishlist.network.LoginBody
import com.nikol.wishlist.network.RegisterBody
import com.nikol.wishlist.network.UserApiService
import com.nikol.wishlist.network.tokenprovider.AuthTokenProvider
import javax.inject.Inject
import javax.inject.Singleton


@Singleton
class AuthInteractor @Inject constructor(
    private val tokenProvider: AuthTokenProvider,
    private val authApiService: AuthApiService,
) {

    suspend fun isUserLogin(): Boolean {
        val accessToken = tokenProvider.getAccessToken()
        val refreshToken = tokenProvider.getRefreshToken()
        return accessToken != null && refreshToken != null
    }

    suspend fun logout() {
        tokenProvider.clearTokens()
    }

    suspend fun register(
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

    suspend fun login(
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