package com.example.autogestion.data.repositories

import androidx.lifecycle.LiveData
import com.example.autogestion.data.ClientDao
import com.example.autogestion.data.Client

class ClientRepository(private val clientDao : ClientDao) {
    val allClients : LiveData<List<Client>> = clientDao.getAllClients()

    suspend fun addClient(client : Client){
        clientDao.addClient(client)
    }

    // TODO check
    suspend fun updateClient(client : Client){
        clientDao.updateClient(client)
    }

    suspend fun deleteClient(client : Client){
        clientDao.deleteClient(client)
    }

    suspend fun getClientById(clientId : Int): Client? {
        return clientDao.getClientById(clientId)
    }

    suspend fun getClientByPhoneNumber(phone: String): Client? {
        return clientDao.getClientByPhoneNumber(phone)
    }

    suspend fun clientExists(phone: String): Boolean {
        return clientDao.countClientsByPhoneNumber(phone) > 0
    }
}