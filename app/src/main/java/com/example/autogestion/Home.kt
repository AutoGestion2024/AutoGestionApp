package com.example.autogestion

import android.content.Context
import android.content.Intent
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
import androidx.compose.foundation.lazy.items
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
import androidx.compose.runtime.mutableStateMapOf
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
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.autogestion.Utils.NavigationUtils.navigateToClientProfile
import com.example.autogestion.data.AppDatabase
import com.example.autogestion.data.Client
import com.example.autogestion.data.Vehicle
import com.example.autogestion.data.viewModels.ClientViewModel
import com.example.autogestion.data.viewModels.VehicleViewModel
import com.example.autogestion.form.ClientForm


class Home : ComponentActivity() {

    // Create an instance of the database
    private lateinit var database: AppDatabase

    private val clientViewModel: ClientViewModel by viewModels()
    private val vehicleViewModel: VehicleViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        database = AppDatabase.getDatabase(this)
        Log.d("AppDatabase", "Database instance: ${database.isOpen}")

        if (!Camera.hasRequiredPermissions(this)) {
            ActivityCompat.requestPermissions(this, Camera.CAMERAX_PERMISSIONS, 0)
        }

        val textSearch = intent.getStringExtra("search_text") ?: ""

        enableEdgeToEdge()
        setContent {
            HomeApp(textSearch)
        }
    }

    @Composable
    fun HomeApp(textSearch: String) {
        val context = LocalContext.current
        val clients by clientViewModel.getAllClients().observeAsState(initial = emptyList())
        var searchText by remember {
            mutableStateOf(
                if (textSearch.isNotEmpty()) TextFieldValue(
                    textSearch
                ) else TextFieldValue("")
            )
        }

        // Sorting clients and caching their vehicles for display
        val sortedClients = clients.sortedBy { it.lastName }
        val vehicleCache = cacheVehiclesForClients(sortedClients = sortedClients, vehicleViewModel = vehicleViewModel)

        // Client filtering based on search text
        val filteredClients = filterClients(sortedClients, searchText.text,vehicleCache)

        Scaffold(
            floatingActionButton = {
                FloatingActionButton(
                    onClick = {
                        // Add new client
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
                HomeTitle(text = "AutoGestion") {}

                // Search bar
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

                // Client and vehicles display
                LazyColumn(
                    modifier = Modifier
                        .padding(16.dp)
                        .wrapContentSize()
                ) {
                    items(filteredClients) { client ->
                        ClientVehicleInfo(client, searchText.text)
                        Spacer(modifier = Modifier.height(8.dp))
                    }
                }
            }
        }
    }

    @Composable
    fun ClientVehicleInfo(
        client: Client,
        searchText: String,
        vehicleViewModel: VehicleViewModel = viewModel()
    ) {
        val context = LocalContext.current
        val vehicles by vehicleViewModel.getVehiclesFromClient(client.clientId)
            .observeAsState(initial = emptyList())


        Column(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(16.dp))
                .background(color = Color(0xFFF3EDF7))
                .padding(15.dp)
                .clickable {
                    navigateToClientProfile(context, client.clientId)
                }
        ) {
            DisplayClientName(client)
            DisplayVehicleListInfo(vehicles)
        }

    }

    @Composable
    fun DisplayClientName(client: Client) {
        Text(
            text = "${client.lastName} ${client.firstName}",
            modifier = Modifier.padding(bottom = 4.dp),
            fontSize = 20.sp
        )
    }

    @Composable
    fun DisplayVehicleListInfo(vehicles: List<Vehicle?>) {
        if (vehicles.isEmpty()) {
            DisplayNoVehiclesText()
        } else {
            vehicles.forEach { vehicle ->
                if (vehicle != null) {
                    DisplayVehicleInfo(vehicle)
                }
            }
        }
    }

    @Composable
    fun DisplayNoVehiclesText() {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Text(
                text = "Aucun véhicule",
                modifier = Modifier.padding(bottom = 4.dp),
                fontStyle = androidx.compose.ui.text.font.FontStyle.Italic
            )
        }
    }

    @Composable
    fun DisplayVehicleInfo(vehicle: Vehicle) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Text(
                text = vehicleDetailsToString(vehicle),
                modifier = Modifier.padding(start = 0.dp)
            )
        }
    }

    private fun clientMatchesSearch(
        client: Client,
        vehicles: List<Vehicle>,
        searchText: String
    ): Boolean {
        return client.firstName.contains(searchText, ignoreCase = true) ||
                client.lastName.contains(searchText, ignoreCase = true) ||
                vehicles.any { vehicle ->
                    vehicle.registrationPlate.contains(searchText, ignoreCase = true)
                }
    }

    fun filterClients(
        sortedClients: List<Client>,
        searchText: String,
        vehicleCache: Map<Int, List<Vehicle>>
    ): List<Client> {
        return if (searchText.isEmpty()) {
            sortedClients
        } else {
            sortedClients.filter { client ->
                val vehicles = vehicleCache[client.clientId] ?: emptyList()
                clientMatchesSearch(client, vehicles, searchText)
            }
        }
    }

    @Composable
    fun cacheVehiclesForClients(
        sortedClients: List<Client>,
        vehicleViewModel: VehicleViewModel
    ): MutableMap<Int, List<Vehicle>> {
        val vehicleCache = remember { mutableStateMapOf<Int, List<Vehicle>>() }

        sortedClients.forEach { client ->
            val vehicles by vehicleViewModel.getVehiclesFromClient(client.clientId)
                .observeAsState(initial = emptyList())
            vehicleCache[client.clientId] = vehicles
        }

        return vehicleCache
    }

    fun vehicleDetailsToString(vehicle: Vehicle): String {
        return listOfNotNull(
                vehicle.registrationPlate,
                vehicle.brand,
                vehicle.model
            ).joinToString(", ")
    }

    @Composable
    fun HomeTitle(text: String, onBackClick: () -> Unit) {
        // Up Bar
        Row(
            modifier = Modifier
                .height(56.dp)
                .fillMaxWidth()
                .background(Color(0xFFF3EDF7)),
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
