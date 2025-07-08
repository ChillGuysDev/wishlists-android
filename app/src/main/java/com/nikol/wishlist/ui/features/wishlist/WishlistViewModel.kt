package com.nikol.wishlist.ui.features.wishlist

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.toRoute
import com.nikol.wishlist.core.domain.wishlist.WishlistItemDomain
import com.nikol.wishlist.core.domain.wishlist.WishlistsInteractor
import com.nikol.wishlist.core.ui.models.toUi
import com.nikol.wishlist.navigation.ScreenRoute
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
internal class WishlistViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val wishlistInteractor: WishlistsInteractor,
) : ViewModel(), WishlistListListener {

    private val wishlistId: Long = savedStateHandle.toRoute<ScreenRoute.Wishlist>().id

    val state = MutableStateFlow<WishlistState>(WishlistState.Loading)

    init {
        viewModelScope.launch {
            loadWishlist()
        }
    }

    override fun onRemoveClick(id: Long) {
        viewModelScope.launch {
            wishlistInteractor.deleteWishlistItem(wishlistId, id)
            loadWishlist()
        }
    }

    override fun onAddItemToWishlist(title: String, description: String) {
        viewModelScope.launch {
            wishlistInteractor.addItemToWishlist(wishlistId, WishlistItemDomain(0, title, description, null, null, null))
            loadWishlist()
        }
    }

    private suspend fun loadWishlist() {
        state.value = WishlistState.Content(wishlistInteractor.getWishlistById(wishlistId).toUi())
    }
}

