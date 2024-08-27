package com.example.autogestion.data.viewModels

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import com.example.autogestion.data.AppDatabase
import com.example.autogestion.data.Client
import com.example.autogestion.data.repositories.ClientVehicleRepository
import kotlinx.coroutines.launch
import kotlinx.coroutines.Dispatchers

class ClientVehicleViewModel(application: Application): AndroidViewModel(application){
    private val getAllClients: LiveData<List<Client>>
    private val repository: ClientVehicleRepository

    init{
        val clientDao = AppDatabase.getDatabase(application).clientDao()
        repository = ClientVehicleRepository(clientDao)
        getAllClients = repository.allClients
    }

    fun addClient(client: Client){
       viewModelScope.launch(Dispatchers.IO){
           repository.addClient(client)
       }
    }
}