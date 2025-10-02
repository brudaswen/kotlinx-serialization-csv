package kotlinx.serialization.csv.formats

import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.builtins.ListSerializer
import kotlinx.serialization.csv.Csv
import kotlinx.serialization.csv.configure
import kotlinx.serialization.csv.records.Location
import kotlinx.serialization.test.assertDecode
import kotlin.test.Test

@OptIn(ExperimentalSerializationApi::class)
class DefaultTest {

    @Test
    fun testCsv() = Csv.assertDecode(
        input = "0.0,1.0\n2.0,3.0",
        expected = listOf(
            Location(lat = 0.0, lon = 1.0),
            Location(lat = 2.0, lon = 3.0),
        ),
        serializer = ListSerializer(Location.serializer()),
    )

    @Test
    fun testCsvDefault() = Csv.Default.assertDecode(
        input = "0.0,1.0\n2.0,3.0",
        expected = listOf(
            Location(lat = 0.0, lon = 1.0),
            Location(lat = 2.0, lon = 3.0),
        ),
        serializer = ListSerializer(Location.serializer()),
    )

    @Test
    fun testCsvDefaultWithCustomConfig() = Csv.configure {
        hasHeaderRecord = true
    }.assertDecode(
        input = "lat,lon\n0.0,1.0",
        expected = Location(lat = 0.0, lon = 1.0),
        serializer = Location.serializer(),
    )
}
