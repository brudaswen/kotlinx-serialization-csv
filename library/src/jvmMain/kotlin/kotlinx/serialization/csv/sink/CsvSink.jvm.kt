package kotlinx.serialization.csv.sink

import java.io.OutputStream

/**
 * Create a [CsvSink] from [OutputStream].
 */
public fun CsvSink(
    output: OutputStream,
): CsvSink = OutputStreamCsvSink(
    output = output,
)
