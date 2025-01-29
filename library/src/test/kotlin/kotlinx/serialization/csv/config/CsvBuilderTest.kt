package kotlinx.serialization.csv.config

import io.kotest.assertions.throwables.shouldThrow
import kotlin.test.Test

class CsvBuilderTest {

    @Test
    fun `should fail if delimiter equals quoteChar`() {
        shouldThrow<IllegalArgumentException> {
            CsvBuilder().apply {
                delimiter = '!'
                quoteChar = '!'
            }.build()
        }
    }

    @Test
    fun `should fail if delimiter equals escapeChar`() {
        shouldThrow<IllegalArgumentException> {
            CsvBuilder().apply {
                delimiter = '!'
                escapeChar = '!'
            }.build()
        }
    }

    @Test
    fun `should fail if escapeChar not set`() {
        shouldThrow<IllegalArgumentException> {
            CsvBuilder().apply {
                quoteMode = QuoteMode.NONE
                escapeChar = null
            }.build()
        }
    }

    @Test
    fun `should fail if recordSeparator is empty`() {
        shouldThrow<IllegalArgumentException> {
            CsvBuilder().apply {
                recordSeparator = ""
            }.build()
        }
    }

    @Test
    fun `should fail if recordSeparator is too long`() {
        shouldThrow<IllegalArgumentException> {
            CsvBuilder().apply {
                recordSeparator = "\r\n\n"
            }.build()
        }
    }

    @Test
    fun `should fail if recordSeparator is delimiter`() {
        shouldThrow<IllegalArgumentException> {
            CsvBuilder().apply {
                delimiter = ','
                recordSeparator = ","
            }.build()
        }
    }

    @Test
    fun `should fail if recordSeparator is quoteChar`() {
        shouldThrow<IllegalArgumentException> {
            CsvBuilder().apply {
                quoteChar = '"'
                recordSeparator = "\""
            }.build()
        }
    }

    @Test
    fun `should fail if recordSeparator is escapeChar`() {
        shouldThrow<IllegalArgumentException> {
            CsvBuilder().apply {
                escapeChar = '\\'
                recordSeparator = "\\"
            }.build()
        }
    }
}