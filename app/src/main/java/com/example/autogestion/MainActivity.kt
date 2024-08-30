package com.example.autogestion

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.*
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.autogestion.data.AppDatabase
import com.example.autogestion.data.viewModels.ClientViewModel


class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            initDB()
            Home().HomeApp()
        }
    }

}

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    Home().HomeApp()
}

@Composable
fun initDB(){
    // Create an instance of the database
    lateinit var database: AppDatabase
    database = AppDatabase.getDatabase(LocalContext.current)
}