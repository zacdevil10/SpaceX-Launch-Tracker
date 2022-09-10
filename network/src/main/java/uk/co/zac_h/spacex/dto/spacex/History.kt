package uk.co.zac_h.spacex.dto.spacex

import com.squareup.moshi.Json
import uk.co.zac_h.spacex.*
import uk.co.zac_h.spacex.utils.EventDate

data class HistoryResponse(
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

data class History(
    val article: String?,
    val title: String?,
    val event: EventDate?,
    val details: String?,
    val id: String
) {

    constructor(
        response: HistoryResponse
    ) : this(
        article = response.links?.article,
        title = response.title,
        event = EventDate(dateUtc = response.dateUtc, dateUnix = response.dateUnix),
        details = response.details,
        id = response.id
    )

}