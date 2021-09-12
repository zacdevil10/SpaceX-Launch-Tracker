package uk.co.zac_h.spacex.vehicles.adapters

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
import uk.co.zac_h.spacex.dto.spacex.Ship
import uk.co.zac_h.spacex.vehicles.VehiclesFragmentDirections

class ShipsAdapter(val setSelected: (String) -> Unit) :
    ListAdapter<Ship, ShipsAdapter.ViewHolder>(ShipComparator) {

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

            vehicleCard.setOnClickListener {
                setSelected(ship.id)
                holder.bind(ship)
            }
            vehicleSpecs.setOnClickListener {
                setSelected(ship.id)
                holder.bind(ship)
            }
        }
    }

    class ViewHolder(val binding: ListItemVehicleBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(ship: Ship) {
            binding.root.findNavController().navigate(
                VehiclesFragmentDirections.actionVehiclesPageToShipDetails(ship.name, ship.id),
                FragmentNavigatorExtras(binding.vehicleCard to ship.id)
            )
        }
    }

    object ShipComparator : DiffUtil.ItemCallback<Ship>() {

        override fun areItemsTheSame(oldItem: Ship, newItem: Ship) = oldItem == newItem

        override fun areContentsTheSame(oldItem: Ship, newItem: Ship) = oldItem.id == newItem.id

    }

}