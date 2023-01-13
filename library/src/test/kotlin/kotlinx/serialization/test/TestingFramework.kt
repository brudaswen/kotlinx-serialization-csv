/*
 * Copyright 2017-2019 JetBrains s.r.o. Use of this source code is governed by the Apache 2.0 license.
 */

package kotlinx.serialization.test

import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.KSerializer
import kotlinx.serialization.StringFormat
import kotlinx.serialization.csv.Csv
import kotlinx.serialization.csv.CsvDecodingException
import java.io.StringReader
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith

@OptIn(ExperimentalSerializationApi::class)
inline fun <reified T : Any?> Csv.assertEncodeAndDecode(
    expected: String,
    original: T,
    serializer: KSerializer<T>,
    printResult: Boolean = false
) {
    val string = encodeToString(serializer, original)
    if (printResult) println("[Serialized form] $string")
    assertEquals(expected, string)
    val restored = decodeFromString(serializer, string)
    if (printResult) println("[Restored form] $restored")
    assertEquals(original, restored)

    val string2 = buildString { encodeToAppendable(serializer, original, this) }
    if (printResult) println("[Serialized form, stream] $string2")
    assertEquals(expected, string2)
    val restored2 = decodeFromReader(serializer, StringReader(string2))
    if (printResult) println("[Restored form, stream] $restored2")
    assertEquals(original, restored2)

    assertEquals(string, string2)
    assertEquals(restored, restored2)
}

@OptIn(ExperimentalSerializationApi::class)
inline fun <reified T : Any> Csv.assertDecode(
    input: String,
    expected: T,
    serializer: KSerializer<T>,
    printResult: Boolean = false
) {
    val restored = decodeFromString(serializer, input)
    if (printResult) println("[Restored form] $restored")
    assertEquals(expected, restored)

    val restored2 = decodeFromReader(serializer, StringReader(input))
    if (printResult) println("[Restored form] $restored2")
    assertEquals(expected, restored2)
}

@OptIn(ExperimentalSerializationApi::class)
inline fun <reified T : Any> Csv.assertDecodeFails(
    input: String,
    serializer: KSerializer<T>
) {
    assertFailsWith<CsvDecodingException> {
        decodeFromString(serializer, input)
    }
}

infix fun <T> T.shouldBe(expected: T) = assertEquals(expected, this)
