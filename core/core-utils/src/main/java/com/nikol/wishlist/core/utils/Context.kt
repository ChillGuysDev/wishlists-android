package com.nikol.wishlist.core.utils

import android.content.Context
import android.net.Uri
import java.io.File
import java.io.FileInputStream
import java.io.FileOutputStream

fun Context.copyFileFromUri(context: Context, uri: Uri, destinationFile: File): Boolean {
    return try {
        context.contentResolver.openFileDescriptor(uri, "r")?.use { parcelFileDescriptor ->
            val fileDescriptor = parcelFileDescriptor.fileDescriptor
            FileInputStream(fileDescriptor).use { inputStream ->
                FileOutputStream(destinationFile).use { outputStream ->
                    inputStream.copyTo(outputStream)
                }
            }
        }
        true
    } catch (e: Exception) {
        e.printStackTrace()
        false
    }
}