package kotlinx.serialization.csv

import kotlinx.serialization.list
import kotlinx.serialization.serializer
import kotlinx.serialization.test.assertStringFormAndRestored
import kotlin.test.Test

class CsvHasTrailingDelimiterTest {

    @Test
    fun testDefault() = assertStringFormAndRestored(
            "1",
            IntRecord(1),
            IntRecord.serializer(),
            Csv
    )

    @Test
    fun testWithoutTrailingDelimiter() = assertStringFormAndRestored(
            "1",
            IntRecord(1),
            IntRecord.serializer(),
            Csv(CsvConfiguration(hasTrailingDelimiter = false))
    )

    @Test
    fun testWithTrailingDelimiter() = assertStringFormAndRestored(
            "1,",
            IntRecord(1),
            IntRecord.serializer(),
            Csv(CsvConfiguration(hasTrailingDelimiter = true))
    )

    @Test
    fun testWithTrailingDelimiterAndHeaderRecord() = assertStringFormAndRestored(
            "a,\r\n1,",
            IntRecord(1),
            IntRecord.serializer(),
            Csv(CsvConfiguration(hasTrailingDelimiter = true, hasHeaderRecord = true))
    )

    @Test
    fun testListWithoutTrailingDelimiter() = assertStringFormAndRestored(
            "1\r\n2\r\n3",
            listOf(IntRecord(1), IntRecord(2), IntRecord(3)),
            IntRecord.serializer().list,
            Csv(CsvConfiguration(hasTrailingDelimiter = false))
    )

    @Test
    fun testListWithTrailingDelimiter() = assertStringFormAndRestored(
            "1,\r\n2,\r\n3,",
            listOf(IntRecord(1), IntRecord(2), IntRecord(3)),
            IntRecord.serializer().list,
            Csv(CsvConfiguration(hasTrailingDelimiter = true))
    )

    @Test
    fun testListWithTrailingDelimiterAndHeaderRecord() = assertStringFormAndRestored(
            "a,\r\n1,\r\n2,\r\n3,",
            listOf(IntRecord(1), IntRecord(2), IntRecord(3)),
            IntRecord.serializer().list,
            Csv(CsvConfiguration(hasTrailingDelimiter = true, hasHeaderRecord = true))
    )

    @Test
    fun testPrimitiveWithTrailingDelimiter() = assertStringFormAndRestored(
            "1,",
            1,
            Int.serializer(),
            Csv(CsvConfiguration(hasTrailingDelimiter = true))
    )

    @Test
    fun testPrimitiveListWithTrailingDelimiter() = assertStringFormAndRestored(
            "-150,\r\n150,\r\n42,",
            listOf(-150, 150, 42),
            Int.serializer().list,
            Csv(CsvConfiguration(hasTrailingDelimiter = true))
    )

    @Test
    fun testEnumWithTrailingDelimiter() = assertStringFormAndRestored(
            "FIRST,",
            Enum.FIRST,
            Enum.serializer(),
            Csv(CsvConfiguration(hasTrailingDelimiter = true))
    )

    @Test
    fun testEnumListWithTrailingDelimiter() = assertStringFormAndRestored(
            "FIRST,\r\nFIRST,",
            listOf(Enum.FIRST, Enum.FIRST),
            Enum.serializer().list,
            Csv(CsvConfiguration(hasTrailingDelimiter = true))
    )
}