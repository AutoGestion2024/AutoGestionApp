package com.example.autogestion.data

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.autogestion.data.dao.ReparationDao
import com.example.autogestion.data.dao.VehicleDao

@Database(entities = [Client::class, Vehicle::class, Reparation::class], version = 1, exportSchema = false)
abstract class ClientDatabase : RoomDatabase() {

    abstract fun clientDao(): ClientDao
    abstract fun vehicleDao(): VehicleDao
    abstract fun reparationDao(): ReparationDao

    companion object {
        @Volatile
        private var INSTANCE: ClientDatabase? = null
    }
}
