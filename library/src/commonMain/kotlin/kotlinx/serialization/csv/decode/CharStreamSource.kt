package kotlinx.serialization.csv.decode

import java.io.EOFException
import java.io.Reader

internal class CharStreamSource(
    private val readChar: () -> Char?,
) : Source {

    constructor(
        reader: Reader,
    ) : this(
        readChar = {
            reader.read().let {
                if (it == -1) null else it.toChar()
            }
        },
    )

    private var nextPosition = 0
    override var offset: Int = 0
        private set

    private var queue = ArrayList<Char>(2048)
    private var marks = ArrayList<Int>(2048)
    private var queueOffset = 0

    private var next: Char? = null
        get() {
            if (field == null && nextPosition == 0) {
                // Reading first char has to happen lazily to avoid blocking read calls
                // during the initialization of this source.
                field = readChar()
            }
            return field
        }

    private fun nextChar(): Char {
        val nextChar = next ?: throw EOFException("No more characters to read.")
        next = readChar()
        nextPosition++
        return nextChar
    }

    override fun canRead(): Boolean = offset <= nextPosition

    override fun read(): Char? {
        if (offset > nextPosition) {
            return null
        } else if (offset == nextPosition) {
            if (next == null) {
                offset++
                if (marks.isEmpty()) queue.clear()
                return null
            }
            val c = nextChar()
            if (marks.isNotEmpty()) {
                if (queue.isEmpty()) {
                    queueOffset = offset
                }
                queue.add(c)
            } else {
                queue.clear()
            }
            offset++
            return c
        } else {
            val indexToCheck = offset - queueOffset
            val result = queue[indexToCheck]
            offset++
            return result
        }
    }

    override fun peek(): Char? = when {
        offset > nextPosition -> null
        offset == nextPosition -> next
        else -> queue[offset - queueOffset]
    }

    override fun mark() {
        marks.add(offset)
    }

    override fun unmark() {
        marks.removeAt(marks.lastIndex)
    }

    override fun reset() {
        offset = marks[marks.lastIndex]
        marks.removeAt(marks.lastIndex)
    }
}
