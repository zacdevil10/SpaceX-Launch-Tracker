package uk.co.zac_h.spacex.network.store

/** Current time in milliseconds since the epoch. An interface so tests can control time. */
fun interface Clock {
    fun now(): Long
}
