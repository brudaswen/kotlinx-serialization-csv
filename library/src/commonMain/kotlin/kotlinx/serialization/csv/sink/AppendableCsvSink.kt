package kotlinx.serialization.csv.sink

internal class AppendableCsvSink(
    private val output: Appendable,
) : CsvSink {
    override fun append(value: Char): CsvSink = apply {
        output.append(value)
    }

    override fun append(value: String): CsvSink = apply {
        output.append(value)
    }
}
