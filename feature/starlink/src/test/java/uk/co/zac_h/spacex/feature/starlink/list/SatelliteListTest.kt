package uk.co.zac_h.spacex.feature.starlink.list

import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

class SatelliteListTest {

    private val rows = listOf(
        row(44713, "STARLINK-1007", Shell.Inclined53, "GB", "United Kingdom"),
        row(44714, "STARLINK-1008", Shell.Inclined53, null, null),
        row(48600, "STARLINK-2305", Shell.Polar, "FR", "France"),
        row(51000, "STARLINK-3001", Shell.Inclined70, "GB", "United Kingdom")
    )

    @Test
    fun `shells are told apart by inclination`() {
        assertEquals(Shell.Inclined43, Shell.of(43.0))
        assertEquals(Shell.Inclined53, Shell.of(53.05))
        assertEquals(Shell.Inclined70, Shell.of(70.0))
        assertEquals(Shell.Polar, Shell.of(97.66))
        assertEquals(Shell.Other, Shell.of(60.0))
    }

    @Test
    fun `no filter keeps everything`() {
        assertEquals(rows, rows.filtered(SatelliteFilter()))
        assertFalse(SatelliteFilter().isActive)
    }

    @Test
    fun `query ignores case and punctuation`() {
        assertEquals(listOf(44713), rows.filtered(SatelliteFilter(query = "starlink 1007")).map { it.noradId })
        assertEquals(listOf(44713, 44714), rows.filtered(SatelliteFilter(query = "  100")).map { it.noradId })
    }

    @Test
    fun `query matches the start of the norad id`() {
        assertEquals(listOf(48600), rows.filtered(SatelliteFilter(query = "486")).map { it.noradId })
        assertTrue(rows.filtered(SatelliteFilter(query = "8600")).isEmpty())
    }

    @Test
    fun `shells and country combine`() {
        val filter = SatelliteFilter(shells = setOf(Shell.Inclined53, Shell.Inclined70), countryCode = "GB")
        assertEquals(listOf(44713, 51000), rows.filtered(filter).map { it.noradId })
        assertTrue(filter.isActive)
    }

    @Test
    fun `country counts include countries with nothing over them`() {
        val counts = rows.countryCounts(listOf("GB" to "United Kingdom", "FR" to "France", "DE" to "Germany"))
        assertEquals(
            listOf(
                CountryCount("FR", "France", 1),
                CountryCount("DE", "Germany", 0),
                CountryCount("GB", "United Kingdom", 2)
            ),
            counts
        )
    }

    private fun row(id: Int, name: String, shell: Shell, code: String?, country: String?) =
        SatelliteRow(id, name, shell, fix = null, countryCode = code, countryName = country)
}
