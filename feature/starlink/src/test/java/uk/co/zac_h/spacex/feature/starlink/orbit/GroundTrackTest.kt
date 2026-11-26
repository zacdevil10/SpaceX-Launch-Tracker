package uk.co.zac_h.spacex.feature.starlink.orbit

import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test
import kotlin.math.abs

class GroundTrackTest {

    // A typical 53° shell satellite, epoch 1 Jan 2025 00:00 UTC.
    private val elements = OrbitalElements(
        epochMillis = 1_735_689_600_000L,
        meanMotion = 15.06,
        eccentricity = 0.0001,
        inclination = 53.05,
        raan = 120.0,
        argOfPerigee = 90.0,
        meanAnomaly = 10.0,
        bstar = 0.0002
    )

    @Test
    fun `shape matches a 550 km circular orbit`() {
        val shape = elements.shape()
        assertEquals(95.6, shape.periodMinutes, 0.1)
        assertEquals(550.0, shape.perigeeKm, 10.0)
        assertTrue(shape.apogeeKm >= shape.perigeeKm)
        assertTrue(shape.apogeeKm - shape.perigeeKm < 5.0)
    }

    @Test
    fun `ground track spans the requested window and stays within the inclination`() {
        val constellation = Constellation(listOf(Satellite(1, "TEST", elements)))
        val now = elements.epochMillis + 3_600_000L
        val track = constellation.groundTrack(0, now, minutesBefore = 10.0, minutesAfter = 20.0, stepMinutes = 0.5)

        assertEquals(61, track.size)
        assertEquals(-10.0, track.first().minutesFromNow, 1e-9)
        assertEquals(20.0, track.last().minutesFromNow, 1e-9)
        assertTrue(track.all { abs(it.latitude) <= 53.5 && it.longitude in -180.0..180.0 })
        assertTrue(track.all { it.altitudeKm in 520.0..600.0 })

        // The point at "now" is where the satellite is now.
        val fix = constellation.fix(0, now)!!
        val current = track.first { it.minutesFromNow == 0.0 }
        assertEquals(fix.position.latitude, current.latitude, 1e-6)
        assertEquals(fix.position.longitude, current.longitude, 1e-6)
    }
}
