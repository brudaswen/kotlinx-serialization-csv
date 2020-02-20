package kotlinx.serialization.csv.config

import kotlinx.serialization.csv.Csv
import kotlinx.serialization.csv.CsvConfiguration
import kotlinx.serialization.csv.records.UnitRecord
import kotlinx.serialization.test.assertStringFormAndRestored
import kotlin.test.Test

/**
 * Test [Csv] with different [CsvConfiguration.unitString]s.
 */
class CsvUnitStringTest {

    @Test
    fun testDefault() = assertStringFormAndRestored(
        "kotlin.Unit",
        UnitRecord(Unit),
        UnitRecord.serializer(),
        Csv
    )

    @Test
    fun testEmpty() = assertStringFormAndRestored(
        "",
        UnitRecord(Unit),
        UnitRecord.serializer(),
        Csv(
            CsvConfiguration(
                unitString = "",
                nullString = "null"
            )
        )
    )

    @Test
    fun testUnit() = assertStringFormAndRestored(
        "Unit",
        UnitRecord(Unit),
        UnitRecord.serializer(),
        Csv(
            CsvConfiguration(
                unitString = "Unit"
            )
        )
    )
}