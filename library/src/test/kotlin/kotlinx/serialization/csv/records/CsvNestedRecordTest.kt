package kotlinx.serialization.csv.records

import kotlinx.serialization.builtins.ListSerializer
import kotlinx.serialization.builtins.list
import kotlinx.serialization.csv.Csv
import kotlinx.serialization.csv.CsvConfiguration
import kotlinx.serialization.test.assertStringFormAndRestored
import kotlin.test.Test

class CsvNestedRecordTest {

    @Test
    fun testNestedRecord() = assertStringFormAndRestored(
        "0,Alice,0.0,1.0,100,info",
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
        Csv
    )

    @Test
    fun testNestedRecordList() = assertStringFormAndRestored(
        "0,Alice,0.0,1.0,100,info\r\n1,Bob,10.0,20.0,50,info2",
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
        ListSerializer(NestedRecord.serializer()),
        Csv
    )

    @Test
    fun testNestedRecordWithHeader() = assertStringFormAndRestored(
        "time,name,data.location.lat,data.location.lon,data.speed,data.info\r\n0,Alice,0.0,1.0,100,info",
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
            CsvConfiguration(
                hasHeaderRecord = true
            )
        )
    )

    @Test
    fun testNestedRecordListWithHeader() = assertStringFormAndRestored(
        "time,name,data.location.lat,data.location.lon,data.speed,data.info\r\n0,Alice,0.0,1.0,100,info\r\n1,Bob,10.0,20.0,50,info2",
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
        ListSerializer(NestedRecord.serializer()),
        Csv(
            CsvConfiguration(
                hasHeaderRecord = true
            )
        )
    )
}
