package uk.co.zac_h.spacex.feature.starlink.globe

import uk.co.zac_h.spacex.feature.starlink.orbit.SatelliteFix

/**
 * Everything the globe draws at one simulated moment.
 *
 * @param positions scene positions (Earth radii) of every satellite, as x, y, z triples.
 * @param sun unit vector towards the Sun in the scene frame.
 * @param selected index of the highlighted satellite, or -1.
 * @param orbit scene positions along the highlighted satellite's orbit, as a line strip.
 * @param details where the highlighted satellite is, for the details card.
 */
class SatelliteFrame(
    val timeMillis: Long,
    val positions: FloatArray,
    val sun: FloatArray,
    val selected: Int = -1,
    val orbit: FloatArray? = null,
    val details: SatelliteFix? = null
) {
    val count: Int get() = positions.size / 3
}
