package kotlinx.serialization.csv.records

import kotlinx.serialization.csv.Csv
import kotlinx.serialization.csv.CsvConfiguration
import kotlinx.serialization.internal.UnitSerializer
import kotlinx.serialization.internal.nullable
import kotlinx.serialization.list
import kotlinx.serialization.serializer
import kotlinx.serialization.test.assertStringFormAndRestored
import kotlin.test.Test

/**
 * Test [Csv] with simple nullable primitive records.
 */
class CsvNullablePrimitivesTest {

    @Test
    fun testByte() = assertStringFormAndRestored(
        "",
        null,
        Byte.serializer().nullable,
        Csv
    )

    @Test
    fun testShort() = assertStringFormAndRestored(
        "",
        null,
        Short.serializer().nullable,
        Csv
    )

    @Test
    fun testInt() = assertStringFormAndRestored(
        "",
        null,
        Int.serializer().nullable,
        Csv
    )

    @Test
    fun testLong() = assertStringFormAndRestored(
        "",
        null,
        Long.serializer().nullable,
        Csv
    )

    @Test
    fun testFloat() = assertStringFormAndRestored(
        "",
        null,
        Float.serializer().nullable,
        Csv
    )

    @Test
    fun testDouble() = assertStringFormAndRestored(
        "",
        null,
        Double.serializer().nullable,
        Csv
    )

    @Test
    fun testBooleanTrue() = assertStringFormAndRestored(
        "",
        null,
        Boolean.serializer().nullable,
        Csv
    )

    @Test
    fun testBooleanFalse() = assertStringFormAndRestored(
        "",
        null,
        Boolean.serializer().nullable,
        Csv
    )

    @Test
    fun testChar() = assertStringFormAndRestored(
        "",
        null,
        Char.serializer().nullable,
        Csv
    )

    @Test
    fun testString() = assertStringFormAndRestored(
        "",
        null,
        String.serializer().nullable,
        Csv
    )

    @Test
    fun testUnit() = assertStringFormAndRestored(
        "",
        null,
        UnitSerializer.nullable,
        Csv
    )

    @Test
    fun testIntList() = assertStringFormAndRestored(
        "-150\r\n\r\n42",
        listOf(-150, null, 42),
        Int.serializer().nullable.list,
        Csv(
            CsvConfiguration(
                ignoreEmptyLines = false
            )
        )
    )
}
