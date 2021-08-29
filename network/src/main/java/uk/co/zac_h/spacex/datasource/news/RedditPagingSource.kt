package uk.co.zac_h.spacex.datasource.news

import androidx.paging.PagingSource
import androidx.paging.PagingState
import uk.co.zac_h.spacex.REDDIT_PARAM_ORDER_HOT
import uk.co.zac_h.spacex.dto.reddit.SubredditPostModel
import uk.co.zac_h.spacex.retrofit.RedditService

class RedditPagingSource(
    private val httpService: RedditService
) : PagingSource<String, SubredditPostModel>() {

    var order: String = REDDIT_PARAM_ORDER_HOT

    override suspend fun load(params: LoadParams<String>): LoadResult<String, SubredditPostModel> =
        try {
            val nextPage = params.key
            val response = httpService.getRedditFeed(
                subreddit = "SpaceX",
                order = order,
                id = nextPage,
                limit = params.loadSize
            )
            LoadResult.Page(
                data = checkNotNull(response.body()?.data?.children),
                prevKey = null,
                nextKey = response.body()?.data?.children?.last()?.data?.name
            )
        } catch (e: Exception) {
            LoadResult.Error(e)
        }

    override fun getRefreshKey(state: PagingState<String, SubredditPostModel>): String? = null

}