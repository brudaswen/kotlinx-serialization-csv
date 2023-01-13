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
        if(offset > nextPosition) {
            return null
        } else if(offset == nextPosition) {
            if(next == null) {
                offset++
                if(marks.isEmpty()) queue.clear()
                return null
            }
            val c = nextChar()
            if(marks.isNotEmpty()) {
                if(queue.isEmpty()) {
                    queueOffset = offset
                }
                queue.add(c)
            } else {
                queue.clear()
            }
            offset++
            return c
        } else {
            val indexToCheck = offset-queueOffset
            val result = queue[indexToCheck]
            offset++
            return result
        }
    }

    override fun peek(): Char? {
        if(offset > nextPosition) {
            return null
        } else if(offset == nextPosition) {
            return next
        } else {
            return queue[offset-queueOffset]
        }
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