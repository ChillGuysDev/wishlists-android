package com.nikol.wishlist

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.BottomNavigation
import androidx.compose.material.BottomNavigationItem
import androidx.compose.material.Icon
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.nikol.wishlist.navigation.ScreenRoute
import com.nikol.wishlist.navigation.topLevelRoutes
import com.nikol.wishlist.ui.features.home.HomeNavigationListener
import com.nikol.wishlist.ui.features.home.HomeScreen
import com.nikol.wishlist.ui.features.mywishlist.MyWishlistScreen
import com.nikol.wishlist.ui.features.profile.ProfileScreen
import com.nikol.wishlist.ui.theme.WishlistsTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            WishlistsTheme {
                val navController = rememberNavController()
                Scaffold(modifier = Modifier.fillMaxSize(), bottomBar = {
                    BottomNavigation {
                        val navBackStackEntry by navController.currentBackStackEntryAsState()
                        val currentDestination = navBackStackEntry?.destination
                        topLevelRoutes.forEach { topLevelRoute ->
                            BottomNavigationItem(
                                icon = {
                                    Icon(
                                        Icons.Default.Home,
                                        contentDescription = ""
                                    )
                                },
                                label = { Text(topLevelRoute.routeName) },
                                selected = currentDestination?.route == topLevelRoute.routeName,
                                onClick = {
                                    navController.navigate(topLevelRoute.routeName) {
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
                }) { innerPadding ->
                    AppNavHost(Modifier.padding(innerPadding), navController)
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
        startDestination = startDestination.routeName
    ) {
        composable(ScreenRoute.Home.routeName) {
            HomeScreen(
                navigationListener = homeNavigation {
                    navController.navigate(
                        route = ScreenRoute.MyWishlist.routeName
                    )
                }
            )
        }
        composable(ScreenRoute.MyWishlist.routeName) { MyWishlistScreen(onNavigateBack = { navController.navigateUp() }) }
        composable(ScreenRoute.Profile.routeName) { ProfileScreen() }
    }
}

private fun homeNavigation(onCreateList: () -> Unit): HomeNavigationListener {
    return object : HomeNavigationListener {
        override fun navigateToCreateList() {
            onCreateList()
        }
    }
}