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
import uk.co.zac_h.spacex.model.LaunchesModel
import uk.co.zac_h.spacex.utils.formatBlockNumber
import uk.co.zac_h.spacex.utils.formatDateMillisLong

class DashboardPinnedAdapter(
    private val context: Context?,
    private val launches: ArrayList<LaunchesModel>
) : RecyclerView.Adapter<DashboardPinnedAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder =
        ViewHolder(
            LayoutInflater.from(parent.context).inflate(
                R.layout.list_item_dashboard_launches,
                parent,
                false
            )
        )

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val launch = launches[position]

        holder.apply {

            heading.visibility = View.GONE

            launch.let {
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
    }

    override fun getItemCount(): Int = launches.size

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val heading: TextView = itemView.findViewById(R.id.dashboard_heading_text)
        val flightNumber: TextView = itemView.findViewById(R.id.dashboard_flight_no_text)
        val blockNumber: TextView = itemView.findViewById(R.id.dashboard_block_text)
        val missionName: TextView = itemView.findViewById(R.id.dashboard_mission_name_text)
        val date: TextView = itemView.findViewById(R.id.dashboard_date_text)
    }
}