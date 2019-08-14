package uk.co.zac_h.spacex.utils.data.rest

import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query
import uk.co.zac_h.spacex.utils.data.LandingPadModel
import uk.co.zac_h.spacex.utils.data.LaunchesModel
import uk.co.zac_h.spacex.utils.data.LaunchpadModel
import uk.co.zac_h.spacex.utils.data.RocketsModel

interface SpaceXInterface {

    @GET("launches/{flight}")
    suspend fun getSingleLaunch(@Path("flight") flight: String): Response<LaunchesModel>

    @GET("launches/{launches}")
    suspend fun getLaunches(@Path("launches") launches: String, @Query("order") order: String): Response<List<LaunchesModel>>

    @GET("launches")
    suspend fun getLaunches(): Response<List<LaunchesModel>>

    @GET("rockets/{rocket_id}")
    suspend fun getSingleRocket(@Path("rocket_id") id: String): Response<RocketsModel>

    @GET("rockets")
    suspend fun getRockets(): Response<List<RocketsModel>>

    @GET("launchpads")
    suspend fun getLaunchpads(): Response<List<LaunchpadModel>>

    @GET("landpads")
    suspend fun getLandingPads(): Response<List<LandingPadModel>>

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