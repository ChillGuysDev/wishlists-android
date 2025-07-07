package com.nikol.wishlist.navigation

import com.nikol.wishlist.profile.ui.ScreenRouteProfile
import kotlinx.serialization.Serializable

enum class Screens {
    HOME, MY_WISHLIST, PROFILE
}
@Serializable
sealed class ScreenRoute(val routeName: String) {
    @Serializable
    data object Home: ScreenRoute(Screens.HOME.name)
    @Serializable
    data object MyWishlist: ScreenRoute(Screens.MY_WISHLIST.name)
    @Serializable
    data object Profile: ScreenRoute(ScreenRouteProfile)
    @Serializable
    data class Wishlist(val id: Long): ScreenRoute("wishlist/$id")
}

val topLevelRoutes = listOf(ScreenRoute.Home, ScreenRoute.MyWishlist, ScreenRoute.Profile)