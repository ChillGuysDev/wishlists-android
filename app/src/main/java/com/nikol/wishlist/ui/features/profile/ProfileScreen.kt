package com.nikol.wishlist.ui.features.profile

import androidx.compose.animation.AnimatedContent
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.nikol.wishlist.ui.features.home.Loading
import com.nikol.wishlist.ui.features.home.domain.AuthManager
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@Composable
fun ProfileScreen(viewModel: ProfileViewModel = hiltViewModel()) {
    val state = viewModel.state.collectAsState()
    val stateValue = state.value

    when (stateValue) {
        is ProfileScreenState.Loading -> Loading()
        is ProfileScreenState.Empty -> Empty()
        is ProfileScreenState.AuthenticateContent -> AuthContent(viewModel)
        is ProfileScreenState.Content -> Content(viewModel)
    }
    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text("Profile screen")
    }
}

@Composable
private fun Empty() {
    Text("Empty")
}

@Composable
private fun AuthContent(viewModel: ProfileViewModel) {
    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        AuthScreen(
            onLoginClick = viewModel::onLoginClick,
            onRegisterClick = viewModel::onRegisterClick,
        )
    }
}

@Composable
fun AuthScreen(
    onLoginClick: (String, String) -> Unit,
    onRegisterClick: (String, String, String) -> Unit
) {
    var showRegister by rememberSaveable { mutableStateOf(false) }

    AnimatedContent(showRegister) { tagetShowRegister ->
        if (tagetShowRegister) {
            RegisterForm(onRegisterClick = onRegisterClick)
        } else {
            LoginForm(
                onLoginClick = onLoginClick,
                onRegisterClick = { showRegister = true }
            )
        }
    }

}

@Composable
fun LoginForm(
    onLoginClick: (String, String) -> Unit,
    onRegisterClick: () -> Unit
) {
    var email by rememberSaveable { mutableStateOf("") }
    var password by rememberSaveable { mutableStateOf("") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        TextField(
            value = email,
            onValueChange = { email = it },
            label = { Text("Email") },
            modifier = Modifier.fillMaxWidth()
        )
        TextField(
            value = password,
            onValueChange = { password = it },
            label = { Text("Password") },
            modifier = Modifier.fillMaxWidth(),
            visualTransformation = PasswordVisualTransformation()
        )
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Button(onClick = { onLoginClick(email, password) }) {
                Text("Login")
            }
            OutlinedButton(onClick = onRegisterClick) {
                Text("Register")
            }
        }
    }
}

@Composable
fun RegisterForm(
    onRegisterClick: (String, String, String) -> Unit
) {
    var email by rememberSaveable { mutableStateOf("") }
    var username by rememberSaveable { mutableStateOf("") }
    var password by rememberSaveable { mutableStateOf("") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        TextField(
            value = email,
            onValueChange = { email = it },
            label = { Text("Email") },
            modifier = Modifier.fillMaxWidth()
        )
        TextField(
            value = username,
            onValueChange = { username = it },
            label = { Text("Username") },
            modifier = Modifier.fillMaxWidth()
        )
        TextField(
            value = password,
            onValueChange = { password = it },
            label = { Text("Password") },
            modifier = Modifier.fillMaxWidth(),
            visualTransformation = PasswordVisualTransformation()
        )
        Button(onClick = { onRegisterClick(email, username, password) }) {
            Text("Register")
        }
    }
}


@Composable
private fun Content(viewModel: ProfileViewModel) {
    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text("Content")
    }
}

sealed interface ProfileScreenState {
    object Loading : ProfileScreenState
    object Empty : ProfileScreenState
    class AuthenticateContent : ProfileScreenState
    class Content : ProfileScreenState
}

@HiltViewModel
class ProfileViewModel @Inject constructor(
    private val authManager: AuthManager,
) : ViewModel() {
    private val _state: MutableStateFlow<ProfileScreenState> =
        MutableStateFlow(ProfileScreenState.Empty)

    val state: StateFlow<ProfileScreenState> = _state

    init {
        _state.tryEmit(ProfileScreenState.Loading)
        if (!handleAuth()) handleContentState()

    }

    private fun handleAuth(): Boolean {
        return if (authManager.isUserLogin()) {
            false
        } else {
            _state.tryEmit(ProfileScreenState.AuthenticateContent())
            true
        }
    }

    private fun handleContentState() {
        _state.tryEmit(ProfileScreenState.Content())
    }


    fun onLoginClick(username: String, password: String) {
        viewModelScope.launch {
            authManager.login(username, password)
            handleContentState()
        }
    }

    fun onRegisterClick(username: String, password: String, email: String) {
        viewModelScope.launch {
            authManager.register(username, password, email)
            handleContentState()
        }
    }

    fun onLogoutClick() {
        authManager.logout()
        _state.tryEmit(ProfileScreenState.AuthenticateContent())
    }
}