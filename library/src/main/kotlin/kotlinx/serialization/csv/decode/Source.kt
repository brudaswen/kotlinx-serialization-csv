package kotlinx.serialization.csv.decode

/**
 * Stream that allow to read [Char]s from.
 */
public interface Source {

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
