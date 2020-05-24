package kotlinx.serialization.csv.encode

import kotlinx.serialization.*
import kotlinx.serialization.builtins.AbstractEncoder
import kotlinx.serialization.csv.Csv
import kotlinx.serialization.modules.SerialModule

/**
 * Default CSV encoder.
 */
internal abstract class CsvEncoder(
    protected val csv: Csv,
    protected val writer: CsvWriter,
    private val parent: CsvEncoder?
) : AbstractEncoder() {

    override val context: SerialModule = csv.context

    protected val configuration
        get() = csv.configuration

    override fun beginCollection(
        descriptor: SerialDescriptor,
        collectionSize: Int,
        vararg typeSerializers: KSerializer<*>
    ): CompositeEncoder {
        encodeCollectionSize(collectionSize)
        return super.beginCollection(descriptor, collectionSize, *typeSerializers)
    }

    override fun beginStructure(
        descriptor: SerialDescriptor,
        vararg typeSerializers: KSerializer<*>
    ): CompositeEncoder {
        return when (descriptor.kind) {
            StructureKind.LIST,
            StructureKind.MAP ->
                SimpleCsvEncoder(csv, writer, this)

            StructureKind.CLASS ->
                SimpleCsvEncoder(csv, writer, this)

            StructureKind.OBJECT ->
                ObjectCsvEncoder(csv, writer, this)

            PolymorphicKind.SEALED ->
                SealedCsvEncoder(csv, writer, this, descriptor)

            PolymorphicKind.OPEN ->
                SimpleCsvEncoder(csv, writer, this)

            else ->
                error("CSV does not support '${descriptor.kind}'.")
        }
    }

    override fun endStructure(descriptor: SerialDescriptor) {
        parent?.endChildStructure(descriptor)
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

    override fun encodeEnum(enumDescriptor: SerialDescriptor, index: Int) {
        encodeColumn(enumDescriptor.getElementName(index))
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
                    printHeader("$name$headerSeparator", childDesc.getElementDescriptor(1))
                }
                childDesc.kind is StructureKind.OBJECT ->
                    Unit

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
