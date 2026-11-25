package uk.co.zac_h.spacex.network.retrofit

import mockwebserver3.MockResponse
import mockwebserver3.MockWebServer
import okhttp3.OkHttpClient
import okhttp3.Request
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Before
import org.junit.Test
import uk.co.zac_h.spacex.network.RateLimitGate

class RateLimitInterceptorTest {

    private var now = 1_000_000L
    private var lookUps = 0
    private var lookUpResult: Long? = null

    private lateinit var server: MockWebServer
    private lateinit var gate: RateLimitGate
    private lateinit var client: OkHttpClient

    @Before
    fun setUp() {
        server = MockWebServer().apply { start() }
        gate = RateLimitGate().apply { clock = { now } }
        client = OkHttpClient.Builder()
            .addInterceptor(
                RateLimitInterceptor(gate) {
                    lookUps++
                    lookUpResult
                }
            )
            .build()
    }

    @After
    fun tearDown() {
        server.close()
    }

    private fun get(): Pair<Int, String> =
        client.newCall(Request.Builder().url(server.url("/2.3.0/launches/upcoming/")).build())
            .execute().use { it.code to it.body.string() }

    private fun throttled(body: String) = MockResponse.Builder().code(429).body(body).build()

    @Test
    fun `429 closes the gate for the wait in the body`() {
        server.enqueue(throttled("""{"detail":"Request was throttled. Expected available in 120 seconds."}"""))

        val (code, body) = get()

        assertEquals(429, code)
        assertEquals(120L, RateLimitInterceptor.secondsFromThrottleMessage(body))
        assertEquals(120L, gate.remainingSeconds())
        assertEquals(0, lookUps)
    }

    @Test
    fun `while the gate is closed no call reaches the API`() {
        server.enqueue(throttled("Expected available in 120 seconds."))
        get()
        now += 30_000

        val (code, body) = get()

        assertEquals(429, code)
        assertEquals(90L, RateLimitInterceptor.secondsFromThrottleMessage(body))
        assertEquals(1, server.requestCount)
    }

    @Test
    fun `calls resume once the wait has passed`() {
        server.enqueue(throttled("Expected available in 60 seconds."))
        server.enqueue(MockResponse.Builder().code(200).body("ok").build())
        get()
        now += 60_000

        assertEquals(200 to "ok", get())
        assertNull(gate.remainingSeconds())
        assertEquals(2, server.requestCount)
    }

    @Test
    fun `429 without a wait asks api-throttle`() {
        lookUpResult = 600
        server.enqueue(throttled("Too many requests"))

        get()

        assertEquals(1, lookUps)
        assertEquals(600L, gate.remainingSeconds())
    }

    @Test
    fun `429 with no wait anywhere falls back to the default`() {
        server.enqueue(throttled(""))

        get()

        assertEquals(RateLimitInterceptor.DEFAULT_WAIT_SECONDS, gate.remainingSeconds())
    }

    @Test
    fun `reads next_use_secs from api-throttle`() {
        val body = """[{"your_request_limit":15,"limit_frequency_secs":3600,"current_use":15,"next_use_secs":1234,"ident":"1.2.3.4"}]"""

        assertEquals(1234L, RateLimitInterceptor.secondsFromThrottleEndpoint(body))
        assertNull(RateLimitInterceptor.secondsFromThrottleEndpoint("""[{"next_use_secs":0}]"""))
        assertNull(RateLimitInterceptor.secondsFromThrottleEndpoint("not json"))
    }
}
