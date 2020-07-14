package uk.co.zac_h.spacex.launches.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.core.os.bundleOf
import androidx.navigation.findNavController
import androidx.navigation.fragment.FragmentNavigatorExtras
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import uk.co.zac_h.spacex.R
import uk.co.zac_h.spacex.model.spacex.ShipExtendedModel

class LaunchDetailsShipsAdapter(private val ships: List<ShipExtendedModel>) :
    RecyclerView.Adapter<LaunchDetailsShipsAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder =
        ViewHolder(
            LayoutInflater.from(parent.context).inflate(
                R.layout.list_item_vehicle,
                parent, false
            )
        )

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val ship = ships[position]

        holder.apply {
            itemView.transitionName = ship.id

            Glide.with(itemView)
                .load(ship.image)
                .error(R.drawable.ic_baseline_error_outline_24)
                .into(image)

            title.text = ship.name
            details.visibility = View.GONE

            card.setOnClickListener { bind(ship) }
            specs.setOnClickListener { bind(ship) }
        }
    }

    override fun getItemCount(): Int = ships.size

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val card: CardView = itemView.findViewById(R.id.list_item_vehicle_card)
        val image: ImageView = itemView.findViewById(R.id.list_item_vehicle_image)
        val title: TextView = itemView.findViewById(R.id.list_item_vehicle_title)
        val details: TextView = itemView.findViewById(R.id.list_item_vehicle_details)
        val specs: Button = itemView.findViewById(R.id.list_item_vehicle_specs_button)

        fun bind(ship: ShipExtendedModel) {
            itemView.findNavController().navigate(
                R.id.action_launch_details_container_fragment_to_ship_details_fragment,
                bundleOf("ship" to ship),
                null,
                FragmentNavigatorExtras(itemView to ship.id)
            )
        }
    }

}