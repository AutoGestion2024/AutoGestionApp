package com.example.autogestion.form

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.autogestion.Home
import com.example.autogestion.NavBar
import com.example.autogestion.data.Vehicle
import com.example.autogestion.data.viewModels.VehicleViewModel
import kotlinx.coroutines.launch
import java.io.File

class VehicleFormUpdate : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val vehicleId = intent.getIntExtra("vehicleId", 0)

        enableEdgeToEdge()

        setContent {
            UpdateVehicleScreen(vehicleId)
        }
    }

    @Composable
    fun UpdateVehicleScreen(vehicleId: Int, vehicleViewModel: VehicleViewModel = viewModel()) {
        LaunchedEffect(vehicleId) {
            vehicleViewModel.getVehicleById(vehicleId)
        }

        val vehicle by vehicleViewModel.getVehicleById(vehicleId).observeAsState()

        Scaffold { innerPadding ->
            vehicle?.let {
                VehicleFormUpdateApp(it, vehicleViewModel, innerPadding)
            }
        }
    }

    @Composable
    fun VehicleFormUpdateApp(
        vehicle: Vehicle,
        vehicleViewModel: VehicleViewModel,
        innerPadding: PaddingValues
    ) {
        val context = LocalContext.current

        var registrationPlate by remember { mutableStateOf(vehicle.registrationPlate) }
        var chassisNum by remember { mutableStateOf(vehicle.chassisNum ?: "") }
        var greyCard by remember { mutableStateOf(vehicle.greyCard ?: "") }
        var brand by remember { mutableStateOf(vehicle.brand ?: "") }
        var model by remember { mutableStateOf(vehicle.model ?: "") }
        var color by remember { mutableStateOf(vehicle.color ?: "") }

        var isRegistrationPlateError by remember { mutableStateOf(false) }

        val coroutineScope = rememberCoroutineScope()

        val greyCardLauncher = rememberLauncherForActivityResult(
            contract = ActivityResultContracts.GetContent()
        ) { uri: Uri? ->
            uri?.let {
                val path = getFilePathFromUri(context, it)
                greyCard = path?.let { it } ?: ""
            }
        }

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {

            NavBar(
                text = "Modifier le Véhicule",
                onBackClick = {
                    val intent = Intent(context, VehicleProfile::class.java).apply {
                        putExtra("vehicleId", vehicle.vehicleId)
                    }
                    context.startActivity(intent)
                }
            )

            OutlinedTextField(
                value = registrationPlate,
                onValueChange = { registrationPlate = it },
                label = { Text("Numéro de plaque") },
                modifier = Modifier.fillMaxWidth(),
                isError = isRegistrationPlateError
            )
            Spacer(modifier = Modifier.height(16.dp))

            OutlinedTextField(
                value = chassisNum,
                onValueChange = { chassisNum = it },
                label = { Text("Numéro de châssis") },
                modifier = Modifier.fillMaxWidth(),
            )
            Spacer(modifier = Modifier.height(16.dp))

            OutlinedTextField(
                value = brand,
                onValueChange = { brand = it },
                label = { Text("Marque") },
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(16.dp))

            OutlinedTextField(
                value = model,
                onValueChange = { model = it },
                label = { Text("Modèle") },
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(16.dp))

            OutlinedTextField(
                value = color,
                onValueChange = { color = it },
                label = { Text("Couleur") },
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(16.dp))

            Button(
                onClick = { greyCardLauncher.launch("*/*") },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(if (greyCard.isEmpty()) "Télécharger la carte grise" else "Carte grise sélectionnée")
            }
            Spacer(modifier = Modifier.height(16.dp))

            Button(
                onClick = {
                    isRegistrationPlateError = registrationPlate.isEmpty()
                    if (!isRegistrationPlateError) {
                        val updatedVehicle = Vehicle(
                            vehicleId = vehicle.vehicleId,  // Conserver l'ID existant
                            registrationPlate = registrationPlate,
                            chassisNum = chassisNum,
                            brand = brand,
                            model = model,
                            color = color,
                            greyCard = greyCard,
                            clientId = vehicle.clientId  // Conserver l'ID du client associé
                        )
                        coroutineScope.launch {
                            vehicleViewModel.updateVehicle(updatedVehicle)
                            redirectToHome(context)
                        }
                    }
                },
                enabled = registrationPlate.isNotEmpty()
            ) {
                Text("Enregistrer les modifications")
            }
        }
    }
}

private fun redirectToHome(context: android.content.Context) {
    val intent = Intent(context, Home::class.java)
    context.startActivity(intent)
    if (context is ComponentActivity) {
        context.finish()
    }
}

// Helper function to convert URI to file path
private fun getFilePathFromUri(context: android.content.Context, uri: Uri): String? {
    val cursor = context.contentResolver.query(uri, null, null, null, null)
    cursor?.use {
        val nameIndex = cursor.getColumnIndex(android.provider.OpenableColumns.DISPLAY_NAME)
        cursor.moveToFirst()
        val fileName = cursor.getString(nameIndex)
        val file = File(context.filesDir, fileName)
        context.contentResolver.openInputStream(uri)?.use { inputStream ->
            file.outputStream().use { outputStream ->
                inputStream.copyTo(outputStream)
            }
        }
        return file.absolutePath
    }
    return null
}

