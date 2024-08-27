package com.example.autogestion.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
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

        // Function to get the singleton instance of the database.
        fun getDatabase(context: Context): ClientDatabase {
            // Return the existing instance if it exists or create a new one in a synchronized block.
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    ClientDatabase::class.java,
                    "appDB"
                ).build()
                INSTANCE = instance
                instance
            }
        }
    }
}
