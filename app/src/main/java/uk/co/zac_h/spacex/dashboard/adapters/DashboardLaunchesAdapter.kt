package uk.co.zac_h.spacex.dashboard.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.core.os.bundleOf
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso
import uk.co.zac_h.spacex.R
import uk.co.zac_h.spacex.model.spacex.LaunchesModel
import uk.co.zac_h.spacex.utils.formatBlockNumber
import uk.co.zac_h.spacex.utils.formatDateMillisLong
import java.util.*

class DashboardLaunchesAdapter(
    private val context: Context?,
    private val launches: LinkedHashMap<String, LaunchesModel>
) : RecyclerView.Adapter<DashboardLaunchesAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder =
        ViewHolder(
            LayoutInflater.from(parent.context).inflate(
                R.layout.list_item_dashboard_launches,
                parent,
                false
            )
        )

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val key = when (position) {
            0 -> "next"
            1 -> "latest"
            else -> ""
        }
        val launch = launches[key]

        holder.apply {
            when (position) {
                0 -> heading.text = context?.getString(R.string.next_launch)
                1 -> heading.text = context?.getString(R.string.latest_launch)
            }

            launch?.let {
                missionPatch.visibility =
                    launch.links.missionPatchSmall?.let { View.VISIBLE } ?: View.GONE

                Picasso.get().load(launch.links.missionPatchSmall)
                    .into(missionPatch)

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

                launchesCard.setOnClickListener { _ ->
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
        val launchesCard: CardView = itemView.findViewById(R.id.launches_card_view)
        val missionPatch: ImageView = itemView.findViewById(R.id.launches_mission_patch_image)
        val heading: TextView = itemView.findViewById(R.id.dashboard_heading_text)
        val flightNumber: TextView = itemView.findViewById(R.id.launches_flight_no_text)
        val blockNumber: TextView = itemView.findViewById(R.id.launches_block_text)
        val missionName: TextView = itemView.findViewById(R.id.launches_mission_name_text)
        val date: TextView = itemView.findViewById(R.id.launches_date_text)
    }
}