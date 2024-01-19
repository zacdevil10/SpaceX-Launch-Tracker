package uk.co.zac_h.spacex.network.datasource.remote.news

import android.net.Uri
import androidx.paging.PagingSource
import androidx.paging.PagingState
import uk.co.zac_h.spacex.network.dto.news.ArticleResponse
import uk.co.zac_h.spacex.network.retrofit.SpaceflightNewsService

class SpaceflightNewsPagingSource(
    private val httpService: SpaceflightNewsService,
    private val launch: String? = null
) : PagingSource<Int, ArticleResponse>() {

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, ArticleResponse> = try {
        val response = httpService.getArticles(
            limit = params.loadSize,
            offset = params.key ?: 0,
            search = if (launch != null) null else "SpaceX",
            launch = launch
        )

        val nextOffset: Int? = response.body()?.next?.let {
            val uri = Uri.parse(it)
            val nextOffsetQuery = uri.getQueryParameter("offset")

            nextOffsetQuery?.toInt()
        }

        if (response.isSuccessful) {
            LoadResult.Page(
                data = checkNotNull(response.body()?.results),
                prevKey = null,
                nextKey = nextOffset
            )
        } else {
            LoadResult.Error(Exception(response.errorBody()?.string()))
        }
    } catch (e: Exception) {
        LoadResult.Error(e)
    }

    override fun getRefreshKey(state: PagingState<Int, ArticleResponse>): Int? = null
}