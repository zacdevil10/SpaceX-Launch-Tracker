package uk.co.zac_h.spacex.feature.vehicles.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.navigation.findNavController
import androidx.navigation.fragment.FragmentNavigatorExtras
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import uk.co.zac_h.spacex.feature.vehicles.VehiclesFragmentDirections
import uk.co.zac_h.spacex.feature.vehicles.databinding.ListItemVehicleBinding
import uk.co.zac_h.spacex.feature.vehicles.rockets.Rocket

class RocketsAdapter(val setSelected: (String) -> Unit) :
    ListAdapter<Rocket, RocketsAdapter.ViewHolder>(RocketComparator) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder = ViewHolder(
        ListItemVehicleBinding.inflate(LayoutInflater.from(parent.context), parent, false)
    )

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val rocket = getItem(position)

        with(holder.binding) {
            vehicleView.apply {
                transitionName = rocket.id
                image = rocket.flickr?.random()
                title = rocket.name
                vehicleSpecs.setOnClickListener {
                    setSelected(rocket.id)
                    holder.bind(rocket)
                }
                setOnClickListener {
                    setSelected(rocket.id)
                    holder.bind(rocket)
                }
            }

            vehicleDetails.text = rocket.description
        }
    }

    class ViewHolder(val binding: ListItemVehicleBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(rocket: Rocket) {
            binding.root.findNavController().navigate(
                VehiclesFragmentDirections.actionVehiclesPageFragmentToRocketDetailsFragment(rocket.name),
                FragmentNavigatorExtras(binding.vehicleView to rocket.id)
            )
        }
    }

    object RocketComparator : DiffUtil.ItemCallback<Rocket>() {

        override fun areItemsTheSame(oldItem: Rocket, newItem: Rocket) = oldItem == newItem

        override fun areContentsTheSame(oldItem: Rocket, newItem: Rocket) = oldItem.id == newItem.id
    }
}
