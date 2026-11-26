package uk.co.zac_h.spacex.feature.starlink.countries

import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Test

class CountryLookupTest {

    // A square country with a square hole, and a second country made of two islands.
    private val lookup = CountryLookup.parse(
        """
        [
          {"n": "Squareland", "c": "SQ", "p": [[[0,0, 10,0, 10,10, 0,10], [4,4, 6,4, 6,6, 4,6]]]},
          {"n": "Islands", "c": "IS", "p": [[[20,0, 22,0, 22,2, 20,2]], [[-30,-5, -28,-5, -29,-3]]]}
        ]
        """.trimIndent()
    )

    @Test
    fun `finds the country a point is inside`() {
        assertEquals("SQ", lookup.countryAt(latitude = 2.0, longitude = 2.0)?.code)
        assertEquals("Squareland", lookup.countryAt(latitude = 9.0, longitude = 1.0)?.name)
    }

    @Test
    fun `a point in a hole is outside`() {
        assertNull(lookup.countryAt(latitude = 5.0, longitude = 5.0))
    }

    @Test
    fun `any of a country's polygons counts`() {
        assertEquals("IS", lookup.countryAt(latitude = 1.0, longitude = 21.0)?.code)
        assertEquals("IS", lookup.countryAt(latitude = -4.5, longitude = -29.0)?.code)
    }

    @Test
    fun `the sea is no country`() {
        assertNull(lookup.countryAt(latitude = 1.0, longitude = 15.0))
        assertNull(lookup.countryAt(latitude = -50.0, longitude = 100.0))
    }
}
