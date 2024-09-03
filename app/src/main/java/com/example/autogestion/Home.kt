package com.example.autogestion

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.CameraAlt
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.autogestion.data.AppDatabase
import com.example.autogestion.data.Client
import com.example.autogestion.data.viewModels.VehicleViewModel
import com.example.autogestion.form.ClientForm


class Home : ComponentActivity() {

    // Create an instance of the database
    private lateinit var database: AppDatabase

    private val vehicleViewModel: VehicleViewModel by viewModels()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        database = AppDatabase.getDatabase(this)
        Log.d("AppDatabase", "Database instance: ${database.isOpen}")

        if (!hasRequiredPermissions()) {
            ActivityCompat.requestPermissions(this, CAMERAX_PERMISSIONS, 0)
        }

        val textSearch = intent.getStringExtra("search_text") ?: ""

        enableEdgeToEdge()
        setContent {
            HomeApp(textSearch)
        }
    }

    private fun hasRequiredPermissions(): Boolean{
        return CAMERAX_PERMISSIONS.all {
            ContextCompat.checkSelfPermission(applicationContext, it) == PackageManager.PERMISSION_GRANTED
        }
    }

    companion object{
        private val CAMERAX_PERMISSIONS = arrayOf(
            Manifest.permission.CAMERA,
            Manifest.permission.INTERNET
        )
    }


    @Composable
    fun HomeApp(textSearch: String ) {
        val context = LocalContext.current

        val clients by database.clientDao().getAllClients().observeAsState(initial = emptyList())

        val items by remember { mutableStateOf(clients) }
//        var searchText by remember { mutableStateOf(TextFieldValue("")) }
        var searchText by remember { mutableStateOf(if (textSearch.isNotEmpty()) TextFieldValue(textSearch) else TextFieldValue("")) }

        println("Dans HomeApp: $searchText")

        Scaffold(
            floatingActionButton = {
                FloatingActionButton(
                    onClick = {
                        val intent = Intent(context, ClientForm::class.java)
                        context.startActivity(intent)
                    },
                    modifier = Modifier
                        .padding(16.dp)
                ) {
                    Icon(imageVector = Icons.Default.Add, contentDescription = "Add")
                }
            }
        ) { padding ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding)
            ) {
                HomeTitle(text = "AutoGestion")

                Row (
                    horizontalArrangement = Arrangement.Start, // Align items horizontally
                    verticalAlignment = Alignment.CenterVertically, // Center vertically
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp)
                ){
                    Icon(
                        imageVector = Icons.Default.Search,
                        contentDescription = null,
                        modifier = Modifier
                            .padding(end = 8.dp)
                    )

                    // Search Bar
                    BasicTextField(
                        value = searchText,
                        onValueChange = { searchText = it },
                        modifier = Modifier
                            .background(color = Color.LightGray)
                            .weight(1f)
                            .padding(end = 8.dp),
                        textStyle = LocalTextStyle.current.copy(fontSize = 18.sp)
                    )


                    IconButton(
                        onClick = {
                            val intent = Intent(context, Camera::class.java)
                            context.startActivity(intent)

                        }
                    ) {
                        Icon(
                            imageVector = Icons.Default.CameraAlt,
                            contentDescription = null,
                            modifier = Modifier
                                .padding(end = 8.dp)
                        )
                    }

                }

                // Filtered List
                val filteredItems = items.filter { client ->
                    client.firstName.contains(searchText.text, ignoreCase = true) ||
                            client.lastName.contains(searchText.text, ignoreCase = true) ||
                            vehicleViewModel.getVehiclesFromClient(client.clientId).any { vehicle ->
                                vehicle?.registrationPlate?.contains(searchText.text, ignoreCase = true) == true
                            }
                }


                LazyColumn(
                    modifier = Modifier
                        .padding(16.dp)
                        .wrapContentSize()
                ) {
                    items(filteredItems.size) { index ->
                        val client = filteredItems[index]
                        ClientVehicleInfo(client = client)
                        if (index < filteredItems.size - 1) {
                            Spacer(modifier = Modifier.height(8.dp))
                        }
                    }
                }
            }
        }
    }

    @Composable
    fun ClientVehicleInfo(client : Client, vehicleViewModel: VehicleViewModel = viewModel()) {
        val current = LocalContext.current
        Column(modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(16.dp))
            .background(color = Color(0xFFF3EDF7))
            .padding(15.dp)
            .clickable {
                val intent = Intent(current, ClientProfile::class.java).apply {
                    putExtra("clientId", client.clientId)
                }

                current.startActivity(intent)
                /*TODO ajouter les parametre de transmission */
            }) {
            Text(
                text = "${client.firstName} ${client.lastName}",
                modifier = Modifier.padding(bottom = 4.dp),
                fontSize = 20.sp
            )
            vehicleViewModel.getVehiclesFromClient(client.clientId).joinToString(separator = "\n ") { "${it?.brand}, ${it?.model}" }.let {
                Text(
                    text = it,
                    modifier = Modifier
                        .padding(bottom = 4.dp)
                )
            }
        }
    }

    @Composable
    fun HomeTitle(text: String){
        // Up Bar
        Row(modifier = Modifier
            .height(56.dp)
            .fillMaxWidth()
            .background(Color(0xFFF3EDF7)) ,
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {
            androidx.compose.material.Text(
                text = text,
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                color = Color.Black,
                textAlign = TextAlign.Center
            )
        }
    }

    @Preview(showBackground = true)
    @Composable
    fun DefaultPreview() {
        Home().HomeApp("")
    }
}
