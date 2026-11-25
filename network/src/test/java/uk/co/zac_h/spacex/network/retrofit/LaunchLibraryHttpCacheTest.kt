package uk.co.zac_h.spacex.network.retrofit

import mockwebserver3.MockResponse
import mockwebserver3.MockWebServer
import okhttp3.Cache
import okhttp3.OkHttpClient
import okhttp3.Request
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.rules.TemporaryFolder
import java.io.IOException
import kotlin.time.Duration.Companion.days
import kotlin.time.Duration.Companion.hours
import kotlin.time.Duration.Companion.minutes

class LaunchLibraryHttpCacheTest {

    @get:Rule
    val folder = TemporaryFolder()

    private lateinit var server: MockWebServer
    private lateinit var client: OkHttpClient

    @Before
    fun setUp() {
        server = MockWebServer().apply { start() }
        client = OkHttpClient.Builder()
            .cache(Cache(folder.newFolder("http"), 1024L * 1024))
            .addInterceptor(LaunchLibraryHttpCache.StaleFallbackInterceptor())
            .addNetworkInterceptor(LaunchLibraryHttpCache.MaxAgeInterceptor())
            .build()
    }

    @After
    fun tearDown() {
        server.close()
    }

    private fun get(path: String): Pair<Int, String> {
        val request = Request.Builder().url(server.url(path)).build()
        return client.newCall(request).execute().use { it.code to it.body.string() }
    }

    private fun ok(body: String) = MockResponse.Builder()
        .code(200)
        .body(body)
        // The API's own headers must not stop us caching.
        .addHeader("Cache-Control", "no-cache")
        .build()

    @Test
    fun `max age per endpoint`() {
        assertEquals(1.minutes, LaunchLibraryHttpCache.maxAgeFor("/2.3.0/launches/upcoming/"))
        assertEquals(1.hours, LaunchLibraryHttpCache.maxAgeFor("/2.3.0/launches/previous/"))
        assertEquals(1.days, LaunchLibraryHttpCache.maxAgeFor("/2.3.0/launchers/"))
        assertEquals(1.days, LaunchLibraryHttpCache.maxAgeFor("/2.3.0/spacecraft/"))
        assertEquals(7.days, LaunchLibraryHttpCache.maxAgeFor("/2.3.0/astronauts/"))
        assertEquals(7.days, LaunchLibraryHttpCache.maxAgeFor("/2.3.0/agencies/121/"))
        assertNull(LaunchLibraryHttpCache.maxAgeFor("/2.3.0/api-throttle/"))
    }

    @Test
    fun `repeat request inside the window is served from disk`() {
        server.enqueue(ok("first"))

        assertEquals(200 to "first", get("/2.3.0/launchers/?offset=0"))
        assertEquals(200 to "first", get("/2.3.0/launchers/?offset=0"))
        assertEquals(1, server.requestCount)
    }

    @Test
    fun `different pages are cached separately`() {
        server.enqueue(ok("page 1"))
        server.enqueue(ok("page 2"))

        get("/2.3.0/launchers/?offset=0")
        assertEquals(200 to "page 2", get("/2.3.0/launchers/?offset=50"))
        assertEquals(2, server.requestCount)
    }

    @Test
    fun `429 falls back to the cached response`() {
        server.enqueue(ok("cached"))
        get("/2.3.0/launches/upcoming/")

        // Make the cached copy stale by asking for a fresh one; the API refuses.
        server.enqueue(MockResponse.Builder().code(429).body("throttled").build())
        val request = Request.Builder()
            .url(server.url("/2.3.0/launches/upcoming/"))
            .header("Cache-Control", "no-cache")
            .build()
        val result = client.newCall(request).execute().use { it.code to it.body.string() }

        assertEquals(200 to "cached", result)
    }

    @Test
    fun `429 with nothing cached is passed through`() {
        server.enqueue(MockResponse.Builder().code(429).body("throttled").build())

        assertEquals(429 to "throttled", get("/2.3.0/launches/previous/"))
    }

    @Test
    fun `network failure falls back to the cached response`() {
        server.enqueue(ok("cached"))
        val url = server.url("/2.3.0/astronauts/")
        get("/2.3.0/astronauts/")
        server.close()

        val request = Request.Builder().url(url).header("Cache-Control", "no-cache").build()
        val result = client.newCall(request).execute().use { it.code to it.body.string() }

        assertEquals(200 to "cached", result)
    }

    @Test(expected = IOException::class)
    fun `network failure with nothing cached throws`() {
        val url = server.url("/2.3.0/astronauts/")
        server.close()

        client.newCall(Request.Builder().url(url).build()).execute()
    }
}
