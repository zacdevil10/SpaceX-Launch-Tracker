package uk.co.zac_h.spacex.feature.starlink.orbit

import kotlin.math.PI

/**
 * Greenwich mean sidereal time, in radians, at [epochMillis] (UTC, used in place of UT1: the
 * difference is under a second). IAU-82 model, as used with SGP4.
 */
fun greenwichSiderealTime(epochMillis: Long): Double {
    val julianDate = epochMillis / 86_400_000.0 + 2_440_587.5
    val tut1 = (julianDate - 2_451_545.0) / 36_525.0
    val seconds = -6.2e-6 * tut1 * tut1 * tut1 + 0.093104 * tut1 * tut1 +
        (876_600.0 * 3600.0 + 8_640_184.812866) * tut1 + 67_310.54841
    val radians = (Math.toRadians(seconds / 240.0)) % (2.0 * PI)
    return if (radians < 0.0) radians + 2.0 * PI else radians
}
