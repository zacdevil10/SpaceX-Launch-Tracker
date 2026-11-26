package uk.co.zac_h.spacex.feature.starlink.list

import uk.co.zac_h.spacex.feature.starlink.orbit.Satellite
import uk.co.zac_h.spacex.feature.starlink.orbit.SatelliteFix

/**
 * Starlink's orbital shells, told apart by inclination. Each shell is a set of orbital planes at
 * one inclination and roughly one height.
 */
enum class Shell(val label: String, private val inclinations: ClosedFloatingPointRange<Double>?) {
    Inclined43("43°", 42.0..44.0),
    Inclined53("53°", 52.5..53.5),
    Inclined70("70°", 69.0..71.0),
    Polar("97.6° polar", 96.5..98.5),
    Other("Other", null);

    companion object {
        fun of(inclination: Double): Shell =
            entries.firstOrNull { it.inclinations?.contains(inclination) == true } ?: Other
    }
}

/** One row of the list: a satellite and where it is now. */
data class SatelliteRow(
    val noradId: Int,
    val name: String,
    val shell: Shell,
    /** Null when the satellite has no valid orbit now (e.g. it has re-entered). */
    val fix: SatelliteFix?,
    /** Country under the satellite, or null over the sea. */
    val countryCode: String?,
    val countryName: String?
)

/** What the list is narrowed to. Empty means everything. */
data class SatelliteFilter(
    val query: String = "",
    val shells: Set<Shell> = emptySet(),
    /** Country code, or null for anywhere. */
    val countryCode: String? = null
) {
    val isActive: Boolean get() = query.isNotBlank() || shells.isNotEmpty() || countryCode != null
}

/**
 * The rows matching [filter], in their original order. The query matches any part of the name, ignoring
 * case and punctuation (so "starlink 1007" finds STARLINK-1007), or the start of the NORAD id.
 */
fun List<SatelliteRow>.filtered(filter: SatelliteFilter): List<SatelliteRow> {
    val query = filter.query.trim().normalised()
    return filter { row ->
        (query.isEmpty() || row.name.normalised().contains(query) || row.noradId.toString().startsWith(query)) &&
            (filter.shells.isEmpty() || row.shell in filter.shells) &&
            (filter.countryCode == null || row.countryCode == filter.countryCode)
    }
}

/** A country with how many satellites are over it now, for the country picker. */
data class CountryCount(val code: String, val name: String, val satellites: Int)

/**
 * Every country in [countries] (code to name) with how many of the rows are over it, in name
 * order, so the picker can offer a country even while nothing is over it.
 */
fun List<SatelliteRow>.countryCounts(countries: List<Pair<String, String>>): List<CountryCount> {
    val counts = mapNotNull { it.countryCode }.groupingBy { it }.eachCount()
    return countries
        .map { (code, name) -> CountryCount(code, name, counts[code] ?: 0) }
        .sortedBy { it.name }
}

internal fun Satellite.shell(): Shell = Shell.of(elements.inclination)

private fun String.normalised(): String = lowercase().filter { it.isLetterOrDigit() }
