package kotlinx.serialization.csv.config

import kotlin.test.Test
import kotlin.test.assertFailsWith

class CsvBuilderTest {

    @Test
    fun `should fail if delimiter equals quoteChar`() {
        assertFailsWith<IllegalArgumentException> {
            CsvBuilder().apply {
                delimiter = '!'
                quoteChar = '!'
            }.build()
        }
    }

    @Test
    fun `should fail if delimiter equals escapeChar`() {
        assertFailsWith<IllegalArgumentException> {
            CsvBuilder().apply {
                delimiter = '!'
                escapeChar = '!'
            }.build()
        }
    }

    @Test
    fun `should fail if escapeChar not set`() {
        assertFailsWith<IllegalArgumentException> {
            CsvBuilder().apply {
                quoteMode = QuoteMode.NONE
                escapeChar = null
            }.build()
        }
    }

    @Test
    fun `should fail if recordSeparator is empty`() {
        assertFailsWith<IllegalArgumentException> {
            CsvBuilder().apply {
                recordSeparator = ""
            }.build()
        }
    }

    @Test
    fun `should fail if recordSeparator is too long`() {
        assertFailsWith<IllegalArgumentException> {
            CsvBuilder().apply {
                recordSeparator = "\r\n\n"
            }.build()
        }
    }

    @Test
    fun `should fail if recordSeparator is delimiter`() {
        assertFailsWith<IllegalArgumentException> {
            CsvBuilder().apply {
                delimiter = ','
                recordSeparator = ","
            }.build()
        }
    }

    @Test
    fun `should fail if recordSeparator is quoteChar`() {
        assertFailsWith<IllegalArgumentException> {
            CsvBuilder().apply {
                quoteChar = '"'
                recordSeparator = "\""
            }.build()
        }
    }

    @Test
    fun `should fail if recordSeparator is escapeChar`() {
        assertFailsWith<IllegalArgumentException> {
            CsvBuilder().apply {
                escapeChar = '\\'
                recordSeparator = "\\"
            }.build()
        }
    }
}