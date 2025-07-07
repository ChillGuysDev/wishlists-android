package com.nikol.wishlist.ui.features.wishlist

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import com.nikol.wishlist.core.ui.Loading

internal fun wishlistNavigationListener(
    onBackClick: () -> Unit
): WishlistNavigationListener {
    return object : WishlistNavigationListener {
        override fun onBackClick() = onBackClick()
    }
}

internal fun interface WishlistNavigationListener {
    fun onBackClick()
}

@Composable
internal fun WishlistScreen(
    navigationListener: WishlistNavigationListener,
    viewModel: WishlistViewModel = hiltViewModel()
) {
    val state by viewModel.state.collectAsState()

    Scaffold { innerPadding ->
        WishlistPane(state, Modifier.padding(innerPadding))
    }
}

@Composable
private fun WishlistPane(
    state: WishlistState,
    modifier: Modifier = Modifier
) {
    Box(modifier) {
        when (state) {
            is WishlistState.Loading -> Loading(Modifier.fillMaxSize())

            is WishlistState.Content -> WishlistContent(state)
        }
    }
}


@Composable
private fun WishlistContent(state: WishlistState.Content, modifier: Modifier = Modifier) {
    Column(horizontalAlignment = Alignment.CenterHorizontally, modifier = modifier) {
        Text(state.wishlist.name, style = MaterialTheme.typography.headlineLarge)
    }
}
