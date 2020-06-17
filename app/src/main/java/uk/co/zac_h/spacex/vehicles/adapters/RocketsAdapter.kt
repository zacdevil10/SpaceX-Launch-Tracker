package uk.co.zac_h.spacex.vehicles.adapters

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
import uk.co.zac_h.spacex.R
import uk.co.zac_h.spacex.model.spacex.RocketsModel

class RocketsAdapter(private val rockets: List<RocketsModel>) :
    RecyclerView.Adapter<RocketsAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder =
        ViewHolder(
            LayoutInflater.from(parent.context).inflate(
                R.layout.list_item_vehicle,
                parent,
                false
            )
        )

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val rocket = rockets[position]

        holder.apply {
            itemView.transitionName = rocket.id

            when (rocket.id) {
                "5e9d0d95eda69955f709d1eb" -> image.setImageResource(R.drawable.falcon1)
                "5e9d0d95eda69973a809d1ec" -> image.setImageResource(R.drawable.falcon9)
                "5e9d0d95eda69974db09d1ed" -> image.setImageResource(R.drawable.falconheavy)
                "5e9d0d96eda699382d09d1ee" -> image.setImageResource(R.drawable.starship)
            }

            title.text = rocket.name
            details.text = rocket.description

            card.setOnClickListener { bind(rocket) }
            specs.setOnClickListener { bind(rocket) }
        }
    }

    override fun getItemCount(): Int = rockets.size

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val card: CardView = itemView.findViewById(R.id.list_item_vehicle_card)
        val image: ImageView = itemView.findViewById(R.id.list_item_vehicle_image)
        val title: TextView = itemView.findViewById(R.id.list_item_vehicle_title)
        val details: TextView = itemView.findViewById(R.id.list_item_vehicle_details)
        val specs: Button = itemView.findViewById(R.id.list_item_vehicle_specs_button)

        fun bind(rocket: RocketsModel) {
            itemView.findNavController().navigate(
                R.id.action_vehicles_page_fragment_to_rocket_details_fragment,
                bundleOf("rocket" to rocket),
                null,
                FragmentNavigatorExtras(itemView to rocket.id)
            )
        }
    }
}