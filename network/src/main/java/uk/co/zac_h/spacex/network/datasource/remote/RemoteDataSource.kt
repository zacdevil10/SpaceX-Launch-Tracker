package uk.co.zac_h.spacex.network.datasource.remote

import retrofit2.Response
import uk.co.zac_h.spacex.network.dto.spacex.QueryModel

interface RemoteDataSource<T> {

    suspend fun fetchAsync(query: QueryModel): Response<T>
}
