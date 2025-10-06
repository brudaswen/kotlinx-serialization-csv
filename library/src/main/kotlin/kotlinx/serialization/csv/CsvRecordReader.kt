package kotlinx.serialization.csv

import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.KSerializer
import kotlinx.serialization.builtins.ListSerializer
import kotlinx.serialization.csv.decode.CsvReader
import kotlinx.serialization.csv.decode.FetchSource
import kotlinx.serialization.csv.decode.RecordListCsvDecoder
import kotlinx.serialization.encoding.CompositeDecoder.Companion.DECODE_DONE
import java.io.Reader

/**
 * Record reader that allows reading CSV line-by-line.
 */
public interface CsvRecordReader<T : Any> : Iterator<T> {
    /**
     * Read next record
     */
    public fun read(): T? = if (hasNext()) next() else null
}

/**
 * Parse CSV line-by-line from the given [input].
 *
 * @param deserializer The deserializer used to parse the given CSV string.
 * @param input The CSV reader to parse. This function *does not close the reader*.
 */
@ExperimentalSerializationApi
public fun <T : Any> Csv.recordReader(deserializer: KSerializer<T>, input: Reader): CsvRecordReader<T> {
    val decoder = RecordListCsvDecoder(
        csv = this,
        reader = CsvReader(FetchSource(input), config)
    )
    val listDescriptor = ListSerializer(deserializer).descriptor
    var previousValue: T? = null

    return object : CsvRecordReader<T> {
        override fun hasNext(): Boolean =
            decoder.decodeElementIndex(listDescriptor) != DECODE_DONE

        override fun next(): T {
            val index = decoder.decodeElementIndex(listDescriptor)
            return decoder.decodeSerializableElement(listDescriptor, index, deserializer, previousValue).also {
                previousValue = it
            }
        }
    }
}
