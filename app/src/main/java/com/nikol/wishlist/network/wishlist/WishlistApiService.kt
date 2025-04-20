package com.nikol.wishlist.network.wishlist

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import retrofit2.http.*

interface WishlistApiService {
    @GET("wishlists")
    suspend fun getWishlists(@Query("user") userId: Int): List<WishlistApi>

    @POST("wishlists")
    suspend fun createWishlist(@Body request: CreateWishlistRequest): WishlistApi

    @PUT("wishlists")
    suspend fun updateWishlist(@Body request: UpdateWishlistRequest): WishlistApi

    @DELETE("wishlists/{wishlist_id}")
    suspend fun deleteWishlist(@Path("wishlist_id") wishlistId: Int)

    @GET("wishlists/{wishlist_id}")
    suspend fun getWishlistById(@Path("wishlist_id") wishlistId: Int): WishlistApi

    @POST("wishlists/{wishlist_id}/items")
    suspend fun addItemToWishlist(
        @Path("wishlist_id") wishlistId: Int,
        @Body request: CreateWishlistItemRequest
    ): WishlistItem

    @DELETE("wishlists/{wishlist_id}/items/{item_id}")
    suspend fun deleteWishlistItem(
        @Path("wishlist_id") wishlistId: Int,
        @Path("item_id") itemId: Int
    )

    @PUT("wishlists/{wishlist_id}/items/{item_id}")
    suspend fun updateWishlistItem(
        @Path("wishlist_id") wishlistId: Int,
        @Path("item_id") itemId: Int,
        @Body request: UpdateWishlistItemRequest
    ): WishlistItem
}

@Serializable
data class WishlistApi(
    val id: Int,
    val name: String,
    val description: String,
    @SerialName("image_url") val imageUrl: String? = null,
    @SerialName("created_at") val createdAt: String,
    val items: List<WishlistItem> = emptyList(),
)

@Serializable
data class WishlistItem(
    val id: Int,
    val name: String,
    val description: String,
    @SerialName("image_url") val imageUrl: String? = null,
    val price: Int? = null,
    val url: String? = null,
    @SerialName("created_at") val createdAt: String,
)


@Serializable
data class CreateWishlistRequest(
    val name: String,
    val description: String
)

@Serializable
data class UpdateWishlistRequest(
    val name: String,
    val description: String
)

@Serializable
data class CreateWishlistItemRequest(
    val name: String,
    val description: String
)

@Serializable
data class UpdateWishlistItemRequest(
    val name: String,
    val description: String
)

@Serializable
class WishlistCreateResponse(
    val id: String,
)

@Serializable
data class WishlistItemBody(
    val name: String,
    val description: String? = null,
)
