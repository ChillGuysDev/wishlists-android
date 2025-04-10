package com.nikol.wishlist.network

import com.nikol.wishlist.domain.user.User
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import okhttp3.MultipartBody
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part

interface UserApiService {
    @GET("me")
    suspend fun getCurrentUser(): UserProfile

    @Multipart
    @POST("me/avatar")
    suspend fun updateUserAvatar(@Part avatar: MultipartBody.Part): UserAvatarResponse

    @DELETE
    suspend fun deleteUserAvatar(): Unit

}

@Serializable
data class UserProfile(
    @SerialName("avatar_url") val avatarUrl: String,
    @SerialName("bio") val bio: String,
    @SerialName("birth_date") val birthDate: String,
    @SerialName("email") val email: String,
    @SerialName("id") val id: Int,
    @SerialName("name") val name: String,
    @SerialName("username") val username: String
)

fun UserProfile.toDomain(): User {
    return User(
        id = id,
        username = username,
        name = name,
        email = email,
        avatarUrl = avatarUrl,
        bio = bio,
        birthDate = birthDate
    )
}

@Serializable
data class UserAvatarResponse(
    @SerialName("path") val path: String,
)