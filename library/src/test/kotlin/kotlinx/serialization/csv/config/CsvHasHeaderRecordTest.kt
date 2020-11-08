package kotlinx.serialization.csv.config

import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.builtins.ListSerializer
import kotlinx.serialization.csv.Csv
import kotlinx.serialization.csv.CsvConfiguration
import kotlinx.serialization.csv.records.*
import kotlinx.serialization.test.assertDecode
import kotlinx.serialization.test.assertEncodeAndDecode
import kotlin.test.Test

@OptIn(ExperimentalSerializationApi::class)
class CsvHasHeaderRecordTest {

    @Test
    fun testDefault() = Csv.assertEncodeAndDecode(
        "1",
        IntRecord(1),
        IntRecord.serializer()
    )

    @Test
    fun testWithoutHeaderRecords() = Csv(
        CsvConfiguration(
            hasHeaderRecord = false
        )
    ).assertEncodeAndDecode(
        "1",
        IntRecord(1),
        IntRecord.serializer()
    )

    @Test
    fun testWithHeaderRecords() = Csv(
        CsvConfiguration(
            hasHeaderRecord = true
        )
    ).assertEncodeAndDecode(
        "a\r\n1",
        IntRecord(1),
        IntRecord.serializer()
    )

    @Test
    fun testListWithoutHeaderRecords() = Csv(
        CsvConfiguration(
            hasHeaderRecord = false
        )
    ).assertEncodeAndDecode(
        "1\r\n2\r\n3",
        listOf(
            IntRecord(1),
            IntRecord(2),
            IntRecord(3)
        ),
        ListSerializer(IntRecord.serializer())
    )

    @Test
    fun testListWithHeaderRecords() = Csv(
        CsvConfiguration(
            hasHeaderRecord = true
        )
    ).assertEncodeAndDecode(
        "a\r\n1\r\n2\r\n3",
        listOf(
            IntRecord(1),
            IntRecord(2),
            IntRecord(3)
        ),
        ListSerializer(IntRecord.serializer())
    )

    @Test
    fun testMultipleColumns() = Csv(
        CsvConfiguration(
            hasHeaderRecord = true
        )
    ).assertEncodeAndDecode(
        "a,b\r\n1,testing",
        IntStringRecord(1, "testing"),
        IntStringRecord.serializer()
    )

    @Test
    fun testSerialName() = Csv(
        CsvConfiguration(
            hasHeaderRecord = true
        )
    ).assertEncodeAndDecode(
        "first,second\r\n1,2",
        SerialNameRecord(1, 2),
        SerialNameRecord.serializer()
    )

    @Test
    fun testMultipleColumnsReordered() = Csv(
        CsvConfiguration(
            hasHeaderRecord = true
        )
    ).assertDecode(
        "b,a\r\ntesting,1",
        IntStringRecord(1, "testing"),
        IntStringRecord.serializer()
    )

    @Test
    fun testListMultipleColumnsReordered() = Csv(
        CsvConfiguration(
            hasHeaderRecord = true
        )
    ).assertDecode(
        "b,a\r\ntesting,1\r\nbar,2",
        listOf(
            IntStringRecord(1, "testing"),
            IntStringRecord(2, "bar")
        ),
        ListSerializer(IntStringRecord.serializer())
    )

    @Test
    fun testNestedRecordWithHeaderReordered() = Csv(
        CsvConfiguration(
            hasHeaderRecord = true
        )
    ).assertDecode(
        "time,data.location.lon,data.location.lat,data.info,data.speed,name\r\n0,1.0,0.0,info,100,Alice",
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
    fun testNestedRecordListWithHeaderReordered() = Csv(
        CsvConfiguration(
            hasHeaderRecord = true
        )
    ).assertDecode(
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
        ListSerializer(NestedRecord.serializer())
    )
}
