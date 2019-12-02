package uk.co.zac_h.spacex.news.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso
import uk.co.zac_h.mediarecyclerview.models.MediaModel
import uk.co.zac_h.mediarecyclerview.ui.MediaRecyclerView
import uk.co.zac_h.mediarecyclerview.utils.MediaType
import uk.co.zac_h.spacex.R
import uk.co.zac_h.spacex.model.twitter.TimelineTweetModel
import uk.co.zac_h.spacex.utils.CircleImageTransform
import uk.co.zac_h.spacex.utils.formatDateString

class TwitterFeedAdapter(
    private val context: Context?,
    private val twitterFeed: ArrayList<TimelineTweetModel>
) :
    RecyclerView.Adapter<TwitterFeedAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder =
        ViewHolder(
            LayoutInflater.from(parent.context).inflate(
                R.layout.list_item_tweet,
                parent,
                false
            )
        )

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val tweet = twitterFeed[position]

        holder.apply {
            Picasso.get().load(tweet.user.profileUrl).transform(CircleImageTransform())
                .into(profileImage)
            date.text = tweet.created.formatDateString()
            name.text = tweet.user.name
            screenName.text = "@${tweet.user.screenName}"
            desc.text = tweet.text

            media.visibility = tweet.entities?.let { View.VISIBLE } ?: View.GONE
            mediaCard.visibility = tweet.entities?.let { View.VISIBLE } ?: View.GONE

            tweet.entities?.let {
                val urls = ArrayList<MediaModel>()

                it.media.forEach { tweetMedia ->
                    urls.add(
                        MediaModel(
                            tweetMedia.url,
                            when (tweetMedia.type) {
                                "photo" -> MediaType.IMAGE
                                "video" -> MediaType.VIDEO
                                "animated_gif" -> MediaType.IMAGE
                                else -> throw IllegalArgumentException("Unknown media type.")
                            }
                        )
                    )
                }

                media.configure(context, urls)
            }
        }
    }

    override fun getItemCount(): Int = twitterFeed.size

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val profileImage: ImageView = itemView.findViewById(R.id.tweet_profile_image)
        val date: TextView = itemView.findViewById(R.id.tweet_date)
        val name: TextView = itemView.findViewById(R.id.tweet_name)
        val screenName: TextView = itemView.findViewById(R.id.tweet_screen_name)
        val mediaCard: CardView = itemView.findViewById(R.id.tweet_media_card)
        val media: MediaRecyclerView = itemView.findViewById(R.id.tweet_media_recycler)
        val desc: TextView = itemView.findViewById(R.id.tweet_full_text)
    }
}