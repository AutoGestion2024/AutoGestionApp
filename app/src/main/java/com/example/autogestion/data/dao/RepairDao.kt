package com.example.autogestion.data.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.autogestion.data.Repair

@Dao
interface RepairDao {
    // Inserer une reparation
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun addRepair(repair: Repair)

    // Modifier une réparation
    @Update
    fun updateRepair(repair: Repair)

    // Supprimer une réparation
    @Delete
    fun deleteRepair(repair: Repair)

    // Obtenir une réparation
    @Query("SELECT * FROM repair_table WHERE repairId = :repairId")
    fun getRepairById(repairId: Int): Repair?

    // Obtenir toutes les réparations
    @Query("SELECT * FROM repair_table")
    fun getAllRepairs(): LiveData<List<Repair>>

    @Query("SELECT * FROM repair_table WHERE vehicleId = :vehicleId")
    fun getRepairsFromVehicle(vehicleId: Int): LiveData<List<Repair>>
}