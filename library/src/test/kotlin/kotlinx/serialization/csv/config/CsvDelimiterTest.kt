package kotlinx.serialization.csv.config

import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.csv.Csv
import kotlinx.serialization.csv.records.ComplexRecord
import kotlinx.serialization.csv.records.Enum
import kotlinx.serialization.test.assertEncodeAndDecode
import kotlin.test.Test

/**
 * Test [Csv] with different [CsvConfig.delimiter]s.
 */
@OptIn(ExperimentalSerializationApi::class)
class CsvDelimiterTest {

    @Test
    fun testDefault() = Csv.assertEncodeAndDecode(
        "-150,-1,42,9223372036854775807,-2.0,24.24,true,testing,,kotlin.Unit,FIRST",
        ComplexRecord(
            -150,
            -1,
            42,
            Long.MAX_VALUE,
            -2f,
            24.24,
            true,
            "testing",
            null,
            Unit,
            Enum.FIRST
        ),
        ComplexRecord.serializer()
    )

    @Test
    fun testComma() = Csv {
        delimiter = ','
    }.assertEncodeAndDecode(
        "-150,-1,42,9223372036854775807,-2.0,24.24,true,testing,,kotlin.Unit,FIRST",
        ComplexRecord(
            -150,
            -1,
            42,
            Long.MAX_VALUE,
            -2f,
            24.24,
            true,
            "testing",
            null,
            Unit,
            Enum.FIRST
        ),
        ComplexRecord.serializer()
    )

    @Test
    fun testColon() = Csv {
        delimiter = ';'
    }.assertEncodeAndDecode(
        "-150;-1;42;9223372036854775807;-2.0;24.24;true;testing;;kotlin.Unit;FIRST",
        ComplexRecord(
            -150,
            -1,
            42,
            Long.MAX_VALUE,
            -2f,
            24.24,
            true,
            "testing",
            null,
            Unit,
            Enum.FIRST
        ),
        ComplexRecord.serializer()
    )

    @Test
    fun testDot() = Csv {
        delimiter = '.'
    }.assertEncodeAndDecode(
        "-150.-1.42.9223372036854775807.\"-2.0\".\"24.24\".true.testing..\"kotlin.Unit\".FIRST",
        ComplexRecord(
            -150,
            -1,
            42,
            Long.MAX_VALUE,
            -2f,
            24.24,
            true,
            "testing",
            null,
            Unit,
            Enum.FIRST
        ),
        ComplexRecord.serializer()
    )

    @Test
    fun testTab() = Csv {
        delimiter = '\t'
    }.assertEncodeAndDecode(
        "-150\t-1\t42\t9223372036854775807\t-2.0\t24.24\ttrue\ttesting\t\tkotlin.Unit\tFIRST",
        ComplexRecord(
            -150,
            -1,
            42,
            Long.MAX_VALUE,
            -2f,
            24.24,
            true,
            "testing",
            null,
            Unit,
            Enum.FIRST
        ),
        ComplexRecord.serializer()
    )
}
