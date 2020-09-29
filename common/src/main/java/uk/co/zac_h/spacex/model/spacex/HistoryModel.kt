package uk.co.zac_h.spacex.model.spacex

import android.os.Parcelable
import com.squareup.moshi.Json
import kotlinx.android.parcel.Parcelize

// v4
data class HistoryDocsModel(
    @field:Json(name = "docs") val docs: List<HistoryModel>
)

@Parcelize
data class HistoryModel(
    @field:Json(name = "links") val links: HistoryLinksModel,
    @field:Json(name = "title") val title: String,
    @field:Json(name = "event_date_utc") val dateUtc: String,
    @field:Json(name = "event_date_unix") val dateUnix: Long,
    @field:Json(name = "details") val details: String,
    @field:Json(name = "id") val id: String
) : Parcelable

@Parcelize
data class HistoryLinksModel(
    @field:Json(name = "article") val article: String?
) : Parcelable