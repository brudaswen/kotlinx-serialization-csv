package kotlinx.serialization.csv.config

import kotlinx.serialization.csv.Csv
import kotlinx.serialization.modules.SerializersModule

public class CsvBuilder internal constructor(conf: CsvConfig = CsvConfig.Default) {

    /**
     * The delimiter character between columns (default: `,`).
     */
    public var delimiter: Char = conf.delimiter

    /**
     * The record separator (default: `\n`).
     */
    public var recordSeparator: String = conf.recordSeparator

    /**
     * The quote character used to quote column values (default: `"`).
     */
    public var quoteChar: Char = conf.quoteChar

    /**
     * The quote mode used to decide if a column value should get quoted (default: [QuoteMode.MINIMAL]).
     */
    public var quoteMode: QuoteMode = conf.quoteMode

    /**
     * The escape character used to escape reserved characters in a column value.
     */
    public var escapeChar: Char? = conf.escapeChar

    /**
     * The value to identify `null` values (default: empty string).
     */
    public var nullString: String = conf.nullString

    /**
     * Ignore empty lines during parsing (default: `true`).
     */
    public var ignoreEmptyLines: Boolean = conf.ignoreEmptyLines

    /**
     * First line is header record (default: `false`).
     */
    public var hasHeaderRecord: Boolean = conf.hasHeaderRecord

    /**
     * Character that is used to separate hierarchical header names (default: `.`).
     */
    public var headerSeparator: Char = conf.headerSeparator

    /**
     * Ignore unknown columns when `hasHeaderRecord` is enabled (default: `false`).
     */
    public var ignoreUnknownColumns: Boolean = conf.ignoreUnknownColumns

    /**
     * If records end with a trailing [delimiter] (default: `false`).
     */
    public var hasTrailingDelimiter: Boolean = conf.hasTrailingDelimiter

    /**
     * Module with contextual and polymorphic serializers to be used in the resulting [Csv] instance.
     */
    public var serializersModule: SerializersModule = conf.serializersModule

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
            delimiter,
            recordSeparator,
            quoteChar,
            quoteMode,
            escapeChar,
            nullString,
            ignoreEmptyLines,
            hasHeaderRecord,
            headerSeparator,
            ignoreUnknownColumns,
            hasTrailingDelimiter,
            serializersModule,
        )
    }
}
