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
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.autogestion.Home
import com.example.autogestion.NavBar
import java.io.File

class RepairForm : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        enableEdgeToEdge()
        val firstName = intent.getStringExtra("firstName") ?: ""
        val lastName = intent.getStringExtra("lastName") ?: ""
        val phoneNumber = intent.getStringExtra("phoneNumber") ?: ""
        val birthDate = intent.getStringExtra("birthDate") ?: ""
        val email = intent.getStringExtra("email") ?: ""

        val plateNumber = intent.getStringExtra("plateNumber") ?: ""
        val make = intent.getStringExtra("make") ?: ""
        val model = intent.getStringExtra("model") ?: ""
        val clientId = intent.getLongExtra("clientId", 0)

        setContent {
            RepairFormApp(firstName, lastName, phoneNumber, birthDate, email, plateNumber, make, model, clientId)
        }
    }

    @Composable
    fun RepairFormApp(firstName: String, lastName: String, phoneNumber: String, birthDate: String, email: String, plateNumber: String, make: String, model: String, clientId: Long) {
        val context = LocalContext.current

        var description by remember { mutableStateOf(TextFieldValue("")) }
        var date by remember { mutableStateOf(TextFieldValue("")) }
        var carRegistrationDoc by remember { mutableStateOf<String?>(null) }
        var quote by remember { mutableStateOf<String?>(null) }

        // Launcher for car registration document
        val carRegistrationDocLauncher = rememberLauncherForActivityResult(
            contract = ActivityResultContracts.GetContent()
        ) { uri: Uri? ->
            uri?.let {
                val path = getFilePathFromUri(context, it)
                carRegistrationDoc = path
            }
        }

        // Launcher for quote document
        val quoteLauncher = rememberLauncherForActivityResult(
            contract = ActivityResultContracts.GetContent()
        ) { uri: Uri? ->
            uri?.let {
                val path = getFilePathFromUri(context, it)
                quote = path
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
                        val intent = Intent(context, ClientForm::class.java).apply {
                            putExtra("firstName", firstName)
                            putExtra("lastName", lastName)
                            putExtra("phoneNumber", phoneNumber)
                            putExtra("birthDate", birthDate)
                            putExtra("email", email)
                            putExtra("plateNumber", plateNumber)
                            putExtra("make", make)
                            putExtra("model", model)
                            putExtra("clientId", clientId)
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
                    label = { Text("Date de réparation") },
                    modifier = Modifier.fillMaxWidth(),
                    isError = date.text.isEmpty()
                )
                Spacer(modifier = Modifier.height(16.dp))

                Button(
                    onClick = { carRegistrationDocLauncher.launch("*/*") },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(if (carRegistrationDoc.isNullOrEmpty()) "Télécharger le document d'immatriculation" else "Document d'immatriculation sélectionné")
                }
                Spacer(modifier = Modifier.height(16.dp))

                Button(
                    onClick = { quoteLauncher.launch("*/*") },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(if (quote.isNullOrEmpty()) "Télécharger le devis" else "Devis sélectionné")
                }
                Spacer(modifier = Modifier.height(16.dp))

                Button(
                    onClick = {
                        println("chemin de la carte grise : $carRegistrationDoc")
                        println("chemin du devis : $quote")
                        // Envoyer les données à la prochaine activité
                        val intent = Intent(context, Home::class.java).apply {
                            putExtra("firstName", firstName)
                            putExtra("lastName", lastName)
                            putExtra("phoneNumber", phoneNumber)
                            putExtra("birthDate", birthDate)
                            putExtra("email", email)
                            putExtra("plateNumber", plateNumber)
                            putExtra("make", make)
                            putExtra("model", model)
                            putExtra("clientId", clientId)
                            putExtra("carRegistrationDoc", carRegistrationDoc)
                            putExtra("quote", quote)
                        }
                        context.startActivity(intent)
                    },
                    modifier = Modifier.align(Alignment.CenterHorizontally),
                    enabled = description.text.isNotEmpty() && date.text.isNotEmpty() && !carRegistrationDoc.isNullOrEmpty() && !quote.isNullOrEmpty()
                ) {
                    Text("Suivant")
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
        RepairFormApp("","","","","","","","",0)
    }
}
