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

class RedditAPITest : BaseNetwork() {

    private var mWebServer = MockWebServer()

    private lateinit var redditInterface: RedditInterface

    @Before
    fun setup() {
        val dispatcher = object : Dispatcher() {
            override fun dispatch(request: RecordedRequest): MockResponse {
                return when (request.path) {
                    "/r/spacex/hot/.json?raw_json=1&count=20" -> MockResponse().apply {
                        setResponseCode(HttpURLConnection.HTTP_OK)
                        setBody(RESTServiceTestHelper().getJSON("reddit.json"))
                    }
                    else -> MockResponse().setResponseCode(404)
                }
            }
        }

        mWebServer.apply {
            this.dispatcher = dispatcher
            start()
        }

        redditInterface = Retrofit.Builder().apply {
            baseUrl(mWebServer.url("/"))
            addConverterFactory(MoshiConverterFactory.create())
        }.build().create(RedditInterface::class.java)
    }

    @After
    fun teardown() {
        mWebServer.shutdown()
    }

    @Test
    fun `Get reddit post`() {
        runBlocking {
            redditInterface.getRedditFeed("spacex").makeCall {
                onResponseSuccess = {
                    assert(it.isSuccessful)

                    it.body()?.data?.children?.get(0)?.data?.run {
                        assert(textHtml == "Test self text html")
                        assert(title == "Test Title")
                        assert(name == "name")
                        assert(author == "Author")
                        assert(created == 1578447831.0f)
                        assert(thumbnail == "self")
                        assert(score == 421)
                        assert(commentsCount == 1581)
                        assert(domain == "self.spacex")
                        assert(stickied)
                        assert(isSelf)
                        assert(!redditDomain)
                        assert(permalink == "permalink")

                        preview!!.images[0].run {
                            assert(source.url == "source url")
                            assert(source.width == 898)
                            assert(source.height == 1200)

                            assert(resolutions[0].url == "resolution url")
                            assert(resolutions[0].width == 108)
                            assert(resolutions[0].height == 144)
                        }
                    }
                }
            }
        }
    }
}