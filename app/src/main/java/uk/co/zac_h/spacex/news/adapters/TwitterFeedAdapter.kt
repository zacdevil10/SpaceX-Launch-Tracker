package uk.co.zac_h.spacex.news.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.constraintlayout.widget.ConstraintSet
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import uk.co.zac_h.mediarecyclerview.models.MediaModel
import uk.co.zac_h.mediarecyclerview.ui.MediaRecyclerView
import uk.co.zac_h.mediarecyclerview.utils.MediaType
import uk.co.zac_h.spacex.R
import uk.co.zac_h.spacex.dto.twitter.TimelineEntityModel.Companion.formatWithUrls
import uk.co.zac_h.spacex.dto.twitter.TimelineExtendedEntityModel
import uk.co.zac_h.spacex.dto.twitter.TimelineTweetModel
import uk.co.zac_h.spacex.utils.convertDate
import uk.co.zac_h.spacex.utils.dateStringToMillis
import uk.co.zac_h.spacex.utils.views.HtmlTextView

class TwitterFeedAdapter(
    private val context: Context,
    private val openLink: (String) -> Unit,
    diffCallback: DiffUtil.ItemCallback<TimelineTweetModel> = TwitterComparator
) : PagingDataAdapter<TimelineTweetModel, TwitterFeedAdapter.ViewHolder>(diffCallback) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder =
        ViewHolder(
            LayoutInflater.from(parent.context).inflate(
                R.layout.list_item_tweet,
                parent,
                false
            )
        )

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val tweet = getItem(position)

        holder.apply {
            tweet?.let {
                container.setOnClickListener {
                    openLink("https://twitter.com/SpaceX/status/${tweet.id}")
                }

                Glide.with(itemView).load(tweet.user.profileUrl)
                    .apply(RequestOptions.circleCropTransform())
                    .into(profileImage)
                date.text = tweet.created.dateStringToMillis()?.convertDate()
                name.text = tweet.user.name
                screenName.text =
                    context.getString(R.string.screen_name, tweet.user.screenName)

                desc.apply {
                    tweet.text?.formatWithUrls(
                        tweet.entities.urls,
                        tweet.entities.mentions,
                        tweet.entities.hashtags
                    )?.let {
                        setHtmlText(it)
                    }
                    movementMethod = HtmlTextView.LocalLinkMovementMethod
                }

                media.visibility = tweet.extendedEntities?.let { View.VISIBLE } ?: View.GONE
                mediaCard.visibility = tweet.extendedEntities?.let { View.VISIBLE } ?: View.GONE

                tweet.extendedEntities?.let {
                    setMediaRecycler(it, mediaConstraint, media)
                }

                if (position < itemCount - 1) {
                    indicatorBottom.visibility = tweet.replyStatusId?.let {
                        if (getItem(position + 1)?.id == it) View.VISIBLE else View.GONE
                    } ?: View.GONE
                }

                if (position != 0) {
                    indicatorTop.visibility = getItem(position - 1)?.replyStatusId?.let {
                        if (tweet.id == it) View.VISIBLE else View.GONE
                    } ?: View.GONE
                }

                quoteCardView.visibility = if (tweet.isQuote) View.VISIBLE else View.GONE

                tweet.quotedStatusLink?.let { quoted ->
                    quoteContainer.setOnClickListener {
                        quoted.expandedUrl?.let { url -> openLink(url) }
                    }
                }

                tweet.quotedStatus?.let { quoted ->
                    quoteDate.text = quoted.created?.dateStringToMillis()?.convertDate()
                    quoteName.text = quoted.user?.name
                    quoteScreenName.text =
                        context.getString(R.string.screen_name, quoted.user?.screenName)
                    quoteDesc.apply {
                        quoted.text?.formatWithUrls(null, null, null)?.let {
                            setHtmlText(it)
                        }
                    }

                    quoted.extendedEntities?.let { quotedMedia ->
                        setMediaRecycler(quotedMedia, quoteContainer, quoteMediaRecycler)
                    }
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

        when (it.media?.get(0)?.type) {
            "video", "animated_gif" -> {
                it.media?.get(0)?.info?.aspectRatio?.let { ratio ->
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
                    setDimensionRatio(mediaRecyclerView.id, "16:10")
                    applyTo(constraintLayout)
                }
            }
        }

        it.media?.forEach { tweetMedia ->
            var bitrate: Long = 0
            if (tweetMedia.type == "video") {
                tweetMedia.info?.variants?.forEachIndexed { index, mediaVariants ->
                    mediaVariants.bitrate?.let { bit ->
                        if (bit > bitrate) {
                            bitrate = bit
                            bitratePosition = index
                        }
                    }
                }
            }
        }

        it.media?.forEach { tweetMedia ->
            urls.add(
                when (tweetMedia.type) {
                    "photo" -> {
                        tweetMedia.url?.let { url ->
                            MediaModel(url = url, type = MediaType.IMAGE_URL)
                        } ?: return@forEach
                    }
                    "video", "animated_gif" -> {
                        tweetMedia.info?.variants?.get(bitratePosition)?.url?.let { static ->
                            tweetMedia.url?.let { url ->
                                MediaModel(
                                    url = url,
                                    static = static,
                                    type = MediaType.VIDEO
                                )
                            }
                        } ?: return@forEach
                    }
                    else -> return@forEach
                }
            )
        }

        mediaRecyclerView.apply {
            configure(context, urls)
        }
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val container: ConstraintLayout = itemView.findViewById(R.id.tweet_container)
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
        val quoteCardView: CardView = itemView.findViewById(R.id.tweet_quoted_layout)
        val quoteContainer: ConstraintLayout = itemView.findViewById(R.id.tweet_quoted_constraint)
        val quoteName: TextView = itemView.findViewById(R.id.tweet_quoted_name)
        val quoteScreenName: TextView = itemView.findViewById(R.id.tweet_quoted_screen_name)
        val quoteDate: TextView = itemView.findViewById(R.id.tweet_quoted_date)
        val quoteDesc: HtmlTextView = itemView.findViewById(R.id.tweet_quoted_full_text)
        val quoteMediaRecycler: MediaRecyclerView =
            itemView.findViewById(R.id.tweet_quoted_media_recycler)
    }

    object TwitterComparator: DiffUtil.ItemCallback<TimelineTweetModel>() {
        override fun areItemsTheSame(
            oldItem: TimelineTweetModel,
            newItem: TimelineTweetModel
        ): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(
            oldItem: TimelineTweetModel,
            newItem: TimelineTweetModel
        ): Boolean {
            return oldItem == newItem
        }
    }
}