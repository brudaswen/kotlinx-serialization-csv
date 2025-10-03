package kotlinx.serialization.csv

import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.KSerializer
import kotlinx.serialization.csv.encode.CsvWriter
import kotlinx.serialization.csv.encode.RecordListCsvEncoder

/**
 * Record writer that allows writing CSV line by line.
 */
public fun interface CsvRecordWriter<T : Any> {
    /**
     * Write next record.
     */
    public fun write(record: T)
}

/**
 * Create [CsvRecordWriter] that allows writing CSV line-by-line.
 *
 * @param serializer The serializer used to serialize the given object.
 * @param output The output where the CSV will be written.
 */
@ExperimentalSerializationApi
public fun <T : Any> Csv.recordWriter(
    serializer: KSerializer<T>,
    output: Appendable,
): CsvRecordWriter<T> {
    val encoder = RecordListCsvEncoder(
        csv = this,
        writer = CsvWriter(output, config)
    )

    return CsvRecordWriter {
        encoder.encodeSerializableValue(serializer, it)
    }
}
