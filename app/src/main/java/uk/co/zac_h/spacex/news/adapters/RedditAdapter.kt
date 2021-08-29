package uk.co.zac_h.spacex.news.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.constraintlayout.widget.ConstraintSet
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.google.android.material.card.MaterialCardView
import uk.co.zac_h.spacex.R
import uk.co.zac_h.spacex.dto.reddit.RedditPost
import uk.co.zac_h.spacex.dto.reddit.SubredditPostModel
import uk.co.zac_h.spacex.utils.convertDate
import uk.co.zac_h.spacex.utils.views.HtmlTextView

class RedditAdapter(
    diffCallback: DiffUtil.ItemCallback<SubredditPostModel> = RedditComparator,
    private val openLink: (String) -> Unit
) : PagingDataAdapter<SubredditPostModel, RedditAdapter.ViewHolder>(diffCallback) {

    companion object {
        const val REDDIT = "https://www.reddit.com"
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder =
        ViewHolder(
            LayoutInflater.from(parent.context).inflate(
                R.layout.list_item_reddit_post,
                parent,
                false
            )
        )

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val postModel = getItem(position)

        holder.apply {
            postModel?.data?.let {
                val post = RedditPost(it)
                if (post.redditDomain || post.isSelf) {
                    thumbCard.visibility = View.GONE
                } else if (post.thumbnail.isNotEmpty()) {
                    thumbCard.visibility = View.VISIBLE
                    Glide.with(itemView).load(post.thumbnail)
                        .placeholder(R.drawable.ic_placeholder_reddit).into(thumbnail)
                    thumbLink.text = post.domain
                }

                if (post.redditDomain && post.preview != null && post.description.isNullOrEmpty()) post.preview?.let {
                    preview.visibility = View.VISIBLE
                    val image = it.images[0].resolutions[it.images[0].resolutions.size - 1]
                    Glide.with(itemView).load(image.url).into(preview)

                    ConstraintSet().apply {
                        clone(content)
                        setDimensionRatio(
                            preview.id,
                            "${image.width}:${image.height}"
                        )
                        applyTo(content)
                    }

                    thumbCard.visibility = View.GONE
                } else {
                    preview.visibility = View.GONE
                }

                title.text = post.title

                text.plainText = true
                post.description?.let { text.setHtmlText(it) }

                author.text = post.author
                date.text = post.created.toLong().convertDate()
                score.text = post.score.toString()
                comments.text = post.commentsCount.toString()

                text.visibility = if (post.description.isNullOrEmpty()) View.GONE else View.VISIBLE

                pin.visibility = if (post.stickied) View.VISIBLE else View.GONE

                card.setOnClickListener {
                    openLink("$REDDIT${post.permalink}")
                }
            }
        }
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val card: MaterialCardView = itemView.findViewById(R.id.list_item_reddit_card)
        val content: ConstraintLayout = itemView.findViewById(R.id.list_item_reddit_content)
        val thumbCard: MaterialCardView =
            itemView.findViewById(R.id.list_item_reddit_thumbnail_card)
        val thumbnail: ImageView = itemView.findViewById(R.id.list_item_reddit_thumbnail)
        val thumbLink: TextView = itemView.findViewById(R.id.list_item_reddit_thumbnail_link)
        val title: TextView = itemView.findViewById(R.id.list_item_reddit_title)
        val text: HtmlTextView = itemView.findViewById(R.id.list_item_reddit_text)
        val preview: ImageView = itemView.findViewById(R.id.list_item_reddit_preview)
        val author: TextView = itemView.findViewById(R.id.list_item_reddit_author)
        val date: TextView = itemView.findViewById(R.id.list_item_reddit_date)
        val score: TextView = itemView.findViewById(R.id.list_item_reddit_score)
        val comments: TextView = itemView.findViewById(R.id.list_item_reddit_comments)
        val pin: ImageView = itemView.findViewById(R.id.list_item_reddit_pinned)
    }

    object RedditComparator: DiffUtil.ItemCallback<SubredditPostModel>() {
        override fun areItemsTheSame(oldItem: SubredditPostModel, newItem: SubredditPostModel): Boolean {
            return oldItem.data.name == newItem.data.name
        }

        override fun areContentsTheSame(oldItem: SubredditPostModel, newItem: SubredditPostModel): Boolean {
            return oldItem == newItem
        }
    }
}