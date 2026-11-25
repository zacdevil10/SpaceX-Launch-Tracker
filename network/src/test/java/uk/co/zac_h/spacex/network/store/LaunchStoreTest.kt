package uk.co.zac_h.spacex.network.store

import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNull
import org.junit.Before
import org.junit.Test
import uk.co.zac_h.spacex.network.dto.spacex.LaunchLibraryPaginatedResponse
import uk.co.zac_h.spacex.network.launchlibrary.model.LaunchDetailedDto
import uk.co.zac_h.spacex.network.retrofit.LaunchLibraryService
import uk.co.zac_h.spacex.network.retrofit.networkJson
import java.lang.reflect.Proxy

class LaunchStoreTest {

    private var now = 0L
    private lateinit var launchDao: FakeLaunchDao
    private lateinit var store: LaunchStore

    /** Real upcoming launches captured from Launch Library (src/test/resources). */
    private val fixture: List<LaunchDetailedDto> by lazy {
        val text = checkNotNull(javaClass.classLoader?.getResource("launchlibrary/upcoming.json")).readText()
        networkJson.decodeFromString<LaunchLibraryPaginatedResponse<LaunchDetailedDto>>(text).results
    }

    /** A service the store must not call: these tests only exercise storage. */
    private val unusedService = Proxy.newProxyInstance(
        LaunchLibraryService::class.java.classLoader,
        arrayOf(LaunchLibraryService::class.java)
    ) { _, method, _ -> error("Unexpected call to ${method.name}") } as LaunchLibraryService

    @Before
    fun setUp() {
        launchDao = FakeLaunchDao()
        store = LaunchStore(unusedService, launchDao, Refresher(FakeFetchMetadataDao()) { now }) { now }
    }

    private fun upcoming() = runBlocking { store.upcomingLaunches.first() }

    private val anyStatus = fixture.firstNotNullOf { it.status }

    /** A launch from the fixture with the given id, launch time and status id (1 = Go). */
    private fun launch(id: String, net: String, statusId: Int = 1) =
        fixture.first().copy(id = id, net = net, status = anyStatus.copy(id = statusId))

    @Test
    fun `real upcoming launches survive a round trip through the database`() {
        runBlocking { store.storeUpcoming(fixture) }
        val expected = fixture.filter { it.isUpcoming(fromUpcomingEndpoint = true) }

        assertEquals(expected.sortedBy { it.net }.map { it.id }, upcoming().map { it.id })
        assertEquals(expected.toSet(), upcoming().toSet())
        assertEquals(fixture.size, launchDao.rows.value.size)
    }

    @Test
    fun `upcoming launches are ordered by launch time`() {
        runBlocking {
            store.storeUpcoming(
                listOf(launch("b", "2026-12-02T10:00:00Z"), launch("a", "2026-12-01T10:00:00Z"))
            )
        }

        assertEquals(listOf("a", "b"), upcoming().map { it.id })
    }

    @Test
    fun `a launch missing after its time has passed moves to previous`() {
        now = java.time.Instant.parse("2026-12-01T12:00:00Z").toEpochMilli()
        runBlocking {
            store.storeUpcoming(listOf(launch("flown", "2026-12-01T10:00:00Z"), launch("next", "2026-12-05T10:00:00Z")))
            store.storeUpcoming(listOf(launch("next", "2026-12-05T10:00:00Z")))
        }

        assertEquals(listOf("next"), upcoming().map { it.id })
        assertFalse(checkNotNull(launchDao.rows.value["flown"]).upcoming)
    }

    @Test
    fun `a launch missing before its time is deleted`() {
        now = java.time.Instant.parse("2026-12-01T12:00:00Z").toEpochMilli()
        runBlocking {
            store.storeUpcoming(listOf(launch("removed", "2026-12-03T10:00:00Z"), launch("next", "2026-12-05T10:00:00Z")))
            store.storeUpcoming(listOf(launch("next", "2026-12-05T10:00:00Z")))
        }

        assertEquals(listOf("next"), upcoming().map { it.id })
        assertNull(launchDao.rows.value["removed"])
    }

    @Test
    fun `a launch in flight stays in upcoming after its launch time`() {
        now = java.time.Instant.parse("2026-12-01T12:00:00Z").toEpochMilli()
        runBlocking { store.storeUpcoming(listOf(launch("flying", "2026-12-01T11:50:00Z", statusId = 6))) }

        assertEquals(listOf("flying"), upcoming().map { it.id })
    }

    @Test
    fun `a launch on hold stays in upcoming after its launch time`() {
        now = java.time.Instant.parse("2026-12-01T12:00:00Z").toEpochMilli()
        runBlocking { store.storeUpcoming(listOf(launch("hold", "2026-12-01T11:50:00Z", statusId = 5))) }

        assertEquals(listOf("hold"), upcoming().map { it.id })
    }

    @Test
    fun `a launch with a result moves to previous even while the upcoming endpoint still returns it`() {
        runBlocking {
            store.storeUpcoming(listOf(launch("done", "2026-12-01T10:00:00Z", statusId = 6)))
            store.storeUpcoming(listOf(launch("done", "2026-12-01T10:00:00Z", statusId = 3)))
        }

        assertEquals(emptyList<String>(), upcoming().map { it.id })
        assertFalse(checkNotNull(launchDao.rows.value["done"]).upcoming)
    }

    @Test
    fun `stores net and last updated as queryable columns`() {
        val launch = launch("a", "2026-12-01T10:00:00Z").copy(lastUpdated = "2026-11-20T08:30:00.123456Z")

        val entity = launch.toEntity(upcoming = true)

        assertEquals(java.time.Instant.parse("2026-12-01T10:00:00Z").toEpochMilli(), entity.net)
        assertEquals(java.time.Instant.parse("2026-11-20T08:30:00.123Z").toEpochMilli(), entity.lastUpdated)
    }
}
