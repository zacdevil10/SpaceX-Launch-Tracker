package uk.co.zac_h.spacex.dto.twitter

import com.squareup.moshi.Json
import java.util.regex.Pattern

data class TimelineTweetModel(
    @field:Json(name = "created_at") var created: String,
    @field:Json(name = "id") var id: Long,
    @field:Json(name = "full_text") var text: String?,
    @field:Json(name = "entities") var entities: TimelineEntityModel,
    @field:Json(name = "extended_entities") var extendedEntities: TimelineExtendedEntityModel?,
    @field:Json(name = "quoted_status_permalink") var quotedStatusLink: TweetQuotedLinkModel?,
    @field:Json(name = "quoted_status") var quotedStatus: TweetQuotedStatusModel?,
    @field:Json(name = "user") var user: TwitterUserModel,
    @field:Json(name = "in_reply_to_status_id") var replyStatusId: Long?,
    @field:Json(name = "is_quote_status") var isQuote: Boolean
)

data class TimelineEntityModel(
    @field:Json(name = "hashtags") var hashtags: List<TweetHashTagModel>,
    @field:Json(name = "user_mentions") var mentions: List<TweetMentionsModel>,
    @field:Json(name = "urls") var urls: List<TweetUrlModel>
) {

    data class TweetHashTagModel(
        @field:Json(name = "text") var tag: String?
    )

    data class TweetMentionsModel(
        @field:Json(name = "screen_name") var screenName: String?,
        @field:Json(name = "name") var name: String?,
        @field:Json(name = "id") var id: Long?
    )

    data class TweetUrlModel(
        @field:Json(name = "url") var url: String,
        @field:Json(name = "expanded_url") var expandedUrl: String,
        @field:Json(name = "display_url") var displayUrl: String?
    )

    companion object {

        fun String.formatWithUrls(
            urls: List<TweetUrlModel>?,
            mentions: List<TweetMentionsModel>?,
            tags: List<TweetHashTagModel>?
        ): String {
            var message = this

            urls?.forEach {
                message = message.replace(it.url, "<a href='${it.url}'>${it.displayUrl}</a>")
            }

            val pattern = Pattern.compile("((https://t.co/)\\w+)\$")
            val matcher = pattern.matcher(message)

            message = matcher.replaceAll("")

            mentions?.forEach {
                message = message.replace(
                    "@${it.screenName}",
                    "<a href='https://twitter.com/${it.screenName}'>@${it.screenName}</a>",
                    true
                )
            }

            tags?.forEach {
                message = message.replace(
                    "#${it.tag}",
                    "<a href='https://twitter.com/hashtag/${it.tag}'>#${it.tag}</a>",
                    true
                )
            }

            return message
        }

    }

}

data class TimelineExtendedEntityModel(
    @field:Json(name = "media") var media: List<TweetMediaModel>?
)

data class TweetMediaModel(
    @field:Json(name = "media_url_https") var url: String?,
    @field:Json(name = "type") var type: String?,
    @field:Json(name = "video_info") var info: TweetVideoInfoModel?
)

data class TweetQuotedLinkModel(
    @field:Json(name = "url") var url: String?,
    @field:Json(name = "expanded") var expandedUrl: String?,
    @field:Json(name = "display") var displayUrl: String?
)

data class TweetQuotedStatusModel(
    @field:Json(name = "created_at") var created: String?,
    @field:Json(name = "full_text") var text: String?,
    @field:Json(name = "entities") var entities: TimelineEntityModel?,
    @field:Json(name = "extended_entities") var extendedEntities: TimelineExtendedEntityModel?,
    @field:Json(name = "user") var user: TwitterUserModel?
)

data class TwitterUserModel(
    @field:Json(name = "name") var name: String,
    @field:Json(name = "screen_name") var screenName: String,
    @field:Json(name = "profile_image_url_https") var profileUrl: String?
)

data class TweetVideoInfoModel(
    @field:Json(name = "aspect_ratio") var aspectRatio: List<Int>?,
    @field:Json(name = "duration_millis") var duration: Int?,
    @field:Json(name = "variants") var variants: List<TweetVideoVariantsModel>?
)

data class TweetVideoVariantsModel(
    @field:Json(name = "bitrate") var bitrate: Long?,
    @field:Json(name = "content_type") var contentType: String?,
    @field:Json(name = "url") var url: String?
)