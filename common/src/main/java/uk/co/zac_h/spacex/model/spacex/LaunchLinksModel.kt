package uk.co.zac_h.spacex.model.spacex

import android.os.Parcelable
import com.squareup.moshi.Json
import kotlinx.android.parcel.Parcelize

// v4
@Parcelize
data class LaunchLinksModel(
    @field:Json(name = "patch") val missionPatch: MissionPatchModel,
    @field:Json(name = "reddit") val redditLinks: MissionRedditLinksModel,
    @field:Json(name = "flickr") val flickr: MissionFlickrModel,
    @field:Json(name = "presskit") val presskit: String?,
    @field:Json(name = "webcast") val webcast: String?,
    @field:Json(name = "youtube_id") val youtubeId: String?,
    @field:Json(name = "article") val article: String?,
    @field:Json(name = "wikipedia") val wikipedia: String?
) : Parcelable

@Parcelize
data class MissionPatchModel(
    @field:Json(name = "small") val patchSmall: String?,
    @field:Json(name = "large") val patchLarge: String?
) : Parcelable

@Parcelize
data class MissionRedditLinksModel(
    @field:Json(name = "campaign") val campaign: String?,
    @field:Json(name = "launch") val launch: String?,
    @field:Json(name = "media") val media: String?,
    @field:Json(name = "recovery") val recovery: String?
) : Parcelable

@Parcelize
data class MissionFlickrModel(
    @field:Json(name = "small") val small: List<String>,
    @field:Json(name = "original") val original: List<String>
) : Parcelable