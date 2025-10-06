package kotlinx.serialization.csv.config

import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.builtins.ListSerializer
import kotlinx.serialization.csv.Csv
import kotlinx.serialization.csv.records.Data
import kotlinx.serialization.csv.records.Id
import kotlinx.serialization.csv.records.IdRecord
import kotlinx.serialization.csv.records.IntRecord
import kotlinx.serialization.csv.records.IntStringRecord
import kotlinx.serialization.csv.records.Location
import kotlinx.serialization.csv.records.NestedRecord
import kotlinx.serialization.csv.records.SerialNameRecord
import kotlinx.serialization.test.assertDecode
import kotlinx.serialization.test.assertEncodeAndDecode
import kotlin.test.Test

@OptIn(ExperimentalSerializationApi::class)
class CsvHasHeaderRecordTest {

    @Test
    fun testDefault() = Csv.assertEncodeAndDecode(
        expected = "1",
        original = IntRecord(1),
        serializer = IntRecord.serializer(),
    )

    @Test
    fun testWithoutHeaderRecords() = Csv {
        hasHeaderRecord = false
    }.assertEncodeAndDecode(
        expected = "1",
        original = IntRecord(1),
        serializer = IntRecord.serializer(),
    )

    @Test
    fun testWithHeaderRecords() = Csv {
        hasHeaderRecord = true
    }.assertEncodeAndDecode(
        expected = "a\n1",
        original = IntRecord(1),
        serializer = IntRecord.serializer(),
    )

    @Test
    fun testListWithoutHeaderRecords() = Csv {
        hasHeaderRecord = false
    }.assertEncodeAndDecode(
        expected = "1\n2\n3",
        original = listOf(
            IntRecord(1),
            IntRecord(2),
            IntRecord(3),
        ),
        serializer = ListSerializer(IntRecord.serializer()),
    )

    @Test
    fun testListWithHeaderRecords() = Csv {
        hasHeaderRecord = true
    }.assertEncodeAndDecode(
        expected = "a\n1\n2\n3",
        original = listOf(
            IntRecord(1),
            IntRecord(2),
            IntRecord(3),
        ),
        serializer = ListSerializer(IntRecord.serializer()),
    )

    @Test
    fun testMultipleColumns() = Csv {
        hasHeaderRecord = true
    }.assertEncodeAndDecode(
        expected = "a,b\n1,testing",
        original = IntStringRecord(1, "testing"),
        serializer = IntStringRecord.serializer(),
    )

    @Test
    fun testSerialName() = Csv {
        hasHeaderRecord = true
    }.assertEncodeAndDecode(
        expected = "first,second\n1,2",
        original = SerialNameRecord(1, 2),
        serializer = SerialNameRecord.serializer(),
    )

    @Test
    fun testMultipleColumnsReordered() = Csv {
        hasHeaderRecord = true
    }.assertDecode(
        input = "b,a\ntesting,1",
        expected = IntStringRecord(1, "testing"),
        serializer = IntStringRecord.serializer(),
    )

    @Test
    fun testListMultipleColumnsReordered() = Csv {
        hasHeaderRecord = true
    }.assertDecode(
        input = "b,a\ntesting,1\nbar,2",
        expected = listOf(
            IntStringRecord(1, "testing"),
            IntStringRecord(2, "bar"),
        ),
        serializer = ListSerializer(IntStringRecord.serializer()),
    )

    @Test
    fun testNestedRecordWithHeaderReordered() = Csv {
        hasHeaderRecord = true
    }.assertDecode(
        input = "time,data.location.lon,data.location.lat,data.info,data.speed,name\n0,1.0,0.0,info,100,Alice",
        expected = NestedRecord(
            time = 0,
            name = "Alice",
            data = Data(
                location = Location(
                    lat = 0.0,
                    lon = 1.0,
                ),
                speed = 100,
                info = "info",
            ),
        ),
        serializer = NestedRecord.serializer(),
    )

    @Test
    fun testNestedRecordListWithHeaderReordered() = Csv {
        hasHeaderRecord = true
    }.assertDecode(
        input = "time,name,data.location.lon,data.location.lat,data.speed,data.info\n0,Alice,1.0,0.0,100,info\n1,Bob,20.0,10.0,50,info2",
        expected = listOf(
            NestedRecord(
                time = 0,
                name = "Alice",
                data = Data(
                    location = Location(
                        lat = 0.0,
                        lon = 1.0,
                    ),
                    speed = 100,
                    info = "info",
                ),
            ),
            NestedRecord(
                time = 1,
                name = "Bob",
                data = Data(
                    location = Location(
                        lat = 10.0,
                        lon = 20.0,
                    ),
                    speed = 50,
                    info = "info2",
                ),
            ),
        ),
        serializer = ListSerializer(NestedRecord.serializer()),
    )

    @Test
    fun testValueClassWithHeaderRecords() = Csv {
        hasHeaderRecord = true
    }.assertEncodeAndDecode(
        expected = "value\n42",
        original = Id(42),
        serializer = Id.serializer(),
    )

    @Test
    fun testNestedValueClassWithHeaderRecords() = Csv {
        hasHeaderRecord = true
    }.assertEncodeAndDecode(
        expected = "id,name\n42,Alice",
        original = IdRecord(Id(42), "Alice"),
        serializer = IdRecord.serializer(),
    )
}
