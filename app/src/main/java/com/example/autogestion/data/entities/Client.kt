package com.example.autogestion.data

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.text.DateFormat

@Entity(tableName = "client_table")
data class Client (
    @PrimaryKey(autoGenerate = true)
    val clientId : Int,
    val name : String,
    val lastName : String,
    //val birthDate : DateFormat,
    val phone : String,
    val email : String,
    val address : String,
)