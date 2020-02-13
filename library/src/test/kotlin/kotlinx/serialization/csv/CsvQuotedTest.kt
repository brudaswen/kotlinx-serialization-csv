package kotlinx.serialization.csv

import kotlinx.serialization.test.shouldBe
import kotlin.test.Test

/**
 * Test [CsvDecoder] with quoted values.
 */
class CsvQuotedTest {

    @Test
    fun readQuotedDelimiter() {
        Csv.parse(StringRecord.serializer(), "\"test,ing\"") shouldBe StringRecord("test,ing")
    }

    @Test
    fun readQuotedDelimiterIgnoreSurroundingSpaces() {
        Csv.parse(StringRecord.serializer(), " \"test,ing\" ") shouldBe StringRecord("test,ing")
    }

    @Test
    fun readQuotedDelimiterIgnoreSurroundingSpaces2() {
        Csv.parse(IntStringRecord.serializer(), " \"42\" , \"test , ing\" ") shouldBe IntStringRecord(42, "test , ing")
    }
}
