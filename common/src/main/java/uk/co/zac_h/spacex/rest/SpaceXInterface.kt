package uk.co.zac_h.spacex.rest

import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.http.*
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

    @POST("capsules/query")
    fun getCapsules(@Body body: QueryModel): Call<CapsulesDocsModel>

    @POST("cores/query")
    fun getCores(@Body body: QueryModel): Call<CoreDocsModel>

    @GET("cores/{serial}")
    fun getSingleCore(@Path("serial") serial: String): Call<CoreModel>

    @POST("crew/query")
    fun getCrew(@Body body: QueryModel): Call<CrewDocsModel>

    @GET("launchpads")
    fun getLaunchpads(): Call<List<LaunchpadModel>>

    @GET("landpads")
    fun getLandingPads(): Call<List<LandingPadModel>>

    @GET("history")
    fun getHistory(@Query("order") order: String): Call<List<HistoryModel>>

    @GET("company")
    fun getCompanyInfo(): Call<CompanyModel>

    companion object RetrofitSetup {
        fun create(): SpaceXInterface = Retrofit.Builder().apply {
            baseUrl("https://api.spacexdata.com/v4/")
            addConverterFactory(MoshiConverterFactory.create())
        }.build().create(SpaceXInterface::class.java)
    }
}