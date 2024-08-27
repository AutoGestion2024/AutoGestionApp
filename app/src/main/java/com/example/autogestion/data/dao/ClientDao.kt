package com.example.autogestion.data

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Update

@Dao
interface ClientDao {

    // Inserer un client
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun addClient(client: Client)

    // Modifier un client
    @Update
    fun updateClient(client: Client)

    // Supprimer un client
    @Delete
    fun deleteClient(client: Client)

    // Obtenir un client
    @Query("SELECT * FROM client_table WHERE clientId = :clientId")
    fun getClientById(clientId: Int): Client?

    // Obtenir un client par son email
    @Query("SELECT * FROM client_table WHERE email = :email")
    fun getClientByEmail(email: String): Client?

    // Obtenir tous les clients
    @Query("SELECT * FROM client_table ORDER BY lastName ASC")
    fun getAllClients(): List<Client>

    // TODO: -> views?
    // Obtenir un client avec ses véhicules
    @Transaction
    @Query("SELECT * FROM client_table WHERE clientId = :clientId")
    fun getClientWithVehicles(clientId: Int): List<ClientWithVehicles>

    // Obtenir un véhicule avec ses réparations
    @Transaction
    @Query("SELECT * FROM vehicle_table WHERE vehicleId = :vehicleId")
    fun getVehicleWithReparations(vehicleId: Int): List<VehicleWithReparations>
}
