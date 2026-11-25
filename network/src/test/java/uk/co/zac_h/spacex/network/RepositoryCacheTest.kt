package uk.co.zac_h.spacex.network

import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import uk.co.zac_h.spacex.network.datasource.remote.RemoteDataSource
import kotlin.time.Duration.Companion.hours
import kotlin.time.Duration.Companion.minutes

class RepositoryCacheTest {

    private class CountingDataSource : RemoteDataSource<String> {
        var calls = 0
        override suspend fun fetchAsync(): String = "response ${++calls}"
    }

    private class TestRepository(
        dataSource: RemoteDataSource<String>,
        cache: Cache<String>
    ) : Repository<String>(dataSource, cache)

    private var now = 0L
    private lateinit var dataSource: CountingDataSource
    private lateinit var repository: TestRepository

    @Before
    fun setUp() {
        now = 1_000_000L
        dataSource = CountingDataSource()
        val cache = Cache<String>().apply { clock = { now } }
        repository = TestRepository(dataSource, cache)
    }

    private fun fetch(maxAge: kotlin.time.Duration) = runBlocking {
        repository.fetch(key = "key", cachePolicy = CachePolicy.EXPIRES, maxAge = maxAge)
    }

    @Test
    fun `expires reuses a response younger than maxAge`() {
        fetch(1.minutes)
        now += 59_000
        val second = fetch(1.minutes)

        assertEquals(1, dataSource.calls)
        assertEquals(ApiResult.Success("response 1"), second)
    }

    @Test
    fun `expires refetches once maxAge has passed`() {
        fetch(1.minutes)
        now += 60_000
        val second = fetch(1.minutes)

        assertEquals(2, dataSource.calls)
        assertEquals(ApiResult.Success("response 2"), second)
    }

    @Test
    fun `agency window keeps one response for a day`() {
        fetch(24.hours)
        now += 23.hours.inWholeMilliseconds
        fetch(24.hours)

        assertEquals(1, dataSource.calls)
    }

    @Test
    fun `failures are not cached`() {
        val failing = object : RemoteDataSource<String> {
            var calls = 0
            override suspend fun fetchAsync(): String {
                calls++
                error("offline")
            }
        }
        val repo = TestRepository(failing, Cache<String>().apply { clock = { now } })

        val first = runBlocking { repo.fetch("key", CachePolicy.EXPIRES, 1.minutes) }
        runBlocking { repo.fetch("key", CachePolicy.EXPIRES, 1.minutes) }

        assertTrue(first is ApiResult.Failure)
        assertEquals(2, failing.calls)
    }
}
