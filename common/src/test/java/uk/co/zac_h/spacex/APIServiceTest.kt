package uk.co.zac_h.spacex

import kotlinx.coroutines.runBlocking
import okhttp3.mockwebserver.Dispatcher
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import okhttp3.mockwebserver.RecordedRequest
import org.junit.After
import org.junit.Before
import org.junit.Test
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import uk.co.zac_h.spacex.rest.SpaceXInterface
import java.net.HttpURLConnection

class APIServiceTest {

    private var mWebServer = MockWebServer()

    private lateinit var spaceXInterface: SpaceXInterface

    @Before
    fun setup() {
        val dispatcher = object : Dispatcher() {
            @Throws(InterruptedException::class)
            override fun dispatch(request: RecordedRequest): MockResponse {
                return when (request.path) {
                    "/launches/1" -> MockResponse().apply {
                        setResponseCode(HttpURLConnection.HTTP_OK)
                        setBody(RESTServiceTestHelper().getJSON("launch.json"))
                    }
                    "/launches/0" -> MockResponse().apply {
                        setResponseCode(HttpURLConnection.HTTP_NOT_FOUND)
                    }
                    else -> MockResponse().setResponseCode(404)
                }
            }
        }

        mWebServer.apply {
            this.dispatcher = dispatcher
            start()
        }

        spaceXInterface = Retrofit.Builder().apply {
            baseUrl(mWebServer.url("/"))
            addConverterFactory(MoshiConverterFactory.create())
        }.build().create(SpaceXInterface::class.java)
    }

    @After
    fun teardown() {
        mWebServer.shutdown()
    }

    @Test
    fun spacexServiceTest() {
        runBlocking {
            val launch = spaceXInterface.getSingleLaunch("1")

            assert(launch.body()?.flightNumber == 1)

            assert(launch.isSuccessful)
        }
    }

    @Test
    fun invalidLaunchNumber() {
        runBlocking {
            val invalidLaunch = spaceXInterface.getSingleLaunch("0")

            assert(invalidLaunch.code() == 404)
        }
    }
}