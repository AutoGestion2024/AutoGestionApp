package com.example.autogestion.ui.profiles

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Button
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.TabRowDefaults.Divider
import androidx.compose.material.Text
import androidx.compose.material3.AlertDialog
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import com.example.autogestion.data.Repair
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.FileProvider
import com.example.autogestion.R
import com.example.autogestion.ui.components.SharedComposables.NavBar
import com.example.autogestion.ui.components.SharedComposables.DisplayEntityInfoRow
import com.example.autogestion.ui.utils.DateUtils
import com.example.autogestion.ui.utils.NavigationUtils.navigateToClientProfile
import com.example.autogestion.data.Vehicle
import com.example.autogestion.data.viewModels.RepairViewModel
import com.example.autogestion.data.viewModels.VehicleViewModel
import com.example.autogestion.ui.forms.RepairFormAdd
import com.example.autogestion.ui.forms.RepairFormUpdate
import com.example.autogestion.ui.forms.VehicleFormUpdate
import kotlinx.coroutines.launch
import java.io.File

class VehicleProfile : ComponentActivity() {

    private val vehicleViewModel: VehicleViewModel by viewModels()
    private val repairViewModel: RepairViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        val vehicleId = intent.getIntExtra("vehicleId", 0)

        setContent {
            VehiclePage(vehicleId)
        }
    }

    @Composable
    fun VehiclePage(vehicleId: Int) {
        val context = LocalContext.current
        val vehicle by vehicleViewModel.getVehicleById(vehicleId).observeAsState()
        val repairList by repairViewModel.getRepairsFromVehicle(vehicleId).observeAsState(emptyList())

        var showDialogVehicle by remember { mutableStateOf(false) }


        val coroutineScope = rememberCoroutineScope()

        Column(modifier = Modifier
            .fillMaxSize()
            .statusBarsPadding()) {

            if (showDialogVehicle) {
                AlertDialog(
                    onDismissRequest = { showDialogVehicle = false },
                    title = {
                        Text(text = "Confirmation de suppression")
                    },
                    text = {
                        Text("Êtes-vous sûr de vouloir supprimer ce véhicule ?")
                    },
                    confirmButton = {
                        Button(onClick = {
                            coroutineScope.launch {
                                val clientId = vehicle!!.clientId
                                vehicleViewModel.deleteVehicle(vehicle!!)
                                val intent = Intent(context, ClientProfile::class.java).apply {
                                    putExtra("clientId", clientId)
                                }
                                context.startActivity(intent)
                            }
                        }) {
                            Text("Supprimer")
                        }
                    },
                    dismissButton = {
                        Button(onClick = {
                            showDialogVehicle = false
                        }) {
                            Text("Annuler")
                        }
                    }
                )
            }

            NavBar(text = vehicleProfileTitle(vehicle)) {
                vehicle?.let { navigateToClientProfile(context, it.clientId) }
            }

            // Display vehicle information
            vehicle?.let { currentVehicle ->
            Row(
                modifier = Modifier
                    .fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column(modifier = Modifier
                    .padding(16.dp)
                    .width(250.dp)) {
                    DisplayEntityInfoRow("Marque : ", vehicle?.brand, 39)
                    DisplayEntityInfoRow("Modèle : ", vehicle?.model, 40)
                    DisplayEntityInfoRow("Couleur : ", vehicle?.color, 38)
                    DisplayEntityInfoRow("N° de chassis : ", vehicle?.chassisNum, 2)
                    DisplayEntityInfoRow("Plaque: ", vehicle?.registrationPlate, 48)

                    Button(
                        onClick = {
                            vehicle?.let { currentVehicle ->
                                val filePath = currentVehicle.greyCard
                                filePath?.let { openFile(it) }
                            }

                        }
                    ) {
                        Text(text = "Voir Carte Grise")
                    }

                }

                // Editing and deleting buttons
                Row {
                    IconButton(onClick = {
                        showDialogVehicle = true


                    }) {
                        Icon(
                            painter = painterResource(id = R.drawable.baseline_delete_24),
                            contentDescription = "Supprimer",
                            tint = Color.Black
                        )
                    }

                    IconButton(onClick = {
                        val intent = Intent(context, VehicleFormUpdate::class.java).apply {
                            putExtra("vehicleId", vehicle!!.vehicleId)
                        }
                        context.startActivity(intent)
                    }) {
                        Icon(
                            painter = painterResource(id = R.drawable.baseline_edit_24),
                            contentDescription = "Modifier",
                            tint = Color.Black
                        )
                    }
                }
            }

            // Header Repair list and add button
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(36.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = "Réparations",
                    fontSize = 15.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(horizontal = 16.dp)
                )
                IconButton(onClick = {
                    val intent = Intent(context, RepairFormAdd::class.java).apply {
                        putExtra("vehicleId", currentVehicle.vehicleId)
                    }
                    context.startActivity(intent)
                }) {
                    Icon(
                        painter = painterResource(id = R.drawable.baseline_add_24),
                        contentDescription = "Ajouter",
                        tint = Color.Black
                    )
                }
            }

            // Divide profile and car list
            Divider(
                color = Color.Gray,
                thickness = 2.dp,
            )

            // Repair List
           DisplayRepairList(repairList)

            } ?: run {
                // Text displayed if vehicle is not found
                Text(
                    text = "Véhicule non trouvé",
                    modifier = Modifier.padding(16.dp),
                    color = Color.Red
                )
            }
        }
    }

    @Composable
    fun DisplayRepairList(repairList: List<Repair?>) {
        LazyColumn(modifier = Modifier
            .padding(16.dp)
            .wrapContentSize()) {

            items(repairList.size) { index ->
                repairList[index]?.let { DisplayRepairItem(it) }
                if (index < repairList.size - 1) {
                    Spacer(modifier = Modifier.height(8.dp))
                }
            }
        }
    }

    // One Car
    @Composable
    fun DisplayRepairItem(repair: Repair) {
        val coroutineScope = rememberCoroutineScope()
        val context = LocalContext.current

        var showDialogRepair by remember { mutableStateOf(false) }

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(16.dp))
                .background(color = Color(0xFFF3EDF7))
                .padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ){

            Column(modifier = Modifier.width(200.dp)) {

                // Formatting repair date and invoice status for display
                val formattedDate = repair.date?.let{
                    DateUtils.dateFormat.format(it)
                } ?: "-"

                val invoiceStatus = when (repair.paid) {
                    true -> "Payée"
                    false -> "Pas payée"
                    else -> "-"
                }

                // Display repair information
                DisplayEntityInfoRow("Description : ", repair.description, 28)
                DisplayEntityInfoRow("Date réparation : ", formattedDate, 2)
                DisplayEntityInfoRow("Facture: ", invoiceStatus, 55)
            }

            // Row for delete and modify buttons.
            Row {
                IconButton(onClick = {
                    showDialogRepair = true
                }) {
                    Icon(
                        painter = painterResource(id = R.drawable.baseline_delete_24),
                        contentDescription = "Delete",
                        tint = Color.Black
                    )
                }
                IconButton(onClick = {
                    val intent = Intent(context, RepairFormUpdate::class.java).apply {
                        putExtra("repairId", repair!!.repairId)
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

            IconButton(onClick = {
                repair.let { currentRepair ->
                    val filePath = currentRepair.invoice
                    filePath?.let { openFile(it) }
                }
            }) {
                Icon(
                    painter = painterResource(id = R.drawable.baseline_attach_file_24),
                    contentDescription = "Fichier Facture Joint",
                    tint = Color.Black
                )
            }


        }

        if (showDialogRepair) {
            AlertDialog(
                onDismissRequest = { showDialogRepair = false },
                title = {
                    Text(text = "Confirmation de suppression")
                },
                text = {
                    Text("Êtes-vous sûr de vouloir supprimer cette réparation ?")
                },
                confirmButton = {
                    Button(onClick = {
                    coroutineScope.launch {
                        val vehicleId = repair.vehicleId
                        repairViewModel.deleteRepair(repair!!)
                        val intent = Intent(context, VehicleProfile::class.java).apply {
                            putExtra("vehicleId", vehicleId)
                        }
                        context.startActivity(intent)
                    }
                    }) {
                        Text("Supprimer")
                    }
                },
                dismissButton = {
                    Button(onClick = {
                        showDialogRepair = false
                    }) {
                        Text("Annuler")
                    }
                }
            )
        }


    }

    fun vehicleProfileTitle(vehicle: Vehicle?): String {
        if (vehicle == null) {
            return "Unavailable"
        }
        return listOfNotNull(
            vehicle.brand?.ifEmpty { "-" },
            vehicle.model?.ifEmpty { "-" },
            vehicle.color?.ifEmpty { "-" }
        ).joinToString(" ")
    }

    private fun openFile(filePath: String) {
        // Create a File object with the given file path
        val file = File(filesDir, filePath)
        Log.d("VehicleProfile", "File path: ${file.absolutePath}")

        if (file.exists()) {
            // Obtain the URI for the file using FileProvider
            val uri = FileProvider.getUriForFile(this, "com.example.autogestion.fileprovider", file)

            // Determine the MIME type of the file
            val mimeType = when {
                filePath.endsWith(".jpg", ignoreCase = true) || filePath.endsWith(".jpeg", ignoreCase = true) -> "image/jpeg"
                filePath.endsWith(".png", ignoreCase = true) -> "image/png"
                filePath.endsWith(".pdf", ignoreCase = true) -> "application/pdf"
                else -> "*/*" // For other types of files
            }

            // Create an Intent to open the file
            val intent = Intent(Intent.ACTION_VIEW).apply {
                setDataAndType(uri, mimeType)
                flags = Intent.FLAG_GRANT_READ_URI_PERMISSION
            }

            // Start the activity to display the file
            startActivity(intent)
        } else {
            // Show an error message if the file does not exist
            Toast.makeText(this, "The file does not exist", Toast.LENGTH_SHORT).show()
        }
    }
}