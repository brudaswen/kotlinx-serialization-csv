package kotlinx.serialization.csv.decode

import java.io.Reader

class FetchSource(private val getChar: ()->Char?): Source {

    constructor(reader: Reader):this(getChar = {
        reader.read().let {
            if(it == -1) null else it.toChar()
        }
    })

    private var nextPosition = 0
    override var offset: Int = 0
        private set

    private var next: Char? = getChar()
    private fun nextChar(): Char {
        val n = next ?: throw IllegalStateException("Out of characters")
        next = getChar()
        nextPosition++
        return n
    }

    private var queue = ArrayList<Char>(2048)
    private var marks = ArrayList<Int>(2048)
    private var queueOffset = 0

    val queueSize: Int get() = queue.size

    override fun canRead(): Boolean = offset <= nextPosition

    override fun read(): Char? {
        // println("read $offset")
        if(offset > nextPosition) {
            return null
        } else if(offset == nextPosition) {
            if(next == null) {
                offset++
                // println("hit the end")
                if(marks.isEmpty()) queue.clear()
                return null
            }
            val c = nextChar()
            if(marks.isNotEmpty()) {
                // println("Adding $c to the queue")
                if(queue.isEmpty()) {
                    // println("Queue offset set to $offset")
                    queueOffset = offset
                }
                queue.add(c)
            } else {
                // println("Queue cleared due to read after end with no marks")
                queue.clear()
            }
            offset++
            return c
        } else {
            // println("Reading $offset from queue: ${queue} at index ${offset-queueOffset} due to offset ${queueOffset}")
            val indexToCheck = offset-queueOffset
            val result = queue[indexToCheck]
            offset++
            return result
        }
    }

    override fun peek(): Char? {
        // println("peek $offset")
        if(offset > nextPosition) {
            return null
        } else if(offset == nextPosition) {
            return next
        } else {
            // println("Peeking $offset from queue: ${queue} at index ${offset-queueOffset} due to offset ${queueOffset}")
            return queue[offset-queueOffset]
        }
    }

    override fun mark() {
        // println("mark $offset")
        marks.add(offset)
        // println("Marks: $marks")
    }

    override fun unmark() {
        // println("unmark $offset")
        marks.removeAt(marks.lastIndex)
        // println("Marks: $marks")
    }

    override fun reset() {
        // println("reset $offset")
        offset = marks[marks.lastIndex]
        marks.removeAt(marks.lastIndex)
        // println("Marks: $marks, reset to ${offset}")
    }
}