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
inline fun <reified T : Any?> assertStringFormAndRestored(
    expected: String,
    original: T,
    serializer: KSerializer<T>,
    format: StringFormat,
    printResult: Boolean = false
) {
    val string = format.encodeToString(serializer, original)
    if (printResult) println("[Serialized form] $string")
    assertEquals(expected, string)
    val restored = format.decodeFromString(serializer, string)
    if (printResult) println("[Restored form] $restored")
    assertEquals(original, restored)
}

@OptIn(ExperimentalSerializationApi::class)
inline fun <reified T : Any> assertParse(
    input: String,
    expected: T,
    serializer: KSerializer<T>,
    format: StringFormat,
    printResult: Boolean = false
) {
    val restored = format.decodeFromString(serializer, input)
    if (printResult) println("[Restored form] $restored")
    assertEquals(expected, restored)
}

@OptIn(ExperimentalSerializationApi::class)
inline fun <reified T : Any> assertParseFails(
    input: String,
    serializer: KSerializer<T>,
    format: StringFormat
) {
    assertFailsWith<CsvDecodingException> {
        format.decodeFromString(serializer, input)
    }
}

@OptIn(ExperimentalSerializationApi::class)
inline fun <reified T : Any> StringFormat.assertStringFormAndRestored(
    expected: String,
    original: T,
    serializer: KSerializer<T>,
    printResult: Boolean = false
) {
    val string = this.encodeToString(serializer, original)
    if (printResult) println("[Serialized form] $string")
    assertEquals(expected, string)
    val restored = this.decodeFromString(serializer, string)
    if (printResult) println("[Restored form] $restored")
    assertEquals(original, restored)
}

infix fun <T> T.shouldBe(expected: T) = assertEquals(expected, this)
