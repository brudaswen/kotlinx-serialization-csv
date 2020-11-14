package kotlinx.serialization.csv.encode

import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.csv.Csv

/**
 * Default CSV encoder that writes each value into the next column.
 */
@OptIn(ExperimentalSerializationApi::class)
internal open class SimpleCsvEncoder(
    csv: Csv,
    writer: CsvWriter,
    parent: CsvEncoder
) : CsvEncoder(csv, writer, parent)
