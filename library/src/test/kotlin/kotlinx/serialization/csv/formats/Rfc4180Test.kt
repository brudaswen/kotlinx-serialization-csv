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
        "0.0,1.0\r\n\r\n2.0,3.0",
        listOf(
            Location(0.0, 1.0),
            null,
            Location(2.0, 3.0),
        ),
        ListSerializer(Location.serializer().nullable)
    )

    @Test
    fun testRfc4180WithCustomConfig() = Csv.Rfc4180.configure {
        hasHeaderRecord = true
    }.assertDecode(
        "lat,lon\r\n0.0,1.0",
        Location(
            0.0,
            1.0
        ),
        Location.serializer()
    )
}
