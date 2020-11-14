package kotlinx.serialization.csv.config

import org.junit.jupiter.api.assertThrows
import kotlin.test.Test

class CsvBuilderTest {

    @Test
    fun `should fail if delimiter equals quoteChar`() {
        assertThrows<IllegalArgumentException> {
            CsvBuilder().apply {
                delimiter = '!'
                quoteChar = '!'
            }.build()
        }
    }

    @Test
    fun `should fail if delimiter equals escapeChar`() {
        assertThrows<IllegalArgumentException> {
            CsvBuilder().apply {
                delimiter = '!'
                escapeChar = '!'
            }.build()
        }
    }

    @Test
    fun `should fail if escapeChar not set`() {
        assertThrows<IllegalArgumentException> {
            CsvBuilder().apply {
                quoteMode = QuoteMode.NONE
                escapeChar = null
            }.build()
        }
    }

    @Test
    fun `should fail if recordSeparator is empty`() {
        assertThrows<IllegalArgumentException> {
            CsvBuilder().apply {
                recordSeparator = ""
            }.build()
        }
    }

    @Test
    fun `should fail if recordSeparator is too long`() {
        assertThrows<IllegalArgumentException> {
            CsvBuilder().apply {
                recordSeparator = "\r\n\n"
            }.build()
        }
    }

    @Test
    fun `should fail if recordSeparator is delimiter`() {
        assertThrows<IllegalArgumentException> {
            CsvBuilder().apply {
                delimiter = ','
                recordSeparator = ","
            }.build()
        }
    }

    @Test
    fun `should fail if recordSeparator is quoteChar`() {
        assertThrows<IllegalArgumentException> {
            CsvBuilder().apply {
                quoteChar = '"'
                recordSeparator = "\""
            }.build()
        }
    }

    @Test
    fun `should fail if recordSeparator is escapeChar`() {
        assertThrows<IllegalArgumentException> {
            CsvBuilder().apply {
                escapeChar = '\\'
                recordSeparator = "\\"
            }.build()
        }
    }
}