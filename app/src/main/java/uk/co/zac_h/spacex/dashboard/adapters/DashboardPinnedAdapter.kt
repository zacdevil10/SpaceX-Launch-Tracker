package uk.co.zac_h.spacex.dashboard.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import androidx.core.os.bundleOf
import androidx.navigation.findNavController
import androidx.navigation.fragment.FragmentNavigatorExtras
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import uk.co.zac_h.spacex.R
import uk.co.zac_h.spacex.model.spacex.LaunchesModel
import uk.co.zac_h.spacex.utils.formatDateMillisLong

class DashboardPinnedAdapter(
    private val context: Context?,
    private val launches: ArrayList<LaunchesModel>
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
            launchesCard.transitionName = launch.id

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

                flightNumber.text = context?.getString(R.string.flight_number, it.flightNumber)

                missionName.text = it.missionName
                date.text = it.launchDateUnix.formatDateMillisLong(it.tbd)

                launchesCard.setOnClickListener { _ ->
                    itemView.findNavController().navigate(
                        R.id.action_dashboard_page_fragment_to_launch_details_fragment,
                        bundleOf(
                            "launch_id" to it.id,
                            "flight_number" to it.flightNumber,
                            "title" to it.missionName
                        ),
                        null,
                        FragmentNavigatorExtras(launchesCard to launch.id)
                    )
                }
            }
        }
    }

    override fun getItemCount(): Int = launches.size

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val launchesCard: ConstraintLayout = itemView.findViewById(R.id.launches_card_view)
        val missionPatch: ImageView = itemView.findViewById(R.id.launches_mission_patch_image)
        val flightNumber: TextView = itemView.findViewById(R.id.launches_flight_no_text)
        val missionName: TextView = itemView.findViewById(R.id.launches_mission_name_text)
        val date: TextView = itemView.findViewById(R.id.launches_date_text)
    }
}