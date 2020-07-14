package uk.co.zac_h.spacex.crew.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import uk.co.zac_h.spacex.R
import uk.co.zac_h.spacex.model.spacex.CrewLaunchesModel

class CrewLaunchAdapter(
    private val context: Context?,
    private val launches: List<CrewLaunchesModel>
) :
    RecyclerView.Adapter<CrewLaunchAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder =
        ViewHolder(
            LayoutInflater.from(parent.context).inflate(
                R.layout.list_item_crew_mission,
                parent,
                false
            )
        )

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val launch = launches[position]

        holder.apply {
            name.text = launch.name
            flight.text = context?.getString(R.string.flight_number, launch.flightNumber)
        }
    }

    override fun getItemCount(): Int = launches.size

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val name: TextView = itemView.findViewById(R.id.list_item_crew_mission_name_text)
        val flight: TextView = itemView.findViewById(R.id.list_item_crew_mission_flight_text)
    }
}