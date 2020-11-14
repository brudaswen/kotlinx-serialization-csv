package kotlinx.serialization.csv.decode

import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.csv.Csv
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.descriptors.StructureKind
import kotlinx.serialization.descriptors.elementDescriptors
import kotlinx.serialization.encoding.CompositeDecoder
import kotlinx.serialization.encoding.CompositeDecoder.Companion.DECODE_DONE

/**
 * CSV decoder for sealed classes.
 *
 * Expects columns for all possible child classes. The columns for the actual type should be filled and all other
 * columns are expected to contain `null` values.
 */
@OptIn(ExperimentalSerializationApi::class)
internal class SealedCsvDecoder(
    csv: Csv,
    reader: CsvReader,
    parent: CsvDecoder,
    private val sealedDesc: SerialDescriptor
) : CsvDecoder(csv, reader, parent) {

    private var elementIndex = 0

    override fun decodeElementIndex(descriptor: SerialDescriptor): Int = when {
        reader.isDone || elementIndex > 1 -> DECODE_DONE
        else -> elementIndex
    }

    override fun beginStructure(descriptor: SerialDescriptor): CompositeDecoder {
        val sealedChildren = sealedDesc.getElementDescriptor(1).elementDescriptors.toList()
        val index = sealedChildren.indexOf(descriptor)
        for (innerDesc in sealedChildren.subList(0, index)) {
            readEmptyColumns(innerDesc)
        }

        return when (descriptor.kind) {
            is StructureKind.OBJECT ->
                SealedObjectDecoder(csv, reader, this)
            else ->
                super.beginStructure(descriptor)
        }
    }

    override fun endChildStructure(descriptor: SerialDescriptor) {
        val sealedChildren = sealedDesc.getElementDescriptor(1).elementDescriptors.toList()
        val index = sealedChildren.indexOf(descriptor)
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

    private fun readEmptyColumns(descriptor: SerialDescriptor) {
        for (innerDesc in descriptor.elementDescriptors.toList()) {
            decodeNull()
        }
    }

    /**
     * Object decoder that does not read any columns since the object
     * is already encoded in the first column of the sealed class itself.
     */
    private class SealedObjectDecoder(
        csv: Csv,
        reader: CsvReader,
        parent: CsvDecoder
    ) : CsvDecoder(csv, reader, parent) {
        override fun decodeElementIndex(descriptor: SerialDescriptor): Int =
            DECODE_DONE
    }
}
