package kotlinx.serialization.csv.encode

import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.csv.Csv
import kotlinx.serialization.descriptors.SerialDescriptor

/**
 * CSV encoder for `object`s.
 *
 * Writes the name of the object (either fully-qualified class name or
 * [kotlinx.serialization.SerialName]).
 */
@OptIn(ExperimentalSerializationApi::class)
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
