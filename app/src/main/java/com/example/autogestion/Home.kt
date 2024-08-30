package com.example.autogestion

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.CameraAlt
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.*
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
import com.example.autogestion.data.Car
import com.example.autogestion.data.Client
import com.example.autogestion.data.Repair
import com.example.autogestion.form.ClientForm


class Home : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (!hasRequiredPermissions()) {
            ActivityCompat.requestPermissions(this, CAMERAX_PERMISSIONS, 0)
        }
        enableEdgeToEdge()
        setContent {
            HomeApp()
        }
    }

    private fun hasRequiredPermissions(): Boolean{
        return CAMERAX_PERMISSIONS.all {
            ContextCompat.checkSelfPermission(applicationContext, it) == PackageManager.PERMISSION_GRANTED
        }
    }

    companion object{
        private val CAMERAX_PERMISSIONS = arrayOf(
            Manifest.permission.CAMERA,
            Manifest.permission.INTERNET
        )
    }


    @Composable
    fun HomeApp() {
        val context = LocalContext.current
        val repair1 = Repair("Engine problem", "16.08.2024")
        val repair2 = Repair("Transmission issue", "13.04.2024")
        val repair3 = Repair("Tyre replacement", "10.01.2024")
        val repair4 = Repair("Oil change", "10.01.2024")
        val repair5 = Repair("Service", "10.01.2024")

        val car1 = Car("ABC123", "Toyota", "Camry",1, listOf(repair1), "./images/car1.jpg")
        val car2 = Car("XYZ789", "Honda", "Civic", 1,listOf(repair2, repair3), "./images/car2.jpg")
        val car3 = Car("DEF789", "Mercedes", "A35 AMG", 2,listOf(repair4), "./images/car3.jpg")
        val car4 = Car("DEF789", "Volkswagen", "Cocinelle", 3,listOf(repair5), "./images/car4.jpg")

        val client1 = Client(1,"John", "Doe", listOf(car1, car2), "123 Main St", "555-1234", "william.henry.harrison@example-pet-store.com")
        val client2 = Client(2,"Lewis", "Hamilton", listOf(car3), "123 Main St", "555-2345", "john.mclean@examplepetstore.com")
        val client3 = Client(3,"Paul", "Albertini", listOf(car4), "123 Main St", "555-3456", "william.a.wheeler@example-pet-store.com")


        var searchText by remember { mutableStateOf(TextFieldValue("")) }
        var items by remember { mutableStateOf(listOf(client1, client2, client3)) }

        Scaffold(
            floatingActionButton = {
                FloatingActionButton(
                    onClick = {
                        // TODO: Redirect to the HomeForm page when it's ready
                        val intent = Intent(context, ClientForm::class.java)
                        context.startActivity(intent)
                    },
                    modifier = Modifier
                        .padding(16.dp)
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

                Row (
                    horizontalArrangement = Arrangement.Start, // Align items horizontally
                    verticalAlignment = Alignment.CenterVertically, // Center vertically
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp)
                ){
                    Icon(
                        imageVector = Icons.Default.Search,
                        contentDescription = null,
                        modifier = Modifier
                            .padding(end = 8.dp)
                    )

                    // Barre de recherche
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
                            modifier = Modifier
                                .padding(end = 8.dp)
                        )
                    }

                }

                // Liste filtrée
                val filteredItems = items.filter { client ->
                    client.firstName.contains(searchText.text, ignoreCase = true) ||
                            client.lastName.contains(searchText.text, ignoreCase = true) ||
                            (client.cars?.any { car ->
                                car.plateNumber.contains(searchText.text, ignoreCase = true)
                            } ?: false )
                }

                LazyColumn(
                    modifier = Modifier
                        .padding(16.dp)
                        .wrapContentSize()
                ) {
                    items(filteredItems.size) { index ->
                        val client = filteredItems[index]
                        ClientCarInfo(client = client)
                        if (index < filteredItems.size - 1) {
                            Spacer(modifier = Modifier.height(8.dp))
                        }
                    }
                }
            }
        }
    }

    @Composable
    fun ClientCarInfo(client : Client){
        val current = LocalContext.current
        Column(modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(16.dp))
            .background(color = Color(0xFFF3EDF7))
            .padding(15.dp)
            .clickable {
                val intent = Intent(current, ClientProfile::class.java)
                current.startActivity(intent)
                /*TODO ajouter les parametre de transmission */
            }) {
            Text(
                text = "${client.firstName} ${client.lastName}",
                modifier = Modifier.padding(bottom = 4.dp),
                fontSize = 20.sp
            )
            client.cars?.joinToString(separator = "\n ") { "${it.make}, ${it.model}" }?.let {
                Text(
                    text = it,
                    modifier = Modifier
                        .padding(bottom = 4.dp)
                )
            }
        }
    }

    @Composable
    fun HomeTitle(text : String,onBackClick: () -> Unit){
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
}
