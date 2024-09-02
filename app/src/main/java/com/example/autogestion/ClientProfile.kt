package com.example.autogestion

import android.content.Intent
import android.os.Bundle
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

class ClientProfile : ComponentActivity(){

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

        val client by clientViewModel.getClientById(clientId).observeAsState()
        val vehicleList by remember { mutableStateOf(vehicleViewModel.getVehiclesFromClient(clientId)) }

        Column(modifier = Modifier.fillMaxSize().statusBarsPadding()) {
            NavBar(text = "Profile client : id $clientId") {
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

                    // Client information
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text(
                            text = "${currentClient.firstName} ${currentClient.lastName}",
                            modifier = Modifier.padding(bottom = 8.dp)
                        )
                        // TODO: Add birthdate
                        Text(text = "${currentClient.phone}", modifier = Modifier.padding(bottom = 8.dp))
                        Text(text = "${currentClient.email}", modifier = Modifier.padding(bottom = 8.dp))
                        Text(text = "${currentClient.address}")
                    }

                    Row {
                        IconButton(onClick = { println("TODO") /* TODO: Bouton Supprimer */ }) {
                            Icon(
                                painter = painterResource(id = R.drawable.baseline_delete_24),
                                contentDescription = "Supprimer",
                                tint = Color.Black
                            )
                        }

                        IconButton(onClick = { println("TODO") /* TODO: Bouton Modifier */ }) {
                            Icon(
                                painter = painterResource(id = R.drawable.baseline_edit_24),
                                contentDescription = "Modifier",
                                tint = Color.Black
                            )
                        }
                    }
                }

                // Header add button + text
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(36.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        text = "Liste Voitures",
                        fontSize = 15.sp,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(horizontal = 16.dp)
                    )

                    IconButton(onClick = { println("TODO") /* TODO: bouton + cote Liste Voiture */ }) {
                        Icon(
                            painter = painterResource(id = R.drawable.baseline_add_24),
                            contentDescription = "Ajouter",
                            tint = Color.Black
                        )
                    }
                }

                // Divide profile and car list
                Divider(
                    color = Color.Gray, // Couleur de la ligne
                    thickness = 2.dp   // Épaisseur de la ligne
                )

                // Car List
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


    // One Car
    @Composable
    fun VoitureItem(vehicle: Vehicle) {
        val context = LocalContext.current

        Column(modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(16.dp))
            .background(color = Color(0xFFF3EDF7))
            .padding(16.dp)
            .clickable {
                val intent = Intent(context, VehicleProfile::class.java).apply{
                    putExtra("vehicleId", vehicle.vehicleId)
                }
                context.startActivity(intent)
                /* TODO Ajouter les parametres */
            }
        ) {
            Text(text = "Plaque: ${vehicle.registrationPlate}", modifier = Modifier.padding(bottom = 4.dp))
            Text(text = "Marque: ${vehicle.brand}", modifier = Modifier.padding(bottom = 4.dp))
            Text(text = "Modèle: ${vehicle.model}", modifier = Modifier.padding(bottom = 4.dp))
        }
    }

}
