package kotlinx.serialization.csv

import kotlinx.serialization.*
import kotlinx.serialization.internal.ListLikeDescriptor
import kotlinx.serialization.modules.SerialModule

/**
 * Default CSV decoder.
 */
internal abstract class CsvDecoder(
    protected val csv: Csv,
    protected val reader: CsvReader,
    private val parent: CsvDecoder?
) : ElementValueDecoder() {

    override val context: SerialModule
        get() = csv.context

    protected val configuration: CsvConfiguration
        get() = csv.configuration

    protected var headers: Headers? = null

    override fun beginStructure(desc: SerialDescriptor, vararg typeParams: KSerializer<*>): CompositeDecoder {
        return when (desc.kind) {
            StructureKind.LIST,
            StructureKind.MAP ->
                CollectionCsvDecoder(csv, reader, this)

            StructureKind.CLASS ->
                ClassCsvDecoder(csv, reader, this, headers)

            UnionKind.OBJECT ->
                ObjectCsvDecoder(csv, reader, this)

            PolymorphicKind.SEALED ->
                SealedCsvDecoder(csv, reader, this, desc)

            PolymorphicKind.OPEN ->
                ClassCsvDecoder(csv, reader, this, headers)

            else ->
                error("CSV does not support '${desc.kind}'.")
        }
    }

    override fun endStructure(desc: SerialDescriptor) {
        parent?.endChildStructure(desc)
    }

    protected open fun endChildStructure(desc: SerialDescriptor) {
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

    override fun decodeUnit() {
        val value = decodeColumn()
        require(value == configuration.unitString) { "Expected '${configuration.unitString}' but was '$value'." }
    }

    override fun decodeEnum(enumDescription: SerialDescriptor): Int {
        return enumDescription.getElementIndex(decodeColumn())
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
                        headers[position] = readHeaders(childDesc, "$prefix$name.")
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

/**
 * Initial entry point for decoding.
 *
 * This root decoder handles the case that the first level is a list
 * (which is interpreted as multiple CSV records/lines). If this is the case, encoding continues in
 * [RecordListCsvDecoder].
 */
internal class RootCsvDecoder(
    csv: Csv,
    reader: CsvReader
) : CsvDecoder(csv, reader, null) {

    private var position = 0

    override fun decodeElementIndex(desc: SerialDescriptor): Int {
        return if (reader.isDone) CompositeDecoder.READ_DONE else position
    }

    override fun beginStructure(desc: SerialDescriptor, vararg typeParams: KSerializer<*>): CompositeDecoder {
        return when (desc.kind) {
            StructureKind.LIST ->
                // Top level list is treated as list of multiple records
                RecordListCsvDecoder(csv, reader)

            else -> {
                // Top level is treated as one single record
                readHeaders(desc)
                super.beginStructure(desc, *typeParams)
            }
        }
    }

    override fun endChildStructure(desc: SerialDescriptor) {
        super.endChildStructure(desc)
        readTrailingDelimiter()
    }

    override fun decodeColumn(): String {
        val value = super.decodeColumn()
        position++
        readTrailingDelimiter()
        return value
    }
}

/**
 * Decodes list of multiple CSV records/lines.
 */
internal class RecordListCsvDecoder(
    csv: Csv,
    reader: CsvReader
) : CsvDecoder(csv, reader, null) {

    private var elementIndex = 0

    override fun decodeElementIndex(desc: SerialDescriptor): Int {
        if (desc is ListLikeDescriptor) {
            readEmptyLines()
            readHeaders(desc.elementDesc)
        }

        readEmptyLines()
        return if (reader.isDone) CompositeDecoder.READ_DONE else elementIndex
    }

    override fun beginStructure(desc: SerialDescriptor, vararg typeParams: KSerializer<*>): CompositeDecoder {
        return when (desc.kind) {
            StructureKind.LIST,
            StructureKind.MAP ->
                CollectionRecordCsvDecoder(csv, reader, this)

            else ->
                super.beginStructure(desc, *typeParams)
        }
    }

    override fun endChildStructure(desc: SerialDescriptor) {
        elementIndex++
        readTrailingDelimiter()
        readEmptyLines()
    }

    override fun decodeColumn(): String {
        val value = super.decodeColumn()
        readTrailingDelimiter()
        elementIndex++
        return value
    }
}

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

    private var elementIndex = 0

    override fun decodeElementIndex(desc: SerialDescriptor): Int = CompositeDecoder.READ_ALL

    override fun decodeCollectionSize(desc: SerialDescriptor) = decodeInt()

    override fun endChildStructure(desc: SerialDescriptor) {
        super.endChildStructure(desc)
        elementIndex++
    }

    override fun decodeColumn(): String {
        val value = super.decodeColumn()
        elementIndex++
        return value
    }
}

/**
 * CSV decoder for classes.
 *
 * Supports header line such that class properties can be in different order.
 */
internal class ClassCsvDecoder(
    csv: Csv,
    reader: CsvReader,
    parent: CsvDecoder,
    private val classHeaders: Headers?
) : CsvDecoder(csv, reader, parent) {

    private var elementIndex = 0

    override fun decodeElementIndex(desc: SerialDescriptor): Int = when {
        reader.isDone || elementIndex >= desc.elementsCount -> CompositeDecoder.READ_DONE
        classHeaders != null -> classHeaders[elementIndex]
        else -> elementIndex
    }

    override fun beginStructure(desc: SerialDescriptor, vararg typeParams: KSerializer<*>): CompositeDecoder {
        return when (desc.kind) {
            StructureKind.CLASS ->
                ClassCsvDecoder(csv, reader, this, classHeaders?.getSubHeaders(decodeElementIndex(desc)))

            else ->
                super.beginStructure(desc, *typeParams)
        }
    }

    override fun endChildStructure(desc: SerialDescriptor) {
        super.endChildStructure(desc)
        elementIndex++
    }

    override fun decodeColumn(): String {
        val value = super.decodeColumn()
        elementIndex++
        return value
    }
}

/**
 * CSV decoder for `object`s.
 *
 * Expects the name of the object (either fully-qualified class name or [SerialName].
 */
internal class ObjectCsvDecoder(
    csv: Csv,
    reader: CsvReader,
    parent: CsvDecoder
) : CsvDecoder(csv, reader, parent) {

    private var elementIndex = 0

    override fun decodeElementIndex(desc: SerialDescriptor): Int = when {
        reader.isDone || elementIndex > 0 -> CompositeDecoder.READ_DONE
        else -> elementIndex
    }

    override fun endStructure(desc: SerialDescriptor) {
        val value = reader.readColumn()
        require(value == desc.name) { "Expected '${desc.name}' but was '$value'." }
        super.endStructure(desc)
    }

    override fun decodeColumn(): String {
        val value = super.decodeColumn()
        elementIndex++
        return value
    }
}

/**
 * Decode collection record.
 *
 * If the CSV record is a collection (list, set, map) the collection elements fill the whole line.
 * Therefore, the number of elements is determined by reading until the end of line and the size of the collection is
 * not required and consequently not expected as the first value.
 */
internal class CollectionRecordCsvDecoder(
    csv: Csv,
    reader: CsvReader,
    parent: RecordListCsvDecoder
) : CsvDecoder(csv, reader, parent) {

    private var elementIndex = 0

    private val recordNo = reader.recordNo

    override fun decodeElementIndex(desc: SerialDescriptor): Int = when {
        // TODO Check for END_OF_RECORD
        reader.isDone || reader.recordNo != recordNo -> CompositeDecoder.READ_DONE
        else -> elementIndex
    }

    override fun endChildStructure(desc: SerialDescriptor) {
        super.endChildStructure(desc)
        elementIndex++
    }

    override fun decodeColumn(): String {
        val value = super.decodeColumn()
        elementIndex++
        return value
    }
}

/**
 * CSV decoder for sealed classes.
 *
 * Expects columns for all possible child classes. The columns for the actual type should be filled and all other
 * columns are expected to contain `null` values.
 */
internal class SealedCsvDecoder(
    csv: Csv,
    reader: CsvReader,
    parent: CsvDecoder,
    private val sealedDesc: SerialDescriptor
) : CsvDecoder(csv, reader, parent) {

    private var elementIndex = 0

    override fun decodeElementIndex(desc: SerialDescriptor): Int = when {
        reader.isDone || elementIndex > 1 -> CompositeDecoder.READ_DONE
        else -> elementIndex
    }

    override fun beginStructure(desc: SerialDescriptor, vararg typeParams: KSerializer<*>): CompositeDecoder {
        val sealedChildren = sealedDesc.elementDescriptors()
        val index = sealedChildren.indexOf(desc)
        for (innerDesc in sealedChildren.subList(0, index)) {
            readEmptyColumns(innerDesc)
        }
        return super.beginStructure(desc, *typeParams)
    }

    override fun endChildStructure(desc: SerialDescriptor) {
        val sealedChildren = sealedDesc.elementDescriptors()
        val index = sealedChildren.indexOf(desc)
        for (innerDesc in sealedChildren.subList(index + 1, sealedChildren.size)) {
            readEmptyColumns(innerDesc)
        }
        elementIndex++
    }

    override fun decodeString(): String {
        val value = super.decodeString()
        elementIndex++
        return value
    }

    private fun readEmptyColumns(desc: SerialDescriptor) {
        for (innerDesc in desc.elementDescriptors()) {
            decodeNull()
        }
    }
}
