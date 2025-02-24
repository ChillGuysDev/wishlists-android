package com.nikol.wishlist.ui.features.home.data

import com.nikol.wishlist.ui.features.home.domain.WishlistDomain
import com.nikol.wishlist.ui.features.home.domain.WishlistElementDomain
import kotlinx.coroutines.delay
import java.util.UUID
import javax.inject.Inject

interface WishlistsDataSource {
    suspend fun getStarredWishlists(): List<WishlistDomain>?
    suspend fun getUserWishlist(): WishlistDomain?
    suspend fun createWishlist(wishlist: WishlistDomain): Boolean
}

class WishlistsMockDataSource @Inject constructor() : WishlistsDataSource {
    var starred: List<WishlistDomain>? = null
    var userWishlist: WishlistDomain? = null

    override suspend fun getStarredWishlists(): List<WishlistDomain>? {
        return starred
    }

    override suspend fun getUserWishlist(): WishlistDomain? {
        return userWishlist
    }

    override suspend fun createWishlist(wishlist: WishlistDomain): Boolean {
        return true
    }

    fun saveStarredWishlists(list: List<WishlistDomain>) {
        starred = list
    }

    fun saveUserWishlist(wishlist: WishlistDomain) {
        userWishlist = wishlist
    }

}

class WishlistsRemoteSource @Inject constructor() : WishlistsDataSource {
    override suspend fun getStarredWishlists(): List<WishlistDomain>? {
        delay(1000)
        return generateMockWishlists(10)
    }

    override suspend fun getUserWishlist(): WishlistDomain? {
        delay(1000)
        return generateMockWishlist("Nikita's wishlist")
    }

    override suspend fun createWishlist(wishlist: WishlistDomain): Boolean {
        return true
    }
}



fun generateMockWishlists(size: Int = 10) = List(size) { generateMockWishlist(name = "wishlist no $it") }

private fun generateMockWishlist(name: String = "author's whishlist 1") = WishlistDomain(
    id = UUID.randomUUID().toString(),
    authorId = UUID.randomUUID().toString(),
    author = "Artur",
    elements = List(10) { generateMockWishlistElement() },
    name = name,
    iconUrl = ""
)

private fun generateMockWishlistElement() = WishlistElementDomain(
    id = UUID.randomUUID().toString(),
    name = "рубашка в супер магазине",
    author = "Artur",
    imageUrl = "https://baconmockup.com/300/200/"
)
