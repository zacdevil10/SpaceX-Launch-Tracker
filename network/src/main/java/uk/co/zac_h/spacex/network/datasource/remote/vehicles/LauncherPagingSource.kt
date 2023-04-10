package uk.co.zac_h.spacex.network.datasource.remote.vehicles

import android.net.Uri
import androidx.paging.PagingSource
import androidx.paging.PagingState
import uk.co.zac_h.spacex.network.dto.spacex.LauncherResponse
import uk.co.zac_h.spacex.network.retrofit.LaunchLibraryService

class LauncherPagingSource(
    private val httpService: LaunchLibraryService
) : PagingSource<Int, LauncherResponse>() {

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, LauncherResponse> = try {
        val response = httpService.getLaunchers(
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

    override fun getRefreshKey(state: PagingState<Int, LauncherResponse>): Int? = null
}