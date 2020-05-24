package kotlinx.serialization.csv.config

import kotlinx.serialization.builtins.list
import kotlinx.serialization.builtins.serializer
import kotlinx.serialization.csv.Csv
import kotlinx.serialization.csv.CsvConfiguration
import kotlinx.serialization.csv.records.IntStringRecord
import kotlinx.serialization.csv.records.NullRecord
import kotlinx.serialization.test.assertParse
import kotlin.test.Test

/**
 * Test [Csv] with simple primitive records.
 */
class CsvEmptyLinesTest {

    @Test
    fun testIntList() = assertParse(
        "\r\n-150\r\n150\r\n\r\n42\r\n",
        listOf(-150, 150, 42),
        Int.serializer().list,
        Csv(
            CsvConfiguration(
                ignoreEmptyLines = true
            )
        )
    )

    @Test
    fun testEmptyIntList() = assertParse(
        "\r\n\r\n\r\n\r\n",
        listOf(),
        Int.serializer().list,
        Csv(
            CsvConfiguration(
                ignoreEmptyLines = true
            )
        )
    )

    @Test
    fun testRecordList() = assertParse(
        "\r\n\r\n1,testing\r\n\r\n2,bar\r\n\r\n",
        listOf(
            IntStringRecord(1, "testing"),
            IntStringRecord(2, "bar")
        ),
        IntStringRecord.serializer().list,
        Csv(
            CsvConfiguration(
                ignoreEmptyLines = true
            )
        )
    )

    @Test
    fun testEmptyRecordList() = assertParse(
        "\r\n\r\n\r\n\r\n",
        listOf(),
        IntStringRecord.serializer().list,
        Csv(
            CsvConfiguration(
                ignoreEmptyLines = true
            )
        )
    )

    @Test
    fun testRecordListWithHeaders() = assertParse(
        "\r\na,b\r\n\r\n1,testing\r\n\r\n2,bar\r\n\r\n",
        listOf(
            IntStringRecord(1, "testing"),
            IntStringRecord(2, "bar")
        ),
        IntStringRecord.serializer().list,
        Csv(
            CsvConfiguration(
                ignoreEmptyLines = true,
                hasHeaderRecord = true
            )
        )
    )

    @Test
    fun testEmptyRecordListWithHeaders() = assertParse(
        "\r\na,b\r\n\r\n\r\n\r\n",
        listOf(),
        IntStringRecord.serializer().list,
        Csv(
            CsvConfiguration(
                ignoreEmptyLines = true,
                hasHeaderRecord = true
            )
        )
    )

    @Test
    fun testListOfIntList() = assertParse(
        "\r\n\r\n1,2\r\n\r\n3,4\r\n\r\n5,6,7\r\n",
        listOf(
            listOf(1, 2),
            listOf(3, 4),
            listOf(5, 6, 7)
        ),
        Int.serializer().list.list,
        Csv(
            CsvConfiguration(
                ignoreEmptyLines = true
            )
        )
    )

    @Test
    fun testEmptyListOfIntList() = assertParse(
        "\r\n\r\n\r\n\r\n",
        listOf(),
        Int.serializer().list.list,
        Csv(
            CsvConfiguration(
                ignoreEmptyLines = true
            )
        )
    )

    @Test
    fun testNullRecordList() = assertParse(
        "\r\n\r\nnull\r\n\r\nnull\r\n\r\n",
        listOf(
            NullRecord(null),
            NullRecord(null)
        ),
        NullRecord.serializer().list,
        Csv(
            CsvConfiguration(
                ignoreEmptyLines = true,
                nullString = "null"
            )
        )
    )

    @Test
    fun testNullRecordListWithEmptyNullString() = assertParse(
        "\r\n\r\n\r\n\r\n\r\n\r\n",
        listOf(),
        NullRecord.serializer().list,
        Csv(
            CsvConfiguration(
                ignoreEmptyLines = true,
                nullString = ""
            )
        )
    )
}
