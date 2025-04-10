package com.nikol.wishlist.ui.features.profile

import android.content.Context
import android.net.Uri
import android.webkit.MimeTypeMap
import androidx.core.net.toFile
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.nikol.wishlist.domain.auth.AuthInteractor
import com.nikol.wishlist.domain.user.User
import com.nikol.wishlist.domain.user.UserInteractor
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import java.io.File
import java.io.FileInputStream
import java.io.FileOutputStream
import javax.inject.Inject
import kotlin.io.path.createTempFile
import kotlin.io.path.outputStream

interface ProfileUserAvatarListener {
    fun onAvatarPickClick(uri: Uri)
    fun onAvatarDeleteClick()
}

interface ProfileAuthListener {
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
    ) : ProfileScreenState
}

@HiltViewModel
class ProfileViewModel @Inject constructor(
    @ApplicationContext private val context: Context,
    private val authInteractor: AuthInteractor,
    private val userInteractor: UserInteractor,
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

            _state.tryEmit(user.toProfileScreenState())
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
}

private fun User.toProfileScreenState(): ProfileScreenState.Content {
    return ProfileScreenState.Content(
        avatarUrl = avatarUrl,
        name = name,
        email = email,
        bio = bio,
        birthDate = birthDate
    )
}

private fun Context.copyFileFromUri(context: Context, uri: Uri, destinationFile: File): Boolean {
    return try {
        context.contentResolver.openFileDescriptor(uri, "r")?.use { parcelFileDescriptor ->
            val fileDescriptor = parcelFileDescriptor.fileDescriptor
            FileInputStream(fileDescriptor).use { inputStream ->
                FileOutputStream(destinationFile).use { outputStream ->
                    inputStream.copyTo(outputStream)
                }
            }
        }
        true
    } catch (e: Exception) {
        e.printStackTrace()
        false
    }
}