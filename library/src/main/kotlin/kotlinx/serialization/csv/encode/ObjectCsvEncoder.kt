package kotlinx.serialization.csv.encode

import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.csv.Csv

/**
 * CSV encoder for `object`s.
 *
 * Writes the name of the object (either fully-qualified class name or
 * [kotlinx.serialization.SerialName]).
 */
internal class ObjectCsvEncoder(
    csv: Csv,
    writer: CsvWriter,
    parent: CsvEncoder
) : SimpleCsvEncoder(csv, writer, parent) {

    override fun endStructure(descriptor: SerialDescriptor) {
        encodeString(descriptor.serialName)
        super.endStructure(descriptor)
    }
}
