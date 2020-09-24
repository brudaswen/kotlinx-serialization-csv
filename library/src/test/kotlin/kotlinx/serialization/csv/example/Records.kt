@file:Suppress("EXPERIMENTAL_API_USAGE")
@file:UseSerializers(UUIDSerializer::class, LocalDateTimeSerializer::class)

package kotlinx.serialization.csv.example

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.Transient
import kotlinx.serialization.UseSerializers
import java.time.LocalDateTime
import java.util.*

@Serializable
data class LocationRecord(
    val id: Long,
    val date: LocalDateTime,
    val position: Position,
    val driver: Person,
    val vehicle: Vehicle,
    val vehicleData: VehicleData
)

@Serializable
data class VehiclePartRecord(
    val id: Long,
    val vehicle: Vehicle,
    val part: Part,
    val wear: Double
)

@Serializable
data class VehicleFeaturesRecord(
    val vehicle: Vehicle,
    val features: List<Feature>?,
    val map: Map<Feature, Int>?
)

@Serializable
data class Person(
    val id: Long,
    val foreName: String,
    val lastName: String,
    val birthday: Long?,
    @Transient val secret: String? = null
)

@Serializable
data class Vehicle(
    val uuid: UUID,
    val type: VehicleType,
    val brand: String
)

@Serializable
enum class VehicleType {
    CAR,

    @SerialName("MOTORBIKE")
    BIKE
}

@Serializable
data class Position(
    val latitude: Double,
    val longitude: Double
)

@Serializable
data class VehicleData(
    val speed: Double?,
    val consumption: Consumption
)

@Serializable
sealed class Consumption {
    @Serializable
    @SerialName("Unknown")
    object Unknown : Consumption()

    @Serializable
    @SerialName("Combustion")
    data class Combustion(
        val consumptionLiterPer100Km: Double
    ) : Consumption()

    @Serializable
    @SerialName("Electric")
    data class Electric(
        val consumptionKWhPer100Km: Double
    ) : Consumption()
}

interface Part

@Serializable
@SerialName("Tire")
data class Tire(
    private val axis: Axis,
    private val side: Side,
    private val width: Int,
    private val aspectRatio: Int,
    private val diameter: Int
) : Part {
    override fun toString() = "Tire[$width/${aspectRatio}R$diameter]"

    enum class Axis { FRONT, REAR }
    enum class Side { LEFT, RIGHT }
}

@Serializable
@SerialName("Oil")
data class Oil(
    private val viscosityCold: Int,
    private val viscosityWarm: Int
) : Part {
    override fun toString() = "Oil[${viscosityCold}W$viscosityWarm]"
}

@Serializable
enum class Feature {
    ELECTRIC, AUTOMATIC, HEATED_SEATS, NAVIGATION_SYSTEM, XENON
}
