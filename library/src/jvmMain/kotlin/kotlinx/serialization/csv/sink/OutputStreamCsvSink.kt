package kotlinx.serialization.csv.sink

import java.io.OutputStream

internal class OutputStreamCsvSink(private val output: OutputStream) : CsvSink {
    override fun append(value: Char): CsvSink = apply {
        output.write(value.code)
    }

    override fun append(value: String): CsvSink = apply {
        output.write(value.toByteArray())
    }
}
