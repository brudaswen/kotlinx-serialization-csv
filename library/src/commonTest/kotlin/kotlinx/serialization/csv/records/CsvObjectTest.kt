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
        expected = "kotlinx.serialization.csv.records.ObjectRecord",
        original = ObjectRecord,
        serializer = ObjectRecord.serializer(),
    )

    @Test
    fun testNullableObject() = Csv.assertEncodeAndDecode(
        expected = "",
        original = null,
        serializer = ObjectRecord.serializer().nullable,
    )

    @Test
    fun testObjectList() = Csv {
        ignoreEmptyLines = false
    }.assertEncodeAndDecode(
        expected = "kotlinx.serialization.csv.records.ObjectRecord\n\nkotlinx.serialization.csv.records.ObjectRecord",
        original = listOf(
            ObjectRecord,
            null,
            ObjectRecord,
        ),
        serializer = ListSerializer(ObjectRecord.serializer().nullable),
    )

    @Test
    fun testInvalidObject() {
        assertFailsWith<IllegalArgumentException> {
            Csv.decodeFromString(
                deserializer = ObjectRecord.serializer(),
                string = "kotlinx.serialization.csv.records.InvalidName",
            )
        }
    }
}
