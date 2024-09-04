package com.example.autogestion

import android.content.Intent
import android.net.Uri
import android.os.Bundle
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
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.rememberCoroutineScope
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
import com.example.autogestion.data.viewModels.RepairViewModel
import com.example.autogestion.data.viewModels.VehicleViewModel
import com.example.autogestion.form.RepairFormAdd
import com.example.autogestion.form.RepairFormUpdate
import com.example.autogestion.form.VehicleFormUpdate
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Locale

class VehicleProfile : ComponentActivity() {

    private val vehicleViewModel: VehicleViewModel by viewModels()
    private val repairViewModel: RepairViewModel by viewModels()
    private val dateFormat = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        val vehicleId = intent.getIntExtra("vehicleId", 0)

        setContent {
            VehiclePage(vehicleId)
        }
    }

    private fun redirectToHome() {
        val intent = Intent(this, Home::class.java)
        startActivity(intent)
        finish() // Ferme l'activité actuelle pour éviter le retour avec le bouton "Back"
    }

    @Composable
    fun VehiclePage(vehicleId: Int) {
        val context = LocalContext.current
        val vehicle by vehicleViewModel.getVehicleById(vehicleId).observeAsState()
        val repairList by repairViewModel.getRepairsFromVehicle(vehicleId).observeAsState(emptyList())


        val coroutineScope = rememberCoroutineScope()

        Column(modifier = Modifier
            .fillMaxSize()
            .statusBarsPadding()) {

            NavBar(text = "${vehicle?.brand?.ifEmpty {"-"}} ${vehicle?.model?.ifEmpty {"-"}} ${vehicle?.color?.ifEmpty {"-"}}") {
                val intent = Intent(context, ClientProfile::class.java).apply {
                    putExtra("clientId", vehicle?.clientId)
                }
                context.startActivity(intent)
            }

            vehicle?.let { currentVehicle ->
            Row(
                modifier = Modifier
                    .fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Car information
                Column(modifier = Modifier.padding(16.dp).width(250.dp)) {
                    Row(
                        modifier = Modifier.padding(bottom = 4.dp)
                    ) {
                        Text(
                            text = "Marque : ",
                            fontWeight = FontWeight.Bold
                        )
                        Spacer(modifier = Modifier.width(39.dp))
                        vehicle?.brand?.ifEmpty {"-"}?.let {
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
                        Spacer(modifier = Modifier.width(40.dp))
                        vehicle?.model?.ifEmpty {"-"}?.let {
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
                        Spacer(modifier = Modifier.width(38.dp))
                        vehicle?.color?.ifEmpty {"-"}?.let {
                            Text(
                                text = it
                            )
                        }
                    }
                    Row(
                        modifier = Modifier.padding(bottom = 4.dp)
                    ) {
                        Text(
                            text = "N° de chassis : ",
                            fontWeight = FontWeight.Bold
                        )
                        Spacer(modifier = Modifier.width(2.dp))
                        vehicle?.chassisNum?.ifEmpty {"-"}?.let {
                            Text(
                                text = it
                            )
                        }
                    }
                    Row(
                        modifier = Modifier.padding(bottom = 4.dp)
                    ) {
                        Text(
                            text = "Plaque: ",
                            fontWeight = FontWeight.Bold
                        )
                        Spacer(modifier = Modifier.width(48.dp))
                        vehicle?.registrationPlate?.let {
                            Text(
                                text = it
                            )
                        }
                    }

                    Button(
                        onClick = {
                            vehicle?.greyCard?.let { uriString ->
                                try {
                                    val uri = Uri.parse(uriString)
                                    val intent = Intent(Intent.ACTION_VIEW).apply {
                                        setDataAndType(uri, "image/*")
                                        addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
                                    }
                                    context.startActivity(intent)
                                } catch (e: Exception) {
                                    e.printStackTrace()
                                }
                            }
                        },
                        modifier = Modifier.padding(bottom = 4.dp)
                    ) {
                        Text(text = "Voir Carte Grise")
                    }

                }

                Row {
                    IconButton(onClick = {
                        coroutineScope.launch {
                            val clientId = vehicle!!.clientId
                            vehicleViewModel.deleteVehicle(vehicle!!)
                            val intent = Intent(context, ClientProfile::class.java).apply {
                                putExtra("clientId", clientId)
                            }
                            context.startActivity(intent)
                        }
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

            // Header add button + text
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
            LazyColumn(modifier = Modifier
                .padding(16.dp)
                .wrapContentSize()) {

                items(repairList.size) { index ->
                    repairList[index]?.let { RepairItem(it) }
                    if (index < repairList.size - 1) {

                        Spacer(modifier = Modifier.height(8.dp))
                    }
                }
            }
            } ?: run {
                Text(
                    text = "Véhicule non trouvé",
                    modifier = Modifier.padding(16.dp),
                    color = Color.Red
                )
            }
        }
    }

    // One Car
    @Composable
    fun RepairItem(repair: Repair) {
        val coroutineScope = rememberCoroutineScope()
        val context = LocalContext.current

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(16.dp))
                .background(color = Color(0xFFF3EDF7))
                .padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ){

            Column(modifier = Modifier.width(250.dp)) {
                Row(
                    modifier = Modifier.padding(bottom = 4.dp)
                ) {
                    Text(
                        text = "Description : ",
                        fontWeight = FontWeight.Bold
                    )
                    Spacer(modifier = Modifier.width(28.dp))
                    repair.description?.ifEmpty { "-" }?.let {
                        Text(
                            text = it
                        )
                    }
                }
                Row(
                    modifier = Modifier.padding(bottom = 4.dp)
                ) {
                    Text(
                        text = "Date réparation : ",
                        fontWeight = FontWeight.Bold
                    )
                    Spacer(modifier = Modifier.width(2.dp))
                    Text(
                        text =  when(repair.date){
                            null -> "-"
                            else -> dateFormat.format(repair.date)
                        }
                    )
                }
                Row(
                    modifier = Modifier.padding(bottom = 4.dp)
                ) {
                    Text(
                        text =  "Facture: ",
                        fontWeight = FontWeight.Bold
                    )
                    Spacer(modifier = Modifier.width(55.dp))
                    Text(
                        text = when (repair.paid) {
                            true -> "Payée"
                            false -> "Pas payée"
                            else -> "-"
                        }
                    )
                }
            }

            Row {
                IconButton(onClick = {
                    coroutineScope.launch {
                        val vehicleId = repair.vehicleId
                        repairViewModel.deleteRepair(repair!!)
                        val intent = Intent(context, VehicleProfile::class.java).apply {
                            putExtra("vehicleId", vehicleId)
                        }
                        context.startActivity(intent)
                    }
                }) {
                    Icon(
                        painter = painterResource(id = R.drawable.baseline_delete_24),
                        contentDescription = "Supprimer",
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
                        contentDescription = "Modifier",
                        tint = Color.Black
                    )
                }
            }

        }

    }
}