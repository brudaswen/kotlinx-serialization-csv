package kotlinx.serialization.csv.decode

import kotlinx.serialization.CompositeDecoder
import kotlinx.serialization.KSerializer
import kotlinx.serialization.SerialDescriptor
import kotlinx.serialization.csv.Csv
import kotlinx.serialization.elementDescriptors

/**
 * CSV decoder for sealed classes.
 *
 * Expects columns for all possible child classes. The columns for the actual type should be filled and all other
 * columns are expected to contain `null` values.
 */
internal class SealedCsvDecoder(
    csv: Csv,
    reader: CsvReader,
    parent: CsvDecoder,
    private val sealedDesc: SerialDescriptor
) : CsvDecoder(csv, reader, parent) {

    private var elementIndex = 0

    override fun decodeElementIndex(desc: SerialDescriptor): Int = when {
        reader.isDone || elementIndex > 1 -> CompositeDecoder.READ_DONE
        else -> elementIndex
    }

    override fun beginStructure(desc: SerialDescriptor, vararg typeParams: KSerializer<*>): CompositeDecoder {
        val sealedChildren = sealedDesc.elementDescriptors()
        val index = sealedChildren.indexOf(desc)
        for (innerDesc in sealedChildren.subList(0, index)) {
            readEmptyColumns(innerDesc)
        }
        return super.beginStructure(desc, *typeParams)
    }

    override fun endChildStructure(desc: SerialDescriptor) {
        val sealedChildren = sealedDesc.elementDescriptors()
        val index = sealedChildren.indexOf(desc)
        for (innerDesc in sealedChildren.subList(index + 1, sealedChildren.size)) {
            readEmptyColumns(innerDesc)
        }
        elementIndex++
    }

    override fun decodeString(): String {
        val value = super.decodeString()
        elementIndex++
        return value
    }

    private fun readEmptyColumns(desc: SerialDescriptor) {
        for (innerDesc in desc.elementDescriptors()) {
            decodeNull()
        }
    }
}
