package uk.co.zac_h.spacex.feature.launch.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import uk.co.zac_h.spacex.feature.launch.R
import uk.co.zac_h.spacex.feature.launch.VideoItem
import uk.co.zac_h.spacex.feature.launch.databinding.ListItemVideoBinding

class VideoAdapter(val onClick: (String) -> Unit) :
    ListAdapter<VideoItem, VideoAdapter.ViewHolder>(Comparator) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder = ViewHolder(
        ListItemVideoBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
    )

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val video = getItem(position)

        with(holder.binding) {
            Glide.with(root).load(video.imageUrl).into(image)

            title.apply {
                isSelected = true
                text = video.title
            }

            watchButton.apply {
                video.source?.let { text = resources.getString(R.string.watch_on_domain_label, it) }
                setOnClickListener {
                    onClick(video.url)
                }
            }
        }
    }

    class ViewHolder(val binding: ListItemVideoBinding) : RecyclerView.ViewHolder(binding.root)

    object Comparator : DiffUtil.ItemCallback<VideoItem>() {

        override fun areItemsTheSame(
            oldItem: VideoItem,
            newItem: VideoItem
        ) = oldItem == newItem

        override fun areContentsTheSame(
            oldItem: VideoItem,
            newItem: VideoItem
        ): Boolean = oldItem.url == newItem.url
    }
}