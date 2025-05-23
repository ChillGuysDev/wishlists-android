package com.nikol.wishlist.ui.features.wishlist

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.toRoute
import com.nikol.wishlist.domain.wishlist.WishlistsInteractor
import com.nikol.wishlist.navigation.ScreenRoute
import com.nikol.wishlist.ui.features.profile.toUi
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
internal class WishlistViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val wishlistInteractor: WishlistsInteractor,
): ViewModel() {

    private val wishlistId: Int = savedStateHandle.toRoute<ScreenRoute.Wishlist>().id

    val state = MutableStateFlow<WishlistState>(WishlistState.Loading)

    init {
        viewModelScope.launch {
            loadWishlist()
        }
    }

    private suspend fun loadWishlist() {
        state.value = WishlistState.Content(wishlistInteractor.getWishlistById(wishlistId).toUi())
    }
}

