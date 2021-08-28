package uk.co.zac_h.spacex.model.reddit

import com.squareup.moshi.Json

data class SubredditModel(
    @field:Json(name = "data") var data: SubredditDataModel
)

data class SubredditDataModel(
    @field:Json(name = "children") var children: List<SubredditPostModel>
)

data class SubredditPostModel(
    @field:Json(name = "data") var data: RedditPostData
)

data class RedditPostData(
    @field:Json(name = "selftext_html") var textHtml: String?,
    @field:Json(name = "title") var title: String,
    @field:Json(name = "name") var name: String,
    @field:Json(name = "author") var author: String,
    @field:Json(name = "created_utc") var created: Float,
    @field:Json(name = "thumbnail") var thumbnail: String,
    @field:Json(name = "score") var score: Int,
    @field:Json(name = "num_comments") var commentsCount: Int,
    @field:Json(name = "preview") var preview: RedditPreviewListModel?,
    @field:Json(name = "domain") var domain: String,
    @field:Json(name = "stickied") var stickied: Boolean,
    @field:Json(name = "is_self") var isSelf: Boolean,
    @field:Json(name = "is_reddit_media_domain") var redditDomain: Boolean,
    @field:Json(name = "permalink") var permalink: String
)

data class RedditPreviewListModel(
    @field:Json(name = "images") var images: List<RedditMediaAlternatesModel>
)

data class RedditMediaAlternatesModel(
    @field:Json(name = "source") var source: RedditMediaModel,
    @field:Json(name = "resolutions") var resolutions: List<RedditMediaModel>
)

data class RedditMediaModel(
    @field:Json(name = "url") var url: String,
    @field:Json(name = "width") var width: Int,
    @field:Json(name = "height") var height: Int
)

data class RedditPost(
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