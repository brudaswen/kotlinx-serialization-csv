package kotlinx.serialization.csv.decode

import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.csv.Csv
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.descriptors.StructureKind
import kotlinx.serialization.encoding.CompositeDecoder
import kotlinx.serialization.encoding.CompositeDecoder.Companion.DECODE_DONE
import kotlinx.serialization.encoding.Decoder

/**
 * Initial entry point for decoding.
 *
 * This root decoder handles the case that the first level is a list
 * (which is interpreted as multiple CSV records/lines). If this is the case, decoding continues in
 * [RecordListCsvDecoder].
 */
@OptIn(ExperimentalSerializationApi::class)
internal class RootCsvDecoder(
    csv: Csv,
    reader: CsvReader,
) : CsvDecoder(csv, reader, null) {

    private var position = 0

    override fun decodeElementIndex(descriptor: SerialDescriptor): Int =
        if (reader.isDone) DECODE_DONE else position

    override fun beginStructure(descriptor: SerialDescriptor): CompositeDecoder =
        when (descriptor.kind) {
            StructureKind.LIST ->
                // Top level list is treated as list of multiple records
                RecordListCsvDecoder(csv, reader)

            else -> {
                // Top level is treated as one single record
                readHeaders(descriptor)
                super.beginStructure(descriptor)
            }
        }

    override fun endChildStructure(descriptor: SerialDescriptor) {
        super.endChildStructure(descriptor)
        readTrailingDelimiter()
    }

    override fun decodeByte(): Byte {
        readPrimitiveHeaders("Byte")
        return super.decodeByte()
    }

    override fun decodeShort(): Short {
        readPrimitiveHeaders("Short")
        return super.decodeShort()
    }

    override fun decodeInt(): Int {
        readPrimitiveHeaders("Int")
        return decodeColumn().toInt()
    }

    override fun decodeLong(): Long {
        readPrimitiveHeaders("Long")
        return super.decodeLong()
    }

    override fun decodeFloat(): Float {
        readPrimitiveHeaders("Float")
        return super.decodeFloat()
    }

    override fun decodeDouble(): Double {
        readPrimitiveHeaders("Double")
        return super.decodeDouble()
    }

    override fun decodeBoolean(): Boolean {
        readPrimitiveHeaders("Boolean")
        return super.decodeBoolean()
    }

    override fun decodeChar(): Char {
        readPrimitiveHeaders("Char")
        return super.decodeChar()
    }

    override fun decodeString(): String {
        readPrimitiveHeaders("String")
        return super.decodeString()
    }

    override fun decodeNull(): Nothing? {
        readPrimitiveHeaders("Null")
        return super.decodeNull()
    }

    override fun decodeEnum(enumDescriptor: SerialDescriptor): Int {
        readPrimitiveHeaders(enumDescriptor.serialName)
        return super.decodeEnum(enumDescriptor)
    }

    override fun decodeInline(descriptor: SerialDescriptor): Decoder {
        readHeaders(descriptor)
        return super.decodeInline(descriptor)
    }

    override fun decodeColumn(): String {
        val value = super.decodeColumn()
        position++
        readTrailingDelimiter()
        return value
    }
}
