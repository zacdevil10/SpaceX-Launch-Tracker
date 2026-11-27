package uk.co.zac_h.spacex.feature.starlink.orbit

import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test
import kotlin.math.sqrt

class FramesTest {

    @Test
    fun `sidereal time matches reference`() {
        // Values from the reference sgp4 package's gstime().
        assertEquals(4.894961212823059, greenwichSiderealTime(946_728_000_000L), 1e-9)
        assertEquals(2.630339211666154, greenwichSiderealTime(1_795_585_564_143L), 1e-9)
    }

    @Test
    fun `scene frame has the north pole up and 90 east towards minus z`() {
        val out = FloatArray(3)
        temeToScene(0.0, 0.0, SCENE_EARTH_RADIUS_KM, 1.0, 0.0, out, 0)
        assertEquals(0f, out[0], 1e-6f); assertEquals(1f, out[1], 1e-6f); assertEquals(0f, out[2], 1e-6f)
        temeToScene(0.0, SCENE_EARTH_RADIUS_KM, 0.0, 1.0, 0.0, out, 0)
        assertEquals(0f, out[0], 1e-6f); assertEquals(0f, out[1], 1e-6f); assertEquals(-1f, out[2], 1e-6f)
    }

    @Test
    fun `earth rotation turns inertial x towards the west`() {
        // After a quarter turn of the Earth, a point fixed in space sits over 90 degrees west.
        val out = FloatArray(3)
        temeToScene(SCENE_EARTH_RADIUS_KM, 0.0, 0.0, 0.0, 1.0, out, 0)
        assertEquals(0f, out[0], 1e-6f); assertEquals(1f, out[2], 1e-6f)
    }

    @Test
    fun `geodetic conversion`() {
        val equator = ecefToGeodetic(6378.137 + 550.0, 0.0, 0.0)
        assertEquals(0.0, equator.latitude, 1e-9)
        assertEquals(0.0, equator.longitude, 1e-9)
        assertEquals(550.0, equator.altitudeKm, 1e-6)

        val pole = ecefToGeodetic(1e-6, 0.0, 6356.752 + 550.0)
        assertEquals(90.0, pole.latitude, 1e-6)
        assertEquals(550.0, pole.altitudeKm, 0.01)

        val east = ecefToGeodetic(0.0, 7000.0, 0.0)
        assertEquals(90.0, east.longitude, 1e-9)
    }

    @Test
    fun `sun is near the december solstice declination in late december`() {
        // 2026-12-21T12:00Z: the Sun sits over about 23.4 S.
        val sun = sunDirectionScene(1_797_854_400_000L)
        val length = sqrt(sun[0] * sun[0] + sun[1] * sun[1] + sun[2] * sun[2])
        assertEquals(1f, length, 1e-5f)
        val declination = Math.toDegrees(kotlin.math.asin(sun[1].toDouble()))
        assertEquals(-23.4, declination, 0.2)
        // Near noon UTC the Sun is over roughly longitude 0 (scene +x).
        assertTrue(sun[0] > 0.9f)
    }
}
