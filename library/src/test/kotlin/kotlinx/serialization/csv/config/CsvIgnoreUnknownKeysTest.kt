package kotlinx.serialization.csv.config

import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.builtins.ListSerializer
import kotlinx.serialization.csv.Csv
import kotlinx.serialization.csv.CsvConfiguration
import kotlinx.serialization.csv.records.Data
import kotlinx.serialization.csv.records.IntStringRecord
import kotlinx.serialization.csv.records.Location
import kotlinx.serialization.csv.records.NestedRecord
import kotlinx.serialization.test.assertParse
import kotlinx.serialization.test.assertParseFails
import kotlin.test.Test

@OptIn(ExperimentalSerializationApi::class)
internal class CsvIgnoreUnknownKeysTest {

    @Test
    fun testMultipleColumns() = assertParse(
        "a,b,IGNORED\r\n1,testing,ignored",
        IntStringRecord(1, "testing"),
        IntStringRecord.serializer(),
        Csv(
            CsvConfiguration(
                hasHeaderRecord = true,
                ignoreUnknownColumns = true
            )
        )
    )

    @Test
    fun testMultipleColumns_failure() = assertParseFails(
        "a,b,IGNORED\r\n1,testing,ignored",
        IntStringRecord.serializer(),
        Csv(
            CsvConfiguration(
                hasHeaderRecord = true
            )
        )
    )

    @Test
    fun testMultipleColumnsReordered() = assertParse(
        "IGNORED,b,a\r\nignored,testing,1",
        IntStringRecord(1, "testing"),
        IntStringRecord.serializer(),
        Csv(
            CsvConfiguration(
                hasHeaderRecord = true,
                ignoreUnknownColumns = true
            )
        )
    )

    @Test
    fun testMultipleColumnsReordered_failure() = assertParseFails(
        "IGNORED,b,a\r\nignored,testing,1",
        IntStringRecord.serializer(),
        Csv(
            CsvConfiguration(
                hasHeaderRecord = true
            )
        )
    )

    @Test
    fun testNestedRecordListWithHeaderReordered() = assertParse(
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
        ListSerializer(NestedRecord.serializer()),
        Csv(
            CsvConfiguration(
                hasHeaderRecord = true,
                ignoreUnknownColumns = true
            )
        )
    )
}
