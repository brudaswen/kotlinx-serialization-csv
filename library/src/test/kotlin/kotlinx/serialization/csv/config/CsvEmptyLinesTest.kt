package kotlinx.serialization.csv.config

import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.builtins.ListSerializer
import kotlinx.serialization.builtins.serializer
import kotlinx.serialization.csv.Csv
import kotlinx.serialization.csv.CsvConfiguration
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
    fun testIntList() = Csv(
        CsvConfiguration(
            ignoreEmptyLines = true
        )
    ).assertDecode(
        "\r\n-150\r\n150\r\n\r\n42\r\n",
        listOf(-150, 150, 42),
        ListSerializer(Int.serializer())
    )

    @Test
    fun testEmptyIntList() = Csv(
        CsvConfiguration(
            ignoreEmptyLines = true
        )
    ).assertDecode(
        "\r\n\r\n\r\n\r\n",
        listOf(),
        ListSerializer(Int.serializer())
    )

    @Test
    fun testRecordList() = Csv(
        CsvConfiguration(
            ignoreEmptyLines = true
        )
    ).assertDecode(
        "\r\n\r\n1,testing\r\n\r\n2,bar\r\n\r\n",
        listOf(
            IntStringRecord(1, "testing"),
            IntStringRecord(2, "bar")
        ),
        ListSerializer(IntStringRecord.serializer())
    )

    @Test
    fun testEmptyRecordList() = Csv(
        CsvConfiguration(
            ignoreEmptyLines = true
        )
    ).assertDecode(
        "\r\n\r\n\r\n\r\n",
        listOf(),
        ListSerializer(IntStringRecord.serializer()),
    )

    @Test
    fun testRecordListWithHeaders() = Csv(
        CsvConfiguration(
            ignoreEmptyLines = true,
            hasHeaderRecord = true
        )
    ).assertDecode(
        "\r\na,b\r\n\r\n1,testing\r\n\r\n2,bar\r\n\r\n",
        listOf(
            IntStringRecord(1, "testing"),
            IntStringRecord(2, "bar")
        ),
        ListSerializer(IntStringRecord.serializer())
    )

    @Test
    fun testEmptyRecordListWithHeaders() = Csv(
        CsvConfiguration(
            ignoreEmptyLines = true,
            hasHeaderRecord = true
        )
    ).assertDecode(
        "\r\na,b\r\n\r\n\r\n\r\n",
        listOf(),
        ListSerializer(IntStringRecord.serializer())
    )

    @Test
    fun testListOfIntList() = Csv(
        CsvConfiguration(
            ignoreEmptyLines = true
        )
    ).assertDecode(
        "\r\n\r\n1,2\r\n\r\n3,4\r\n\r\n5,6,7\r\n",
        listOf(
            listOf(1, 2),
            listOf(3, 4),
            listOf(5, 6, 7)
        ),
        ListSerializer(ListSerializer(Int.serializer()))
    )

    @Test
    fun testEmptyListOfIntList() = Csv(
        CsvConfiguration(
            ignoreEmptyLines = true
        )
    ).assertDecode(
        "\r\n\r\n\r\n\r\n",
        listOf(),
        ListSerializer(ListSerializer(Int.serializer()))
    )

    @Test
    fun testNullRecordList() = Csv(
        CsvConfiguration(
            ignoreEmptyLines = true,
            nullString = "null"
        )
    ).assertDecode(
        "\r\n\r\nnull\r\n\r\nnull\r\n\r\n",
        listOf(
            NullRecord(null),
            NullRecord(null)
        ),
        ListSerializer(NullRecord.serializer())
    )

    @Test
    fun testNullRecordListWithEmptyNullString() = Csv(
        CsvConfiguration(
            ignoreEmptyLines = true,
            nullString = ""
        )
    ).assertDecode(
        "\r\n\r\n\r\n\r\n\r\n\r\n",
        listOf(),
        ListSerializer(NullRecord.serializer())
    )
}
