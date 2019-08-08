package uk.co.zac_h.spacex.dashboard.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import uk.co.zac_h.spacex.R
import uk.co.zac_h.spacex.utils.data.LaunchesModel
import java.text.SimpleDateFormat
import java.util.*

class DashboardLaunchesAdapter(private val context: Context?, private val launches: LinkedHashMap<String, LaunchesModel>): RecyclerView.Adapter<DashboardLaunchesAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.list_item_dashboard_launches, parent, false))
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val key = launches.keys.elementAt(position)
        val launch = launches[key]

        when(key) {
            "next" -> holder.heading.text = context?.getString(R.string.next_launch)
            "latest" -> holder.heading.text = context?.getString(R.string.latest_launch)
        }

        val dateF = launch?.launchDateUnix?.times(1000L)?.let { Date(it) }
        val dateFormat = SimpleDateFormat("dd MMM yy HH:mm", Locale.getDefault())
        dateFormat.timeZone = TimeZone.getDefault()

        val formattedDate = dateFormat.format(dateF)

        var blockText = ""

        holder.apply {
            flightNumber.text = context?.getString(R.string.flight_number, launch?.flightNumber)

            launch?.rocket?.firstStage?.cores?.forEach { i ->
                blockText += "${i.block} "
            }

            blockNumber.text = context?.getString(R.string.block_number, blockText)
            missionName.text = launch?.missionName
            date.text = formattedDate
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