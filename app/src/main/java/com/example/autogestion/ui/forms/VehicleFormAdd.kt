package com.example.autogestion.ui.forms

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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.autogestion.ui.profiles.ClientProfile
import com.example.autogestion.ui.Home
import com.example.autogestion.ui.components.NavBar
import com.example.autogestion.data.Vehicle
import com.example.autogestion.data.viewModels.VehicleViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import com.example.autogestion.ui.utils.getFilePathFromUri


class VehicleFormAdd : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val clientId = intent.getIntExtra("clientId", 0)

        enableEdgeToEdge()

        val initRegistrationPlate = intent.getStringExtra("RegistrationPlate") ?: ""
        val initChassisNum = intent.getStringExtra("chassisNum") ?: ""
        val initBrand = intent.getStringExtra("brand") ?: ""
        val initModel = intent.getStringExtra("model") ?: ""
        val initColor = intent.getStringExtra("color") ?: ""

        setContent {
            CarFormApp(
                initRegistrationPlate,
                initChassisNum,
                initBrand,
                initModel,
                initColor,
                clientId
            )
        }
    }


    @Composable
    fun CarFormApp(
        initRegistrationPlate: String,
        initChassisNum: String,
        initBrand: String,
        initModel: String,
        initColor: String,
        clientId: Int,
        vehicleViewModel: VehicleViewModel = viewModel()
    ) {
        val context = LocalContext.current

        var registrationPlate by remember { mutableStateOf(TextFieldValue(initRegistrationPlate)) }
        var chassisNum by remember { mutableStateOf(TextFieldValue(initChassisNum)) }
        var greyCard by remember { mutableStateOf<String?>(null) }
        var brand by remember { mutableStateOf(TextFieldValue(initBrand)) }
        var model by remember { mutableStateOf(TextFieldValue(initModel)) }
        var color by remember { mutableStateOf(TextFieldValue(initColor)) }

        var isRegistrationPlateError by remember { mutableStateOf(false) }

        val coroutineScope = rememberCoroutineScope()

        val greyCardLauncher = rememberLauncherForActivityResult(
            contract = ActivityResultContracts.GetContent()
        ) { uri: Uri? ->
            uri?.let {
                val path = getFilePathFromUri(context, it, "carteGrise")
                greyCard = path
            }
        }

        Scaffold { padding ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding)
            ) {

                NavBar(text = "Ajouter un véhicule",
                    onBackClick = {
                        val intent = Intent(context, ClientProfile::class.java).apply {
                            putExtra("clientId", clientId)
                        }
                        context.startActivity(intent)
                    }
                )

                OutlinedTextField(
                    value = registrationPlate,
                    onValueChange = { registrationPlate = it },
                    label = { Text("Numéro de plaque") },
                    modifier = Modifier.fillMaxWidth(),
                    isError = registrationPlate.text.isEmpty()
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
                    Text(if (greyCard.isNullOrEmpty()) "Télécharger la carte grise" else "Carte grise sélectionnée")
                }
                Spacer(modifier = Modifier.height(16.dp))

                Button(
                    onClick = {
                        isRegistrationPlateError = registrationPlate.text.isEmpty()
                        if (!isRegistrationPlateError) {
                            coroutineScope.launch(Dispatchers.IO) {
                                val vehicle = Vehicle(
                                    vehicleId = 0,  // Room générera automatiquement l'ID
                                    registrationPlate = registrationPlate.text,
                                    chassisNum = chassisNum.text,
                                    brand = brand.text,
                                    model = model.text,
                                    color = color.text,
                                    greyCard = greyCard,
                                    clientId = clientId
                                )

                                coroutineScope.launch {
                                    vehicleViewModel.addVehicle(vehicle)
                                    val intent = Intent(context, ClientProfile::class.java).apply {
                                        putExtra("clientId", vehicle.clientId)
                                    }
                                    context.startActivity(intent)
                                }
                            }
                        }
                    },
                    enabled = registrationPlate.text.isNotEmpty(),
                    modifier = Modifier.align(Alignment.CenterHorizontally)
                ) {
                    Text("Enregistrer le véhicule")
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


    @Preview(showBackground = true)
    @Composable
    fun DefaultPreview2() {
        CarFormApp("", "", "", "", "", 0)
    }
}
