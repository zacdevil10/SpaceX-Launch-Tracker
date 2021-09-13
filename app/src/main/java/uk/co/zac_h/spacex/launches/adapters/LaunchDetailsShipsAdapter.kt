package uk.co.zac_h.spacex.launches.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.findNavController
import androidx.navigation.fragment.FragmentNavigatorExtras
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import uk.co.zac_h.spacex.R
import uk.co.zac_h.spacex.databinding.ListItemVehicleBinding
import uk.co.zac_h.spacex.dto.spacex.Ship
import uk.co.zac_h.spacex.launches.details.LaunchDetailsContainerFragmentDirections

class LaunchDetailsShipsAdapter : RecyclerView.Adapter<LaunchDetailsShipsAdapter.ViewHolder>() {

    private var ships: List<Ship> = emptyList()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder =
        ViewHolder(
            ListItemVehicleBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        )

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val ship = ships[position]

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

    override fun getItemCount(): Int = ships.size

    fun update(list: List<Ship>) {
        ships = list
        notifyDataSetChanged()
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

}