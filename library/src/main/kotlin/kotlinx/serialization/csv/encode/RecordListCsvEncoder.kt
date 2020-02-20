package kotlinx.serialization.csv.encode

import kotlinx.serialization.CompositeEncoder
import kotlinx.serialization.KSerializer
import kotlinx.serialization.SerialDescriptor
import kotlinx.serialization.csv.Csv

/**
 * Encodes list of multiple CSV records/lines.
 */
internal class RecordListCsvEncoder(
    csv: Csv,
    writer: CsvWriter
) : CsvEncoder(csv, writer, null) {

    override fun beginStructure(desc: SerialDescriptor, vararg typeParams: KSerializer<*>): CompositeEncoder {
        // For complex records: Begin a new record and end it in [endChildStructure]
        if (configuration.hasHeaderRecord && writer.isFirstRecord) {
            printHeaderRecord(desc)
        }
        writer.beginRecord()
        return super.beginStructure(desc, *typeParams)
    }

    override fun endChildStructure(desc: SerialDescriptor) {
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
