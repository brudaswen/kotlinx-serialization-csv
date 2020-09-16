package kotlinx.serialization.csv.records

import kotlinx.serialization.builtins.ListSerializer
import kotlinx.serialization.builtins.list
import kotlinx.serialization.builtins.nullable
import kotlinx.serialization.csv.Csv
import kotlinx.serialization.csv.CsvConfiguration
import kotlinx.serialization.test.assertParse
import kotlinx.serialization.test.assertStringFormAndRestored
import kotlin.test.Test

/**
 * Test [Csv] with simple nullable [Serializable] records.
 */
class CsvNullableSimpleRecordTest {

    @Test
    fun testInteger() = assertStringFormAndRestored(
        "",
        null,
        IntRecord.serializer().nullable,
        Csv
    )

    @Test
    fun testByte() = assertStringFormAndRestored(
        "",
        null,
        ByteRecord.serializer().nullable,
        Csv
    )

    @Test
    fun testShort() = assertStringFormAndRestored(
        "",
        null,
        ShortRecord.serializer().nullable,
        Csv
    )

    @Test
    fun testLong() = assertStringFormAndRestored(
        "",
        null,
        LongRecord.serializer().nullable,
        Csv
    )

    @Test
    fun testFloat() = assertStringFormAndRestored(
        "",
        null,
        FloatRecord.serializer().nullable,
        Csv
    )

    @Test
    fun testDouble() = assertStringFormAndRestored(
        "",
        null,
        DoubleRecord.serializer().nullable,
        Csv
    )

    @Test
    fun testBoolean() = assertStringFormAndRestored(
        "",
        null,
        BooleanRecord.serializer().nullable,
        Csv
    )

    @Test
    fun testChar() = assertStringFormAndRestored(
        "",
        null,
        CharRecord.serializer().nullable,
        Csv
    )

    @Test
    fun testString() = assertStringFormAndRestored(
        "",
        null,
        StringRecord.serializer().nullable,
        Csv
    )

    @Test
    fun testNull() = assertStringFormAndRestored(
        "",
        null,
        NullRecord.serializer().nullable,
        Csv
    )

    @Test
    fun testUnit() = assertStringFormAndRestored(
        "",
        null,
        UnitRecord.serializer().nullable,
        Csv
    )

    @Test
    fun testEnum() = assertStringFormAndRestored(
        "",
        null,
        EnumRecord.serializer().nullable,
        Csv
    )

    @Test
    fun testComplex() = assertStringFormAndRestored(
        "",
        null,
        ComplexRecord.serializer().nullable,
        Csv
    )

    @Test
    fun testIntList() = assertStringFormAndRestored(
        "-150\r\n\r\n150",
        listOf(
            IntRecord(-150), null,
            IntRecord(150)
        ),
        ListSerializer(IntRecord.serializer().nullable),
        Csv(
            CsvConfiguration(
                ignoreEmptyLines = false
            )
        )
    )

    @Test
    fun testIntListWithLastLineEmpty() = assertParse(
        "-150\r\n\r\n150\r\n",
        listOf(
            IntRecord(-150), null,
            IntRecord(150), null
        ),
        ListSerializer(IntRecord.serializer().nullable),
        Csv(
            CsvConfiguration(
                ignoreEmptyLines = false
            )
        )
    )
}
