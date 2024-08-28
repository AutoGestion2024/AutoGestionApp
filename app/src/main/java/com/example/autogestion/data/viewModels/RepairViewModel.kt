package com.example.autogestion.data.viewModels

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.autogestion.data.AppDatabase
import com.example.autogestion.data.Repair
import com.example.autogestion.data.Vehicle
import com.example.autogestion.data.repositories.RepairRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class RepairViewModel(application: Application): AndroidViewModel(application) {
    private val repository: RepairRepository
    private val allRepairs: List<Repair?>

    val message = MutableLiveData<String>()

    init {
        val repairDao = AppDatabase.getDatabase(application).repairDao()
        repository = RepairRepository(repairDao)
        allRepairs = repository.allRepairs
    }

    fun getAllRepairs(): List<Repair?> {
        return allRepairs
    }

    fun addRepair(repair: Repair) {
        viewModelScope.launch(Dispatchers.IO){
            repository.addRepair(repair)
        }
    }

    fun updateRepair(repair: Repair) {
        viewModelScope.launch(Dispatchers.IO){
            repository.updateRepair(repair)
        }
    }

    fun deleteRepair(repair: Repair) {
        viewModelScope.launch(Dispatchers.IO){
            repository.deleteRepair(repair)
        }
    }

    fun getRepairById(repairId: Int): LiveData<Repair?> {
        val repair = MutableLiveData<Repair?>()
        viewModelScope.launch(Dispatchers.IO){
            repair.postValue(repository.getRepairById(repairId))
        }
        return repair
    }

    fun getRepairsFromVehicle(vehicleId: Int): List<Repair?> {
        return repository.getRepairsFromVehicle(vehicleId)
    }
}
