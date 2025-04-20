package com.nikol.wishlist.ui.features.createWishlist

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.nikol.wishlist.R
import com.nikol.wishlist.ui.features.profile.WishlistItemInput
import com.nikol.wishlist.ui.theme.WishlistsTheme
import com.nikol.wishlist.ui.uikit.ButtonRegular
import com.nikol.wishlist.ui.uikit.ButtonSmall

fun interface CreateWishlistBottomSheetListener {
    fun onCreateWishlist(
        name: String,
        description: String,
        items: List<WishlistItemInput>
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CreateWishlistBottomSheet(
    showBottomSheet: Boolean,
    onDismiss: () -> Unit,
    listener: CreateWishlistBottomSheetListener,
) {
    if (showBottomSheet) {
        ModalBottomSheet(onDismiss) {
            CreateWishlistContent(
                modifier = Modifier,
                listener = listener,
            )
        }
    }
}

@Composable
private fun CreateWishlistContent(
    modifier: Modifier = Modifier,
    listener: CreateWishlistBottomSheetListener,
) {
    var name by rememberSaveable { mutableStateOf("") }
    var description by rememberSaveable { mutableStateOf("") }

    var items by rememberSaveable { mutableStateOf(listOf<WishlistItemInput>()) }
    var isAddingItem by rememberSaveable { mutableStateOf(false) }

    Column(modifier, verticalArrangement = Arrangement.spacedBy(16.dp)) {
        OutlinedTextField(
            value = name,
            onValueChange = { name = it },
            label = { Text("Name") },
            placeholder = { Text("Enter wishlist name") },
            modifier = Modifier.fillMaxWidth(),
        )

        OutlinedTextField(
            value = description,
            onValueChange = { description = it },
            label = { Text("Description") },
            placeholder = { Text("Enter wishlist description") },
            modifier = Modifier.fillMaxWidth(),
        )

        if (isAddingItem) {
            CreateWishlistItemInput(
                modifier = Modifier.fillMaxWidth(),
                onComplete = { item ->
                    items += item
                    isAddingItem = false
                },
                onCancel = {
                    isAddingItem = false
                }
            )

        } else {
            ButtonSmall(
                stringResource(R.string.create_wishlist_add_item_title),
                onClick = { isAddingItem = true },
                modifier = Modifier.fillMaxWidth(),
            )
        }

        ButtonRegular(
            stringResource(R.string.create_wishlist_create_title),
            onClick = {
                listener.onCreateWishlist(
                    name = name,
                    description = description,
                    items = items
                )
            },
            modifier = Modifier.fillMaxWidth(),
        )
    }
}

@Composable
private fun CreateWishlistItemInput(
    modifier: Modifier = Modifier,
    onComplete: (WishlistItemInput) -> Unit,
    onCancel: () -> Unit,
) {
    var name by rememberSaveable { mutableStateOf("") }
    var description by rememberSaveable { mutableStateOf("") }
    var price by rememberSaveable { mutableStateOf("") }
    var url by rememberSaveable { mutableStateOf("") }

    Column(modifier, horizontalAlignment = Alignment.CenterHorizontally) {
        OutlinedTextField(
            value = name,
            onValueChange = { name = it },
            label = { Text("Name") },
            placeholder = { Text("Enter item name") },
            modifier = Modifier.fillMaxWidth(),
        )

        OutlinedTextField(
            value = description,
            onValueChange = { description = it },
            label = { Text("Description") },
            placeholder = { Text("Enter item description") },
            modifier = Modifier.fillMaxWidth(),
        )

        OutlinedTextField(
            value = price,
            onValueChange = { price = it },
            label = { Text("Price") },
            placeholder = { Text("Enter item price") },
            keyboardOptions = KeyboardOptions.Default.copy(
                keyboardType = KeyboardType.Number
            ),
            modifier = Modifier.fillMaxWidth(),
        )

        OutlinedTextField(
            value = url,
            onValueChange = { url = it },
            label = { Text("URL") },
            placeholder = { Text("Enter item URL") },
            modifier = Modifier.fillMaxWidth(),
        )

        Row(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
            ButtonSmall(text = stringResource(R.string.create_wishlist_add_item_cancel_title),
                onClick = onCancel,
                modifier = Modifier.weight(1f),
            )
            ButtonSmall(
                text = stringResource(R.string.create_wishlist_add_item_title),
                onClick = {
                    onComplete(WishlistItemInput(
                        name = name,
                        description = description,
                        imageUrl = null,
                        price = price.toIntOrNull(),
                        url = url
                    ))
                },
                modifier = Modifier.weight(1f),
            )
        }
    }
}

@Preview
@Composable
fun CreateWishlistBottomSheetPreview() {
    WishlistsTheme {
        CreateWishlistContent(
            modifier = Modifier.fillMaxWidth(),
            listener = { _, _, _ -> }
        )
    }
}

@Preview
@Composable
fun CreateWishlistItemInputPreview() {
    WishlistsTheme {
        CreateWishlistItemInput(
            modifier = Modifier.fillMaxWidth(),
            onComplete = {},
            onCancel = {}
        )
    }
}