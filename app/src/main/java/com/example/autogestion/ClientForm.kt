package com.example.autogestion

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.example.autogestion.data.Client
import java.text.SimpleDateFormat
import java.util.Locale

@Composable
fun ClientForm(onSubmit: (Client) -> Unit, modifier: Modifier = Modifier) {
    var firstName by remember { mutableStateOf("") }
    var lastName by remember { mutableStateOf("") }
    var phone by remember { mutableStateOf("") }
    var birthDate by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var address by remember { mutableStateOf("") }
    var isFirstNameError by remember { mutableStateOf(false) }
    var isLastNameError by remember { mutableStateOf(false) }
    var isPhoneError by remember { mutableStateOf(false) }
    var isBirthDateError by remember { mutableStateOf(false) }

    val dateFormat = SimpleDateFormat("dd.MM.yyyy", Locale.getDefault())

    Column(modifier = modifier.padding(16.dp)) {
        TextField(
            value = lastName,
            onValueChange = {
                lastName = it
                isLastNameError = it.isEmpty()
            },
            label = { Text("Nom *") },
            modifier = Modifier.fillMaxWidth(),
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
            modifier = Modifier.fillMaxWidth(),
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
            modifier = Modifier.fillMaxWidth(),
            isError = isPhoneError
        )
        if (isPhoneError) {
            Text("Le téléphone est obligatoire", color = MaterialTheme.colorScheme.error)
        }
        Spacer(modifier = Modifier.height(8.dp))

        TextField(
            value = birthDate,
            onValueChange = {
                birthDate = it
                isBirthDateError = try {
                    dateFormat.parse(it)
                    false
                } catch (e: Exception) {
                    true
                }
            },
            label = { Text("Date de naissance (jj.mm.aaaa)") },
            keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Phone),
            modifier = Modifier.fillMaxWidth(),
            isError = isBirthDateError,
        )
        if (isBirthDateError) {
            Text("Format de date invalide", color = MaterialTheme.colorScheme.error)
        }
        Spacer(modifier = Modifier.height(8.dp))

        TextField(
            value = email,
            onValueChange = {
                email = it
            },
            label = { Text("Email") },
            modifier = Modifier.fillMaxWidth(),
        )
        Spacer(modifier = Modifier.height(8.dp))

        TextField(
            value = address,
            onValueChange = {
                address = it
            },
            label = { Text("Adresse") },
            modifier = Modifier.fillMaxWidth(),
        )
        Spacer(modifier = Modifier.height(8.dp))

        Button(onClick = {
            isFirstNameError = firstName.isEmpty()
            isLastNameError = lastName.isEmpty()
            isPhoneError = phone.isEmpty()

            if (!isFirstNameError && !isLastNameError && !isPhoneError && !isBirthDateError) {
                val birthDateLong = if (birthDate.isNotEmpty()) {
                    dateFormat.parse(birthDate)?.time ?: 0L
                } else {
                    null
                }
                val newClient = Client(
                    clientId = 0,
                    firstName = firstName,
                    lastName = lastName,
                    phone = phone,
                    birthDate = birthDateLong ?: 0L,
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
