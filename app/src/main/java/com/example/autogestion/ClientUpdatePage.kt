package com.example.autogestion

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.ImeAction
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.autogestion.data.Client
import com.example.autogestion.ui.theme.AutoGestionTheme
import com.example.autogestion.data.viewModels.ClientViewModel
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class ClientUpdatePage : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val clientId = intent.getIntExtra("clientId", 0)

        setContent {
            AutoGestionTheme {
                UpdateClientScreen(clientId)
            }
        }
    }

    @Composable
    fun UpdateClientScreen(clientId: Int, clientViewModel: ClientViewModel = viewModel()) {
        LaunchedEffect(clientId) {
            clientViewModel.getClientById(clientId)
        }

        val client = clientViewModel.currentClient.observeAsState().value

        Scaffold(
        ) { innerPadding ->
            client?.let {
                ClientUpdateForm(it, clientViewModel, innerPadding)
            }
        }
    }

    @Composable
    fun ClientUpdateForm(client: Client, clientViewModel: ClientViewModel, innerPadding: PaddingValues) {
        LaunchedEffect(client.clientId) {
            clientViewModel.getClientById(client.clientId)
        }

        var firstName = rememberSaveable { mutableStateOf(client.firstName) }
        var lastName = rememberSaveable { mutableStateOf(client.lastName) }
        var phone = rememberSaveable { mutableStateOf(client.phone) }
        var email = rememberSaveable { mutableStateOf(client.email) }
        var birthDate by rememberSaveable { mutableStateOf(formatDate(client.birthDate))}
        var address by rememberSaveable { mutableStateOf(client.address) }

        Column(modifier = Modifier.padding(innerPadding)) {
            TextField(
                value = lastName.value,
                onValueChange = { lastName.value = it },
                label = { Text("Nom *") },
                keyboardOptions = KeyboardOptions.Default.copy(imeAction = ImeAction.Next)
            )
            TextField(
                value = firstName.value,
                onValueChange = { firstName.value = it },
                label = { Text("Prénom *") },
                keyboardOptions = KeyboardOptions.Default.copy(imeAction = ImeAction.Next)
            )
            TextField(
                value = phone.value,
                onValueChange = { phone.value = it },
                label = { Text("Téléphone *") },
                keyboardOptions = KeyboardOptions.Default.copy(imeAction = ImeAction.Next)
            )
            TextField(
                value = birthDate ?: "",
                onValueChange = { birthDate= it },
                label = { Text("Date de naissance (jj.mm.aaaa)") },
                keyboardOptions = KeyboardOptions.Default.copy(imeAction = ImeAction.Next)
            )
            TextField(
                value = email.value ?: "",   // Default value is empty string
                onValueChange = { email.value = it },
                label = { Text("Email") },
                keyboardOptions = KeyboardOptions.Default.copy(imeAction = ImeAction.Done),
                keyboardActions = KeyboardActions(onDone = { /* Define actions on keyboard 'Done' */ })
            )
            TextField(
                value = address?: "",   // Default value is empty string
                onValueChange = { address = it },
                label = { Text("Email") },
                keyboardOptions = KeyboardOptions.Default.copy(imeAction = ImeAction.Done),
                keyboardActions = KeyboardActions(onDone = { /* Define actions on keyboard 'Done' */ })
            )
            Button(onClick = {
                val birthDateLong = parseDate(birthDate) ?: client.birthDate
                clientViewModel.updateClient(client.copy(
                    lastName = lastName.value,
                    firstName = firstName.value,
                    phone = phone.value,
                    birthDate = birthDateLong,
                    email = email.value,
                    address = address,
                ))
                val intent = Intent(this@ClientUpdatePage, ClientDetails::class.java).apply {
                    putExtra("clientId", client.clientId)
                }
                startActivity(intent)
                finish()
            }) {
                Text("Enregistrer")
            }
        }
    }
    private fun formatDate(dateInMillis: Long): String {
        val dateFormat = SimpleDateFormat("dd.MM.yyyy", Locale.getDefault())
        return dateFormat.format(Date(dateInMillis))
    }

    private fun parseDate(dateString: String): Long? {
        return try {
            val dateFormat = SimpleDateFormat("dd.MM.yyyy", Locale.getDefault())
            dateFormat.parse(dateString)?.time
        } catch (e: Exception) {
            null
        }
    }
}