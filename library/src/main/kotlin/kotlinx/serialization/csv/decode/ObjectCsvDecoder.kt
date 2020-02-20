package kotlinx.serialization.csv.decode

import kotlinx.serialization.SerialDescriptor
import kotlinx.serialization.csv.Csv

/**
 * CSV decoder for `object`s.
 *
 * Expects the name of the object (either fully-qualified class name or
 * [kotlinx.serialization.SerialName]).
 */
internal class ObjectCsvDecoder(
    csv: Csv,
    reader: CsvReader,
    parent: CsvDecoder
) : CsvDecoder(csv, reader, parent) {

    override fun endStructure(desc: SerialDescriptor) {
        val value = reader.readColumn()
        require(value == desc.name) { "Expected '${desc.name}' but was '$value'." }
        super.endStructure(desc)
    }
}
