package kotlinx.serialization.csv.config

import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.builtins.ListSerializer
import kotlinx.serialization.csv.Csv
import kotlinx.serialization.csv.CsvConfiguration
import kotlinx.serialization.csv.records.IntRecord
import kotlinx.serialization.test.assertEncodeAndDecode
import kotlin.test.Test

/**
 * Test [Csv] with different [CsvConfiguration.recordSeparator]s.
 */
@OptIn(ExperimentalSerializationApi::class)
class CsvRecordSeparatorTest {

    @Test
    fun testDefault() = Csv.assertEncodeAndDecode(
        "1\r\n2\r\n3",
        listOf(
            IntRecord(1),
            IntRecord(2),
            IntRecord(3)
        ),
        ListSerializer(IntRecord.serializer())
    )

    @Test
    fun testWindows() = Csv(
        CsvConfiguration(
            recordSeparator = "\r\n"
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
    fun testUnix() = Csv(
        CsvConfiguration(
            recordSeparator = "\n"
        )
    ).assertEncodeAndDecode(
        "1\n2\n3",
        listOf(
            IntRecord(1),
            IntRecord(2),
            IntRecord(3)
        ),
        ListSerializer(IntRecord.serializer())
    )

    @Test
    fun testHash() = Csv(
        CsvConfiguration(
            recordSeparator = "#"
        )
    ).assertEncodeAndDecode(
        "1#2#3",
        listOf(
            IntRecord(1),
            IntRecord(2),
            IntRecord(3)
        ),
        ListSerializer(IntRecord.serializer())
    )
}