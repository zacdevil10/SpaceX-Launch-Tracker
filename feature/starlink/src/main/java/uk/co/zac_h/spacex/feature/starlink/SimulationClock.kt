package uk.co.zac_h.spacex.feature.starlink

/**
 * Simulated time for the globe, which can run faster than real time. It was [anchorSimMillis] at
 * real time [anchorRealMillis] and runs [speed] times faster from there.
 */
data class SimulationClock(
    val anchorSimMillis: Long,
    val anchorRealMillis: Long,
    val speed: Int = 1
) {
    fun timeAt(realMillis: Long): Long = anchorSimMillis + (realMillis - anchorRealMillis) * speed

    /** The same simulated moment now, running at [newSpeed] from here. */
    fun withSpeed(newSpeed: Int, realMillis: Long) =
        SimulationClock(timeAt(realMillis), realMillis, newSpeed)

    /** True when simulated time matches real time. */
    fun isLive(realMillis: Long): Boolean =
        speed == 1 && kotlin.math.abs(timeAt(realMillis) - realMillis) < 1_000

    /**
     * How often to work out new positions. In real time a satellite moves about 4 km in half a
     * second, under a pixel; sped up, positions are updated often enough to look smooth.
     */
    val updateIntervalMillis: Long get() = if (speed == 1) 500 else 50

    companion object {
        val SPEEDS = listOf(1, 60, 600)

        fun live(realMillis: Long) = SimulationClock(realMillis, realMillis)
    }
}
