package uk.co.zac_h.spacex.model.spacex

import android.os.Parcelable
import com.squareup.moshi.Json
import kotlinx.android.parcel.Parcelize
import uk.co.zac_h.spacex.utils.*

data class HistoryDocsModel(
    @field:Json(name = "docs") val docs: List<HistoryQueriedResponse>
)

data class HistoryQueriedResponse(
    @field:Json(name = SPACEX_FIELD_HISTORY_LINKS) val links: HistoryLinksModel?,
    @field:Json(name = SPACEX_FIELD_HISTORY_TITLE) val title: String?,
    @field:Json(name = SPACEX_FIELD_HISTORY_EVENT_DATE_UTC) val dateUtc: String?,
    @field:Json(name = SPACEX_FIELD_HISTORY_EVENT_DATE_UNIX) val dateUnix: Long?,
    @field:Json(name = SPACEX_FIELD_HISTORY_DETAILS) val details: String?,
    @field:Json(name = SPACEX_FIELD_ID) val id: String
)

data class HistoryLinksModel(
    @field:Json(name = SPACEX_FIELD_HISTORY_ARTICLE) val article: String?
)

@Parcelize
data class History(
    val article: String?,
    val title: String?,
    val event: EventDate?,
    val details: String?,
    val id: String
) : Parcelable {

    constructor(
        response: HistoryQueriedResponse
    ) : this(
        article = response.links?.article,
        title = response.title,
        event = EventDate(dateUtc = response.dateUtc, dateUnix = response.dateUnix),
        details = response.details,
        id = response.id
    )

}