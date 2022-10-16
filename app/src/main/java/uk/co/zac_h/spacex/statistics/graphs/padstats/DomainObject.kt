package uk.co.zac_h.spacex.statistics.graphs.padstats

import uk.co.zac_h.spacex.launches.Launch
import uk.co.zac_h.spacex.network.*
import uk.co.zac_h.spacex.network.dto.spacex.*
import uk.co.zac_h.spacex.vehicles.rockets.Rocket

data class LandingPad(
    val name: String?,
    val fullName: String?,
    val status: PadStatus?,
    val type: String?,
    val locality: String?,
    val region: String?,
    val lat: Float?,
    val lng: Float?,
    val landingAttempts: Int?,
    val landingSuccesses: Int?,
    val wikipedia: String?,
    val details: String?,
    val launchIds: List<String>? = null,
    val launches: List<Launch>? = null,
    val id: String
) {

    constructor(
        response: LaunchResponse.Rocket.LauncherStage.Landing.Location
    ) : this(
        name = response.abbrev,
        fullName = response.name,
        status = null,
        type = null,
        locality = null,
        region = null,
        lat = null,
        lng = null,
        landingAttempts = null,
        landingSuccesses = response.successfulLandings,
        wikipedia = null,
        details = response.description,
        id = response.id.toString()
    )

    constructor(
        response: LandingPadResponse
    ) : this(
        name = response.name,
        fullName = response.fullName,
        status = response.status.toLandingPadStatus(),
        type = response.type,
        locality = response.locality,
        region = response.region,
        lat = response.lat,
        lng = response.lng,
        landingAttempts = response.landingAttempts,
        landingSuccesses = response.landingSuccesses,
        wikipedia = response.wikipedia,
        details = response.details,
        launchIds = response.launches,
        id = response.id
    )

    constructor(
        response: LandingPadQueriedResponse
    ) : this(
        name = response.name,
        fullName = response.fullName,
        status = response.status.toLandingPadStatus(),
        type = response.type,
        locality = response.locality,
        region = response.region,
        lat = response.lat,
        lng = response.lng,
        landingAttempts = response.landingAttempts,
        landingSuccesses = response.landingSuccesses,
        wikipedia = response.wikipedia,
        details = response.details,
        launches = response.launches?.map { Launch(it) },
        id = response.id
    )

    companion object {
        private fun String?.toLandingPadStatus() = when (this) {
            SPACEX_LANDING_PAD_STATUS_ACTIVE -> PadStatus.ACTIVE
            SPACEX_LANDING_PAD_STATUS_INACTIVE -> PadStatus.INACTIVE
            SPACEX_LANDING_PAD_STATUS_RETIRED -> PadStatus.RETIRED
            SPACEX_LANDING_PAD_STATUS_LOST -> PadStatus.LOST
            SPACEX_LANDING_PAD_STATUS_UNDER_CONSTRUCTION -> PadStatus.UNDER_CONSTRUCTION
            else -> PadStatus.UNKNOWN
        }
    }
}

data class Launchpad(
    val name: String?,
    val fullName: String?,
    val status: PadStatus?,
    val locality: String?,
    val region: String?,
    val timezone: String?,
    val lat: Float?,
    val lng: Float?,
    val launchAttempts: Int?,
    val launchSuccesses: Int?,
    val rocketIds: List<String>? = null,
    val rockets: List<Rocket>? = null,
    val launchIds: List<String>? = null,
    val launches: List<Launch>? = null,
    var id: String
) {

    constructor(
        response: LaunchResponse.Pad
    ) : this(
        name = response.name,
        fullName = response.name,
        status = null,
        locality = null,
        region = null,
        timezone = null,
        lat = response.latitude?.toFloat(),
        lng = response.longitude?.toFloat(),
        launchAttempts = null,
        launchSuccesses = null,
        id = response.id.toString()
    )

    constructor(
        response: LaunchpadResponse
    ) : this(
        name = response.name,
        fullName = response.fullName,
        status = response.status.toLaunchpadStatus(),
        locality = response.locality,
        region = response.region,
        timezone = response.timezone,
        lat = response.lat,
        lng = response.lng,
        launchAttempts = response.launchAttempts,
        launchSuccesses = response.launchSuccesses,
        rocketIds = response.rockets,
        launchIds = response.launches,
        id = response.id
    )

    constructor(
        response: LaunchpadQueriedResponse
    ) : this(
        name = response.name,
        fullName = response.fullName,
        status = response.status.toLaunchpadStatus(),
        locality = response.locality,
        region = response.region,
        timezone = response.timezone,
        lat = response.lat,
        lng = response.lng,
        launchAttempts = response.launchAttempts,
        launchSuccesses = response.launchSuccesses,
        rockets = response.rockets?.map { Rocket(it) },
        launches = response.launches?.map { Launch(it) },
        id = response.id
    )

    companion object {
        private fun String?.toLaunchpadStatus() = when (this) {
            SPACEX_LANDING_PAD_STATUS_ACTIVE -> PadStatus.ACTIVE
            SPACEX_LANDING_PAD_STATUS_INACTIVE -> PadStatus.INACTIVE
            SPACEX_LANDING_PAD_STATUS_RETIRED -> PadStatus.RETIRED
            SPACEX_LANDING_PAD_STATUS_LOST -> PadStatus.LOST
            SPACEX_LANDING_PAD_STATUS_UNDER_CONSTRUCTION -> PadStatus.UNDER_CONSTRUCTION
            else -> PadStatus.UNKNOWN
        }
    }
}
