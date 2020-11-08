package kotlinx.serialization.csv.decode

/**
 * Stream that allow to read [Char]s from.
 */
interface Source {

    /**
     * Current read offset in the source.
     */
    val offset: Int

    /**
     * Check if there are more characters to read.
     * @return True if EOF has not been read, yet; false if EOF has already been read.
     */
    fun canRead(): Boolean

    /**
     * Read one [Char] from the stream.
     * @return The read [Char] or `null` if stream reached EOF.
     */
    fun read(): Char?

    /**
     * Return the next [Char] from the stream without actually reading it.
     * @return The next [Char] or `null` if stream reached EOF.
     */
    fun peek(): Char?

    /**
     * Mark the current position in the stream. Calling [reset] afterwards resets the stream to this marked position.
     *
     * Calling [mark] must be proceeded by a call to [reset] or [unmark].
     */
    fun mark()

    /**
     * Remove the last [mark] without resetting the stream to the marked position.
     */
    fun unmark()

    /**
     * Reset the stream to the last [mark]ed position (and remove the mark).
     */
    fun reset()
}
