package uk.co.zac_h.spacex.utils.data.rest

import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.http.GET
import retrofit2.http.Path
import uk.co.zac_h.spacex.utils.data.LaunchesModel

interface SpaceXInterface {

    @GET("launches/{flight}")
    suspend fun getSingleLaunch(@Path("flight") flight: String): Response<LaunchesModel>

    companion object RetrofitSetup {
        fun create(): SpaceXInterface {
            val retrofit = Retrofit.Builder().apply {
                baseUrl("https://api.spacexdata.com/v3/")
                addConverterFactory(MoshiConverterFactory.create())
            }.build()

            return retrofit.create(SpaceXInterface::class.java)
        }
    }
}