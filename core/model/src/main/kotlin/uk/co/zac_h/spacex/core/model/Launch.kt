package uk.co.zac_h.spacex.core.model

import java.time.Duration
import java.time.Instant

/**
 * One launch, upcoming or previous. Values are as Launch Library gives them, not formatted for
 * display: each form factor formats dates, units and titles in its own UI.
 */
data class Launch(
    val id: String,
    /** The mission name, or the launch name when there is no mission. */
    val name: String,
    /** No earlier than: the launch time, or null if Launch Library has none. */
    val net: Instant?,
    val status: LaunchStatus,
    /** The rocket's configuration name, such as "Falcon 9". */
    val rocket: String?,
    val missionPatchUrl: String?,
    val imageUrl: String?,
    val pad: Pad?,
    val mission: Mission?,
    val failReason: String?,
    val webcasts: List<Webcast>,
    val webcastLive: Boolean,
    val firstStages: List<FirstStage>,
    val crew: List<CrewMember>,
    /** When Launch Library last changed this launch. */
    val lastUpdated: Instant?,
) {
    val isUpcoming: Boolean get() = status.isUpcoming
}

data class LaunchStatus(
    val id: Int?,
    val name: String?,
    val abbreviation: String?,
    val description: String?,
) {
    /** Go, TBD, hold, in flight or TBC: still to happen, or happening now. */
    val isUpcoming: Boolean get() = id in UPCOMING_IDS

    private companion object {
        val UPCOMING_IDS = setOf(1, 2, 5, 6, 8)
    }
}

data class Pad(
    val name: String?,
    val mapImageUrl: String?,
    val mapUrl: String?,
)

data class Mission(
    val name: String?,
    val description: String?,
    val type: String?,
    val orbit: String?,
)

/** A video link for a launch, usually a YouTube or X stream. */
data class Webcast(
    val url: String,
    val title: String?,
    /** The kind of video, such as "Official Webcast". */
    val type: String?,
    val description: String?,
    val imageUrl: String?,
    /** The site's own name, if Launch Library has one. */
    val source: String?,
    /** Lower is better; Launch Library ranks duplicates of the same stream by this. */
    val priority: Int?,
)

data class FirstStage(
    val id: Int,
    val serial: String?,
    val type: CoreType,
    val reused: Boolean,
    /** Flights this booster had flown, including this one. */
    val totalFlights: Int?,
    val launcherDetails: String?,
    val landing: Landing?,
    val previousFlight: String?,
    /** Time since this booster's previous flight. */
    val turnAroundTime: Duration?,
)

data class Landing(
    val attempt: Boolean,
    val description: String?,
    /** Short landing type, such as "ASDS" or "RTLS". */
    val type: String?,
    /** Short landing location, such as "OCISLY". */
    val location: String?,
    val locationName: String?,
)

data class CrewMember(
    val id: Int,
    val role: String?,
    val astronaut: Astronaut,
)
