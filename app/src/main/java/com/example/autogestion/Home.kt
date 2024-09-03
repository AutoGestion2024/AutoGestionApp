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
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.CameraAlt
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
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
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.autogestion.data.AppDatabase
import com.example.autogestion.data.Client
import com.example.autogestion.data.Vehicle
import com.example.autogestion.data.viewModels.ClientViewModel
import com.example.autogestion.data.viewModels.RepairViewModel
import com.example.autogestion.data.viewModels.VehicleViewModel
import com.example.autogestion.form.ClientForm


class Home : ComponentActivity() {

    private lateinit var database: AppDatabase
    private val clientViewModel: ClientViewModel by viewModels()
    private val vehicleViewModel: VehicleViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        database = AppDatabase.getDatabase(this)
        Log.d("AppDatabase", "Database instance: ${database.isOpen}")

        if (!hasRequiredPermissions()) {
            ActivityCompat.requestPermissions(this, CAMERAX_PERMISSIONS, 0)
        }
        enableEdgeToEdge()
        setContent {
            HomeApp()
        }
    }

    private fun hasRequiredPermissions(): Boolean {
        return CAMERAX_PERMISSIONS.all {
            ContextCompat.checkSelfPermission(applicationContext, it) == PackageManager.PERMISSION_GRANTED
        }
    }

    companion object {
        private val CAMERAX_PERMISSIONS = arrayOf(
            Manifest.permission.CAMERA,
            Manifest.permission.INTERNET
        )
    }

    @Composable
    fun HomeApp() {
        val context = LocalContext.current
        val clients by getClients().observeAsState(initial = emptyList())
        var searchText by remember { mutableStateOf(TextFieldValue("")) }

        val sortedClients = clients.sortedBy { it.lastName }

        /*val filteredItems = sortedClients.filter { client ->
            val vehicles = vehicleViewModel.getVehiclesFromClient(client.clientId)

            client.firstName.contains(searchText.text, ignoreCase = true) ||
                    client.lastName.contains(searchText.text, ignoreCase = true) ||
                    vehicles.any { vehicle ->
                        vehicle?.registrationPlate?.contains(searchText.text, ignoreCase = true) == true
                    }
        }*/

        Scaffold(
            floatingActionButton = {
                FloatingActionButton(
                    onClick = {
                        val intent = Intent(context, ClientForm::class.java)
                        context.startActivity(intent)
                    },
                    modifier = Modifier.padding(16.dp)
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
                HomeTitle(text = "AutoGestion"){}

                Row(
                    horizontalArrangement = Arrangement.Start,
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.Search,
                        contentDescription = null,
                        modifier = Modifier.padding(end = 8.dp)
                    )

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
                            modifier = Modifier.padding(end = 8.dp)
                        )
                    }
                }

                LazyColumn(
                    modifier = Modifier
                        .padding(16.dp)
                        .wrapContentSize()
                ) {
                    // Passez la liste directement à la fonction items
                    items(sortedClients) { client ->
                        ClientVehicleInfo(client, searchText.text)
                        Spacer(modifier = Modifier.height(8.dp))
                    }
                }
            }
        }
    }

    @Composable
    fun ClientVehicleInfo(client: Client, searchText: String) {
        val context = LocalContext.current
        val vehicles by vehicleViewModel.getVehiclesFromClient(client.clientId)
            .observeAsState(initial = emptyList())

        if (searchText.isEmpty() || clientMatchesSearch(client, vehicles, searchText)) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(16.dp))
                    .background(color = Color(0xFFF3EDF7))
                    .padding(15.dp)
                    .clickable {
                        val intent = Intent(context, ClientProfile::class.java).apply {
                            putExtra("clientId", client.clientId)
                        }
                        context.startActivity(intent)
                    }
            ) {
                Text(
                    text = "${client.lastName} ${client.firstName}",
                    modifier = Modifier.padding(bottom = 4.dp),
                    fontSize = 20.sp
                )
                if (vehicles.isEmpty()) {
                    Text(
                        text = "Aucun véhicule",
                        modifier = Modifier.padding(bottom = 4.dp),
                        fontStyle = androidx.compose.ui.text.font.FontStyle.Italic
                    )
                } else {
                    vehicles.joinToString(separator = "\n ") { vehicle ->
                        buildString {
                            append(vehicle?.registrationPlate ?: "")
                            if (!vehicle?.brand.isNullOrEmpty() || !vehicle?.model.isNullOrEmpty()) {
                                append(", ")
                                append(vehicle?.brand ?: "")
                                if (!vehicle?.model.isNullOrEmpty()) {
                                    append(", ")
                                    append(vehicle?.model ?: "")
                                }
                            }
                        }
                    }.let {
                        Text(
                            text = it,
                            modifier = Modifier.padding(bottom = 4.dp)
                        )
                    }
                }
            }
        }
    }

    private fun clientMatchesSearch(client: Client, vehicles: List<Vehicle>, searchText: String): Boolean {
        return client.firstName.contains(searchText, ignoreCase = true) ||
                client.lastName.contains(searchText, ignoreCase = true) ||
                vehicles.any { vehicle ->
                    vehicle.registrationPlate.contains(searchText, ignoreCase = true)
                }
    }

    @Composable
    private fun HomeTitle(text : String,onBackClick: () -> Unit){
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
        Home().HomeApp()
    }


    private fun getClients(): LiveData<List<Client>> {
        return database.clientDao().getAllClients()
    }

}
