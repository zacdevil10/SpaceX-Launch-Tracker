package uk.co.zac_h.spacex.network.store

import uk.co.zac_h.spacex.network.dto.spacex.LaunchLibraryPaginatedResponse
import uk.co.zac_h.spacex.network.launchlibrary.model.AgencyEndpointDetailedDto
import uk.co.zac_h.spacex.network.launchlibrary.model.AstronautEndpointNormalDto
import uk.co.zac_h.spacex.network.launchlibrary.model.LaunchDetailedDto
import uk.co.zac_h.spacex.network.launchlibrary.model.LauncherDetailedDto
import uk.co.zac_h.spacex.network.launchlibrary.model.SpacecraftNormalDto
import uk.co.zac_h.spacex.network.retrofit.LaunchLibraryService

/** Answers Launch Library requests from the lists set on it and records each call. */
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

    var previous: List<LaunchDetailedDto> = emptyList()

    /** (limit, net__lt, last_updated__gte) of each previous launches call. */
    val previousCalls = mutableListOf<Triple<Int, String?, String?>>()

    override suspend fun getPreviousLaunches(
        limit: Int,
        netLt: String?,
        lastUpdatedGte: String?,
        search: String,
        mode: String
    ): LaunchLibraryPaginatedResponse<LaunchDetailedDto> {
        previousCalls += Triple(limit, netLt, lastUpdatedGte)
        val matching = previous
            .filter { netLt == null || checkNotNull(it.net) < netLt }
            .filter { lastUpdatedGte == null || checkNotNull(it.lastUpdated) >= lastUpdatedGte }
            .sortedByDescending { it.net }
        val results = matching.take(limit)
        return LaunchLibraryPaginatedResponse(
            count = matching.size,
            next = if (matching.size > limit) "next" else null,
            results = results
        )
    }

    var launchers: List<LauncherDetailedDto> = emptyList()

    /** (limit, offset) of each launchers call. */
    val launcherCalls = mutableListOf<Pair<Int, Int>>()

    override suspend fun getLaunchers(
        limit: Int, offset: Int, launcherConfigManufacturerName: String, ordering: String,
        isPlaceholder: Boolean, mode: String
    ): LaunchLibraryPaginatedResponse<LauncherDetailedDto> {
        launcherCalls += limit to offset
        val results = launchers.drop(offset).take(limit)
        return LaunchLibraryPaginatedResponse(
            count = launchers.size,
            next = if (offset + limit < launchers.size) "next" else null,
            results = results
        )
    }

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

    var agency: AgencyEndpointDetailedDto? = null
    var agencyCalls = 0

    override suspend fun getAgency(): AgencyEndpointDetailedDto {
        agencyCalls++
        return checkNotNull(agency)
    }

    private fun unused(): Nothing = error("Not used by these tests")
}
