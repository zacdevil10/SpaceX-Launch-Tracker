package uk.co.zac_h.spacex.network.datasource

import android.net.Uri
import androidx.paging.PagingSource
import androidx.paging.PagingState
import retrofit2.HttpException
import uk.co.zac_h.spacex.network.TooManyRequestsException
import uk.co.zac_h.spacex.network.dto.spacex.LaunchLibraryPaginatedResponse

abstract class SpaceXPagingSource<T : Any> : PagingSource<Int, T>() {

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, T> = try {
        val response = getResponse(
            limit = params.loadSize,
            offset = params.key ?: 0
        )

        val nextOffset: Int? = response.next?.let {
            val uri = Uri.parse(response.next)
            val nextOffsetQuery = uri.getQueryParameter("offset")

            nextOffsetQuery?.toInt()
        }

        LoadResult.Page(
            data = checkNotNull(response.results),
            prevKey = null,
            nextKey = nextOffset
        )
    } catch (e: HttpException) {
        LoadResult.Error(
            when (e.code()) {
                429 -> TooManyRequestsException(
                    e.response()?.errorBody()?.string()?.filter { it.isDigit() }?.toInt()
                )
                else -> Exception(e.message())
            }
        )
    } catch (e: Throwable) {
        LoadResult.Error(e)
    }

    override fun getRefreshKey(state: PagingState<Int, T>): Int? = null

    abstract suspend fun getResponse(
        limit: Int,
        offset: Int
    ): LaunchLibraryPaginatedResponse<T>
}