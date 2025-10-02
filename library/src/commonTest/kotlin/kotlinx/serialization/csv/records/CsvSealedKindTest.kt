package kotlinx.serialization.csv.records

import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.builtins.ListSerializer
import kotlinx.serialization.builtins.nullable
import kotlinx.serialization.csv.Csv
import kotlinx.serialization.test.assertEncodeAndDecode
import kotlin.test.Ignore
import kotlin.test.Test

/**
 * Test [Csv] with simple primitive records.
 */
@OptIn(ExperimentalSerializationApi::class)
class CsvSealedKindTest {

    @Test
    fun testSealedObject() = Csv.assertEncodeAndDecode(
        expected = "kotlinx.serialization.csv.records.SealedRecord.Object,,,,",
        original = SealedRecord.Object,
        serializer = SealedRecord.serializer(),
    )

    // For now, serialization library does not provide the required information to write multiple columns
    @Ignore
    @Test
    fun testSealedNullable() = Csv.assertEncodeAndDecode(
        expected = ",,",
        original = null,
        serializer = SealedRecord.serializer().nullable,
    )

    @Test
    fun testSealedOtherObject() = Csv.assertEncodeAndDecode(
        expected = "kotlinx.serialization.csv.records.SealedRecord.OtherObject,,,,",
        original = SealedRecord.OtherObject,
        serializer = SealedRecord.serializer(),
    )

    @Test
    fun testSealedClass() = Csv.assertEncodeAndDecode(
        expected = "kotlinx.serialization.csv.records.SealedRecord.Class,42,testing,,",
        original = SealedRecord.Class("testing"),
        serializer = SealedRecord.serializer(),
    )

    @Test
    fun testSealedOtherClass() = Csv.assertEncodeAndDecode(
        expected = "kotlinx.serialization.csv.records.SealedRecord.OtherClass,,,41,test-ing",
        original = SealedRecord.OtherClass("test-ing"),
        serializer = SealedRecord.serializer(),
    )

    @Test
    fun testSealedList() = Csv.assertEncodeAndDecode(
        expected = """
          |kotlinx.serialization.csv.records.SealedRecord.Object,,,,
          |kotlinx.serialization.csv.records.SealedRecord.OtherObject,,,,
          |kotlinx.serialization.csv.records.SealedRecord.Class,42,testing,,
        """.trimMargin(),
        original = listOf(
            SealedRecord.Object,
            SealedRecord.OtherObject,
            SealedRecord.Class("testing"),
        ),
        serializer = ListSerializer(SealedRecord.serializer()),
    )
}
