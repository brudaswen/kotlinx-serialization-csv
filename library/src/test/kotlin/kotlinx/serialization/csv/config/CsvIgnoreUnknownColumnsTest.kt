package kotlinx.serialization.csv.config

import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.builtins.ListSerializer
import kotlinx.serialization.csv.Csv
import kotlinx.serialization.csv.records.Data
import kotlinx.serialization.csv.records.IntStringRecord
import kotlinx.serialization.csv.records.Location
import kotlinx.serialization.csv.records.NestedRecord
import kotlinx.serialization.test.assertDecode
import kotlinx.serialization.test.assertDecodeFails
import kotlin.test.Test

@OptIn(ExperimentalSerializationApi::class)
internal class CsvIgnoreUnknownColumnsTest {

    @Test
    fun testMultipleColumns() = Csv {
        hasHeaderRecord = true
        ignoreUnknownColumns = true
    }.assertDecode(
        "a,b,IGNORED\n1,testing,ignored",
        IntStringRecord(1, "testing"),
        IntStringRecord.serializer()
    )

    @Test
    fun testMultipleColumns_failure() = Csv {
        hasHeaderRecord = true
    }.assertDecodeFails(
        "a,b,IGNORED\n1,testing,ignored",
        IntStringRecord.serializer()
    )

    @Test
    fun testMultipleColumnsReordered() = Csv {
        hasHeaderRecord = true
        ignoreUnknownColumns = true
    }.assertDecode(
        "IGNORED,b,a\nignored,testing,1",
        IntStringRecord(1, "testing"),
        IntStringRecord.serializer()
    )

    @Test
    fun testMultipleColumnsReordered_failure() = Csv {
        hasHeaderRecord = true
    }.assertDecodeFails(
        "IGNORED,b,a\nignored,testing,1",
        IntStringRecord.serializer()
    )

    @Test
    fun testNestedRecordListWithHeaderReordered() = Csv {
        hasHeaderRecord = true
        ignoreUnknownColumns = true
    }.assertDecode(
        """IGNORED,time,name,data.location.lon,data.location.IGNORED,data.location.lat,data.speed,data.info,IGNORED
          |IGNORED,0,Alice,1.0,IGNORED,0.0,100,info,IGNORED
          |IGNORED,1,Bob,20.0,IGNORED,10.0,50,info2,IGNORED
          |""".trimMargin(),
        listOf(
            NestedRecord(
                time = 0,
                name = "Alice",
                data = Data(
                    location = Location(
                        lat = 0.0,
                        lon = 1.0
                    ),
                    speed = 100,
                    info = "info"
                )
            ),
            NestedRecord(
                time = 1,
                name = "Bob",
                data = Data(
                    location = Location(
                        lat = 10.0,
                        lon = 20.0
                    ),
                    speed = 50,
                    info = "info2"
                )
            )
        ),
        ListSerializer(NestedRecord.serializer())
    )
}
