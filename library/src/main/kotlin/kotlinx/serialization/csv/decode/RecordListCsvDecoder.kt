package kotlinx.serialization.csv.decode

import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.csv.Csv
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.descriptors.StructureKind
import kotlinx.serialization.encoding.CompositeDecoder
import kotlinx.serialization.encoding.CompositeDecoder.Companion.DECODE_DONE

/**
 * Decodes list of multiple CSV records/lines.
 */
@OptIn(ExperimentalSerializationApi::class)
internal class RecordListCsvDecoder(
    csv: Csv,
    reader: CsvReader,
) : CsvDecoder(csv, reader, null) {

    private var elementIndex = 0

    override fun decodeElementIndex(descriptor: SerialDescriptor): Int {
        if (descriptor.kind is StructureKind.LIST) {
            readEmptyLines()
            readHeaders(descriptor.getElementDescriptor(0))
        }

        readEmptyLines()
        return if (reader.isDone) DECODE_DONE else elementIndex
    }

    override fun beginStructure(descriptor: SerialDescriptor): CompositeDecoder =
        when (descriptor.kind) {
            StructureKind.LIST,
            StructureKind.MAP,
            -> CollectionRecordCsvDecoder(csv, reader, this)

            else -> super.beginStructure(descriptor)
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

    private fun readEmptyLines() {
        if (config.ignoreEmptyLines) {
            reader.readEmptyLines()
        } else {
            // Last line in file is always allowed to be empty
            readLastEmptyLine()
        }
    }

    private fun readLastEmptyLine() {
        reader.mark()
        reader.readEndOfRecord()
        if (reader.isDone) {
            reader.unmark()
        } else {
            reader.reset()
        }
    }
}
