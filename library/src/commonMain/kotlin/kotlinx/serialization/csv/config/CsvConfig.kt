package kotlinx.serialization.csv.config

import kotlinx.serialization.modules.EmptySerializersModule
import kotlinx.serialization.modules.SerializersModule

/**
 * CSV serialization/parsing settings.
 *
 * @param delimiter The delimiter character between columns (default: `,`).
 * @param recordSeparator The record separator (default: `\n`).
 * @param quoteChar The quote character used to quote column values (default: `"`).
 * @param quoteMode The quote mode used to decide if a column value should get quoted (default: [QuoteMode.MINIMAL]).
 * @param escapeChar The escape character used to escape reserved characters in a column value.
 * @param nullString The value to identify `null` values (default: empty string).
 * @param ignoreEmptyLines Ignore empty lines during parsing (default: `true`).
 * @param hasHeaderRecord First line is header record (default: `false`).
 * @param headerSeparator Character that is used to separate hierarchical header names (default: `.`).
 * @param ignoreUnknownColumns Ignore unknown columns when `hasHeaderRecord` is enabled (default: `false`).
 * @param hasTrailingDelimiter If records end with a trailing [delimiter] (default: `false`).
 * @param shortValueClassHeaderName Use short value class header name (default: `false`).
 */
public data class CsvConfig(
    val delimiter: Char = ',',
    val recordSeparator: String = "\n",
    val quoteChar: Char = '"',
    val quoteMode: QuoteMode = QuoteMode.MINIMAL,
    val escapeChar: Char? = null,
    val nullString: String = "",
    val ignoreEmptyLines: Boolean = true,
    val hasHeaderRecord: Boolean = false,
    val headerSeparator: Char = '.',
    val ignoreUnknownColumns: Boolean = false,
    val hasTrailingDelimiter: Boolean = false,
    val shortValueClassHeaderName: Boolean = false,
    val serializersModule: SerializersModule = EmptySerializersModule(),
) {

    public companion object {
        /**
         * Standard *Comma Separated Value* format.
         */
        public val Default: CsvConfig
            get() = CsvConfig()

        /**
         * [RFC 4180](http://tools.ietf.org/html/rfc4180) *Comma Separated Value* format.
         */
        public val Rfc4180: CsvConfig
            get() = CsvConfig(
                recordSeparator = "\r\n",
                ignoreEmptyLines = false,
            )
    }
}
