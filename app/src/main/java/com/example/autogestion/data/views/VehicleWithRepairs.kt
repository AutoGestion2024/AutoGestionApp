package com.example.autogestion.data

import androidx.room.Embedded
import androidx.room.Relation

data class VehicleWithRepairs(
    @Embedded val vehicle: Vehicle,
    @Relation(
        parentColumn = "vehicleId",
        entityColumn = "vehicleId"
    )
    val repairs: List<Repair>
)
