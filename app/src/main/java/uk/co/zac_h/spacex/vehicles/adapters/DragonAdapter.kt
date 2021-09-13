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
import uk.co.zac_h.spacex.dto.spacex.Dragon
import uk.co.zac_h.spacex.vehicles.VehiclesFragmentDirections

class DragonAdapter(val setSelected: (String) -> Unit) :
    ListAdapter<Dragon, DragonAdapter.ViewHolder>(DragonComparator) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder = ViewHolder(
        ListItemVehicleBinding.inflate(LayoutInflater.from(parent.context), parent, false)
    )

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val dragon = getItem(position)

        holder.binding.apply {
            vehicleCard.transitionName = dragon.id

            Glide.with(root)
                .load(dragon.flickr?.random())
                .error(R.drawable.ic_baseline_error_outline_24)
                .into(vehicleImage)

            vehicleName.text = dragon.name
            vehicleDetails.text = dragon.description

            vehicleCard.setOnClickListener {
                setSelected(dragon.id)
                holder.bind(dragon)
            }
            vehicleSpecs.setOnClickListener {
                setSelected(dragon.id)
                holder.bind(dragon)
            }
        }
    }

    class ViewHolder(val binding: ListItemVehicleBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(dragon: Dragon) {
            binding.root.findNavController().navigate(
                VehiclesFragmentDirections.actionVehiclesPageFragmentToDragonDetailsFragment(dragon.name),
                FragmentNavigatorExtras(binding.vehicleCard to dragon.id)
            )
        }
    }

    object DragonComparator : DiffUtil.ItemCallback<Dragon>() {

        override fun areItemsTheSame(oldItem: Dragon, newItem: Dragon) = oldItem == newItem

        override fun areContentsTheSame(oldItem: Dragon, newItem: Dragon) = oldItem.id == newItem.id

    }
}