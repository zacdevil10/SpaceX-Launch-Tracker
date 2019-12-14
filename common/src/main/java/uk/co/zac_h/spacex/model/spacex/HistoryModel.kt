package uk.co.zac_h.spacex.model.spacex

import android.os.Parcelable
import com.squareup.moshi.Json
import kotlinx.android.parcel.Parcelize

@Parcelize
data class HistoryModel(
    @field:Json(name = "id") val id: Int,
    @field:Json(name = "title") val title: String,
    @field:Json(name = "event_date_utc") val dateUtc: String,
    @field:Json(name = "event_date_unix") val dateUnix: Long,
    @field:Json(name = "flight_number") val flightNumber: Int?,
    @field:Json(name = "details") val details: String,
    @field:Json(name = "links") val links: HistoryLinksModel
) : Parcelable

@Parcelize
data class HistoryLinksModel(
    @field:Json(name = "reddit") val reddit: String?,
    @field:Json(name = "article") val article: String?,
    @field:Json(name = "wikipedia") val wikipedia: String?
) : Parcelable