package kotlinx.serialization.csv

import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.KSerializer
import kotlinx.serialization.builtins.ListSerializer
import kotlinx.serialization.csv.decode.CsvReader
import kotlinx.serialization.csv.decode.RecordListCsvDecoder
import kotlinx.serialization.csv.source.CsvSource
import kotlinx.serialization.encoding.CompositeDecoder.Companion.DECODE_DONE

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
 * Parse CSV line-by-line from the given [source].
 *
 * @param deserializer The deserializer used to parse the given CSV string.
 * @param source The CSV source to parse. This function *does not close the source*.
 */
@ExperimentalSerializationApi
public fun <T : Any> Csv.recordReader(
    deserializer: KSerializer<T>,
    source: CsvSource,
): CsvRecordReader<T> {
    val decoder = RecordListCsvDecoder(
        csv = this,
        reader = CsvReader(source, config),
    )
    val listDescriptor = ListSerializer(deserializer).descriptor

    return object : CsvRecordReader<T> {
        override fun hasNext(): Boolean = decoder.decodeElementIndex(listDescriptor) != DECODE_DONE

        override fun next(): T = decoder.decodeSerializableElement(
            descriptor = listDescriptor,
            index = decoder.decodeElementIndex(listDescriptor),
            deserializer = deserializer,
            previousValue = null,
        )
    }
}
