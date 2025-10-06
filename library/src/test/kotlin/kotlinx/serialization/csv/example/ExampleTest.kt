package kotlinx.serialization.csv.example

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.delay
import kotlinx.coroutines.test.runTest
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.builtins.ListSerializer
import kotlinx.serialization.builtins.nullable
import kotlinx.serialization.csv.Csv
import kotlinx.serialization.csv.example.Feature.AUTOMATIC
import kotlinx.serialization.csv.example.Feature.ELECTRIC
import kotlinx.serialization.csv.example.Feature.HEATED_SEATS
import kotlinx.serialization.csv.example.Feature.NAVIGATION_SYSTEM
import kotlinx.serialization.csv.example.Feature.XENON
import kotlinx.serialization.csv.example.Tire.Axis.FRONT
import kotlinx.serialization.csv.example.Tire.Axis.REAR
import kotlinx.serialization.csv.example.Tire.Side.LEFT
import kotlinx.serialization.csv.example.Tire.Side.RIGHT
import kotlinx.serialization.csv.recordReader
import kotlinx.serialization.csv.recordWriter
import kotlinx.serialization.modules.SerializersModule
import kotlinx.serialization.test.assertEncodeAndDecode
import java.io.PipedReader
import java.io.PipedWriter
import java.time.LocalDateTime
import java.util.UUID
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.time.Duration.Companion.milliseconds

/**
 * Test complex [LocationRecord].
 */
@OptIn(ExperimentalSerializationApi::class)
class ExampleTest {

    // Persons
    private val jonSmith = Person(12, "Jon", "Smith", null)
    private val janeDoe = Person(42, "Jane", "Doe", 1581602631744)

    // Vehicles
    private val tesla = Vehicle(
        uuid = UUID.fromString("f9682dcb-30f7-4e88-915e-60e3b2758da7"),
        type = VehicleType.CAR,
        brand = "Tesla",
    )

    private val porsche = Vehicle(
        uuid = UUID.fromString("5e1afd88-97a2-4373-a83c-44a49c552abd"),
        type = VehicleType.CAR,
        brand = "Porsche",
    )

    private val harley = Vehicle(
        uuid = UUID.fromString("c038c27b-a3fd-4e35-b6ac-ab06d747e16c"),
        type = VehicleType.BIKE,
        brand = "Harley",
    )

    @Test
    fun testLocationRecords() = Csv {
        hasHeaderRecord = true
    }.assertEncodeAndDecode(
        expected = """
            |id,date,position.latitude,position.longitude,driver.id,driver.foreName,driver.lastName,driver.birthday,vehicle.uuid,vehicle.type,vehicle.brand,vehicleData.speed,vehicleData.consumption,vehicleData.consumption.Combustion.consumptionLiterPer100Km,vehicleData.consumption.Electric.consumptionKWhPer100Km
            |0,2020-02-01T13:33:00,0.0,0.0,12,Jon,Smith,,f9682dcb-30f7-4e88-915e-60e3b2758da7,CAR,Tesla,,Unknown,,
            |1,2020-02-01T13:37:00,0.1,0.1,12,Jon,Smith,,f9682dcb-30f7-4e88-915e-60e3b2758da7,CAR,Tesla,27.7778,Electric,,18.1
            |9000,2020-02-05T07:59:00,48.137154,11.576124,42,Jane,Doe,1581602631744,c038c27b-a3fd-4e35-b6ac-ab06d747e16c,MOTORBIKE,Harley,20.0,Combustion,7.9,
        """.trimMargin(),
        original = listOf(
            LocationRecord(
                id = 0,
                date = LocalDateTime.of(2020, 2, 1, 13, 33),
                position = Position(0.0, 0.0),
                driver = jonSmith,
                vehicle = tesla,
                vehicleData = VehicleData(null, Consumption.Unknown),
            ),
            LocationRecord(
                id = 1,
                date = LocalDateTime.of(2020, 2, 1, 13, 37),
                position = Position(0.1, 0.1),
                driver = jonSmith,
                vehicle = tesla,
                vehicleData = VehicleData(27.7778, Consumption.Electric(18.1)),
            ),
            LocationRecord(
                id = 9_000,
                date = LocalDateTime.of(2020, 2, 5, 7, 59),
                position = Position(48.137154, 11.576124),
                driver = janeDoe,
                vehicle = harley,
                vehicleData = VehicleData(20.0, Consumption.Combustion(7.9)),
            ),
        ),
        serializer = ListSerializer(LocationRecord.serializer()),
    )

    @Test
    fun testVehiclePartRecords() = Csv {
        serializersModule = SerializersModule {
            polymorphic(Part::class, Tire::class, Tire.serializer())
            polymorphic(Part::class, Oil::class, Oil.serializer())
        }
    }.assertEncodeAndDecode(
        expected = """
            |101,f9682dcb-30f7-4e88-915e-60e3b2758da7,CAR,Tesla,Tire,FRONT,LEFT,245,35,21,0.25
            |102,f9682dcb-30f7-4e88-915e-60e3b2758da7,CAR,Tesla,Tire,FRONT,RIGHT,245,35,21,0.21
            |103,f9682dcb-30f7-4e88-915e-60e3b2758da7,CAR,Tesla,Tire,REAR,LEFT,265,35,21,0.35
            |104,f9682dcb-30f7-4e88-915e-60e3b2758da7,CAR,Tesla,Tire,REAR,RIGHT,265,35,21,0.32
            |201,5e1afd88-97a2-4373-a83c-44a49c552abd,CAR,Porsche,Oil,20,50,0.2
            |202,5e1afd88-97a2-4373-a83c-44a49c552abd,CAR,Porsche,Tire,FRONT,LEFT,265,35,20,0.2
            |203,5e1afd88-97a2-4373-a83c-44a49c552abd,CAR,Porsche,Tire,FRONT,RIGHT,265,35,20,0.2
            |204,5e1afd88-97a2-4373-a83c-44a49c552abd,CAR,Porsche,Tire,REAR,LEFT,265,35,20,0.2
            |205,5e1afd88-97a2-4373-a83c-44a49c552abd,CAR,Porsche,Tire,REAR,RIGHT,265,35,20,0.2
        """.trimMargin(),
        original = listOf(
            VehiclePartRecord(101, tesla, Tire(FRONT, LEFT, 245, 35, 21), 0.25),
            VehiclePartRecord(102, tesla, Tire(FRONT, RIGHT, 245, 35, 21), 0.21),
            VehiclePartRecord(103, tesla, Tire(REAR, LEFT, 265, 35, 21), 0.35),
            VehiclePartRecord(104, tesla, Tire(REAR, RIGHT, 265, 35, 21), 0.32),
            VehiclePartRecord(201, porsche, Oil(20, 50), 0.2),
            VehiclePartRecord(202, porsche, Tire(FRONT, LEFT, 265, 35, 20), 0.2),
            VehiclePartRecord(203, porsche, Tire(FRONT, RIGHT, 265, 35, 20), 0.2),
            VehiclePartRecord(204, porsche, Tire(REAR, LEFT, 265, 35, 20), 0.2),
            VehiclePartRecord(205, porsche, Tire(REAR, RIGHT, 265, 35, 20), 0.2),
        ),
        serializer = ListSerializer(VehiclePartRecord.serializer()),
    )

    @Test
    fun testVehicleFeaturesRecords() = Csv.assertEncodeAndDecode(
        expected = """
            |c038c27b-a3fd-4e35-b6ac-ab06d747e16c,MOTORBIKE,Harley,,
            |c038c27b-a3fd-4e35-b6ac-ab06d747e16c,MOTORBIKE,Harley,0,0
            |f9682dcb-30f7-4e88-915e-60e3b2758da7,CAR,Tesla,5,ELECTRIC,AUTOMATIC,HEATED_SEATS,NAVIGATION_SYSTEM,XENON,2,ELECTRIC,0,XENON,1
        """.trimMargin(),
        original = listOf(
            VehicleFeaturesRecord(
                vehicle = harley,
                features = null,
                map = null,
            ),
            VehicleFeaturesRecord(
                vehicle = harley,
                features = emptyList(),
                map = emptyMap(),
            ),
            VehicleFeaturesRecord(
                vehicle = tesla,
                features = listOf(ELECTRIC, AUTOMATIC, HEATED_SEATS, NAVIGATION_SYSTEM, XENON),
                map = mapOf(ELECTRIC to 0, XENON to 1),
            ),
        ),
        serializer = ListSerializer(VehicleFeaturesRecord.serializer()),
    )

    @Test
    fun testRfc4180() = Csv.Rfc4180.assertEncodeAndDecode(
        expected = """
            |c038c27b-a3fd-4e35-b6ac-ab06d747e16c,MOTORBIKE,Harley,,
            |c038c27b-a3fd-4e35-b6ac-ab06d747e16c,MOTORBIKE,Harley,0,0
            |
            |f9682dcb-30f7-4e88-915e-60e3b2758da7,CAR,Tesla,5,ELECTRIC,AUTOMATIC,HEATED_SEATS,NAVIGATION_SYSTEM,XENON,2,ELECTRIC,0,XENON,1
        """.trimMargin().replace("\n", "\r\n"),
        original = listOf(
            VehicleFeaturesRecord(
                vehicle = harley,
                features = null,
                map = null,
            ),
            VehicleFeaturesRecord(
                vehicle = harley,
                features = emptyList(),
                map = emptyMap(),
            ),
            null,
            VehicleFeaturesRecord(
                vehicle = tesla,
                features = listOf(ELECTRIC, AUTOMATIC, HEATED_SEATS, NAVIGATION_SYSTEM, XENON),
                map = mapOf(ELECTRIC to 0, XENON to 1),
            ),
        ),
        serializer = ListSerializer(VehicleFeaturesRecord.serializer().nullable),
    )

    @Test
    fun testStreaming() = runTest {
        val csv = Csv {
            serializersModule = SerializersModule {
                polymorphic(Part::class, Tire::class, Tire.serializer())
                polymorphic(Part::class, Oil::class, Oil.serializer())
            }
        }
        val testData = listOf(
            VehiclePartRecord(101, tesla, Tire(FRONT, LEFT, 245, 35, 21), 0.25),
            VehiclePartRecord(102, tesla, Tire(FRONT, RIGHT, 245, 35, 21), 0.21),
            VehiclePartRecord(103, tesla, Tire(REAR, LEFT, 265, 35, 21), 0.35),
            VehiclePartRecord(104, tesla, Tire(REAR, RIGHT, 265, 35, 21), 0.32),
            VehiclePartRecord(201, porsche, Oil(20, 50), 0.2),
            VehiclePartRecord(202, porsche, Tire(FRONT, LEFT, 265, 35, 20), 0.2),
            VehiclePartRecord(203, porsche, Tire(FRONT, RIGHT, 265, 35, 20), 0.2),
            VehiclePartRecord(204, porsche, Tire(REAR, LEFT, 265, 35, 20), 0.2),
            VehiclePartRecord(205, porsche, Tire(REAR, RIGHT, 265, 35, 20), 0.2),
        )

        val input = PipedReader()
        val sink = PipedWriter(input)

        val writer = csv.recordWriter(VehiclePartRecord.serializer(), sink)
        val reader = csv.recordReader(VehiclePartRecord.serializer(), input)

        val readerTask = async(Dispatchers.IO) {
            reader.asSequence().toList()
        }
        val writerTask = async(Dispatchers.IO) {
            sink.use { output ->
                testData.forEach {
                    writer.write(it)
                    output.flush()
                }
            }
        }

        writerTask.await()
        val result = readerTask.await()

        assertEquals(testData, result)
    }

    @Test
    fun testStreamingHeaders() = runTest {
        val csv = Csv {
            hasHeaderRecord = true
        }
        val testData = listOf(
            Tire(FRONT, LEFT, 245, 35, 21),
            Tire(FRONT, RIGHT, 245, 35, 21),
            Tire(REAR, LEFT, 265, 35, 21),
            Tire(REAR, RIGHT, 265, 35, 21),
            Tire(FRONT, LEFT, 265, 35, 20),
            Tire(FRONT, RIGHT, 265, 35, 20),
            Tire(REAR, LEFT, 265, 35, 20),
            Tire(REAR, RIGHT, 265, 35, 20),
        )

        val source = PipedReader()
        val sink = PipedWriter(source)

        val readerTask = async(Dispatchers.IO) {
            source.buffered().let { input ->
                val reader = csv.recordReader(Tire.serializer(), input)
                reader.asSequence().onEach {
                    println("Read $it")
                }.toList()
            }
        }

        val writerTask = async(Dispatchers.IO) {
            sink.buffered().use { output ->
                val writer = csv.recordWriter(Tire.serializer(), output)
                testData.forEach {
                    println("Writing $it")
                    writer.write(it)
                    output.flush()
                    delay(1.milliseconds)
                }
            }
        }

        writerTask.await()
        val result = readerTask.await()

        assertEquals(testData, result)
    }
}
