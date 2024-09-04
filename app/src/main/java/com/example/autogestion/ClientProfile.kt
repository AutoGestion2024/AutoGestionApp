package com.example.autogestion

import android.content.Intent
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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.TabRowDefaults.Divider
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.autogestion.data.Vehicle
import com.example.autogestion.data.viewModels.ClientViewModel
import com.example.autogestion.data.viewModels.RepairViewModel
import com.example.autogestion.data.viewModels.VehicleViewModel
import com.example.autogestion.form.ClientFormUpdate
import com.example.autogestion.form.VehicleFormAdd
import kotlinx.coroutines.launch

class ClientProfile : ComponentActivity() {

    private val clientViewModel: ClientViewModel by viewModels()
    private val vehicleViewModel: VehicleViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        val clientId = intent.getIntExtra("clientId", 0)

        setContent {
            ProfilPage(clientId)
        }
    }

    @Composable
    fun ProfilPage(clientId: Int) {
        val context = LocalContext.current
        val coroutineScope = rememberCoroutineScope()

        val client by clientViewModel.getClientById(clientId).observeAsState()
        val vehicleList by vehicleViewModel.getVehiclesFromClient(clientId).observeAsState(emptyList())

        Column(modifier = Modifier
            .fillMaxSize()
            .statusBarsPadding()) {

            NavBar(text = "${client?.lastName} ${client?.firstName}") {
                val intent = Intent(context, Home::class.java)
                context.startActivity(intent)
            }

            client?.let { currentClient ->
                Row(
                    modifier = Modifier
                        .fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column(modifier = Modifier.padding(16.dp).width(250.dp)) {
                        Row(
                            modifier = Modifier.padding(bottom = 4.dp)
                        ) {
                            Text(
                                text = "N° de téléphone : ",
                                fontWeight = FontWeight.Bold
                            )
                            Spacer(modifier = Modifier.width(2.dp))
                            currentClient.phone.let {
                                Text(
                                    text = it
                                )
                            }
                        }
                        Row(
                            modifier = Modifier.padding(bottom = 4.dp)
                        ) {
                            Text(
                                text = "Email : ",
                                fontWeight = FontWeight.Bold
                            )
                            Spacer(modifier = Modifier.width(67.dp))
                            currentClient.email?.ifEmpty {"-"}?.let {
                                Text(
                                    text = it
                                )
                            }
                        }
                        Row(
                            modifier = Modifier.padding(bottom = 4.dp)
                        ) {
                            Text(
                                text = "Adresse : ",
                                fontWeight = FontWeight.Bold
                            )
                            Spacer(modifier = Modifier.width(50.dp))
                            currentClient.address?.ifEmpty {"-"}?.let {
                                Text(
                                    text = it
                                )
                            }
                        }
                    }

                    Row {
                        IconButton(onClick = {
                            coroutineScope.launch {
                                clientViewModel.deleteClient(currentClient)
                                redirectToHome()
                            }
                        }) {
                            Icon(
                                painter = painterResource(id = R.drawable.baseline_delete_24),
                                contentDescription = "Delete",
                                tint = Color.Black
                            )
                        }

                        IconButton(onClick = {
                            val intent = Intent(context, ClientFormUpdate::class.java).apply {
                                putExtra("clientId", currentClient.clientId)
                            }
                            context.startActivity(intent)
                        }) {
                            Icon(
                                painter = painterResource(id = R.drawable.baseline_edit_24),
                                contentDescription = "Modify",
                                tint = Color.Black
                            )
                        }
                    }
                }

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(36.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        text = "Voitures",
                        fontSize = 15.sp,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(horizontal = 16.dp)
                    )

                    IconButton(onClick = {
                        val intent = Intent(context, VehicleFormAdd::class.java).apply {
                            putExtra("clientId", currentClient.clientId)
                        }
                        context.startActivity(intent)
                    }) {
                        Icon(
                            painter = painterResource(id = R.drawable.baseline_add_24),
                            contentDescription = "Add",
                            tint = Color.Black
                        )
                    }
                }

                Divider(
                    color = Color.Gray,
                    thickness = 2.dp
                )

                LazyColumn(
                    modifier = Modifier
                        .padding(16.dp)
                        .wrapContentSize()
                ) {
                    items(vehicleList.size) { index ->
                        vehicleList[index]?.let { VoitureItem(it) }
                        if (index < vehicleList.size - 1) {
                            Spacer(modifier = Modifier.height(8.dp))
                        }
                    }
                }
            } ?: run {
                Text(
                    text = "Client non trouvé",
                    modifier = Modifier.padding(16.dp),
                    color = Color.Red
                )
            }
        }
    }

    private fun redirectToHome() {
        val intent = Intent(this, Home::class.java)
        startActivity(intent)
        finish()
    }

    @Composable
    fun VoitureItem(vehicle: Vehicle) {
        val context = LocalContext.current

        Column(modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(16.dp))
            .background(color = Color(0xFFF3EDF7))
            .padding(16.dp)
            .clickable {
                val intent = Intent(context, VehicleProfile::class.java).apply {
                    putExtra("vehicleId", vehicle.vehicleId)
                }
                context.startActivity(intent)
            }
        ) {
            Row(
                modifier = Modifier.padding(bottom = 4.dp)
            ) {
                Text(
                    text = "Marque : ",
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.width(20.dp))
                vehicle.brand?.ifEmpty {"-"}?.let {
                    Text(
                        text = it
                    )
                }
            }
            Row(
                modifier = Modifier.padding(bottom = 4.dp)
            ) {
                Text(
                    text = "Modèle : ",
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.width(20.dp))
                vehicle.model?.ifEmpty {"-"}?.let {
                    Text(
                        text = it
                    )
                }
            }
            Row(
                modifier = Modifier.padding(bottom = 4.dp)
            ) {
                Text(
                    text = "Couleur : ",
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.width(20.dp))
                vehicle.color?.ifEmpty {"-"}?.let {
                    Text(
                        text = it
                    )
                }
            }
            Row(
                modifier = Modifier.padding(bottom = 4.dp)
            ) {
                Text(
                    text = "N° chassis : ",
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.width(2.dp))
                vehicle.chassisNum?.ifEmpty {"-"}?.let {
                    Text(
                        text = it
                    )
                }
            }
            Row(
                modifier = Modifier.padding(bottom = 4.dp)
            ) {
                Text(
                    text = "Plaque : ",
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.width(25.dp))
                Text(
                    text = vehicle.registrationPlate
                )
            }
        }
    }
}
