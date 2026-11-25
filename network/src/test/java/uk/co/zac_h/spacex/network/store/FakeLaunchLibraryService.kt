package uk.co.zac_h.spacex.network.store

import uk.co.zac_h.spacex.network.dto.spacex.LaunchLibraryPaginatedResponse
import uk.co.zac_h.spacex.network.launchlibrary.model.AgencyEndpointDetailedDto
import uk.co.zac_h.spacex.network.launchlibrary.model.AstronautEndpointNormalDto
import uk.co.zac_h.spacex.network.launchlibrary.model.LaunchDetailedDto
import uk.co.zac_h.spacex.network.launchlibrary.model.LauncherDetailedDto
import uk.co.zac_h.spacex.network.launchlibrary.model.SpacecraftNormalDto
import uk.co.zac_h.spacex.network.retrofit.LaunchLibraryService

/** Answers upcoming launch requests from [upcoming] and records each call. */
class FakeLaunchLibraryService : LaunchLibraryService {

    var upcoming: List<LaunchDetailedDto> = emptyList()
    val upcomingCalls = mutableListOf<Pair<Int, String?>>()

    override suspend fun getUpcomingLaunches(
        limit: Int,
        search: String,
        mode: String,
        statusIds: String?
    ): LaunchLibraryPaginatedResponse<LaunchDetailedDto> {
        upcomingCalls += limit to statusIds
        val ids = statusIds?.split(",")?.map { it.toInt() }?.toSet()
        val results = upcoming
            .filter { ids == null || it.status?.id in ids }
            .sortedBy { it.net }
            .take(limit)
        return LaunchLibraryPaginatedResponse(count = results.size, results = results)
    }

    override suspend fun getPreviousLaunches(limit: Int, offset: Int, search: String, mode: String) =
        unused()

    override suspend fun getLaunchers(
        limit: Int, offset: Int, launcherConfigManufacturerName: String, ordering: String,
        isPlaceholder: Boolean, mode: String
    ): LaunchLibraryPaginatedResponse<LauncherDetailedDto> = unused()

    override suspend fun getLaunchersForConfig(
        limit: Int, offset: Int, launcherConfigManufacturerName: String, ordering: String,
        isPlaceholder: Boolean, launcherConfigId: Int, mode: String
    ): LaunchLibraryPaginatedResponse<LauncherDetailedDto> = unused()

    override suspend fun getSpacecrafts(
        limit: Int, offset: Int, search: String, ordering: String, mode: String
    ): LaunchLibraryPaginatedResponse<SpacecraftNormalDto> = unused()

    override suspend fun getAstronauts(
        limit: Int, offset: Int, search: String, mode: String
    ): LaunchLibraryPaginatedResponse<AstronautEndpointNormalDto> = unused()

    override suspend fun getAgency(): AgencyEndpointDetailedDto = unused()

    private fun unused(): Nothing = error("Not used by these tests")
}
