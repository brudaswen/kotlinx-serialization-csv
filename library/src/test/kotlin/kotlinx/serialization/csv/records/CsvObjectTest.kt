package kotlinx.serialization.csv.records

import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.builtins.ListSerializer
import kotlinx.serialization.builtins.nullable
import kotlinx.serialization.csv.Csv
import kotlinx.serialization.csv.CsvConfiguration
import kotlinx.serialization.test.assertStringFormAndRestored
import org.junit.jupiter.api.assertThrows
import kotlin.test.Test

/**
 * Test [Csv] with simple primitive records.
 */
@OptIn(ExperimentalSerializationApi::class)
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
        ListSerializer(ObjectRecord.serializer().nullable),
        Csv(
            CsvConfiguration(
                ignoreEmptyLines = false
            )
        )
    )

    @Test
    fun testInvalidObject() {
        assertThrows<IllegalArgumentException> {
            Csv.decodeFromString(
                ObjectRecord.serializer(),
                "kotlinx.serialization.csv.records.InvalidName"
            )
        }
    }
}
