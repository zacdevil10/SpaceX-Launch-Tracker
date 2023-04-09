package uk.co.zac_h.spacex.network.datasource.remote

import android.net.Uri
import androidx.paging.PagingSource
import androidx.paging.PagingState
import uk.co.zac_h.spacex.network.dto.spacex.AstronautResponse
import uk.co.zac_h.spacex.network.retrofit.LaunchLibraryService

class AstronautPagingSource(
    private val httpService: LaunchLibraryService
) : PagingSource<Int, AstronautResponse>() {

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, AstronautResponse> = try {
        val response = httpService.getAstronauts(
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
            LoadResult.Error(
                Exception(
                    when (response.code()) {
                        429 -> "Too many request have been made. There is a limit of 15 requests per hour."
                        else -> response.errorBody()?.string()
                    }
                )
            )
        }
    } catch (e: Exception) {
        LoadResult.Error(e)
    }

    override fun getRefreshKey(state: PagingState<Int, AstronautResponse>): Int? = null
}
