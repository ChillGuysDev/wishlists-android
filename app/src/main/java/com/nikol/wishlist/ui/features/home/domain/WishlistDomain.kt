package com.nikol.wishlist.ui.features.home.domain

import androidx.compose.runtime.Immutable

@Immutable
data class WishlistDomain(
    val id: String,
    val authorId: String,
    val author: String,
    val elements: List<WishlistElementDomain>,
    val name: String,
    val iconUrl: String?,
)

data class WishlistElementDomain(
    val id: String,
    val name: String,
    val author: String,
    val imageUrl: String?,
)