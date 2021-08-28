package uk.co.zac_h.spacex.datasource

import retrofit2.Response
import uk.co.zac_h.spacex.dto.spacex.QueryModel

interface RemoteDataSource<T> {

    suspend fun fetchAsync(body: QueryModel): Response<T>

}