package kotlinx.serialization.csv.records

import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.builtins.ListSerializer
import kotlinx.serialization.csv.Csv
import kotlinx.serialization.test.assertEncodeAndDecode
import kotlin.test.Test

@OptIn(ExperimentalSerializationApi::class)
class CsvNestedRecordTest {

    @Test
    fun testNestedRecord() = Csv.assertEncodeAndDecode(
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
        NestedRecord.serializer()
    )

    @Test
    fun testNestedRecordList() = Csv.assertEncodeAndDecode(
        "0,Alice,0.0,1.0,100,info\n1,Bob,10.0,20.0,50,info2",
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
        ListSerializer(NestedRecord.serializer())
    )

    @Test
    fun testNestedRecordWithHeader() = Csv {
        hasHeaderRecord = true
    }.assertEncodeAndDecode(
        "time,name,data.location.lat,data.location.lon,data.speed,data.info\n0,Alice,0.0,1.0,100,info",
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
        NestedRecord.serializer()
    )

    @Test
    fun testNestedRecordListWithHeader() = Csv {
        hasHeaderRecord = true
    }.assertEncodeAndDecode(
        "time,name,data.location.lat,data.location.lon,data.speed,data.info\n0,Alice,0.0,1.0,100,info\n1,Bob,10.0,20.0,50,info2",
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
        ListSerializer(NestedRecord.serializer())
    )
}
