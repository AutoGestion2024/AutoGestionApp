package com.example.autogestion

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Scaffold
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import androidx.lifecycle.LiveData
import com.example.autogestion.data.AppDatabase
import com.example.autogestion.data.Client
import com.example.autogestion.ui.theme.AutoGestionTheme

class Home : ComponentActivity() {

    private lateinit var database: AppDatabase

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        database = AppDatabase.getDatabase(this)
        Log.d("AppDatabase", "Database instance: ${database.isOpen}")

        enableEdgeToEdge()
        setContent {
            AutoGestionTheme {
                HomeScreen()
            }
        }
    }

    @Composable
    fun HomeScreen() {
        val clientList by getClients().observeAsState(initial = emptyList())

        Scaffold(
            modifier = Modifier.fillMaxSize(),
            floatingActionButton = {
                ExtendedFloatingActionButton(
                    onClick = {
                        val intent = Intent(this@Home, HomeForm::class.java)
                        startActivity(intent)
                    },
                    content = { Text("+") }
                )
            }
        ) { innerPadding ->
            Column(
                modifier = Modifier.padding(innerPadding)
            ) {
                Text("Liste clients")
                ClientList(clientList)
            }
        }
    }

    @Composable
    fun ClientList(clients: List<Client>) {
        LazyColumn {
            items(clients) { client ->
                Text(text = "${client.lastName} ${client.firstName}")
            }
        }
    }

    private fun getClients(): LiveData<List<Client>> {
        return database.clientDao().getAllClients()
    }
}
