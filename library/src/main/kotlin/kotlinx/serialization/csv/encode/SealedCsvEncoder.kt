package kotlinx.serialization.csv.encode

import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.csv.Csv
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.descriptors.StructureKind
import kotlinx.serialization.descriptors.elementDescriptors
import kotlinx.serialization.encoding.CompositeEncoder

/**
 * CSV encoder for sealed classes.
 *
 * Writes columns for all possible child classes. The columns for the actual type get filled and all other columns
 * are filled with `null` values.
 */
@OptIn(ExperimentalSerializationApi::class)
internal class SealedCsvEncoder(
    csv: Csv,
    writer: CsvWriter,
    parent: CsvEncoder,
    private val sealedDesc: SerialDescriptor
) : SimpleCsvEncoder(csv, writer, parent) {

    override fun beginStructure(
        descriptor: SerialDescriptor
    ): CompositeEncoder {
        val sealedChildren = sealedDesc.getElementDescriptor(1).elementDescriptors.toList()
        val index = sealedChildren.indexOf(descriptor)
        for (innerDesc in sealedChildren.subList(0, index)) {
            printEmptyColumns(innerDesc)
        }

        return when (descriptor.kind) {
            is StructureKind.OBJECT ->
                SealedObjectEncoder(csv, writer, this)
            else ->
                super.beginStructure(descriptor)
        }
    }

    override fun endChildStructure(descriptor: SerialDescriptor) {
        val sealedChildren = sealedDesc.getElementDescriptor(1).elementDescriptors.toList()
        val index = sealedChildren.indexOf(descriptor)
        for (innerDesc in sealedChildren.subList(index + 1, sealedChildren.size)) {
            printEmptyColumns(innerDesc)
        }
    }

    private fun printEmptyColumns(descriptor: SerialDescriptor) {
        for (innerDesc in descriptor.elementDescriptors.toList()) {
            encodeNull()
        }
    }

    /**
     * Object encoder that does not print any columns since the object
     * is already encoded in the first column of the sealed class itself.
     */
    private class SealedObjectEncoder(
        csv: Csv,
        writer: CsvWriter,
        parent: CsvEncoder
    ) : CsvEncoder(csv, writer, parent)
}
