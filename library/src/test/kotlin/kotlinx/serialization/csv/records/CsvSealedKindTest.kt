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
        "kotlinx.serialization.csv.records.SealedRecord.Object,,,,",
        SealedRecord.Object,
        SealedRecord.serializer()
    )

    @Ignore("For now, serialization library does not provide the required information to write multiple columns")
    @Test
    fun testSealedNullable() = Csv.assertEncodeAndDecode(
        ",,",
        null,
        SealedRecord.serializer().nullable
    )

    @Test
    fun testSealedOtherObject() = Csv.assertEncodeAndDecode(
        "kotlinx.serialization.csv.records.SealedRecord.OtherObject,,,,",
        SealedRecord.OtherObject,
        SealedRecord.serializer()
    )

    @Test
    fun testSealedClass() = Csv.assertEncodeAndDecode(
        "kotlinx.serialization.csv.records.SealedRecord.Class,42,testing,,",
        SealedRecord.Class("testing"),
        SealedRecord.serializer()
    )

    @Test
    fun testSealedOtherClass() = Csv.assertEncodeAndDecode(
        "kotlinx.serialization.csv.records.SealedRecord.OtherClass,,,41,test-ing",
        SealedRecord.OtherClass("test-ing"),
        SealedRecord.serializer()
    )

    @Test
    fun testSealedList() = Csv.assertEncodeAndDecode(
        """
          |kotlinx.serialization.csv.records.SealedRecord.Object,,,,
          |kotlinx.serialization.csv.records.SealedRecord.OtherObject,,,,
          |kotlinx.serialization.csv.records.SealedRecord.Class,42,testing,,
        """.trimMargin().replace("\n", "\r\n"),
        listOf(
            SealedRecord.Object,
            SealedRecord.OtherObject,
            SealedRecord.Class("testing")
        ),
        ListSerializer(SealedRecord.serializer())
    )
}
