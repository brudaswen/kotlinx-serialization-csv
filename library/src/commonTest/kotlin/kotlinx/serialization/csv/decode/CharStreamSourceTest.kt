package kotlinx.serialization.csv.decode

import kotlinx.io.Buffer
import kotlinx.io.writeString
import kotlinx.serialization.csv.source.CsvSource
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertNull
import kotlin.test.assertTrue

class CharStreamSourceTest {

    @Test
    fun testCanRead() {
        val source = csvSourceFromCharStream("")
        assertTrue(source.canRead())
    }

    @Test
    fun testNotCanRead() {
        val source = csvSourceFromCharStream("")
        source.read()
        assertFalse(source.canRead())
    }

    @Test
    fun testRead() {
        val source = csvSourceFromCharStream("abc")
        assertEquals('a', source.read())
        assertEquals('b', source.read())
        assertEquals('c', source.read())
        assertNull(source.read())
    }

    @Test
    fun testReadEof() {
        val source = csvSourceFromCharStream("")
        assertNull(source.read())
    }

    @Test
    fun testPeek() {
        val source = csvSourceFromCharStream("abc")
        assertEquals('a', source.peek())
    }

    @Test
    fun testPeekMultipleTimes() {
        val source = csvSourceFromCharStream("abc")
        assertEquals('a', source.peek())
        assertEquals('a', source.peek())
        assertEquals('a', source.peek())
    }

    @Test
    fun testPeekEof() {
        val source = csvSourceFromCharStream("")
        assertNull(source.peek())
    }

    @Test
    fun testMarkUnmark() {
        val source = csvSourceFromCharStream("abc")
        assertEquals('a', source.read())
        source.mark()
        assertEquals('b', source.read())
        source.unmark()
        assertEquals('c', source.read())
    }

    @Test
    fun testMarkMarkUnmarkReset() {
        val source = csvSourceFromCharStream("0123456789")
        assertEquals('0', source.read())
        source.mark()
        assertEquals('1', source.read())
        source.mark()
        assertEquals('2', source.read())
        source.unmark()
        assertEquals('3', source.read())
        source.reset()
        assertEquals('1', source.read())
    }

    @Test
    fun testMarkReset() {
        val source = csvSourceFromCharStream("abc")
        assertEquals('a', source.read())
        source.mark()
        assertEquals('b', source.read())
        source.reset()
        assertEquals('b', source.read())
    }

    @Test
    fun testMarkResetMultiple() {
        val source = csvSourceFromCharStream("abcdef")
        assertEquals('a', source.read())
        source.mark()
        assertEquals('b', source.read())
        source.mark()
        assertEquals('c', source.read())
        source.reset()
        assertEquals('c', source.read())
        source.reset()
        assertEquals('b', source.read())
    }

    @Test
    fun testMarkPeekRead() {
        val source = csvSourceFromCharStream("abc")
        assertEquals('a', source.read())
        source.mark()
        assertEquals('b', source.peek())
        assertEquals('b', source.read())
        source.reset()
        assertEquals('b', source.peek())
        assertEquals('b', source.read())
        source.mark()
        assertEquals('c', source.peek())
        assertEquals('c', source.read())
        source.reset()
        assertEquals('c', source.peek())
        assertEquals('c', source.read())
    }
}

private fun csvSourceFromCharStream(
    string: String,
): CsvSource = CsvSource(
    input = Buffer().apply {
        writeString(string)
    },
)
