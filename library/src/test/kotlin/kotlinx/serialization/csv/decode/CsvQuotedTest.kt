package kotlinx.serialization.csv.decode

import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.csv.Csv
import kotlinx.serialization.csv.records.IntStringRecord
import kotlinx.serialization.csv.records.StringRecord
import kotlinx.serialization.test.shouldBe
import kotlin.test.Test

/**
 * Test [kotlinx.serialization.csv.decode.CsvDecoder] with quoted values.
 */
@OptIn(ExperimentalSerializationApi::class)
class CsvQuotedTest {

    @Test
    fun readQuotedDelimiter() {
        Csv.decodeFromString(
            deserializer = StringRecord.serializer(),
            string = "\"test,ing\"",
        ) shouldBe StringRecord(
            a = "test,ing",
        )
    }

    @Test
    fun readQuotedDelimiterIgnoreSurroundingSpaces() {
        Csv.decodeFromString(
            deserializer = StringRecord.serializer(),
            string = " \"test,ing\" ",
        ) shouldBe StringRecord(
            a = "test,ing",
        )
    }

    @Test
    fun readQuotedDelimiterIgnoreSurroundingSpaces2() {
        Csv.decodeFromString(
            deserializer = IntStringRecord.serializer(),
            string = " \"42\" , \"test , ing\" ",
        ) shouldBe IntStringRecord(
            a = 42,
            b = "test , ing",
        )
    }
}
