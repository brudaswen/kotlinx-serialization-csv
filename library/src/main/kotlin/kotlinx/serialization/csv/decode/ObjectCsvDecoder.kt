package kotlinx.serialization.csv.decode

import kotlinx.serialization.CompositeDecoder
import kotlinx.serialization.SerialDescriptor
import kotlinx.serialization.csv.Csv

/**
 * CSV decoder for `object`s.
 *
 * Expects the name of the object (either fully-qualified class name or [SerialName].
 */
internal class ObjectCsvDecoder(
    csv: Csv,
    reader: CsvReader,
    parent: CsvDecoder
) : CsvDecoder(csv, reader, parent) {

    private var elementIndex = 0

    override fun decodeElementIndex(desc: SerialDescriptor): Int = when {
        reader.isDone || elementIndex > 0 -> CompositeDecoder.READ_DONE
        else -> elementIndex
    }

    override fun endStructure(desc: SerialDescriptor) {
        val value = reader.readColumn()
        require(value == desc.name) { "Expected '${desc.name}' but was '$value'." }
        super.endStructure(desc)
    }

    override fun decodeColumn(): String {
        val value = super.decodeColumn()
        elementIndex++
        return value
    }
}
