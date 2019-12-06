package uk.co.zac_h.spacex.model.twitter

import android.os.Parcelable
import com.squareup.moshi.Json
import kotlinx.android.parcel.Parcelize

@Parcelize
data class TimelineTweetModel(
    @field:Json(name = "created_at") var created: String,
    @field:Json(name = "id") var id: Long,
    @field:Json(name = "full_text") var text: String,
    @field:Json(name = "entities") var entities: TimelineEntityModel,
    @field:Json(name = "extended_entities") var extendedEntities: TimelineExtendedEntityModel?,
    @field:Json(name = "user") var user: TwitterUserModel,
    @field:Json(name = "in_reply_to_status_id") var replyStatusId: Long?
) : Parcelable

@Parcelize
data class TimelineEntityModel(
    @field:Json(name = "user_mentions") var mentions: List<TweetMentionsModel>,
    @field:Json(name = "urls") var urls: List<TweetUrlModel>
) : Parcelable

@Parcelize
data class TimelineExtendedEntityModel(
    @field:Json(name = "media") var media: List<TweetMediaModel>
) : Parcelable

@Parcelize
data class TweetMentionsModel(
    @field:Json(name = "screen_name") var screenName: String,
    @field:Json(name = "name") var name: String,
    @field:Json(name = "id") var id: Long
) : Parcelable

@Parcelize
data class TweetUrlModel(
    @field:Json(name = "url") var url: String,
    @field:Json(name = "expanded_url") var expandedUrl: String,
    @field:Json(name = "display_url") var displayUrl: String
) : Parcelable

@Parcelize
data class TweetMediaModel(
    @field:Json(name = "media_url_https") var url: String,
    @field:Json(name = "type") var type: String,
    @field:Json(name = "video_info") var info: TweetVideoInfoModel
) : Parcelable

@Parcelize
data class TwitterUserModel(
    @field:Json(name = "name") var name: String,
    @field:Json(name = "screen_name") var screenName: String,
    @field:Json(name = "profile_image_url_https") var profileUrl: String
) : Parcelable

@Parcelize
data class TweetVideoInfoModel(
    @field:Json(name = "aspect_ratio") var aspectRatio: List<Int>,
    @field:Json(name = "duration_millis") var duration: Int,
    @field:Json(name = "variants") var variants: List<TweetVideoVariantsModel>
) : Parcelable

@Parcelize
data class TweetVideoVariantsModel(
    @field:Json(name = "bitrate") var bitrate: Long?,
    @field:Json(name = "content_type") var contentType: String,
    @field:Json(name = "url") var url: String
) : Parcelable