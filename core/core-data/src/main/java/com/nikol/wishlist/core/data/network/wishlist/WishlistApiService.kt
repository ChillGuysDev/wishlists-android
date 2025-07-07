package com.nikol.wishlist.core.data.network.wishlist

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import retrofit2.http.*

interface WishlistApiService {
    @GET("wishlists")
    suspend fun getWishlists(
        @Query("user") userId: Int,
        @Query("cursor") cursor: String? = null,
        @Query("limit") limit: Int? = null
    ): PaginatedResponse<WishlistDto>

    @POST("wishlists")
    suspend fun createWishlist(@Body request: CreateWishlistRequest): WishlistDto

    @PUT("wishlists")
    suspend fun updateWishlist(@Body request: UpdateWishlistRequest): WishlistDto

    @DELETE("wishlists/{wishlist_id}")
    suspend fun deleteWishlist(@Path("wishlist_id") wishlistId: Long)

    @GET("wishlists/{wishlist_id}")
    suspend fun getWishlistById(@Path("wishlist_id") wishlistId: Long): WishlistDto

    @POST("wishlists/{wishlist_id}/items")
    suspend fun addItemToWishlist(
        @Path("wishlist_id") wishlistId: Long,
        @Body request: CreateWishlistItemRequest
    ): WishlistItemDto

    @DELETE("wishlists/{wishlist_id}/items/{item_id}")
    suspend fun deleteWishlistItem(
        @Path("wishlist_id") wishlistId: Long,
        @Path("item_id") itemId: Long
    )

    @PUT("wishlists/{wishlist_id}/items/{item_id}")
    suspend fun updateWishlistItem(
        @Path("wishlist_id") wishlistId: Long,
        @Path("item_id") itemId: Long,
        @Body request: UpdateWishlistItemRequest
    ): WishlistItemDto
}



@Serializable
data class PaginatedResponse<T>(
    @SerialName("has_more") val hasMore: Boolean,
    @SerialName("next_cursor") val nextCursor: String? = null,
    val data: List<T>
)

@Serializable
data class WishlistDto(
    val id: Long,
    val name: String,
    val description: String? = null,
    @SerialName("owner_id") val ownerId: Long,
    @SerialName("created_at") val createdAt: String,
    val items: List<WishlistItemDto> = emptyList()
)

@Serializable
data class WishlistItemDto(
    val id: Long,
    val name: String,
    val description: String? = null,
    @SerialName("image_url") val imageUrl: String? = null,
    val price: Int? = null,
    val url: String? = null,
    @SerialName("created_at") val createdAt: String
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
