package uk.co.zac_h.spacex.feature.launch

import uk.co.zac_h.spacex.network.datasource.remote.news.SpaceflightNewsPagingSource
import uk.co.zac_h.spacex.network.retrofit.SpaceflightNewsClient
import uk.co.zac_h.spacex.network.retrofit.SpaceflightNewsService
import javax.inject.Inject

/** News articles about one launch. Moves to :core:data with the rest of the news code. */
class LaunchNewsRepository @Inject constructor(
    @SpaceflightNewsClient private val newsHttpService: SpaceflightNewsService
) {

    fun articlesPagingSource(launchId: String?) = SpaceflightNewsPagingSource(
        httpService = newsHttpService,
        launch = launchId
    )
}
