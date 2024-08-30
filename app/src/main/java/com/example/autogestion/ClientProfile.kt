package com.example.autogestion

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.Scaffold
import androidx.compose.material.TabRowDefaults.Divider
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.autogestion.data.Car
import com.example.autogestion.data.Client

class ClientProfile : ComponentActivity(){

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()


        val client = Client(1,"Alex"," Doe", null,"Avenue des 2" ,"0123456789","john.doe@example.com")
        val carList = listOf(
            Car("VD 12345","Toyota", "Corolla", 1,null,""),
            Car("VD 54233","Toyota", "XD", 1,null,""),
            Car("VD 73345","Toyota", "WESH", 1,null,""),
            Car("VD 73345","Toyota", "WESH", 1,null,""),
            Car("VD 73345","Toyota", "WESH", 1,null,""),
            Car("VD 73345","Toyota", "WESH", 1,null,""),
            Car("VD 73345","Toyota", "WESH", 1,null,""),
            Car("VD 73345","Toyota", "WESH", 1,null,""),
            Car("VD 73345","Toyota", "WESH", 1,null,""),


            )

        setContent {
            ProfilPage(client,carList)
        }
    }



    @Composable
    fun ProfilPage(client: Client, carList: List<Car>) {
        val context = LocalContext.current

        Scaffold(){padding ->
            Column(modifier = Modifier.fillMaxSize().padding(padding)) {

                NavBar(text =  "Profile client : id ${client.id}") {
                    val intent = Intent(context, Home::class.java)
                    context.startActivity(intent)
                }

                Row(
                    modifier = Modifier
                        .fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {

                    // Client information
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text(text = "${client.firstName} ${client.lastName} ", modifier = Modifier.padding(bottom = 8.dp))
                        Text(text = client.email, modifier = Modifier.padding(bottom = 8.dp))
                        Text(text = client.phone, modifier = Modifier.padding(bottom = 8.dp))
                        Text(text = client.address)
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
                        text = "Liste Voitures",
                        fontSize = 15.sp,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(horizontal = 16.dp)
                    )

                    IconButton(onClick = { println("TODO") /* TODO bouton + cote Liste Voiture */ })
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


                // Car List
                LazyColumn(modifier = Modifier
                    .padding(16.dp)
                    .wrapContentSize()) {

                    items(carList.size) { index ->
                        VoitureItem(carList[index])
                        if (index < carList.size - 1) {

                            Spacer(modifier = Modifier.height(8.dp))
                        }
                    }
                }
            }
        }
    }


    // One Car
    @Composable
    fun VoitureItem(car: Car) {
        val context = LocalContext.current

        Column(modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(16.dp))
            .background(color = Color(0xFFF3EDF7))
            .padding(16.dp)
            .clickable {
                val intent = Intent(context, CarProfile::class.java)
                context.startActivity(intent)
                /* TODO Ajouter les parametres */
            }
        ) {
            Text(text = "Plaque: ${car.plateNumber}", modifier = Modifier.padding(bottom = 4.dp))
            Text(text = "Marque: ${car.make}", modifier = Modifier.padding(bottom = 4.dp))
            Text(text = "Modèle: ${car.model}", modifier = Modifier.padding(bottom = 4.dp))
        }
    }

}






@Preview(showBackground = true)
@Composable
fun ProfilPagePreview() {

    val client = Client(1,"Alex"," Doe", null,"Avenue des 2" ,"0123456789","john.doe@example.com")
    val carList = listOf(
        Car("VD 12345","Toyota", "Corolla", 1,null,""),
        Car("VD 54233","Toyota", "XD", 1,null,""),
        Car("VD 73345","Toyota", "WESH", 1,null,""),
        Car("VD 73345","Toyota", "WESH", 1,null,""),
        Car("VD 73345","Toyota", "WESH", 1,null,""),
        Car("VD 73345","Toyota", "WESH", 1,null,""),
        Car("VD 73345","Toyota", "WESH", 1,null,""),
        Car("VD 73345","Toyota", "WESH", 1,null,""),
        Car("VD 73345","Toyota", "WESH", 1,null,""),


        )

    ClientProfile().ProfilPage(client = client, carList = carList)
}
