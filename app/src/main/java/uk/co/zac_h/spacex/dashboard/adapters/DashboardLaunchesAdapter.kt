package uk.co.zac_h.spacex.dashboard.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import uk.co.zac_h.spacex.R
import uk.co.zac_h.spacex.utils.data.LaunchesModel

class DashboardLaunchesAdapter(private val launches: ArrayList<LaunchesModel>): RecyclerView.Adapter<DashboardLaunchesAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.list_item_dashboard_launches, parent, false))
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val launch = launches[position]

        when(position) {
            0 -> holder.heading.text = "Next Launch"
            1 -> holder.heading.text = "Latest Launch"
        }

        holder.apply {
            flightNumber.text = "Flight ${launch.flightNumber}"
            blockNumber.text = "NYI"
            missionName.text = launch.missionName
        }
    }

    override fun getItemCount(): Int = launches.size

    class ViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
        val heading: TextView = itemView.findViewById(R.id.dashboard_heading_text)
        val flightNumber: TextView = itemView.findViewById(R.id.dashboard_flight_no_text)
        val blockNumber: TextView = itemView.findViewById(R.id.dashboard_block_text)
        val missionName: TextView = itemView.findViewById(R.id.dashboard_mission_name_text)
    }
}