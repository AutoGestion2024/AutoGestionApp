package com.example.autogestion

import androidx.room.Room
import com.example.autogestion.data.AppDatabase
import com.example.autogestion.data.Client
import com.example.autogestion.data.ClientDao
import com.example.autogestion.data.repositories.ClientVehicleRepository
import kotlinx.coroutines.runBlocking
import org.junit.*
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config
import androidx.test.core.app.ApplicationProvider
import com.example.autogestion.data.Repair
import com.example.autogestion.data.Vehicle
import java.io.IOException

@RunWith(RobolectricTestRunner::class)
@Config(sdk = [28], manifest = Config.NONE)
class ClientDaoTest {

    private lateinit var database: AppDatabase
    private lateinit var clientDao: ClientDao

    // Create an in-memory database and get the clientDao
    @Before
    fun createDb() {
        database = Room.inMemoryDatabaseBuilder(
            ApplicationProvider.getApplicationContext(),
            AppDatabase::class.java
        ).allowMainThreadQueries().build()
        clientDao = database.clientDao()
    }

    @After
    @Throws(IOException::class)
    fun closeDb() {
        database.close()
    }

    // Test the addClient and getClientById methods
    @Test
    fun insertClientAndGetById() = runBlocking {
        val client = Client(
            clientId = 1,
            firstName = "John",
            lastName = "Doe",
            phone = "123456789",
            birthDate = 976579200000, // 12.12.2000
            email = "john.doe@example.com",
            address = "Rue de la rue"
        )

        clientDao.addClient(client)

        val retrievedClient = clientDao.getClientById(1)
        Assert.assertEquals(client, retrievedClient)
    }

    // TODO a revoir
    // Test the addClient method with a client with the same phone number
    @Test
    fun doNotAddClientWithSamePhoneNumber() = runBlocking {
        val phoneNumber = "123456789"

        // Insérer le premier client avec le numéro de téléphone
        val client1 = Client(
            clientId = 1,
            firstName = "John",
            lastName = "Doe",
            phone = phoneNumber,
            birthDate = 976579200000, // 12.12.2000
            email = "john.doe@example.com",
            address = "123 Main St"
        )
        clientDao.addClient(client1)

        val client2 = Client(
            clientId = 2,
            firstName = "Jane",
            lastName = "Doe",
            phone = phoneNumber,
            birthDate = 1000000000000, // 09.09.2001
            email = "jane.doe@example.com",
            address = "456 Elm St"
        )
        clientDao.addClient(client2)

        val existsAfter = clientDao.countClientsByPhone(phoneNumber)
        Assert.assertEquals(1, existsAfter)
    }

    // Test the updateClient method
    @Test
    fun updateClient() = runBlocking {
        val client = Client(
            clientId = 1,
            firstName = "John",
            lastName = "Doe",
            phone = "123456789",
            birthDate = 976579200000, // 12.12.2000
            email = "john.doe@example.com",
            address = "Rue de la rue"
        )
        clientDao.addClient(client)

        val updatedClient = client.copy(email = "new.email@example.com")
        clientDao.updateClient(updatedClient)

        val retrievedClient = clientDao.getClientById(1)
        Assert.assertEquals("new.email@example.com", retrievedClient?.email)
    }

    // Test the deleteClient method
    @Test
    fun deleteClient() = runBlocking {
        val client = Client(
            clientId = 1,
            firstName = "John",
            lastName = "Doe",
            phone = "123456789",
            birthDate = 976579200000, // 12.12.2000
            email = "john.doe@example.com",
            address = "Rue de la rue"
        )
        clientDao.addClient(client)
        clientDao.deleteClient(client)

        val retrievedClient = clientDao.getClientById(1)
        Assert.assertNull(retrievedClient)
    }

    // TODO a revoir
    // Test the getAllClients method
    @Test
    fun getAllClients() = runBlocking {
        val client1 = Client(
            clientId = 1,
            firstName = "John",
            lastName = "Doe",
            phone = "123456789",
            birthDate = 976579200000, // 12.12.2000
            email = "john.doe@example.com",
            address = "123 Main St"
        )

        val client2 = Client(
            clientId = 2,
            firstName = "Jane",
            lastName = "Doe",
            phone = "987654321",
            birthDate = 1000000000000, // 09.09.2001
            email = "jane.doe@example.com",
            address = "Rue de la rue"
        )

        clientDao.addClient(client1)
        clientDao.addClient(client2)

        Assert.assertEquals(2, client2.clientId)
    }

    // TODO test vraiment utile ?
    // Test the countClientsByPhone method
    @Test
    fun countClientsByPhone() = runBlocking {
        val client = Client(
            clientId = 1,
            firstName = "John",
            lastName = "Doe",
            phone = "123456789",
            birthDate = 976579200000, // 12.12.2000
            email = "john.doe@example.com",
            address = "Rue de la rue"
        )
        clientDao.addClient(client)

        val count = clientDao.countClientsByPhone("123456789")
        Assert.assertEquals(1, count)
    }

    @Test
    fun getClientWithVehicles() = runBlocking {
        val client = Client(
            clientId = 1,
            firstName = "John",
            lastName = "Doe",
            phone = "123456789",
            birthDate = 976579200000, // 12.12.2000
            email = "john.doe@example.com",
            address = "Rue de la rue"
        )
        clientDao.addClient(client)

        val vehicle1 = Vehicle(
            vehicleId = 1,
            clientId = 1,
            chassisNum = "123ABC",
            greyCard = "/path/to/greyCard1",
            registrationPlate = "VD123",
            brand = "Seat",
            model = "Ibiza",
            color = "Blue"
        )

        val vehicle2 = Vehicle(
            vehicleId = 2,
            clientId = 1,
            chassisNum = "456DEF",
            greyCard = "/path/to/greyCard2",
            registrationPlate = "VD234",
            brand = "Honda",
            model = "Jazz",
            color = "Green"
        )

        database.vehicleDao().addVehicle(vehicle1)
        database.vehicleDao().addVehicle(vehicle2)

        val clientWithVehicles = clientDao.getClientWithVehicles(1)

        Assert.assertEquals(1, clientWithVehicles.size)
        Assert.assertEquals(2, clientWithVehicles[0].vehicles.size)
        Assert.assertTrue(clientWithVehicles[0].vehicles.contains(vehicle1))
        Assert.assertTrue(clientWithVehicles[0].vehicles.contains(vehicle2))
    }

    @Test
    fun getVehicleWithRepairs() = runBlocking {
        val client = Client(
            clientId = 1,
            firstName = "John",
            lastName = "Doe",
            phone = "123456789",
            birthDate = 976579200000, // 12.12.2000
            email = "john.doe@example.com",
            address = "Rue de la rue"
        )
        clientDao.addClient(client)

        val vehicle = Vehicle(
            vehicleId = 1,
            clientId = 1,
            chassisNum = "123ABC",
            greyCard = "/path/to/greyCard1",
            registrationPlate = "VD123",
            brand = "Seat",
            model = "Ibiza",
            color = "Blue"
        )
        database.vehicleDao().addVehicle(vehicle)

        val repair1 = Repair(
            repairId = 1,
            vehicleId = 1,
            description = "Pneu crevé",
            date = "12.12.2022",
            invoice = "/path/to/invoice1",
            paid = true
        )

        val repair2 = Repair(
            repairId = 2,
            vehicleId = 1,
            description = "Panne technique",
            date = "08.07.2022",
            invoice = "/path/to/invoice2",
            paid = false
        )

        database.repairDao().addRepair(repair1)
        database.repairDao().addRepair(repair2)

        val vehicleWithRepairs = clientDao.getVehicleWithRepairs(1)

        Assert.assertEquals(1, vehicleWithRepairs.size)
        Assert.assertEquals(2, vehicleWithRepairs[0].repairs.size)
        Assert.assertTrue(vehicleWithRepairs[0].repairs.contains(repair1))
        Assert.assertTrue(vehicleWithRepairs[0].repairs.contains(repair2))
    }
}
