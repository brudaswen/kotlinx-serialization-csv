package kotlinx.serialization.csv.decode

import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.csv.Csv
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.descriptors.StructureKind
import kotlinx.serialization.encoding.CompositeDecoder

/**
 * CSV decoder for classes.
 *
 * Supports header line such that class properties can be in different order.
 */
@OptIn(ExperimentalSerializationApi::class)
internal class ClassCsvDecoder(
    csv: Csv,
    reader: CsvReader,
    parent: CsvDecoder,
    private val classHeaders: Headers?
) : CsvDecoder(csv, reader, parent) {

    private var elementIndex = 0
    private var columnIndex = 0

    override fun decodeElementIndex(descriptor: SerialDescriptor): Int = when {
        reader.isDone -> CompositeDecoder.DECODE_DONE
        elementIndex >= descriptor.elementsCount -> CompositeDecoder.DECODE_DONE
        classHeaders != null && columnIndex >= classHeaders.size -> CompositeDecoder.DECODE_DONE

        classHeaders != null ->
            when (val result = classHeaders[columnIndex]) {
                CompositeDecoder.UNKNOWN_NAME -> {
                    ignoreColumn()
                    decodeElementIndex(descriptor)
                }
                null -> CompositeDecoder.UNKNOWN_NAME
                else -> result
            }

        else -> elementIndex
    }

    override fun beginStructure(descriptor: SerialDescriptor): CompositeDecoder {
        return when (descriptor.kind) {
            StructureKind.CLASS ->
                ClassCsvDecoder(
                    csv,
                    reader,
                    this,
                    classHeaders?.getSubHeaders(decodeElementIndex(descriptor))
                )

            else ->
                super.beginStructure(descriptor)
        }
    }

    override fun endStructure(descriptor: SerialDescriptor) {
        super.endStructure(descriptor)

        if (classHeaders != null && csv.configuration.ignoreUnknownColumns) {
            while (columnIndex < classHeaders.size) {
                ignoreColumn()
            }
        }
    }

    override fun endChildStructure(descriptor: SerialDescriptor) {
        super.endChildStructure(descriptor)
        elementIndex++
        columnIndex++
    }

    override fun decodeColumn(): String {
        val value = super.decodeColumn()
        elementIndex++
        columnIndex++
        return value
    }

    private fun ignoreColumn() {
        reader.readColumn()
        columnIndex++
    }
}
