package com.nikol.wishlist.core.data.network

import com.nikol.wishlist.core.domain.user.User
import com.nikol.wishlist.core.domain.user.UserInteractor
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import java.io.File
import javax.inject.Inject
import javax.inject.Singleton


@Singleton
class UserInteractorImpl @Inject constructor(
    private val userApiService: UserApiService,
) : UserInteractor {
    private var currentUser: User? = null

    override suspend fun getUser(force: Boolean): User? {
        if (force) {
            currentUser = loadUser()
        }

        return currentUser ?: loadUser()?.also {
            currentUser = it
        }
    }

    override suspend fun updateUserAvatar(file: File): String? {
        return try {
            val requestBody = file.asRequestBody("image/*".toMediaTypeOrNull())
            val body = MultipartBody.Part.createFormData("file", file.name, requestBody)
            userApiService.updateUserAvatar(body).path
        } catch (e: Exception) {
            null
        }
    }

    override suspend fun deleteUserAvatar(): Boolean {
        return try {
            userApiService.deleteUserAvatar()
            true
        } catch (e: Exception) {
            false
        }
    }

    private suspend fun loadUser() = try {
        userApiService.getCurrentUser().toDomain()
    } catch (e: Exception) {
        null
    }
}