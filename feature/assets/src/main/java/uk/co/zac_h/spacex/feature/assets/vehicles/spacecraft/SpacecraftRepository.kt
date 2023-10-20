package uk.co.zac_h.spacex.feature.assets.vehicles.spacecraft

import uk.co.zac_h.spacex.network.datasource.remote.vehicles.SpacecraftPagingSource
import uk.co.zac_h.spacex.network.retrofit.LaunchLibraryClient
import uk.co.zac_h.spacex.network.retrofit.LaunchLibraryService
import javax.inject.Inject

class SpacecraftRepository @Inject constructor(
    @LaunchLibraryClient private val httpService: LaunchLibraryService
) {

    val spacecraftPagingSource: SpacecraftPagingSource
        get() = SpacecraftPagingSource(httpService)
}