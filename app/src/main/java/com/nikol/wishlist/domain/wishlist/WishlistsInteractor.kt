package com.nikol.wishlist.domain.wishlist

import com.nikol.wishlist.domain.ImageUrlProvider
import com.nikol.wishlist.domain.auth.AuthInteractor
import com.nikol.wishlist.domain.user.UserInteractor
import com.nikol.wishlist.network.wishlist.CreateWishlistItemRequest
import com.nikol.wishlist.network.wishlist.CreateWishlistRequest
import com.nikol.wishlist.network.wishlist.UpdateWishlistItemRequest
import com.nikol.wishlist.network.wishlist.UpdateWishlistRequest
import com.nikol.wishlist.network.wishlist.WishlistApi
import com.nikol.wishlist.network.wishlist.WishlistApiService
import com.nikol.wishlist.network.wishlist.WishlistItem
import javax.inject.Inject
import javax.inject.Singleton

data class WishlistDomain(
    val id: Int,
    val name: String,
    val description: String?,
    val createdAt: String,
    val items: List<WishlistItemDomain>
)

data class WishlistItemDomain(
    val id: Int,
    val name: String,
    val description: String?,
    val imageUrl: String?,
    val price: Int?,
    val url: String?,
)

data class CreateWishlistInput(
    val name: String,
    val description: String
)

data class UpdateWishlistInput(
    val name: String,
    val description: String
)

data class CreateWishlistItemInput(
    val name: String,
    val description: String
)

data class UpdateWishlistItemInput(
    val name: String,
    val description: String
)

private fun WishlistApi.toDomain(urlProvider: ImageUrlProvider): WishlistDomain {
    return WishlistDomain(
        id = id,
        name = name,
        description = description,
        createdAt = createdAt,
        items = items.map { it.toDomain(urlProvider) }
    )
}

private fun WishlistItem.toDomain(urlProvider: ImageUrlProvider): WishlistItemDomain {
    return WishlistItemDomain(
        id = id,
        name = name,
        description = description,
        imageUrl = urlProvider.getImageUrl(imageUrl),
        price = price,
        url = url,
    )
}

private fun CreateWishlistInput.toApi(): CreateWishlistRequest {
    return CreateWishlistRequest(name = name, description = description)
}

private fun UpdateWishlistInput.toApi(): UpdateWishlistRequest {
    return UpdateWishlistRequest(name = name, description = description)
}

private fun CreateWishlistItemInput.toApi(): CreateWishlistItemRequest {
    return CreateWishlistItemRequest(name = name, description = description)
}

private fun UpdateWishlistItemInput.toApi(): UpdateWishlistItemRequest {
    return UpdateWishlistItemRequest(name = name, description = description)
}

@Singleton
class WishlistsInteractor @Inject constructor(
    private val wishlistApi: WishlistApiService,
    private val imageUrlProvider: ImageUrlProvider,
    private val userInteractor: UserInteractor,
)  {
    suspend fun getUserWishlists(): List<WishlistDomain> {
        val userId = userInteractor.getUser()?.id ?: return emptyList()

        return wishlistApi.getWishlists(userId).map { it.toDomain(imageUrlProvider) }
    }

    suspend fun createWishlist(input: CreateWishlistInput, items: List<WishlistItemDomain>): WishlistDomain {
        val wishlist = wishlistApi.createWishlist(input.toApi()).toDomain(imageUrlProvider)
        val responseItems = items.map { item ->
            wishlistApi.addItemToWishlist(wishlist.id, item.toApi())
        }
        return wishlist.copy(items = responseItems.map { it.toDomain(imageUrlProvider) })
    }

    suspend fun updateWishlist(input: UpdateWishlistInput): WishlistDomain {
        return wishlistApi.updateWishlist(input.toApi()).toDomain(imageUrlProvider)
    }

    suspend fun deleteWishlist(wishlistId: Int) {
        wishlistApi.deleteWishlist(wishlistId)
    }

    suspend fun getWishlistById(wishlistId: Int): WishlistDomain {
        return wishlistApi.getWishlistById(wishlistId).toDomain(imageUrlProvider)
    }

    suspend fun addItemToWishlist(wishlistId: Int, input: WishlistItemDomain): WishlistItemDomain {
        return wishlistApi.addItemToWishlist(wishlistId, input.toApi()).toDomain(imageUrlProvider)
    }

    suspend fun deleteWishlistItem(wishlistId: Int, itemId: Int) {
        wishlistApi.deleteWishlistItem(wishlistId, itemId)
    }

    suspend fun updateWishlistItem(
        wishlistId: Int,
        itemId: Int,
        input: UpdateWishlistItemInput
    ): WishlistItemDomain {
        return wishlistApi.updateWishlistItem(wishlistId, itemId, input.toApi()).toDomain(imageUrlProvider)
    }
}

private fun WishlistItemDomain.toApi(): CreateWishlistItemRequest {
    return CreateWishlistItemRequest(
        name = name,
        description = description.orEmpty()
    )
}