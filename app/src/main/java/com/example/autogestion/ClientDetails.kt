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
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.autogestion.data.AppDatabase
import com.example.autogestion.data.Client
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.text.SimpleDateFormat
import java.util.Locale

class ClientDetails : ComponentActivity() {

    private lateinit var database: AppDatabase

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        database = AppDatabase.getDatabase(this)

        val clientId = intent.getIntExtra("clientId", -1)

        setContent {
            ClientDetailScreen(clientId)
        }
    }

    @Composable
    fun ClientDetailScreen(clientId: Int) {
        var client by remember { mutableStateOf<Client?>(null) }

        LaunchedEffect(clientId) {
            // Exécuter l'opération de base de données dans Dispatchers.IO
            client = withContext(Dispatchers.IO) {
                database.clientDao().getClientById(clientId)
            }
        }

        client?.let { client ->
            Column(modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)) {

                Text(text = "Nom: ${client.lastName}")
                Text(text = "Prénom: ${client.firstName}")
                Text(text = "Téléphone: ${client.phone}")
                Text(text = "Email: ${client.email}")
                Text(text = "Adresse: ${client.address}")
                val birthDateFormatted = SimpleDateFormat("dd.MM.yyyy", Locale.getDefault()).format(client.birthDate)
                Text(text = "Date de naissance: $birthDateFormatted")

                Spacer(modifier = Modifier.height(16.dp))

                Button(onClick = {
                    val intent = Intent(this@ClientDetails, ClientUpdatePage::class.java).apply {
                        putExtra("clientId", client.clientId)
                    }
                    startActivity(intent)
                }) {
                    Text("Modifier")
                }
            }
        } ?: run {
            Text(text = "Client non trouvé", modifier = Modifier.padding(16.dp))
        }
    }
}
