package com.example.autogestion.form

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
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
import com.example.autogestion.NavBar



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

        val initPlateNumber = intent.getStringExtra("plateNumber") ?: ""
        val initBrand = intent.getStringExtra("brand") ?: ""
        val initModel = intent.getStringExtra("model") ?: ""
        val initClientId = intent.getLongExtra("clientId", 0)

        setContent {
            CarFormApp(firstName, lastName, phoneNumber, birthDate, email, initPlateNumber, initBrand, initModel, initClientId)
        }
    }

    @Composable
    fun CarFormApp(firstName: String, lastName: String, phoneNumber: String, birthDate: String, email: String, initPlateNumber: String, initMake: String, initModel: String, initClientId: Long) {
        val context = LocalContext.current

        var plateNumber by remember { mutableStateOf(TextFieldValue(initPlateNumber)) }
        var brand by remember { mutableStateOf(TextFieldValue(initMake)) }
        var model by remember { mutableStateOf(TextFieldValue(initModel)) }
        var clientId by remember { mutableStateOf(initClientId) }

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
                        }
                        context.startActivity(intent)
                    })

                OutlinedTextField(
                    value = plateNumber,
                    onValueChange = { plateNumber = it },
                    label = { Text("Numéro de plaque") },
                    modifier = Modifier.fillMaxWidth(),
                    isError = plateNumber.text.isEmpty()
                )
                Spacer(modifier = Modifier.height(16.dp))

                OutlinedTextField(
                    value = brand,
                    onValueChange = { brand = it },
                    label = { Text("Marque") },
                    modifier = Modifier.fillMaxWidth(),
                    isError = brand.text.isEmpty()
                )
                Spacer(modifier = Modifier.height(16.dp))

                OutlinedTextField(
                    value = model,
                    onValueChange = { model = it },
                    label = { Text("Modèle") },
                    modifier = Modifier.fillMaxWidth(),
                    isError = model.text.isEmpty()
                )
                Spacer(modifier = Modifier.height(16.dp))

                Button(
                    onClick = {
                        val intent = Intent(context, RepairForm::class.java).apply {
                            putExtra("firstName", firstName)
                            putExtra("lastName", lastName)
                            putExtra("phoneNumber", phoneNumber)
                            putExtra("birthDate", birthDate)
                            putExtra("email", email)
                            putExtra("plateNumber", plateNumber.text)
                            putExtra("brand", brand.text)
                            putExtra("model", model.text)
                            putExtra("clientId", clientId)
                        }
                        context.startActivity(intent)
                    },
                    modifier = Modifier.align(Alignment.CenterHorizontally),
                    enabled = plateNumber.text.isNotEmpty() && brand.text.isNotEmpty() && model.text.isNotEmpty()
                ) {
                    Text("Suivant")
                }
            }
        }
    }

    @Preview(showBackground = true)
    @Composable
    fun DefaultPreview2() {
        CarFormApp("","","","","","","","",0)
    }


}