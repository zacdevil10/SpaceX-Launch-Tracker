package uk.co.zac_h.spacex.network.datasource.remote

interface RemoteDataSource<T> {

    suspend fun fetchAsync(): T
}
