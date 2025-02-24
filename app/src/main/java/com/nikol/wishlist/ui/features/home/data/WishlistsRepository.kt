package com.nikol.wishlist.ui.features.home.data

import com.nikol.wishlist.ui.features.home.domain.WishlistDomain
import javax.inject.Inject

class WishlistsRepository @Inject constructor(
    private val wishlistsDataSource: WishlistsMockDataSource,
    private val wishlistRemoteDataSource: WishlistsRemoteSource,
) {

    suspend fun getStarredWishlists(): List<WishlistDomain>? {
        return wishlistRemoteDataSource.getStarredWishlists()
    }

}