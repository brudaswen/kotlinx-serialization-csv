package kotlinx.serialization.csv.records

import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.builtins.ListSerializer
import kotlinx.serialization.builtins.serializer
import kotlinx.serialization.csv.Csv
import kotlinx.serialization.test.assertStringFormAndRestored
import kotlin.test.Test

/**
 * Test [Csv] with simple primitive records.
 */
@OptIn(ExperimentalSerializationApi::class)
class CsvPrimitivesTest {

    @Test
    fun testByte() = assertStringFormAndRestored(
        "-123",
        -123,
        Byte.serializer(),
        Csv
    )

    @Test
    fun testShort() = assertStringFormAndRestored(
        "-150",
        -150,
        Short.serializer(),
        Csv
    )

    @Test
    fun testInt() = assertStringFormAndRestored(
        "-150",
        -150,
        Int.serializer(),
        Csv
    )

    @Test
    fun testLong() = assertStringFormAndRestored(
        "-150",
        -150,
        Long.serializer(),
        Csv
    )

    @Test
    fun testFloat() = assertStringFormAndRestored(
        "-150.0",
        -150f,
        Float.serializer(),
        Csv
    )

    @Test
    fun testDouble() = assertStringFormAndRestored(
        "-150.0",
        -150.0,
        Double.serializer(),
        Csv
    )

    @Test
    fun testBooleanTrue() = assertStringFormAndRestored(
        "true",
        true,
        Boolean.serializer(),
        Csv
    )

    @Test
    fun testBooleanFalse() = assertStringFormAndRestored(
        "false",
        false,
        Boolean.serializer(),
        Csv
    )

    @Test
    fun testChar() = assertStringFormAndRestored(
        "a",
        'a',
        Char.serializer(),
        Csv
    )

    @Test
    fun testString() = assertStringFormAndRestored(
        "testing",
        "testing",
        String.serializer(),
        Csv
    )

    @Test
    fun testUnit() = assertStringFormAndRestored(
        "kotlin.Unit",
        Unit,
        Unit.serializer(),
        Csv
    )

    @Test
    fun testIntList() = assertStringFormAndRestored(
        "-150\r\n150\r\n42",
        listOf(-150, 150, 42),
        ListSerializer(Int.serializer()),
        Csv
    )
}
