package com.example.autogestion

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.example.autogestion.data.Client

@Composable
fun ClientForm(onSubmit: (Client) -> Unit, modifier: Modifier = Modifier) {
    var firstName by remember { mutableStateOf("") }
    var lastName by remember { mutableStateOf("") }
    var phone by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var address by remember { mutableStateOf("") }
    var isFirstNameError by remember { mutableStateOf(false) }
    var isLastNameError by remember { mutableStateOf(false) }
    var isPhoneError by remember { mutableStateOf(false) }

    Column(modifier = modifier.padding(16.dp)) {
        TextField(
            value = lastName,
            onValueChange = {
                lastName = it
                isLastNameError = it.isEmpty()
            },
            label = { Text("Nom *") },
            isError = isLastNameError
        )
        if (isLastNameError) {
            Text("Le nom est obligatoire", color = MaterialTheme.colorScheme.error)
        }
        Spacer(modifier = Modifier.height(8.dp))

        TextField(
            value = firstName,
            onValueChange = {
                firstName = it
                isFirstNameError = it.isEmpty()
            },
            label = { Text("Prénom *") },
            isError = isFirstNameError
        )
        if (isFirstNameError) {
            Text("Le prénom est obligatoire", color = MaterialTheme.colorScheme.error)
        }
        Spacer(modifier = Modifier.height(8.dp))

        TextField(
            value = phone,
            onValueChange = {
                phone = it
                isPhoneError = it.isEmpty()
            },
            label = { Text("Téléphone *") },
            keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Phone),
            isError = isPhoneError
        )
        if (isPhoneError) {
            Text("Le téléphone est obligatoire", color = MaterialTheme.colorScheme.error)
        }
        Spacer(modifier = Modifier.height(8.dp))

        TextField(
            value = email,
            onValueChange = {
                email = it
            },
            label = { Text("Email") },
        )
        Spacer(modifier = Modifier.height(8.dp))

        TextField(
            value = address,
            onValueChange = {
                address = it
            },
            label = { Text("Adresse") },
        )
        Spacer(modifier = Modifier.height(16.dp))

        Button(onClick = {
            // Mettre à jour les états d'erreur pour forcer la validation
            isFirstNameError = firstName.isEmpty()
            isLastNameError = lastName.isEmpty()
            isPhoneError = phone.isEmpty()

            if (!isFirstNameError && !isLastNameError && !isPhoneError) {
                val newClient = Client(
                    clientId = 0,  // Auto-incremented by Room
                    firstName = firstName,
                    lastName = lastName,
                    phone = phone,
                    email = email,
                    address = address
                )
                onSubmit(newClient)
            }
        }) {
            Text("Ajouter le client")
        }
    }
}
