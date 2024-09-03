package com.example.autogestion.webService

import android.content.Context
import android.net.Uri
import android.util.Log
import java.io.File
import java.io.FileOutputStream
import java.util.*

/**
 * Retrieves a File object from a given Uri.
 *
 * This function takes a Uri representing a file or media content and converts it into a File object.
 * It first generates a unique file name using UUID and creates a File object in the application's files directory.
 * Then, it tries to open an input stream from the Uri and an output stream to the generated file.
 * The input stream's data is copied to the output stream, effectively saving the file.
 *
 * @param uri The Uri representing the file or media content.
 * @return A File object representing the saved file.
 * @throws NullPointerException If the input stream from the Uri is null.
 * @throws IOException If there's an error during file operations.
 */
fun Context.getFileFromUri(uri: Uri): File {

    val file = File(this.filesDir, UUID.randomUUID().toString() + ".jpg")
    try {
        val inputStream =
            this.contentResolver.openInputStream(uri)
                ?: throw NullPointerException("file was null")
        val outputStream = FileOutputStream(file)
        inputStream.use { i ->
            outputStream.use { o ->
                i.copyTo(o, 1024)
            }
        }
    } catch (e: Exception) {
        Log.e(">>>>>>>>", e.message.toString())
    }
    return file
}