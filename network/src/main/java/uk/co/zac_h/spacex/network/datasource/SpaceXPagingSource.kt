package uk.co.zac_h.spacex.network.datasource

import android.net.Uri
import androidx.paging.PagingSource
import androidx.paging.PagingState
import retrofit2.Response
import uk.co.zac_h.spacex.network.dto.spacex.LaunchLibraryPaginatedResponse

abstract class SpaceXPagingSource<T : Any> : PagingSource<Int, T>() {

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, T> = try {
        val response = getResponse(
            limit = params.loadSize,
            offset = params.key ?: 0
        )

        val nextOffset: Int? = response.body()?.next?.let {
            val uri = Uri.parse(response.body()?.next)
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
            response.code()
            LoadResult.Error(Exception(response.errorBody()?.string()))
        }
    } catch (e: Exception) {
        LoadResult.Error(e)
    }

    override fun getRefreshKey(state: PagingState<Int, T>): Int? = null

    abstract suspend fun getResponse(
        limit: Int,
        offset: Int
    ): Response<LaunchLibraryPaginatedResponse<T>>
}