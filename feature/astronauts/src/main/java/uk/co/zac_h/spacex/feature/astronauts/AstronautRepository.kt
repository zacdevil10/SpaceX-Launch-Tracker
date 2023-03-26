package uk.co.zac_h.spacex.feature.astronauts

import uk.co.zac_h.spacex.network.datasource.remote.AstronautPagingSource
import uk.co.zac_h.spacex.network.retrofit.LaunchLibraryClient
import uk.co.zac_h.spacex.network.retrofit.LaunchLibraryService
import javax.inject.Inject

class AstronautRepository @Inject constructor(
    @LaunchLibraryClient private val httpService: LaunchLibraryService
) {

    val astronautPagingSource: AstronautPagingSource
        get() = AstronautPagingSource(httpService)
}
