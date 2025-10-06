package kotlinx.serialization.csv.config

import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.csv.Csv
import kotlinx.serialization.csv.records.NullRecord
import kotlinx.serialization.test.assertEncodeAndDecode
import kotlin.test.Test

/**
 * Test [Csv] with different [CsvConfig.nullString]s.
 */
@OptIn(ExperimentalSerializationApi::class)
class CsvNullStringTest {

    @Test
    fun testDefault() = Csv.assertEncodeAndDecode(
        expected = "",
        original = NullRecord(null),
        serializer = NullRecord.serializer(),
    )

    @Test
    fun testEmpty() = Csv {
        nullString = ""
    }.assertEncodeAndDecode(
        expected = "",
        original = NullRecord(null),
        serializer = NullRecord.serializer(),
    )

    @Test
    fun testNull() = Csv {
        nullString = "null"
    }.assertEncodeAndDecode(
        expected = "null",
        original = NullRecord(null),
        serializer = NullRecord.serializer(),
    )

    @Test
    fun testNA() = Csv {
        nullString = "N/A"
    }.assertEncodeAndDecode(
        expected = "N/A",
        original = NullRecord(null),
        serializer = NullRecord.serializer(),
    )
}
