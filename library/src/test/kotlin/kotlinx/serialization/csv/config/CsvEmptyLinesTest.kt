package kotlinx.serialization.csv.config

import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.builtins.ListSerializer
import kotlinx.serialization.builtins.serializer
import kotlinx.serialization.csv.Csv
import kotlinx.serialization.csv.records.IntStringRecord
import kotlinx.serialization.csv.records.NullRecord
import kotlinx.serialization.test.assertDecode
import kotlin.test.Test

/**
 * Test [Csv] with simple primitive records.
 */
@OptIn(ExperimentalSerializationApi::class)
class CsvEmptyLinesTest {

    @Test
    fun testIntList() = Csv {
        ignoreEmptyLines = true
    }.assertDecode(
        input = "\n-150\n150\n\n42\n",
        expected = listOf(-150, 150, 42),
        serializer = ListSerializer(Int.serializer()),
    )

    @Test
    fun testEmptyIntList() = Csv {
        ignoreEmptyLines = true
    }.assertDecode(
        input = "\n\n\n\n",
        expected = listOf(),
        serializer = ListSerializer(Int.serializer()),
    )

    @Test
    fun testLastLineAlwaysIgnored() = Csv {
        ignoreEmptyLines = false
    }.assertDecode(
        input = "1\n2\n3\n4\n",
        expected = listOf(1, 2, 3, 4),
        serializer = ListSerializer(Int.serializer()),
    )

    @Test
    fun testRecordList() = Csv {
        ignoreEmptyLines = true
    }.assertDecode(
        input = "\n\n1,testing\n\n2,bar\n\n",
        expected = listOf(
            IntStringRecord(1, "testing"),
            IntStringRecord(2, "bar"),
        ),
        serializer = ListSerializer(IntStringRecord.serializer()),
    )

    @Test
    fun testEmptyRecordList() = Csv {
        ignoreEmptyLines = true
    }.assertDecode(
        input = "\n\n\n\n",
        expected = listOf(),
        serializer = ListSerializer(IntStringRecord.serializer()),
    )

    @Test
    fun testRecordListWithHeaders() = Csv {
        ignoreEmptyLines = true
        hasHeaderRecord = true
    }.assertDecode(
        input = "\na,b\n\n1,testing\n\n2,bar\n\n",
        expected = listOf(
            IntStringRecord(1, "testing"),
            IntStringRecord(2, "bar"),
        ),
        serializer = ListSerializer(IntStringRecord.serializer()),
    )

    @Test
    fun testEmptyRecordListWithHeaders() = Csv {
        ignoreEmptyLines = true
        hasHeaderRecord = true
    }.assertDecode(
        input = "\na,b\n\n\n\n",
        expected = listOf(),
        serializer = ListSerializer(IntStringRecord.serializer()),
    )

    @Test
    fun testListOfIntList() = Csv {
        ignoreEmptyLines = true
    }.assertDecode(
        input = "\n\n1,2\n\n3,4\n\n5,6,7\n",
        expected = listOf(
            listOf(1, 2),
            listOf(3, 4),
            listOf(5, 6, 7),
        ),
        serializer = ListSerializer(ListSerializer(Int.serializer())),
    )

    @Test
    fun testEmptyListOfIntList() = Csv {
        ignoreEmptyLines = true
    }.assertDecode(
        input = "\n\n\n\n",
        expected = listOf(),
        serializer = ListSerializer(ListSerializer(Int.serializer())),
    )

    @Test
    fun testNullRecordList() = Csv {
        ignoreEmptyLines = true
        nullString = "null"
    }.assertDecode(
        input = "\n\nnull\n\nnull\n\n",
        expected = listOf(
            NullRecord(null),
            NullRecord(null),
        ),
        serializer = ListSerializer(NullRecord.serializer()),
    )

    @Test
    fun testNullRecordListWithEmptyNullString() = Csv {
        ignoreEmptyLines = true
        nullString = ""
    }.assertDecode(
        input = "\n\n\n\n\n\n",
        expected = listOf(),
        serializer = ListSerializer(NullRecord.serializer()),
    )
}
