package kotlinx.serialization.csv

import kotlinx.serialization.csv.CsvConfiguration.QuoteMode.NONE
import kotlinx.serialization.test.assertParse
import kotlinx.serialization.test.assertStringFormAndRestored
import kotlin.test.Test

/**
 * Test [Csv] ([CsvEncoder] and [CsvDecoder]) with different [CsvConfiguration.escapeChar]s.
 */
class CsvEscapeCharTest {

    @Test
    fun testDefault() = assertStringFormAndRestored(
            "a\\\"b",
            StringRecord("a\"b"),
            StringRecord.serializer(),
            Csv(CsvConfiguration(quoteMode = NONE))
    )

    @Test
    fun testSlash() = assertStringFormAndRestored(
            "a\\\"b",
            StringRecord("a\"b"),
            StringRecord.serializer(),
            Csv(CsvConfiguration(quoteMode = NONE, escapeChar = '\\'))
    )

    @Test
    fun testBang() = assertStringFormAndRestored(
            "a!\"b",
            StringRecord("a\"b"),
            StringRecord.serializer(),
            Csv(CsvConfiguration(quoteMode = NONE, escapeChar = '!'))
    )

    @Test
    fun testEscapedDelimiter() = assertStringFormAndRestored(
            """test\,ing""",
            StringRecord("test,ing"),
            StringRecord.serializer(),
            Csv(CsvConfiguration(quoteMode = NONE, escapeChar = '\\'))
    )

    @Test
    fun testEscapedEscapeChar() = assertStringFormAndRestored(
            """test\\ing""",
            StringRecord("""test\ing"""),
            StringRecord.serializer(),
            Csv(CsvConfiguration(quoteMode = NONE, escapeChar = '\\'))
    )

    @Test
    fun testParseEscapedEscapeChar() = assertParse(
            """test\\ing""",
            StringRecord("""test\ing"""),
            StringRecord.serializer(),
            Csv(CsvConfiguration(escapeChar = '\\'))
    )

    @Test
    fun testEscapedNewLine() = assertStringFormAndRestored(
            """test\ning""",
            StringRecord("test\ning"),
            StringRecord.serializer(),
            Csv(CsvConfiguration(quoteMode = NONE, escapeChar = '\\'))
    )

    @Test
    fun testParseEscapedNewLine() = assertParse(
            """test\ning""",
            StringRecord("test\ning"),
            StringRecord.serializer(),
            Csv(CsvConfiguration(escapeChar = '\\'))
    )

    @Test
    fun testTab() = assertStringFormAndRestored(
            """test\ting""",
            StringRecord("test\ting"),
            StringRecord.serializer(),
            Csv(CsvConfiguration(delimiter = '\t', quoteMode = NONE, escapeChar = '\\'))
    )

    @Test
    fun testParseEscapedTab() = assertParse(
            """test\ting""",
            StringRecord("test\ting"),
            StringRecord.serializer(),
            Csv(CsvConfiguration(escapeChar = '\\'))
    )
}
