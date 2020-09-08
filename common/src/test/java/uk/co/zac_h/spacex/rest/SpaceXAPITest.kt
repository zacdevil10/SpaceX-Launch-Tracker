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
import uk.co.zac_h.spacex.utils.BaseNetwork
import java.net.HttpURLConnection

class SpaceXAPITest : BaseNetwork() {

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
            spaceXInterface.getSingleLaunch("1").makeCall {
                onResponseSuccess = {
                    assert(it.isSuccessful)

                    it.body()?.run {
                        assert(flightNumber == 1)
                        assert(missionName == "Name")
                        assert(id == "5eb87d46ffd86e000604b389")
                        assert(launchDateUnix == 1592040060L)
                        assert(datePrecision == "hour")
                        assert(!tbd)
                        assert(window == 0)

                        assert(ships.isEmpty())

                        assert(success == false)

                        assert(details == "Details")
                        assert(!upcoming)
                    }
                }
            }

            spaceXInterface.getSingleLaunch("0").makeCall {
                onResponseFailure = {
                    assert(it == "Error: 404")
                }
            }
        }
    }
}