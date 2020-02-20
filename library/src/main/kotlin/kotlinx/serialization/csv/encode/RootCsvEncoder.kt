package kotlinx.serialization.csv.encode

import kotlinx.serialization.CompositeEncoder
import kotlinx.serialization.KSerializer
import kotlinx.serialization.SerialDescriptor
import kotlinx.serialization.StructureKind
import kotlinx.serialization.csv.Csv

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
        desc: SerialDescriptor,
        collectionSize: Int,
        vararg typeParams: KSerializer<*>
    ): CompositeEncoder {
        return if (desc.kind == StructureKind.LIST) {
            RecordListCsvEncoder(csv, writer)
        } else {
            super.beginCollection(desc, collectionSize, *typeParams)
        }
    }

    override fun beginStructure(desc: SerialDescriptor, vararg typeParams: KSerializer<*>): CompositeEncoder {
        if (configuration.hasHeaderRecord && writer.isFirstRecord) {
            printHeaderRecord(desc)
        }
        writer.beginRecord()
        return super.beginStructure(desc, *typeParams)
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
