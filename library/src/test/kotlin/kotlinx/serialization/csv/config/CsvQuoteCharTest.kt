package kotlinx.serialization.csv.config

import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.csv.Csv
import kotlinx.serialization.csv.CsvConfiguration
import kotlinx.serialization.csv.CsvConfiguration.QuoteMode.ALL
import kotlinx.serialization.csv.records.IntRecord
import kotlinx.serialization.csv.records.StringRecord
import kotlinx.serialization.test.assertStringFormAndRestored
import kotlin.test.Test

/**
 * Test [Csv] with different [CsvConfiguration.quoteChar]s.
 */
@ExperimentalSerializationApi
class CsvQuoteCharTest {

    @Test
    fun testDefault() = assertStringFormAndRestored(
        "\"1\"",
        IntRecord(1),
        IntRecord.serializer(),
        Csv(
            CsvConfiguration(
                quoteMode = ALL
            )
        )
    )

    @Test
    fun testDoubleQuote() = assertStringFormAndRestored(
        "\"1\"",
        IntRecord(1),
        IntRecord.serializer(),
        Csv(
            CsvConfiguration(
                quoteMode = ALL,
                quoteChar = '"'
            )
        )
    )

    @Test
    fun testSingleQuote() = assertStringFormAndRestored(
        "'1'",
        IntRecord(1),
        IntRecord.serializer(),
        Csv(
            CsvConfiguration(
                quoteMode = ALL,
                quoteChar = '\''
            )
        )
    )

    @Test
    fun testBang() = assertStringFormAndRestored(
        "!1!",
        IntRecord(1),
        IntRecord.serializer(),
        Csv(
            CsvConfiguration(
                quoteMode = ALL,
                quoteChar = '!'
            )
        )
    )

    @Test
    fun testEscapingOfQuoteChar() = assertStringFormAndRestored(
        "'a''b'",
        StringRecord("a'b"),
        StringRecord.serializer(),
        Csv(
            CsvConfiguration(
                quoteMode = ALL,
                quoteChar = '\''
            )
        )
    )
}