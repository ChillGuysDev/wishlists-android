package com.nikol.wishlist.core.domain.auth


interface AuthInteractor {
    suspend fun isUserLogin(): Boolean
    suspend fun logout()
    suspend fun register(username: String, password: String, email: String): Boolean
    suspend fun login(username: String, password: String): Boolean
}