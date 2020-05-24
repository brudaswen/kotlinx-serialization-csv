package kotlinx.serialization.csv.example

import kotlinx.serialization.*
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.*

@Serializer(forClass = UUID::class)
object UUIDSerializer : KSerializer<UUID> {
    override val descriptor: SerialDescriptor = SerialDescriptor("UUID")

    override fun serialize(encoder: Encoder, value: UUID) =
        encoder.encodeString(value.toString())

    override fun deserialize(decoder: Decoder): UUID =
        UUID.fromString(decoder.decodeString())
}

@Serializer(forClass = LocalDateTime::class)
object LocalDateTimeSerializer : KSerializer<LocalDateTime> {
    private val format = DateTimeFormatter.ISO_DATE_TIME

    override val descriptor: SerialDescriptor = SerialDescriptor("LocalDateTime")

    override fun serialize(encoder: Encoder, value: LocalDateTime) =
        encoder.encodeString(format.format(value))

    override fun deserialize(decoder: Decoder): LocalDateTime =
        format.parse(decoder.decodeString(), LocalDateTime::from)
}
