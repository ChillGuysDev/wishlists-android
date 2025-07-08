package com.nikol.wishlist.ui.features.wishlist

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SwipeToDismissBox
import androidx.compose.material3.SwipeToDismissBoxValue
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.material3.rememberSwipeToDismissBoxState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import coil3.ColorImage
import coil3.annotation.ExperimentalCoilApi
import coil3.compose.AsyncImage
import coil3.compose.AsyncImagePreviewHandler
import coil3.compose.LocalAsyncImagePreviewHandler
import com.nikol.wishlist.core.ui.Loading
import com.nikol.wishlist.core.ui.models.WishlistItemUi
import com.nikol.wishlist.core.ui.models.WishlistUi
import kotlinx.coroutines.launch

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


internal interface WishlistListListener {
    fun onRemoveClick(id: Long)
    fun onAddItemToWishlist(title: String, description: String)
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun WishlistScreen(
    navigationListener: WishlistNavigationListener,
    viewModel: WishlistViewModel = hiltViewModel()
) {
    val state by viewModel.state.collectAsState()

    var isAddItemBsVisible by remember { mutableStateOf(false) }

    val addItemSheetState = rememberModalBottomSheetState()
    val scope = rememberCoroutineScope()

    if (isAddItemBsVisible) {
        ModalBottomSheet(
            onDismissRequest = {
                scope.launch {
                    addItemSheetState.hide()
                }.invokeOnCompletion {
                    isAddItemBsVisible = false
                }
            },
            sheetState = addItemSheetState
        ) {
            var title by rememberSaveable { mutableStateOf("") }
            TextField(
                title,
                { title = it },
                Modifier
                    .fillMaxWidth()
                    .padding(8.dp),
                label = { Text("Название") })

            var description by rememberSaveable { mutableStateOf("") }
            TextField(
                description,
                { description = it },
                Modifier
                    .fillMaxWidth()
                    .padding(8.dp),
                label = { Text("Описание") })

            Button({
                viewModel.onAddItemToWishlist(title, description)
                scope.launch { addItemSheetState.hide() }
                    .invokeOnCompletion { isAddItemBsVisible = false }
            }) {
                Text("Добавить в список")
            }
        }
    }
    Scaffold { innerPadding ->
        WishlistPane(
            state,
            viewModel,
            { isAddItemBsVisible = true },
            Modifier.padding(innerPadding)
        )
    }
}

@Composable
private fun WishlistPane(
    state: WishlistState,
    listener: WishlistListListener,
    onAddItemClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Box(modifier) {
        when (state) {
            is WishlistState.Loading -> Loading(Modifier.fillMaxSize())

            is WishlistState.Content -> WishlistContent(state.wishlist, listener, onAddItemClick)
        }
    }
}

@Composable
private fun WishlistContent(
    wishlistUi: WishlistUi,
    listener: WishlistListListener,
    onAddItemClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(modifier = modifier.padding(16.dp)) {
        Text(
            text = wishlistUi.name,
            style = MaterialTheme.typography.headlineLarge
        )
        Spacer(modifier = Modifier.height(8.dp))
        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(
                items = wishlistUi.items,
                key = { it.id }
            ) { item ->
                WishlistItemRow(item, onRemove = listener::onRemoveClick)
            }

            item {
                Button(onClick = onAddItemClick) {
                    Text("Добавить элемент")
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun WishlistItemRow(
    item: WishlistItemUi,
    modifier: Modifier = Modifier,
    onRemove: (id: Long) -> Unit = {},
) {
    val swipeToDismissBoxState = rememberSwipeToDismissBoxState(
        confirmValueChange = {
            if (it == SwipeToDismissBoxValue.EndToStart) onRemove(item.id)
            false
        }
    )

    Box(
        modifier
            .fillMaxSize()
            .padding(8.dp)
    ) {
        SwipeToDismissBox(
            state = swipeToDismissBoxState,
            modifier = Modifier,
            backgroundContent = {
                when (swipeToDismissBoxState.dismissDirection) {
                    SwipeToDismissBoxValue.EndToStart -> Box(
                        Modifier
                            .fillMaxSize()
                            .padding(1.dp)
                            .clip(RoundedCornerShape(8.dp))
                            .background(Color.Red)
                    )

                    else -> Unit
                }
            }
        ) {
            Row(
                modifier = modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(8.dp))
                    .background(MaterialTheme.colorScheme.background),
                verticalAlignment = Alignment.CenterVertically
            ) {
                AsyncImage(
                    model = item.imageUrl,
                    contentDescription = item.name,
                    modifier = Modifier
                        .size(64.dp)
                        .clip(RoundedCornerShape(8.dp)),
                    placeholder = painterResource(com.nikol.wishlist.core.ui.R.drawable.ic_image_placeholder)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Column(modifier = Modifier.weight(1f)) {
                    Text(text = item.name, style = MaterialTheme.typography.titleMedium)
                    item.description?.takeIf { it.isNotBlank() }?.let { desc ->
                        Text(
                            text = desc,
                            style = MaterialTheme.typography.bodyMedium,
                            maxLines = 2,
                            overflow = TextOverflow.Ellipsis
                        )
                    }
                    item.price?.let { price ->
                        Text(
                            text = "\$${price}",
                            style = MaterialTheme.typography.bodySmall
                        )
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalCoilApi::class)
private val previewHandler = AsyncImagePreviewHandler {
    ColorImage(Color.Red.toArgb())
}

@OptIn(ExperimentalCoilApi::class)
@Preview(showBackground = true)
@Composable
private fun WishlistContentPreview() {
    CompositionLocalProvider(LocalAsyncImagePreviewHandler provides previewHandler) {
        WishlistContent(
            wishlistUi = WishlistUi(
                id = 1L,
                name = "My Wishlist",
                items = listOf(
                    WishlistItemUi(
                        1L,
                        "Camera",
                        "A stylish camera",
                        "https://via.placeholder.com/64",
                        299,
                        "https://"
                    ),
                    WishlistItemUi(2L, "Headphones", null, null, null, null)
                )
            ),
            listener = object : WishlistListListener {
                override fun onRemoveClick(id: Long) = Unit
                override fun onAddItemToWishlist(title: String, description: String) = Unit
            },
            onAddItemClick = {},
        )
    }
}