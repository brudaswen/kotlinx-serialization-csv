package kotlinx.serialization.csv.config

import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.builtins.ListSerializer
import kotlinx.serialization.csv.Csv
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
    fun testWithoutHeaderRecords() = Csv {
        hasHeaderRecord = false
    }.assertEncodeAndDecode(
        "1",
        IntRecord(1),
        IntRecord.serializer()
    )

    @Test
    fun testWithHeaderRecords() = Csv {
        hasHeaderRecord = true
    }.assertEncodeAndDecode(
        "a\n1",
        IntRecord(1),
        IntRecord.serializer()
    )

    @Test
    fun testListWithoutHeaderRecords() = Csv {
        hasHeaderRecord = false
    }.assertEncodeAndDecode(
        "1\n2\n3",
        listOf(
            IntRecord(1),
            IntRecord(2),
            IntRecord(3)
        ),
        ListSerializer(IntRecord.serializer())
    )

    @Test
    fun testListWithHeaderRecords() = Csv {
        hasHeaderRecord = true
    }.assertEncodeAndDecode(
        "a\n1\n2\n3",
        listOf(
            IntRecord(1),
            IntRecord(2),
            IntRecord(3)
        ),
        ListSerializer(IntRecord.serializer())
    )

    @Test
    fun testMultipleColumns() = Csv {
        hasHeaderRecord = true
    }.assertEncodeAndDecode(
        "a,b\n1,testing",
        IntStringRecord(1, "testing"),
        IntStringRecord.serializer()
    )

    @Test
    fun testSerialName() = Csv {
        hasHeaderRecord = true
    }.assertEncodeAndDecode(
        "first,second\n1,2",
        SerialNameRecord(1, 2),
        SerialNameRecord.serializer()
    )

    @Test
    fun testMultipleColumnsReordered() = Csv {
        hasHeaderRecord = true
    }.assertDecode(
        "b,a\ntesting,1",
        IntStringRecord(1, "testing"),
        IntStringRecord.serializer()
    )

    @Test
    fun testListMultipleColumnsReordered() = Csv {
        hasHeaderRecord = true
    }.assertDecode(
        "b,a\ntesting,1\nbar,2",
        listOf(
            IntStringRecord(1, "testing"),
            IntStringRecord(2, "bar")
        ),
        ListSerializer(IntStringRecord.serializer())
    )

    @Test
    fun testNestedRecordWithHeaderReordered() = Csv {
        hasHeaderRecord = true
    }.assertDecode(
        "time,data.location.lon,data.location.lat,data.info,data.speed,name\n0,1.0,0.0,info,100,Alice",
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
    fun testNestedRecordListWithHeaderReordered() = Csv {
        hasHeaderRecord = true
    }.assertDecode(
        "time,name,data.location.lon,data.location.lat,data.speed,data.info\n0,Alice,1.0,0.0,100,info\n1,Bob,20.0,10.0,50,info2",
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
