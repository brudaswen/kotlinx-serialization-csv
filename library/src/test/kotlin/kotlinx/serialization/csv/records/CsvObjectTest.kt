package kotlinx.serialization.csv.records

import kotlinx.serialization.csv.Csv
import kotlinx.serialization.csv.CsvConfiguration
import kotlinx.serialization.internal.nullable
import kotlinx.serialization.list
import kotlinx.serialization.test.assertStringFormAndRestored
import kotlin.test.Test

/**
 * Test [Csv] with simple primitive records.
 */
class CsvObjectTest {

    @Test
    fun testObject() = assertStringFormAndRestored(
        "kotlinx.serialization.csv.records.ObjectRecord",
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
        "kotlinx.serialization.csv.records.ObjectRecord\r\n\r\nkotlinx.serialization.csv.records.ObjectRecord",
        listOf(
            ObjectRecord,
            null,
            ObjectRecord
        ),
        ObjectRecord.serializer().nullable.list,
        Csv(
            CsvConfiguration(
                ignoreEmptyLines = false
            )
        )
    )
}
