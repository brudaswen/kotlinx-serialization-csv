package kotlinx.serialization.csv

import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.KSerializer
import kotlinx.serialization.csv.source.CsvSource
import java.io.InputStream
import java.io.Reader

/**
 * Parse CSV line-by-line from the given [input].
 *
 * @param deserializer The deserializer used to parse the given CSV string.
 * @param input The CSV source to parse. This function *does not close the source*.
 */
@ExperimentalSerializationApi
public fun <T : Any> Csv.recordReader(
    deserializer: KSerializer<T>,
    input: Reader,
): CsvRecordReader<T> = recordReader(
    deserializer = deserializer,
    source = CsvSource(input),
)

/**
 * Parse CSV line-by-line from the given [input].
 *
 * @param deserializer The deserializer used to parse the given CSV string.
 * @param input The CSV source to parse. This function *does not close the source*.
 */
@ExperimentalSerializationApi
public fun <T : Any> Csv.recordReader(
    deserializer: KSerializer<T>,
    input: InputStream,
): CsvRecordReader<T> = recordReader(
    deserializer = deserializer,
    source = CsvSource(input),
)
