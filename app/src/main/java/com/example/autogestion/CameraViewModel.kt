package com.example.autogestion

import android.graphics.Bitmap
import android.net.Uri
import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class CameraViewModel: ViewModel() {
    private val _bitmaps = MutableStateFlow<List<Bitmap>>(emptyList())

    val bitmaps = _bitmaps.asStateFlow()

    fun onPhotoTaken(bitmap: Bitmap){
        _bitmaps.value += bitmap
    }
}

