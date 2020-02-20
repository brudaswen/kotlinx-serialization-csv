package kotlinx.serialization.csv.config

import kotlinx.serialization.csv.*
import kotlinx.serialization.csv.records.*
import kotlinx.serialization.list
import kotlinx.serialization.test.assertParse
import kotlinx.serialization.test.assertStringFormAndRestored
import kotlin.test.Test

class CsvHasHeaderRecordTest {

    @Test
    fun testDefault() = assertStringFormAndRestored(
        "1",
        IntRecord(1),
        IntRecord.serializer(),
        Csv
    )

    @Test
    fun testWithoutHeaderRecords() = assertStringFormAndRestored(
        "1",
        IntRecord(1),
        IntRecord.serializer(),
        Csv(
            CsvConfiguration(
                hasHeaderRecord = false
            )
        )
    )

    @Test
    fun testWithHeaderRecords() = assertStringFormAndRestored(
        "a\r\n1",
        IntRecord(1),
        IntRecord.serializer(),
        Csv(
            CsvConfiguration(
                hasHeaderRecord = true
            )
        )
    )

    @Test
    fun testListWithoutHeaderRecords() = assertStringFormAndRestored(
        "1\r\n2\r\n3",
        listOf(
            IntRecord(1),
            IntRecord(2),
            IntRecord(3)
        ),
        IntRecord.serializer().list,
        Csv(
            CsvConfiguration(
                hasHeaderRecord = false
            )
        )
    )

    @Test
    fun testListWithHeaderRecords() = assertStringFormAndRestored(
        "a\r\n1\r\n2\r\n3",
        listOf(
            IntRecord(1),
            IntRecord(2),
            IntRecord(3)
        ),
        IntRecord.serializer().list,
        Csv(
            CsvConfiguration(
                hasHeaderRecord = true
            )
        )
    )

    @Test
    fun testMultipleColumns() = assertStringFormAndRestored(
        "a,b\r\n1,testing",
        IntStringRecord(1, "testing"),
        IntStringRecord.serializer(),
        Csv(
            CsvConfiguration(
                hasHeaderRecord = true
            )
        )
    )

    @Test
    fun testSerialName() = assertStringFormAndRestored(
        "first,second\r\n1,2",
        SerialNameRecord(1, 2),
        SerialNameRecord.serializer(),
        Csv(
            CsvConfiguration(
                hasHeaderRecord = true
            )
        )
    )

    @Test
    fun testMultipleColumnsReordered() = assertParse(
        "b,a\r\ntesting,1",
        IntStringRecord(1, "testing"),
        IntStringRecord.serializer(),
        Csv(
            CsvConfiguration(
                hasHeaderRecord = true
            )
        )
    )

    @Test
    fun testListMultipleColumnsReordered() = assertParse(
        "b,a\r\ntesting,1\r\nbar,2",
        listOf(
            IntStringRecord(1, "testing"),
            IntStringRecord(2, "bar")
        ),
        IntStringRecord.serializer().list,
        Csv(
            CsvConfiguration(
                hasHeaderRecord = true
            )
        )
    )

    @Test
    fun testNestedRecordWithHeaderReordered() = assertParse(
        "time,name,data.location.lon,data.location.lat,data.speed,data.info\r\n0,Alice,1.0,0.0,100,info",
        NestedRecord(
            0,
            "Alice",
            Data(
                Location(
                    0.0,
                    1.0
                ), 100, "info"
            )
        ),
        NestedRecord.serializer(),
        Csv(
            (CsvConfiguration(
                hasHeaderRecord = true
            ))
        )
    )

    @Test
    fun testNestedRecordListWithHeaderReordered() = assertParse(
        "time,name,data.location.lon,data.location.lat,data.speed,data.info\r\n0,Alice,1.0,0.0,100,info\r\n1,Bob,20.0,10.0,50,info2",
        listOf(
            NestedRecord(
                0,
                "Alice",
                Data(
                    Location(
                        0.0,
                        1.0
                    ), 100, "info"
                )
            ),
            NestedRecord(
                1,
                "Bob",
                Data(
                    Location(
                        10.0,
                        20.0
                    ), 50, "info2"
                )
            )
        ),
        NestedRecord.serializer().list,
        Csv(
            (CsvConfiguration(
                hasHeaderRecord = true
            ))
        )
    )
}
