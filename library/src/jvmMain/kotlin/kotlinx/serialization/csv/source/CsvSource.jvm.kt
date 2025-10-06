package kotlinx.serialization.csv.source

import java.io.InputStream
import java.io.Reader

/**
 * Create [CsvSource] from [InputStream].
 */
public fun CsvSource(
    input: InputStream,
): CsvSource = CharStreamSource(
    readChar = { input.read().takeIf { it != -1 }?.toChar() },
)

/**
 * Create [CsvSource] from [Reader].
 */
public fun CsvSource(
    input: Reader,
): CsvSource = CharStreamSource(
    readChar = { input.read().takeIf { it != -1 }?.toChar() },
)
