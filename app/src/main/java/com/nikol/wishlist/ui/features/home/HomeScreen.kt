package com.nikol.wishlist.ui.features.home

import android.R
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.TextField
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.nikol.wishlist.ui.features.home.data.generateMockWishlists
import com.nikol.wishlist.ui.uikit.WishlistCard


interface HomeScreenListener {
    fun onSearchClick(query: String)
}

interface HomeNavigationListener {
    fun navigateToCreateList()
}

@Composable
fun HomeScreen(
    navigationListener: HomeNavigationListener,
    viewModel: HomeViewModel = hiltViewModel()
) {
    val state = viewModel.state.collectAsState()
    val stateValue = state.value

    when (stateValue) {
        is HomeState.Content -> Content(
            stateValue,
            viewModel::onSearchClick,
            navigationListener::navigateToCreateList
        )

        is HomeState.Loading -> Loading(Modifier.fillMaxSize())
        is HomeState.Error -> Error(stateValue.message, Modifier.fillMaxSize())
        is HomeState.Empty -> Empty(Modifier.fillMaxSize())
    }
}

@Composable
private fun Content(
    state: HomeState.Content,
    onSearch: (String) -> Unit,
    onCreateWishlistClick: () -> Unit
) {
    val listState = rememberLazyListState()

    Box(Modifier.fillMaxSize()) {
        Column {
            SearchField(
                onSearch, Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp).padding(top = 16.dp)
            )
            LazyColumn(
                state = listState,
                verticalArrangement = Arrangement.spacedBy(16.dp),
                contentPadding = PaddingValues(top = 16.dp, bottom = 40.dp, start = 16.dp, end = 16.dp)
            ) {
                items(items = state.wishlists, key = { it.id }) {
                    WishlistCard(
                        it.name,
                        it.iconUrl,
                        modifier = Modifier
                            .fillMaxWidth()
                            .animateItem()
                    )
                }
            }
        }

        FloatingActionButton(
            onClick = onCreateWishlistClick,
            modifier = Modifier
                .padding(16.dp)
                .align(Alignment.BottomEnd)
        ) {
            Icon(painterResource(R.drawable.ic_input_add), contentDescription = null)
        }
    }


}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun SearchField(onSearch: (query: String) -> Unit, modifier: Modifier = Modifier) {
    var query by rememberSaveable { mutableStateOf("") }

    TextField(
        value = query,
        onValueChange = { newQuery ->
            query = newQuery
            onSearch(newQuery)
        },
        modifier = modifier.clip(RoundedCornerShape(16.dp)),
        maxLines = 1,
    )
}

@Composable
private fun Error(msg: String, modifier: Modifier = Modifier) {
    Box(modifier, contentAlignment = Alignment.Center) {
        Text(msg, style = MaterialTheme.typography.displayLarge)
    }
}

@Composable
private fun Loading(modifier: Modifier = Modifier) {
    Box(modifier, contentAlignment = Alignment.Center) {
        CircularProgressIndicator()
    }
}

@Composable
private fun Empty(modifier: Modifier = Modifier) {
    Box(modifier, contentAlignment = Alignment.Center) { }
}

@Composable
@Preview
private fun ContentPreview() {
    Content(
        state = HomeState.Content(
            generateMockWishlists()
        ),
        onSearch = {},
        onCreateWishlistClick = {}
    )
}