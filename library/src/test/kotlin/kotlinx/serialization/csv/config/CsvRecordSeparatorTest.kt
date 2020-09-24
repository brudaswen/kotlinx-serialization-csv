package kotlinx.serialization.csv.config

import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.builtins.ListSerializer
import kotlinx.serialization.csv.Csv
import kotlinx.serialization.csv.CsvConfiguration
import kotlinx.serialization.csv.records.IntRecord
import kotlinx.serialization.test.assertStringFormAndRestored
import kotlin.test.Test

/**
 * Test [Csv] with different [CsvConfiguration.recordSeparator]s.
 */
@OptIn(ExperimentalSerializationApi::class)
class CsvRecordSeparatorTest {

    @Test
    fun testDefault() = assertStringFormAndRestored(
        "1\r\n2\r\n3",
        listOf(
            IntRecord(1),
            IntRecord(2),
            IntRecord(3)
        ),
        ListSerializer(IntRecord.serializer()),
        Csv
    )

    @Test
    fun testWindows() = assertStringFormAndRestored(
        "1\r\n2\r\n3",
        listOf(
            IntRecord(1),
            IntRecord(2),
            IntRecord(3)
        ),
        ListSerializer(IntRecord.serializer()),
        Csv(
            CsvConfiguration(
                recordSeparator = "\r\n"
            )
        )
    )

    @Test
    fun testUnix() = assertStringFormAndRestored(
        "1\n2\n3",
        listOf(
            IntRecord(1),
            IntRecord(2),
            IntRecord(3)
        ),
        ListSerializer(IntRecord.serializer()),
        Csv(
            CsvConfiguration(
                recordSeparator = "\n"
            )
        )
    )

    @Test
    fun testHash() = assertStringFormAndRestored(
        "1#2#3",
        listOf(
            IntRecord(1),
            IntRecord(2),
            IntRecord(3)
        ),
        ListSerializer(IntRecord.serializer()),
        Csv(
            CsvConfiguration(
                recordSeparator = "#"
            )
        )
    )
}