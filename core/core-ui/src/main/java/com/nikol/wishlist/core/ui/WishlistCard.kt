package com.nikol.wishlist.core.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil3.ColorImage
import coil3.annotation.ExperimentalCoilApi
import coil3.compose.AsyncImage
import coil3.compose.AsyncImagePreviewHandler
import coil3.compose.LocalAsyncImagePreviewHandler

@Composable
fun WishlistCard(
    title: String,
    icon: String?,
    modifier: Modifier = Modifier
) {
    Card(modifier, shape = RoundedCornerShape(16.dp)) {
        Row(Modifier.fillMaxWidth().padding(16.dp), horizontalArrangement = Arrangement.spacedBy(16.dp), verticalAlignment = Alignment.CenterVertically) {
            AsyncImage(
                model = icon,
                contentDescription = null,
                modifier = Modifier.size(80.dp)
            )
            Text(title, style = MaterialTheme.typography.displayMedium)
        }
    }
}


@Preview
@Composable
private fun WishlistCardPreview() {
    PreviewWithCoil {
        WishlistCard(
            title = "whishlist",
            icon = "",
        )
    }
}

@OptIn(ExperimentalCoilApi::class)
@Composable
inline fun PreviewWithCoil(crossinline content: @Composable () -> Unit = {}) {
    CompositionLocalProvider(LocalAsyncImagePreviewHandler provides previewHandler) {
        content()
    }
}
@OptIn(ExperimentalCoilApi::class)
val previewHandler = AsyncImagePreviewHandler {
    ColorImage(Color.Red.toArgb())
}
