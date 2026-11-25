package uk.co.zac_h.spacex.feature.starlink.orbit

import kotlin.math.cos
import kotlin.math.sin
import kotlin.math.sqrt

/** One satellite to propagate. */
data class Satellite(val noradId: Int, val name: String, val elements: OrbitalElements)

/** Where one satellite is at a moment, for the details card. */
data class SatelliteFix(
    val noradId: Int,
    val name: String,
    val position: Geodetic,
    /** Inertial speed, km/s. */
    val speedKmPerSecond: Double
)

/**
 * Propagates every satellite in a constellation. Satellites whose elements SGP4 can't use are kept
 * at the origin (inside the globe, so never drawn), so indexes always match [satellites].
 */
class Constellation(val satellites: List<Satellite>) {

    private val propagators: List<Sgp4?> = satellites.map { runCatching { Sgp4(it.elements) }.getOrNull() }

    private val indexById: Map<Int, Int> =
        satellites.withIndex().associate { (index, satellite) -> satellite.noradId to index }

    val size: Int get() = satellites.size

    /** The index of the satellite with catalogue number [noradId], or -1. */
    fun indexOf(noradId: Int): Int = indexById[noradId] ?: -1

    /**
     * Scene positions (Earth radii) of every satellite at [timeMillis], as x, y, z triples.
     * A satellite with no valid orbit then is left at the origin.
     */
    fun scenePositions(timeMillis: Long): FloatArray {
        val out = FloatArray(size * 3)
        val gmst = greenwichSiderealTime(timeMillis)
        val cosGmst = cos(gmst)
        val sinGmst = sin(gmst)
        val state = DoubleArray(6)
        propagators.forEachIndexed { index, sgp4 ->
            if (sgp4 != null && sgp4.propagate(minutesSince(sgp4, timeMillis), state)) {
                temeToScene(state[0], state[1], state[2], cosGmst, sinGmst, out, index * 3)
            }
        }
        return out
    }

    /**
     * One full orbit of the satellite at [index], as scene positions of [points] samples starting
     * at [timeMillis], all turned with the Earth's angle at [timeMillis]. This draws the orbit as
     * the ring it is in space rather than its spiral over the ground.
     */
    fun orbitPath(index: Int, timeMillis: Long, points: Int = 180): FloatArray? {
        val sgp4 = propagators.getOrNull(index) ?: return null
        val periodMinutes = 1440.0 / satellites[index].elements.meanMotion
        val gmst = greenwichSiderealTime(timeMillis)
        val cosGmst = cos(gmst)
        val sinGmst = sin(gmst)
        val start = minutesSince(sgp4, timeMillis)
        val state = DoubleArray(6)
        val out = FloatArray((points + 1) * 3)
        for (i in 0..points) {
            if (!sgp4.propagate(start + periodMinutes * i / points, state)) return null
            temeToScene(state[0], state[1], state[2], cosGmst, sinGmst, out, i * 3)
        }
        return out
    }

    /** Where the satellite at [index] is at [timeMillis], or null if it has no valid orbit. */
    fun fix(index: Int, timeMillis: Long): SatelliteFix? {
        val sgp4 = propagators.getOrNull(index) ?: return null
        val state = DoubleArray(6)
        if (!sgp4.propagate(minutesSince(sgp4, timeMillis), state)) return null
        val gmst = greenwichSiderealTime(timeMillis)
        val ecefX = cos(gmst) * state[0] + sin(gmst) * state[1]
        val ecefY = -sin(gmst) * state[0] + cos(gmst) * state[1]
        val satellite = satellites[index]
        return SatelliteFix(
            noradId = satellite.noradId,
            name = satellite.name,
            position = ecefToGeodetic(ecefX, ecefY, state[2]),
            speedKmPerSecond = sqrt(state[3] * state[3] + state[4] * state[4] + state[5] * state[5])
        )
    }

    private fun minutesSince(sgp4: Sgp4, timeMillis: Long) = (timeMillis - sgp4.epochMillis) / 60_000.0
}
