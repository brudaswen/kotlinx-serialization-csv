package kotlinx.serialization.csv.encode

import kotlinx.serialization.csv.config.CsvConfig
import kotlinx.serialization.csv.config.QuoteMode

/**
 * Writer to generate CSV output.
 *
 * To write one CSV record, call [beginRecord], followed by multiple calls to [printColumn] and
 * finally call [endRecord] to finish the record.
 */
internal class CsvWriter(
    private val output: Appendable,
    private val config: CsvConfig,
) {

    var isFirstRecord = true
    private var isFirstColumn = true

    /**
     * Start a new CSV record. Directly prints the record separator and all proceeding
     * calls to [printColumn] will be in the new line.
     */
    fun beginRecord() {
        if (!isFirstRecord) {
            output.append(config.recordSeparator)
        }
    }

    /**
     * End the current CSV record (line). Depending on current configuration,
     * this may print a trailing column delimiter.
     */
    fun endRecord() {
        if (config.hasTrailingDelimiter) {
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

        val delimiter = config.delimiter
        val recordSeparator = config.recordSeparator
        val quoteChar = config.quoteChar
        val escapeChar = config.escapeChar

        val mode: WriteMode = when (config.quoteMode) {
            QuoteMode.ALL -> WriteMode.QUOTED
            QuoteMode.ALL_NON_NULL -> if (!isNull || requiresQuoting(value)) WriteMode.QUOTED else WriteMode.PLAIN
            QuoteMode.ALL_NON_NUMERIC -> if (!isNumeric || requiresQuoting(value)) WriteMode.QUOTED else WriteMode.PLAIN
            QuoteMode.MINIMAL -> if (requiresQuoting(value)) WriteMode.QUOTED else WriteMode.PLAIN
            QuoteMode.NONE -> WriteMode.ESCAPED
        }

        if (mode == WriteMode.ESCAPED && escapeChar != null) {
            val escapedValue = value.escape(
                escapeCharacters = "$escapeChar$delimiter$quoteChar$recordSeparator",
                escapeChar = escapeChar,
            )
            output.append(escapedValue)
        } else if (mode == WriteMode.QUOTED || mode == WriteMode.ESCAPED) {
            val escapedValue = value.replace("$quoteChar", "$quoteChar$quoteChar")
            output.append(quoteChar).append(escapedValue).append(quoteChar)
        } else {
            output.append(value)
        }
    }

    /** End the current column (which writes the column delimiter). */
    private fun nextColumn() {
        if (!isFirstColumn) {
            output.append(config.delimiter)
        }
        isFirstColumn = false
    }

    /** Check if given [value] contains reserved chars that require quoting. */
    private fun requiresQuoting(value: String): Boolean {
        val chars = with(config) { "$delimiter$quoteChar$recordSeparator" }
        return value.contains("[${Regex.escape(chars)}]".toRegex())
    }

    /** Escape all [escapeCharacters] in this [String] using [escapeChar]. */
    private fun String.escape(escapeCharacters: String, escapeChar: Char) =
        fold(StringBuilder()) { builder, char ->
            when {
                escapeCharacters.contains(char) -> builder.append(char.escape(escapeChar))
                else -> builder.append(char)
            }
        }.toString()

    /** Escape this [Char] using [escapeChar]. */
    private fun Char.escape(escapeChar: Char): String = when (this) {
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
        PLAIN,
    }
}
