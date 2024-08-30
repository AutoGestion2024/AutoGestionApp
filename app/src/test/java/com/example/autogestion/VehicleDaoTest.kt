import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import com.example.autogestion.data.AppDatabase
import com.example.autogestion.data.Client
import com.example.autogestion.data.Vehicle
import com.example.autogestion.data.dao.VehicleDao
import kotlinx.coroutines.runBlocking
import org.junit.*
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config
import java.io.IOException

@RunWith(RobolectricTestRunner::class)
@Config(sdk = [28], manifest = Config.NONE)
class VehicleDaoTest {

    private lateinit var database: AppDatabase
    private lateinit var vehicleDao: VehicleDao

    @Before
    fun createDb() {
        database = Room.inMemoryDatabaseBuilder(
            ApplicationProvider.getApplicationContext(),
            AppDatabase::class.java
        ).allowMainThreadQueries().build()
        vehicleDao = database.vehicleDao()
    }

    @After
    @Throws(IOException::class)
    fun closeDb() {
        database.close()
    }

    // Create a client object for testing
    val clientJohn = Client(
        clientId = 1,
        firstName = "John",
        lastName = "Doe",
        phone = "123456789",
        birthDate = 976579200000, // 12.12.2000
        email = "john.doe@example.com",
        address = "Rue de la rue"
    )

    // Create a vehicle object for testing
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

    // Test insertVehicle and getVehicleById methods
    @Test
    fun insertVehicleAndGetById() = runBlocking {

        database.clientDao().addClient(clientJohn)

        vehicleDao.addVehicle(vehicle1)

        val retrievedVehicle = vehicleDao.getVehicleById(1)
        Assert.assertEquals(vehicle1, retrievedVehicle)
    }

    //Test updateVehicle method
    @Test
    fun updateVehicle() = runBlocking {

        database.clientDao().addClient(clientJohn)

        vehicleDao.addVehicle(vehicle1)

        val updatedVehicle = vehicle1.copy(color = "Red")
        vehicleDao.updateVehicle(updatedVehicle)

        val retrievedVehicle = vehicleDao.getVehicleById(1)
        Assert.assertEquals("Red", retrievedVehicle?.color)
    }

    // Test deleteVehicle method
    @Test
    fun deleteVehicle() = runBlocking {

        database.clientDao().addClient(clientJohn)

        vehicleDao.addVehicle(vehicle1)

        vehicleDao.deleteVehicle(vehicle1)

        val retrievedVehicle = vehicleDao.getVehicleById(1)
        Assert.assertNull(retrievedVehicle)
    }

    // Test getAllVehicles method
    @Test
    fun getAllVehicles() = runBlocking {
        database.clientDao().addClient(clientJohn)

        vehicleDao.addVehicle(vehicle1)
        vehicleDao.addVehicle(vehicle2)

        val allVehicles = vehicleDao.getAllVehicles()
        Assert.assertEquals(2, allVehicles.size)
        Assert.assertTrue(allVehicles.contains(vehicle1))
        Assert.assertTrue(allVehicles.contains(vehicle2))
    }
}
