    package com.example.autogestion

    import android.os.Bundle
    import android.util.Log
    import androidx.activity.ComponentActivity
    import androidx.activity.compose.setContent
    import androidx.activity.enableEdgeToEdge
    import androidx.compose.foundation.layout.fillMaxSize
    import androidx.compose.foundation.layout.padding
    import androidx.compose.material3.Scaffold
    import androidx.compose.ui.Modifier
    import com.example.autogestion.data.Client
    import com.example.autogestion.data.AppDatabase
    import com.example.autogestion.ui.theme.AutoGestionTheme
    import kotlinx.coroutines.*


    class HomeForm : ComponentActivity() {

        private lateinit var database: AppDatabase
        private val coroutineScope = CoroutineScope(Dispatchers.IO)

        override fun onCreate(savedInstanceState: Bundle?) {
            super.onCreate(savedInstanceState)

            // Initialize the database
            database = AppDatabase.getDatabase(this)
            Log.d("AppDatabase", "Database instance: ${database.isOpen}")

            // UI setup
            enableEdgeToEdge()
            setContent {
                AutoGestionTheme {
                    Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                        ClientForm(
                            onSubmit = { client ->
                                coroutineScope.launch {
                                    addClient(client)
                                }
                            },
                            modifier = Modifier.padding(innerPadding)
                        )
                    }
                }
            }
        }

        private suspend fun addClient(client: Client) {
            withContext(Dispatchers.IO) {
                val emailExists = database.clientDao().getClientByEmail(client.email)
                if (emailExists != null) {
                    Log.d("HomeForm", "Client avec cet email existe déjà: ${client.name} ${client.lastName} ${client.email}")
                    return@withContext
                }
                else {
                    database.clientDao().addClient(client)
                    Log.d("HomeForm", "Client ajouté: ${client.name} ${client.lastName}")
                }
            }
        }
    }
