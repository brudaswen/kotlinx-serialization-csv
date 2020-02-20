package kotlinx.serialization.csv.encode

import kotlinx.serialization.CompositeEncoder
import kotlinx.serialization.KSerializer
import kotlinx.serialization.SerialDescriptor
import kotlinx.serialization.csv.Csv
import kotlinx.serialization.elementDescriptors

/**
 * CSV encoder for sealed classes.
 *
 * Writes columns for all possible child classes. The columns for the actual type get filled and all other columns
 * are filled with `null` values.
 */
internal class SealedCsvEncoder(
    csv: Csv,
    writer: CsvWriter,
    parent: CsvEncoder,
    private val sealedDesc: SerialDescriptor
) : SimpleCsvEncoder(csv, writer, parent) {

    override fun beginStructure(desc: SerialDescriptor, vararg typeParams: KSerializer<*>): CompositeEncoder {
        val sealedChildren = sealedDesc.elementDescriptors()
        val index = sealedChildren.indexOf(desc)
        for (innerDesc in sealedChildren.subList(0, index)) {
            printEmptyColumns(innerDesc)
        }
        return super.beginStructure(desc, *typeParams)
    }

    override fun endChildStructure(desc: SerialDescriptor) {
        val sealedChildren = sealedDesc.elementDescriptors()
        val index = sealedChildren.indexOf(desc)
        for (innerDesc in sealedChildren.subList(index + 1, sealedChildren.size)) {
            printEmptyColumns(innerDesc)
        }
    }

    private fun printEmptyColumns(desc: SerialDescriptor) {
        for (innerDesc in desc.elementDescriptors()) {
            encodeNull()
        }
    }
}
