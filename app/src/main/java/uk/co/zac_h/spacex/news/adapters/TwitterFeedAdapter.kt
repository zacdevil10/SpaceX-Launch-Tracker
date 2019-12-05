package uk.co.zac_h.spacex.news.adapters

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
import uk.co.zac_h.spacex.news.twitter.TwitterFeedView
import uk.co.zac_h.spacex.utils.CircleImageTransform
import uk.co.zac_h.spacex.utils.HtmlTextView
import uk.co.zac_h.spacex.utils.formatDateString
import java.util.regex.Pattern

class TwitterFeedAdapter(
    private val twitterFeed: ArrayList<TimelineTweetModel>,
    private val view: TwitterFeedView
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
            itemView.setOnClickListener {
                view.openWebLink("https://twitter.com/SpaceX/status/${tweet.id}")
            }

            Picasso.get().load(tweet.user.profileUrl).transform(CircleImageTransform())
                .into(profileImage)
            date.text = tweet.created.formatDateString()
            name.text = tweet.user.name
            screenName.text = "@${tweet.user.screenName}"

            var tweetMessage = tweet.text

            tweet.entities.urls.forEach {
                tweetMessage =
                    tweetMessage.replace(it.url, "<a href='${it.url}'>${it.displayUrl}</a>")
            }

            val pattern = Pattern.compile("((https://t.co/)\\w+)\$")
            val matcher = pattern.matcher(tweetMessage)

            tweetMessage = matcher.replaceAll("")

            tweet.entities.mentions.forEach {
                tweetMessage = tweetMessage.replace(
                    "@${it.screenName}",
                    "<a href='https://twitter.com/${it.screenName}'>@${it.screenName}</a>",
                    true
                )
            }

            desc.setHtmlText(tweetMessage)
            desc.movementMethod = HtmlTextView.LocalLinkMovementMethod

            media.visibility = tweet.extendedEntities?.let { View.VISIBLE } ?: View.GONE
            mediaCard.visibility = tweet.extendedEntities?.let { View.VISIBLE } ?: View.GONE

            tweet.extendedEntities?.let {
                val urls = ArrayList<MediaModel>()

                it.media.forEach { tweetMedia ->
                    urls.add(
                        when (tweetMedia.type) {
                            "photo" -> MediaModel(tweetMedia.url, MediaType.IMAGE)
                            "video" -> MediaModel(
                                tweetMedia.info.variants[0].url,
                                MediaType.VIDEO,
                                tweetMedia.url
                            )
                            else -> return@forEach
                        }
                    )
                }

                media.apply {
                    configure(context, urls)
                }
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
        val desc: HtmlTextView = itemView.findViewById(R.id.tweet_full_text)
    }
}