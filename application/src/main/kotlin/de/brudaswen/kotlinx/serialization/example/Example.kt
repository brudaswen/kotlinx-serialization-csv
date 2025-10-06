package de.brudaswen.kotlinx.serialization.example

import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.Serializable
import kotlinx.serialization.builtins.ListSerializer
import kotlinx.serialization.csv.Csv

@Serializable
data class Person(
    val nickname: String,
    val name: String?,
    val appearance: Appearance,
)

@Serializable
data class Appearance(
    val gender: Gender?,
    val age: Int?,
    val height: Double?,
)

@Serializable
enum class Gender {
    MALE,
    FEMALE,
}

@OptIn(ExperimentalSerializationApi::class)
fun main() {
    val csv = Csv { hasHeaderRecord = true }

    val records = listOf(
        Person("Neo", "Thomas A. Anderson", Appearance(Gender.MALE, 37, 1.86)),
        Person("Trinity", null, Appearance(Gender.FEMALE, null, 1.74)),
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
