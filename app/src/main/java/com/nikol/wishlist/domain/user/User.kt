package com.nikol.wishlist.domain.user

data class User(
    val id: Int,
    val username: String,
    val name: String,
    val email: String,
    val avatarUrl: String,
    val bio: String,
    val birthDate: String
)