package com.nikol.wishlist.ui.features.home.domain

import com.nikol.wishlist.network.AuthApiService
import com.nikol.wishlist.network.LoginBody
import com.nikol.wishlist.network.RegisterBody
import com.nikol.wishlist.network.tokenprovider.KeystoreTokenProvider
import com.nikol.wishlist.ui.features.home.data.WishlistsRepository
import kotlinx.coroutines.delay
import javax.inject.Inject
import kotlin.collections.orEmpty
import kotlin.collections.plus

class HomeManager @Inject constructor(
    private val repository: WishlistsRepository,
    private val tokenProvider: KeystoreTokenProvider,
) {
    private val savedWishlists = mutableListOf<WishlistDomain>()
    suspend fun getStarredWishlists() = repository.getStarredWishlists().orEmpty() + savedWishlists

    suspend fun saveWishlist(wishlist: WishlistDomain): Boolean {
        delay(300)
        return savedWishlists.add(wishlist)
    }

    fun isUserLogin(): Boolean {
        val accessToken = tokenProvider.getAccessToken()
        val refreshToken = tokenProvider.getRefreshToken()
        return accessToken != null && refreshToken != null
    }
}


class AuthManager @Inject constructor(
    private val tokenProvider: KeystoreTokenProvider,
    private val authApiService: AuthApiService,
) {
    fun isUserLogin(): Boolean {
        val accessToken = tokenProvider.getAccessToken()
        val refreshToken = tokenProvider.getRefreshToken()
        return accessToken != null && refreshToken != null
    }

    fun logout() {
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