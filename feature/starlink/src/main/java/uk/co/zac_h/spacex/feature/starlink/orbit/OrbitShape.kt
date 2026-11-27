package uk.co.zac_h.spacex.feature.starlink.orbit

import kotlin.math.PI
import kotlin.math.cbrt

/** Size and period of an orbit, from its mean elements. */
data class OrbitShape(
    val periodMinutes: Double,
    /** Lowest and highest points above the equatorial radius, in km. */
    val perigeeKm: Double,
    val apogeeKm: Double
)

fun OrbitalElements.shape(): OrbitShape {
    val radiansPerSecond = meanMotion * 2.0 * PI / 86_400.0
    val semiMajorAxis = cbrt(MU_KM3_S2 / (radiansPerSecond * radiansPerSecond))
    return OrbitShape(
        periodMinutes = 1440.0 / meanMotion,
        perigeeKm = semiMajorAxis * (1.0 - eccentricity) - EQUATORIAL_RADIUS_KM,
        apogeeKm = semiMajorAxis * (1.0 + eccentricity) - EQUATORIAL_RADIUS_KM
    )
}

private const val MU_KM3_S2 = 398_600.4418
private const val EQUATORIAL_RADIUS_KM = 6378.137
