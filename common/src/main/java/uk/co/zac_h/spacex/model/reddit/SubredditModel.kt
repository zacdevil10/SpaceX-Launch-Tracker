package uk.co.zac_h.spacex.model.reddit

import android.os.Parcelable
import com.squareup.moshi.Json
import kotlinx.android.parcel.Parcelize

@Parcelize
data class SubredditModel(
    @field:Json(name = "kind") var kind: String,
    @field:Json(name = "data") var data: SubredditDataModel
) : Parcelable

@Parcelize
data class SubredditDataModel(
    @field:Json(name = "modhash") var modhash: String,
    @field:Json(name = "dist") var dist: Int,
    @field:Json(name = "children") var children: List<SubredditPostModel>
) : Parcelable

@Parcelize
data class SubredditPostModel(
    @field:Json(name = "kind") var kind: String,
    @field:Json(name = "data") var data: RedditPostData
) : Parcelable

@Parcelize
data class RedditPostData(
    @field:Json(name = "selftext") var text: String,
    @field:Json(name = "title") var title: String,
    @field:Json(name = "name") var name: String,
    @field:Json(name = "author") var author: String,
    @field:Json(name = "created_utc") var created: Float,
    @field:Json(name = "thumbnail") var thumbnail: String,
    @field:Json(name = "score") var score: Int,
    @field:Json(name = "num_comments") var commentsCount: Int,
    @field:Json(name = "url") var url: String
) : Parcelable