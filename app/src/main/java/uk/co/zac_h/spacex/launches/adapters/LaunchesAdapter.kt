package uk.co.zac_h.spacex.launches.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
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
import uk.co.zac_h.spacex.utils.formatDateMillisYYYY
import java.util.*
import kotlin.collections.ArrayList

class LaunchesAdapter(
    private val context: Context?,
    private val launches: ArrayList<LaunchesExtendedModel>
) : RecyclerView.Adapter<LaunchesAdapter.ViewHolder>(), Filterable {

    private var filteredLaunches: ArrayList<LaunchesExtendedModel>

    init {
        filteredLaunches = launches
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder =
        ViewHolder(
            LayoutInflater.from(parent.context).inflate(
                R.layout.list_item_launches,
                parent,
                false
            )
        )

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val launch = filteredLaunches[position]

        holder.apply {
            itemView.transitionName = launch.id

            flightNumber.text = context?.getString(R.string.flight_number, launch.flightNumber)

            Glide.with(itemView)
                .load(launch.links?.missionPatch?.patchSmall)
                .error(context?.let { ContextCompat.getDrawable(it, R.drawable.ic_mission_patch) })
                .fallback(context?.let {
                    ContextCompat.getDrawable(it, R.drawable.ic_mission_patch)
                })
                .placeholder(context?.let {
                    ContextCompat.getDrawable(it, R.drawable.ic_mission_patch)
                })
                .into(missionPatch)

            if (launch.rocket?.name == "Falcon 9") {
                reusedTag.visibility = launch.cores?.get(0)?.reused?.let {
                    if (it) View.VISIBLE else View.GONE
                } ?: View.GONE

                landingVehicleTag.visibility =
                    launch.cores?.get(0)?.landingSuccess?.let {
                        if (it) View.VISIBLE else View.GONE
                    } ?: View.GONE

                landingVehicleTag.text = launch.cores?.get(0)?.landingPad?.name
            } else {
                reusedTag.visibility = View.GONE
                landingVehicleTag.visibility = View.GONE
            }

            vehicle.text = launch.rocket?.name

            missionName.text = launch.missionName
            date.text = launch.tbd?.let { launch.launchDateUnix?.formatDateMillisLong(it) }

            itemView.setOnClickListener {
                itemView.findNavController()
                    .navigate(
                        R.id.action_launches_page_fragment_to_launch_details_fragment,
                        bundleOf(
                            "launch_short" to launch,
                            "launch_id" to launch.id,
                            "flight_number" to launch.flightNumber,
                            "title" to launch.missionName
                        ),
                        null,
                        FragmentNavigatorExtras(itemView to launch.id)
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
                            val filteredList = ArrayList<LaunchesExtendedModel>()
                            launches.forEach { launch ->
                                if (launch.missionName?.toLowerCase(Locale.getDefault()).toString()
                                        .contains(
                                            it.toString().toLowerCase(
                                                Locale.getDefault()
                                            )
                                        ) || launch.launchDateUnix?.formatDateMillisYYYY()
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
                                    if (rocketName.toLowerCase(Locale.getDefault()).contains(s)) {
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

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val flightNumber: TextView = itemView.findViewById(R.id.launches_flight_no_text)
        val vehicle: TextView = itemView.findViewById(R.id.launches_vehicle_text)
        val missionPatch: ImageView = itemView.findViewById(R.id.launches_mission_patch_image)
        val missionName: TextView = itemView.findViewById(R.id.launches_mission_name_text)
        val date: TextView = itemView.findViewById(R.id.launches_date_text)
        val reusedTag: TextView = itemView.findViewById(R.id.launches_reused_text)
        val landingVehicleTag: TextView = itemView.findViewById(R.id.launches_landing_vehicle_text)
    }
}