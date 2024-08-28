package com.example.autogestion.data

data class Car(
    val plateNumber: String,
    val make: String,
    val model: String,
    val clientId: Long,
    val repairs: List<Repair>? = emptyList(),
    val registrationDocURI: String
)