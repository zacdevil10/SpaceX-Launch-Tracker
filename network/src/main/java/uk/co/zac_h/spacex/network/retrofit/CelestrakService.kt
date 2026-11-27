package uk.co.zac_h.spacex.network.retrofit

import retrofit2.http.GET
import retrofit2.http.Query
import uk.co.zac_h.spacex.network.CELESTRAK_GP
import uk.co.zac_h.spacex.network.dto.celestrak.GpElementDto

/**
 * CelesTrak's free GP data. CelesTrak asks clients not to download the same group more than once
 * every two hours; the app keeps it for [uk.co.zac_h.spacex.network.store.Freshness.STARLINK].
 */
interface CelestrakService {

    @GET(CELESTRAK_GP)
    suspend fun getGroup(
        @Query("GROUP") group: String,
        @Query("FORMAT") format: String = "json"
    ): List<GpElementDto>
}
