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
        expected = "0,Alice,0.0,1.0,100,info",
        original = NestedRecord(
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
    fun testNestedRecordList() = Csv.assertEncodeAndDecode(
        expected = "0,Alice,0.0,1.0,100,info\n1,Bob,10.0,20.0,50,info2",
        original = listOf(
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
    fun testNestedRecordWithHeader() = Csv {
        hasHeaderRecord = true
    }.assertEncodeAndDecode(
        expected = "time,name,data.location.lat,data.location.lon,data.speed,data.info\n0,Alice,0.0,1.0,100,info",
        original = NestedRecord(
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
    fun testNestedRecordListWithHeader() = Csv {
        hasHeaderRecord = true
    }.assertEncodeAndDecode(
        expected = "time,name,data.location.lat,data.location.lon,data.speed,data.info\n0,Alice,0.0,1.0,100,info\n1,Bob,10.0,20.0,50,info2",
        original = listOf(
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
}
