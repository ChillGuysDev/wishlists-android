package com.nikol.wishlist.navigation

enum class Screens {
    HOME, MY_WISHLIST, PROFILE
}
sealed class ScreenRoute(val routeName: String) {
    data object Home: ScreenRoute(Screens.HOME.name)
    data object MyWishlist: ScreenRoute(Screens.MY_WISHLIST.name)
    data object Profile: ScreenRoute(Screens.PROFILE.name)
}

val topLevelRoutes = listOf(ScreenRoute.Home, ScreenRoute.MyWishlist, ScreenRoute.Profile)