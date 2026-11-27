package uk.co.zac_h.spacex.feature.starlink.orbit

import kotlin.math.atan2
import kotlin.math.cos
import kotlin.math.sin
import kotlin.math.sqrt

/*
 * Frames used by the globe.
 *
 * TEME: SGP4's output, Earth-centred and (nearly) inertial.
 * ECEF: Earth-centred, Earth-fixed: TEME turned by Greenwich sidereal time about the pole. Polar
 *   motion is ignored (a few metres).
 * Scene: what the renderer draws, in Earth radii, Y up: scene (x, y, z) = ECEF (x, z, -y). The
 *   Earth model sits still in this frame and the satellites move around it.
 */

/** Earth radius used to scale the scene: the globe is drawn as a unit sphere. */
const val SCENE_EARTH_RADIUS_KM = Sgp4.RADIUS_EARTH_KM

/**
 * Writes the scene position (Earth radii) of TEME position [x], [y], [z] (km) into [out] at
 * [offset], for Greenwich sidereal angle with cosine [cosGmst] and sine [sinGmst].
 */
fun temeToScene(
    x: Double,
    y: Double,
    z: Double,
    cosGmst: Double,
    sinGmst: Double,
    out: FloatArray,
    offset: Int
) {
    val ecefX = cosGmst * x + sinGmst * y
    val ecefY = -sinGmst * x + cosGmst * y
    out[offset] = (ecefX / SCENE_EARTH_RADIUS_KM).toFloat()
    out[offset + 1] = (z / SCENE_EARTH_RADIUS_KM).toFloat()
    out[offset + 2] = (-ecefY / SCENE_EARTH_RADIUS_KM).toFloat()
}

/** Latitude and longitude in degrees and height above the WGS-84 ellipsoid in km. */
data class Geodetic(val latitude: Double, val longitude: Double, val altitudeKm: Double)

/** Geodetic position of ECEF point [x], [y], [z] (km), on the WGS-84 ellipsoid. */
fun ecefToGeodetic(x: Double, y: Double, z: Double): Geodetic {
    val a = 6378.137
    val f = 1.0 / 298.257223563
    val e2 = f * (2.0 - f)
    val p = sqrt(x * x + y * y)
    var lat = atan2(z, p * (1.0 - e2))
    var height = 0.0
    repeat(5) {
        val sinLat = sin(lat)
        val n = a / sqrt(1.0 - e2 * sinLat * sinLat)
        height = p / cos(lat) - n
        lat = atan2(z, p * (1.0 - e2 * n / (n + height)))
    }
    return Geodetic(Math.toDegrees(lat), Math.toDegrees(atan2(y, x)), height)
}

/**
 * Unit vector towards the Sun in the scene frame at [epochMillis], from the low-precision solar
 * position in the Astronomical Almanac (good to about 0.01 degrees). Used to light the day side.
 */
fun sunDirectionScene(epochMillis: Long): FloatArray {
    val n = epochMillis / 86_400_000.0 + 2_440_587.5 - 2_451_545.0
    val meanLongitude = Math.toRadians(280.460 + 0.9856474 * n)
    val meanAnomaly = Math.toRadians(357.528 + 0.9856003 * n)
    val eclipticLongitude = meanLongitude + Math.toRadians(1.915) * sin(meanAnomaly) +
        Math.toRadians(0.020) * sin(2.0 * meanAnomaly)
    val obliquity = Math.toRadians(23.439 - 0.0000004 * n)
    val x = cos(eclipticLongitude)
    val y = cos(obliquity) * sin(eclipticLongitude)
    val z = sin(obliquity) * sin(eclipticLongitude)
    val gmst = greenwichSiderealTime(epochMillis)
    val out = FloatArray(3)
    temeToScene(x * SCENE_EARTH_RADIUS_KM, y * SCENE_EARTH_RADIUS_KM, z * SCENE_EARTH_RADIUS_KM, cos(gmst), sin(gmst), out, 0)
    return out
}
