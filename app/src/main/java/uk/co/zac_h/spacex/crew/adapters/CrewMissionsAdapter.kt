package uk.co.zac_h.spacex.crew.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import uk.co.zac_h.spacex.R
import uk.co.zac_h.spacex.databinding.ListItemCrewMissionBinding
import uk.co.zac_h.spacex.launches.Launch

class CrewMissionsAdapter(private val context: Context) :
    ListAdapter<Launch, CrewMissionsAdapter.ViewHolder>(CrewMissionComparator) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder =
        ViewHolder(
            ListItemCrewMissionBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val launch = getItem(position)

        holder.binding.apply {
            listItemCrewMissionNameText.text = launch.missionName
            listItemCrewMissionFlightText.text =
                context.getString(R.string.flight_number, launch.flightNumber)
        }
    }

    class ViewHolder(val binding: ListItemCrewMissionBinding) :
        RecyclerView.ViewHolder(binding.root)

    object CrewMissionComparator : DiffUtil.ItemCallback<Launch>() {

        override fun areItemsTheSame(oldItem: Launch, newItem: Launch) = oldItem == newItem

        override fun areContentsTheSame(oldItem: Launch, newItem: Launch) =
            oldItem.id == newItem.id
                    && oldItem.missionName == newItem.missionName
                    && oldItem.flightNumber == newItem.flightNumber
    }

}