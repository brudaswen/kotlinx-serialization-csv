package kotlinx.serialization.csv.records

import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.builtins.ListSerializer
import kotlinx.serialization.csv.Csv
import kotlinx.serialization.test.assertDecode
import kotlinx.serialization.test.assertEncodeAndDecode
import kotlin.test.Test

/**
 * Test [Csv] with simple [Serializable] records.
 */
@OptIn(ExperimentalSerializationApi::class)
class CsvSimpleRecordTest {

    @Test
    fun testInteger() = Csv.assertEncodeAndDecode(
        expected = "-150",
        original = IntRecord(-150),
        serializer = IntRecord.serializer(),
    )

    @Test
    fun testByte() = Csv.assertEncodeAndDecode(
        expected = "-15",
        original = ByteRecord(-15),
        serializer = ByteRecord.serializer(),
    )

    @Test
    fun testShort() = Csv.assertEncodeAndDecode(
        expected = "-150",
        original = ShortRecord(-150),
        serializer = ShortRecord.serializer(),
    )

    @Test
    fun testLong() = Csv.assertEncodeAndDecode(
        expected = "-150",
        original = LongRecord(-150),
        serializer = LongRecord.serializer(),
    )

    @Test
    fun testFloat() = Csv.assertEncodeAndDecode(
        expected = "-150.0",
        original = FloatRecord(-150.0f),
        serializer = FloatRecord.serializer(),
    )

    @Test
    fun testDouble() = Csv.assertEncodeAndDecode(
        expected = "-150.0",
        original = DoubleRecord(-150.0),
        serializer = DoubleRecord.serializer(),
    )

    @Test
    fun testBoolean() = Csv.assertEncodeAndDecode(
        expected = "true",
        original = BooleanRecord(true),
        serializer = BooleanRecord.serializer(),
    )

    @Test
    fun testChar() = Csv.assertEncodeAndDecode(
        expected = "t",
        original = CharRecord('t'),
        serializer = CharRecord.serializer(),
    )

    @Test
    fun testString() = Csv.assertEncodeAndDecode(
        expected = "testing",
        original = StringRecord("testing"),
        serializer = StringRecord.serializer(),
    )

    @Test
    fun testNull() = Csv.assertEncodeAndDecode(
        expected = "",
        original = NullRecord(null),
        serializer = NullRecord.serializer(),
    )

    @Test
    fun testUnit() = Csv.assertEncodeAndDecode(
        expected = "kotlin.Unit",
        original = UnitRecord(Unit),
        serializer = UnitRecord.serializer(),
    )

    @Test
    fun testEnum() = Csv.assertEncodeAndDecode(
        expected = "FIRST",
        original = EnumRecord(Enum.FIRST),
        serializer = EnumRecord.serializer(),
    )

    @Test
    fun testComplex() = Csv.assertEncodeAndDecode(
        expected = "-150,-1,42,9223372036854775807,-2.0,24.24,true,testing,,kotlin.Unit,FIRST",
        original = ComplexRecord(
            a = -150,
            b = -1,
            c = 42,
            d = Long.MAX_VALUE,
            e = -2f,
            f = 24.24,
            g = true,
            h = "testing",
            i = null,
            j = Unit,
            k = Enum.FIRST,
        ),
        serializer = ComplexRecord.serializer(),
    )

    @Test
    fun testIntList() = Csv.assertEncodeAndDecode(
        expected = "-150\n150",
        original = listOf(
            IntRecord(-150),
            IntRecord(150),
        ),
        serializer = ListSerializer(IntRecord.serializer()),
    )

    @Test
    fun testIntListWithLastLineEmpty() = Csv.assertDecode(
        input = "-150\n150\n",
        expected = listOf(
            IntRecord(-150),
            IntRecord(150),
        ),
        serializer = ListSerializer(IntRecord.serializer()),
    )
}
