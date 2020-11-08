package kotlinx.serialization.csv.records

import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.Serializable
import kotlinx.serialization.builtins.*
import kotlinx.serialization.csv.Csv
import kotlinx.serialization.test.assertEncodeAndDecode
import kotlin.test.Test

/**
 * Test [Csv] with [List]s, [Set]s and [Map]s.
 */
@OptIn(ExperimentalSerializationApi::class)
class CsvCollectionsTest {

    @Test
    fun testListOfIntList() = Csv.assertEncodeAndDecode(
        "1,2\n3,4\n5,6,7",
        listOf(
            listOf(1, 2),
            listOf(3, 4),
            listOf(5, 6, 7)
        ),
        ListSerializer(ListSerializer(Int.serializer()))
    )

    @Test
    fun testListOfIntSet() = Csv.assertEncodeAndDecode(
        "1,2\n3,4\n5,6,7",
        listOf(
            setOf(1, 2),
            setOf(3, 4),
            setOf(5, 6, 7)
        ),
        ListSerializer(SetSerializer(Int.serializer()))
    )

    @Test
    fun testNullableListOfNullableIntList() = Csv {
        ignoreEmptyLines = false
    }.assertEncodeAndDecode(
        "1,2\n\n5,,7",
        listOf(
            listOf(1, 2),
            null,
            listOf(5, null, 7)
        ),
        ListSerializer(ListSerializer(Int.serializer().nullable).nullable)
    )

    @Test
    fun testMapOfIntLists() = Csv.assertEncodeAndDecode(
        "3,2,1,2,2,3,4,2,5,6,2,7,8,1,9,4,10,11,12,13",
        mapOf(
            listOf(1, 2) to listOf(3, 4),
            listOf(5, 6) to listOf(7, 8),
            listOf(9) to listOf(10, 11, 12, 13)
        ),
        MapSerializer(ListSerializer(Int.serializer()), ListSerializer(Int.serializer()))
    )

    @Test
    fun testMultipleMapOfIntLists() = Csv.assertEncodeAndDecode(
        """|2,1,2,2,3,4,2,5,6,2,7,8,1,9,4,10,11,12,13
           |1,1,2,2,3,3,4,5,6,4,7,8,9,10
        """.trimMargin(),
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
        ListSerializer(
            MapSerializer(
                ListSerializer(Int.serializer()),
                ListSerializer(Int.serializer())
            )
        )
    )

    @Test
    fun testRecordWithMapOfIntLists() = Csv.assertEncodeAndDecode(
        "3,2,1,2,2,3,4,2,5,6,2,7,8,1,9,4,10,11,12,13",
        Record(
            mapOf(
                listOf(1, 2) to listOf(3, 4),
                listOf(5, 6) to listOf(7, 8),
                listOf(9) to listOf(10, 11, 12, 13)
            )
        ),
        Record.serializer()
    )

    @Test
    fun testMultipleRecordsWithMapOfIntLists() = Csv.assertEncodeAndDecode(
        """|3,2,1,2,2,3,4,2,5,6,2,7,8,1,9,4,10,11,12,13
           |2,1,1,2,2,3,3,4,5,6,4,7,8,9,10
        """.trimMargin(),
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
        ListSerializer(Record.serializer())
    )
}

@Serializable
data class Record(
    val map: Map<List<Int>, List<Int>>
)
