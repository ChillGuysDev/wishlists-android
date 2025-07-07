package com.nikol.wishlist.core.domain.wishlist

data class WishlistDomain(
    val id: Long,
    val name: String,
    val description: String?,
    val createdAt: String,
    val items: List<WishlistItemDomain>
)

data class WishlistItemDomain(
    val id: Long,
    val name: String,
    val description: String?,
    val imageUrl: String?,
    val price: Int?,
    val url: String?,
)

data class CreateWishlistInput(
    val name: String,
    val description: String
)

data class UpdateWishlistInput(
    val name: String,
    val description: String
)

data class CreateWishlistItemInput(
    val name: String,
    val description: String
)

data class UpdateWishlistItemInput(
    val name: String,
    val description: String
)