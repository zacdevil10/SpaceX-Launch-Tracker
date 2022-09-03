package uk.co.zac_h.spacex.launches.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.findNavController
import androidx.navigation.fragment.FragmentNavigatorExtras
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import uk.co.zac_h.spacex.databinding.ListItemVehicleBinding
import uk.co.zac_h.spacex.launches.details.LaunchDetailsContainerFragmentDirections
import uk.co.zac_h.spacex.vehicles.ships.Ship

class LaunchDetailsShipsAdapter :
    ListAdapter<Ship, LaunchDetailsShipsAdapter.ViewHolder>(Comparator) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder = ViewHolder(
        ListItemVehicleBinding.inflate(LayoutInflater.from(parent.context), parent, false)
    )

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val ship = getItem(position)

        with(holder.binding) {
            vehicleView.apply {
                transitionName = ship.id
                image = ship.image
                title = ship.name
                vehicleSpecs.setOnClickListener { holder.bind(ship) }
                setOnClickListener { holder.bind(ship) }
            }

            vehicleDetails.visibility = View.GONE
        }
    }

    class ViewHolder(val binding: ListItemVehicleBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(ship: Ship) {
            binding.root.findNavController().navigate(
                LaunchDetailsContainerFragmentDirections.actionLaunchDetailsContainerFragmentToShipDetailsFragment(
                    ship.name,
                    ship.id
                ),
                FragmentNavigatorExtras(binding.vehicleView to ship.id)
            )
        }
    }

    object Comparator : DiffUtil.ItemCallback<Ship>() {

        override fun areItemsTheSame(oldItem: Ship, newItem: Ship) = oldItem == newItem

        override fun areContentsTheSame(oldItem: Ship, newItem: Ship) = oldItem.id == newItem.id
    }

}