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
    var name by remember { mutableStateOf("") }
    var lastName by remember { mutableStateOf("") }
    var phone by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var address by remember { mutableStateOf("") }

    Column(modifier = modifier.padding(16.dp)) {
        TextField(
            value = name,
            onValueChange = { name = it },
            label = { Text("Nom") }
        )
        Spacer(modifier = Modifier.height(8.dp))
        TextField(
            value = lastName,
            onValueChange = { lastName = it },
            label = { Text("Prénom") }
        )
        Spacer(modifier = Modifier.height(8.dp))
        TextField(
            value = phone,
            onValueChange = { phone = it },
            label = { Text("Téléphone") },
            keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Phone)
        )
        Spacer(modifier = Modifier.height(8.dp))
        TextField(
            value = email,
            onValueChange = { email = it },
            label = { Text("Email") },
            keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Email)
        )
        Spacer(modifier = Modifier.height(8.dp))
        TextField(
            value = address,
            onValueChange = { address = it },
            label = { Text("Adresse") }
        )
        Spacer(modifier = Modifier.height(16.dp))
        Button(onClick = {
            val newClient = Client(
                clientId = 0,  // Auto-incremented by Room
                name = name,
                lastName = lastName,
                phone = phone,
                email = email,
                address = address
            )
            onSubmit(newClient)
        }) {
            Text("Ajouter le client")
        }
    }
}
