package com.nikol.wishlist.ui.features.home

import com.nikol.wishlist.ui.features.home.domain.WishlistDomain

sealed class HomeState {
    data class Error(val message: String) : HomeState()
    data object Loading : HomeState()
    data class Content(val wishlists: List<WishlistDomain>) : HomeState()
    data object Empty : HomeState()
}