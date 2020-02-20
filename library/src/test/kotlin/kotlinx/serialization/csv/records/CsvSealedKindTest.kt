package kotlinx.serialization.csv.records

import kotlinx.serialization.csv.Csv
import kotlinx.serialization.internal.nullable
import kotlinx.serialization.list
import kotlinx.serialization.test.assertStringFormAndRestored
import kotlin.test.Ignore
import kotlin.test.Test

/**
 * Test [Csv] with simple primitive records.
 */
class CsvSealedKindTest {

    @Test
    fun testSealedObject() = assertStringFormAndRestored(
        "kotlinx.serialization.csv.records.SealedRecord.Object,kotlinx.serialization.csv.records.SealedRecord.Object,,,,,",
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
        "kotlinx.serialization.csv.records.SealedRecord.OtherObject,,kotlinx.serialization.csv.records.SealedRecord.OtherObject,,,,",
        SealedRecord.OtherObject,
        SealedRecord.serializer(),
        Csv
    )

    @Test
    fun testSealedClass() = assertStringFormAndRestored(
        "kotlinx.serialization.csv.records.SealedRecord.Class,,,42,testing,,",
        SealedRecord.Class("testing"),
        SealedRecord.serializer(),
        Csv
    )

    @Test
    fun testSealedOtherClass() = assertStringFormAndRestored(
        "kotlinx.serialization.csv.records.SealedRecord.OtherClass,,,,,41,test-ing",
        SealedRecord.OtherClass("test-ing"),
        SealedRecord.serializer(),
        Csv
    )

    @Test
    fun testSealedList() = assertStringFormAndRestored(
        "kotlinx.serialization.csv.records.SealedRecord.Object,kotlinx.serialization.csv.records.SealedRecord.Object,,,,,\r\nkotlinx.serialization.csv.records.SealedRecord.OtherObject,,kotlinx.serialization.csv.records.SealedRecord.OtherObject,,,,\r\nkotlinx.serialization.csv.records.SealedRecord.Class,,,42,testing,,",
        listOf(
            SealedRecord.Object,
            SealedRecord.OtherObject,
            SealedRecord.Class("testing")
        ),
        SealedRecord.serializer().list,
        Csv
    )
}
