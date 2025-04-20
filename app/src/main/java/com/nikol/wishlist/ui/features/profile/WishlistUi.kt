package com.nikol.wishlist.ui.features.profile

import com.nikol.wishlist.domain.wishlist.WishlistDomain
import com.nikol.wishlist.domain.wishlist.WishlistItemDomain

class WishlistUi(
    val id: Int,
    val name: String,
    val items: List<WishlistItemUi>
)

class WishlistItemUi(
    val id: Int,
    val name: String,
    val description: String?,
    val imageUrl: String?,
    val price: Int?,
    val url: String?
)

fun List<WishlistDomain>.toUi(): List<WishlistUi> = map { it.toUi() }

fun WishlistDomain.toUi(): WishlistUi {
    return WishlistUi(
        id = id,
        name = name,
        items = items.map { it.toUi() }
    )
}

fun WishlistItemDomain.toUi(): WishlistItemUi {
    return WishlistItemUi(
        id = id,
        name = name,
        description = description,
        imageUrl = imageUrl,
        price = price,
        url = url
    )
}

class WishlistItemInput(
    val name: String,
    val description: String?,
    val imageUrl: String?,
    val price: Int?,
    val url: String?
)

fun WishlistItemInput.toDomain(): WishlistItemDomain {
    return WishlistItemDomain(
        id = 0,
        name = name,
        description = description,
        imageUrl = imageUrl,
        price = price,
        url = url,
    )
}