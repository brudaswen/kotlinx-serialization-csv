package kotlinx.serialization.csv.decode

import kotlinx.serialization.CompositeDecoder
import kotlinx.serialization.KSerializer
import kotlinx.serialization.SerialDescriptor
import kotlinx.serialization.StructureKind
import kotlinx.serialization.csv.Csv
import kotlinx.serialization.internal.ListLikeDescriptor

/**
 * Decodes list of multiple CSV records/lines.
 */
internal class RecordListCsvDecoder(
    csv: Csv,
    reader: CsvReader
) : CsvDecoder(csv, reader, null) {

    private var elementIndex = 0

    override fun decodeElementIndex(desc: SerialDescriptor): Int {
        if (desc is ListLikeDescriptor) {
            readEmptyLines()
            readHeaders(desc.elementDesc)
        }

        readEmptyLines()
        return if (reader.isDone) CompositeDecoder.READ_DONE else elementIndex
    }

    override fun beginStructure(desc: SerialDescriptor, vararg typeParams: KSerializer<*>): CompositeDecoder {
        return when (desc.kind) {
            StructureKind.LIST,
            StructureKind.MAP ->
                CollectionRecordCsvDecoder(csv, reader, this)

            else ->
                super.beginStructure(desc, *typeParams)
        }
    }

    override fun endChildStructure(desc: SerialDescriptor) {
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
