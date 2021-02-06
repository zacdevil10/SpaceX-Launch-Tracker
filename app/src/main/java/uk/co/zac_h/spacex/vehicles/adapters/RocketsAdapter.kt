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
import uk.co.zac_h.spacex.model.spacex.Rocket

class RocketsAdapter(private val rockets: List<Rocket>) :
    RecyclerView.Adapter<RocketsAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder =
        ViewHolder(
            ListItemVehicleBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        )

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val rocket = rockets[position]

        holder.binding.apply {
            vehicleCard.transitionName = rocket.id

            Glide.with(root)
                .load(rocket.flickr?.random())
                .error(R.drawable.ic_baseline_error_outline_24)
                .into(vehicleImage)

            vehicleName.text = rocket.name
            vehicleDetails.text = rocket.description

            vehicleCard.setOnClickListener { holder.bind(rocket) }
            vehicleSpecs.setOnClickListener { holder.bind(rocket) }
        }
    }

    override fun getItemCount(): Int = rockets.size

    class ViewHolder(val binding: ListItemVehicleBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(rocket: Rocket) {
            binding.root.findNavController().navigate(
                R.id.action_vehicles_page_fragment_to_rocket_details_fragment,
                bundleOf("rocket" to rocket),
                null,
                FragmentNavigatorExtras(binding.vehicleCard to rocket.id)
            )
        }
    }
}