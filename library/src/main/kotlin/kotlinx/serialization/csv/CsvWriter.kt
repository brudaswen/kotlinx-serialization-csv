package kotlinx.serialization.csv

/**
 * Writer to generate CSV output.
 *
 * To write one CSV record, call [beginRecord], followed by multiple calls to [printColumn] and
 * finally call [endRecord] to finish the record.
 */
internal class CsvWriter(private val sb: Appendable, private val configuration: CsvConfiguration) {

    var isFirstRecord = true
    private var isFirstColumn = true

    /**
     * Start a new CSV record. Directly prints the record separator and all proceeding
     * calls to [printColumn] will be in the new line.
     */
    fun beginRecord() {
        if (!isFirstRecord) {
            sb.append(configuration.recordSeparator)
        }
    }

    /**
     * End the current CSV record (line). Depending on current configuration,
     * this may print a trailing column delimiter.
     */
    fun endRecord() {
        if (configuration.hasTrailingDelimiter) {
            nextColumn()
        }
        isFirstRecord = false
        isFirstColumn = true
    }

    /**
     * Write column value. The [value] gets escaped or quoted depending on the current configuration.
     *
     * @param value The value for this column.
     * @param isNumeric Set to true if the given value is representing a number.
     * @param isNull Set to true if the given value is representing `null`.
     */
    fun printColumn(value: String, isNumeric: Boolean = false, isNull: Boolean = false) {
        nextColumn()

        val delimiter = configuration.delimiter
        val recordSeparator = configuration.recordSeparator
        val quoteChar = configuration.quoteChar
        val escapeChar = configuration.escapeChar

        val mode: WriteMode = when (configuration.quoteMode) {
            CsvConfiguration.QuoteMode.ALL -> WriteMode.QUOTED
            CsvConfiguration.QuoteMode.ALL_NON_NULL -> if (!isNull || requiresQuoting(value)) WriteMode.QUOTED else WriteMode.PLAIN
            CsvConfiguration.QuoteMode.ALL_NON_NUMERIC -> if (!isNumeric || requiresQuoting(value)) WriteMode.QUOTED else WriteMode.PLAIN
            CsvConfiguration.QuoteMode.MINIMAL -> if (requiresQuoting(value)) WriteMode.QUOTED else WriteMode.PLAIN
            CsvConfiguration.QuoteMode.NONE -> WriteMode.ESCAPED
        }

        if (mode == WriteMode.ESCAPED && escapeChar != null) {
            val escapedValue = value.escape("$escapeChar$delimiter$quoteChar$recordSeparator", escapeChar = escapeChar)
            sb.append(escapedValue)
        } else if (mode == WriteMode.QUOTED || mode == WriteMode.ESCAPED) {
            val escapedValue = value.replace("$quoteChar", "$quoteChar$quoteChar")
            sb.append(quoteChar).append(escapedValue).append(quoteChar)
        } else {
            sb.append(value)
        }
    }

    /** End the current column (which writes the column delimiter). */
    private fun nextColumn() {
        if (!isFirstColumn) {
            sb.append(configuration.delimiter)
        }
        isFirstColumn = false
    }

    /** Check if given [value] contains reserved chars that require quoting. */
    private fun requiresQuoting(value: String): Boolean {
        val chars = "${configuration.delimiter}${configuration.quoteChar}${configuration.recordSeparator}"
        return value.contains("[${Regex.escape(chars)}]".toRegex())
    }

    /** Escape all [escapeCharacters] in this [String] using [escapeChar]. */
    private fun String.escape(escapeCharacters: String, escapeChar: Char) =
        fold(StringBuilder()) { builder, char ->
            if (escapeCharacters.contains(char)) {
                builder.append(char.escape(escapeChar))
            } else {
                builder.append(char)
            }
        }.toString()

    /** Escape this [Char] using [escapeChar]. */
    private fun Char.escape(escapeChar: Char): String =
        when (this) {
            '\t' -> "${escapeChar}t"
            '\r' -> "${escapeChar}r"
            '\n' -> "${escapeChar}n"
            '\b' -> "${escapeChar}b"
            else -> "${escapeChar}$this"
        }

    /** Mode for writing values that defines if quoting or escaping should be used. */
    private enum class WriteMode {
        /** Write quoted value. */
        QUOTED,
        /** Write value and escape reserved chars. */
        ESCAPED,
        /** Write plain value without any quoting or escaping. */
        PLAIN
    }
}
