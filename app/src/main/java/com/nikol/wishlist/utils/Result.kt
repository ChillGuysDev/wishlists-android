package com.nikol.wishlist.utils

sealed class Result<out T> {
    data class Success<out T>(val data: T) : Result<T>()
    data class Error(val exception: Throwable? = null) : Result<Nothing>()
    object Loading : Result<Nothing>()
}