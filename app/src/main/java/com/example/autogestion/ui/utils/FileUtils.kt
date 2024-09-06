package com.example.autogestion.ui.utils

import android.content.Context
import android.net.Uri
import java.io.File

/**
 * Utility function to get the file path from a URI and save it in a specified folder within the app's internal storage.
 *
 * This function copies a file from a given URI to a specific folder in the app's internal storage directory,
 * and returns the relative path of the saved file within that folder.
 *
 * @param context The context used to access the content resolver and the file system.
 * @param uri The URI pointing to the file that needs to be copied.
 * @param folderName The name of the folder within the app's internal storage where the file will be saved.
 * @return The relative path of the saved file within the specified folder, or null if the file could not be saved.
 */

fun getFilePathFromUri(context: Context, uri: Uri, folderName: String): String? {
    val cursor = context.contentResolver.query(uri, null, null, null, null)
    cursor?.use {
        val nameIndex = cursor.getColumnIndex(android.provider.OpenableColumns.DISPLAY_NAME)
        cursor.moveToFirst()
        val fileName = cursor.getString(nameIndex)

        // Create the specific folder if it does not exist
        val directory = File(context.filesDir, folderName)
        if (!directory.exists()) {
            directory.mkdirs()
        }

        // Create the file in the specified directory
        val file = File(directory, fileName)
        context.contentResolver.openInputStream(uri)?.use { inputStream ->
            file.outputStream().use { outputStream ->
                inputStream.copyTo(outputStream)
            }
        }

        // Return the relative path
        return File(folderName, fileName).toString()
    }
    return null
}
