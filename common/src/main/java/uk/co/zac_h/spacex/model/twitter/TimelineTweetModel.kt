package uk.co.zac_h.spacex.model.twitter

import android.os.Parcelable
import com.squareup.moshi.Json
import kotlinx.android.parcel.Parcelize

@Parcelize
data class TimelineTweetModel(
    @field:Json(name = "created_at") var created: String,
    @field:Json(name = "id") var id: Long,
    @field:Json(name = "full_text") var text: String,
    @field:Json(name = "extended_entities") var entities: TimelineEntityModel?,
    @field:Json(name = "user") var user: TwitterUserModel
) : Parcelable

@Parcelize
data class TimelineEntityModel(
    @field:Json(name = "media") var media: List<TweetMediaModel>
) : Parcelable

@Parcelize
data class TweetMediaModel(
    @field:Json(name = "media_url_https") var url: String,
    @field:Json(name = "type") var type: String
) : Parcelable

@Parcelize
data class TwitterUserModel(
    @field:Json(name = "name") var name: String,
    @field:Json(name = "screen_name") var screenName: String,
    @field:Json(name = "profile_image_url_https") var profileUrl: String
) : Parcelable