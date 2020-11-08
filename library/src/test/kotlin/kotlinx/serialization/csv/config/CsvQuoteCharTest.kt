package kotlinx.serialization.csv.config

import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.csv.Csv
import kotlinx.serialization.csv.CsvConfiguration
import kotlinx.serialization.csv.CsvConfiguration.QuoteMode.ALL
import kotlinx.serialization.csv.records.IntRecord
import kotlinx.serialization.csv.records.StringRecord
import kotlinx.serialization.test.assertEncodeAndDecode
import kotlin.test.Test

/**
 * Test [Csv] with different [CsvConfiguration.quoteChar]s.
 */
@OptIn(ExperimentalSerializationApi::class)
class CsvQuoteCharTest {

    @Test
    fun testDefault() = Csv(
        CsvConfiguration(
            quoteMode = ALL
        )
    ).assertEncodeAndDecode(
        "\"1\"",
        IntRecord(1),
        IntRecord.serializer()
    )

    @Test
    fun testDoubleQuote() = Csv(
        CsvConfiguration(
            quoteMode = ALL,
            quoteChar = '"'
        )
    ).assertEncodeAndDecode(
        "\"1\"",
        IntRecord(1),
        IntRecord.serializer()
    )

    @Test
    fun testSingleQuote() = Csv(
        CsvConfiguration(
            quoteMode = ALL,
            quoteChar = '\''
        )
    ).assertEncodeAndDecode(
        "'1'",
        IntRecord(1),
        IntRecord.serializer()
    )

    @Test
    fun testBang() = Csv(
        CsvConfiguration(
            quoteMode = ALL,
            quoteChar = '!'
        )
    ).assertEncodeAndDecode(
        "!1!",
        IntRecord(1),
        IntRecord.serializer()
    )

    @Test
    fun testEscapingOfQuoteChar() = Csv(
        CsvConfiguration(
            quoteMode = ALL,
            quoteChar = '\''
        )
    ).assertEncodeAndDecode(
        "'a''b'",
        StringRecord("a'b"),
        StringRecord.serializer()
    )
}