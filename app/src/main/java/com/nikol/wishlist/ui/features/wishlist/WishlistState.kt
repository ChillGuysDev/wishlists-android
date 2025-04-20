package com.nikol.wishlist.ui.features.wishlist

import com.nikol.wishlist.ui.features.profile.WishlistUi

internal sealed interface WishlistState {
    object Loading : WishlistState
    class Content(val wishlist: WishlistUi) : WishlistState
}
