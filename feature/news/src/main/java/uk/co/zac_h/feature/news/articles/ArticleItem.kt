package uk.co.zac_h.feature.news.articles

import uk.co.zac_h.spacex.core.common.recyclerview.RecyclerViewItem
import uk.co.zac_h.spacex.network.dto.news.ArticleResponse

data class ArticleItem(
    val id: Int,
    val title: String,
    val url: String,
    val image: String,
    val site: String,
    val summary: String,
    val published: String,
    val updated: String
) : RecyclerViewItem {

    constructor(
        response: ArticleResponse
    ) : this(
        id = response.id,
        title = response.title,
        url = response.url,
        image = response.imageUrl,
        site = response.newsSite,
        summary = response.summary,
        published = response.publishedAt,
        updated = response.updatedAt
    )
}