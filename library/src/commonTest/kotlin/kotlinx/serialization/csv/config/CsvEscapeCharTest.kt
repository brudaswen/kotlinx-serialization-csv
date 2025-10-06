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
        expected = "a\\\"b",
        original = StringRecord("a\"b"),
        serializer = StringRecord.serializer(),
    )

    @Test
    fun testBang() = Csv {
        quoteMode = NONE
        escapeChar = '!'
    }.assertEncodeAndDecode(
        expected = "a!\"b",
        original = StringRecord("a\"b"),
        serializer = StringRecord.serializer(),
    )

    @Test
    fun testEscapedDelimiter() = Csv {
        quoteMode = NONE
        escapeChar = '\\'
    }.assertEncodeAndDecode(
        expected = """test\,ing""",
        original = StringRecord("test,ing"),
        serializer = StringRecord.serializer(),
    )

    @Test
    fun testEscapedEscapeChar() = Csv {
        quoteMode = NONE
        escapeChar = '\\'
    }.assertEncodeAndDecode(
        expected = """test\\ing""",
        original = StringRecord("""test\ing"""),
        serializer = StringRecord.serializer(),
    )

    @Test
    fun testParseEscapedEscapeChar() = Csv {
        escapeChar = '\\'
    }.assertDecode(
        input = """test\\ing""",
        expected = StringRecord("""test\ing"""),
        serializer = StringRecord.serializer(),
    )

    @Test
    fun testEscapedNewLine() = Csv {
        quoteMode = NONE
        escapeChar = '\\'
    }.assertEncodeAndDecode(
        expected = """test\ning""",
        original = StringRecord("test\ning"),
        serializer = StringRecord.serializer(),
    )

    @Test
    fun testParseEscapedNewLine() = Csv {
        escapeChar = '\\'
    }.assertDecode(
        input = """test\ning""",
        expected = StringRecord("test\ning"),
        serializer = StringRecord.serializer(),
    )

    @Test
    fun testTab() = Csv {
        delimiter = '\t'
        quoteMode = NONE
        escapeChar = '\\'
    }.assertEncodeAndDecode(
        expected = """test\ting""",
        original = StringRecord("test\ting"),
        serializer = StringRecord.serializer(),
    )

    @Test
    fun testParseEscapedTab() = Csv {
        escapeChar = '\\'
    }.assertDecode(
        input = """test\ting""",
        expected = StringRecord("test\ting"),
        serializer = StringRecord.serializer(),
    )
}
