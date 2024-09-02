package com.example.autogestion.data.repositories

import androidx.lifecycle.LiveData
import com.example.autogestion.data.Vehicle
import com.example.autogestion.data.dao.VehicleDao
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class VehicleRepository(private val vehicleDao : VehicleDao) {
    //val allVehicles: List<Vehicle> = vehicleDao.getAllVehicles()

    suspend fun addVehicle(vehicle : Vehicle){
        withContext(Dispatchers.IO) {
            vehicleDao.addVehicle(vehicle)
        }
    }

    // TODO check
    suspend fun updateVehicle(vehicle : Vehicle){
        vehicleDao.updateVehicle(vehicle)
    }

    suspend fun deleteVehicle(vehicle : Vehicle){
        vehicleDao.deleteVehicle(vehicle)
    }

    suspend fun getVehicleById(vehicleId : Int): Vehicle? {
        return vehicleDao.getVehicleById(vehicleId)
    }

    // Vehicles from given client
    fun getVehiclesFromClient(clientId: Int): List<Vehicle?> {
        return vehicleDao.getVehiclesFromClient(clientId)
    }

    suspend fun fetchAllVehicles(): List<Vehicle>? {
        return withContext(Dispatchers.IO) {
            vehicleDao.getAllVehicles()
        }
    }
}