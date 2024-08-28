package com.example.autogestion.data.repositories

import androidx.lifecycle.LiveData
import com.example.autogestion.data.Vehicle
import com.example.autogestion.data.dao.VehicleDao

class VehicleRepository(private val vehicleDao : VehicleDao) {
    val allVehicles: LiveData<List<Vehicle>> = vehicleDao.getAllVehicles()

    suspend fun addVehicle(vehicle : Vehicle){
        vehicleDao.addVehicle(vehicle)
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
    fun getVehiclesFromClient(clientId: Int): LiveData<List<Vehicle?>> {
        return vehicleDao.getVehiclesFromClient(clientId)
    }
}