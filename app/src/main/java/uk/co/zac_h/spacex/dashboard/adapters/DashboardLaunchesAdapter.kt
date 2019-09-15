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
import uk.co.zac_h.spacex.utils.DashboardListModel
import uk.co.zac_h.spacex.utils.formatBlockNumber
import uk.co.zac_h.spacex.utils.formatDateMillisLong
import java.util.*

class DashboardLaunchesAdapter(
    private val context: Context?,
    private val launches: ArrayList<DashboardListModel>
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder =
        when (viewType) {
            0 -> {
                HeaderViewHolder(
                    LayoutInflater.from(parent.context).inflate(
                        R.layout.list_item_heading,
                        parent,
                        false
                    )
                )
            }
            else -> {
                ViewHolder(
                    LayoutInflater.from(parent.context).inflate(
                        R.layout.list_item_dashboard_launches,
                        parent,
                        false
                    )
                )
            }
        }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val launch = launches[position]

        when (holder) {
            is ViewHolder -> holder.apply {
                launch.launchModel?.let {
                    flightNumber.text = context?.getString(R.string.flight_number, it.flightNumber)

                    blockNumber.text = context?.getString(
                        R.string.vehicle_block_type,
                        it.rocket.name,
                        it.rocket.firstStage?.cores?.formatBlockNumber()
                    )
                    missionName.text = it.missionName
                    date.text = it.tbd?.let { tbd ->
                        it.launchDateUnix.formatDateMillisLong(tbd)
                    } ?: it.launchDateUnix.formatDateMillisLong()

                    itemView.setOnClickListener { _ ->
                        itemView.findNavController().navigate(
                            R.id.action_dashboard_page_fragment_to_launch_details_fragment,
                            bundleOf("launch" to it, "title" to it.missionName)
                        )
                    }
                }
            }
            is HeaderViewHolder -> holder.heading.text = launch.headingName
        }
    }

    fun isHeader(): (itemPosition: Int) -> Boolean {
        return {
            launches[it].isHeader
        }
    }

    override fun getItemCount(): Int = launches.size

    override fun getItemViewType(position: Int): Int = if (launches[position].isHeader) 0 else 1

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val flightNumber: TextView = itemView.findViewById(R.id.dashboard_flight_no_text)
        val blockNumber: TextView = itemView.findViewById(R.id.dashboard_block_text)
        val missionName: TextView = itemView.findViewById(R.id.dashboard_mission_name_text)
        val date: TextView = itemView.findViewById(R.id.dashboard_date_text)
    }

    class HeaderViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var heading: TextView = itemView.findViewById(R.id.list_item_heading)
    }
}