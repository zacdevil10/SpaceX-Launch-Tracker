package uk.co.zac_h.spacex.vehicles.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.navigation.findNavController
import androidx.navigation.fragment.FragmentNavigatorExtras
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import uk.co.zac_h.spacex.R
import uk.co.zac_h.spacex.databinding.ListItemVehicleBinding
import uk.co.zac_h.spacex.model.spacex.Dragon

class DragonAdapter(private val dragonList: List<Dragon>) :
    RecyclerView.Adapter<DragonAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder =
        ViewHolder(
            ListItemVehicleBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        )

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val dragon = dragonList[position]

        holder.binding.apply {
            listItemVehicleCard.transitionName = dragon.id

            Glide.with(root)
                .load(dragon.flickr?.random())
                .error(R.drawable.ic_baseline_error_outline_24)
                .into(listItemVehicleImage)

            listItemVehicleTitle.text = dragon.name
            listItemVehicleDetails.text = dragon.description

            listItemVehicleCard.setOnClickListener { holder.bind(dragon) }
            listItemVehicleSpecsButton.setOnClickListener { holder.bind(dragon) }
        }
    }

    override fun getItemCount(): Int = dragonList.size

    class ViewHolder(val binding: ListItemVehicleBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(dragon: Dragon) {
            binding.root.findNavController().navigate(
                R.id.action_vehicles_page_fragment_to_dragon_details_fragment,
                bundleOf("dragon" to dragon, "title" to dragon.name),
                null,
                FragmentNavigatorExtras(binding.listItemVehicleCard to dragon.id)
            )
        }
    }
}