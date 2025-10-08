package kotlinx.serialization.csv

import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.KSerializer
import kotlinx.serialization.csv.sink.CsvSink
import java.io.OutputStream

/**
 * Create [CsvRecordWriter] that allows writing CSV line-by-line.
 *
 * @param serializer The serializer used to serialize the given object.
 * @param output The output where the CSV will be written.
 */
@ExperimentalSerializationApi
public fun <T : Any> Csv.recordWriter(
    serializer: KSerializer<T>,
    output: OutputStream,
): CsvRecordWriter<T> = recordWriter(
    serializer = serializer,
    sink = CsvSink(output),
)
