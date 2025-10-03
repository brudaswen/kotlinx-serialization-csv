package kotlinx.serialization.csv.decode

import java.io.StringReader
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertNull
import kotlin.test.assertTrue

class CharStreamSourceTest {

    @Test
    fun testCanRead() {
        val source = CharStreamSource("")
        assertTrue(source.canRead())
    }

    @Test
    fun testNotCanRead() {
        val source = CharStreamSource("")
        source.read()
        assertFalse(source.canRead())
    }

    @Test
    fun testRead() {
        val source = CharStreamSource("abc")
        assertEquals('a', source.read())
        assertEquals('b', source.read())
        assertEquals('c', source.read())
        assertNull(source.read())
    }

    @Test
    fun testReadEof() {
        val source = CharStreamSource("")
        assertNull(source.read())
    }

    @Test
    fun testPeek() {
        val source = CharStreamSource("abc")
        assertEquals('a', source.peek())
    }

    @Test
    fun testPeekMultipleTimes() {
        val source = CharStreamSource("abc")
        assertEquals('a', source.peek())
        assertEquals('a', source.peek())
        assertEquals('a', source.peek())
    }

    @Test
    fun testPeekEof() {
        val source = CharStreamSource("")
        assertNull(source.peek())
    }

    @Test
    fun testMarkUnmark() {
        val source = CharStreamSource("abc")
        assertEquals('a', source.read())
        source.mark()
        assertEquals('b', source.read())
        source.unmark()
        assertEquals('c', source.read())
    }

    @Test
    fun testMarkMarkUnmarkReset() {
        val source = CharStreamSource("0123456789")
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
        val source = CharStreamSource("abc")
        assertEquals('a', source.read())
        source.mark()
        assertEquals('b', source.read())
        source.reset()
        assertEquals('b', source.read())
    }

    @Test
    fun testMarkResetMultiple() {
        val source = CharStreamSource("abcdef")
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
        val source = CharStreamSource("abc")
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

@Suppress("TestFunctionName")
private fun CharStreamSource(
    string: String,
): CharStreamSource = CharStreamSource(
    reader = StringReader(string)
)
