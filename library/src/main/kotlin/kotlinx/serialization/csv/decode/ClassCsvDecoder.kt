package kotlinx.serialization.csv.decode

import kotlinx.serialization.CompositeDecoder
import kotlinx.serialization.KSerializer
import kotlinx.serialization.SerialDescriptor
import kotlinx.serialization.StructureKind
import kotlinx.serialization.csv.Csv

/**
 * CSV decoder for classes.
 *
 * Supports header line such that class properties can be in different order.
 */
internal class ClassCsvDecoder(
    csv: Csv,
    reader: CsvReader,
    parent: CsvDecoder,
    private val classHeaders: Headers?
) : CsvDecoder(csv, reader, parent) {

    private var elementIndex = 0

    override fun decodeElementIndex(desc: SerialDescriptor): Int = when {
        reader.isDone || elementIndex >= desc.elementsCount -> CompositeDecoder.READ_DONE
        classHeaders != null -> classHeaders[elementIndex]
        else -> elementIndex
    }

    override fun beginStructure(desc: SerialDescriptor, vararg typeParams: KSerializer<*>): CompositeDecoder {
        return when (desc.kind) {
            StructureKind.CLASS ->
                ClassCsvDecoder(
                    csv,
                    reader,
                    this,
                    classHeaders?.getSubHeaders(decodeElementIndex(desc))
                )

            else ->
                super.beginStructure(desc, *typeParams)
        }
    }

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
