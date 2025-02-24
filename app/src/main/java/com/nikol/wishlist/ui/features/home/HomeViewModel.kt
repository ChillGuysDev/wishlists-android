package com.nikol.wishlist.ui.features.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.nikol.wishlist.ui.features.home.domain.HomeManager
import com.nikol.wishlist.ui.features.home.domain.WishlistDomain
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val manager: HomeManager
) : ViewModel(), HomeScreenListener {
    private val _state = MutableStateFlow<HomeState>(HomeState.Empty)
    val state: StateFlow<HomeState> = _state

    private var cachedWishlists = emptyList<WishlistDomain>()
    init {
        viewModelScope.launch {
            _state.value = HomeState.Loading
            cachedWishlists = manager.getStarredWishlists()
            _state.value = HomeState.Content(cachedWishlists)
        }
    }

    override fun onSearchClick(query: String) {
        viewModelScope.launch {
            _state.value =
                HomeState.Content(cachedWishlists.filter { it.name.contains(query) })
        }
    }
}