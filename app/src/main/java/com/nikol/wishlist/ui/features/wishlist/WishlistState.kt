package com.nikol.wishlist.ui.features.wishlist

import com.nikol.wishlist.core.ui.models.WishlistUi

internal sealed interface WishlistState {
    object Loading : WishlistState
    class Content(val wishlist: WishlistUi) : WishlistState
}
