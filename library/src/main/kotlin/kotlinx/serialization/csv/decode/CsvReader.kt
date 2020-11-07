package kotlinx.serialization.csv.decode

import kotlinx.serialization.csv.CsvConfiguration

/**
 * Reader that parses CSV input.
 */
internal class CsvReader(private val source: Source, private val configuration: CsvConfiguration) {

    val offset
        get() = source.offset

    var recordNo = 0
        private set

    val isFirstRecord: Boolean
        get() = recordNo == 0

    val isDone: Boolean
        get() = !source.canRead()

    private var marks = arrayListOf<Int>()

    /**
     * Read value in the next column.
     */
    fun readColumn(): String {
        val value = StringBuilder()

        val delimiter = configuration.delimiter
        val escapeChar = configuration.escapeChar
        val quoteChar = configuration.quoteChar

        while (source.canRead()) {
            if (read(configuration.recordSeparator)) {
                recordNo++
                break
            }

            val char = source.read()
            if (char == null) {
                break
            } else if (char == escapeChar) {
                value.append(readEscaped())
                continue
            } else if (char == delimiter) {
                break
            } else if (char == quoteChar && value.isBlank()) {
                value.clear()
                val quoted = readQuotedColumn()
                readWhitespace()
                return quoted
            }

            // Append current char
            value.append(char.toChar())
        }

        return value.toString()
    }

    /**
     * Read quoted value until quoting is closed.
     */
    private fun readQuotedColumn(): String {
        val value = StringBuilder()

        val escapeChar = configuration.escapeChar
        val quoteChar = configuration.quoteChar

        while (source.canRead()) {
            val char = source.read()
            if (char == null) {
                break
            } else if (char == escapeChar) {
                val escaped = readEscaped()
                if (escaped != null) {
                    value.append(escaped)
                }
                continue
            } else if (char == quoteChar && source.peek() != quoteChar) {
                break
            } else if (char == quoteChar && source.peek() == quoteChar) {
                source.read()
                value.append(quoteChar)
                continue
            }

            // Append current char
            value.append(char.toChar())
        }

        return value.toString()
    }

    /**
     * Read white-space characters until non-white-space character appears or until end-of-record.
     */
    private fun readWhitespace() {
        while (source.canRead()) {
            if (read(configuration.recordSeparator)) {
                recordNo++
                break
            }

            val char = source.read()
            if (char == null || !char.isWhitespace()) {
                break
            }
        }
    }

    /**
     * Read next char in escape mode.
     *
     * `t`, `r`, `n` and `b` will get converted to `\t` (tab), `\r` (carriage return),
     * `\n` (line feed) and `\b (backspace)`.
     */
    private fun readEscaped(): Char? =
        when (val char = source.read()) {
            't' -> '\t'
            'r' -> '\r'
            'n' -> '\n'
            'b' -> '\b'
            else -> char
        }

    /** Read empty lines from the stream. */
    fun readEmptyLines() {
        if (configuration.ignoreEmptyLines) {
            while (read(configuration.recordSeparator)) {
                // Read all empty lines
            }
            if (source.peek() == null) {
                source.read()
            }
        }
    }

    /** Read end of CSV record (which is either the record separator or EOF). */
    fun readEndOfRecord() {
        if (source.peek() == null) {
            source.read()
        } else {
            read(configuration.recordSeparator)
        }
    }

    /**
     * Read the given [expected] value from the stream. Or do nothing if the next chars do not match.
     * @return True if the [expected] value was read; false otherwise.
     */
    private fun read(expected: String): Boolean {
        source.mark()
        for (i in expected.indices) {
            val char = source.read()
            if (char != expected[i]) {
                source.reset()
                return false
            }
        }

        source.unmark()
        return true
    }

    /**
     * Mark the current position in the stream. Calling [reset] afterwards resets the stream to this marked position.
     *
     * Calling [mark] must be proceeded by a call to [reset] or [unmark].
     */
    fun mark() {
        source.mark()
        marks.add(recordNo)
    }

    /**
     * Remove the last [mark] without resetting the stream to the marked position.
     */
    fun unmark() {
        source.unmark()
        marks.removeAt(marks.size - 1)
    }

    /**
     * Reset the stream to the last [mark]ed position (and remove the mark).
     */
    fun reset() {
        source.reset()
        recordNo = marks.removeAt(marks.size - 1)
    }

    /**
     * Check is the next column value is `null`.
     * @return True if the next column value is `null`; false otherwise.
     */
    fun isNullToken(): Boolean {
        source.mark()
        val isNull = readColumn() == configuration.nullString
        source.reset()
        return isNull
    }

    override fun toString() = "CsvReader(line=$recordNo, source='$source')"
}
