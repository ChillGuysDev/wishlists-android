package com.nikol.wishlist.core.domain.user

import java.io.File

interface UserInteractor {
    suspend fun getUser(force: Boolean = false): User?
    suspend fun updateUserAvatar(file: File): String?
    suspend fun deleteUserAvatar(): Boolean
}
