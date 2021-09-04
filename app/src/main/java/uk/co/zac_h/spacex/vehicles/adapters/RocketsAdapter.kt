package uk.co.zac_h.spacex.vehicles.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.navigation.findNavController
import androidx.navigation.fragment.FragmentNavigatorExtras
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import uk.co.zac_h.spacex.R
import uk.co.zac_h.spacex.databinding.ListItemVehicleBinding
import uk.co.zac_h.spacex.dto.spacex.Rocket
import uk.co.zac_h.spacex.vehicles.VehiclesFragmentDirections

class RocketsAdapter(val setSelected: (String) -> Unit) :
    ListAdapter<Rocket, RocketsAdapter.ViewHolder>(RocketComparator) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder =
        ViewHolder(
            ListItemVehicleBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        )

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val rocket = getItem(position)

        holder.binding.apply {
            vehicleCard.transitionName = rocket.id

            Glide.with(root)
                .load(rocket.flickr?.random())
                .error(R.drawable.ic_baseline_error_outline_24)
                .into(vehicleImage)

            vehicleName.text = rocket.name
            vehicleDetails.text = rocket.description

            vehicleCard.setOnClickListener {
                setSelected(rocket.id)
                holder.bind(rocket)
            }
            vehicleSpecs.setOnClickListener {
                setSelected(rocket.id)
                holder.bind(rocket)
            }
        }
    }

    class ViewHolder(val binding: ListItemVehicleBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(rocket: Rocket) {
            binding.root.findNavController().navigate(
                VehiclesFragmentDirections.actionVehiclesPageFragmentToRocketDetailsFragment(),
                FragmentNavigatorExtras(binding.vehicleCard to rocket.id)
            )
        }
    }

    object RocketComparator : DiffUtil.ItemCallback<Rocket>() {

        override fun areItemsTheSame(oldItem: Rocket, newItem: Rocket) = oldItem == newItem

        override fun areContentsTheSame(oldItem: Rocket, newItem: Rocket) = oldItem.id == newItem.id

    }
}