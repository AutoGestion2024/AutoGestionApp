package com.example.autogestion.data.viewModels

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.autogestion.data.AppDatabase
import com.example.autogestion.data.Vehicle
import com.example.autogestion.data.repositories.VehicleRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class VehicleViewModel(application: Application): AndroidViewModel(application) {
    private val repository: VehicleRepository
    private val allVehicles: List<Vehicle>

    val message = MutableLiveData<String>()

    init{
        val vehicleDao = AppDatabase.getDatabase(application).vehicleDao()
        repository = VehicleRepository(vehicleDao)
        allVehicles = repository.allVehicles
    }

    fun getAllVehicles(): List<Vehicle> {
        return allVehicles
    }

    fun addVehicle(vehicle: Vehicle) {
        viewModelScope.launch(Dispatchers.IO){
            repository.addVehicle(vehicle)
        }
    }

    fun updateVehicle(vehicle: Vehicle) {
        viewModelScope.launch(Dispatchers.IO){
            repository.updateVehicle(vehicle)
        }
    }

    fun deleteVehicle(vehicle: Vehicle) {
        viewModelScope.launch(Dispatchers.IO){
            repository.deleteVehicle(vehicle)
        }
    }

    fun getVehicleById(vehicleId: Int): LiveData<Vehicle?> {
        val vehicle = MutableLiveData<Vehicle?>()
        viewModelScope.launch(Dispatchers.IO){
            vehicle.postValue(repository.getVehicleById(vehicleId))
        }
        return vehicle
    }

    fun getVehiclesFromClient(clientId: Int): List<Vehicle?> {
        return repository.getVehiclesFromClient(clientId)
    }
}