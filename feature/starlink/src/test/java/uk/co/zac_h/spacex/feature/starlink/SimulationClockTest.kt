package uk.co.zac_h.spacex.feature.starlink

import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

class SimulationClockTest {

    @Test
    fun `live clock follows real time`() {
        val clock = SimulationClock.live(1_000L)
        assertEquals(61_000L, clock.timeAt(61_000L))
        assertTrue(clock.isLive(61_000L))
    }

    @Test
    fun `changing speed keeps the current simulated moment`() {
        val fast = SimulationClock.live(0L).withSpeed(60, 10_000L)
        assertEquals(10_000L, fast.timeAt(10_000L))
        assertEquals(10_000L + 60_000L, fast.timeAt(11_000L))
        assertFalse(fast.isLive(11_000L))

        // Slowing back to 1x stays ahead of real time until going live again.
        val slow = fast.withSpeed(1, 11_000L)
        assertEquals(70_000L, slow.timeAt(11_000L))
        assertFalse(slow.isLive(11_000L))
    }
}
