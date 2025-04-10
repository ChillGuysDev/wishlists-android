package com.nikol.wishlist.domain.user

import com.nikol.wishlist.network.UserApiService
import com.nikol.wishlist.network.UserProfile
import com.nikol.wishlist.network.toDomain
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import java.io.File
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class UserInteractor @Inject constructor(
    private val userApiService: UserApiService,
) {
    private var currentUser: User? = null


    suspend fun getUser(force: Boolean = false): User? {
        if (force) {
            currentUser = loadUser()
        }

        return currentUser ?: loadUser()?.also {
            currentUser = it
        }
    }

    suspend fun updateUserAvatar(file: File): String? {
        return try {
            val requestBody = file.asRequestBody("image/*".toMediaTypeOrNull())
            val body = MultipartBody.Part.createFormData("file", file.name, requestBody)
            userApiService.updateUserAvatar(body).path
        } catch (e: Exception) {
            null
        }
    }

    suspend fun deleteUserAvatar(): Boolean {
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