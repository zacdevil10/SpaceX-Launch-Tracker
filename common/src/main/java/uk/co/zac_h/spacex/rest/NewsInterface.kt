package uk.co.zac_h.spacex.rest

import org.simpleframework.xml.convert.AnnotationStrategy
import org.simpleframework.xml.core.Persister
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.simplexml.SimpleXmlConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query
import uk.co.zac_h.spacex.model.news.RSSNewsFeed

interface NewsInterface {

    @GET("news")
    suspend fun getNewsFeed(@Query("q") query: String, @Query("output") output: String = "rss"): Response<RSSNewsFeed>

    companion object RetrofitSetup {
        fun create(): NewsInterface {
            val retrofit = Retrofit.Builder().apply {
                baseUrl("https://news.google.com/")
                addConverterFactory(
                    SimpleXmlConverterFactory.createNonStrict(
                        Persister(
                            AnnotationStrategy()
                        )
                    )
                )
            }.build()

            return retrofit.create(NewsInterface::class.java)
        }
    }

}