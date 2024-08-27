package com.example.autogestion

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.example.autogestion.data.Client
import com.example.autogestion.data.ClientDatabase
import com.example.autogestion.ui.theme.AutoGestionTheme
import kotlinx.coroutines.*

class MainActivity : ComponentActivity() {

    private lateinit var database: ClientDatabase
    // Coroutine scope for running database operations on the IO dispatcher.
    private val coroutineScope = CoroutineScope(Dispatchers.IO)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Initialize the database
        database = ClientDatabase.getDatabase(this)
        // Launch a coroutine to add a client to the database.
        coroutineScope.launch {
            addClient()
        }

        // UI setup
        enableEdgeToEdge()
        setContent {
            AutoGestionTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    Greeting(
                        name = "Android",
                        modifier = Modifier.padding(innerPadding)
                    )
                }
            }
        }
    }

    // Function to add a client to the database.
    private fun addClient() {
        val newClient = Client(
            clientId = 0,  // Auto-incremented
            name = "John",
            lastName = "Doe",
            phone = "1234567890",
            email = "john.doe@example.com",
            address = "1234 Elm Street"
        )
        database.clientDao().addClient(newClient)
        Log.d("MainActivity", "Client added")
    }

}

@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
    Text(
        text = "Hello $name!",
        modifier = modifier
    )
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    AutoGestionTheme {
        Greeting("Hello, demo!")
    }
}