package uk.co.zac_h.spacex.network.store

import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import kotlinx.serialization.builtins.ListSerializer
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import okhttp3.ResponseBody.Companion.toResponseBody
import retrofit2.HttpException
import retrofit2.Response
import uk.co.zac_h.spacex.network.ApiResult
import uk.co.zac_h.spacex.network.CelestrakRateLimitedException
import uk.co.zac_h.spacex.network.celestrakRetryAt
import uk.co.zac_h.spacex.network.dto.celestrak.GpElementDto
import uk.co.zac_h.spacex.network.retrofit.CelestrakService
import uk.co.zac_h.spacex.network.retrofit.networkJson
import kotlin.time.Duration.Companion.hours

class StarlinkStoreTest {

    private var now = 1_800_000_000_000L
    private var calls = 0
    private var response: List<GpElementDto> = emptyList()
    private var failWith: Exception? = null
    private val dao = FakeStarlinkDao()
    private lateinit var store: StarlinkStore

    private val service = object : CelestrakService {
        override suspend fun getGroup(group: String, format: String): List<GpElementDto> {
            calls++
            assertEquals("starlink", group)
            failWith?.let { throw it }
            return response
        }
    }

    @Before
    fun setUp() {
        response = networkJson.decodeFromString(ListSerializer(GpElementDto.serializer()), resource())
        val metadata = FakeFetchMetadataDao()
        store = StarlinkStore(service, dao, Refresher(metadata) { now }, metadata) { now }
    }

    private fun resource(): String =
        checkNotNull(javaClass.classLoader?.getResource("celestrak/starlink.json")).readText()

    @Test
    fun `parses CelesTrak OMM json into entities`() {
        val entity = checkNotNull(response.first().toEntity())
        assertEquals(44713, entity.noradId)
        assertEquals("STARLINK-1007", entity.name)
        // 2026-11-25T05:46:04.143Z
        assertEquals(1795585564143L, entity.epoch)
        assertEquals(15.06395117, entity.meanMotion, 0.0)
        assertEquals(0.00031023, entity.bstar, 0.0)
    }

    @Test
    fun `a record with an unreadable epoch is skipped`() {
        assertNull(response.first().copy(epoch = "yesterday").toEntity())
    }

    @Test
    fun `refresh stores the group and is skipped while fresh`() = runBlocking {
        assertEquals(ApiResult.Success(Unit), store.refresh())
        assertEquals(2, store.satellites.first().size)
        assertEquals(now, store.fetchedAt.first())

        now += 23.hours.inWholeMilliseconds
        store.refresh()
        assertEquals(1, calls)

        now += 1.hours.inWholeMilliseconds
        store.refresh()
        assertEquals(2, calls)
        assertEquals(2, store.satellites.first().size)
    }

    @Test
    fun `an empty response keeps the stored satellites`() = runBlocking {
        store.refresh()
        response = emptyList()
        val result = store.refresh(force = true)
        assertTrue(result is ApiResult.Failure)
        assertEquals(2, store.satellites.first().size)
    }

    @Test
    fun `a 403 waits until two hours after the last download before asking again`() = runBlocking {
        // 2027-01-15T08:00:00Z
        now = 1_800_000_000_000L
        val lastDownload = now - 30 * 60_000L
        failWith = HttpException(
            Response.error<Any>(
                403,
                ("GP data has not updated since your last successful download of GROUP=starlink at " +
                    "2027-01-15 07:30:00 UTC. Data is updated once every 2 hours.").toResponseBody()
            )
        )
        val result = store.refresh()
        val error = (result as ApiResult.Failure).exception as CelestrakRateLimitedException
        assertEquals(lastDownload + 2.hours.inWholeMilliseconds, error.retryAtMillis)

        // Retrying, even forced, doesn't reach CelesTrak again until then.
        now += 60.minutes()
        assertTrue((store.refresh(force = true) as ApiResult.Failure).exception is CelestrakRateLimitedException)
        assertEquals(1, calls)

        now = error.retryAtMillis
        failWith = null
        assertEquals(ApiResult.Success(Unit), store.refresh())
        assertEquals(2, calls)
    }

    @Test
    fun `retry time falls back to two hours from now when the 403 body is unreadable`() {
        assertEquals(1_000L + 2.hours.inWholeMilliseconds, celestrakRetryAt("Forbidden", 1_000L))
        assertEquals(1_000L + 2.hours.inWholeMilliseconds, celestrakRetryAt(null, 1_000L))
    }

    private fun Int.minutes() = this * 60_000L
}
