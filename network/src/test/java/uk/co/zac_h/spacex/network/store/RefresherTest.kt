package uk.co.zac_h.spacex.network.store

import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import okhttp3.ResponseBody.Companion.toResponseBody
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import retrofit2.HttpException
import retrofit2.Response
import uk.co.zac_h.spacex.network.ApiResult
import uk.co.zac_h.spacex.network.TooManyRequestsException
import kotlin.time.Duration.Companion.minutes

class RefresherTest {

    private var now = 1_000_000L
    private var calls = 0
    private lateinit var refresher: Refresher

    @Before
    fun setUp() {
        refresher = Refresher(FakeFetchMetadataDao()) { now }
    }

    private fun refresh(force: Boolean = false, fetch: () -> Unit = {}) = runBlocking {
        refresher.refresh("key", 15.minutes, force) {
            calls++
            fetch()
        }
    }

    private fun fetchedAt() = runBlocking { refresher.fetchedAt("key").first() }

    @Test
    fun `never fetched is stale`() {
        assertTrue(runBlocking { refresher.isStale("key", 15.minutes) })
        assertNull(fetchedAt())
    }

    @Test
    fun `first refresh fetches and records the time`() {
        assertEquals(ApiResult.Success(Unit), refresh())
        assertEquals(1, calls)
        assertEquals(now, fetchedAt())
    }

    @Test
    fun `refresh inside the window makes no call`() {
        refresh()
        now += 14.minutes.inWholeMilliseconds

        assertEquals(ApiResult.Success(Unit), refresh())
        assertEquals(1, calls)
    }

    @Test
    fun `refresh after the window fetches again`() {
        refresh()
        now += 15.minutes.inWholeMilliseconds

        refresh()
        assertEquals(2, calls)
        assertEquals(now, fetchedAt())
    }

    @Test
    fun `force fetches inside the window`() {
        refresh()
        refresh(force = true)
        assertEquals(2, calls)
    }

    @Test
    fun `failure keeps the previous fetch time`() {
        refresh()
        val firstFetch = now
        now += 20.minutes.inWholeMilliseconds

        val result = refresh { error("offline") }

        assertTrue(result is ApiResult.Failure)
        assertEquals(firstFetch, fetchedAt())
    }

    @Test
    fun `429 becomes TooManyRequestsException with the wait`() {
        val body = """{"detail":"Request was throttled. Expected available in 120 seconds."}"""
        val result = refresh { throw HttpException(Response.error<Any>(429, body.toResponseBody())) }

        val exception = (result as ApiResult.Failure).exception
        assertTrue(exception is TooManyRequestsException)
        assertEquals(120, (exception as TooManyRequestsException).time)
    }
}
