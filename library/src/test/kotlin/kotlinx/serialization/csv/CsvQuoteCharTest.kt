package kotlinx.serialization.csv

import kotlinx.serialization.csv.CsvConfiguration.QuoteMode.ALL
import kotlinx.serialization.test.assertStringFormAndRestored
import kotlin.test.Test

/**
 * Test [Csv] ([CsvEncoder] and [CsvDecoder]) with different [CsvConfiguration.quoteChar]s.
 */
class CsvQuoteCharTest {

    @Test
    fun testDefault() = assertStringFormAndRestored(
            "\"1\"",
            IntRecord(1),
            IntRecord.serializer(),
            Csv(CsvConfiguration(quoteMode = ALL))
    )

    @Test
    fun testDoubleQuote() = assertStringFormAndRestored(
            "\"1\"",
            IntRecord(1),
            IntRecord.serializer(),
            Csv(CsvConfiguration(quoteMode = ALL, quoteChar = '"'))
    )

    @Test
    fun testSingleQuote() = assertStringFormAndRestored(
            "'1'",
            IntRecord(1),
            IntRecord.serializer(),
            Csv(CsvConfiguration(quoteMode = ALL, quoteChar = '\''))
    )

    @Test
    fun testBang() = assertStringFormAndRestored(
            "!1!",
            IntRecord(1),
            IntRecord.serializer(),
            Csv(CsvConfiguration(quoteMode = ALL, quoteChar = '!'))
    )

    @Test
    fun testEscapingOfQuoteChar() = assertStringFormAndRestored(
            "'a''b'",
            StringRecord("a'b"),
            StringRecord.serializer(),
            Csv(CsvConfiguration(quoteMode = ALL, quoteChar = '\''))
    )
}