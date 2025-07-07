package com.nikol.wishlist.core.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val LightColorScheme = lightColorScheme(
    primary = SoftOrange,
    onPrimary = Color.White,
    secondary = WarmPeach,
    onSecondary = MutedBrown,
    background = LightBeige,
    onBackground = TextPrimary,
    surface = LightBeige,
    onSurface = TextPrimary,
    error = DeepTerracotta,
    onError = Color.White,
    surfaceVariant = WarmGray
)

private val DarkColorScheme = darkColorScheme(
    primary = SoftOrange,
    onPrimary = Color.Black,
    secondary = WarmPeach,
    onSecondary = MutedBrown,
    background = Color.Black,
    onBackground = WarmGray,
    surface = Color.DarkGray,
    onSurface = WarmGray,
    error = DeepTerracotta,
    onError = Color.Black,
    surfaceVariant = WarmGray
)
//private val DarkColorScheme = darkColorScheme(
//    primary = Purple80,
//    secondary = PurpleGrey80,
//    tertiary = Pink80
//)
//
//private val LightColorScheme = lightColorScheme(
//    primary = Purple40,
//    secondary = PurpleGrey40,
//    tertiary = Pink40
//
//    /* Other default colors to override
//    background = Color(0xFFFFFBFE),
//    surface = Color(0xFFFFFBFE),
//    onPrimary = Color.White,
//    onSecondary = Color.White,
//    onTertiary = Color.White,
//    onBackground = Color(0xFF1C1B1F),
//    onSurface = Color(0xFF1C1B1F),
//    */
//)

@Composable
fun WishlistsTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    // Dynamic color is available on Android 12+
    dynamicColor: Boolean = true,
    content: @Composable () -> Unit
) {
    val colorScheme = when {
//        dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
//            val context = LocalContext.current
//            if (darkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
//        }

        darkTheme -> DarkColorScheme
        else -> LightColorScheme
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        shapes = AppShapes,
        content = content
    )
}