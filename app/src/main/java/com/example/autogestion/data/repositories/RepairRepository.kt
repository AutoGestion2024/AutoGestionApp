package com.example.autogestion.data.repositories

import androidx.lifecycle.LiveData
import com.example.autogestion.data.Repair
import com.example.autogestion.data.Vehicle
import com.example.autogestion.data.dao.RepairDao
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class RepairRepository(private val repairDao: RepairDao) {
    //val allRepairsList: List<Repair?> = repairDao.getAllRepairs()

    suspend fun addRepair(repair: Repair) {
        repairDao.addRepair(repair)
    }

    /*fun getAllRepairs(): List<Repair?> {
        return repairDao.getAllRepairs()
    }*/

    suspend fun updateRepair(repair: Repair) {
        repairDao.updateRepair(repair)
    }

    suspend fun deleteRepair(repair: Repair) {
        repairDao.deleteRepair(repair)
    }

    suspend fun getRepairById(repairId: Int): Repair? {
        return repairDao.getRepairById(repairId)
    }

    fun getRepairsFromVehicle(vehicleId: Int): List<Repair?> {
        return repairDao.getRepairsFromVehicle(vehicleId)
    }

    suspend fun fetchAllRepairs(): List<Repair>? {
        return withContext(Dispatchers.IO) {
            repairDao.getAllRepairs()
        }
    }
}