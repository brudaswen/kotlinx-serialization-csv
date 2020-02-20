package kotlinx.serialization.csv.records

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ByteRecord(val a: Byte)

@Serializable
data class ShortRecord(val a: Short)

@Serializable
data class IntRecord(val a: Int)

@Serializable
data class LongRecord(val a: Long)

@Serializable
data class FloatRecord(val a: Float)

@Serializable
data class DoubleRecord(val a: Double)

@Serializable
data class BooleanRecord(val a: Boolean)

@Serializable
data class CharRecord(val a: Char)

@Serializable
data class StringRecord(val a: String)

@Serializable
data class NullRecord(val a: String?)

@Serializable
data class UnitRecord(val a: Unit)

@Serializable
enum class Enum { FIRST }

@Serializable
data class EnumRecord(val a: Enum)

@Serializable
data class IntStringRecord(val a: Int, val b: String)

@Serializable
data class ComplexRecord(
    val a: Int,
    val b: Byte,
    val c: Short,
    val d: Long,
    val e: Float,
    val f: Double,
    val g: Boolean,
    val h: String,
    val i: String?,
    val j: Unit,
    val k: Enum
)

@Serializable
data class SerialNameRecord(
    val first: Int,
    @SerialName("second") val b: Int
)

@Serializable
data class NestedRecord(
    val time: Int,
    val name: String,
    val data: Data
)

@Serializable
data class Data(val location: Location, val speed: Int, val info: String)

@Serializable
data class Location(val lat: Double, val lon: Double)

@Serializable
object ObjectRecord {
    const val x = 42
}

@Serializable
sealed class SealedRecord(val a: Int) {
    @Serializable
    object Object : SealedRecord(1)

    @Serializable
    object OtherObject : SealedRecord(2)

    @Serializable
    data class Class(val x: String) : SealedRecord(42)

    @Serializable
    data class OtherClass(val x: String) : SealedRecord(41)
}
