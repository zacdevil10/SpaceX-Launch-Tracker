package uk.co.zac_h.spacex.network.store

import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import uk.co.zac_h.spacex.network.ApiResult
import uk.co.zac_h.spacex.network.TooManyRequestsException
import uk.co.zac_h.spacex.network.launchlibrary.model.AgencyEndpointDetailedDto
import uk.co.zac_h.spacex.network.launchlibrary.model.LauncherDetailedDto
import kotlin.time.Duration.Companion.hours

class AssetStoreTest {

    private var now = 0L
    private lateinit var service: FakeLaunchLibraryService
    private lateinit var listItemDao: FakeListItemDao
    private lateinit var refresher: Refresher
    private lateinit var store: AssetStore

    private val spacex = AgencyEndpointDetailedDto(id = 121, name = "SpaceX")

    @Before
    fun setUp() {
        service = FakeLaunchLibraryService()
        listItemDao = FakeListItemDao()
        refresher = Refresher(FakeFetchMetadataDao()) { now }
        store = AssetStore(service, listItemDao, FakeDocumentDao(), refresher)
    }

    private fun launchersStore() = ListStore(
        list = AssetStore.LAUNCHERS,
        maxAge = Freshness.LAUNCHERS,
        serializer = LauncherDetailedDto.serializer(),
        id = { it.id },
        listItemDao = listItemDao,
        refresher = refresher
    ) { limit, offset -> service.getLaunchers(limit = limit, offset = offset) }

    private fun launchers(count: Int) = (1..count).map { LauncherDetailedDto(id = it, serialNumber = "B$it") }

    @Test
    fun `the agency is stored and read back`() {
        service.agency = spacex

        val result = runBlocking { store.refreshAgency() }

        assertEquals(ApiResult.Success(Unit), result)
        assertEquals(spacex, runBlocking { store.agency.first() })
    }

    @Test
    fun `the agency is fetched once a day, however many screens ask`() {
        service.agency = spacex
        runBlocking {
            store.refreshAgency()
            store.refreshAgency()
            now += 23.hours.inWholeMilliseconds
            store.refreshAgency()
        }
        assertEquals(1, service.agencyCalls)

        now += 1.hours.inWholeMilliseconds
        runBlocking { store.refreshAgency() }
        assertEquals(2, service.agencyCalls)
    }

    @Test
    fun `nothing is stored when the agency fetch fails`() {
        service.agency = null

        val result = runBlocking { store.refreshAgency() }

        assertTrue(result is ApiResult.Failure)
        assertNull(runBlocking { store.agency.first() })
    }

    @Test
    fun `scrolling fetches each page once, from the end of the stored list`() {
        service.launchers = launchers(5)
        val launchers = launchersStore()

        runBlocking { launchers.refresh(pageSize = 2) }
        val end1 = runBlocking { launchers.loadMore(pageSize = 2) }
        val end2 = runBlocking { launchers.loadMore(pageSize = 2) }

        assertEquals(listOf(2 to 0, 2 to 2, 2 to 4), service.launcherCalls)
        assertEquals(false, end1)
        assertEquals(true, end2)
        assertEquals(listOf("1", "2", "3", "4", "5"), listItemDao.ids(AssetStore.LAUNCHERS))
    }

    @Test
    fun `an out of date list is replaced by its first page`() {
        service.launchers = launchers(5)
        val launchers = launchersStore()
        runBlocking {
            launchers.refresh(pageSize = 2)
            launchers.loadMore(pageSize = 2)
        }
        assertTrue(runBlocking { launchers.isFresh() })

        now += Freshness.LAUNCHERS.inWholeMilliseconds
        service.launchers = listOf(LauncherDetailedDto(id = 6, serialNumber = "B6")) + launchers(5)
        runBlocking { launchers.refresh(pageSize = 2) }

        assertEquals(listOf("6", "1"), listItemDao.ids(AssetStore.LAUNCHERS))
    }

    @Test
    fun `a fresh list is not fetched again`() {
        service.launchers = launchers(5)
        val launchers = launchersStore()

        runBlocking {
            launchers.refresh(pageSize = 2)
            launchers.refresh(pageSize = 2)
        }

        assertEquals(1, service.launcherCalls.size)
    }

    @Test
    fun `a rate limited refresh keeps the stored list`() {
        service.launchers = launchers(2)
        val launchers = launchersStore()
        runBlocking { launchers.refresh(pageSize = 2) }
        now += Freshness.LAUNCHERS.inWholeMilliseconds
        val failing = ListStore(
            list = AssetStore.LAUNCHERS,
            maxAge = Freshness.LAUNCHERS,
            serializer = LauncherDetailedDto.serializer(),
            id = { it.id },
            listItemDao = listItemDao,
            refresher = refresher
        ) { _, _ -> throw TooManyRequestsException(60) }

        val result = runBlocking { failing.refresh(pageSize = 2) }

        assertTrue((result as ApiResult.Failure).exception is TooManyRequestsException)
        assertEquals(listOf("1", "2"), listItemDao.ids(AssetStore.LAUNCHERS))
    }
}
