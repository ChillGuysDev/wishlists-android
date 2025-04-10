package com.nikol.wishlist.ui.features.profile

import android.net.Uri
import androidx.compose.animation.AnimatedContent
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.nikol.wishlist.ui.features.home.Loading
import com.nikol.wishlist.ui.uikit.auth.LoginForm
import com.nikol.wishlist.ui.uikit.auth.RegisterForm

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileScreen(viewModel: ProfileViewModel = hiltViewModel()) {
    val state by viewModel.state.collectAsState()

    Scaffold(
        Modifier.fillMaxSize(),
        topBar = {
            TopAppBar(
                title = { Text("Profile") },
                actions = {
                    if (state is ProfileScreenState.Content) {
                        Button(onClick = { viewModel.onLogoutClick() }) {
                            Text("Logout")
                        }
                    }
                },
                modifier = Modifier.shadow(elevation = 4.dp),
            )
        }
    ) { paddingValues ->
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
    state: ProfileScreenState,
    listener: ProfileListener,
    modifier: Modifier = Modifier
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
                    onRegisterClick = { showRegister = true }
                )
            }
        }
    }
}

@Composable
private fun Content(
    state: ProfileScreenState.Content,
    listener: ProfileListener,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        HeaderContent(
            modifier = Modifier.fillMaxWidth(),
            avatarUrl = state.avatarUrl,
            bio = state.bio,
            onAvatarPickClick = listener::onAvatarPickClick,
            onOpenPreviewClick = { /* TODO: Open preview */ },
        )

    }
}

@Composable
private fun HeaderContent(
    modifier: Modifier = Modifier,
    avatarUrl: String?,
    bio: String,
    onAvatarPickClick: (Uri) -> Unit,
    onOpenPreviewClick: () -> Unit = {},
) {
    Column(modifier) {
        Row(
            horizontalArrangement = Arrangement.Center,
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 16.dp)
        ) {
            UserAvatar(
                imageUrl = avatarUrl,
                onImagePicked = onAvatarPickClick,
                onImageClick = onOpenPreviewClick,
            )
        }
        if (bio.isNotEmpty()) {
            Bio(
                text = bio,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp)
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

// write preview for content
@Preview
@Composable
fun ProfileScreenPreview() {
    ProfileScreenContent(
        state = ProfileScreenState.Content(
            avatarUrl = null,
            name = "John Doe",
            email = "nikita.nikita@gmail.com",
            bio = "This is a bio",
            birthDate = "10.06.1999"
        ),
        listener = getProfileListenerStub(),
        modifier = Modifier.fillMaxSize()
    )
}

private fun getProfileListenerStub() = object : ProfileListener {
    override fun onAvatarPickClick(uri: Uri) {}
    override fun onAvatarDeleteClick() {}
    override fun onLoginClick(username: String, password: String) {}
    override fun onRegisterClick(username: String, password: String, email: String) {}
    override fun onLogoutClick() {}
}

