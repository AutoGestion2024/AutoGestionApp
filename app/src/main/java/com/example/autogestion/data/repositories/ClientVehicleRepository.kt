package com.example.autogestion.data.repositories

import androidx.lifecycle.LiveData
import com.example.autogestion.data.Client
import com.example.autogestion.data.ClientDao

class ClientVehicleRepository(private val clientDao : ClientDao) {
    val allClients : LiveData<List<Client>> = clientDao.getAllClients()

    suspend fun addClient(client : Client){
        clientDao.addClient(client)
    }

    suspend fun clientExists(phone: String): Boolean {
        return clientDao.countClientsByEmail(phone) > 0
    }

    // TODO : complete the repository
}