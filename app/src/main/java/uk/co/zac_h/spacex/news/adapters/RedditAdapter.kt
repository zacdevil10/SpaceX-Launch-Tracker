package uk.co.zac_h.spacex.news.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.card.MaterialCardView
import com.squareup.picasso.Picasso
import uk.co.zac_h.spacex.R
import uk.co.zac_h.spacex.model.reddit.SubredditPostModel
import uk.co.zac_h.spacex.news.reddit.RedditFeedView
import uk.co.zac_h.spacex.utils.convertDate

class RedditAdapter(private val view: RedditFeedView, private val posts: List<SubredditPostModel>) :
    RecyclerView.Adapter<RedditAdapter.ViewHolder>() {

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
            Picasso.get().load(post.data.thumbnail).into(thumbnail)

            thumbnail.visibility = when (post.data.thumbnail) {
                "", "self", "default" -> View.GONE
                else -> View.VISIBLE
            }

            title.text = post.data.title
            text.text = post.data.text
            author.text = post.data.author
            date.text = post.data.created.toLong().convertDate()
            score.text = post.data.score.toString()
            comments.text = post.data.commentsCount.toString()

            text.visibility = if (post.data.text.isEmpty()) View.GONE else View.VISIBLE

            card.setOnClickListener {
                view.openWebLink(post.data.url)
            }
        }
    }

    override fun getItemCount(): Int = posts.size

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val card: MaterialCardView = itemView.findViewById(R.id.list_item_reddit_card)
        val thumbnail: ImageView = itemView.findViewById(R.id.list_item_reddit_thumbnail)
        val title: TextView = itemView.findViewById(R.id.list_item_reddit_title)
        val text: TextView = itemView.findViewById(R.id.list_item_reddit_text)
        val author: TextView = itemView.findViewById(R.id.list_item_reddit_author)
        val date: TextView = itemView.findViewById(R.id.list_item_reddit_date)
        val score: TextView = itemView.findViewById(R.id.list_item_reddit_score)
        val comments: TextView = itemView.findViewById(R.id.list_item_reddit_comments)
    }
}