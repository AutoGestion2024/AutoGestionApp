package com.example.autogestion.ui

import android.graphics.Bitmap
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

/**
 * CameraViewModel is a class that handles the logic related to capturing images in an app.
 * It inherits from ViewModel, which allows it to survive configuration changes (like screen rotations).
 *
 * This class contains a state flow (_bitmaps) that stores a list of Bitmaps and provides a method
 * to add a new photo to this list when it is captured.
 */
class CameraViewModel: ViewModel() {

    // _bitmaps is a mutable state flow that contains the list of images (of type Bitmap).
    // It is initialized with an empty list.
    private val _bitmaps = MutableStateFlow<List<Bitmap>>(emptyList())

    // Exposes _bitmaps as an immutable StateFlow.
    // This allows observers to access the list of images without being able to modify it directly.
    val bitmaps = _bitmaps.asStateFlow()

    /**
     * This function is called when a photo is taken.
     * It adds the newly captured image (bitmap) to the existing list in the _bitmaps flow.
     *
     * @param bitmap The image that was just captured as a Bitmap.
     */
    fun onPhotoTaken(bitmap: Bitmap){
        _bitmaps.value += bitmap
    }
}

