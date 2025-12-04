package kotlinx.serialization.csv.source

private const val EOF = -1

/**
 * Character stream generated from a [String].
 */
internal class StringSource(
    private val content: String,
) : CsvSource {

    override val offset: Int
        get() = position

    private var position = 0

    private var marks = arrayListOf<Int>()

    override fun canRead() = position != EOF

    override fun read() = peek().also {
        when (position) {
            in content.indices -> position++
            else -> position = EOF
        }
    }

    override fun peek() = when (position) {
        in content.indices -> content[position]
        else -> null
    }

    override fun mark() {
        marks.add(position)
    }

    override fun unmark() {
        marks.removeLast()
    }

    override fun reset() {
        position = marks.removeLast()
    }

    override fun toString(): String =
        "StringSource(position=$position, content.length=${content.length})"
}
