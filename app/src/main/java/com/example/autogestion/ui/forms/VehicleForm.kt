package com.example.autogestion.ui.forms

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.autogestion.ui.Home
import com.example.autogestion.ui.components.SharedComposables.NavBar
import com.example.autogestion.data.Client
import com.example.autogestion.data.Vehicle
import com.example.autogestion.data.viewModels.ClientViewModel
import com.example.autogestion.data.viewModels.VehicleViewModel
import com.example.autogestion.ui.utils.NavigationUtils.navigateToClientForm
import com.example.autogestion.ui.utils.NavigationUtils.navigateToHome
import com.example.autogestion.ui.utils.NavigationUtils.navigateToRepairForm
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.Locale
import com.example.autogestion.ui.utils.getFilePathFromUri


class VehicleForm : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        enableEdgeToEdge()
        
        // Extracting data from Intent that started this activity
        val firstName = intent.getStringExtra("firstName") ?: ""
        val lastName = intent.getStringExtra("lastName") ?: ""
        val phoneNumber = intent.getStringExtra("phoneNumber") ?: ""
        val birthDate = intent.getStringExtra("birthDate") ?: ""
        val address = intent.getStringExtra("address") ?: ""
        val email = intent.getStringExtra("email") ?: ""

        val initRegistrationPlate = intent.getStringExtra("registrationPlate") ?: ""
        val initGreyCard = intent.getStringExtra("greyCard") ?: ""
        val initChassisNum = intent.getStringExtra("chassisNum") ?: ""
        val initBrand = intent.getStringExtra("brand") ?: ""
        val initModel = intent.getStringExtra("model") ?: ""
        val initColor = intent.getStringExtra("color") ?: ""
        val initClientId = intent.getIntExtra("clientId", 0)

        setContent {
            VehicleFormApp(firstName, lastName, phoneNumber, birthDate, email, address, initRegistrationPlate, initChassisNum, initGreyCard, initBrand, initModel, initColor, initClientId)
        }
    }

    @Composable
    fun VehicleFormApp(
        firstName: String,
        lastName: String,
        phoneNumber: String,
        birthDate: String,
        email: String,
        address: String,
        initRegistrationPlate: String,
        initChassisNum: String,
        initGreyCard: String,
        initBrand: String,
        initModel: String,
        initColor: String,
        initClientId: Int,
        clientViewModel: ClientViewModel = viewModel(),
        vehicleViewModel: VehicleViewModel = viewModel()
    ) {
        val context = LocalContext.current

        // State management for input fields with initial values if provided
        var registrationPlate by remember { mutableStateOf(TextFieldValue(initRegistrationPlate)) }
        var chassisNum by remember { mutableStateOf(TextFieldValue(initChassisNum)) }
        var greyCard by remember { mutableStateOf(initGreyCard)}
        var brand by remember { mutableStateOf(TextFieldValue(initBrand)) }
        var model by remember { mutableStateOf(TextFieldValue(initModel)) }
        var color by remember { mutableStateOf(TextFieldValue(initColor)) }

        var isRegistrationPlateError by remember { mutableStateOf(false) }

        val coroutineScope = rememberCoroutineScope()

        // Launcher for selecting a document file for grey card
        val greyCardLauncher = rememberLauncherForActivityResult(
            contract = ActivityResultContracts.GetContent()
        ) { uri: Uri? ->
            uri?.let {
                val path = getFilePathFromUri(context, it, "carteGrise")
                greyCard = path.toString()
            }
        }

        // Form display and user input handling.
        // Each field is bound to a specific part of the vehicle's data.
        // Validators are set to trigger visual indicators of errors (isError).
        Scaffold(
        ) { padding ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding)
            ) {

                NavBar(text = "Formulaire Voiture",
                    onBackClick = {
                        navigateToClientForm(context, firstName, lastName, phoneNumber, birthDate, email, address)
                        val intent = Intent(context, ClientForm::class.java).apply {
                            putExtra("firstName", firstName)
                            putExtra("lastName", lastName)
                            putExtra("phoneNumber", phoneNumber)
                            putExtra("birthDate", birthDate)
                            putExtra("email", email)
                            putExtra("address", address)
                        }
                        context.startActivity(intent)
                    })

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
                    Text(if (greyCard.isEmpty()) "Télécharger la carte grise" else "Carte grise sélectionnée")
                }
                Spacer(modifier = Modifier.height(16.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {

                    // Button to submit form, create client and vehicle.
                    Button(
                        onClick = {
                            isRegistrationPlateError = registrationPlate.text.isEmpty()
                            if (!isRegistrationPlateError) {
                                coroutineScope.launch(Dispatchers.IO) {
                                    val birthDateLong = if (birthDate.isNotEmpty()) {
                                        try {
                                            SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).parse(birthDate)?.time ?: 0L
                                        } catch (e: ParseException) {
                                            0L
                                        }
                                    } else {
                                        0L
                                    }

                                    val newClient = Client(
                                        clientId = 0,  // Room générera automatiquement l'ID
                                        lastName = lastName,
                                        firstName = firstName,
                                        phone = phoneNumber,
                                        birthDate = birthDateLong,
                                        email = email,
                                        address = address
                                    )

                                    val newClientId = clientViewModel.addClientAndRetrieveId(newClient)

                                    Log.e("VehicleForm", "New client: $newClient.clientId")

                                    if (newClientId != null) {
                                        if (newClientId > 0) {

                                            val vehicle = Vehicle(
                                                vehicleId = 0,  // Room générera automatiquement l'ID
                                                registrationPlate = registrationPlate.text,
                                                chassisNum = chassisNum.text,
                                                brand = brand.text,
                                                model = model.text,
                                                color = color.text,
                                                greyCard = greyCard,
                                                clientId = newClientId
                                            )
                                            vehicleViewModel.addVehicle(vehicle)
                                            navigateToHome(context)
                                        } else {
                                            Log.e("VehicleForm", "Failed to retrieve clientId")
                                        }
                                    }
                                }
                            }
                        },
                        enabled = registrationPlate.text.isNotEmpty()
                    ) {
                        Text("Enregistrer le véhicule")
                    }

                    // Button for saving data and navigate to next form
                    Button(
                        onClick = {
                            isRegistrationPlateError = registrationPlate.text.isEmpty()
                            if (!isRegistrationPlateError) {
                                navigateToRepairForm(context, firstName, lastName, phoneNumber, birthDate, address, email, registrationPlate.text, greyCard, chassisNum.text, brand.text, model.text, color.text)
                            }
                        },
                        enabled = registrationPlate.text.isNotEmpty()
                    ) {
                        Text("Suivant")
                    }
                }
            }
        }
    }

    @Preview(showBackground = true)
    @Composable
    fun DefaultPreview2() {
        VehicleFormApp("","","","","","", "","","","","","",0)
    }
}
