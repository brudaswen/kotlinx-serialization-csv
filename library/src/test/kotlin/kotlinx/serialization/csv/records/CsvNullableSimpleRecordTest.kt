package kotlinx.serialization.csv.records

import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.builtins.ListSerializer
import kotlinx.serialization.builtins.nullable
import kotlinx.serialization.csv.Csv
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
        expected = "",
        original = null,
        serializer = IntRecord.serializer().nullable,
    )

    @Test
    fun testByte() = Csv.assertEncodeAndDecode(
        expected = "",
        original = null,
        serializer = ByteRecord.serializer().nullable,
    )

    @Test
    fun testShort() = Csv.assertEncodeAndDecode(
        expected = "",
        original = null,
        serializer = ShortRecord.serializer().nullable,
    )

    @Test
    fun testLong() = Csv.assertEncodeAndDecode(
        expected = "",
        original = null,
        serializer = LongRecord.serializer().nullable,
    )

    @Test
    fun testFloat() = Csv.assertEncodeAndDecode(
        expected = "",
        original = null,
        serializer = FloatRecord.serializer().nullable,
    )

    @Test
    fun testDouble() = Csv.assertEncodeAndDecode(
        expected = "",
        original = null,
        serializer = DoubleRecord.serializer().nullable,
    )

    @Test
    fun testBoolean() = Csv.assertEncodeAndDecode(
        expected = "",
        original = null,
        serializer = BooleanRecord.serializer().nullable,
    )

    @Test
    fun testChar() = Csv.assertEncodeAndDecode(
        expected = "",
        original = null,
        serializer = CharRecord.serializer().nullable,
    )

    @Test
    fun testString() = Csv.assertEncodeAndDecode(
        expected = "",
        original = null,
        serializer = StringRecord.serializer().nullable,
    )

    @Test
    fun testNull() = Csv.assertEncodeAndDecode(
        expected = "",
        original = null,
        serializer = NullRecord.serializer().nullable,
    )

    @Test
    fun testUnit() = Csv.assertEncodeAndDecode(
        expected = "",
        original = null,
        serializer = UnitRecord.serializer().nullable,
    )

    @Test
    fun testEnum() = Csv.assertEncodeAndDecode(
        expected = "",
        original = null,
        serializer = EnumRecord.serializer().nullable,
    )

    @Test
    fun testComplex() = Csv.assertEncodeAndDecode(
        expected = "",
        original = null,
        serializer = ComplexRecord.serializer().nullable,
    )

    @Test
    fun testIntList() = Csv {
        ignoreEmptyLines = false
    }.assertEncodeAndDecode(
        expected = "-150\n\n150",
        original = listOf(
            IntRecord(-150),
            null,
            IntRecord(150),
        ),
        serializer = ListSerializer(IntRecord.serializer().nullable),
    )

    @Test
    fun testIntListWithLastLineEmpty() = Csv {
        ignoreEmptyLines = false
    }.assertDecode(
        input = "-150\n\n150\n",
        expected = listOf(
            IntRecord(-150),
            null,
            IntRecord(150),
        ),
        serializer = ListSerializer(IntRecord.serializer().nullable),
    )

    @Test
    fun testIntListWithLastTwoLinesEmpty() = Csv {
        ignoreEmptyLines = false
    }.assertDecode(
        input = "-150\n\n150\n\n",
        expected = listOf(
            IntRecord(-150),
            null,
            IntRecord(150),
            null,
        ),
        serializer = ListSerializer(IntRecord.serializer().nullable),
    )
}
