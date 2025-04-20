package com.nikol.wishlist.ui.features.profile

import android.net.Uri
import androidx.compose.animation.AnimatedContent
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.LargeTopAppBar
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.layout.ContentScale
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
import com.nikol.wishlist.R
import com.nikol.wishlist.ui.features.createWishlist.CreateWishlistBottomSheet
import com.nikol.wishlist.ui.features.home.Loading
import com.nikol.wishlist.ui.theme.WishlistsTheme
import com.nikol.wishlist.ui.uikit.auth.LoginForm
import com.nikol.wishlist.ui.uikit.auth.RegisterForm
import okhttp3.internal.toImmutableList

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileScreen(viewModel: ProfileViewModel = hiltViewModel()) {
    val state by viewModel.state.collectAsState()
    val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior()

    Scaffold(
        Modifier
            .fillMaxSize()
            .nestedScroll(scrollBehavior.nestedScrollConnection), topBar = {
            if (state is ProfileScreenState.Content) {
                LargeTopAppBar(
                    title = {
                        Text("Profile")
                    },
                    actions = {
                        HeaderContent(
                            avatarUrl = (state as ProfileScreenState.Content).avatarUrl,
                            bio = (state as ProfileScreenState.Content).bio,
                            onAvatarPickClick = viewModel::onAvatarPickClick,
                            onOpenPreviewClick = { /* TODO: Open preview */ },
                            onLogoutClick = viewModel::onLogoutClick,
                            modifier = Modifier.padding(16.dp)
                        )
                    },
                    modifier = Modifier.shadow(elevation = 4.dp),
                    scrollBehavior = scrollBehavior,
                )
            }

        }) { paddingValues ->
        ProfileScreenContent(
            state = state,
            listener = viewModel,
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        )
    }
}

@Composable
private fun ProfileScreenContent(
    state: ProfileScreenState, listener: ProfileListener, modifier: Modifier = Modifier
) {
    AnimatedContent(state, modifier) { targetState ->
        when (targetState) {
            is ProfileScreenState.Loading -> Loading()
            is ProfileScreenState.Empty -> Empty()
            is ProfileScreenState.AuthenticateContent -> AuthContent(listener)
            is ProfileScreenState.Content -> Content(targetState, listener)
        }
    }

}

@Composable
private fun Empty(modifier: Modifier = Modifier) {
    Text("Empty", modifier = modifier)
}

@Composable
private fun AuthContent(listener: ProfileAuthListener, modifier: Modifier = Modifier) {
    var showRegister by rememberSaveable { mutableStateOf(false) }

    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        AnimatedContent(showRegister) { tagetShowRegister ->
            if (tagetShowRegister) {
                RegisterForm(onRegisterClick = listener::onRegisterClick)
            } else {
                LoginForm(
                    onLoginClick = listener::onLoginClick,
                    onRegisterClick = { showRegister = true })
            }
        }
    }
}

@Composable
private fun Content(
    state: ProfileScreenState.Content, listener: ProfileListener, modifier: Modifier = Modifier
) {
    var isShowBottomSheet by rememberSaveable { mutableStateOf(false) }

    if (isShowBottomSheet) {
        CreateWishlistBottomSheet(
            showBottomSheet = isShowBottomSheet,
            onDismiss = {
                isShowBottomSheet = false
            },
            listener = { name, description, items ->
                listener.onWishlistCreateClick(name, description, items)
                isShowBottomSheet = false
            }
        )
    }
    Column(
        modifier = modifier, horizontalAlignment = Alignment.CenterHorizontally
    ) {
        LazyVerticalGrid(
            columns = GridCells.Adaptive(minSize = 160.dp),
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 8.dp),
            contentPadding = PaddingValues(bottom = 80.dp)
        ) {
            item {
                WishlistCreateCard(
                    onClick = { isShowBottomSheet = true },
                    modifier = Modifier.padding(8.dp)
                )
            }
            items(state.wishlists) { wishlist ->
                WishlistCardItem(
                    onClick = { /* onWishlistClick(wishlist.id) */ },
                    modifier = Modifier.padding(8.dp),
                    item = wishlist
                )
            }
        }
    }
}

@Composable
private fun HeaderContent(
    modifier: Modifier = Modifier,
    avatarUrl: String?,
    bio: String,
    onAvatarPickClick: (Uri) -> Unit,
    onOpenPreviewClick: () -> Unit = {},
    onLogoutClick: () -> Unit = {},
) {
    Column(modifier) {
        Row(
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 16.dp)
        ) {
            UserAvatar(
                imageUrl = avatarUrl,
                onImagePicked = onAvatarPickClick,
                onImageClick = onOpenPreviewClick,
            )

            Button(
                onClick = onLogoutClick,
                shape = RoundedCornerShape(8.dp),
            ) {
                Text("Logout")
            }
        }
        if (bio.isNotEmpty()) {
            Bio(
                text = bio,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 24.dp)
            )
        }
    }
}

@Composable
private fun Bio(text: String, modifier: Modifier = Modifier) {
    Column(modifier) {
        Text(
            text = "Bio",
            modifier = Modifier,
        )
        Text(
            text = text,
            modifier = Modifier,
        )
    }
}

@Composable
fun WishlistCreateCard(
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    OutlinedCard(onClick = onClick, modifier = modifier) {
        Box(Modifier.aspectRatio(1f), contentAlignment = Alignment.Center) {
            Text(
                text = "Create Wishlist",
                modifier = Modifier
                    .padding(16.dp)
                    .align(Alignment.Center),
                style = MaterialTheme.typography.labelLarge
            )
        }
    }
}

@Composable
fun WishlistCardItem(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    item: WishlistUi,
) {
    OutlinedCard(
        onClick, modifier,
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.background)
    ) {
        Box(modifier = Modifier.aspectRatio(1f), contentAlignment = Alignment.Center) {
            Column(Modifier.padding(16.dp)) {
                Text(
                    item.name,
                    style = MaterialTheme.typography.labelMedium,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )

                val previewItemUrls by rememberSaveable(item.items.take(3).map { it.id }) {
                    val resultList = mutableListOf<String>()
                    resultList.addAll(item.items.take(3).mapNotNull { it.imageUrl })
                    while (resultList.size < 3) {
                        resultList.add("")
                    }
                    mutableStateOf(resultList.toImmutableList())
                }

                Row(
                    horizontalArrangement = Arrangement.spacedBy((-20).dp),
                    modifier = Modifier.padding(top = 16.dp)
                ) {
                    previewItemUrls.forEach { previewUrl ->
                        OutlinedCard(
                            elevation = CardDefaults.cardElevation(4.dp),
                            border = BorderStroke(1.dp, MaterialTheme.colorScheme.secondary)
                        ) {
                            AsyncImage(
                                model = previewUrl,
                                contentDescription = "Wishlist Item",
                                modifier = Modifier
                                    .size(50.dp)
                                    .clip(RoundedCornerShape(8.dp)),
                                contentScale = ContentScale.Crop,
                                placeholder = painterResource(R.drawable.ic_image_placeholder)
                            )
                        }
                    }
                }
            }
        }

    }
}


@Preview
@Composable
private fun ProfileScreenPreview() {
    ProfileScreenContent(
        state = ProfileScreenState.Content(
            avatarUrl = null,
            name = "John Doe",
            email = "nikita.nikita@gmail.com",
            bio = "This is a bio",
            birthDate = "10.06.1999",
            wishlists = dummyWishlistList,
        ), listener = getProfileListenerStub(), modifier = Modifier.fillMaxSize()
    )
}


@OptIn(ExperimentalCoilApi::class)
@Preview
@Composable
private fun WishlistPreview() {
    CompositionLocalProvider(LocalAsyncImagePreviewHandler provides previewHandler) {
        WishlistCardItem(
            onClick = {},
            item = dummyWishlistUiItem,
            modifier = Modifier.height(200.dp)
        )
    }
}

@Preview
@Composable
private fun WishlistCreateCardPreview() {
    WishlistsTheme {
        WishlistCreateCard(onClick = {}, modifier = Modifier.height(200.dp))
    }
}

private fun getProfileListenerStub() = object : ProfileListener {
    override fun onAddWishlistItemClick(wishlistId: Int, item: WishlistItemInput) = Unit
    override fun onWishlistCreateClick(
        name: String, description: String, items: List<WishlistItemInput>
    ) = Unit

    override fun onAvatarPickClick(uri: Uri) {}
    override fun onAvatarDeleteClick() {}
    override fun onLoginClick(username: String, password: String) {}
    override fun onRegisterClick(username: String, password: String, email: String) {}
    override fun onLogoutClick() {}
}

@OptIn(ExperimentalCoilApi::class)
private val previewHandler = AsyncImagePreviewHandler {
    ColorImage(Color.Red.toArgb())
}

private val dummyWishlistUiItem = WishlistUi(
    id = 1,
    name = "My Wishlist",
    items = listOf(
        WishlistItemUi(
            id = 1,
            name = "Item 1",
            description = "Description 1",
            imageUrl = null,
            price = null,
            url = null
        ),
        WishlistItemUi(
            id = 2,
            name = "Item 2",
            description = "Description 2",
            imageUrl = null,
            price = null,
            url = null
        ),
        WishlistItemUi(
            id = 3,
            name = "Item 3",
            description = "Description 3",
            imageUrl = null,
            price = null,
            url = null
        ),
        WishlistItemUi(
            id = 4,
            name = "Item 4",
            description = "Description 4",
            imageUrl = null,
            price = null,
            url = null
        ),
    )
)

private val dummyWishlistList = listOf(
    WishlistUi(
        id = 1,
        name = "My Wishlist",
        items = listOf(
            WishlistItemUi(
                id = 1,
                name = "Item 1",
                description = "Description 1",
                imageUrl = null,
                price = null,
                url = null
            ),
            WishlistItemUi(
                id = 2,
                name = "Item 2",
                description = "Description 2",
                imageUrl = null,
                price = null,
                url = null
            ),
        )
    ),
    WishlistUi(
        id = 2,
        name = "My Wishlist 2",
        items = emptyList()
    ),
    WishlistUi(
        id = 3,
        name = "My Wishlist 3",
        items = listOf(
            WishlistItemUi(
                id = 3,
                name = "Item 3",
                description = "Description 3",
                imageUrl = null,
                price = null,
                url = null
            ),
            WishlistItemUi(
                id = 4,
                name = "Item 4",
                description = "Description 4",
                imageUrl = null,
                price = null,
                url = null
            ),
        )
    ),

    WishlistUi(
        id = 4,
        name = "My Wishlist 4",
        items = emptyList()
    ),
)