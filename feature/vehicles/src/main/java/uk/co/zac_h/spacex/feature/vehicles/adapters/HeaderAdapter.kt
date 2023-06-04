package uk.co.zac_h.spacex.feature.vehicles.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import uk.co.zac_h.spacex.feature.vehicles.databinding.ListItemVehicleHeaderBinding

class HeaderAdapter : ListAdapter<HeaderItem, HeaderAdapter.ViewHolder>(Comparator) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = ViewHolder(
        ListItemVehicleHeaderBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
    )

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val header = getItem(position)

        with(holder.binding) {
            listItemVehicleHeaderImage.isVisible = header.image != null

            Glide.with(root.context)
                .load(header.image)
                .into(listItemVehicleHeaderImage)

            listItemVehicleHeaderDescription.text = header.description
        }
    }

    class ViewHolder(val binding: ListItemVehicleHeaderBinding) :
        RecyclerView.ViewHolder(binding.root)

    object Comparator : DiffUtil.ItemCallback<HeaderItem>() {
        override fun areItemsTheSame(oldItem: HeaderItem, newItem: HeaderItem) =
            oldItem.description == newItem.description

        override fun areContentsTheSame(oldItem: HeaderItem, newItem: HeaderItem) =
            oldItem == newItem
    }
}

data class HeaderItem(val image: String?, val description: String?)