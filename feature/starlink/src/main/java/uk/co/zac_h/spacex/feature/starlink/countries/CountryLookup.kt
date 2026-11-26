package uk.co.zac_h.spacex.feature.starlink.countries

import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonArray
import kotlinx.serialization.json.double
import kotlinx.serialization.json.jsonArray
import kotlinx.serialization.json.jsonObject
import kotlinx.serialization.json.jsonPrimitive

/** A country's land borders. [code] is its ISO 3166-1 alpha-2 code where it has one. */
class Country(val code: String, val name: String, internal val polygons: List<Polygon>)

/**
 * One piece of a country: its outline followed by any holes, each ring as longitude, latitude
 * pairs in degrees. The bounding box lets most points be ruled out without walking the rings.
 */
class Polygon(internal val rings: List<DoubleArray>) {
    private val minLon = rings[0].filterIndexed { i, _ -> i % 2 == 0 }.min()
    private val maxLon = rings[0].filterIndexed { i, _ -> i % 2 == 0 }.max()
    private val minLat = rings[0].filterIndexed { i, _ -> i % 2 == 1 }.min()
    private val maxLat = rings[0].filterIndexed { i, _ -> i % 2 == 1 }.max()

    /** Even-odd ray casting over every ring, so points inside a hole are outside. */
    fun contains(latitude: Double, longitude: Double): Boolean {
        if (longitude < minLon || longitude > maxLon || latitude < minLat || latitude > maxLat) return false
        var inside = false
        for (ring in rings) {
            var j = ring.size - 2
            var i = 0
            while (i < ring.size) {
                val xi = ring[i]
                val yi = ring[i + 1]
                val xj = ring[j]
                val yj = ring[j + 1]
                if ((yi > latitude) != (yj > latitude) &&
                    longitude < (xj - xi) * (latitude - yi) / (yj - yi) + xi
                ) {
                    inside = !inside
                }
                j = i
                i += 2
            }
        }
        return inside
    }
}

/**
 * Finds which country is under a point, from Natural Earth's 1:50m country borders (public
 * domain), bundled with the app so it works offline. Territorial waters aren't included, so a
 * satellite just off a coast is over the sea.
 */
class CountryLookup(val countries: List<Country>) {

    fun countryAt(latitude: Double, longitude: Double): Country? =
        countries.firstOrNull { country -> country.polygons.any { it.contains(latitude, longitude) } }

    companion object {
        /**
         * Reads the bundled format: a JSON array of `{"n": name, "c": code, "p": polygons}`, where
         * each polygon is a list of rings and each ring a flat list of longitude, latitude pairs.
         */
        fun parse(json: String): CountryLookup = CountryLookup(
            Json.parseToJsonElement(json).jsonArray.map { element ->
                val country = element.jsonObject
                Country(
                    code = country.getValue("c").jsonPrimitive.content,
                    name = country.getValue("n").jsonPrimitive.content,
                    polygons = country.getValue("p").jsonArray.map { polygon ->
                        Polygon(polygon.jsonArray.map { ring -> (ring as JsonArray).toDoubleArray() })
                    }
                )
            }
        )

        private fun JsonArray.toDoubleArray() = DoubleArray(size) { this[it].jsonPrimitive.double }
    }
}
