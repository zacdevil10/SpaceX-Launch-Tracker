package uk.co.zac_h.spacex.launches.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.navigation.findNavController
import androidx.navigation.fragment.FragmentNavigatorExtras
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import uk.co.zac_h.spacex.R
import uk.co.zac_h.spacex.databinding.ListItemVehicleBinding
import uk.co.zac_h.spacex.model.spacex.Ship

class LaunchDetailsShipsAdapter(private val ships: List<Ship>) :
    RecyclerView.Adapter<LaunchDetailsShipsAdapter.ViewHolder>() {

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

    class ViewHolder(val binding: ListItemVehicleBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(ship: Ship) {
            binding.root.findNavController().navigate(
                R.id.action_launch_details_container_fragment_to_ship_details_fragment,
                bundleOf("ship" to ship),
                null,
                FragmentNavigatorExtras(binding.vehicleCard to ship.id)
            )
        }
    }

}