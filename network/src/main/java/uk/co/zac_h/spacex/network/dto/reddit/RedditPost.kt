package uk.co.zac_h.spacex.network.dto.reddit

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class SubredditModel(
    var data: SubredditDataModel
)

@Serializable
data class SubredditDataModel(
    var children: List<SubredditPostModel>
)

@Serializable
data class SubredditPostModel(
    var data: RedditPostData
)

@Serializable
data class RedditPostData(
    var id: String,
    @SerialName("selftext_html") var textHtml: String?,
    var title: String,
    var name: String,
    var author: String,
    @SerialName("created_utc") var created: Float,
    var thumbnail: String,
    var score: Int,
    @SerialName("num_comments") var commentsCount: Int,
    var preview: RedditPreviewListModel?,
    var domain: String,
    var stickied: Boolean,
    @SerialName("is_self") var isSelf: Boolean,
    @SerialName("is_reddit_media_domain") var redditDomain: Boolean,
    var permalink: String
)

@Serializable
data class RedditPreviewListModel(
    var images: List<RedditMediaAlternatesModel>
)

@Serializable
data class RedditMediaAlternatesModel(
    var source: RedditMediaModel,
    var resolutions: List<RedditMediaModel>
)

@Serializable
data class RedditMediaModel(
    var url: String,
    var width: Int,
    var height: Int
)

data class RedditPost(
    var id: String,
    var name: String,
    var title: String,
    var description: String?,
    var author: String,
    var created: Float,
    var thumbnail: String,
    var score: Int,
    var commentsCount: Int,
    var preview: RedditPreviewListModel?,
    var domain: String,
    var stickied: Boolean,
    var isSelf: Boolean,
    var redditDomain: Boolean,
    var permalink: String
) {

    constructor(data: RedditPostData) : this(
        id = data.id,
        name = data.name,
        title = data.title,
        description = data.textHtml,
        author = data.author,
        created = data.created,
        thumbnail = data.thumbnail,
        score = data.score,
        commentsCount = data.commentsCount,
        preview = data.preview,
        domain = data.domain,
        stickied = data.stickied,
        isSelf = data.isSelf,
        redditDomain = data.redditDomain,
        permalink = data.permalink
    )
}
