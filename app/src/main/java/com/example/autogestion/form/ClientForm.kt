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
import com.example.autogestion.Home
import com.example.autogestion.NavBar


class ClientForm : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        enableEdgeToEdge()

        // Récupérer les données de l'Intent
        val firstName = intent.getStringExtra("firstName") ?: ""
        val lastName = intent.getStringExtra("lastName") ?: ""
        val phoneNumber = intent.getStringExtra("phoneNumber") ?: ""
        val birthDate = intent.getStringExtra("birthDate") ?: ""
        val email = intent.getStringExtra("email") ?: ""
        setContent {

            ClientFormApp(initFirstName = firstName,
                initLastName = lastName,
                initPhoneNumber = phoneNumber,
                initBirthDate = birthDate,
                initEmail = email)

        }
    }

    @Composable
    fun ClientFormApp(initFirstName: String,
                      initLastName: String,
                      initPhoneNumber: String,
                      initBirthDate: String,
                      initEmail: String) {
        val context = LocalContext.current

        var firstName by remember { mutableStateOf(TextFieldValue(initFirstName)) }
        var lastName by remember { mutableStateOf(TextFieldValue(initLastName)) }
        var phoneNumber by remember { mutableStateOf(TextFieldValue(initPhoneNumber)) }
        var birthDate by remember { mutableStateOf(TextFieldValue(initBirthDate)) }
        var email by remember { mutableStateOf(TextFieldValue(initEmail)) }

        Scaffold(
        ) { padding ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding)
            ) {

                NavBar(text = "Formulaire Client",
                    onBackClick = {
                        val intent = Intent(context, Home::class.java)
                    context.startActivity(intent)})

                OutlinedTextField(
                    value = firstName,
                    onValueChange = { firstName = it },
                    label = { Text("Prénom") },
                    modifier = Modifier.fillMaxWidth(),
                    isError = firstName.text.isEmpty()
                )
                Spacer(modifier = Modifier.height(16.dp))

                OutlinedTextField(
                    value = lastName,
                    onValueChange = { lastName = it },
                    label = { Text("Nom") },
                    modifier = Modifier.fillMaxWidth(),
                    isError = lastName.text.isEmpty()
                )
                Spacer(modifier = Modifier.height(16.dp))

                OutlinedTextField(
                    value = phoneNumber,
                    onValueChange = { phoneNumber = it },
                    label = { Text("Numéro de téléphone") },
                    modifier = Modifier.fillMaxWidth(),
                    isError = phoneNumber.text.isEmpty()
                )
                Spacer(modifier = Modifier.height(16.dp))

                OutlinedTextField(
                    value = birthDate,
                    onValueChange = { birthDate = it },
                    label = { Text("Date de naissance (optional)") },
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(modifier = Modifier.height(16.dp))

                OutlinedTextField(
                    value = email,
                    onValueChange = { email = it },
                    label = { Text("Email (optional)") },
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(modifier = Modifier.height(32.dp))

                Button(
                    onClick = {
                        // TODO: Navigate to the CarForm screen
                        val intent = Intent(context, CarForm::class.java).apply {
                            putExtra("firstName", firstName.text)
                            putExtra("lastName", lastName.text)
                            putExtra("phoneNumber", phoneNumber.text)
                            putExtra("birthDate", birthDate.text)
                            putExtra("email", email.text)
                        }
                        context.startActivity(intent)
                    },
                    modifier = Modifier.align(Alignment.CenterHorizontally),
                    enabled = firstName.text.isNotEmpty() && lastName.text.isNotEmpty() && phoneNumber.text.isNotEmpty()
                ) {
                    Text("Suivant")
                }
            }
        }
    }

    @Preview(showBackground = true)
    @Composable
    fun DefaultPreview2() {
        ClientFormApp("","","","","")
    }


}
