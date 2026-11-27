package uk.co.zac_h.spacex.feature.starlink.orbit

import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

/**
 * Expected states come from the reference `sgp4` Python package (Vallado's C++ SGP4, WGS-72,
 * "improved" mode) for the same elements.
 */
class Sgp4Test {

    private fun elements(
        meanMotion: Double,
        eccentricity: Double,
        inclination: Double,
        raan: Double,
        argOfPerigee: Double,
        meanAnomaly: Double,
        bstar: Double
    ) = OrbitalElements(0L, meanMotion, eccentricity, inclination, raan, argOfPerigee, meanAnomaly, bstar)

    private fun assertState(sgp4: Sgp4, minutes: Double, expected: DoubleArray) {
        val state = DoubleArray(6)
        assertTrue(sgp4.propagate(minutes, state))
        for (i in 0..2) assertEquals("position[$i] at $minutes min", expected[i], state[i], 1e-3)
        for (i in 3..5) assertEquals("velocity[$i] at $minutes min", expected[i], state[i], 1e-6)
    }

    @Test
    fun `starlink shell 1 orbit matches reference`() {
        val sgp4 = Sgp4(elements(15.06, 0.00015, 53.05, 120.5, 90.2, 270.0, 0.00031))
        assertState(sgp4, 0.0, doubleArrayOf(-3521.199786, 5965.783802, 8.109781, -3.919094605, -2.330352127, 6.065899975))
        assertState(sgp4, 90.0, doubleArrayOf(-1975.833999, 6342.337897, -1972.701539, -5.038391679, 0.186794592, 5.666954811))
        assertState(sgp4, 1440.0, doubleArrayOf(-4323.266214, 4889.017086, 2313.285496, -2.325953216, -4.675728569, 5.508965350))
    }

    @Test
    fun `low decaying orbit matches reference`() {
        val sgp4 = Sgp4(elements(15.90, 0.0005, 43.0, 10.0, 200.0, 45.0, 0.002))
        assertState(sgp4, 0.0, doubleArrayOf(-2002.611068, -4854.406095, -4130.696005, 7.309507158, -1.129527241, -2.222563632))
        assertState(sgp4, 90.0, doubleArrayOf(-2232.444958, -4806.164863, -4068.271844, 7.224561877, -1.353791878, -2.371398948))
        assertState(sgp4, 1440.0, doubleArrayOf(-5014.712322, -3377.378516, -2831.326037, 5.087587550, -4.098422207, -4.130170501))
    }

    @Test
    fun `very low orbit uses the simplified drag model and matches reference`() {
        val sgp4 = Sgp4(elements(16.20, 0.0008, 97.6, 300.0, 15.0, 180.0, 0.0008))
        assertState(sgp4, 0.0, doubleArrayOf(-2991.787019, 5638.418405, -1709.323748, 1.867944578, -1.253939778, -7.430724402))
        assertState(sgp4, 90.0, doubleArrayOf(-2872.187004, 5539.525083, -2174.207547, 2.126562077, -1.740720540, -7.260475137))
        assertState(sgp4, 1440.0, doubleArrayOf(645.765585, 618.985572, -6537.232716, 4.039923129, -6.621381133, -0.221107543))
    }

    @Test
    fun `eccentric polar orbit matches reference`() {
        val sgp4 = Sgp4(elements(14.2, 0.02, 97.6, 50.0, 300.0, 10.0, 0.0001))
        assertState(sgp4, 0.0, doubleArrayOf(2393.911745, 3962.450710, -5341.566372, 4.218069032, 4.022542544, 4.841567026))
        assertState(sgp4, 90.0, doubleArrayOf(-867.202571, 424.582712, -7021.381852, 4.831517821, 5.803609486, -0.166929839))
        assertState(sgp4, 1440.0, doubleArrayOf(4495.756968, 4979.317836, 2575.893533, -0.902673230, -2.575517467, 6.941319962))
    }

    @Test
    fun `a satellite long past re-entry has no position`() {
        val sgp4 = Sgp4(elements(16.2, 0.0008, 53.0, 0.0, 0.0, 0.0, 0.01))
        assertFalse(sgp4.propagate(60.0 * 24 * 365, DoubleArray(6)))
    }

    @Test(expected = IllegalArgumentException::class)
    fun `deep space orbits are rejected`() {
        Sgp4(elements(1.0027, 0.0002, 0.05, 0.0, 0.0, 0.0, 0.0))
    }
}
