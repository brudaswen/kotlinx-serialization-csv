package kotlinx.serialization.csv.records

import kotlinx.serialization.Serializable
import kotlinx.serialization.builtins.*
import kotlinx.serialization.csv.Csv
import kotlinx.serialization.csv.CsvConfiguration
import kotlinx.serialization.test.assertStringFormAndRestored
import kotlin.test.Test

/**
 * Test [Csv] with [List]s, [Set]s and [Map]s.
 */
class CsvCollectionsTest {

    @Test
    fun testListOfIntList() = assertStringFormAndRestored(
        "1,2\r\n3,4\r\n5,6,7",
        listOf(
            listOf(1, 2),
            listOf(3, 4),
            listOf(5, 6, 7)
        ),
        Int.serializer().list.list,
        Csv
    )

    @Test
    fun testListOfIntSet() = assertStringFormAndRestored(
        "1,2\r\n3,4\r\n5,6,7",
        listOf(
            setOf(1, 2),
            setOf(3, 4),
            setOf(5, 6, 7)
        ),
        Int.serializer().set.list,
        Csv
    )

    @Test
    fun testNullableListOfNullableIntList() = assertStringFormAndRestored(
        "1,2\r\n\r\n5,,7",
        listOf(
            listOf(1, 2),
            null,
            listOf(5, null, 7)
        ),
        Int.serializer().nullable.list.nullable.list,
        Csv(
            CsvConfiguration(
                ignoreEmptyLines = false
            )
        )
    )

    @Test
    fun testMapOfIntLists() = assertStringFormAndRestored(
        "3,2,1,2,2,3,4,2,5,6,2,7,8,1,9,4,10,11,12,13",
        mapOf(
            listOf(1, 2) to listOf(3, 4),
            listOf(5, 6) to listOf(7, 8),
            listOf(9) to listOf(10, 11, 12, 13)
        ),
        MapSerializer(Int.serializer().list, Int.serializer().list),
        Csv
    )

    @Test
    fun testMultipleMapOfIntLists() = assertStringFormAndRestored(
        """|2,1,2,2,3,4,2,5,6,2,7,8,1,9,4,10,11,12,13
           |1,1,2,2,3,3,4,5,6,4,7,8,9,10
        """.trimMargin().replace("\n", "\r\n"),
        listOf(
            mapOf(
                listOf(1, 2) to listOf(3, 4),
                listOf(5, 6) to listOf(7, 8),
                listOf(9) to listOf(10, 11, 12, 13)
            ),
            mapOf(
                listOf(1) to listOf(2, 3),
                listOf(4, 5, 6) to listOf(7, 8, 9, 10)
            )
        ),
        MapSerializer(Int.serializer().list, Int.serializer().list).list,
        Csv
    )

    @Test
    fun testRecordWithMapOfIntLists() = assertStringFormAndRestored(
        "3,2,1,2,2,3,4,2,5,6,2,7,8,1,9,4,10,11,12,13",
        Record(
            mapOf(
                listOf(1, 2) to listOf(3, 4),
                listOf(5, 6) to listOf(7, 8),
                listOf(9) to listOf(10, 11, 12, 13)
            )
        ),
        Record.serializer(),
        Csv
    )

    @Test
    fun testMultipleRecordsWithMapOfIntLists() = assertStringFormAndRestored(
        """|3,2,1,2,2,3,4,2,5,6,2,7,8,1,9,4,10,11,12,13
           |2,1,1,2,2,3,3,4,5,6,4,7,8,9,10
        """.trimMargin().replace("\n", "\r\n"),
        listOf(
            Record(
                mapOf(
                    listOf(1, 2) to listOf(3, 4),
                    listOf(5, 6) to listOf(7, 8),
                    listOf(9) to listOf(10, 11, 12, 13)
                )
            ),
            Record(
                mapOf(
                    listOf(1) to listOf(2, 3),
                    listOf(4, 5, 6) to listOf(7, 8, 9, 10)
                )
            )
        ),
        Record.serializer().list,
        Csv
    )
}

@Serializable
data class Record(
    val map: Map<List<Int>, List<Int>>
)
