package kotlinx.serialization.csv.decode

import kotlinx.serialization.CompositeDecoder
import kotlinx.serialization.KSerializer
import kotlinx.serialization.SerialDescriptor
import kotlinx.serialization.StructureKind
import kotlinx.serialization.csv.Csv

/**
 * Initial entry point for decoding.
 *
 * This root decoder handles the case that the first level is a list
 * (which is interpreted as multiple CSV records/lines). If this is the case, decoding continues in
 * [RecordListCsvDecoder].
 */
internal class RootCsvDecoder(
    csv: Csv,
    reader: CsvReader
) : CsvDecoder(csv, reader, null) {

    private var position = 0

    override fun decodeElementIndex(desc: SerialDescriptor): Int {
        return if (reader.isDone) CompositeDecoder.READ_DONE else position
    }

    override fun beginStructure(desc: SerialDescriptor, vararg typeParams: KSerializer<*>): CompositeDecoder {
        return when (desc.kind) {
            StructureKind.LIST ->
                // Top level list is treated as list of multiple records
                RecordListCsvDecoder(csv, reader)

            else -> {
                // Top level is treated as one single record
                readHeaders(desc)
                super.beginStructure(desc, *typeParams)
            }
        }
    }

    override fun endChildStructure(desc: SerialDescriptor) {
        super.endChildStructure(desc)
        readTrailingDelimiter()
    }

    override fun decodeColumn(): String {
        val value = super.decodeColumn()
        position++
        readTrailingDelimiter()
        return value
    }
}
