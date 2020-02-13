package kotlinx.serialization.csv

interface Source {
    fun canRead(): Boolean
    fun read(): Char?
    fun peek(): Char?
    fun mark()
    fun unmark()
    fun reset()
}
