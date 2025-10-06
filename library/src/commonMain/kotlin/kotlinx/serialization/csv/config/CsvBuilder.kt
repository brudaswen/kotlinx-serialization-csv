package kotlinx.serialization.csv.config

import kotlinx.serialization.csv.Csv
import kotlinx.serialization.modules.SerializersModule

public class CsvBuilder internal constructor(
    config: CsvConfig = CsvConfig.Default,
) {

    /**
     * The delimiter character between columns (default: `,`).
     */
    public var delimiter: Char = config.delimiter

    /**
     * The record separator (default: `\n`).
     */
    public var recordSeparator: String = config.recordSeparator

    /**
     * The quote character used to quote column values (default: `"`).
     */
    public var quoteChar: Char = config.quoteChar

    /**
     * The quote mode used to decide if a column value should get quoted (default: [QuoteMode.MINIMAL]).
     */
    public var quoteMode: QuoteMode = config.quoteMode

    /**
     * The escape character used to escape reserved characters in a column value.
     */
    public var escapeChar: Char? = config.escapeChar

    /**
     * The value to identify `null` values (default: empty string).
     */
    public var nullString: String = config.nullString

    /**
     * Ignore empty lines during parsing (default: `true`).
     */
    public var ignoreEmptyLines: Boolean = config.ignoreEmptyLines

    /**
     * First line is header record (default: `false`).
     */
    public var hasHeaderRecord: Boolean = config.hasHeaderRecord

    /**
     * Character that is used to separate hierarchical header names (default: `.`).
     */
    public var headerSeparator: Char = config.headerSeparator

    /**
     * Ignore unknown columns when `hasHeaderRecord` is enabled (default: `false`).
     */
    public var ignoreUnknownColumns: Boolean = config.ignoreUnknownColumns

    /**
     * If records end with a trailing [delimiter] (default: `false`).
     */
    public var hasTrailingDelimiter: Boolean = config.hasTrailingDelimiter

    /**
     * Use short value class header name (default: `false`).
     */
    public var shortValueClassHeaderName: Boolean = config.shortValueClassHeaderName

    /**
     * Module with contextual and polymorphic serializers to be used in the resulting [Csv] instance.
     */
    public var serializersModule: SerializersModule = config.serializersModule

    internal fun build(): CsvConfig {
        require(delimiter != quoteChar) {
            "The quoteChar character and the delimiter cannot be the same ('$delimiter')."
        }
        require(delimiter != escapeChar) {
            "The escapeChar character and the delimiter cannot be the same ('$delimiter')."
        }

        if (quoteMode == QuoteMode.NONE) {
            require(escapeChar != null) {
                "The QuoteMode NONE requires an escapeChar."
            }
        }

        require(recordSeparator.isNotEmpty()) {
            "The recordSeparator can not be empty ('$recordSeparator')."
        }
        require(recordSeparator.length <= 2) {
            "The recordSeparator can contain at most two characters ('$recordSeparator')."
        }
        require(!recordSeparator.contains(delimiter)) {
            "The recordSeparator can not contain the delimiter ('$recordSeparator')."
        }
        require(!recordSeparator.contains(quoteChar)) {
            "The recordSeparator can not contain the quoteChar ('$recordSeparator')."
        }
        escapeChar?.let {
            require(!recordSeparator.contains(it)) {
                "The recordSeparator can not contain the escapeChar ('$recordSeparator')."
            }
        }

        return CsvConfig(
            delimiter = delimiter,
            recordSeparator = recordSeparator,
            quoteChar = quoteChar,
            quoteMode = quoteMode,
            escapeChar = escapeChar,
            nullString = nullString,
            ignoreEmptyLines = ignoreEmptyLines,
            hasHeaderRecord = hasHeaderRecord,
            headerSeparator = headerSeparator,
            ignoreUnknownColumns = ignoreUnknownColumns,
            hasTrailingDelimiter = hasTrailingDelimiter,
            shortValueClassHeaderName = shortValueClassHeaderName,
            serializersModule = serializersModule,
        )
    }
}
