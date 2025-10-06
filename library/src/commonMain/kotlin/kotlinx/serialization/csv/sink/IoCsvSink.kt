package kotlinx.serialization.csv.sink

import kotlinx.io.Sink
import kotlinx.io.writeString

internal class IoCsvSink(
    private val output: Sink,
) : CsvSink {
    override fun append(value: Char): CsvSink = apply {
        output.writeString(value.toString())
    }

    override fun append(value: String): CsvSink = apply {
        output.writeString(value)
    }
}
