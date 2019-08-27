package uk.co.zac_h.spacex.utils.data

import android.os.Parcelable
import com.squareup.moshi.Json
import kotlinx.android.parcel.Parcelize

@Parcelize
data class LaunchLinksModel(
    @field:Json(name = "mission_patch") var missionPatch: String,
    @field:Json(name = "mission_patch_small") var missionPatchSmall: String,
    @field:Json(name = "reddit_campaign") var redditCampaign: String,
    @field:Json(name = "reddit_launch") var redditLaunch: String,
    @field:Json(name = "reddit_recovery") var redditRecovery: String,
    @field:Json(name = "reddit_media") var redditMedia: String,
    @field:Json(name = "presskit") var presskit: String,
    @field:Json(name = "article_link") var articleLink: String,
    @field:Json(name = "wikipedia") var wikipedia: String,
    @field:Json(name = "video_link") var videoLink: String,
    @field:Json(name = "youtube_id") var youtubeId: String,
    @field:Json(name = "flickr_images") var flickrImages: List<String>
) : Parcelable