package uk.co.zac_h.spacex.network.store

import kotlin.time.Duration
import kotlin.time.Duration.Companion.minutes

/**
 * How long each kind of Launch Library data stays fresh in the database before it is fetched
 * again. Launch Library allows only 15 calls per hour per IP, so data that rarely changes is kept
 * much longer than launches.
 */
object Freshness {
    val UPCOMING_LAUNCHES: Duration = 15.minutes
}
