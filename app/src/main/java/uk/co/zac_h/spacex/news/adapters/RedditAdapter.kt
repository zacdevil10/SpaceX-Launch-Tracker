package uk.co.zac_h.spacex.news.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.constraintlayout.widget.ConstraintSet
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.google.android.material.card.MaterialCardView
import uk.co.zac_h.spacex.R
import uk.co.zac_h.spacex.model.reddit.SubredditPostModel
import uk.co.zac_h.spacex.news.reddit.RedditFeedContract
import uk.co.zac_h.spacex.utils.convertDate
import uk.co.zac_h.spacex.utils.views.HtmlTextView

class RedditAdapter(
    private val view: RedditFeedContract.RedditFeedView,
    private val posts: List<SubredditPostModel>
) :
    RecyclerView.Adapter<RedditAdapter.ViewHolder>() {

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
        val post = posts[position]

        holder.apply {
            if (post.data.redditDomain || post.data.isSelf) {
                thumbCard.visibility = View.GONE
            } else if (post.data.thumbnail.isNotEmpty()) {
                thumbCard.visibility = View.VISIBLE
                Glide.with(itemView).load(post.data.thumbnail)
                    .placeholder(R.drawable.ic_placeholder_reddit).into(thumbnail)
                thumbLink.text = post.data.domain
            }

            if (post.data.redditDomain && post.data.preview != null && post.data.textHtml.isNullOrEmpty()) post.data.preview?.let {
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

            title.text = post.data.title

            text.plainText = true
            post.data.textHtml?.let { text.setHtmlText(it) }

            author.text = post.data.author
            date.text = post.data.created.toLong().convertDate()
            score.text = post.data.score.toString()
            comments.text = post.data.commentsCount.toString()

            text.visibility = if (post.data.textHtml.isNullOrEmpty()) View.GONE else View.VISIBLE

            pin.visibility = if (post.data.stickied) View.VISIBLE else View.GONE

            card.setOnClickListener {
                view.openWebLink("$REDDIT${post.data.permalink}")
            }
        }
    }

    override fun getItemCount(): Int = posts.size

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
}