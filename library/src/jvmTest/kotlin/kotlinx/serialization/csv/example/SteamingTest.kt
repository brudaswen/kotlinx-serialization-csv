package kotlinx.serialization.csv.example

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.delay
import kotlinx.coroutines.test.runTest
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.Serializable
import kotlinx.serialization.csv.Csv
import kotlinx.serialization.csv.example.Tire.Axis.FRONT
import kotlinx.serialization.csv.example.Tire.Axis.REAR
import kotlinx.serialization.csv.example.Tire.Side.LEFT
import kotlinx.serialization.csv.example.Tire.Side.RIGHT
import kotlinx.serialization.csv.recordReader
import kotlinx.serialization.csv.recordWriter
import kotlinx.serialization.modules.SerializersModule
import java.io.File
import java.io.FileInputStream
import java.io.FileOutputStream
import java.io.PipedReader
import java.io.PipedWriter
import kotlin.io.buffered
import kotlin.io.println
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.time.Duration.Companion.milliseconds
import kotlin.time.Duration.Companion.minutes
import kotlin.use
import kotlin.uuid.Uuid

/**
 * Test complex [LocationRecord].
 */
@OptIn(ExperimentalSerializationApi::class)
class SteamingTest {

    // Vehicles
    private val tesla = Vehicle(
        uuid = Uuid.parse("f9682dcb-30f7-4e88-915e-60e3b2758da7"),
        type = VehicleType.CAR,
        brand = "Tesla",
    )

    private val porsche = Vehicle(
        uuid = Uuid.parse("5e1afd88-97a2-4373-a83c-44a49c552abd"),
        type = VehicleType.CAR,
        brand = "Porsche",
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
        val output = PipedWriter(input)

        val writer = csv.recordWriter(VehiclePartRecord.serializer(), output)
        val reader = csv.recordReader(VehiclePartRecord.serializer(), input)

        val readerTask = async(Dispatchers.IO) {
            reader.asSequence().toList()
        }
        val writerTask = async(Dispatchers.IO) {
            output.use { output ->
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

        val input = PipedReader()
        val output = PipedWriter(input)

        val readerTask = async(Dispatchers.IO) {
            val reader = csv.recordReader(Tire.serializer(), input)
            reader.asSequence().onEach {
                println("Read $it")
            }.toList()
        }

        val writerTask = async(Dispatchers.IO) {
            output.buffered().use { output ->
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

    @Test
    fun testLargeFile() = runTest(timeout = 10.minutes) {
        @Serializable
        data class Entry(
            val id: Int,
            val value: String,
        )

        val csv = Csv {
            hasHeaderRecord = true
        }

        val file = File("file.csv")

        println("Writing...")
        FileOutputStream(file).buffered().use { output ->
            val writer = csv.recordWriter(Entry.serializer(), output)
            repeat(10_000_000) {
                val entry = Entry(it, "Value $it")
                writer.write(entry)

                if (entry.id % 100_000 == 0) {
                    println("$entry written.")
                }
            }
        }

        println("Reading...")
        FileInputStream(file).buffered().use { input ->
            val reader = csv.recordReader(Entry.serializer(), input)

            reader.asSequence().forEach { entry ->
                if (entry.id % 100_000 == 0) {
                    println("$entry read.")
                }
            }
        }

        file.delete()
    }
}
