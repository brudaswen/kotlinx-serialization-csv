package kotlinx.serialization.csv.example

import kotlinx.coroutines.test.runTest
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.builtins.ListSerializer
import kotlinx.serialization.csv.Csv
import kotlinx.serialization.csv.decodeFrom
import kotlinx.serialization.csv.encodeTo
import kotlinx.serialization.csv.example.Tire.Axis.FRONT
import kotlinx.serialization.csv.example.Tire.Side.LEFT
import kotlinx.serialization.csv.recordReader
import kotlinx.serialization.csv.recordWriter
import kotlinx.serialization.modules.SerializersModule
import java.io.ByteArrayInputStream
import java.io.ByteArrayOutputStream
import kotlin.io.bufferedReader
import kotlin.io.bufferedWriter
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.use
import kotlin.uuid.Uuid

@OptIn(ExperimentalSerializationApi::class)
internal class CsvJvmTest {

    private val tasla = Vehicle(
        uuid = Uuid.parse("f9682dcb-30f7-4e88-915e-60e3b2758da7"),
        type = VehicleType.CAR,
        brand = "Tesla",
    )

    private val porsche = Vehicle(
        uuid = Uuid.parse("5e1afd88-97a2-4373-a83c-44a49c552abd"),
        type = VehicleType.CAR,
        brand = "Porsche",
    )

    private val testData = listOf(
        VehiclePartRecord(101, tasla, Tire(FRONT, LEFT, 245, 35, 21), 0.25),
        VehiclePartRecord(201, porsche, Oil(20, 50), 0.2),
    )

    private val csv = Csv {
        serializersModule = SerializersModule {
            polymorphic(Part::class, Tire::class, Tire.serializer())
            polymorphic(Part::class, Oil::class, Oil.serializer())
        }
    }

    private val serializer = ListSerializer(VehiclePartRecord.serializer())

    @Test
    fun test_Csv_encodeTo_InputOutputStream() = runTest {
        val output = ByteArrayOutputStream().use { output ->
            csv.encodeTo(serializer, testData, output)
            output.toByteArray()
        }

        val result = ByteArrayInputStream(output).use { input ->
            csv.decodeFrom(serializer, input).toList()
        }

        assertEquals(testData, result)
    }

    @Test
    fun test_Csv_decodeFrom_ReaderWriter() = runTest {
        val output = ByteArrayOutputStream().use { output ->
            output.bufferedWriter().use { writer ->
                csv.encodeTo(serializer, testData, writer)
            }
            output.toByteArray()
        }

        val result = ByteArrayInputStream(output).use { input ->
            csv.decodeFrom(serializer, input.bufferedReader()).toList()
        }

        assertEquals(testData, result)
    }

    @Test
    fun test_Csv_recordReaderWriter_InputOutputStream() = runTest {
        val serializer = VehiclePartRecord.serializer()

        val output = ByteArrayOutputStream().use { output ->
            val writer = csv.recordWriter(serializer, output)
            testData.forEach(writer::write)
            output.toByteArray()
        }

        val result = ByteArrayInputStream(output).use { input ->
            csv.recordReader(serializer, input).asSequence().toList()
        }

        assertEquals(testData, result)
    }
}
