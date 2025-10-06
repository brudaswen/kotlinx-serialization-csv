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
        expected = "-150,-1,42,9223372036854775807,-2.0,24.24,true,testing,,kotlin.Unit,FIRST",
        original = ComplexRecord(
            a = -150,
            b = -1,
            c = 42,
            d = Long.MAX_VALUE,
            e = -2f,
            f = 24.24,
            g = true,
            h = "testing",
            i = null,
            j = Unit,
            k = Enum.FIRST,
        ),
        serializer = ComplexRecord.serializer(),
    )

    @Test
    fun testComma() = Csv {
        delimiter = ','
    }.assertEncodeAndDecode(
        expected = "-150,-1,42,9223372036854775807,-2.0,24.24,true,testing,,kotlin.Unit,FIRST",
        original = ComplexRecord(
            a = -150,
            b = -1,
            c = 42,
            d = Long.MAX_VALUE,
            e = -2f,
            f = 24.24,
            g = true,
            h = "testing",
            i = null,
            j = Unit,
            k = Enum.FIRST,
        ),
        serializer = ComplexRecord.serializer(),
    )

    @Test
    fun testColon() = Csv {
        delimiter = ';'
    }.assertEncodeAndDecode(
        expected = "-150;-1;42;9223372036854775807;-2.0;24.24;true;testing;;kotlin.Unit;FIRST",
        original = ComplexRecord(
            a = -150,
            b = -1,
            c = 42,
            d = Long.MAX_VALUE,
            e = -2f,
            f = 24.24,
            g = true,
            h = "testing",
            i = null,
            j = Unit,
            k = Enum.FIRST,
        ),
        serializer = ComplexRecord.serializer(),
    )

    @Test
    fun testDot() = Csv {
        delimiter = '.'
    }.assertEncodeAndDecode(
        expected = "-150.-1.42.9223372036854775807.\"-2.0\".\"24.24\".true.testing..\"kotlin.Unit\".FIRST",
        original = ComplexRecord(
            a = -150,
            b = -1,
            c = 42,
            d = Long.MAX_VALUE,
            e = -2f,
            f = 24.24,
            g = true,
            h = "testing",
            i = null,
            j = Unit,
            k = Enum.FIRST,
        ),
        serializer = ComplexRecord.serializer(),
    )

    @Test
    fun testTab() = Csv {
        delimiter = '\t'
    }.assertEncodeAndDecode(
        expected = "-150\t-1\t42\t9223372036854775807\t-2.0\t24.24\ttrue\ttesting\t\tkotlin.Unit\tFIRST",
        original = ComplexRecord(
            a = -150,
            b = -1,
            c = 42,
            d = Long.MAX_VALUE,
            e = -2f,
            f = 24.24,
            g = true,
            h = "testing",
            i = null,
            j = Unit,
            k = Enum.FIRST,
        ),
        serializer = ComplexRecord.serializer(),
    )
}
