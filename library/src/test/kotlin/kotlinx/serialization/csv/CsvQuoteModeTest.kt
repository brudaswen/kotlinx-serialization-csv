package kotlinx.serialization.csv

import kotlinx.serialization.csv.CsvConfiguration.QuoteMode.*
import kotlinx.serialization.test.assertStringFormAndRestored
import kotlin.test.Test

class CsvQuoteModeTest {

    // TestString

    @Test
    fun test_None_WithString_NotRequiringQuotes() = testString(NONE, "testing", "testing")

    @Test
    fun test_None_WithString_RequiringQuotes() = testString(NONE, "a,b\r\nc\\d\"e", "a\\,b\\r\\nc\\\\d\\\"e")

    @Test
    fun test_NonNumeric_WithString_NotRequiringQuotes() = testString(ALL_NON_NUMERIC, "testing", "\"testing\"")

    @Test
    fun test_NonNumeric_WithString_RequiringQuotes() = testString(ALL_NON_NUMERIC, "a,b\r\nc\\d\"e", "\"a,b\r\nc\\d\"\"e\"")

    @Test
    fun test_AllNonNull_WithString_NotRequiringQuotes() = testString(ALL_NON_NULL, "testing", "\"testing\"")

    @Test
    fun test_AllNonNull_WithString_RequiringQuotes() = testString(ALL_NON_NULL, "a,b\r\nc\\d\"e", "\"a,b\r\nc\\d\"\"e\"")

    @Test
    fun test_Minimal_WithString_NotRequiringQuotes() = testString(MINIMAL, "testing", "testing")

    @Test
    fun test_Minimal_WithString_NotRequiringQuotes2() = testString(MINIMAL, "test\\ing", "test\\ing")

    @Test
    fun test_Minimal_WithString_RequiringQuotes() = testString(MINIMAL, "test,ing", "\"test,ing\"")

    @Test
    fun test_Minimal_WithString_RequiringQuotes2() = testString(MINIMAL, "test\ring", "\"test\ring\"")

    @Test
    fun test_Minimal_WithString_RequiringQuotes3() = testString(MINIMAL, "test\ning", "\"test\ning\"")

    @Test
    fun test_Minimal_WithString_RequiringQuotes4() = testString(MINIMAL, "test\"ing", "\"test\"\"ing\"")

    @Test
    fun test_Minimal_WithString_RequiringQuotes5() = testString(MINIMAL, "a,b\r\nc\\d\"e", "\"a,b\r\nc\\d\"\"e\"")

    @Test
    fun test_All_WithString_NotRequiringQuotes() = testString(ALL, "testing", "\"testing\"")

    @Test
    fun test_All_WithString_RequiringQuotes() = testString(ALL, "a,b\r\nc\\d\"e", "\"a,b\r\nc\\d\"\"e\"")

    // TestByte

    @Test
    fun test_None_WithByte_NotRequiringQuotes() = testByte(NONE, 44, "44")

    @Test
    fun test_None_WithByte_RequiringQuotes() = testByte(NONE, 123, "\\1\\23")

    @Test
    fun test_NonNumeric_WithByte_NotRequiringQuotes() = testByte(ALL_NON_NUMERIC, 44, "44")

    @Test
    fun test_NonNumeric_WithByte_RequiringQuotes() = testByte(ALL_NON_NUMERIC, 123, "\"123\"")

    @Test
    fun test_AllNonNull_WithByte_NotRequiringQuotes() = testByte(ALL_NON_NULL, 44, "\"44\"")

    @Test
    fun test_AllNonNull_WithByte_RequiringQuotes() = testByte(ALL_NON_NULL, 123, "\"123\"")

    @Test
    fun test_Minimal_WithByte_NotRequiringQuotes() = testByte(MINIMAL, 44, "44")

    @Test
    fun test_Minimal_WithByte_RequiringQuotes() = testByte(MINIMAL, 123, "\"123\"")

    @Test
    fun test_All_WithByte_NotRequiringQuotes() = testByte(ALL, 44, "\"44\"")

    @Test
    fun test_All_WithByte_RequiringQuotes() = testByte(ALL, 123, "\"123\"")

    // TestShort

    @Test
    fun test_None_WithShort_NotRequiringQuotes() = testShort(NONE, 44, "44")

    @Test
    fun test_None_WithShort_RequiringQuotes() = testShort(NONE, 123, "\\1\\23")

    @Test
    fun test_NonNumeric_WithShort_NotRequiringQuotes() = testShort(ALL_NON_NUMERIC, 44, "44")

    @Test
    fun test_NonNumeric_WithShort_RequiringQuotes() = testShort(ALL_NON_NUMERIC, 123, "\"123\"")

    @Test
    fun test_AllNonNull_WithShort_NotRequiringQuotes() = testShort(ALL_NON_NULL, 44, "\"44\"")

    @Test
    fun test_AllNonNull_WithShort_RequiringQuotes() = testShort(ALL_NON_NULL, 123, "\"123\"")

    @Test
    fun test_Minimal_WithShort_NotRequiringQuotes() = testShort(MINIMAL, 44, "44")

    @Test
    fun test_Minimal_WithShort_RequiringQuotes() = testShort(MINIMAL, 123, "\"123\"")

    @Test
    fun test_All_WithShort_NotRequiringQuotes() = testShort(ALL, 44, "\"44\"")

    @Test
    fun test_All_WithShort_RequiringQuotes() = testShort(ALL, 123, "\"123\"")

    // TestInt

    @Test
    fun test_None_WithInt_NotRequiringQuotes() = testInt(NONE, 44, "44")

    @Test
    fun test_None_WithInt_RequiringQuotes() = testInt(NONE, 123, "\\1\\23")

    @Test
    fun test_NonNumeric_WithInt_NotRequiringQuotes() = testInt(ALL_NON_NUMERIC, 44, "44")

    @Test
    fun test_NonNumeric_WithInt_RequiringQuotes() = testInt(ALL_NON_NUMERIC, 123, "\"123\"")

    @Test
    fun test_AllNonNull_WithInt_NotRequiringQuotes() = testInt(ALL_NON_NULL, 44, "\"44\"")

    @Test
    fun test_AllNonNull_WithInt_RequiringQuotes() = testInt(ALL_NON_NULL, 123, "\"123\"")

    @Test
    fun test_Minimal_WithInt_NotRequiringQuotes() = testInt(MINIMAL, 44, "44")

    @Test
    fun test_Minimal_WithInt_RequiringQuotes() = testInt(MINIMAL, 123, "\"123\"")

    @Test
    fun test_All_WithInt_NotRequiringQuotes() = testInt(ALL, 44, "\"44\"")

    @Test
    fun test_All_WithInt_RequiringQuotes() = testInt(ALL, 123, "\"123\"")

    // TestLong

    @Test
    fun test_None_WithLong_NotRequiringQuotes() = testLong(NONE, 44, "44")

    @Test
    fun test_None_WithLong_RequiringQuotes() = testLong(NONE, 123, "\\1\\23")

    @Test
    fun test_NonNumeric_WithLong_NotRequiringQuotes() = testLong(ALL_NON_NUMERIC, 44, "44")

    @Test
    fun test_NonNumeric_WithLong_RequiringQuotes() = testLong(ALL_NON_NUMERIC, 123, "\"123\"")

    @Test
    fun test_AllNonNull_WithLong_NotRequiringQuotes() = testLong(ALL_NON_NULL, 44, "\"44\"")

    @Test
    fun test_AllNonNull_WithLong_RequiringQuotes() = testLong(ALL_NON_NULL, 123, "\"123\"")

    @Test
    fun test_Minimal_WithLong_NotRequiringQuotes() = testLong(MINIMAL, 44, "44")

    @Test
    fun test_Minimal_WithLong_RequiringQuotes() = testLong(MINIMAL, 123, "\"123\"")

    @Test
    fun test_All_WithLong_NotRequiringQuotes() = testLong(ALL, 44, "\"44\"")

    @Test
    fun test_All_WithLong_RequiringQuotes() = testLong(ALL, 123, "\"123\"")

    // TestFloat

    @Test
    fun test_None_WithFloat_NotRequiringQuotes() = testFloat(NONE, 44f, "44.0")

    @Test
    fun test_None_WithFloat_RequiringQuotes() = testFloat(NONE, 123f, "\\1\\23.0")

    @Test
    fun test_NonNumeric_WithFloat_NotRequiringQuotes() = testFloat(ALL_NON_NUMERIC, 44f, "44.0")

    @Test
    fun test_NonNumeric_WithFloat_RequiringQuotes() = testFloat(ALL_NON_NUMERIC, 123f, "\"123.0\"")

    @Test
    fun test_AllNonNull_WithFloat_NotRequiringQuotes() = testFloat(ALL_NON_NULL, 44f, "\"44.0\"")

    @Test
    fun test_AllNonNull_WithFloat_RequiringQuotes() = testFloat(ALL_NON_NULL, 123f, "\"123.0\"")

    @Test
    fun test_Minimal_WithFloat_NotRequiringQuotes() = testFloat(MINIMAL, 44f, "44.0")

    @Test
    fun test_Minimal_WithFloat_RequiringQuotes() = testFloat(MINIMAL, 123f, "\"123.0\"")

    @Test
    fun test_All_WithFloat_NotRequiringQuotes() = testFloat(ALL, 44f, "\"44.0\"")

    @Test
    fun test_All_WithFloat_RequiringQuotes() = testFloat(ALL, 123f, "\"123.0\"")

    // TestDouble

    @Test
    fun test_None_WithDouble_NotRequiringQuotes() = testDouble(NONE, 44.0, "44.0")

    @Test
    fun test_None_WithDouble_RequiringQuotes() = testDouble(NONE, 123.0, "\\1\\23.0")

    @Test
    fun test_NonNumeric_WithDouble_NotRequiringQuotes() = testDouble(ALL_NON_NUMERIC, 44.0, "44.0")

    @Test
    fun test_NonNumeric_WithDouble_RequiringQuotes() = testDouble(ALL_NON_NUMERIC, 123.0, "\"123.0\"")

    @Test
    fun test_AllNonNull_WithDouble_NotRequiringQuotes() = testDouble(ALL_NON_NULL, 44.0, "\"44.0\"")

    @Test
    fun test_AllNonNull_WithDouble_RequiringQuotes() = testDouble(ALL_NON_NULL, 123.0, "\"123.0\"")

    @Test
    fun test_Minimal_WithDouble_NotRequiringQuotes() = testDouble(MINIMAL, 44.0, "44.0")

    @Test
    fun test_Minimal_WithDouble_RequiringQuotes() = testDouble(MINIMAL, 123.0, "\"123.0\"")

    @Test
    fun test_All_WithDouble_NotRequiringQuotes() = testDouble(ALL, 44.0, "\"44.0\"")

    @Test
    fun test_All_WithDouble_RequiringQuotes() = testDouble(ALL, 123.0, "\"123.0\"")

    // TestBoolean

    @Test
    fun test_None_WithBoolean_NotRequiringQuotes() = testBoolean(NONE, true, "true")

    @Test
    fun test_None_WithBoolean_RequiringQuotes() = testBoolean(NONE, false, "\\f\\alse")

    @Test
    fun test_NonNumeric_WithBoolean_NotRequiringQuotes() = testBoolean(ALL_NON_NUMERIC, true, "\"true\"")

    @Test
    fun test_NonNumeric_WithBoolean_RequiringQuotes() = testBoolean(ALL_NON_NUMERIC, false, "\"false\"")

    @Test
    fun test_AllNonNull_WithBoolean_NotRequiringQuotes() = testBoolean(ALL_NON_NULL, true, "\"true\"")

    @Test
    fun test_AllNonNull_WithBoolean_RequiringQuotes() = testBoolean(ALL_NON_NULL, false, "\"false\"")

    @Test
    fun test_Minimal_WithBoolean_NotRequiringQuotes() = testBoolean(MINIMAL, true, "true")

    @Test
    fun test_Minimal_WithBoolean_RequiringQuotes() = testBoolean(MINIMAL, false, "\"false\"")

    @Test
    fun test_All_WithBoolean_NotRequiringQuotes() = testBoolean(ALL, true, "\"true\"")

    @Test
    fun test_All_WithBoolean_RequiringQuotes() = testBoolean(ALL, false, "\"false\"")

    // TestNull

    @Test
    fun test_None_WithNull_NotRequiringQuotes() = testNull(NONE, "null")

    @Test
    fun test_None_WithNull_RequiringQuotes() = testNullRequiringQuotes(NONE, "n\\u\\l\\l")

    @Test
    fun test_NonNumeric_WithNull_NotRequiringQuotes() = testNull(ALL_NON_NUMERIC, "\"null\"")

    @Test
    fun test_NonNumeric_WithNull_RequiringQuotes() = testNullRequiringQuotes(ALL_NON_NUMERIC, "\"null\"")

    @Test
    fun test_AllNonNull_WithNull_NotRequiringQuotes() = testNull(ALL_NON_NULL, "null")

    @Test
    fun test_AllNonNull_WithNull_RequiringQuotes() = testNullRequiringQuotes(ALL_NON_NULL, "\"null\"")

    @Test
    fun test_Minimal_WithNull_NotRequiringQuotes() = testNull(MINIMAL, "null")

    @Test
    fun test_Minimal_WithNull_RequiringQuotes() = testNullRequiringQuotes(MINIMAL, "\"null\"")

    @Test
    fun test_All_WithNull_NotRequiringQuotes() = testNull(ALL, "\"null\"")

    @Test
    fun test_All_WithNull_RequiringQuotes() = testNullRequiringQuotes(ALL, "\"null\"")

    // TestUnit

    @Test
    fun test_None_WithUnit_NotRequiringQuotes() = testUnit(NONE, "unit")

    @Test
    fun test_None_WithUnit_RequiringQuotes() = testUnitRequiringQuotes(NONE, "\\un\\it")

    @Test
    fun test_NonNumeric_WithUnit_NotRequiringQuotes() = testUnit(ALL_NON_NUMERIC, "\"unit\"")

    @Test
    fun test_NonNumeric_WithUnit_RequiringQuotes() = testUnitRequiringQuotes(ALL_NON_NUMERIC, "\"unit\"")

    @Test
    fun test_AllNonNull_WithUnit_NotRequiringQuotes() = testUnit(ALL_NON_NULL, "\"unit\"")

    @Test
    fun test_AllNonNull_WithUnit_RequiringQuotes() = testUnitRequiringQuotes(ALL_NON_NULL, "\"unit\"")

    @Test
    fun test_Minimal_WithUnit_NotRequiringQuotes() = testUnit(MINIMAL, "unit")

    @Test
    fun test_Minimal_WithUnit_RequiringQuotes() = testUnitRequiringQuotes(MINIMAL, "\"unit\"")

    @Test
    fun test_All_WithUnit_NotRequiringQuotes() = testUnit(ALL, "\"unit\"")

    @Test
    fun test_All_WithUnit_RequiringQuotes() = testUnitRequiringQuotes(ALL, "\"unit\"")


    private fun testString(quoteMode: CsvConfiguration.QuoteMode, value: String, expected: String) = assertStringFormAndRestored(
            expected,
            StringRecord(value),
            StringRecord.serializer(),
            Csv(CsvConfiguration(quoteMode = quoteMode))
    )

    private fun testByte(quoteMode: CsvConfiguration.QuoteMode, value: Byte, expected: String) = assertStringFormAndRestored(
            expected,
            ByteRecord(value),
            ByteRecord.serializer(),
            Csv(CsvConfiguration(quoteMode = quoteMode, delimiter = '1', recordSeparator = "2"))
    )

    private fun testShort(quoteMode: CsvConfiguration.QuoteMode, value: Short, expected: String) = assertStringFormAndRestored(
            expected,
            ShortRecord(value),
            ShortRecord.serializer(),
            Csv(CsvConfiguration(quoteMode = quoteMode, delimiter = '1', recordSeparator = "2"))
    )

    private fun testInt(quoteMode: CsvConfiguration.QuoteMode, value: Int, expected: String) = assertStringFormAndRestored(
            expected,
            IntRecord(value),
            IntRecord.serializer(),
            Csv(CsvConfiguration(quoteMode = quoteMode, delimiter = '1', recordSeparator = "2"))
    )

    private fun testLong(quoteMode: CsvConfiguration.QuoteMode, value: Long, expected: String) = assertStringFormAndRestored(
            expected,
            LongRecord(value),
            LongRecord.serializer(),
            Csv(CsvConfiguration(quoteMode = quoteMode, delimiter = '1', recordSeparator = "2"))
    )

    private fun testFloat(quoteMode: CsvConfiguration.QuoteMode, value: Float, expected: String) = assertStringFormAndRestored(
            expected,
            FloatRecord(value),
            FloatRecord.serializer(),
            Csv(CsvConfiguration(quoteMode = quoteMode, delimiter = '1', recordSeparator = "2"))
    )

    private fun testDouble(quoteMode: CsvConfiguration.QuoteMode, value: Double, expected: String) = assertStringFormAndRestored(
            expected,
            DoubleRecord(value),
            DoubleRecord.serializer(),
            Csv(CsvConfiguration(quoteMode = quoteMode, delimiter = '1', recordSeparator = "2"))
    )

    private fun testBoolean(quoteMode: CsvConfiguration.QuoteMode, value: Boolean, expected: String) = assertStringFormAndRestored(
            expected,
            BooleanRecord(value),
            BooleanRecord.serializer(),
            Csv(CsvConfiguration(quoteMode = quoteMode, delimiter = 'f', recordSeparator = "a"))
    )

    private fun testNull(quoteMode: CsvConfiguration.QuoteMode, expected: String) = assertStringFormAndRestored(
            expected,
            NullRecord(null),
            NullRecord.serializer(),
            Csv(CsvConfiguration(quoteMode = quoteMode, nullString = "null"))
    )

    private fun testNullRequiringQuotes(quoteMode: CsvConfiguration.QuoteMode, expected: String) = assertStringFormAndRestored(
            expected,
            NullRecord(null),
            NullRecord.serializer(),
            Csv(CsvConfiguration(quoteMode = quoteMode, nullString = "null", delimiter = 'u', recordSeparator = "l"))
    )

    private fun testUnit(quoteMode: CsvConfiguration.QuoteMode, expected: String) = assertStringFormAndRestored(
            expected,
            UnitRecord(Unit),
            UnitRecord.serializer(),
            Csv(CsvConfiguration(quoteMode = quoteMode, unitString = "unit"))
    )

    private fun testUnitRequiringQuotes(quoteMode: CsvConfiguration.QuoteMode, expected: String) = assertStringFormAndRestored(
            expected,
            UnitRecord(Unit),
            UnitRecord.serializer(),
            Csv(CsvConfiguration(quoteMode = quoteMode, unitString = "unit", delimiter = 'u', recordSeparator = "i"))
    )
}