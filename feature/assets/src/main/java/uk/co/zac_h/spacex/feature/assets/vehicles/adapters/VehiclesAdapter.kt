package uk.co.zac_h.spacex.feature.assets.vehicles.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.navigation.findNavController
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import uk.co.zac_h.spacex.feature.assets.databinding.ListItemVehicleBinding
import uk.co.zac_h.spacex.feature.assets.vehicles.VehicleItem
import uk.co.zac_h.spacex.feature.assets.vehicles.VehiclesFragmentDirections

class VehiclesAdapter(val setSelected: (VehicleItem) -> Unit) :
    ListAdapter<VehicleItem, VehiclesAdapter.ViewHolder>(Comparator) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder = ViewHolder(
        ListItemVehicleBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
    )

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val vehicle = getItem(position)

        with(holder.binding) {
            vehicleView.apply {
                image = vehicle.imageUrl
                title = vehicle.title
                vehicleSpecs.setOnClickListener {
                    setSelected(vehicle)
                    holder.bind(vehicle.title.orEmpty())
                }
                setOnClickListener {
                    setSelected(vehicle)
                    holder.bind(vehicle.title.orEmpty())
                }
            }

            vehicleDetails.text = vehicle.description
        }
    }

    class ViewHolder(val binding: ListItemVehicleBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(title: String) {
            binding.root.findNavController().navigate(
                VehiclesFragmentDirections.actionVehiclesPageToVehicleDetails(title = title)
            )
        }
    }

    companion object Comparator : DiffUtil.ItemCallback<VehicleItem>() {

        override fun areItemsTheSame(oldItem: VehicleItem, newItem: VehicleItem) =
            oldItem.id == newItem.id

        override fun areContentsTheSame(oldItem: VehicleItem, newItem: VehicleItem) =
            oldItem.title == newItem.title
                    && oldItem.imageUrl == newItem.imageUrl
                    && oldItem.description == newItem.description
                    && oldItem.longDescription == newItem.longDescription
    }
}