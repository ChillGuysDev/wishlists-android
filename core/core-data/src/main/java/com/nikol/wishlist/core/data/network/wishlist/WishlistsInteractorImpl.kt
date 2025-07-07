package com.nikol.wishlist.core.data.network.wishlist

import com.nikol.wishlist.core.domain.ImageUrlProvider
import com.nikol.wishlist.core.domain.user.UserInteractor
import com.nikol.wishlist.core.domain.wishlist.CreateWishlistInput
import com.nikol.wishlist.core.domain.wishlist.CreateWishlistItemInput
import com.nikol.wishlist.core.domain.wishlist.UpdateWishlistInput
import com.nikol.wishlist.core.domain.wishlist.UpdateWishlistItemInput
import com.nikol.wishlist.core.domain.wishlist.WishlistDomain
import com.nikol.wishlist.core.domain.wishlist.WishlistItemDomain
import com.nikol.wishlist.core.domain.wishlist.WishlistsInteractor
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class WishlistsInteractorImpl @Inject constructor(
    private val wishlistApi: WishlistApiService,
    private val imageUrlProvider: ImageUrlProvider,
    private val userInteractor: UserInteractor,
) : WishlistsInteractor {
    override suspend fun getUserWishlists(): List<WishlistDomain> {
        val userId = userInteractor.getUser()?.id ?: return emptyList()

        return wishlistApi.getWishlists(userId).data.map { it.toDomain(imageUrlProvider) }
    }

    override suspend fun createWishlist(
        input: CreateWishlistInput,
        items: List<WishlistItemDomain>
    ): WishlistDomain {
        val wishlist = wishlistApi.createWishlist(input.toApi()).toDomain(imageUrlProvider)
        val responseItems = items.map { item ->
            wishlistApi.addItemToWishlist(wishlist.id, item.toApi())
        }
        return wishlist.copy(items = responseItems.map { it.toDomain(imageUrlProvider) })
    }

    override suspend fun updateWishlist(input: UpdateWishlistInput): WishlistDomain {
        return wishlistApi.updateWishlist(input.toApi()).toDomain(imageUrlProvider)
    }

    override suspend fun deleteWishlist(wishlistId: Long) {
        wishlistApi.deleteWishlist(wishlistId)
    }

    override suspend fun getWishlistById(wishlistId: Long): WishlistDomain {
        return wishlistApi.getWishlistById(wishlistId).toDomain(imageUrlProvider)
    }

    override suspend fun addItemToWishlist(
        wishlistId: Long,
        input: WishlistItemDomain
    ): WishlistItemDomain {
        return wishlistApi.addItemToWishlist(wishlistId, input.toApi()).toDomain(imageUrlProvider)
    }

    override suspend fun deleteWishlistItem(wishlistId: Long, itemId: Long) {
        wishlistApi.deleteWishlistItem(wishlistId, itemId)
    }

    override suspend fun updateWishlistItem(
        wishlistId: Long,
        itemId: Long,
        input: UpdateWishlistItemInput
    ): WishlistItemDomain {
        return wishlistApi.updateWishlistItem(wishlistId, itemId, input.toApi())
            .toDomain(imageUrlProvider)
    }
}

private fun WishlistItemDomain.toApi(): CreateWishlistItemRequest {
    return CreateWishlistItemRequest(
        name = name,
        description = description.orEmpty()
    )
}

private fun WishlistDto.toDomain(urlProvider: ImageUrlProvider): WishlistDomain {
    return WishlistDomain(
        id = id,
        name = name,
        description = description,
        createdAt = createdAt,
        items = items.map { it.toDomain(urlProvider) }
    )
}

private fun WishlistItemDto.toDomain(urlProvider: ImageUrlProvider): WishlistItemDomain {
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