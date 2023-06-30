package uk.co.zac_h.spacex.network.datasource.remote.vehicles

import android.net.Uri
import androidx.paging.PagingSource
import androidx.paging.PagingState
import uk.co.zac_h.spacex.network.TooManyRequestsException
import uk.co.zac_h.spacex.network.dto.spacex.LauncherResponse
import uk.co.zac_h.spacex.network.retrofit.LaunchLibraryService

class LauncherPagingSource(
    private val httpService: LaunchLibraryService,
    private val launcherConfigId: Int? = null
) : PagingSource<Int, LauncherResponse>() {

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, LauncherResponse> = try {
        val response = launcherConfigId?.let {
            httpService.getLaunchersForConfig(
                launcherConfigId = it,
                limit = params.loadSize,
                offset = params.key ?: 0
            )
        } ?: httpService.getLaunchers(
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
                when (response.code()) {
                    429 -> TooManyRequestsException(0)
                    else -> Exception(response.errorBody()?.string())
                }
            )
        }
    } catch (e: Exception) {
        LoadResult.Error(e)
    }

    override fun getRefreshKey(state: PagingState<Int, LauncherResponse>): Int? = null
}