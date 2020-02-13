package kotlinx.serialization.csv

import kotlinx.serialization.internal.nullable
import kotlinx.serialization.list
import kotlinx.serialization.test.assertStringFormAndRestored
import kotlin.test.Test

/**
 * Test [Csv] ([CsvEncoder] and [CsvDecoder]) with simple primitive records.
 */
class CsvObjectTest {

    @Test
    fun testObject() = assertStringFormAndRestored(
            "kotlinx.serialization.csv.ObjectRecord",
            ObjectRecord,
            ObjectRecord.serializer(),
            Csv
    )

    @Test
    fun testNullableObject() = assertStringFormAndRestored(
            "",
            null,
            ObjectRecord.serializer().nullable,
            Csv
    )

    @Test
    fun testObjectList() = assertStringFormAndRestored(
            "kotlinx.serialization.csv.ObjectRecord\r\n\r\nkotlinx.serialization.csv.ObjectRecord",
            listOf(
                    ObjectRecord,
                    null,
                    ObjectRecord
            ),
            ObjectRecord.serializer().nullable.list,
            Csv(CsvConfiguration(ignoreEmptyLines = false))
    )
}
