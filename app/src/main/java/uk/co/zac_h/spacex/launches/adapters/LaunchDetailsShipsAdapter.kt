package uk.co.zac_h.spacex.launches.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.findNavController
import androidx.navigation.fragment.FragmentNavigatorExtras
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import uk.co.zac_h.spacex.R
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

        holder.binding.apply {
            vehicleCard.transitionName = ship.id

            Glide.with(root)
                .load(ship.image)
                .error(R.drawable.ic_baseline_directions_boat_24)
                .into(vehicleImage)

            vehicleName.text = ship.name
            vehicleDetails.visibility = View.GONE

            vehicleCard.setOnClickListener { holder.bind(ship) }
            vehicleSpecs.setOnClickListener { holder.bind(ship) }
        }
    }

    class ViewHolder(val binding: ListItemVehicleBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(ship: Ship) {
            binding.root.findNavController().navigate(
                LaunchDetailsContainerFragmentDirections.actionLaunchDetailsContainerFragmentToShipDetailsFragment(
                    ship.name,
                    ship.id
                ),
                FragmentNavigatorExtras(binding.vehicleCard to ship.id)
            )
        }
    }

    object Comparator : DiffUtil.ItemCallback<Ship>() {

        override fun areItemsTheSame(oldItem: Ship, newItem: Ship) = oldItem == newItem

        override fun areContentsTheSame(oldItem: Ship, newItem: Ship) = oldItem.id == newItem.id
    }

}