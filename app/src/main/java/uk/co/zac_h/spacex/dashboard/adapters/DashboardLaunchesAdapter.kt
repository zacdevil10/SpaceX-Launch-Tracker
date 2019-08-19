package uk.co.zac_h.spacex.dashboard.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.os.bundleOf
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import uk.co.zac_h.spacex.R
import uk.co.zac_h.spacex.utils.data.LaunchesModel
import uk.co.zac_h.spacex.utils.format
import java.util.*

class DashboardLaunchesAdapter(private val context: Context?, private val launches: LinkedHashMap<String, LaunchesModel>): RecyclerView.Adapter<DashboardLaunchesAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.list_item_dashboard_launches, parent, false))
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val key = when(position) {
            0 -> "next"
            1 -> "latest"
            else -> ""
        }
        val launch = launches[key]

        when(key) {
            "next" -> holder.heading.text = context?.getString(R.string.next_launch)
            "latest" -> holder.heading.text = context?.getString(R.string.latest_launch)
        }

        var blockText = ""

        holder.apply {
            flightNumber.text = context?.getString(R.string.flight_number, launch?.flightNumber)

            launch?.rocket?.firstStage?.cores?.forEach { i ->
                blockText += "${i.block} "
            }

            blockNumber.text = context?.getString(R.string.vehicle_block_type, launch?.rocket?.name, blockText)
            missionName.text = launch?.missionName
            date.text = launch?.launchDateUnix?.format()

            itemView.setOnClickListener {
                itemView.findNavController().navigate(
                    R.id.launch_details_fragment,
                    bundleOf("launch" to launch, "title" to launch?.missionName)
                )
            }
        }
    }

    override fun getItemCount(): Int = launches.size

    class ViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
        val heading: TextView = itemView.findViewById(R.id.dashboard_heading_text)
        val flightNumber: TextView = itemView.findViewById(R.id.dashboard_flight_no_text)
        val blockNumber: TextView = itemView.findViewById(R.id.dashboard_block_text)
        val missionName: TextView = itemView.findViewById(R.id.dashboard_mission_name_text)
        val date: TextView = itemView.findViewById(R.id.dashboard_date_text)
    }
}