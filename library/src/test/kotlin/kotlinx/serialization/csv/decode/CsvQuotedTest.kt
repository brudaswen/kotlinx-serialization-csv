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
            StringRecord.serializer(),
            "\"test,ing\""
        ) shouldBe StringRecord(
            "test,ing"
        )
    }

    @Test
    fun readQuotedDelimiterIgnoreSurroundingSpaces() {
        Csv.decodeFromString(
            StringRecord.serializer(),
            " \"test,ing\" "
        ) shouldBe StringRecord(
            "test,ing"
        )
    }

    @Test
    fun readQuotedDelimiterIgnoreSurroundingSpaces2() {
        Csv.decodeFromString(
            IntStringRecord.serializer(),
            " \"42\" , \"test , ing\" "
        ) shouldBe IntStringRecord(
            42,
            "test , ing"
        )
    }
}
