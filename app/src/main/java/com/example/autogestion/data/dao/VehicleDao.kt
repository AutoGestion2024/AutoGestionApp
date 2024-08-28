package com.example.autogestion.data.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.autogestion.data.Vehicle

@Dao
interface VehicleDao {
    // Inserer un véhicule
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun addVehicle(vehicle: Vehicle)

    // Modifier un véhicule
    @Update
    fun updateVehicle(vehicle: Vehicle)

    // Supprimer un véhicule
    @Delete
    fun deleteVehicle(vehicle: Vehicle)

    // Obtenir un véhicule
    @Query("SELECT * FROM vehicle_table WHERE vehicleId = :vehicleId")
    fun getVehicleById(vehicleId: Int): Vehicle?

    // Obtenir tous les véhicules
    @Query("SELECT * FROM vehicle_table")
    fun getAllVehicles(): LiveData<List<Vehicle>>

    // Obtenir les véhicules d'un client
    @Query("SELECT * FROM vehicle_table WHERE clientId = :clientId")
    fun getVehiclesFromClient(clientId: Int): LiveData<List<Vehicle?>>
}
