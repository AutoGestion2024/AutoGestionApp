package com.example.autogestion.ui.profiles

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
import androidx.compose.material3.AlertDialog
import androidx.compose.material.Button
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.autogestion.R
import com.example.autogestion.ui.utils.DateUtils
import com.example.autogestion.ui.utils.NavigationUtils.navigateToClientFormUpdate
import com.example.autogestion.ui.utils.NavigationUtils.navigateToHome
import com.example.autogestion.ui.utils.NavigationUtils.navigateToVehicleFormAdd
import com.example.autogestion.ui.utils.NavigationUtils.navigateToVehicleProfile
import com.example.autogestion.data.Client
import com.example.autogestion.data.Vehicle
import com.example.autogestion.data.viewModels.ClientViewModel
import com.example.autogestion.data.viewModels.VehicleViewModel
import com.example.autogestion.ui.components.SharedComposables.DeleteButton
import com.example.autogestion.ui.components.SharedComposables.NavBar
import com.example.autogestion.ui.components.SharedComposables.DisplayEntityInfoRow
import com.example.autogestion.ui.components.SharedComposables.ModifyButton
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

        // State to control visibility of a dialog
        var showDialog by remember { mutableStateOf(false) }

        val client by clientViewModel.getClientById(clientId).observeAsState()
        val vehicleList by vehicleViewModel.getVehiclesFromClient(clientId).observeAsState(emptyList())

        Column(modifier = Modifier
            .fillMaxSize()
            .statusBarsPadding()) {

            // Display client name and redirect to Home on GoBack button
            NavBar(text = clientProfileTitle(client)) {
                navigateToHome(context)
            }

            // Display clients info
            client?.let { currentClient ->
                Row(
                    modifier = Modifier
                        .fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column(modifier = Modifier
                        .padding(16.dp)
                        .width(250.dp)) {

                        // Formatting client's birthday for display
                        val formattedDate = currentClient.birthDate?.let {
                            DateUtils.dateFormat.format(it)
                        } ?: "-"

                        // Display clients information
                        DisplayEntityInfoRow("N° de téléphone : ", currentClient.phone, 16)
                        DisplayEntityInfoRow("Date de naissance : ", formattedDate, 2)
                        DisplayEntityInfoRow("Email : ", currentClient.email, 82)
                        DisplayEntityInfoRow("Adresse : ", currentClient.address, 65)
                    }

                    // Row for delete and modify buttons.

                    Row {
                        // Button to trigger a deletion confirmation dialog.
                        DeleteButton {
                            showDialog = true
                        }

                        // Button to navigate to a form for modifying the client's details.
                        ModifyButton {
                            navigateToClientFormUpdate(context, currentClient.clientId)
                        }
                    }
                }

                // Row for displaying the title "Voitures" and an add button.
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
                        navigateToVehicleFormAdd(context, currentClient.clientId)
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

                // Display list of vehicles
                DisplayVehicleList(vehicleList)
            } ?: run {
                // Text displayed if no client is found.
                Text(
                    text = "Client non trouvé",
                    modifier = Modifier.padding(16.dp),
                    color = Color.Red
                )
            }
        }

        // AlertDialog for confirming the deletion of a client.
        if (showDialog) {
            AlertDialog(
                onDismissRequest = { showDialog = false },
                title = { Text(text = "Confirmation de suppression") },
                text = { Text(text = "Êtes-vous sûr de vouloir supprimer ce client ? Cette action est irréversible.") },
                confirmButton = {
                    Button(
                        onClick = {
                            coroutineScope.launch {
                                client?.let { currentClient ->
                                    clientViewModel.deleteClient(currentClient)
                                    navigateToHome(context)
                                }
                            }
                            showDialog = false
                        }
                    ) {
                        Text("Supprimer")
                    }
                },
                dismissButton = {
                    Button(onClick = { showDialog = false }) {
                        Text("Annuler")
                    }
                }
            )
        }

    }

    private fun clientProfileTitle(client: Client?): String {
        return if (client != null) {
            "${client.lastName} ${client.firstName}"
        } else {
            "Client Unknown"
        }
    }

    @Composable
    fun DisplayVehicleList(vehicleList: List<Vehicle?>) {
        LazyColumn(
            modifier = Modifier
                .padding(16.dp)
                .wrapContentSize()
        ) {
            items(vehicleList.size) { index ->
                vehicleList[index]?.let { DisplayVehicleItem(it) }
                if (index < vehicleList.size - 1) {
                    Spacer(modifier = Modifier.height(8.dp))
                }
            }
        }
    }

    @Composable
    fun DisplayVehicleItem(vehicle: Vehicle) {
        val context = LocalContext.current

        Column(modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(16.dp))
            .background(color = Color(0xFFF3EDF7))
            .padding(16.dp)
            .clickable {
                navigateToVehicleProfile(context, vehicle.vehicleId)
            }
        ) {
            DisplayEntityInfoRow("Marque : ", vehicle.brand, 20)
            DisplayEntityInfoRow(rowContentName = "Modèle : ", vehicle.model, 22)
            DisplayEntityInfoRow("Couleur : ", vehicle.color, 20)
            DisplayEntityInfoRow(rowContentName = "N° chassis : ", vehicle.chassisNum , 2)
            DisplayEntityInfoRow("Plaque : ", vehicle.registrationPlate, 25)
        }
    }

}
