package com.nikol.wishlist.ui.features.profile

import android.content.Context
import android.net.Uri
import android.webkit.MimeTypeMap
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.nikol.wishlist.domain.ImageUrlProvider
import com.nikol.wishlist.domain.auth.AuthInteractor
import com.nikol.wishlist.domain.user.UserInteractor
import com.nikol.wishlist.domain.wishlist.CreateWishlistInput
import com.nikol.wishlist.domain.wishlist.WishlistsInteractor
import com.nikol.wishlist.utils.copyFileFromUri
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import java.io.File
import javax.inject.Inject

interface ProfileUserAvatarListener {
    fun onAvatarPickClick(uri: Uri)
    fun onAvatarDeleteClick()
}

interface ProfileAuthListener {
    fun onWishlistCreateClick(name: String, description: String, items: List<WishlistItemInput>)
    fun onAddWishlistItemClick(wishlistId: Int, item: WishlistItemInput)
    fun onLoginClick(username: String, password: String)
    fun onRegisterClick(username: String, password: String, email: String)
    fun onLogoutClick()
}

interface ProfileListener : ProfileUserAvatarListener, ProfileAuthListener

sealed interface ProfileScreenState {
    object Loading : ProfileScreenState
    object Empty : ProfileScreenState
    class AuthenticateContent : ProfileScreenState
    class Content(
        val avatarUrl: String? = null,
        val name: String,
        val email: String,
        val bio: String,
        val birthDate: String,
        val wishlists: List<WishlistUi> = emptyList()
    ) : ProfileScreenState
}

@HiltViewModel
class ProfileViewModel @Inject constructor(
    @ApplicationContext private val context: Context,
    private val authInteractor: AuthInteractor,
    private val userInteractor: UserInteractor,
    private val imageUrlProvider: ImageUrlProvider,
    private val wishlistsInteractor: WishlistsInteractor,
) : ViewModel(), ProfileListener {
    private val _state: MutableStateFlow<ProfileScreenState> =
        MutableStateFlow(ProfileScreenState.Empty)

    val state: StateFlow<ProfileScreenState> = _state

    init {
        viewModelScope.launch {
            _state.tryEmit(ProfileScreenState.Loading)
            if (!handleAuth()) handleContentState()

        }
    }

    private suspend fun handleAuth(): Boolean {
        return if (authInteractor.isUserLogin()) {
            false
        } else {
            _state.tryEmit(ProfileScreenState.AuthenticateContent())
            true
        }
    }

    private fun handleContentState() {
        viewModelScope.launch {
            val user = userInteractor.getUser()
            if (user == null) {
                _state.tryEmit(ProfileScreenState.AuthenticateContent())
                return@launch
            }

            val wishlists = loadUserWishlists()

            val state = ProfileScreenState.Content(
                avatarUrl = imageUrlProvider.getImageUrl(user.avatarUrl),
                name = user.name,
                email = user.email,
                bio = user.bio,
                birthDate = user.birthDate,
                wishlists = wishlists
            )
            _state.tryEmit(state)
        }
    }

    override fun onWishlistCreateClick(
        name: String,
        description: String,
        items: List<WishlistItemInput>
    ) {
        viewModelScope.launch {
            wishlistsInteractor.createWishlist(
                input = CreateWishlistInput(
                    name = name,
                    description = description
                ),
                items = items.map { it.toDomain() },
            )
        }
    }

    override fun onAddWishlistItemClick(wishlistId: Int, item: WishlistItemInput) {
        viewModelScope.launch {
            wishlistsInteractor.addItemToWishlist(
                wishlistId = wishlistId,
                input = item.toDomain()
            )
        }
    }

    override fun onAvatarDeleteClick() {
        viewModelScope.launch {
            userInteractor.deleteUserAvatar()
            handleContentState()
        }
    }

    override fun onAvatarPickClick(uri: Uri) {
        viewModelScope.launch {
            val mimeType = context.contentResolver.getType(uri)
            val extension = MimeTypeMap.getSingleton().getExtensionFromMimeType(mimeType)
            val file = File.createTempFile(
                "user_icon_${System.currentTimeMillis()}",
                extension,
                context.cacheDir
            )
            val isFileCopied = context.copyFileFromUri(context, uri, file)
            if (!isFileCopied) return@launch

            userInteractor.updateUserAvatar(file)
            file.delete()
            handleContentState()
        }
    }


    override fun onLoginClick(username: String, password: String) {
        viewModelScope.launch {
            authInteractor.login(username, password)
            handleContentState()
        }
    }

    override fun onRegisterClick(username: String, password: String, email: String) {
        viewModelScope.launch {
            authInteractor.register(username, password, email)
            handleContentState()
        }
    }

    override fun onLogoutClick() {
        viewModelScope.launch {
            authInteractor.logout()
            _state.tryEmit(ProfileScreenState.AuthenticateContent())
        }
    }

    private suspend fun loadUserWishlists() = try {
        wishlistsInteractor.getUserWishlists().toUi()
    } catch (e: Throwable) {
        emptyList()
    }
}
