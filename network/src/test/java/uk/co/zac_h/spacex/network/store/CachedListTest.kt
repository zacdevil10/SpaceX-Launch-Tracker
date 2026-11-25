package uk.co.zac_h.spacex.network.store

import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import kotlinx.serialization.builtins.serializer
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
import uk.co.zac_h.spacex.network.dto.spacex.LaunchLibraryPaginatedResponse
import uk.co.zac_h.spacex.network.launchlibrary.model.LaunchDetailedDto
import uk.co.zac_h.spacex.network.retrofit.networkJson
import kotlin.time.Duration.Companion.minutes

class CachedListTest {

    private var now = 1_000_000L
    private var calls = 0
    private lateinit var itemDao: FakeCachedItemDao
    private lateinit var metadataDao: FakeFetchMetadataDao
    private lateinit var list: CachedList<String>

    @Before
    fun setUp() {
        itemDao = FakeCachedItemDao()
        metadataDao = FakeFetchMetadataDao()
        list = CachedList("names", String.serializer(), { it }, itemDao, metadataDao, networkJson) { now }
    }

    private fun refresh(force: Boolean = false, result: () -> List<String> = { listOf("a", "b") }) =
        runBlocking {
            list.refresh(maxAge = 15.minutes, force = force) {
                calls++
                result()
            }
        }

    private fun items() = runBlocking { list.items.first() }

    @Test
    fun `first refresh fetches and stores in order`() {
        assertEquals(ApiResult.Success(Unit), refresh { listOf("b", "a", "c") })

        assertEquals(listOf("b", "a", "c"), items())
        assertEquals(now, runBlocking { list.fetchedAt.first() })
        assertEquals(1, calls)
    }

    @Test
    fun `refresh inside the window makes no call`() {
        refresh()
        now += 14.minutes.inWholeMilliseconds

        assertEquals(ApiResult.Success(Unit), refresh())
        assertEquals(1, calls)
    }

    @Test
    fun `refresh after the window fetches again and replaces the list`() {
        refresh { listOf("a", "b") }
        now += 15.minutes.inWholeMilliseconds

        refresh { listOf("c") }

        assertEquals(2, calls)
        assertEquals(listOf("c"), items())
    }

    @Test
    fun `force fetches inside the window`() {
        refresh()
        refresh(force = true)

        assertEquals(2, calls)
    }

    @Test
    fun `failed refresh keeps the stored list and its fetch time`() {
        refresh { listOf("a") }
        val fetchedAt = now
        now += 20.minutes.inWholeMilliseconds

        val result = refresh { error("offline") }

        assertTrue(result is ApiResult.Failure)
        assertEquals(listOf("a"), items())
        assertEquals(fetchedAt, runBlocking { list.fetchedAt.first() })
    }

    @Test
    fun `429 becomes TooManyRequestsException with the wait`() {
        val body = """{"detail":"Request was throttled. Expected available in 120 seconds."}"""
        val result = refresh {
            throw HttpException(Response.error<Any>(429, body.toResponseBody()))
        }

        val exception = (result as ApiResult.Failure).exception
        assertTrue(exception is TooManyRequestsException)
        assertEquals(120, (exception as TooManyRequestsException).time)
    }

    @Test
    fun `never fetched means stale and no fetch time`() {
        assertTrue(runBlocking { list.isStale(15.minutes) })
        assertNull(runBlocking { list.fetchedAt.first() })
    }

    @Test
    fun `real upcoming launches survive a round trip through the database`() {
        val text = checkNotNull(javaClass.classLoader?.getResource("launchlibrary/upcoming.json")).readText()
        val launches = networkJson.decodeFromString<LaunchLibraryPaginatedResponse<LaunchDetailedDto>>(text).results
        val store = LaunchLibraryStore(itemDao, metadataDao) { now }

        runBlocking { store.upcomingLaunches.replace(launches) }

        assertEquals(launches, runBlocking { store.upcomingLaunches.items.first() })
    }
}
