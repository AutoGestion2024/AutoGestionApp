package com.example.autogestion

import android.content.Intent
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
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.TabRowDefaults.Divider
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import com.example.autogestion.data.Vehicle
import com.example.autogestion.data.Repair
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.autogestion.data.viewModels.ClientViewModel
import com.example.autogestion.data.viewModels.RepairViewModel
import com.example.autogestion.data.viewModels.VehicleViewModel

class VehicleProfile : ComponentActivity(){

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
    fun VehiclePage(vehicleId: Int){
        val context = LocalContext.current
        var vehicle = vehicleViewModel.getVehicleById(vehicleId).value
        var repairList = repairViewModel.getRepairsFromVehicle(vehicleId)

        Column(modifier = Modifier.fillMaxSize().statusBarsPadding()) {

            NavBar(text = "Profile Voiture") {
                val intent = Intent(context, ClientProfile::class.java).apply{
                    putExtra("clientId", vehicle?.clientId)
                }
                context.startActivity(intent)
            }

            Row(
                modifier = Modifier
                    .fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Car information
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(text = "${vehicle?.brand}, ${vehicle?.model}", modifier = Modifier.padding(bottom = 4.dp))
                    Text(text = "Plaque: ${vehicle?.registrationPlate}", modifier = Modifier.padding(bottom = 4.dp))
                    // TODO add link to grey card
                    Text(text = "Carte grise: ${vehicle?.greyCard}", modifier = Modifier.padding(bottom = 4.dp))
                }

                Row {

                    IconButton(onClick = { println("TODO") /* TODO Bouton Supprimer */ }) {
                        Icon(
                            painter = painterResource(id = R.drawable.baseline_delete_24),
                            contentDescription = "Supprimer",
                            tint = Color.Black
                        )
                    }

                    IconButton(onClick = { println("TODO") /* TODO Bouton Modifier */ }) {
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
                    text = "Liste Pannes",
                    fontSize = 15.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(horizontal = 16.dp)
                )
                // TODO
                IconButton(onClick = { println("TODO") })
                {
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
                thickness = 2.dp,   // Épaisseur de la ligne
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
        }
    }

    // One Car
    @Composable
    fun RepairItem(repair: Repair) {
        Column(modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(16.dp))
            .background(color = Color(0xFFF3EDF7))
            .padding(16.dp)) {
            Text(text = "${repair.description}", modifier = Modifier.padding(bottom = 4.dp))
            Text(text = "${repair.date}", modifier = Modifier.padding(bottom = 4.dp))
        }
    }



}
