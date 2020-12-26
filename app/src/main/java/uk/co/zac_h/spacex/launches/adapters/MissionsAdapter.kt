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
import uk.co.zac_h.spacex.model.spacex.Launch

class MissionsAdapter(
    private val context: Context?,
    private val launches: List<Launch>
) :
    RecyclerView.Adapter<MissionsAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder = ViewHolder(
        LayoutInflater.from(parent.context).inflate(
            R.layout.list_item_mission,
            parent,
            false
        )
    )

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val launch = launches[position]

        holder.apply {
            itemView.transitionName = launch.id
            missionName.text = launch.missionName
            flightNumber.text = context?.getString(R.string.flight_number, launch.flightNumber)

            itemView.setOnClickListener {
                itemView.findNavController()
                    .navigate(
                        R.id.missions_to_launch_details_container_fragment,
                        bundleOf("launch_id" to launch.id),
                        null,
                        FragmentNavigatorExtras(itemView to launch.id)
                    )
            }
        }
    }

    override fun getItemCount(): Int = launches.size

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val missionName: TextView = itemView.findViewById(R.id.list_item_mission_name_text)
        val flightNumber: TextView = itemView.findViewById(R.id.list_item_mission_flight_text)
    }
}