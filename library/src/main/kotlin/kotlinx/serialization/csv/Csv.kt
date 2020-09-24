package kotlinx.serialization.csv

import kotlinx.serialization.*
import kotlinx.serialization.csv.CsvConfiguration.Companion.rfc4180
import kotlinx.serialization.csv.decode.CsvReader
import kotlinx.serialization.csv.decode.RootCsvDecoder
import kotlinx.serialization.csv.decode.StringSource
import kotlinx.serialization.csv.encode.RootCsvEncoder
import kotlinx.serialization.modules.EmptySerializersModule
import kotlinx.serialization.modules.SerializersModule

/**
 * The main entry point to work with CSV serialization.
 *
 * It is typically used by constructing an application-specific instance, with configured CSV-specific behaviour
 * ([configuration] parameter) and, if necessary, registered
 * custom serializers (in [SerializersModule] provided by [context] parameter).
 *
 * Then constructed instance can be used either as regular [SerialFormat] or [StringFormat].
 *
 * @param configuration CSV settings used during parsing/serialization.
 * @param serializersModule Serialization module settings (e.g. custom serializers).
 */
@ExperimentalSerializationApi
class Csv(
    internal val configuration: CsvConfiguration,
    override val serializersModule: SerializersModule = EmptySerializersModule
) : SerialFormat, StringFormat {
    /**
     * Serialize [value] into CSV record(s).
     *
     * @param serializer The serializer used to serialize the given object.
     * @param value The [Serializable] object.
     */
    override fun <T> encodeToString(serializer: SerializationStrategy<T>, value: T): String {
        val result = StringBuilder()
        RootCsvEncoder(this, result).encodeSerializableValue(serializer, value)
        return result.toString()
    }

    /**
     * Parse CSV [string] into [Serializable] object.
     *
     * @param deserializer The deserializer used to parse the given CSV string.
     * @param string The CSV string to parse.
     */
    override fun <T> decodeFromString(deserializer: DeserializationStrategy<T>, string: String): T {
        val reader = CsvReader(StringSource(string), configuration)
        val input = RootCsvDecoder(this, reader)
        val result = input.decodeSerializableValue(deserializer)

        require(reader.isDone) { "Reader has not consumed the whole input: $reader" }
        return result
    }

    companion object : StringFormat {

        /**
         * Standard Comma Separated Value format, as for [rfc4180] but allowing empty lines.
         *
         * Settings are:
         * - [CsvConfiguration.delimiter] = `','`
         * - [CsvConfiguration.quoteChar] = `'"'`
         * - [CsvConfiguration.recordSeparator] = `"\r\n"`
         * - [CsvConfiguration.ignoreEmptyLines] = `true`
         */
        val default = Csv(CsvConfiguration.default)

        /**
         * Comma separated format as defined by [RFC 4180](http://tools.ietf.org/html/rfc4180).
         *
         * Settings are:
         * - [CsvConfiguration.delimiter] = `','`
         * - [CsvConfiguration.quoteChar] = `'"'`
         * - [CsvConfiguration.recordSeparator] = `"\r\n"`
         * - [CsvConfiguration.ignoreEmptyLines] = `false`
         */
        val rfc4180 = Csv(CsvConfiguration.rfc4180)

        override val serializersModule: SerializersModule
            get() = default.serializersModule

        /**
         * Serialize [value] into CSV record(s) using [CsvConfiguration.default].
         *
         * @param serializer The serializer used to serialize the given object.
         * @param value The [Serializable] object.
         */
        override fun <T> encodeToString(serializer: SerializationStrategy<T>, value: T): String =
            default.encodeToString(serializer, value)

        /**
         * Parse CSV [string] into [Serializable] object using [CsvConfiguration.default].
         *
         * @param deserializer The deserializer used to parse the given CSV string.
         * @param string The CSV string to parse.
         */
        override fun <T> decodeFromString(
            deserializer: DeserializationStrategy<T>,
            string: String
        ): T =
            default.decodeFromString(deserializer, string)
    }
}
