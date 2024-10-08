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
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CalendarToday
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.autogestion.ui.components.NavBar
import com.example.autogestion.ui.profiles.VehicleProfile
import com.example.autogestion.data.Repair
import com.example.autogestion.data.viewModels.RepairViewModel
import com.example.autogestion.ui.utils.DateUtils.dateFormat
import com.example.autogestion.ui.utils.DateUtils.showDatePicker
import com.example.autogestion.ui.utils.NavigationUtils.navigateToVehicleProfile
import kotlinx.coroutines.launch
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale
import com.example.autogestion.ui.utils.getFilePathFromUri

class RepairFormUpdate : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val repairId = intent.getIntExtra("repairId", 0)

        enableEdgeToEdge()

        setContent {
            UpdateRepairScreen(repairId)
        }
    }

    @Composable
    fun UpdateRepairScreen(repairId: Int, repairViewModel: RepairViewModel = viewModel()) {
        LaunchedEffect(repairId) {
            repairViewModel.getRepairById(repairId)
        }

        val repair by repairViewModel.getRepairById(repairId).observeAsState()

        Scaffold { innerPadding ->
            repair?.let {
                RepairFormUpdateApp(it, repairViewModel, innerPadding)
            }
        }
    }

    @Composable
    fun RepairFormUpdateApp(
        repair: Repair,
        repairViewModel: RepairViewModel,
        innerPadding: PaddingValues
    ) {
        val context = LocalContext.current

        // State management for form fields, with initial values set.
        var description by remember { mutableStateOf(repair.description ?: "") }
        var date by remember { mutableStateOf(dateFormat.format(repair.date)) }
        var invoice by remember { mutableStateOf(repair.invoice ?: "") }
        var paid by remember { mutableStateOf(repair.paid ?: false) }

        // Error state management for form validation.
        var isDateError by remember { mutableStateOf(false) }

        // Instance for managing dates
        val calendar = Calendar.getInstance()

        val coroutineScope = rememberCoroutineScope()

        // Launcher for selecting a document file for invoice
        val invoiceLauncher = rememberLauncherForActivityResult(
            contract = ActivityResultContracts.GetContent()
        ) { uri: Uri? ->
            uri?.let {
                val path = getFilePathFromUri(context, it, "facture")
                invoice = path ?: ""
            }
        }

        // Form display and user input handling.
        // Each field is bound to a specific part of the repair's data.
        // Validators are set to trigger visual indicators of errors (isError).
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {

            NavBar(
                text = "Modifier la Réparation",
                onBackClick = {
                    navigateToVehicleProfile(context, repair.vehicleId)

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
                        /*val datePickerDialog = android.app.DatePickerDialog(
                            context,
                            { _, year, month, dayOfMonth ->
                                calendar.set(year, month, dayOfMonth)
                                date = dateFormat.format(calendar.time)
                                isDateError = false
                            },
                            calendar.get(Calendar.YEAR),
                            calendar.get(Calendar.MONTH),
                            calendar.get(Calendar.DAY_OF_MONTH)
                        )
                        datePickerDialog.show()*/
                        showDatePicker(context, calendar) { newDate ->
                            date = TextFieldValue(newDate).toString()
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

            Text(
                "Statut du paiement",
                style = MaterialTheme.typography.bodyLarge,
                modifier = Modifier.padding(8.dp)
            )
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
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
                Text(if (invoice.isEmpty()) "Télécharger la facture" else "Facture sélectionnée")
            }
            Spacer(modifier = Modifier.height(16.dp))

            // Button to submit repair update form
            Button(
                onClick = {
                    val updatedRepair = Repair(
                        repairId = repair.repairId, // Utilisez l'ID existant
                        description = description,
                        date = try {
                            dateFormat.parse(date)?.time ?: 0L
                        } catch (e: ParseException) {
                            0L
                        },
                        invoice = invoice,
                        paid = paid,
                        vehicleId = repair.vehicleId
                    )

                    coroutineScope.launch {
                        repairViewModel.updateRepair(updatedRepair)
                        navigateToVehicleProfile(context, repair.vehicleId)
                    }
                },
                modifier = Modifier.align(Alignment.CenterHorizontally)
            ) {
                Text("Enregistrer les modifications")
            }
        }
    }

}
