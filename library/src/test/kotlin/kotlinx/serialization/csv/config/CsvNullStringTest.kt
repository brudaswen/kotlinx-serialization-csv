package kotlinx.serialization.csv.config

import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.csv.Csv
import kotlinx.serialization.csv.CsvConfiguration
import kotlinx.serialization.csv.records.NullRecord
import kotlinx.serialization.test.assertEncodeAndDecode
import kotlin.test.Test

/**
 * Test [Csv] with different [CsvConfiguration.nullString]s.
 */
@OptIn(ExperimentalSerializationApi::class)
class CsvNullStringTest {

    @Test
    fun testDefault() = Csv.assertEncodeAndDecode(
        "",
        NullRecord(null),
        NullRecord.serializer()
    )

    @Test
    fun testEmpty() = Csv(
        CsvConfiguration(
            nullString = ""
        )
    ).assertEncodeAndDecode(
        "",
        NullRecord(null),
        NullRecord.serializer()
    )

    @Test
    fun testNull() = Csv(
        CsvConfiguration(
            nullString = "null"
        )
    ).assertEncodeAndDecode(
        "null",
        NullRecord(null),
        NullRecord.serializer()
    )

    @Test
    fun testNA() = Csv(
        CsvConfiguration(
            nullString = "N/A"
        )
    ).assertEncodeAndDecode(
        "N/A",
        NullRecord(null),
        NullRecord.serializer()
    )
}
