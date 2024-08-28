package com.example.autogestion

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CameraAlt
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.example.autogestion.data.Car
import com.example.autogestion.data.Client
import com.example.autogestion.data.Repair
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors

class Home : ComponentActivity() {

//    private lateinit var cameraExecutor: ExecutorService

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (!hasRequiredPermissions()) {
            ActivityCompat.requestPermissions(this, CAMERAX_PERMISSIONS, 0)
        }
        enableEdgeToEdge()
//        cameraExecutor = Executors.newSingleThreadExecutor()
        setContent {
            MyApp1()
        }


//        startCamera()
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
//    private fun startCamera() {
//        val cameraProviderFuture = ProcessCameraProvider.getInstance(this)
//        cameraProviderFuture.addListener({
//            val cameraProvider = cameraProviderFuture.get()
//
//            val preview = Preview.Builder().build().also {
//                it.setSurfaceProvider(previewView.surfaceProvider)
//            }
//
//            imageCapture = ImageCapture.Builder().build()
//
//            val cameraSelector = CameraSelector.DEFAULT_BACK_CAMERA
//
//            try {
//                cameraProvider.unbindAll()
//                cameraProvider.bindToLifecycle(
//                    this, cameraSelector, preview, imageCapture
//                )
//            } catch (exc: Exception) {
//                Log.e("CameraX", "Use case binding failed", exc)
//            }
//        }, ContextCompat.getMainExecutor(this))
//    }


@Composable
fun MyApp1() {
    val context = LocalContext.current
    val repair1 = Repair("Engine problem", "16.08.2024")
    val repair2 = Repair("Transmission issue", "13.04.2024")
    val repair3 = Repair("Tyre replacement", "10.01.2024")
    val repair4 = Repair("Oil change", "10.01.2024")
    val repair5 = Repair("Service", "10.01.2024")

    val car1 = Car("ABC123", "Toyota", "Camry",1, listOf(repair1))
    val car2 = Car("XYZ789", "Honda", "Civic", 1,listOf(repair2, repair3))
    val car3 = Car("DEF789", "Mercedes", "A35 AMG", 2,listOf(repair4))
    val car4 = Car("DEF789", "Volkswagen", "Cocinelle", 3,listOf(repair5))

    val client1 = Client(1,"John", "Doe", listOf(car1, car2))
    val client2 = Client(2,"Lewis", "Hamilton", listOf(car3))
    val client3 = Client(3,"Paul", "Albertini", listOf(car4))


    var searchText by remember { mutableStateOf(TextFieldValue("")) }
    var items by remember { mutableStateOf(listOf(client1, client2, client3)) }

    Scaffold(
        floatingActionButton = {
            FloatingActionButton(
                onClick = {
                    // TODO: Redirect to the HomeForm page when it's ready
                },
                modifier = Modifier
                    .padding(16.dp)
            ) {
                Text("+")
            }
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
        ) {
            Row (
                horizontalArrangement = Arrangement.Start, // Align items horizontally
                verticalAlignment = Alignment.CenterVertically, // Center vertically
//                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
            ){

                Icon(
                    imageVector = Icons.Default.Search,
                    contentDescription = null,
                    modifier = Modifier
                        .padding(end = 8.dp)
                )

                // Barre de recherche
                BasicTextField(
                    value = searchText,
                    onValueChange = { searchText = it },
                    modifier = Modifier
                        .background(color = androidx.compose.ui.graphics.Color.LightGray)
                        .weight(1f)
                        .padding(end = 8.dp),
                    textStyle = LocalTextStyle.current.copy(fontSize = 18.sp)
                )
                IconButton(
                    onClick = {
                    println("Icon button clicked")
                        val intent = Intent(context, Camera::class.java)
                        context.startActivity(intent)
//                        val outputDirectory = getOutputDirectory(context)
//                        val photoFile = File(
//                            outputDirectory,
//                            "${System.currentTimeMillis()}.jpg"
//                        )
//                        val outputOptions = ImageCapture.OutputFileOptions.Builder(photoFile).build()
//                        takePhoto(context, outputOptions, Uri.fromFile(photoFile) )
                    }
                ) {
                    Icon(
                        imageVector = Icons.Default.CameraAlt,
                        contentDescription = null,
                        modifier = Modifier
                            .padding(end = 8.dp)
                    )
                }

            }

            // Liste filtrée
            val filteredItems = items.filter { client ->
                client.firstName.contains(searchText.text, ignoreCase = true) ||
                        client.lastName.contains(searchText.text, ignoreCase = true) ||
                        client.cars.any { car ->
                            car.plateNumber.contains(searchText.text, ignoreCase = true)
                        }
            }

            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp)
            ) {
                items(filteredItems.size) { index ->
                    val client = filteredItems[index]
                    val carsDescription = client.cars.joinToString(separator = "\n ") { "${it.make}, ${it.model}" }
                    Text(
                        text = "${client.firstName} ${client.lastName}\n $carsDescription",
                        fontSize = 20.sp,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 8.dp)
                            .clickable {
                                println("${client.firstName} ${client.lastName}")
                                // TODO: Navigate to the client's profile page when it's ready
                            }
                    )
                }
            }
        }
    }
}
//private fun takePhoto(context: Context, outputOptions: ImageCapture.OutputFileOptions, savedUri: Uri) {
//    val imageCapture = ImageCapture.Builder().build()
//    imageCapture.takePicture(
//        outputOptions,
//        ContextCompat.getMainExecutor(context),
//        object : ImageCapture.OnImageSavedCallback {
//            override fun onError(exception: ImageCaptureException) {
//                Toast.makeText(
//                    context,
//                    "Error capturing photo: ${exception.message}",
//                    Toast.LENGTH_SHORT
//                ).show()
//            }
//
//            override fun onImageSaved(output: ImageCapture.OutputFileResults) {
//                recognizePlate(savedUri)
//            }
//        }
//    )
//}
//
//
//private fun recognizePlate(uri: Uri?) {
//    // Implémentez la reconnaissance de plaque ici en utilisant PlateRecognizer API
//    Toast.makeText(this, "Plate recognized from image at $uri", Toast.LENGTH_SHORT).show()
//    // Une fois la plaque reconnue, mettez à jour la barre de recherche avec le résultat
//}
//
//private fun getOutputDirectory(context: Context): File {
//    val mediaDir = context.getExternalFilesDirs(null).firstOrNull()?.let {
//        File(it, context.getString(R.string.app_name)).apply { mkdirs() }
//    }
//    return if (mediaDir != null && mediaDir.exists()) mediaDir else context.filesDir
//}
//
//
//override fun onDestroy() {
//    super.onDestroy()
//    cameraExecutor.shutdown()
//}
    @Preview(showBackground = true)
    @Composable
    fun DefaultPreview1() {
        MyApp1()
    }
}



