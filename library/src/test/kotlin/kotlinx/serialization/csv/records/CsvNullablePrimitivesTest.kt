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
        "",
        null,
        Byte.serializer().nullable
    )

    @Test
    fun testShort() = Csv.assertEncodeAndDecode(
        "",
        null,
        Short.serializer().nullable
    )

    @Test
    fun testInt() = Csv.assertEncodeAndDecode(
        "",
        null,
        Int.serializer().nullable
    )

    @Test
    fun testLong() = Csv.assertEncodeAndDecode(
        "",
        null,
        Long.serializer().nullable
    )

    @Test
    fun testFloat() = Csv.assertEncodeAndDecode(
        "",
        null,
        Float.serializer().nullable
    )

    @Test
    fun testDouble() = Csv.assertEncodeAndDecode(
        "",
        null,
        Double.serializer().nullable
    )

    @Test
    fun testBooleanTrue() = Csv.assertEncodeAndDecode(
        "",
        null,
        Boolean.serializer().nullable
    )

    @Test
    fun testBooleanFalse() = Csv.assertEncodeAndDecode(
        "",
        null,
        Boolean.serializer().nullable
    )

    @Test
    fun testChar() = Csv.assertEncodeAndDecode(
        "",
        null,
        Char.serializer().nullable
    )

    @Test
    fun testString() = Csv.assertEncodeAndDecode(
        "",
        null,
        String.serializer().nullable
    )

    @Test
    fun testUnit() = Csv.assertEncodeAndDecode(
        "",
        null,
        Unit.serializer().nullable
    )

    @Test
    fun testIntList() = Csv {
        ignoreEmptyLines = false
    }.assertEncodeAndDecode(
        "-150\n\n42",
        listOf(-150, null, 42),
        ListSerializer(Int.serializer().nullable)
    )
}
