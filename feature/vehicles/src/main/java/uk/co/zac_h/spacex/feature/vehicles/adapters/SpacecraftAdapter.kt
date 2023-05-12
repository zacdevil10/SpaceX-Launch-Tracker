package uk.co.zac_h.spacex.feature.vehicles.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import uk.co.zac_h.spacex.feature.vehicles.databinding.ListItemVehicleBinding
import uk.co.zac_h.spacex.feature.vehicles.spacecraft.SpacecraftItem

class SpacecraftAdapter :
    PagingDataAdapter<SpacecraftItem, SpacecraftAdapter.ViewHolder>(Comparator) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder = ViewHolder(
        ListItemVehicleBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
    )

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val spacecraft = getItem(position)

        with(holder.binding) {
            vehicleView.apply {
                spacecraft?.let { image = it.imageUrl }
                title = spacecraft?.serialNumber
            }
            vehicleDetails.text = spacecraft?.description
        }
    }

    class ViewHolder(val binding: ListItemVehicleBinding) : RecyclerView.ViewHolder(binding.root)

    object Comparator : DiffUtil.ItemCallback<SpacecraftItem>() {
        override fun areItemsTheSame(
            oldItem: SpacecraftItem,
            newItem: SpacecraftItem
        ): Boolean = oldItem.id == newItem.id

        override fun areContentsTheSame(
            oldItem: SpacecraftItem,
            newItem: SpacecraftItem
        ): Boolean = oldItem == newItem
    }
}