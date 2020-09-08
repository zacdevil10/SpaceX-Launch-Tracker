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
import uk.co.zac_h.BuildConfig
import uk.co.zac_h.spacex.model.twitter.OAuthKeys
import uk.co.zac_h.spacex.utils.BaseNetwork
import uk.co.zac_h.spacex.utils.OAuthSigningInterceptor
import java.net.HttpURLConnection

class TwitterAPITest : BaseNetwork() {

    private var mWebServer = MockWebServer()

    private lateinit var twitterInterface: TwitterInterface

    @Before
    fun setup() {
        val dispatcher = object : Dispatcher() {
            override fun dispatch(request: RecordedRequest): MockResponse {
                return when (request.path) {
                    "/statuses/user_timeline.json?screen_name=spacex&include_rts=false&trim_user=false&tweet_mode=extended&count=1" -> MockResponse().apply {
                        setResponseCode(HttpURLConnection.HTTP_OK)
                        setBody(RESTServiceTestHelper().getJSON("tweet.json"))
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

        twitterInterface = Retrofit.Builder().apply {
            baseUrl(mWebServer.url("/"))
            addConverterFactory(MoshiConverterFactory.create())
            client(
                OAuthSigningInterceptor.addKeys(
                    OAuthKeys(
                        BuildConfig.CONSUMER_KEY,
                        BuildConfig.CONSUMER_SECRET,
                        BuildConfig.ACCESS_TOKEN,
                        BuildConfig.TOKEN_SECRET
                    )
                ).build()
            )
        }.build().create(TwitterInterface::class.java)
    }

    @After
    fun teardown() {
        mWebServer.shutdown()
    }

    @Test
    fun `Get Tweet with all possible data attached`() {
        runBlocking {
            twitterInterface.getTweets(
                "spacex",
                rts = false,
                trim = false,
                mode = "extended",
                count = 1
            ).makeCall {
                onResponseSuccess = {
                    assert(it.isSuccessful)

                    it.body()?.get(0)?.run {
                        assert(created == "Mon Feb 17 15:23:23 +0000 2020")
                        assert(id == 1229426122720346113L)
                        assert(text == "Sample Text")

                        assert(entities.hashtags[0].tag == "HashTag")
                        assert(entities.mentions[0].id == 1451773004L)
                        assert(entities.mentions[0].screenName == "Test")
                        assert(entities.mentions[0].name == "Testing Name")
                        assert(entities.urls[0].url == "https://t.co/test")
                        assert(entities.urls[0].expandedUrl == "https://twitter.com/i/test")
                        assert(entities.urls[0].displayUrl == "twitter.com/i/t...")

                        extendedEntities!!.media!![0].run {
                            assert(url == "media url https")
                            assert(type == "video")
                            info!!.run {
                                assert(aspectRatio!![0] == 16)
                                assert(aspectRatio!![1] == 9)
                                assert(duration == 6540)
                                assert(variants!!.size == 2)
                            }
                        }

                        assert(replyStatusId == null)
                        assert(!isQuote)
                    }
                }
            }
        }
    }
}