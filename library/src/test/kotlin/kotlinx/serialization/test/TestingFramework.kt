/*
 * Copyright 2017-2019 JetBrains s.r.o. Use of this source code is governed by the Apache 2.0 license.
 */

package kotlinx.serialization.test

import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.KSerializer
import kotlinx.serialization.StringFormat
import kotlinx.serialization.csv.CsvDecodingException
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith

@OptIn(ExperimentalSerializationApi::class)
inline fun <reified T : Any?> StringFormat.assertEncodeAndDecode(
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
}

@OptIn(ExperimentalSerializationApi::class)
inline fun <reified T : Any> StringFormat.assertDecode(
    input: String,
    expected: T,
    serializer: KSerializer<T>,
    printResult: Boolean = false
) {
    val restored = decodeFromString(serializer, input)
    if (printResult) println("[Restored form] $restored")
    assertEquals(expected, restored)
}

@OptIn(ExperimentalSerializationApi::class)
inline fun <reified T : Any> StringFormat.assertDecodeFails(
    input: String,
    serializer: KSerializer<T>
) {
    assertFailsWith<CsvDecodingException> {
        decodeFromString(serializer, input)
    }
}

infix fun <T> T.shouldBe(expected: T) = assertEquals(expected, this)
