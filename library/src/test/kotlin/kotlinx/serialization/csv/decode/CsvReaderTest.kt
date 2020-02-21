package kotlinx.serialization.csv.decode

import kotlinx.serialization.csv.CsvConfiguration
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class CsvReaderTest {

    @Test
    fun testToString() {
        val reader = CsvReader(StringSource(""), CsvConfiguration.default)
        assertTrue(reader.toString().contains("CsvReader"))
    }

    @Test
    fun testReadColumns() {
        val csv = """
            |1,a
            |2,b
        """.trimMargin().replace("\n", "\r\n")
        val reader = CsvReader(StringSource(csv), CsvConfiguration.default)

        assertEquals("1", reader.readColumn())
        assertEquals("a", reader.readColumn())
        assertEquals("2", reader.readColumn())
        assertEquals("b", reader.readColumn())
    }

    @Test
    fun testRecordNo() {
        val csv = """
            |1,a
            |2,b
        """.trimMargin().replace("\n", "\r\n")
        val reader = CsvReader(StringSource(csv), CsvConfiguration.default)

        assertEquals(0, reader.recordNo)
        reader.readColumn()
        assertEquals(0, reader.recordNo)
        reader.readColumn()
        assertEquals(1, reader.recordNo)
        reader.readColumn()
        assertEquals(1, reader.recordNo)
    }

    @Test
    fun testIsFirstRecord() {
        val csv = """
            |1,a
            |2,b
        """.trimMargin().replace("\n", "\r\n")
        val reader = CsvReader(StringSource(csv), CsvConfiguration.default)

        assertTrue(reader.isFirstRecord)
        reader.readColumn()
        assertTrue(reader.isFirstRecord)
        reader.readColumn()
        assertFalse(reader.isFirstRecord)
    }

    @Test
    fun testIsDone() {
        val csv = """
            |1
        """.trimMargin().replace("\n", "\r\n")
        val reader = CsvReader(StringSource(csv), CsvConfiguration.default)

        assertFalse(reader.isDone)
        reader.readColumn()
        assertTrue(reader.isDone)
    }

    @Test
    fun testReadEscaped() {
        val csv = """
            |1,a\,b,2
        """.trimMargin().replace("\n", "\r\n")
        val reader = CsvReader(StringSource(csv), CsvConfiguration(escapeChar = '\\'))

        assertEquals("1", reader.readColumn())
        assertEquals("a,b", reader.readColumn())
        assertEquals("2", reader.readColumn())
    }

    @Test
    fun testReadQuoted() {
        val csv = """
            |"1","a"
            |"2","b"
        """.trimMargin().replace("\n", "\r\n")
        val reader = CsvReader(StringSource(csv), CsvConfiguration.default)

        assertEquals("1", reader.readColumn())
        assertEquals("a", reader.readColumn())
        assertEquals("2", reader.readColumn())
        assertEquals("b", reader.readColumn())
    }

    @Test
    fun testReadQuotedWithEscapedChar() {
        val csv = """
            |"\a"
        """.trimMargin().replace("\n", "\r\n")
        val reader = CsvReader(StringSource(csv), CsvConfiguration(escapeChar = '\\'))

        assertEquals("a", reader.readColumn())
    }

    @Test
    fun testReadQuotedWithEscapedTab() {
        val csv = """
            |"\t"
        """.trimMargin().replace("\n", "\r\n")
        val reader = CsvReader(StringSource(csv), CsvConfiguration(escapeChar = '\\'))

        assertEquals("\t", reader.readColumn())
    }

    @Test
    fun testReadQuotedWithEscapedLineFeed() {
        val csv = """
            |"\n"
        """.trimMargin().replace("\n", "\r\n")
        val reader = CsvReader(StringSource(csv), CsvConfiguration(escapeChar = '\\'))

        assertEquals("\n", reader.readColumn())
    }

    @Test
    fun testReadQuotedWithEscapedCarriageReturn() {
        val csv = """
            |"\r"
        """.trimMargin().replace("\n", "\r\n")
        val reader = CsvReader(StringSource(csv), CsvConfiguration(escapeChar = '\\'))

        assertEquals("\r", reader.readColumn())
    }

    @Test
    fun testReadQuotedWithEscapedBackspace() {
        val csv = """
            |"\b"
        """.trimMargin().replace("\n", "\r\n")
        val reader = CsvReader(StringSource(csv), CsvConfiguration(escapeChar = '\\'))

        assertEquals("\b", reader.readColumn())
    }

    @Test
    fun testReadQuotedWithQuotes() {
        val csv = """
            |"a""b"
        """.trimMargin().replace("\n", "\r\n")
        val reader = CsvReader(StringSource(csv), CsvConfiguration(escapeChar = '\\'))

        assertEquals("a\"b", reader.readColumn())
    }

    @Test
    fun testReadWithWhiteSpace() {
        val csv = """
            |  "1"  ,  "a"  
            |  "2"  ,  "b"  
        """.trimMargin().replace("\n", "\r\n")
        val reader = CsvReader(StringSource(csv), CsvConfiguration.default)

        assertEquals("1", reader.readColumn())
        assertEquals("a", reader.readColumn())
        assertEquals("2", reader.readColumn())
        assertEquals("b", reader.readColumn())
    }

    @Test
    fun testReadEmptyLines() {
        val csv = """
            |1
            |
            |
            |2
        """.trimMargin().replace("\n", "\r\n")
        val reader = CsvReader(StringSource(csv), CsvConfiguration.default)

        assertEquals("1", reader.readColumn())
        reader.readEmptyLines()
        assertEquals("2", reader.readColumn())
    }

    @Test
    fun testReadEmptyLinesAtEof() {
        val csv = """
            |1
            |
            |
        """.trimMargin().replace("\n", "\r\n")
        val reader = CsvReader(StringSource(csv), CsvConfiguration.default)

        assertEquals("1", reader.readColumn())
        reader.readEmptyLines()
        assertTrue(reader.isDone)
    }

    @Test
    fun testEndOfRecord() {
        val csv = """
            |1,
            |2,
        """.trimMargin().replace("\n", "\r\n")
        val reader = CsvReader(StringSource(csv), CsvConfiguration.default)

        assertEquals("1", reader.readColumn())
        reader.readEndOfRecord()
        assertEquals("2", reader.readColumn())
        reader.readEndOfRecord()
        assertTrue(reader.isDone)
    }

    @Test
    fun testMarkUnmark() {
        val csv = """
            |1,2,3
        """.trimMargin().replace("\n", "\r\n")
        val reader = CsvReader(StringSource(csv), CsvConfiguration.default)

        assertEquals("1", reader.readColumn())
        reader.mark()
        assertEquals("2", reader.readColumn())
        reader.unmark()
        assertEquals("3", reader.readColumn())
    }

    @Test
    fun testMarkMarkUnmarkReset() {
        val csv = """
            |1,2,3,4,5
        """.trimMargin().replace("\n", "\r\n")
        val reader = CsvReader(StringSource(csv), CsvConfiguration.default)

        assertEquals("1", reader.readColumn())
        reader.mark()
        assertEquals("2", reader.readColumn())
        reader.mark()
        assertEquals("3", reader.readColumn())
        reader.unmark()
        assertEquals("4", reader.readColumn())
        reader.reset()
        assertEquals("2", reader.readColumn())
    }

    @Test
    fun testIsNullToken() {
        val csv = """
            |1,null,3
        """.trimMargin().replace("\n", "\r\n")
        val reader = CsvReader(StringSource(csv), CsvConfiguration(nullString = "null"))

        assertFalse(reader.isNullToken())
        assertEquals("1", reader.readColumn())

        assertTrue(reader.isNullToken())
        assertEquals("null", reader.readColumn())

        assertFalse(reader.isNullToken())
        assertEquals("3", reader.readColumn())
    }
}
