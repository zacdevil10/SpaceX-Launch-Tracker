package uk.co.zac_h.spacex.network.dto.news

import com.squareup.moshi.Json

data class ArticleResponse(
    val id: Int,
    val title: String,
    val url: String,
    @field:Json(name = "image_url") val imageUrl: String,
    @field:Json(name = "news_site") val newsSite: String,
    val summary: String,
    @field:Json(name = "published_at") val publishedAt: String,
    @field:Json(name = "updated_at") val updatedAt: String,
    val featured: Boolean,
    val launches: List<Launch>
) {

    data class Launch(
        @field:Json(name = "launch_id") val id: String,
        val provider: String,
    )
}
