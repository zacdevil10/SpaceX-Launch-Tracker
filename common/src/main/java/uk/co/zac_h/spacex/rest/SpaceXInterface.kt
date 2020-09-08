package uk.co.zac_h.spacex.rest

import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.http.*
import uk.co.zac_h.spacex.model.spacex.*

interface SpaceXInterface {

    @POST("capsules/query")
    fun getCapsules(@Body body: QueryModel): Call<CapsulesDocsModel>

    @GET("company")
    fun getCompanyInfo(): Call<CompanyModel>

    @POST("cores/query")
    fun getCores(@Body body: QueryModel): Call<CoreDocsModel>

    @GET("cores/{serial}")
    fun getSingleCore(@Path("serial") serial: String): Call<CoreExtendedModel>

    @POST("crew/query")
    fun getCrew(@Body body: QueryModel): Call<CrewDocsModel>

    @GET("dragons")
    fun getDragons(): Call<List<DragonModel>>

    @POST("landpads/query")
    fun getQueriedLandingPads(@Body body: QueryModel): Call<LandingPadDocsModel>

    @GET("launches/{id}")
    fun getSingleLaunch(@Path("id") id: String): Call<LaunchesModel>

    @GET("launches/{launches}")
    fun getLaunches(
        @Path("launches") launches: String,
        @Query("order") order: String
    ): Call<List<LaunchesModel>>

    @POST("launches/query")
    fun getQueriedLaunches(@Body body: QueryModel): Call<LaunchesExtendedDocsModel>

    @POST("launchpads/query")
    fun getQueriedLaunchpads(@Body body: QueryModel): Call<LaunchpadDocsModel>

    @GET("rockets")
    fun getRockets(): Call<List<RocketsModel>>

    @POST("ships/query")
    fun getShips(@Body body: QueryModel): Call<ShipsDocsModel>

    @GET("history")
    fun getHistory(@Query("order") order: String): Call<List<HistoryModel>>

    companion object RetrofitSetup {
        fun create(): SpaceXInterface = Retrofit.Builder().apply {
            baseUrl("https://api.spacexdata.com/v4/")
            addConverterFactory(MoshiConverterFactory.create())
        }.build().create(SpaceXInterface::class.java)
    }
}