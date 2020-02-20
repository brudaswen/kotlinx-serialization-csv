package kotlinx.serialization.csv.config

import kotlinx.serialization.csv.CsvConfiguration
import org.junit.jupiter.api.assertThrows
import kotlin.test.Test

class CsvConfigurationTest {

    @Test
    fun `should fail if delimiter equals quoteChar`() {
        assertThrows<IllegalArgumentException> {
            CsvConfiguration(delimiter = '!', quoteChar = '!')
        }
    }

    @Test
    fun `should fail if delimiter equals escapeChar`() {
        assertThrows<IllegalArgumentException> {
            CsvConfiguration(delimiter = '!', escapeChar = '!')
        }
    }

    @Test
    fun `should fail if nullString equals unitString`() {
        assertThrows<IllegalArgumentException> {
            CsvConfiguration(nullString = "", unitString = "")
        }
    }

    @Test
    fun `should fail if escapeChar not set`() {
        assertThrows<IllegalArgumentException> {
            CsvConfiguration(quoteMode = CsvConfiguration.QuoteMode.NONE, escapeChar = null)
        }
    }

    @Test
    fun `should fail if recordSeparator is empty`() {
        assertThrows<IllegalArgumentException> {
            CsvConfiguration(recordSeparator = "")
        }
    }

    @Test
    fun `should fail if recordSeparator is too long`() {
        assertThrows<IllegalArgumentException> {
            CsvConfiguration(recordSeparator = "\r\n\n")
        }
    }

    @Test
    fun `should fail if recordSeparator is delimiter`() {
        assertThrows<IllegalArgumentException> {
            CsvConfiguration(delimiter = ',', recordSeparator = ",")
        }
    }

    @Test
    fun `should fail if recordSeparator is quoteChar`() {
        assertThrows<IllegalArgumentException> {
            CsvConfiguration(quoteChar = '"', recordSeparator = "\"")
        }
    }

    @Test
    fun `should fail if recordSeparator is escapeChar`() {
        assertThrows<IllegalArgumentException> {
            CsvConfiguration(escapeChar = '\\', recordSeparator = "\\")
        }
    }
}