package kotlinx.serialization.csv.records

import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.builtins.ListSerializer
import kotlinx.serialization.builtins.nullable
import kotlinx.serialization.builtins.serializer
import kotlinx.serialization.csv.Csv
import kotlinx.serialization.test.assertEncodeAndDecode
import kotlin.test.Test

/**
 * Test [Csv] with simple nullable primitive records.
 */
@OptIn(ExperimentalSerializationApi::class)
class CsvNullablePrimitivesTest {

    @Test
    fun testByte() = Csv.assertEncodeAndDecode(
        expected = "",
        original = null,
        serializer = Byte.serializer().nullable,
    )

    @Test
    fun testShort() = Csv.assertEncodeAndDecode(
        expected = "",
        original = null,
        serializer = Short.serializer().nullable,
    )

    @Test
    fun testInt() = Csv.assertEncodeAndDecode(
        expected = "",
        original = null,
        serializer = Int.serializer().nullable,
    )

    @Test
    fun testLong() = Csv.assertEncodeAndDecode(
        expected = "",
        original = null,
        serializer = Long.serializer().nullable,
    )

    @Test
    fun testFloat() = Csv.assertEncodeAndDecode(
        expected = "",
        original = null,
        serializer = Float.serializer().nullable,
    )

    @Test
    fun testDouble() = Csv.assertEncodeAndDecode(
        expected = "",
        original = null,
        serializer = Double.serializer().nullable,
    )

    @Test
    fun testBooleanTrue() = Csv.assertEncodeAndDecode(
        expected = "",
        original = null,
        serializer = Boolean.serializer().nullable,
    )

    @Test
    fun testBooleanFalse() = Csv.assertEncodeAndDecode(
        expected = "",
        original = null,
        serializer = Boolean.serializer().nullable,
    )

    @Test
    fun testChar() = Csv.assertEncodeAndDecode(
        expected = "",
        original = null,
        serializer = Char.serializer().nullable,
    )

    @Test
    fun testString() = Csv.assertEncodeAndDecode(
        expected = "",
        original = null,
        serializer = String.serializer().nullable,
    )

    @Test
    fun testUnit() = Csv.assertEncodeAndDecode(
        expected = "",
        original = null,
        serializer = Unit.serializer().nullable,
    )

    @Test
    fun testIntList() = Csv {
        ignoreEmptyLines = false
    }.assertEncodeAndDecode(
        expected = "-150\n\n42",
        original = listOf(-150, null, 42),
        serializer = ListSerializer(Int.serializer().nullable),
    )
}
