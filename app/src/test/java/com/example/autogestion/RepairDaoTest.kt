import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import com.example.autogestion.data.AppDatabase
import com.example.autogestion.data.Client
import com.example.autogestion.data.Repair
import com.example.autogestion.data.Vehicle
import com.example.autogestion.data.dao.RepairDao
import kotlinx.coroutines.runBlocking
import org.junit.*
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config
import java.io.IOException

@RunWith(RobolectricTestRunner::class)
@Config(sdk = [28], manifest = Config.NONE)
class RepairDaoTest {

    private lateinit var database: AppDatabase
    private lateinit var repairDao: RepairDao

    @Before
    fun createDb() {
        database = Room.inMemoryDatabaseBuilder(
            ApplicationProvider.getApplicationContext(),
            AppDatabase::class.java
        ).allowMainThreadQueries().build()
        repairDao = database.repairDao()
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

    val repair1 = Repair(
        repairId = 1,
        vehicleId = 1,
        description = "Pneu crevé",
        date = 976579200000, //"12.12.2022",
        invoice = "/path/to/invoice1",
        paid = true
    )

    val repair2 = Repair(
        repairId = 2,
        vehicleId = 1,
        description = "Panne technique",
        date = 976579200000, // "08.07.2022",
        invoice = "/path/to/invoice2",
        paid = false
    )



    // Test insertVehicle and getVehicleById methods
    @Test
    fun insertVehicleAndGetById() = runBlocking {

        database.clientDao().addClient(clientJohn)
        database.vehicleDao().addVehicle(vehicle)

        repairDao.addRepair(repair1)

        val retrievedRepair = repairDao.getRepairById(1)
        Assert.assertEquals(repair1, retrievedRepair)
    }

    //Test updateVehicle method
    @Test
    fun updateRepair() = runBlocking {

        database.clientDao().addClient(clientJohn)
        database.vehicleDao().addVehicle(vehicle)

        repairDao.addRepair(repair1)

        val updatedRepair = repair1.copy(description = "Panne complexe")
        repairDao.updateRepair(updatedRepair)

        val retrievedRepair = repairDao.getRepairById(1)
        Assert.assertEquals("Panne complexe", retrievedRepair?.description)
    }

    // Test deleteVehicle method
    @Test
    fun deleteRepair() = runBlocking {

        database.clientDao().addClient(clientJohn)
        database.vehicleDao().addVehicle(vehicle)

        repairDao.addRepair(repair1)
        repairDao.deleteRepair(repair1)

        val retrievedVehicle = repairDao.getRepairById(1)
        Assert.assertNull(retrievedVehicle)
    }

    // Test getAllVehicles method
    @Test
    fun getAllRepairs() = runBlocking {
        database.clientDao().addClient(clientJohn)
        database.vehicleDao().addVehicle(vehicle)

        repairDao.addRepair(repair1)
        repairDao.addRepair(repair2)

        val allVehicles = repairDao.getAllRepairs()
        Assert.assertEquals(2, allVehicles.size)
        Assert.assertTrue(allVehicles.contains(repair1))
        Assert.assertTrue(allVehicles.contains(repair2))
    }
}
