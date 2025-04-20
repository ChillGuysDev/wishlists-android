package com.nikol.wishlist.ui.features.createWishlist

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.nikol.wishlist.ui.features.profile.WishlistItemInput
import com.nikol.wishlist.ui.theme.WishlistsTheme

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
        TextField(
            value = name,
            onValueChange = { name = it },
            label = { Text("Name") },
            placeholder = { Text("Enter wishlist name") },
        )

        TextField(
            value = description,
            onValueChange = { description = it },
            label = { Text("Description") },
            placeholder = { Text("Enter wishlist description") },
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
            Button(onClick = { isAddingItem = true }) {
                Text("Add Item")
            }
        }

        Button(
            onClick = {
                listener.onCreateWishlist(
                    name = name,
                    description = description,
                    items = items
                )
            }
        ) {
            Text("Create Wishlist")
        }
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
        TextField(
            value = name,
            onValueChange = { name = it },
            label = { Text("Name") },
            placeholder = { Text("Enter item name") },
            modifier = Modifier.fillMaxWidth(),
        )

        TextField(
            value = description,
            onValueChange = { description = it },
            label = { Text("Description") },
            placeholder = { Text("Enter item description") },
            modifier = Modifier.fillMaxWidth(),
        )

        TextField(
            value = price,
            onValueChange = { price = it },
            label = { Text("Price") },
            placeholder = { Text("Enter item price") },
            keyboardOptions = KeyboardOptions.Default.copy(
                keyboardType = KeyboardType.Number
            ),
            modifier = Modifier.fillMaxWidth(),
        )

        TextField(
            value = url,
            onValueChange = { url = it },
            label = { Text("URL") },
            placeholder = { Text("Enter item URL") },
            modifier = Modifier.fillMaxWidth(),
        )

        Button(
            onClick = {
                onComplete(WishlistItemInput(name, description, null, price.toIntOrNull(), url))
            }) {
            Text("Add Item")
        }

        Button(onClick = onCancel) {
            Text("Cancel")
        }
    }
}

// make preview
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