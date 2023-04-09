package uk.co.zac_h.spacex.network.dto.spacex

import com.squareup.moshi.Json
import uk.co.zac_h.spacex.network.SPACEX_ACTIVE
import uk.co.zac_h.spacex.network.SPACEX_DECEASED
import uk.co.zac_h.spacex.network.SPACEX_FIELD_CREW_AGENCY
import uk.co.zac_h.spacex.network.SPACEX_FIELD_CREW_IMAGE
import uk.co.zac_h.spacex.network.SPACEX_FIELD_CREW_LAUNCHES
import uk.co.zac_h.spacex.network.SPACEX_FIELD_CREW_NAME
import uk.co.zac_h.spacex.network.SPACEX_FIELD_CREW_STATUS
import uk.co.zac_h.spacex.network.SPACEX_FIELD_CREW_WIKI
import uk.co.zac_h.spacex.network.SPACEX_FIELD_ID
import uk.co.zac_h.spacex.network.SPACEX_INACTIVE
import uk.co.zac_h.spacex.network.SPACEX_IN_TRAINING
import uk.co.zac_h.spacex.network.SPACEX_LOST_IN_TRAINING
import uk.co.zac_h.spacex.network.SPACEX_RETIRED
import uk.co.zac_h.spacex.network.SPACEX_UNKNOWN

data class CrewQueriedResponse(
    @field:Json(name = SPACEX_FIELD_CREW_NAME) val name: String?,
    @field:Json(name = SPACEX_FIELD_CREW_STATUS) val status: String?,
    @field:Json(name = SPACEX_FIELD_CREW_AGENCY) val agency: String?,
    @field:Json(name = SPACEX_FIELD_CREW_IMAGE) val image: String?,
    @field:Json(name = SPACEX_FIELD_CREW_WIKI) val wikipedia: String?,
    @field:Json(name = SPACEX_FIELD_CREW_LAUNCHES) val launches: List<LegacyLaunchResponse>?,
    @field:Json(name = SPACEX_FIELD_ID) val id: String
)

enum class CrewStatus(val status: String) {
    IN_TRAINING(SPACEX_IN_TRAINING),
    ACTIVE(SPACEX_ACTIVE),
    INACTIVE(SPACEX_INACTIVE),
    RETIRED(SPACEX_RETIRED),
    DECEASED(SPACEX_DECEASED),
    LOST_IN_TRAINING(SPACEX_LOST_IN_TRAINING),
    UNKNOWN(SPACEX_UNKNOWN)
}
