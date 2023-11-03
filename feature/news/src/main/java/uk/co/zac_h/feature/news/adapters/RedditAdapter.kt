package uk.co.zac_h.feature.news.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.constraintlayout.widget.ConstraintSet
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import uk.co.zac_h.feature.news.R
import uk.co.zac_h.feature.news.databinding.ListItemRedditPostBinding
import uk.co.zac_h.spacex.core.common.utils.convertDate
import uk.co.zac_h.spacex.network.dto.reddit.RedditPost

class RedditAdapter(private val openLink: (String) -> Unit) :
    PagingDataAdapter<RedditPost, RedditAdapter.ViewHolder>(RedditComparator) {

    companion object {
        const val REDDIT = "https://www.reddit.com"
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder =
        ViewHolder(
            ListItemRedditPostBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val post = getItem(position)

        with(holder.binding) {
            post?.let {
                if (post.redditDomain || post.isSelf) {
                    listItemRedditThumbnailCard.visibility = View.GONE
                } else if (post.thumbnail.isNotEmpty()) {
                    listItemRedditThumbnailCard.visibility = View.VISIBLE
                    Glide.with(root).load(post.thumbnail)
                        .placeholder(R.drawable.ic_placeholder_reddit).into(listItemRedditThumbnail)
                    listItemRedditThumbnailLink.text = post.domain
                }

                if (post.redditDomain && post.preview != null && post.description.isNullOrEmpty()) post.preview?.let {
                    listItemRedditPreview.visibility = View.VISIBLE
                    val image = it.images[0].resolutions[it.images[0].resolutions.size - 1]
                    Glide.with(root).load(image.url).into(listItemRedditPreview)

                    ConstraintSet().apply {
                        clone(listItemRedditContent)
                        setDimensionRatio(
                            listItemRedditPreview.id,
                            "${image.width}:${image.height}"
                        )
                        applyTo(listItemRedditContent)
                    }

                    listItemRedditThumbnailCard.visibility = View.GONE
                } else {
                    listItemRedditPreview.visibility = View.GONE
                }

                listItemRedditTitle.text = post.title

                listItemRedditText.plainText = true
                post.description?.let { listItemRedditText.setHtmlText(it) }

                listItemRedditAuthor.text = post.author
                listItemRedditDate.text = post.created.toLong().convertDate()
                listItemRedditScore.text = post.score.toString()
                listItemRedditComments.text = post.commentsCount.toString()

                listItemRedditText.visibility =
                    if (post.description.isNullOrEmpty()) View.GONE else View.VISIBLE

                listItemRedditPinned.visibility = if (post.stickied) View.VISIBLE else View.GONE

                listItemRedditCard.setOnClickListener {
                    openLink("${REDDIT}${post.permalink}")
                }
            }
        }
    }

    class ViewHolder(val binding: ListItemRedditPostBinding) : RecyclerView.ViewHolder(binding.root)

    object RedditComparator : DiffUtil.ItemCallback<RedditPost>() {
        override fun areItemsTheSame(oldItem: RedditPost, newItem: RedditPost): Boolean {
            return oldItem.name == newItem.name
        }

        override fun areContentsTheSame(oldItem: RedditPost, newItem: RedditPost): Boolean {
            return oldItem == newItem
        }
    }
}
