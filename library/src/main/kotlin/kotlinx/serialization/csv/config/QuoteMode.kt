package kotlinx.serialization.csv.config

/** Defines quoting behavior when printing. */
enum class QuoteMode {
    /**
     * Quotes *all* fields.
     */
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

    /**
     * *Never* quotes fields (requires [CsvConfig.escapeChar] to be set).
     */
    NONE
}
