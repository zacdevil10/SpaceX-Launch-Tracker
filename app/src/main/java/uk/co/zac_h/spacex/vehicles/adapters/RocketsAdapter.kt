package uk.co.zac_h.spacex.vehicles.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import uk.co.zac_h.spacex.R
import uk.co.zac_h.spacex.model.RocketsModel

class RocketsAdapter(private val rockets: List<RocketsModel>) :
    RecyclerView.Adapter<RocketsAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder =
        ViewHolder(
            LayoutInflater.from(parent.context).inflate(
                R.layout.list_item_rocket,
                parent,
                false
            )
        )

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val rocket = rockets[position]

        holder.apply {
            when (rocket.rocketId) {
                "falcon1" -> image.setImageResource(R.drawable.falcon1)
                "falcon9" -> image.setImageResource(R.drawable.falcon9)
                "falconheavy" -> image.setImageResource(R.drawable.falconheavy)
                "starship" -> image.setImageResource(R.drawable.starship)
            }

            title.text = rocket.rocketName
            details.text = rocket.description
        }
    }

    override fun getItemCount(): Int = rockets.size

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val image: ImageView = itemView.findViewById(R.id.list_item_rocket_image)
        val title: TextView = itemView.findViewById(R.id.list_item_rocket_title)
        val details: TextView = itemView.findViewById(R.id.list_item_rocket_details)
    }
}