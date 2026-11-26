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
import uk.co.zac_h.spacex.network.retrofit.networkJson
import uk.co.zac_h.spacex.network.ApiResult
import kotlin.time.Duration.Companion.minutes

class LaunchStoreTest {

    private var now = 0L
    private lateinit var launchDao: FakeLaunchDao
    private lateinit var store: LaunchStore

    /** Real upcoming launches captured from Launch Library (src/test/resources). */
    private val fixture: List<LaunchDetailedDto> by lazy {
        val text = checkNotNull(javaClass.classLoader?.getResource("launchlibrary/upcoming.json")).readText()
        networkJson.decodeFromString<LaunchLibraryPaginatedResponse<LaunchDetailedDto>>(text).results
    }

    private lateinit var service: FakeLaunchLibraryService
    private lateinit var callBudget: CallBudget

    @Before
    fun setUp() {
        launchDao = FakeLaunchDao()
        service = FakeLaunchLibraryService()
        callBudget = CallBudget { now }
        val metadataDao = FakeFetchMetadataDao()
        store = LaunchStore(service, launchDao, metadataDao, Refresher(metadataDao) { now }, callBudget) { now }
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

    private fun at(time: String) = java.time.Instant.parse(time).toEpochMilli()

    @Test
    fun `next launch refresh fetches all upcoming when they are due anyway`() {
        service.upcoming = listOf(launch("a", "2026-12-01T10:00:00Z"), launch("b", "2026-12-02T10:00:00Z"))

        runBlocking { store.refreshNextLaunch() }

        assertEquals(listOf(10 to null), service.upcomingCalls)
        assertEquals(listOf("a", "b"), upcoming().map { it.id })
    }

    @Test
    fun `next launch refresh asks for one launch without a result`() {
        service.upcoming = listOf(launch("a", "2026-12-01T10:00:00Z"), launch("b", "2026-12-02T10:00:00Z"))
        runBlocking { store.refreshUpcoming() }
        now += 2.minutes.inWholeMilliseconds
        service.upcomingCalls.clear()

        runBlocking { store.refreshNextLaunch() }

        assertEquals(listOf(1 to "1,2,5,6,8"), service.upcomingCalls)
    }

    @Test
    fun `next launch is not refetched within a minute`() {
        service.upcoming = listOf(launch("a", "2026-12-01T10:00:00Z"))
        runBlocking { store.refreshUpcoming() }
        service.upcomingCalls.clear()
        now += 30_000

        runBlocking { store.refreshNextLaunch() }

        assertEquals(emptyList<Pair<Int, String?>>(), service.upcomingCalls)
    }

    @Test
    fun `a different next launch refetches all upcoming`() {
        service.upcoming = listOf(launch("a", "2026-12-01T10:00:00Z"), launch("b", "2026-12-02T10:00:00Z"))
        runBlocking { store.refreshUpcoming() }
        now += 2.minutes.inWholeMilliseconds
        // "a" is delayed past "b".
        service.upcoming = listOf(launch("a", "2026-12-03T10:00:00Z"), launch("b", "2026-12-02T10:00:00Z"))
        service.upcomingCalls.clear()

        val result = runBlocking { store.refreshNextLaunch() }

        assertEquals(ApiResult.Success(Unit), result)
        assertEquals(listOf(1 to "1,2,5,6,8", 10 to null), service.upcomingCalls)
        assertEquals(listOf("b", "a"), upcoming().map { it.id })
    }

    @Test
    fun `polling stops before using the calls kept for screens`() {
        service.upcoming = listOf(launch("a", "2026-12-01T10:00:00Z"))
        repeat(CallBudget.LIMIT - CallBudget.RESERVED_FOR_SCREENS) { callBudget.record() }

        assertNull(runBlocking { store.pollNextLaunch() })
        assertEquals(emptyList<Pair<Int, String?>>(), service.upcomingCalls)
    }

    @Test
    fun `polls more often from an hour before launch`() {
        service.upcoming = listOf(launch("a", "2026-12-01T10:00:00Z"))
        runBlocking { store.refreshUpcoming() }

        now = at("2026-12-01T08:00:00Z")
        assertEquals(Freshness.NEXT_LAUNCH_POLL, runBlocking { store.nextLaunchPollInterval() })
        now = at("2026-12-01T09:00:00Z")
        assertEquals(Freshness.NEXT_LAUNCH_POLL_NEAR, runBlocking { store.nextLaunchPollInterval() })
        now = at("2026-12-01T10:30:00Z")
        assertEquals(Freshness.NEXT_LAUNCH_POLL_NEAR, runBlocking { store.nextLaunchPollInterval() })
    }

    /** A finished launch (status 3 = Success) with the given launch time and last change. */
    private fun flown(id: String, net: String, lastUpdated: String = net) =
        launch(id, net, statusId = 3).copy(lastUpdated = lastUpdated)

    private val history = listOf(
        flown("p1", "2026-11-20T10:00:00Z"),
        flown("p2", "2026-11-10T10:00:00Z"),
        flown("p3", "2026-11-01T10:00:00Z"),
        flown("p4", "2026-10-20T10:00:00Z"),
        flown("p5", "2026-10-10T10:00:00Z")
    )

    @Test
    fun `first open fetches the most recent page of previous launches`() {
        service.previous = history

        val result = runBlocking { store.refreshPreviousLaunches(pageSize = 2) }

        assertEquals(ApiResult.Success(Unit), result)
        assertEquals(listOf(Triple(2, null, null)), service.previousCalls)
        assertEquals(listOf("p1", "p2"), launchDao.previousIds())
    }

    @Test
    fun `scrolling fetches only launches older than the oldest stored`() {
        service.previous = history
        runBlocking { store.refreshPreviousLaunches(pageSize = 2) }

        val end1 = runBlocking { store.loadOlderPreviousLaunches(pageSize = 2) }
        val end2 = runBlocking { store.loadOlderPreviousLaunches(pageSize = 2) }

        assertEquals(
            listOf(Triple(2, null, null), Triple(2, "2026-11-10T10:00:00Z", null), Triple(2, "2026-10-20T10:00:00Z", null)),
            service.previousCalls
        )
        assertEquals(false, end1)
        assertEquals(true, end2)
        assertEquals(listOf("p1", "p2", "p3", "p4", "p5"), launchDao.previousIds())
    }

    @Test
    fun `previous launches are not checked again within the hour`() {
        service.previous = history
        runBlocking { store.refreshPreviousLaunches(pageSize = 2) }
        now += 59.minutes.inWholeMilliseconds

        runBlocking { store.refreshPreviousLaunches(pageSize = 2) }

        assertEquals(1, service.previousCalls.size)
    }

    @Test
    fun `later opens ask only for launches changed since the last sync`() {
        service.previous = history
        runBlocking { store.refreshPreviousLaunches(pageSize = 2) }
        runBlocking { store.loadOlderPreviousLaunches(pageSize = 2) }
        now += 61.minutes.inWholeMilliseconds
        // A new launch completes and an old one is corrected.
        service.previous = listOf(flown("p0", "2026-11-24T10:00:00Z", lastUpdated = "2026-11-24T12:00:00Z")) +
            history.map { if (it.id == "p3") it.copy(lastUpdated = "2026-11-24T13:00:00Z", name = "Corrected") else it }
        service.previousCalls.clear()

        runBlocking { store.refreshPreviousLaunches(pageSize = 2) }

        assertEquals(listOf(Triple(100, null, "2026-11-20T10:00:00Z")), service.previousCalls)
        assertEquals(listOf("p0", "p1", "p2", "p3", "p4"), launchDao.previousIds())
        val p3 = networkJson.decodeFromString(LaunchDetailedDto.serializer(), launchDao.rows.value.getValue("p3").json)
        assertEquals("Corrected", p3.name)
    }

    @Test
    fun `too many changes replaces previous launches with the most recent page`() {
        service.previous = history
        runBlocking { store.refreshPreviousLaunches(pageSize = 2) }
        runBlocking { store.loadOlderPreviousLaunches(pageSize = 2) }
        now += 61.minutes.inWholeMilliseconds
        service.previous = (1..101).map { flown("n$it", "2026-11-2${it % 5}T%02d:00:00Z".format(it % 24), "2026-11-25T00:00:00Z") } + history
        service.previousCalls.clear()

        runBlocking { store.refreshPreviousLaunches(pageSize = 2) }

        assertEquals(2, service.previousCalls.size)
        assertEquals(2, launchDao.previousIds().size)
    }

    @Test
    fun `a launch in flight from the previous endpoint stays in upcoming`() {
        service.previous = listOf(launch("flying", "2026-11-25T10:00:00Z", statusId = 6).copy(lastUpdated = "2026-11-25T10:01:00Z"))

        runBlocking { store.refreshPreviousLaunches(pageSize = 2) }

        assertEquals(emptyList<String>(), launchDao.previousIds())
        assertEquals(listOf("flying"), upcoming().map { it.id })
    }
}
