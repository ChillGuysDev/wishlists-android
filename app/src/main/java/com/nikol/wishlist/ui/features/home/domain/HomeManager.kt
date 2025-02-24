package com.nikol.wishlist.ui.features.home.domain

import com.nikol.wishlist.ui.features.home.data.WishlistsRepository
import kotlinx.coroutines.delay
import javax.inject.Inject
import kotlin.collections.orEmpty
import kotlin.collections.plus

class HomeManager @Inject constructor(private val repository: WishlistsRepository) {
    private val savedWishlists = mutableListOf<WishlistDomain>()
    suspend fun getStarredWishlists() = repository.getStarredWishlists().orEmpty() + savedWishlists

    suspend fun saveWishlist(wishlist: WishlistDomain): Boolean {
        delay(300)
        return savedWishlists.add(wishlist)
    }
}