package uk.co.zac_h.spacex.network.datasource.remote

import retrofit2.Response

interface RemoteDataSource<T> {

    suspend fun fetchAsync(): Response<T>
}
