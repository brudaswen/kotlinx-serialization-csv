package kotlinx.serialization.csv.example

import kotlinx.serialization.*
import kotlinx.serialization.internal.SerialClassDescImpl
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.*

@Serializer(forClass = UUID::class)
object UUIDSerializer : KSerializer<UUID> {
    override val descriptor: SerialDescriptor = SerialClassDescImpl("UUID")

    override fun serialize(encoder: Encoder, obj: UUID) =
        encoder.encodeString(obj.toString())

    override fun deserialize(decoder: Decoder): UUID =
        UUID.fromString(decoder.decodeString())
}

@Serializer(forClass = LocalDateTime::class)
object LocalDateTimeSerializer : KSerializer<LocalDateTime> {
    private val format = DateTimeFormatter.ISO_DATE_TIME

    override val descriptor: SerialDescriptor = SerialClassDescImpl("LocalDateTime")

    override fun serialize(encoder: Encoder, obj: LocalDateTime) =
        encoder.encodeString(format.format(obj))

    override fun deserialize(decoder: Decoder): LocalDateTime =
        format.parse(decoder.decodeString(), LocalDateTime::from)
}
