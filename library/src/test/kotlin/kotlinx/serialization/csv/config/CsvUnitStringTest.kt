package kotlinx.serialization.csv.config

import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.csv.Csv
import kotlinx.serialization.csv.records.UnitRecord
import kotlinx.serialization.test.assertStringFormAndRestored
import kotlin.test.Test

/**
 * Test [Csv] with [kotlin.Unit].
 */
@OptIn(ExperimentalSerializationApi::class)
class CsvUnitStringTest {

    @Test
    fun testDefault() = assertStringFormAndRestored(
        "kotlin.Unit",
        UnitRecord(Unit),
        UnitRecord.serializer(),
        Csv
    )
}