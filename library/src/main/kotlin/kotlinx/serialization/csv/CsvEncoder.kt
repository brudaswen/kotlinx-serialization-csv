/*
 * Copyright 2017-2019 JetBrains s.r.o. Use of this source code is governed by the Apache 2.0 license.
 */

package kotlinx.serialization.csv

import kotlinx.serialization.*
import kotlinx.serialization.modules.SerialModule

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

            if (childDesc.kind is UnionKind) {
                writer.printColumn(name)
            } else if (childDesc.kind is PolymorphicKind.SEALED) {
                writer.printColumn(name)
                val headerSeparator = configuration.headerSeparator
                printHeader("$name$headerSeparator", childDesc)
            } else if (childDesc.elementsCount > 0) {
                val headerSeparator = configuration.headerSeparator
                printHeader("$name$headerSeparator", childDesc)
            } else {
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

/**
 * Initial entry point for encoding.
 */
internal class RootCsvEncoder(
    csv: Csv,
    writer: CsvWriter
) : CsvEncoder(csv, writer, null) {

    internal constructor(csv: Csv, output: Appendable) :
            this(csv, CsvWriter(output, csv.configuration))

    override fun beginCollection(
        desc: SerialDescriptor,
        collectionSize: Int,
        vararg typeParams: KSerializer<*>
    ): CompositeEncoder {
        return if (desc.kind == StructureKind.LIST) {
            RecordListCsvEncoder(csv, writer)
        } else {
            super.beginCollection(desc, collectionSize, *typeParams)
        }
    }

    override fun beginStructure(desc: SerialDescriptor, vararg typeParams: KSerializer<*>): CompositeEncoder {
        if (configuration.hasHeaderRecord && writer.isFirstRecord) {
            printHeaderRecord(desc)
        }
        writer.beginRecord()
        return super.beginStructure(desc, *typeParams)
    }

    override fun endChildStructure(desc: SerialDescriptor) {
        writer.endRecord()
    }

    override fun encodeColumn(value: String, isNumeric: Boolean, isNull: Boolean) {
        writer.beginRecord()
        super.encodeColumn(value, isNumeric, isNull)
        writer.endRecord()
    }
}

/**
 * Encodes CSV records (lines).
 */
internal class RecordListCsvEncoder(
    csv: Csv,
    writer: CsvWriter
) : CsvEncoder(csv, writer, null) {

    override fun beginStructure(desc: SerialDescriptor, vararg typeParams: KSerializer<*>): CompositeEncoder {
        // For complex records: Begin a new record and end it in [endChildStructure]
        if (configuration.hasHeaderRecord && writer.isFirstRecord) {
            printHeaderRecord(desc)
        }
        writer.beginRecord()
        return super.beginStructure(desc, *typeParams)
    }

    override fun endChildStructure(desc: SerialDescriptor) {
        // For complex records: End the record here
        writer.endRecord()
    }

    override fun encodeCollectionSize(collectionSize: Int) {
        // Collection records do not write their size.
        // Instead the size is implicitly determined by reading until end-of-line.
    }

    override fun encodeColumn(value: String, isNumeric: Boolean, isNull: Boolean) {
        // For simple one-column records: Begin and end record here
        writer.beginRecord()
        super.encodeColumn(value, isNumeric, isNull)
        writer.endRecord()
    }
}

internal open class SimpleCsvEncoder(
    csv: Csv,
    writer: CsvWriter,
    parent: CsvEncoder
) : CsvEncoder(csv, writer, parent)

internal class ObjectCsvEncoder(
    csv: Csv,
    writer: CsvWriter,
    parent: CsvEncoder
) : SimpleCsvEncoder(csv, writer, parent) {

    override fun endStructure(desc: SerialDescriptor) {
        encodeString(desc.name)
        super.endStructure(desc)
    }
}

internal class SealedCsvEncoder(
    csv: Csv,
    writer: CsvWriter,
    parent: CsvEncoder,
    private val sealedDesc: SerialDescriptor
) : SimpleCsvEncoder(csv, writer, parent) {

    override fun beginStructure(desc: SerialDescriptor, vararg typeParams: KSerializer<*>): CompositeEncoder {
        val sealedChildren = sealedDesc.elementDescriptors()
        val index = sealedChildren.indexOf(desc)
        for (innerDesc in sealedChildren.subList(0, index)) {
            printEmptyColumns(innerDesc)
        }
        return super.beginStructure(desc, *typeParams)
    }

    override fun endChildStructure(desc: SerialDescriptor) {
        val sealedChildren = sealedDesc.elementDescriptors()
        val index = sealedChildren.indexOf(desc)
        for (innerDesc in sealedChildren.subList(index + 1, sealedChildren.size)) {
            printEmptyColumns(innerDesc)
        }
    }

    private fun printEmptyColumns(desc: SerialDescriptor) {
        for (innerDesc in desc.elementDescriptors()) {
            encodeNull()
        }
    }
}
