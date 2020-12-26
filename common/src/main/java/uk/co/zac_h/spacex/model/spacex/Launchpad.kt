package uk.co.zac_h.spacex.model.spacex

import android.os.Parcelable
import com.squareup.moshi.Json
import kotlinx.android.parcel.Parcelize
import uk.co.zac_h.spacex.utils.*

data class LaunchpadDocsModel(
    @field:Json(name = "docs") val docs: List<LaunchpadQueriedResponse>
)

data class LaunchpadResponse(
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
    @field:Json(name = SPACEX_FIELD_LAUNCHPAD_ROCKETS) val rockets: List<String>?,
    @field:Json(name = SPACEX_FIELD_LAUNCHPAD_LAUNCHES) val launches: List<String>?,
    @field:Json(name = SPACEX_FIELD_ID) var id: String
)

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
    @field:Json(name = SPACEX_FIELD_LAUNCHPAD_LAUNCHES) val launches: List<LaunchResponse>?,
    @field:Json(name = SPACEX_FIELD_ID) var id: String
)

@Parcelize
data class Launchpad(
    val name: String?,
    val fullName: String?,
    val status: String?,
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
) : Parcelable {

    constructor(
        response: LaunchpadResponse
    ) : this(
        name = response.name,
        fullName = response.fullName,
        status = response.status,
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
        status = response.status,
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

}