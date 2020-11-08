@file:Suppress("FunctionName")

package kotlinx.serialization.csv

import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.SerializationException
import kotlinx.serialization.descriptors.SerialDescriptor

/**
 * Generic exception indicating a problem with CSV serialization and deserialization.
 */
internal open class CsvException(message: String) : SerializationException(message)

/**
 * Thrown when [Csv] has failed to create a CSV string from the given value.
 */
internal class CsvEncodingException(message: String) : CsvException(message)

@OptIn(ExperimentalSerializationApi::class)
internal fun UnsupportedSerialDescriptorException(descriptor: SerialDescriptor) =
    CsvEncodingException(
        "CSV does not support '${descriptor.kind}'."
    )

@OptIn(ExperimentalSerializationApi::class)
internal fun HeadersNotSupportedForSerialDescriptorException(descriptor: SerialDescriptor) =
    CsvEncodingException(
        "CSV headers are not supported for variable sized type '${descriptor.kind}'."
    )

/**
 * Thrown when [Csv] has failed to parse the given CSV string or deserialize it to a target class.
 */
internal class CsvDecodingException(message: String) : CsvException(message)

internal fun CsvDecodingException(offset: Int?, message: String) =
    CsvDecodingException(if (offset != null) "Unexpected CSV token at offset $offset: $message" else message)

internal fun UnknownColumnHeaderException(offset: Int, header: String) = CsvDecodingException(
    offset,
    """
    |Encountered unknown column header '$header'.
    |Use 'ignoreUnknownColumns = true' in 'Csv {}' builder to ignore unknown columns.
    |""".trimMargin()
)
