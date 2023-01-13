package kotlinx.serialization.csv.decode

import java.io.StringReader
import kotlin.test.*

class FetchSourceTest {
    
    fun make(string: String): FetchSource {
        return FetchSource(StringReader(string))
    }

    @Test
    fun testCanRead() {
        val source = make("")
        assertTrue(source.canRead())
    }

    @Test
    fun testNotCanRead() {
        val source = make("")
        source.read()
        assertFalse(source.canRead())
    }

    @Test
    fun testRead() {
        val source = make("abc")
        assertEquals('a', source.read())
        assertEquals('b', source.read())
        assertEquals('c', source.read())
        assertNull(source.read())
    }

    @Test
    fun testReadEof() {
        val source = make("")
        assertNull(source.read())
    }

    @Test
    fun testPeek() {
        val source = make("abc")
        assertEquals('a', source.peek())
    }

    @Test
    fun testPeekMultipleTimes() {
        val source = make("abc")
        assertEquals('a', source.peek())
        assertEquals('a', source.peek())
        assertEquals('a', source.peek())
    }

    @Test
    fun testPeekEof() {
        val source = make("")
        assertNull(source.peek())
    }

    @Test
    fun testMarkUnmark() {
        val source = make("abc")
        assertEquals('a', source.read())
        source.mark()
        assertEquals('b', source.read())
        source.unmark()
        assertEquals('c', source.read())
    }

    @Test
    fun testMarkMarkUnmarkReset() {
        val source = make("0123456789")
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
        val source = make("abc")
        assertEquals('a', source.read())
        source.mark()
        assertEquals('b', source.read())
        source.reset()
        assertEquals('b', source.read())
    }

    @Test
    fun testMarkResetMultiple() {
        val source = make("abcdef")
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
        val source = make("abc")
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

    private fun Source.read(expected: String): Boolean {
        println("premark - $offset")
        mark()
        for (i in expected.indices) {
            println("preread - $offset")
            val char = read()
            println("postread - $offset")
            if (char != expected[i]) {
                reset()
                println("reset - $offset")
                return false
            }
        }

        println("unmarking - $offset")
        unmark()
        println("unmarked - $offset")
        return true
    }
    private fun Source.readColumn(): String {
        val value = StringBuilder()

        val delimiter = ','
        val escapeChar = '\\'
        val recordSeparator = "\n"
        val quoteChar = '"'

        while (canRead()) {
            println("canRead at $offset")
            if (read(recordSeparator)) {
                break
            }

            val char = read()
            println("char: $char")
            if (char == null) {
                break
            } else if (char == escapeChar) {
                value.append(when (val char = read()) {
                    't' -> '\t'
                    'r' -> '\r'
                    'n' -> '\n'
                    'b' -> '\b'
                    else -> char
                })
                continue
            } else if (char == delimiter) {
                break
            } else if (char == quoteChar && value.isBlank()) {
                value.clear()
                val quoted = buildString {
                    while (canRead()) {
                        val char = read() ?: break
                        if (char == quoteChar && peek() != quoteChar) {
                            break
                        }

                        // Append current char
                        value.append(char.toChar())
                    }
                }
                while (canRead()) {
                    if (read(recordSeparator)) {
                        break
                    }

                    val char = read()
                    if (char == null || !char.isWhitespace()) {
                        break
                    }
                }
                return quoted
            }

            // Append current char
            value.append(char.toChar())
        }

        return value.toString()
    }


    @Test
    fun testRealWorld() {
                        // sssssssssssssssss
        val source = make("some,data,to,read")
        println(source.readColumn())
        source.read()
        assertEquals(0, source.queueSize)
//        source.read("some")
//        source.read()
//        source.read("some")
//        source.read()
    }
}