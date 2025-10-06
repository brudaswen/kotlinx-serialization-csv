package kotlinx.serialization.csv.source

import kotlinx.io.EOFException
import kotlinx.io.Source

/**
 * Stream that allow to read [Char]s from.
 */
public interface CsvSource {

    /**
     * Current read offset in the source.
     */
    public val offset: Int

    /**
     * Check if there are more characters to read.
     * @return True if EOF has not been read, yet; false if EOF has already been read.
     */
    public fun canRead(): Boolean

    /**
     * Read one [Char] from the stream.
     * @return The read [Char] or `null` if stream reached EOF.
     */
    public fun read(): Char?

    /**
     * Return the next [Char] from the stream without actually reading it.
     * @return The next [Char] or `null` if stream reached EOF.
     */
    public fun peek(): Char?

    /**
     * Mark the current position in the stream. Calling [reset] afterwards resets the stream to this marked position.
     *
     * Calling [mark] must be proceeded by a call to [reset] or [unmark].
     */
    public fun mark()

    /**
     * Remove the last [mark] without resetting the stream to the marked position.
     */
    public fun unmark()

    /**
     * Reset the stream to the last [mark]ed position (and remove the mark).
     */
    public fun reset()
}

/**
 * Create [CsvSource] that reads from [String].
 */
public fun CsvSource(
    input: String,
): CsvSource = StringSource(input)

/**
 * Create [CsvSource] that reads from [Source].
 */
public fun CsvSource(
    input: Source,
): CsvSource = CharStreamSource(
    readChar = {
        try {
            input.readByte().toInt().toChar()
        } catch (_: EOFException) {
            null
        }
    },
)
