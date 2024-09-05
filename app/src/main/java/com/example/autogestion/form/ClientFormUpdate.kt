package com.example.autogestion.form

import android.app.DatePickerDialog
import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CalendarToday
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.autogestion.ClientProfile
import com.example.autogestion.Home
import com.example.autogestion.NavBar
import com.example.autogestion.VehicleProfile
import com.example.autogestion.data.Client
import com.example.autogestion.data.viewModels.ClientViewModel
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*

class ClientFormUpdate : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val clientId = intent.getIntExtra("clientId", 0)

        enableEdgeToEdge()

        setContent {
            UpdateClientScreen(clientId)
        }
    }

    @Composable
    fun UpdateClientScreen(clientId: Int, clientViewModel: ClientViewModel = viewModel()) {
        LaunchedEffect(clientId) {
            clientViewModel.getClientById(clientId)
        }

        val client = clientViewModel.currentClient.observeAsState().value

        Scaffold { innerPadding ->
            client?.let {
                ClientFormUpdateApp(it, clientViewModel, innerPadding)
            }
        }
    }

    @Composable
    fun ClientFormUpdateApp(
        client: Client,
        clientViewModel: ClientViewModel,
        innerPadding: PaddingValues
    ) {
        val context = LocalContext.current

        var firstName by rememberSaveable { mutableStateOf(client.firstName) }
        var lastName by rememberSaveable { mutableStateOf(client.lastName) }
        var phoneNumber by rememberSaveable { mutableStateOf(client.phone) }
        var email by rememberSaveable { mutableStateOf(client.email ?: "") }
        var birthDate by rememberSaveable { mutableStateOf(formatDate(client.birthDate)) }
        var address by rememberSaveable { mutableStateOf(client.address ?: "") }

        var isFirstNameError by remember { mutableStateOf(false) }
        var isLastNameError by remember { mutableStateOf(false) }
        var isPhoneError by remember { mutableStateOf(false) }
        var isBirthDateError by remember { mutableStateOf(false) }
        var phoneExistsError by remember { mutableStateOf(false) }

        val coroutineScope = rememberCoroutineScope()
        val dateFormat = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
        val calendar = Calendar.getInstance()

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {

            NavBar(
                text = "Modifier le Client",
                onBackClick = {
                    val intent = Intent(context, ClientProfile::class.java).apply {
                        putExtra("clientId", client.clientId)
                    }
                    context.startActivity(intent)
                }
            )

            OutlinedTextField(
                value = firstName,
                onValueChange = { firstName = it },
                label = { Text("Prénom") },
                modifier = Modifier.fillMaxWidth(),
                isError = isFirstNameError
            )
            Spacer(modifier = Modifier.height(16.dp))

            OutlinedTextField(
                value = lastName,
                onValueChange = { lastName = it },
                label = { Text("Nom") },
                modifier = Modifier.fillMaxWidth(),
                isError = isLastNameError
            )
            Spacer(modifier = Modifier.height(16.dp))

            OutlinedTextField(
                value = phoneNumber,
                onValueChange = { phoneNumber = it },
                label = { Text("Numéro de téléphone") },
                keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Phone),
                modifier = Modifier.fillMaxWidth(),
                isError = isPhoneError || phoneExistsError
            )
            if (phoneExistsError) {
                Text(
                    "Ce numéro de téléphone existe déjà.",
                    color = MaterialTheme.colorScheme.error,
                    modifier = Modifier.padding(start = 16.dp)
                )
            }
            Spacer(modifier = Modifier.height(16.dp))

            OutlinedTextField(
                value = birthDate,
                onValueChange = { birthDate = it },
                label = { Text("Date de naissance (optionnel)") },
                keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Number),
                modifier = Modifier.fillMaxWidth(),
                readOnly = true,
                isError = isBirthDateError,
                trailingIcon = {
                    IconButton(onClick = {
                        val datePickerDialog = DatePickerDialog(
                            context,
                            { _, year, month, dayOfMonth ->
                                calendar.set(year, month, dayOfMonth)
                                birthDate = dateFormat.format(calendar.time)
                                isBirthDateError = false
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
            if (isBirthDateError) {
                Text("Format de date invalide", color = MaterialTheme.colorScheme.error)
            }
            Spacer(modifier = Modifier.height(16.dp))

            OutlinedTextField(
                value = email,
                onValueChange = { email = it },
                label = { Text("Email (optionnel)") },
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(16.dp))

            OutlinedTextField(
                value = address,
                onValueChange = { address = it },
                label = { Text("Adresse (optionnel)") },
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(16.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Center
            ) {
                Button(
                    onClick = {
                        isFirstNameError = firstName.isEmpty()
                        isLastNameError = lastName.isEmpty()
                        isPhoneError = phoneNumber.isEmpty()

                        if (!isFirstNameError && !isLastNameError && !isPhoneError && !isBirthDateError) {
                            coroutineScope.launch {
                                val clientLiveData = clientViewModel.getClientByPhoneNumber(phoneNumber)
                                clientLiveData.observe(context as ComponentActivity) { existingClient ->
                                    if (existingClient != null && existingClient.clientId != client.clientId) {
                                        phoneExistsError = true
                                    } else {
                                        phoneExistsError = false
                                        val birthDateLong = if (birthDate.isNotEmpty()) {
                                            dateFormat.parse(birthDate)?.time ?: 0L
                                        } else {
                                            null
                                        }
                                        val updatedClient = client.copy(
                                            firstName = firstName,
                                            lastName = lastName,
                                            phone = phoneNumber,
                                            birthDate = birthDateLong ?: 0L,
                                            email = email,
                                            address = address
                                        )

                                        coroutineScope.launch {
                                            clientViewModel.updateClient(updatedClient)
                                            val intent = Intent(context, ClientProfile::class.java).apply {
                                                putExtra("clientId", updatedClient.clientId)
                                            }
                                            context.startActivity(intent)
                                        }
                                    }
                                }
                            }
                        }
                    },
                    enabled = firstName.isNotEmpty() && lastName.isNotEmpty() && phoneNumber.isNotEmpty() && !isBirthDateError
                ) {
                    Text("Enregistrer les modifications")
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

    private fun formatDate(timestamp: Long?): String {
        return if (timestamp != null && timestamp > 0) {
            SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).format(Date(timestamp))
        } else {
            ""
        }
    }
}
