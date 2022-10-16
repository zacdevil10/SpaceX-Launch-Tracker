package uk.co.zac_h.spacex.network.datasource.remote.news

import androidx.paging.PagingSource
import androidx.paging.PagingState
import uk.co.zac_h.spacex.network.dto.twitter.TimelineTweetModel
import uk.co.zac_h.spacex.network.retrofit.TwitterService

class TwitterPagingSource(
    private val httpService: TwitterService
) : PagingSource<Long, TimelineTweetModel>() {

    override suspend fun load(params: LoadParams<Long>): LoadResult<Long, TimelineTweetModel> =
        try {
            val response = httpService.getTweets(
                screenName = "SpaceX",
                rts = false,
                trim = false,
                mode = "extended",
                count = params.loadSize,
                maxId = params.key
            )
            LoadResult.Page(
                data = checkNotNull(response.body()),
                prevKey = null,
                nextKey = response.body()?.last()?.id
            )
        } catch (e: Exception) {
            LoadResult.Error(e)
        }

    override fun getRefreshKey(state: PagingState<Long, TimelineTweetModel>): Long? = null
}
