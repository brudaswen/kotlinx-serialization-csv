package kotlinx.serialization.csv.records

import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.builtins.ListSerializer
import kotlinx.serialization.builtins.nullable
import kotlinx.serialization.csv.Csv
import kotlinx.serialization.csv.CsvConfiguration
import kotlinx.serialization.test.assertDecode
import kotlinx.serialization.test.assertEncodeAndDecode
import kotlin.test.Test

/**
 * Test [Csv] with simple nullable [Serializable] records.
 */
@OptIn(ExperimentalSerializationApi::class)
class CsvNullableSimpleRecordTest {

    @Test
    fun testInteger() = Csv.assertEncodeAndDecode(
        "",
        null,
        IntRecord.serializer().nullable
    )

    @Test
    fun testByte() = Csv.assertEncodeAndDecode(
        "",
        null,
        ByteRecord.serializer().nullable
    )

    @Test
    fun testShort() = Csv.assertEncodeAndDecode(
        "",
        null,
        ShortRecord.serializer().nullable
    )

    @Test
    fun testLong() = Csv.assertEncodeAndDecode(
        "",
        null,
        LongRecord.serializer().nullable
    )

    @Test
    fun testFloat() = Csv.assertEncodeAndDecode(
        "",
        null,
        FloatRecord.serializer().nullable
    )

    @Test
    fun testDouble() = Csv.assertEncodeAndDecode(
        "",
        null,
        DoubleRecord.serializer().nullable
    )

    @Test
    fun testBoolean() = Csv.assertEncodeAndDecode(
        "",
        null,
        BooleanRecord.serializer().nullable
    )

    @Test
    fun testChar() = Csv.assertEncodeAndDecode(
        "",
        null,
        CharRecord.serializer().nullable
    )

    @Test
    fun testString() = Csv.assertEncodeAndDecode(
        "",
        null,
        StringRecord.serializer().nullable
    )

    @Test
    fun testNull() = Csv.assertEncodeAndDecode(
        "",
        null,
        NullRecord.serializer().nullable
    )

    @Test
    fun testUnit() = Csv.assertEncodeAndDecode(
        "",
        null,
        UnitRecord.serializer().nullable
    )

    @Test
    fun testEnum() = Csv.assertEncodeAndDecode(
        "",
        null,
        EnumRecord.serializer().nullable
    )

    @Test
    fun testComplex() = Csv.assertEncodeAndDecode(
        "",
        null,
        ComplexRecord.serializer().nullable
    )

    @Test
    fun testIntList() = Csv(
        CsvConfiguration(
            ignoreEmptyLines = false
        )
    ).assertEncodeAndDecode(
        "-150\r\n\r\n150",
        listOf(
            IntRecord(-150), null,
            IntRecord(150)
        ),
        ListSerializer(IntRecord.serializer().nullable)
    )

    @Test
    fun testIntListWithLastLineEmpty() = Csv(
        CsvConfiguration(
            ignoreEmptyLines = false
        )
    ).assertDecode(
        "-150\r\n\r\n150\r\n",
        listOf(
            IntRecord(-150), null,
            IntRecord(150), null
        ),
        ListSerializer(IntRecord.serializer().nullable)
    )
}
