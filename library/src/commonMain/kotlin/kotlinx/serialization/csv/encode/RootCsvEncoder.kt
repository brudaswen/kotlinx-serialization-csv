package kotlinx.serialization.csv.encode

import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.csv.Csv
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.descriptors.StructureKind
import kotlinx.serialization.encoding.CompositeEncoder
import kotlinx.serialization.encoding.Encoder

/**
 * Initial entry point for encoding.
 *
 * This root encoder handles the case that the first level is a list
 * (which is interpreted as multiple CSV records/lines). If this is the case, encoding continues in
 * [RecordListCsvEncoder].
 */
@OptIn(ExperimentalSerializationApi::class)
internal class RootCsvEncoder(
    csv: Csv,
    writer: CsvWriter,
) : CsvEncoder(csv, writer, null) {

    override fun beginCollection(
        descriptor: SerialDescriptor,
        collectionSize: Int,
    ): CompositeEncoder = when (descriptor.kind) {
        StructureKind.LIST -> RecordListCsvEncoder(csv, writer)
        else -> super.beginCollection(descriptor, collectionSize)
    }

    override fun beginStructure(
        descriptor: SerialDescriptor,
    ): CompositeEncoder {
        printHeaderRecord(descriptor)
        writer.beginRecord()
        return super.beginStructure(descriptor)
    }

    override fun endChildStructure(descriptor: SerialDescriptor) {
        writer.endRecord()
    }

    override fun encodeByte(value: Byte) {
        printPrimitiveHeaderRecord("Byte")
        super.encodeByte(value)
    }

    override fun encodeShort(value: Short) {
        printPrimitiveHeaderRecord("Short")
        super.encodeShort(value)
    }

    override fun encodeInt(value: Int) {
        printPrimitiveHeaderRecord("Int")
        super.encodeInt(value)
    }

    override fun encodeLong(value: Long) {
        printPrimitiveHeaderRecord("Long")
        super.encodeLong(value)
    }

    override fun encodeFloat(value: Float) {
        printPrimitiveHeaderRecord("Float")
        super.encodeFloat(value)
    }

    override fun encodeDouble(value: Double) {
        printPrimitiveHeaderRecord("Double")
        super.encodeDouble(value)
    }

    override fun encodeBoolean(value: Boolean) {
        printPrimitiveHeaderRecord("Boolean")
        super.encodeBoolean(value)
    }

    override fun encodeChar(value: Char) {
        printPrimitiveHeaderRecord("Char")
        super.encodeChar(value)
    }

    override fun encodeString(value: String) {
        printPrimitiveHeaderRecord("String")
        super.encodeString(value)
    }

    override fun encodeNull() {
        printPrimitiveHeaderRecord("Null")
        super.encodeNull()
    }

    override fun encodeEnum(
        enumDescriptor: SerialDescriptor,
        index: Int,
    ) {
        printPrimitiveHeaderRecord(enumDescriptor.serialName)
        super.encodeEnum(enumDescriptor, index)
    }

    override fun encodeInline(descriptor: SerialDescriptor): Encoder {
        printHeaderRecord(descriptor)
        return super.encodeInline(descriptor)
    }

    override fun encodeColumn(value: String, isNumeric: Boolean, isNull: Boolean) {
        writer.beginRecord()
        super.encodeColumn(value, isNumeric, isNull)
        writer.endRecord()
    }
}
