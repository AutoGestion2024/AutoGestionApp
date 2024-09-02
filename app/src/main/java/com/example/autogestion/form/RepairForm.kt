package com.example.autogestion.form

import android.content.Intent
import android.net.Uri
import android.os.Bundle
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
import com.example.autogestion.Home
import com.example.autogestion.NavBar
import java.io.File
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

class RepairForm : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        enableEdgeToEdge()
        val firstName = intent.getStringExtra("firstName") ?: ""
        val lastName = intent.getStringExtra("lastName") ?: ""
        val phoneNumber = intent.getStringExtra("phoneNumber") ?: ""
        val birthDate = intent.getStringExtra("birthDate") ?: ""
        val email = intent.getStringExtra("email") ?: ""
        val address = intent.getStringExtra("address") ?: ""

        val registrationPlate = intent.getStringExtra("registrationPlate") ?: ""
        val brand = intent.getStringExtra("brand") ?: ""
        val model = intent.getStringExtra("model") ?: ""
        val color = intent.getStringExtra("color") ?: ""
        val greyCard = intent.getStringExtra("greyCard") ?: ""
        val clientId = intent.getIntExtra("clientId", 0)
        val vehicleId = intent.getIntExtra("vehicleId", 0)

        setContent {
            RepairFormApp(firstName, lastName, phoneNumber, birthDate, email, address, registrationPlate, brand, model, color, greyCard, clientId, vehicleId)
        }
    }

    @Composable
    fun RepairFormApp(firstName: String, lastName: String, phoneNumber: String, birthDate: String, email: String, address: String, registrationPlate: String, brand: String, model: String, color: String, greyCard: String, clientId: Int, vehicleId: Int) {
        val context = LocalContext.current

        var description by remember { mutableStateOf(TextFieldValue("")) }
        var date by remember { mutableStateOf(TextFieldValue("")) }
        var invoice by remember { mutableStateOf<String?>(null) }
        val calendar = Calendar.getInstance()

        val dateFormat = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
        var isDateError by remember { mutableStateOf(false) }

        // Launcher for quote document
        val invoiceLauncher = rememberLauncherForActivityResult(
            contract = ActivityResultContracts.GetContent()
        ) { uri: Uri? ->
            uri?.let {
                val path = getFilePathFromUri(context, it)
                invoice = path
            }
        }

        Scaffold(
        ) { padding ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding)
            ) {

                NavBar(text = "Formulaire Réparation",
                    onBackClick = {
                        val intent = Intent(context, VehicleForm::class.java).apply {
                            putExtra("firstName", firstName)
                            putExtra("lastName", lastName)
                            putExtra("phoneNumber", phoneNumber)
                            putExtra("birthDate", birthDate)
                            putExtra("email", email)
                            putExtra("address", address)
                            putExtra("registrationPlate", registrationPlate)
                            putExtra("brand", brand)
                            putExtra("model", model)
                            putExtra("color", color)
                            putExtra("clientId", clientId)
                            putExtra("vehicleId", vehicleId)
                        }
                        context.startActivity(intent)
                    })

                OutlinedTextField(
                    value = description,
                    onValueChange = { description = it },
                    label = { Text("Description de la réparation") },
                    modifier = Modifier.fillMaxWidth(),
                    isError = description.text.isEmpty()
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


                Button(
                    onClick = { invoiceLauncher.launch("*/*") },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(if (invoice.isNullOrEmpty()) "Télécharger de la facture" else "Facture sélectionnée")
                }
                Spacer(modifier = Modifier.height(16.dp))

                Button(
                    onClick = {
                        println("chemin de la facture : $invoice")
                        // Envoyer les données à la prochaine activité
                        val intent = Intent(context, Home::class.java).apply {
                            putExtra("firstName", firstName)
                            putExtra("lastName", lastName)
                            putExtra("phoneNumber", phoneNumber)
                            putExtra("birthDate", birthDate)
                            putExtra("email", email)
                            putExtra("address", address)
                            putExtra("registrationPlate", registrationPlate)
                            putExtra("brand", brand)
                            putExtra("model", model)
                            putExtra("greyCard", greyCard)
                            putExtra("clientId", clientId)
                            putExtra("vehicleId", vehicleId)
                            putExtra("invoice", invoice)
                        }
                        context.startActivity(intent)
                    },
                    modifier = Modifier.align(Alignment.CenterHorizontally),
                    //enabled = description.text.isNotEmpty() && date.text.isNotEmpty() && !carRegistrationDoc.isNullOrEmpty() && !quote.isNullOrEmpty()
                ) {
                    Text("Enregistrer")
                }
            }
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
        RepairFormApp("","","","","","","","","","","",0,0)
    }
}
