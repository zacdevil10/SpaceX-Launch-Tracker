package uk.co.zac_h.spacex.launches.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.os.bundleOf
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import uk.co.zac_h.spacex.R
import uk.co.zac_h.spacex.utils.data.LaunchesModel
import uk.co.zac_h.spacex.utils.format

class LaunchesAdapter(
    private val context: Context?,
    private val launches: ArrayList<LaunchesModel>
) : RecyclerView.Adapter<LaunchesAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder =
        ViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.list_item_launches, parent, false))

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val launch = launches[position]

        var blockText = ""

        holder.apply {
            flightNumber.text = context?.getString(R.string.flight_number, launch.flightNumber)

            launch.rocket.firstStage?.cores?.forEach { i ->
                if (i.block == null) {
                    blockText = "TBD "
                    return@forEach
                }
                blockText += "${i.block} "
            }

            val landed = launch.rocket.firstStage?.cores?.get(0)?.landingSuccess

            if (launch.rocket.id == "falcon9") {
                reusedTag.visibility =
                    if (launch.rocket.firstStage?.cores?.get(0)?.reused != null && launch.rocket.firstStage?.cores?.get(
                            0
                        )?.reused!!
                    )
                        View.VISIBLE else View.INVISIBLE
                landingVehicleTag.visibility = if (landed != null && landed) View.VISIBLE else View.INVISIBLE
                landingVehicleTag.text = launch.rocket.firstStage?.cores?.get(0)?.landingVehicle
            } else {
                reusedTag.visibility = View.INVISIBLE
                landingVehicleTag.visibility = View.INVISIBLE
            }

            blockNumber.text = context?.getString(
                R.string.vehicle_block_type,
                launch.rocket.name,
                blockText.dropLast(1).replace(" ", " | ")
            )
            missionName.text = launch.missionName
            date.text = launch.launchDateUnix.format()

            itemView.setOnClickListener {
                itemView.findNavController()
                    .navigate(R.id.launch_details_fragment, bundleOf("launch" to launch, "title" to launch.missionName))
            }
        }
    }

    override fun getItemCount(): Int = launches.size

    class ViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
        val flightNumber: TextView = itemView.findViewById(R.id.launches_flight_no_text)
        val blockNumber: TextView = itemView.findViewById(R.id.launches_block_text)
        val missionName: TextView = itemView.findViewById(R.id.launches_mission_name_text)
        val date: TextView = itemView.findViewById(R.id.launches_date_text)

        val reusedTag: TextView = itemView.findViewById(R.id.launches_reused_text)
        val landingVehicleTag: TextView = itemView.findViewById(R.id.launches_landing_vehicle_text)
    }
}