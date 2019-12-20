package uk.co.zac_h.spacex.model.reddit

import com.squareup.moshi.Json

data class SubredditModel(
    @field:Json(name = "kind") var kind: String,
    @field:Json(name = "data") var data: SubredditDataModel
)

data class SubredditDataModel(
    @field:Json(name = "modhash") var modhash: String,
    @field:Json(name = "dist") var dist: Int,
    @field:Json(name = "children") var children: List<SubredditPostModel>
)

data class SubredditPostModel(
    @field:Json(name = "kind") var kind: String,
    @field:Json(name = "data") var data: RedditPostData
)

data class RedditPostData(
    @field:Json(name = "selftext") var text: String,
    @field:Json(name = "title") var title: String,
    @field:Json(name = "name") var name: String,
    @field:Json(name = "author") var author: String,
    @field:Json(name = "num_comments") var commentsNum: Int,
    @field:Json(name = "created_utc") var created: Float,
    @field:Json(name = "thumbnail") var thumbnail: String
)