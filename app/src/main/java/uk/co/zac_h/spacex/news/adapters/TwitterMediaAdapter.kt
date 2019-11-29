package uk.co.zac_h.spacex.news.adapters

import android.content.Context
import android.content.Intent
import android.util.DisplayMetrics
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.VideoView
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.squareup.picasso.Picasso
import uk.co.zac_h.spacex.R
import uk.co.zac_h.spacex.model.twitter.TweetMediaModel
import uk.co.zac_h.spacex.utils.ui.PhotoView
import kotlin.math.min
import kotlin.math.roundToInt


class TwitterMediaAdapter(
    private val context: Context?,
    private val media: List<TweetMediaModel>
) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder =
        when (viewType) {
            0 -> {
                ImageViewHolder(
                    LayoutInflater.from(parent.context).inflate(
                        R.layout.list_item_tweet_image,
                        parent,
                        false
                    )
                )
            }
            1 -> {
                VideoViewHolder(
                    LayoutInflater.from(parent.context).inflate(
                        R.layout.list_item_tweet_video,
                        parent,
                        false
                    )
                )
            }
            else -> {
                ImageViewHolder(
                    LayoutInflater.from(parent.context).inflate(
                        R.layout.list_item_tweet_image,
                        parent,
                        false
                    )
                )
            }
        }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val item = media[position]

        when (holder) {
            is ImageViewHolder -> holder.apply {
                image.setOnClickListener {
                    context?.startActivity(Intent(context, PhotoView::class.java).apply {
                        putExtra("position", position)
                        putParcelableArrayListExtra(
                            "media",
                            ArrayList<TweetMediaModel>().apply { addAll(media) })
                    })
                }

                Picasso.get().load(item.url).into(image)

                val displayMetrics: DisplayMetrics? = context?.resources?.displayMetrics
                displayMetrics?.let {
                    val defaultHeight =
                        (196 * (displayMetrics.xdpi / DisplayMetrics.DENSITY_DEFAULT)).roundToInt()

                    when (position) {
                        0 -> {
                            if (media.size == 4) {
                                image.layoutParams.height = defaultHeight / 2
                                (image.layoutParams as StaggeredGridLayoutManager.LayoutParams).bottomMargin =
                                    8
                            }
                            if (media.size > 1) {
                                (image.layoutParams as StaggeredGridLayoutManager.LayoutParams).marginEnd =
                                    8
                            }
                        }
                        1 -> {
                            if (media.size > 2) {
                                image.layoutParams.height = defaultHeight / 2
                                (image.layoutParams as StaggeredGridLayoutManager.LayoutParams).bottomMargin =
                                    8

                            }
                            if (media.size > 1) {
                                (image.layoutParams as StaggeredGridLayoutManager.LayoutParams).marginStart =
                                    8
                            }
                        }
                        2 -> {
                            if (media.size > 2) {
                                image.layoutParams.height = defaultHeight / 2
                            }
                            if (media.size == 3) {
                                (image.layoutParams as StaggeredGridLayoutManager.LayoutParams).marginStart =
                                    8
                                (image.layoutParams as StaggeredGridLayoutManager.LayoutParams).topMargin =
                                    8
                            } else if (media.size == 4) {
                                (image.layoutParams as StaggeredGridLayoutManager.LayoutParams).marginEnd =
                                    8
                                (image.layoutParams as StaggeredGridLayoutManager.LayoutParams).topMargin =
                                    8
                            }
                        }
                        3 -> {
                            if (media.size > 3) {
                                image.layoutParams.height = defaultHeight / 2
                                (image.layoutParams as StaggeredGridLayoutManager.LayoutParams).marginStart =
                                    8
                                (image.layoutParams as StaggeredGridLayoutManager.LayoutParams).topMargin =
                                    8
                            }
                        }
                    }

                }
            }
        }
    }

    override fun getItemCount(): Int = min(media.size, 4)

    override fun getItemViewType(position: Int): Int = when (media[position].type) {
        "photo" -> 0
        "video" -> 1
        else -> 0
    }

    class ImageViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val image: ImageView = itemView.findViewById(R.id.media_image)
    }

    class VideoViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val video: VideoView = itemView.findViewById(R.id.media_video)
    }
}