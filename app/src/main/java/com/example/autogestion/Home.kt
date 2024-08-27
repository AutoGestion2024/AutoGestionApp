package com.example.autogestion

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.autogestion.data.Car
import com.example.autogestion.data.Client
import com.example.autogestion.data.Repair

class ListPage : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MyApp1()
        }
    }
}

@Composable
fun MyApp1() {
    val repair1 = Repair("Engine problem", "1633036800000")
    val repair2 = Repair("Transmission issue", "1633123200000")

    val car1 = Car("ABC123", "Toyota", "Camry",1, listOf(repair1))
    val car2 = Car("XYZ789", "Honda", "Civic", 1,listOf(repair2))

    val client = Client(1,"John", "Doe", listOf(car1, car2))


    var searchText by remember { mutableStateOf(TextFieldValue("")) }
    var items by remember { mutableStateOf(listOf("Item 1", "Item 2", "Item 3")) }

    Scaffold(
        floatingActionButton = {
            FloatingActionButton(
                onClick = {
                    items = items + "Item ${items.size + 1}"
                },
                modifier = Modifier
                    .padding(16.dp)
            ) {
                Text("+")
            }
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
        ) {
            Row (
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
            ){
                // Barre de recherche
                BasicTextField(
                    value = searchText,
                    onValueChange = { searchText = it },
                    modifier = Modifier
                        .weight(1f)
                        .padding(end = 8.dp),
                    textStyle = LocalTextStyle.current.copy(fontSize = 18.sp)
                )
                IconButton(
                    onClick = {
                    println("Icon button clicked")
                    }
                ) {
                    Icon(
                        imageVector = Icons.Default.Search,
                        contentDescription = null,
                        modifier = Modifier
                            .padding(end = 8.dp)
                    )
                }

            }

            // Liste filtrée
            val filteredItems = items.filter {
                it.contains(searchText.text, ignoreCase = true)
            }
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp)
            ) {
                items(filteredItems.size) { index ->
                    Text(
                        text = filteredItems[index],
                        fontSize = 20.sp,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 8.dp)
                    )
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun DefaultPreview1() {
    MyApp1()
}