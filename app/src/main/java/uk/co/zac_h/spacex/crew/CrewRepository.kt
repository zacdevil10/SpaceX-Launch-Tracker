package uk.co.zac_h.spacex.crew

import uk.co.zac_h.spacex.network.datasource.remote.AstronautPagingSource
import uk.co.zac_h.spacex.network.retrofit.LaunchLibraryClient
import uk.co.zac_h.spacex.network.retrofit.LaunchLibraryService
import javax.inject.Inject

class CrewRepository @Inject constructor(
    @LaunchLibraryClient private val httpService: LaunchLibraryService
) {

    val astronautPagingSource: AstronautPagingSource
        get() = AstronautPagingSource(httpService)
}
