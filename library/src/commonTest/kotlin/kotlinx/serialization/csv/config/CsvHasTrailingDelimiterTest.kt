package kotlinx.serialization.csv.config

import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.builtins.ListSerializer
import kotlinx.serialization.builtins.serializer
import kotlinx.serialization.csv.Csv
import kotlinx.serialization.csv.records.EnumClass
import kotlinx.serialization.csv.records.IntRecord
import kotlinx.serialization.test.assertEncodeAndDecode
import kotlin.test.Test

@OptIn(ExperimentalSerializationApi::class)
class CsvHasTrailingDelimiterTest {

    @Test
    fun testDefault() = Csv.assertEncodeAndDecode(
        expected = "1",
        original = IntRecord(1),
        serializer = IntRecord.serializer(),
    )

    @Test
    fun testWithoutTrailingDelimiter() = Csv {
        hasTrailingDelimiter = false
    }.assertEncodeAndDecode(
        expected = "1",
        original = IntRecord(1),
        serializer = IntRecord.serializer(),
    )

    @Test
    fun testWithTrailingDelimiter() = Csv {
        hasTrailingDelimiter = true
    }.assertEncodeAndDecode(
        expected = "1,",
        original = IntRecord(1),
        serializer = IntRecord.serializer(),
    )

    @Test
    fun testWithTrailingDelimiterAndHeaderRecord() = Csv {
        hasTrailingDelimiter = true
        hasHeaderRecord = true
    }.assertEncodeAndDecode(
        expected = "a,\n1,",
        original = IntRecord(1),
        serializer = IntRecord.serializer(),
    )

    @Test
    fun testListWithoutTrailingDelimiter() = Csv {
        hasTrailingDelimiter = false
    }.assertEncodeAndDecode(
        expected = "1\n2\n3",
        original = listOf(
            IntRecord(1),
            IntRecord(2),
            IntRecord(3),
        ),
        serializer = ListSerializer(IntRecord.serializer()),
    )

    @Test
    fun testListWithTrailingDelimiter() = Csv {
        hasTrailingDelimiter = true
    }.assertEncodeAndDecode(
        expected = "1,\n2,\n3,",
        original = listOf(
            IntRecord(1),
            IntRecord(2),
            IntRecord(3),
        ),
        serializer = ListSerializer(IntRecord.serializer()),
    )

    @Test
    fun testListWithTrailingDelimiterAndHeaderRecord() = Csv {
        hasTrailingDelimiter = true
        hasHeaderRecord = true
    }.assertEncodeAndDecode(
        expected = "a,\n1,\n2,\n3,",
        original = listOf(
            IntRecord(1),
            IntRecord(2),
            IntRecord(3),
        ),
        serializer = ListSerializer(IntRecord.serializer()),
    )

    @Test
    fun testPrimitiveWithTrailingDelimiter() = Csv {
        hasTrailingDelimiter = true
    }.assertEncodeAndDecode(
        expected = "1,",
        original = 1,
        serializer = Int.serializer(),
    )

    @Test
    fun testPrimitiveListWithTrailingDelimiter() = Csv {
        hasTrailingDelimiter = true
    }.assertEncodeAndDecode(
        expected = "-150,\n150,\n42,",
        original = listOf(-150, 150, 42),
        serializer = ListSerializer(Int.serializer()),
    )

    @Test
    fun testEnumWithTrailingDelimiter() = Csv {
        hasTrailingDelimiter = true
    }.assertEncodeAndDecode(
        expected = "FIRST,",
        original = EnumClass.FIRST,
        serializer = EnumClass.serializer(),
    )

    @Test
    fun testEnumListWithTrailingDelimiter() = Csv {
        hasTrailingDelimiter = true
    }.assertEncodeAndDecode(
        expected = "FIRST,\nFIRST,",
        original = listOf(
            EnumClass.FIRST,
            EnumClass.FIRST,
        ),
        serializer = ListSerializer(EnumClass.serializer()),
    )
}
