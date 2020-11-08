package kotlinx.serialization.csv.config

import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.builtins.ListSerializer
import kotlinx.serialization.builtins.serializer
import kotlinx.serialization.csv.Csv
import kotlinx.serialization.csv.records.Enum
import kotlinx.serialization.csv.records.IntRecord
import kotlinx.serialization.test.assertEncodeAndDecode
import kotlin.test.Test

@OptIn(ExperimentalSerializationApi::class)
class CsvHasTrailingDelimiterTest {

    @Test
    fun testDefault() = Csv.assertEncodeAndDecode(
        "1",
        IntRecord(1),
        IntRecord.serializer()
    )

    @Test
    fun testWithoutTrailingDelimiter() = Csv {
        hasTrailingDelimiter = false
    }.assertEncodeAndDecode(
        "1",
        IntRecord(1),
        IntRecord.serializer()
    )

    @Test
    fun testWithTrailingDelimiter() = Csv {
        hasTrailingDelimiter = true
    }.assertEncodeAndDecode(
        "1,",
        IntRecord(1),
        IntRecord.serializer()
    )

    @Test
    fun testWithTrailingDelimiterAndHeaderRecord() = Csv {
        hasTrailingDelimiter = true
        hasHeaderRecord = true
    }.assertEncodeAndDecode(
        "a,\n1,",
        IntRecord(1),
        IntRecord.serializer()
    )

    @Test
    fun testListWithoutTrailingDelimiter() = Csv {
        hasTrailingDelimiter = false
    }.assertEncodeAndDecode(
        "1\n2\n3",
        listOf(
            IntRecord(1),
            IntRecord(2),
            IntRecord(3)
        ),
        ListSerializer(IntRecord.serializer())
    )

    @Test
    fun testListWithTrailingDelimiter() = Csv {
        hasTrailingDelimiter = true
    }.assertEncodeAndDecode(
        "1,\n2,\n3,",
        listOf(
            IntRecord(1),
            IntRecord(2),
            IntRecord(3)
        ),
        ListSerializer(IntRecord.serializer())
    )

    @Test
    fun testListWithTrailingDelimiterAndHeaderRecord() = Csv {
        hasTrailingDelimiter = true
        hasHeaderRecord = true
    }.assertEncodeAndDecode(
        "a,\n1,\n2,\n3,",
        listOf(
            IntRecord(1),
            IntRecord(2),
            IntRecord(3)
        ),
        ListSerializer(IntRecord.serializer())
    )

    @Test
    fun testPrimitiveWithTrailingDelimiter() = Csv {
        hasTrailingDelimiter = true
    }.assertEncodeAndDecode(
        "1,",
        1,
        Int.serializer()
    )

    @Test
    fun testPrimitiveListWithTrailingDelimiter() = Csv {
        hasTrailingDelimiter = true
    }.assertEncodeAndDecode(
        "-150,\n150,\n42,",
        listOf(-150, 150, 42),
        ListSerializer(Int.serializer())
    )

    @Test
    fun testEnumWithTrailingDelimiter() = Csv {
        hasTrailingDelimiter = true
    }.assertEncodeAndDecode(
        "FIRST,",
        Enum.FIRST,
        Enum.serializer()
    )

    @Test
    fun testEnumListWithTrailingDelimiter() = Csv {
        hasTrailingDelimiter = true
    }.assertEncodeAndDecode(
        "FIRST,\nFIRST,",
        listOf(
            Enum.FIRST,
            Enum.FIRST
        ),
        ListSerializer(Enum.serializer())
    )
}