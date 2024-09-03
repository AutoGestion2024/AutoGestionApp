package com.example.autogestion.data.viewModels

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.autogestion.data.AppDatabase
import com.example.autogestion.data.Client
import com.example.autogestion.data.Repair
import com.example.autogestion.data.Vehicle
import com.example.autogestion.data.repositories.RepairRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class RepairViewModel(application: Application) : AndroidViewModel(application) {
    private val repository: RepairRepository

    private val _allRepairs: MutableLiveData<List<Repair>> = MutableLiveData()
    //val allRepairs: LiveData<List<Repair?>> get() = _allRepairs

    val currentRepair: MutableLiveData<Repair?> = MutableLiveData(null)

    val message = MutableLiveData<String>()

    init {
        val repairDao = AppDatabase.getDatabase(application).repairDao()
        repository = RepairRepository(repairDao)
        viewModelScope.launch {
            _allRepairs.postValue(repository.fetchAllRepairs())
        }
    }

    fun addRepair(repair: Repair) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.addRepair(repair)
            _allRepairs.postValue(repository.fetchAllRepairs())
        }
    }

    fun updateRepair(repair: Repair) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.updateRepair(repair)
            currentRepair.postValue(repository.getRepairById(repair.repairId))  // Mise à jour du repair actuel
        }
    }

    fun deleteRepair(repair: Repair) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.deleteRepair(repair)
        }
    }

    fun getRepairById(repairId: Int): LiveData<Repair?> {
        val repair = MutableLiveData<Repair?>()
        viewModelScope.launch(Dispatchers.IO) {
            repair.postValue(repository.getRepairById(repairId))
        }
        return repair
    }

    fun getRepairsFromVehicle(vehicleId: Int): LiveData<List<Repair?>> {
        val repairs = MutableLiveData<List<Repair?>>()
        viewModelScope.launch(Dispatchers.IO) {
            repairs.postValue(repository.getRepairsFromVehicle(vehicleId))
        }
        return repairs
    }
}
