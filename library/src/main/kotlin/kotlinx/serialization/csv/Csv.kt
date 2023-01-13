package kotlinx.serialization.csv

import kotlinx.serialization.*
import kotlinx.serialization.builtins.ListSerializer
import kotlinx.serialization.csv.config.CsvBuilder
import kotlinx.serialization.csv.config.CsvConfig
import kotlinx.serialization.csv.decode.*
import kotlinx.serialization.csv.decode.CsvReader
import kotlinx.serialization.csv.decode.RecordListCsvDecoder
import kotlinx.serialization.csv.decode.RootCsvDecoder
import kotlinx.serialization.csv.decode.StringSource
import kotlinx.serialization.csv.encode.CsvWriter
import kotlinx.serialization.csv.encode.RecordListCsvEncoder
import kotlinx.serialization.csv.encode.RootCsvEncoder
import kotlinx.serialization.encoding.CompositeDecoder.Companion.DECODE_DONE
import kotlinx.serialization.encoding.encodeStructure
import kotlinx.serialization.modules.SerializersModule
import java.io.Reader

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
sealed class Csv(internal val config: CsvConfig) : SerialFormat, StringFormat {

    override val serializersModule: SerializersModule
        get() = config.serializersModule

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
     * Serialize [value] into CSV record(s).
     *
     * @param serializer The serializer used to serialize the given object.
     * @param value The [Serializable] object.
     * @param appendable The output where the CSV will be written.
     */
    fun <T> encodeToAppendable(serializer: SerializationStrategy<T>, value: T, appendable: Appendable) {
        RootCsvEncoder(this, appendable).encodeSerializableValue(serializer, value)
    }

    /**
     * Serialize [values] into CSV record(s).
     *
     * @param serializer The serializer used to serialize the given object.
     * @param values The [Serializable] objects as a sequence.
     * @param appendable The output where the CSV will be written.
     */
    fun <T> encodeSequenceToAppendable(serializer: KSerializer<T>, values: Sequence<T>, appendable: Appendable) {
        val encoder = RecordListCsvEncoder(this, CsvWriter(appendable, config))
        val listDescriptor = ListSerializer(serializer).descriptor
        encoder.encodeStructure(listDescriptor) {
            var index = 0
            for (value in values) {
                encodeSerializableElement(listDescriptor, index++, serializer, value)
            }
        }
    }

    /**
     * Start serializing values into CSV record(s).
     *
     * @param serializer The serializer used to serialize the given object.
     * @param appendable The output where the CSV will be written.
     * @return A function that emits a new item to the CSV.
     */
    @ExperimentalSerializationApi
    fun <T> beginEncodingToAppendable(serializer: KSerializer<T>, appendable: Appendable): (T) -> Unit {
        val encoder = RecordListCsvEncoder(this, CsvWriter(appendable, config))
        val listDescriptor = ListSerializer(serializer).descriptor
        encoder.beginStructure(listDescriptor)
        var index = 0
        return {
            encoder.encodeSerializableElement(listDescriptor, index++, serializer, it)
        }
    }

    /**
     * Parse CSV [string] into [Serializable] object.
     *
     * @param deserializer The deserializer used to parse the given CSV string.
     * @param string The CSV string to parse.
     */
    override fun <T> decodeFromString(deserializer: DeserializationStrategy<T>, string: String): T {
        val reader = CsvReader(StringSource(string), config)
        val input = RootCsvDecoder(this, reader)
        val result = input.decodeSerializableValue(deserializer)

        require(reader.isDone) { "Reader has not consumed the whole input: $reader" }
        return result
    }

    /**
     * Parse CSV from the given [reader] into [Serializable] object.
     *
     * @param deserializer The deserializer used to parse the given CSV string.
     * @param reader The CSV reader to parse.
     */
    fun <T> decodeFromReader(deserializer: DeserializationStrategy<T>, reader: Reader): T {
        return reader.use {
            val csv = CsvReader(FetchSource(it), config)
            val input = RootCsvDecoder(this, csv)
            val result = input.decodeSerializableValue(deserializer)

            require(csv.isDone) { "Reader has not consumed the whole input: $csv" }
            result
        }
    }

    /**
     * Parse CSV line-by-line from the given [reader] into a sequence.
     *
     * @param deserializer The deserializer used to parse the given CSV string.
     * @param reader The CSV reader to parse.  This function *does not close the reader*.
     * @return A sequence of each element decoded.
     */
    @ExperimentalSerializationApi
    fun <T> decodeSequenceFromReader(deserializer: KSerializer<T>, reader: Reader): Sequence<T> {
        val csv = CsvReader(FetchSource(reader), config)
        val listDescriptor = ListSerializer(deserializer).descriptor
        val input = RecordListCsvDecoder(this, csv)
        val structure = input.beginStructure(listDescriptor)
        var previousValue: T? = null

        return generateSequence {
            val decodedIndex = structure.decodeElementIndex(listDescriptor)
            if (decodedIndex == DECODE_DONE) return@generateSequence null
            val nextValue =
                structure.decodeSerializableElement(listDescriptor, decodedIndex, deserializer, previousValue)
            previousValue = nextValue
            nextValue
        }
    }

    /**
     * Parse CSV from the given [reader] into a sequence of [Serializable] objects.
     * Designed to be comparable to [Reader.useLines].
     *
     * @param deserializer The deserializer used to parse the given CSV string.
     * @param reader The CSV reader to parse.
     * @param handler The code to handle the sequence of incoming values.  The sequence will not be available after the
     * function completes.
     */
    fun <T> decodeFromReaderUsingSequence(
        deserializer: KSerializer<T>,
        reader: Reader,
        handler: (Sequence<T>) -> Unit,
    ) {
        reader.use {
            handler(decodeSequenceFromReader(deserializer, reader))
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
 * Creates an instance of [Csv] configured from the optionally given [Csv instance][from] and
 * adjusted with [action].
 */
@ExperimentalSerializationApi
@Suppress("FunctionName")
fun Csv(from: Csv = Csv.Default, action: CsvBuilder.() -> Unit): Csv {
    val conf = CsvBuilder(from.config).run {
        action()
        build()
    }
    return Csv.Impl(conf)
}
