package com.example.autogestion.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "client_table")
data class Client (
    @PrimaryKey(autoGenerate = true)
    val clientId : Int,
    val firstName : String,
    val lastName : String,
    val birthDate : Long?,
    val phone : String,
    val email : String?,
    val address : String?,
)