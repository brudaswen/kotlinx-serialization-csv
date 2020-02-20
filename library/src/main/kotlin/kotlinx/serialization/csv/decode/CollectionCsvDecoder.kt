package kotlinx.serialization.csv.decode

import kotlinx.serialization.CompositeDecoder
import kotlinx.serialization.SerialDescriptor
import kotlinx.serialization.csv.Csv

/**
 * Decode collections (e.g. lists, sets, maps).
 *
 * Expects that the first values defines the number of elements in the collection.
 */
internal class CollectionCsvDecoder(
    csv: Csv,
    reader: CsvReader,
    parent: CsvDecoder
) : CsvDecoder(csv, reader, parent) {

    private var elementIndex = 0

    override fun decodeElementIndex(desc: SerialDescriptor): Int =
        CompositeDecoder.READ_ALL

    override fun decodeCollectionSize(desc: SerialDescriptor) = decodeInt()

    override fun endChildStructure(desc: SerialDescriptor) {
        super.endChildStructure(desc)
        elementIndex++
    }

    override fun decodeColumn(): String {
        val value = super.decodeColumn()
        elementIndex++
        return value
    }
}
