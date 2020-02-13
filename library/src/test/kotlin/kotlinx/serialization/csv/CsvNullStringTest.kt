package kotlinx.serialization.csv

import kotlinx.serialization.test.assertStringFormAndRestored
import kotlin.test.Test

/**
 * Test [Csv] ([CsvEncoder] and [CsvDecoder]) with different [CsvConfiguration.nullString]s.
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
            Csv(CsvConfiguration(nullString = "", unitString = "unit"))
    )

    @Test
    fun testNull() = assertStringFormAndRestored(
            "null",
            NullRecord(null),
            NullRecord.serializer(),
            Csv(CsvConfiguration(nullString = "null"))
    )

    @Test
    fun testNA() = assertStringFormAndRestored(
            "N/A",
            NullRecord(null),
            NullRecord.serializer(),
            Csv(CsvConfiguration(nullString = "N/A"))
    )
}
