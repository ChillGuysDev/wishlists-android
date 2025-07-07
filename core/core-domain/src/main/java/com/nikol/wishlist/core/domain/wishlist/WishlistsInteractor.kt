package com.nikol.wishlist.core.domain.wishlist

interface WishlistsInteractor {
    suspend fun getUserWishlists(): List<WishlistDomain>
    suspend fun createWishlist(
        input: CreateWishlistInput,
        items: List<WishlistItemDomain>
    ): WishlistDomain

    suspend fun updateWishlist(input: UpdateWishlistInput): WishlistDomain
    suspend fun deleteWishlist(wishlistId: Long)
    suspend fun getWishlistById(wishlistId: Long): WishlistDomain
    suspend fun addItemToWishlist(wishlistId: Long, input: WishlistItemDomain): WishlistItemDomain
    suspend fun deleteWishlistItem(wishlistId: Long, itemId: Long)
    suspend fun updateWishlistItem(
        wishlistId: Long,
        itemId: Long,
        input: UpdateWishlistItemInput
    ): WishlistItemDomain
}
