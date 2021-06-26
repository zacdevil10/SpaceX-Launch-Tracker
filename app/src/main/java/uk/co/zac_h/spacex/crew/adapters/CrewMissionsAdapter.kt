package uk.co.zac_h.spacex.crew.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import uk.co.zac_h.spacex.R
import uk.co.zac_h.spacex.databinding.ListItemCrewMissionBinding
import uk.co.zac_h.spacex.model.spacex.Launch

class CrewMissionsAdapter(
    private val context: Context,
    private val launches: List<Launch>
) : RecyclerView.Adapter<CrewMissionsAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder =
        ViewHolder(
            ListItemCrewMissionBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val launch = launches[position]

        holder.binding.apply {
            listItemCrewMissionNameText.text = launch.missionName
            listItemCrewMissionFlightText.text =
                context.getString(R.string.flight_number, launch.flightNumber)
        }
    }

    override fun getItemCount(): Int = launches.size

    class ViewHolder(val binding: ListItemCrewMissionBinding) : RecyclerView.ViewHolder(binding.root)
}