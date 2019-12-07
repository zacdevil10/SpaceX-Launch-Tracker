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
import uk.co.zac_h.spacex.model.twitter.TimelineExtendedEntityModel
import uk.co.zac_h.spacex.model.twitter.TimelineTweetModel
import uk.co.zac_h.spacex.news.twitter.TwitterFeedView
import uk.co.zac_h.spacex.utils.CircleImageTransform
import uk.co.zac_h.spacex.utils.HtmlTextView
import uk.co.zac_h.spacex.utils.convertDate
import uk.co.zac_h.spacex.utils.formatWithUrls

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

            desc.apply {
                setHtmlText(
                    tweet.text.formatWithUrls(
                        tweet.entities.urls,
                        tweet.entities.mentions,
                        tweet.entities.hashtags
                    )
                )
                movementMethod = HtmlTextView.LocalLinkMovementMethod
            }

            media.visibility = tweet.extendedEntities?.let { View.VISIBLE } ?: View.GONE
            mediaCard.visibility = tweet.extendedEntities?.let { View.VISIBLE } ?: View.GONE

            tweet.extendedEntities?.let {
                setMediaRecycler(it, mediaConstraint, media)
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

            quoteContainer.visibility = if (tweet.isQuote) View.VISIBLE else View.GONE

            tweet.quotedStatusLink?.let { quoted ->
                quoteContainer.setOnClickListener {
                    view.openWebLink(quoted.expandedUrl)
                }
            }

            tweet.quotedStatus?.let { quoted ->
                quoteDate.text = quoted.created.convertDate()
                quoteName.text = quoted.user.name
                quoteScreenName.text = "@${quoted.user.screenName}"
                quoteDesc.text = quoted.text

                quoted.extendedEntities?.let { quotedMedia ->
                    setMediaRecycler(quotedMedia, quoteContainer, quoteMediaRecycler)
                }
            }
        }
    }

    private fun setMediaRecycler(
        it: TimelineExtendedEntityModel,
        constraintLayout: ConstraintLayout,
        mediaRecyclerView: MediaRecyclerView
    ) {
        var bitratePosition = 0
        val urls = ArrayList<MediaModel>()

        when (it.media[0].type) {
            "video", "animated_gif" -> {
                it.media[0].info.aspectRatio.also { ratio ->
                    ConstraintSet().apply {
                        clone(constraintLayout)
                        setDimensionRatio(mediaRecyclerView.id, "${ratio[0]}:${ratio[1]}")
                        applyTo(constraintLayout)
                    }
                }
            }
            else -> {
                ConstraintSet().apply {
                    clone(constraintLayout)
                    setDimensionRatio(mediaRecyclerView.id, "16:9")
                    applyTo(constraintLayout)
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

        mediaRecyclerView.apply {
            configure(context, urls)
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

        //Quoted layout
        val quoteContainer: ConstraintLayout = itemView.findViewById(R.id.tweet_quoted_layout)
        val quoteName: TextView = itemView.findViewById(R.id.tweet_quoted_name)
        val quoteScreenName: TextView = itemView.findViewById(R.id.tweet_quoted_screen_name)
        val quoteDate: TextView = itemView.findViewById(R.id.tweet_quoted_date)
        val quoteDesc: TextView = itemView.findViewById(R.id.tweet_quoted_full_text)
        val quoteMediaRecycler: MediaRecyclerView =
            itemView.findViewById(R.id.tweet_quoted_media_recycler)
    }
}