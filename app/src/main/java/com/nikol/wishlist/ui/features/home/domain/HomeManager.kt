package com.nikol.wishlist.ui.features.home.domain

import com.nikol.wishlist.core.domain.tokenprovider.AuthTokenProvider
import com.nikol.wishlist.ui.features.home.data.WishlistsRepository
import kotlinx.coroutines.delay
import javax.inject.Inject

class HomeManager @Inject constructor(
    private val repository: WishlistsRepository,
    private val tokenProvider: AuthTokenProvider,
) {
    private val savedWishlists = mutableListOf<WishlistDomain>()
    suspend fun getStarredWishlists() = repository.getStarredWishlists().orEmpty() + savedWishlists

    suspend fun saveWishlist(wishlist: WishlistDomain): Boolean {
        delay(300)
        return savedWishlists.add(wishlist)
    }

    suspend fun isUserLogin(): Boolean {
        val accessToken = tokenProvider.getAccessToken()
        val refreshToken = tokenProvider.getRefreshToken()
        return accessToken != null && refreshToken != null
    }
}
