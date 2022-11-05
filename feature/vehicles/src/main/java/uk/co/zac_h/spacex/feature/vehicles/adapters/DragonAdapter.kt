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
import uk.co.zac_h.spacex.network.dto.spacex.Dragon

class DragonAdapter(val setSelected: (String) -> Unit) :
    ListAdapter<Dragon, DragonAdapter.ViewHolder>(DragonComparator) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder = ViewHolder(
        ListItemVehicleBinding.inflate(LayoutInflater.from(parent.context), parent, false)
    )

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val dragon = getItem(position)

        with(holder.binding) {
            vehicleView.apply {
                transitionName = dragon.id
                image = dragon.flickr?.random()
                title = dragon.name
                vehicleSpecs.setOnClickListener {
                    setSelected(dragon.id)
                    holder.bind(dragon)
                }
                setOnClickListener {
                    setSelected(dragon.id)
                    holder.bind(dragon)
                }
            }

            vehicleDetails.text = dragon.description
        }
    }

    class ViewHolder(val binding: ListItemVehicleBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(dragon: Dragon) {
            binding.root.findNavController().navigate(
                VehiclesFragmentDirections.actionVehiclesPageFragmentToDragonDetailsFragment(dragon.name),
                FragmentNavigatorExtras(binding.vehicleView to dragon.id)
            )
        }
    }

    object DragonComparator : DiffUtil.ItemCallback<Dragon>() {

        override fun areItemsTheSame(oldItem: Dragon, newItem: Dragon) = oldItem == newItem

        override fun areContentsTheSame(oldItem: Dragon, newItem: Dragon) = oldItem.id == newItem.id
    }
}
