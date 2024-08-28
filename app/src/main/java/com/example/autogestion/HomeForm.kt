    package com.example.autogestion

    import android.os.Bundle
    import android.util.Log
    import androidx.activity.ComponentActivity
    import androidx.activity.compose.setContent
    import androidx.activity.enableEdgeToEdge
    import androidx.activity.viewModels
    import androidx.compose.foundation.layout.fillMaxSize
    import androidx.compose.foundation.layout.padding
    import androidx.compose.material3.Scaffold
    import androidx.compose.ui.Modifier
    import com.example.autogestion.data.AppDatabase
    import com.example.autogestion.data.viewModels.ClientViewModel
    import com.example.autogestion.ui.theme.AutoGestionTheme


    class HomeForm : ComponentActivity() {

        private lateinit var database: AppDatabase

        private val clientVehicleViewModel: ClientViewModel by viewModels()

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
                                clientVehicleViewModel.addClient(client)
                            },
                            modifier = Modifier.padding(innerPadding)
                        )
                    }
                }
            }
        }
    }
