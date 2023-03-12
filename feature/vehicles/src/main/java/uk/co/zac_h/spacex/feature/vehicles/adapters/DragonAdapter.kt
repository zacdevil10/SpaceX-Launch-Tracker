package uk.co.zac_h.spacex.feature.vehicles.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.navigation.findNavController
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import uk.co.zac_h.spacex.feature.vehicles.VehiclesFragmentDirections
import uk.co.zac_h.spacex.feature.vehicles.databinding.ListItemVehicleBinding
import uk.co.zac_h.spacex.feature.vehicles.dragon.SpacecraftItem

class DragonAdapter(val setSelected: (String) -> Unit) :
    ListAdapter<SpacecraftItem, DragonAdapter.ViewHolder>(DragonComparator) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder = ViewHolder(
        ListItemVehicleBinding.inflate(LayoutInflater.from(parent.context), parent, false)
    )

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val dragon = getItem(position)

        with(holder.binding) {
            vehicleView.apply {
                transitionName = dragon.id
                image = dragon.imageUrl
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

            vehicleDetails.text = dragon.history
        }
    }

    class ViewHolder(val binding: ListItemVehicleBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(dragon: SpacecraftItem) {
            binding.root.findNavController().navigate(
                VehiclesFragmentDirections.actionVehiclesPageFragmentToDragonDetailsFragment(dragon.name)
            )
        }
    }

    object DragonComparator : DiffUtil.ItemCallback<SpacecraftItem>() {

        override fun areItemsTheSame(oldItem: SpacecraftItem, newItem: SpacecraftItem) =
            oldItem == newItem

        override fun areContentsTheSame(oldItem: SpacecraftItem, newItem: SpacecraftItem) =
            oldItem.id == newItem.id
    }
}
