package kotlinx.serialization.csv.config

import kotlinx.serialization.csv.Csv
import kotlinx.serialization.csv.CsvConfiguration
import kotlinx.serialization.csv.records.NullRecord
import kotlinx.serialization.test.assertStringFormAndRestored
import kotlin.test.Test

/**
 * Test [Csv] with different [CsvConfiguration.nullString]s.
 */
class CsvNullStringTest {

    @Test
    fun testDefault() = assertStringFormAndRestored(
        "",
        NullRecord(null),
        NullRecord.serializer(),
        Csv
    )

    @Test
    fun testEmpty() = assertStringFormAndRestored(
        "",
        NullRecord(null),
        NullRecord.serializer(),
        Csv(
            CsvConfiguration(
                nullString = ""
            )
        )
    )

    @Test
    fun testNull() = assertStringFormAndRestored(
        "null",
        NullRecord(null),
        NullRecord.serializer(),
        Csv(
            CsvConfiguration(
                nullString = "null"
            )
        )
    )

    @Test
    fun testNA() = assertStringFormAndRestored(
        "N/A",
        NullRecord(null),
        NullRecord.serializer(),
        Csv(
            CsvConfiguration(
                nullString = "N/A"
            )
        )
    )
}
