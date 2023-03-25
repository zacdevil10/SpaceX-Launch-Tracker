package uk.co.zac_h.spacex.news.articles

import uk.co.zac_h.spacex.network.datasource.remote.news.SpaceflightNewsPagingSource
import uk.co.zac_h.spacex.network.retrofit.SpaceflightNewsClient
import uk.co.zac_h.spacex.network.retrofit.SpaceflightNewsService
import javax.inject.Inject

class ArticlesRepository @Inject constructor(
    @SpaceflightNewsClient private val httpService: SpaceflightNewsService
) {

    val articlesPagingSource: SpaceflightNewsPagingSource
        get() = SpaceflightNewsPagingSource(httpService)
}