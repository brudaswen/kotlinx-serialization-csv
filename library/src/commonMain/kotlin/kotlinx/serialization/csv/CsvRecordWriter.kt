package kotlinx.serialization.csv

import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.KSerializer
import kotlinx.serialization.csv.encode.CsvWriter
import kotlinx.serialization.csv.encode.RecordListCsvEncoder
import kotlinx.serialization.csv.sink.CsvSink

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
 * @param sink The output where the CSV will be written.
 */
@ExperimentalSerializationApi
public fun <T : Any> Csv.recordWriter(
    serializer: KSerializer<T>,
    sink: CsvSink,
): CsvRecordWriter<T> {
    val encoder = RecordListCsvEncoder(
        csv = this,
        writer = CsvWriter(sink, config),
    )

    return CsvRecordWriter {
        encoder.encodeSerializableValue(serializer, it)
    }
}
