package uk.co.zac_h.spacex.news.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.constraintlayout.widget.ConstraintSet
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
import uk.co.zac_h.spacex.utils.convertDate
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
            date.text = tweet.created.convertDate()
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
            mediaConstraint.visibility = tweet.extendedEntities?.let { View.VISIBLE } ?: View.GONE

            var bitratePosition = 0

            tweet.extendedEntities?.let {
                val urls = ArrayList<MediaModel>()

                when (it.media[0].type) {
                    "video", "animated_gif" -> {
                        it.media[0].info.aspectRatio.also { ratio ->
                            ConstraintSet().apply {
                                clone(mediaConstraint)
                                setDimensionRatio(media.id, "${ratio[0]}:${ratio[1]}")
                                applyTo(mediaConstraint)
                            }
                        }
                    }
                    else -> {
                        ConstraintSet().apply {
                            clone(mediaConstraint)
                            setDimensionRatio(media.id, "16:9")
                            applyTo(mediaConstraint)
                        }
                    }
                }

                it.media.forEach { tweetMedia ->
                    var bitrate: Long = 0
                    if (tweetMedia.type == "video") {
                        tweetMedia.info.variants.forEachIndexed { index, mediaVariants ->
                            mediaVariants.bitrate?.let { bit ->
                                if (bit > bitrate) {
                                    bitrate = bit
                                } else {
                                    bitratePosition = index
                                }
                            }
                        }
                    }
                }

                it.media.forEach { tweetMedia ->
                    urls.add(
                        when (tweetMedia.type) {
                            "photo" -> MediaModel(tweetMedia.url, MediaType.IMAGE)
                            "video", "animated_gif" -> MediaModel(
                                tweetMedia.info.variants[bitratePosition].url,
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

            if (position < twitterFeed.size - 1) {
                indicatorBottom.visibility = twitterFeed[position].replyStatusId?.let {
                    if (twitterFeed[position + 1].id == it) View.VISIBLE else View.GONE
                } ?: View.GONE
            }

            if (position != 0) {
                indicatorTop.visibility = twitterFeed[position - 1].replyStatusId?.let {
                    if (twitterFeed[position].id == it) View.VISIBLE else View.GONE
                } ?: View.GONE
            }

        }
    }

    override fun getItemCount(): Int = twitterFeed.size

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val profileImage: ImageView = itemView.findViewById(R.id.tweet_profile_image)
        val date: TextView = itemView.findViewById(R.id.tweet_date)
        val name: TextView = itemView.findViewById(R.id.tweet_name)
        val screenName: TextView = itemView.findViewById(R.id.tweet_screen_name)

        val mediaConstraint: ConstraintLayout = itemView.findViewById(R.id.tweet_media_constraint)
        val mediaCard: CardView = itemView.findViewById(R.id.tweet_media_card)
        val media: MediaRecyclerView = itemView.findViewById(R.id.tweet_media_recycler)

        val desc: HtmlTextView = itemView.findViewById(R.id.tweet_full_text)

        val indicatorTop: View = itemView.findViewById(R.id.tweet_reply_indicator_top)
        val indicatorBottom: View = itemView.findViewById(R.id.tweet_reply_indicator_bottom)
    }
}