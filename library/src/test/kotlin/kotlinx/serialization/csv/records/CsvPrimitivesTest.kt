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
        "-123",
        -123,
        Byte.serializer()
    )

    @Test
    fun testShort() = Csv.assertEncodeAndDecode(
        "-150",
        -150,
        Short.serializer()
    )

    @Test
    fun testInt() = Csv.assertEncodeAndDecode(
        "-150",
        -150,
        Int.serializer()
    )

    @Test
    fun testLong() = Csv.assertEncodeAndDecode(
        "-150",
        -150,
        Long.serializer()
    )

    @Test
    fun testFloat() = Csv.assertEncodeAndDecode(
        "-150.0",
        -150f,
        Float.serializer()
    )

    @Test
    fun testDouble() = Csv.assertEncodeAndDecode(
        "-150.0",
        -150.0,
        Double.serializer()
    )

    @Test
    fun testBooleanTrue() = Csv.assertEncodeAndDecode(
        "true",
        true,
        Boolean.serializer()
    )

    @Test
    fun testBooleanFalse() = Csv.assertEncodeAndDecode(
        "false",
        false,
        Boolean.serializer()
    )

    @Test
    fun testChar() = Csv.assertEncodeAndDecode(
        "a",
        'a',
        Char.serializer()
    )

    @Test
    fun testString() = Csv.assertEncodeAndDecode(
        "testing",
        "testing",
        String.serializer()
    )

    @Test
    fun testUnit() = Csv.assertEncodeAndDecode(
        "kotlin.Unit",
        Unit,
        Unit.serializer()
    )

    @Test
    fun testIntList() = Csv.assertEncodeAndDecode(
        "-150\n150\n42",
        listOf(-150, 150, 42),
        ListSerializer(Int.serializer())
    )
}
