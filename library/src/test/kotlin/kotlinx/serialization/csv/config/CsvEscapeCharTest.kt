package kotlinx.serialization.csv.config

import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.csv.Csv
import kotlinx.serialization.csv.CsvConfiguration
import kotlinx.serialization.csv.CsvConfiguration.QuoteMode.NONE
import kotlinx.serialization.csv.records.StringRecord
import kotlinx.serialization.test.assertDecode
import kotlinx.serialization.test.assertEncodeAndDecode
import kotlin.test.Test

/**
 * Test [Csv] with different [CsvConfiguration.escapeChar]s.
 */
@OptIn(ExperimentalSerializationApi::class)
class CsvEscapeCharTest {

    @Test
    fun testDefault() = Csv(
        CsvConfiguration(
            quoteMode = NONE
        )
    ).assertEncodeAndDecode(
        "a\\\"b",
        StringRecord("a\"b"),
        StringRecord.serializer()
    )

    @Test
    fun testSlash() = Csv(
        CsvConfiguration(
            quoteMode = NONE,
            escapeChar = '\\'
        )
    ).assertEncodeAndDecode(
        "a\\\"b",
        StringRecord("a\"b"),
        StringRecord.serializer()
    )

    @Test
    fun testBang() = Csv(
        CsvConfiguration(
            quoteMode = NONE,
            escapeChar = '!'
        )
    ).assertEncodeAndDecode(
        "a!\"b",
        StringRecord("a\"b"),
        StringRecord.serializer()
    )

    @Test
    fun testEscapedDelimiter() = Csv(
        CsvConfiguration(
            quoteMode = NONE,
            escapeChar = '\\'
        )
    ).assertEncodeAndDecode(
        """test\,ing""",
        StringRecord("test,ing"),
        StringRecord.serializer()
    )

    @Test
    fun testEscapedEscapeChar() = Csv(
        CsvConfiguration(
            quoteMode = NONE,
            escapeChar = '\\'
        )
    ).assertEncodeAndDecode(
        """test\\ing""",
        StringRecord("""test\ing"""),
        StringRecord.serializer()
    )

    @Test
    fun testParseEscapedEscapeChar() = Csv(
        CsvConfiguration(
            escapeChar = '\\'
        )
    ).assertDecode(
        """test\\ing""",
        StringRecord("""test\ing"""),
        StringRecord.serializer()
    )

    @Test
    fun testEscapedNewLine() = Csv(
        CsvConfiguration(
            quoteMode = NONE,
            escapeChar = '\\'
        )
    ).assertEncodeAndDecode(
        """test\ning""",
        StringRecord("test\ning"),
        StringRecord.serializer()
    )

    @Test
    fun testParseEscapedNewLine() = Csv(
        CsvConfiguration(
            escapeChar = '\\'
        )
    ).assertDecode(
        """test\ning""",
        StringRecord("test\ning"),
        StringRecord.serializer()
    )

    @Test
    fun testTab() = Csv(
        CsvConfiguration(
            delimiter = '\t',
            quoteMode = NONE,
            escapeChar = '\\'
        )
    ).assertEncodeAndDecode(
        """test\ting""",
        StringRecord("test\ting"),
        StringRecord.serializer()
    )

    @Test
    fun testParseEscapedTab() = Csv(
        CsvConfiguration(
            escapeChar = '\\'
        )
    ).assertDecode(
        """test\ting""",
        StringRecord("test\ting"),
        StringRecord.serializer()
    )
}
