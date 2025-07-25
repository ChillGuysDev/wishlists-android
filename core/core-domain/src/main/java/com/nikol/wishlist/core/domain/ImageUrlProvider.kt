package com.nikol.wishlist.core.domain

interface ImageUrlProvider {
    fun getImageUrl(imagePath: String?): String?
}
class ImageUrlProviderImpl(
    private val baseUrl: String,
) : ImageUrlProvider {
    override fun getImageUrl(imagePath: String?): String? {
        if (imagePath.isNullOrBlank()) return null

        return "${baseUrl.removeSuffix("/")}$imagePath"
    }
}