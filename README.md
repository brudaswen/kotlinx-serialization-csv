# Kotlin Serialization CSV

[![Maven Central](https://img.shields.io/maven-central/v/de.brudaswen.kotlinx.serialization/kotlinx-serialization-csv?style=flat-square)](https://search.maven.org/artifact/de.brudaswen.kotlinx.serialization/kotlinx-serialization-csv)
![Snapshot](https://img.shields.io/nexus/s/de.brudaswen.kotlinx.serialization/kotlinx-serialization-csv?label=snapshot&server=https%3A%2F%2Foss.sonatype.org&style=flat-square)
[![CI Status](https://img.shields.io/github/workflow/status/brudaswen/kotlinx-serialization-csv/CI?style=flat-square)](https://github.com/brudaswen/kotlinx-serialization-csv/actions?query=workflow%3ACI)
[![Codecov](https://img.shields.io/codecov/c/github/brudaswen/kotlinx-serialization-csv?style=flat-square)](https://codecov.io/gh/brudaswen/kotlinx-serialization-csv)
[![License](https://img.shields.io/github/license/brudaswen/kotlinx-serialization-csv?style=flat-square)](https://www.apache.org/licenses/LICENSE-2.0)

Library to easily use *Kotlin Serialization* to serialize/parse CSV.

All types of record classes are supported (primitives, classes, enums, nested classes, ...).
However, CSV serialization works best if the column number if fixed. So, collections (lists, sets, maps) and 
open classes should be avoided.

## Gradle Dependencies
```kotlin
// Kotlin Serialization CSV
implementation("de.brudaswen.kotlinx.serialization:kotlinx-serialization-csv:2.0.0")

// Kotlin Serialization is added automatically, but can be added to force a specific version
implementation("org.jetbrains.kotlinx:kotlinx-serialization-core:1.0.0")
```

## Usage
First configure your project according to the 
[documentation](https://github.com/Kotlin/kotlinx.serialization#setup)
of the *Kotlin Serialization* library.

### CSV Example
```kotlin
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.Serializable
import kotlinx.serialization.builtins.ListSerializer
import kotlinx.serialization.csv.Csv

@Serializable
data class Person(val nickname: String, val name: String?, val appearance: Appearance)

@Serializable
data class Appearance(val gender: Gender?, val age: Int?, val height: Double?)

@Serializable
enum class Gender { MALE, FEMALE }

@OptIn(ExperimentalSerializationApi::class)
fun main() {
    val csv = Csv { hasHeaderRecord = true }

    val records = listOf(
        Person("Neo", "Thomas A. Anderson", Appearance(Gender.MALE, 37, 1.86)),
        Person("Trinity", null, Appearance(Gender.FEMALE, null, 1.74))
    )
    val serialized = csv.encodeToString(ListSerializer(Person.serializer()), records)
    println(serialized)
    // nickname,name,appearance.gender,appearance.age,appearance.height
    // Neo,Thomas A. Anderson,MALE,37,1.86
    // Trinity,,FEMALE,,1.74

    val input = """
        nickname,appearance.gender,appearance.height,appearance.age,name
        Neo,MALE,1.86,37,Thomas A. Anderson
        Trinity,FEMALE,1.74,,
    """.trimIndent()
    val parsed = csv.decodeFromString(ListSerializer(Person.serializer()), input)
    println(parsed)
    // [
    //   Person(nickname=Neo, name=Thomas A. Anderson, appearance=Appearance(gender=MALE, age=37, height=1.86)),
    //   Person(nickname=Trinity, name=null, appearance=Appearance(gender=FEMALE, age=null, height=1.74))
    // ]
}
```
### Pre-defined CSV formats
The library comes with multiple pre-defined Csv formats that can be used out of the box.

| Config                 | Description |
|---                     |---          |
| `Csv.Default`          | Standard Comma Separated Value format, as for `Rfc4180` but using Unix newline (`\n`) as record separator and ignoring empty lines. *Format is unstable and may change in upcoming versions.* |
| `Csv.Rfc4180`          | Comma separated format as defined by [RFC 4180](http://tools.ietf.org/html/rfc4180). |

### Configuration
CSV serialization and parsing options can be changed by configuring the `Csv` instance during
initialization via the `Csv { }` builder function.

| Option                 | Default Value  | Description |
|---                     |---             | ---         |
| `delimiter`            | `,`            | The delimiter character between columns. |
| `recordSeparator`      | `\n`           | The record separator. |
| `quoteChar`            | `"`            | The quote character used to quote column values. |
| `quoteMode`            | `MINIMAL`      | The quote mode used to decide if a column value should get quoted.<ul><li>`ALL`: Quotes *all* fields.</li><li>`ALL_NON_NULL`: Quotes all *non-null fields* and *fields which contain special characters*.</li><li>`ALL_NON_NUMERIC`: Quotes all *non-numeric fields* and *fields which contain special characters*.</li><li>`MINIMAL`: Quotes *fields which contain special characters*.</li><li>`NONE`: *Never* quotes fields (requires `CsvConfiguration.escapeChar` to be set).</li></ul> |
| `escapeChar`           | `null`         | The escape character used to escape reserved characters in a column value. |
| `nullString`           | *empty string* | The value to identify `null` values. |
| `ignoreEmptyLines`     | `true`         | Ignore empty lines during parsing. |
| `hasHeaderRecord`      | `false`        | First line is header record. |
| `headerSeparator`      | `.`            | Character that is used to separate hierarchical header names. |
| `ignoreUnknownColumns` | `false`        | Ignore unknown columns (only has effect when `hasHeaderRecord` is enabled). |
| `hasTrailingDelimiter` | `false`        | If records end with a trailing `delimiter`. |

## Requirements

| Dependency             | Versions |
|---                     |---       |
| *Kotlin Serialization* | 1.0.0    |

## License

```
Copyright 2020 Sven Obser

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

   http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
```
