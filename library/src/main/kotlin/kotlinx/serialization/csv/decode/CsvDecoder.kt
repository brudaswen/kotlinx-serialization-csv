package kotlinx.serialization.csv.decode

import kotlinx.serialization.*
import kotlinx.serialization.csv.Csv
import kotlinx.serialization.csv.CsvConfiguration
import kotlinx.serialization.descriptors.PolymorphicKind
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.descriptors.StructureKind
import kotlinx.serialization.encoding.AbstractDecoder
import kotlinx.serialization.encoding.CompositeDecoder
import kotlinx.serialization.modules.SerializersModule

/**
 * Default CSV decoder.
 */
@ExperimentalSerializationApi
internal abstract class CsvDecoder(
    protected val csv: Csv,
    protected val reader: CsvReader,
    private val parent: CsvDecoder?
) : AbstractDecoder() {

    override val serializersModule: SerializersModule
        get() = csv.serializersModule

    protected val configuration: CsvConfiguration
        get() = csv.configuration

    protected var headers: Headers? = null

    override fun beginStructure(descriptor: SerialDescriptor): CompositeDecoder {
        return when (descriptor.kind) {
            StructureKind.LIST,
            StructureKind.MAP ->
                CollectionCsvDecoder(csv, reader, this)

            StructureKind.CLASS ->
                ClassCsvDecoder(csv, reader, this, headers)

            StructureKind.OBJECT ->
                ObjectCsvDecoder(csv, reader, this)

            PolymorphicKind.SEALED ->
                SealedCsvDecoder(csv, reader, this, descriptor)

            PolymorphicKind.OPEN ->
                ClassCsvDecoder(csv, reader, this, headers)

            else ->
                error("CSV does not support '${descriptor.kind}'.")
        }
    }

    override fun endStructure(descriptor: SerialDescriptor) {
        parent?.endChildStructure(descriptor)
    }

    protected open fun endChildStructure(descriptor: SerialDescriptor) {
    }

    override fun decodeByte(): Byte {
        return decodeColumn().toByte()
    }

    override fun decodeShort(): Short {
        return decodeColumn().toShort()
    }

    override fun decodeInt(): Int {
        return decodeColumn().toInt()
    }

    override fun decodeLong(): Long {
        return decodeColumn().toLong()
    }

    override fun decodeFloat(): Float {
        return decodeColumn().toFloat()
    }

    override fun decodeDouble(): Double {
        return decodeColumn().toDouble()
    }

    override fun decodeBoolean(): Boolean {
        return decodeColumn().toBoolean()
    }

    override fun decodeChar(): Char {
        val value = decodeColumn()
        require(value.length == 1)
        return value[0]
    }

    override fun decodeString(): String {
        return decodeColumn()
    }

    override fun decodeNotNullMark(): Boolean {
        return !reader.isNullToken()
    }

    override fun decodeNull(): Nothing? {
        val value = decodeColumn()
        require(value == configuration.nullString) { "Expected '${configuration.nullString}' but was '$value'." }
        return null
    }

    override fun decodeEnum(enumDescriptor: SerialDescriptor): Int {
        return enumDescriptor.getElementIndex(decodeColumn())
    }

    protected open fun decodeColumn() = reader.readColumn()

    protected fun readHeaders(desc: SerialDescriptor) {
        if (configuration.hasHeaderRecord && headers == null) {
            this.headers = readHeaders(desc, "")

            readTrailingDelimiter()
        }
    }

    private fun readHeaders(desc: SerialDescriptor, prefix: String): Headers {
        val headers = Headers()
        var position = 0
        while (reader.isFirstRecord) {
            // Read header value and check if it (still) starts with required prefix
            reader.mark()
            val value = reader.readColumn()
            if (!value.startsWith(prefix)) {
                reader.reset()
                break
            }

            // If there is an exact name match, store the header, otherwise try reading class structure
            val header = value.substringAfter(prefix)
            val headerIndex = desc.getElementIndex(header)
            if (headerIndex != CompositeDecoder.UNKNOWN_NAME) {
                headers[position] = headerIndex
                reader.unmark()
            } else {
                val name = header.substringBefore(configuration.headerSeparator)
                val nameIndex = desc.getElementIndex(name)
                if (nameIndex != CompositeDecoder.UNKNOWN_NAME) {
                    val childDesc = desc.getElementDescriptor(nameIndex)
                    if (childDesc.kind is StructureKind.CLASS) {
                        reader.reset()
                        headers[position] = nameIndex
                        headers[nameIndex] = readHeaders(childDesc, "$prefix$name.")
                    } else {
                        reader.unmark()
                    }
                } else {
                    reader.unmark()
                }
            }
            position++
        }
        return headers
    }

    protected fun readEmptyLines() {
        if (configuration.ignoreEmptyLines) {
            reader.readEmptyLines()
        }
    }

    protected fun readTrailingDelimiter() {
        if (configuration.hasTrailingDelimiter) {
            reader.readEndOfRecord()
        }
    }

    internal class Headers {
        private val map = mutableMapOf<Int, Int>()
        private val subHeaders = mutableMapOf<Int, Headers>()

        operator fun get(position: Int) =
            map.getOrElse(position) { CompositeDecoder.UNKNOWN_NAME }

        operator fun set(key: Int, value: Int) {
            map[key] = value
        }

        fun getSubHeaders(position: Int) =
            subHeaders.getOrElse(position) { null }

        operator fun set(key: Int, value: Headers) {
            subHeaders[key] = value
        }
    }
}
