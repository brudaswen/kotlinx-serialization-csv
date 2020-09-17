package kotlinx.serialization.csv.decode

import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.encoding.CompositeDecoder
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.csv.Csv
import kotlinx.serialization.descriptors.StructureKind

/**
 * Decodes list of multiple CSV records/lines.
 */
@ExperimentalSerializationApi
internal class RecordListCsvDecoder(
    csv: Csv,
    reader: CsvReader
) : CsvDecoder(csv, reader, null) {

    private var elementIndex = 0

    override fun decodeElementIndex(descriptor: SerialDescriptor): Int {
        if (descriptor.kind is StructureKind.LIST) {
            readEmptyLines()
            readHeaders(descriptor.getElementDescriptor(0))
        }

        readEmptyLines()
        return if (reader.isDone) CompositeDecoder.DECODE_DONE else elementIndex
    }

    override fun beginStructure(descriptor: SerialDescriptor): CompositeDecoder {
        return when (descriptor.kind) {
            StructureKind.LIST,
            StructureKind.MAP ->
                CollectionRecordCsvDecoder(csv, reader, this)

            else ->
                super.beginStructure(descriptor)
        }
    }

    override fun endChildStructure(descriptor: SerialDescriptor) {
        elementIndex++
        readTrailingDelimiter()
        readEmptyLines()
    }

    override fun decodeColumn(): String {
        val value = super.decodeColumn()
        readTrailingDelimiter()
        elementIndex++
        return value
    }
}
