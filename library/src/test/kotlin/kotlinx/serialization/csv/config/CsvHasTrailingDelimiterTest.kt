package kotlinx.serialization.csv.config

import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.builtins.ListSerializer
import kotlinx.serialization.builtins.serializer
import kotlinx.serialization.csv.Csv
import kotlinx.serialization.csv.CsvConfiguration
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
    fun testWithoutTrailingDelimiter() = Csv(
        CsvConfiguration(
            hasTrailingDelimiter = false
        )
    ).assertEncodeAndDecode(
        "1",
        IntRecord(1),
        IntRecord.serializer()
    )

    @Test
    fun testWithTrailingDelimiter() = Csv(
        CsvConfiguration(
            hasTrailingDelimiter = true
        )
    ).assertEncodeAndDecode(
        "1,",
        IntRecord(1),
        IntRecord.serializer()
    )

    @Test
    fun testWithTrailingDelimiterAndHeaderRecord() = Csv(
        CsvConfiguration(
            hasTrailingDelimiter = true,
            hasHeaderRecord = true
        )
    ).assertEncodeAndDecode(
        "a,\r\n1,",
        IntRecord(1),
        IntRecord.serializer()
    )

    @Test
    fun testListWithoutTrailingDelimiter() = Csv(
        CsvConfiguration(
            hasTrailingDelimiter = false
        )
    ).assertEncodeAndDecode(
        "1\r\n2\r\n3",
        listOf(
            IntRecord(1),
            IntRecord(2),
            IntRecord(3)
        ),
        ListSerializer(IntRecord.serializer())
    )

    @Test
    fun testListWithTrailingDelimiter() = Csv(
        CsvConfiguration(
            hasTrailingDelimiter = true
        )
    ).assertEncodeAndDecode(
        "1,\r\n2,\r\n3,",
        listOf(
            IntRecord(1),
            IntRecord(2),
            IntRecord(3)
        ),
        ListSerializer(IntRecord.serializer())
    )

    @Test
    fun testListWithTrailingDelimiterAndHeaderRecord() = Csv(
        CsvConfiguration(
            hasTrailingDelimiter = true,
            hasHeaderRecord = true
        )
    ).assertEncodeAndDecode(
        "a,\r\n1,\r\n2,\r\n3,",
        listOf(
            IntRecord(1),
            IntRecord(2),
            IntRecord(3)
        ),
        ListSerializer(IntRecord.serializer())
    )

    @Test
    fun testPrimitiveWithTrailingDelimiter() = Csv(
        CsvConfiguration(
            hasTrailingDelimiter = true
        )
    ).assertEncodeAndDecode(
        "1,",
        1,
        Int.serializer()
    )

    @Test
    fun testPrimitiveListWithTrailingDelimiter() = Csv(
        CsvConfiguration(
            hasTrailingDelimiter = true
        )
    ).assertEncodeAndDecode(
        "-150,\r\n150,\r\n42,",
        listOf(-150, 150, 42),
        ListSerializer(Int.serializer())
    )

    @Test
    fun testEnumWithTrailingDelimiter() = Csv(
        CsvConfiguration(
            hasTrailingDelimiter = true
        )
    ).assertEncodeAndDecode(
        "FIRST,",
        Enum.FIRST,
        Enum.serializer()
    )

    @Test
    fun testEnumListWithTrailingDelimiter() = Csv(
        CsvConfiguration(
            hasTrailingDelimiter = true
        )
    ).assertEncodeAndDecode(
        "FIRST,\r\nFIRST,",
        listOf(
            Enum.FIRST,
            Enum.FIRST
        ),
        ListSerializer(Enum.serializer())
    )
}