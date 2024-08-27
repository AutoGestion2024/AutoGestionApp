package com.example.autogestion.data

data class Client(
    val id: Long,
    val firstName: String,
    val lastName: String,
    val cars: List<Car> = emptyList()
//    val address: String,
//    val phone: String,
//    val email: String
)