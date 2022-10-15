package uk.co.zac_h.spacex.launches.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import uk.co.zac_h.spacex.R
import uk.co.zac_h.spacex.databinding.ListItemMissionBinding
import uk.co.zac_h.spacex.launches.Launch

class MissionsAdapter : ListAdapter<Launch, MissionsAdapter.ViewHolder>(Comparator) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder = ViewHolder(
        ListItemMissionBinding.inflate(LayoutInflater.from(parent.context), parent, false)
    )

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val launch = getItem(position)

        holder.binding.apply {
            root.transitionName = launch.id
            missionName.text = launch.missionName
            missionFlightNumber.text =
                root.resources.getString(R.string.flight_number, launch.flightNumber)

            /*root.setOnClickListener {
                root.findNavController().navigate(
                    NavGraphDirections.missionsToLaunchDetailsContainerFragment(),
                    FragmentNavigatorExtras(root to launch.id.orEmpty())
                )
            }*/
        }
    }

    class ViewHolder(val binding: ListItemMissionBinding) : RecyclerView.ViewHolder(binding.root)

    object Comparator : DiffUtil.ItemCallback<Launch>() {

        override fun areItemsTheSame(oldItem: Launch, newItem: Launch) = oldItem == newItem

        override fun areContentsTheSame(oldItem: Launch, newItem: Launch) = oldItem.id == newItem.id
    }
}
