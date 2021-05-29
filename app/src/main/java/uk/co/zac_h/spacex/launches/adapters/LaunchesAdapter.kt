package uk.co.zac_h.spacex.launches.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import androidx.core.content.ContextCompat
import androidx.core.os.bundleOf
import androidx.navigation.findNavController
import androidx.navigation.fragment.FragmentNavigatorExtras
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import uk.co.zac_h.spacex.R
import uk.co.zac_h.spacex.databinding.ListItemLaunchesBinding
import uk.co.zac_h.spacex.model.spacex.Launch
import uk.co.zac_h.spacex.utils.LAUNCH_SHORT_KEY
import uk.co.zac_h.spacex.utils.formatDateMillisLong
import uk.co.zac_h.spacex.utils.formatDateMillisYYYY
import java.util.*
import kotlin.collections.ArrayList

class LaunchesAdapter(
    private val context: Context,
    private val launches: ArrayList<Launch>
) : RecyclerView.Adapter<LaunchesAdapter.ViewHolder>(), Filterable {

    private var filteredLaunches: ArrayList<Launch>

    init {
        filteredLaunches = launches
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder = ViewHolder(
        ListItemLaunchesBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
    )

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val launch = filteredLaunches[position]

        holder.binding.apply {
            root.transitionName = launch.id

            Glide.with(root)
                .load(launch.links?.missionPatch?.patchSmall)
                .error(ContextCompat.getDrawable(context, R.drawable.ic_mission_patch))
                .fallback(ContextCompat.getDrawable(context, R.drawable.ic_mission_patch))
                .placeholder(ContextCompat.getDrawable(context, R.drawable.ic_mission_patch))
                .into(missionPatch)

            if (launch.rocket?.name == "Falcon 9") {
                reused.visibility = launch.cores?.get(0)?.reused?.let {
                    if (it) View.VISIBLE else View.GONE
                } ?: View.GONE

                landingVehicle.visibility =
                    launch.cores?.get(0)?.landingSuccess?.let {
                        if (it) View.VISIBLE else View.GONE
                    } ?: View.GONE

                landingVehicle.text = launch.cores?.get(0)?.landingPad?.name
            } else {
                reused.visibility = View.GONE
                landingVehicle.visibility = View.GONE
            }

            flightNumber.text = context.getString(R.string.flight_number, launch.flightNumber)
            vehicle.text = launch.rocket?.name
            missionName.text = launch.missionName
            date.text =
                launch.launchDate?.dateUnix?.formatDateMillisLong(launch.datePrecision)

            root.setOnClickListener {
                root.findNavController().navigate(
                    R.id.action_launch_item_to_launch_details_container_fragment,
                    bundleOf(LAUNCH_SHORT_KEY to launch),
                    null,
                    FragmentNavigatorExtras(root to launch.id)
                )
            }
        }
    }

    override fun getItemCount(): Int = filteredLaunches.size

    override fun getFilter(): Filter {
        return object : Filter() {
            override fun performFiltering(s: CharSequence?): FilterResults {
                val filterResults = FilterResults()
                s?.let {
                    filteredLaunches = when {
                        it.isEmpty() -> launches
                        else -> {
                            val filteredList = ArrayList<Launch>()
                            launches.forEach { launch ->
                                if (launch.missionName?.lowercase().toString()
                                        .contains(
                                            it.toString().lowercase()
                                        ) || launch.launchDate?.dateUnix?.formatDateMillisYYYY()
                                        .toString().contains(
                                            it
                                        ) || launch.flightNumber.toString().contains(
                                        it
                                    )
                                ) {
                                    filteredList.add(launch)
                                    return@forEach
                                }

                                launch.rocket?.name?.let { rocketName ->
                                    if (rocketName.lowercase().contains(s)) {
                                        filteredList.add(launch)
                                    }
                                }
                            }

                            filteredList
                        }
                    }

                    filterResults.values = filteredLaunches
                    filterResults.count = filteredLaunches.size
                }
                return filterResults
            }

            override fun publishResults(
                charSequence: CharSequence?,
                filterResults: FilterResults?
            ) {
                notifyDataSetChanged()
            }
        }
    }

    class ViewHolder(val binding: ListItemLaunchesBinding) : RecyclerView.ViewHolder(binding.root)
}