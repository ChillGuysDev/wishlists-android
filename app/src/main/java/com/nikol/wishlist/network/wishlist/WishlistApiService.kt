package com.nikol.wishlist.network.wishlist

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import retrofit2.http.*

interface WishlistApiService {
    @GET("/wishlists")
    suspend fun getWishlists(@Query("id") id: String): List<Wishlist>

    @POST("/wishlists")
    suspend fun createWishlist(@Body wishlistBody: WishlistBody): WishlistCreateResponse

    @PUT("/wishlists")
    suspend fun updateWishlist(@Body wishlistBody: WishlistBody): String

    @DELETE("/wishlists/{wishlist_id}")
    suspend fun deleteWishlist(@Path("wishlist_id") wishlistId: String): String

    @GET("/wishlists/{wishlist_id}")
    suspend fun getWishlistById(@Path("wishlist_id") wishlistId: String): Wishlist

    @POST("/wishlists/{wishlist_id}/items")
    suspend fun addItemToWishlist(
        @Path("wishlist_id") wishlistId: String,
        @Body itemRequest: WishlistItemBody
    ): WishlistCreateResponse

    @DELETE("/wishlists/{wishlist_id}/items/{item_id}")
    suspend fun deleteWishlistItem(
        @Path("wishlist_id") wishlistId: String,
        @Path("item_id") itemId: String
    ): String

    @PUT("/wishlists/{wishlist_id}/items/{item_id}")
    suspend fun updateWishlistItem(
        @Path("wishlist_id") wishlistId: String,
        @Path("item_id") itemId: String,
        @Body itemRequest: WishlistItemBody
    ): String
}

data class Wishlist(
    val id: Int,
    val name: String,
    val description: String,
    @SerialName("created_at") val createdAt: String,
    val items: List<WishlistItem> = emptyList(),
)

data class WishlistItem(
    val id: Int,
    val name: String,
    val description: String,
    @SerialName("created_at") val createdAt: String,
)


@Serializable
data class WishlistBody(
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


