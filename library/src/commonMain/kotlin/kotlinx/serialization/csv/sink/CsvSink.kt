package kotlinx.serialization.csv.sink

import kotlinx.io.Sink

/**
 * A sink for CSV output.
 */
public interface CsvSink {
    public fun append(value: Char): CsvSink
    public fun append(value: String): CsvSink
}

/**
 * Create [CsvSink] that outputs to [Appendable].
 */
public fun CsvSink(
    output: Appendable,
): CsvSink = AppendableCsvSink(output)

/**
 * Create [CsvSink] that outputs to [Sink].
 */
public fun CsvSink(
    output: Sink,
): CsvSink = IoCsvSink(output)
