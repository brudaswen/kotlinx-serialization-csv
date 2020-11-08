package kotlinx.serialization.csv.decode

import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.csv.Csv
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.encoding.CompositeDecoder.Companion.UNKNOWN_NAME

/**
 * CSV decoder for `object`s.
 *
 * Expects the name of the object (either fully-qualified class name or
 * [kotlinx.serialization.SerialName]).
 */
@OptIn(ExperimentalSerializationApi::class)
internal class ObjectCsvDecoder(
    csv: Csv,
    reader: CsvReader,
    parent: CsvDecoder
) : CsvDecoder(csv, reader, parent) {

    override fun decodeSequentially(): Boolean = true

    override fun decodeElementIndex(descriptor: SerialDescriptor): Int = UNKNOWN_NAME

    override fun endStructure(descriptor: SerialDescriptor) {
        val value = reader.readColumn()
        require(value == descriptor.serialName) { "Expected '${descriptor.serialName}' but was '$value'." }
        super.endStructure(descriptor)
    }
}
