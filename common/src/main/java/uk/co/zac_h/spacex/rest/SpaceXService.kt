package uk.co.zac_h.spacex.rest

import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.POST
import uk.co.zac_h.spacex.model.spacex.LaunchDocsModel
import uk.co.zac_h.spacex.model.spacex.QueryModel
import uk.co.zac_h.spacex.utils.SPACEX_LAUNCHES_QUERY

interface SpaceXService {

    @POST(SPACEX_LAUNCHES_QUERY)
    fun queryLaunches(@Body body: QueryModel): Call<LaunchDocsModel>

}