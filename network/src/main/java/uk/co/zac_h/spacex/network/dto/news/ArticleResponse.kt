package uk.co.zac_h.spacex.network.dto.news

import com.squareup.moshi.Json

data class ArticleResponse(
    @field:Json(name = "id") val id: Int,
    @field:Json(name = "title") val title: String,
    @field:Json(name = "url") val url: String,
    @field:Json(name = "image_url") val imageUrl: String,
    @field:Json(name = "news_site") val newsSite: String,
    @field:Json(name = "summary") val summary: String,
    @field:Json(name = "published_at") val publishedAt: String,
    @field:Json(name = "updated_at") val updatedAt: String,
    @field:Json(name = "featured") val featured: Boolean,
    @field:Json(name = "launches") val launches: List<Launch>
) {

    data class Launch(
        @field:Json(name = "launch_id") val id: String,
        @field:Json(name = "provider") val provider: String,
    )
}
