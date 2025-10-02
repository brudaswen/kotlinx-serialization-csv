package kotlinx.serialization.csv.decode

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertNull
import kotlin.test.assertTrue

class StringSourceTest {

    @Test
    fun testCanRead() {
        val source = StringSource("")
        assertTrue(source.canRead())
    }

    @Test
    fun testNotCanRead() {
        val source = StringSource("")
        source.read()
        assertFalse(source.canRead())
    }

    @Test
    fun testRead() {
        val source = StringSource("abc")
        assertEquals('a', source.read())
        assertEquals('b', source.read())
        assertEquals('c', source.read())
        assertNull(source.read())
    }

    @Test
    fun testReadEof() {
        val source = StringSource("")
        assertNull(source.read())
    }

    @Test
    fun testPeek() {
        val source = StringSource("abc")
        assertEquals('a', source.peek())
    }

    @Test
    fun testPeekMultipleTimes() {
        val source = StringSource("abc")
        assertEquals('a', source.peek())
        assertEquals('a', source.peek())
        assertEquals('a', source.peek())
    }

    @Test
    fun testPeekEof() {
        val source = StringSource("")
        assertNull(source.peek())
    }

    @Test
    fun testMarkUnmark() {
        val source = StringSource("abc")
        assertEquals('a', source.read())
        source.mark()
        assertEquals('b', source.read())
        source.unmark()
        assertEquals('c', source.read())
    }

    @Test
    fun testMarkMarkUnmarkReset() {
        val source = StringSource("0123456789")
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
        val source = StringSource("abc")
        assertEquals('a', source.read())
        source.mark()
        assertEquals('b', source.read())
        source.reset()
        assertEquals('b', source.read())
    }

    @Test
    fun testMarkResetMultiple() {
        val source = StringSource("0123456789")
        assertEquals('0', source.read())
        source.mark()
        assertEquals('1', source.read())
        source.mark()
        assertEquals('2', source.read())
        source.reset()
        assertEquals('2', source.read())
        source.reset()
        assertEquals('1', source.read())
    }

    @Test
    fun testToString() {
        val source = StringSource("abc")
        assertTrue(source.toString().contains("StringSource"))
    }
}
