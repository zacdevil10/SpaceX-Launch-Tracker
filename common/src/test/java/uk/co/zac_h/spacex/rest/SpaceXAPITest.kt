package uk.co.zac_h.spacex.rest

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
import java.net.HttpURLConnection

class SpaceXAPITest {

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
                    else -> MockResponse().setResponseCode(404).setBody(
                        RESTServiceTestHelper().getJSON(
                            "error.json"
                        )
                    )
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
    fun getSingleLaunchFromSpaceXAPI() {
        runBlocking {
            val launch = spaceXInterface.getSingleLaunch("1")
            val invalidLaunch = spaceXInterface.getSingleLaunch("0")

            assert(launch.isSuccessful)
            assert(!invalidLaunch.isSuccessful)

            launch.body()?.run {
                assert(flightNumber == 1)
                assert(missionName == "FalconSat")
                assert(missionId.isEmpty())
                assert(launchDateUnix == 1143239400L)
                assert(tentative == false)
                assert(tentativeMaxPrecision == "hour")
                assert(tbd == false)
                assert(launchWindow == 0)

                assert(ships.isEmpty())

                assert(success == false)

                assert(details == "Details")
                assert(!upcoming)
                assert(staticFireDateUTC == "2006-03-17T00:00:00.000Z")
                assert(staticFireDateUnix == 1142553600L)
            }
        }
    }
}