package com.nikol.wishlist.core.ui.models

import com.nikol.wishlist.core.domain.wishlist.WishlistDomain
import com.nikol.wishlist.core.domain.wishlist.WishlistItemDomain
import com.nikol.wishlist.core.ui.createWishlist.WishlistItemInput

class WishlistUi(
    val id: Long,
    val name: String,
    val items: List<WishlistItemUi>
)

class WishlistItemUi(
    val id: Long,
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