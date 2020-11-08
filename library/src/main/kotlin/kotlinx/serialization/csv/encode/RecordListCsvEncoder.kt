package kotlinx.serialization.csv.encode

import kotlinx.serialization.csv.Csv
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.encoding.CompositeEncoder

/**
 * Encodes list of multiple CSV records/lines.
 */
internal class RecordListCsvEncoder(
    csv: Csv,
    writer: CsvWriter
) : CsvEncoder(csv, writer, null) {

    override fun beginStructure(
        descriptor: SerialDescriptor
    ): CompositeEncoder {
        // For complex records: Begin a new record and end it in [endChildStructure]
        if (configuration.hasHeaderRecord && writer.isFirstRecord) {
            printHeaderRecord(descriptor)
        }
        writer.beginRecord()
        return super.beginStructure(descriptor)
    }

    override fun endChildStructure(descriptor: SerialDescriptor) {
        // For complex records: End the record here
        writer.endRecord()
    }

    override fun encodeCollectionSize(collectionSize: Int) {
        // Collection records do not write their size.
        // Instead the size is implicitly determined by reading until end-of-line.
    }

    override fun encodeColumn(value: String, isNumeric: Boolean, isNull: Boolean) {
        // For simple one-column records: Begin and end record here
        writer.beginRecord()
        super.encodeColumn(value, isNumeric, isNull)
        writer.endRecord()
    }
}
