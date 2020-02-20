package kotlinx.serialization.csv.encode

import kotlinx.serialization.*
import kotlinx.serialization.csv.Csv
import kotlinx.serialization.modules.SerialModule

/**
 * Default CSV encoder.
 */
internal abstract class CsvEncoder(
    protected val csv: Csv,
    protected val writer: CsvWriter,
    private val parent: CsvEncoder?
) : ElementValueEncoder() {

    override val context: SerialModule = csv.context

    protected val configuration
        get() = csv.configuration

    override fun beginCollection(
        desc: SerialDescriptor,
        collectionSize: Int,
        vararg typeParams: KSerializer<*>
    ): CompositeEncoder {
        encodeCollectionSize(collectionSize)
        return super.beginCollection(desc, collectionSize, *typeParams)
    }

    override fun beginStructure(desc: SerialDescriptor, vararg typeParams: KSerializer<*>): CompositeEncoder {
        return when (desc.kind) {
            StructureKind.LIST,
            StructureKind.MAP ->
                SimpleCsvEncoder(csv, writer, this)

            StructureKind.CLASS ->
                SimpleCsvEncoder(csv, writer, this)

            UnionKind.OBJECT ->
                ObjectCsvEncoder(csv, writer, this)

            PolymorphicKind.SEALED ->
                SealedCsvEncoder(csv, writer, this, desc)

            PolymorphicKind.OPEN ->
                SimpleCsvEncoder(csv, writer, this)

            else ->
                error("CSV does not support '${desc.kind}'.")
        }
    }

    override fun endStructure(desc: SerialDescriptor) {
        parent?.endChildStructure(desc)
    }

    protected open fun endChildStructure(desc: SerialDescriptor) {
    }

    override fun encodeByte(value: Byte) {
        encodeColumn(value.toString(), isNumeric = true)
    }

    override fun encodeShort(value: Short) {
        encodeColumn(value.toString(), isNumeric = true)
    }

    override fun encodeInt(value: Int) {
        encodeColumn(value.toString(), isNumeric = true)
    }

    override fun encodeLong(value: Long) {
        encodeColumn(value.toString(), isNumeric = true)
    }

    override fun encodeFloat(value: Float) {
        encodeColumn(value.toString(), isNumeric = true)
    }

    override fun encodeDouble(value: Double) {
        encodeColumn(value.toString(), isNumeric = true)
    }

    override fun encodeBoolean(value: Boolean) {
        encodeColumn(value.toString(), isNumeric = false)
    }

    override fun encodeChar(value: Char) {
        encodeColumn(value.toString())
    }

    override fun encodeString(value: String) {
        encodeColumn(value)
    }

    override fun encodeNull() {
        encodeColumn(configuration.nullString, isNull = true)
    }

    override fun encodeUnit() {
        encodeColumn(configuration.unitString)
    }

    override fun encodeEnum(enumDescription: SerialDescriptor, ordinal: Int) {
        encodeColumn(enumDescription.getElementName(ordinal))
    }

    protected fun printHeaderRecord(desc: SerialDescriptor) {
        writer.beginRecord()
        printHeader("", desc)
        writer.endRecord()
    }

    private fun printHeader(prefix: String, desc: SerialDescriptor) {
        when (desc.kind) {
            is StructureKind.LIST,
            is StructureKind.MAP,
            is PolymorphicKind.OPEN -> {
                error("CSV headers are not supported for variable sized type '${desc.kind}'.")
            }
        }

        for (i in 0 until desc.elementsCount) {
            val name = prefix + desc.getElementName(i)
            val childDesc = desc.getElementDescriptor(i)

            when {
                childDesc.kind is UnionKind ->
                    writer.printColumn(name)

                childDesc.kind is PolymorphicKind.SEALED -> {
                    writer.printColumn(name)
                    val headerSeparator = configuration.headerSeparator
                    printHeader("$name$headerSeparator", childDesc)
                }

                childDesc.elementsCount > 0 -> {
                    val headerSeparator = configuration.headerSeparator
                    printHeader("$name$headerSeparator", childDesc)
                }

                else ->
                    writer.printColumn(name)
            }
        }
    }

    protected open fun encodeCollectionSize(collectionSize: Int) {
        writer.printColumn(collectionSize.toString(), isNumeric = true)
    }

    protected open fun encodeColumn(value: String, isNumeric: Boolean = false, isNull: Boolean = false) {
        writer.printColumn(value, isNumeric, isNull)
    }
}
