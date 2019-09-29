package uk.co.zac_h.spacex.launches.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import uk.co.zac_h.spacex.R
import uk.co.zac_h.spacex.model.LaunchesModel
import uk.co.zac_h.spacex.utils.formatDateMillisShort

class LaunchesWearAdapter(
    private val context: Context?,
    private val launches: List<LaunchesModel>
) : RecyclerView.Adapter<LaunchesWearAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder = ViewHolder(
        LayoutInflater.from(parent.context).inflate(
            R.layout.list_item_launches_wear, parent, false
        )
    )

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val launch = launches[position]

        holder.apply {
            missionName.text = launch.missionName
            flightNumber.text = context?.getString(R.string.flight_number, launch.flightNumber)
            date.text = launch.launchDateUnix.formatDateMillisShort(launch.tbd?.let { it } ?: false)
        }
    }

    override fun getItemCount(): Int = launches.size

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val missionName: TextView = itemView.findViewById(R.id.list_item_launches_wear_mission_name)
        val flightNumber: TextView = itemView.findViewById(R.id.list_item_launches_wear_flight_no)
        val date: TextView = itemView.findViewById(R.id.list_item_launches_wear_date)
    }

}