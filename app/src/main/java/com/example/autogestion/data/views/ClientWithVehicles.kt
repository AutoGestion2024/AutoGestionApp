package com.example.autogestion.data

import androidx.room.Embedded
import androidx.room.Relation

data class ClientWithVehicles(
    @Embedded val client: Client,
    @Relation(
        parentColumn = "clientId",
        entityColumn = "clientId"
    )
    val vehicles: List<Vehicle>
)
