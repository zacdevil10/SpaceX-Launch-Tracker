package uk.co.zac_h.spacex.model.spacex

import android.os.Parcelable
import com.squareup.moshi.Json
import kotlinx.android.parcel.Parcelize
import uk.co.zac_h.spacex.utils.*

data class LandingPadDocsModel(
    @field:Json(name = "docs") val docs: List<LandingPadQueriedResponse>
)

data class LandingPadResponse(
    @field:Json(name = SPACEX_FIELD_LANDING_PAD_NAME) val name: String?,
    @field:Json(name = SPACEX_FIELD_LANDING_PAD_FULL_NAME) val fullName: String?,
    @field:Json(name = SPACEX_FIELD_LANDING_PAD_STATUS) val status: String,
    @field:Json(name = SPACEX_FIELD_LANDING_PAD_TYPE) val type: String?,
    @field:Json(name = SPACEX_FIELD_LANDING_PAD_LOCALITY) val locality: String?,
    @field:Json(name = SPACEX_FIELD_LANDING_PAD_REGION) val region: String?,
    @field:Json(name = SPACEX_FIELD_LANDING_PAD_LATITUDE) val lat: Float?,
    @field:Json(name = SPACEX_FIELD_LANDING_PAD_LONGITUDE) val lng: Float?,
    @field:Json(name = SPACEX_FIELD_LANDING_PAD_LANDING_ATTEMPTS) val landingAttempts: Int?,
    @field:Json(name = SPACEX_FIELD_LANDING_PAD_LANDING_SUCCESS) val landingSuccesses: Int?,
    @field:Json(name = SPACEX_FIELD_LANDING_PAD_WIKI) val wikipedia: String?,
    @field:Json(name = SPACEX_FIELD_LANDING_PAD_DETAILS) val details: String?,
    @field:Json(name = SPACEX_FIELD_LANDING_PAD_LAUNCHES) val launches: List<String>?,
    @field:Json(name = SPACEX_FIELD_ID) val id: String
)

data class LandingPadQueriedResponse(
    @field:Json(name = SPACEX_FIELD_LANDING_PAD_NAME) val name: String?,
    @field:Json(name = SPACEX_FIELD_LANDING_PAD_FULL_NAME) val fullName: String?,
    @field:Json(name = SPACEX_FIELD_LANDING_PAD_STATUS) val status: String?,
    @field:Json(name = SPACEX_FIELD_LANDING_PAD_TYPE) val type: String?,
    @field:Json(name = SPACEX_FIELD_LANDING_PAD_LOCALITY) val locality: String?,
    @field:Json(name = SPACEX_FIELD_LANDING_PAD_REGION) val region: String?,
    @field:Json(name = SPACEX_FIELD_LANDING_PAD_LATITUDE) val lat: Float?,
    @field:Json(name = SPACEX_FIELD_LANDING_PAD_LONGITUDE) val lng: Float?,
    @field:Json(name = SPACEX_FIELD_LANDING_PAD_LANDING_ATTEMPTS) val landingAttempts: Int?,
    @field:Json(name = SPACEX_FIELD_LANDING_PAD_LANDING_SUCCESS) val landingSuccesses: Int?,
    @field:Json(name = SPACEX_FIELD_LANDING_PAD_WIKI) val wikipedia: String?,
    @field:Json(name = SPACEX_FIELD_LANDING_PAD_DETAILS) val details: String?,
    @field:Json(name = SPACEX_FIELD_LANDING_PAD_LAUNCHES) val launches: List<LaunchResponse>?,
    @field:Json(name = SPACEX_FIELD_ID) val id: String
)

@Parcelize
data class LandingPad(
    val name: String?,
    val fullName: String?,
    val status: LandingPadStatus?,
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
) : Parcelable {

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
            SPACEX_LANDING_PAD_STATUS_ACTIVE -> LandingPadStatus.ACTIVE
            SPACEX_LANDING_PAD_STATUS_INACTIVE -> LandingPadStatus.INACTIVE
            SPACEX_LANDING_PAD_STATUS_RETIRED -> LandingPadStatus.RETIRED
            SPACEX_LANDING_PAD_STATUS_LOST -> LandingPadStatus.LOST
            SPACEX_LANDING_PAD_STATUS_UNDER_CONSTRUCTION -> LandingPadStatus.UNDER_CONSTRUCTION
            else -> LandingPadStatus.UNKNOWN
        }
    }
}

enum class LandingPadStatus(val status: String) {
    ACTIVE(SPACEX_ACTIVE),
    INACTIVE(SPACEX_INACTIVE),
    UNKNOWN(SPACEX_UNKNOWN),
    RETIRED(SPACEX_RETIRED),
    LOST(SPACEX_LOST),
    UNDER_CONSTRUCTION(SPACEX_UNDER_CONSTRUCTION)
}