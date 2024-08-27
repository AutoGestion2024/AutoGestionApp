package com.example.autogestion.data

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import androidx.room.Index

@Entity(
    tableName = "repair_table",
    foreignKeys = [ForeignKey(
        entity = Vehicle::class,
        parentColumns = ["vehicleId"],
        childColumns = ["vehicleId"],
        onDelete = ForeignKey.CASCADE
    )],
    indices = [Index(value = ["vehicleId"])]        // index foreign key for faster queries
)
data class Repair(
    @PrimaryKey(autoGenerate = true)
    val repairId: Int,
    val vehicleId: Int,                             // foreign key to Vehicle        
    val description: String,
    val date: String,
    val invoice: String,
    val paid : Boolean,
)
