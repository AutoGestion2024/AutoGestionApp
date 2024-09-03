package com.example.autogestion.form

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
import com.example.autogestion.ClientProfile
import com.example.autogestion.Home
import com.example.autogestion.NavBar
import com.example.autogestion.VehicleProfile
import com.example.autogestion.data.Repair
import com.example.autogestion.data.viewModels.RepairViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.io.File
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

class RepairFormAdd : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val vehicleId = intent.getIntExtra("vehicleId", 0)

        enableEdgeToEdge()

        setContent {
            RepairFormApp("", "", "", false, vehicleId)
        }
    }

    @Composable
    fun RepairFormApp(
        initDescription: String,
        initDate: String,
        initInvoice: String,
        initPaid: Boolean,
        vehicleId: Int,
        repairViewModel: RepairViewModel = viewModel()
    ) {
        val context = LocalContext.current

        var description by remember { mutableStateOf(TextFieldValue(initDescription)) }
        var date by remember { mutableStateOf(TextFieldValue(initDate)) }
        var invoice by remember { mutableStateOf<String?>(initInvoice) }
        val calendar = Calendar.getInstance()
        var paid by remember { mutableStateOf(initPaid) }

        val dateFormat = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
        var isDateError by remember { mutableStateOf(false) }

        val coroutineScope = rememberCoroutineScope()

        val invoiceLauncher = rememberLauncherForActivityResult(
            contract = ActivityResultContracts.GetContent()
        ) { uri: Uri? ->
            uri?.let {
                val path = getFilePathFromUri(context, it)
                invoice = path
            }
        }

        Scaffold { padding ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding)
            ) {

                NavBar(text = "Ajouter une réparation",
                    onBackClick = { context.startActivity(Intent(context, Home::class.java)) })

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
                            val datePickerDialog = android.app.DatePickerDialog(
                                context,
                                { _, year, month, dayOfMonth ->
                                    calendar.set(year, month, dayOfMonth)
                                    date = TextFieldValue(dateFormat.format(calendar.time))
                                    isDateError = false
                                },
                                calendar.get(Calendar.YEAR),
                                calendar.get(Calendar.MONTH),
                                calendar.get(Calendar.DAY_OF_MONTH)
                            )
                            datePickerDialog.show()
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
                    Text(if (invoice.isNullOrEmpty()) "Télécharger la facture" else "Facture sélectionnée")
                }
                Spacer(modifier = Modifier.height(16.dp))

                Button(
                    onClick = {
                        coroutineScope.launch(Dispatchers.IO) {
                            val repair = Repair(
                                repairId = 0,  // Room générera automatiquement l'ID
                                description = description.text,
                                date = if (date.text.isNotEmpty()) {
                                    try {
                                        dateFormat.parse(date.text)?.time ?: 0L
                                    } catch (e: ParseException) {
                                        0L
                                    }
                                } else {
                                    0L
                                },
                                invoice = invoice,
                                paid = paid,
                                vehicleId = vehicleId
                            )

                            coroutineScope.launch {
                                repairViewModel.addRepair(repair)
                                val intent = Intent(context, VehicleProfile::class.java).apply {
                                    putExtra("vehicleId", repair.vehicleId)
                                }
                                context.startActivity(intent)
                            }
                        }
                    },
                    modifier = Modifier.align(Alignment.CenterHorizontally),
                ) {
                    Text("Enregistrer la réparation")
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
        RepairFormApp("", "", "", false, 0)
    }
}