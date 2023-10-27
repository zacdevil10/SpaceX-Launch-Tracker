package uk.co.zac_h.spacex.network.retrofit

import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query
import uk.co.zac_h.spacex.network.SPACEFLIGHT_NEWS_ARTICLES
import uk.co.zac_h.spacex.network.dto.news.ArticleResponse
import uk.co.zac_h.spacex.network.dto.news.SpaceflightNewsPaginatedResponse

interface SpaceflightNewsService {

    @GET(SPACEFLIGHT_NEWS_ARTICLES)
    suspend fun getArticles(
        @Query("limit") limit: Int,
        @Query("offset") offset: Int,
        @Query("search") search: String = "SpaceX",
    ): Response<SpaceflightNewsPaginatedResponse<ArticleResponse>>
}
