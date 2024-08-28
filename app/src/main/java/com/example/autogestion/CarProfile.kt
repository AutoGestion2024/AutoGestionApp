package com.example.autogestion

import androidx.compose.foundation.background
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
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.TabRowDefaults.Divider
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import com.example.autogestion.data.Car
import com.example.autogestion.data.Repair
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun Carpage(car: Car, repairList: List<Repair>, onBackClick: ()-> Unit){
    Column(modifier = Modifier.fillMaxSize()) {
        NavBar(text = "Profile Voiture") {
            
        }

        Row(
            modifier = Modifier
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Car information
            Column(modifier = Modifier.padding(16.dp)) {
                Text(text = "${car.make}, ${car.model}", modifier = Modifier.padding(bottom = 4.dp))
                Text(text = "Plaque: ${car.plateNumber}", modifier = Modifier.padding(bottom = 4.dp))
                Text(text = "Carte grise")
            }

            Row {
                // TODO
                IconButton(onClick = { println("TODO") }) {
                    Icon(
                        painter = painterResource(id = R.drawable.baseline_delete_24), // Remplacez par votre icône de poubelle
                        contentDescription = "Supprimer",
                        tint = Color.Black // Couleur de l'icône
                    )
                }

                // TODO
                IconButton(onClick = { println("TODO") }) {
                    Icon(
                        painter = painterResource(id = R.drawable.baseline_edit_24), // Remplacez par votre icône de crayon
                        contentDescription = "Modifier",
                        tint = Color.Black // Couleur de l'icône
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
                RepairItem(repairList[index])
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
        Text(text = repair.description, modifier = Modifier.padding(bottom = 4.dp))
        Text(text = repair.date, modifier = Modifier.padding(bottom = 4.dp))
    }
}

@Preview(showBackground = true)
@Composable
fun ProfilePrew() {
    val car = Car("VD 12345","Toyota", "Corolla", 1,null,"")
    val listeRepar = listOf(
        Repair("ok cass","20 janvier 2000"),
        Repair("ok cass","20 janvier 2000"),
        Repair("ok cass","20 janvier 2000")
    )
    
    Carpage(car = car, repairList = listeRepar) {}
}