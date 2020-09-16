package kotlinx.serialization.csv.encode

import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.csv.Csv
import kotlinx.serialization.descriptors.StructureKind
import kotlinx.serialization.encoding.CompositeEncoder

/**
 * Initial entry point for encoding.
 *
 * This root encoder handles the case that the first level is a list
 * (which is interpreted as multiple CSV records/lines). If this is the case, encoding continues in
 * [RecordListCsvEncoder].
 */
internal class RootCsvEncoder(
    csv: Csv,
    writer: CsvWriter
) : CsvEncoder(csv, writer, null) {

    internal constructor(csv: Csv, output: Appendable) :
            this(csv, CsvWriter(output, csv.configuration))

    override fun beginCollection(
        descriptor: SerialDescriptor,
        collectionSize: Int
    ): CompositeEncoder {
        return if (descriptor.kind == StructureKind.LIST) {
            RecordListCsvEncoder(csv, writer)
        } else {
            super.beginCollection(descriptor, collectionSize)
        }
    }

    override fun beginStructure(
        descriptor: SerialDescriptor
    ): CompositeEncoder {
        if (configuration.hasHeaderRecord && writer.isFirstRecord) {
            printHeaderRecord(descriptor)
        }
        writer.beginRecord()
        return super.beginStructure(descriptor)
    }

    override fun endChildStructure(desc: SerialDescriptor) {
        writer.endRecord()
    }

    override fun encodeColumn(value: String, isNumeric: Boolean, isNull: Boolean) {
        writer.beginRecord()
        super.encodeColumn(value, isNumeric, isNull)
        writer.endRecord()
    }
}
