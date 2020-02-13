/*
 * Copyright 2017-2019 JetBrains s.r.o. Use of this source code is governed by the Apache 2.0 license.
 */

package kotlinx.serialization.csv

import kotlinx.serialization.*

internal abstract class CsvEncoder(
        protected val configuration: CsvConfiguration,
        protected val writer: CsvWriter,
        private val parent: CsvEncoder?
) : ElementValueEncoder() {

    override fun beginStructure(desc: SerialDescriptor, vararg typeParams: KSerializer<*>): CompositeEncoder {
        return when (desc.kind) {
            StructureKind.LIST ->
                SimpleCsvEncoder(configuration, writer, this)

            StructureKind.CLASS ->
                SimpleCsvEncoder(configuration, writer, this)

            UnionKind.OBJECT ->
                ObjectCsvEncoder(configuration, writer, this)

            PolymorphicKind.SEALED ->
                SealedCsvEncoder(configuration, writer, this, desc)

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
        for (i in 0 until desc.elementsCount) {
            val name = prefix + desc.getElementName(i)
            val childDesc = desc.getElementDescriptor(i)
            if (childDesc.elementsCount > 0) {
                val headerSeparator = configuration.headerSeparator
                printHeader("$name$headerSeparator", childDesc)
            } else {
                writer.printColumn(name)
            }
        }
    }

    protected open fun encodeColumn(value: String, isNumeric: Boolean = false, isNull: Boolean = false) {
        writer.printColumn(value, isNumeric, isNull)
    }
}

internal class RootCsvEncoder(
        configuration: CsvConfiguration,
        writer: CsvWriter
) : CsvEncoder(configuration, writer, null) {

    internal constructor(configuration: CsvConfiguration, output: Appendable) :
            this(configuration, CsvWriter(output, configuration))

    override fun beginStructure(desc: SerialDescriptor, vararg typeParams: KSerializer<*>): CompositeEncoder {
        return when (desc.kind) {
            StructureKind.LIST ->
                RecordListCsvEncoder(configuration, writer)

            else -> {
                if (configuration.hasHeaderRecord && writer.isFirstRecord) {
                    printHeaderRecord(desc)
                }
                writer.beginRecord()
                super.beginStructure(desc, *typeParams)
            }
        }
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

internal class RecordListCsvEncoder(
        configuration: CsvConfiguration,
        writer: CsvWriter
) : CsvEncoder(configuration, writer, null) {

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

internal class SimpleCsvEncoder(
        configuration: CsvConfiguration,
        writer: CsvWriter,
        parent: CsvEncoder
) : CsvEncoder(configuration, writer, parent)

internal class ObjectCsvEncoder(
        configuration: CsvConfiguration,
        writer: CsvWriter,
        parent: CsvEncoder
) : CsvEncoder(configuration, writer, parent) {

    override fun endStructure(desc: SerialDescriptor) {
        encodeString(desc.serialName)
        super.endStructure(desc)
    }
}

internal class SealedCsvEncoder(
        configuration: CsvConfiguration,
        writer: CsvWriter,
        parent: CsvEncoder,
        private val sealedDesc: SerialDescriptor
) : CsvEncoder(configuration, writer, parent) {

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
