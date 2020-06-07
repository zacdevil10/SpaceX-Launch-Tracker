package uk.co.zac_h.spacex.rest

import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query
import uk.co.zac_h.spacex.model.spacex.*

interface SpaceXInterface {

    @GET("launches/{id}")
    fun getSingleLaunch(@Path("id") id: String): Call<LaunchesModel>

    @GET("launches/{launches}")
    fun getLaunches(
        @Path("launches") launches: String,
        @Query("order") order: String
    ): Call<List<LaunchesModel>>

    @GET("launches")
    fun getLaunches(): Call<List<LaunchesModel>>

    @GET("rockets")
    fun getRockets(): Call<List<RocketsModel>>

    @GET("dragons")
    fun getDragons(): Call<List<DragonModel>>

    @GET("capsules")
    fun getCapsules(): Call<List<CapsulesModel>>

    @GET("cores")
    fun getCores(): Call<List<CoreModel>>

    @GET("cores/{serial}")
    fun getSingleCore(@Path("serial") serial: String): Call<CoreModel>

    @GET("crew")
    fun getCrew(): Call<List<CrewModel>>

    @GET("launchpads")
    fun getLaunchpads(): Call<List<LaunchpadModel>>

    @GET("landpads")
    fun getLandingPads(): Call<List<LandingPadModel>>

    @GET("history")
    fun getHistory(@Query("order") order: String): Call<List<HistoryModel>>

    @GET("info")
    fun getCompanyInfo(): Call<CompanyModel>

    companion object RetrofitSetup {
        fun create(): SpaceXInterface = Retrofit.Builder().apply {
            baseUrl("https://api.spacexdata.com/v4/")
            addConverterFactory(MoshiConverterFactory.create())
        }.build().create(SpaceXInterface::class.java)
    }
}