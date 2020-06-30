package uk.co.zac_h.spacex.launches.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.os.bundleOf
import androidx.navigation.findNavController
import androidx.navigation.fragment.FragmentNavigatorExtras
import androidx.recyclerview.widget.RecyclerView
import uk.co.zac_h.spacex.R
import uk.co.zac_h.spacex.model.spacex.CoreLaunchesModel

class CoreMissionsAdapter(
    private val context: Context?,
    private val missions: List<CoreLaunchesModel>
) :
    RecyclerView.Adapter<CoreMissionsAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder = ViewHolder(
        LayoutInflater.from(parent.context).inflate(
            R.layout.list_item_core_mission,
            parent,
            false
        )
    )

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val mission = missions[position]

        holder.apply {
            itemView.transitionName = mission.id

            missionName.text = mission.name
            flightNumber.text = context?.getString(R.string.flight_number, mission.flightNumber)


            itemView.setOnClickListener {
                itemView.findNavController()
                    .navigate(
                        R.id.action_core_details_fragment_to_launch_details_container_fragment,
                        bundleOf(
                            "launch_id" to mission.id
                        ),
                        null,
                        FragmentNavigatorExtras(itemView to mission.id)
                    )
            }
        }
    }

    override fun getItemCount(): Int = missions.size

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val missionName: TextView = itemView.findViewById(R.id.list_item_core_mission_name_text)
        val flightNumber: TextView = itemView.findViewById(R.id.list_item_core_mission_flight_text)
    }
}