package kotlinx.serialization.csv

import kotlinx.serialization.internal.nullable
import kotlinx.serialization.list
import kotlinx.serialization.test.assertStringFormAndRestored
import kotlin.test.Ignore
import kotlin.test.Test

/**
 * Test [Csv] ([CsvEncoder] and [CsvDecoder]) with simple primitive records.
 */
class CsvSealedKindTest {

    @Test
    fun testSealedObject() = assertStringFormAndRestored(
            "kotlinx.serialization.csv.SealedRecord.Object,kotlinx.serialization.csv.SealedRecord.Object,,,,,",
            SealedRecord.Object,
            SealedRecord.serializer(),
            Csv
    )

    @Ignore("For now, serialization library does not provide the required information to write multiple columns")
    @Test
    fun testSealedNullable() = assertStringFormAndRestored(
            ",,",
            null,
            SealedRecord.serializer().nullable,
            Csv
    )

    @Test
    fun testSealedOtherObject() = assertStringFormAndRestored(
            "kotlinx.serialization.csv.SealedRecord.OtherObject,,kotlinx.serialization.csv.SealedRecord.OtherObject,,,,",
            SealedRecord.OtherObject,
            SealedRecord.serializer(),
            Csv
    )

    @Test
    fun testSealedClass() = assertStringFormAndRestored(
            "kotlinx.serialization.csv.SealedRecord.Class,,,42,testing,,",
            SealedRecord.Class("testing"),
            SealedRecord.serializer(),
            Csv
    )

    @Test
    fun testSealedOtherClass() = assertStringFormAndRestored(
            "kotlinx.serialization.csv.SealedRecord.OtherClass,,,,,41,test-ing",
            SealedRecord.OtherClass("test-ing"),
            SealedRecord.serializer(),
            Csv
    )

    @Test
    fun testSealedList() = assertStringFormAndRestored(
            "kotlinx.serialization.csv.SealedRecord.Object,kotlinx.serialization.csv.SealedRecord.Object,,,,,\r\nkotlinx.serialization.csv.SealedRecord.OtherObject,,kotlinx.serialization.csv.SealedRecord.OtherObject,,,,\r\nkotlinx.serialization.csv.SealedRecord.Class,,,42,testing,,",
            listOf(
                    SealedRecord.Object,
                    SealedRecord.OtherObject,
                    SealedRecord.Class("testing")
            ),
            SealedRecord.serializer().list,
            Csv
    )
}
