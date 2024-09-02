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
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.autogestion.Home
import com.example.autogestion.NavBar
import com.example.autogestion.data.Client
import com.example.autogestion.data.Vehicle
import com.example.autogestion.data.viewModels.ClientViewModel
import com.example.autogestion.data.viewModels.VehicleViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.io.File
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.Locale


class VehicleForm : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        enableEdgeToEdge()
        val firstName = intent.getStringExtra("firstName") ?: ""
        val lastName = intent.getStringExtra("lastName") ?: ""
        val phoneNumber = intent.getStringExtra("phoneNumber") ?: ""
        val birthDate = intent.getStringExtra("birthDate") ?: ""
        val address = intent.getStringExtra("address") ?: ""
        val email = intent.getStringExtra("email") ?: ""

        val initRegistrationPlate = intent.getStringExtra("RegistrationPlate") ?: ""
        val initChassisNum = intent.getStringExtra("RegistrationPlate") ?: ""
        val initBrand = intent.getStringExtra("brand") ?: ""
        val initModel = intent.getStringExtra("model") ?: ""
        val initColor = intent.getStringExtra("model") ?: ""
        val initClientId = intent.getLongExtra("clientId", 0)

        setContent {
            CarFormApp(firstName, lastName, phoneNumber, birthDate, email, address, initRegistrationPlate, initChassisNum, initBrand, initModel, initColor, initClientId)
        }
    }

    @Composable
    fun CarFormApp(
        firstName: String,
        lastName: String,
        phoneNumber: String,
        birthDate: String,
        email: String,
        address: String,
        initRegistrationPlate: String,
        initChassisNum: String,
        initBrand: String,
        initModel: String,
        initColor: String,
        initClientId: Long,
        clientViewModel: ClientViewModel = viewModel(),
        vehicleViewModel: VehicleViewModel = viewModel()
    ) {
        val context = LocalContext.current

        var registrationPlate by remember { mutableStateOf(TextFieldValue(initRegistrationPlate)) }
        var chassisNum by remember { mutableStateOf(TextFieldValue(initChassisNum)) }
        var greyCard by remember { mutableStateOf<String?>(null) }
        var brand by remember { mutableStateOf(TextFieldValue(initBrand)) }
        var model by remember { mutableStateOf(TextFieldValue(initModel)) }
        var color by remember { mutableStateOf(TextFieldValue(initColor)) }
        var clientId by remember { mutableStateOf(initClientId) }

        var isRegistrationPlateError by remember { mutableStateOf(false) }

        val coroutineScope = rememberCoroutineScope()

        val greyCardLauncher = rememberLauncherForActivityResult(
            contract = ActivityResultContracts.GetContent()
        ) { uri: Uri? ->
            uri?.let {
                val path = getFilePathFromUri(context, it)
                greyCard = path
            }
        }

        Scaffold(
        ) { padding ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding)
            ) {

                NavBar(text = "Formulaire Voiture",
                    onBackClick = {
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
                    Text(if (greyCard.isNullOrEmpty()) "Télécharger la carte grise" else "Carte grise sélectionnée")
                }
                Spacer(modifier = Modifier.height(16.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
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

                                    val client = Client(
                                        clientId = 0,  // Room will auto-generate the ID
                                        lastName = lastName,
                                        firstName = firstName,
                                        phone = phoneNumber,
                                        birthDate = birthDateLong,
                                        email = email,
                                        address = address
                                    )

                                    clientViewModel.addClient(client)
                                    val newClientId = clientViewModel.getClientByPhoneNumber(phoneNumber).value?.clientId ?: 0

                                    val vehicle = Vehicle(
                                        vehicleId = 0,  // Room will auto-generate the ID
                                        registrationPlate = registrationPlate.text,
                                        chassisNum = chassisNum.text,
                                        brand = brand.text,
                                        model = model.text,
                                        color = color.text,
                                        greyCard = greyCard,
                                        clientId = newClientId
                                    )
                                    vehicleViewModel.addVehicle(vehicle)

                                    redirectToHome(context)
                                }
                            }
                        },
                        enabled = registrationPlate.text.isNotEmpty()
                    ) {
                        Text("Enregistrer le véhicule")
                    }

                    Button(
                        onClick = {
                            val intent = Intent(context, RepairForm::class.java).apply {
                                putExtra("firstName", firstName)
                                putExtra("lastName", lastName)
                                putExtra("phoneNumber", phoneNumber)
                                putExtra("birthDate", birthDate)
                                putExtra("email", email)
                                putExtra("address", address)
                                putExtra("registrationPlate", registrationPlate.text)
                                putExtra("chassisNum", chassisNum.text)
                                putExtra("greyCard", greyCard)
                                putExtra("brand", brand.text)
                                putExtra("model", model.text)
                                putExtra("color", color.text)
                                putExtra("clientId", clientId)
                            }
                            context.startActivity(intent)
                        },
                        enabled = registrationPlate.text.isNotEmpty()
                    ) {
                        Text("Suivant")
                    }
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

    @Preview(showBackground = true)
    @Composable
    fun DefaultPreview2() {
        CarFormApp("","","","","","", "","","","","",0)
    }


}
