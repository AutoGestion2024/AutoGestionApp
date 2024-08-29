package com.example.autogestion.data.viewModels

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.autogestion.data.AppDatabase
import com.example.autogestion.data.Client
import com.example.autogestion.data.repositories.ClientRepository
import kotlinx.coroutines.launch
import kotlinx.coroutines.Dispatchers

class ClientViewModel(application: Application): AndroidViewModel(application){
    private val allClients: LiveData<List<Client>>
    private val repository: ClientRepository
    val currentClient: MutableLiveData<Client?> = MutableLiveData(null)

    val message = MutableLiveData<String>()

    init{
        val clientDao = AppDatabase.getDatabase(application).clientDao()
        repository = ClientRepository(clientDao)
        allClients = repository.allClients
    }

    fun getAllClients(): LiveData<List<Client>> {
        return allClients
    }

    fun addClient(client: Client){
       viewModelScope.launch(Dispatchers.IO) {
           if (repository.clientExists(client.phone)) {
               message.postValue("Client avec ce numéro de téléphone existe déjà.")
           } else {
               repository.addClient(client)
               message.postValue("Client ajouté.")
           }
       }
    }

    fun updateClient(client: Client){
        viewModelScope.launch(Dispatchers.IO) {
            repository.updateClient(client)
        }
    }

    fun deleteClient(client: Client){
        viewModelScope.launch(Dispatchers.IO) {
            repository.deleteClient(client)
        }
    }

    fun getClientById(clientId: Int): LiveData<Client?> {
        viewModelScope.launch(Dispatchers.IO) {
            currentClient.postValue(repository.getClientById(clientId))
        }
        return currentClient
    }

    fun getClientByPhoneNumber(phone: String): LiveData<Client?> {
        val client = MutableLiveData<Client?>()
        viewModelScope.launch(Dispatchers.IO) {
            client.postValue(repository.getClientByPhoneNumber(phone))
        }
        return client
    }

    fun clientExists(phone: String): LiveData<Boolean> {
        val result = MutableLiveData<Boolean>()
        viewModelScope.launch(Dispatchers.IO) {
            result.postValue(repository.clientExists(phone))
        }
        return result
    }
}