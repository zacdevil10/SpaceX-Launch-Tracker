package uk.co.zac_h.spacex.network.dto.spacex

import com.squareup.moshi.Json
import uk.co.zac_h.spacex.network.SPACEX_FIELD_ID
import uk.co.zac_h.spacex.network.SPACEX_FIELD_LAUNCHPAD_FULL_NAME
import uk.co.zac_h.spacex.network.SPACEX_FIELD_LAUNCHPAD_LAT
import uk.co.zac_h.spacex.network.SPACEX_FIELD_LAUNCHPAD_LAUNCHES
import uk.co.zac_h.spacex.network.SPACEX_FIELD_LAUNCHPAD_LAUNCH_ATTEMPTS
import uk.co.zac_h.spacex.network.SPACEX_FIELD_LAUNCHPAD_LAUNCH_SUCCESSES
import uk.co.zac_h.spacex.network.SPACEX_FIELD_LAUNCHPAD_LNG
import uk.co.zac_h.spacex.network.SPACEX_FIELD_LAUNCHPAD_LOCALITY
import uk.co.zac_h.spacex.network.SPACEX_FIELD_LAUNCHPAD_NAME
import uk.co.zac_h.spacex.network.SPACEX_FIELD_LAUNCHPAD_REGION
import uk.co.zac_h.spacex.network.SPACEX_FIELD_LAUNCHPAD_ROCKETS
import uk.co.zac_h.spacex.network.SPACEX_FIELD_LAUNCHPAD_STATUS
import uk.co.zac_h.spacex.network.SPACEX_FIELD_LAUNCHPAD_TIMEZONE

data class LaunchpadQueriedResponse(
    @field:Json(name = SPACEX_FIELD_LAUNCHPAD_NAME) val name: String?,
    @field:Json(name = SPACEX_FIELD_LAUNCHPAD_FULL_NAME) val fullName: String?,
    @field:Json(name = SPACEX_FIELD_LAUNCHPAD_STATUS) val status: String?,
    @field:Json(name = SPACEX_FIELD_LAUNCHPAD_LOCALITY) val locality: String?,
    @field:Json(name = SPACEX_FIELD_LAUNCHPAD_REGION) val region: String?,
    @field:Json(name = SPACEX_FIELD_LAUNCHPAD_TIMEZONE) val timezone: String?,
    @field:Json(name = SPACEX_FIELD_LAUNCHPAD_LAT) val lat: Float?,
    @field:Json(name = SPACEX_FIELD_LAUNCHPAD_LNG) val lng: Float?,
    @field:Json(name = SPACEX_FIELD_LAUNCHPAD_LAUNCH_ATTEMPTS) val launchAttempts: Int?,
    @field:Json(name = SPACEX_FIELD_LAUNCHPAD_LAUNCH_SUCCESSES) val launchSuccesses: Int?,
    @field:Json(name = SPACEX_FIELD_LAUNCHPAD_ROCKETS) val rockets: List<RocketResponse>?,
    @field:Json(name = SPACEX_FIELD_LAUNCHPAD_LAUNCHES) val launches: List<LegacyLaunchResponse>?,
    @field:Json(name = SPACEX_FIELD_ID) var id: String
)
