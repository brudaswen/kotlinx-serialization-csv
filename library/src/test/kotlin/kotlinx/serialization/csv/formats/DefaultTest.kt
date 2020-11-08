package kotlinx.serialization.csv.formats

import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.builtins.ListSerializer
import kotlinx.serialization.csv.Csv
import kotlinx.serialization.csv.records.Location
import kotlinx.serialization.test.assertDecode
import org.junit.jupiter.api.Test

@OptIn(ExperimentalSerializationApi::class)
class DefaultTest {

    @Test
    fun testCsv() = Csv.assertDecode(
        "0.0,1.0\n2.0,3.0",
        listOf(
            Location(0.0, 1.0),
            Location(2.0, 3.0),
        ),
        ListSerializer(Location.serializer())
    )

    @Test
    fun testCsvDefault() = Csv.Default.assertDecode(
        "0.0,1.0\n2.0,3.0",
        listOf(
            Location(0.0, 1.0),
            Location(2.0, 3.0),
        ),
        ListSerializer(Location.serializer())
    )

    @Test
    fun testCsvDefaultWithCustomConfig() = Csv(Csv.Default) {
        hasHeaderRecord = true
    }.assertDecode(
        "lat,lon\n0.0,1.0",
        Location(
            0.0,
            1.0
        ),
        Location.serializer()
    )
}
