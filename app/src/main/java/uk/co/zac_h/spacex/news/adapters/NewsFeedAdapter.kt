package uk.co.zac_h.spacex.news.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import uk.co.zac_h.spacex.R
import uk.co.zac_h.spacex.model.news.ArticleModel
import uk.co.zac_h.spacex.news.news.NewsFeedView
import uk.co.zac_h.spacex.utils.clearHtmlTags
import uk.co.zac_h.spacex.utils.formatNewsFeedDate

class NewsFeedAdapter(private val articles: List<ArticleModel>, private val view: NewsFeedView) :
    RecyclerView.Adapter<NewsFeedAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder =
        ViewHolder(
            LayoutInflater.from(parent.context).inflate(
                R.layout.list_item_news,
                parent,
                false
            )
        )

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val article = articles[position]

        holder.apply {
            title.text = article.title
            desc.text = article.description?.clearHtmlTags()
            date.text = article.pubDate?.formatNewsFeedDate()
            source.text = article.source

            itemView.setOnClickListener {
                article.link?.let { it1 -> view.openWebLink(it1) }
            }
        }
    }

    override fun getItemCount(): Int = articles.size

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val title: TextView = itemView.findViewById(R.id.list_item_news_title)
        val desc: TextView = itemView.findViewById(R.id.list_item_news_desc)
        val date: TextView = itemView.findViewById(R.id.list_item_news_date)
        val source: TextView = itemView.findViewById(R.id.list_item_news_source)
    }
}