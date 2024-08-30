package com.example.autogestion.data

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import androidx.room.Index

@Entity(
    tableName = "vehicle_table",
    foreignKeys = [ForeignKey(
        entity = Client::class,
        parentColumns = ["clientId"],
        childColumns = ["clientId"],
        onDelete = ForeignKey.CASCADE
    )],
    indices = [Index(value = ["clientId"])]     // index foreign key for faster queries
)
data class Vehicle(
    @PrimaryKey(autoGenerate = true)
    val vehicleId: Int,
    val clientId: Int,                          // foreign key to Client
    val chassisNum: String?,
    val greyCard: String?,
    val registrationPlate: String,
    val brand: String?,
    val model: String?,
    val color: String?,
)
