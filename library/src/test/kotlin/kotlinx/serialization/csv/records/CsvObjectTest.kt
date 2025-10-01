package kotlinx.serialization.csv.records

import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.builtins.ListSerializer
import kotlinx.serialization.builtins.nullable
import kotlinx.serialization.csv.Csv
import kotlinx.serialization.test.assertEncodeAndDecode
import kotlin.test.Test
import kotlin.test.assertFailsWith

/**
 * Test [Csv] with simple primitive records.
 */
@OptIn(ExperimentalSerializationApi::class)
class CsvObjectTest {

    @Test
    fun testObject() = Csv.assertEncodeAndDecode(
        "kotlinx.serialization.csv.records.ObjectRecord",
        ObjectRecord,
        ObjectRecord.serializer()
    )

    @Test
    fun testNullableObject() = Csv.assertEncodeAndDecode(
        "",
        null,
        ObjectRecord.serializer().nullable
    )

    @Test
    fun testObjectList() = Csv {
        ignoreEmptyLines = false
    }.assertEncodeAndDecode(
        "kotlinx.serialization.csv.records.ObjectRecord\n\nkotlinx.serialization.csv.records.ObjectRecord",
        listOf(
            ObjectRecord,
            null,
            ObjectRecord
        ),
        ListSerializer(ObjectRecord.serializer().nullable)
    )

    @Test
    fun testInvalidObject() {
        assertFailsWith<IllegalArgumentException> {
            Csv.decodeFromString(
                ObjectRecord.serializer(),
                "kotlinx.serialization.csv.records.InvalidName"
            )
        }
    }
}
