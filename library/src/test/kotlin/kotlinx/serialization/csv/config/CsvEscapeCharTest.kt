package kotlinx.serialization.csv.config

import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.csv.Csv
import kotlinx.serialization.csv.config.QuoteMode.NONE
import kotlinx.serialization.csv.records.StringRecord
import kotlinx.serialization.test.assertDecode
import kotlinx.serialization.test.assertEncodeAndDecode
import kotlin.test.Test

/**
 * Test [Csv] with different [CsvConfig.escapeChar]s.
 */
@OptIn(ExperimentalSerializationApi::class)
class CsvEscapeCharTest {

    @Test
    fun testSlash() = Csv {
        quoteMode = NONE
        escapeChar = '\\'
    }.assertEncodeAndDecode(
        "a\\\"b",
        StringRecord("a\"b"),
        StringRecord.serializer()
    )

    @Test
    fun testBang() = Csv {
        quoteMode = NONE
        escapeChar = '!'
    }.assertEncodeAndDecode(
        "a!\"b",
        StringRecord("a\"b"),
        StringRecord.serializer()
    )

    @Test
    fun testEscapedDelimiter() = Csv {
        quoteMode = NONE
        escapeChar = '\\'
    }.assertEncodeAndDecode(
        """test\,ing""",
        StringRecord("test,ing"),
        StringRecord.serializer()
    )

    @Test
    fun testEscapedEscapeChar() = Csv {
        quoteMode = NONE
        escapeChar = '\\'
    }.assertEncodeAndDecode(
        """test\\ing""",
        StringRecord("""test\ing"""),
        StringRecord.serializer()
    )

    @Test
    fun testParseEscapedEscapeChar() = Csv {
        escapeChar = '\\'
    }.assertDecode(
        """test\\ing""",
        StringRecord("""test\ing"""),
        StringRecord.serializer()
    )

    @Test
    fun testEscapedNewLine() = Csv {
        quoteMode = NONE
        escapeChar = '\\'
    }.assertEncodeAndDecode(
        """test\ning""",
        StringRecord("test\ning"),
        StringRecord.serializer()
    )

    @Test
    fun testParseEscapedNewLine() = Csv {
        escapeChar = '\\'
    }.assertDecode(
        """test\ning""",
        StringRecord("test\ning"),
        StringRecord.serializer()
    )

    @Test
    fun testTab() = Csv {
        delimiter = '\t'
        quoteMode = NONE
        escapeChar = '\\'
    }.assertEncodeAndDecode(
        """test\ting""",
        StringRecord("test\ting"),
        StringRecord.serializer()
    )

    @Test
    fun testParseEscapedTab() = Csv {
        escapeChar = '\\'
    }.assertDecode(
        """test\ting""",
        StringRecord("test\ting"),
        StringRecord.serializer()
    )
}
