package com.example.autogestion.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.autogestion.data.dao.RepairDao
import com.example.autogestion.data.dao.VehicleDao

@Database(
    entities = [Client::class, Vehicle::class, Repair::class],
    version = 2,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {

    abstract fun clientDao(): ClientDao
    abstract fun vehicleDao(): VehicleDao
    abstract fun repairDao(): RepairDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        // Function to get the singleton instance of the database.
        fun getDatabase(context: Context): AppDatabase {
            // Return the existing instance if it exists or create a new one in a synchronized block.
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "appDB"
                ).fallbackToDestructiveMigration().build()
                INSTANCE = instance
                instance
            }
        }
    }
}
