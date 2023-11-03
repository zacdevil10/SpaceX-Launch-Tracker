package uk.co.zac_h.feature.news.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import uk.co.zac_h.feature.news.articles.ArticleItem
import uk.co.zac_h.feature.news.databinding.ListItemArticleBinding
import uk.co.zac_h.spacex.core.common.utils.convertDate
import uk.co.zac_h.spacex.core.common.utils.toMillis

class ArticlesAdapter(private val openLink: (String) -> Unit) :
    PagingDataAdapter<ArticleItem, ArticlesAdapter.ViewHolder>(Comparator) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder = ViewHolder(
        ListItemArticleBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
    )

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val article = getItem(position)

        with(holder.binding) {
            article?.let { article ->
                Glide.with(root).load(article.image).into(image)
                newsSite.text = article.site
                title.text = article.title
                date.text = article.published.toMillis()?.convertDate()

                root.setOnClickListener {
                    openLink(article.url)
                }
            }
        }
    }

    class ViewHolder(val binding: ListItemArticleBinding) : RecyclerView.ViewHolder(binding.root)

    object Comparator : DiffUtil.ItemCallback<ArticleItem>() {

        override fun areItemsTheSame(oldItem: ArticleItem, newItem: ArticleItem) =
            oldItem.id == newItem.id

        override fun areContentsTheSame(oldItem: ArticleItem, newItem: ArticleItem) =
            oldItem == newItem
    }
}