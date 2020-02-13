package kotlinx.serialization.csv

import kotlinx.serialization.list
import kotlinx.serialization.test.assertParse
import kotlinx.serialization.test.assertStringFormAndRestored
import kotlin.test.Test

/**
 * Test [Csv] ([CsvEncoder] and [CsvDecoder]) with simple [Serializable] records.
 */
class CsvSimpleRecordTest {

    @Test
    fun testInteger() = assertStringFormAndRestored(
            "-150",
            IntRecord(-150),
            IntRecord.serializer(),
            Csv
    )

    @Test
    fun testByte() = assertStringFormAndRestored(
            "-15",
            ByteRecord(-15),
            ByteRecord.serializer(),
            Csv
    )

    @Test
    fun testShort() = assertStringFormAndRestored(
            "-150",
            ShortRecord(-150),
            ShortRecord.serializer(),
            Csv
    )

    @Test
    fun testLong() = assertStringFormAndRestored(
            "-150",
            LongRecord(-150),
            LongRecord.serializer(),
            Csv
    )

    @Test
    fun testFloat() = assertStringFormAndRestored(
            "-150.0",
            FloatRecord(-150.0f),
            FloatRecord.serializer(),
            Csv
    )

    @Test
    fun testDouble() = assertStringFormAndRestored(
            "-150.0",
            DoubleRecord(-150.0),
            DoubleRecord.serializer(),
            Csv
    )

    @Test
    fun testBoolean() = assertStringFormAndRestored(
            "true",
            BooleanRecord(true),
            BooleanRecord.serializer(),
            Csv
    )

    @Test
    fun testChar() = assertStringFormAndRestored(
            "t",
            CharRecord('t'),
            CharRecord.serializer(),
            Csv
    )

    @Test
    fun testString() = assertStringFormAndRestored(
            "testing",
            StringRecord("testing"),
            StringRecord.serializer(),
            Csv
    )

    @Test
    fun testNull() = assertStringFormAndRestored(
            "",
            NullRecord(null),
            NullRecord.serializer(),
            Csv
    )

    @Test
    fun testUnit() = assertStringFormAndRestored(
            "kotlin.Unit",
            UnitRecord(Unit),
            UnitRecord.serializer(),
            Csv
    )

    @Test
    fun testEnum() = assertStringFormAndRestored(
            "FIRST",
            EnumRecord(Enum.FIRST),
            EnumRecord.serializer(),
            Csv
    )

    @Test
    fun testComplex() = assertStringFormAndRestored(
            "-150,-1,42,9223372036854775807,-2.0,24.24,true,testing,,kotlin.Unit,FIRST",
            ComplexRecord(-150, -1, 42, Long.MAX_VALUE, -2f, 24.24, true, "testing", null, Unit, Enum.FIRST),
            ComplexRecord.serializer(),
            Csv
    )

    @Test
    fun testIntList() = assertStringFormAndRestored(
            "-150\r\n150",
            listOf(IntRecord(-150), IntRecord(150)),
            IntRecord.serializer().list,
            Csv
    )

    @Test
    fun testIntListWithLastLineEmpty() = assertParse(
            "-150\r\n150\r\n",
            listOf(IntRecord(-150), IntRecord(150)),
            IntRecord.serializer().list,
            Csv
    )
}
