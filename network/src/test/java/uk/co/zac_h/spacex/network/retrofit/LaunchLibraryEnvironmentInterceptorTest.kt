package uk.co.zac_h.spacex.network.retrofit

import okhttp3.HttpUrl
import okhttp3.OkHttpClient
import okhttp3.Protocol
import okhttp3.Request
import okhttp3.Response
import okhttp3.ResponseBody.Companion.toResponseBody
import org.junit.Assert.assertEquals
import org.junit.Test
import uk.co.zac_h.spacex.network.LaunchLibraryEnvironment

class LaunchLibraryEnvironmentInterceptorTest {

    private var environment = LaunchLibraryEnvironment.PRODUCTION
    private var sentUrl: HttpUrl? = null

    // The second interceptor answers locally and records where the request would have gone.
    private val client = OkHttpClient.Builder()
        .addInterceptor(LaunchLibraryEnvironmentInterceptor { environment })
        .addInterceptor { chain ->
            sentUrl = chain.request().url
            Response.Builder()
                .request(chain.request())
                .protocol(Protocol.HTTP_1_1)
                .code(200)
                .message("OK")
                .body("{}".toResponseBody())
                .build()
        }
        .build()

    private fun get(url: String): String {
        client.newCall(Request.Builder().url(url).build()).execute().close()
        return sentUrl.toString()
    }

    @Test
    fun `production leaves production requests alone`() {
        val url = "https://ll.thespacedevs.com/2.3.0/launches/upcoming/?mode=list&limit=50"

        assertEquals(url, get(url))
    }

    @Test
    fun `development moves the request to the development host, keeping path and query`() {
        environment = LaunchLibraryEnvironment.DEVELOPMENT

        assertEquals(
            "https://lldev.thespacedevs.com/2.3.0/launches/upcoming/?mode=list&limit=50",
            get("https://ll.thespacedevs.com/2.3.0/launches/upcoming/?mode=list&limit=50")
        )
    }

    @Test
    fun `the server is read for every request, so a switch applies straight away`() {
        val url = "https://ll.thespacedevs.com/2.3.0/agencies/121/"
        get(url)
        environment = LaunchLibraryEnvironment.DEVELOPMENT

        assertEquals("https://lldev.thespacedevs.com/2.3.0/agencies/121/", get(url))
    }
}
