package uk.co.zac_h.spacex.network.store

import kotlin.time.Duration
import kotlin.time.Duration.Companion.days
import kotlin.time.Duration.Companion.hours
import kotlin.time.Duration.Companion.minutes
import kotlin.time.Duration.Companion.seconds

/**
 * How long each kind of Launch Library data stays fresh in the database before it is fetched
 * again. Launch Library allows only 15 calls per hour per IP, so data that rarely changes is kept
 * much longer than launches.
 */
object Freshness {
    val UPCOMING_LAUNCHES: Duration = 15.minutes

    /** How often to ask for previous launches that changed (newly completed, or corrected). */
    val PREVIOUS_LAUNCHES: Duration = 1.hours

    /** The next launch is kept as current as possible: refetched on open if older than this. */
    val NEXT_LAUNCH: Duration = 1.minutes

    /** While the launches screen is visible, the next launch is refreshed this often... */
    val NEXT_LAUNCH_POLL: Duration = 10.minutes

    /** ...or this often from an hour before its launch time until it leaves upcoming. */
    val NEXT_LAUNCH_POLL_NEAR: Duration = 5.minutes
    val NEXT_LAUNCH_NEAR: Duration = 1.hours

    /** Boosters change after each flight (flight count, status), so they're checked a few times a day. */
    val LAUNCHERS: Duration = 6.hours

    /** Dragon capsules, astronauts and the agency (company, rockets) change rarely. */
    val SPACECRAFT: Duration = 1.days
    val ASTRONAUTS: Duration = 1.days
    val AGENCY: Duration = 1.days

    /** The least time between two taps of the refresh button. */
    val MANUAL_REFRESH_COOLDOWN: Duration = 30.seconds
}
