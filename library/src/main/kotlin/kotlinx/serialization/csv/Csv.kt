/*
 * Copyright 2017-2019 JetBrains s.r.o. Use of this source code is governed by the Apache 2.0 license.
 */

package kotlinx.serialization.csv

import kotlinx.serialization.*
import kotlinx.serialization.modules.EmptyModule
import kotlinx.serialization.modules.SerialModule

class Csv(internal val configuration: CsvConfiguration, context: SerialModule = EmptyModule) : AbstractSerialFormat(context), StringFormat {

    override fun <T> stringify(serializer: SerializationStrategy<T>, obj: T): String {
        val result = StringBuilder()
        RootCsvEncoder(configuration, result).encode(serializer, obj)
        return result.toString()
    }

    override fun <T> parse(deserializer: DeserializationStrategy<T>, string: String): T {
        val reader = CsvReader(StringSource(string), configuration)
        val input = RootCsvDecoder(configuration, reader)
        val result = input.decode(deserializer)
        if (!reader.isDone) {
            error("Reader has not consumed the whole input: $reader")
        }
        return result
    }

    companion object : StringFormat {
        @UnstableDefault
        val default = Csv(CsvConfiguration.default)

        @UnstableDefault
        val rfc4180 = Csv(CsvConfiguration.rfc4180)

        @UseExperimental(UnstableDefault::class)
        override val context: SerialModule
            get() = default.context

        @UnstableDefault
        override fun <T> stringify(serializer: SerializationStrategy<T>, obj: T): String =
                default.stringify(serializer, obj)

        @UnstableDefault
        override fun <T> parse(deserializer: DeserializationStrategy<T>, string: String): T =
                default.parse(deserializer, string)
    }
}
