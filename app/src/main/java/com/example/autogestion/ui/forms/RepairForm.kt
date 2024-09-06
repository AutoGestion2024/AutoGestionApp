package com.example.autogestion.ui.forms

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CalendarToday
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.autogestion.ui.Home
import com.example.autogestion.ui.components.SharedComposables.NavBar
import com.example.autogestion.data.Client
import com.example.autogestion.data.Repair
import com.example.autogestion.data.Vehicle
import com.example.autogestion.data.viewModels.ClientViewModel
import com.example.autogestion.data.viewModels.RepairViewModel
import com.example.autogestion.data.viewModels.VehicleViewModel
import com.example.autogestion.ui.utils.DateUtils.dateFormat
import com.example.autogestion.ui.utils.DateUtils.showDatePicker
import com.example.autogestion.ui.utils.NavigationUtils.navigateToHome
import com.example.autogestion.ui.utils.NavigationUtils.navigateToVehicleForm
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale
import com.example.autogestion.ui.utils.getFilePathFromUri

class RepairForm : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        enableEdgeToEdge()

        // Extracting data from Intent that started this activity
        val firstName = intent.getStringExtra("firstName") ?: ""
        val lastName = intent.getStringExtra("lastName") ?: ""
        val phoneNumber = intent.getStringExtra("phoneNumber") ?: ""
        val birthDate = intent.getStringExtra("birthDate") ?: ""
        val email = intent.getStringExtra("email") ?: ""
        val address = intent.getStringExtra("address") ?: ""

        val registrationPlate = intent.getStringExtra("registrationPlate") ?: ""
        val chassisNum = intent.getStringExtra("chassisNum") ?: ""
        val brand = intent.getStringExtra("brand") ?: ""
        val model = intent.getStringExtra("model") ?: ""
        val color = intent.getStringExtra("color") ?: ""
        val greyCard = intent.getStringExtra("greyCard") ?: ""
        val clientId = intent.getIntExtra("clientId", 0)

        setContent {
            RepairFormApp(firstName, lastName, phoneNumber, birthDate, email, address, registrationPlate, chassisNum, brand, model, color, greyCard, clientId)
        }
    }

    @Composable
    fun RepairFormApp(firstName: String,
                      lastName: String,
                      phoneNumber: String,
                      birthDate: String,
                      email: String,
                      address: String,
                      registrationPlate: String,
                      chassisNum: String,
                      brand: String,
                      model: String,
                      color: String,
                      greyCard: String,
                      clientId: Int,
                      clientViewModel: ClientViewModel = viewModel(),
                      vehicleViewModel: VehicleViewModel = viewModel(),
                      repairViewModel: RepairViewModel = viewModel()
    ) {
        val context = LocalContext.current

        // State management for input fields with initial values if provided
        var description by remember { mutableStateOf(TextFieldValue("")) }
        var date by remember { mutableStateOf(TextFieldValue("")) }
        var invoice by remember { mutableStateOf<String?>(null) }
        val calendar = Calendar.getInstance()
        var paid by remember { mutableStateOf(false) }

        var isDateError by remember { mutableStateOf(false) }

        val coroutineScope = rememberCoroutineScope()

        // Launcher for selecting a document file for invoice
        val invoiceLauncher = rememberLauncherForActivityResult(
            contract = ActivityResultContracts.GetContent()
        ) { uri: Uri? ->
            uri?.let {
                val path = getFilePathFromUri(context, it, "facture")
                invoice = path
            }
        }

        // Form display and user input handling.
        // Each field is bound to a specific part of the repair's data.
        // Validators are set to trigger visual indicators of errors (isError).
        Scaffold { padding ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding)
            ) {

                NavBar(
                    text = "Formulaire Réparation",
                    onBackClick = {
                        navigateToVehicleForm(context, firstName, lastName, phoneNumber, birthDate, email, address, registrationPlate, chassisNum, greyCard, brand, model, color, clientId)
                    }
                )

                OutlinedTextField(
                    value = description,
                    onValueChange = { description = it },
                    label = { Text("Description de la réparation") },
                    modifier = Modifier.fillMaxWidth(),
                )
                Spacer(modifier = Modifier.height(16.dp))

                OutlinedTextField(
                    value = date,
                    onValueChange = { date = it },
                    label = { Text("Date de la réparation (optionnel)") },
                    keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Number),
                    modifier = Modifier.fillMaxWidth(),
                    readOnly = true,
                    isError = isDateError,
                    trailingIcon = {
                        IconButton(onClick = {
                            showDatePicker(context, calendar) { newDate ->
                                date = TextFieldValue(newDate)
                                isDateError = false
                            }
                        }) {
                            Icon(Icons.Default.CalendarToday, contentDescription = "Select Date")
                        }
                    }
                )
                if (isDateError) {
                    Text("Format de date invalide", color = MaterialTheme.colorScheme.error)
                }
                Spacer(modifier = Modifier.height(16.dp))

                Text("Statut du paiement", style = MaterialTheme.typography.bodyLarge, modifier = Modifier.padding(8.dp))
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.fillMaxWidth().padding(16.dp)
                ) {
                    RadioButton(
                        selected = paid,
                        onClick = { paid = true }
                    )
                    Text("Payé", modifier = Modifier.padding(start = 8.dp))

                    Spacer(modifier = Modifier.width(16.dp))

                    RadioButton(
                        selected = !paid,
                        onClick = { paid = false }
                    )
                    Text("Non payé", modifier = Modifier.padding(start = 8.dp))
                }

                Spacer(modifier = Modifier.height(16.dp))

                Button(
                    onClick = { invoiceLauncher.launch("*/*") },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(if (invoice.isNullOrEmpty()) "Télécharger la facture" else "Facture sélectionnée")
                }
                Spacer(modifier = Modifier.height(16.dp))


                // Button to submit the form, create client, vehicle and repair in the database
                Button(
                    onClick = {
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

                            Log.e("RepairForm", "New client ID: $newClientId")

                            if (newClientId != null && newClientId > 0) {
                                val newVehicle = Vehicle(
                                    vehicleId = 0,  // Room générera automatiquement l'ID
                                    registrationPlate = registrationPlate,
                                    chassisNum = chassisNum,
                                    brand = brand,
                                    model = model,
                                    color = color,
                                    greyCard = greyCard,
                                    clientId = newClientId
                                )

                                val newVehicleId = vehicleViewModel.addVehicleAndRetrieveId(newVehicle)

                                Log.e("RepairForm", "New vehicle ID: $newVehicleId")

                                if (newVehicleId != null && newVehicleId > 0) {
                                    val newRepair = Repair(
                                        repairId = 0,  // Room générera automatiquement l'ID
                                        description = description.text,
                                        date = if (date.text.isNotEmpty()) {
                                            try {
                                                dateFormat.parse(date.text)?.time ?: 0L
                                            } catch (e: ParseException) {
                                                0L
                                            }
                                        } else {
                                            0L  // ou une autre valeur par défaut
                                        },
                                        invoice = invoice,
                                        paid = paid,
                                        vehicleId = newVehicleId
                                    )

                                    repairViewModel.addRepair(newRepair)
                                    navigateToHome(context)
                                } else {
                                    Log.e("RepairForm", "Failed to retrieve vehicleId")
                                }
                            } else {
                                Log.e("RepairForm", "Failed to retrieve clientId")
                            }
                        }
                    },
                    modifier = Modifier.align(Alignment.CenterHorizontally),
                    //enabled = description.text.isNotEmpty()
                ) {
                    Text("Enregistrer")
                }
            }
        }
    }

    @Preview(showBackground = true)
    @Composable
    fun DefaultPreview2() {
        RepairFormApp("","","","","","","","","","","","",0)
    }
}
