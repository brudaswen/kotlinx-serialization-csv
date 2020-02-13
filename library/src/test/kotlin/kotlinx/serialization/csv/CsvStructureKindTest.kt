package kotlinx.serialization.csv

import kotlinx.serialization.internal.nullable
import kotlinx.serialization.list
import kotlinx.serialization.serializer
import kotlinx.serialization.set
import kotlinx.serialization.test.assertStringFormAndRestored
import kotlin.test.Test

class CsvStructureKindTest {

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
            Csv(CsvConfiguration(ignoreEmptyLines = false))
    )
}