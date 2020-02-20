package kotlinx.serialization.csv.decode

import kotlinx.serialization.SerialDescriptor
import kotlinx.serialization.csv.Csv

/**
 * Decode collections (e.g. lists, sets, maps).
 *
 * Expects that the first values defines the number of elements in the collection.
 */
internal class CollectionCsvDecoder(
    csv: Csv,
    reader: CsvReader,
    parent: CsvDecoder
) : CsvDecoder(csv, reader, parent) {

    override fun decodeCollectionSize(desc: SerialDescriptor) = decodeInt()
}
