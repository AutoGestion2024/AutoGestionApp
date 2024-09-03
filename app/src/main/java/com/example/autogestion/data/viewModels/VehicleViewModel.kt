package com.example.autogestion.data.viewModels

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.autogestion.data.AppDatabase
import com.example.autogestion.data.Client
import com.example.autogestion.data.Vehicle
import com.example.autogestion.data.repositories.VehicleRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class VehicleViewModel(application: Application): AndroidViewModel(application) {
    private val repository: VehicleRepository
    private val _currentVehicle: MutableLiveData<Vehicle?> = MutableLiveData()
    val currentVehicle: LiveData<Vehicle?> = _currentVehicle

    private val _allVehicles: MutableLiveData<List<Vehicle>> = MutableLiveData()
    val allVehicles: LiveData<List<Vehicle>> = _allVehicles

    val message = MutableLiveData<String>()

    init {
        val vehicleDao = AppDatabase.getDatabase(application).vehicleDao()
        repository = VehicleRepository(vehicleDao)

        // Charger tous les véhicules au démarrage
        viewModelScope.launch {
            _allVehicles.postValue(repository.fetchAllVehicles())
        }
    }

    /*fun getAllVehicles(): MutableLiveData<List<Vehicle>> {
        return allVehicles
    }*/

    fun addVehicle(vehicle: Vehicle) {
        viewModelScope.launch {
            repository.addVehicle(vehicle)
            _allVehicles.postValue(repository.fetchAllVehicles())
        }
    }

    fun updateVehicle(vehicle: Vehicle) {
        viewModelScope.launch(Dispatchers.IO){
            repository.updateVehicle(vehicle)
            _currentVehicle.postValue(repository.getVehicleById(vehicle.vehicleId))
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

    fun getVehiclesFromClient(clientId: Int): LiveData<List<Vehicle>> {
        val vehicles = MutableLiveData<List<Vehicle>>()
        viewModelScope.launch(Dispatchers.IO) {
            val fetchedVehicles = repository.getVehiclesFromClient(clientId)
            vehicles.postValue((fetchedVehicles ?: emptyList()) as List<Vehicle>?)
        }
        return vehicles
    }


    suspend fun addVehicleAndRetrieveId(vehicle: Vehicle): Int? {
        return withContext(Dispatchers.IO) {
            if (repository.vehicleExists(vehicle.registrationPlate)) { // Vous devez implémenter `vehicleExists` dans le repository
                message.postValue("Véhicule avec cette plaque d'immatriculation existe déjà.")
                return@withContext null
            } else {
                repository.addVehicle(vehicle)
                val insertedVehicle = repository.getVehicleByRegistrationPlate(vehicle.registrationPlate) // Implémentez `getVehicleByRegistrationPlate`
                message.postValue("Véhicule ajouté.")
                return@withContext insertedVehicle?.vehicleId
            }
        }
    }
}