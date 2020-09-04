package uk.co.zac_h.spacex.dashboard.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.core.os.bundleOf
import androidx.navigation.findNavController
import androidx.navigation.fragment.FragmentNavigatorExtras
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import uk.co.zac_h.spacex.R
import uk.co.zac_h.spacex.model.spacex.LaunchesExtendedModel
import uk.co.zac_h.spacex.utils.formatDateMillisLong

class DashboardPinnedAdapter(
    private val context: Context?,
    private val launches: ArrayList<LaunchesExtendedModel>
) : RecyclerView.Adapter<DashboardPinnedAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder =
        ViewHolder(
            LayoutInflater.from(parent.context).inflate(
                R.layout.list_item_launches,
                parent,
                false
            )
        )

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val launch = launches[position]

        holder.apply {
            itemView.transitionName = launch.id

            launch.let {
                Glide.with(itemView)
                    .load(launch.links?.missionPatch?.patchSmall)
                    .error(context?.let { context ->
                        ContextCompat.getDrawable(context, R.drawable.ic_mission_patch)
                    })
                    .fallback(context?.let { context ->
                        ContextCompat.getDrawable(context, R.drawable.ic_mission_patch)
                    })
                    .placeholder(context?.let { context ->
                        ContextCompat.getDrawable(context, R.drawable.ic_mission_patch)
                    })
                    .into(missionPatch)

                if (launch.rocket?.name == "Falcon 9") {
                    reusedTag.visibility = launch.cores?.get(0)?.reused?.let { reused ->
                        if (reused) View.VISIBLE else View.GONE
                    } ?: View.GONE

                    landingVehicleTag.visibility =
                        launch.cores?.get(0)?.landingSuccess?.let { landingSuccess ->
                            if (landingSuccess) View.VISIBLE else View.GONE
                        } ?: View.GONE

                    landingVehicleTag.text = launch.cores?.get(0)?.landingPad?.name
                } else {
                    reusedTag.visibility = View.GONE
                    landingVehicleTag.visibility = View.GONE
                }

                flightNumber.text = context?.getString(R.string.flight_number, it.flightNumber)
                vehicle.text = it.rocket?.name
                missionName.text = it.missionName
                date.text = it.launchDateUnix?.formatDateMillisLong(it.datePrecision)

                itemView.setOnClickListener { _ ->
                    itemView.findNavController().navigate(
                        R.id.action_dashboard_page_fragment_to_launch_details_container_fragment,
                        bundleOf(
                            "launch_short" to it
                        ),
                        null,
                        FragmentNavigatorExtras(itemView to launch.id)
                    )
                }
            }
        }
    }

    override fun getItemCount(): Int = launches.size

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val missionPatch: ImageView = itemView.findViewById(R.id.launches_mission_patch_image)
        val flightNumber: TextView = itemView.findViewById(R.id.launches_flight_no_text)
        val vehicle: TextView = itemView.findViewById(R.id.launches_vehicle_text)
        val missionName: TextView = itemView.findViewById(R.id.launches_mission_name_text)
        val date: TextView = itemView.findViewById(R.id.launches_date_text)
        val reusedTag: TextView = itemView.findViewById(R.id.launches_reused_text)
        val landingVehicleTag: TextView = itemView.findViewById(R.id.launches_landing_vehicle_text)
    }
}