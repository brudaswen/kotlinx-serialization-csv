package kotlinx.serialization.csv

import kotlinx.serialization.DeserializationStrategy
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.SerializationStrategy
import kotlinx.serialization.csv.sink.CsvSink
import kotlinx.serialization.csv.source.CsvSource
import java.io.InputStream
import java.io.OutputStream
import java.io.Reader

/**
 * Serialize [value] into CSV record(s).
 *
 * @param serializer The serializer used to serialize the given object.
 * @param value The [Serializable] object.
 * @param output The output where the CSV will be written.
 */
@ExperimentalSerializationApi
public fun <T> Csv.encodeTo(serializer: SerializationStrategy<T>, value: T, output: OutputStream) {
    encodeTo(serializer, value, CsvSink(output))
}

/**
 * Parse CSV from the given [input] into [Serializable] object.
 *
 * @param deserializer The deserializer used to parse the given CSV string.
 * @param input The CSV input to parse.
 */
@ExperimentalSerializationApi
public fun <T> Csv.decodeFrom(deserializer: DeserializationStrategy<T>, input: Reader): T =
    decodeFrom(deserializer, CsvSource(input))

/**
 * Parse CSV from the given [input] into [Serializable] object.
 *
 * @param deserializer The deserializer used to parse the given CSV string.
 * @param input The CSV input to parse.
 */
@ExperimentalSerializationApi
public fun <T> Csv.decodeFrom(deserializer: DeserializationStrategy<T>, input: InputStream): T =
    decodeFrom(deserializer, CsvSource(input))
