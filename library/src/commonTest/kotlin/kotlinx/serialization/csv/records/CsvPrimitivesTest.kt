package kotlinx.serialization.csv.records

import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.builtins.ListSerializer
import kotlinx.serialization.builtins.serializer
import kotlinx.serialization.csv.Csv
import kotlinx.serialization.test.assertEncodeAndDecode
import kotlin.test.Test

/**
 * Test [Csv] with simple primitive records.
 */
@OptIn(ExperimentalSerializationApi::class)
class CsvPrimitivesTest {

    @Test
    fun testByte() = Csv.assertEncodeAndDecode(
        expected = "-123",
        original = -123,
        serializer = Byte.serializer(),
    )

    @Test
    fun testShort() = Csv.assertEncodeAndDecode(
        expected = "-150",
        original = -150,
        serializer = Short.serializer(),
    )

    @Test
    fun testInt() = Csv.assertEncodeAndDecode(
        expected = "-150",
        original = -150,
        serializer = Int.serializer(),
    )

    @Test
    fun testLong() = Csv.assertEncodeAndDecode(
        expected = "-150",
        original = -150,
        serializer = Long.serializer(),
    )

    @Test
    fun testFloat() = Csv.assertEncodeAndDecode(
        expected = "-150.0",
        original = -150f,
        serializer = Float.serializer(),
    )

    @Test
    fun testDouble() = Csv.assertEncodeAndDecode(
        expected = "-150.0",
        original = -150.0,
        serializer = Double.serializer(),
    )

    @Test
    fun testBooleanTrue() = Csv.assertEncodeAndDecode(
        expected = "true",
        original = true,
        serializer = Boolean.serializer(),
    )

    @Test
    fun testBooleanFalse() = Csv.assertEncodeAndDecode(
        expected = "false",
        original = false,
        serializer = Boolean.serializer(),
    )

    @Test
    fun testChar() = Csv.assertEncodeAndDecode(
        expected = "a",
        original = 'a',
        serializer = Char.serializer(),
    )

    @Test
    fun testString() = Csv.assertEncodeAndDecode(
        expected = "testing",
        original = "testing",
        serializer = String.serializer(),
    )

    @Test
    fun testUnit() = Csv.assertEncodeAndDecode(
        expected = "kotlin.Unit",
        original = Unit,
        serializer = Unit.serializer(),
    )

    @Test
    fun testIntList() = Csv.assertEncodeAndDecode(
        expected = "-150\n150\n42",
        original = listOf(-150, 150, 42),
        serializer = ListSerializer(Int.serializer()),
    )
}
