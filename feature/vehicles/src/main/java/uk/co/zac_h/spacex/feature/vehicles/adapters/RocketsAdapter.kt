package uk.co.zac_h.spacex.feature.vehicles.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.navigation.findNavController
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import uk.co.zac_h.spacex.feature.vehicles.VehiclesFragmentDirections
import uk.co.zac_h.spacex.feature.vehicles.databinding.ListItemVehicleBinding
import uk.co.zac_h.spacex.feature.vehicles.rockets.LauncherItem

class RocketsAdapter(val setSelected: (LauncherItem) -> Unit) :
    ListAdapter<LauncherItem, RocketsAdapter.ViewHolder>(RocketComparator) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder = ViewHolder(
        ListItemVehicleBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
    )

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val rocket = getItem(position)

        with(holder.binding) {
            vehicleView.apply {
                image = rocket.imageUrl
                title = rocket.fullName
                vehicleSpecs.setOnClickListener {
                    setSelected(rocket)
                    holder.bind()
                }
                setOnClickListener {
                    setSelected(rocket)
                    holder.bind()
                }
            }

            vehicleDetails.text = rocket.description
        }
    }

    class ViewHolder(val binding: ListItemVehicleBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind() {
            binding.root.findNavController().navigate(
                VehiclesFragmentDirections.actionVehiclesPageFragmentToRocketDetailsFragment()
            )
        }
    }

    object RocketComparator : DiffUtil.ItemCallback<LauncherItem>() {

        override fun areItemsTheSame(
            oldItem: LauncherItem,
            newItem: LauncherItem
        ) = oldItem == newItem

        override fun areContentsTheSame(
            oldItem: LauncherItem,
            newItem: LauncherItem
        ) = oldItem.id == newItem.id
    }
}
