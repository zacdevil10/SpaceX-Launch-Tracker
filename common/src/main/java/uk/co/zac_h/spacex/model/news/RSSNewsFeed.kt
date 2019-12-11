package uk.co.zac_h.spacex.model.news

import org.simpleframework.xml.Element
import org.simpleframework.xml.ElementList
import org.simpleframework.xml.Root

@Root(name = "rss", strict = false)
data class RSSNewsFeed(
    @field:Element(name = "channel") var channel: ChannelModel? = null
)

@Root(name = "channel", strict = true)
data class ChannelModel(
    @field:ElementList(
        name = "item",
        inline = true,
        required = false
    ) var articleList: List<ArticleModel>? = null
)

@Root(name = "item", strict = true)
data class ArticleModel(
    @field:Element(name = "title") var title: String? = null,
    @field:Element(name = "link") var link: String? = null,
    @field:Element(name = "pubDate") var pubDate: String? = null,
    @field:Element(name = "description") var description: String? = null,
    @field:Element(name = "source") var source: String? = null
)