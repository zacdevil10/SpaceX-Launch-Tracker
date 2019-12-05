package uk.co.zac_h.spacex.launches.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import android.widget.ImageView
import android.widget.TextView
import androidx.core.os.bundleOf
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso
import uk.co.zac_h.spacex.R
import uk.co.zac_h.spacex.model.spacex.LaunchesModel
import uk.co.zac_h.spacex.utils.formatBlockNumber
import uk.co.zac_h.spacex.utils.formatDateMillisLong
import java.util.*
import kotlin.collections.ArrayList

class LaunchesAdapter(
    private val context: Context?,
    private val launches: ArrayList<LaunchesModel>
) : RecyclerView.Adapter<LaunchesAdapter.ViewHolder>(), Filterable {

    private var filteredLaunches: ArrayList<LaunchesModel>

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
            flightNumber.text = context?.getString(R.string.flight_number, launch.flightNumber)

            missionPatch.visibility =
                launch.links.missionPatchSmall?.let { View.VISIBLE } ?: View.GONE

            Picasso.get().load(launch.links.missionPatchSmall)
                .into(missionPatch)

            if (launch.rocket.id == "falcon9") {
                reusedTag.visibility = launch.rocket.firstStage?.cores?.get(0)?.reused?.let {
                    if (it) View.VISIBLE else View.GONE
                } ?: View.GONE

                landingVehicleTag.visibility =
                    launch.rocket.firstStage?.cores?.get(0)?.landingSuccess?.let {
                        if (it) View.VISIBLE else View.GONE
                    } ?: View.GONE

                landingVehicleTag.text = launch.rocket.firstStage?.cores?.get(0)?.landingVehicle
            } else {
                reusedTag.visibility = View.GONE
                landingVehicleTag.visibility = View.GONE
            }

            blockNumber.text = context?.getString(
                R.string.vehicle_block_type,
                launch.rocket.name,
                launch.rocket.firstStage?.cores?.formatBlockNumber()
            )
            missionName.text = launch.missionName
            date.text = launch.launchDateUnix.formatDateMillisLong(launch.tbd?.let { it } ?: false)

            itemView.setOnClickListener {
                itemView.findNavController()
                    .navigate(
                        R.id.action_launches_page_fragment_to_launch_details_fragment,
                        bundleOf("launch" to launch, "title" to launch.missionName)
                    )
            }
        }
    }

    override fun getItemCount(): Int = filteredLaunches.size

    override fun getFilter(): Filter {
        return object : Filter() {
            override fun performFiltering(search: CharSequence?): FilterResults {
                val filterResults = FilterResults()
                search?.let {
                    filteredLaunches = when {
                        it.isEmpty() -> launches
                        else -> {
                            val filteredList = ArrayList<LaunchesModel>()
                            launches.forEach { launch ->
                                if (launch.missionName.toLowerCase(Locale.getDefault()).contains(
                                        it.toString().toLowerCase(
                                            Locale.getDefault()
                                        )
                                    ) || launch.launchYear.toString().contains(it) || launch.flightNumber.toString().contains(
                                        it
                                    )
                                ) {
                                    filteredList.add(launch)
                                    return@forEach
                                }

                                launch.rocket.name?.let { rocketName ->
                                    if (rocketName.toLowerCase(Locale.getDefault()).contains(search)) filteredList.add(
                                        launch
                                    )
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
        val blockNumber: TextView = itemView.findViewById(R.id.launches_block_text)
        val missionPatch: ImageView = itemView.findViewById(R.id.launches_mission_patch_image)
        val missionName: TextView = itemView.findViewById(R.id.launches_mission_name_text)
        val date: TextView = itemView.findViewById(R.id.launches_date_text)
        val reusedTag: TextView = itemView.findViewById(R.id.launches_reused_text)
        val landingVehicleTag: TextView = itemView.findViewById(R.id.launches_landing_vehicle_text)
    }
}