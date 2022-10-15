package uk.co.zac_h.spacex.datasource.remote.launches

import android.net.Uri
import androidx.paging.PagingSource
import androidx.paging.PagingState
import uk.co.zac_h.spacex.dto.spacex.LaunchResponse
import uk.co.zac_h.spacex.retrofit.LaunchLibraryService

class LaunchesUpcomingPagingSource(
    private val httpService: LaunchLibraryService
) : PagingSource<Int, LaunchResponse>() {

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, LaunchResponse> = try {
        val response = httpService.getUpcomingLaunches(
            limit = params.loadSize,
            offset = params.key ?: 0
        )

        val nextOffset: Int? = response.body()?.next?.let {
            val uri = Uri.parse(response.body()?.next)
            val nextOffsetQuery = uri.getQueryParameter("offset")

            nextOffsetQuery?.toInt()
        }

        LoadResult.Page(
            data = checkNotNull(response.body()?.results),
            prevKey = null,
            nextKey = nextOffset
        )
    } catch (e: Exception) {
        LoadResult.Error(e)
    }

    override fun getRefreshKey(state: PagingState<Int, LaunchResponse>): Int? = null
}