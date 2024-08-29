package com.example.autogestion

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.autogestion.data.AppDatabase
import com.example.autogestion.data.Client
import com.example.autogestion.data.viewModels.ClientViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.text.SimpleDateFormat
import java.util.Locale

class ClientDetails : ComponentActivity() {

    private var clientId: Int = -1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        clientId = intent.getIntExtra("clientId", -1)

        setContent {
            ClientDetailScreen(clientId)
        }
    }

    override fun onResume() {
        super.onResume()
        setContent {
            ClientDetailScreen(clientId)
        }
    }
    @Composable
    fun ClientDetailScreen(clientId: Int,  clientViewModel: ClientViewModel = viewModel()) {

        val coroutineScope = rememberCoroutineScope()

        LaunchedEffect(clientId) {
            clientViewModel.getClientById(clientId)
        }

        val client by clientViewModel.currentClient.observeAsState()


        client?.let { client ->
            Column(modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)) {

                Text(text = "Nom : ${client.lastName}")
                Text(text = "Prénom : ${client.firstName}")
                Text(text = "Téléphone : ${client.phone}")
                val birthDateFormatted = SimpleDateFormat("dd.MM.yyyy", Locale.getDefault()).format(client.birthDate)
                Text(text = "Date de naissance : $birthDateFormatted")
                Text(text = "Email : ${client.email}")
                Text(text = "Adresse : ${client.address}")


                Spacer(modifier = Modifier.height(16.dp))

                Button(onClick = {
                    val intent = Intent(this@ClientDetails, ClientUpdatePage::class.java).apply {
                        putExtra("clientId", client.clientId)
                    }
                    startActivity(intent)
                }) {
                    Text("Modifier")
                }
                Button(onClick = {
                    coroutineScope.launch {
                        clientViewModel.deleteClient(client)
                        redirectToHome()
                    }
                }) {
                    Text("Supprimer")
                }
            }
        } ?: run {
            Text(text = "Client non trouvé", modifier = Modifier.padding(16.dp))
        }
    }
    private fun redirectToHome() {
        val intent = Intent(this, Home::class.java)
        startActivity(intent)
        finish() // Ferme l'activité actuelle pour éviter le retour avec le bouton "Back"
    }
}
