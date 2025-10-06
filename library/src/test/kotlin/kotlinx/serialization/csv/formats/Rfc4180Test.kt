package kotlinx.serialization.csv.formats

import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.builtins.ListSerializer
import kotlinx.serialization.builtins.nullable
import kotlinx.serialization.csv.Csv
import kotlinx.serialization.csv.configure
import kotlinx.serialization.csv.records.Location
import kotlinx.serialization.test.assertDecode
import kotlin.test.Test

@OptIn(ExperimentalSerializationApi::class)
class Rfc4180Test {

    @Test
    fun testRfc4180() = Csv.Rfc4180.assertDecode(
        input = "0.0,1.0\r\n\r\n2.0,3.0",
        expected = listOf(
            Location(lat = 0.0, lon = 1.0),
            null,
            Location(lat = 2.0, lon = 3.0),
        ),
        serializer = ListSerializer(Location.serializer().nullable),
    )

    @Test
    fun testRfc4180WithCustomConfig() = Csv.Rfc4180.configure {
        hasHeaderRecord = true
    }.assertDecode(
        input = "lat,lon\r\n0.0,1.0",
        expected = Location(lat = 0.0, lon = 1.0),
        serializer = Location.serializer(),
    )
}
