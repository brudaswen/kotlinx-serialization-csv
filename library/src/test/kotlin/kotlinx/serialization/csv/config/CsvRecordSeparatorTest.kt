package kotlinx.serialization.csv.config

import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.builtins.ListSerializer
import kotlinx.serialization.csv.Csv
import kotlinx.serialization.csv.records.IntRecord
import kotlinx.serialization.test.assertEncodeAndDecode
import kotlin.test.Test

/**
 * Test [Csv] with different [CsvConfig.recordSeparator]s.
 */
@OptIn(ExperimentalSerializationApi::class)
class CsvRecordSeparatorTest {

    @Test
    fun testDefault() = Csv.assertEncodeAndDecode(
        expected = "1\n2\n3",
        original = listOf(
            IntRecord(1),
            IntRecord(2),
            IntRecord(3),
        ),
        serializer = ListSerializer(IntRecord.serializer()),
    )

    @Test
    fun testWindows() = Csv {
        recordSeparator = "\n"
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
    fun testUnix() = Csv {
        recordSeparator = "\n"
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
    fun testHash() = Csv {
        recordSeparator = "#"
    }.assertEncodeAndDecode(
        expected = "1#2#3",
        original = listOf(
            IntRecord(1),
            IntRecord(2),
            IntRecord(3),
        ),
        serializer = ListSerializer(IntRecord.serializer()),
    )
}
