package kotlinx.serialization.csv.config

import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.builtins.ListSerializer
import kotlinx.serialization.csv.Csv
import kotlinx.serialization.csv.CsvConfiguration
import kotlinx.serialization.csv.records.Data
import kotlinx.serialization.csv.records.IntStringRecord
import kotlinx.serialization.csv.records.Location
import kotlinx.serialization.csv.records.NestedRecord
import kotlinx.serialization.test.assertDecode
import kotlinx.serialization.test.assertDecodeFails
import kotlin.test.Test

@OptIn(ExperimentalSerializationApi::class)
internal class CsvIgnoreUnknownKeysTest {

    @Test
    fun testMultipleColumns() = Csv(
        CsvConfiguration(
            hasHeaderRecord = true,
            ignoreUnknownColumns = true
        )
    ).assertDecode(
        "a,b,IGNORED\r\n1,testing,ignored",
        IntStringRecord(1, "testing"),
        IntStringRecord.serializer()
    )

    @Test
    fun testMultipleColumns_failure() = Csv(
        CsvConfiguration(
            hasHeaderRecord = true
        )
    ).assertDecodeFails(
        "a,b,IGNORED\r\n1,testing,ignored",
        IntStringRecord.serializer()
    )

    @Test
    fun testMultipleColumnsReordered() = Csv(
        CsvConfiguration(
            hasHeaderRecord = true,
            ignoreUnknownColumns = true
        )
    ).assertDecode(
        "IGNORED,b,a\r\nignored,testing,1",
        IntStringRecord(1, "testing"),
        IntStringRecord.serializer()
    )

    @Test
    fun testMultipleColumnsReordered_failure() = Csv(
        CsvConfiguration(
            hasHeaderRecord = true
        )
    ).assertDecodeFails(
        "IGNORED,b,a\r\nignored,testing,1",
        IntStringRecord.serializer()
    )

    @Test
    fun testNestedRecordListWithHeaderReordered() = Csv(
        CsvConfiguration(
            hasHeaderRecord = true,
            ignoreUnknownColumns = true
        )
    ).assertDecode(
        """IGNORED,time,name,data.location.lon,data.location.IGNORED,data.location.lat,data.speed,data.info,IGNORED
          |IGNORED,0,Alice,1.0,IGNORED,0.0,100,info,IGNORED
          |IGNORED,1,Bob,20.0,IGNORED,10.0,50,info2,IGNORED
          |""".trimMargin().replace("\n", "\r\n"),
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
