package uk.co.zac_h.spacex.dto.spacex

import com.squareup.moshi.Json
import uk.co.zac_h.spacex.*

data class CrewDocsModel(
    @field:Json(name = "docs") val docs: List<CrewQueriedResponse>
)

data class CrewResponse(
    @field:Json(name = SPACEX_FIELD_CREW_NAME) val name: String?,
    @field:Json(name = SPACEX_FIELD_CREW_STATUS) val status: String,
    @field:Json(name = SPACEX_FIELD_CREW_AGENCY) val agency: String?,
    @field:Json(name = SPACEX_FIELD_CREW_IMAGE) val image: String?,
    @field:Json(name = SPACEX_FIELD_CREW_WIKI) val wikipedia: String?,
    @field:Json(name = SPACEX_FIELD_CREW_LAUNCHES) val launches: List<String>?,
    @field:Json(name = SPACEX_FIELD_ID) val id: String
)

data class CrewQueriedResponse(
    @field:Json(name = SPACEX_FIELD_CREW_NAME) val name: String?,
    @field:Json(name = SPACEX_FIELD_CREW_STATUS) val status: String?,
    @field:Json(name = SPACEX_FIELD_CREW_AGENCY) val agency: String?,
    @field:Json(name = SPACEX_FIELD_CREW_IMAGE) val image: String?,
    @field:Json(name = SPACEX_FIELD_CREW_WIKI) val wikipedia: String?,
    @field:Json(name = SPACEX_FIELD_CREW_LAUNCHES) val launches: List<LaunchResponse>?,
    @field:Json(name = SPACEX_FIELD_ID) val id: String
)

enum class CrewStatus(val status: String) {
    ACTIVE(SPACEX_ACTIVE),
    INACTIVE(SPACEX_INACTIVE),
    RETIRED(SPACEX_RETIRED),
    UNKNOWN(SPACEX_UNKNOWN)
}