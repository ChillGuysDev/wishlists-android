package com.nikol.wishlist.domain

interface ImageUrlProvider {
    fun getImageUrl(imagePath: String): String
}
class ImageUrlProviderImpl(
    private val baseUrl: String,
) : ImageUrlProvider {
    override fun getImageUrl(imagePath: String): String {
        return "${baseUrl.removeSuffix("/")}$imagePath"
    }
}