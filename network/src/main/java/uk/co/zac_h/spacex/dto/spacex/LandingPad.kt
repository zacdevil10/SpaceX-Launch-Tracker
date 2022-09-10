package uk.co.zac_h.spacex.dto.spacex

import com.squareup.moshi.Json
import uk.co.zac_h.spacex.*

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

