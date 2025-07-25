package com.nikol.wishlist

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.calculateEndPadding
import androidx.compose.foundation.layout.calculateStartPadding
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.padding
import androidx.compose.material.BottomNavigation
import androidx.compose.material.BottomNavigationItem
import androidx.compose.material.Icon
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Stable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import androidx.navigation.NavDestination.Companion.hasRoute
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.nikol.wishlist.core.ui.theme.WishlistsTheme
import com.nikol.wishlist.navigation.ScreenRoute
import com.nikol.wishlist.navigation.topLevelRoutes
import com.nikol.wishlist.profile.ui.profileScreen
import com.nikol.wishlist.ui.features.home.HomeNavigationListener
import com.nikol.wishlist.ui.features.home.HomeScreen
import com.nikol.wishlist.ui.features.mywishlist.MyWishlistScreen
import com.nikol.wishlist.ui.features.wishlist.WishlistScreen
import com.nikol.wishlist.ui.features.wishlist.wishlistNavigationListener
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            WishlistsTheme {
                val navController = rememberNavController()

                val navBackStackEntry by navController.currentBackStackEntryAsState()
                val currentDestination = navBackStackEntry?.destination
                val showBottomBar = remember(currentDestination) {
                    topLevelRoutes.any { currentDestination?.hasRoute(it::class) == true }
                }
                Scaffold(
                    modifier = Modifier.fillMaxSize(),
                    bottomBar = {
                        if (showBottomBar) {
                            BottomNavigation(WindowInsets.navigationBars) {
                                topLevelRoutes.forEach { topLevelRoute ->
                                    BottomNavigationItem(
                                        icon = {
                                            Icon(
                                                Icons.Default.Home,
                                                contentDescription = ""
                                            )
                                        },
                                        label = { Text(topLevelRoute.routeName) },
                                        selected = currentDestination?.hasRoute(topLevelRoute::class) == true,
                                        onClick = {
                                            navController.navigate(topLevelRoute) {
                                                popUpTo(navController.graph.findStartDestination().id) {
                                                    saveState = true
                                                }
                                                launchSingleTop = true
                                                restoreState = true
                                            }
                                        }
                                    )
                                }
                            }
                        }
                    }
                ) { innerPadding ->
                    AppNavHost(
                        modifier = Modifier.padding(
                            bottom = if (showBottomBar) innerPadding.calculateBottomPadding() else 0.dp,
                            top = innerPadding.calculateTopPadding(),
                            start = innerPadding.calculateStartPadding(LayoutDirection.Ltr),
                            end = innerPadding.calculateEndPadding(LayoutDirection.Ltr)
                        ),
                        navController = navController
                    )
                }
            }
        }
    }
}

@Composable
fun AppNavHost(
    modifier: Modifier = Modifier,
    navController: NavHostController,
    startDestination: ScreenRoute = ScreenRoute.Home,
) {
    NavHost(
        modifier = modifier,
        navController = navController,
        startDestination = startDestination
    ) {
        composable<ScreenRoute.Home> {
            HomeScreen(
                navigationListener = homeNavigation {
                    navController.navigate(
                        route = ScreenRoute.MyWishlist.routeName
                    )
                }
            )
        }
        composable<ScreenRoute.MyWishlist> { MyWishlistScreen(onNavigateBack = { navController.navigateUp() }) }
        profileScreen<ScreenRoute.Profile>(
            openWishlist = { id: Long ->
                navController.navigate(ScreenRoute.Wishlist(id)) {
                    popUpTo(ScreenRoute.Profile) {
                        saveState = true
                    }
                    restoreState = true
                }
            }
        )
//        composable<ScreenRoute.Profile> {
//            ProfileScreen(
//                profileNavigationListener {
//                    navController.navigate(ScreenRoute.Wishlist(it)) {
//                        popUpTo(ScreenRoute.Profile) {
//                            saveState = true
//                        }
//                        restoreState = true
//                    }
//                }
//            )
//        }
        composable<ScreenRoute.Wishlist> { backStackEntry ->
            WishlistScreen(wishlistNavigationListener { navController.navigateUp() })
        }
    }
}

private fun homeNavigation(onCreateList: () -> Unit): HomeNavigationListener {
    return object : HomeNavigationListener {
        override fun navigateToCreateList() {
            onCreateList()
        }
    }
}

@Stable
class BottomBarVisibilityState(
    private val navController: NavHostController
) {
    val isVisible: Boolean
        @Composable get() {
            val navBackStackEntry by navController.currentBackStackEntryAsState()
            val currentRoute = navBackStackEntry?.destination?.route
            return currentRoute in topLevelRoutes.map { it.routeName }
        }
}