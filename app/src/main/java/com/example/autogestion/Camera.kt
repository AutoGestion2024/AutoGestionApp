//@file:OptIn(ExperimentalMaterial3Api::class)
//
//package com.example.autogestion
//
//import android.Manifest
//import android.content.pm.PackageManager
//import android.os.Bundle
//import androidx.activity.ComponentActivity
//import androidx.activity.compose.setContent
//import androidx.activity.enableEdgeToEdge
//import androidx.camera.view.CameraController
//import androidx.camera.view.LifecycleCameraController
//import androidx.camera.view.PreviewView
//import androidx.compose.foundation.layout.Box
//import androidx.compose.foundation.layout.fillMaxSize
//import androidx.compose.foundation.layout.padding
//import androidx.compose.material3.BottomSheetScaffold
//import androidx.compose.material3.ExperimentalMaterial3Api
//import androidx.compose.material3.rememberBottomSheetScaffoldState
//import androidx.compose.runtime.Composable
//import androidx.compose.runtime.remember
//import androidx.core.app.ActivityCompat
//import androidx.core.content.ContextCompat
//import androidx.compose.ui.Modifier
//import androidx.compose.ui.platform.LocalLifecycleOwner
//import androidx.compose.ui.unit.dp
//import androidx.compose.ui.viewinterop.AndroidView
//
//
//class Camera : ComponentActivity(){
//
//    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//        if (!hasRequiredPermissions()) {
//            ActivityCompat.requestPermissions(this, Camera.CAMERAX_PERMISSIONS, 0)
//        }
//        enableEdgeToEdge()
//        setContent {
//            val scaffoldState = rememberBottomSheetScaffoldState()
//            val controller = remember {
//                LifecycleCameraController(applicationContext).apply {
//                setEnabledUseCases(CameraController.IMAGE_CAPTURE)
//                }
//            }
//            BottomSheetScaffold(
//                scaffoldState = scaffoldState,
//                sheetPeekHeight = 0.dp,
//                sheetContent = {
//
//                }
//            ){padding ->
//                Box(
//                    modifier = Modifier
//                        .fillMaxSize()
//                        .padding(padding)
//                ){
//                    CameraPreview(controller = controller,
//                        modifier = Modifier
//                            .fillMaxSize()
//                    )
//                }
//
//            }
//        }
//
//    }
//
//    private fun hasRequiredPermissions(): Boolean{
//        return CAMERAX_PERMISSIONS.all {
//            ContextCompat.checkSelfPermission(applicationContext, it) == PackageManager.PERMISSION_GRANTED
//        }
//    }
//
//    companion object{
//        private val CAMERAX_PERMISSIONS = arrayOf(
//            Manifest.permission.CAMERA,
//            Manifest.permission.INTERNET
//        )
//    }
//}
//
//@Composable
//fun CameraPreview(
//    controller: LifecycleCameraController,
//    modifier: Modifier = Modifier
//){
//    val lifecycleOwner = LocalLifecycleOwner.current
//    AndroidView(factory = {
//        PreviewView(it).apply {
//            this.controller = controller
//            controller.bindToLifecycle(lifecycleOwner)
//        }
//    },
//        modifier = modifier
//    )
//}
@file:OptIn(ExperimentalMaterial3Api::class)

package com.example.autogestion

import RetrofitCallClass
import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.camera.core.ImageCapture
import androidx.camera.core.ImageCaptureException
import androidx.camera.core.ImageProxy
import androidx.camera.view.CameraController
import androidx.camera.view.LifecycleCameraController
import androidx.camera.view.PreviewView
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.staggeredgrid.LazyVerticalStaggeredGrid
import androidx.compose.foundation.lazy.staggeredgrid.StaggeredGridCells
import androidx.compose.foundation.lazy.staggeredgrid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Photo
import androidx.compose.material.icons.filled.PhotoCamera
import androidx.compose.material3.BottomSheetScaffold
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.rememberBottomSheetScaffoldState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.lifecycle.viewmodel.compose.viewModel
import kotlinx.coroutines.launch
import java.io.File


class Camera : ComponentActivity(){

    private lateinit var imageCapture: ImageCapture

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (!hasRequiredPermissions()) {
            ActivityCompat.requestPermissions(this, Camera.CAMERAX_PERMISSIONS, 0)
        }
        enableEdgeToEdge()
        setContent {
            val scope = rememberCoroutineScope()
            val scaffoldState = rememberBottomSheetScaffoldState()
            val controller = remember {
                LifecycleCameraController(applicationContext).apply {
                    setEnabledUseCases(CameraController.IMAGE_CAPTURE)
                }
            }

            val viewModel = viewModel<CameraViewModel>()
            val bitmaps by viewModel.bitmaps.collectAsState()

            BottomSheetScaffold(
                scaffoldState = scaffoldState,
                sheetPeekHeight = 0.dp,
                sheetContent = {
                    PhotoBottomSheetContent(
                        bitmaps = bitmaps,
                        modifier = Modifier
                            .fillMaxWidth()
                    )
                }
            ){padding ->
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(padding)
                ){
                    CameraPreview(
                        controller = controller,
                        modifier = Modifier
                            .fillMaxSize()
                    )
                    Box (
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(16.dp),
                        contentAlignment = Alignment.BottomCenter
                    ){
                        IconButton(
                            onClick = {

//                                takePhoto(controller) { uri ->
//                                    RetrofitCallClass(scope).uploadImageToServerAndGetResults(
//                                        this@Camera,
//                                        uri
//                                    ) { vrn ->
//                                        vrn?.let {
//                                            // Code pour mettre le VRN dans la barre de recherche
//                                            println("plaques voiture: $vrn")
////                                            searchViewModel.updateSearchQuery(it) // Assurez-vous d'avoir un ViewModel pour gérer l'état de la barre de recherche
//                                        }
//                                        // Redirigez vers la page Home
//                                        val intent = Intent(this@Camera, Home::class.java)
//                                        startActivity(intent)
//                                        finish() // termine l'activité actuelle pour ne pas revenir en arrière
//                                    }
//                                }
                                takePhoto(controller) { uri ->
                                    RetrofitCallClass(scope).uploadImageToServerAndGetResults(this@Camera, uri) { vrn ->
                                        vrn?.let {
                                            val intent = Intent(this@Camera, Home::class.java)
                                            intent.putExtra("search_text", it)
                                            startActivity(intent)
                                            finish() // termine l'activité actuelle pour ne pas revenir en arrière
                                        }
                                    }
                                }


                            },
                            modifier = Modifier
                                .size(72.dp)
                                .clip(RoundedCornerShape(36.dp))
                                .background(Color.White.copy(alpha = 0.6f))
                        ) {
                            Icon(
                                imageVector = Icons.Default.PhotoCamera,
                                contentDescription = "Take photo",
                                tint = Color.Black, // Changez la couleur si nécessaire
                                modifier = Modifier.size(48.dp) // Ajuster la taille
                            )

                        }
                    }

                }

            }
        }

    }

    private fun takePhoto(controller: LifecycleCameraController, onPhotoSaved: (Uri) -> Unit) {
        // Crée un fichier temporaire pour sauvegarder l'image
        val tempFile = File.createTempFile("temp_photo_", ".jpg", cacheDir)
        val outputOptions = ImageCapture.OutputFileOptions.Builder(tempFile).build()

        controller.takePicture(
            outputOptions,
            ContextCompat.getMainExecutor(applicationContext),
            object : ImageCapture.OnImageSavedCallback {
                override fun onImageSaved(output: ImageCapture.OutputFileResults) {
                    // Obtenez l'URI du fichier temporaire
                    val savedUri: Uri = Uri.fromFile(tempFile)
                    onPhotoSaved(savedUri)

                    // Redirigez vers la page Home
                    val intent = Intent(this@Camera, Home::class.java)
                    startActivity(intent)
                    finish() // termine l'activité actuelle pour ne pas revenir en arrière
                }

                override fun onError(exception: ImageCaptureException) {
                    Log.e("Camera", "Error taking photo", exception)
                }
            }
        )
    }



    private fun hasRequiredPermissions(): Boolean{
        return CAMERAX_PERMISSIONS.all {
            ContextCompat.checkSelfPermission(applicationContext, it) == PackageManager.PERMISSION_GRANTED
        }
    }

    companion object{
        private val CAMERAX_PERMISSIONS = arrayOf(
            Manifest.permission.CAMERA,
            Manifest.permission.INTERNET
        )
    }
}

@Composable
fun PhotoBottomSheetContent(
    bitmaps: List<Bitmap>,
    modifier: Modifier = Modifier

){
    if (bitmaps.isEmpty()){
        Box(modifier = modifier
            .padding(16.dp),
            contentAlignment = Alignment.Center
        ){
            Text(text = "There are no photos")
        }
    }else{
        LazyVerticalStaggeredGrid(
            columns = StaggeredGridCells.Fixed(2),
            horizontalArrangement = Arrangement.spacedBy(16.dp),
            verticalItemSpacing = 16.dp,
            contentPadding = PaddingValues(16.dp),
            modifier = modifier
        ) {
            items(bitmaps){
                    bitmap ->
                Image(
                    bitmap = bitmap.asImageBitmap(),
                    contentDescription = null,
                    modifier = Modifier
                        .clip(RoundedCornerShape(10.dp))
                )
            }
        }
    }

}

@Composable
fun CameraPreview(
    controller: LifecycleCameraController,
    modifier: Modifier = Modifier
){
    val lifecycleOwner = LocalLifecycleOwner.current
    AndroidView(factory = {
        PreviewView(it).apply {
            this.controller = controller
            controller.bindToLifecycle(lifecycleOwner)
        }
    },
        modifier = modifier
    )
}
private fun imageProxyToBitmap(image: ImageProxy): Bitmap {
    val buffer = image.planes[0].buffer
    val bytes = ByteArray(buffer.remaining())
    buffer.get(bytes)

    return BitmapFactory.decodeByteArray(bytes, 0, bytes.size)
}