package kotlinx.serialization.csv

import kotlinx.serialization.DeserializationStrategy
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.SerialFormat
import kotlinx.serialization.Serializable
import kotlinx.serialization.SerializationStrategy
import kotlinx.serialization.StringFormat
import kotlinx.serialization.csv.config.CsvBuilder
import kotlinx.serialization.csv.config.CsvConfig
import kotlinx.serialization.csv.decode.CsvReader
import kotlinx.serialization.csv.decode.FetchSource
import kotlinx.serialization.csv.decode.RootCsvDecoder
import kotlinx.serialization.csv.decode.Source
import kotlinx.serialization.csv.decode.StringSource
import kotlinx.serialization.csv.encode.RootCsvEncoder
import kotlinx.serialization.modules.SerializersModule
import java.io.Reader
import java.io.StringWriter

/**
 * The main entry point to work with CSV serialization.
 *
 * It is typically used by constructing an application-specific instance, with configured CSV-specific behaviour
 * and, if necessary, registered in [SerializersModule] custom serializers.
 * `Csv` instance can be configured in its `Csv {}` factory function using [CsvBuilder].
 * For demonstration purposes or trivial usages, Csv [companion][Csv.Default] can be used instead.
 *
 * Then constructed instance can be used either as regular [SerialFormat] or [StringFormat].
 */
@ExperimentalSerializationApi
sealed class Csv(val config: CsvConfig) : StringFormat {

    override val serializersModule: SerializersModule
        get() = config.serializersModule

    /**
     * Serialize [value] into CSV record(s).
     *
     * @param serializer The serializer used to serialize the given object.
     * @param value The [Serializable] object.
     */
    override fun <T> encodeToString(serializer: SerializationStrategy<T>, value: T): String =
        StringWriter().apply {
            encodeTo(serializer, value, this)
        }.toString()

    /**
     * Serialize [value] into CSV record(s).
     *
     * @param serializer The serializer used to serialize the given object.
     * @param value The [Serializable] object.
     * @param output The output where the CSV will be written.
     */
    fun <T> encodeTo(serializer: SerializationStrategy<T>, value: T, output: Appendable) {
        output.encode(serializer, value)
    }

    /**
     * Parse CSV [string] into [Serializable] object.
     *
     * @param deserializer The deserializer used to parse the given CSV string.
     * @param string The CSV string to parse.
     */
    override fun <T> decodeFromString(deserializer: DeserializationStrategy<T>, string: String): T =
        StringSource(string).decode(deserializer)

    /**
     * Parse CSV from the given [input] into [Serializable] object.
     *
     * @param deserializer The deserializer used to parse the given CSV string.
     * @param input The CSV input to parse.
     */
    fun <T> decodeFrom(deserializer: DeserializationStrategy<T>, input: Reader): T =
        FetchSource(input).decode(deserializer)

    /**
     * Serialize [value] into CSV record(s).
     *
     * @param serializer The serializer used to serialize the given object.
     * @param value The [Serializable] object.
     */
    private fun <T> Appendable.encode(serializer: SerializationStrategy<T>, value: T) {
        RootCsvEncoder(
            csv = this@Csv,
            output = this
        ).encodeSerializableValue(serializer, value)
    }

    /**
     * Parse CSV from [this] input into [Serializable] object.
     *
     * @param deserializer The deserializer used to parse the given CSV string.
     */
    private fun <T> Source.decode(deserializer: DeserializationStrategy<T>): T {
        val reader = CsvReader(
            source = this,
            config = config
        )

        return RootCsvDecoder(
            csv = this@Csv,
            reader = reader,
        ).decodeSerializableValue(deserializer).also {
            require(reader.isDone) { "Reader has not consumed the whole input: $reader" }
        }
    }

    internal class Impl(config: CsvConfig) : Csv(config)

    /**
     * Standard *Comma Separated Value* format.
     *
     * Settings are:
     * - [CsvConfig.delimiter] = `','`
     * - [CsvConfig.quoteChar] = `'"'`
     * - [CsvConfig.recordSeparator] = `"\n"`
     * - [CsvConfig.ignoreEmptyLines] = `true`
     */
    companion object Default : Csv(CsvConfig.Default) {

        /**
         * [RFC 4180](http://tools.ietf.org/html/rfc4180) *Comma Separated Value* format.
         *
         * Settings are:
         * - [CsvConfig.delimiter] = `','`
         * - [CsvConfig.quoteChar] = `'"'`
         * - [CsvConfig.recordSeparator] = `"\r\n"`
         * - [CsvConfig.ignoreEmptyLines] = `false`
         */
        val Rfc4180: Csv
            get() = Impl(CsvConfig.Rfc4180)
    }
}

/**
 * Creates an instance of [Csv] with adjusted configuration defined by [action].
 */
@ExperimentalSerializationApi
fun Csv(action: CsvBuilder.() -> Unit): Csv =
    Csv.configure(action)

/**
 * Creates a new instance of [Csv] based on the configuration of [this] and adjusted with [action].
 */
@ExperimentalSerializationApi
fun Csv.configure(action: CsvBuilder.() -> Unit): Csv =
    Csv.Impl(
        config = CsvBuilder(config).run {
            action()
            build()
        },
    )
