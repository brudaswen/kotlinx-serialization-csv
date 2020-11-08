package kotlinx.serialization.csv.decode

import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.csv.Csv
import kotlinx.serialization.csv.UnknownColumnHeaderException
import kotlinx.serialization.csv.UnsupportedSerialDescriptorException
import kotlinx.serialization.csv.config.CsvConfig
import kotlinx.serialization.descriptors.PolymorphicKind
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.descriptors.StructureKind
import kotlinx.serialization.encoding.AbstractDecoder
import kotlinx.serialization.encoding.CompositeDecoder
import kotlinx.serialization.encoding.CompositeDecoder.Companion.UNKNOWN_NAME
import kotlinx.serialization.modules.SerializersModule
import kotlin.collections.set

/**
 * Default CSV decoder.
 */
@OptIn(ExperimentalSerializationApi::class)
internal abstract class CsvDecoder(
    protected val csv: Csv,
    protected val reader: CsvReader,
    private val parent: CsvDecoder?
) : AbstractDecoder() {

    override val serializersModule: SerializersModule
        get() = csv.serializersModule

    protected val config: CsvConfig
        get() = csv.config

    private var headers: Headers? = null

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

            else -> throw UnsupportedSerialDescriptorException(descriptor)
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
        require(value == config.nullString) { "Expected '${config.nullString}' but was '$value'." }
        return null
    }

    override fun decodeEnum(enumDescriptor: SerialDescriptor): Int {
        return enumDescriptor.getElementIndex(decodeColumn())
    }

    protected open fun decodeColumn() = reader.readColumn()

    protected fun readHeaders(descriptor: SerialDescriptor) {
        if (config.hasHeaderRecord && headers == null) {
            this.headers = readHeaders(descriptor, "")

            readTrailingDelimiter()
        }
    }

    private fun readHeaders(descriptor: SerialDescriptor, prefix: String): Headers {
        val headers = Headers()
        var position = 0
        while (!reader.isDone && reader.isFirstRecord) {
            val offset = reader.offset
            reader.mark()

            // Read header value and check if it (still) starts with required prefix
            val value = reader.readColumn()
            if (!value.startsWith(prefix)) {
                reader.reset()
                break
            }

            // If there is an exact name match, store the header, otherwise try reading class structure
            val header = value.substringAfter(prefix)
            val headerIndex = descriptor.getElementIndex(header)
            if (headerIndex != UNKNOWN_NAME) {
                headers[position] = headerIndex
                reader.unmark()
            } else {
                val name = header.substringBefore(config.headerSeparator)
                val nameIndex = descriptor.getElementIndex(name)
                if (nameIndex != UNKNOWN_NAME) {
                    val childDesc = descriptor.getElementDescriptor(nameIndex)
                    if (childDesc.kind is StructureKind.CLASS) {
                        reader.reset()
                        headers[position] = nameIndex
                        headers[nameIndex] = readHeaders(childDesc, "$prefix$name.")
                    } else {
                        reader.unmark()
                    }
                } else if (csv.config.ignoreUnknownColumns) {
                    headers[position] = UNKNOWN_NAME
                    reader.unmark()
                } else if (value == "" && !reader.isFirstRecord && config.hasTrailingDelimiter) {
                    reader.unmark()
                } else {
                    throw UnknownColumnHeaderException(offset, value)
                }
            }
            position++
        }
        return headers
    }

    protected fun readTrailingDelimiter() {
        if (config.hasTrailingDelimiter) {
            reader.readEndOfRecord()
        }
    }

    internal class Headers {
        private val map = mutableMapOf<Int, Int>()
        private val subHeaders = mutableMapOf<Int, Headers>()

        val size
            get() = map.size

        operator fun get(position: Int) =
            map[position]

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
