package uk.co.zac_h.spacex.network.store

import androidx.paging.PagingData
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import uk.co.zac_h.spacex.core.database.DocumentDao
import uk.co.zac_h.spacex.core.database.DocumentEntity
import uk.co.zac_h.spacex.core.database.ListItemDao
import uk.co.zac_h.spacex.network.ApiResult
import uk.co.zac_h.spacex.network.LAUNCH_LIBRARY_PAGE_SIZE
import uk.co.zac_h.spacex.network.launchlibrary.model.AgencyEndpointDetailedDto
import uk.co.zac_h.spacex.network.launchlibrary.model.AstronautEndpointNormalDto
import uk.co.zac_h.spacex.network.launchlibrary.model.LauncherDetailedDto
import uk.co.zac_h.spacex.network.launchlibrary.model.SpacecraftNormalDto
import uk.co.zac_h.spacex.network.retrofit.LaunchLibraryClient
import uk.co.zac_h.spacex.network.retrofit.LaunchLibraryService
import uk.co.zac_h.spacex.network.retrofit.networkJson
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Everything from Launch Library other than launches, with the database as the source of truth:
 * the SpaceX agency (company details, rockets and Dragon) and the launcher, spacecraft and
 * astronaut lists. These change rarely, so each is fetched at most once in its window.
 */
@Singleton
class AssetStore @Inject constructor(
    @LaunchLibraryClient private val httpService: LaunchLibraryService,
    listItemDao: ListItemDao,
    private val documentDao: DocumentDao,
    private val refresher: Refresher
) {

    private val launchers = ListStore(
        list = LAUNCHERS,
        maxAge = Freshness.LAUNCHERS,
        serializer = LauncherDetailedDto.serializer(),
        id = { it.id },
        listItemDao = listItemDao,
        refresher = refresher
    ) { limit, offset -> httpService.getLaunchers(limit = limit, offset = offset) }

    private val spacecraft = ListStore(
        list = SPACECRAFT,
        maxAge = Freshness.SPACECRAFT,
        serializer = SpacecraftNormalDto.serializer(),
        id = { it.id },
        listItemDao = listItemDao,
        refresher = refresher
    ) { limit, offset -> httpService.getSpacecrafts(limit = limit, offset = offset) }

    private val astronauts = ListStore(
        list = ASTRONAUTS,
        maxAge = Freshness.ASTRONAUTS,
        serializer = AstronautEndpointNormalDto.serializer(),
        id = { it.id },
        listItemDao = listItemDao,
        refresher = refresher
    ) { limit, offset -> httpService.getAstronauts(limit = limit, offset = offset) }

    fun launchers(pageSize: Int = LAUNCH_LIBRARY_PAGE_SIZE): Flow<PagingData<LauncherDetailedDto>> =
        launchers.items(pageSize)

    fun spacecraft(pageSize: Int = LAUNCH_LIBRARY_PAGE_SIZE): Flow<PagingData<SpacecraftNormalDto>> =
        spacecraft.items(pageSize)

    fun astronauts(pageSize: Int = LAUNCH_LIBRARY_PAGE_SIZE): Flow<PagingData<AstronautEndpointNormalDto>> =
        astronauts.items(pageSize)

    /** The SpaceX agency, or null until it has been fetched once. */
    val agency: Flow<AgencyEndpointDetailedDto?> = documentDao.observe(AGENCY).map { document ->
        document?.let {
            runCatching {
                networkJson.decodeFromString(AgencyEndpointDetailedDto.serializer(), it.json)
            }.getOrNull()
        }
    }

    /** Fetches the agency if the stored one is older than its window, or if [force]. */
    suspend fun refreshAgency(force: Boolean = false): ApiResult<Unit> =
        refresher.refresh(AGENCY, Freshness.AGENCY, force) {
            val agency = httpService.getAgency()
            documentDao.upsert(
                DocumentEntity(
                    key = AGENCY,
                    json = networkJson.encodeToString(AgencyEndpointDetailedDto.serializer(), agency)
                )
            )
        }

    companion object {
        internal const val AGENCY = "agency"
        internal const val LAUNCHERS = "launchers"
        internal const val SPACECRAFT = "spacecraft"
        internal const val ASTRONAUTS = "astronauts"
    }
}
