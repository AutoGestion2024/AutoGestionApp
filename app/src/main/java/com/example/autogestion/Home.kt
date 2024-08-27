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
    import androidx.compose.material3.Scaffold
    import androidx.compose.ui.Modifier
    import com.example.autogestion.ui.theme.AutoGestionTheme
    import androidx.compose.material3.ExtendedFloatingActionButton
    import androidx.compose.material3.Text
    import androidx.compose.runtime.Composable
    import com.example.autogestion.data.AppDatabase

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
            ) {
                    innerPadding ->
                Column(
                    modifier = Modifier.padding(innerPadding)
                ) {
                    Text("Liste clients")
                }
            }
        }
    }
