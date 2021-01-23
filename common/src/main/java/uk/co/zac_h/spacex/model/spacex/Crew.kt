package uk.co.zac_h.spacex.model.spacex

import android.os.Parcelable
import com.squareup.moshi.Json
import kotlinx.android.parcel.Parcelize
import uk.co.zac_h.spacex.utils.*

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

@Parcelize
data class Crew(
    val name: String?,
    val status: CrewStatus?,
    val agency: String?,
    val image: String?,
    val wikipedia: String?,
    val launchIds: List<String>? = null,
    val launches: List<Launch>? = null,
    val id: String
) : Parcelable {

    constructor(
        response: CrewResponse
    ) : this(
        name = response.name,
        status = response.status.toCrewStatus(),
        agency = response.agency,
        image = response.image,
        wikipedia = response.wikipedia,
        launchIds = response.launches,
        id = response.id
    )

    constructor(
        response: CrewQueriedResponse
    ) : this(
        name = response.name,
        status = response.status.toCrewStatus(),
        agency = response.agency,
        image = response.image,
        wikipedia = response.wikipedia,
        launches = response.launches?.map { Launch(it) },
        id = response.id
    )

    companion object {
        private fun String?.toCrewStatus() = when (this) {
            SPACEX_CREW_STATUS_ACTIVE -> CrewStatus.ACTIVE
            SPACEX_CREW_STATUS_INACTIVE -> CrewStatus.INACTIVE
            SPACEX_CREW_STATUS_RETIRED -> CrewStatus.RETIRED
            else -> CrewStatus.UNKNOWN
        }
    }
}

enum class CrewStatus(val status: String) {
    ACTIVE(SPACEX_ACTIVE),
    INACTIVE(SPACEX_INACTIVE),
    RETIRED(SPACEX_RETIRED),
    UNKNOWN(SPACEX_UNKNOWN)
}