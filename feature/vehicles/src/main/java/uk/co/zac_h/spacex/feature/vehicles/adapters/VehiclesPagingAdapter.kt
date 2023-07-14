package uk.co.zac_h.spacex.feature.vehicles.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.navigation.findNavController
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import uk.co.zac_h.spacex.feature.vehicles.VehicleItem
import uk.co.zac_h.spacex.feature.vehicles.VehiclesFragmentDirections
import uk.co.zac_h.spacex.feature.vehicles.databinding.ListItemVehicleBinding

class VehiclesPagingAdapter(private val setSelected: (VehicleItem) -> Unit) :
    PagingDataAdapter<VehicleItem, VehiclesPagingAdapter.ViewHolder>(Comparator) {

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
                image = vehicle?.imageUrl
                title = vehicle?.title
                vehicleSpecs.setOnClickListener {
                    vehicle?.let { setSelected(it) }
                    holder.bind(vehicle?.title.orEmpty())
                }
                setOnClickListener {
                    vehicle?.let { setSelected(it) }
                    holder.bind(vehicle?.title.orEmpty())
                }
            }

            vehicleDetails.text = vehicle?.description
        }
    }

    class ViewHolder(val binding: ListItemVehicleBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(title: String) {
            binding.root.findNavController().navigate(
                VehiclesFragmentDirections.actionVehiclesPageToVehicleDetails(title = title)
            )
        }
    }

    object Comparator : DiffUtil.ItemCallback<VehicleItem>() {

        override fun areItemsTheSame(oldItem: VehicleItem, newItem: VehicleItem) =
            oldItem.id == newItem.id

        override fun areContentsTheSame(oldItem: VehicleItem, newItem: VehicleItem) =
            oldItem.title == newItem.title
                    && oldItem.imageUrl == newItem.imageUrl
                    && oldItem.description == newItem.description
                    && oldItem.longDescription == newItem.longDescription
    }
}