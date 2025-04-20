package com.nikol.wishlist.ui.uikit

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.nikol.wishlist.ui.theme.WishlistsTheme

@Composable
fun ButtonRegular(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    isLoading: Boolean = false,
) {
    Button(
        onClick = onClick,
        enabled = enabled,
        modifier = modifier,
        shape = MaterialTheme.shapes.medium,
        content = {
            if (isLoading) {
                CircularProgressIndicator(Modifier.size(16.dp))
            } else {
                Text(text, style = MaterialTheme.typography.bodyLarge)
            }
        }
    )
}

@Composable
fun ButtonSmall(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    isLoading: Boolean = false,
) {
    Button(
        onClick = onClick,
        enabled = enabled,
        modifier = modifier,
        shape = MaterialTheme.shapes.medium,
        content = {
            Box(contentAlignment = Alignment.Center) {
                if (isLoading) {
                    CircularProgressIndicator(
                        Modifier.size(12.dp),
                        color = MaterialTheme.colorScheme.onSurface,
                        strokeWidth = 1.dp
                    )
                }
                Text(text, Modifier.alpha(if (isLoading) 0f else 1f), style = MaterialTheme.typography.labelMedium)
            }
        }
    )
}

@Preview
@Composable
fun ButtonRegularPreview() {
    WishlistsTheme {
        ButtonRegular(
            text = "Button",
            onClick = {},
            modifier = Modifier,
            enabled = true,
            isLoading = false
        )
    }
}

@Preview
@Composable
fun ButtonSmallPreview() {
    WishlistsTheme {
        ButtonSmall(
            text = "Button",
            onClick = {},
            modifier = Modifier,
            enabled = true,
            isLoading = false
        )
    }
}

@Preview
@Composable
fun ButtonSmallLoadingPreview() {
    WishlistsTheme {
        ButtonSmall(
            text = "Button",
            onClick = {},
            modifier = Modifier,
            enabled = true,
            isLoading = true
        )
    }
}