package uk.co.zac_h.spacex.dashboard.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.core.os.bundleOf
import androidx.navigation.findNavController
import androidx.navigation.fragment.FragmentNavigatorExtras
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import uk.co.zac_h.spacex.R
import uk.co.zac_h.spacex.databinding.ListItemLaunchesBinding
import uk.co.zac_h.spacex.model.spacex.Launch
import uk.co.zac_h.spacex.utils.formatDateMillisLong

class DashboardPinnedAdapter(
    private val context: Context?,
    private val launches: ArrayList<Launch>
) : RecyclerView.Adapter<DashboardPinnedAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder =
        ViewHolder(
            ListItemLaunchesBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        )

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val launch = launches[position]

        holder.binding.apply {
            root.transitionName = launch.id

            launch.let {
                Glide.with(root)
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
                    .into(launchesMissionPatchImage)

                if (launch.rocket?.name == "Falcon 9") {
                    launchesReusedText.visibility = launch.cores?.get(0)?.reused?.let { reused ->
                        if (reused) View.VISIBLE else View.GONE
                    } ?: View.GONE

                    launchesLandingVehicleText.visibility =
                        launch.cores?.get(0)?.landingSuccess?.let { landingSuccess ->
                            if (landingSuccess) View.VISIBLE else View.GONE
                        } ?: View.GONE

                    launchesLandingVehicleText.text = launch.cores?.get(0)?.landingPad?.name
                } else {
                    launchesReusedText.visibility = View.GONE
                    launchesLandingVehicleText.visibility = View.GONE
                }

                launchesFlightNoText.text =
                    context?.getString(R.string.flight_number, it.flightNumber)
                launchesVehicleText.text = it.rocket?.name
                launchesMissionNameText.text = it.missionName
                launchesDateText.text =
                    it.launchDate?.dateUnix?.formatDateMillisLong(it.datePrecision)

                root.setOnClickListener { _ ->
                    root.findNavController().navigate(
                        R.id.action_dashboard_page_fragment_to_launch_details_container_fragment,
                        bundleOf(
                            "launch_short" to it
                        ),
                        null,
                        FragmentNavigatorExtras(root to launch.id)
                    )
                }
            }
        }
    }

    override fun getItemCount(): Int = launches.size

    class ViewHolder(val binding: ListItemLaunchesBinding) : RecyclerView.ViewHolder(binding.root)
}