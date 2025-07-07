package com.nikol.wishlist.core.data.network

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.Header
import retrofit2.http.POST

interface AuthApiService {
    @POST("auth/login")
    suspend fun login(@Body body: LoginBody): LoginResponse

    @POST("auth/logout")
    suspend fun logout(): String

    @POST("auth/refresh")
    fun refreshToken(@Body body: RefreshTokenBody): Call<LoginResponse>

    @POST("auth/register")
    suspend fun register(@Body body: RegisterBody): RegisterResponse
}

@Serializable
class RefreshTokenBody(@SerialName("refresh_token") val refreshToken: String)

@Serializable
class LoginBody(val username: String, val password: String)

@Serializable
data class LoginResponse(
    @SerialName("access_token") val accessToken: String,
    @SerialName("refresh_token") val refreshToken: String
)

@Serializable
class RegisterBody(
    val username: String,
    val password: String,
    val email: String
)

@Serializable
class RegisterResponse(
    @SerialName("access_token") val accessToken: String,
    @SerialName("refresh_token") val refreshToken: String,
    @SerialName("user_id") val userId: String,
)
