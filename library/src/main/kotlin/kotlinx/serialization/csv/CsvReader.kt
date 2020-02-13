package kotlinx.serialization.csv

internal class CsvReader(private val source: Source, private val configuration: CsvConfiguration) {

    var recordNo = 0
        private set

    val isFirstRecord: Boolean
        get() = recordNo == 0

    val isDone: Boolean
        get() = !source.canRead()

    fun readColumn(): String {
        return readSimpleColumn()
    }

    private fun readSimpleColumn(): String {
        val value = StringBuilder()

        val delimiter = configuration.delimiter
        val escapeChar = configuration.escapeChar
        val quoteChar = configuration.quoteChar

        while (source.canRead()) {
            val char = source.read()
            if (char == null) {
                break
            } else if (readRecordSeparator(char)) {
                recordNo++
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

    // FIXME Should only read Whitespace
    private fun readWhitespace() {
        val value = StringBuilder()

        val delimiter = configuration.delimiter

        while (source.canRead()) {
            val char = source.read()
            if (char == null) {
                break
            } else if (readRecordSeparator(char)) {
                recordNo++
                break
            } else if (char == delimiter) {
                break
            }

            // Append current char
            value.append(char.toChar())
        }
    }

    private fun readRecordSeparator(char: Char): Boolean {
        val sep0 = configuration.recordSeparator[0]
        val sep1 = configuration.recordSeparator.getOrNull(1)
        if (char == sep0 && (sep1 == null || source.peek() == sep1)) {
            if (sep1 != null) {
                source.read()
            }
            return true
        }
        return false
    }

    private fun readEscaped(): Char? =
            when (val char = source.read()) {
                't' -> '\t'
                'r' -> '\r'
                'n' -> '\n'
                'b' -> '\b'
                else -> char
            }

    fun readEmptyLines() {
        if (configuration.ignoreEmptyLines) {
            while (read(configuration.recordSeparator)) {
            }
            if (source.peek() == null) {
                source.read()
            }
        }
    }

    fun readEndOfRecord() {
        if (source.peek() == null) {
            source.read()
        } else {
            read(configuration.recordSeparator)
        }
    }

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

    fun mark() {
        source.mark()
    }

    fun unmark() {
        source.unmark()
    }

    fun reset() {
        source.reset()
    }

    fun isNullToken(): Boolean {
        source.mark()
        val isNull = readColumn() == configuration.nullString
        source.reset()
        return isNull
    }

    private fun String.rangeEquals(start: Int, pattern: String): Boolean {
        if (start < 0 || start + pattern.length >= length) return false
        for (i in pattern.indices) {
            if (this[start + i] != pattern[i]) {
                return false
            }
        }
        return true
    }

    override fun toString(): String {
        return "CsvReader(line=$recordNo, source='$source')"
    }
}
