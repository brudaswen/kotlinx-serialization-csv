package kotlinx.serialization.csv

data class CsvConfiguration(
        val delimiter: Char = ',',
        val recordSeparator: String = "\r\n",
        val quoteChar: Char = '"',
        val quoteMode: QuoteMode = QuoteMode.MINIMAL,
        val escapeChar: Char? = if (quoteMode == QuoteMode.NONE) '\\' else null,
        val nullString: String = "",
        val unitString: String = Unit.toString(),
        val ignoreEmptyLines: Boolean = true,
        val hasHeaderRecord: Boolean = false,
        val headerSeparator: Char = '.',
        val hasTrailingDelimiter: Boolean = false
) {

    init {
        require(delimiter != quoteChar) {
            "The quoteChar character and the delimiter cannot be the same ('$delimiter')."
        }
        require(delimiter != escapeChar) {
            "The escapeChar character and the delimiter cannot be the same ('$delimiter')."
        }

        require(nullString != unitString) {
            "The nullString and the unitString cannot be the same ('$nullString')."
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
        if (escapeChar != null) {
            require(!recordSeparator.contains(escapeChar)) {
                "The recordSeparator can not contain the escapeChar ('$recordSeparator')."
            }
        }
    }

    /** Defines quoting behavior when printing. */
    enum class QuoteMode {
        /** Quotes *all* fields. */
        ALL,

        /**
         * Quotes all *non-null fields* and *fields which contain special characters* (such as a the field delimiter,
         * quote character or any of the characters in the line separator string).
         */
        ALL_NON_NULL,

        /**
         * Quotes all *non-numeric fields* and *fields which contain special characters* (such as a the field delimiter,
         * quote character or any of the characters in the line separator string).
         */
        ALL_NON_NUMERIC,

        /**
         * Quotes *fields which contain special characters* (such as a the field delimiter, quote character or any of
         * the characters in the line separator string).
         */
        MINIMAL,

        /** *Never* quotes fields (requires [CsvConfiguration.escapeChar] to be set). */
        NONE
    }

    companion object {
        /**
         * Standard Comma Separated Value format, as for [rfc4180] but allowing empty lines.
         *
         * Settings are:
         * - [delimiter] = `','`
         * - [quoteChar] = `'"'`
         * - [recordSeparator] = `"\r\n"`
         * - [ignoreEmptyLines] = `true`
         */
        val default = CsvConfiguration()

        /**
         * Comma separated format as defined by [RFC 4180](http://tools.ietf.org/html/rfc4180).
         *
         * Settings are:
         * - [delimiter] = `','`
         * - [quoteChar] = `'"'`
         * - [recordSeparator] = `"\r\n"`
         * - [ignoreEmptyLines] = `false`
         */
        val rfc4180 = default.copy(ignoreEmptyLines = false)

        /**
         * Excel file format (using a comma as the value delimiter). Note that the actual value delimiter used by Excel
         * is locale dependent, it might be necessary to customize this format to accommodate to your regional settings.
         *
         * For example for parsing or generating a CSV file on a French system the following format will be used:
         * ```
         * CsvConfiguration.excel.copy(delimiter = ';')
         * ```
         *
         * Settings are:
         * - [delimiter] = `','`
         * - [quoteChar] = `'"'`
         * - [recordSeparator] = `"\r\n"`
         * - [ignoreEmptyLines] = `false`
         */
        val excel = default.copy(ignoreEmptyLines = false)
    }
}
