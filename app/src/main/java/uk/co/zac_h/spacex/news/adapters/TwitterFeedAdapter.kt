package uk.co.zac_h.spacex.news.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
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
import uk.co.zac_h.spacex.core.common.utils.convertDate
import uk.co.zac_h.spacex.core.common.utils.dateStringToMillis
import uk.co.zac_h.spacex.databinding.ListItemTweetBinding
import uk.co.zac_h.spacex.network.dto.twitter.TimelineEntityModel.Companion.formatWithUrls
import uk.co.zac_h.spacex.network.dto.twitter.TimelineExtendedEntityModel
import uk.co.zac_h.spacex.network.dto.twitter.TimelineTweetModel
import uk.co.zac_h.spacex.utils.views.HtmlTextView

class TwitterFeedAdapter(
    private val context: Context,
    private val openLink: (String) -> Unit
) : PagingDataAdapter<TimelineTweetModel, TwitterFeedAdapter.ViewHolder>(TwitterComparator) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder =
        ViewHolder(
            ListItemTweetBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val tweet = getItem(position)

        with(holder.binding) {
            tweet?.let {
                tweetContainer.setOnClickListener {
                    openLink("https://twitter.com/SpaceX/status/${tweet.id}")
                }

                Glide.with(root).load(tweet.user.profileUrl)
                    .apply(RequestOptions.circleCropTransform())
                    .into(tweetProfileImage)
                tweetDate.text = tweet.created.dateStringToMillis()?.convertDate()
                tweetName.text = tweet.user.name
                tweetScreenName.text =
                    context.getString(R.string.screen_name, tweet.user.screenName)

                tweetFullText.apply {
                    tweet.text?.formatWithUrls(
                        tweet.entities.urls,
                        tweet.entities.mentions,
                        tweet.entities.hashtags
                    )?.let {
                        setHtmlText(it)
                    }
                    movementMethod = HtmlTextView.LocalLinkMovementMethod
                }

                tweetMediaRecycler.visibility =
                    tweet.extendedEntities?.let { View.VISIBLE } ?: View.GONE
                tweetMediaCard.visibility =
                    tweet.extendedEntities?.let { View.VISIBLE } ?: View.GONE

                tweet.extendedEntities?.let {
                    setMediaRecycler(it, tweetMediaConstraint, tweetMediaRecycler)
                }

                if (position < itemCount - 1) {
                    tweetReplyIndicatorBottom.visibility = tweet.replyStatusId?.let {
                        if (getItem(position + 1)?.id == it) View.VISIBLE else View.GONE
                    } ?: View.GONE
                }

                if (position != 0) {
                    tweetReplyIndicatorTop.visibility = getItem(position - 1)?.replyStatusId?.let {
                        if (tweet.id == it) View.VISIBLE else View.GONE
                    } ?: View.GONE
                }

                tweetQuotedLayout.visibility = if (tweet.isQuote) View.VISIBLE else View.GONE

                tweet.quotedStatusLink?.let { quoted ->
                    tweetQuotedConstraint.setOnClickListener {
                        quoted.expandedUrl?.let { url -> openLink(url) }
                    }
                }

                tweet.quotedStatus?.let { quoted ->
                    tweetQuotedDate.text = quoted.created?.dateStringToMillis()?.convertDate()
                    tweetQuotedName.text = quoted.user?.name
                    tweetQuotedScreenName.text =
                        context.getString(R.string.screen_name, quoted.user?.screenName)
                    tweetQuotedFullText.apply {
                        quoted.text?.formatWithUrls(null, null, null)?.let {
                            setHtmlText(it)
                        }
                    }

                    quoted.extendedEntities?.let { quotedMedia ->
                        setMediaRecycler(
                            quotedMedia,
                            tweetQuotedConstraint,
                            tweetQuotedMediaRecycler
                        )
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

    class ViewHolder(val binding: ListItemTweetBinding) : RecyclerView.ViewHolder(binding.root)

    object TwitterComparator : DiffUtil.ItemCallback<TimelineTweetModel>() {
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
