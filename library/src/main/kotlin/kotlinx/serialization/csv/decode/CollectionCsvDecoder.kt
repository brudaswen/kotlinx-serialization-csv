package kotlinx.serialization.csv.decode

import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.csv.Csv
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.encoding.CompositeDecoder.Companion.DECODE_DONE

/**
 * Decode collections (e.g. lists, sets, maps).
 *
 * Expects that the first values defines the number of elements in the collection.
 */
@OptIn(ExperimentalSerializationApi::class)
internal class CollectionCsvDecoder(
    csv: Csv,
    reader: CsvReader,
    parent: CsvDecoder
) : CsvDecoder(csv, reader, parent) {

    private var elementIndex = 0

    override fun decodeSequentially(): Boolean = true

    override fun decodeElementIndex(descriptor: SerialDescriptor): Int = when {
        reader.isDone || elementIndex >= descriptor.elementsCount -> DECODE_DONE
        else -> elementIndex
    }

    override fun decodeCollectionSize(descriptor: SerialDescriptor) = decodeInt()

    override fun endChildStructure(descriptor: SerialDescriptor) {
        super.endChildStructure(descriptor)
        elementIndex++
    }

    override fun decodeColumn(): String {
        val value = super.decodeColumn()
        elementIndex++
        return value
    }
}
